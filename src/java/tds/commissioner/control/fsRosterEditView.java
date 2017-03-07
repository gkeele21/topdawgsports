/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.commissioner.control;

import bglib.tags.ListBoxItem;
import bglib.util.FSUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.*;
import tds.main.control.BaseView;

/**
 *
 * @author grant.keele
 */
public class fsRosterEditView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = this.htmlPage;
        
        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();
        
        FSLeague fsLeague = (FSLeague)session.getAttribute("commFSLeague");
        int fsRosterID = FSUtils.getIntRequestParameter(request, "fsRosterID", 0);

        FSRoster fsRoster = null;
        if (fsRosterID > 0) {
            try {
                fsRoster = new FSRoster(fsRosterID);
            } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
            }
        } else {
            fsRoster = (FSRoster)session.getAttribute("commFSRoster");
        }

        session.setAttribute("commFSRosterID", fsRosterID);
        
        if (fsRoster != null) {
            session.setAttribute("commFSRoster", fsRoster);
        }
        
        // Players
        try {

            List<Player> players = Player.getPlayerList(null, " ps.PositionName,tm.DisplayName,p.LastName", null, fsLeague.getFSSeason().getSeasonID(),false);
            List<ListBoxItem> items = new ArrayList<ListBoxItem>();
            for (Player player : players) {
                boolean selected = false;
                if (player.getPlayerID() == fsRoster.getPlayerID())
                {
                    selected = true;
                }
                items.add(new ListBoxItem("(" + player.getPosition().getPositionName() + ") " + player.getTeam().getAbbreviation() + " - " + player.getFullName(), ""+player.getPlayerID(), selected));
            }
            session.setAttribute("playerList", items);
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        return page;
    }
    
}
