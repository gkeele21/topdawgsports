package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class GolfEventRoundFormatPayout implements Serializable {

    // CONSTANTS
    public enum PayoutType {Individual, Group, Team};
    
    // DB FIELDS
    private Integer _GolfEventRoundFormatID;
    private Integer _PositionNumber;        
    private String _PayoutType;
    private Double _Amount;

    // OBJECTS
    private GolfEventRoundFormat _GolfEventRoundFormat;

    // CONSTRUCTORS
    public GolfEventRoundFormatPayout() {
    }
    
    public GolfEventRoundFormatPayout(int golfEventRoundFormatID, int positionNumber) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRoundFormatPayout", "", ""));
            sql.append("FROM GolfEventRoundFormatPayout");
            sql.append("WHERE GolfEventRoundFormatID = ").append(golfEventRoundFormatID).append(" AND PositionNumber = ").append(positionNumber);

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

    public GolfEventRoundFormatPayout(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }
    
    // GETTERS
    public Integer getGolfEventRoundFormatID() {return _GolfEventRoundFormatID;}
    public Integer getPositionNumber() {return _PositionNumber;}
    public String getPayoutType() {return _PayoutType;}
    public Double getAmount() {return _Amount;}
    public GolfEventRoundFormat getGolfEventRoundFormat() {return _GolfEventRoundFormat;} 
    
    // SETTERS
    public void setGolfEventRoundFormatID(Integer GolfEventRoundFormatID) {_GolfEventRoundFormatID = GolfEventRoundFormatID;}
    public void setPositionNumber(Integer PositionNumber) {_PositionNumber = PositionNumber;}
    public void setPayoutType(String PayoutType) {_PayoutType = PayoutType;}
    public void setAmount(Double Amount) {_Amount = Amount;}
    public void setGolfEventRoundFormat(GolfEventRoundFormat GolfEventRoundFormat) {_GolfEventRoundFormat = GolfEventRoundFormat;}

    // PUBLIC METHODS
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEventRoundFormatPayout", "GolfEventRoundFormatID", getGolfEventRoundFormatID(), "PositionNumber",getPositionNumber());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {    
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundFormatID")) { setGolfEventRoundFormatID(crs.getInt(prefix + "GolfEventRoundFormatID")); }            
            if (FSUtils.fieldExists(crs, prefix, "PositionNumber")) { setPositionNumber(crs.getInt(prefix + "PositionNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "PayoutType")) { setPayoutType(crs.getString(prefix + "PayoutType")); }
            if (FSUtils.fieldExists(crs, prefix, "Amount")) { setAmount(crs.getDouble(prefix + "Amount")); }
            
            // OBJECTS            
            if (FSUtils.fieldExists(crs, "GolfEventRoundFormat$", "GolfEventRoundFormatID")) { setGolfEventRoundFormat(new GolfEventRoundFormat(crs, "GolfEventRoundFormat$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
     private void Insert() {
        StringBuilder sql = new StringBuilder();
     
        sql.append("INSERT INTO GolfEventRoundFormatPayout ");
        sql.append("(GolfEventRoundFormatID, PositionNumber, PayoutType, Amount) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventRoundFormatID()));
        sql.append(FSUtils.InsertDBFieldValue(getPositionNumber())); 
        sql.append(FSUtils.InsertDBFieldValue(getPayoutType(), true)); 
        sql.append(FSUtils.InsertDBFieldValue(getAmount()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfEventRoundFormatPayout SET ");
        sql.append(FSUtils.UpdateDBFieldValue("PayoutType", getPayoutType(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Amount", getAmount()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventRoundFormatID = ").append(getGolfEventRoundFormatID()).append(" AND PositionNumber = ").append(getPositionNumber());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
