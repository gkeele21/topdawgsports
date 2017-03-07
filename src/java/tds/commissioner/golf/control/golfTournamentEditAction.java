package tds.commissioner.golf.control;

import bglib.util.FSUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.PGATournament;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;

public class golfTournamentEditAction extends BaseAction {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        String prefix = "";
        if ("local".equals(this.host))
        {
            prefix = "commissioner/golf/";
        }

        nextPage = prefix + "golfTournaments";

//        FSSeason fsSeason = (FSSeason)session.getHttpSession().getAttribute("commFSSeason");
//        PGATournament tournament = (PGATournament)session.getHttpSession().getAttribute("commPGATournament");
        
        int tournamentID = FSUtils.getIntRequestParameter(request,"PGATournamentID", 0);
        
        PGATournament tournament;
        if (tournamentID > 0)
        {
            tournament = new PGATournament(tournamentID);
        } else
        {
            tournament = new PGATournament();
        }
        
        String tournamentName = FSUtils.getRequestParameter(request, "tournamentName", null);
        String tournamentNameShort = FSUtils.getRequestParameter(request, "tournamentNameShort", null);
        String startDateTemp = FSUtils.getRequestParameter(request, "startDate", null);
        String endDateTemp = FSUtils.getRequestParameter(request, "endDate", null);
        int active = FSUtils.getIntRequestParameter(request, "active", 0);
        int defendingChampionID = FSUtils.getIntRequestParameter(request, "defendingChampionID", 0);
        String leaderboardURL = FSUtils.getRequestParameter(request, "leaderboardURL", null);
        int numRounds = FSUtils.getIntRequestParameter(request, "numRounds", 0);
        String externalTournamentID = FSUtils.getRequestParameter(request, "externalTournamentID", null);

        if (tournamentName != null)
        {
            tournament.setTournamentName(tournamentName);
        }
        if (tournamentNameShort != null)
        {
            tournament.setTournamentNameShort(tournamentNameShort);
        }
        if (leaderboardURL != null)
        {
            tournament.setLeaderboardURL(leaderboardURL);
        }
        tournament.setActive(active);
        tournament.setDefendingChampionID(defendingChampionID);
        tournament.setNumRounds(numRounds);
        if (externalTournamentID != null)
        {
            tournament.setExternalTournamentID(externalTournamentID);
        }
        
        try {
            int result = tournament.Save();
//            session.getHttpSession().setAttribute("commPGATournament", tournament);
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            session.setErrorMessage("Error saving PGA Tournament.");
        }
            

        return nextPage;
    }
}