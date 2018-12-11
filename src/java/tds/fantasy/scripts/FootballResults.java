/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.fantasy.scripts;

import bglib.scripts.ResultCode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.*;

/**
 *
 * @author grant.keele
 */
public class FootballResults  {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

//    private static final int _TenmanID = 25;
//    private static final int _KeeperID = 26;
//    private static final int _FSSeasonID = 20;
//    private static final int _SeasonID = 8;

    public FootballResults() {
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
            FootballResults results = new FootballResults();
            
            boolean resultsFinal = false;
            int fsseasonweekid = 0;
            if (args != null && args.length > 0) {
                try {
                    resultsFinal = Boolean.parseBoolean(args[0]);
                    fsseasonweekid = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    
                }
            }
//            fsseasonweekid = 657;
            results.run(resultsFinal, fsseasonweekid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void run() {
        run(false, 0);
    }
    
    public void run(boolean resultsFinal, int tempFsseasonweekid) {

        int statsofficial = 1;
        boolean figurebeststarters = true;
        boolean calcHiScoreOnly = false;

        FSGame fsGame = new FSGame(1);
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
            
            //int fsseasonweekid = 14;
            _Logger.info("Processing results for FSSeasonWeekID : " + fsseasonweekid);
            calculateResults(con, fsseason, fsseasonweekid, figurebeststarters, resultsFinal, calcHiScoreOnly);
            con.commit();
            
            if (resultsFinal) {
                _Logger.info("Calculating Position Rankings.");
                // Calculate the players' rankings for this week
                FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
                calculatePlayerPositionRankings(con, thisWeek.getSeasonWeekID(), fsGame.getCurrentFSSeasonID());
                //calculatePlayerPositionRankings(con, 34, _SeasonID);

                con.commit();
                // Calculate the players' rankings for the season's total stats
                calculatePlayerPositionRankings(con, 0, fsGame.getCurrentFSSeasonID());

                con.commit();
            }
            

        } catch (Exception e) {
            _ResultCode = ResultCode.RC_ERROR;
            _Logger.log(Level.SEVERE, "Exception in FootballStats.run()", e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {}
            }

        }
    }

    private static void calculateResults(Connection con, FSSeason fsseason, int fsseasonweekid, boolean figurebeststarters, boolean resultsFinal, boolean calcHiScoreOnly) throws Exception {

        // Calculate week's results
        List<FSLeague> leagues = fsseason.GetLeagues();
        
        for (FSLeague league : leagues) {
            
            // Get the Schedule for this league

            List<FSFootballMatchup> matchups = league.GetResults(fsseasonweekid);

            int hiscoreid = 0;
            double hiscore = 0.00;
            for (FSFootballMatchup matchup : matchups) {

                int gameno = matchup.getGameNo();
                FSTeam team1 = matchup.getFSTeam1();
                FSTeam team2 = matchup.getFSTeam2();
                System.out.println("============================================================");
                System.out.println("FSSeasonWeekID : " + fsseasonweekid + ", GameNo : " + gameno);
                System.out.println("Team 1 : " + team1.getTeamName() + ", Team 2 : " + team2.getTeamName());
                if (figurebeststarters) {
                    //boolean includeTEasWR = league.getFSLeagueID() == _TenmanID ? false : true;
                    boolean includeTEasWR = (league.getIncludeTEasWR() != 0);
                    team1.figureBestStarters(fsseasonweekid,includeTEasWR, league.getFSLeagueID());
                    team2.figureBestStarters(fsseasonweekid,includeTEasWR, league.getFSLeagueID());
                }
                
                con.commit();
                
                double tm1pts = 0.00;
                double tm2pts = 0.00;

                // Calculate Team1 Points
                //String custom = lg.getCustomLeague();
                String custom = "n";
                if (custom.equals("n")) {
                    tm1pts = team1.getWeekFantasyPoints(fsseasonweekid,fsseason.getSeasonID());
                    tm2pts = team2.getWeekFantasyPoints(fsseasonweekid,fsseason.getSeasonID());
                } else {
                    //tm1pts = FootballTeam.getTeamWeekCustomPoints(lgid,team1ID,weekno);
                    //tm2pts = FootballTeam.getTeamWeekCustomPoints(lgid,team2ID,weekno);
                }

                System.out.println("Team1Pts : " + tm1pts + ", Team2Pts : " + tm2pts);
                if (tm1pts > hiscore) {
                    hiscore = tm1pts;
                    hiscoreid = team1.getFSTeamID();
                }
                if (tm2pts > hiscore) {
                    hiscore = tm2pts;
                    hiscoreid = team2.getFSTeamID();
                }

                // Insert Results into FootballResultsTable
                int winner = 0;
                if (tm1pts > tm2pts) {
                    winner = team1.getFSTeamID();
                } else if (tm2pts > tm1pts) {
                    winner = team2.getFSTeamID();
                } else { // Tie
                    winner = 0;
                }

                matchup.setTeam1Pts(tm1pts);
                matchup.setTeam2Pts(tm2pts);
                matchup.update();

                if (resultsFinal) {
                    // Update the Players' Standings
                    // Team 1
                    
                        updateStandings(con, winner, team1.getFSTeamID(), team2.getFSTeamID(), fsseasonweekid, tm1pts, tm2pts, calcHiScoreOnly);

                        // Team 2
                        updateStandings(con, winner, team2.getFSTeamID(), team1.getFSTeamID(), fsseasonweekid, tm2pts, tm1pts, calcHiScoreOnly);
                    
                        con.commit();
                        
                    if (!calcHiScoreOnly) {                    
                        calculateLastFive(con, team1.getFSTeamID(), fsseasonweekid);

                        calculateLastFive(con, team2.getFSTeamID(), fsseasonweekid);
                    }
                }
            }

            con.commit();
            
            System.out.println("Add Rosters.");

            if (resultsFinal) {
                updateHiScoreWinner(con,hiscoreid,fsseasonweekid);

                con.commit();

                if (!calcHiScoreOnly) {
                    
                    // check if the week type is FINAL.  If so, then don't do this.
                    FSSeasonWeek fsSeasonWeek = new FSSeasonWeek(fsseasonweekid);
                    
                    String weekType = fsSeasonWeek.getWeekType();
                    if (!weekType.equals(FSSeasonWeek.WeekType.FINAL.toString())) {
                        addMaxRequests(con,fsseasonweekid,league.getFSLeagueID());

                        con.commit();

                        //FSFootballTransaction.updateTransactionOrder(lg,weekno);

                        //ProcessRequests.createNewTransactionOrder(league,weekno+1);

                        // Add New Weeks Rosters for Football
                        addNewWeeksRosters(con,fsseasonweekid,league.getFSLeagueID());

                        con.commit();
                    }
                }
                
                // Calculate the players' rankings for this week
                FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
//                calculatePlayerPositionRankings(con, thisWeek.getSeasonWeekID(), thisWeek.getFSSeason().getSeasonID());
                
                // Calculate the players' rankings for the season's total stats
//                calculatePlayerPositionRankings(con, 0, thisWeek.getFSSeason().getSeasonID());
            }
        }

    }

    public static void updateStandings(Connection con, int winner, int team1id, int team2id, int fsseasonweekid, double tm1pts, double tm2pts, boolean calcHiScoreOnly) {
        
        try {
            FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
            int thisweekNo = thisWeek.getFSSeasonWeekNo();

            FSSeasonWeek lastWeek = thisweekNo == 1 ? thisWeek : thisWeek.getFSSeason().GetCurrentFSSeasonWeeks().get(new Integer(thisweekNo-1));

            int wins = 0;
            int losses = 0;
            int ties = 0;
            double totfantasypts = 0;
            double totptsagainst = 0;
            int totalhiscores = 0;
            int streak = 0;
            double winpct = 0;
            int rank = 0;
            String lastfive = "";
            int hi = 0;

            if (thisWeek != lastWeek)
            {
                StringBuffer sql = new StringBuffer();
                sql.append("select * from FSFootballStandings " +
                            "where FSTeamID = " + team1id +
                            " and FSSeasonWeekID = " + lastWeek.getFSSeasonWeekID());

                CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());
                if (crs3.next()) {

                    wins = crs3.getInt("Wins");
                    losses = crs3.getInt("Losses");
                    ties = crs3.getInt("Ties");
                    totfantasypts = crs3.getDouble("TotalFantasyPts");
                    totptsagainst = crs3.getDouble("TotalFantasyPtsAgainst");
                    totalhiscores = crs3.getInt("TotalHiScores");
                    streak = crs3.getInt("CurrentStreak");
                    winpct = crs3.getDouble("WinPercentage");
                    rank = crs3.getInt("Rank");
                    lastfive = crs3.getString("LastFive");
                    hi = 0;
                    
                }   
                crs3.close();
            }

            double fantasypts = tm1pts;
            double ptsagainst = tm2pts;

            if (!calcHiScoreOnly) {
                if (winner == team1id) {
                    wins++;
                    if (streak > 0) {
                        streak++;
                    } else {
                        streak = 1;
                    }
                } else if (winner == team2id) {
                    losses++;
                    if (streak < 0) {
                        streak--;
                    } else {
                        streak = -1;
                    }
                } else {
                    ties++;
                }

                totfantasypts = totfantasypts + fantasypts;
                totptsagainst = totptsagainst + ptsagainst;

                winpct = 0;
                rank = 1;

                lastfive = "";
            }
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            fantasypts = Double.valueOf(twoDForm.format(fantasypts));
            totfantasypts = Double.valueOf(twoDForm.format(totfantasypts));
            ptsagainst = Double.valueOf(twoDForm.format(ptsagainst));
            totptsagainst = Double.valueOf(twoDForm.format(totptsagainst));

//            if (thisweekNo == 1) {
//                // update
//
//                sql = new StringBuffer();
//                sql.append("update FSFootballStandings " +
//                            " set FantasyPts = " + fantasypts +
//                            " ,TotalFantasyPts = " + totfantasypts +
//                            " ,Wins = " + wins +
//                            " ,Losses = " + losses +
//                            " ,Ties = " + ties +
//                            " ,WinPercentage = " + winpct +
//                            " ,FantasyPtsAgainst = " + ptsagainst +
//                            " ,TotalFantasyPtsAgainst = " + totptsagainst +
//                            " ,HiScore = " + hi +
//                            " ,TotalHiScores = " + totalhiscores +
//                            " ,Rank = " + rank +
//                            " ,CurrentStreak = " + streak +
//                            " ,LastFive = '" + lastfive + "' " +
//                            " where FSTeamID = " + team1id +
//                            " and FSSeasonWeekID = " + fsseasonweekid);
//
//                System.out.println(sql);
//
//                CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());
//
//            } else {
                // Insert new Record into FootballStandings Table

                CTApplication._CT_QUICK_DB.executeUpdate(con, "delete from FSFootballStandings " +
                                        " where FSTeamID = " + team1id +
                                        " and FSSeasonWeekID = " + fsseasonweekid);

                StringBuffer sql = new StringBuffer();
                sql.append("insert into FSFootballStandings " +
                            " (FSTeamID,FSSeasonWeekID,FantasyPts,TotalFantasyPts," +
                            " Wins,Losses,Ties,WinPercentage,FantasyPtsAgainst,TotalFantasyPtsAgainst," +
                            " HiScore,TotalHiScores,Rank,CurrentStreak,LastFive) " +
                            "values (" + team1id + "," + 
                            fsseasonweekid + "," +
                            fantasypts + "," +
                            totfantasypts + "," +
                            wins + "," +
                            losses + "," +
                            ties + "," +
                            winpct + "," +
                            ptsagainst + "," +
                            totptsagainst + "," +
                            hi + "," +
                            totalhiscores + "," +
                            rank + "," +
                            streak + ",'" +
                            lastfive + "')");

                System.out.println(sql);

                CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());

//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void updateHiScoreWinner(Connection con,int teamid,int fsseasonweekid) throws Exception {

        FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
        int thisweekNo = thisWeek.getFSSeasonWeekNo();
        FSSeasonWeek lastWeek = thisweekNo == 1 ? thisWeek : thisWeek.getFSSeason().GetCurrentFSSeasonWeeks().get(new Integer(thisweekNo-1));

        StringBuffer sql = new StringBuffer();
        sql.append("select * from FSFootballStandings " +
                "where FSTeamID = " + teamid +
                " and FSSeasonWeekID = " + (lastWeek.getFSSeasonWeekID()));

        CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());
        if (crs.next()) {
            int hi = crs.getInt("TotalHiScores");
            hi++;
            CTApplication._CT_QUICK_DB.executeUpdate(con,"update FSFootballStandings " +
            " set TotalHiScores = " + hi + ",HiScore = 1 where FSTeamID = " + teamid +
            " and FSSeasonWeekID = " + thisWeek.getFSSeasonWeekID());
        }
    }

    public static void addMaxRequests(Connection con,int fsseasonweekid,int fsleagueid) throws Exception {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from FSTeam t " +
            " where t.FSLeagueID = " + fsleagueid);

        FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
        int thisweekNo = thisWeek.getFSSeasonWeekNo();
        FSSeasonWeek nextWeek = thisWeek.getFSSeason().GetCurrentFSSeasonWeeks().get(new Integer(thisweekNo+1));

        CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());
        while (crs.next()) {

            int teamid = crs.getInt("FSTeamID");

            sql = new StringBuffer();
            sql.append("delete from FSFootballMaxRequests " +
                        " where FSTeamID = " + teamid + " and FSSeasonWeekID = " + nextWeek.getFSSeasonWeekID());
            CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());

            sql = new StringBuffer();
            sql.append("insert into FSFootballMaxRequests " +
                        " (FSTeamID,FSSeasonWeekID,MaxRequests) " +
                        " values (" + teamid + "," + nextWeek.getFSSeasonWeekID() + ",0)");
            CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());
         }

        crs.close();

    }

    public static void addNewWeeksRosters(Connection con,int fsseasonweekid,int fsleagueid) throws Exception {

        FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
        int thisweekNo = thisWeek.getFSSeasonWeekNo();
        FSSeasonWeek nextWeek = thisWeek.getFSSeason().GetCurrentFSSeasonWeeks().get(new Integer(thisweekNo+1));

        StringBuffer sql = new StringBuffer();
        sql.append("select * from FSRoster r " +
                    " INNER JOIN FSTeam t on t.FSTeamID = r.FSTeamID " +
                    " INNER JOIN Player p on p.PlayerID = r.PlayerID " +
                    " where t.FSLeagueID = " + fsleagueid +
                    " and FSSeasonWeekID = " + fsseasonweekid);

         CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());
         while (crs.next()) {

            String playerid = crs.getString("PlayerID");
            String fsteamid = crs.getString("FSTeamID");
            String starterstate = "bench";
            String activestate = crs.getString("ActiveState");


            CTApplication._CT_QUICK_DB.executeUpdate(con,"delete from FSRoster where FSSeasonWeekID = " + nextWeek.getFSSeasonWeekID() + " and FSTeamID = " + fsteamid + " and PlayerID = " + playerid);
            StringBuffer sql2 = new StringBuffer();
            sql2.append("insert into FSRoster (FSSeasonWeekID,PlayerID,FSTeamID,StarterState,ActiveState) "+
                        "values(" + nextWeek.getFSSeasonWeekID() + ", " +
                        playerid + ", " +
                        fsteamid + ", '" +
                        starterstate + "', '" +
                        activestate + "')");

             CTApplication._CT_QUICK_DB.executeUpdate(con,sql2.toString());

         }

        crs.close();

    }

    public static void calculateLastFive(Connection con,int teamid,int fsseasonweekid) throws Exception {

        FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
        int thisweekNo = thisWeek.getFSSeasonWeekNo();
        int prevWeekNo = thisweekNo - 5;
        if (prevWeekNo < 1) {
            prevWeekNo = 1;
        }
        
        FSSeason fsseason = thisWeek.getFSSeason();
        
        FSSeasonWeek prevWeek = fsseason.GetCurrentFSSeasonWeeks().get(prevWeekNo);
        
        int prevWins = 0;
        int prevLosses = 0;
        
        StringBuffer sql = new StringBuffer();
        sql.append("select * from FSFootballStandings " +
                "where FSTeamID = " + teamid +
                " and FSSeasonWeekID = " + (prevWeek.getFSSeasonWeekID()));

        CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());
        if (crs.next()) {
            prevWins = crs.getInt("Wins");
            prevLosses = crs.getInt("Losses");
            
        }
        
        int wins = 0;
        int losses = 0;
        
        sql = new StringBuffer();
        sql.append("select * from FSFootballStandings " +
                "where FSTeamID = " + teamid +
                " and FSSeasonWeekID = " + (thisWeek.getFSSeasonWeekID()));

        crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());
        if (crs.next()) {
            wins = crs.getInt("Wins");
            losses = crs.getInt("Losses");
            
        }
        
        int l5wins = wins - prevWins;
        int l5losses = losses - prevLosses;
        
        StringBuffer sql2 = new StringBuffer();
        sql2.append("update FSFootballStandings ");
        sql2.append(" set LastFive = '").append(l5wins).append("-").append(l5losses).append("'");
        sql2.append(" where FSTeamID = ").append(teamid);
        sql2.append(" and FSSeasonWeekID = ").append(thisWeek.getFSSeasonWeekID());

        CTApplication._CT_QUICK_DB.executeUpdate(con,sql2.toString());

    }

    public static void calculatePlayerPositionRankings(Connection con, int seasonweekid, int seasonid) {
        
        try {
            
            Collection<Position> positions = Position.getAllPositions();
            
            for (Position position : positions) {
                
                StringBuffer sql = new StringBuffer();
                sql.append(" select s.* ");
                sql.append(" from FootballStats s ");
                sql.append(" inner join Player p on p.StatsPlayerID = s.StatsPlayerID ");
                sql.append(" where p.PositionID = ").append(position.getPositionID());
                sql.append(" and s.SeasonWeekID = ").append(seasonweekid);
                sql.append(" and s.SeasonID = ").append(seasonid);
                sql.append(" order by s.FantasyPts desc");

                CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());
                int posnum = 1;
                int prevposnum = 1;
                double prevpts = 0;
                while (crs.next()) {

                    String statsplayerid = crs.getString("StatsPlayerID");
                    double fantasypts = crs.getDouble("FantasyPts");
                    
                    int currposnum = posnum;
                    if (fantasypts == prevpts) {
                        currposnum = prevposnum;
                    }
                    
                    String ranking = position.getPositionName() + currposnum;
                    
                    prevposnum = posnum;
                    posnum++;
                    
                    // update
                    StringBuffer upsql = new StringBuffer();
                    upsql.append("update FootballStats ");
                    upsql.append(" set PositionRank = '").append(ranking).append("' ");
                    upsql.append(" where StatsPlayerID = '").append(statsplayerid).append("' ");
                    upsql.append(" and SeasonWeekID = ").append(seasonweekid);
                    upsql.append(" and SeasonID = ").append(seasonid);
                    
                    System.out.println("StatsPlayerID : " + statsplayerid + ", Rank : " + ranking + ", Query : " + upsql);
                    
                    CTApplication._CT_QUICK_DB.executeUpdate(con,upsql.toString());
                }
                
                crs.close();
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
