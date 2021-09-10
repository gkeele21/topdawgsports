package tds.commissioner.fantasy.control;

import bglib.util.FSUtils;
import tds.main.bo.FSFootballDraft;
import tds.main.bo.FSLeague;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class fsLeague_modifyDraftAction extends BaseAction {

    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        nextPage = "fsLeague_modifyDraft";

        int round = FSUtils.getIntRequestParameter(request,"round", 0);
        int place = FSUtils.getIntRequestParameter(request,"place", 0);
        int teamid = FSUtils.getIntRequestParameter(request,"team", 0);
        int playerid = FSUtils.getIntRequestParameter(request,"player", 0);
        FSLeague fsLeague = (FSLeague)session.getHttpSession().getAttribute("commFSLeague");
        int fsLeagueId = fsLeague.getFSLeagueID();
        if (place == 0 || teamid == 0 || playerid == 0) {
            session.setErrorMessage("ERROR: Please select all values.");
            return nextPage;
        }

        try {
            FSFootballDraft.insertPick(round, place, teamid, playerid, fsLeagueId);

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            session.setErrorMessage("Error processing team pick.");
        }

        return nextPage;
    }
}
