/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.commissioner.golf.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.PGATournamentWeek;
import tds.main.bo.PGATournamentWeekPlayer;
import tds.main.bo.Player;
import tds.main.bo.UserSession;
import tds.main.control.BaseCommissionerView;

/**
 *
 * @author grant.keele
 */
public class golfTournamentWeekFieldEditView extends BaseCommissionerView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
        
        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();
        
        int tournamentID = FSUtils.getIntRequestParameter(request, "PGATournamentID", 0);
        int fsSeasonWeekID = FSUtils.getIntRequestParameter(request, "FSSeasonWeekID", 0);
        
        if (tournamentID == 0)
        {
            tournamentID = Integer.parseInt(""+session.getAttribute("commGolfPGATournamentID"));
        } else
        {
            session.setAttribute("commGolfPGATournamentID", tournamentID);
        }
        if (fsSeasonWeekID == 0)
        {
            fsSeasonWeekID = Integer.parseInt(""+session.getAttribute("commGolfFSSeasonWeekID"));
        } else
        {
            session.setAttribute("commGolfFSSeasonWeekID", fsSeasonWeekID);
        }
        
        PGATournamentWeek tournWeek = new PGATournamentWeek(tournamentID, fsSeasonWeekID);
        
        session.setAttribute("commGolfTournamentWeek", tournWeek);
        
        // Retrieve current field with player values
        List<PGATournamentWeekPlayer> tournamentField = tournWeek.GetField();
        
        session.setAttribute("tournamentField", tournamentField);
        
        // Retrieve the remaining possible players
        List<Player> possiblePlayers = tournWeek.GetPossibleField(tournamentField);
        
        session.setAttribute("possiblePlayers", possiblePlayers);
        
        return page;
    }
    
}
