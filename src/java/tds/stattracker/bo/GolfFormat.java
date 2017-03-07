package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class GolfFormat implements Serializable {

    // CONSTANTS
    public enum FormatType {Chicago, Net, Gross, Skins, KP, ModifiedChicago, Custom};
    public enum BestScore {Low, High};
    
    // DB FIELDS
    private Integer _GolfFormatID;
    private String _DisplayName;
    private String _FormatType;
    private String _Description;
    private String _BestScore;

    // CONSTRUCTORS
    public GolfFormat() {
    }
    
    public GolfFormat(int golfFormatId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfFormat", "", ""));
            sql.append("FROM GolfFormat");
            sql.append("WHERE GolfFormatID = ").append(golfFormatId);

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

    public GolfFormat(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfFormatID() {return _GolfFormatID;}
    public String getDisplayName() {return _DisplayName;}
    public String getFormatType() {return _FormatType;}
    public String getDescription() {return _Description;}
    public String getBestScore() {return _BestScore;}
    
    // SETTERS
    public void setGolfFormatID(Integer GolfFormatID) {_GolfFormatID = GolfFormatID;}
    public void setDisplayName(String DisplayName) {_DisplayName = DisplayName;}
    public void setFormatType(String FormatType) {_FormatType = FormatType;}
    public void setDescription(String Description) {_Description = Description;}
    public void setBestScore(String BestScore) {_BestScore = BestScore;}

    // PUBLIC METHODS
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfFormat", "GolfFormatID", getGolfFormatID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfFormatID")) { setGolfFormatID(crs.getInt(prefix + "GolfFormatID")); }
            if (FSUtils.fieldExists(crs, prefix, "DisplayName")) { setDisplayName(crs.getString(prefix + "DisplayName")); }
            if (FSUtils.fieldExists(crs, prefix, "FormatType")) { setFormatType(crs.getString(prefix + "FormatType")); }
            if (FSUtils.fieldExists(crs, prefix, "Description")) { setDescription(crs.getString(prefix + "Description")); }
            if (FSUtils.fieldExists(crs, prefix, "BestScore")) { setBestScore(crs.getString(prefix + "BestScore")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfFormat ");
        sql.append("(DisplayName, FormatType, Description, BestScore) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getDisplayName(), true)); 
        sql.append(FSUtils.InsertDBFieldValue(getFormatType(), true)); 
        sql.append(FSUtils.InsertDBFieldValue(getDescription(), true)); 
        sql.append(FSUtils.InsertDBFieldValue(getBestScore(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfFormat SET ");
        sql.append(FSUtils.UpdateDBFieldValue("DisplayName", getDisplayName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("FormatType", getFormatType(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Description", getDescription(), true));
        sql.append(FSUtils.UpdateDBFieldValue("BestScore", getBestScore(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfFormatID = ").append(getGolfFormatID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }   
}