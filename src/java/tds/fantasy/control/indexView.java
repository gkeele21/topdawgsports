/*
 * indexView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.fantasy.control;

import bglib.util.FSUtils;
import tds.main.control.BaseView;
import tds.main.bo.CTApplication;
import tds.main.bo.FSUser;
import tds.main.bo.UserSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author grant.keele
 */
public class indexView extends BaseView {
    
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);
        
        // Create FSUser obj
        int userID = FSUtils.getIntRequestParameter(request, "userid", 0);
        String authKey = FSUtils.getRequestParameter(request,"authKey","");

        FSUser user = null;
        if (userID > 0) {
            try {
                user = new FSUser(userID);
            } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
            }
        } else {
            user = (FSUser)session.getHttpSession().getAttribute("validUser");
        }

        if (!authKey.equals("")) {
            if (!user.getAuthenticationKey().equals(authKey)) {
            session.setErrorMessage("ERROR : You must first register or be logged in to sign up for a specific game.");
            return "/topdawg/register.htm";
            }
        }
        if (user != null) {
            session.getHttpSession().setAttribute("validUser", user);
        }
        
        return page;
    }
    
}
