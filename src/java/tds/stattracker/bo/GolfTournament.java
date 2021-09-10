package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

import java.io.Serializable;

import static tds.data.CTColumnLists._Cols;

public class GolfTournament implements Serializable {

    // DB FIELDS
    private Integer _GolfTournamentID;
    private Integer _DefaultGolfCourseID;
    private String _TournamentName;

    // OBJECTS
    private GolfCourse _DefaultGolfCourse;

    // CONSTRUCTORS
    public GolfTournament() {
    }

    public GolfTournament(int golfTournamentId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfTournament", "", ""));
            sql.append("FROM GolfTournament");
            sql.append("WHERE GolfTournamentID = ").append(golfTournamentId);

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

    public GolfTournament(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfTournamentID() {return _GolfTournamentID;}
    public Integer getDefaultGolfCourseID() {return _DefaultGolfCourseID;}
    public String getTournamentName() {return _TournamentName;}
    public GolfCourse getDefaultGolfCourse() {return _DefaultGolfCourse;}

    // SETTERS
    public void setGolfTournamentID(Integer GolfTournamentID) {_GolfTournamentID = GolfTournamentID;}
    public void setDefaultGolfCourseID(Integer DefaultGolfCourseID) {_DefaultGolfCourseID = DefaultGolfCourseID;}
    public void setTournamentName(String TournamentName) {_TournamentName = TournamentName;}
    public void setDefaultGolfCourse(GolfCourse DefaultGolfCourse) {_DefaultGolfCourse = DefaultGolfCourse;}

    // PUBLIC METHODS

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfTournament", "GolfTournamentID", getGolfTournamentID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /*  This method populates the object from a cached row set.  */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfTournamentID")) { setGolfTournamentID(crs.getInt(prefix + "GolfTournamentID")); }
            if (FSUtils.fieldExists(crs, prefix, "DefaultGolfCourseID")) { setDefaultGolfCourseID(crs.getInt(prefix + "DefaultGolfCourseID")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentName")) { setTournamentName(crs.getString(prefix + "TournamentName")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "DefaultGolfCourse$", "DefaultGolfCourseID")) { setDefaultGolfCourse(new GolfCourse(crs, "DefaultGolfCourse$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfTournament ");
        sql.append("(DefaultGolfCourseID, TournamentName) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getDefaultGolfCourseID()));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentName(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfTournament SET ");
        sql.append(FSUtils.UpdateDBFieldValue("DefaultGolfCourseID", getDefaultGolfCourseID()));
        sql.append(FSUtils.UpdateDBFieldValue("TournamentName", getTournamentName(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfTournamentID = ").append(getGolfTournamentID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
