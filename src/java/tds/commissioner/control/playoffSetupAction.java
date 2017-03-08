package tds.commissioner.control;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.*;
import tds.main.control.BaseAction;
import tds.playoff.bo.PlayoffGame;
import tds.playoff.bo.PlayoffLeague;
import tds.playoff.bo.PlayoffRound;
import tds.playoff.bo.PlayoffTournament;
public class playoffSetupAction extends BaseAction {
    
    Random random = new Random();
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        
        int year = Calendar.getInstance().get(Calendar.YEAR);      
        
        int numTeams = 32;
        int seasonId = 18;        
        int begSeasonWeekId = 223;        
        String leagueName = "2014 Fantasy Playoff";
        
        int numRounds = (int)(Math.log(numTeams) / Math.log(2));        
        
        String nextPage = super.process(request,response);
        if (nextPage != null) { return nextPage; }

        nextPage = "playoffSetup.htm";

        // Figure out the next unique ids
        int fsSeasonId = FSUtils.GetHighestIdNumber("FSSeason", "FSSeasonID") + 1;
        int fsSeasonWeekId = FSUtils.GetHighestIdNumber("FSSeasonWeek", "FSSeasonWeekID") + 1;
        int fsLeagueId = FSUtils.GetHighestIdNumber("FSLeague", "FSLeagueID") + 1;
        int tournamentId = FSUtils.GetHighestIdNumber("PlayoffTournament", "TournamentID") + 1;
        int roundId = FSUtils.GetHighestIdNumber("PlayoffRound", "RoundID") + 1;
        int gameId = FSUtils.GetHighestIdNumber("PlayoffGame", "GameID") + 1;
        
        // PLAYOFF TOURNAMENT
        PlayoffTournament tournament = new PlayoffTournament();
        tournament.setTournamentID(tournamentId);
        tournament.setTournamentName("FF Playoff " + year);
        tournament.setStatus(PlayoffTournament.Status.UPCOMING.toString());
        tournament.setNumTeams(numTeams);
        tournament.setNumRounds(numRounds);
        tournament.Save();
      
        // FSSEASON        
        FSSeason fsSeason = new FSSeason();
        fsSeason.setFSSeasonID(fsSeasonId);
        fsSeason.setFSGameID(FSGame.FF_PLAYOFF);                
        fsSeason.setSeasonID(seasonId);
        fsSeason.setIsActive(true);
        fsSeason.setDisplayTeams(true);
        fsSeason.setSeasonName(year + " FF Playoff");
        fsSeason.Save();
        
        int roundNumTeams = numTeams;
        int nextGameId = gameId;
        int gameNumber = 1;
        int nextNextGameId = gameId + (numTeams / 2) - 1;

        
        for (int i=0; i < numRounds; i++ ) {            
            // FSSEASON WEEK
            FSSeasonWeek fsSeasonWeek = new FSSeasonWeek();
            fsSeasonWeek.setFSSeasonWeekID(fsSeasonWeekId + i);
            fsSeasonWeek.setFSSeasonID(fsSeasonId);
            fsSeasonWeek.setSeasonWeekID(begSeasonWeekId + i);
            fsSeasonWeek.setFSSeasonWeekNo(i+ 1);
            fsSeasonWeek.setStatus((i==0) ? SeasonWeek.Status.CURRENT.toString() : FSSeasonWeek.Status.PENDING.toString());
            fsSeasonWeek.setWeekType((i + 1 == 1) ? SeasonWeek.WeekType.INITIAL.toString() : (i + 1 == numRounds) ? SeasonWeek.WeekType.FINAL.toString() : SeasonWeek.WeekType.MIDDLE.toString());
            fsSeasonWeek.Save();
            
            // PLAYOFF ROUND
            PlayoffRound round = new PlayoffRound();
            round.setRoundID(roundId + i);
            round.setTournamentID(tournamentId);
            round.setFSSeasonWeekID(fsSeasonWeekId + i);
            round.setRoundNumber(i + 1);
            round.setRoundName("Round " + round.getRoundNumber());
            round.setNumTeams(roundNumTeams);
            round.Save();
            
            roundNumTeams /= 2;
            
            for (int k=0; k < roundNumTeams; k++ ) {
                // PLAYOFF GAME
                PlayoffGame game = new PlayoffGame();                               
                game.setGameID(nextGameId);
                game.setRoundID(roundId + i);
                game.setGameNumber(gameNumber);
                game.setStatus(PlayoffGame.Status.UPCOMING.toString());
                String nextPosition = PlayoffGame.NextPosition.BOTTOM.toString();
                if ((k + 1) % 2 == 1) {
                    nextNextGameId += 1; 
                    nextPosition=PlayoffGame.NextPosition.TOP.toString();
                } 
                game.setNextGameID((gameNumber == numTeams - 1) ? null : nextNextGameId);
                game.setNextPosition((gameNumber == numTeams - 1) ? null : nextPosition);
                game.Save();
                nextGameId += 1;
                gameNumber += 1;
            }
        }

        // FSLEAGUE
        FSLeague fsLeague = new FSLeague();
        fsLeague.setFSLeagueID(fsLeagueId);
        fsLeague.setFSSeasonID(fsSeasonId);
        fsLeague.setLeagueName(leagueName);
        fsLeague.setIsDefaultLeague(1);
        fsLeague.Save();

        // FSGAME
        FSGame fsGame = new FSGame(FSGame.FF_PLAYOFF);
        fsGame.setCurrentFSSeasonID(fsSeasonId);
        fsGame.Save();
        
        // PLAYOFF LEAGUE
        PlayoffLeague.Insert(fsLeagueId, tournamentId);
    
        // ASSIGN TO BRACKET
        try {
            CachedRowSet crs = GetLeagueTeams(fsLeagueId);
            Map<Integer,Integer> bracketTeams = new HashMap();

            while (crs.next()) {
                List<Integer> numUserTeams = GetUserTeams(crs.getInt("FSUserID"), fsLeagueId);
                if (numUserTeams.isEmpty()) { break; }
                int min = 0;
                int bracketSpacer = numTeams / numUserTeams.size();
                System.out.println("USER ID : " + crs.getInt("FSUserID"));

                for (int i=0; i < numUserTeams.size(); i++ ) { 
                    boolean isPlaced = false;
                    while (isPlaced == false) {                            
                        int position = GetRandomNumber(min, min + bracketSpacer);
                        if (!bracketTeams.containsKey(position)) {
                            System.out.println("Place team " + numUserTeams.get(i) + " in position " + position); 
                            bracketTeams.put(position, numUserTeams.get(i));
                            isPlaced = true;
                        }
                    }
                    min += bracketSpacer;
                }
            }
                        
            System.out.println("FINAL RESULTS");
            for (int i=0; i < numTeams; i++ ) {
                if (bracketTeams.get(i) == null) {
                    bracketTeams.put(i, PlayoffTournament.AverageJoe);
                }
                System.out.println("Team " + i + ": " + bracketTeams.get(i));
            }
            
            List<PlayoffGame> games = PlayoffGame.GetTournamentGames(tournamentId, 1, 1);
            int teamNumber = 1;
            for (int i=0; i < games.size(); i++ ) {
                games.get(i).setFSTeam1ID(bracketTeams.get(teamNumber));
                teamNumber+=1;
                games.get(i).setFSTeam2ID(bracketTeams.get(teamNumber));
                teamNumber+=1;
                games.get(i).Save();
            } 
        }
        
        catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        
        return nextPage;
    }
    
    private CachedRowSet GetLeagueTeams(int fsLeagueId) {
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT FSUserID, count(FSUserID) NumTeams ");
        sql.append("FROM FSTeam ");
        sql.append("WHERE FSLeagueID = ").append(fsLeagueId).append(" ");
        sql.append("GROUP BY FSUserID ");
        sql.append("ORDER BY NumTeams desc, FSUserID");        

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
        } 
        catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            JDBCDatabase.closeCRS(crs);
        }
        return crs;
    }
    
    private List<Integer> GetUserTeams(int fsUserId, int fsLeagueId) {
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();
        List<Integer> teamsIds = new ArrayList<Integer>();

        sql.append("SELECT FSTeamID ");
        sql.append("FROM FSTeam ");
        sql.append("WHERE FSUserID = ").append(fsUserId).append(" AND FSLeagueID = ").append(fsLeagueId).append(" ");
        sql.append("ORDER BY rand()");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                teamsIds.add(crs.getInt("FSTeamID"));
            }
        } 
        catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            JDBCDatabase.closeCRS(crs);
        }
        return teamsIds;   
    }
    
    private int GetRandomNumber(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}