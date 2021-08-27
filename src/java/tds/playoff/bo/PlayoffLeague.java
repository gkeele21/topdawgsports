package tds.playoff.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;
import tds.main.bo.FSLeague;

public class PlayoffLeague implements Serializable {

    // DB FIELDS
    private Integer _FSLeagueID;
    private Integer _TournamentID;

    // OBJECTS
    private FSLeague _FSLeague;
    private PlayoffTournament _Tournament;

    // CONSTRUCTORS
    public PlayoffLeague() {
    }
    
    public PlayoffLeague(int fsLeagueId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("PlayoffLeague", "l.", "")).append(",");
            sql.append(_Cols.getColumnList("PlayoffTournament", "t.", "PlayoffTournament$")).append(",");
            sql.append(_Cols.getColumnList("FSLeague", "fsl.", "FSLeague$"));
            sql.append("FROM PlayoffLeague l ");
            sql.append("JOIN PlayoffTournament t ON t.TournamentID = l.TournamentID ");
            sql.append("JOIN FSLeague fsl ON fsl.FSLeagueID = l.FSLeagueID ");
            sql.append("WHERE l.FSLeagueID = ").append(fsLeagueId);

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

    public PlayoffLeague(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }

    // GETTERS
    public int getFSLeagueID() {return _FSLeagueID;}    
    public int getTournamentID() {return _TournamentID;}
    public FSLeague getFSLeague() {return _FSLeague;}
    public PlayoffTournament getTournament() {return _Tournament;}
    
    // SETTERS
    public void setFSLeagueID(int FSLeagueID) {_FSLeagueID = FSLeagueID;}
    public void setTournamentID(int TournamentID) {_TournamentID = TournamentID;}
    public void setFSLeague(FSLeague FSLeague) {_FSLeague = FSLeague;}
    public void setTournament(PlayoffTournament Tournament) {_Tournament = Tournament;}

    // PUBLIC METHODS    
       
    public static void Insert(int fsLeagueId, int tournamentId) {
        StringBuilder sql = new StringBuilder();
        try {
            sql.append("INSERT INTO PlayoffLeague VALUES (");
            sql.append(fsLeagueId).append(", ").append(tournamentId).append(")");
            
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());

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
            if (FSUtils.fieldExists(crs, "PlayoffTournament$", "TournamentID")) { setTournament(new PlayoffTournament(crs, "PlayoffTournament$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
