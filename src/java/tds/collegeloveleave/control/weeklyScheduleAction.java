package tds.collegeloveleave.control;

import bglib.util.FSUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.collegeloveleave.bo.CollegeLoveLeave;
import tds.main.bo.CTApplication;
import tds.main.control.BaseAction;

public class weeklyScheduleAction extends BaseAction {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        int fsSeasonWeekId = 0;
        int fsTeamId = 0;
        int teamPickedId = 0;
        int gameId = 0;
        
        String nextPage = super.process(request,response);

        // If nextPage something that means an error happened
        if (nextPage != null) { return nextPage; }

        nextPage = "weeklySchedule.htm";
        
        fsSeasonWeekId = FSUtils.getIntRequestParameter(request,"wk", 0);
        fsTeamId = FSUtils.getIntRequestParameter(request,"fst", 0);
        teamPickedId = FSUtils.getIntRequestParameter(request,"tp", 0);
        gameId = FSUtils.getIntRequestParameter(request,"gid", 0);

        try {
            CollegeLoveLeave.SavePick(fsSeasonWeekId, fsTeamId, gameId, teamPickedId);
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return nextPage;
    }
}
