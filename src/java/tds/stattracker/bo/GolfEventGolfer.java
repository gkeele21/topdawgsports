package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class GolfEventGolfer implements Serializable { 

    // DB FIELDS
    private Integer _GolfEventID;
    private Integer _GolferID;
    private Integer _Rank;    
    private Double _Handicap;
    private Double _TotalEarnings;
    private Integer _FinalPosition;    

    // OBJECTS
    private GolfEvent _GolfEvent;
    private Golfer _Golfer;

    // CONSTRUCTORS
    public GolfEventGolfer() {
    }
    
    public GolfEventGolfer(int golfEventId, int golferId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventGolfer", "", ""));
            sql.append("FROM GolfEventGolfer");
            sql.append("WHERE GolfEventID = ").append(golfEventId).append(" AND GolferID = ").append(golferId);

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

    public GolfEventGolfer(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfEventID() {return _GolfEventID;}
    public Integer getGolferID() {return _GolferID;}
    public Integer getRank() {return _Rank;}
    public Double getHandicap() {return _Handicap;}
    public Double getTotalEarnings() {return _TotalEarnings;}
    public Integer getFinalPosition() {return _FinalPosition;}
    public GolfEvent getGolfEvent() {return _GolfEvent;} 
    public Golfer getGolfer() {return _Golfer;} 
    
    // SETTERS
    public void setGolfEventID(Integer GolfEventID) {_GolfEventID = GolfEventID;}
    public void setGolferID(Integer GolferID) {_GolferID = GolferID;}
    public void setRank(Integer Rank) {_Rank = Rank;}
    public void setHandicap(Double Handicap) {_Handicap = Handicap;}
    public void setTotalEarnings(Double TotalEarnings) {_TotalEarnings = TotalEarnings;}
    public void setFinalPosition(Integer FinalPosition) {_FinalPosition = FinalPosition;}    
    public void setGolfEvent(GolfEvent GolfEvent) {_GolfEvent = GolfEvent;}
    public void setGolfer(Golfer Golfer) {_Golfer = Golfer;}

    // PUBLIC METHODS
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEventGolfer", "GolfEventID", getGolfEventID(), "GolferID", getGolferID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS
   
    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS            
            if (FSUtils.fieldExists(crs, prefix, "GolfEventID")) { setGolfEventID(crs.getInt(prefix + "GolfEventID")); }            
            if (FSUtils.fieldExists(crs, prefix, "GolferID")) { setGolferID(crs.getInt(prefix + "GolferID")); }            
            if (FSUtils.fieldExists(crs, prefix, "Rank")) { setRank(crs.getInt(prefix + "Rank")); }            
            if (FSUtils.fieldExists(crs, prefix, "Handicap")) { setHandicap(crs.getDouble(prefix + "Handicap")); }            
            if (FSUtils.fieldExists(crs, prefix, "TotalEarnings")) { setTotalEarnings(crs.getDouble(prefix + "TotalEarnings")); }            
            if (FSUtils.fieldExists(crs, prefix, "FinalPosition")) { setFinalPosition(crs.getInt(prefix + "FinalPosition")); }  
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolfEvent$", "GolfEventID")) { setGolfEvent(new GolfEvent(crs, "GolfEvent$")); }
            if (FSUtils.fieldExists(crs, "Golfer$", "GolferID")) { setGolfer(new Golfer(crs, "Golfer$")); }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfEventGolfer ");
        sql.append("(GolfEventID, GolferID, Rank, Handicap, GamePoints, TotalEarnings, FinalPosition) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventID()));
        sql.append(FSUtils.InsertDBFieldValue(getGolferID()));
        sql.append(FSUtils.InsertDBFieldValue(getRank()));
        sql.append(FSUtils.InsertDBFieldValue(getHandicap()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalEarnings()));
        sql.append(FSUtils.InsertDBFieldValue(getFinalPosition()));
        sql.deleteCharAt(sql.length()-1).append(")");
        
        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfEventGolfer SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolfEventID", getGolfEventID()));
        sql.append(FSUtils.UpdateDBFieldValue("GolferID", getGolferID()));
        sql.append(FSUtils.UpdateDBFieldValue("Rank", getRank()));
        sql.append(FSUtils.UpdateDBFieldValue("Handicap", getHandicap()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalEarnings", getTotalEarnings()));
        sql.append(FSUtils.UpdateDBFieldValue("FinalPosition", getFinalPosition()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventID = ").append(getGolfEventID()).append(" AND GolferID = ").append(getGolferID());

        // Execute Query
        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}