package tds.sal.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;
import tds.main.bo.*;
import tds.main.control.BaseTeamView;

/**
 *
 * @author grant.keele
 */
public class playersSelectedView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request,response);

        if (page != null) {
            return page;
        }

        page = htmlPage;

        try {

            FSLeague league = _FSTeam.getFSLeague();
            FSSeason fsseason = league.getFSSeason();
            
            int currentWeekNo = _CurrentFSSeasonWeek.getFSSeasonWeekNo();
            FSSeasonWeek displayWeek = null;
            if (currentWeekNo == 1) {
                displayWeek = _CurrentFSSeasonWeek;
            } else {
                int wk = FSUtils.getIntRequestParameter(request, "wk", 0);
                if (wk > 0) {
                    if (wk >= currentWeekNo) {
                        _Session.setErrorMessage("Error : you cannot look at a week's picks until that week is finished.");
                        displayWeek = fsseason.GetCurrentFSSeasonWeeks().get(currentWeekNo);
                    } else {
                        displayWeek = fsseason.GetCurrentFSSeasonWeeks().get(wk);
                    }
                } else {
                    displayWeek = fsseason.GetCurrentFSSeasonWeeks().get(new Integer(currentWeekNo-1));
                }
            }
            
            SeasonWeek sw = displayWeek.getSeasonWeek();
            if (sw != null) {
                request.setAttribute("dispWeek",displayWeek.getSeasonWeek().getWeekNo());
            }
            
            // Sort params
            String sortParam = FSUtils.getRequestParameter(request, "sort");
            if (sortParam.length()==0) {
                // check for the sorting object from the session
                Object temp = _Session.getHttpSession().getAttribute("listSort");
                if (temp != null) {
                    sortParam = temp.toString();
                } else {
                    sortParam="1_d";
                }
            }
            String sort = resolveSortColumn(sortParam);
            _Session.getHttpSession().setAttribute("listSort", StringEscapeUtils.escapeHtml(sortParam));
            
            List<FSPlayerValueSelected> list = league.GetPlayersSelected(displayWeek.getFSSeasonWeekID(),sort);

            request.setAttribute("playersSelected",list);

            List<FSTeam> leagueTeams = league.GetTeams();
            StringBuilder teamsStr = new StringBuilder();
            int count = 0;
            for (FSTeam team : leagueTeams) {
                count++;
                if (count > 1) {
                    teamsStr.append(",");
                }
                teamsStr.append(team.getFSTeamID());
            }

            request.setAttribute("leagueTeams",teamsStr.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return page;
    }

    public static String resolveSortColumn(String sortParam) {
        String[] cols = { "FSPlayerValueSelected$Count", "Player$LastName", "Position$PositionName", "Team$TeamName", "FootballStats$FantasyPts", "FSPlayerValue$Value" };
        String[] sortParamArr = sortParam.split("_");
        int sortColNo = 0;
        try {
            sortColNo = Integer.parseInt(sortParamArr[0]);
        }
        catch (NumberFormatException e) {}

        String ret;
        if (sortColNo==0) {
            ret = "FSPlayerValueSelected$Count desc";
        } else {
            ret = cols[sortColNo-1];
            if (sortParamArr.length>1) {
                if (sortParamArr[1].compareToIgnoreCase("a")==0 && !ret.endsWith(" asc")) {
                    ret += " asc";
                } else if (sortParamArr[1].compareToIgnoreCase("d")==0 && !ret.endsWith(" desc")) {
                    ret += " desc";
                }
            }
        }

        return ret;
    }

}
