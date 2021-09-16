package tds.fantasy.control;

import bglib.util.FSUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.constants.FSGame;
import tds.main.bo.FSLeague;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.SeasonWeek;
import tds.main.bo.PlayerStats;
import tds.main.control.BaseTeamView;

public class statLeadersView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;
               
        String posname = FSUtils.getRequestParameter(request, "pos", "QB");
        int reqWeek = FSUtils.getIntRequestParameter(request, "wk", 0);
        int startingRowNum = FSUtils.getIntRequestParameter(request,"start",1); 
        
        // Retrieve league info
        FSLeague league = _FSTeam.getFSLeague();
        
        // Retrieve all of the FSSeasonWeek's for the entire FSSeason
        List<PlayerStats> players = null;
        List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(_CurrentFSSeasonWeek.getFSSeasonID());
        List<FSSeasonWeek> displayWeeks = new ArrayList<>();
        
        for (FSSeasonWeek week : allWeeks) {
            
            // We will only show completed weeks on the page
            if (week.getStatus().equals(SeasonWeek.Status.COMPLETED.toString())) {
                displayWeeks.add(week);                    
            }
        } 
        
        if (reqWeek == 0) { 
            players = league.GetPlayerStatsTotal(_CurrentFSSeasonWeek.getFSSeasonWeekID(), posname);
        }
        else {
            players = league.GetPlayerStatsByWeek(reqWeek, posname);
        }
        
        boolean showDefense = _FSTeam.getFSLeague().getDraftType()!= null &&_FSTeam.getFSLeague().getDraftType().equals("dynasty");
        boolean showOwner = _FSTeam.getFSLeague().getFSSeason().getFSGameID() == FSGame.HEADTOHEAD;
        
        //Retrieve stats      
        request.setAttribute("posname",posname);
        request.setAttribute("reqWeek",reqWeek);
        request.setAttribute("startingRowNum",startingRowNum);
        request.setAttribute("players",players);
        request.setAttribute("displayWeeks",displayWeeks);
        request.setAttribute("showDefense",showDefense);
        request.setAttribute("showOwner",showOwner);
       
        return page;
    }    
}