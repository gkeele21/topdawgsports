package tds.fantasy.control;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.*;
import tds.main.control.BaseTeamView;

public class draftResultsView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
        
        System.out.println("Page : " + page);

        // Retrieve current week info
        System.out.println("FSTeamID : " + _FSTeam.getFSTeamID());
        FSLeague league = _FSTeam.getFSLeague();
        FSSeason fsseason = league.getFSSeason();
        
        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();

        List<FSFootballDraft> leagueDraft = FSFootballDraft.getDraftResults(league.getFSLeagueID());
        session.setAttribute("leagueDraft", leagueDraft);

        return page;
    }
    
}
