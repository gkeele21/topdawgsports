/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.fantasy.scripts;

import bglib.scripts.ResultCode;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.*;

/**
 *
 * @author grant.keele
 */
public class FootballDraft {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public FootballDraft() {
        _Logger = Logger.global;
        _Logger.setLevel(Level.ALL);
    }

    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    public ResultCode getResultCode() { return _ResultCode; }

    public void setScriptArgs(String[] args) { _Args = args; }

    public void run() {
        try {

            _Logger.info("Starting to insert roster from draft table...");
            
            // grab the current FSSeasonWeekID
            
            FSGame fsGame = new FSGame(1);
            int currentFSSeasonID = fsGame.getCurrentFSSeasonID();
            FSSeason currentFSSeason = new FSSeason(currentFSSeasonID);
            FSSeasonWeek currentFSSeasonWeek = currentFSSeason.getCurrentFSSeasonWeek();
            int fsSeasonWeekID = currentFSSeasonWeek.getFSSeasonWeekID();
            
            // grab all leagues for this fsSeason
            List<FSLeague> fsLeagues = currentFSSeason.GetLeagues();
            for (FSLeague league : fsLeagues)
            {
                System.out.println("LeagueId : ");
                if (league.getIsDraftComplete() != null)
                {
                    int draftComplete = league.getIsDraftComplete();
                    if (draftComplete == 1)
                    {
                        _Logger.info("Calling insertRosters for League : " + league.getLeagueName());
                        insertRosters(fsSeasonWeekID,league.getFSLeagueID());

                    }
                }
            }
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in FootballDraft.run()", e);
        }
    }

    public void insertRosters(int fsseasonweekid,int fsleagueid) throws Exception {

        Connection con = null;

        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);

            FSLeague fsleague = new FSLeague(fsleagueid);
            
            // Retrieve the Teams in this league
            List<FSTeam> teams = fsleague.GetTeams();
            StringBuilder teamsStr = new StringBuilder();
            int count = 0;
            for (FSTeam team : teams) {
                count++;
                if (count > 1) {
                    teamsStr.append(",");
                }
                teamsStr.append(team.getFSTeamID());
            }

            CTApplication._CT_QUICK_DB.executeUpdate(con,"delete from FSRoster where FSSeasonWeekID = " + fsseasonweekid + " and FSTeamID in (" + teamsStr.toString() + ")");
            
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT * ");
            sql.append(" FROM FSFootballDraft ");
            sql.append(" WHERE FSLeagueID = ").append(fsleagueid);
            sql.append(" ORDER BY Round,Place ");

            _Logger.info(sql.toString());
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());
            while (crs.next()) {
                int playerid = crs.getInt("PlayerID");
                int teamid = crs.getInt("FSTeamID");
                
                String activeState = "active";
                String starterState = "bench";

                StringBuilder sql3 = new StringBuilder();
                sql3.append(" INSERT INTO FSRoster (FSTeamID,PlayerID,FSSeasonWeekID,StarterState,ActiveState) ");
                sql3.append(" VALUES (").append(teamid);
                sql3.append(" ,").append(playerid);
                sql3.append(" ,").append(fsseasonweekid);
                sql3.append(" ,'").append(starterState).append("' ");
                sql3.append(" ,'").append(activeState).append("')");
                _Logger.info(sql3.toString());
                CTApplication._CT_QUICK_DB.executeUpdate(con,sql3.toString());

            }

            crs.close();
            
            con.commit();
        } catch (Exception e) {
            _Logger.log(Level.SEVERE,"FootballDraft Update Error : " + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    public static void main(String[] args) {
        try {
            FootballDraft draft = new FootballDraft();
            draft.run();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
