/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.fantasy.scripts;

import bglib.scripts.ResultCode;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;
import tds.main.bo.FSGame;
import tds.main.bo.FSLeague;
import tds.main.bo.FSSeason;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class FootballMatchup {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public FootballMatchup() {
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

            _Logger.info("Starting to create League Matchups from scheduleconfig table...");
//            createMatchups();

            FSGame fsGame = new FSGame(1);
            int currentFSSeasonID = fsGame.getCurrentFSSeasonID();
            FSSeason currentFSSeason = new FSSeason(currentFSSeasonID);

            // grab all leagues for this fsSeason
            List<FSLeague> fsLeagues = currentFSSeason.GetLeagues();
            for (FSLeague league : fsLeagues)
            {
                int fsleagueId = league.getFSLeagueID();
//                if (fsleagueId != 73)
//                {
//                    continue;
//                }
//                int draftComplete = league.getIsDraftComplete();
//                if (draftComplete == 1)
//                {
                    String scheduleName = league.getScheduleName();

                    createMatchups(league.getFSLeagueID(), scheduleName);
//                }
            }

            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in FootballMatchup.run()", e);
        }
    }

    public void createMatchups(int fsleagueid, String scheduleName) throws Exception {

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM FSLeague where FSLeagueID = ").append(fsleagueid);

            _Logger.info(sql.toString());
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            int weekno = 1;
            if (crs.next()) {
                int startingFSSeasonWeekID = crs.getInt("StartFSSeasonWeekID");
                String sql2 = "SELECT * FROM FSFootballScheduleConfig " +
                            " WHERE ScheduleName = '" + scheduleName + "' " +
                            " ORDER BY WeekNo, GameNo";
                _Logger.info(sql2);
                CachedRowSet crs2 = CTApplication._CT_QUICK_DB.executeQuery(sql2);
                while (crs2.next()) {
                    int tempweekno = crs2.getInt("WeekNo");
                    int gameno = crs2.getInt("GameNo");
                    int team1no = crs2.getInt("Team1");
                    int team2no = crs2.getInt("Team2");

                    if (tempweekno != weekno) {
                        startingFSSeasonWeekID++;
                        weekno = tempweekno;
                    }

                    // figure out, for this league, which teams are team1 and team2
                    int team1id = grabTeamID(fsleagueid, team1no);
                    int team2id = grabTeamID(fsleagueid, team2no);

                    // create the matchup record
                    String sql3 = "INSERT INTO FSFootballMatchup " +
                                "(FSLeagueID,FSSeasonWeekID,GameNo,Team1ID,Team2ID) " +
                                " VALUES (" + fsleagueid + "," + startingFSSeasonWeekID +
                                "," + gameno + "," + team1id + "," + team2id + ")";
                    _Logger.info(sql3);
                    CTApplication._CT_QUICK_DB.executeUpdate(sql3);

                }

                crs2.close();

            }

            crs.close();

        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "FootballMatchup Update Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {
        }

        return;
    }

    public static int grabTeamID(int fsleagueid, int schedteamno) throws Exception {
        int teamid = 0;

        String tsql = "select * from FSTeam " +
                        " where FSLeagueID = " + fsleagueid +
                        " and ScheduleTeamNo = " + schedteamno;

        CachedRowSet tcrs = CTApplication._CT_QUICK_DB.executeQuery(tsql);
        if (tcrs.next()) {
            teamid = tcrs.getInt("FSTeamID");
        }

        tcrs.close();

        return teamid;
    }

    public static void main(String[] args) {
        try {
            FootballMatchup client = new FootballMatchup();

            client.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
