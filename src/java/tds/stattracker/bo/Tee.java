package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class Tee implements Serializable {

    // CONSTANTS
    public enum TeeType {Womens, Junior, Mens, Pro, Tournament};
    
    // DB FIELDS
    private Integer _TeeID;
    private Integer _GolfCourseID;
    private Integer _TeeOrder;
    private String _Color;
    private String _TeeType;
    private String _TeeName;

    // OBJECTS
    private GolfCourse _GolfCourse;

    // CONSTRUCTORS
    public Tee() {
    }
    
    public Tee(int teeId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Tee", "", ""));
            sql.append("FROM Tee");
            sql.append("WHERE TeeID = ").append(teeId);

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

    public Tee(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getTeeID() {return _TeeID;}
    public Integer getGolfCourseID() {return _GolfCourseID;}
    public Integer getTeeOrder() {return _TeeOrder;}
    public String getColor() {return _Color;}
    public String getTeeType() {return _TeeType;}
    public String getTeeName() {return _TeeName;}
    public GolfCourse getGolfCourse() {return _GolfCourse;} 

    // SETTERS
    public void setTeeID(Integer teeId) {_TeeID = teeId;}
    public void setGolfCourseID(Integer GolfCourseID) {_GolfCourseID = GolfCourseID;}
    public void setTeeOrder(Integer teeOrder) {_TeeOrder = teeOrder;}
    public void setColor(String color) {_Color = color;}
    public void setTeeType(String teeType) {_TeeType = teeType;}
    public void setTeeName(String teeName) {_TeeName = teeName;}
    public void setGolfCourse(GolfCourse GolfCourse) {_GolfCourse = GolfCourse;}

    // PUBLIC METHODS
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("Tee", "TeeID", getTeeID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS
   
    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "TeeID")) { setTeeID(crs.getInt(prefix + "TeeID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfCourseID")) { setGolfCourseID(crs.getInt(prefix + "GolfCourseID")); }
            if (FSUtils.fieldExists(crs, prefix, "TeeOrder")) { setTeeOrder(crs.getInt(prefix + "TeeOrder")); }
            if (FSUtils.fieldExists(crs, prefix, "Color")) { setColor(crs.getString(prefix + "Color")); }
            if (FSUtils.fieldExists(crs, prefix, "TeeType")) { setTeeType(crs.getString(prefix + "TeeType")); }            
            if (FSUtils.fieldExists(crs, prefix, "TeeName")) { setTeeName(crs.getString(prefix + "TeeName")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolfCourse$", "GolfCourseID")) { setGolfCourse(new GolfCourse(crs, "GolfCourse$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO Tee ");
        sql.append("(GolfCourseID, TeeOrder, Color, TeeType, TeeName) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfCourseID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeeOrder()));
        sql.append(FSUtils.InsertDBFieldValue(getColor(), true));
        sql.append(FSUtils.InsertDBFieldValue(getTeeType(), true));
        sql.append(FSUtils.InsertDBFieldValue(getTeeName(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE Tee SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolfCourseID", getGolfCourseID()));
        sql.append(FSUtils.UpdateDBFieldValue("TeeOrder", getTeeOrder()));
        sql.append(FSUtils.UpdateDBFieldValue("Color", getColor()));
        sql.append(FSUtils.UpdateDBFieldValue("TeeType", getTeeType()));
        sql.append(FSUtils.UpdateDBFieldValue("TeeName", getTeeName()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE TeeID = ").append(getTeeID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}