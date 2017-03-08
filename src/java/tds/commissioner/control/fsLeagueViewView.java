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
import tds.main.control.BaseView;
import tds.main.bo.FSSeason;
import tds.main.bo.UserSession;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.FSLeague;
import tds.main.bo.FSTeam;

/**
 *
 * @author grant.keele
 */
public class fsLeagueViewView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
        
        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();
        
        int fsLeagueID = FSUtils.getIntRequestParameter(request, "fsLeagueID", 0);
        FSLeague fsLeague;
        if (fsLeagueID > 0) {
            fsLeague = new FSLeague(fsLeagueID);
            session.setAttribute("commFSLeagueID", fsLeagueID);
            session.setAttribute("commFSLeague", fsLeague);
        } else {
            fsLeagueID = FSUtils.getIntSessionAttribute(session, "commFSLeagueID", 0);
            fsLeague = (FSLeague)session.getAttribute("commFSLeague");
        }

        // Retrieve current fsTeams
        List<FSTeam> fsTeams = fsLeague.GetTeams();
        
        session.setAttribute("fsTeams", fsTeams);
        
        return page;
    }
    
}
