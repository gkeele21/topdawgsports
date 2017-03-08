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
import tds.main.control.BaseView;
import tds.main.bo.CTApplication;
import tds.main.bo.FSSeason;
import tds.main.bo.FSGame;
import tds.main.bo.UserSession;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author grant.keele
 */
public class fsGameEditView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = this.htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);
        
        int fsGameID = FSUtils.getIntRequestParameter(request, "fsGameID", 0);

        FSGame fsGame = null;
        if (fsGameID > 0) {
            try {
                fsGame = new FSGame(fsGameID);
            } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
            }
        } else {
            fsGame = (FSGame)session.getHttpSession().getAttribute("commFSGame");
        }

        session.getHttpSession().setAttribute("commFSGameID", fsGameID);
        
        if (fsGame != null) {
            session.getHttpSession().setAttribute("commFSGame", fsGame);
        }
        
        return page;
    }
    
}
