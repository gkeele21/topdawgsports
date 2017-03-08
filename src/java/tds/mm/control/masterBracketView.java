package tds.mm.control;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.control.BaseTeamView;
import tds.mm.bo.MarchMadnessGame;
import tds.mm.bo.MarchMadnessLeague;

public class masterBracketView extends BaseTeamView {
    
    static final int BEGINNING_ROUND_NUMBER = 1;
    static final int ENDING_ROUND_NUMBER = 6;

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        // If nextPage something that means an error happened
        if (nextPage != null) { return nextPage; }

        nextPage = htmlPage;

        try {           
            // Get all of the march madness games
            List<MarchMadnessGame> games = MarchMadnessGame.GetTournamentGames(new MarchMadnessLeague(_FSTeam.getFSLeagueID()).getTournament().getTournamentID(), BEGINNING_ROUND_NUMBER, ENDING_ROUND_NUMBER);
            
            request.setAttribute("games",games);
            
        } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
        }

        return nextPage;

    }
}