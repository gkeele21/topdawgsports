/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.fantasy.control;

import tds.main.bo.FSRoster;
import tds.main.control.BaseTeamView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
        try {
            List<FSRoster> activeRoster = _FSTeam.getRoster(_CurrentFSSeasonWeek.getFSSeasonWeekID(), "active", false);
            request.setAttribute("activeRoster",activeRoster);

    //        List<FSRoster> inactiveRoster = _FSTeam.getRoster(_FSSeasonWeek.getFSSeasonWeekID(), "inactive",_FSSeasonWeek.getFSSeason().getSeasonID());
    //        request.setAttribute("inactiveRoster",inactiveRoster);

            List<FSRoster> irRoster = _FSTeam.getRoster(_CurrentFSSeasonWeek.getFSSeasonWeekID(), "'ir','ir-covid'");
            request.setAttribute("irRoster",irRoster);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

}
