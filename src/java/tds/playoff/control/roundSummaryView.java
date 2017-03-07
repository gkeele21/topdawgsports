package tds.playoff.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.control.BaseView;
import tds.main.bo.*;

public class roundSummaryView extends BaseView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        int fsTeamId = 0;
        String teamName = "";
        List<FSTournamentGame> gameResults = null;
        
        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        nextPage = htmlPage;
        
        fsTeamId = FSUtils.getIntRequestParameter(request, "tid", 0);
        
        if (fsTeamId > 0) {  

            // Retrieve the game results portion of the round summary
            try {
                //gameResults = FSTournamentGame.getGameResults(fsTeamId);
            } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
            }
        } else {
            // decide what to do
        }

        // Get the team Name to display
        teamName = FSTeam.getFSTeamName(fsTeamId);

        request.setAttribute("fsTeamId",fsTeamId);
        request.setAttribute("displayName",teamName);
        request.setAttribute("gameResults",gameResults);

        return nextPage;
    }
}