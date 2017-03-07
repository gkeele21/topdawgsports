package tds.fantasy.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSFootballTransaction;
import tds.main.bo.FSLeague;
import tds.main.bo.FSSeasonWeek;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class transactionsView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;

        // Retrieve current week info
        FSLeague league = _FSTeam.getFSLeague();
        
        int reqFSSeasonWeekId = FSUtils.getIntRequestParameter(request, "wk", 0);
        
       
        FSSeasonWeek displayWeek = null;
        
        // Retrieve all of the FSSeasonWeek's for the entire FSSeason
            List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(_FSTeam.getFSLeague().getFSSeasonID());
            
            // See if we can find the week in the allWeeks variable
            for (FSSeasonWeek week : allWeeks) {
                
                // The requested week takes precedence
                if (week.getFSSeasonWeekID() == reqFSSeasonWeekId) {
                    displayWeek = week;
                    break;
                }
                
                // Set it to be the current week
                if (week.getStatus().equals(FSSeasonWeek.Status.CURRENT.toString())) {
                    displayWeek = week;
                }
                
                // If we get to the final week and the display week was never set (Current Week)
                if (week.getWeekType().equals(FSSeasonWeek.WeekType.FINAL.toString()) && displayWeek == null) {
                    displayWeek = week;
                }
            }
        
        // retrieve current transactions
        List<FSFootballTransaction> transactions = league.GetTransactions(displayWeek.getFSSeasonWeekID());
        
        // Set all the request / session objects
        request.setAttribute("displayWeek",displayWeek);
        request.setAttribute("currentTransactions",transactions);
        request.setAttribute("allWeeks",allWeeks);

        return page;
    }
    
}