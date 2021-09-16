/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.fantasy.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import tds.main.bo.*;
import tds.util.CTReturnCode;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class FootballTransactionRequests implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

//    private int _FSSeasonid = 30;

    public FootballTransactionRequests() {
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

    public static void main(String[] args) {
        try {
            FootballTransactionRequests requests = new FootballTransactionRequests();

            requests.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        Connection con = null;
        try {

            con = CTApplication._CT_QUICK_DB.getConn(false);

            FSGame fsGame = new FSGame(1);
            int currentFSSeasonID = fsGame.getCurrentFSSeasonID();

            processTransactionRequests(con, currentFSSeasonID);

        } catch (Exception e) {
            _ResultCode = ResultCode.RC_ERROR;
            _Logger.log(Level.SEVERE, "Exception in FootballTransactionRequests.run()", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {

            }
        }
    }

    public void processTransactionRequests(Connection con, int fsseasonid) {

        try {

            FSSeason fsseason = new FSSeason(fsseasonid);
            List<FSLeague> leagues = fsseason.GetLeagues();

            FSSeasonWeek fsSeasonWeek = fsseason.getCurrentFSSeasonWeek();
            for (FSLeague league : leagues) {

                int leagueid = league.getFSLeagueID();

                _Logger.info("Processing Requests for League : " + leagueid + "[START]");

                processRequests(league,fsSeasonWeek.getFSSeasonWeekID());

                _Logger.info("Processing Requests for League : " + leagueid + "[END]");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processRequests(FSLeague league,int fsseasonweekid) throws Exception {
        try {

            // Grab Transaction Order
            List<FSTeam> tOrder = league.GetTransactionOrder(fsseasonweekid);
            List<Integer> newOrder = new ArrayList<Integer>();
            for (FSTeam team : tOrder)
            {
                newOrder.add(new Integer(team.getFSTeamID()));
            }

            // While we still have teams with transaction requests, cycle through the array and process them
            boolean alldone = false;
            while (!alldone) {
                alldone = true;
                for (FSTeam team : tOrder) {
                    int teamid = team.getFSTeamID();
                    System.out.println("TeamID : " + teamid);

                    // Check to see if this team has a transaction
                    List<FSFootballTransactionRequest> teamRequests = team.getTransactionRequests(fsseasonweekid,0);
                    int size = teamRequests.size();
                    _Logger.info("Team " + teamid + " : size : " + size);

                    if (size > 0) {
                        FSFootballTransactionRequest request = teamRequests.get(0);

                        alldone = false;

                        CTReturnCode rc = FSFootballTransaction.insert(request, 1);
                        if (rc.isSuccess()) {
                            // mark this request as granted
                            request.setProcessed(1);
                            request.setGranted(1);
                            request.update();

                            // swap the players on this team's roster
                            if (request.getDropType().equals("ONIR"))
                            {
                                FSRoster newRoster = new FSRoster();

                                newRoster.setActiveState("active");
                                newRoster.setFSSeasonWeekID(request.getFSSeasonWeekID());
                                newRoster.setFSTeamID(teamid);
                                newRoster.setPlayerID(request.getPUPlayerID());
                                newRoster.setStarterState("bench");
                                newRoster.Save();

                                FSRoster roster = FSRoster.getRosterByPlayerID(teamid, request.getFSSeasonWeekID(), request.getDropPlayerID());
                                if (roster == null || roster.getID() == 0)
                                {
                                    System.out.println("Problem getting roster player : teamid [" + teamid + "] fsseasonweekid [" + request.getFSSeasonWeekID() + "] playerid [" + request.getDropPlayerID() + "]");
                                } else
                                {
                                    roster.setActiveState("ir");
                                    roster.setStarterState("bench");
                                    roster.Save();
                                }

                            } else
                            {
                                swapPlayers(teamid, request.getDropPlayerID(), request.getPUPlayerID(), fsseasonweekid);
                            }

                            newOrder.remove(new Integer(teamid));
                            newOrder.add(new Integer(teamid));
                        } else {
                            throw new Exception ("Error inserting transaction ID : " + request.getRequestID());
                        }

                        _Logger.info("Granting Request[" + request.toString() + "]");

                        // update all other transactionrequests that have not yet been processed, but involve
                        // the 2 players involved in this transaction

                        eliminateOtherRequests(request,league);

                    }
                }

            }

            // create the new order for the next week

            FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekid);
            int thisweekNo = thisWeek.getFSSeasonWeekNo();
            FSSeasonWeek nextWeek = thisWeek.getFSSeason().GetCurrentFSSeasonWeeks().get(new Integer(thisweekNo+1));
            if (nextWeek != null) {
                int count = 0;
                int nextweekid = nextWeek.getFSSeasonWeekID();

                StringBuilder sql = new StringBuilder();
                sql.append("DELETE FROM FSFootballTransactionOrder ");
                sql.append(" WHERE FSSeasonWeekID = ").append(nextweekid);
                sql.append(" AND FSLeagueID = ").append(league.getFSLeagueID());
                CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

                for (Integer teamId : newOrder)
                {
                    count++;

                    sql = new StringBuilder();
                    sql.append("INSERT INTO FSFootballTransactionOrder ");
                    sql.append(" (FSLeagueID,FSSeasonWeekID,OrderNumber,FSTeamID) ");
                    sql.append(" VALUES (").append(league.getFSLeagueID());
                    sql.append(",").append(nextweekid);
                    sql.append(",").append(count);
                    sql.append(",").append(teamId).append(")");
                    CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public void eliminateOtherRequests(FSFootballTransactionRequest request,FSLeague fsleague) throws Exception {

        // Retrieve the Teams in this league
        List<FSTeam> teams = fsleague.GetTeams();
        StringBuffer teamsStr = new StringBuffer();
        int count = 0;
        for (FSTeam team : teams) {
            count++;
            if (count > 1) {
                teamsStr.append(",");
            }
            teamsStr.append(team.getFSTeamID());
        }

        StringBuffer sql = new StringBuffer();
        sql.append("update FSFootballTransactionRequest ");
        sql.append(" set Processed = 1,Granted = 0 ");
        sql.append(" where FSTeamID in (").append(teamsStr).append(") ");
        sql.append(" and FSSeasonWeekID = ").append(request.getFSSeasonWeekID());
        sql.append(" and RequestID <> ").append(request.getRequestID());
        sql.append(" and (PUPlayerID = ").append(request.getPUPlayerID());
        sql.append(" or DropPlayerID = ").append(request.getDropPlayerID()).append(") ");

        CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
    }

    public boolean swapPlayers(int fsteamid, int dropplayerid, int puplayerid, int fsseasonweekid) throws Exception {

        StringBuffer sql = new StringBuffer();
        sql.append("  update FSRoster ");
        sql.append(" set PlayerID = ").append(puplayerid);
        sql.append(" where FSTeamID = ").append(fsteamid);
        sql.append(" and FSSeasonWeekID = ").append(fsseasonweekid);
        sql.append(" and PlayerID = ").append(dropplayerid);

        CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

        return true;
    }


}
