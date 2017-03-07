package tds.commissioner.control;

import bglib.util.FSUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSSeason;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;
public class fsSeasonEditAction extends BaseAction {
    
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        nextPage = "fsSeasonView";

        FSSeason fsSeason = (FSSeason)session.getHttpSession().getAttribute("commFSSeason");
        if (fsSeason != null)
        {
            int fsGameID = FSUtils.getIntRequestParameter(request,"fsGameID", 0);
            int seasonID = FSUtils.getIntRequestParameter(request, "seasonID", 0);
            String seasonName = FSUtils.getRequestParameter(request, "seasonName", null);
            String isActive = FSUtils.getRequestParameter(request, "isActive", null);
            String displayTeams = FSUtils.getRequestParameter(request, "displayTeams", null);
            String displayStatsYear = FSUtils.getRequestParameter(request, "displayStatsYear", null);
            int currentFSSeasonWeekID = FSUtils.getIntRequestParameter(request, "currentFSSeasonWeekID", 0);
            
            fsSeason.setFSGameID(fsGameID);
            fsSeason.setSeasonID(seasonID);
            if (seasonName != null)
            {
                fsSeason.setSeasonName(seasonName);
            }
            if (isActive != null)
            {
                fsSeason.setIsActive(Boolean.parseBoolean(isActive));
            }
            if (displayTeams != null)
            {
                fsSeason.setDisplayTeams(Boolean.parseBoolean(displayTeams));
            }
            if (displayStatsYear != null)
            {
                fsSeason.setDisplayStatsYear(displayStatsYear);
            }
            fsSeason.setCurrentFSSeasonWeekID(currentFSSeasonWeekID);

            try {
                fsSeason.Save();

                session.getHttpSession().setAttribute("commFSSeason", fsSeason);
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                session.setErrorMessage("Error updating FSGame.");
            }
            
        }
        

        return nextPage;
    }
}