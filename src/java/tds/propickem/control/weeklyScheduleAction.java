package tds.propickem.control;

import bglib.util.FSUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.control.BaseAction;
import tds.main.bo.*;
import tds.propickem.bo.ProPickem;

public class weeklyScheduleAction extends BaseAction {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        FSTeam fsTeam = null;
        FSSeasonWeek nflPickemDisplayWeek = null;
        UserSession session = null;
        int teamPickedID = 0;
        int gameId = 0;

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        session = UserSession.getUserSession(request, response);

        nextPage = "weeklySchedule";

        teamPickedID = FSUtils.getIntRequestParameter(request,"teamPickedId", 0);
        gameId = FSUtils.getIntRequestParameter(request,"gameId", 0);
        fsTeam = (FSTeam)session.getHttpSession().getAttribute("fsteam");
        nflPickemDisplayWeek = (FSSeasonWeek)session.getHttpSession().getAttribute("nflPickemDisplayWeek");

        if (teamPickedID == 0 || gameId == 0 || fsTeam == null || nflPickemDisplayWeek == null) {
            // Reload the page, throw an error, or email me something???
            return nextPage;
        }

        try {
            ProPickem.SavePick(nflPickemDisplayWeek.getFSSeasonWeekID(), fsTeam.getFSTeamID(), gameId, teamPickedID);

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            session.setErrorMessage("Error processing team pick.");
        }

        return nextPage;
    }
}