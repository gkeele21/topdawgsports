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
public class faDropPlayerView extends BaseTeamView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;

        String puPID = FSUtils.getRequestParameter(request,"pu");
        Player puPlayer = null;
        if (puPID == null || puPID.equals("")) {
            puPlayer = (Player)_Session.getHttpSession().getAttribute("puPlayer");
        } else {
            //puPlayer = new Player(Integer.parseInt(puPID));
            puPlayer = Player.getInstance(Integer.parseInt(puPID));
        }

        if (puPlayer == null) {
            _Session.setErrorMessage("Error : please select a player to pickup first.");
            return "faAcquirePlayer.htm";
        }

        _Session.getHttpSession().setAttribute("puPlayer",puPlayer);

        String addType = FSUtils.getRequestParameter(request, "addType");
        if (!FSUtils.isEmpty(addType))
        {
            _Session.getHttpSession().setAttribute("addType",addType);
        }

        List<FSRoster> teamRoster = _FSTeam.getRoster(_CurrentFSSeasonWeek.getFSSeasonWeekID());
        request.setAttribute("teamRoster",teamRoster);

        return page;
    }

}
