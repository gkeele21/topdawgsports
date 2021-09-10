/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.fantasy.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.Application;
import bglib.util.FSUtils;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class FootballPlayersJson implements Harnessable {

    public static final String PLAYERS_FILE_PROP = "tds.Football.playersFile";
    public static String _PLAYERS_FILE = Application._GLOBAL_SETTINGS.getProperty(PLAYERS_FILE_PROP);

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public FootballPlayersJson() {
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

    @Override
    public void run() {
        try {

            _Logger.info("Starting to update player list...");
            importPlayers();
            updateNFLPlayers();
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in FootballPlayers.run()", e);
        }
    }

    public void importPlayers() throws Exception {

        try {
            // clear out the temp table
            StringBuilder clear = new StringBuilder();
            clear.append("delete from TempProPlayer");

            CTApplication._CT_QUICK_DB.executeUpdate(clear.toString());

            System.out.println("Original players file : " + _PLAYERS_FILE);
            if (_PLAYERS_FILE == "") {
                _PLAYERS_FILE = "/usr/lib/python2.7/dist-packages/nflgame/players.json";
            }
            System.out.println("Grabbing players file : " + _PLAYERS_FILE);
            File playersFile = new File(_PLAYERS_FILE);
            String playerData = FileUtils.readFileToString(playersFile);

            JSONObject objs =(JSONObject) JSONValue.parse(playerData);
            Iterator it = objs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                String newPlayerId = (String)pairs.getKey();
                JSONObject playerDataObj = (JSONObject)pairs.getValue();

                String firstName = "";
                String lastName = "";
                String position = "";
                String team = "";
                String status = "";
                int yearsPro = 0;
                int active = 0;

                Iterator playerIt = playerDataObj.entrySet().iterator();
                while (playerIt.hasNext())
                {
                    Map.Entry playerPairs = (Map.Entry)playerIt.next();
                    String fieldName = (String)playerPairs.getKey();
                    String value = String.valueOf(playerPairs.getValue());

                    if ("first_name".equals(fieldName))
                    {
                        firstName = value;
                    } else if ("last_name".equals(fieldName))
                    {
                        lastName = value;
                    } else if ("position".equals(fieldName))
                    {
                        position = value;
                        if ("FB".equals(position))
                        {
                            position = "RB";
                        } else if ("K".equals(position))
                        {
                            position = "PK";
                        }
                    } else if ("status".equals(fieldName))
                    {
                        status = value;
                    } else if ("team".equals(fieldName))
                    {
                        if (value.equals("SD")) {
                            value = "LAC";
                        }
                        if (value.equals("LA")) {
                            value = "LAR";
                        }
                        team = value;
                    } else if ("years_pro".equals(fieldName))
                    {
                        yearsPro = Integer.parseInt(value);
                    }

                    if (lastName.equals("Yeldon"))
                    {
                        System.out.println("Here he is.");
                    }
                    if (!FSUtils.isEmpty(status))
                    {
                        active = 1;
                    }

                }

                if (active == 1 && ("RB".equals(position) || "WR".equals(position) ||
                        "QB".equals(position) || "TE".equals(position) || "PK".equals(position)))
                {
                    // grab the TeamId
                    int teamId = 0;
                    CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery("select * from " + CTApplication.TBL_PREF + "Team where Abbreviation = '" + team + "'");
                    if (crs3.next()) {
                        teamId = crs3.getInt("TeamID");
                    }
                    int positionId = 0;
                    crs3 = CTApplication._CT_QUICK_DB.executeQuery("select * from Position where PositionName = '" + position + "'");
                    if (crs3.next()) {
                        positionId = crs3.getInt("PositionID");
                    }

                    // grab the playerId
                    int playerId = 0;
                    StringBuilder query = new StringBuilder();
                    query.append("select * from Player where NFLGameStatsId = '").append(newPlayerId).append("'");
                    crs3 = CTApplication._CT_QUICK_DB.executeQuery(query.toString());
                    _Logger.info(query.toString());
                    if (crs3.next()) {
                        playerId = crs3.getInt("PlayerID");
                    } else
                    {

                        query = new StringBuilder();
                        query.append(" select * ");
                        query.append(" from Player ");
                        query.append(" where FirstName = '").append(FSUtils.fixupUserInputForDB(firstName)).append("' ");
                        query.append(" and LastName = '").append(FSUtils.fixupUserInputForDB(lastName)).append("' ");
                        query.append(" and PositionID = ").append(positionId);

                        _Logger.info(query.toString());
                        crs3 = CTApplication._CT_QUICK_DB.executeQuery(query.toString());
                        int count = 0;
                        while (crs3.next()) {
                            count++;
                            playerId = crs3.getInt("PlayerID");
                        }

                        if (count > 1)
                        {
                            playerId = -1;
                            _Logger.info("Too many players found with that name and position to grab the correct one");
                        }
                    }

                    StringBuilder sql = new StringBuilder();
                    sql.append("insert into TempProPlayer ");
                    sql.append("(NFLGameStatsId,Position,LastName,FirstName,Active,TeamID,PositionId,YearsPro,PlayerId) ");
                    sql.append(" values ('").append(newPlayerId).append("' ");
                    sql.append(", '").append(position).append("' ");
                    sql.append(", '").append(FSUtils.fixupUserInputForDB(lastName)).append("' ");
                    sql.append(", '").append(FSUtils.fixupUserInputForDB(firstName)).append("' ");
                    sql.append(", ").append(active);
                    sql.append(", ").append(teamId);
                    sql.append(", ").append(positionId);
                    sql.append(", ").append(yearsPro);
                    sql.append(", ").append(playerId).append(" )");

                    _Logger.info(sql.toString());
                    CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

                }

            }
            System.out.println("Players updated.");
        } catch (Exception e) {
            _Logger.log(Level.SEVERE,"FootballPlayers Update Error : " + e.getMessage());
            e.printStackTrace();
        } finally {
        }

    }

    public void updateNFLPlayers() throws Exception {

        try {
            CTApplication._CT_QUICK_DB.executeUpdate("update Player set isActive = 0 where positionID in (1,2,3,4,5,6,7)");

            StringBuilder sql = new StringBuilder();
            sql.append("select * from TempProPlayer order by PlayerId");

            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            while (crs.next()) {
                int playerid = crs.getInt("PlayerId");
                int teamid = crs.getInt("TeamID");
                int posId = crs.getInt("PositionId");
                String first = crs.getString("FirstName");
                String last = crs.getString("LastName");
                String statsId = crs.getString("NFLGameStatsId");
                int yearsPro = crs.getInt("YearsPro");
//                String quickstatsname = crs.getString("FullName");

                StringBuilder tempSql2 = new StringBuilder();
                tempSql2.append("select * from ").append(CTApplication.TBL_PREF).append("Player where PlayerID = ").append(playerid);
                _Logger.info(tempSql2.toString());
                CachedRowSet crs2 = CTApplication._CT_QUICK_DB.executeQuery(tempSql2.toString());
                if (crs2.next() && playerid > 0) {

                    int currentteam = crs2.getInt("TeamID");
                    sql = new StringBuilder();
                    sql.append("update ").append(CTApplication.TBL_PREF).append("Player set IsActive = 1");
                    sql.append(", NFLGameStatsID = '").append(statsId).append("' ");
                    sql.append(", YearsPro = ").append(yearsPro);

                    if (currentteam != teamid) {

                        sql.append(" ,TeamID = ").append(teamid);
                    }

                    sql.append(" where PlayerID = ").append(playerid);
                    CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
                    _Logger.info(sql.toString());
                } else {

                    int active = 1;
                    sql = new StringBuilder();
                    sql.append("insert into ").append(CTApplication.TBL_PREF).append("Player(TeamID,PositionID,FirstName,LastName,NFLGameStatsID,YearsPro,IsActive) ");
                    sql.append("values(").append(teamid).append(", ");
                    sql.append(posId).append(", '");
                    sql.append(FSUtils.fixupUserInputForDB(first)).append("', '");
                    sql.append(FSUtils.fixupUserInputForDB(last));
                    sql.append("', ").append(playerid);
                    sql.append(", ").append(yearsPro);
                    sql.append(",").append(active).append(")");

                    _Logger.info(sql.toString());
                    CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

                }

                crs2.close();
            }

            crs.close();
        } catch (Exception e) {
            e.printStackTrace();
            _Logger.log(Level.SEVERE,"Error in FootballPlayers : " + e.getMessage());
        } finally {
        }
    }

    public static void main(String[] args) {
        try {
            FootballPlayersJson players = new FootballPlayersJson();
            players.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
