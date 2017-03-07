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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSLeague;
import tds.main.bo.FSSeason;
import tds.main.bo.UserSession;
import tds.main.control.BaseCommissionerView;

/**
 *
 * @author grant.keele
 */
public class fsSeasonViewView extends BaseCommissionerView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response, 3);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);
        
        // Create FSLeagues obj
        int fsSeasonID = FSUtils.getIntRequestParameter(request, "fsSeasonID", 0);

        FSSeason fsSeason;
        
        if (fsSeasonID > 0) {
            fsSeason = new FSSeason(fsSeasonID);
            session.getHttpSession().setAttribute("commFSSeasonID", fsSeasonID);
            session.getHttpSession().setAttribute("commFSSeason", fsSeason);
        } else {
            fsSeasonID = FSUtils.getIntSessionAttribute(session.getHttpSession(), "commFSSeasonID", 0);
            fsSeason = (FSSeason)session.getHttpSession().getAttribute("commFSSeason");
        }

        // Retrieve current fsLeagues
        List<FSLeague> fsLeagues = fsSeason.GetLeagues();
        
        session.getHttpSession().setAttribute("fsLeagues", fsLeagues);
        
        return page;
    }
    
}
