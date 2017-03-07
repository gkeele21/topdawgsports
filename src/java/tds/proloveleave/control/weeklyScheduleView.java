package tds.proloveleave.control;

import bglib.util.FSUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.*;
import tds.main.control.BaseTeamView;
import tds.proloveleave.bo.ProLoveLeave;

public class weeklyScheduleView extends BaseTeamView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
       
        String nextPage = super.process(request,response);

        // If nextPage something that means an error happened
        if (nextPage != null) { return nextPage; }

        nextPage = htmlPage;
        
        try {            
            FSTeam displayTeam = null;
            FSSeasonWeek displayWeek = null;
            
            // Get request/session objects
            int reqFSTeamId = FSUtils.getIntRequestParameter(request, "dtid", 0);
            int reqFSSeasonWeekId = FSUtils.getIntRequestParameter(request, "wk", 0);
            
            // Get all the teams in the league based off the FSTeam (user logged in)
            List<FSTeam> allLeagueTeams = FSTeam.GetLeagueTeams(_FSTeam.getFSLeagueID());
            
            // Figure out the display team
            if (reqFSTeamId > 0) {
                for (FSTeam team : allLeagueTeams) {
                    if (team.getFSTeamID() == reqFSTeamId) {
                        displayTeam = team;
                    }            
                }                
            }
                
            else {
                // Maybe it was retrieved from the session, if not grab the logged in FSTeam
                if (displayTeam == null) {
                    displayTeam = _FSTeam;
                }
            }
            
            // Retrieve all of the FSSeasonWeek's for the entire FSSeason
            List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(displayTeam.getFSLeague().getFSSeasonID());
            
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
            
            // Retrieve all of the user's picks for the entire season
            List<ProLoveLeave> picks = ProLoveLeave.GetPicks(displayWeek.getFSSeasonID(), displayTeam.getFSTeamID(), 0);

            // Retrieve the weekly schedule
            List<Game> games = Game.GetWeeklySchedule(displayWeek.getSeasonWeekID(), false);
            
            // Find out which teams have expired (meaning they can't be picked anymore for the season) AND the teamPicked for the week
            Map<Integer, ProLoveLeave> expiredTeams = new HashMap<Integer, ProLoveLeave>();
            int teamPickedId = 0;

            for (ProLoveLeave pick : picks) {
                if (pick.getTeamPickedID() > 0 && pick.getFSSeasonWeekID() < displayWeek.getFSSeasonWeekID()) {
                    expiredTeams.put(pick.getTeamPickedID(), pick);
                }
                if (pick.getFSSeasonWeekID() == displayWeek.getFSSeasonWeekID()) {
                    teamPickedId = pick.getTeamPickedID();
                }
            }
            
            // Retrieve the Standings for the prior week
            List<Standings> priorStandings = null;
            Map<Integer, Standings> weekStandings = new HashMap();

            SeasonWeek standingsWeek = SeasonWeek.GetPriorSeasonWeek(displayWeek.getSeasonWeek());
                        
            if (standingsWeek != null) {                
                priorStandings = Standings.GetStandings(standingsWeek.getSeasonWeekID());
                for (int i=0; i < priorStandings.size(); i++) {
                    weekStandings.put(priorStandings.get(i).getTeamID(), priorStandings.get(i));
                }
            }
            
            // Retrieve the FSTeam's standings for the week as long as the week is finished
            FSFootballStandings fsStandings = null;
            if(displayWeek.getStatus().equals(FSSeasonWeek.Status.COMPLETED.toString())) {                
                fsStandings = FSFootballStandings.GetWeekStandingsForFSTeam(displayWeek.getFSSeasonWeekID(), displayTeam.getFSTeamID());
            }

            // Retrieve the bye teams
            List<Game> byeTeams = Game.GetByeTeams(displayWeek.getSeasonWeekID(), false);
            
            // Set all the request / session objects
            request.setAttribute("displayWeek",displayWeek);
            request.setAttribute("displayTeam",displayTeam);
            request.setAttribute("allLeagueTeams",allLeagueTeams);
            request.setAttribute("allWeeks",allWeeks);   
            request.setAttribute("picks",picks);
            request.setAttribute("games",games);
            request.setAttribute("expiredTeams",expiredTeams);
            request.setAttribute("teamPickedID",teamPickedId);
            request.setAttribute("weekStandings",weekStandings);
            request.setAttribute("fsStandings",fsStandings);
            request.setAttribute("byeTeams",byeTeams);
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }

        return nextPage;
    }
}