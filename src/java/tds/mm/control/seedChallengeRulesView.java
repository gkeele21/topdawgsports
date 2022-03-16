package tds.mm.control;

import tds.main.bo.CTApplication;
import tds.main.bo.FSTeam;
import tds.main.bo.UserSession;
import tds.main.control.BaseView;
import tds.mm.bo.MarchMadnessLeague;
import tds.mm.bo.MarchMadnessTournament;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class seedChallengeRulesView extends BaseView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        if (nextPage != null) { return nextPage; }

        nextPage = htmlPage;

        try {

            _Session = UserSession.getUserSession(request, response);

            // Check that the FSTeam was created
            FSTeam _FSTeam = (FSTeam)_Session.getHttpSession().getAttribute("fsteam");

            if (_FSTeam == null) {
                return nextPage;
            }

            FSTeam displayTeam = _FSTeam;

            // Get the current tournament
            MarchMadnessTournament leagueTournament = new MarchMadnessLeague(_FSTeam.getFSLeagueID()).getTournament();

            request.setAttribute("leagueTournament",leagueTournament);
            request.setAttribute("displayTeam",displayTeam);

        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }

        return nextPage;
    }
}
