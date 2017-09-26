package tds.fantasy.control;

import bglib.util.FSUtils;
import tds.main.control.BaseAction;
import tds.main.bo.FSFootballTransactionRequest;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.FSTeam;
import tds.main.bo.UserSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

public class transactionRequestsAction extends BaseAction {

    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        nextPage = "transactionRequests";
        UserSession session = UserSession.getUserSession(request, response);

        // Create FSTeam obj
        FSTeam team = (FSTeam)session.getHttpSession().getAttribute("fsteam");
        FSSeasonWeek currFSSeasonWeek = (FSSeasonWeek)session.getHttpSession().getAttribute("fantasyCurrentWeek");
        
        // check to see if they hit the button to update # of max requests
        String submit = request.getParameter("btnSubmit.x");
        if (submit != null) {
            int maxNum = FSUtils.getIntRequestParameter(request, "txtNumTrades", 10);
            
            team.updateMaxRequests(currFSSeasonWeek.getFSSeasonWeekID(), maxNum);
            return nextPage;
        }
        
        String[] delIDs = request.getParameterValues("ckDelete");
        int rank = 0;
        for (String id : delIDs) {
            FSFootballTransactionRequest tRequest = new FSFootballTransactionRequest(Integer.parseInt(id));
            rank = tRequest.getRank();
            try {
                tRequest.delete();
            } catch (Exception e) {}
        }
        
        // update all the Transactions from 'Rank' on to adjust their new ranks
        
        List<FSFootballTransactionRequest> requests = team.getTransactionRequests(currFSSeasonWeek.getFSSeasonWeekID());
        int count = 0;
        for (FSFootballTransactionRequest req : requests) {
            count++;
            if (req.getRank() > rank) {
                req.setRank(count);
                try {
                    req.update();
                } catch (Exception e) {}
            }
        }
        
        return nextPage;
    }
}
