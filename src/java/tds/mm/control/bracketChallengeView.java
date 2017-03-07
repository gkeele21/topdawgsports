package tds.mm.control;

import bglib.util.FSUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.bo.FSTeam;
import tds.main.control.BaseTeamView;
import tds.mm.bo.*;

public class bracketChallengeView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        // If nextPage something that means an error happened
        if (nextPage != null) { return nextPage; }
        
        nextPage = htmlPage;
        
        try {
            FSTeam displayTeam = null;
            
            // Get request/session objects
            int reqFSTeamId = FSUtils.getIntRequestParameter(request, "dtid", 0);
            
            // Get the current tournament
            MarchMadnessTournament leagueTournament = new MarchMadnessLeague(_FSTeam.getFSLeagueID()).getTournament();
                        
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
            
            // Retrieve all of the games to display
            List<BracketChallenge> picks = BracketChallenge.GetPicks(leagueTournament.getTournamentID(), displayTeam.getFSTeamID(), 0);
            
            // Retrieve the standings for each round for the user team that we're displaying
            Map<Integer, Integer> roundStandings = BracketChallengeStandings.GetAllRoundStandingsForFSTeam(leagueTournament.getTournamentID(), displayTeam.getFSTeamID());
            
            // Figure out the  college teams that are out of the tournament
            List<MarchMadnessTeamSeed> teamSeeds = MarchMadnessTeamSeed.GetTournamentTeams(leagueTournament.getTournamentID());            
            Map<Integer, MarchMadnessTeamSeed> defeatedTeams = new HashMap<Integer, MarchMadnessTeamSeed>();
            
            for (MarchMadnessTeamSeed team : teamSeeds) {                
                if (team.getStatus().equals(MarchMadnessTeamSeed.Status.OUT.toString())) { defeatedTeams.put(team.getTeamID(), team); }
            }

            request.setAttribute("leagueTournament",leagueTournament);
            request.setAttribute("allLeagueTeams",allLeagueTeams);
            request.setAttribute("displayTeam",displayTeam);
            request.setAttribute("picks",picks);
            request.setAttribute("roundStandings",roundStandings);
            request.setAttribute("defeatedTeams",defeatedTeams);
        
        } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
        } 

        return nextPage;

    }
}