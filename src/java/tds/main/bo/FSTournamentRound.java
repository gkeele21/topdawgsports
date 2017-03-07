package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSTournamentRound implements Serializable {

    // DB FIELDS
    private int _RoundID;
    private int _TournamentID;
    private int _FSSeasonWeekID;
    private int _RoundNumber;
    private String _RoundName;
    private int _NumTeams;

    // OBJECTS
    private FSTournament _FSTournament;
    private FSSeasonWeek _FSSeasonWeek;

    // CONSTRUCTORS
    public FSTournamentRound() {
    }

    public FSTournamentRound(int roundId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSTournamentRound", "", ""));
            sql.append("FROM FSTournamentRound ");
            sql.append("WHERE RoundID = ").append(roundId);

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

    public FSTournamentRound(CachedRowSet crs, String prefix) {
        initFromCRS(crs, prefix);
    }

    // GETTERS
    public int getRoundID() {return _RoundID;}
    public int getTournamentID() {return _TournamentID;}
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public int getRoundNumber() {return _RoundNumber;}
    public String getRoundName() {return _RoundName;}
    public int getNumTeams() {return _NumTeams;}
    public FSTournament getFSTournament() {return _FSTournament;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}
    
    // SETTERS
    public void setRoundID(int RoundID) {_RoundID = RoundID;}
    public void setTournamentID(int TournamentID) {_TournamentID = TournamentID;}
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setRoundNumber(int RoundNumber) {_RoundNumber = RoundNumber;}
    public void setRoundName(String RoundName) {_RoundName = RoundName;}
    public void setNumTeams(int NumTeams) {_NumTeams = NumTeams;}
    public void setFSTournament(FSTournament FSTournament) {_FSTournament = FSTournament;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}

    // PUBLIC METHODS

    public static FSTournamentRound getCurrentRoundByFSSeasonWeek(int fsSeasonWeekId) {

        FSTournamentRound objRound = null;
        CachedRowSet crs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSTournamentRound", "r.", "")).append(", ");
            sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$"));
            sql.append("FROM FSTournamentRound r ");
            sql.append("INNER JOIN FSSeasonWeek fssw ON fssw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append("WHERE r.FSSeasonWeekID = ").append(fsSeasonWeekId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                objRound = new FSTournamentRound(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return objRound;
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "RoundID")) {
                setRoundID(crs.getInt(prefix + "RoundID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) {
                setTournamentID(crs.getInt(prefix + "TournamentID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) {
                setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RoundNumber")) {
                setRoundNumber(crs.getInt(prefix + "RoundNumber"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RoundName")) {
                setRoundName(crs.getString(prefix + "RoundName"));
            }

            if (FSUtils.fieldExists(crs, prefix, "NumTeams")) {
                setNumTeams(crs.getInt(prefix + "NumTeams"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTournament$", "TournamentID")) {
                setFSTournament(new FSTournament(crs, "FSTournament$"));
            }

            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) {
                setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}