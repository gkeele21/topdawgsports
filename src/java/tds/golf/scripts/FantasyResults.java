/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.golf.scripts;

import bglib.scripts.ResultCode;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        System.out.println("Calculating Golf results.");
        FSGame fsGame = new FSGame(12);
        int currentFSSeasonID = fsGame.getCurrentFSSeasonID();
        FSSeason fsseason = new FSSeason(currentFSSeasonID);

        try {

            FSSeasonWeek fsSeasonWeek = fsseason.getCurrentFSSeasonWeek();
            if (tempFsseasonweekid > 0) {
                fsSeasonWeek = new FSSeasonWeek(tempFsseasonweekid);
            }

            int fsseasonweekid = fsSeasonWeek.getFSSeasonWeekID();
            System.out.println("For FSSeasonWeekID : " + fsseasonweekid);
            int lastweekfsseasonweekid = fsseasonweekid - 1;

            PGATournamentWeek tournamentWeek = PGATournamentWeek.getTournamentWeek(fsseasonweekid);
            double thisWeekFees = tournamentWeek.getTeamFee();

            //int fsseasonweekid = 14;
            _Logger.info("Processing results for FSSeasonWeekID : " + fsseasonweekid);

            StringBuilder del = new StringBuilder();
            del.append(" delete ");
            del.append(" FROM FSGolfStandings ");
            del.append(" WHERE FSSeasonWeekID = ").append(fsseasonweekid);

            CTApplication._CT_QUICK_DB.executeUpdate(del.toString());

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
                    double totalWinnings = 0;
                    double totalFees = 0;
                    int totalEventsEntered = 0;
                    CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
                    if (crs.next()) {
                        totalMoneyEarned = crs.getDouble("TotalMoneyEarned");
                        totalMoneyEarned += moneyEarned;
                        totalWinnings = crs.getDouble("TotalWinnings");
                        totalFees = crs.getDouble("TotalFees");
                        totalEventsEntered = crs.getInt("TotalEventsEntered");
                        if (teamEntered) {
                            totalFees += thisWeekFees;
                            totalEventsEntered++;
                        }
                    } else
                    {
                        totalMoneyEarned = moneyEarned;
                        totalFees = thisWeekFees;
                        totalEventsEntered++;
                    }

                    StringBuilder sql2 = new StringBuilder();
                    sql2.append("INSERT INTO FSGolfStandings ");
                    sql2.append(" (FSTeamID, FSSeasonWeekID, WeekMoneyEarned, TotalMoneyEarned, TotalWinnings, TotalFees, TotalEventsEntered, WeekFees, WeekEventEntered)");
                    sql2.append(" VALUES(").append(team.getFSTeamID());
                    sql2.append(", ").append(fsseasonweekid);
                    sql2.append(", ").append(moneyEarned);
                    sql2.append(", ").append(totalMoneyEarned);
                    sql2.append(", ").append(totalWinnings);
                    sql2.append(", ").append(totalFees);
                    sql2.append(", ").append(totalEventsEntered);
                    if (teamEntered) {
                        sql2.append(", ").append(thisWeekFees);
                        sql2.append(", 1");
                    } else {
                        sql2.append(", NULL");
                        sql2.append(", 0");
                    }
                    sql2.append(" )");

                    CTApplication._CT_QUICK_DB.executeUpdate(sql2.toString());
                    crs.close();
                }
            }

            // Figure out who won
            for (FSLeague league : leagues) {
                List<FSGolfStandings> teams = tournamentWeek.GetLeagueTeamsEntered(league.getFSLeagueID(),null);
                int numTeamsEntered = teams.size();
                int rank = 0;
                for (FSGolfStandings standingsTeam : teams)
                {
                    rank++;
                    if (rank == 1) {
                        double weekFees = standingsTeam.getWeekFees();
                        double totalWinnings = standingsTeam.getTotalWinnings();
                        double weekWinnings = numTeamsEntered * weekFees;
                        // set the winnings
                        standingsTeam.setWeekWinnings(weekWinnings);
                        standingsTeam.setTotalWinnings(totalWinnings + weekWinnings);
                    }

                    standingsTeam.setRank(rank);

                    standingsTeam.Save();
                }
            }

            // Advance the week
            System.out.println("Advancing the week...");
            FSSeasonWeek.CompleteFSSeasonWeek(fsSeasonWeek);

            _Logger.log(Level.INFO, "All standings have been set.");
        } catch (Exception e) {
            _ResultCode = ResultCode.RC_ERROR;
            _Logger.log(Level.SEVERE, "Exception in FantasyResults.run()", e);
            System.out.println("Error in FantasyGolfResults : " + e.getMessage());
        }
    }


}
