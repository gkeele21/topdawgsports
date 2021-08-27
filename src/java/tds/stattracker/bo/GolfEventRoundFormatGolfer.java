package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class GolfEventRoundFormatGolfer implements Serializable {
    
    // DB FIELDS
    private Integer _GolfEventRoundFormatGolferID;    
    private Integer _GolfEventRoundFormatID;
    private Integer _GolfEventRoundGolferID;    
    private Integer _GameScore;
    private Integer _NetGameScore;
    private Double _Earnings;
    private Integer _FinalPosition;
    
    // OBJECTS
    private GolfEventRoundFormat _GolfEventRoundFormat;
    private GolfEventRoundGolfer _GolfEventRoundGolfer;    

    // CONSTRUCTORS
    public GolfEventRoundFormatGolfer() {
    }
    
    public GolfEventRoundFormatGolfer(int GolfEventRoundFormatGolferId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRoundFormatGolfer", "", ""));
            sql.append("FROM GolfEventRoundFormatGolfer");
            sql.append("WHERE GolfEventRoundFormatGolferID = ").append(GolfEventRoundFormatGolferId);

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

    public GolfEventRoundFormatGolfer(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfEventRoundFormatGolferID() {return _GolfEventRoundFormatGolferID;}
    public Integer getGolfEventRoundFormatID() {return _GolfEventRoundFormatID;}
    public Integer getGolfEventRoundGolferID() {return _GolfEventRoundGolferID;}    
    public Integer getGameScore() {return _GameScore;}
    public Integer getNetGameScore() {return _NetGameScore;}
    public Double getEarnings() {return _Earnings;}
    public Integer getFinalPosition() {return _FinalPosition;}
    public GolfEventRoundFormat getGolfEventRoundFormat() {return _GolfEventRoundFormat;} 
    public GolfEventRoundGolfer getGolfEventRoundGolfer() {return _GolfEventRoundGolfer;}         
    
    // SETTERS
    public void setGolfEventRoundFormatGolferID(Integer GolfEventRoundFormatGolferID) {_GolfEventRoundFormatGolferID = GolfEventRoundFormatGolferID;}
    public void setGolfEventRoundFormatID(Integer GolfEventRoundFormatID) {_GolfEventRoundFormatID = GolfEventRoundFormatID;}
    public void setGolfEventRoundGolferID(Integer GolfEventRoundGolferID) {_GolfEventRoundGolferID = GolfEventRoundGolferID;}    
    public void setGameScore(Integer GameScore) {_GameScore = GameScore;}
    public void setNetGameScore(Integer NetGameScore) {_NetGameScore = NetGameScore;}
    public void setEarnings(Double Earnings) {_Earnings = Earnings;}
    public void setFinalPosition(Integer FinalPosition) {_FinalPosition = FinalPosition;}
    public void setGolfEventRoundFormat(GolfEventRoundFormat GolfEventRoundFormat) {_GolfEventRoundFormat = GolfEventRoundFormat;}
    public void setGolfEventRoundGolfer(GolfEventRoundGolfer GolfEventRoundGolfer) {_GolfEventRoundGolfer = GolfEventRoundGolfer;}
    
    // PUBLIC METHODS
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEventRoundFormatGolfer", "GolfEventRoundFormatGolferID", getGolfEventRoundFormatGolferID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundFormatGolferID")) { setGolfEventRoundFormatGolferID(crs.getInt(prefix + "GolfEventRoundFormatGolferID")); }            
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundFormatID")) { setGolfEventRoundFormatID(crs.getInt(prefix + "GolfEventRoundFormatID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundGolferID")) { setGolfEventRoundGolferID(crs.getInt(prefix + "GolfEventRoundGolferID")); }
            if (FSUtils.fieldExists(crs, prefix, "GameScore")) { setGameScore(crs.getInt(prefix + "GameScore")); }            
            if (FSUtils.fieldExists(crs, prefix, "NetGameScore")) { setNetGameScore(crs.getInt(prefix + "NetGameScore")); }
            if (FSUtils.fieldExists(crs, prefix, "Earnings")) { setEarnings(crs.getDouble(prefix + "Earnings")); }            
            if (FSUtils.fieldExists(crs, prefix, "FinalPosition")) { setFinalPosition(crs.getInt(prefix + "FinalPosition")); }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolfEventRoundFormat$", "GolfEventRoundFormatID")) { setGolfEventRoundFormat(new GolfEventRoundFormat(crs, "GolfEventRoundFormat$")); }            
            if (FSUtils.fieldExists(crs, "GolfEventRoundGolfer$", "GolfEventRoundGolferID")) { setGolfEventRoundGolfer(new GolfEventRoundGolfer(crs, "GolfEventRoundGolfer$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO GolfEventRoundFormatGolfer ");
        sql.append("(GolfEventRoundFormatID, GolfEventRoundGolferID, GameScore, NetGameScore, Earnings, FinalPosition) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventRoundFormatID()));
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventRoundGolferID()));         
        sql.append(FSUtils.InsertDBFieldValue(getGameScore())); 
        sql.append(FSUtils.InsertDBFieldValue(getNetGameScore()));
        sql.append(FSUtils.InsertDBFieldValue(getEarnings())); 
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
    
        sql.append("UPDATE GolfEventRoundFormatGolfer SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolfEventRoundFormatID", getGolfEventRoundFormatID()));
        sql.append(FSUtils.UpdateDBFieldValue("GolfEventRoundGolferID", getGolfEventRoundGolferID()));        
        sql.append(FSUtils.UpdateDBFieldValue("GameScore", getGameScore()));
        sql.append(FSUtils.UpdateDBFieldValue("NetGameScore", getNetGameScore()));
        sql.append(FSUtils.UpdateDBFieldValue("Earnings", getEarnings()));
        sql.append(FSUtils.UpdateDBFieldValue("FinalPosition", getFinalPosition()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventRoundFormatGolferID = ").append(getGolfEventRoundFormatGolferID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    } 
}
