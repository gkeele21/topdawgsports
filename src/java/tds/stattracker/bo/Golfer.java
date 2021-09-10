package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;
import tds.main.bo.FSUser;

import java.io.Serializable;
import java.util.Date;

import static tds.data.CTColumnLists._Cols;

public class Golfer implements Serializable {

    // DB FIELDS
    private Integer _GolferID;
    private Integer _FSUserID;
    private Integer _HomeCourseID;
    private Double _CurrentHandicap;
    private Integer _CurrentRank;
    private Date _DateJoined;
    private String _Notes;

    // OBJECTS
    private FSUser _FSUser;
    private Course _HomeCourse;

    // CONSTRUCTORS
    public Golfer() {
    }

    public Golfer(int golferId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Golfer", "g.", "")).append(",");
            sql.append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
            sql.append("FROM Golfer g ");
            sql.append("JOIN FSUser u ON u.FSUserID = g.FSUserID ");
            sql.append("WHERE GolferID = ").append(golferId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                InitFromCRS(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public Golfer(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolferID() {return _GolferID;}
    public Integer getFSUserID() {return _FSUserID;}
    public Integer getHomeCourseID() {return _HomeCourseID;}
    public Double getCurrentHandicap() {return _CurrentHandicap;}
    public Integer getCurrentRank() {return _CurrentRank;}
    public Date getDateJoined() {return _DateJoined;}
    public String getNotes() {return _Notes;}
    public FSUser getFSUser() {return _FSUser;}
    public Course getHomeCourse() {return _HomeCourse;}

    // SETTERS
    public void setGolferID(Integer golferId) {_GolferID = golferId;}
    public void setFSUserID(Integer fsUserId) {_FSUserID = fsUserId;}
    public void setHomeCourseID(Integer courseId) {_HomeCourseID = courseId;}
    public void setCurrentHandicap(Double currentHandicap) {_CurrentHandicap = currentHandicap;}
    public void setCurrentRank(Integer currentRank) {_CurrentRank = currentRank;}
    public void setDateJoined(Date DateJoined) {_DateJoined = DateJoined;}
    public void setNotes(String Notes) {_Notes = Notes;}
    public void setFSUser(FSUser FSUser) {_FSUser = FSUser;}
    public void setHomeCourse(Course homeCourse) {_HomeCourse = homeCourse;}

    // PUBLIC METHODS

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("Golfer", "GolferID", getGolferID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolferID")) { setGolferID(crs.getInt(prefix + "GolferID")); }
            if (FSUtils.fieldExists(crs, prefix, "FSUserID")) { setFSUserID(crs.getInt(prefix + "FSUserID")); }
            if (FSUtils.fieldExists(crs, prefix, "HomeCourseID")) { setHomeCourseID(crs.getInt(prefix + "HomeCourseID")); }
            if (FSUtils.fieldExists(crs, prefix, "CurrentHandicap")) { setCurrentHandicap(crs.getDouble(prefix + "CurrentHandicap")); }
            if (FSUtils.fieldExists(crs, prefix, "CurrentRank")) { setCurrentRank(crs.getInt(prefix + "CurrentRank")); }
            if (FSUtils.fieldExists(crs, prefix, "DateJoined")) { setDateJoined(crs.getDate(prefix + "DateJoined")); }
            if (FSUtils.fieldExists(crs, prefix, "Notes")) { setNotes(crs.getString(prefix + "Notes")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSUser$", "FSUserID")) { setFSUser(new FSUser(crs, "FSUser$")); }
            if (FSUtils.fieldExists(crs, "HomeCourse$", "CourseID")) { setHomeCourse(new Course(crs, "HomeCourse$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO Golfer ");
        sql.append("(FSUserID, HomeCourseID, CurrentHandicap, CurrentRank, DateJoined, Notes) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSUserID()));
        sql.append(FSUtils.InsertDBFieldValue(getHomeCourseID()));
        sql.append(FSUtils.InsertDBFieldValue(getCurrentHandicap()));
        sql.append(FSUtils.InsertDBFieldValue(getCurrentRank()));
        sql.append(FSUtils.InsertDBFieldValue(getDateJoined(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE Golfer SET ");
        sql.append(FSUtils.UpdateDBFieldValue("FSUserID", getFSUserID()));
        sql.append(FSUtils.UpdateDBFieldValue("CurrentHandicap", getCurrentHandicap()));
        sql.append(FSUtils.UpdateDBFieldValue("CurrentRank", getCurrentRank()));
        sql.append(FSUtils.UpdateDBFieldValue("HomeCourseID", getHomeCourseID()));
        sql.append(FSUtils.UpdateDBFieldValue("DateJoined", getDateJoined(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Notes", getNotes(), true));
        sql.append("WHERE GolferID = ").append(getGolferID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
