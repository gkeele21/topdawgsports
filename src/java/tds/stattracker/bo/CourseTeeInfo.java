package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class CourseTeeInfo implements Serializable {

    // DB FIELDS
    private Integer _CourseID;
    private Integer _TeeID;
    private Double _MensRating;
    private Double _MensSlope;
    private Double _WomensRating;
    private Double _WomensSlope;

    // OBJECTS
    private Course _Course;
    private Tee _Tee;

    // CONSTRUCTORS
    public CourseTeeInfo() {
    }

    public CourseTeeInfo(int courseId, int teeId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("CourseTeeInfo", "", ""));
            sql.append("FROM CourseTeeInfo");
            sql.append("WHERE CourseID = ").append(courseId).append(" AND TeeID = ").append(teeId);

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

    public CourseTeeInfo(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getCourseID() {return _CourseID;}
    public Integer getTeeID() {return _TeeID;}
    public Double getMensRating() {return _MensRating;}
    public Double getMensSlope() {return _MensSlope;}
    public Double getWomensRating() {return _WomensRating;}
    public Double getWomensSlope() {return _WomensSlope;}
    public Course getCourse() {return _Course;}
    public Tee getTee() {return _Tee;}

    // SETTERS
    public void setCourseID(Integer CourseID) {_CourseID = CourseID;}
    public void setTeeID(Integer teeId) {_TeeID = teeId;}
    public void setMensRating(double mensRating) {_MensRating = mensRating;}
    public void setMensSlope(double mensSlope) {_MensSlope = mensSlope;}
    public void setWomensRating(double womensRating) {_WomensRating = womensRating;}
    public void setWomensSlope(double womensSlope) {_WomensSlope = womensSlope;}
    public void setCourse(Course Course) {_Course = Course;}
    public void setTee(Tee Tee) {this._Tee = Tee;}

    // PUBLIC METHODS

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("CourseTeeInfo", "CourseID", getCourseID(), "TeeID", getTeeID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "CourseID")) { setCourseID(crs.getInt(prefix + "CourseID")); }
            if (FSUtils.fieldExists(crs, prefix, "TeeID")) { setTeeID(crs.getInt(prefix + "TeeID")); }
            if (FSUtils.fieldExists(crs, prefix, "MensRating")) { setMensRating(crs.getDouble(prefix + "MensRating")); }
            if (FSUtils.fieldExists(crs, prefix, "MensSlope")) { setMensSlope(crs.getDouble(prefix + "MensSlope")); }
            if (FSUtils.fieldExists(crs, prefix, "WomensRating")) { setWomensRating(crs.getDouble(prefix + "WomensRating")); }
            if (FSUtils.fieldExists(crs, prefix, "WomensSlope")) { setWomensSlope(crs.getDouble(prefix + "WomensSlope")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "Course$", "CourseID")) { setCourse(new Course(crs, "Course$")); }
            if (FSUtils.fieldExists(crs, "Tee$", "TeeID")) { setTee(new Tee(crs, "Tee$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO CourseTeeInfo ");
        sql.append("(CourseID, TeeID, MensRating, MensSlope, WomensRating, WomensSlope) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getCourseID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeeID()));
        sql.append(FSUtils.InsertDBFieldValue(getMensRating()));
        sql.append(FSUtils.InsertDBFieldValue(getMensSlope()));
        sql.append(FSUtils.InsertDBFieldValue(getWomensRating()));
        sql.append(FSUtils.InsertDBFieldValue(getWomensSlope()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    /*  This method updates a record in the DB. */
    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE CourseTeeInfo SET ");
        sql.append(FSUtils.UpdateDBFieldValue("CourseID", getCourseID()));
        sql.append(FSUtils.UpdateDBFieldValue("TeeID", getTeeID()));
        sql.append(FSUtils.UpdateDBFieldValue("MensRating", getMensRating()));
        sql.append(FSUtils.UpdateDBFieldValue("MensSlope", getMensSlope()));
        sql.append(FSUtils.UpdateDBFieldValue("WomensRating", getWomensRating()));
        sql.append(FSUtils.UpdateDBFieldValue("WomensSlope", getWomensSlope()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE CourseID = ").append(getCourseID()).append(" AND TeeID = ").append(getTeeID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
