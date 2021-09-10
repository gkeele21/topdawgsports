package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;
import tds.main.bo.FSLeague;

import java.io.Serializable;

import static tds.data.CTColumnLists._Cols;

public class MarchMadnessLeague implements Serializable {

    // DB FIELDS
    private Integer _FSLeagueID;
    private Integer _TournamentID;

    // OBJECTS
    private FSLeague _FSLeague;
    private MarchMadnessTournament _Tournament;

    // CONSTRUCTORS
    public MarchMadnessLeague() {
    }

    public MarchMadnessLeague(int fsLeagueId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessLeague", "l.", "")).append(",");
            sql.append(_Cols.getColumnList("MarchMadnessTournament", "t.", "MarchMadnessTournament$")).append(",");
            sql.append(_Cols.getColumnList("FSLeague", "fsl.", "FSLeague$"));
            sql.append("FROM MarchMadnessLeague l ");
            sql.append("JOIN MarchMadnessTournament t ON t.TournamentID = l.TournamentID ");
            sql.append("JOIN FSLeague fsl ON fsl.FSLeagueID = l.FSLeagueID ");
            sql.append("WHERE l.FSLeagueID = ").append(fsLeagueId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                InitFromCRS(crs, "");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public MarchMadnessLeague(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }

    // GETTERS
    public int getFSLeagueID() {return _FSLeagueID;}
    public int getTournamentID() {return _TournamentID;}
    public FSLeague getFSLeague() {return _FSLeague;}
    public MarchMadnessTournament getTournament() {return _Tournament;}

    // SETTERS
    public void setFSLeagueID(int FSLeagueID) {_FSLeagueID = FSLeagueID;}
    public void setTournamentID(int TournamentID) {_TournamentID = TournamentID;}
    public void setFSLeague(FSLeague FSLeague) {_FSLeague = FSLeague;}
    public void setTournament(MarchMadnessTournament Tournament) {_Tournament = Tournament;}

    // PUBLIC METHODS

    public static void Insert(int fsLeagueId, int tournamentId) {
        StringBuilder sql = new StringBuilder();
        try {
            sql.append("INSERT INTO MarchMadnessLeague VALUES (");
            sql.append(fsLeagueId).append(", ").append(tournamentId).append(")");

            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSLeagueID")) { setFSLeagueID(crs.getInt(prefix + "FSLeagueID")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) { setTournamentID(crs.getInt(prefix + "TournamentID")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSLeague$", "FSLeagueID")) { setFSLeague(new FSLeague(crs, "FSLeague$")); }
            if (FSUtils.fieldExists(crs, "MarchMadnessTournament$", "TournamentID")) { setTournament(new MarchMadnessTournament(crs, "MarchMadnessTournament$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
