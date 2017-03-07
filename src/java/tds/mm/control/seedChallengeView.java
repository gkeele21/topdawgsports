package tds.mm.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.bo.FSTeam;
import tds.main.control.BaseTeamView;
import tds.mm.bo.*;

public class seedChallengeView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        
        String nextPage = super.process(request,response);

        // If nextPage is something that means an error happened
        if (nextPage != null) { return nextPage; }
        
        nextPage = htmlPage;
        
        try {
            FSTeam displayTeam = null;
            int teamsRemaining = 0;
            
            // Get request/session objects
            int reqSeedGroupId = FSUtils.getIntRequestParameter(request, "sg", 0);
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

            // Retrieve the user's picks
            List<SeedChallenge> picks = SeedChallenge.GetSeedChallengePicks(leagueTournament.getTournamentID(), displayTeam.getFSTeamID());
            
            // Figure out how many teams the user has left in the game
            for (SeedChallenge pick : picks) {
                // Ensure a pick was made
                if (pick.getTeamSeedPickedID() != null) {
                    if (pick.getTeamSeedPicked().getStatus().equals(MarchMadnessTeamSeed.Status.IN.toString())) {
                        teamsRemaining += 1;
                    }
                }            
            }

            // Retrieve all of the seed groups            
            List<SeedChallengeGroup> seedGroups = SeedChallengeGroup.GetSeedGroups(leagueTournament.getTournamentID());


            // If the user didn't click on one of the seed group links then just default getting the first seed group
            if (reqSeedGroupId == 0) { reqSeedGroupId = seedGroups.get(0).getSeedChallengeGroupID(); }

            // Retrieve all of the available teams for the seed group
            List<MarchMadnessTeamSeed> availableTeams = MarchMadnessTeamSeed.GetMarchMadnessTeamsBySeedGroup(leagueTournament.getTournamentID(), reqSeedGroupId);

            request.setAttribute("leagueTournament",leagueTournament);
            request.setAttribute("allLeagueTeams",allLeagueTeams);
            request.setAttribute("displayTeam",displayTeam);
            request.setAttribute("picks",picks);
            request.setAttribute("teamsRemaining",teamsRemaining);
            request.setAttribute("seedGroups",seedGroups);
            request.setAttribute("reqSeedGroupId",reqSeedGroupId);
            request.setAttribute("availableTeams",availableTeams);
            
        } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
        } 

        return nextPage;

    }
}