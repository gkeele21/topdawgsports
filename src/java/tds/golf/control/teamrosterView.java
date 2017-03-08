package tds.golf.control;

import bglib.util.FSUtils;
import java.util.ArrayList;
import tds.main.bo.FSRoster;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.FSTeam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import tds.main.bo.*;
import tds.main.control.BaseTeamView;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Apr 24, 2006
 * Time: 11:39:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class teamrosterView extends BaseTeamView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        nextPage = htmlPage;
        
        try {
            int teamid = FSUtils.getIntRequestParameter(request,"teamid",0);

            FSTeam team = new FSTeam(teamid);
            
            if (teamid > 0) {
                _Session.getHttpSession().setAttribute("rosterteam", team);
            }

            FSSeasonWeek fsSeasonWeek = (FSSeasonWeek) _Session.getHttpSession().getAttribute("golfdisplayfsseasonweek");
            int fsseasonweekid = fsSeasonWeek.getFSSeasonWeekID();
            List<FSRoster> teamRoster = team.getRoster(fsseasonweekid);
            PGATournamentWeek tournamentWeek = PGATournamentWeek.getTournamentWeek(fsseasonweekid);
            
//            List<PGATournamentWeekPlayer> curPlayerValues = new ArrayList<PGATournamentWeekPlayer>();
            
            for (FSRoster roster : teamRoster) {
                PGATournamentWeekPlayer temp = new PGATournamentWeekPlayer(tournamentWeek.getPGATournamentID(), fsseasonweekid, roster.getPlayerID());
//                curPlayerValues.add(temp);
                
                roster.setPGATournamentWeekPlayer(temp);
                System.out.println("Hello");
            }            

            request.setAttribute("tournamentWeek", tournamentWeek);
            request.setAttribute("teamRoster", teamRoster);
            request.setAttribute("rosterWeek", fsSeasonWeek);
            
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return nextPage;
    }

}
