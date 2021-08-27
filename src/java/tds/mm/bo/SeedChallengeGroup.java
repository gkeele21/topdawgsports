package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class SeedChallengeGroup implements Serializable {

    // DB FIELDS
    private Integer _SeedChallengeGroupID;
    private Integer _TournamentID;
    private Integer _StartingSeedNumber;
    private Integer _EndingSeedNumber;
    
    // OBJECTS
    private MarchMadnessTournament _Tournament;

    // CONSTRUCTORS
    public SeedChallengeGroup() {
    }

    public SeedChallengeGroup(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }

    // GETTERS
    public Integer getSeedChallengeGroupID() {return _SeedChallengeGroupID;}
    public Integer getTournamentID() {return _TournamentID;}
    public Integer getStartingSeedNumber() {return _StartingSeedNumber;}
    public Integer getEndingSeedNumber() {return _EndingSeedNumber;}
    public MarchMadnessTournament getTournament() {return _Tournament;}
    
    // SETTERS
    public void setSeedChallengeGroupID(Integer SeedChallengeGroupID) {_SeedChallengeGroupID = SeedChallengeGroupID;}
    public void setTournamentID(Integer TournamentID) {_TournamentID = TournamentID;}
    public void setStartingSeedNumber(Integer StartingSeedNumber) {_StartingSeedNumber = StartingSeedNumber;}
    public void setEndingSeedNumber(Integer EndingSeedNumber) {_EndingSeedNumber = EndingSeedNumber;}
    public void setTournament(MarchMadnessTournament Tournament) {_Tournament = Tournament;}
    
    // PUBLIC METHODS
    
    /* This retrieves all of the Seed Groups for a given fsTournament */
    public static List<SeedChallengeGroup> GetSeedGroups(int tournamentId) {

        List<SeedChallengeGroup> seedGroups = new ArrayList<SeedChallengeGroup>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("SeedChallengeGroup", "", ""));
        sql.append("FROM SeedChallengeGroup ");
        sql.append("WHERE TournamentID = ").append(tournamentId).append(" ");
        sql.append("ORDER BY StartingSeedNumber");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                seedGroups.add(new SeedChallengeGroup(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return seedGroups;
   }
    
    public void Save() {
            boolean exists = FSUtils.DoesARecordExistInDB("SeedChallengeGroup", "SeedChallengeGroupID", getSeedChallengeGroupID());
            if (exists) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "SeedChallengeGroupID")) { setSeedChallengeGroupID(crs.getInt(prefix + "SeedChallengeGroupID")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) { setTournamentID(crs.getInt(prefix + "TournamentID")); }
            if (FSUtils.fieldExists(crs, prefix, "StartingSeedNumber")) { setStartingSeedNumber(crs.getInt(prefix + "StartingSeedNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "EndingSeedNumber")) { setEndingSeedNumber(crs.getInt(prefix + "EndingSeedNumber")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "Tournament$", "TournamentID")) { setTournament(new MarchMadnessTournament(crs, "Tournament$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO SeedChallengeGroup ");
        sql.append("(SeedChallengeGroupID, TournamentID, StartingSeedNumber, EndingSeedNumber) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getSeedChallengeGroupID()));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getStartingSeedNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getEndingSeedNumber()));        
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE SeedChallengeGroup SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TournamentID", getTournamentID()));
        sql.append(FSUtils.UpdateDBFieldValue("StartingSeedNumber", getStartingSeedNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("EndingSeedNumber", getEndingSeedNumber()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE SeedChallengeGroupID = ").append(getSeedChallengeGroupID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
