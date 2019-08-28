package tds.salelim.control;

import tds.sal.control.*;
import bglib.util.FSUtils;
import tds.main.bo.FSRoster;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.FSTeam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import tds.main.control.BaseTeamView;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Apr 24, 2006
 * Time: 11:39:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class teamrosterView extends BaseTeamView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        nextPage = htmlPage;
        
        try {
            int teamid = FSUtils.getIntRequestParameter(request,"teamid",0);
//            int fsseasonweekid = FSUtils.getIntRequestParameter(request, "wkid", 0);

            FSTeam team = new FSTeam(teamid);
            
            if (teamid > 0) {
                _Session.getHttpSession().setAttribute("rosterteam", team);
            }

//            if (fsseasonweekid > 0) {
//                FSSeasonWeek week = new FSSeasonWeek(fsseasonweekid);
//                request.setAttribute("rosterWeek",week);
//            }
            FSSeasonWeek fsSeasonWeek = (FSSeasonWeek) _Session.getHttpSession().getAttribute("saldisplayfsseasonweek");
            int fsseasonweekid = fsSeasonWeek.getFSSeasonWeekID();
            List<FSRoster> roster = team.getRoster(fsseasonweekid);
            request.setAttribute("teamRoster", roster);
            request.setAttribute("rosterWeek", fsSeasonWeek);
            
//            String descriptor = FSUtils.getRequestParameter(request, "descriptor");
//            if (descriptor.length()==0) {
//                //descriptor = "SUM_" + FSGlobals._FSGlobals.getStatsYear();
//                descriptor = prevWeekInfo.getStartDate().toString(FSConstants.PLAYDATE_PATTERN);
//            }
//
//            List<ListBoxItem> list = new ArrayList<ListBoxItem>();
//            int maxYear = Math.max(FSGlobals._FSGlobals.getStatsYear(), 2003); // ensure no infinite loop in next line
//            for (int year=maxYear; year>=2003; year--) {
//                String addStr = "SUM_" + year;
//                list.add(new ListBoxItem(String.valueOf(year) + " (total)", addStr, addStr.equals(descriptor)));
//                addStr = "AVG_" + year;
//                list.add(new ListBoxItem(String.valueOf(year) + " (avg)", addStr, addStr.equals(descriptor)));
//            }
//            session.getHttpSession().setAttribute("listItems",list);
//            session.getHttpSession().setAttribute("statsDescriptor", StringEscapeUtils.escapeHtml(descriptor));
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return nextPage;
    }

}
