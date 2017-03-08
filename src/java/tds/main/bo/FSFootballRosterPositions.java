package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.sql.Connection;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSFootballRosterPositions {
    
    // DB FIELDS
    private int _FSSeasonID;
    private int _PositionID;
    private int _MaxStart;
    private int _MinStart;
    private int _MaxNum;
    private int _MinNum;
    private int _DraftNum;
    private int _MinActive;
    
    // CONSTRUCTORS
    public FSFootballRosterPositions() {
    }
    
    public FSFootballRosterPositions(int fsseasonID, int positionID) {
        this(null, fsseasonID, positionID);
    }

    public FSFootballRosterPositions(Connection con, int fsseasonID, int positionID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSFootballRosterPositions", "rp.", ""));
            sql.append(" FROM FSFootballRosterPositions rp ");
            sql.append(" WHERE rp.FSSeasonID = ").append(fsseasonID);
            sql.append(" AND rp.PositionID = ").append(positionID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }
    
    public FSFootballRosterPositions(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FSFootballRosterPositions(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }
    
    // GETTERS
    public int getFSSeasonID() {return _FSSeasonID;}
    public int getPositionID() {return _PositionID;}
    public int getMaxStart() {return _MaxStart;}
    public int getMinStart() {return _MinStart;}
    public int getMaxNum() {return _MaxNum;}
    public int getMinNum() {return _MinNum;}
    public int getDraftNum() {return _DraftNum;}
    public int getMinActive() {return _MinActive;}
    
    // SETTERS
    public void setFSSeasonID(int FSSeasonID) {_FSSeasonID = FSSeasonID;}
    public void setPositionID(int PositionID) {_PositionID = PositionID;}
    public void setMaxStart(int MaxStart) {_MaxStart = MaxStart;}
    public void setMinStart(int MinStart) {_MinStart = MinStart;}
    public void setMaxNum(int MaxNum) {_MaxNum = MaxNum;}
    public void setMinNum(int MinNum) {_MinNum = MinNum;}
    public void setDraftNum(int DraftNum) {_DraftNum = DraftNum;}
    public void setMinActive(int MinActive) {_MinActive = MinActive;}
    
    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        
        try {
            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonID")) {
                setFSSeasonID(crs.getInt(prefix + "FSSeasonID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "PositionID")) {
                setPositionID(crs.getInt(prefix + "PositionID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "MaxStart")) {
                setMaxStart(crs.getInt(prefix + "MaxStart"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "MinStart")) {
                setMinStart(crs.getInt(prefix + "MinStart"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "MaxNum")) {
                setMaxNum(crs.getInt(prefix + "MaxNum"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "MinNum")) {
                setMinNum(crs.getInt(prefix + "MinNum"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "DraftNum")) {
                setDraftNum(crs.getInt(prefix + "DraftNum"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "MinActive")) {
                setMinActive(crs.getInt(prefix + "MinActive"));
            }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSFootballRosterPositions", "FSSeasonID", getFSSeasonID(), "PositionID", getPositionID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSFootballRosterPositions ");
        sql.append("(FSSeasonID, PositionID, MaxStart, MinStart, MaxNum, MinNum, DraftNum, MinActive)");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonID()));
        sql.append(FSUtils.InsertDBFieldValue(getPositionID()));
        sql.append(FSUtils.InsertDBFieldValue(getMaxStart()));
        sql.append(FSUtils.InsertDBFieldValue(getMinStart()));
        sql.append(FSUtils.InsertDBFieldValue(getMaxNum()));
        sql.append(FSUtils.InsertDBFieldValue(getMinNum()));
        sql.append(FSUtils.InsertDBFieldValue(getDraftNum()));
        sql.append(FSUtils.InsertDBFieldValue(getMinActive()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE FSFootballRosterPositions SET ");
        sql.append(FSUtils.UpdateDBFieldValue("MaxStart", getMaxStart()));
        sql.append(FSUtils.UpdateDBFieldValue("MinStart", getMinStart()));
        sql.append(FSUtils.UpdateDBFieldValue("MaxNum", getMaxNum()));
        sql.append(FSUtils.UpdateDBFieldValue("MinNum", getMinNum()));
        sql.append(FSUtils.UpdateDBFieldValue("DraftNum", getDraftNum()));
        sql.append(FSUtils.UpdateDBFieldValue("MinActive", getMinActive()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSSeasonID = ").append(getFSSeasonID());
        sql.append(" AND PositionID = ").append(getPositionID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
