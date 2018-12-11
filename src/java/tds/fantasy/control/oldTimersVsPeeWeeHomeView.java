/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.fantasy.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.*;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class oldTimersVsPeeWeeHomeView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
        
        System.out.println("Page : " + page);

        FSLeague league = _FSTeam.getFSLeague();
        FSSeason fsseason = league.getFSSeason();
        
//        int currentFSSeasonWeekID = fsseason.getCurrentFSSeasonWeekID();
//        FSSeasonWeek currFSSeasonWeek = new FSSeasonWeek(currentFSSeasonWeekID);
        FSFootballSeasonDetail details = new FSFootballSeasonDetail(fsseason.getFSSeasonID());
        
        int maxNumWeeks = details.getStartingWeekNumber() + details.getNumWeeksRegSeason() - 1;
        if (maxNumWeeks < 0) {
            maxNumWeeks = 0;
        }
        
        int reqFSSeasonWeekId = FSUtils.getIntRequestParameter(request, "weekid", 0);
        _DisplayFSSeasonWeek = (FSSeasonWeek)_Session.getHttpSession().getAttribute("fantasyDisplayWeek");
//        _Session.getHttpSession().setAttribute("fsseasonweek",currFSSeasonWeek);
        // Retrieve all of the FSSeasonWeek's for the entire FSSeason
        List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(_FSTeam.getFSLeague().getFSSeasonID());

        // See if we can find the week in the allWeeks variable
        FSSeasonWeek prevWeek = null;
        boolean stopSettingDisplayWeek = false;
        weeks1 : for (FSSeasonWeek week : allWeeks) {

            // The requested week takes precedence
            if (week.getFSSeasonWeekID() == reqFSSeasonWeekId) {
                _DisplayFSSeasonWeek = week;
            }

            if (week.getStatus().equals("CURRENT")) {
                _CurrentFSSeasonWeek = week;
                break weeks1;
            }
            
            if (reqFSSeasonWeekId == 0)
            {
                _DisplayFSSeasonWeek = week;
            }
            
            if (week.getWeekType().equals(FSSeasonWeek.WeekType.INITIAL.toString()) && _DisplayFSSeasonWeek == null) {
                _DisplayFSSeasonWeek = week;
            }
            
            // If we get to the final week then set it to this as long as the week hasn't been found and the session object didn't have anything (null)
            if (week.getWeekType().equals(FSSeasonWeek.WeekType.FINAL.toString()) && _DisplayFSSeasonWeek == null) {
                _DisplayFSSeasonWeek = week;
            }
        }     

        // Set it to be the current week as long as the session object didn't have anything (null)
        if (_DisplayFSSeasonWeek == null)
        {
//            _DisplayFSSeasonWeek = fsseason.getCurrentFSSeasonWeeks().get(new Integer(_CurrentFSSeasonWeek-1));
            _DisplayFSSeasonWeek = _CurrentFSSeasonWeek;
        }
        _Session.getHttpSession().setAttribute("fantasyCurrentWeek", _CurrentFSSeasonWeek);
        
        // retrieve league standings
        

        
        request.setAttribute("fantasyDisplayWeek", _DisplayFSSeasonWeek);
        request.setAttribute("fantasyCurrentWeek", _CurrentFSSeasonWeek);
        
        String sort = "s.Wins desc,s.TotalFantasyPts desc";
        List<FSFootballStandings> lgStandings = league.GetStandings(_DisplayFSSeasonWeek.getFSSeasonWeekID(),sort);
        if (lgStandings.isEmpty())
        {
            // get all teams in the league
            List<FSTeam> leagueTeams = league.GetTeams();
            for (FSTeam team : leagueTeams)
            {
                FSFootballStandings stand = new FSFootballStandings();
                stand.setFSTeam(team);
                
                lgStandings.add(stand);
            }
        }
        request.setAttribute("leagueStandings",lgStandings);
        
        // retrieve previous results
        List<FSFootballMatchup> results = league.GetResults(_DisplayFSSeasonWeek.getFSSeasonWeekID());
        request.setAttribute("previousResults",results);

        String displayWeekType = _DisplayFSSeasonWeek.getWeekType();
        String displayWeekStatus = _DisplayFSSeasonWeek.getStatus();
        
        // retrieve current schedule
        if (displayWeekType.equals("FINAL") && displayWeekStatus.equals("COMPLETED")) {
            
        } else
        {
            List<FSFootballMatchup> schedule = league.GetResults(_CurrentFSSeasonWeek.getFSSeasonWeekID());
            request.setAttribute("currentSchedule",schedule);
            // retrieve current transactions
            List<FSFootballTransaction> transactions = league.GetTransactions(_CurrentFSSeasonWeek.getFSSeasonWeekID());
            request.setAttribute("currentTransactions",transactions);

            // retrieve transaction order
            List<FSTeam> tOrder = FSFootballTransaction.getTransactionOrder(league.getFSLeagueID(), _CurrentFSSeasonWeek.getFSSeasonWeekID());
            request.setAttribute("transactionOrder",tOrder);
        }


        // retrieve past transactions
        List<FSFootballTransaction> prevtransactions = league.GetTransactions(_DisplayFSSeasonWeek.getFSSeasonWeekID());
        request.setAttribute("previousTransactions",prevtransactions);

//        AuDate now = new AuDate();
//        boolean after = now.after(_FSSeasonWeek.getTransactionDeadline(), false);
//
//        request.setAttribute("afterTransDeadline",after);

        /*UserSession session = UserSession.getUserSession(request, response);
        Login user = (Login)session.getHttpSession().getAttribute("validUser");
        if (user.hasAccessLevel(AccessLevel.ADMIN)==false) {
            if (user.getBranch()!=null) {
                session.getHttpSession().setAttribute("branch", user.getBranch());
                return "BranchInfo";
            } else {
                return "index";
            }
        }

        */
        
        return page;
    }
    
}
