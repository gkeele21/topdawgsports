package tds.mm.control;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.control.BaseTeamView;
import tds.mm.bo.MarchMadnessLeague;
import tds.mm.bo.SeedChallenge;

public class seedChallengeAllPicksView extends BaseTeamView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        // If nextPage something that means an error happened
        if (nextPage != null) { return nextPage; }
        
        nextPage = htmlPage;
        
        try {
            List<SeedChallenge> picks = SeedChallenge.GetAllSeedChallengePicks(new MarchMadnessLeague(_FSTeam.getFSLeagueID()).getTournament().getTournamentID());
            
            request.setAttribute("picks",picks);
            
        } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
        } 

        return nextPage;
    }
}