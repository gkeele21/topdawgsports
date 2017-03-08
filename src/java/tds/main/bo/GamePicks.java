package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.mm.bo.BracketChallenge;

public class GamePicks implements Serializable {

    // DB FIELDS
    private int _GamePicksID;
    private int _FSSeasonWeekID;
    private int _FSTeamID;
    private int _GameID;
    private int _TeamPickedID;

    // OBJECTS
    private FSSeasonWeek _FSSeasonWeek;
    private FSTeam _FSTeam;
    private Game _Game;
    private Team _TeamPicked;

    // ADDITIONAL FIELDS
    private boolean _isPickCorrect;
    private FSTournament _FSTournament; // Comes from the Seed Challenge game
    private FSTournamentSeed _FSTournamentSeed; // Comes from the Seed Challenge game
    private FSSeedChallengeGroup _FSSeedChallengeGroup; // Comes from the Seed Challenge game    

    // CONSTRUCTORS
    public GamePicks() {
    }

    public GamePicks(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS
    public int getGamePicksID() {return _GamePicksID;}
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public int getFSTeamID() {return _FSTeamID;}
    public int getGameID() {return _GameID;}
    public int getTeamPickedID() {return _TeamPickedID;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}
    public FSTeam getFSTeam() {if (_FSTeam == null && _FSTeamID > 0) {_FSTeam = new FSTeam(_FSTeamID);}return _FSTeam;}
    public Game getGame() {if (_Game == null && _GameID > 0) {_Game = new Game(_GameID);}return _Game;}
    public Team getTeamPicked() {if (_TeamPicked == null && _TeamPickedID > 0) {_TeamPicked = new Team(_TeamPickedID);}return _TeamPicked;}
    public boolean getIsPickCorrect() {return _isPickCorrect;}
    public FSTournament getFSTournament() {return _FSTournament;}
    public FSTournamentSeed getFSTournamentSeed() {return _FSTournamentSeed;}
    public FSSeedChallengeGroup getFSSeedChallengeGroup() {return _FSSeedChallengeGroup;}
    
    // SETTERS
    public void setGamePicksID(int GamePicksID) {_GamePicksID = GamePicksID;}
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setFSTeamID(int FSTeamID) {_FSTeamID = FSTeamID;}
    public void setGameID(int GameID) {_GameID = GameID;}
    public void setTeamPickedID(int TeamPickedID) {_TeamPickedID = TeamPickedID;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setGame(Game Game) {_Game = Game;}
    public void setTeamPicked(Team TeamPicked) {_TeamPicked = TeamPicked;}
    public void setIsPickCorrect(boolean IsCorrect) {_isPickCorrect = IsCorrect;}
    public void setFSTournament(FSTournament FSTournament) {_FSTournament = FSTournament;}
    public void setFSTournamentSeed(FSTournamentSeed FSTournamentSeed) {_FSTournamentSeed = FSTournamentSeed;}
    public void setFSSeedChallengeGroup(FSSeedChallengeGroup FSSeedChallengeGroup) {_FSSeedChallengeGroup = FSSeedChallengeGroup;}

    // PUBLIC METHODS

    /* This method removes a pick out of the DB. */
     public static int DeletePick(int gamePicksId) {

        int retVal = -1;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("DELETE FROM GamePicks ");
	sql.append("WHERE GamePicksID = ").append(gamePicksId);

        try {
            retVal = CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }
     
     /*  This method calculates the standings for the bracket challenge game for a given week (round) */
    public static int CalculateBracketChallengeStandings(int weekNo, int fsGameId) {
        
        int retVal = 0;
        /*
        List<FSFootballStandings> objPriorStandings = null;
        List<BracketChallenge> picks = null;
        double gamePoints = 0;
        int gamesCorrect = 0;
        int gamesWrong = 0;
        double prevGamePointsTotal = 0;
        int prevGamesCorrectTotal = 0;
        int prevGamesWrongTotal = 0;
        int fsTeamId = 0;
        int fsSeasonWeekId = 0;
        int tournamentId = 5;

        try {
            // Grab all the active leagues for this game
            List<FSLeague> fsLeagues = FSLeague.GetActiveLeaguesByGame(fsGameId);

            for (int leagueCount=0; leagueCount < fsLeagues.size(); leagueCount++) {

                // Grab all of the teams in the league
                List<FSTeam> fsTeams = FSTeam.GetTeams(fsLeagues.get(leagueCount).getFSLeagueID());

                // Grab all standing records for the week prior (Doing it this way rather than calling a query for each individual team)
                if ((weekNo - 1) > 0) {
                    objPriorStandings = FSFootballStandings.GetWeeklyStandings(fsGameId, weekNo -1);
                }

                for (int teamCount=0; teamCount < fsTeams.size(); teamCount++) {

                    FSFootballStandings objStandings = new FSFootballStandings();
                    
                    fsTeamId = fsTeams.get(teamCount).getFSTeamID();
                    
                    // Grab the user's picks for the round
                    picks = BracketChallenge.GetBracketChallengePicks(tournamentId, fsTeamId, 0, weekNo);

                    for (int pickCount=0; pickCount < picks.size(); pickCount++) {
                        
                        if (picks.get(pickCount).getPredictedWinner() != null && picks.get(pickCount).getPredictedWinner().getTeamID() == picks.get(pickCount).getFSTournamentGame().getWinnerID()) {
                            gamesCorrect += 1;
                            gamePoints += 1 * (Math.pow(2, (weekNo -1))); 
                        }
                        else {
                            if (picks.get(pickCount).getFSTournamentGame().getWinnerID() > 0) {
                                gamesWrong +=1;
                            }                            
                        }
                        
                        fsSeasonWeekId = picks.get(pickCount).getFSTournamentGame().getFSTournamentRound().getFSSeasonWeekID();
                    }     

                    // Calculate yearly totals
                    if (objPriorStandings != null) {
                        for (int standingsCount=0; standingsCount < objPriorStandings.size(); standingsCount++) {
                            // Make sure we have the right fsTeam
                            if (fsTeamId == objPriorStandings.get(standingsCount).getFSTeamID()) {
                                prevGamePointsTotal = objPriorStandings.get(standingsCount).getTotalGamePoints();
                                prevGamesCorrectTotal = objPriorStandings.get(standingsCount).getTotalGamesCorrect();
                                prevGamesWrongTotal = objPriorStandings.get(standingsCount).getTotalGamesWrong();
                            }
                        }
                    }

                    // Set the Standings object
                    objStandings.setFSTeamID(fsTeamId);
                    objStandings.setFSSeasonWeekID(fsSeasonWeekId);
                    objStandings.setGamePoints(gamePoints);
                    objStandings.setGamesCorrect(gamesCorrect);
                    objStandings.setGamesWrong(gamesWrong);
                    objStandings.setTotalGamePoints(prevGamePointsTotal + gamePoints);
                    objStandings.setTotalGamesCorrect(prevGamesCorrectTotal + gamesCorrect);
                    objStandings.setTotalGamesWrong(prevGamesWrongTotal + gamesWrong);

                    FSFootballStandings.SaveStandings(objStandings);

                    // Reset the variables
                    gamePoints = 0;
                    gamesCorrect = 0;
                    gamesWrong =0;
                    prevGamePointsTotal = 0;
                    prevGamesCorrectTotal = 0;
                    prevGamesWrongTotal = 0;
                }
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        */
        return retVal;
    }

    /*  This method calculates the standings for all pickem type football games for a given week */
    public static int CalculatePickemGamesStandings(int weekNo, int fsGameId) {
        
        int retVal = 0;
        /*
        List<FSFootballStandings> objPriorStandings = null;
        List<GamePicks> picks = null;
        double gamePoints = 0;
        int gamesCorrect = 0;
        int gamesWrong = 0;
        double prevGamePointsTotal = 0;
        int prevGamesCorrectTotal = 0;
        int prevGamesWrongTotal = 0;
        int fsTeamId = 0;
        int fsSeasonWeekId = 0;

        try {
            // Grab all the active leagues for this game
            List<FSLeague> fsLeagues = FSLeague.GetActiveLeaguesByGame(fsGameId);

            for (int leagueCount=0; leagueCount < fsLeagues.size(); leagueCount++) {

                // Grab all of the teams in the league
                List<FSTeam> fsTeams = FSTeam.GetTeams(fsLeagues.get(leagueCount).getFSLeagueID());

                // Grab all standing records for the week prior (Doing it this way rather than calling a query for each individual team)
                if ((weekNo - 1) > 0) {
                    objPriorStandings = FSFootballStandings.GetWeeklyStandings(fsGameId, weekNo -1);
                }

                for (int teamCount=0; teamCount < fsTeams.size(); teamCount++) {

                    FSFootballStandings objStandings = new FSFootballStandings();

                    // Grab the user's picks for the week for the game specified
                    if (fsGameId == FSGame.NFL_PICKEM) {
                        picks = GetWeeklyPickemGamePicks(fsGameId, weekNo, fsTeams.get(teamCount).getFSTeamID(), false);
                    }
                    else if(fsGameId == FSGame.NCAA_PICKEM) {
                        picks = GetWeeklyPickemGamePicks(fsGameId, weekNo, fsTeams.get(teamCount).getFSTeamID(), true);
                    }
                    else if (fsGameId == FSGame.NFL_LOVEEMLEAVEEM || fsGameId == FSGame.NCAA_LOVEEMLEAVEEM) {
                        picks = GetWeeklyLoveEmLeaveEmGamePicks(fsGameId, weekNo, fsTeams.get(teamCount).getFSTeamID());
                    }
                    else {
                        // Throw an error.  Shouldn't ever get here
                    }
                    
                    fsTeamId = fsTeams.get(teamCount).getFSTeamID();

                    for (int pickCount=0; pickCount < picks.size(); pickCount++) {

                        int teamPickedId = picks.get(pickCount).getTeamPickedID();
                        
                        // Calculate weekly points
                        if (teamPickedId > 0 && teamPickedId == picks.get(pickCount).getGame().getWinnerID()) {
                            gamePoints += 1;
                            gamesCorrect += 1;

                            // Award an additional point for NCAA Pickem when an upset occurred
                            if (fsGameId == FSGame.NCAA_PICKEM) {
                                
                                int visitorRank = picks.get(pickCount).getGame().getVisitorStandings().getOverallRanking();
                                int homeRank = picks.get(pickCount).getGame().getHomeStandings().getOverallRanking();
                                
                                if (teamPickedId == picks.get(pickCount).getGame().getVisitorID()) {
                                    if (visitorRank == 0) {
                                        gamePoints += 1;
                                    }
                                    else {
                                        if (homeRank != 0) {
                                            if (visitorRank > homeRank) {
                                                gamePoints += 1;
                                            }
                                        }
                                    }
                                }
                                
                                else if (teamPickedId == picks.get(pickCount).getGame().getHomeID()) {
                                    if (homeRank == 0) {
                                        gamePoints += 1;
                                    }
                                    else {
                                        if (visitorRank != 0) {
                                            if (homeRank > visitorRank) {
                                                gamePoints += 1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            gamesWrong +=1;
                            // If only one pick was made in NCAA L&L then there will be an additional game wrong (query will bring back 1 record even if 0 picks were made)
                            if (fsGameId == FSGame.NCAA_LOVEEMLEAVEEM && picks.size() == 1) {
                                gamesWrong +=1;
                            }
                        }

                        fsSeasonWeekId = picks.get(pickCount).getFSSeasonWeek().getFSSeasonWeekID();
                    }

                    // Calculate yearly totals
                    if (objPriorStandings != null) {
                        for (int standingsCount=0; standingsCount < objPriorStandings.size(); standingsCount++) {
                            // Make sure we have the right fsTeam
                            if (fsTeamId == objPriorStandings.get(standingsCount).getFSTeamID()) {
                                prevGamePointsTotal = objPriorStandings.get(standingsCount).getTotalGamePoints();
                                prevGamesCorrectTotal = objPriorStandings.get(standingsCount).getTotalGamesCorrect();
                                prevGamesWrongTotal = objPriorStandings.get(standingsCount).getTotalGamesWrong();
                            }
                        }
                    }

                    // Set the Standings object
                    objStandings.setFSTeamID(fsTeamId);
                    objStandings.setFSSeasonWeekID(fsSeasonWeekId);
                    objStandings.setGamePoints(gamePoints);
                    objStandings.setGamesCorrect(gamesCorrect);
                    objStandings.setGamesWrong(gamesWrong);
                    objStandings.setTotalGamePoints(prevGamePointsTotal + gamePoints);
                    objStandings.setTotalGamesCorrect(prevGamesCorrectTotal + gamesCorrect);
                    objStandings.setTotalGamesWrong(prevGamesWrongTotal + gamesWrong);

                    FSFootballStandings.SaveStandings(objStandings);

                    // Reset the variables
                    gamePoints = 0;
                    gamesCorrect = 0;
                    gamesWrong =0;
                    prevGamePointsTotal = 0;
                    prevGamesCorrectTotal = 0;
                    prevGamesWrongTotal = 0;
                }
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
        */
        return retVal;
    }
    
    /*  This method calculates the standings for the seed challenge game for a given week (round) */
    public static int CalculateSeedChallengeStandings(int weekNo, int fsGameId) {
        
        int retVal = 0;
        /*
        List<GamePicks> picks = null;
        double totalGamePoints = 0;
        int fsTeamId = 0;
        int fsSeasonWeekId = 0;
        int tournamentId = 5;

        try {
            // Grab all the active leagues for this game
            List<FSLeague> fsLeagues = FSLeague.GetActiveLeaguesByGame(fsGameId);

            for (int leagueCount=0; leagueCount < fsLeagues.size(); leagueCount++) {

                fsSeasonWeekId = FSSeasonWeek.getFSSeasonWeekID(fsLeagues.get(leagueCount).getFSLeagueID(), weekNo);
                
                // Grab all of the teams in the league
                List<FSTeam> fsTeams = FSTeam.GetTeams(fsLeagues.get(leagueCount).getFSLeagueID());

                for (int teamCount=0; teamCount < fsTeams.size(); teamCount++) {

                    FSFootballStandings objStandings = new FSFootballStandings();    
                    
                    fsTeamId = fsTeams.get(teamCount).getFSTeamID();
                    
                    // Grab the user's picks for the round
                    picks = GamePicks.GetSeedChallengePicks(tournamentId, fsTeamId);

                    for (int pickCount=0; pickCount < picks.size(); pickCount++) {
                        if (picks.get(pickCount).getFSTournamentSeed() != null && picks.get(pickCount).getFSTournamentSeed().getTourneyStatus() == 1) {
                            totalGamePoints += 1;
                        }                     
                    }     
                    
                    // Set the Standings object
                    objStandings.setFSTeamID(fsTeamId);
                    objStandings.setFSSeasonWeekID(fsSeasonWeekId);
                    objStandings.setTotalGamePoints(totalGamePoints);

                    FSFootballStandings.SaveStandings(objStandings);

                    // Reset the variables
                    totalGamePoints = 0;
                }
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
        */
        return retVal;
    }
    
    /*  This method is used to get a user's picks for the NCAA Tournament's Seed Challenge game. */
    public static List<GamePicks> GetAllSeedChallengePicks(int tournamentId) {

        List<GamePicks> picks = new ArrayList<GamePicks>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();
        
        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("GamePicks", "gp.", "")).append(",");
        sql.append(_Cols.getColumnList("FSTeam", "fst.", "FSTeam$")).append(",");
        sql.append(_Cols.getColumnList("FSTournamentSeed", "ts.", "FSTournamentSeed$")).append(",");
        sql.append(_Cols.getColumnList("Team", "tm.", "Team$"));
        sql.append("FROM GamePicks gp ");
        sql.append("JOIN FSTeam fst ON fst.FSTeamID = gp.FSTeamID ");
        sql.append("JOIN Team tm ON tm.TeamID = gp.TeamPickedID ");
        sql.append("JOIN FSTournamentSeed ts ON ts.TournamentID = gp.FSSeasonWeekID AND ts.TeamID = gp.TeamPickedID ");
        sql.append("WHERE gp.FSSeasonWeekID = ").append(tournamentId).append(" AND fst.IsActive = 1 ");
        sql.append("ORDER BY fst.FSTeamID, gp.FSTeamID, ts.SeedNumber");
         
        // Execute Query
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new GamePicks(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return picks;
   }

    /*  This method is used to get a user's picks for the NCAA Tournament's Seed Challenge game. */
    public static List<GamePicks> GetSeedChallengePicks(int tournamentId, int fsTeamId) {

        List<GamePicks> picks = new ArrayList<GamePicks>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSSeedChallengeGroup", "scg.", "FSSeedChallengeGroup$")).append(",");
        sql.append(_Cols.getColumnList("GamePicks", "gp.", "")).append(",");
        sql.append(_Cols.getColumnList("FSTournament", "t.", "FSTournament$")).append(",");
        sql.append(_Cols.getColumnList("FSTournamentSeed", "ts.", "FSTournamentSeed$")).append(",");
        sql.append(_Cols.getColumnList("FSTeam", "fst.", "FSTeam$")).append(",");
        sql.append(_Cols.getColumnList("Team", "tm.", "Team$"));
        sql.append("FROM FSSeedChallengeGroup scg ");
        sql.append("LEFT JOIN GamePicks gp ON gp.GameID = scg.SeedChallengeGroupID AND gp.FSTeamID = ").append(fsTeamId).append(" ");
        sql.append("LEFT JOIN FSTournament t ON t.TournamentID = gp.FSSeasonWeekID ");
        sql.append("LEFT JOIN FSTournamentSeed ts ON ts.TournamentID = t.TournamentID AND ts.TeamID = gp.TeamPickedID ");
        sql.append("LEFT JOIN FSTeam fst ON fst.FSTeamID = gp.FSTeamID ");
        sql.append("LEFT JOIN Team tm ON tm.TeamID = gp.TeamPickedID ");        
        sql.append("WHERE scg.TournamentID = ").append(tournamentId).append(" ");
        sql.append("ORDER BY scg.StartingSeedNumber");
         
        // Execute Query
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new GamePicks(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return picks;
   }

    /*  This method is used to get a user's picks for a specific week for the NFL and NCAA Love Em Leave Em Games. */
    public static List<GamePicks> GetOtherUserLoveEmLeaveEmPicks(int fsSeasonId, int fsTeamId, int fsSeasonWeekId) {

        List<GamePicks> picks = new ArrayList<GamePicks>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("GamePicks", "p.", "")).append(", ");
        sql.append(_Cols.getColumnList("FSTeam", "fst.", "FSTeam$")).append(", ");
        sql.append(_Cols.getColumnList("Game", "g.", "Game$")).append(", ");
        sql.append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append("FROM FSSeasonWeek fssw ");
        sql.append("LEFT JOIN GamePicks p ON p.FSSeasonWeekID = fssw.FSSeasonWeekID AND p.FSTeamID = ").append(fsTeamId).append(" ");
        sql.append("LEFT JOIN FSTeam fst ON fst.FSteamID = p.FSTeamID ");
        sql.append("LEFT JOIN Game g ON g.SeasonWeekID = fssw.SeasonWeekID AND (p.TeamPickedID = g.VisitorID OR p.TeamPickedID = g.HomeID) ");
        sql.append("LEFT JOIN Team t ON p.TeamPickedID = t.TeamID ");
        sql.append("WHERE FSSeasonID = ").append(fsSeasonId).append(" AND fssw.FSSeasonWeekID <= ").append(fsSeasonWeekId).append(" ");
        sql.append("ORDER BY fssw.FSSeasonWeekNo");
        
        // Execute Query
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new GamePicks(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return picks;
   }
    
    /*  This method is used to get a user's picks for a specific week for the NFL and NCAA Love Em Leave Em Games. */
    public static List<GamePicks> GetWeeklyLoveEmLeaveEmGamePicks(int fsGameId, int weekNo, int fsTeamId) {

        List<GamePicks> picks = new ArrayList<GamePicks>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("FSSeason", "fss.", "FSSeason$")).append(", ");
        if (weekNo > 0) {
            sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$")).append(", ");
        }
        sql.append(_Cols.getColumnList("GamePicks", "p.", "")).append(", ");
        sql.append(_Cols.getColumnList("Game", "g.", "Game$")).append(", ");
        sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append("FROM FSSeasonWeek fssw ");
        sql.append("JOIN FSSeason fss ON fss.FSSeasonID = fssw.FSSeasonID AND fss.IsActive = 1 AND FSGameID = ").append(fsGameId).append(" ");
        sql.append("JOIN SeasonWeek sw ON sw.SeasonWeekID = fssw.SeasonWeekID ");
        sql.append("LEFT JOIN GamePicks p ON fssw.FSSeasonWeekID = p.FSSeasonWeekID AND p.FSTeamID = ").append(fsTeamId).append(" ");
        sql.append("LEFT JOIN Game g ON fssw.SeasonWeekID = g.SeasonWeekID AND (g.VisitorID = p.TeamPickedID OR g.HomeID = p.TeamPickedID) ");
        sql.append("LEFT JOIN Team t ON p.TeamPickedID = t.TeamID ");
        if (weekNo > 0) {
            sql.append("WHERE sw.WeekNo = ").append(weekNo).append(" ");
        }
        sql.append("ORDER BY fssw.FSSeasonWeekNo");
        
        // Execute Query
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new GamePicks(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return picks;
   }
    
    /*  This method is used to get a user's picks for a specific week for the NFL and NCAA Pickem Games. */
    public static List<GamePicks> GetWeeklyPickemGamePicks(int fsGameId, int weekNo, int fsTeamId, boolean justTop25) {

        List<GamePicks> picks = new ArrayList<GamePicks>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("Game", "g.", "Game$")).append(", ");
        sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("FSSeason", "fss.", "FSSeason$")).append(", ");
        sql.append(_Cols.getColumnList("GamePicks", "p.", "")).append(", ");
        sql.append(_Cols.getColumnList("Team", "t.", "Team$")).append(", ");
        sql.append(_Cols.getColumnList("Standings", "vst.", "VisitorStandings$")).append(", ");
        sql.append(_Cols.getColumnList("Standings", "hst.", "HomeStandings$"));
        sql.append("FROM Game g ");
        sql.append("JOIN SeasonWeek sw ON sw.SeasonWeekID = g.SeasonWeekID ");
        sql.append("JOIN FSSeasonWeek fssw ON fssw.SeasonWeekID = g.SeasonWeekID ");
        sql.append("JOIN FSSeason fss ON fss.FSSeasonID = fssw.FSSeasonID AND fss.IsActive = 1 AND FSGameID = ").append(fsGameId).append(" ");
        sql.append("LEFT JOIN GamePicks p ON p.GameID = g.GameID AND p.FSTeamID = ").append(fsTeamId).append(" ");
        sql.append("LEFT JOIN Team t ON p.TeamPickedID = t.TeamID ");
        sql.append("LEFT JOIN Standings vst ON vst.TeamID = g.VisitorID AND vst.SeasonWeekID = g.SeasonWeekID ");
        sql.append("LEFT JOIN Standings hst ON hst.TeamID = g.HomeID AND hst.SeasonWeekID = g.SeasonWeekID ");
        sql.append("WHERE sw.WeekNo = ").append(weekNo).append(" AND g.VisitorID > 0 ");
        if (justTop25) {
            sql.append(" AND (vst.OverallRanking > 0 or hst.OverallRanking > 0) ");
        }
        sql.append("ORDER BY g.gameDate, g.gameID");

        // Execute Query
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new GamePicks(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return picks;
   }

    /*  This method is used to store the Pickem data in the DB. */
    public static int SavePick(int fsSeasonWeekId, int fsTeamId, int gameId, int teamPickedId) {

        int retVal = -1;

        try {

            // First verify that the game hasn't already started
            Game game = new Game(gameId);

            if (game.getGameHasStarted()) {
                // Don't save the pick.  Decide what TODO:
                System.out.println("Game has already started - can't save pick.");
                return retVal;
            }

            int existingID = DoesAPickAlreadyExistInDB(fsSeasonWeekId, gameId, fsTeamId);

            if (existingID > 0) {
                retVal = UpdatePick(existingID, teamPickedId);
            }
            else {
                retVal = InsertPick(fsSeasonWeekId, fsTeamId, gameId, teamPickedId);
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }

    // PRIVATE METHODS   

    /*  This method determines if the user has already made a pick for a specific matchup.
        It's main purposes is to determine if we need to update or insert the pick into the DB. */
    public static int DoesAPickAlreadyExistInDB(int fsSeasonWeekId, int gameId, int fsTeamId) {

        int retVal = -1;
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        try {
            // Create SQL statement
            sql.append("SELECT GamePicksID ");
            sql.append("FROM GamePicks ");
            sql.append("WHERE FSSeasonWeekID = ").append(fsSeasonWeekId).append(" AND FSTeamID = ").append(fsTeamId).append(" ");
            if (gameId > 0) {
                sql.append("AND GameID = ").append(gameId);
            }

            // Execute Query
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                retVal = crs.getInt("GamePicksID");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return retVal;
    }

    /*  This method finds out how many picks have been made by the user for the week. It's main purposes is for the NCAA L&L game to see if 2 teams
        have been picked to determine if we need to insert another pick into the DB. */
    public static int HowManyPicksExistInDB(int fsSeasonWeekId, int fsTeamId) {

        int retVal = -1;
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        try {
            // Create SQL statement
            sql.append("SELECT count(GamePicksID) as NumPicks ");
            sql.append("FROM GamePicks ");
            sql.append("WHERE FSSeasonWeekID = ").append(fsSeasonWeekId).append(" AND FSTeamID = ").append(fsTeamId);

            // Execute Query
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                retVal = crs.getInt("NumPicks");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return retVal;
    }

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        
        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GamePicksID")) {
                setGamePicksID(crs.getInt(prefix + "GamePicksID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) {
                setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) {
                setFSTeamID(crs.getInt(prefix + "FSTeamID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "GameID")) {
                setGameID(crs.getInt(prefix + "GameID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TeamPickedID")) {
                setTeamPickedID(crs.getInt(prefix + "TeamPickedID"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) {
                setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$"));
            }

            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) {
                setFSTeam(new FSTeam(crs, "FSTeam$"));
            }

            if (FSUtils.fieldExists(crs, "Game$", "GameID")) {
                setGame(new Game(crs, "Game$"));
            }

            if (FSUtils.fieldExists(crs, "Team$", "TeamID")) {
                setTeamPicked(new Team(crs, "Team$"));
            }

            // ADDITIONAL FIELDS
            if (FSUtils.fieldExists(crs, "Game$", "WinnerID")) {
                int winnerId = crs.getInt("Game$WinnerID");
                if (winnerId > 0) {
                    if (winnerId == _TeamPickedID) {
                        _isPickCorrect = true;
                    }
                    else {
                        _isPickCorrect = false;
                    }
                }
            }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTournament$", "TournamentID")) {
                setFSTournament(new FSTournament(crs, "FSTournament$"));
            }
            
            if (FSUtils.fieldExists(crs, "FSTournamentSeed$", "TournamentSeedID")) {
                setFSTournamentSeed(new FSTournamentSeed(crs, "FSTournamentSeed$"));
            }
            
            if (FSUtils.fieldExists(crs, "FSSeedChallengeGroup$", "SeedChallengeGroupID")) {
                setFSSeedChallengeGroup(new FSSeedChallengeGroup(crs, "FSSeedChallengeGroup$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }   

    /*  This method inserts a new record into the DB. */
    public static int InsertPick(int fsSeasonWeekId, int fsTeamId, int gameId, int teamPickedId) {

        int retVal = -1;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("INSERT INTO GamePicks ");
        sql.append("(FSSeasonWeekID, FSTeamID, GameID, TeamPickedID) ");
        sql.append("VALUES (").append(fsSeasonWeekId).append(", ").append(fsTeamId).append(", ").append(gameId).append(", ").append(teamPickedId).append(")");

        // Execute Query
        try {
            retVal = CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }    

    /*  This method updates a record in the DB. */
    public static int UpdatePick(int gamesPickedId, int teamPickedId) {

        int retVal = -1;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("UPDATE GamePicks ");
        sql.append("SET TeamPickedID = ").append(teamPickedId).append(" ");
        sql.append("WHERE GamePicksID = ").append(gamesPickedId);

        // Execute Query
        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
            retVal = gamesPickedId;
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }
}