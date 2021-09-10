package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;

import java.io.Serializable;

import static tds.data.CTColumnLists._Cols;

public class FSTournamentLeague implements Serializable {

    // DB FIELDS
    private int _FSLeagueID;
    private int _TournamentID;

    // OBJECTS
    private FSLeague _FSLeague;
    private FSTournament _FSTournament;

    // CONSTRUCTORS
    public FSTournamentLeague() {
    }

    public FSTournamentLeague(CachedRowSet crs, String prefix) {
        initFromCRS(crs, prefix);
    }

    // GETTERS
    public int getFSLeagueID() {return _FSLeagueID;}
    public int getTournamentID() {return _TournamentID;}
    public FSLeague getFSLeague() {if (_FSLeague == null && _FSLeagueID > 0) {_FSLeague = new FSLeague(_FSLeagueID);}return _FSLeague;}
    public FSTournament geFSTournament() {return _FSTournament;}

    // SETTERS
    public void setFSLeagueID(int FSLeagueID) {_FSLeagueID = FSLeagueID;}
    public void setTournamentID(int TournamentID) {_TournamentID = TournamentID;}
    public void setFSLeague(FSLeague FSLeague) {_FSLeague = FSLeague;}
    public void setFSTournament(FSTournament FSTournament) {_FSTournament = FSTournament;}

    // PUBLIC METHODS

    public static FSTournament getTournamentByFSLeague(int fsLeagueId) {

        FSTournament objTournament = null;
        CachedRowSet crs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSTournamentLeague", "tl.", "FSTournamentLeague$")).append(",");
            sql.append(_Cols.getColumnList("FSTournament", "t.", "")).append(",");
            sql.append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
            sql.append("FROM FSTournamentLeague tl ");
            sql.append("JOIN FSTournament t ON t.TournamentID = tl.TournamentID ");
            sql.append("JOIN FSLeague l ON l.FSLeagueID = tl.FSLeagueID ");
            sql.append("WHERE tl.FSLeagueID = ").append(fsLeagueId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                objTournament = new FSTournament(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return objTournament;
    }

    public static int insert(int fsLeagueId, int tournamentId) {

        int retVal = 0;
        StringBuilder sql = new StringBuilder();
        try {

            // Create SQL statement
            sql.append(" INSERT INTO FSTournamentLeague ");
            sql.append(" ( FSLeagueID, TournamentID)");
            sql.append(" VALUES (").append(fsLeagueId);
            sql.append(" , ").append(tournamentId);
            sql.append(" )");

            retVal = CTApplication._CT_QUICK_DB.executeInsert(sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSLeagueID")) {
                setFSLeagueID(crs.getInt(prefix + "FSLeagueID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) {
                setTournamentID(crs.getInt(prefix + "TournamentID"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSLeague$", "FSLeagueID")) {
                setFSLeague(new FSLeague(crs, "FSLeague$"));
            }

            if (FSUtils.fieldExists(crs, "FSTournament$", "TournamentID")) {
                setFSTournament(new FSTournament(crs, "FSTournament$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
