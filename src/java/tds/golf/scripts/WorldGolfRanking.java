/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.golf.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import sun.jdbc.rowset.CachedRowSet;
import tds.constants.Team;
import tds.main.bo.*;

/**
 *
 * @author grant.keele
 */
public class WorldGolfRanking implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public WorldGolfRanking() {
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

    public void run(int pgaTournamentId, int fsSeasonWeekId) {
        try {

            _Logger.info("Starting to retrieve World Golf Ranking...");
            importWGR(pgaTournamentId, fsSeasonWeekId);
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in WorldGolfRanking.run()", e);
        }
    }

    public void importWGR(int pgaTournamentId, int fsSeasonWeekId) throws Exception {

        final String filePath = "https://www.pgatour.com/stats/stat.186.html";
        StringBuilder sb = new StringBuilder();
        Connection con = null;
        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);

            URL url = new URL(filePath);
//
//            InputStream uin = url.openStream();
//            BufferedReader in = new BufferedReader(new InputStreamReader(uin));
//            StringBuilder file = new StringBuilder();
//            // this web page uses nbsp, so add a line at the beginning to convert those
//            file.append("<?xml version=\"1.0\"?>\n\r");
//            file.append("<!DOCTYPE some_name [\n\r");
//            file.append("<!ENTITY nbsp \" \">\n\r");
//            file.append("]>\n\r");
//            String line;
            boolean ok = false;
//            while ((line = in.readLine()) != null) {
//                if (line.indexOf("<table class=\"table-styled\" id=\"statsTable\">") >= 0) {
//                    ok = true;
//                } else if (ok && line.indexOf("</table>") >= 0) {
//                    ok = false;
//                }
//
//                if (ok) {
////                    if (line.indexOf("colspan=\"8\"") < 0) {
//                        file.append(line);
////                    }
//                }
//            }
//
//            file.append("</table>");

            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            int responseCode = urlcon.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlcon.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.indexOf("<table class=\"table-styled\" id=\"statsTable\">") >= 0) {
                    ok = true;
                } else if (ok && inputLine.indexOf("</table>") >= 0) {
                    ok = false;
                }

                if (ok) {
//                    if (line.indexOf("colspan=\"8\"") < 0) {
                        response.append(inputLine);
//                    }
                }
            }
            response.append("</table>");
            in.close();

            String responseStr = response.toString();

            SAXBuilder jdomBuilder = new SAXBuilder();

            Document jdomDocument = jdomBuilder.build(new StringReader(responseStr));

//            System.out.println(jdomDocument.getRootElement().getName());
            Element table = jdomDocument.getRootElement();

//            System.out.println(table.getNamespacesIntroduced());

            List<Element> tableChildren = table.getChildren();

            Element tBody = tableChildren.get(1);
            List<Element> tBodyChildren = tBody.getChildren();

            int numRows = tBodyChildren.size();

            for (int i = 0; i < numRows; i++) {
                Element rowChild = tBodyChildren.get(i);

                String fullId = rowChild.getAttributeValue("id");
//                System.out.println("ID : " + fullId);

                if (fullId == null)
                {
                    continue;
                }

                // parse out the Id to get the PlayerId
                String statsPlayerId = fullId.replace("playerStatsRow", "");
                System.out.println("PlayerId : " + statsPlayerId);

                // for each row - get the td children
                List<Element> rowChildren = rowChild.getChildren();

                int numCols = rowChildren.size();
                if (numCols > 0) {
                    // col 0 is this week's ranking
                    Element wgrElement = rowChildren.get(0);

                    String wgr = wgrElement.getTextTrim();
                    System.out.print("   WGR : " + wgr);

                    // update this player's WGR for this week
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
                            weekPlayer.setWorldGolfRanking(Integer.parseInt(wgr));
                            weekPlayer.Save();
                            System.out.println(" : SUCCESS!");
                        } else {
                            System.out.println(" : SKIPPED - not in field.");
                        }

                    } else {
                        Element playerNameElement = rowChildren.get(2);

                        // this has an anchor child that contains the name
                        List<Element> playerChildren = playerNameElement.getChildren();

                        int playerChildrenSize = playerChildren.size();

                        String playerName = "";
                        for (int x = 0; x < playerChildrenSize; x++) {
                            Element pc = playerChildren.get(x);

                            String pcName = pc.getName();
                            if ("a".equals(pcName)) {
                                playerName = pc.getText().trim();
                            }

                        }

                        String country = rowChildren.get(8).getTextTrim();
                        System.out.println(" : ERROR - No Player found: [" + playerName + "] from [" + country + "] with extId of [" + statsPlayerId + "]");
                    }
                }
            }

        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "WorldGolfRanking Update Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {

        }

    }

    public static void main(String[] args) {
        try {
            WorldGolfRanking ranking = new WorldGolfRanking();

            FSGame fsGame = new FSGame(12);
            int currentFSSeasonID = fsGame.getCurrentFSSeasonID();
            FSSeason fsseason = new FSSeason(currentFSSeasonID);

            FSSeasonWeek fsSeasonWeek = fsseason.getCurrentFSSeasonWeek();
            int fsSeasonWeekId = fsSeasonWeek.getFSSeasonWeekID();

            PGATournamentWeek tournamentWeek = PGATournamentWeek.getTournamentWeek(fsSeasonWeekId);
            int pgaTournamentId = tournamentWeek.getPGATournamentID();

            if (args != null && args.length > 0) {
                try {
                    pgaTournamentId = Integer.parseInt(args[0]);
                    fsSeasonWeekId = Integer.parseInt(args[1]);
                } catch (Exception e) {

                }
            }

            if (fsSeasonWeekId == 0 || pgaTournamentId == 0)
            {
                System.out.println("Error: You must pass in PGATournamentID as the 1st param and FSSeasonWeekId as the 2nd param");
                return;
            }
            ranking.run(pgaTournamentId, fsSeasonWeekId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
