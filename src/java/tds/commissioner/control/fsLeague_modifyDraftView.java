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
import tds.main.control.BaseView;
import tds.main.bo.UserSession;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.FSFootballDraft;
import tds.main.bo.FSLeague;
import tds.main.bo.FSTeam;
import tds.main.bo.Player;

/**
 *
 * @author grant.keele
 */
public class fsLeague_modifyDraftView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
        
        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();
        
        int fsLeagueID = FSUtils.getIntSessionAttribute(session, "commFSLeagueID", 0);
        if (fsLeagueID <= 0)
        {
            userSession.setErrorMessage("You have to select a league first.");
            return "fsLeagueView";

        }
        FSLeague fsLeague = (FSLeague)session.getAttribute("commFSLeague");
        
        String action = FSUtils.getRequestParameter(request, "action", "");
        if (action.equals("delete"))
        {
            int round = FSUtils.getIntRequestParameter(request, "round", 0);
            int place = FSUtils.getIntRequestParameter(request, "place", 0);
            
            if (round > 0 && place > 0)
            {
                FSFootballDraft draftEntry = new FSFootballDraft(fsLeagueID, round, place);
                
                int retVal = draftEntry.deletePick();
            }
        }

        // Retrieve draft results
        List<FSFootballDraft> leagueDraft = FSFootballDraft.getDraftResults(fsLeagueID);
        session.setAttribute("leagueDraft", leagueDraft);

        // Round
        List<ListBoxItem> items = new ArrayList<ListBoxItem>();
        for (int x=0; x<=20; x++) {
            items.add(new ListBoxItem(""+x, ""+x, false));
        }
        session.setAttribute("roundList", items);

        // Place
        items = new ArrayList<ListBoxItem>();
        for (int x=1; x<=10; x++) {
            items.add(new ListBoxItem(""+x, ""+x, false));
        }
        session.setAttribute("placeList", items);

        // Retrieve league teams
        List<FSTeam> teams = fsLeague.GetTeams();
        items = new ArrayList<ListBoxItem>();
        for (FSTeam team : teams) {
            items.add(new ListBoxItem(team.getTeamName(), ""+team.getFSTeamID(), false));
        }
        session.setAttribute("teamList", items);

        // Players
        try {

            List<Player> players = Player.getPlayerList(null, " ps.PositionName,tm.DisplayName,p.LastName", null, fsLeague.getFSSeason().getSeasonID(),false);
            items = new ArrayList<ListBoxItem>();
            for (Player player : players) {
                items.add(new ListBoxItem("(" + player.getPosition().getPositionName() + ") " + player.getTeam().getAbbreviation() + " - " + player.getFullName(), ""+player.getPlayerID(), false));
            }
            session.setAttribute("playerList", items);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }
    
}
