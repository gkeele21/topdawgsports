/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.commissioner.fantasy.control;

import bglib.util.FSUtils;
import tds.main.control.BaseView;
import tds.main.bo.FSSeason;
import tds.main.bo.UserSession;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.*;

/**
 *
 * @author grant.keele
 */
public class viewTransactionRequestsView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
        
        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();
        
        FSLeague fsLeague = (FSLeague)session.getAttribute("commFSLeague");
        
        if (fsLeague != null)
        {
            FSSeason fsSeason = new FSSeason(fsLeague.getFSSeasonID());
            FSSeasonWeek fsseasonweek = fsSeason.getCurrentFSSeasonWeek();
            
            // get TransactionRequests
            List<FSFootballTransactionRequest> requests = FSFootballTransactionRequest.getLeagueRequests(fsLeague.getFSLeagueID(), fsseasonweek.getFSSeasonWeekID());
            session.setAttribute("requests", requests);
            session.setAttribute("commFSSeasonWeekID", fsseasonweek.getFSSeasonWeekID());
            session.setAttribute("commFSSeasonWeek", fsseasonweek);
        }

        return page;
    }
    
}
