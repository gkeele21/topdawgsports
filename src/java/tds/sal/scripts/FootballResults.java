/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.sal.scripts;

import bglib.scripts.ResultCode;
import bglib.util.FSUtils;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.*;
import tds.util.CTReturnCode;

/**
 *
 * @author grant.keele
 */
public class FootballResults  {

    private static final int _FSSeasonID = 70;
    
    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

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
            
            int fsseasonweekid = 931;
            if (args != null && args.length > 0)
            {
                try {
                    fsseasonweekid = Integer.parseInt(args[0]);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            
            results.run(fsseasonweekid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void run() {
        run(0);
    }
    
    public void run(int tempFsseasonweekid) {
        
        try {
            FSSeason fsseason = new FSSeason(_FSSeasonID);
            
            FSSeasonWeek fsSeasonWeek = fsseason.getCurrentFSSeasonWeek();
            if (tempFsseasonweekid > 0)
            {
                fsSeasonWeek = new FSSeasonWeek(tempFsseasonweekid);
            }
            int fsseasonweekid = fsSeasonWeek.getFSSeasonWeekID();
            boolean runStatsFirst=false;
            boolean fixEmptyRosters=false;
            boolean generateSalaries=false;

//            for (String arg : _Args) {
//                if (arg.trim().length()==0) continue;
//                if (arg.compareToIgnoreCase("runStatsFirst")==0) {
//                    runStatsFirst=true;
//                    continue;
//                }
//                if (arg.compareToIgnoreCase("generateSalaries")==0) {
//                    generateSalaries=true;
//                    continue;
//                }
//                try {
//                    AuDate playDate = new AuDate(arg, FSConstants.PLAYDATE_PATTERN);
//                    weekNo = WeekInfo.getWeekInfo(Season.getCurrentSeason(), playDate).getWeekNo();
//                }
//                catch (Exception e) {
//                    _Logger.warning("Did not recognize argument '" + arg + "' as a date; will calculate last week's results.");
//                }
//            }

//            if (weekNo==0) {
//                weekNo = Math.max(WeekInfo.getWeekInfo(Season.getCurrentSeason(), new AuDate()).getWeekNo() - 1, 1);
//            }

            FSSeasonWeek fsseasonWeek = new FSSeasonWeek(fsseasonweekid);
            if (runStatsFirst) {
//                FSAHarness results = new FSAHarness("FootballStats", new String[] { wi.getEndDate().toString(FSConstants.PLAYDATE_PATTERN) } );
//                results.chain(_Logger);
            }
            if (generateSalaries) {
//                FSAHarness salaries = new FSAHarness("GenerateSalaries", new String[] { wi.getEndDate().toString(FSConstants.PLAYDATE_PATTERN) } );
//                salaries.chain(_Logger);
            }

            // don't run this before Tuesday
//            if (wi.getEndDate().before(new AuDate(), true)==false || (statsAreReady(wi.getStartDate())==false)) {
//                _ResultCode = ResultCode.RC_INCOMPLETE;
//                _Logger.info("Not yet ready to run results. Returning Incomplete...");
//                return;
//            }

            List<FSLeague> leagues = fsseason.GetLeagues();
            for (FSLeague league : leagues) {
                calculateResults(league.getFSLeagueID(), fsseasonweekid);
            }

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in FootballResults.run()", e);
        }
    }

//    private static boolean statsAreReady(AuDate playDate) {
//        boolean ret = false;
//        String sql = "select count(*) as cnt from FootballStats where PlayDate = '" + playDate.toString(FSConstants.PLAYDATE_PATTERN) + "'";
//        try {
//            CachedRowSet crs = _DB.executeQuery(sql);
//            if (crs.next()) {
//                ret = crs.getInt("cnt") > 100;
//            }
//        }
//        catch (Exception e) {
//            Log.error(e);
//        }
//
//        return ret;
//    }

    public void calculateResults(int fsleagueid, int fsseasonweekid) throws Exception {
        _Logger.info("Starting results for FSLeagueID : " + fsleagueid + ", FSSeasonWeekID : " + fsseasonweekid);

        //fixEmptyRosters(_Logger, fsleagueid, fsseasonweekid);
        calculateFantasyPts(_Logger, fsleagueid, fsseasonweekid);
        calculateStandings(_Logger, fsleagueid, fsseasonweekid);
        //declareWagerWinners(room, playDate);

        _Logger.info("Finished results for FSLeagueID : " + fsleagueid + ", FSSeasonWeekID : " + fsseasonweekid);
    }

    // note: if we fail to calculate the pts for one team, the whole process fails
    static void calculateFantasyPts(Logger logger, int fsleagueid, int fsseasonweekid) throws Exception {
        logger.info("Calculating fantasy points for FSLeagueID : " + fsleagueid + ", FSSeasonWeekID : " + fsseasonweekid);
        Connection con = null;
        CTReturnCode rc = null;
        int count;
        try {
            con = CTApplication._CT_QUICK_DB.getConn();
            con.setAutoCommit(false);

            FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
            int thisweekNo = thisWeek.getFSSeasonWeekNo();
            FSSeasonWeek lastWeek = thisweekNo == 1 ? thisWeek : thisWeek.getFSSeason().GetCurrentFSSeasonWeeks().get(new Integer(thisweekNo-1));

            String sql2 = "delete from FSFootballStandings " +
                        " where FSSeasonWeekID = " + fsseasonweekid +
                        " and FSTeamID in (select FSTeamID from FSTeam where FSLeagueID = " + fsleagueid + ")";
            CTApplication._CT_QUICK_DB.executeUpdate(con,sql2);

            //String sql = "select * from Team where PlayDate = '" + playdate.toString(FSConstants.PLAYDATE_PATTERN) + "' and RoomID = " + room.getRoomID();
            String sql = "select * from FSTeam where FSLeagueID = " + fsleagueid + " and IsActive = 1";

            logger.finer(sql);
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql);
            count = 0;
            while (crs.next()) {
                int teamid = crs.getInt("FSTeamID");
                FSTeam team = new FSTeam(teamid);
                
                double teampts = team.getWeekFantasyPoints(con, fsseasonweekid,"s.SalFantasyPts",thisWeek.getFSSeason().getSeasonID());
                
                // grab last week's standings info for this team
                double totfpts = 0.0;
                double totgpts = 0.0;
                StringBuffer s = new StringBuffer();
                s.append("select * from FSFootballStandings where FSTeamID = ").append(teamid);
                s.append(" and FSSeasonWeekID = ").append(lastWeek.getFSSeasonWeekID());
                
                CachedRowSet cc = CTApplication._CT_QUICK_DB.executeQuery(con, s.toString());
                if (cc.next()) {
                    totfpts = cc.getDouble("TotalFantasyPts");
                    totgpts = cc.getDouble("TotalGamePoints");
                }
                
                cc.close();

                DecimalFormat twoDForm = new DecimalFormat("#.##");
                teampts = Double.valueOf(twoDForm.format(teampts));
                totfpts = Double.valueOf(twoDForm.format(totfpts));
                totgpts = Double.valueOf(twoDForm.format(totgpts));

                // create a new record for this week
                sql2 = "insert into FSFootballStandings " +
                        " (FSTeamID,FSSeasonWeekID,FantasyPts,GamePoints,TotalFantasyPts,TotalGamePoints) " +
                        " values (" + teamid + "," + fsseasonweekid + "," + teampts + ",0," + totfpts + "," + totgpts + ")";
                CTApplication._CT_QUICK_DB.executeUpdate(con,sql2);

                logger.info("Updated Team ID #" + teamid + " ('" + team.getTeamName() + "') fantasy points to " + teampts);
                count++;
            }
            rc = CTReturnCode.RC_SUCCESS;
            crs.close();
        }
        finally {
            FSUtils.finishTransaction(con, rc);
        }
        logger.info("Calculated and updated to db fantasy points for " + count + " teams.");
    }

    static void fixEmptyRosters(Logger logger, int fsleagueid, int fsseasonweekid) throws Exception {
        logger.info("Fixing Empty Rosters for FSLeagueID : " + fsleagueid + ", FSSeasonWeekID : " + fsseasonweekid);
        Connection con = null;
        CTReturnCode rc = null;
        int count;
        try {
            con = CTApplication._CT_QUICK_DB.getConn();
            con.setAutoCommit(false);

            String sql2 = "select * from FSTeam t " +
                        " left join FSRoster r on r.FSTeamID = t.FSTeamID and r.FSSeasonWeekID = " + fsseasonweekid +
                        " where t.IsActive = 1 " +
                        " and r.FSTeamID is null " +
                        " and t.FSLeagueID = " + fsleagueid;
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql2);

            logger.finer(sql2);
            count = 0;
            while (crs.next()) {
                int teamid = crs.getInt("FSTeamID");
                FSTeam team = new FSTeam(teamid);

                // grab this team's last week's roster
                FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
                int thisweekNo = thisWeek.getFSSeasonWeekNo();
                FSSeasonWeek lastWeek = thisweekNo == 1 ? thisWeek : thisWeek.getFSSeason().GetCurrentFSSeasonWeeks().get(new Integer(thisweekNo-1));
                List<FSRoster> players = team.getRoster(lastWeek.getFSSeasonWeekID());

                // TODO: check to see if the players are under this week's salary cap.
                // if not, remove the most costly player

                team.insertOrUpdateRoster(con,fsseasonweekid,players);

                logger.info("Created Roster from last week for Team ID #" + teamid + " ('" + team.getTeamName() + "') ");
                count++;
            }
            rc = CTReturnCode.RC_SUCCESS;
            crs.close();
        }
        finally {
            FSUtils.finishTransaction(con, rc);
        }
        logger.info("Updated this week's rosters from last week's rosters for " + count + " teams.");
    }

    static void calculateStandings(Logger logger, int fsleagueid, int fsseasonweekid) throws Exception {
        logger.info("Calculating standings for FSLeagueID : " + fsleagueid + ", FSSeasonWeekID : " + fsseasonweekid);
        Connection con = null;
        CTReturnCode rc = null;
        int count;
        try {
            con = CTApplication._CT_QUICK_DB.getConn();
            con.setAutoCommit(false);

            // grab previous week's standings
            
            FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
            int thisweekNo = thisWeek.getFSSeasonWeekNo();
            FSSeasonWeek lastWeek = thisweekNo == 1 ? thisWeek : thisWeek.getFSSeason().GetCurrentFSSeasonWeeks().get(new Integer(thisweekNo-1));

            FSLeague league = new FSLeague(con,fsleagueid);
            int numteams = league.getNumTeams();
                    
//            String sql = "select count(1) as cnt " +
//                        " from " + CTApplication.TBL_PREF + "FSFootballStandings s " +
//                        " INNER JOIN " + CTApplication.TBL_PREF + "FSTeam t on t.FSTeamID = s.FSTeamID " +
//                        " where s.FSSeasonWeekID = " + lastWeek.getFSSeasonWeekID() + 
//                        " and t.FSLeagueID = " + fsleagueid;
//
//            logger.finer(sql);
//            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql);
//            if (crs.next()) {
//                numteams = crs.getInt("cnt");
//            }

            String sql = "select s.* from FSFootballStandings s " +
                        " INNER JOIN FSTeam t on t.FSTeamID = s.FSTeamID " +
                        " where s.FSSeasonWeekID = " + fsseasonweekid +
                        " and t.FSLeagueID = " + fsleagueid +
                        " order by s.FantasyPts desc";

            logger.info(sql);
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql);
            int numpoints = numteams;
            //numpoints = 28;
            double prevfantasypoints = -1;
            int prevpoints = numpoints;
            count = 0;
            while (crs.next()) {
                count++;
                int teamid = crs.getInt("FSTeamID");
                double wkfpts = crs.getDouble("FantasyPts");

                // Grab last week's standings for this team
                String tempsql = "select s.* from FSFootballStandings s " +
                            " INNER JOIN FSTeam t on t.FSTeamID = s.FSTeamID " +
                            " where s.FSSeasonWeekID = " + fsseasonweekid +
                            " and s.FSTeamID = " + teamid;

                logger.info(tempsql);
                double wkgpts = 0;
                if (wkfpts == prevfantasypoints) {
                    wkgpts = prevpoints;
                } else {
                    wkgpts = numpoints;
                    prevpoints = numpoints;
                }
                double totfpts = wkfpts;
                double totgpts = wkgpts;
                CachedRowSet tempcrs = CTApplication._CT_QUICK_DB.executeQuery(con, tempsql);
                if (tempcrs.next()) {
                    double tot = tempcrs.getDouble("TotalFantasyPts");
                    totfpts = totfpts + tot;
                    totgpts = tempcrs.getDouble("TotalGamePoints");
                    totgpts = wkgpts + totgpts;
                }

                tempcrs.close();

                DecimalFormat twoDForm = new DecimalFormat("#.##");
                totfpts = Double.valueOf(twoDForm.format(totfpts));
                totgpts = Double.valueOf(twoDForm.format(totgpts));
                wkgpts = Double.valueOf(twoDForm.format(wkgpts));


                String sql2 = "update FSFootballStandings " +
                            " set GamePoints = " + wkgpts + " " +
                            " ,TotalFantasyPts = " + totfpts +
                            " ,TotalGamePoints = " + totgpts +
                            " where FSTeamID = " + teamid +
                            " and FSSeasonWeekID = " + fsseasonweekid;
                CTApplication._CT_QUICK_DB.executeUpdate(con, sql2);
                logger.info("Updated Team ID #" + teamid + " salary points to " + numpoints);
                numpoints--;
                prevfantasypoints = wkfpts;
            }
            rc = CTReturnCode.RC_SUCCESS;
            crs.close();
        }
        finally {
            FSUtils.finishTransaction(con, rc);
        }
        logger.info("Calculated and updated to db salary points for " + count + " teams.");
    }

    
}
