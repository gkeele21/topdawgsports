package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSTournamentGame implements Serializable {

    // DB FIELDS
    private int _GameID;
    private int _RoundID;
    private int _RegionID;
    private int _GameNumber;
    private int _GameStatus;
    private int _FSTeam1ID;
    private int _FSTeam2ID;
    private double _Team1Pts;
    private double _Team2Pts;
    private int _WinnerID;  
    private int _NextGameID;
    private int _NextPosition;
    private String _Location;

    // OBJECTS
    private FSTournamentRound _FSTournamentRound;
    private FSTournamentRegion _FSTournamentRegion;
    private FSTeam _TopFSTeam;
    private FSTeam _BottomFSTeam;
    private Team _TopTeam;
    private Team _BottomTeam;
    private FSTournamentSeed _TopTeamSeed;
    private FSTournamentSeed _BottomTeamSeed;
    private Team _Winner;

    // ADDITIONAL FIELDS
    private String _GameStatusValue;

    // CONSTRUCTORS
    public FSTournamentGame() {        
    }
    
    public FSTournamentGame(int gameId) {
        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSTournamentGame", "g.", ""));
            sql.append("FROM FSTournamentGame g ");
            sql.append("WHERE g.GameID = ").append(gameId);

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                initFromCRS(crs, "");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }
        
    public FSTournamentGame(int gameId, boolean isNCAATourney) {
        CachedRowSet crs = null;
        String table = "";
        try {
            StringBuilder sql = new StringBuilder();
            // If it's the ncaaTourney game then we need to grab the team from the Team table.  If it's the playoff game then it comes from the FSTeam table
            if (isNCAATourney) {
                table = "Team";
            }
            else {
                table = "FSTeam";
            }

            sql.append("SELECT").append(_Cols.getColumnList("FSTournamentGame", "g.", "")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRegion", "s.", "FSTournamentRegion$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRound", "rd.", "FSTournamentRound$")).append(", ");
            sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournament", "t.", "FSTournament$")).append(", ");
            sql.append(_Cols.getColumnList(table, "tm.", "Top"+table+"$")).append(", ");
            sql.append(_Cols.getColumnList(table, "tm2.", "Bottom"+table+"$")).append(", ");
            if (isNCAATourney) {
                sql.append(_Cols.getColumnList("FSTournamentSeed", "ts.", "TopTeamSeed$")).append(", ");
                sql.append(_Cols.getColumnList("FSTournamentSeed", "ts2.", "BottomTeamSeed$")).append(", ");
            }
            sql.append(_Cols.getColumnList(table, "w.", "Winner$"));
            sql.append("FROM FSTournamentGame g ");
            sql.append("INNER JOIN FSTournamentRegion s ON s.RegionID = g.RegionID ");
            sql.append("INNER JOIN FSTournamentRound rd ON rd.RoundID = g.RoundID ");
            sql.append("INNER JOIN FSSeasonWeek fssw ON fssw.FSSeasonWeekID = rd.FSSeasonWeekID ");
            sql.append("INNER JOIN FSTournament t ON t.TournamentID = s.TournamentID ");
            sql.append("LEFT JOIN ").append(table).append(" tm ON tm.").append(table).append("ID = g.FSTeam1ID ");
            sql.append("LEFT JOIN ").append(table).append(" tm2 ON tm2.").append(table).append("ID = g.FSTeam2ID ");
            sql.append("LEFT JOIN ").append(table).append(" w ON w.").append(table).append("ID = t.WinnerID ");
            if (isNCAATourney) {
                sql.append("LEFT JOIN FSTournamentSeed ts ON ts.TeamID = tm.TeamID AND ts.TournamentID = t.TournamentID ");
                sql.append("LEFT JOIN FSTournamentSeed ts2 ON ts2.TeamID = tm2.TeamID AND ts2.TournamentID = t.TournamentID ");
            }
            sql.append("WHERE g.GameID = ").append(gameId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                initFromCRS(crs, "");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public FSTournamentGame(CachedRowSet crs, String prefix) {
        initFromCRS(crs, prefix);
    }

    // GETTERS
    public int getGameID() {return _GameID;}
    public int getRoundID() {return _RoundID;}
    public int getRegionID() {return _RegionID;}
    public int getGameNumber() {return _GameNumber;}
    public int getGameStatus() {return _GameStatus;}
    public int getFSTeam1ID() {return _FSTeam1ID;}
    public int getFSTeam2ID() {return _FSTeam2ID;}
    public double getTeam1Pts() {return _Team1Pts;}
    public double getTeam2Pts() {return _Team2Pts;}
    public int getWinnerID() {return _WinnerID;}
    public int getNextGameID() {return _NextGameID;}
    public int getNextPosition() {return _NextPosition;}
    public String getLocation() {return _Location;}
    public String getGameStatusValue() {return _GameStatusValue;}
    public FSTournamentRound getFSTournamentRound() {return _FSTournamentRound;}
    public FSTournamentRegion getFSTournamentRegion() {return _FSTournamentRegion;}
    public FSTeam getTopFSTeam() {return _TopFSTeam;}
    public FSTeam getBottomFSTeam() {return _BottomFSTeam;}
    public Team getTopTeam() {return _TopTeam;}
    public Team getBottomTeam() {return _BottomTeam;}
    public FSTournamentSeed getTopTeamSeed() {return _TopTeamSeed;}
    public FSTournamentSeed getBottomTeamSeed() {return _BottomTeamSeed;}
    public Team getWinner() {return _Winner;}

    // SETTERS
    public void setGameID(int GameID) {_GameID = GameID;}
    public void setRoundID(int RoundID) {_RoundID = RoundID;}
    public void setRegionID(int RegionID) {_RegionID = RegionID;}
    public void setGameNumber(int GameNumber) {_GameNumber = GameNumber;}
    public void setGameStatus(int GameStatus) {_GameStatus = GameStatus;}
    public void setFSTeam1ID(int FSTeam1ID) {_FSTeam1ID = FSTeam1ID;}
    public void setFSTeam2ID(int FSTeam2ID) {_FSTeam2ID = FSTeam2ID;}
    public void setTeam1Pts(double Team1Pts) {_Team1Pts = Team1Pts;}
    public void setTeam2Pts(double Team2Pts) {_Team2Pts = Team2Pts;}
    public void setWinnerID(int WinnerID) {_WinnerID = WinnerID;}
    public void setNextGameID(int NextGameID) {_NextGameID = NextGameID;}
    public void setNextPosition(int NextPosition) {_NextPosition = NextPosition;}
    public void setLocation(String Location) {_Location = Location;}
    public void setFSTournamentRound(FSTournamentRound FSTournamentRound) {_FSTournamentRound = FSTournamentRound;}
    public void setFSTournamentRegion(FSTournamentRegion FSTournamentRegion) {_FSTournamentRegion = FSTournamentRegion;}
    public void setTopFSTeam(FSTeam TopFSTeam) {_TopFSTeam = TopFSTeam;}
    public void setBottomFSTeam(FSTeam BottomFSTeam) {_BottomFSTeam = BottomFSTeam;}
    public void setTopTeam(Team TopTeam) {_TopTeam = TopTeam;}
    public void setBottomTeam(Team BottomTeam) {_BottomTeam = BottomTeam;}
    public void setTopTeamSeed(FSTournamentSeed TopTeamSeed) {_TopTeamSeed = TopTeamSeed;}
    public void setBottomTeamSeed(FSTournamentSeed BottomTeamSeed) {_BottomTeamSeed = BottomTeamSeed;}
    public void setWinner(Team Winner) {_Winner = Winner;}
    public void setGameStatusValue(String GameStatusValue) {_GameStatusValue = GameStatusValue;}

    // PUBLIC METHODS
    
    /* Retrieves a list of Playoff games or College Tournament games for specific rounds */
    public static List<FSTournamentGame> GetTournamentGames(int tournamentId, int begRoundNum, int endRoundNum) {

        CachedRowSet crs = null;
        List<FSTournamentGame> games = new ArrayList<FSTournamentGame>();
        StringBuilder sql = new StringBuilder();

        Connection con = null;
        try {
            sql.append("SELECT").append(_Cols.getColumnList("FSTournamentGame", "g.", "")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRegion", "r.", "FSTournamentRegion$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRound", "rd.", "FSTournamentRound$")).append(", ");
            sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournament", "t.", "FSTournament$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRound", "sr.", "StartingRound$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "tm.", "TopFSTeam$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "tm2.", "BottomFSTeam$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "w.", "Winner$"));
            sql.append("FROM FSTournamentGame g ");
            sql.append("JOIN FSTournamentRegion r ON r.RegionID = g.RegionID ");
            sql.append("JOIN FSTournamentRound rd ON rd.RoundID = g.RoundID ");
            sql.append("JOIN FSSeasonWeek fssw ON fssw.FSSeasonWeekID = rd.FSSeasonWeekID ");
            sql.append("JOIN FSTournament t ON t.TournamentID = r.TournamentID AND t.TournamentID = ").append(tournamentId).append(" ");
            sql.append("JOIN FSTournamentRound sr ON sr.RoundID = r.startingRoundID ");
            sql.append("LEFT JOIN FSTeam tm ON tm.FSTeamID = g.FSTeam1ID ");
            sql.append("LEFT JOIN FSTeam tm2 ON tm2.FSTeamID = g.FSTeam2ID ");
            sql.append("LEFT JOIN FSTeam w ON w.FSTeamID = t.WinnerID ");
            sql.append("WHERE rd.RoundNumber BETWEEN ").append(begRoundNum).append(" AND ").append(endRoundNum).append(" ");
            sql.append("ORDER BY GameID");

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                games.add(new FSTournamentGame(crs, ""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return games;
   }

    /* Retrieves a list of Playoff games or College Tournament games for specific rounds */
    public static List<FSTournamentGame> GetTournamentGames(int tournamentId, int begRoundNum, int endRoundNum, boolean isNCAATourney) {

        CachedRowSet crs = null;
        Connection con = null;
        List<FSTournamentGame> games = new ArrayList<FSTournamentGame>();
        StringBuilder sql = new StringBuilder();
        String table = "";
        // If it's the ncaaTourney game then we need to grab the team from the Team table.  If it's the playoff game then it comes from the FSTeam table
        if (isNCAATourney) {
            table = "Team";
        }
        else {
            table = "FSTeam";
        }

        try {
            sql.append("SELECT").append(_Cols.getColumnList("FSTournamentGame", "g.", "")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRegion", "r.", "FSTournamentRegion$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRound", "rd.", "FSTournamentRound$")).append(", ");
            sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournament", "t.", "FSTournament$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRound", "sr.", "StartingRound$")).append(", ");
            sql.append(_Cols.getColumnList(table, "tm.", "Top"+table+"$")).append(", ");
            sql.append(_Cols.getColumnList(table, "tm2.", "Bottom"+table+"$")).append(", ");
            if (isNCAATourney) {
                sql.append(_Cols.getColumnList("FSTournamentSeed", "ts.", "TopTeamSeed$")).append(", ");
                sql.append(_Cols.getColumnList("FSTournamentSeed", "ts2.", "BottomTeamSeed$")).append(", ");
            }
            sql.append(_Cols.getColumnList(table, "w.", "Winner$"));
            sql.append("FROM FSTournamentGame g ");
            sql.append("JOIN FSTournamentRegion r ON r.RegionID = g.RegionID ");
            sql.append("JOIN FSTournamentRound rd ON rd.RoundID = g.RoundID ");
            sql.append("JOIN FSSeasonWeek fssw ON fssw.FSSeasonWeekID = rd.FSSeasonWeekID ");
            sql.append("JOIN FSTournament t ON t.TournamentID = r.TournamentID AND t.TournamentID = ").append(tournamentId).append(" ");
            sql.append("JOIN FSTournamentRound sr ON sr.RoundID = r.startingRoundID ");
            sql.append("LEFT JOIN ").append(table).append(" tm ON tm.").append(table).append("ID = g.FSTeam1ID ");
            sql.append("LEFT JOIN ").append(table).append(" tm2 ON tm2.").append(table).append("ID = g.FSTeam2ID ");
            sql.append("LEFT JOIN ").append(table).append(" w ON w.").append(table).append("ID = t.WinnerID ");
            if (isNCAATourney) {
                sql.append("LEFT JOIN FSTournamentSeed ts ON ts.TeamID = tm.TeamID AND ts.TournamentID = t.TournamentID ");
                sql.append("LEFT JOIN FSTournamentSeed ts2 ON ts2.TeamID = tm2.TeamID AND ts2.TournamentID = t.TournamentID ");
            }
            sql.append("WHERE rd.RoundNumber BETWEEN ").append(begRoundNum).append(" AND ").append(endRoundNum).append(" ");
            sql.append("ORDER BY GameID");

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                FSTournamentGame gameObj = new FSTournamentGame(crs, "");
                games.add(gameObj);
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return games;
   }

   /* This retrieves the current game id for a given fsTeam for a specific round.  The purpose is to have it scroll down in the bracket where the fsteam's game is. */
    public static int getGameIDByRound(int fsTeamId, int roundNum) {

        CachedRowSet crs = null;
        int currentGame = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT GameID ");
            sql.append("FROM FSTournamentGame g ");
            sql.append("INNER JOIN FSTournamentRound r ON r.RoundID = g.RoundID ");
            sql.append("INNER JOIN FSSeasonWeek fssw ON fssw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append("WHERE fssw.FSSeasonWeekNo = ").append(roundNum).append(" AND g.FSTeam1ID = ").append(fsTeamId).append(" OR g.FSTeam2ID = ").append(fsTeamId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                currentGame = crs.getInt("GameID");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return currentGame;
   }
    
     /*  This method is used to store NFLPickem data in the DB. */
    public static int SaveGame(FSTournamentGame objGame) {

        int retVal = 0;

        boolean doesExist = DoesAPickAlreadyExistInDB(objGame.getGameID());

        if (doesExist) {
            retVal = UpdateGame(objGame);
        }
        else {
            retVal = InsertGame(objGame);
        }

        return retVal;
    }

    // PRIVATE METHODS

    /*  This method determines if a record already exists in the DB. */
    private static boolean DoesAPickAlreadyExistInDB(int gameId) {

        boolean doesExist = false;
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        try {
            // Create SQL statement
            sql.append("SELECT 1 ");
            sql.append("FROM FSTournamentGame ");
            sql.append("WHERE GameID = ").append(gameId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                doesExist = true;
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return doesExist;
    }
    
    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GameID")) {
                setGameID(crs.getInt(prefix + "GameID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RoundID")) {
                setRoundID(crs.getInt(prefix + "RoundID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RegionID")) {
                setRegionID(crs.getInt(prefix + "RegionID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "GameNumber")) {
                setGameNumber(crs.getInt(prefix + "GameNumber"));
            }

            if (FSUtils.fieldExists(crs, prefix, "GameStatus")) {
                setGameStatus(crs.getInt(prefix + "GameStatus"));
                switch (_GameStatus) {
                    case 1:
                        setGameStatusValue("Not Started");
                        break;
                    case 2:
                        setGameStatusValue("In Progress");
                        break;
                    case 3:
                        setGameStatusValue("Final");
                        break;
                }
            }

            if (FSUtils.fieldExists(crs, prefix, "FSTeam1ID")) {
                setFSTeam1ID(crs.getInt(prefix + "FSTeam1ID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FSTeam2ID")) {
                setFSTeam2ID(crs.getInt(prefix + "FSTeam2ID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Team1Pts")) {
                setTeam1Pts(crs.getDouble(prefix + "Team1Pts"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Team2Pts")) {
                setTeam2Pts(crs.getDouble(prefix + "Team2Pts"));
            }

            if (FSUtils.fieldExists(crs, prefix, "WinnerID")) {
                setWinnerID(crs.getInt(prefix + "WinnerID"));
            }            

            if (FSUtils.fieldExists(crs, prefix, "NextGameID")) {
                setNextGameID(crs.getInt(prefix + "NextGameID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "NextPosition")) {
                setNextPosition(crs.getInt(prefix + "NextPosition"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Location")) {
                setLocation(crs.getString(prefix + "Location"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTournamentRound$", "RoundID")) {
                setFSTournamentRound(new FSTournamentRound(crs, "FSTournamentRound$"));
            }

            if (FSUtils.fieldExists(crs, "FSTournamentRegion$", "RegionID")) {
                setFSTournamentRegion(new FSTournamentRegion(crs, "FSTournamentRegion$"));
            }

            if (FSUtils.fieldExists(crs, "TopFSTeam$", "FSTeamID")) {
                setTopFSTeam(new FSTeam(crs, "TopFSTeam$"));
            }

            if (FSUtils.fieldExists(crs, "BottomFSTeam$", "FSTeamID")) {
                setBottomFSTeam(new FSTeam(crs, "BottomFSTeam$"));
            }

            if (FSUtils.fieldExists(crs, "TopTeam$", "TeamID")) {
                setTopTeam(new Team(crs, "TopTeam$"));
            }

            if (FSUtils.fieldExists(crs, "BottomTeam$", "TeamID")) {
                setBottomTeam(new Team(crs, "BottomTeam$"));
            }

            if (FSUtils.fieldExists(crs, "TopTeamSeed$", "TournamentSeedID")) {
                setTopTeamSeed(new FSTournamentSeed(crs, "TopTeamSeed$"));
            }

            if (FSUtils.fieldExists(crs, "BottomTeamSeed$", "TournamentSeedID")) {
                setBottomTeamSeed(new FSTournamentSeed(crs, "BottomTeamSeed$"));
            }
            
            if (FSUtils.fieldExists(crs, "Winner$", "TeamID")) {
                setWinner(new Team(crs, "Winner$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
     /*  This method inserts a new record into the DB. */
    private static int InsertGame(FSTournamentGame objGame) {

        int retVal = 0;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("INSERT INTO FSTournamentGame ");
        sql.append("(RoundID, RegionID, GameNumber, GameStatus, FSTeam1ID, FSTeam2ID, Team1Pts, Team2Pts, WinnerID, NextGameID, NextPosition, Location) ");
        sql.append("VALUES (");
        sql.append(objGame.getRoundID()).append(", ");
        sql.append(objGame.getRegionID()).append(", ");
        sql.append(objGame.getGameNumber()).append(", ");
        sql.append(objGame.getGameStatus()).append(", ");
        sql.append(objGame.getFSTeam1ID()).append(", ");
        sql.append(objGame.getFSTeam2ID()).append(", ");
        sql.append(objGame.getTeam1Pts()).append(", ");
        sql.append(objGame.getTeam2Pts()).append(", ");
        sql.append(objGame.getWinnerID()).append(", ");
        sql.append(objGame.getNextGameID()).append(", ");
        sql.append(objGame.getNextPosition()).append(", '");
        sql.append(objGame.getLocation()).append("'");
        sql.append(")");

        // Call QueryCreator
        try {
            retVal = CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }    

    /*  This method updates a record in the DB. */
    private static int UpdateGame(FSTournamentGame objGame) {

        int retVal = 0;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("UPDATE FSTournamentGame SET ");
        sql.append("RoundID = ").append(objGame.getRoundID()).append(", ");
        sql.append("RegionID = ").append(objGame.getRegionID()).append(", ");
        sql.append("GameNumber = ").append(objGame.getGameNumber()).append(", ");
        sql.append("GameStatus = ").append(objGame.getGameStatus()).append(", ");
        if (objGame.getFSTeam1ID() > 0) {
            sql.append("FSTeam1ID = ").append(objGame.getFSTeam1ID()).append(", ");
        }
        if (objGame.getFSTeam2ID() > 0) {
            sql.append("FSTeam2ID = ").append(objGame.getFSTeam2ID()).append(", ");
        }
        sql.append("Team1Pts = ").append(objGame.getTeam1Pts()).append(", ");
        sql.append("Team2Pts = ").append(objGame.getTeam2Pts()).append(", ");
        sql.append("WinnerID = ").append(objGame.getWinnerID()).append(", ");
        sql.append("NextGameID = ").append(objGame.getNextGameID()).append(", ");
        sql.append("NextPosition = ").append(objGame.getNextPosition()).append(", ");
        sql.append("Location = '").append(objGame.getLocation()).append("' ");
        sql.append("WHERE GameID = ").append(objGame.getGameID());

        // Call QueryCreator
        try {
            retVal = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }
}

  /* Retrieves a list of Playoff games for a given a Region id   -   NOTE : eventually we might want to look at retrieving all games and do some caching of the data */
    /*
    public static List<FSTournamentGame> GetPlayoffGames(int sectionId) {

        CachedRowSet crs = null;
        List<FSTournamentGame> games = new ArrayList<FSTournamentGame>();
        StringBuilder sql = new StringBuilder();

        try {            
            sql.append("SELECT").append(_Cols.getColumnList("FSTournamentGame", "g.", "")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRegion", "s.", "FSTournamentRegion$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRound", "rd.", "FSTournamentRound$")).append(", ");
            sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournament", "p.", "FSTournament$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRound", "sr.", "StartingRound$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "tm.", "TopTeam$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "tm2.", "BottomTeam$"));
            sql.append("FROM FSTournamentGame g ");
            sql.append("INNER JOIN FSTournamentRegion s ON g.RegionID = s.RegionID AND s.RegionID = ").append(sectionId).append(" ");
            sql.append("INNER JOIN FSTournamentRound rd ON rd.RoundID = g.RoundID ");
            sql.append("INNER JOIN FSSeasonWeek fssw ON rd.FSSeasonWeekID = fssw.FSSeasonWeekID ");
            sql.append("INNER JOIN FSTournament p ON s.PlayoffID = p.PlayoffID ");
            sql.append("INNER JOIN FSTournamentRound sr ON s.startingRoundID = sr.RoundID ");
            sql.append("LEFT JOIN FSTeam tm ON g.FSTeam1ID = tm.FSTeamID ");
            sql.append("LEFT JOIN FSTeam tm2 ON g.FSTeam2ID = tm2.FSTeamID ");
            sql.append("ORDER BY GameID");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                FSTournamentGame gameObj = new FSTournamentGame(crs, "");
                games.add(gameObj);
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return games;
   }
*/

    /* Retrieves a list of Playoff games for a specific fsTeam.  This is the round summary's game results portion. */
    /*
    public static List<FSTournamentGame> getGameResults(int fsTeamId) {

        CachedRowSet crs = null;
        List<FSTournamentGame> games = new ArrayList<FSTournamentGame>();

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSTournamentGame", "g.", "")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRound", "rd.", "FSTournamentRound$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournamentRegion", "s.", "FSTournamentRegion$")).append(", ");
            sql.append(_Cols.getColumnList("FSTournament", "p.", "FSTournament$")).append(", ");
            sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "tm.", "TopTeam$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "tm2.", "BottomTeam$"));
            sql.append("FROM FSTournamentGame g ");
            sql.append("INNER JOIN FSTournamentRound rd ON g.RoundID = rd.RoundID ");
            sql.append("INNER JOIN FSTournamentRegion s ON g.RegionID = s.RegionID ");
            sql.append("INNER JOIN FSTournament p ON s.PlayoffID = p.PlayoffID ");
            sql.append("INNER JOIN FSSeasonWeek fssw ON rd.FSSeasonWeekID = fssw.FSSeasonWeekID ");
            sql.append("LEFT JOIN FSTeam tm ON g.FSTeam1ID = tm.FSTeamID ");
            sql.append("LEFT JOIN FSTeam tm2 ON g.FSTeam2ID = tm2.FSTeamID ");
            sql.append("WHERE g.FSTeam1ID = ").append(fsTeamId).append(" OR g.FSTeam2ID = ").append(fsTeamId).append(" ");
            sql.append("ORDER BY GameID");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                FSTournamentGame gameObj = new FSTournamentGame(crs, "");
                games.add(gameObj);
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return games;

   }
*/
