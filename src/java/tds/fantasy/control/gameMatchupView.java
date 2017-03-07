/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.fantasy.control;

import bglib.util.FSUtils;
import tds.main.bo.FSFootballMatchup;
import tds.main.bo.FSLeague;
import tds.main.bo.FSRoster;
import tds.main.bo.FSSeasonWeek;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class gameMatchupView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;

        FSLeague league = _FSTeam.getFSLeague();

        int matchupID = FSUtils.getIntRequestParameter(request, "id", 0);

        FSFootballMatchup matchup = null;
        if (matchupID < 1) {
            // check for weekno and game to be passed in
            int weekNo = FSUtils.getIntRequestParameter(request, "wk", 0);
            int gameNo = FSUtils.getIntRequestParameter(request, "game", 0);
            
            if (weekNo < 1 || gameNo < 1) {
                _Session.setErrorMessage("Please select a valid Game Matchup.");
                return page;
            }
            // TODO: grab the fsseasonweekid based on the fsleagueid and weekno
            // I'm lucky now because the weekno matches the FSSeasonWeekID but
            // that won't last past this first season.
            int lgid = league.getFSLeagueID();
            int fsseasonweekid = FSSeasonWeek.GetFSSeasonWeekID(lgid, weekNo);
            
            matchup = new FSFootballMatchup(lgid,fsseasonweekid,gameNo);
        } else {
            matchup = new FSFootballMatchup(matchupID);    
        }
        
        request.setAttribute("matchup",matchup);
        
        // retrieve team1's roster
        List<FSRoster> team1roster = matchup.getFSTeam1().getRoster(matchup.getFSSeasonWeekID(), "active");
        request.setAttribute("team1roster",team1roster);
                
        // retrieve team2's roster
        List<FSRoster> team2roster = matchup.getFSTeam2().getRoster(matchup.getFSSeasonWeekID(), "active");
        request.setAttribute("team2roster",team2roster);
        
        // retrieve # of league games per week
        int numgames = matchup.getFSTeam1().getFSLeague().getNumTeams() / 2;
        request.setAttribute("numgames",numgames);
        
        return page;
    }
    
}
