/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.commissioner.golf.control;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.FSSeason;
import tds.main.bo.PGATournamentWeek;
import tds.main.bo.UserSession;
import tds.main.control.BaseCommissionerView;

/**
 *
 * @author grant.keele
 */
public class golfTournamentWeeksView extends BaseCommissionerView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response, 4);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
        
        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();
        
        FSSeason fsSeason = (FSSeason)session.getAttribute("commFSSeason");

        // Retrieve current PGATournaments
        List<PGATournamentWeek> tournamentWeeks = PGATournamentWeek.ReadAllByFSSeason(fsSeason.getFSSeasonID());
        
        session.setAttribute("tournamentWeeks", tournamentWeeks);
        
        return page;
    }
    
}
