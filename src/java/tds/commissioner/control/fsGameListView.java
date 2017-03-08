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
public class fsGameListView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = this.htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);
        
        // Retrieve fsGames
        List<FSGame> fsGames = FSGame.getFSGames();
        
        session.getHttpSession().setAttribute("fsGames", fsGames);
        
        return page;
    }
    
}
