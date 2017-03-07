package tds.playoff.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.control.BaseTeamView;
import tds.playoff.bo.PlayoffGame;
import tds.playoff.bo.PlayoffLeague;
import tds.playoff.bo.PlayoffRound;
import tds.playoff.bo.PlayoffTournament;

public class bracketView extends BaseTeamView {

    final int MAX_ROUNDS_TO_DISPLAY = 5;

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        // If nextPage is something that means an error happened
        if (nextPage != null) { return nextPage; }
        
        nextPage = htmlPage;

         // Get request/session objects        
        int roundId = FSUtils.getIntRequestParameter(request, "rd", 0);
        int nextRoundNo = FSUtils.getIntRequestParameter(request, "nrd", 0);
        int prevRoundNo = FSUtils.getIntRequestParameter(request, "prd", 0);
        
        // Get the current tournament based off the user's league
        PlayoffTournament leagueTournament = new PlayoffLeague(_FSTeam.getFSLeagueID()).getTournament();

        /*
         THIS NEXT SECTION IS TRYING TO DETERMINE WHICH ROUNDS TO RETRIEVE IN ORDER TO DISPLAY THEM. WE'LL ALWAYS DISPLAY UP TO 5 ROUNDS BUT WE NEED TO FIGURE OUT WHICH 
         ROUND TO START FROM. IF THE ROUND ID WAS PASSED IN THEN THAT'S THE ROUND TO START FROM. IF NOT THEN WE CHECK TO SEE IF THE PREVIOUS OR NEXT ROUND NUMBERS WERE 
         PASSED IN (OFF THE ARROW BUTTONS). IF NONE OF THOSE WERE PASSED IN THEN WE CAN JUST GO GET THE CURRENT FSSEASONWEEK OF THE FSSEASON TABLE AND FIGURE IT OUT.
        */       
        PlayoffRound currentRound = PlayoffRound.GetCurrentRound(leagueTournament.getTournamentID());
        int begRoundNumber = 
                (roundId > 0) ? new PlayoffRound(roundId).getRoundNumber() :
                (nextRoundNo > 0) ? nextRoundNo : 
                (prevRoundNo > 0) ? prevRoundNo :
                (currentRound.getFSSeasonWeek() == null) ? leagueTournament.getNumRounds() - MAX_ROUNDS_TO_DISPLAY + 1 :
                (currentRound.getFSSeasonWeek().getFSSeasonWeekNo() + MAX_ROUNDS_TO_DISPLAY <= leagueTournament.getNumRounds()) ? currentRound.getFSSeasonWeek().getFSSeasonWeekNo() : 
                leagueTournament.getNumRounds() - MAX_ROUNDS_TO_DISPLAY + 1;

        // Get FSTeam's current gameID based on the beginning round that we're displaying
        int currentGameID = 0;
        try {
            currentGameID = PlayoffGame.GetGameIDByRound(_FSTeam.getFSTeamID(), begRoundNumber);
        } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
        }

        // Retrieve all of the games to display
        List<PlayoffGame> games = null;
        try {
          games = PlayoffGame.GetTournamentGames(leagueTournament.getTournamentID(), begRoundNumber, begRoundNumber + MAX_ROUNDS_TO_DISPLAY - 1);
        } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
        }

        request.setAttribute("begRoundNumber",begRoundNumber);
        request.setAttribute("currentGameID",currentGameID);
        request.setAttribute("games",games);
        
        return nextPage;

    }
}