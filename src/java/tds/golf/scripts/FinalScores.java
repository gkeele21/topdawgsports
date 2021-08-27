/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.golf.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.AuUtil;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import sun.jdbc.rowset.CachedRowSet;
import tds.constants.Team;
import tds.main.bo.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class FinalScores implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public FinalScores() {
        _Logger = Logger.global;
        _Logger.setLevel(Level.ALL);
    }

    @Override
    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    @Override
    public ResultCode getResultCode() { return _ResultCode; }

    @Override
    public void setScriptArgs(String[] args) { _Args = args; }

    public void run(int pgaTournamentId, int fsSeasonWeekId, String exp, String hmac) {
        try {

            _Logger.info("Starting to retrieve Final Scores...");
            importFinalScoresJsonFromPGATour(pgaTournamentId, fsSeasonWeekId, exp, hmac);
//            importFinalScoresJsonFromFoxSports(pgaTournamentId, fsSeasonWeekId);
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in FinalEarnings.run()", e);
        }
    }

    public void importFinalScores(int pgaTournamentId, int fsSeasonWeekId) throws Exception {

        StringBuilder sb = new StringBuilder();
        Connection con = null;
        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);

            // get Tournament external id
            PGATournamentWeek tournamentWeek = new PGATournamentWeek(pgaTournamentId, fsSeasonWeekId);

            String extId = tournamentWeek.getPGATournament().getExternalTournamentID();

            if (AuUtil.isEmpty(extId))
            {
                _Logger.log(Level.SEVERE, "Error: ExternalId is not set for this tournament.");
                return;
            }

            final String filePath = "http://www.pgatour.com/data/r/" + extId + "/2021/leaderboard.xml";

            // www.pgatour.com/data/r/475/2015/money.xml
            // scores : leaderboard.xml
            // field : field.xml

            System.out.println("Reading from URL : " + filePath);
            URL url = new URL(filePath);

            InputStream uin = url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(uin));
            StringBuilder file = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                file.append(line);
            }

            SAXBuilder jdomBuilder = new SAXBuilder();

            Document jdomDocument = jdomBuilder.build(new StringReader(file.toString()));

//            System.out.println("Root Element: " + jdomDocument.getRootElement().getName());
            Element tournament = jdomDocument.getRootElement();

            Element lb = tournament.getChild("lb");

//            System.out.println(tournament.getNamespacesIntroduced());

            List<Element> lbChildren = lb.getChildren();

            int numRows = lbChildren.size();

            for (int i = 0; i < numRows; i++) {
                Element playerElement = lbChildren.get(i);

                String statsPlayerId = playerElement.getAttributeValue("id");

                if (statsPlayerId == null)
                {
                    continue;
                }

                System.out.print("   StatsId  : " + statsPlayerId);

                // retrieve PGATournamentWeekPlayer record for this week

                int playerId = 0;
                StringBuilder query = new StringBuilder();
                query.append("select * from Player where StatsPlayerId = '").append(statsPlayerId).append("'").append(" AND TeamID = ").append(Team.PGATOUR);
                CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery(con,query.toString());
//                    _Logger.info(query.toString());
                if (crs3.next()) {
                    playerId = crs3.getInt("PlayerID");
                }

                if (playerId > 0)
                {

                    PGATournamentWeekPlayer weekPlayer = new PGATournamentWeekPlayer(pgaTournamentId, fsSeasonWeekId, playerId);

                    if (weekPlayer != null && weekPlayer.getID() > 0) {

                        Element posElement = playerElement.getChild("pos");
                        Element moneyElement = playerElement.getChild("pMoney");

                        String status = playerElement.getAttributeValue("status");
                        String rank = posElement.getAttributeValue("cPos");
                        boolean active = true;
                        if ("cut".equals(status))
                        {
                            rank = "CUT";
                            active = false;
                        }

                        String tied = posElement.getAttributeValue("cPosTied");
                        if ("y".equals(tied))
                        {
                            rank += "T";
                        }

                        if (!AuUtil.isEmpty(rank)) {
                            weekPlayer.setTournamentRank(rank);
                        }

                        if (active)
                        {
                            String relStr = posElement.getAttributeValue("pTot");
                            if (!AuUtil.isEmpty(relStr)) {
                                weekPlayer.setRelativeToPar(Integer.parseInt(relStr));
                            }
                        }

                        String sortStr = posElement.getAttributeValue("sortTot");
                        if (!AuUtil.isEmpty(sortStr)) {
                            weekPlayer.setSortOrder(Integer.parseInt(sortStr));
                        }

                        String moneyStr = moneyElement.getAttributeValue("event");
                        if (!AuUtil.isEmpty(moneyStr)) {
                           weekPlayer.setMoneyEarned(Double.parseDouble(moneyStr));
                        }

                        String scStr = posElement.getAttributeValue("sc");
                        if (!AuUtil.isEmpty(scStr)) {
                            weekPlayer.setFinalScore(scStr);
                        }

                        String thruStr = posElement.getAttributeValue("thru");
                        if (!AuUtil.isEmpty(thruStr)) {
                            weekPlayer.setThru(Integer.parseInt(thruStr));
                        }

                        String todayStr = posElement.getAttributeValue("pDay");
                        if (!AuUtil.isEmpty(todayStr)) {
                            weekPlayer.setTodayRound(Integer.parseInt(todayStr));
                        }

                        // get round scores
                        Element roundsElement = playerElement.getChild("rnds");
                        List<Element> roundChildren = roundsElement.getChildren();

                        int roundsSize = roundChildren.size();

                        for (int r = 0; r < roundsSize; r++) {
                            Element round = roundChildren.get(r);

                            String roundNumberStr = round.getAttributeValue("n");
                            String roundScore = round.getAttributeValue("sc");
                            if (AuUtil.isEmpty(roundScore))
                            {
                                continue;
                            }

                            int roundNumber = Integer.parseInt(roundNumberStr);

                            switch (roundNumber) {
                                case 1 :
                                    weekPlayer.setRound1(roundScore);
                                    break;
                                case 2 :
                                    weekPlayer.setRound2(roundScore);
                                    break;
                                case 3 :
                                    weekPlayer.setRound3(roundScore);
                                    break;
                                case 4 :
                                    weekPlayer.setRound4(roundScore);
                                    break;
                                case 5 :
                                    weekPlayer.setRound5(roundScore);
                                    break;
                            }
                        }

                        weekPlayer.Save();
                        System.out.println(" : SUCCESS!");
                    } else {
                        System.out.println(" : SKIPPED - player not in field.");
                    }

                } else
                {
                    System.out.println(" : SKIPPED - player does not exist.");
                }
            }

        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "FinalEarnings Creation Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {

        }

    }

    public void importFinalScoresJsonFromPGATour(int pgaTournamentId, int fsSeasonWeekId, String exp, String hmac) throws Exception {

        Connection con;
        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);

            // get Tournament external id
            PGATournamentWeek tournamentWeek = new PGATournamentWeek(pgaTournamentId, fsSeasonWeekId);

            String extId = tournamentWeek.getPGATournament().getExternalTournamentID();

            if (AuUtil.isEmpty(extId))
            {
                _Logger.log(Level.SEVERE, "Error: ExternalId is not set for this tournament.");
                return;
            }

//            final String filePath = "https://statdata.pgatour.com/r/" + extId + "/2020/leaderboard-v2mini.json";
            String extraParams = "?userTrackingId=exp=" + exp + "~acl=*~hmac=" + hmac;
            final String filePath = "https://lbdata.pgatour.com/2021/r/" + extId + "/leaderboard.json" + extraParams;

            // www.pgatour.com/data/r/475/2015/money.xml
            // scores : leaderboard.xml
            // field : field.xml

//            https://api.foxsports.com/bifrost/v1/golf/event/3560/data?apikey=jE7yBJVRNAwdDesMgTzTXUUSx1It41Fq
            // https://lbdata.pgatour.com/2020/r/013/leaderboard.json?userTrackingId=exp=1597378321~acl=*~hmac=e8774e501940d24eed76bf5298eea5eaf1579d53d8227cc7072004459b750529
            // https://lbdata.pgatour.com/2020/r/013/leaderboard.json?userTrackingId=exp=1597546483~acl=*~hmac=63ef2ccafca143af37cb3a33cd41d842c43e58b103ea9293e3a1a6cfca15fd63
//            final String filePath = "https://lbdata.pgatour.com/2020/r/013/leaderboard.json?userTrackingId=exp=1597378321~acl=*~hmac=e8774e501940d24eed76bf5298eea5eaf1579d53d8227cc7072004459b750529";
            System.out.println("Reading from URL : " + filePath);
            URL url = new URL(filePath);

            InputStream uin = url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(uin));
            StringBuilder file = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                file.append(line);
            }

            JSONObject objs = (JSONObject) JSONValue.parse(file.toString());

//            JSONObject leaderboardObj = (JSONObject)objs.get("leaderboard");
//            JSONArray playersArr = (JSONArray)leaderboardObj.get("rows");
            JSONArray playersArr = (JSONArray)objs.get("rows");

            playerLoop: for (Object p : playersArr)
            {
                JSONObject player = (JSONObject) p;
                String statsPlayerId = player.get("playerId").toString();

                if (statsPlayerId == null)
                {
                    continue;
                }

                System.out.print("   StatsId  : " + statsPlayerId);

                // retrieve PGATournamentWeekPlayer record for this week

                int playerId = 0;
                StringBuilder query = new StringBuilder();
                query.append("select * from Player where StatsPlayerId = '").append(statsPlayerId).append("'").append(" AND TeamID = ").append(Team.PGATOUR);
                CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery(con,query.toString());
//                    _Logger.info(query.toString());
                if (crs3.next()) {
                    playerId = crs3.getInt("PlayerID");
                }

                if (playerId > 0)
                {

                    PGATournamentWeekPlayer weekPlayer = new PGATournamentWeekPlayer(pgaTournamentId, fsSeasonWeekId, playerId);

                    if (weekPlayer != null && weekPlayer.getID() > 0) {

                        try {

                            String status = player.get("status").toString();
                            String rank = player.get("positionCurrent").toString();
                            boolean active = true;
                            if ("cut".equals(status))
                            {
                                rank = "CUT";
                                active = false;
                            }

                            if (!AuUtil.isEmpty(rank)) {
                                weekPlayer.setTournamentRank(rank);
                            }

                            if (active)
                            {
                                String relStr = player.get("total").toString();
                                if (!AuUtil.isEmpty(relStr)) {
                                    weekPlayer.setRelativeToPar(Integer.parseInt(relStr));
                                }
                            }

                            Object tsObj = player.get("strokes");
                            if (tsObj != null) {
                                String scStr = tsObj.toString();
                                if (!AuUtil.isEmpty(scStr)) {
                                    weekPlayer.setFinalScore(scStr);
                                }
                            }

                            Object thruObj = player.get("thru");
                            if (thruObj != null) {
                                String thruStr = thruObj.toString().replace("*","");
                                if ("F".equals(thruStr)) {
                                    weekPlayer.setThru(18);
                                } else if ("--".equals(thruStr)) {
                                    weekPlayer.setThru(0);
                                } else {
                                    weekPlayer.setThru(Integer.parseInt(thruStr));
                                }
                            }

                            Object todayObj = player.get("round");
                            if (todayObj != null) {
                                String todayStr = todayObj.toString();
                                if ("E".equals(todayStr) || "--".equals(todayStr)) {
                                    weekPlayer.setTodayRound(0);
                                } else {
                                    weekPlayer.setTodayRound(Integer.parseInt(todayStr));
                                }
                            }

                            // get round scores
                            JSONArray roundsArr = (JSONArray)player.get("rounds");
                            int roundNumber = 0;
                            for (Object r : roundsArr) {
                                roundNumber++;
                                JSONObject round = (JSONObject) r;
                                Object roundScoreObj = round.get("strokes");
                                if (roundScoreObj == null)
                                {
                                    continue;
                                }
                                String roundScore = roundScoreObj.toString();
                                if ("--".equals(roundScore)) {
                                    roundScore = null;
                                }

                                switch (roundNumber) {
                                    case 1 :
                                        weekPlayer.setRound1(roundScore);
                                        break;
                                    case 2 :
                                        weekPlayer.setRound2(roundScore);
                                        break;
                                    case 3 :
                                        weekPlayer.setRound3(roundScore);
                                        break;
                                    case 4 :
                                        weekPlayer.setRound4(roundScore);
                                        break;
                                    case 5 :
                                        weekPlayer.setRound5(roundScore);
                                        break;
                                }
                            }

                            // Get Money Earnings
//                            JSONObject rankingsObj = (JSONObject)player.get("rankings");
//                            String moneyEarned = rankingsObj.get("projected_money_event").toString();
//                            if (!AuUtil.isEmpty(moneyEarned)) {
//                            weekPlayer.setMoneyEarned(Double.parseDouble(moneyEarned));
//                            }

                            weekPlayer.Save();
                            System.out.println(" : SUCCESS!");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(" : ERROR!");
                        }
                    } else {
                        System.out.println(" : SKIPPED - player not in field.");
                    }

                } else
                {
                    System.out.println(" : SKIPPED - player does not exist.");
                }
            }
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "FinalEarnings Creation Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {

        }

    }

    public void importFinalScoresJsonFromFoxSports(int pgaTournamentId, int fsSeasonWeekId) throws Exception {

        StringBuilder sb = new StringBuilder();
        Connection con = null;
        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);

            // get Tournament external id
            PGATournamentWeek tournamentWeek = new PGATournamentWeek(pgaTournamentId, fsSeasonWeekId);

            String extId = tournamentWeek.getPGATournament().getExternalTournamentIDFoxSports();

            if (AuUtil.isEmpty(extId))
            {
                _Logger.log(Level.SEVERE, "Error: ExternalId is not set for this tournament.");
                return;
            }

            final String filePath = "https://api.foxsports.com/bifrost/v1/golf/event/" + extId + "/data?apikey=jE7yBJVRNAwdDesMgTzTXUUSx1It41Fq";
//            final String filePath = "https://api.foxsports.com/bifrost/v1/golf/event/" + extId + "/data";
            System.out.println("Reading from URL : " + filePath);
            URL url = new URL(filePath);


            HttpsURLConnection con2 = (HttpsURLConnection) url.openConnection();
            // optional default is GET
            con2.setRequestMethod("GET");
            //add request header
            con2.setRequestProperty("User-Agent", "PostmanRuntime/7.26.3");
            con2.setRequestProperty("Accept", "application/json");
//            con2.setRequestProperty("Connection", "keep-alive");
//            con2.setRequestProperty("Postman-Token", "14e945f5-39ad-4842-8beb-db19b2bdcf9e");
//            con2.setRequestProperty("apikey", "jE7yBJVRNAwdDesMgTzTXUUSx1It41Fq");
//            con2.setDoOutput(true);
//            con2.setUseCaches(false);
//            con2.setInstanceFollowRedirects(true);
            int responseCode = con2.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con2.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print in String
//            System.out.println(response.toString());

            JSONObject objs = (JSONObject) JSONValue.parse(response.toString());

            JSONObject leaderboardObj = (JSONObject)objs.get("leaderboard");
            JSONArray leaderboardSections = (JSONArray)leaderboardObj.get("leaderboardSections");
            if (leaderboardSections != null) {
                JSONObject leaderboard2 = (JSONObject)leaderboardSections.get(0);
                if (leaderboard2 != null) {
                    JSONArray leaderboard3 = (JSONArray) leaderboard2.get("leaderboard");
                    JSONObject eventStats = (JSONObject) ((JSONObject) leaderboard3.get(0)).get("eventStats");
                    if (eventStats != null) {
                        JSONArray rows = (JSONArray)eventStats.get("rows");

                        playerLoop: for (Object p : rows)
                        {
                            JSONObject player = (JSONObject) p;

                            // get statsPlayer Id
                            JSONObject entityLink = (JSONObject) player.get("entityLink");
                            String contentUri = (String)entityLink.get("contentUri");
                            String[] parts = contentUri.split("/");
                            String statsPlayerId = parts[3];

                            if (statsPlayerId == null)
                            {
                                continue;
                            }
                            System.out.print("   StatsId  : " + statsPlayerId);
                            JSONArray playerColumns = (JSONArray) player.get("columns");

                            // retrieve PGATournamentWeekPlayer record for this week
                            int playerId = 0;
                            StringBuilder query = new StringBuilder();
                            query.append("select * from Player where StatsPlayerId2 = '").append(statsPlayerId).append("'").append(" AND TeamID = ").append(Team.PGATOUR);
                            CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery(con,query.toString());
//                    _Logger.info(query.toString());
                            if (crs3.next()) {
                                playerId = crs3.getInt("PlayerID");
                            }

                            if (playerId > 0)
                            {

                                PGATournamentWeekPlayer weekPlayer = new PGATournamentWeekPlayer(pgaTournamentId, fsSeasonWeekId, playerId);

                                if (weekPlayer != null && weekPlayer.getID() > 0) {

                                    try {

//                                        String status = player.get("status").toString();
                                        String rank = (String) ((JSONObject)playerColumns.get(0)).get("text");
                                        boolean active = true;
//                                        if ("cut".equals(status))
//                                        {
//                                            rank = "CUT";
//                                            active = false;
//                                        }

                                        if (!AuUtil.isEmpty(rank)) {
                                            weekPlayer.setTournamentRank(rank);
                                        }

                                        if (active)
                                        {
                                            String relStr = (String) ((JSONObject)playerColumns.get(2)).get("text");
                                            if (!AuUtil.isEmpty(relStr)) {
                                                weekPlayer.setRelativeToPar(Integer.parseInt(relStr));
                                            }
                                        }

                                        String round1Str = (String) ((JSONObject)playerColumns.get(3)).get("text");
                                        if (!AuUtil.isEmpty(round1Str)) {
                                            weekPlayer.setRound1(round1Str);
                                        }

                                        String round2Str = (String) ((JSONObject)playerColumns.get(4)).get("text");
                                        if (!AuUtil.isEmpty(round2Str)) {
                                            weekPlayer.setRound2(round2Str);
                                        }
                                        String round3Str = (String) ((JSONObject)playerColumns.get(5)).get("text");
                                        if (!AuUtil.isEmpty(round3Str)) {
                                            weekPlayer.setRound3(round3Str);
                                        }
                                        String round4Str = (String) ((JSONObject)playerColumns.get(6)).get("text");
                                        if (!AuUtil.isEmpty(round4Str)) {
                                            weekPlayer.setRound4(round4Str);
                                        }

                                        weekPlayer.Save();
                                        System.out.println(" : SUCCESS!");
                                    } catch (Exception e) {
                                        System.out.println(" : ERROR!");
                                    }
                                } else {
                                    System.out.println(" : SKIPPED - player not in field.");
                                }

                            } else
                            {
                                System.out.println(" : SKIPPED - player does not exist.");
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "FinalEarnings Creation Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {

        }

    }

    public static void main(String[] args) {
        try {
            FinalScores field = new FinalScores();

            FSGame fsGame = new FSGame(12);
            int currentFSSeasonID = fsGame.getCurrentFSSeasonID();
            FSSeason fsseason = new FSSeason(currentFSSeasonID);

            FSSeasonWeek fsSeasonWeek = fsseason.getCurrentFSSeasonWeek();
            int fsSeasonWeekId = fsSeasonWeek.getFSSeasonWeekID();

            PGATournamentWeek tournamentWeek = PGATournamentWeek.getTournamentWeek(fsSeasonWeekId);
            int pgaTournamentId = tournamentWeek.getPGATournamentID();

            String exp = "";
            String hmac = "";
            if (args != null && args.length > 0) {
                try {
                    pgaTournamentId = Integer.parseInt(args[0]);
                    fsSeasonWeekId = Integer.parseInt(args[1]);
                } catch (Exception e) {

                }
            }

            try {
                exp = System.getProperty("exp");
                hmac = System.getProperty("hmac");
            } catch (Exception e) {

            }
            if (fsSeasonWeekId == 0 || pgaTournamentId == 0)
            {
                System.out.println("Error: You must pass in PGATournamentID as the 1st param and FSSeasonWeekId as the 2nd param");
                return;
            }

            System.out.println("Running for PGATournamentId : " + pgaTournamentId);
            System.out.println("FSSeasonWeekID : " + fsSeasonWeekId);
            field.run(pgaTournamentId, fsSeasonWeekId, exp, hmac);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
