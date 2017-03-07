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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.PGATournament;
import tds.main.bo.UserSession;
import tds.main.control.BaseCommissionerView;

/**
 *
 * @author grant.keele
 */
public class golfTournamentEditView extends BaseCommissionerView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = this.htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);
        
        int tournamentID = FSUtils.getIntRequestParameter(request, "PGATournamentID", 0);
        
        if (tournamentID > 0)
        {
            PGATournament tournament = new PGATournament(tournamentID);
            
            if (tournament != null)
            {
                request.setAttribute("tournament", tournament);
            }
        }
        request.setAttribute("PGATournamentID", tournamentID);

        return page;
    }
    
}
