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

public class GolferRound implements Serializable {

    // DB FIELDS
    private Integer _GolferRoundID;
    private Integer _GolferID;
    private Integer _CourseID;
    private Integer _TeeID;
    private Date _RoundDate;
    private Integer _RoundNumber;
    private String _RoundName;    
    private Integer _StartingHoleNumber;    
    private Integer _NumHoles;
    private Integer _Score;
    private Integer _RelativeToPar;
    private String _Conditions;
    private String _Notes;

    // OBJECTS
    private Golfer _Golfer;
    private Course _Course;
    private Tee _Tee;

    // CONSTRUCTORS
    public GolferRound() {
    }
    
    public GolferRound(int golferRoundId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolferRound", "", ""));
            sql.append("FROM GolferRound");
            sql.append("WHERE GolferRoundID = ").append(golferRoundId);

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
    
    public GolferRound(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolferRoundID() {return _GolferRoundID;}
    public Integer getGolferID() {return _GolferID;}
    public Integer getCourseID() {return _CourseID;}
    public Integer getTeeID() {return _TeeID;}
    public Date getRoundDate() {return _RoundDate;}
    public Integer getRoundNumber() {return _RoundNumber;}
    public String getRoundName() {return _RoundName;}
    public Integer getStartingHoleNumber() {return _StartingHoleNumber;}
    public Integer getNumHoles() {return _NumHoles;}
    public Integer getScore() {return _Score;}
    public Integer getRelativeToPar() {return _RelativeToPar;}
    public String getConditions() {return _Conditions;}
    public String getNotes() {return _Notes;}
    public Golfer getGolfer() {return _Golfer;} 
    public Course getCourse() {return _Course;} 
    public Tee getTee() {return _Tee;} 
    
    // SETTERS
    public void setGolferRoundID(Integer GolferRoundID) {_GolferRoundID = GolferRoundID;}
    public void setGolferID(Integer GolferID) {_GolferID = GolferID;}
    public void setCourseID(Integer CourseID) {_CourseID = CourseID;}
    public void setTeeID(Integer TeeID) {_TeeID = TeeID;}
    public void setRoundDate(Date RoundDate) {_RoundDate = RoundDate;}
    public void setRoundNumber(Integer RoundNumber) {_RoundNumber = RoundNumber;}
    public void setRoundName(String RoundName) {_RoundName = RoundName;}
    public void setStartingHoleNumber(Integer StartingHoleNumber) {_StartingHoleNumber = StartingHoleNumber;}
    public void setNumHoles(Integer NumHoles) {_NumHoles = NumHoles;}
    public void setScore(Integer Score) {_Score = Score;}
    public void setRelativeToPar(Integer RelativeToPar) {_RelativeToPar = RelativeToPar;}
    public void setConditions(String Conditions) {_Conditions = Conditions;}
    public void setNotes(String Notes) {_Notes = Notes;}
    public void setGolfer(Golfer Golfer) {_Golfer = Golfer;}
    public void setCourse(Course Course) {_Course = Course;}
    public void setTee(Tee Tee) {_Tee = Tee;}

    // PUBLIC METHODS

    public static List<GolferRound> GetGolferRounds(Integer golferId) {
        List<GolferRound> rounds = new ArrayList<GolferRound>();
        CachedRowSet crs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolferRound", "r.", "")).append(",");
            sql.append(_Cols.getColumnList("Golfer", "g.", "Golfer$")).append(",");
            sql.append(_Cols.getColumnList("Course", "c.", "Course$")).append(",");
            sql.append(_Cols.getColumnList("GolfCourse", "gc.", "GolfCourse$")).append(",");
            sql.append(_Cols.getColumnList("Tee", "t.", "Tee$"));
            sql.append("FROM GolferRound r ");
            sql.append("JOIN Golfer g ON g.GolferID = r.GolferID ");
            sql.append("JOIN Course c ON c.CourseID = r.CourseID ");
            sql.append("JOIN GolfCourse gc ON gc.GolfCourseID = c.GolfCourseID ");
            sql.append("JOIN Tee t ON t.TeeID = r.TeeID ");
            sql.append("WHERE r.GolferID = ").append(golferId).append(" ");       
            sql.append("ORDER BY r.RoundDate desc, r.roundNumber desc;");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                GolferRound objRound = new GolferRound(crs,"");
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
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolferRound", "GolferRoundID", getGolferRoundID());
        if (doesExist) { Update(); } else { Insert(); }
    }
    
    // PRIVATE METHODS
    
    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {    
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolferRoundID")) { setGolferRoundID(crs.getInt(prefix + "GolferRoundID")); }            
            if (FSUtils.fieldExists(crs, prefix, "GolferID")) { setGolferID(crs.getInt(prefix + "GolferID")); }            
            if (FSUtils.fieldExists(crs, prefix, "CourseID")) { setCourseID(crs.getInt(prefix + "CourseID")); }            
            if (FSUtils.fieldExists(crs, prefix, "TeeID")) { setTeeID(crs.getInt(prefix + "TeeID")); }            
            if (FSUtils.fieldExists(crs, prefix, "RoundDate")) { setRoundDate(crs.getDate(prefix + "RoundDate")); }            
            if (FSUtils.fieldExists(crs, prefix, "RoundNumber")) { setRoundNumber(crs.getInt(prefix + "RoundNumber")); }            
            if (FSUtils.fieldExists(crs, prefix, "RoundName")) { setRoundName(crs.getString(prefix + "RoundName")); }            
            if (FSUtils.fieldExists(crs, prefix, "StartingHoleNumber")) { setStartingHoleNumber(crs.getInt(prefix + "StartingHoleNumber")); }            
            if (FSUtils.fieldExists(crs, prefix, "NumHoles")) { setNumHoles(crs.getInt(prefix + "NumHoles")); }             
            if (FSUtils.fieldExists(crs, prefix, "Score")) { setScore(crs.getInt(prefix + "Score")); }               
            if (FSUtils.fieldExists(crs, prefix, "RelativeToPar")) { setRelativeToPar(crs.getInt(prefix + "RelativeToPar")); }               
            if (FSUtils.fieldExists(crs, prefix, "Conditions")) { setConditions(crs.getString(prefix + "Conditions")); }            
            if (FSUtils.fieldExists(crs, prefix, "Notes")) { setNotes(crs.getString(prefix + "Notes")); }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "Golfer$", "GolferID")) { setGolfer(new Golfer(crs, "Golfer$")); }            
            if (FSUtils.fieldExists(crs, "Course$", "CourseID")) { setCourse(new Course(crs, "Course$")); }            
            if (FSUtils.fieldExists(crs, "Tee$", "TeeID")) { setTee(new Tee(crs, "Tee$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolferRound ");
        sql.append("(GolferID, CourseID, TeeID, RoundDate, RoundNumber, RoundName, StartingHoleNumber, NumHoles, Score, RelativeToPar, Conditions, Notes) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolferID()));
        sql.append(FSUtils.InsertDBFieldValue(getCourseID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeeID()));
        sql.append(FSUtils.InsertDBFieldValue(getRoundDate(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRoundNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getRoundName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getStartingHoleNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getNumHoles()));
        sql.append(FSUtils.InsertDBFieldValue(getScore()));
        sql.append(FSUtils.InsertDBFieldValue(getRelativeToPar()));
        sql.append(FSUtils.InsertDBFieldValue(getConditions(), true));     
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
        
        sql.append("UPDATE GolferRound SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolferID", getGolferID()));
        sql.append(FSUtils.UpdateDBFieldValue("CourseID", getCourseID()));
        sql.append(FSUtils.UpdateDBFieldValue("TeeID", getTeeID()));
        sql.append(FSUtils.UpdateDBFieldValue("RoundDate", getRoundDate(), true)); 
        sql.append(FSUtils.UpdateDBFieldValue("RoundNumber", getRoundNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("RoundName", getRoundName(), true)); 
        sql.append(FSUtils.UpdateDBFieldValue("NumHoles", getNumHoles()));
        sql.append(FSUtils.UpdateDBFieldValue("Score", getScore())); 
        sql.append(FSUtils.UpdateDBFieldValue("RelativeToPar", getRelativeToPar())); 
        sql.append(FSUtils.UpdateDBFieldValue("Conditions", getConditions(), true)); 
        sql.append(FSUtils.UpdateDBFieldValue("Notes", getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolferRoundID = ").append(getGolferRoundID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}