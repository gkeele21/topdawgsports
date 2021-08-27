package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;
import tds.main.bo.FSTeam;

public class SeedChallenge implements Serializable {

    // DB FIELDS
    private Integer _TournamentID;
    private Integer _FSTeamID;
    private Integer _SeedChallengeGroupID;
    private Integer _TeamSeedPickedID;

    // OBJECTS
    private MarchMadnessTournament _Tournament;
    private FSTeam _FSTeam;    
    private SeedChallengeGroup _SeedChallengeGroup;
    private MarchMadnessTeamSeed _TeamSeedPicked;

    // ADDITIONAL FIELDS
    private boolean _isPickCorrect;

    // CONSTRUCTORS
    public SeedChallenge() {
    }

    public SeedChallenge(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getTournamentID() {return _TournamentID;}
    public Integer getFSTeamID() {return _FSTeamID;}
    public Integer getSeedChallengeGroupID() {return _SeedChallengeGroupID;}
    public Integer getTeamSeedPickedID() {return _TeamSeedPickedID;}
    public MarchMadnessTournament getTournament() {return _Tournament;}    
    public FSTeam getFSTeam() {return _FSTeam;}    
    public SeedChallengeGroup getSeedChallengeGroup() {return _SeedChallengeGroup;}
    public MarchMadnessTeamSeed getTeamSeedPicked() {return _TeamSeedPicked;}
    public boolean getIsPickCorrect() {return _isPickCorrect;}    
    
    // SETTERS
    public void setTournamentID(int TournamentID) {_TournamentID = TournamentID;}
    public void setFSTeamID(int FSTeamID) {_FSTeamID = FSTeamID;}
    public void setSeedChallengeGroupID(int SeedChallengeGroupID) {_SeedChallengeGroupID = SeedChallengeGroupID;}
    public void setTeamSeedPickedID(int TeamSeedPickedID) {_TeamSeedPickedID = TeamSeedPickedID;}
    public void setTournament(MarchMadnessTournament Tournament) {_Tournament = Tournament;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setSeedChallengeGroup(SeedChallengeGroup SeedChallengeGroup) {_SeedChallengeGroup = SeedChallengeGroup;}
    public void setTeamSeedPicked(MarchMadnessTeamSeed TeamSeedPicked) {_TeamSeedPicked = TeamSeedPicked;}    
    public void setIsPickCorrect(boolean IsCorrect) {_isPickCorrect = IsCorrect;}    

    // PUBLIC METHODS
    
    /*  This method is used to get a user's picks for the College Tournament's Seed Challenge game. */
    public static List<SeedChallenge> GetAllSeedChallengePicks(int tournamentId) {
        List<SeedChallenge> picks = new ArrayList<SeedChallenge>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("SeedChallenge", "sc.", "")).append(",");        
        sql.append(_Cols.getColumnList("FSTeam", "fst.", "FSTeam$")).append(",");
        sql.append(_Cols.getColumnList("FSUser", "u.", "FSUser$")).append(",");
        sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "ts.", "MarchMadnessTeamSeed$")).append(",");
        sql.append(_Cols.getColumnList("Team", "tm.", "Team$"));
        sql.append("FROM SeedChallenge sc ");
        sql.append("JOIN FSTeam fst ON fst.FSTeamID = sc.FSTeamID ");
        sql.append("JOIN FSUser u ON u.FSUserID = fst.FSUserID ");
        sql.append("JOIN MarchMadnessTeamSeed ts ON ts.TeamSeedID = sc.TeamSeedPickedID ");
        sql.append("JOIN Team tm ON tm.TeamID = ts.TeamID ");        
        sql.append("WHERE sc.TournamentID = ").append(tournamentId).append(" ");
        sql.append("ORDER BY fst.FSTeamID, sc.FSTeamID, ts.SeedNumber");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new SeedChallenge(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return picks;
   }

    /*  This method is used to get a user's picks for the College Tournament's Seed Challenge game. */
    public static List<SeedChallenge> GetSeedChallengePicks(int tournamentId, int fsTeamId) {
        List<SeedChallenge> picks = new ArrayList<SeedChallenge>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("SeedChallengeGroup", "scg.", "SeedChallengeGroup$")).append(",");
        sql.append(_Cols.getColumnList("SeedChallenge", "sc.", "")).append(",");
        sql.append(_Cols.getColumnList("MarchMadnessTournament", "t.", "MarchMadnessTournament$")).append(",");
        sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "ts.", "MarchMadnessTeamSeed$")).append(",");
        sql.append(_Cols.getColumnList("Team", "tm.", "Team$")).append(",");
        sql.append(_Cols.getColumnList("FSTeam", "fst.", "FSTeam$"));        
        sql.append("FROM SeedChallengeGroup scg ");
        sql.append("LEFT JOIN SeedChallenge sc ON sc.SeedChallengeGroupID = scg.SeedChallengeGroupID AND sc.FSTeamID = ").append(fsTeamId).append(" ");
        sql.append("LEFT JOIN MarchMadnessTournament t ON t.TournamentID = sc.TournamentID ");
        sql.append("LEFT JOIN MarchMadnessTeamSeed ts ON ts.TeamSeedID = sc.TeamSeedPickedID ");
        sql.append("LEFT JOIN Team tm ON tm.TeamID = ts.TeamID ");
        sql.append("LEFT JOIN FSTeam fst ON fst.FSTeamID = sc.FSTeamID ");        
        sql.append("WHERE scg.TournamentID = ").append(tournamentId).append(" ");
        sql.append("ORDER BY scg.StartingSeedNumber");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new SeedChallenge(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return picks;
   }

    /*  This method is used to store the SeedChallenge data in the DB. */
    public void Save() {        
        boolean doesExist = FSUtils.DoesARecordExistInDB("SeedChallenge", "FSTeamID", getFSTeamID(), "SeedChallengeGroupID", getSeedChallengeGroupID());
        if (doesExist) Update(); else Insert();
    } 

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB Fields
            //if (FSUtils.fieldExists(crs, prefix, "SeedChallengeID")) { setSeedChallengeID(crs.getInt(prefix + "SeedChallengeID")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) { setTournamentID(crs.getInt(prefix + "TournamentID")); }
            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) { setFSTeamID(crs.getInt(prefix + "FSTeamID")); }
            if (FSUtils.fieldExists(crs, prefix, "SeedChallengeGroupID")) { setSeedChallengeGroupID(crs.getInt(prefix + "SeedChallengeGroupID")); }
            if (FSUtils.fieldExists(crs, prefix, "TeamSeedPickedID")) { setTeamSeedPickedID(crs.getInt(prefix + "TeamSeedPickedID")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "MarchMadnessTournament$", "TournamentID")) { setTournament(new MarchMadnessTournament(crs, "MarchMadnessTournament$")); }
            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) { setFSTeam(new FSTeam(crs, "FSTeam$")); }                     
            if (FSUtils.fieldExists(crs, "SeedChallengeGroup$", "SeedChallengeGroupID")) { setSeedChallengeGroup(new SeedChallengeGroup(crs, "SeedChallengeGroup$")); }
            if (FSUtils.fieldExists(crs, "MarchMadnessTeamSeed$", "TeamSeedID")) { setTeamSeedPicked(new MarchMadnessTeamSeed(crs, "MarchMadnessTeamSeed$")); }   

            // ADDITIONAL FIELDS
            if (FSUtils.fieldExists(crs, "Game$", "WinnerID")) {
                int winnerId = crs.getInt("Game$WinnerID");
                if (winnerId > 0) { setIsPickCorrect((winnerId == getTeamSeedPickedID()) ? true : false); }
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }   

    /*  This method inserts a new record into the DB. */
    public void Insert() {        
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO SeedChallenge ");
        sql.append("(TournamentID, FSTeamID, SeedChallengeGroupID, TeamSeedPickedID) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getTournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getSeedChallengeGroupID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeamSeedPickedID()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    public void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE SeedChallenge SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TournamentID", getTournamentID()));
        sql.append(FSUtils.UpdateDBFieldValue("FSTeamID", getFSTeamID()));
        sql.append(FSUtils.UpdateDBFieldValue("SeedChallengeGroupID", getSeedChallengeGroupID()));
        sql.append(FSUtils.UpdateDBFieldValue("TeamSeedPickedID", getTeamSeedPickedID()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSTeamID = ").append(getFSTeamID()).append(" AND SeedChallengeGroupID = ").append(getSeedChallengeGroupID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
