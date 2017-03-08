package tds.mm.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.control.BaseTeamView;
import tds.mm.bo.MarchMadnessLeague;
import tds.mm.bo.MarchMadnessTournament;

public class bracketSelectionView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        
        try {           
            // Get the current tournament
            MarchMadnessTournament leagueTournament = new MarchMadnessLeague(_FSTeam.getFSLeagueID()).getTournament();
                        
            if (leagueTournament.getStatus().equals(MarchMadnessTournament.Status.UPCOMING.toString())) {
                nextPage = "bracketChallengePicks.htm";
            }
            else {
                nextPage = "bracketChallenge.htm";
            }
        
        } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
        } 
        
        System.out.println("nextPage = "+nextPage);

        return nextPage;

    }
}