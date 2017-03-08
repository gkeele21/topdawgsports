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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.bo.Country;
import tds.main.bo.Player;
import tds.main.bo.UserSession;
import tds.main.control.BaseCommissionerView;

/**
 *
 * @author grant.keele
 */
public class golfPlayerEditView extends BaseCommissionerView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = this.htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);
        
        try {
            int playerID = FSUtils.getIntRequestParameter(request, "playerID", 0);

            if (playerID > 0)
            {
                Player player = new Player(playerID);

                if (player != null)
                {
                    request.setAttribute("player", player);
                }
            }
            request.setAttribute("playerID", playerID);

            // get list of countries
            List<Country> countries = Country.ReadAll(null);
            
            request.setAttribute("countries", countries);
            
        } catch (Exception ex) {
            CTApplication._CT_LOG.error(request, ex);
        }
        

        return page;
    }
    
}
