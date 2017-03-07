package tds.commissioner.control;

import bglib.util.AuDate;
import bglib.util.BGConstants;
import bglib.util.FSUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSTeam;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;
public class fsTeamEditAction extends BaseAction {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        nextPage = "fsTeamView";

        FSTeam fsTeam = (FSTeam)session.getHttpSession().getAttribute("commFSTeam");
        if (fsTeam != null)
        {
            int fsLeagueID = FSUtils.getIntRequestParameter(request,"fsLeagueID", 0);
            int fsUserID = FSUtils.getIntRequestParameter(request, "fsUserID", 0);
            String dateCreated = FSUtils.getRequestParameter(request, "dateCreated", null);
            String teamName = FSUtils.getRequestParameter(request, "teamName", null);
            String isActive = FSUtils.getRequestParameter(request, "isActive", null);
            int division = FSUtils.getIntRequestParameter(request, "division", 0);
            int teamNo = FSUtils.getIntRequestParameter(request, "teamNo", 0);
            int scheduleTeamNo = FSUtils.getIntRequestParameter(request, "scheduleTeamNo", 0);
            String lastAccessed = FSUtils.getRequestParameter(request, "lastAccessed", null);
            String rankDraftMode = FSUtils.getRequestParameter(request, "rankDraftMode", null);
            String isAlive = FSUtils.getRequestParameter(request, "isAlive", null);
                        
            fsTeam.setFSLeagueID(fsLeagueID);
            fsTeam.setFSUserID(fsUserID);
            if (dateCreated != null)
            {
                AuDate dt = AuDate.getInstance(dateCreated, BGConstants.PLAYDATE_PATTERN);
                fsTeam.setDateCreated(dt);
            }
            if (teamName != null)
            {
                fsTeam.setTeamName(teamName);
            }
            if (isActive != null)
            {
                fsTeam.setIsActive(Boolean.parseBoolean(isActive));
            }
            fsTeam.setDivision(division);
            fsTeam.setTeamNo(teamNo);
            fsTeam.setScheduleTeamNo(scheduleTeamNo);
            if (lastAccessed != null)
            {
                AuDate dt = AuDate.getInstance(lastAccessed, BGConstants.PLAYDATE_PATTERN);
                fsTeam.setLastAccessed(dt);
            }
            if (rankDraftMode != null)
            {
                fsTeam.setRankDraftMode(rankDraftMode);
            }
            if (isAlive != null)
            {
                fsTeam.setIsAlive(Boolean.parseBoolean(isAlive));
            }
            
            try {
                //fsTeam.update();

                session.getHttpSession().setAttribute("commFSTeam", fsTeam);
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                session.setErrorMessage("Error updating FSTeam.");
            }
            
        }
        

        return nextPage;
    }
}