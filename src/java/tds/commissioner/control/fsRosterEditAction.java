package tds.commissioner.control;

import bglib.util.FSUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSRoster;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;

public class fsRosterEditAction extends BaseAction {
    
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        nextPage = "fsTeamView.htm";

        FSRoster fsRoster = (FSRoster)session.getHttpSession().getAttribute("commFSRoster");
        if (fsRoster != null)
        {
            int ID = FSUtils.getIntRequestParameter(request,"ID", 0);
            int fsTeamID = FSUtils.getIntRequestParameter(request, "fsTeamID", 0);
            int playerID = FSUtils.getIntRequestParameter(request, "playerID", 0);
            int fsSeasonWeekID = FSUtils.getIntRequestParameter(request, "fsSeasonWeekID", 0);
            String starterState = FSUtils.getRequestParameter(request, "starterState", null);
            String activeState = FSUtils.getRequestParameter(request, "activeState", null);
            
            fsRoster.setFSTeamID(fsTeamID);
            fsRoster.setPlayerID(playerID);
            fsRoster.setFSSeasonWeekID(fsSeasonWeekID);
            if (starterState != null)
            {
                fsRoster.setStarterState(starterState);
            }
            if (activeState != null)
            {
                fsRoster.setActiveState(activeState);
            }
            
            try {
                fsRoster.Save();

                session.getHttpSession().setAttribute("commFSRoster", fsRoster);
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                session.setErrorMessage("Error updating FSRoster.");
            }
            
        }
        
        System.out.println("Done Editing Roster; returning user to " + nextPage);
        return nextPage;
    }
}