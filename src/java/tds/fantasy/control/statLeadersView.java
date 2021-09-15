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
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSLeague;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.SeasonWeek;
import tds.main.bo.PlayerStats;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
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
        
        //Retrieve stats      
        request.setAttribute("startingRowNum",startingRowNum);
        request.setAttribute("reqWeek",reqWeek);
        request.setAttribute("displayWeeks",displayWeeks); 
        request.setAttribute("players",players);
        request.setAttribute("posname",posname);
        request.setAttribute("showDefense",_FSTeam.getFSLeague().getDraftType().equals("dynasty"));
        request.setAttribute("currentWeek",_CurrentFSSeasonWeek.getFSSeasonWeekID());
       
        return page;
    }
    
}
