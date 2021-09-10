package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;
import tds.main.bo.Team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static tds.data.CTColumnLists._Cols;

public class MarchMadnessTeamSeed implements Serializable {

    public enum Status {IN, OUT};

    // DB FIELDS
    private Integer _TeamSeedID;
    private Integer _TournamentID;
    private Integer _RegionID;
    private Integer _TeamID;
    private Integer _SeedNumber;
    private String _SeasonRecord;
    private String _Status;
    private Integer _TournamentWins;

    // OBJECTS
    private MarchMadnessTournament _Tournament;
    private MarchMadnessRegion _Region;
    private Team _Team;

    // ADDITIONAL FIELDS
    private Integer _TimesPicked;

    // CONSTRUCTORS
    public MarchMadnessTeamSeed() {
    }

    public MarchMadnessTeamSeed(int teamSeedId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessTeamSeed", "", ""));
            sql.append("FROM MarchMadnessTeamSeed ");
            sql.append("WHERE TeamSeedID = ").append(teamSeedId);

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

    public MarchMadnessTeamSeed(int tournamentId, int teamSeedId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessTeamSeed", "s.", ""));
            sql.append("FROM MarchMadnessTeamSeed s ");
            sql.append("WHERE s.TournamentID = ").append(tournamentId).append(" AND TeamSeedID = ").append(teamSeedId);

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

    public MarchMadnessTeamSeed(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }

    // GETTERS
    public Integer getTeamSeedID() {return _TeamSeedID;}
    public Integer getTournamentID() {return _TournamentID;}
    public Integer getRegionID() {return _RegionID;}
    public Integer getTeamID() {return _TeamID;}
    public Integer getSeedNumber() {return _SeedNumber;}
    public String getSeasonRecord() {return _SeasonRecord;}
    public String getStatus() {return _Status;}
    public Integer getTournamentWins() {return _TournamentWins;}
    public MarchMadnessTournament getTournament() {return _Tournament;}
    public MarchMadnessRegion getRegion() {return _Region;}
    public Team getTeam() {return _Team;}
    public Integer getTimesPicked() {return _TimesPicked;}

    // SETTERS
    public void setTeamSeedID(Integer TeamSeedID) {_TeamSeedID = TeamSeedID;}
    public void setTournamentID(Integer TournamentID) {_TournamentID = TournamentID;}
    public void setRegionID(Integer RegionID) {_RegionID = RegionID;}
    public void setTeamID(Integer TeamID) {_TeamID = TeamID;}
    public void setSeedNumber(Integer SeedNumber) {_SeedNumber = SeedNumber;}
    public void setSeasonRecord(String SeasonRecord) {_SeasonRecord = SeasonRecord;}
    public void setStatus(String Status) {_Status = Status;}
    public void setTournamentWins(Integer TournamentWins) {_TournamentWins = TournamentWins;}
    public void setTournament(MarchMadnessTournament Tournament) {_Tournament = Tournament;}
    public void setRegion(MarchMadnessRegion Region) {_Region = Region;}
    public void setTeam(Team Team) {_Team = Team;}
    public void setTimesPicked(Integer TimesPicked) {_TimesPicked = TimesPicked;}

    // PUBLIC METHODS

    /* This retrieves all of the Tournament teams */
    public static List<MarchMadnessTeamSeed> GetTournamentTeams(int tournamentId) {
        List<MarchMadnessTeamSeed> teams = new ArrayList<MarchMadnessTeamSeed>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessTeamSeed", "ts.", "")).append(",");
        sql.append(_Cols.getColumnList("MarchMadnessTournament", "t.", "MarchMadnessTournament$")).append(",");
        sql.append(_Cols.getColumnList("Team", "tm.", "Team$")).append(",");
        sql.append(_Cols.getColumnList("MarchMadnessRegion", "r.", "MarchMadnessRegion$"));
        sql.append("FROM MarchMadnessTeamSeed ts ");
        sql.append("JOIN MarchMadnessTournament t ON t.TournamentID = ts.TournamentID ");
        sql.append("JOIN MarchMadnessRegion r ON r.RegionID = ts.RegionID ");
        sql.append("LEFT JOIN Team tm ON tm.TeamID = ts.TeamID ");
        sql.append("WHERE ts.TournamentID = ").append(tournamentId).append(" ");
        sql.append("ORDER BY r.RegionNumber, ts.SeedNumber");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                teams.add(new MarchMadnessTeamSeed(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return teams;
    }

    /* This retrieves all of the Seed Groups for the given tournament */
    public static List<MarchMadnessTeamSeed> GetMarchMadnessTeamsBySeedGroup(int tournamentId, int seedGroupId) {
        List<MarchMadnessTeamSeed> availableTeams = new ArrayList<MarchMadnessTeamSeed>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessTeamSeed", "ts.", "")).append(",");
        sql.append(_Cols.getColumnList("MarchMadnessTournament", "t.", "MarchMadnessTournament$")).append(",");
        sql.append(_Cols.getColumnList("Team", "tm.", "Team$")).append(",");
        sql.append(_Cols.getColumnList("MarchMadnessRegion", "r.", "MarchMadnessRegion$")).append(",");
        sql.append("(SELECT count(TeamSeedPickedID) FROM SeedChallenge WHERE TeamSeedPickedID = ts.TeamSeedID) as TimesPicked ");
        sql.append("FROM MarchMadnessTeamSeed ts ");
        sql.append("JOIN MarchMadnessTournament t ON t.TournamentID = ts.TournamentID ");
        sql.append("JOIN Team tm ON tm.TeamID = ts.TeamID ");
        sql.append("JOIN MarchMadnessRegion r ON r.RegionID = ts.RegionID ");
        sql.append("WHERE ts.TournamentID = ").append(tournamentId).append(" AND ts.SeedNumber BETWEEN (SELECT StartingSeedNumber FROM SeedChallengeGroup WHERE SeedChallengeGroupID = ").append(seedGroupId).append(") AND (SELECT EndingSeedNumber FROM SeedChallengeGroup WHERE SeedChallengeGroupID = ").append(seedGroupId).append(") ");
        sql.append("ORDER BY ts.SeedNumber, r.RegionNumber");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                availableTeams.add(new MarchMadnessTeamSeed(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return availableTeams;
    }

    /*  This method determines if a record already exists in the DB so it can call either an Insert or Update */
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("MarchMadnessTeamSeed", "TeamSeedID", getTeamSeedID());
        if (doesExist) Update(); else Insert();
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "TeamSeedID")) { setTeamSeedID(crs.getInt(prefix + "TeamSeedID")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) { setTournamentID(crs.getInt(prefix + "TournamentID")); }
            if (FSUtils.fieldExists(crs, prefix, "RegionID")) { setRegionID(crs.getInt(prefix + "RegionID")); }
            if (FSUtils.fieldExists(crs, prefix, "TeamID")) { setTeamID(crs.getInt(prefix + "TeamID")); }
            if (FSUtils.fieldExists(crs, prefix, "SeedNumber")) { setSeedNumber(crs.getInt(prefix + "SeedNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "SeasonRecord")) { setSeasonRecord(crs.getString(prefix + "SeasonRecord")); }
            if (FSUtils.fieldExists(crs, prefix, "Status")) { setStatus(crs.getString(prefix + "Status")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentWins")) { setTournamentWins(crs.getInt(prefix + "TournamentWins")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "MarchMadnessTournament$", "TournamentID")) { setTournament(new MarchMadnessTournament(crs, "MarchMadnessTournament$")); }
            if (FSUtils.fieldExists(crs, "MarchMadnessRegion$", "RegionID")) { setRegion(new MarchMadnessRegion(crs, "MarchMadnessRegion$")); }
            if (FSUtils.fieldExists(crs, "Team$", "TeamID")) { setTeam(new Team(crs, "Team$")); }

            // ADDITIONAL FIELDS
            if (FSUtils.fieldExists(crs, prefix, "TimesPicked")) { setTimesPicked(crs.getInt(prefix + "TimesPicked")); }
            if (prefix.equals("MarchMadnessTeamSeed$") && FSUtils.fieldExists(crs, "PredictedTeam$", "TeamID")) { setTeam(new Team(crs, "PredictedTeam$")); }
            if (prefix.equals("TopTeamSeed$") && FSUtils.fieldExists(crs, "TopTeam$", "TeamID")) { setTeam(new Team(crs, "TopTeam$")); }
            if (prefix.equals("BottomTeamSeed$") && FSUtils.fieldExists(crs, "BottomTeam$", "TeamID")) { setTeam(new Team(crs, "BottomTeam$")); }
            if (prefix.equals("PreviousTopGameTeamSeedPicked$") && FSUtils.fieldExists(crs, "PreviousTopGameTeamPicked$", "TeamID")) { setTeam(new Team(crs, "PreviousTopGameTeamPicked$")); }
            if (prefix.equals("PreviousBottomGameTeamSeedPicked$") && FSUtils.fieldExists(crs, "PreviousBottomGameTeamPicked$", "TeamID")) { setTeam(new Team(crs, "PreviousBottomGameTeamPicked$")); }
            if (prefix.equals("GameWinnerTeamSeed$") && FSUtils.fieldExists(crs, "GameWinnerTeam$", "TeamID")) { setTeam(new Team(crs, "GameWinnerTeam$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    /*  This method inserts a new record into the DB. */
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO MarchMadnessTeamSeed ");
        sql.append("(TeamSeedID, TournamentID, RegionID, TeamID, SeedNumber, SeasonRecord, Status, TournamentWins) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getTeamSeedID()));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getRegionID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getSeedNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getSeasonRecord(), true));
        sql.append(FSUtils.InsertDBFieldValue(getStatus(), true));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentWins()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    /*  This method updates a record in the DB. */
    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE MarchMadnessTeamSeed SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TournamentID", getTournamentID()));
        sql.append(FSUtils.UpdateDBFieldValue("RegionID", getRegionID()));
        sql.append(FSUtils.UpdateDBFieldValue("TeamID", getTeamID()));
        sql.append(FSUtils.UpdateDBFieldValue("SeedNumber", getSeedNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("SeasonRecord", getSeasonRecord(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Status", getStatus(), true));
        sql.append(FSUtils.UpdateDBFieldValue("TournamentWins", getTournamentWins()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE TeamSeedID = ").append(getTeamSeedID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
