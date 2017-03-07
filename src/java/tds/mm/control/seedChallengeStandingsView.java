package tds.mm.control;

import bglib.util.FSUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.FSTeam;
import tds.main.control.BaseTeamView;
import tds.mm.bo.MarchMadnessLeague;
import tds.mm.bo.MarchMadnessTournament;
import tds.mm.bo.SeedChallengeStandings;

public class seedChallengeStandingsView extends BaseTeamView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        
        String nextPage = super.process(request,response);

        // If nextPage something that means an error happened
        if (nextPage != null) { return nextPage; }

        nextPage = htmlPage;
        
        try {

            // Get request/session objects
            int reqFSSeasonWeekId = FSUtils.getIntRequestParameter(request, "fsSeasonWeekID", 0);
            
            MarchMadnessTournament leagueTournament = new MarchMadnessLeague(_FSTeam.getFSLeagueID()).getTournament();
            
            // Get all the teams in the league based off the FSTeam (user logged in)
            List<FSTeam> allLeagueTeams = FSTeam.GetLeagueTeams(_FSTeam.getFSLeagueID());
            
            // Retrieve all of the week's of the tournament
            List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(_FSTeam.getFSLeague().getFSSeasonID());  
            
            FSSeasonWeek displayWeek = new FSSeasonWeek();
            List<FSSeasonWeek> standingsWeeks = new ArrayList<FSSeasonWeek>();
            
            // See if we can find the week in the allWeeks variable
            for (FSSeasonWeek week : allWeeks) {
                
                // (Allowing CURRENT weeks on this page because standings can now be updated after each game without waiting for the round to finish)
                if (!week.getStatus().equals(FSSeasonWeek.Status.PENDING.toString())) {
                    standingsWeeks.add(week);                    
                }
                
                // The requested week takes precedence over all
                if (week.getFSSeasonWeekID() == reqFSSeasonWeekId) {
                    displayWeek = week;
                }
                
                // End the looping on the CURRENT week as long as display Week hasn't been set.  Could have been set via reqFSSeasonWeekId
                if (week.getStatus().equals(FSSeasonWeek.Status.CURRENT.toString()) && displayWeek.getFSSeasonWeekID() == null) {
                    displayWeek = week;
                    break;
                }
                
                // Past season's won't have a CURRENT week so if we're at the final week and the display week hasn't been set, show this week
                if (week.getWeekType().equals(FSSeasonWeek.WeekType.FINAL.toString()) && displayWeek.getFSSeasonWeekID() == null) {
                    displayWeek = week;
                }
            }
            
            // Grab the league standings
            List<SeedChallengeStandings> standings = SeedChallengeStandings.GetStandings(displayWeek.getFSSeasonWeekID());
            
            request.setAttribute("leagueTournament",leagueTournament);
            request.setAttribute("allLeagueTeams",allLeagueTeams);
            request.setAttribute("displayWeek", displayWeek);
            request.setAttribute("standingsWeeks",standingsWeeks);
            request.setAttribute("standings",standings);

        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }
        
        return nextPage;
    }
}