package tds.proloveleave.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.*;

public class ProLoveLeave implements Serializable {

    // DB FIELDS
    private int _ProLoveLeaveID;
    private int _FSSeasonWeekID;
    private int _FSTeamID;
    private int _TeamPickedID;

    // OBJECTS
    private FSSeasonWeek _FSSeasonWeek;
    private FSTeam _FSTeam;
    private Team _TeamPicked;

    // ADDITIONAL FIELDS
    private Game _Game;
    private boolean _isPickCorrect;   

    // CONSTRUCTORS
    public ProLoveLeave() {
    }

    public ProLoveLeave(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public int getProLoveLeaveID() {return _ProLoveLeaveID;}
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public int getFSTeamID() {return _FSTeamID;}
    public int getTeamPickedID() {return _TeamPickedID;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}
    public FSTeam getFSTeam() {if (_FSTeam == null && _FSTeamID > 0) {_FSTeam = new FSTeam(_FSTeamID);}return _FSTeam;}
    public Team getTeamPicked() {if (_TeamPicked == null && _TeamPickedID > 0) {_TeamPicked = new Team(_TeamPickedID);}return _TeamPicked;}
    public Game getGame() {return _Game;}
    public boolean getIsPickCorrect() {return _isPickCorrect;}    
    
    // SETTERS
    public void setProLoveLeaveID(int ProLoveLeaveID) {_ProLoveLeaveID = ProLoveLeaveID;}
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setFSTeamID(int FSTeamID) {_FSTeamID = FSTeamID;}
    public void setTeamPickedID(int TeamPickedID) {_TeamPickedID = TeamPickedID;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setTeamPicked(Team TeamPicked) {_TeamPicked = TeamPicked;}
    public void setGame(Game Game) {_Game = Game;}
    public void setIsPickCorrect(boolean IsCorrect) {_isPickCorrect = IsCorrect;}
    
    // PUBLIC METHODS

    /*  This method calculates the standings for all leagues and teams for the given FSSeasonWeek */
    public static int CalculateStandings(FSSeasonWeek week) {
        
        int retVal = 0;
        List<FSLeague> fsLeagues = null;
        List<FSTeam> fsTeams = null;
        List<FSFootballStandings> priorStandings = null;
        List<ProLoveLeave> picks = null;
        double gamePoints = 0;
        int gamesCorrect = 0;
        int gamesWrong = 0;
        double prevGamePointsTotal = 0;
        int prevGamesCorrectTotal = 0;
        int prevGamesWrongTotal = 0;
        int fsTeamId = 0;
        int priorFSSeasonWeekId = 0;
        int teamPickedId = 0;

        try {
            
            // Grab all the active leagues for the pro pickem season
            fsLeagues = FSLeague.GetLeagues(week.getFSSeasonID());

            for (int i=0; i < fsLeagues.size(); i++) {

                // Grab all of the teams in the league
                fsTeams = FSTeam.GetLeagueTeams(fsLeagues.get(i).getFSLeagueID());

                // Retrieve the prior standings
                if (week.getFSSeasonWeekNo() == 1) {
                    priorStandings = new ArrayList<FSFootballStandings>();
                }
                else {
                    priorFSSeasonWeekId = new FSSeasonWeek(week.getFSSeasonID(),week.getFSSeasonWeekNo() - 1).getFSSeasonWeekID();
                    priorStandings = FSFootballStandings.GetWeeklyStandings(priorFSSeasonWeekId);
                }                

                for (int teamCount=0; teamCount < fsTeams.size(); teamCount++) {
                    
                    FSFootballStandings standings = new FSFootballStandings();                    
                    gamePoints = 0;
                    gamesCorrect = 0;
                    gamesWrong = 0;
                    prevGamePointsTotal = 0;
                    prevGamesCorrectTotal = 0;
                    prevGamesWrongTotal = 0;
                    
                    fsTeamId = fsTeams.get(teamCount).getFSTeamID();

                    // Grab the user's picks for the week
                    picks = ProLoveLeave.GetPicks(week.getFSSeasonID(), fsTeamId, week.getFSSeasonWeekID());

                    for (int j=0; j < picks.size(); j++) {

                        teamPickedId = picks.get(j).getTeamPickedID();
                        
                        // Calculate weekly points
                        if (teamPickedId > 0 && picks.get(j).getGame().getWinnerID().equals(teamPickedId)) {
                            gamePoints += 1;
                            gamesCorrect += 1;                                                                
                        }
                        else {
                            gamesWrong += 1;  
                        }
                    }

                    // Calculate yearly totals
                        for (int k=0; k < priorStandings.size(); k++) {
                            // Make sure we have the right fsTeam
                            if (fsTeamId == priorStandings.get(k).getFSTeamID()) {
                                prevGamePointsTotal = priorStandings.get(k).getTotalGamePoints();
                                prevGamesCorrectTotal = priorStandings.get(k).getTotalGamesCorrect();
                                prevGamesWrongTotal = priorStandings.get(k).getTotalGamesWrong();
                            }
                        }

                    // Set the Standings object
                    standings.setFSTeamID(fsTeamId);
                    standings.setFSSeasonWeekID(week.getFSSeasonWeekID());
                    standings.setGamePoints(gamePoints);
                    standings.setGamesCorrect(gamesCorrect);
                    standings.setGamesWrong(gamesWrong);
                    standings.setTotalGamePoints(prevGamePointsTotal + gamePoints);
                    standings.setTotalGamesCorrect(prevGamesCorrectTotal + gamesCorrect);
                    standings.setTotalGamesWrong(prevGamesWrongTotal + gamesWrong);
                    standings.Save();                 
                }
                
                FSFootballStandings.CalculateRank(fsLeagues.get(i).getFSLeagueID(), week.getFSSeasonWeekID(), "TotalGamePoints desc");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
 
        return retVal;
    }

    /*  This method is used to get a user's picks for a specific week. */
    public static List<ProLoveLeave> GetPicks(int fsSeasonId, int fsTeamId, int fsSeasonWeekId) {

        List<ProLoveLeave> picks = new ArrayList<ProLoveLeave>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("FSSeason", "fss.", "FSSeason$")).append(", ");
        sql.append(_Cols.getColumnList("ProLoveLeave", "p.", "")).append(", ");
        sql.append(_Cols.getColumnList("Game", "g.", "Game$")).append(", ");
        sql.append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append("FROM FSSeasonWeek fssw ");
        sql.append("JOIN FSSeason fss ON fss.FSSeasonID = fssw.FSSeasonID AND fss.FSSeasonID = ").append(fsSeasonId).append(" ");
        sql.append("LEFT JOIN ProLoveLeave p ON fssw.FSSeasonWeekID = p.FSSeasonWeekID AND p.FSTeamID = ").append(fsTeamId).append(" ");
        sql.append("LEFT JOIN Game g ON fssw.SeasonWeekID = g.SeasonWeekID AND (g.VisitorID = p.TeamPickedID OR g.HomeID = p.TeamPickedID) ");
        sql.append("LEFT JOIN Team t ON p.TeamPickedID = t.TeamID ");
        if (fsSeasonWeekId > 0) { sql.append("WHERE fssw.FSSeasonWeekID = ").append(fsSeasonWeekId).append(" "); }
        sql.append("ORDER BY fssw.FSSeasonWeekNo");

        // Execute Query
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new ProLoveLeave(crs,""));
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
        int id = 0;

        try {

            // First verify that the game hasn't already started
            Game game = new Game(gameId);

            if (game.getGameHasStarted()) {
                // Don't save the pick.  Decide what TODO:
                System.out.println("Game has already started - can't save pick.");
                return retVal;
            }
            
            /* See if the team has been picked prior and needs to be removed. This should only affect future 
            picks because you shouldn't be able to select a team that you've picked in the past. */
            id = HasTeamBeenPicked(fsTeamId, teamPickedId);
            
            int existingID = DoesAPickAlreadyExistInDB(fsSeasonWeekId, fsTeamId);

            if (existingID > 0) {
                retVal = UpdatePick(existingID, teamPickedId);
            }
            else {
                retVal = InsertPick(fsSeasonWeekId, fsTeamId, teamPickedId);
            }
            
            // Now do the deletion that was flagged before now that the new pick has been inserted/updated.
            if (id > 0) { DeletePick(id); }
     
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }

    // PRIVATE METHODS
    
    /* This method removes a pick out of the DB. */
     private static int DeletePick(int ProLoveLeaveId) {

        int retVal = -1;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("DELETE FROM ProLoveLeave ");
	sql.append("WHERE ProLoveLeaveID = ").append(ProLoveLeaveId);

        try {
            retVal = CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }

    /*  This method determines if the user has already made a pick for a specific matchup.
        It's main purposes is to determine if we need to update or insert the pick into the DB. */
    private static int DoesAPickAlreadyExistInDB(int fsSeasonWeekId, int fsTeamId) {

        int id = 0;
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        try {
            // Create SQL statement
            sql.append("SELECT ProLoveLeaveID ");
            sql.append("FROM ProLoveLeave ");
            sql.append("WHERE FSSeasonWeekID = ").append(fsSeasonWeekId).append(" AND FSTeamID = ").append(fsTeamId).append(" ");

            // Execute Query
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                id = crs.getInt("ProLoveLeaveID");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return id;
    }
    
    private static int HasTeamBeenPicked(int fsTeamId, int teamPickedId) {

        int id = 0;
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        try {
            // Create SQL statement
            sql.append("SELECT ProLoveLeaveID ");
            sql.append("FROM ProLoveLeave ");
            sql.append("WHERE FSTeamID = ").append(fsTeamId).append(" AND TeamPickedID = ").append(teamPickedId).append(" ");

            // Execute Query
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                id = crs.getInt("ProLoveLeaveID");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return id;
    }

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        
        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "ProLoveLeaveID")) {
                setProLoveLeaveID(crs.getInt(prefix + "ProLoveLeaveID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) {
                setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) {
                setFSTeamID(crs.getInt(prefix + "FSTeamID"));
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

            if (FSUtils.fieldExists(crs, "Team$", "TeamID")) {
                setTeamPicked(new Team(crs, "Team$"));
            }

            // ADDITIONAL FIELDS
            if (FSUtils.fieldExists(crs, "Game$", "GameID")) {
                setGame(new Game(crs, "Game$"));
            }
            
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

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }   

    /*  This method inserts a new record into the DB. */
    public static int InsertPick(int fsSeasonWeekId, int fsTeamId, int teamPickedId) {

        int retVal = -1;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("INSERT INTO ProLoveLeave ");
        sql.append("(FSSeasonWeekID, FSTeamID, TeamPickedID) ");
        sql.append("VALUES (").append(fsSeasonWeekId).append(", ").append(fsTeamId).append(", ").append(teamPickedId).append(")");

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
        sql.append("UPDATE ProLoveLeave ");
        sql.append("SET TeamPickedID = ").append(teamPickedId).append(" ");        
        sql.append("WHERE ProLoveLeaveID = ").append(gamesPickedId);

        // Execute Query
        try {
            retVal = CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }
}
