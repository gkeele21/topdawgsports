/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.commissioner.control;

import bglib.util.FSUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.bo.FSGame;
import tds.main.bo.FSTeam;
import tds.main.bo.UserSession;
import tds.main.control.BaseView;

/**
 *
 * @author grant.keele
 */
public class fsTeamEditView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = this.htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);
        
        int fsTeamID = FSUtils.getIntRequestParameter(request, "fsTeamID", 0);

        FSTeam fsTeam = null;
        if (fsTeamID > 0) {
            try {
                fsTeam = new FSTeam(fsTeamID);
            } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
            }
        } else {
            fsTeam = (FSTeam)session.getHttpSession().getAttribute("commFSTeam");
        }

        session.getHttpSession().setAttribute("commFSTeamID", fsTeamID);
        
        if (fsTeam != null) {
            session.getHttpSession().setAttribute("commFSTeam", fsTeam);
        } else
        {
            System.out.println("CommFSTeam is null");
            
        }
        
        return page;
    }
    
}
