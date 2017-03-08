/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.commissioner.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.FSRoster;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.FSTeam;
import tds.main.bo.UserSession;
import tds.main.control.BaseView;

/**
 *
 * @author grant.keele
 */
public class fsTeamViewView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
        
        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();
        
        int fsTeamID = FSUtils.getIntRequestParameter(request, "fsTeamID", 0);
        FSTeam fsTeam;
        if (fsTeamID > 0) {
            fsTeam = new FSTeam(fsTeamID);
            session.setAttribute("commFSTeamID", fsTeamID);
            session.setAttribute("commFSTeam", fsTeam);
        } else {
            fsTeam = (FSTeam)session.getAttribute("commFSTeam");
        }
        
        // figure current week
        List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(fsTeam.getFSLeague().getFSSeasonID());
        session.setAttribute("commAllWeeks", allWeeks);
        int reqFSSeasonWeekId = FSUtils.getIntRequestParameter(request, "weekid", 0);
        FSSeasonWeek displayFSSeasonWeek = null;
        
        boolean stopSettingDisplayWeek = false;
        weeks1 : for (FSSeasonWeek week : allWeeks) {

            // The requested week takes precedence
            if (week.getFSSeasonWeekID() == reqFSSeasonWeekId) {
                displayFSSeasonWeek = week;
                break weeks1;
            }

            if (week.getStatus().equals("CURRENT")) {
                displayFSSeasonWeek = week;
            }
            
            if (displayFSSeasonWeek == null) {
                displayFSSeasonWeek = week;
            }
            
        }

        // Retrieve current roster
        List<FSRoster> roster = fsTeam.getRoster(displayFSSeasonWeek.getFSSeasonWeekID());
        
        session.setAttribute("commFSRoster", roster);
        session.setAttribute("commFSSeasonWeek", displayFSSeasonWeek);
        
        return page;
    }
    
}
