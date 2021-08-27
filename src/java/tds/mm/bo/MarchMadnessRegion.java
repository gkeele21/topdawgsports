package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class MarchMadnessRegion implements Serializable {
    
    public enum Type {UPPER, LOWER, FINAL};
    public enum Location {FIRST, MIDDLE, LAST};

    // DB FIELDS
    private Integer _RegionID;
    private Integer _TournamentID;
    private String _RegionName;
    private Integer _RegionNumber;
    private Integer _StartingRoundID;
    private String _Type;
    private String _Location;
    private Integer _NextRegionID;

    // OBJECTS
    private MarchMadnessTournament _Tournament;
    private MarchMadnessRound _StartingRound;

    // CONSTRUCTORS
    public MarchMadnessRegion() {
    }

    public MarchMadnessRegion(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }

    // GETTERS
    public Integer getRegionID() {return _RegionID;}
    public Integer getTournamentID() {return _TournamentID;}
    public String getRegionName() {return _RegionName;}
    public Integer getRegionNumber() {return _RegionNumber;}
    public Integer getStartingRoundID() {return _StartingRoundID;}
    public String getType() {return _Type;}
    public String getLocation() {return _Location;}
    public Integer getNextRegionID() {return _NextRegionID;}
    public MarchMadnessTournament getTournament() {return _Tournament;}
    public MarchMadnessRound getStartingRound() {return _StartingRound;}
    
    // SETTERS
    public void setRegionID(Integer RegionID) {_RegionID = RegionID;}
    public void setTournamentID(Integer TournamentID) {_TournamentID = TournamentID;}
    public void setRegionName(String RegionName) {_RegionName = RegionName;}
    public void setRegionNumber(Integer RegionNumber) {_RegionNumber = RegionNumber;}
    public void setStartingRoundID(Integer StartingRoundID) {_StartingRoundID = StartingRoundID;}
    public void setType(String RegionType) {_Type = RegionType;}
    public void setLocation(String RegionLocation) {_Location = RegionLocation;}
    public void setNextRegionID(Integer NextRegionID) {_NextRegionID = NextRegionID;}
    public void setTournament(MarchMadnessTournament Tournament) {_Tournament = Tournament;}
    public void setStartingRound(MarchMadnessRound StartingRound) {_StartingRound = StartingRound;}

    // PUBLIC METHODS
    
    /* This retrieves all of the Regions for a given fsTournament */
    public static List<MarchMadnessRegion> GetRegions(int tournamentId) {
        List<MarchMadnessRegion> regions = new ArrayList<MarchMadnessRegion>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessRegion", "", ""));
        sql.append("FROM MarchMadnessRegion ");
        sql.append("WHERE TournamentID = ").append(tournamentId).append(" ");
        sql.append("ORDER BY RegionNumber");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                regions.add(new MarchMadnessRegion(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return regions;
    }

    public void Save() {
            boolean doesExist = FSUtils.DoesARecordExistInDB("MarchMadnessRegion", "RegionID", getRegionID());
            if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "RegionID")) { setRegionID(crs.getInt(prefix + "RegionID")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) { setTournamentID(crs.getInt(prefix + "TournamentID")); }
            if (FSUtils.fieldExists(crs, prefix, "RegionName")) { setRegionName(crs.getString(prefix + "RegionName")); }
            if (FSUtils.fieldExists(crs, prefix, "RegionNumber")) { setRegionNumber(crs.getInt(prefix + "RegionNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "StartingRoundID")) { setStartingRoundID(crs.getInt(prefix + "StartingRoundID")); }
            if (FSUtils.fieldExists(crs, prefix, "Type")) { setType(crs.getString(prefix + "Type")); }
            if (FSUtils.fieldExists(crs, prefix, "Location")) { setLocation(crs.getString(prefix + "Location")); }
            if (FSUtils.fieldExists(crs, prefix, "NextRegionID")) { setNextRegionID(crs.getInt(prefix + "NextRegionID")); }
            
            // OBJECTS 
            if (FSUtils.fieldExists(crs, "MarchMadnessTournament$", "TournamentID")) { setTournament(new MarchMadnessTournament(crs, "MarchMadnessTournament$")); }
            if (FSUtils.fieldExists(crs, "MarchMadnessRegionStartingRound$", "RoundID")) { setStartingRound(new MarchMadnessRound(crs, "MarchMadnessRegionStartingRound$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO MarchMadnessRegion ");
        sql.append("(RegionID, TournamentID, RegionName, RegionNumber, StartingRoundID, Type, Location, NextRegionID) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getRegionID()));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getRegionName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRegionNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getStartingRoundID()));
        sql.append(FSUtils.InsertDBFieldValue(getType(), true));
        sql.append(FSUtils.InsertDBFieldValue(getLocation(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNextRegionID()));        
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE MarchMadnessRegion SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TournamentID", getTournamentID()));
        sql.append(FSUtils.UpdateDBFieldValue("RegionName", getRegionName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("RegionNumber", getRegionNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("StartingRoundID", getStartingRoundID()));
        sql.append(FSUtils.UpdateDBFieldValue("Type", getType(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Location", getLocation(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NextRegionID", getNextRegionID()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE RegionID = ").append(getRegionID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
