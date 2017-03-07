/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.main.control;

import bglib.util.AuUtil;
import bglib.util.FSUtils;
import tds.main.bo.UserSession;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSLeague;
import tds.main.bo.FSSeason;

/**
 *
 * @author grant.keele
 */
public class joinLeagueView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;
        
        UserSession session = UserSession.getUserSession(request, response);
        FSSeason fsSeason = (FSSeason)session.getHttpSession().getAttribute("gameSignupFSSeason");
        if (fsSeason == null) {
            session.setErrorMessage("Error : could not retrieve leagues for this game.  Please go back and try again.");
            return page;
        }
        
        String teamName = FSUtils.getRequestParameter(request, "teamName", "");
        
        if (AuUtil.isEmpty(teamName)) {
            teamName = (String)session.getHttpSession().getAttribute("signupTeamName");
        } else {
            session.getHttpSession().setAttribute("signupTeamName", teamName);
        }
        
        // retrieve list available leagues in this season
        List<FSLeague> leagues = fsSeason.GetLeagues();
        System.out.println("# of Leagues : " + leagues.size());
        request.setAttribute("leaguesList", leagues);
        
        // retrieve the generic leagueId for this season
        int genericLeagueId = 0;
        for (FSLeague league : leagues) {
            if (league.getIsDefaultLeague() == 1) {
                genericLeagueId = league.getFSLeagueID();
                System.out.println("Found Default League : " + league.getFSLeagueID());
                break;
            }
        }
        
        request.setAttribute("genericLeagueId", genericLeagueId);
        
        return page;
    }
    
}
