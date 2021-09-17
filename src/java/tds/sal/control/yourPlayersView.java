package tds.sal.control;

import bglib.util.AuTimer;
import bglib.util.FSUtils;
import org.apache.commons.lang.StringEscapeUtils;
import tds.main.bo.*;
import tds.main.control.BaseTeamView;
import tds.util.CTReturnCode;
import tds.util.CTReturnType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Apr 24, 2006
 * Time: 11:39:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class yourPlayersView extends BaseTeamView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        AuTimer.init();
        String timerName = "yourPlayersView";
        AuTimer.start(timerName);

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;

        System.out.println("Timer[" + timerName + "] - After parent process : " + AuTimer.elapsedTime(timerName));
        // Retrieve players

        FSLeague league = _FSTeam.getFSLeague();
        FSSeason season = league.getFSSeason();
        if (season == null) {
            season = new FSSeason(league.getFSSeasonID());
        }
        System.out.println("Timer[" + timerName + "] - After creating Season : " + AuTimer.elapsedTime(timerName));
        try {
            // Sort params
            String sortParam = FSUtils.getRequestParameter(request, "sort");
            if (sortParam.length()==0) {
                // check for the sorting object from the session
                Object temp = _Session.getHttpSession().getAttribute("salSort");
                if (temp != null) {
                    sortParam = temp.toString();
                } else {
                    sortParam="4_d";
                }
            }
            String sort = resolveSortColumn(sortParam);
            _Session.getHttpSession().setAttribute("salSort", StringEscapeUtils.escapeHtml(sortParam));
            _Session.getHttpSession().setAttribute("resSort", sort);
            System.out.println("Timer[" + timerName + "] - After resolving sort : " + AuTimer.elapsedTime(timerName));
            int posid = FSUtils.getIntRequestParameter(request, "pos", 0);
            if (posid == 0) {
                if (_Session.getHttpSession().getAttribute("createpostpos") != null) {
                    try {
                        posid = Integer.parseInt(_Session.getHttpSession().getAttribute("createpostpos").toString());
                    } catch (Exception e) {
                        posid = 1;
                    }
                } else {
                    posid = 1;
                }
            }
            Position position = Position.getInstance(posid);
            _Session.getHttpSession().setAttribute("createpostpos",position.getPositionID());

            System.out.println("Timer[" + timerName + "] - After creating Position : " + AuTimer.elapsedTime(timerName));
            FSFootballSeasonDetail seasonDetail = new FSFootballSeasonDetail(season.getFSSeasonID());
            Long salarycap = seasonDetail.getMaxSalaryCap();

            _Session.getHttpSession().setAttribute("salaryCap", salarycap);
            _Session.getHttpSession().setAttribute("seasonDetail", seasonDetail);

            System.out.println("Timer[" + timerName + "] - After getting Season Detail : " + AuTimer.elapsedTime(timerName));
            // Nav links
            int startingRowNum = FSUtils.getIntRequestParameter(request,"start",1);
            System.out.println("Timer[" + timerName + "] - After getting startingRowNum : " + AuTimer.elapsedTime(timerName));
            request.setAttribute("startingRowNum",startingRowNum);
            System.out.println("Timer[" + timerName + "] - After setting startingRowNum attr : " + AuTimer.elapsedTime(timerName));
            int playerID = FSUtils.getIntRequestParameter(request, "pid", 0);
            System.out.println("Timer[" + timerName + "] - After getting playerID : " + AuTimer.elapsedTime(timerName));

            int reqFSSeasonWeekId = FSUtils.getIntRequestParameter(request, "weekid", 0);
//            _DisplayFSSeasonWeek = (FSSeasonWeek)_Session.getHttpSession().getAttribute("fantasyDisplayWeek");
//          _Session.getHttpSession().setAttribute("fsseasonweek",currFSSeasonWeek);
            // Retrieve all of the FSSeasonWeek's for the entire FSSeason
//            List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(_FSTeam.getFSLeague().getFSSeasonID());
//
//            // See if we can find the week in the allWeeks variable
//            FSSeasonWeek prevWeek = null;
//            for (FSSeasonWeek week : allWeeks) {
//
//                // The requested week takes precedence
//                if (week.getFSSeasonWeekID() == reqFSSeasonWeekId) {
//                    _DisplayFSSeasonWeek = week;
//                }
//
//                if (week.getStatus().equals(FSSeasonWeek.Status.CURRENT.toString()) && _CurrentFSSeasonWeek == null) {
//                    _CurrentFSSeasonWeek = week;
//                }
//
//                if (week.getWeekType().equals(FSSeasonWeek.WeekType.INITIAL.toString()) && _DisplayFSSeasonWeek == null) {
//                    _DisplayFSSeasonWeek = week;
//                }
//
//                // If we get to the final week then set it to this as long as the week hasn't been found and the session object didn't have anything (null)
//                if (week.getWeekType().equals(FSSeasonWeek.WeekType.FINAL.toString()) && _DisplayFSSeasonWeek == null) {
//                    _DisplayFSSeasonWeek = week;
//                }
//            }
//
//            // Set it to be the current week as long as the session object didn't have anything (null)
//            if (_DisplayFSSeasonWeek == null)
//            {
//                _DisplayFSSeasonWeek = _CurrentFSSeasonWeek;
//            }
            // retrieve league standings

    //        displayWeek = fsseason.getCurrentFSSeasonWeeks().get(new Integer(currentWeekNo-1));
            _CurrentFSSeasonWeek = (FSSeasonWeek) _Session.getHttpSession().getAttribute("salfsseasonweek");
            _DisplayFSSeasonWeek = (FSSeasonWeek) _Session.getHttpSession().getAttribute("saldisplayfsseasonweek");
            request.setAttribute("salDisplayWeek", _DisplayFSSeasonWeek);
            request.setAttribute("salCurrentWeek", _CurrentFSSeasonWeek);

            if (playerID > 0) {

                addPlayer(salarycap, _Session, playerID,_CurrentFSSeasonWeek.getFSSeasonWeekID(), true);
                System.out.println("Timer[" + timerName + "] - After adding Player to session : " + AuTimer.elapsedTime(timerName));
//                String temp = updateRoster(_Session);
//                if (!temp.equals("")) {
//                    return temp;
//                }

            }

            System.out.println("Timer[" + timerName + "] - After checking if we should add player : " + AuTimer.elapsedTime(timerName));
            int dropPlayer = FSUtils.getIntRequestParameter(request, "dip", 0);
            if (dropPlayer > 0) {
                dropPlayer(salarycap, _Session, dropPlayer);

//                String temp = updateRoster(_Session);
//                if (!temp.equals("")) {
//                    return temp;
//                }
            }

            System.out.println("Timer[" + timerName + "] - After dropping player : " + AuTimer.elapsedTime(timerName));
            if (dropPlayer <= 0 && playerID <= 0) {
                if (!teamIsUnderSalaryCap(salarycap, _Session)) {
                    _Session.setErrorMessage("Warning: You are now over the salary cap. You will be unable to Post until total team salary is reduced.");
                }
            }

//            User user = session.getUser();
            List<FSPlayerValue> list = null;
//            List<FSPlayerValue> curRosterList = getRosterListFromSession(_Session);
            List<FSPlayerValue> curRosterList = new ArrayList<FSPlayerValue>();

            System.out.println("Timer[" + timerName + "] - After getting roster from session : " + AuTimer.elapsedTime(timerName));
            // If the player list from the session is empty, then check
            // to see if this team already has players from the Database.
            if (curRosterList.size() < 1) {
                List<FSRoster> roster = _FSTeam.getRoster(_CurrentFSSeasonWeek.getFSSeasonWeekID());

                // clear out session info
                _Session.getHttpSession().removeAttribute("QB1");
                _Session.getHttpSession().removeAttribute("RB1");
                _Session.getHttpSession().removeAttribute("RB2");
                _Session.getHttpSession().removeAttribute("WR1");
                _Session.getHttpSession().removeAttribute("WR2");
                _Session.getHttpSession().removeAttribute("TE1");
                _Session.getHttpSession().removeAttribute("PK1");

                System.out.println("Timer[" + timerName + "] - After getting Roster : " + AuTimer.elapsedTime(timerName));
//                if (roster.size() < 1) {
//                    // Try to get this team's players from last week
//                    String uselastweek = FSUtils.getRequestParameter(request, "roster");
//                    if (uselastweek != null && uselastweek.equals("last")) {
////                        AuDate lastweek = ((WeekInfo)session.getHttpSession().getAttribute("standingsWeekInfo")).getStartDate();
////                        players = team.getRoster(lastweek);
//                    }
//                }
                for (FSRoster player : roster) {
                    addPlayer(salarycap, _Session, player.getPlayerID(),_CurrentFSSeasonWeek.getFSSeasonWeekID(), false);
                    System.out.println("Timer[" + timerName + "] - After adding a player on roster to the session : " + AuTimer.elapsedTime(timerName));
                }
            }

            System.out.println("Timer[" + timerName + "] - After checking roster size : " + AuTimer.elapsedTime(timerName));
            try {
                list = Player.getPlayerValues(_CurrentFSSeasonWeek, position.getPositionID(), sort, curRosterList);
            } catch (Exception e) {
                e.printStackTrace();
                CTApplication._CT_LOG.error(_Session.getHttpSession(),e);
            }
            System.out.println("Timer[" + timerName + "] - After getting player values : " + AuTimer.elapsedTime("FSPlayerValues"));
            _Session.getHttpSession().setAttribute("playerSalaries",list);
            _Session.getHttpSession().removeAttribute("teamName");

        } catch (Exception e) {
            e.printStackTrace();
            CTApplication._CT_LOG.error(e);
        }

        return page;
    }

    public static String resolveSortColumn(String sortParam) {
        String[] cols = { "p.LastName", "Team$TeamName", "TotalFootballStats$AvgFantasyPts", "pv.Value" };
        String[] sortParamArr = sortParam.split("_");
        int sortColNo = 0;
        try {
            sortColNo = Integer.parseInt(sortParamArr[0]);
        }
        catch (NumberFormatException e) {}

        String ret = null;
        if (sortColNo==0) {
            ret = "pv.Value, TotalFootballStats$AvgFantasyPts desc ";
        } else {
            ret = cols[sortColNo-1];
            if (sortParamArr.length>1) {
                if (sortParamArr[1].compareToIgnoreCase("a")==0 && !ret.endsWith(" asc")) {
                    ret += " asc";
                } else if (sortParamArr[1].compareToIgnoreCase("d")==0 && !ret.endsWith(" desc")) {
                    ret += " desc";
                }
            }
            ret += ",TotalFootballStats$AvgFantasyPts desc ";
        }

        return ret;
    }

    public static String updateRoster(UserSession session) {

        String nextPage = "";

//        RoomView room = (RoomView)session.getHttpSession().getAttribute("postRoom");

        List<FSPlayerValue> playerValues = getRosterListFromSession(session);
        CTReturnCode rc = CTReturnCode.RC_SUCCESS;
//        FSReturnCode rc = User.checkPostConditions(room, players);
//        if (rc.getReturnType() != ReturnType.SUCCESS) {
//            session.setErrorMessage("Error: " + rc.getReturnType().getDefaultErrorMessage());
//            return nextPage;
//        }

        FSUser user = session.getLoggedInUser();
        if (user==null) {
            session.setErrorMessage("You must first login or create a user account.");
            return "/topdawg/register.htm";
        }

        FSTeam team = session.getLoggedInTeam();

        if (team == null) {
            session.setErrorMessage("You must first login or create a user account.");
            return "/topdawg/register.htm";
        }

        FSSeasonWeek currFSSeasonWeek = session.getCurrentSalWeek();
        try {
            // from the list of players, create a list of FSRoster
            List<FSRoster> roster = new ArrayList<FSRoster>();
            for (FSPlayerValue tempplayerValue : playerValues) {
                FSRoster temproster = new FSRoster();
                temproster.setActiveState("active");
                temproster.setStarterState("starter");
                temproster.setFSSeasonWeekID(currFSSeasonWeek.getFSSeasonWeekID());
                temproster.setFSTeamID(team.getFSTeamID());
                temproster.setPlayerID(tempplayerValue.getPlayerID());
                roster.add(temproster);
            }
            rc = team.insertOrUpdateRoster(currFSSeasonWeek.getFSSeasonWeekID(), roster);
//            team = new FSTeam(team.getFSTeamID());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(session.getHttpSession(), e);
            rc = CTReturnCode.RC_DB_ERROR;
        }

        if (rc.getReturnType() != CTReturnType.SUCCESS) {
            session.setErrorMessage("Error: " + rc.getReturnType().getDefaultErrorMessage());
            return nextPage;
        }

        return nextPage;
    }

    private void addPlayer(Long salarycap, UserSession session, int playerID, int fsseasonweekid, boolean insertPlayer) {
        try {
            Player player = Player.getInstance(playerID);
            FSPlayerValue playerValue = player.getFSPlayerValue(fsseasonweekid);
            if (player==null) {
                session.setErrorMessage("PlayerID not recognized. Please try adding a legal player.");
                return;
            }

            Position pos = player.getPosition();
            FSSeasonWeek currFSSeasonWeek = (FSSeasonWeek)session.getHttpSession().getAttribute("salfsseasonweek");

            FSFootballRosterPositions rosterPosition = new FSFootballRosterPositions(currFSSeasonWeek.getFSSeasonID(),player.getPositionID());

            for (int i=1; i<=rosterPosition.getMaxNum(); i++) {
                String attrName = pos.getPositionName() + i;
                FSPlayerValue existingValue = (FSPlayerValue)session.getHttpSession().getAttribute(attrName);
                if (existingValue==null) {
                    if (!playerIsUnderSalaryCap(salarycap, session, player)) {
                        session.setErrorMessage("Warning: You are now over the salary cap. You will be unable to complete your team until total team salary is reduced.");
                    }
                    session.getHttpSession().setAttribute(attrName, playerValue);

                    if (insertPlayer)
                    {
                        StringBuilder sql = new StringBuilder();
                        sql.append("insert into FSRoster(FSTeamID,PlayerID,FSSeasonWeekID,StarterState, ");
                        sql.append(" ActiveState) ");
                        sql.append(" values (").append(this._FSTeam.getFSTeamID());
                        sql.append(", ").append(player.getPlayerID());
                        sql.append(",").append(currFSSeasonWeek.getFSSeasonWeekID());
                        sql.append(",'").append("starter");
                        sql.append("','").append("active").append("')");

                        int result = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

                        System.out.println("Adding player");
                    }

                    return;
                } else if (insertPlayer && existingValue.getPlayer().getPlayerID() == player.getPlayerID()) { // Note: there is a tiny crack in the algorithm that could still allow for a duplicate player on the roster, but the db will still not allow it, so we don't worry about it
                    session.setErrorMessage("Error: " + player.getFullName() + " is already on your roster");
                    return;
                }
            }

            session.setErrorMessage("Your roster is full at " + pos.getPositionNameLong() + ". " +
                                    "Either drop a " + pos.getPositionNameLong() + " or add a player at a different position.");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void dropPlayer(Long salarycap, UserSession session, int playerID) throws Exception {
        Player player = Player.getInstance(playerID);
        if (player==null) {
            session.setErrorMessage("PlayerID not recognized. Please try dropping a legal player.");
            return;
        }

        Position pos = player.getPosition();
        FSSeasonWeek currFSSeasonWeek = (FSSeasonWeek)session.getHttpSession().getAttribute("salfsseasonweek");

        FSFootballRosterPositions rosterPosition = new FSFootballRosterPositions(currFSSeasonWeek.getFSSeasonID(),player.getPositionID());

        for (int i=rosterPosition.getMaxNum(); i>0; i--) {
            String attrName = pos.getPositionName() + i;
            try {
                FSPlayerValue existingValue = (FSPlayerValue)session.getHttpSession().getAttribute(attrName);
                if (existingValue != null && existingValue.getPlayerID()==playerID) {
                    double existingsal = getCreatingTeamSalaryCap(session);
                    FSPlayerValue playerValue = player.getFSPlayerValue(currFSSeasonWeek.getFSSeasonWeekID());
                    double playersal = playerValue.getValue();
                    if (!(existingsal - playersal <= salarycap)) {
                        session.setErrorMessage("Warning: You are still over the salary cap. You will be unable to Post until total team salary is reduced.");
                    }
                    session.getHttpSession().removeAttribute(attrName);

                    StringBuilder sql = new StringBuilder();
                    sql.append(" DELETE FROM FSRoster ");
                    sql.append(" WHERE FSTeamID = ").append(this._FSTeam.getFSTeamID());
                    sql.append(" AND PlayerID = ").append(player.getPlayerID());
                    sql.append(" AND FSSeasonWeekID = ").append(currFSSeasonWeek.getFSSeasonWeekID());

                    int result = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

                    return;
                }
            }
            catch (NumberFormatException e) {}
        }

        session.setErrorMessage(player.getFullName() + " is not on your roster.");

    }

    static boolean playerIsUnderSalaryCap(Long salarycap, UserSession session, Player addPlayer) {

        FSSeasonWeek currFSSeasonWeek = (FSSeasonWeek)session.getHttpSession().getAttribute("salfsseasonweek");

        double existing = getCreatingTeamSalaryCap(session);
        FSPlayerValue playerValue = addPlayer.getFSPlayerValue(currFSSeasonWeek.getFSSeasonWeekID());
        double playersal = playerValue.getValue();
        return (existing + playersal) <= salarycap;
    }

    static boolean teamIsUnderSalaryCap(Long salarycap, UserSession session) {
        boolean under = true;
        double existing = getCreatingTeamSalaryCap(session);
        System.out.println("Existing salary : " + existing);
        if (existing <= salarycap) {
            under = true;
        } else {
            under = false;
        }
        return under;
    }

    static double getCreatingTeamSalaryCap(UserSession session) {
        Collection<Position> allPositions = Position.getAllPositions();
        double totalSalaries=0;

        FSSeasonWeek currFSSeasonWeek = session.getCurrentSalWeek();

        for (Position pos : allPositions) {
            FSFootballRosterPositions rosterPosition = new FSFootballRosterPositions(currFSSeasonWeek.getFSSeasonID(),pos.getPositionID());

            if (rosterPosition != null)
            {
                for (int i=1; i<=rosterPosition.getMaxNum(); i++) {
                    String attrName = pos.getPositionName() + i;
                    FSPlayerValue playerValue = (FSPlayerValue)session.getHttpSession().getAttribute(attrName);

                    if (playerValue!=null) {
                        double playersal = playerValue.getValue();
                        totalSalaries+=playersal;
                    }
                }
            }
        }

        return totalSalaries;

    }
    static List<FSPlayerValue> getRosterListFromSession(UserSession session) {
        Collection<Position> allPositions = Position.getAllPositions();
        List<FSPlayerValue> ret = new ArrayList<FSPlayerValue>();

        FSSeasonWeek currFSSeasonWeek = session.getCurrentSalWeek();

        for (Position pos : allPositions) {
            FSFootballRosterPositions rosterPosition = new FSFootballRosterPositions(currFSSeasonWeek.getFSSeasonID(),pos.getPositionID());
            for (int i=1; i<=rosterPosition.getMaxNum(); i++) {
                String attrName = pos.getPositionName() + i;
                FSPlayerValue playerValue = (FSPlayerValue)session.getHttpSession().getAttribute(attrName);
                if (playerValue!=null) {
                    try {
                        ret.add(playerValue);
                    }
                    catch (NumberFormatException e) { }
                }
            }
        }

        return ret;
    }

    static void clearRosterListFromSession(UserSession session) {
        Collection<Position> allPositions = Position.getAllPositions();
        HttpSession httpSession = session.getHttpSession();
        FSSeasonWeek currFSSeasonWeek = session.getCurrentSalWeek();

        for (Position pos : allPositions) {
            FSFootballRosterPositions rosterPosition = new FSFootballRosterPositions(currFSSeasonWeek.getFSSeasonWeekID(),pos.getPositionID());

            for (int i=1; i<=rosterPosition.getMaxNum(); i++) {
                String attrName = pos.getPositionName() + i;
                httpSession.removeAttribute(attrName);
            }
        }

//        httpSession.removeAttribute("teamName");
//        httpSession.removeAttribute("teamMessage");
//        httpSession.removeAttribute("postAmount");
    }

}
