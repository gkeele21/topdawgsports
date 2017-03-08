package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class GolfEventRound implements Serializable {
        
    public enum DisplayOption {FIELD, GROUPS, TEAMS};
    
    // DB FIELDS
    private Integer _GolfEventRoundID;
    private Integer _GolfEventID;
    private Integer _CourseID;
    private Integer _TeeID; 
    private Date _RoundDate;
    private Integer _RoundNumber;
    private String _RoundName;
    private Integer _NumHoles; 
    private String _Notes;

    // OBJECTS
    private GolfEvent _GolfEvent;
    private Course _Course;
    private Tee _Tee;
    
    // CONSTRUCTORS
    public GolfEventRound() {
    }
    
    public GolfEventRound(int golfEventRoundId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRound", "", ""));
            sql.append("FROM GolfEventRound ");
            sql.append("WHERE GolfEventRoundID = ").append(golfEventRoundId);

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
    
    public GolfEventRound(int golfEventId, int roundNumber) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRound", "", ""));
            sql.append("FROM GolfEventRound ");
            sql.append("WHERE GolfEventID = ").append(golfEventId).append(" AND RoundNumber = ").append(roundNumber);

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

    public GolfEventRound(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfEventRoundID() {return _GolfEventRoundID;}
    public Integer getGolfEventID() {return _GolfEventID;}
    public Integer getCourseID() {return _CourseID;}
    public Integer getTeeID() {return _TeeID;}
    public Date getRoundDate() {return _RoundDate;}
    public Integer getRoundNumber() {return _RoundNumber;}
    public String getRoundName() {return _RoundName;}
    public Integer getNumHoles() {return _NumHoles;}
    public String getNotes() {return _Notes;}
    public GolfEvent getGolfEvent() {return _GolfEvent;} 
    public Course getCourse() {return _Course;} 
    public Tee getTee() {return _Tee;} 
    
    // SETTERS
    public void setGolfEventRoundID(Integer GolfEventRoundID) {_GolfEventRoundID = GolfEventRoundID;}
    public void setGolfEventID(Integer GolfEventID) {_GolfEventID = GolfEventID;}
    public void setCourseID(Integer CourseID) {_CourseID = CourseID;}
    public void setTeeID(Integer TeeID) {_TeeID = TeeID;}
    public void setRoundDate(Date RoundDate) {_RoundDate = RoundDate;}
    public void setRoundNumber(Integer RoundNumber) {_RoundNumber = RoundNumber;}
    public void setRoundName(String RoundName) {_RoundName = RoundName;}
    public void setNumHoles(Integer NumHoles) {_NumHoles = NumHoles;}
    public void setNotes(String Notes) {_Notes = Notes;}
    public void setGolfEvent(GolfEvent GolfEvent) {_GolfEvent = GolfEvent;}
    public void setCourse(Course Course) {_Course = Course;}
    public void setTee(Tee Tee) {_Tee = Tee;}    
        
    // PUBLIC METHODS
      
    public static List<GolfEventRound> GetGolfEventRounds(int golfEventId) {
        List<GolfEventRound> rounds = new ArrayList<GolfEventRound>();
        CachedRowSet crs = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRound", "er.", ""));
            sql.append(_Cols.getColumnList("GolfEvent", "e.", "GolfEvent$"));
            sql.append(_Cols.getColumnList("GolfTournament", "gt.", "GolfTournament$"));
            sql.append(_Cols.getColumnList("Course", "c.", "Course$"));
            sql.append(_Cols.getColumnList("GolfCourse", "gc.", "GolfCourse$"));
            sql.append(_Cols.getColumnList("Tee", "t.", "Tee$"));
            sql.append(_Cols.getColumnList("Golfer", "g.", "Golfer$"));
            sql.append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
            sql.append("FROM GolfEventRound er ");
            sql.append("JOIN GolfEvent e ON e.GolfEventID = er.GolfEventID ");
            sql.append("JOIN GolfTournament gt ON gt.GolfTournamentID = e.GolfTournamentID ");
            sql.append("JOIN Course c ON c.CourseID = er.CourseID ");
            sql.append("JOIN Golfcourse gc ON gc.GolfCourseID = c.GolfCourseID ");
            sql.append("JOIN Tee t ON t.TeeID = er.TeeID ");
            sql.append("LEFT JOIN Golfer g ON g.GolferID = e.WinnerID ");
            sql.append("LEFT JOIN FSUser u ON u.FSUserID = g.FSUserID ");
            sql.append("WHERE er.GolfEventID = ").append(golfEventId).append(" ");        
            sql.append("ORDER BY er.RoundNumber");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                GolfEventRound objRound = new GolfEventRound(crs,"");
                rounds.add(objRound);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return rounds;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEventRound", "GolfEventRoundID", getGolfEventRoundID());
        if (doesExist) { Update(); } else { Insert(); }
    }    

    // PRIVATE METHODS
   
    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        
        try {
           
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundID")) { setGolfEventRoundID(crs.getInt(prefix + "GolfEventRoundID")); }            
            if (FSUtils.fieldExists(crs, prefix, "GolfEventID")) { setGolfEventID(crs.getInt(prefix + "GolfEventID")); }
            if (FSUtils.fieldExists(crs, prefix, "CourseID")) { setCourseID(crs.getInt(prefix + "CourseID")); }             
            if (FSUtils.fieldExists(crs, prefix, "TeeID")) { setTeeID(crs.getInt(prefix + "TeeID")); }            
            if (FSUtils.fieldExists(crs, prefix, "RoundDate")) { setRoundDate(crs.getDate(prefix + "RoundDate")); }
            if (FSUtils.fieldExists(crs, prefix, "RoundNumber")) { setRoundNumber(crs.getInt(prefix + "RoundNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "RoundName")) { setRoundName(crs.getString(prefix + "RoundName")); }            
            if (FSUtils.fieldExists(crs, prefix, "NumHoles")) { setNumHoles(crs.getInt(prefix + "NumHoles")); }
            if (FSUtils.fieldExists(crs, prefix, "Notes")) { setNotes(crs.getString(prefix + "Notes")); }
            
            // OBJECTS   
            if (FSUtils.fieldExists(crs, "GolfEvent$", "GolfEventID")) { setGolfEvent(new GolfEvent(crs, "GolfEvent$")); }            
            if (FSUtils.fieldExists(crs, "Course$", "CourseID")) { setCourse(new Course(crs, "Course$")); }            
            if (FSUtils.fieldExists(crs, "Tee$", "TeeID")) { setTee(new Tee(crs, "Tee$")); }            

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO GolfEventRound ");
        sql.append("(GolfEventID, CourseID, TeeID, RoundDate, RoundNumber, RoundName, NumHoles, Notes) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventID()));
        sql.append(FSUtils.InsertDBFieldValue(getCourseID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeeID())); 
        sql.append(FSUtils.InsertDBFieldValue(getRoundDate(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRoundNumber()));    
        sql.append(FSUtils.InsertDBFieldValue(getRoundName(), true));  
        sql.append(FSUtils.InsertDBFieldValue(getNumHoles()));         
        sql.append(FSUtils.InsertDBFieldValue(getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfEventRound SET ");    
        sql.append(FSUtils.UpdateDBFieldValue("GolfEventID", getGolfEventID()));
        sql.append(FSUtils.UpdateDBFieldValue("CourseID", getCourseID()));  
        sql.append(FSUtils.UpdateDBFieldValue("TeeID", getTeeID()));
        sql.append(FSUtils.UpdateDBFieldValue("RoundDate", getRoundDate(), true));
        sql.append(FSUtils.UpdateDBFieldValue("RoundNumber", getRoundNumber()));        
        sql.append(FSUtils.UpdateDBFieldValue("RoundName", getRoundName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NumHoles", getNumHoles()));                            
        sql.append(FSUtils.UpdateDBFieldValue("Notes", getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventRoundID = ").append(getGolfEventRoundID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}