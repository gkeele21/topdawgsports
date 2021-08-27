package tds.playoff.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;
import tds.main.bo.FSSeasonWeek;

public class PlayoffRound implements Serializable {

    // DB FIELDS
    private Integer _RoundID;
    private Integer _TournamentID;
    private Integer _FSSeasonWeekID;
    private Integer _RoundNumber;
    private String _RoundName;
    private Integer _NumTeams;

    // OBJECTS
    private PlayoffTournament _PlayoffTournament;
    private FSSeasonWeek _FSSeasonWeek;

    // CONSTRUCTORS
    public PlayoffRound() {
    }

    public PlayoffRound(int roundId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("PlayoffRound", "", ""));
            sql.append("FROM PlayoffRound ");
            sql.append("WHERE RoundID = ").append(roundId);

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

    public PlayoffRound(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }

    // GETTERS
    public Integer getRoundID() {return _RoundID;}
    public Integer getTournamentID() {return _TournamentID;}
    public Integer getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public Integer getRoundNumber() {return _RoundNumber;}
    public String getRoundName() {return _RoundName;}
    public Integer getNumTeams() {return _NumTeams;}
    public PlayoffTournament getPlayoffTournament() {return _PlayoffTournament;}
    public FSSeasonWeek getFSSeasonWeek() {return _FSSeasonWeek;}
    
    // SETTERS
    public void setRoundID(Integer RoundID) {_RoundID = RoundID;}
    public void setTournamentID(Integer TournamentID) {_TournamentID = TournamentID;}
    public void setFSSeasonWeekID(Integer FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setRoundNumber(Integer RoundNumber) {_RoundNumber = RoundNumber;}
    public void setRoundName(String RoundName) {_RoundName = RoundName;}
    public void setNumTeams(Integer NumTeams) {_NumTeams = NumTeams;}
    public void setPlayoffTournament(PlayoffTournament PlayoffTournament) {_PlayoffTournament = PlayoffTournament;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}

    // PUBLIC METHODS

    public static PlayoffRound GetCurrentRound(int tournamentId) {        
        PlayoffRound round = null;
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("PlayoffRound", "rd.", "")).append(", ");
            sql.append(_Cols.getColumnList("PlayoffTournament", "t.", "PlayoffTournament$")).append(", ");;
            sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$"));
            sql.append("FROM PlayoffRound rd ");
            sql.append("JOIN PlayoffTournament t ON t.TournamentID = rd.TournamentID ");
            sql.append("JOIN FSSeasonWeek fssw ON fssw.FSSeasonWeekID = rd.FSSeasonWeekID ");
            sql.append("WHERE t.TournamentID = ").append(tournamentId).append(" AND fssw.Status = '").append(FSSeasonWeek.Status.CURRENT).append("'");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            crs.next();
            round = new PlayoffRound(crs, "");

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return round;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("PlayoffRound", "RoundID", getRoundID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "RoundID")) { setRoundID(crs.getInt(prefix + "RoundID")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) { setTournamentID(crs.getInt(prefix + "TournamentID")); }
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) { setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID")); }
            if (FSUtils.fieldExists(crs, prefix, "RoundNumber")) { setRoundNumber(crs.getInt(prefix + "RoundNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "RoundName")) { setRoundName(crs.getString(prefix + "RoundName")); }
            if (FSUtils.fieldExists(crs, prefix, "NumTeams")) { setNumTeams(crs.getInt(prefix + "NumTeams")); }
            
            // OBJECTS            
            if (FSUtils.fieldExists(crs, "PlayoffTournament$", "TournamentID")) { setPlayoffTournament(new PlayoffTournament(crs, "PlayoffTournament$")); }
            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) { setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO PlayoffRound ");
        sql.append("(RoundID, TournamentID, FSSeasonWeekID, RoundNumber, RoundName, NumTeams) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getRoundID()));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getRoundNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getRoundName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNumTeams()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE PlayoffRound SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TournamentID", getTournamentID()));
        sql.append(FSUtils.UpdateDBFieldValue("FSSeasonWeekID", getFSSeasonWeekID()));
        sql.append(FSUtils.UpdateDBFieldValue("RoundNumber", getRoundNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("RoundName", getRoundName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NumTeams", getNumTeams()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE RoundID = ").append(getRoundID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
