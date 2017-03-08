package tds.collegeloveleave.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.GamePicks;
import tds.main.control.BaseView;

public class ncaaLLPopupView extends BaseView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        List<GamePicks> otherUserPicks = null;
        FSSeasonWeek currentNCAALoveLeaveWeek = null;
        int fsTeamID = 0;

        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        nextPage = htmlPage;

        // Grab the request parameters        
        fsTeamID = FSUtils.getIntRequestParameter(request, "teamid", 0);
        
        if (fsTeamID == 0) {
            return nextPage;
        }

        currentNCAALoveLeaveWeek = (FSSeasonWeek)_Session.getHttpSession().getAttribute("currentNCAALoveLeaveWeek");

        // Retrieve all of the week's of the season along with the user's picks
        try {
            if (currentNCAALoveLeaveWeek.getStatus().equals(FSSeasonWeek.Status.COMPLETED.toString()) || currentNCAALoveLeaveWeek.getWeekType().equals(FSSeasonWeek.WeekType.INITIAL.toString())) {
                otherUserPicks = GamePicks.GetOtherUserLoveEmLeaveEmPicks(currentNCAALoveLeaveWeek.getFSSeasonID(), fsTeamID, currentNCAALoveLeaveWeek.getFSSeasonWeekID());
            }
            else {
                otherUserPicks = GamePicks.GetOtherUserLoveEmLeaveEmPicks(currentNCAALoveLeaveWeek.getFSSeasonID(), fsTeamID, currentNCAALoveLeaveWeek.getFSSeasonWeekID() - 1);
            }  
            
        } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
        }
        
        request.setAttribute("otherUserPicks",otherUserPicks);

        return nextPage;
    }
}