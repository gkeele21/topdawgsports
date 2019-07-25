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
import tds.main.bo.FSLeague;
import tds.main.bo.PlayerStats;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class totalStatsRBView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;
        
        String posname = "RB";

        int startingRowNum = FSUtils.getIntRequestParameter(request,"start",1);
        request.setAttribute("startingRowNum",startingRowNum);

        String orderBy = FSUtils.getRequestParameter(request,"orderBy","tst.FantasyPts");

        // Retrieve players
        FSLeague league = _FSTeam.getFSLeague();
        
        List<PlayerStats> players = league.GetPlayerStats(_DisplayFSSeasonWeek.getFSSeasonWeekID(),posname,orderBy);
        request.setAttribute("players",players);
        
        return page;
    }
    
}
