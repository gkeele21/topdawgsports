/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.fantasy.control;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.*;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class teamWeekMatchupsView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;

        // retrieve Tenman schedule
        FSLeague tenmanLeague = new FSLeague(9);
        List<FSFootballMatchup> schedule = tenmanLeague.GetResults(_CurrentFSSeasonWeek.getFSSeasonWeekID());
        FSFootballMatchup tenmanMatchup = null;
        for (FSFootballMatchup matchup : schedule) {
            if (matchup.getFSTeam1().getFSUserID() == _FSTeam.getFSUserID() || matchup.getFSTeam2().getFSUserID() == _FSTeam.getFSUserID()) {
                tenmanMatchup = matchup;
            }
        }
        
        request.setAttribute("tenmanMatchup",tenmanMatchup);
        
        if (tenmanMatchup != null) {
            // retrieve team1's roster
            List<FSRoster> tenmanTeam1roster = tenmanMatchup.getFSTeam1().getRoster(tenmanMatchup.getFSSeasonWeekID());
            request.setAttribute("tenmanTeam1roster",tenmanTeam1roster);

            // retrieve team2's roster
            List<FSRoster> tenmanTeam2roster = tenmanMatchup.getFSTeam2().getRoster(tenmanMatchup.getFSSeasonWeekID());
            request.setAttribute("tenmanTeam2roster",tenmanTeam2roster);
        }
        
        // retrieve Keeper schedule
        FSLeague keeperLeague = new FSLeague(10);
        schedule = keeperLeague.GetResults(_CurrentFSSeasonWeek.getFSSeasonWeekID());
        FSFootballMatchup keeperMatchup = null;
        for (FSFootballMatchup matchup : schedule) {
            if (matchup.getFSTeam1().getFSUserID() == _FSTeam.getFSUserID() || matchup.getFSTeam2().getFSUserID() == _FSTeam.getFSUserID()) {
                keeperMatchup = matchup;
            }
        }
        
        request.setAttribute("keeperMatchup",keeperMatchup);
        
        if (keeperMatchup != null) {
            // retrieve team1's roster
            List<FSRoster> keeperTeam1roster = keeperMatchup.getFSTeam1().getRoster(keeperMatchup.getFSSeasonWeekID());
            request.setAttribute("keeperTeam1roster",keeperTeam1roster);

            // retrieve team2's roster
            List<FSRoster> keeperTeam2roster = keeperMatchup.getFSTeam2().getRoster(keeperMatchup.getFSSeasonWeekID());
            request.setAttribute("keeperTeam2roster",keeperTeam2roster);
        }
        
        // retrieve Salary Cap roster
        FSLeague salLeague = new FSLeague(11);

        FSSeason season = salLeague.getFSSeason();
        int currSalFSSeasonWeekID = season.getCurrentFSSeasonWeekID();
        FSSeasonWeek currSalFSSeasonWeek = new FSSeasonWeek(currSalFSSeasonWeekID);
        int currentWeekNo = currSalFSSeasonWeek.getSeasonWeek().getWeekNo();
        FSSeasonWeek displayWeek = null;
        if (currentWeekNo == 1) {
            displayWeek = currSalFSSeasonWeek;
        } else {
            displayWeek = season.GetCurrentFSSeasonWeeks().get(new Integer(currentWeekNo-1));
        }

        FSTeam salTeam = null;
        List<FSFootballStandings> lgStandings = salLeague.GetStandings(displayWeek.getFSSeasonWeekID(),"");
        for (FSFootballStandings standing : lgStandings) {
            if (standing.getFSTeam().getFSUserID() == _FSTeam.getFSUserID()) {
                salTeam = standing.getFSTeam();
            }
        }

        if (salTeam != null) {
            List<FSRoster> salRoster = salTeam.getRoster(currSalFSSeasonWeek.getFSSeasonWeekID());
            request.setAttribute("salRoster",salRoster);
        }

        return page;
    }
    
}
