package tds.commissioner.control;

import bglib.util.FSUtils;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.*;
import tds.main.control.BaseView;
import tds.mm.bo.MarchMadnessGame;
import tds.mm.bo.MarchMadnessTournament;

public class gameMaintenanceView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
       
        String nextPage = super.process(request,response);

        if (nextPage != null) { return nextPage; }

        nextPage = htmlPage;

        // Get request/session objects
        int reqSportId = FSUtils.getIntRequestParameter(request, "spid", Sport.COLLEGE_BASKETBALL);
        int reqSeasonWeekId = FSUtils.getIntRequestParameter(request, "swid", 0);
        int reqYear = FSUtils.getIntRequestParameter(request, "yr", Calendar.getInstance().get(Calendar.YEAR));        

        // Retrieve all of the weeks for the selected sport for the current year
        List<SeasonWeek> allWeeks = SeasonWeek.GetSportWeeks(reqSportId, reqYear);        
        SeasonWeek displayWeek = null;
            
        // See if we can find the week in the allWeeks variable.  The requested week takes precedence.
        for (SeasonWeek week : allWeeks) {
                if (week.getSeasonWeekID() == reqSeasonWeekId) {
                    displayWeek = week;
                    break;
                }
                
                if (week.getStatus().equals(SeasonWeek.Status.CURRENT.toString())) {
                    displayWeek = week;
                }
                
                if (week.getWeekType().equals(SeasonWeek.WeekType.FINAL.toString()) && displayWeek == null) {
                    displayWeek = week;
                }
        }
        
        try {
            
            if (reqSportId == Sport.COLLEGE_BASKETBALL) {
                MarchMadnessTournament tournament = MarchMadnessTournament.GetTournamentByYear(reqYear);
                List<MarchMadnessGame> games = MarchMadnessGame.GetTournamentGames(tournament.getTournamentID(), displayWeek.getWeekNo(), displayWeek.getWeekNo());
                request.setAttribute("marchMadnessGames",games);
            }

            else if (reqSportId == Sport.PRO_FOOTBALL || reqSportId == Sport.COLLEGE_FOOTBALL) {
                // Get Weekly Football Schedule
                List<Game> footballGames = Game.GetWeeklySchedule(displayWeek.getSeasonWeekID(), false);

                // Retrieve the Football Standings for the prior week
                Map<Integer, Standings> footballStandings = new HashMap();

                SeasonWeek standingsWeek = SeasonWeek.GetPriorSeasonWeek(displayWeek);

                if (standingsWeek != null) {                
                    List<Standings> priorStandings = Standings.GetStandings(standingsWeek.getSeasonWeekID());
                    for (int i=0; i < priorStandings.size(); i++) {
                        footballStandings.put(priorStandings.get(i).getTeamID(), priorStandings.get(i));
                    }
                }

                // Retrieve Football Top 25 Rankings
                Map<Integer, Standings> apRankings = new HashMap();

                if (reqSportId == Sport.COLLEGE_FOOTBALL) {                
                    List<Standings> standings = Standings.GetTop25Rankings(displayWeek.getSeasonWeekID());
                    for (int i=0; i < standings.size(); i++) {
                        apRankings.put(standings.get(i).getTeamID(), standings.get(i));
                    }
                }

                // Retrieve Bye Teams
                List<Game> byeTeams = Game.GetByeTeams(displayWeek.getSeasonWeekID(), false);
                
                request.setAttribute("footballGames",footballGames);
                request.setAttribute("footballStandings",footballStandings);
                request.setAttribute("apRankings",apRankings);
                request.setAttribute("byeTeams",byeTeams);  
            }
            
            request.setAttribute("sportId",reqSportId);
            request.setAttribute("displayWeek",displayWeek);
            request.setAttribute("allWeeks",allWeeks);
            request.setAttribute("yr",reqYear);
           
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        } 
        
        return nextPage;
    }
}