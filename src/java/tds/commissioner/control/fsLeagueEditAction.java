package tds.commissioner.control;

import bglib.util.Application;
import bglib.util.FSUtils;
import tds.main.bo.FSLeague;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class fsLeagueEditAction extends BaseAction {

    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        nextPage = "fsLeagueView";

        FSLeague fsLeague = (FSLeague)session.getHttpSession().getAttribute("commFSLeague");
        if (fsLeague != null)
        {
            int fsSeasonID = FSUtils.getIntRequestParameter(request,"fsSeasonID", 0);
            String leagueName = FSUtils.getRequestParameter(request, "leagueName", null);
            String leaguePassword = FSUtils.getRequestParameter(request, "leaguePassword", null);
            int isFull = FSUtils.getIntRequestParameter(request, "isFull", 0);
            int isPublic = FSUtils.getIntRequestParameter(request, "isPublic", 0);
            int numTeams = FSUtils.getIntRequestParameter(request, "numTeams", 0);
            String description = FSUtils.getRequestParameter(request, "description", null);
            int isGeneral = FSUtils.getIntRequestParameter(request, "isGeneral", 0);
            int startFSSeasonWeekID = FSUtils.getIntRequestParameter(request, "startFSSeasonWeekID", 0);
            int vendorID = FSUtils.getIntRequestParameter(request, "vendorID", 0);
            String draftType = FSUtils.getRequestParameter(request, "draftType", null);
            String draftDateTemp = FSUtils.getRequestParameter(request, "draftDate", null);
            int hasPaid = FSUtils.getIntRequestParameter(request, "hasPaid", 0);
            int isDraftComplete = FSUtils.getIntRequestParameter(request, "isDraftComplete", 0);
            int commissionerUserID = FSUtils.getIntRequestParameter(request, "commissionerUserID", 0);
            int isCustomLeague = FSUtils.getIntRequestParameter(request, "isCustomLeague", 0);
            String scheduleName = FSUtils.getRequestParameter(request, "scheduleName", null);
            int isDefaultLeague = FSUtils.getIntRequestParameter(request, "isDefaultLeague", 0);
            String signupType = FSUtils.getRequestParameter(request, "signupType", null);

            fsLeague.setFSSeasonID(fsSeasonID);
            if (leagueName != null)
            {
                fsLeague.setLeagueName(leagueName);
            }
            if (leaguePassword != null)
            {
                fsLeague.setLeaguePassword(leaguePassword);
            }
            fsLeague.setIsFull(isFull);
            fsLeague.setIsPublic(isPublic);
            fsLeague.setNumTeams(numTeams);
            if (description != null)
            {
                fsLeague.setDescription(description);
            }
            fsLeague.setIsGeneral(isGeneral);
            fsLeague.setStartFSSeasonWeekID(startFSSeasonWeekID);
            fsLeague.setVendorID(vendorID);
            if (draftType != null)
            {
                fsLeague.setDraftType(draftType);
            }
            if (draftDateTemp != null)
            {
                LocalDateTime dt = LocalDateTime.parse(draftDateTemp, Application._DATE_TIME_FORMATTER);
                fsLeague.setDraftDate(dt);
            }
            fsLeague.setHasPaid(hasPaid);
            fsLeague.setIsDraftComplete(isDraftComplete);
            fsLeague.setCommissionerUserID(commissionerUserID);
            fsLeague.setIsCustomLeague(isCustomLeague);
            fsLeague.setScheduleName(scheduleName);
            fsLeague.setIsDefaultLeague(isDefaultLeague);
            fsLeague.setSignupType(signupType);

            try {
                fsLeague.Save();

                session.getHttpSession().setAttribute("commFSLeague", fsLeague);
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                session.setErrorMessage("Error updating FSLeague.");
            }

        }


        return nextPage;
    }
}
