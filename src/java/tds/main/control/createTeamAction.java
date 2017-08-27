package tds.main.control;

import bglib.util.AuUtil;
import bglib.util.FSUtils;
import bglib.util.FormField;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSLeague;
import tds.main.bo.FSUser;
import tds.main.bo.UserSession;

public class createTeamAction extends BaseAction {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        int userId = FSUtils.getIntRequestParameter(request, "userId", 0);
        String teamName = FSUtils.getRequestParameter(request, "pass", "");
        int leagueId = FSUtils.getIntRequestParameter(request, "leagueId", 0);
        
        String nextURL = FSUtils.getRequestParameter(request, "nextURL");
        String origURL = FSUtils.getRequestParameter(request, "origURL");
        String strNextPage = origURL;
        if (strNextPage == null && strNextPage.equals("")) {
            strNextPage = FSUtils.stringValue(session.getHttpSession().getAttribute("origURL"),true);
        }
        if (strNextPage==null || strNextPage.length()==0) {
            strNextPage = "userProfile";
        }

        try {

            // check if the username requested already exists
            if (userId <= 0) {
                System.out.println("Error creating team : Pass in a valid userId.");
                UserSession.getUserSession(request, response).setErrorMessage("Invalid userId passed in.");
                return strNextPage;
            }

            FSUser user = new FSUser(userId);
            
            FSLeague league = new FSLeague(leagueId);
            
            if (FSUtils.isEmpty(league.getLeagueName())) {
                System.out.println("Error getting league info : Pass in a valid leagueId.");
                UserSession.getUserSession(request, response).setErrorMessage("Invalid leagueId passed in.");
                return strNextPage;
            }
            
            String teamname = user.getFullName();
            int fsteamid = league.AddTeam(user, teamname);

            if (fsteamid <= 0) {
                System.out.println("Error creating team.");
                UserSession.getUserSession(request, response).setErrorMessage("Error creating team.");
                return strNextPage;
            }

            if (!AuUtil.isEmpty(nextURL)) {
                strNextPage = nextURL;
            }
        }
        catch (Exception e) {
            // invalid login
            System.out.println("Error : " + e.getMessage());
            UserSession.getUserSession(request, response).setErrorMessage("Invalid Username or Password");
        }

        System.out.println("CreateUserAction: redirecting to " + strNextPage);
        return strNextPage;
    }
}
