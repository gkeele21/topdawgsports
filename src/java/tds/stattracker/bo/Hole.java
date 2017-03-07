package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class Hole implements Serializable {

    // DB FIELDS
    private Integer _HoleID;
    private Integer _CourseID;
    private Integer _HoleNumber;
    private String _HoleName;

    // OBJECTS
    private Course _Course;
    
    // ADDITIONALS FIELDS
    private HoleTeeInfo _HoleTeeInfo;

    // CONSTRUCTORS
    public Hole() {
    }
    
    public Hole(int holeId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Hole", "", ""));
            sql.append("FROM Hole");
            sql.append("WHERE HoleID = ").append(holeId);

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

    public Hole(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getHoleID() {return _HoleID;}
    public Integer getCourseID() {return _CourseID;}
    public Integer getHoleNumber() {return _HoleNumber;}
    public String getHoleName() {return _HoleName;}
    public Course getCourse() {return _Course;} 
    public HoleTeeInfo getHoleTeeInfo() {return _HoleTeeInfo;}
    
    // SETTERS
    public void setHoleID(Integer holeId) {_HoleID = holeId;}
    public void setCourseID(Integer courseId) {_CourseID = courseId;}
    public void setHoleNumber(Integer holeNum) {_HoleNumber = holeNum;}
    public void setHoleName(String holeName) {_HoleName = holeName;}
    public void setCourse(Course course) {_Course = course;}
    public void setHoleTeeInfo(HoleTeeInfo holeTeeInfo) {_HoleTeeInfo = holeTeeInfo;}

    // PUBLIC METHODS
    
    public static List<HoleTeeInfo> GetHoleInfo(Integer courseId, Integer teeId) {
        List<HoleTeeInfo> holes = new ArrayList<HoleTeeInfo>();
        CachedRowSet crs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Hole", "h.", "Hole$")).append(", ");
            sql.append(_Cols.getColumnList("Course", "c.", "Course$")).append(", ");
            sql.append(_Cols.getColumnList("GolfCourse", "gc.", "GolfCourse$")).append(", ");
            sql.append(_Cols.getColumnList("Tee", "t.", "Tee$")).append(", ");
            sql.append(_Cols.getColumnList("HoleTeeInfo", "ht.", "HoleTeeInfo$")).append(", ");
            sql.append(_Cols.getColumnList("CourseTeeInfo", "ct.", "CourseTeeInfo$"));
            sql.append("FROM Hole h ");
            sql.append("JOIN Course c ON c.CourseID = h.CourseID ");
            sql.append("JOIN GolfCourse gc ON gc.GolfCourseID = c.GolfCourseID ");
            sql.append("JOIN Tee t ON t.GolfCourseID = gc.GolfCourseID ");
            sql.append("JOIN HoleTeeInfo ht ON ht.HoleID = h.HoleID AND ht.TeeID = t.TeeID ");
            sql.append("JOIN CourseTeeInfo ct ON ct.CourseID = c.CourseID AND ct.TeeID = t.TeeID ");
            sql.append("WHERE c.CourseID = ").append(courseId).append(" AND t.TeeID = ").append(teeId).append(" ");    
            sql.append("ORDER BY h.HoleNumber");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                HoleTeeInfo objHoleTeeInfo = new HoleTeeInfo(crs,"HoleTeeInfo$");
                holes.add(objHoleTeeInfo);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return holes;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("Hole", "HoleID", getHoleID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "HoleID")) { setHoleID(crs.getInt(prefix + "HoleID")); }
            if (FSUtils.fieldExists(crs, prefix, "CourseID")) { setCourseID(crs.getInt(prefix + "CourseID")); }
            if (FSUtils.fieldExists(crs, prefix, "HoleNumber")) { setHoleNumber(crs.getInt(prefix + "HoleNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "HoleName")) { setHoleName(crs.getString(prefix + "HoleName")); }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "Course$", "CourseID")) { setCourse(new Course(crs, "Course$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();
      
        sql.append("INSERT INTO Hole ");
        sql.append("(CourseID, HoleNumber, HoleName) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getCourseID()));
        sql.append(FSUtils.InsertDBFieldValue(getHoleNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getHoleName(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE Hole SET ");
        sql.append(FSUtils.UpdateDBFieldValue("CourseID", getCourseID()));
        sql.append(FSUtils.UpdateDBFieldValue("HoleNumber", getHoleNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("HoleName", getHoleName(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE HoleID = ").append(getHoleID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}