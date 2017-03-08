/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.golf.control;

import bglib.util.AuDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.*;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class choosePlayersView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;

        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();
        
        try {
            if (_CurrentFSSeasonWeek == null)
            {
                userSession.setErrorMessage("Error : there is no tournament set for this week.");
                return page;
            }
            
            // get Current Tournament
            PGATournamentWeek tournamentWeek = PGATournamentWeek.getTournamentWeek(_CurrentFSSeasonWeek.getFSSeasonWeekID());
            if (tournamentWeek == null)
            {
                userSession.setErrorMessage("Error : there is no tournament set for this week.");
                return page;
            }
            
            boolean deadlinePassed = tournamentWeek.getFSSeasonWeek().GetStartersDeadlineHasPassed();
            AuDate today = new AuDate();
            request.setAttribute("today", today);
            request.setAttribute("deadlinePassed", deadlinePassed);
            session.setAttribute("tournamentWeek", tournamentWeek);
            
            // get the team's current roster
            List<FSRoster> curRosterList = _FSTeam.getRoster(_CurrentFSSeasonWeek.getFSSeasonWeekID());
            request.setAttribute("yourRoster", curRosterList);
            
            List<PGATournamentWeekPlayer> curPlayerValues = new ArrayList<PGATournamentWeekPlayer>();
            
            for (FSRoster roster : curRosterList) {
                PGATournamentWeekPlayer temp = new PGATournamentWeekPlayer(tournamentWeek.getPGATournamentID(), _CurrentFSSeasonWeek.getFSSeasonWeekID(), roster.getPlayerID());
                curPlayerValues.add(temp);
                
                roster.setPGATournamentWeekPlayer(temp);
            }            
            
            // get the list of players for this week
            List<PGATournamentWeekPlayer> weekPlayers = tournamentWeek.GetField(curPlayerValues, null);
            request.setAttribute("weekPlayers", weekPlayers);
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }

        return page;
    }
    
}
