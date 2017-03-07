package tds.collegepickem.control;

import bglib.util.FSUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.collegepickem.bo.CollegePickem;
import tds.main.bo.*;
import tds.main.control.BaseTeamView;

public class weeklyScheduleView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
    
        String nextPage = super.process(request,response);

        // If nextPage is something that means an error happened
        if (nextPage != null) { return nextPage; }
        
        nextPage = htmlPage;
                
        try {
            FSTeam displayTeam = null;
            FSSeasonWeek displayWeek = null;
            boolean showConfidencePts = false;
            
            // Get request/session objects
            int reqFSTeamId = FSUtils.getIntRequestParameter(request, "dtid", 0);
            int reqFSSeasonWeekId = FSUtils.getIntRequestParameter(request, "wk", 0);
                   
            // Get all the teams in the league based off the FSTeam (user logged in)
            List<FSTeam> allLeagueTeams = FSTeam.GetLeagueTeams(_FSTeam.getFSLeagueID());
            
            // Figure out the display team
            if (reqFSTeamId > 0) {
                for (FSTeam team : allLeagueTeams) {
                    if (team.getFSTeamID() == reqFSTeamId) {
                        displayTeam = team;
                    }            
                }                
            }
                
            else { if (displayTeam == null) { displayTeam = _FSTeam; } }
          
            // Retrieve all of the FSSeasonWeek's for the entire FSSeason
            List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(displayTeam.getFSLeague().getFSSeasonID());
            
            // See if we can find the week in the allWeeks variable
            for (FSSeasonWeek week : allWeeks) {
                
                // The requested week takes precedence
                if (week.getFSSeasonWeekID() == reqFSSeasonWeekId) {
                    displayWeek = week;
                    break;
                }
                
                // Set it to be the current week
                if (week.getStatus().equals(FSSeasonWeek.Status.CURRENT.toString())) {
                    displayWeek = week;
                }
                
                // If we get to the final week and the display week was never set (Current Week)
                if (week.getWeekType().equals(FSSeasonWeek.WeekType.FINAL.toString()) && displayWeek == null) {
                    displayWeek = week;
                }
            }

            // Get the weekly schedule and user picks
            Map<Integer, Integer> assignedPts = new HashMap<Integer, Integer>();
            List<CollegePickem> picks = CollegePickem.GetWeeklyPicks(displayWeek.getFSSeasonWeekID(), displayTeam.getFSTeamID());
            
            // Make a list of all the assigned pts used thus far
            for (int i=0; i < picks.size(); i++) {
                if (picks.get(i).getConfidencePts() > 0 ) {
                    assignedPts.put(picks.get(i).getConfidencePts(), picks.get(i).getConfidencePts());
                }
            }
            
            // Retrieve the Standings for the prior week
            List<Standings> priorStandings = null;
            Map<Integer, Standings> weekStandings = new HashMap();

            SeasonWeek standingsWeek = SeasonWeek.GetPriorSeasonWeek(displayWeek.getSeasonWeek());
                        
            if (standingsWeek != null) {                
                priorStandings = Standings.GetStandings(standingsWeek.getSeasonWeekID());
                for (int i=0; i < priorStandings.size(); i++) {
                    weekStandings.put(priorStandings.get(i).getTeamID(), priorStandings.get(i));
                }
            }
            
            // Retrieve the Top 25 teams
            List<Standings> standings = Standings.GetTop25Rankings(displayWeek.getSeasonWeekID());
            Map<Integer, Standings> apRankings = new HashMap();
            
            for (int i=0; i < standings.size(); i++) {
                apRankings.put(standings.get(i).getTeamID(), standings.get(i));
            }
            
            // Retrieve the FSTeam's standings for the week as long as the week is finished
            FSFootballStandings fsStandings = null;
            if(displayWeek.getStatus().equals(FSSeasonWeek.Status.COMPLETED.toString())) {
                fsStandings = FSFootballStandings.GetWeekStandingsForFSTeam(displayWeek.getFSSeasonWeekID(), displayTeam.getFSTeamID());
            }

            // Retrieve the bye teams
            List<Game> byeTeams = Game.GetByeTeams(displayWeek.getSeasonWeekID(), true);
            
            // Determine if we need to show confidence points sections - Currently setting isCustomLeague to true for leagues that don't want to use conf pts.
            if (!displayWeek.getStatus().equals(FSSeasonWeek.Status.COMPLETED.toString()) && _FSTeam.getFSTeamID().equals(displayTeam.getFSTeamID())
                    && (displayTeam.getFSLeague().getIsCustomLeague() == null || displayTeam.getFSLeague().getIsCustomLeague() != 1)) { 
                showConfidencePts = true; 
            } 
            
            // Set all the request / session objects
            request.setAttribute("displayWeek",displayWeek);
            request.setAttribute("displayTeam",displayTeam);
            request.setAttribute("showConfidencePts", showConfidencePts);
            request.setAttribute("allLeagueTeams",allLeagueTeams);
            request.setAttribute("allWeeks",allWeeks);          
            request.setAttribute("picks",picks);
            request.setAttribute("weekStandings",weekStandings);
            request.setAttribute("assignedPts",assignedPts);
            request.setAttribute("apRankings",apRankings);
            request.setAttribute("fsStandings",fsStandings);
            request.setAttribute("byeTeams",byeTeams);            
      
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }

        return nextPage;
    }
}
