package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;
import tds.main.bo.Team;

public class MarchMadnessTournament implements Serializable {
    
    public enum Status {UPCOMING, ONGOING, ENDED};

    // DB FIELDS
    private Integer _TournamentID;
    private String _TournamentName;
    private Integer _SportYear;
    private String _Status;
    private Integer _NumTeams;
    private Integer _NumRounds;
    private Integer _NumRegions;
    
    // OBJECTS
    private Team _Winner;

    // CONSTRUCTORS
    public MarchMadnessTournament() {
    }
    
    public MarchMadnessTournament(int tournamentId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessTournament", "", ""));
            sql.append("FROM MarchMadnessTournament ");
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
       
    public MarchMadnessTournament(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }

    // GETTERS
    public Integer getTournamentID() {return _TournamentID;}
    public String getTournamentName() {return _TournamentName;}    
    public Integer getSportYear() {return _SportYear;}
    public String getStatus() {return _Status;}
    public Integer getNumTeams() {return _NumTeams;}
    public Integer getNumRounds() {return _NumRounds;}
    public Integer getNumRegions() {return _NumRegions;}
    
    // SETTERS
    public void setTournamentID(Integer TournamentID) {_TournamentID = TournamentID;}
    public void setTournamentName(String TournamentName) {_TournamentName = TournamentName;}
    public void setSportYear(Integer SportYear) {_SportYear = SportYear;}
    public void setStatus(String Status) {_Status = Status;}
    public void setNumTeams(Integer NumTeams) {_NumTeams = NumTeams;}
    public void setNumRounds(Integer NumRounds) {_NumRounds = NumRounds;}
    public void setNumRegions(Integer NumRegions) {_NumRegions = NumRegions;}
    
    // PUBLIC METHODS
    
    public static MarchMadnessTournament GetTournamentByYear(int sportYear) {
        CachedRowSet crs = null;
        MarchMadnessTournament tournament = null;
        StringBuilder sql = new StringBuilder();

        try {
            sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessTournament", "", ""));
            sql.append("FROM MarchMadnessTournament ");
            sql.append("WHERE SportYear = ").append(sportYear).append(" ");
            sql.append("ORDER BY TournamentID desc");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                tournament = new MarchMadnessTournament(crs,"");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return tournament;
   }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("MarchMadnessTournament", "TournamentID", getTournamentID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) { setTournamentID(crs.getInt(prefix + "TournamentID")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentName")) { setTournamentName(crs.getString(prefix + "TournamentName")); }  
            if (FSUtils.fieldExists(crs, prefix, "SportYear")) { setNumRegions(crs.getInt(prefix + "SportYear")); }
            if (FSUtils.fieldExists(crs, prefix, "Status")) { setStatus(crs.getString(prefix + "Status")); }
            if (FSUtils.fieldExists(crs, prefix, "NumTeams")) { setNumTeams(crs.getInt(prefix + "NumTeams")); }
            if (FSUtils.fieldExists(crs, prefix, "NumRounds")) { setNumRounds(crs.getInt(prefix + "NumRounds")); }
            if (FSUtils.fieldExists(crs, prefix, "NumRegions")) { setNumRegions(crs.getInt(prefix + "NumRegions")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO MarchMadnessTournament ");
        sql.append("(TournamentID, TournamentName, SportYear, Status, NumTeams, NumRounds, NumRegions) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getTournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getSportYear()));
        sql.append(FSUtils.InsertDBFieldValue(getStatus(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNumTeams()));
        sql.append(FSUtils.InsertDBFieldValue(getNumRounds()));
        sql.append(FSUtils.InsertDBFieldValue(getNumRegions()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE MarchMadnessTournament SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TournamentName", getTournamentName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("SportYear", getSportYear()));
        sql.append(FSUtils.UpdateDBFieldValue("Status", getStatus(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NumTeams", getNumTeams()));
        sql.append(FSUtils.UpdateDBFieldValue("NumRounds", getNumRounds()));
        sql.append(FSUtils.UpdateDBFieldValue("NumRegions", getNumRegions()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE TournamentID = ").append(getTournamentID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
