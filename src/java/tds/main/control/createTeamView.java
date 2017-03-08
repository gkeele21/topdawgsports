/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.main.control;

import tds.main.bo.UserSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSSeason;

/**
 *
 * @author grant.keele
 */
public class createTeamView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);
        FSSeason fsSeason = (FSSeason)session.getHttpSession().getAttribute("gameSignupFSSeason");
        if (fsSeason == null) {
            session.setErrorMessage("Error : no valid game was selected.  Please go back and try again.");
            return page;
        }
        
        return page;
    }
    
}
