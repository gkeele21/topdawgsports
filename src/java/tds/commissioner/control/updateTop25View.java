package tds.commissioner.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.bo.SeasonWeek;
import tds.main.bo.Standings;
import tds.main.control.BaseView;

public class updateTop25View extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
       
        String nextPage = super.process(request,response);

        if (nextPage != null) { return nextPage; }

        nextPage = htmlPage;

        // Get request/session objects
        int reqSeasonWeekId = FSUtils.getIntRequestParameter(request, "sw", 0);
        
        if (reqSeasonWeekId == 0) { return nextPage; }
        
        try {
            SeasonWeek week = new SeasonWeek(reqSeasonWeekId);
            List<Standings> currentRankings = Standings.GetTop25Rankings(week.getSeasonWeekID());
            SeasonWeek priorWeek = SeasonWeek.GetPriorSeasonWeek(week);
            List<Standings> priorRankings = Standings.GetTop25Rankings(priorWeek.getSeasonWeekID());
            
            if (priorRankings.isEmpty()) {
                for (int i=1; i <= 25; i++) {
                    Standings newStandings = new Standings();
                    newStandings.setOverallRanking(i);
                    priorRankings.add(newStandings);
                }               
            }

            request.setAttribute("currentRankings", currentRankings);
            request.setAttribute("priorRankings", priorRankings);
           
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        } 
        
        return nextPage;
    }
}