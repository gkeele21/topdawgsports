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

public class GolfEvent implements Serializable {

    // DB FIELDS
    private Integer _GolfEventID;
    private Integer _GolfTournamentID;
    private Integer _WinnerID;
    private String _EventName;
    private Date _StartDate;    
    private Date _EndDate;
    private Integer _NumRounds;
    private Integer _NumHoles;
    private String _Notes;

    // OBJECTS
    private GolfTournament _GolfTournament;
    private Golfer _Winner;

    // CONSTRUCTORS
    public GolfEvent() {
    }
    
    public GolfEvent(int golfEventId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEvent", "", ""));
            sql.append("FROM GolfEvent ");
            sql.append("WHERE GolfEventID = ").append(golfEventId);

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

    public GolfEvent(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfEventID() {return _GolfEventID;}
    public Integer getGolfTournamentID() {return _GolfTournamentID;}
    public Integer getWinnerID() {return _WinnerID;}
    public String getEventName() {return _EventName;}
    public Date getStartDate() {return _StartDate;}
    public Date getEndDate() {return _EndDate;}
    public Integer getNumRounds() {return _NumRounds;}
    public Integer getNumHoles() {return _NumHoles;}
    public String getNotes() {return _Notes;}
    public GolfTournament getGolfTournament() {return _GolfTournament;}
    public Golfer getWinner() {return _Winner;}
    
    // SETTERS
    public void setGolfEventID(Integer GolfEventID) {_GolfEventID = GolfEventID;}
    public void setGolfTournamentID(Integer GolfTournamentID) {_GolfTournamentID = GolfTournamentID;}
    public void setWinnerID(Integer WinnerID) {_WinnerID = WinnerID;}
    public void setEventName(String EventName) {_EventName = EventName;}
    public void setStartDate(Date StartDate) {_StartDate = StartDate;}
    public void setEndDate(Date EndDate) {_EndDate = EndDate;}
    public void setNumRounds(Integer NumRounds) {_NumRounds = NumRounds;}
    public void setNumHoles(Integer NumHoles) {_NumHoles = NumHoles;}
    public void setNotes(String Notes) {_Notes = Notes;}
    public void setGolfTournament(GolfTournament GolfTournament) {_GolfTournament = GolfTournament;}
    public void setWinner(Golfer Winner) {_Winner = Winner;}

    // PUBLIC METHODS
    
    public static List<GolfEvent> GetGolfEvents(Integer golferId) {
        List<GolfEvent> events = new ArrayList<GolfEvent>();
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEvent", "e.", "")).append(", ");
            sql.append(_Cols.getColumnList("GolfTournament", "t.", "GolfTournament$")).append(", ");
            sql.append(_Cols.getColumnList("GolfEventGolfer", "eg.", "GolfEventGolfer$")).append(", ");
            sql.append(_Cols.getColumnList("Golfer", "g.", "Golfer$")).append(", ");
            sql.append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
            sql.append("FROM GolfEvent e  ");
            sql.append("JOIN GolfTournament t on t.GolfTournamentID = e.GolfTournamentID ");
            sql.append("JOIN GolfEventGolfer eg on eg.GolfEventID = e.GolfEventID ");
            sql.append("LEFT JOIN Golfer g ON g.GolferID = e.WinnerID ");
            sql.append("LEFT JOIN FSUser u ON u.FSUserID = g.FSUserID ");
            sql.append("WHERE eg.GolferID = ").append(golferId).append(" ");        
            sql.append("ORDER BY StartDate desc");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                GolfEvent objEvent = new GolfEvent(crs,"");
                events.add(objEvent);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return events;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEvent", "GolfEventID", getGolfEventID());
        if (doesExist) { Update(); } else { Insert(); }
    }
 
    // PRIVATE METHODS
   
    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS            
            if (FSUtils.fieldExists(crs, prefix, "GolfEventID")) { setGolfEventID(crs.getInt(prefix + "GolfEventID")); }            
            if (FSUtils.fieldExists(crs, prefix, "GolfTournamentID")) { setGolfTournamentID(crs.getInt(prefix + "GolfTournamentID")); }
            if (FSUtils.fieldExists(crs, prefix, "WinnerID")) { setWinnerID(crs.getInt(prefix + "WinnerID")); }            
            if (FSUtils.fieldExists(crs, prefix, "EventName")) { setEventName(crs.getString(prefix + "EventName")); }            
            if (FSUtils.fieldExists(crs, prefix, "StartDate")) { setStartDate(crs.getDate(prefix + "StartDate")); }
            if (FSUtils.fieldExists(crs, prefix, "EndDate")) { setEndDate(crs.getDate(prefix + "EndDate")); }            
            if (FSUtils.fieldExists(crs, prefix, "NumRounds")) { setNumRounds(crs.getInt(prefix + "NumRounds")); }
            if (FSUtils.fieldExists(crs, prefix, "NumHoles")) { setNumHoles(crs.getInt(prefix + "NumHoles")); }     
            if (FSUtils.fieldExists(crs, prefix, "Notes")) { setNotes(crs.getString(prefix + "Notes")); }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolfTournament$", "GolfTournamentID")) { setGolfTournament(new GolfTournament(crs, "GolfTournament$")); }
            if (FSUtils.fieldExists(crs, "Golfer$", "GolferID")) { setWinner(new Golfer(crs, "Golfer$")); }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfEvent ");
        sql.append("(GolfTournamentID, WinnerID, EventName, StartDate, EndDate, NumRounds, NumHoles, Notes) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfTournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getWinnerID()));
        sql.append(FSUtils.InsertDBFieldValue(getEventName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getStartDate(), true));
        sql.append(FSUtils.InsertDBFieldValue(getEndDate(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNumRounds()));
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

        sql.append("UPDATE GolfEvent SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolfTournamentID", getGolfTournamentID()));
        sql.append(FSUtils.UpdateDBFieldValue("WinnerID", getWinnerID()));
        sql.append(FSUtils.UpdateDBFieldValue("EventName", getEventName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("StartDate", getStartDate(), true));
        sql.append(FSUtils.UpdateDBFieldValue("EndDate", getEndDate(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NumRounds", getNumRounds()));
        sql.append(FSUtils.UpdateDBFieldValue("NumHoles", getNumHoles()));
        sql.append(FSUtils.UpdateDBFieldValue("Notes", getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventID = ").append(getGolfEventID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }  
}