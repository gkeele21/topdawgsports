/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.golf.control;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.*;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class teamInfoView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;

        try {
            
            FSLeague league = _FSTeam.getFSLeague();
            
            List<PGATournamentWeek> tournamentWeeks = PGATournamentWeek.ReadAllByFSSeason(_FSTeam.getFSLeague().getFSSeasonID());
            request.setAttribute("tournamentWeeks", tournamentWeeks);
            
            List<FSGolfStandings> teamStandings = FSGolfStandings.getTeamStandings(_FSTeam.getFSTeamID());
            request.setAttribute("teamStandings", teamStandings);
            
            // Get all the teams in the league based off the FSTeam (user logged in)
            List<FSTeam> allLeagueTeams = FSTeam.GetLeagueTeams(_FSTeam.getFSLeagueID());
            
            // Retrieve all of the FSSeasonWeek's for the entire FSSeason
            List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(_FSTeam.getFSLeague().getFSSeasonID());
            
            FSSeasonWeek displayWeek = new FSSeasonWeek();
            List<FSSeasonWeek> standingsWeeks = new ArrayList<FSSeasonWeek>();
            
            // See if we can find the week in the allWeeks variable
            weeks1 : for (FSSeasonWeek week : allWeeks) {
                
                // We will only show completed weeks on the Standings
                if (week.getStatus().equals(FSSeasonWeek.Status.COMPLETED.toString())) {
                    standingsWeeks.add(week);                    
                }
                
                // End the looping on the Current week.  If display week wasn't set via reqFSSeasonWeekId then use the previous week
                if (week.getStatus().equals(FSSeasonWeek.Status.CURRENT.toString())) {
                    if (displayWeek.getFSSeasonWeekID() == null) {
                        int ind = allWeeks.indexOf(week);
                        if (ind > 0)
                        {
                            displayWeek = allWeeks.get(ind - 1);
                        } else
                        {
                            displayWeek = week;
                        }
                    }
                    _CurrentFSSeasonWeek = week;
                    break weeks1;

                }
                
                // Past season's won't have a Current week so if we're at the final week show this week
                if (week.getWeekType().equals(FSSeasonWeek.WeekType.FINAL.toString())) {
                    displayWeek = week;
                }
            }

            // Grab the league standings
            List<FSGolfStandings> weekResults = FSGolfStandings.getLeagueStandings(league.getFSLeagueID(), displayWeek.getFSSeasonWeekID(), "s.Rank asc, s.WeekWinnings desc, s.WeekMoneyEarned desc, t.teamName", true, true);

            // Grab the league standings
            List<FSGolfStandings> leagueStandings = FSGolfStandings.getLeagueStandings(league.getFSLeagueID(), displayWeek.getFSSeasonWeekID(), "s.TotalWinnings desc, s.TotalMoneyEarned desc, t.teamName");

            PGATournamentWeek lastWeekTournament = new PGATournamentWeek(displayWeek.getFSSeasonWeekID());
            
            request.setAttribute("golfallLeagueTeams",allLeagueTeams);
            request.setAttribute("golfdisplayWeek", displayWeek);
            request.setAttribute("golfstandingsWeeks",standingsWeeks);
            request.setAttribute("golfleagueStandings",leagueStandings);
            request.setAttribute("weekGolfResults", weekResults);
            request.setAttribute("lastWeekTournament", lastWeekTournament);
            request.setAttribute("fsTeam", _FSTeam);
            _Session.getHttpSession().setAttribute("fantasyCurrentWeek",_CurrentFSSeasonWeek);
            _Session.getHttpSession().setAttribute("golfdisplayfsseasonweek",displayWeek);
        
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }
        
        return page;
    }
    
}
