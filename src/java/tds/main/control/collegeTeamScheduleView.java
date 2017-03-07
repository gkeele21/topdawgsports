package tds.main.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.bo.Game;
import tds.main.bo.Team;
import tds.main.bo.UserSession;

public class collegeTeamScheduleView extends BaseView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        
        String nextPage = super.process(request,response);

        // If nextPage something that means an error happened
        if (nextPage != null) { return nextPage; }

        nextPage = htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);

        // Grab the request parameters
        int schedYear = FSUtils.getIntRequestParameter(request, "year", 0);
        int teamId = FSUtils.getIntRequestParameter(request, "tid", 0);
        
        if (schedYear == 0) { schedYear = (Integer)session.getHttpSession().getAttribute("sportYear"); }        

        // Retrieve team schedule
        try {
            List<Game> games = Game.GetTeamSchedule(teamId, schedYear);
            
            Team team = new Team(teamId);
            
            request.setAttribute("games",games);
            request.setAttribute("team",team);
            request.setAttribute("schedYear",schedYear);
            
        } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
        }

        return nextPage;
    }
}