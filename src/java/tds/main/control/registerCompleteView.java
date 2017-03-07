/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.main.control;

import tds.main.bo.FSUser;
import tds.main.bo.UserSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author grant.keele
 */
public class registerCompleteView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        UserSession session = UserSession.getUserSession(request, response);
        FSUser user = (FSUser)session.getHttpSession().getAttribute("validUser");
        
        user.clearTeams();
        if (user == null) {
            session.setErrorMessage("ERROR : You must first register or be logged in to sign up for a specific game.");
            return "/topdawg/register.htm";
        }

        return page;
    }
    
}
