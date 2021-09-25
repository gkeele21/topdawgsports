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
import tds.main.bo.FSLeague;
import tds.main.bo.FSRoster;
import tds.main.bo.Player;
import tds.main.control.BaseTeamView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

        List<FSRoster> teamActiveRoster = _FSTeam.getRoster(_CurrentFSSeasonWeek.getFSSeasonWeekID(), "active", false);
        request.setAttribute("teamActiveRoster",teamActiveRoster);

        List<FSRoster> teamIRRoster = _FSTeam.getRoster(_CurrentFSSeasonWeek.getFSSeasonWeekID(), "'ir','ir-covid'", false);
        request.setAttribute("teamIRRoster",teamIRRoster);

        List<Player> freeagents = league.GetFreeAgents(_CurrentFSSeasonWeek.getFSSeasonWeekID(),posname);
        request.setAttribute("freeAgents",freeagents);
        System.out.println("FA Size : " + freeagents.size());

        _Session.getHttpSession().removeAttribute("dropPlayer");
        _Session.getHttpSession().removeAttribute("puPlayer");
        _Session.getHttpSession().removeAttribute("faTransaction");

        return page;
    }

}
