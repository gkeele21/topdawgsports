/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.fantasy.control;

import bglib.util.AuDate;
import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSFootballTransactionRequest;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class transactionRequestsView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        
        if (page != null) {
            return page;
        }

        page = htmlPage;

        // check to see if we need to redirect to the FAAquire page
        AuDate dt = _CurrentFSSeasonWeek.getTransactionDeadline();
        AuDate now = new AuDate();
        if (now.after(dt, false)) {
            return "faAcquirePlayer.htm";
        }

        // Retrieve transaction requests
        int id = FSUtils.getIntRequestParameter(request, "id", 0);
        if (id > 0) {
            int newrank = FSUtils.getIntRequestParameter(request, "rank", 0);
            FSFootballTransactionRequest tRequest = new FSFootballTransactionRequest(id);
            
            FSFootballTransactionRequest otherRequest = new FSFootballTransactionRequest(_FSTeam.getFSTeamID(), _CurrentFSSeasonWeek.getFSSeasonWeekID(), newrank);
            otherRequest.setRank(tRequest.getRank());
            tRequest.setRank(newrank);
            try {
                tRequest.update();
                otherRequest.update();
            } catch (Exception e) {
                
            }
            
        }
        
        List<FSFootballTransactionRequest> teamRequests = _FSTeam.getTransactionRequests(_CurrentFSSeasonWeek.getFSSeasonWeekID());
        request.setAttribute("teamRequests",teamRequests);
        request.setAttribute("teamRequestsSize",teamRequests.size());
        
        // retrieve # of max requests
        int num = _FSTeam.getMaxRequests(_CurrentFSSeasonWeek.getFSSeasonWeekID());
        request.setAttribute("maxRequests",num);
        
        return page;
    }
    
}
