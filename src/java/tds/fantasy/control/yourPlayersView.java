/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.fantasy.control;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSRoster;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class yourPlayersView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;
        
        List<FSRoster> activeRoster = _FSTeam.getRoster(_CurrentFSSeasonWeek.getFSSeasonWeekID(), "active", false);
        request.setAttribute("activeRoster",activeRoster);

//        List<FSRoster> inactiveRoster = _FSTeam.getRoster(_FSSeasonWeek.getFSSeasonWeekID(), "inactive",_FSSeasonWeek.getFSSeason().getSeasonID());
//        request.setAttribute("inactiveRoster",inactiveRoster);

        List<FSRoster> irRoster = _FSTeam.getRoster(_CurrentFSSeasonWeek.getFSSeasonWeekID(), "ir");
        request.setAttribute("irRoster",irRoster);
        
        return page;
    }
    
}
