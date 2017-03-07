package tds.commissioner.control;

import tds.main.bo.UserSession;
import tds.main.bo.FSLeague;
import tds.main.bo.FSFootballDraft;
import bglib.util.FSUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.control.BaseAction;
import tds.main.*;
import tds.main.bo.FSGame;
public class fsGameEditAction extends BaseAction {
    
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        nextPage = "fsGameView";

        FSGame fsGame = (FSGame)session.getHttpSession().getAttribute("commFSGame");
        if (fsGame != null)
        {
            int sportID = FSUtils.getIntRequestParameter(request,"sportID", 0);
            String gameName = FSUtils.getRequestParameter(request, "gameName", null);
            String gameNameShort = FSUtils.getRequestParameter(request, "gameNameShort", null);
            String gamePrefix = FSUtils.getRequestParameter(request, "gamePrefix", null);
            String scoringStyle = FSUtils.getRequestParameter(request, "scoringStyle", null);
            String groupingStyle = FSUtils.getRequestParameter(request, "groupingStyle", null);
            int currentFSSeasonID = FSUtils.getIntRequestParameter(request, "currentFSSeasonID", 0);
            String displayName = FSUtils.getRequestParameter(request, "displayName", null);
            String homeURL = FSUtils.getRequestParameter(request, "homeURL", null);
            String homeURLShort = FSUtils.getRequestParameter(request, "homeURLShort", null);
            
            fsGame.setSportID(sportID);
            if (gameName != null)
            {
                fsGame.setGameName(gameName);
            }
            if (gameNameShort != null)
            {
                fsGame.setGameNameShort(gameNameShort);
            }
            if (gamePrefix != null)
            {
                fsGame.setGamePrefix(gamePrefix);
            }
            if (scoringStyle != null)
            {
                fsGame.setScoringStyle(scoringStyle);
            }
            if (groupingStyle != null)
            {
                fsGame.setGroupingStyle(groupingStyle);
            }
            fsGame.setCurrentFSSeasonID(currentFSSeasonID);
            if (displayName != null)
            {
                fsGame.setDisplayName(displayName);
            }
            if (homeURL != null)
            {
                fsGame.setHomeURL(homeURL);
            }
            if (homeURLShort != null)
            {
                fsGame.setHomeURLShort(homeURLShort);
            }
            
            try {
                fsGame.update();

                session.getHttpSession().setAttribute("commFSGame", fsGame);
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                session.setErrorMessage("Error updating FSGame.");
            }
            
        }
        

        return nextPage;
    }
}