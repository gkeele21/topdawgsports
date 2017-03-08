/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.fantasy.control;

import bglib.util.AuDate;
import bglib.util.FSUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSFootballTransaction;
import tds.main.bo.FSRoster;
import tds.main.bo.Player;
import tds.main.control.BaseTeamView;

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
        FSRoster dropRoster = null;
        if (dropRosterID == null || dropRosterID.equals("")) {
            dropRoster = (FSRoster)_Session.getHttpSession().getAttribute("dropRoster");
        } else {
            dropRoster = new FSRoster(Integer.parseInt(dropRosterID));
        }

        if (dropRoster == null) {
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

        _Session.getHttpSession().setAttribute("dropRoster",dropRoster);
        _Session.getHttpSession().setAttribute("dropType", dropType);
        
        Player puPlayer = (Player)_Session.getHttpSession().getAttribute("puPlayer");
        
        FSFootballTransaction transaction = new FSFootballTransaction();
        
        transaction.setDropPlayer(dropRoster.getPlayer());
        transaction.setDropPlayerID(dropRoster.getPlayerID());
        transaction.setDropType(dropType.toUpperCase());
        transaction.setFSLeagueID(_FSTeam.getFSLeagueID());
        transaction.setFSSeasonWeek(_CurrentFSSeasonWeek);
        transaction.setFSSeasonWeekID(_CurrentFSSeasonWeek.getFSSeasonWeekID());
        transaction.setFSTeam(_FSTeam);
        transaction.setFSTeamID(_FSTeam.getFSTeamID());
        transaction.setPUPlayer(puPlayer);
        transaction.setPUPlayerID(puPlayer.getPlayerID());
        transaction.setPUType("PU");
        transaction.setTransactionDate(new AuDate());
        transaction.setTransactionType("PU");
        
        _Session.getHttpSession().setAttribute("faTransaction",transaction);
        
        return page;
    }
    
}
