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
import tds.main.bo.FSRoster;
import tds.main.bo.Player;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class faAcquirePlayerView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;

        String posname = FSUtils.getRequestParameter(request,"pos");
        if (posname == null || posname.equals("")) {
            posname = FSUtils.noNull(""+_Session.getHttpSession().getAttribute("faposname")).toString();
        }
        if (posname == null || posname.equals("") || posname.equals("null")) {
            posname = "QB";
        }
        
        _Session.getHttpSession().setAttribute("faposname",posname);

        int startingRowNum = FSUtils.getIntRequestParameter(request,"start",1);
        request.setAttribute("startingRowNum",startingRowNum);

        // Retrieve players
        
        FSLeague league = _FSTeam.getFSLeague();
        
        List<FSRoster> teamRoster = _FSTeam.getRoster(_CurrentFSSeasonWeek.getFSSeasonWeekID());
        request.setAttribute("teamRoster",teamRoster);
        
        List<Player> freeagents = league.GetFreeAgents(_CurrentFSSeasonWeek.getFSSeasonWeekID(),posname);
        request.setAttribute("freeAgents",freeagents);
        System.out.println("FA Size : " + freeagents.size());
        
        _Session.getHttpSession().removeAttribute("dropPlayer");
        _Session.getHttpSession().removeAttribute("puPlayer");
        _Session.getHttpSession().removeAttribute("faTransaction");

        return page;
    }
    
}
