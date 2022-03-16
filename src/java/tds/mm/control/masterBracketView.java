package tds.mm.control;

import tds.main.bo.CTApplication;
import tds.main.bo.FSTeam;
import tds.main.bo.UserSession;
import tds.main.control.BaseTeamView;
import tds.mm.bo.MarchMadnessGame;
import tds.mm.bo.MarchMadnessLeague;
import tds.mm.bo.MarchMadnessTournament;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

            _Session = UserSession.getUserSession(request, response);

            // Check that the FSTeam was created
            FSTeam _FSTeam = (FSTeam)_Session.getHttpSession().getAttribute("fsteam");

            if (_FSTeam != null) {
                FSTeam displayTeam = _FSTeam;

                // Get the current tournament
                MarchMadnessTournament leagueTournament = new MarchMadnessLeague(_FSTeam.getFSLeagueID()).getTournament();

                request.setAttribute("leagueTournament",leagueTournament);
                request.setAttribute("displayTeam",displayTeam);
            }

        } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
        }

        return nextPage;

    }
}
