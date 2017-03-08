package tds.collegepickem.control;

import bglib.util.FSUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.bo.FSFootballStandings;
import tds.main.bo.FSLeague;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.FSTeam;
import tds.main.control.BaseTeamView;

public class standingsView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        // If nextPage something that means an error happened
        if (nextPage != null) { return nextPage; }

        nextPage = htmlPage;
        
        try {
            
            FSLeague league = _FSTeam.getFSLeague();
            
            // Get request/session objects
            int reqFSSeasonWeekId = FSUtils.getIntRequestParameter(request, "wid", 0);
            
            // Get all the teams in the league based off the FSTeam (user logged in)
            List<FSTeam> allLeagueTeams = FSTeam.GetLeagueTeams(_FSTeam.getFSLeagueID());
            
            // Retrieve all of the FSSeasonWeek's for the entire FSSeason
            List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(_FSTeam.getFSLeague().getFSSeasonID());
            
            FSSeasonWeek displayWeek = new FSSeasonWeek();
            List<FSSeasonWeek> standingsWeeks = new ArrayList<FSSeasonWeek>();
            
            // See if we can find the week in the allWeeks variable
            for (FSSeasonWeek week : allWeeks) {
                
                // We will only show completed weeks on the Standings
                if (week.getStatus().equals(FSSeasonWeek.Status.COMPLETED.toString())) {
                    standingsWeeks.add(week);                    
                }
                
                // The requested week takes precedence over all
                if (week.getFSSeasonWeekID() == reqFSSeasonWeekId) {
                    displayWeek = week;
                }
                
                // End the looping on the Current week.  If display week wasn't set via reqFSSeasonWeekId then use the previous week
                if (week.getStatus().equals(FSSeasonWeek.Status.CURRENT.toString())) {
                    if (displayWeek.getFSSeasonWeekID() == null) { displayWeek = allWeeks.get(allWeeks.indexOf(week) - 1); }
                    break;
                }

                // Past season's won't have a Current week so if we're at the final week show this week
                if (week.getWeekType().equals(FSSeasonWeek.WeekType.FINAL.toString())) {
                    displayWeek = week;
                }
            }     
            
            // Grab the league standings
            List<FSFootballStandings> leagueStandings = league.GetStandings(displayWeek.getFSSeasonWeekID(), "s.TotalGamePoints desc, s.TotalGamesCorrect desc, s.GamesCorrect desc, t.teamName");

            request.setAttribute("allLeagueTeams",allLeagueTeams);
            request.setAttribute("displayWeek", displayWeek);
            request.setAttribute("standingsWeeks",standingsWeeks);
            request.setAttribute("leagueStandings",leagueStandings);            

        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }
        
        return nextPage;
    }
}

