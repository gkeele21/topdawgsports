/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.playoff.scripts;

import bglib.scripts.ResultCode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tds.main.bo.*;

/**
 *
 * @author grant.keele
 */
public class FootballResults  {

    private static final int _FSSeasonID = 55;
    private static final int _AverageJoeTeamID = 520;
    
    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public FootballResults() {
        _Logger = Logger.global;
        _Logger.setLevel(Level.ALL);
    }

    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    public ResultCode getResultCode() { return _ResultCode; }

    public void setScriptArgs(String[] args) { _Args = args; }

    public static void main(String[] args) {
        try {
            FootballResults results = new FootballResults();
            
            results.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void run() {
        
        boolean resultsFinal = false;
        try {
            FSSeason fsseason = new FSSeason(_FSSeasonID);
            
            int fsseasonweekid = fsseason.getCurrentFSSeasonWeekID();

            FSSeasonWeek fsseasonWeek = new FSSeasonWeek(fsseasonweekid);

            calculateResults(fsseason, fsseasonweekid, resultsFinal);

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in tds.ffplayoff.scripts.FootballResults.run()", e);
        }
    }

    public void calculateResults(FSSeason fsseason, int fsseasonweekid, boolean resultsFinal) throws Exception {

        // Calculate week's results
        List<FSLeague> leagues = fsseason.GetLeagues();
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        
        for (FSLeague league : leagues) {
            
            // Get the FFPlayoff record for this league
            FSTournament tournament = FSTournament.getTournamentByFSLeague(league.getFSLeagueID());
            if (tournament != null) {
                
                boolean roundHasAverageJoe = false;
                double teamPoints = 0;
                int numTeams = 0;
                
                // Grab the Current Round for this Playoff game
                FSTournamentRound round = FSTournamentRound.getCurrentRoundByFSSeasonWeek(fsseasonweekid);
                
                // Grab the games for this round
               List<FSTournamentGame> games = FSTournamentGame.GetTournamentGames(tournament.getTournamentID(),round.getRoundNumber(), round.getRoundNumber());
                
                if (games == null) {
                    _Logger.log(Level.INFO, "No games found for Round #" + round.getRoundNumber());
                    continue;
                }
                
                _Logger.log(Level.INFO, "# of games found for Round #" + round.getRoundNumber() + " : " + games.size());
                
                // We have the Average Joe team, which is a computer team with no players.
                // The Average Joe team will get assigned the average number of fantasy
                // points scored by all other teams in that round for that week.
                // Therefore, I need to figure out all other teams' points first
                // and if a game has an Average Joe team then I need to figure out
                // the winner of those games last.
                for (FSTournamentGame game : games) {
                    boolean gameHasAverageJoe = false;
                    int team1Id = game.getFSTeam1ID();
                    int team2Id = game.getFSTeam2ID();
                    
                    if (team1Id < 1 || team2Id < 1) {
                        _Logger.log(Level.INFO, "GameId " + game.getGameID() + " has an empty FSTeamID - skipping game.");
                        continue;
                    }
                    
                    if (team1Id == _AverageJoeTeamID || team2Id == _AverageJoeTeamID) {
                        roundHasAverageJoe = true;
                        gameHasAverageJoe = true;
                    }
                    FSTeam team1 = new FSTeam(team1Id);
                    FSTeam team2 = new FSTeam(team2Id);
                    
                    double team1pts = 0;
                    double team2pts = 0;
                    if (team1Id != _AverageJoeTeamID) {
                        // Figure out team's points
                        team1pts = team1.getWeekFantasyPoints(null, fsseasonweekid, "s.SalFantasyPts", _FSSeasonID);
                        team1pts = Double.valueOf(twoDForm.format(team1pts));

                        teamPoints += team1pts;
                        numTeams++;
                        
                        game.setTeam1Pts(team1pts);
                    }
                    
                    if (team2Id != _AverageJoeTeamID) {
                        // Figure out team's points
                        team2pts = team2.getWeekFantasyPoints(null, fsseasonweekid, "s.SalFantasyPts", _FSSeasonID);
                        team2pts = Double.valueOf(twoDForm.format(team2pts));
                        teamPoints += team2pts;
                        numTeams++;
                        
                        game.setTeam2Pts(team2pts);
                    }
                    
                    if (!gameHasAverageJoe) {
                        // Set the Winner
                        setGameWinner(game);
                    }
                    
                }
                
                // Figure out the # of points for Average Joe
                double averageJoePoints = teamPoints / numTeams;
                averageJoePoints = Double.valueOf(twoDForm.format(averageJoePoints));
                
                System.out.println("Average Joe : " + averageJoePoints);
                
            }
            
        }

    }

    public void setGameWinner(FSTournamentGame game) {
        double team1pts = game.getTeam1Pts();
        double team2pts = game.getTeam2Pts();
        
        if (team1pts > team2pts) {
            game.setWinnerID(game.getFSTeam1ID());
        } else if (team2pts > team1pts) {
            game.setWinnerID(game.getFSTeam2ID());
        } else {
            game.setWinnerID(0);
        }
        
        game.setGameStatus(3);
        FSTournamentGame.SaveGame(game);
    }
    
}
