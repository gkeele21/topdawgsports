package tds.playoff.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;
import tds.main.bo.FSTeam;

public class PlayoffTournament implements Serializable {

    public enum Status {UPCOMING, ONGOING, FINAL};
    public static final int AverageJoe = 520;
    
    // DB FIELDS
    private Integer _TournamentID;    
    private String _TournamentName;
    private String _Status;
    private Integer _NumTeams;
    private Integer _NumRounds;
    private Integer _WinnerID;

    // OBJECTS
    private FSTeam _Winner;

    // CONSTRUCTORS
    public PlayoffTournament() {
    }

    public PlayoffTournament(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }
    
    public PlayoffTournament(int tournamentId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("PlayoffTournament", "", ""));
            sql.append("FROM PlayoffTournament ");
            sql.append("WHERE TournamentID = ").append(tournamentId);

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

    // GETTERS
    public Integer getTournamentID() {return _TournamentID;}
    public String getTournamentName() {return _TournamentName;}
    public String getStatus() {return _Status;}
    public Integer getNumTeams() {return _NumTeams;}
    public Integer getNumRounds() {return _NumRounds;}
    public Integer getWinnerID() {return _WinnerID;}
    public FSTeam getWinner() {return _Winner;}
    
    // SETTERS
    public void setTournamentID(Integer TournamentID) {_TournamentID = TournamentID;}
    public void setTournamentName(String TournamentName) {_TournamentName = TournamentName;}
    public void setStatus(String Status) {_Status = Status;}
    public void setNumTeams(Integer NumTeams) {_NumTeams = NumTeams;}
    public void setNumRounds(Integer NumRounds) {_NumRounds = NumRounds;}
    public void setWinnerID(Integer WinnerID) {_WinnerID = WinnerID;}
    public void setWinner(FSTeam Winner) {_Winner = Winner;}

    // PUBLIC METHODS
/*
    public static PlayoffTournament getPlayoffByFSLeague(int fsLeagueId) {

        PlayoffTournament playoffObj = null;
        CachedRowSet crs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FFPlayoff", "", ""));
            sql.append("FROM FFPlayoff ");
            sql.append("WHERE FSLeagueID = ").append(fsLeagueId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                playoffObj = new PlayoffTournament(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return playoffObj;
    }
*/
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("PlayoffTournament", "TournamentID", getTournamentID());
        if (doesExist) { Update(); } else { Insert(); }
    }
    
    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) { setTournamentID(crs.getInt(prefix + "TournamentID")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentName")) { setTournamentName(crs.getString(prefix + "TournamentName")); }
            if (FSUtils.fieldExists(crs, prefix, "Status")) { setStatus(crs.getString(prefix + "Status")); }
            if (FSUtils.fieldExists(crs, prefix, "NumTeams")) { setNumTeams(crs.getInt(prefix + "NumTeams")); }
            if (FSUtils.fieldExists(crs, prefix, "NumRounds")) { setNumRounds(crs.getInt(prefix + "NumRounds")); }
            if (FSUtils.fieldExists(crs, prefix, "WinnerID")) { setWinnerID(crs.getInt(prefix + "WinnerID")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "Winner$", "FSTeamID")) { setWinner(new FSTeam(crs, "Winner$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO PlayoffTournament ");
        sql.append("(TournamentID, TournamentName, Status, NumTeams, NumRounds, WinnerID) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getTournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getStatus(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNumTeams()));
        sql.append(FSUtils.InsertDBFieldValue(getNumRounds()));
        sql.append(FSUtils.InsertDBFieldValue(getWinnerID()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE PlayoffTournament SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TournamentName", getTournamentName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Status", getStatus(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NumTeams", getNumTeams()));
        sql.append(FSUtils.UpdateDBFieldValue("NumRounds", getNumRounds()));
        sql.append(FSUtils.UpdateDBFieldValue("WinnerID", getWinnerID()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE TournamentID = ").append(getTournamentID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
