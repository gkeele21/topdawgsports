package tds.main.control;

import bglib.util.FSUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSGame;
import tds.main.bo.FSTeam;
import tds.main.bo.FSUser;
import tds.main.bo.Season;
import tds.main.bo.UserSession;

public class userProfileView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        
        String page = super.process(request, response);
        
        if (page != null) { return page; }

        page = htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);
        
        // Get request/session objects
        FSUser user = (FSUser)session.getHttpSession().getAttribute("validUser");
        if (user == null) {
            System.out.println("User not found in session.");
            Integer userId = (Integer)session.getHttpSession().getAttribute("validUserId");
            if (userId > 0) {
                user = new FSUser(userId);
            } else {
                user = UserSession._UserCache.get(session.getHttpSession().getId());
            }
            
            if (user == null) {
                System.out.println("Problem : user obj still null");
            }
            
        }
        List<Integer> allYears = (List<Integer>)session.getHttpSession().getAttribute("allYears");
        Integer sportYear = (Integer)session.getHttpSession().getAttribute("sportYear");
        Integer reqYear = FSUtils.getIntRequestParameter(request, "reqYear", 0);
        
        // Get all of the Distinct SportYears
        if (allYears == null || allYears.isEmpty()) {
            allYears = Season.GetAllSportYears();
            session.getHttpSession().setAttribute("allYears", allYears);
        }        

        // Set the sport year
        if (reqYear > 0) {
            sportYear = reqYear;
        }
        
        else {
            // Maybe it was retrieved from the session, if not grab the first year
            if (sportYear == null) {
                sportYear = allYears.get(0);
            }
        }
        
        // Get all of the user's teams for the sport year        
        List<FSTeam> allUserTeams = user.getTeams(sportYear);
        
        List<FSTeam> activeTeams = new ArrayList<FSTeam>();
        List<FSTeam> inactiveTeams = new ArrayList<FSTeam>();
        
        for (FSTeam team : allUserTeams) {
            if (team.isIsActive()) {
                activeTeams.add(team);
            }
            else {
                inactiveTeams.add(team);
            }
        }
        
        session.getHttpSession().setAttribute("sportYear", sportYear);
        session.getHttpSession().setAttribute("allUserTeams", allUserTeams);
        session.getHttpSession().setAttribute("activeTeams", activeTeams);
        session.getHttpSession().setAttribute("inactiveTeams", inactiveTeams);
        session.getHttpSession().setAttribute("HEAD_TO_HEAD_GAMEID", FSGame.HEAD_TO_HEAD);
        session.getHttpSession().setAttribute("NFL_WINS_POOL_GAMEID", FSGame.NFL_WINS_POOL);
        session.getHttpSession().setAttribute("NBA_WINS_POOL_GAMEID", FSGame.NBA_WINS_POOL);
        
        return page;
    }    
}