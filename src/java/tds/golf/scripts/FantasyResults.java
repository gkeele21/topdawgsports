/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.golf.scripts;

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
public class FantasyResults  {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public FantasyResults() {
        _Logger = Logger.global;
        _Logger.setLevel(Level.ALL);
    }

    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    public ResultCode getResultCode() { return _ResultCode; }

    public void setScriptArgs(String[] args) { _Args = args; }

    public static void main(String[] args) {
        try {
            FantasyResults results = new FantasyResults();
            
            int fsseasonweekid = 0;
            if (args != null && args.length > 0) {
                try {
                    fsseasonweekid = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    
                }
            }
//            fsseasonweekid = 759;
            results.run(fsseasonweekid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void run() {
        run(0);
    }
    
    public void run(int tempFsseasonweekid) {

        FSGame fsGame = new FSGame(12);
        int currentFSSeasonID = fsGame.getCurrentFSSeasonID();
        FSSeason fsseason = new FSSeason(currentFSSeasonID);

        Connection con = null;
        try {

            con = CTApplication._CT_QUICK_DB.getConn(false);
            
            FSSeasonWeek fsSeasonWeek = fsseason.getCurrentFSSeasonWeek();
            if (tempFsseasonweekid > 0) {
                fsSeasonWeek = new FSSeasonWeek(tempFsseasonweekid);
            }
            
            int fsseasonweekid = fsSeasonWeek.getFSSeasonWeekID();
            int lastweekfsseasonweekid = fsseasonweekid - 1;
            
            PGATournamentWeek tournamentWeek = PGATournamentWeek.getTournamentWeek(fsseasonweekid);
            
            //int fsseasonweekid = 14;
            _Logger.info("Processing results for FSSeasonWeekID : " + fsseasonweekid);

            // Calculate week's results
            List<FSLeague> leagues = fsseason.GetLeagues();

            for (FSLeague league : leagues) {
                List<FSTeam> teams = league.GetTeams();
                
                for (FSTeam team : teams)
                {
                    double moneyEarned = 0;
                    int numPlayers = 0;
                    boolean teamEntered = false;
                    // get team roster
                    List<FSRoster> rosterPlayers = team.getRoster(fsseasonweekid);
                    
                    if (rosterPlayers.size() >= 1) {
                        teamEntered = true;
                    }
                    
                    for (FSRoster rosterPlayer : rosterPlayers)
                    {
                        PGATournamentWeekPlayer weekPlayer = new PGATournamentWeekPlayer(tournamentWeek.getPGATournamentID(), fsseasonweekid, rosterPlayer.getPlayerID());
                        double playerEarnings = weekPlayer.getMoneyEarned();
                        moneyEarned += playerEarnings;
                        numPlayers++;
                    }
                    
//                    if (numPlayers < 6)
//                    {
//                        moneyEarned = 0;
//                    }
                    
                    StringBuilder sql = new StringBuilder();
                    sql.append(" SELECT s.* ");
                    sql.append(" FROM FSGolfStandings s ");
                    sql.append(" INNER JOIN FSTeam t on t.FSTeamID = s.FSTeamID ");
                    sql.append(" WHERE s.FSSeasonWeekID = ").append(lastweekfsseasonweekid);
                    sql.append(" AND t.FSTeamID = ").append(team.getFSTeamID());
                    
                    double totalMoneyEarned = 0;
                    CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
                    if (crs.next()) {
                        totalMoneyEarned = crs.getDouble("TotalMoneyEarned");
                        totalMoneyEarned += moneyEarned;
                    } else
                    {
                        totalMoneyEarned = moneyEarned;
                    }
                    
                    StringBuilder sql2 = new StringBuilder();
                    sql2.append("INSERT INTO FSGolfStandings ");
                    sql2.append(" (FSTeamID, FSSeasonWeekID, WeekMoneyEarned, TotalMoneyEarned, WeekEventEntered)");
                    sql2.append(" VALUES(").append(team.getFSTeamID());
                    sql2.append(", ").append(fsseasonweekid);
                    sql2.append(", ").append(moneyEarned);
                    sql2.append(", ").append(totalMoneyEarned);
                    sql2.append(", ");
                    if (teamEntered) {
                        sql2.append("1");
                    } else {
                        sql2.append("0");
                    }
                    sql2.append(" )");
                    
                    CTApplication._CT_QUICK_DB.executeUpdate(con, sql2.toString());
                    crs.close();
                }
            }
            
            con.commit();
            
            // Figure out who won
            for (FSLeague league : leagues) {
                int numTeamsEntered = 0;
                List<FSTeam> teams = tournamentWeek.GetLeagueTeamsEntered(league.getFSLeagueID(),null);
                
                for (FSTeam team : teams)
                {
                    
                }
            }

        } catch (Exception e) {
            _ResultCode = ResultCode.RC_ERROR;
            _Logger.log(Level.SEVERE, "Exception in FantasyResults.run()", e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {}
            }

        }
    }


}
