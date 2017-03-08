package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSSeedChallengeGroup implements Serializable {

    // DB FIELDS
    private int _SeedChallengeGroupID;
    private int _TournamentID;
    private int _StartingSeedNumber;
    private int _EndingSeedNumber;
    
    // OBJECTS
    private FSTournament _FSTournament;

    // CONSTRUCTORS
    public FSSeedChallengeGroup() {
    }

    public FSSeedChallengeGroup(CachedRowSet crs, String prefix) {
        initFromCRS(crs, prefix);
    }

    // GETTERS
    public int getSeedChallengeGroupID() {return _SeedChallengeGroupID;}
    public int getTournamentID() {return _TournamentID;}
    public int getStartingSeedNumber() {return _StartingSeedNumber;}
    public int getEndingSeedNumber() {return _EndingSeedNumber;}
    public FSTournament getFSTournament() {return _FSTournament;}
    
    // SETTERS
    public void setSeedChallengeGroupID(int SeedChallengeGroupID) {_SeedChallengeGroupID = SeedChallengeGroupID;}
    public void setTournamentID(int TournamentID) {_TournamentID = TournamentID;}
    public void setStartingSeedNumber(int StartingSeedNumber) {_StartingSeedNumber = StartingSeedNumber;}
    public void setEndingSeedNumber(int EndingSeedNumber) {_EndingSeedNumber = EndingSeedNumber;}
    public void setFSTournament(FSTournament FSTournament) {_FSTournament = FSTournament;}
    
    // PUBLIC METHODS
    
    /* This retrieves all of the Seed Groups for a given fsTournament */
    public static List<FSSeedChallengeGroup> GetSeedGroups(int tournamentId) {

        List<FSSeedChallengeGroup> seedGroups = new ArrayList<FSSeedChallengeGroup>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSSeedChallengeGroup", "", ""));
        sql.append("FROM FSSeedChallengeGroup ");
        sql.append("WHERE TournamentID = ").append(tournamentId).append(" ");
        sql.append("ORDER BY StartingSeedNumber");

        // Execute Query
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                seedGroups.add(new FSSeedChallengeGroup(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return seedGroups;
   }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "SeedChallengeGroupID")) {
                setSeedChallengeGroupID(crs.getInt(prefix + "SeedChallengeGroupID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) {
                setTournamentID(crs.getInt(prefix + "TournamentID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "StartingSeedNumber")) {
                setStartingSeedNumber(crs.getInt(prefix + "StartingSeedNumber"));
            }

            if (FSUtils.fieldExists(crs, prefix, "EndingSeedNumber")) {
                setEndingSeedNumber(crs.getInt(prefix + "EndingSeedNumber"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTournament$", "FSTournamentID")) {
                setFSTournament(new FSTournament(crs, "FSTournament$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}