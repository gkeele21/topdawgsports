package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class Course implements Serializable {

    // DB FIELDS
    private Integer _CourseID;
    private Integer _GolfCourseID;
    private Integer _CourseNumber;
    private String _CourseName;
    private Integer _NumHoles;
    private Integer _MensPar;
    private Integer _WomensPar;

    // OBJECTS
    private GolfCourse _GolfCourse;
    
    // CONSTRUCTORS
    public Course() {
    }
    
    public Course(int courseId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Course", "", ""));
            sql.append("FROM Course");
            sql.append("WHERE CourseID = ").append(courseId);

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

    public Course(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getCourseID() {return _CourseID;}
    public Integer getGolfCourseID() {return _GolfCourseID;}
    public Integer getCourseNumber() {return _CourseNumber;}
    public String getCourseName() {return _CourseName;}
    public Integer getNumHoles() {return _NumHoles;}
    public Integer getMensPar() {return _MensPar;}
    public Integer getWomensPar() {return _WomensPar;}
    public GolfCourse getGolfCourse() {return _GolfCourse;} 
    
    // SETTERS
    public void setCourseID(Integer courseId) {_CourseID = courseId;}
    public void setGolfCourseID(Integer GolfCourseID) {_GolfCourseID = GolfCourseID;}
    public void setCourseNumber(Integer courseNumber) {_CourseNumber = courseNumber;}
    public void setCourseName(String courseName) {_CourseName = courseName;}
    public void setNumHoles(Integer numHoles) {_NumHoles = numHoles;}
    public void setMensPar(Integer mensPar) {_MensPar = mensPar;}
    public void setWomensPar(Integer womensPar) {_WomensPar = womensPar;}
    public void setGolfCourse(GolfCourse golfCourse) {_GolfCourse = golfCourse;}

    // PUBLIC METHODS
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("Course", "CourseID", getCourseID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS
    
    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "CourseID")) { setCourseID(crs.getInt(prefix + "CourseID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfCourseID")) { setGolfCourseID(crs.getInt(prefix + "GolfCourseID")); }
            if (FSUtils.fieldExists(crs, prefix, "CourseNumber")) { setCourseNumber(crs.getInt(prefix + "CourseNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "CourseName")) { setCourseName(crs.getString(prefix + "CourseName")); }
            if (FSUtils.fieldExists(crs, prefix, "NumHoles")) { setNumHoles(crs.getInt(prefix + "NumHoles")); }
            if (FSUtils.fieldExists(crs, prefix, "MensPar")) { setMensPar(crs.getInt(prefix + "MensPar")); }
            if (FSUtils.fieldExists(crs, prefix, "WomensPar")) { setWomensPar(crs.getInt(prefix + "WomensPar")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolfCourse$", "GolfCourseID")) { setGolfCourse(new GolfCourse(crs, "GolfCourse$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO Course ");
        sql.append("(GolfCourseID, CourseNumber, CourseName, NumHoles, MensPar, WomensPar) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfCourseID()));
        sql.append(FSUtils.InsertDBFieldValue(getCourseNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getCourseName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNumHoles()));
        sql.append(FSUtils.InsertDBFieldValue(getMensPar()));
        sql.append(FSUtils.InsertDBFieldValue(getWomensPar()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE Course SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolfCourseID", getGolfCourseID()));
        sql.append(FSUtils.UpdateDBFieldValue("CourseNumber", getCourseNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("CourseName", getCourseName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NumHoles", getNumHoles()));
        sql.append(FSUtils.UpdateDBFieldValue("MensPar", getMensPar()));
        sql.append(FSUtils.UpdateDBFieldValue("WomensPar", getWomensPar()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE CourseID = ").append(getCourseID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}