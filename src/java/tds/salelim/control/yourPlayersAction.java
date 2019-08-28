package tds.salelim.control;

import tds.sal.control.*;
import tds.main.control.BaseAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Apr 24, 2006
 * Time: 11:39:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class yourPlayersAction extends BaseAction {

    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

//        nextPage = "createpost2.htm";
//        UserSession session = UserSession.getUserSession(request, response);
//        RoomView room = (RoomView)session.getHttpSession().getAttribute("postRoom");
//
//        String existingTeam = request.getParameter("SelExistingTeam");
//        if (existingTeam != null) {
//            try {
//                int startTeamID = Integer.parseInt(existingTeam);
//                if (startTeamID>0) {
//                    Team startTeam = new Team(startTeamID);
//                    createpost1View.clearRosterListFromSession(session);
//                    for (ProPlayer player : startTeam.getPlayers()) {
//                        createpost1View.addPlayerToSession(room, session, player.getProPlayerID());
//                    }
//                    session.getHttpSession().setAttribute("selTeamID", startTeamID);
//                }
//            } catch (Exception e) {
//                // no big deal
//            }
//            return nextPage;
//        }
//
//        String rostercomplete = request.getParameter("rostercomplete");
//        String rcx = request.getParameter("rostercomplete.x");
//        //String cl = request.getParameter("clearroster"); 
//        String clearx = request.getParameter("clearroster.x");
//
//        session.getHttpSession().removeAttribute("selTeamID");
//        List<ProPlayer> players = createpost1View.getRosterListFromSession(session);
//        FSReturnCode rc = User.checkPostConditions(room, players);
//        if (rc.getReturnType() != ReturnType.SUCCESS) {
//            session.setErrorMessage("Error: " + rc.getReturnType().getDefaultErrorMessage());
//            return nextPage;
//        }
//
//        User user = session.getUser();
//        if (user==null) {
//            session.setErrorMessage("You must first login or create a FREE user account.");
//            return "join.htm?origURL=createpost2.htm";
//        }
//
//        if (room==null) {
//            return "createpost2.htm?rostertype=Salary Cap";
//        }
//
//        Team team = null;
//        if (session.getHttpSession().getAttribute("validTeam") != null) {
//            team = (Team)session.getHttpSession().getAttribute("validTeam");
//        }
//
//        if (team == null) {
//            team = user.getTeam(null,room);
//        }
//
//        if (team == null) {
//            session.setErrorMessage("You must first login or create a FREE user account.");
//            return "join.htm?origURL=createpost2.htm";
//        }
//
//        try {
//            rc = team.insertOrUpdateRoster(null,room.getSeason().getCurrentPlayDate(), players);
//            team = new Team(team.getTeamID());
//        } catch (Exception e) {
//            Log.error(session,e);
//            rc = new FSReturnCode(DB_ERROR);
//        }
//
//        session.getHttpSession().setAttribute("validTeam",team);
//        if (rc.getReturnType() != ReturnType.SUCCESS) {
//            session.setErrorMessage("Error: " + rc.getReturnType().getDefaultErrorMessage());
//            return nextPage;
//        }
//
//        createpost1View.clearRosterListFromSession(session);
//        session.setErrorMessage("Successfully created your roster for this week.");

        return "standings.htm";

    }
}
