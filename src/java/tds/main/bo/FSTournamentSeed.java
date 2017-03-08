package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSTournamentSeed implements Serializable {

    // DB FIELDS
    private int _TournamentSeedID;
    private int _TournamentID;
    private int _RegionID;
    private int _TeamID;
    private int _SeedNumber;
    private String _SeasonRecord;
    private int _TourneyStatus;
    private int _TourneyWins;
    private int _TimesPicked;
    
    // OBJECTS
    private FSTournament _FSTournament;
    private Team _Team;
    private FSTournamentRegion _FSTournamentRegion;

    // CONSTRUCTORS
    public FSTournamentSeed() {
    }
    
    public FSTournamentSeed(int tournamentId, int teamId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSTournamentSeed", "s.", ""));
            sql.append("FROM FSTournamentSeed s ");
            sql.append("WHERE s.TournamentID = ").append(tournamentId).append(" AND TeamID = ").append(teamId);

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
    
    public FSTournamentSeed(CachedRowSet crs, String prefix) {
        initFromCRS(crs, prefix);
    }

    // GETTERS
    public int getTournamentSeedID() {return _TournamentSeedID;}
    public int getTournamentID() {return _TournamentID;}
    public int getRegionID() {return _RegionID;}
    public int getTeamID() {return _TeamID;}
    public int getSeedNumber() {return _SeedNumber;}
    public String getSeasonRecord() {return _SeasonRecord;}
    public int getTourneyStatus() {return _TourneyStatus;}
    public int getTourneyWins() {return _TourneyWins;}
    public int getTimesPicked() {return _TimesPicked;}
    public FSTournament getFSTournament() {return _FSTournament;}
    public Team getTeam() {if (_Team == null && _TeamID > 0) {_Team = new Team(_TeamID);}return _Team;}
    public FSTournamentRegion getFSTournamentRegion() {return _FSTournamentRegion;}

    // SETTERS
    public void setTournamentSeedID(int TournamentSeedID) {this._TournamentSeedID = TournamentSeedID;}
    public void setTournamentID(int TournamentID) {this._TournamentID = TournamentID;}
    public void setRegionID(int RegionID) {this._RegionID = RegionID;}
    public void setTeamID(int TeamID) {this._TeamID = TeamID;}
    public void setSeedNumber(int SeedNumber) {this._SeedNumber = SeedNumber;}
    public void setSeasonRecord(String SeasonRecord) {this._SeasonRecord = SeasonRecord;}
    public void setTourneyStatus(int TourneyStatus) {this._TourneyStatus = TourneyStatus;}
    public void setTourneyWins(int TourneyWins) {this._TourneyWins = TourneyWins;}
    public void setTimesPicked(int TimesPicked) {this._TimesPicked = TimesPicked;}
    public void setFSTournament(FSTournament FSTournament) {this._FSTournament = FSTournament;}
    public void setTeam(Team Team) {this._Team = Team;}
    public void setFSTournamentRegion(FSTournamentRegion FSTournamentRegion) {this._FSTournamentRegion = FSTournamentRegion;}
    
    // PUBLIC METHODS
    
    /* This retrieves all of the Seed Groups for a given fsTournament */
    public static List<FSTournamentSeed> GetTournamentTeams(int tournamentId) {

        List<FSTournamentSeed> teams = new ArrayList<FSTournamentSeed>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSTournamentSeed", "ts.", "")).append(",");
        sql.append(_Cols.getColumnList("FSTournament", "t.", "FSTournament$")).append(",");
        sql.append(_Cols.getColumnList("Team", "tm.", "Team$")).append(",");
        sql.append(_Cols.getColumnList("FSTournamentRegion", "r.", "FSTournamentRegion$"));
        sql.append("FROM FSTournamentSeed ts ");
        sql.append("JOIN FSTournament t ON t.TournamentID = ts.TournamentID ");
        sql.append("JOIN Team tm ON tm.TeamID = ts.TeamID ");
        sql.append("JOIN FSTournamentRegion r ON r.RegionID = ts.RegionID ");
        sql.append("WHERE ts.TournamentID = ").append(tournamentId).append(" ");        
        sql.append("ORDER BY r.RegionNumber, ts.SeedNumber");

        // Execute Query
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                teams.add(new FSTournamentSeed(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return teams;
    }
    
    /* This retrieves all of the Seed Groups for a given fsTournament */
    public static List<FSTournamentSeed> GetNCAATeamsBySeedGroup(int tournamentId, int seedGroupId) {

        List<FSTournamentSeed> availableTeams = new ArrayList<FSTournamentSeed>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSTournamentSeed", "ts.", "")).append(",");
        sql.append(_Cols.getColumnList("FSTournament", "t.", "FSTournament$")).append(",");
        sql.append(_Cols.getColumnList("Team", "tm.", "Team$")).append(",");
        sql.append(_Cols.getColumnList("FSTournamentRegion", "r.", "FSTournamentRegion$"));
        sql.append("FROM FSTournamentSeed ts ");
        sql.append("JOIN FSTournament t ON t.TournamentID = ts.TournamentID ");
        sql.append("JOIN Team tm ON tm.TeamID = ts.TeamID ");
        sql.append("JOIN FSTournamentRegion r ON r.RegionID = ts.RegionID ");
        sql.append("WHERE ts.TournamentID = ").append(tournamentId).append(" AND ts.SeedNumber BETWEEN (SELECT StartingSeedNumber FROM FSSeedChallengeGroup WHERE SeedChallengeGroupID = ").append(seedGroupId).append(") AND (SELECT EndingSeedNumber FROM FSSeedChallengeGroup WHERE SeedChallengeGroupID = ").append(seedGroupId).append(") ");        
        sql.append("ORDER BY ts.SeedNumber, r.RegionNumber");

        // Execute Query
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                availableTeams.add(new FSTournamentSeed(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return availableTeams;
    }
    
     /*  This method is used to store NFLPickem data in the DB. */
    public static int SaveTournamentSeed(FSTournamentSeed objTourneySeed) {

        int retVal = 0;

        boolean doesExist = DoesAPickAlreadyExistInDB(objTourneySeed.getTournamentSeedID());

        if (doesExist) {
            retVal = UpdateTournamentSeed(objTourneySeed);
        }
        else {
            retVal = InsertTournamentSeed(objTourneySeed);
        }

        return retVal;
    }

    // PRIVATE METHODS

    /*  This method determines if a record already exists in the DB. */
    private static boolean DoesAPickAlreadyExistInDB(int tourneySeedId) {

        boolean doesExist = false;
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        try {
            // Create SQL statement
            sql.append("SELECT 1 ");
            sql.append("FROM FSTournamentSeed ");
            sql.append("WHERE TournamentSeedID = ").append(tourneySeedId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                doesExist = true;
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return doesExist;
    }

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "TournamentSeedID")) {
                setTournamentSeedID(crs.getInt(prefix + "TournamentSeedID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) {
                setTournamentID(crs.getInt(prefix + "TournamentID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "RegionID")) {
                setRegionID(crs.getInt(prefix + "RegionID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TeamID")) {
                setTeamID(crs.getInt(prefix + "TeamID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "SeedNumber")) {
                setSeedNumber(crs.getInt(prefix + "SeedNumber"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "SeasonRecord")) {
                setSeasonRecord(crs.getString(prefix + "SeasonRecord"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "TourneyStatus")) {
                setTourneyStatus(crs.getInt(prefix + "TourneyStatus"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "TourneyWins")) {
                setTourneyWins(crs.getInt(prefix + "TourneyWins"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "TimesPicked")) {
                setTimesPicked(crs.getInt(prefix + "TimesPicked"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTournament$", "FSTournamentID")) {
                setFSTournament(new FSTournament(crs, "FSTournament$"));
            }

            if (FSUtils.fieldExists(crs, "Team$", "TeamID")) {
                setTeam(new Team(crs, "Team$"));
            }
            
            if (FSUtils.fieldExists(crs, "FSTournamentRegion$", "RegionID")) {
                setFSTournamentRegion(new FSTournamentRegion(crs, "FSTournamentRegion$"));
            } 

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    /*  This method inserts a new record into the DB. */
    private static int InsertTournamentSeed(FSTournamentSeed objTourneySeed) {

        int retVal = 0;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("INSERT INTO FSTournamentSeed ");
        sql.append("(TournamentID, RegionID, TeamID, SeedNumber, SeasonRecord, TourneyStatus, TourneyWins) ");
        sql.append("VALUES (");
        sql.append(objTourneySeed.getTournamentID()).append(", ");
        sql.append(objTourneySeed.getRegionID()).append(", ");
        sql.append(objTourneySeed.getTeamID()).append(", ");
        sql.append(objTourneySeed.getSeedNumber()).append(", '");
        sql.append(objTourneySeed.getSeasonRecord()).append("', ");
        sql.append(objTourneySeed.getTourneyStatus()).append(", ");
        sql.append(objTourneySeed.getTourneyWins()).append(" ");
        sql.append(")");

        // Call QueryCreator
        try {
            retVal = CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }    

    /*  This method updates a record in the DB. */
    private static int UpdateTournamentSeed(FSTournamentSeed objTourneySeed) {

        int retVal = 0;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("UPDATE FSTournamentSeed SET ");
        sql.append("RegionID = ").append(objTourneySeed.getRegionID()).append(", ");
        sql.append("TeamID = ").append(objTourneySeed.getTeamID()).append(", ");
        sql.append("SeedNumber = ").append(objTourneySeed.getSeedNumber()).append(", ");
        sql.append("SeasonRecord = '").append(objTourneySeed.getSeasonRecord()).append("', ");
        sql.append("TourneyStatus = ").append(objTourneySeed.getTourneyStatus()).append(", ");
        sql.append("TourneyWins = ").append(objTourneySeed.getTourneyWins()).append(" ");
        sql.append("WHERE TournamentSeedID = ").append(objTourneySeed.getTournamentSeedID());

        // Call QueryCreator
        try {
            retVal = CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }
}