package tds.main.control;

import bglib.util.AuUtil;
import bglib.util.FSUtils;
import bglib.util.FormField;
import tds.main.bo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class joinLeagueAction extends BaseAction {

    public static final int SESSION_TIMEOUT=86400; // 24 hours

    public static final FormField LEAGUEID = new FormField(1, "leagueId", "League Id", 1, 50);

    public static List<FormField> REG_FIELDS = Arrays.asList(
        LEAGUEID
    );

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);
        String strNextPage = FSUtils.getRequestParameter(request, "origURL");
        if (strNextPage==null || strNextPage.length()==0) {
            strNextPage = "joinLeague";
        }

        try {
            FSUser user = (FSUser)session.getHttpSession().getAttribute("validUser");

            if (user == null) {
                session.setErrorMessage("Error : you must be logged in.  Please go back and try again.");
                return strNextPage;
            }

            FSSeason fsSeason = (FSSeason)session.getHttpSession().getAttribute("gameSignupFSSeason");
            if (fsSeason == null) {
                session.setErrorMessage("Error : no Game selected.  Please go back and try again.");
                return strNextPage;
            }

            String leagueString = FSUtils.getRequestParameter(request, "leagueId", "");

            if (AuUtil.isEmpty(leagueString)) {
                session.setErrorMessage("Error : You must select a valid league to join.  Please go back and try again.");
                return strNextPage;
            }

            String[] leagueParams = leagueString.split("_");
            if (leagueParams.length < 2) {
                session.setErrorMessage("Error : You must select a valid league to join.  Please go back and try again.");
                return strNextPage;
            }

            int leagueId = Integer.parseInt(leagueParams[0]);
            String scope = leagueParams[1];

            if (leagueId < 1) {
                session.setErrorMessage("Error : no league selected.  Please go back and try again.");
                return strNextPage;
            }

            FSLeague league = new FSLeague(leagueId);

            // check for league password
            if (("private").equals(scope)) {
                String leaguePass = FSUtils.getRequestParameter(request, "leaguePassword", "");

                if (!leaguePass.equals(league.getLeaguePassword())) {
                    session.setErrorMessage("Error : Invalid league password. Please try again.");
                    return strNextPage;
                }
            }

            // check if user already has team in league, except in FFPlayoff Game
            FSGame fsGame = fsSeason.getFSGame();
            if (fsGame.getFSGameID() != FSGame.FF_PLAYOFF && fsGame.getFSGameID() != FSGame.BRACKET_CHALLENGE && fsGame.getFSGameID() != FSGame.SEED_CHALLENGE)
            {
                if (league.GetUserAlreadyInLeague(user.getFSUserID())) {
                    session.setErrorMessage("Error : You already have a team in that league. Please choose a different league to join.");
                    return strNextPage;
                }
            }

            FSTeam fsTeam = new FSTeam();
            fsTeam.setFSTeamID(FSUtils.GetHighestIdNumber("FSTeam", "FSTeamID") + 1);
            fsTeam.setFSLeagueID(leagueId);
            fsTeam.setFSUserID(user.getFSUserID());
            fsTeam.setTeamName((String)session.getHttpSession().getAttribute("signupTeamName"));
            fsTeam.setIsActive(true);
            fsTeam.setDateCreated(LocalDateTime.now());
            fsTeam.Save();

            league.setNumTeams((league.getNumTeams()!= null) ? league.getNumTeams() + 1 : 1);
            league.Save();

            nextPage = "userProfile";
        }
        catch (Exception e) {
            // invalid login
            session.setErrorMessage("Error joining league, please notify system administrator: ");
            System.out.println("Error : " + e.getMessage());
            if (strNextPage==null || strNextPage.length()==0) {
                nextPage = "joinLeague";
            }
        }

        return nextPage;
    }
}
