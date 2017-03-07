/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.main.control;

import bglib.util.FSUtils;
import tds.main.bo.State;
import tds.main.bo.UserSession;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSGame;
import tds.main.bo.FSSeason;

/**
 *
 * @author grant.keele
 */
public class gameSignupView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;
        
        // retrieve list of all the states
        List<State> states = State.getAll();
        System.out.println("# of States : " + states.size());
        request.setAttribute("statesList",states);
        
        UserSession session = UserSession.getUserSession(request, response);
        int fsGameId = FSUtils.getIntRequestParameter(request, "fsGameId", 0);
        
        FSSeason fsSeason = null;
        if (fsGameId < 1) {
            fsSeason = (FSSeason)session.getHttpSession().getAttribute("gameSignupFSSeason");
        } else {
            FSGame fsGame = new FSGame(fsGameId);
            // TODO : Add code here so it grabs the current FSSeason for the Game without the need to store the CurrentFSSeasonID in the FSGame table
            fsSeason = new FSSeason(fsGame.getCurrentFSSeasonID());
            session.getHttpSession().setAttribute("gameSignupFSSeason", fsSeason);
        }
        
        request.setAttribute("gameSignupFSSeason", fsSeason);
        
        return page;
    }
    
}
