package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSTournament implements Serializable {

    // DB FIELDS
    private int _TournamentID;
    private String _TournamentName;
    private int _TournamentStatus;
    private int _NumTeams;
    private int _NumRounds;
    private int _NumRegions;
    private int _WinnerID;

    // OBJECTS
    private FSTeam _Winner;
    private Team _NCAAWinner;

    // CONSTRUCTORS
    public FSTournament() {
    }

    public FSTournament(CachedRowSet crs, String prefix) {
        initFromCRS(crs, prefix);
    }

    // GETTERS
    public int getTournamentID() {return _TournamentID;}
    public String getTournamentName() {return _TournamentName;}    
    public int getTournamentStatus() {return _TournamentStatus;}
    public int getNumTeams() {return _NumTeams;}
    public int getNumRounds() {return _NumRounds;}
    public int getNumRegions() {return _NumRegions;}
    public int getWinnerID() {return _WinnerID;}
    public FSTeam getWinner() {return _Winner;}    
    public Team getNCAAWinner() {return _NCAAWinner;}
    
    // SETTERS
    public void setTournamentID(int TournamentID) {_TournamentID = TournamentID;}
    public void setTournamentName(String TournamentName) {_TournamentName = TournamentName;}
    public void setTournamentStatus(int TournamentStatus) {_TournamentStatus = TournamentStatus;}
    public void setNumTeams(int NumTeams) {_NumTeams = NumTeams;}
    public void setNumRounds(int NumRounds) {_NumRounds = NumRounds;}
    public void setNumRegions(int NumRegions) {_NumRegions = NumRegions;}
    public void setWinnerID(int WinnerID) {_WinnerID = WinnerID;}
    public void setWinner(FSTeam Winner) {_Winner = Winner;}
    public void setNCAAWinner(Team NCAAWinner) {_NCAAWinner = NCAAWinner;}

    public static FSTournament getTournamentByFSLeague(int fsLeagueId) {

        FSTournament tournamentObj = null;
        CachedRowSet crs = null;

        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSTournament", "", ""));
            sql.append("FROM FSTournament ");
            sql.append("WHERE FSLeagueID = ").append(fsLeagueId);

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            if (crs.next()) {
                tournamentObj = new FSTournament(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return tournamentObj;
    }
    
    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) {
                setTournamentID(crs.getInt(prefix + "TournamentID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TournamentName")) {
                setTournamentName(crs.getString(prefix + "TournamentName"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "TournamentStatus")) {
                setTournamentStatus(crs.getInt(prefix + "TournamentStatus"));
            }

            if (FSUtils.fieldExists(crs, prefix, "NumTeams")) {
                setNumTeams(crs.getInt(prefix + "NumTeams"));
            }

            if (FSUtils.fieldExists(crs, prefix, "NumRounds")) {
                setNumRounds(crs.getInt(prefix + "NumRounds"));
            }

            if (FSUtils.fieldExists(crs, prefix, "NumRegions")) {
                setNumRegions(crs.getInt(prefix + "NumRegions"));
            }

            if (FSUtils.fieldExists(crs, prefix, "WinnerID")) {
                setWinnerID(crs.getInt(prefix + "WinnerID"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "Winner$", "FSTeamID")) {
                setWinner(new FSTeam(crs, "Winner$"));
            }
            
            if (FSUtils.fieldExists(crs, "Winner$", "TeamID")) {
                setNCAAWinner(new Team(crs, "Winner$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    } 
}
