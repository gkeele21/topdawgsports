/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.golf.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.AuUtil;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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
public class TournamentField implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public TournamentField() {
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

            _Logger.info("Starting to retrieve Tournament Field...");
            importField(pgaTournamentId, fsSeasonWeekId);
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in TournamentField.run()", e);
        }
    }

    public void importField(int pgaTournamentId, int fsSeasonWeekId) throws Exception {

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
            
            final String filePath = "http://www.pgatour.com/data/r/" + extId + "/2016/field.xml";
            // www.pgatour.com/data/r/475/2015/money.xml
            // scores : leaderboard.xml
            // field : field.json
            
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
            
//            System.out.println(jdomDocument.getRootElement().getName());
            Element tournament = jdomDocument.getRootElement();
            
//            System.out.println(table.getNamespacesIntroduced());
                      
            List<Element> tournamentChildren = tournament.getChildren();
            
            int numRows = tournamentChildren.size();
            
            for (int i = 0; i < numRows; i++) {
                Element playerElement = tournamentChildren.get(i);
                
                String statsPlayerId = playerElement.getAttributeValue("TournamentPlayerId");
//                System.out.println("ID : " + fullId);
                
                if (statsPlayerId == null)
                {
                    continue;
                }
                
                System.out.print("   StatsId  : " + statsPlayerId);
                String isAlternate = playerElement.getAttributeValue("isAlternate");
                if ("Yes".equals(isAlternate))
                {
                    System.out.println(" : SKIPPED - player is alternate.");
                    continue;
                }
                
                // create PGATournamentWeekPlayer record for this week
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

                    if (weekPlayer == null || weekPlayer.getID() < 1) {
                        weekPlayer.setPGATournamentID(pgaTournamentId);
                        weekPlayer.setFSSeasonWeekID(fsSeasonWeekId);
                        weekPlayer.setPlayerID(playerId);
                        weekPlayer.setWorldGolfRanking(1000);
                        
                        weekPlayer.Save();
                        System.out.println(" : SUCCESS!");
                    } else {
                        System.out.println(" : SKIPPED - already in field.");
                    }

                } else
                {
                    String playerName = playerElement.getAttributeValue("PlayerName");
                    
                    System.out.println(" : SKIPPED - player does not exist [" + playerName + "]");
                }
            }
            
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "TournamentField Creation Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {

        }

    }

    public static void main(String[] args) {
        try {
            TournamentField field = new TournamentField();
            
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
            field.run(pgaTournamentId, fsSeasonWeekId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
