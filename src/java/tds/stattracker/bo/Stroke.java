package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class Stroke implements Serializable {
    
    // CONSTANTS
    public enum StrokeType {Normal, DropWaterHazard, DropOB, ReTee, Other};
    public enum Lie {Teebox, Fairway, Rough, Sand, Fringe, Green, FirstCut, SecondCut, Trees, Dirt};

    // DB FIELDS
    private Integer _StrokeID;
    private Integer _GolferRoundID;    
    private Integer _HoleID;
    private Integer _StrokeNumber;
    private String _StrokeType;
    private String _Club;
    private Integer _DistanceToPin;
    private String _Lie;
    private String _ShotDirection;
    private String _Notes;

    // OBJECTS
    private GolferRound _GolferRound;
    private Hole _Hole;

    // CONSTRUCTORS
    public Stroke() {
    }
    
    public Stroke(int strokeId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Stroke", "", ""));
            sql.append("FROM Stroke");
            sql.append("WHERE StrokeID = ").append(strokeId);

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

    public Stroke(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getStrokeID() {return _StrokeID;}
    public Integer getGolferRoundID() {return _GolferRoundID;}
    public Integer getHoleID() {return _HoleID;}
    public Integer getStrokeNumber() {return _StrokeNumber;}
    public String getStrokeType() {return _StrokeType;}
    public String getClub() {return _Club;}
    public Integer getDistanceToPin() {return _DistanceToPin;}
    public String getLie() {return _Lie;}
    public String getShotDirection() {return _ShotDirection;}
    public String getNotes() {return _Notes;}
    public GolferRound getGolferRound() {return _GolferRound;} 
    public Hole getHole() {return _Hole;} 
    
    // SETTERS
    public void setStrokeID(Integer StrokeID) {_StrokeID = StrokeID;}
    public void setGolferRoundID(Integer GolferRoundID) {_GolferRoundID = GolferRoundID;}
    public void setHoleID(Integer HoleID) {_HoleID = HoleID;}
    public void setStrokeNumber(Integer StrokeNumber) {_StrokeNumber = StrokeNumber;}
    public void setStrokeType(String StrokeType) {_StrokeType = StrokeType;}
    public void setClub(String Club) {_Club = Club;}
    public void setDistanceToPin(Integer DistanceToPin) {_DistanceToPin = DistanceToPin;}
    public void setLie(String Lie) {_Lie = Lie;}
    public void setShotDirection(String ShotDirection) {_ShotDirection = ShotDirection;}
    public void setNotes(String Notes) {_Notes = Notes;}
    public void setGolferRound(GolferRound GolferRound) {_GolferRound = GolferRound;}
    public void setHole(Hole Hole) {_Hole = Hole;}

    // PUBLIC METHODS
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("Stroke", "StrokeID", getStrokeID());
        if (doesExist) { Update(); } else { Insert(); }
    }
    
    // PRIVATE METHODS
   
    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS            
            if (FSUtils.fieldExists(crs, prefix, "StrokeID")) { setStrokeID(crs.getInt(prefix + "StrokeID")); }            
            if (FSUtils.fieldExists(crs, prefix, "GolferRoundID")) { setGolferRoundID(crs.getInt(prefix + "GolferRoundID")); }
            if (FSUtils.fieldExists(crs, prefix, "HoleID")) { setHoleID(crs.getInt(prefix + "HoleID")); }
            if (FSUtils.fieldExists(crs, prefix, "StrokeNumber")) { setStrokeNumber(crs.getInt(prefix + "StrokeNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "StrokeType")) { setStrokeType(crs.getString(prefix + "StrokeType")); }
            if (FSUtils.fieldExists(crs, prefix, "Club")) { setClub(crs.getString(prefix + "Club")); }
            if (FSUtils.fieldExists(crs, prefix, "DistanceToPin")) { setDistanceToPin(crs.getInt(prefix + "DistanceToPin")); }            
            if (FSUtils.fieldExists(crs, prefix, "Lie")) { setLie(crs.getString(prefix + "Lie")); }            
            if (FSUtils.fieldExists(crs, prefix, "ShotDirection")) { setShotDirection(crs.getString(prefix + "ShotDirection")); }
            if (FSUtils.fieldExists(crs, prefix, "Notes")) { setNotes(crs.getString(prefix + "Notes")); }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolferRound$", "GolferRoundID")) { setGolferRound(new GolferRound(crs, "GolferRound$")); }
            if (FSUtils.fieldExists(crs, "Hole$", "HoleID")) { setHole(new Hole(crs, "Hole$")); }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO Stroke ");
        sql.append("(GolferRoundID, HoleID, StrokeNumber, StrokeType, Club, DistanceToPin, Lie, ShotDirection, Notes) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolferRoundID()));
        sql.append(FSUtils.InsertDBFieldValue(getHoleID()));
        sql.append(FSUtils.InsertDBFieldValue(getStrokeNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getStrokeType(), true));
        sql.append(FSUtils.InsertDBFieldValue(getClub(), true));        
        sql.append(FSUtils.InsertDBFieldValue(getDistanceToPin()));
        sql.append(FSUtils.InsertDBFieldValue(getLie(), true));
        sql.append(FSUtils.InsertDBFieldValue(getShotDirection(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE Stroke SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolferRoundID", getGolferRoundID()));
        sql.append(FSUtils.UpdateDBFieldValue("HoleID", getHoleID()));
        sql.append(FSUtils.UpdateDBFieldValue("StrokeNumber", getStrokeNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("StrokeType", getStrokeType(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Club", getClub(), true));
        sql.append(FSUtils.UpdateDBFieldValue("DistanceToPin", getDistanceToPin()));
        sql.append(FSUtils.UpdateDBFieldValue("Lie", getLie(), true));
        sql.append(FSUtils.UpdateDBFieldValue("ShotDirection", getShotDirection(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Notes", getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE StrokeID = ").append(getStrokeID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}