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
public class irPlayersView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;
        
        // Retrieve ir players
        List<FSRoster> irRoster = _FSTeam.getFSLeague().GetIRPlayers(_CurrentFSSeasonWeek.getFSSeasonWeekID());
        request.setAttribute("irRoster",irRoster);

        return page;
    }
    
}
