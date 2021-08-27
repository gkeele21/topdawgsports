package tds.playoff.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;
import tds.main.bo.FSTeam;

public class PlayoffGame implements Serializable {
    
    public enum Status {UPCOMING, ONGOING, FINAL};
    public enum NextPosition {TOP, BOTTOM};

    // DB FIELDS
    private Integer _GameID;
    private Integer _RoundID;
    private Integer _GameNumber;
    private String _Status;
    private Integer _FSTeam1ID;
    private Integer _FSTeam2ID;
    private Double _Team1Pts;
    private Double _Team2Pts;
    private Integer _WinnerID;  
    private Integer _NextGameID;
    private String _NextPosition;

    // OBJECTS
    private PlayoffRound _Round;
    private FSTeam _TopTeam;
    private FSTeam _BottomTeam;    
    private FSTeam _Winner;
    

    // CONSTRUCTORS
    public PlayoffGame() {        
    }
    
    public PlayoffGame(int gameId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();            
            sql.append("SELECT").append(_Cols.getColumnList("PlayoffGame", "g.", "")).append(", ");
            sql.append(_Cols.getColumnList("PlayoffRound", "rd.", "PlayoffRound$")).append(", ");
            sql.append(_Cols.getColumnList("PlayoffTournament", "t.", "PlayoffTournament$")).append(", ");
            sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");            
            sql.append(_Cols.getColumnList("FSTeam", "tm.", "TopTeam$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "tm2.", "BottomTeam$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "w.", "Winner$"));
            sql.append("FROM PlayoffGame g ");
            sql.append("JOIN PlayoffRound rd ON rd.RoundID = g.RoundID ");
            sql.append("JOIN PlayoffTournament t ON t.TournamentID = rd.TournamentID ");
            sql.append("JOIN FSSeasonWeek fssw ON fssw.FSSeasonWeekID = rd.FSSeasonWeekID ");            
            sql.append("LEFT JOIN FSTeam tm ON tm.FSTeamID = g.FSTeam1ID ");
            sql.append("LEFT JOIN FSTeam tm2 ON tm2.FSTeamID = g.FSTeam2ID ");
            sql.append("LEFT JOIN FSTeam w ON w.FSTeamID = g.WinnerID ");
            sql.append("WHERE GameID = ").append(gameId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                InitFromCRS(crs, "");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }
  
    public PlayoffGame(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }

    // GETTERS
    public Integer getGameID() {return _GameID;}
    public Integer getRoundID() {return _RoundID;}
    public Integer getGameNumber() {return _GameNumber;}
    public String getStatus() {return _Status;}
    public Integer getFSTeam1ID() {return _FSTeam1ID;}
    public Integer getFSTeam2ID() {return _FSTeam2ID;}
    public Double getTeam1Pts() {return _Team1Pts;}
    public Double getTeam2Pts() {return _Team2Pts;}
    public Integer getWinnerID() {return _WinnerID;}
    public Integer getNextGameID() {return _NextGameID;}
    public String getNextPosition() {return _NextPosition;}
    public PlayoffRound getRound() {return _Round;}
    public FSTeam getTopTeam() {return _TopTeam;}
    public FSTeam getBottomTeam() {return _BottomTeam;}
    public FSTeam getWinner() {return _Winner;}

    // SETTERS
    public void setGameID(Integer GameID) {_GameID = GameID;}
    public void setRoundID(Integer RoundID) {_RoundID = RoundID;}
    public void setGameNumber(Integer GameNumber) {_GameNumber = GameNumber;}
    public void setStatus(String Status) {_Status = Status;}
    public void setFSTeam1ID(Integer FSTeam1ID) {_FSTeam1ID = FSTeam1ID;}
    public void setFSTeam2ID(Integer FSTeam2ID) {_FSTeam2ID = FSTeam2ID;}
    public void setTeam1Pts(Double Team1Pts) {_Team1Pts = Team1Pts;}
    public void setTeam2Pts(Double Team2Pts) {_Team2Pts = Team2Pts;}
    public void setWinnerID(Integer WinnerID) {_WinnerID = WinnerID;}
    public void setNextGameID(Integer NextGameID) {_NextGameID = NextGameID;}
    public void setNextPosition(String NextPosition) {_NextPosition = NextPosition;}
    public void setRound(PlayoffRound Round) {_Round = Round;}
    public void setTopTeam(FSTeam TopTeam) {_TopTeam = TopTeam;}
    public void setBottomTeam(FSTeam BottomTeam) {_BottomTeam = BottomTeam;}
    public void setWinner(FSTeam Winner) {_Winner = Winner;}

    // PUBLIC METHODS
    
    /* Retrieves a list of March Madness Tournament games for specific rounds */
    public static List<PlayoffGame> GetTournamentGames(int tournamentId, int begRoundNum, int endRoundNum) {
        CachedRowSet crs = null;
        List<PlayoffGame> games = new ArrayList<PlayoffGame>();
        StringBuilder sql = new StringBuilder();

        try {
            sql.append("SELECT").append(_Cols.getColumnList("PlayoffGame", "g.", "")).append(", ");
            sql.append(_Cols.getColumnList("PlayoffRound", "rd.", "PlayoffRound$")).append(", ");
            sql.append(_Cols.getColumnList("PlayoffTournament", "t.", "PlayoffTournament$")).append(", ");
            sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");            
            sql.append(_Cols.getColumnList("FSTeam", "tm.", "TopTeam$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "tm2.", "BottomTeam$")).append(", ");
            sql.append(_Cols.getColumnList("FSTeam", "gw.", "Winner$"));
            sql.append("FROM PlayoffGame g ");
            sql.append("JOIN PlayoffRound rd ON rd.RoundID = g.RoundID ");
            sql.append("JOIN PlayoffTournament t ON t.TournamentID = rd.TournamentID AND t.TournamentID = ").append(tournamentId).append(" ");
            sql.append("JOIN FSSeasonWeek fssw ON fssw.FSSeasonWeekID = rd.FSSeasonWeekID ");            
            sql.append("LEFT JOIN FSTeam tm ON tm.FSTeamID = g.FSTeam1ID ");
            sql.append("LEFT JOIN FSTeam tm2 ON tm2.FSTeamID = g.FSTeam2ID ");
            sql.append("LEFT JOIN FSTeam gw ON gw.FSTeamID = t.WinnerID ");
            sql.append("WHERE rd.RoundNumber BETWEEN ").append(begRoundNum).append(" AND ").append(endRoundNum).append(" ");
            sql.append("ORDER BY GameNumber");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                games.add(new PlayoffGame(crs, ""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return games;
   }

   /* This retrieves the current game id for a given fsTeam for a specific round.  The purpose is to have it scroll down in the bracket where the fsteam's game is. */
    public static int GetGameIDByRound(int fsTeamId, int roundNum) {
        CachedRowSet crs = null;
        int currentGame = 0;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT GameID ");
            sql.append("FROM PlayoffGame g ");
            sql.append("JOIN PlayoffRound r ON r.RoundID = g.RoundID ");
            sql.append("JOIN FSSeasonWeek fssw ON fssw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append("WHERE fssw.FSSeasonWeekNo = ").append(roundNum).append(" AND g.FSTeam1ID = ").append(fsTeamId).append(" OR g.FSTeam2ID = ").append(fsTeamId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
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
    
     /*  This method is used to store the March Madness Game data in the DB. */
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("PlayoffGame", "GameID", getGameID());
        if (doesExist) { Update(); } else { Insert(); }
    }
    
    // PRIVATE METHODS
   
    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GameID")) { setGameID(crs.getInt(prefix + "GameID")); }
            if (FSUtils.fieldExists(crs, prefix, "RoundID")) { setRoundID(crs.getInt(prefix + "RoundID")); }
            if (FSUtils.fieldExists(crs, prefix, "GameNumber")) { setGameNumber(crs.getInt(prefix + "GameNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "Status")) { setStatus(crs.getString(prefix + "Status")); }
            if (FSUtils.fieldExists(crs, prefix, "FSTeam1ID")) { setFSTeam1ID(crs.getInt(prefix + "FSTeam1ID")); }
            if (FSUtils.fieldExists(crs, prefix, "FSTeam2ID")) { setFSTeam2ID(crs.getInt(prefix + "FSTeam2ID")); }
            if (FSUtils.fieldExists(crs, prefix, "Team1Pts")) { setTeam1Pts(crs.getDouble(prefix + "Team1Pts")); }
            if (FSUtils.fieldExists(crs, prefix, "Team2Pts")) { setTeam2Pts(crs.getDouble(prefix + "Team2Pts")); }
            if (FSUtils.fieldExists(crs, prefix, "WinnerID")) { setWinnerID(crs.getInt(prefix + "WinnerID")); }            
            if (FSUtils.fieldExists(crs, prefix, "NextGameID")) { setNextGameID(crs.getInt(prefix + "NextGameID")); }
            if (FSUtils.fieldExists(crs, prefix, "NextPosition")) { setNextPosition(crs.getString(prefix + "NextPosition")); }
            
            // OBJECTS 
            if (FSUtils.fieldExists(crs, "PlayoffRound$", "RoundID")) { setRound(new PlayoffRound(crs, "PlayoffRound$")); }
            if (FSUtils.fieldExists(crs, "TopTeam$", "FSTeamID")) { setTopTeam(new FSTeam(crs, "TopTeam$")); }
            if (FSUtils.fieldExists(crs, "BottomTeam$", "FSTeamID")) { setBottomTeam(new FSTeam(crs, "BottomTeam$")); }
            if (FSUtils.fieldExists(crs, "Winner$", "WinnerID")) { setWinner(new FSTeam(crs, "Winner$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO PlayoffGame ");
        sql.append("(GameID, RoundID, GameNumber, Status, FSTeam1ID, FSTeam2ID, Team1Pts, Team2Pts, WinnerID, NextGameID, NextPosition) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGameID()));
        sql.append(FSUtils.InsertDBFieldValue(getRoundID()));
        sql.append(FSUtils.InsertDBFieldValue(getGameNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getStatus(), true));
        sql.append(FSUtils.InsertDBFieldValue(getFSTeam1ID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSTeam2ID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeam1Pts()));
        sql.append(FSUtils.InsertDBFieldValue(getTeam2Pts()));
        sql.append(FSUtils.InsertDBFieldValue(getWinnerID()));
        sql.append(FSUtils.InsertDBFieldValue(getNextGameID()));
        sql.append(FSUtils.InsertDBFieldValue(getNextPosition(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE PlayoffGame SET ");
        sql.append(FSUtils.UpdateDBFieldValue("RoundID", getRoundID()));
        sql.append(FSUtils.UpdateDBFieldValue("GameNumber", getGameNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("Status", getStatus(), true));
        sql.append(FSUtils.UpdateDBFieldValue("FSTeam1ID", getFSTeam1ID()));
        sql.append(FSUtils.UpdateDBFieldValue("FSTeam2ID", getFSTeam2ID()));
        sql.append(FSUtils.UpdateDBFieldValue("Team1Pts", getTeam1Pts()));
        sql.append(FSUtils.UpdateDBFieldValue("Team2Pts", getTeam2Pts()));
        sql.append(FSUtils.UpdateDBFieldValue("WinnerID", getWinnerID()));
        sql.append(FSUtils.UpdateDBFieldValue("NextGameID", getNextGameID()));
        sql.append(FSUtils.UpdateDBFieldValue("NextPosition", getNextPosition(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GameID = ").append(getGameID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
