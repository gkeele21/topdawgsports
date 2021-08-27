package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSTournamentRegion implements Serializable {

    // DB FIELDS
    private int _RegionID;
    private int _TournamentID;
    private String _RegionName;
    private int _RegionNumber;
    private int _StartingRoundID;
    private int _RegionType;
    private int _RegionLocation;
    private int _RegionSide;
    private int _NextRegionID;

    // OBJECTS
    private FSTournament _FSTournament;
    private FSTournamentRound _StartingFSTournamentRound;

    // CONSTRUCTORS
    public FSTournamentRegion() {
    }

    public FSTournamentRegion(CachedRowSet crs, String prefix) {
        initFromCRS(crs, prefix);
    }

    // GETTERS
    public int getRegionID() {return _RegionID;}
    public int getTournamentID() {return _TournamentID;}
    public String getRegionName() {return _RegionName;}
    public int getRegionNumber() {return _RegionNumber;}
    public int getStartingRoundID() {return _StartingRoundID;}
    public int getRegionType() {return _RegionType;}
    public int getRegionLocation() {return _RegionLocation;}
    public int getRegionSide() {return _RegionSide;}
    public int getNextRegionID() {return _NextRegionID;}
    public FSTournament getFSTournament() {return _FSTournament;}
    public FSTournamentRound getStartingFSTournamentRound() {return _StartingFSTournamentRound;}
    
    // SETTERS
    public void setRegionID(int RegionID) {_RegionID = RegionID;}
    public void setTournamentID(int TournamentID) {_TournamentID = TournamentID;}
    public void setRegionName(String RegionName) {_RegionName = RegionName;}
    public void setRegionNumber(int RegionNumber) {_RegionNumber = RegionNumber;}
    public void setStartingRoundID(int StartingRoundID) {_StartingRoundID = StartingRoundID;}
    public void setRegionType(int RegionType) {_RegionType = RegionType;}
    public void setRegionLocation(int RegionLocation) {_RegionLocation = RegionLocation;}
    public void setRegionSide(int RegionSide) {_RegionSide = RegionSide;}
    public void setNextRegionID(int NextRegionID) {_NextRegionID = NextRegionID;}
    public void setFSTournament(FSTournament FSTournament) {_FSTournament = FSTournament;}
    public void setStartingFSTournamentRound(FSTournamentRound StartingFSTournamentRound) {_StartingFSTournamentRound = StartingFSTournamentRound;}

    // PUBLIC METHODS
    
    /* This retrieves all of the Regions for a given fsTournament */
    public static List<FSTournamentRegion> GetRegions(int tournamentId) {

        List<FSTournamentRegion> regions = new ArrayList<FSTournamentRegion>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSTournamentRegion", "", ""));
        sql.append("FROM FSTournamentRegion ");
        sql.append("WHERE TournamentID = ").append(tournamentId).append(" ");
        sql.append("ORDER BY RegionID");

        // Execute Query
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                regions.add(new FSTournamentRegion(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return regions;
   }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "RegionID")) {
                setRegionID(crs.getInt(prefix + "RegionID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) {
                setTournamentID(crs.getInt(prefix + "TournamentID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RegionName")) {
                setRegionName(crs.getString(prefix + "RegionName"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RegionNumber")) {
                setRegionNumber(crs.getInt(prefix + "RegionNumber"));
            }

            if (FSUtils.fieldExists(crs, prefix, "StartingRoundID")) {
                setStartingRoundID(crs.getInt(prefix + "StartingRoundID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RegionType")) {
                setRegionType(crs.getInt(prefix + "RegionType"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RegionLocation")) {
                setRegionLocation(crs.getInt(prefix + "RegionLocation"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RegionSide")) {
                setRegionSide(crs.getInt(prefix + "RegionSide"));
            }

            if (FSUtils.fieldExists(crs, prefix, "NextRegionID")) {
                setNextRegionID(crs.getInt(prefix + "NextRegionID"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTournament$", "TournamentID")) {
                setFSTournament(new FSTournament(crs, "FSTournament$"));
            }

            if (FSUtils.fieldExists(crs, "StartingRound$", "RoundID")) {
                setStartingFSTournamentRound(new FSTournamentRound(crs, "StartingRound$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    
}

/* This retrieves the current Region for a given fsTeam in order to determine which Region to initially display on the UI */
    /*
    public static int getCurrentRegionID(int fsTeamId) {

        CachedRowSet crs = null;
        int currentRegion = 0;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT max(b.RegionID) as CurrentRegion ");
            sql.append("FROM FSTournamentGame g ");
            sql.append("INNER JOIN FSTournamentRegion b ON g.RegionID = b.RegionID ");
            sql.append("WHERE g.FSTeam1ID = ").append(fsTeamId).append(" OR g.FSTeam2ID = ").append(fsTeamId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                currentRegion = crs.getInt("CurrentRegion");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return currentRegion;
   }
*/
