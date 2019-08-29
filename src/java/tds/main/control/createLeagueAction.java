package tds.main.control;

import bglib.util.FSUtils;
import bglib.util.FormField;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.*;
import tds.mm.bo.MarchMadnessLeague;
import tds.mm.bo.MarchMadnessTournament;

public class createLeagueAction extends BaseAction {

    public static final int SESSION_TIMEOUT=86400; // 24 hours
    
    public static final FormField LEAGUENAME = new FormField(1, "leagueName", "League Name", 1, 50);
    public static final FormField DESCRIPTION = new FormField(2, "description", "Description", 1, 1000);
    public static final FormField SCOPE = new FormField(3, "publicScope", "Public", 1, 20);
    public static final FormField LEAGUEPASSWORD = new FormField(4, "leaguePassword", "Password", 1, 20);

    public static List<FormField> REG_FIELDS = Arrays.asList(
        LEAGUENAME, DESCRIPTION, SCOPE, LEAGUEPASSWORD
    );

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        String leagueName = FSUtils.getRequestParameter(request, "leagueName");
        String description = FSUtils.getRequestParameter(request, "description");
        String leagueScope = FSUtils.getRequestParameter(request, "publicScope");
        String password = FSUtils.getRequestParameter(request, "leaguePassword");
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
            
            FSLeague league = new FSLeague();
            league.setFSLeagueID(FSUtils.GetHighestIdNumber("FSLeague", "FSLeagueID") + 1);            
            league.setCommissionerUserID(user.getFSUserID());
            league.setDescription(description);
            league.setLeagueName(leagueName);
            league.setLeaguePassword(password);
            league.setFSSeasonID(fsSeason.getFSSeasonID());
            league.setIsFull(0);
            league.setIsCustomLeague(0);
            league.setStatus(FSLeague.Status.FORMING.toString());
            if (("public").equals(leagueScope)) {
                league.setIsPublic(1);
            } else {
                league.setIsPublic(0);
            }
            league.setNumTeams(0);
            
            league.Save();
            
            if (fsSeason.getFSGameID() == FSGame.BRACKET_CHALLENGE || fsSeason.getFSGameID() == FSGame.SEED_CHALLENGE) {
                int tournamentId = MarchMadnessTournament.GetTournamentByYear(Calendar.getInstance().get(Calendar.YEAR)).getTournamentID();
                MarchMadnessLeague.Insert(league.getFSLeagueID(), tournamentId);
            }
            
            String teamName = (String)session.getHttpSession().getAttribute("signupTeamName");
            
            league.AddTeam(user, teamName);
            league.setNumTeams(league.getNumTeams() + 1);            
            league.Save();
            
            user.clearTeams();
            
            nextPage = "userProfile";
        }
        catch (Exception e) {
            // invalid login
            System.out.println("Error : " + e.getMessage());
            if (strNextPage==null || strNextPage.length()==0) {
                nextPage = "joinLeague";
            } else {
                //nextPage = "register.htm?origURL=" + strNextPage;
            }
        }

        return nextPage;
    }
}
