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
import tds.main.bo.FSFootballTransaction;
import tds.main.bo.FSRoster;
import tds.main.bo.Player;
import tds.main.control.BaseTeamView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 *
 * @author grant.keele
 */
public class faConfirmView extends BaseTeamView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;

        String dropRosterID = FSUtils.getRequestParameter(request,"drop");
        FSRoster dropRosterSpot = null;
        if (dropRosterID == null || dropRosterID.equals("")) {
            dropRosterSpot = (FSRoster)_Session.getHttpSession().getAttribute("dropRosterSpot");
        } else {
            dropRosterSpot = new FSRoster(Integer.parseInt(dropRosterID));
        }

        if (dropRosterSpot == null) {
            _Session.setErrorMessage("Error : please select a player to drop first.");
            return "faDropPlayer.htm";
        }

        String dropType = FSUtils.getRequestParameter(request, "dropType");
        if (dropType == null || dropType.equals(""))
        {
            dropType = (String)_Session.getHttpSession().getAttribute("dropType");
            if (FSUtils.isEmpty(dropType))
            {
                dropType = "drop";
            }
        }

        _Session.getHttpSession().setAttribute("dropRosterSpot",dropRosterSpot);
        _Session.getHttpSession().setAttribute("dropType", dropType);

        String addType = (String)_Session.getHttpSession().getAttribute("addType");
        if (FSUtils.isEmpty(addType))
        {
            addType = "PU";
        }

        Player puPlayer = (Player)_Session.getHttpSession().getAttribute("puPlayer");

        FSFootballTransaction transaction = new FSFootballTransaction();

        transaction.setDropPlayer(dropRosterSpot.getPlayer());
        transaction.setDropPlayerID(dropRosterSpot.getPlayerID());
        transaction.setDropType(dropType.toUpperCase());
        transaction.setFSLeagueID(_FSTeam.getFSLeagueID());
        transaction.setFSSeasonWeek(_CurrentFSSeasonWeek);
        transaction.setFSSeasonWeekID(_CurrentFSSeasonWeek.getFSSeasonWeekID());
        transaction.setFSTeam(_FSTeam);
        transaction.setFSTeamID(_FSTeam.getFSTeamID());
        transaction.setPUPlayer(puPlayer);
        transaction.setPUPlayerID(puPlayer.getPlayerID());
        transaction.setPUType(addType);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType("PU");

        _Session.getHttpSession().setAttribute("faTransaction",transaction);

        return page;
    }

}
