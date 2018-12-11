package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.AuDate;
import bglib.util.BGConstants;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSSeasonWeek implements Serializable {
    
    public enum Status {PENDING, CURRENT, COMPLETED};
    public enum WeekType {INITIAL, MIDDLE, FINAL};
    
    // DB FIELDS
    private Integer _FSSeasonWeekID;
    private Integer _FSSeasonID;    
    private Integer _SeasonWeekID;    
    private AuDate _StartersDeadline;
    private AuDate _TransactionDeadline;
    private AuDate _StartPlayDate;
    private AuDate _EndPlayDate;
    private Integer _FSSeasonWeekNo;
    private String _Status;
    private String _WeekType;
    
    // OBJECTS
    private FSSeason _FSSeason;
    private SeasonWeek _SeasonWeek;
    
    // CONSTRUCTORS
    public FSSeasonWeek() {
    }
    
    public FSSeasonWeek(int fsSeasonWeekID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSSeasonWeek", "w.", ""));
            sql.append(",").append(_Cols.getColumnList("FSSeason", "s.", "FSSeason$"));
            sql.append(",").append(_Cols.getColumnList("FSGame", "g.", "FSGame$"));
            sql.append(",").append(_Cols.getColumnList("Sport", "sp.", "Sport$"));
            sql.append(",").append(_Cols.getColumnList("SeasonWeek","sw.", "SeasonWeek$"));
            sql.append(" FROM FSSeasonWeek w ");
            sql.append(" INNER JOIN FSSeason s ON s.FSSeasonID = w.FSSeasonID ");
            sql.append(" INNER JOIN FSGame g ON g.FSGameID = s.FSGameID ");
            sql.append(" INNER JOIN Sport sp ON sp.SportID = g.SportID ");
            sql.append(" INNER JOIN SeasonWeek sw ON sw.SeasonWeekID = w.SeasonWeekID ");
            sql.append(" WHERE w.FSSeasonWeekID = ").append(fsSeasonWeekID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            InitFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }
    
    public FSSeasonWeek(int fsSeasonId, int weekNo) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSSeasonWeek", "w.", "")).append(",");
            sql.append(_Cols.getColumnList("FSSeason", "s.", "FSSeason$")).append(",");
            sql.append(_Cols.getColumnList("FSGame", "g.", "FSGame$")).append(",");
            sql.append(_Cols.getColumnList("Sport", "sp.", "Sport$")).append(",");
            sql.append(_Cols.getColumnList("SeasonWeek","sw.", "SeasonWeek$"));
            sql.append("FROM FSSeasonWeek w ");
            sql.append("JOIN FSSeason s ON s.FSSeasonID = w.FSSeasonID ");
            sql.append("JOIN FSGame g ON g.FSGameID = s.FSGameID ");
            sql.append("JOIN Sport sp ON sp.SportID = g.SportID ");
            sql.append("JOIN SeasonWeek sw ON sw.SeasonWeekID = w.SeasonWeekID ");
            sql.append("WHERE s.FSSeasonID = ").append(fsSeasonId).append(" AND weekNo = ").append(weekNo);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            crs.next();
            InitFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }
    
    public FSSeasonWeek(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public FSSeasonWeek(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public Integer getFSSeasonID() {return _FSSeasonID;}    
    public Integer getSeasonWeekID() {return _SeasonWeekID;}    
    public AuDate getStartersDeadline() {return _StartersDeadline;}
    public AuDate getTransactionDeadline() {return _TransactionDeadline;}
    public AuDate getStartPlayDate() {return _StartPlayDate;}
    public AuDate getEndPlayDate() {return _EndPlayDate;}
    public Integer getFSSeasonWeekNo() {return _FSSeasonWeekNo;}
    public String getStatus() {return _Status;}
    public String getWeekType() {return _WeekType;}
    public FSSeason getFSSeason() {if (_FSSeason == null && _FSSeasonID > 0) {_FSSeason = new FSSeason(_FSSeasonID);}return _FSSeason;}
    public SeasonWeek getSeasonWeek() {if (_SeasonWeek == null && _SeasonWeekID > 0) {_SeasonWeek = new SeasonWeek(_SeasonWeekID);}return _SeasonWeek;}
    
    // SETTERS
    public void setFSSeasonWeekID(Integer FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setFSSeasonID(Integer FSSeasonID) {_FSSeasonID = FSSeasonID;}
    public void setSeasonWeekID(Integer SeasonWeekID) {_SeasonWeekID = SeasonWeekID;}
    public void setStartersDeadline(AuDate StartersDeadline) {_StartersDeadline = StartersDeadline;}
    public void setTransactionDeadline(AuDate TransactionDeadline) {_TransactionDeadline = TransactionDeadline;}
    public void setStartPlayDate(AuDate StartPlayDate) {_StartPlayDate = StartPlayDate;}
    public void setEndPlayDate(AuDate EndPlayDate) {_EndPlayDate = EndPlayDate;}
    public void setFSSeasonWeekNo(Integer FSSeasonWeekNo) {_FSSeasonWeekNo = FSSeasonWeekNo;}
    public void setStatus(String Status) {_Status = Status;}
    public void setWeekType(String WeekType) {_WeekType = WeekType;}
    public void setFSSeason(FSSeason FSSeason) {_FSSeason = FSSeason;}
    public void setSeasonWeek(SeasonWeek SeasonWeek) {_SeasonWeek = SeasonWeek;}
    
    // PUBLIC METHODS
    public boolean GetStartersDeadlineHasPassed() {
        AuDate deadline = getStartersDeadline();
        return new AuDate().after(deadline, false);
    }
    
    public static int GetFSSeasonWeekID(int leagueid,int weekno) {        
        int fsseasonweekid = 0;
        
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSSeasonWeek", "w.", ""));
            sql.append(",").append(_Cols.getColumnList("FSSeason", "s.", "FSSeason$"));
            sql.append(",").append(_Cols.getColumnList("FSGame", "g.", "FSGame$"));
            sql.append(",").append(_Cols.getColumnList("Sport", "sp.", "Sport$"));
            sql.append(",").append(_Cols.getColumnList("SeasonWeek","sw.", "SeasonWeek$"));
            sql.append(" FROM FSSeasonWeek w ");
            sql.append(" INNER JOIN FSSeason s ON s.FSSeasonID = w.FSSeasonID ");
            sql.append(" INNER JOIN FSGame g ON g.FSGameID = s.FSGameID ");
            sql.append(" INNER JOIN Sport sp ON sp.SportID = g.SportID ");
            sql.append(" INNER JOIN SeasonWeek sw ON sw.SeasonWeekID = w.SeasonWeekID ");
            sql.append(" INNER JOIN FSLeague l ON l.FSSeasonID = s.FSSeasonID and l.FSLeagueID = ").append(leagueid);
            sql.append(" WHERE w.FSSeasonWeekNo = ").append(weekno);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                fsseasonweekid = crs.getInt("FSSeasonWeekID");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        
        return fsseasonweekid;
    }
    
    public static List<FSSeasonWeek> GetAllFSSeasonWeeks(int fsSeasonId) {
        List<FSSeasonWeek> allWeeks = new ArrayList<FSSeasonWeek>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "")).append(",");
        sql.append(_Cols.getColumnList("FSSeason", "fss.", "FSSeason$"));
        sql.append("FROM FSSeasonWeek fssw ");
        sql.append("JOIN FSSeason fss ON fss.FSSeasonID = fssw.FSSeasonID AND fssw.FSSeasonID = ").append(fsSeasonId).append(" ");
        sql.append("ORDER BY fssw.FSSeasonWeekNo");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                allWeeks.add(new FSSeasonWeek(crs,""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return allWeeks;
    }

    public static FSSeasonWeek GetFSSeasonWeekBySeasonWeekAndGameID(int seasonWeekId, int fsGameId) {
        FSSeasonWeek week = null;
        CachedRowSet crs = null;
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT").append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "")).append(",");
        sql.append(_Cols.getColumnList("FSSeason", "fss.", "FSSeason$"));
        sql.append("FROM FSSeasonWeek fssw ");
        sql.append("JOIN FSSeason fss ON fss.FSSeasonID = fssw.FSSeasonID ");        
        sql.append("WHERE fssw.SeasonWeekID = ").append(seasonWeekId).append(" AND fss.FSGameID = ").append(fsGameId);

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            crs.next();
            week = new FSSeasonWeek(crs, "");

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return week;
    }
    
    public static List<FSSeasonWeek> GetFSSeasonWeeks(int seasonWeekId) {
        List<FSSeasonWeek> weeks = new ArrayList<FSSeasonWeek>();
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "")).append(", ");            
            sql.append(_Cols.getColumnList("FSSeason", "fss.", "FSSeason$")).append(", ");    
            sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("FSGame", "g.", "FSGame$"));
            sql.append("FROM FSSeasonWeek fssw ");
            sql.append("JOIN FSSeason fss ON fss.FSSeasonID = fssw.FSSeasonID ");
            sql.append("JOIN SeasonWeek sw ON sw.SeasonWeekID = fssw.SeasonWeekID ");
            sql.append("JOIN FSGame g ON g.FSGameID = fss.FSGameID ");
            sql.append("WHERE sw.SeasonWeekID = ").append(seasonWeekId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                weeks.add(new FSSeasonWeek(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return weeks;
    }
       
    public static int CompleteFSSeasonWeek(FSSeasonWeek fsSeasonWeek) {
        int retVal = -1;
        CachedRowSet crs = null;
        int nextFSSeasonWeekId = 0;
        
        try {
            // Update the fsSeasonWeek to be COMPLETED
            fsSeasonWeek.setStatus(Status.COMPLETED.toString());
            fsSeasonWeek.Save();
           
            // Get the next week and set it to be CURRENT
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT FSSeasonWeekID ");
            sql.append("FROM FSSeasonWeek ");
            sql.append("WHERE FSSeasonID = ").append(fsSeasonWeek.getFSSeasonID()).append(" AND FSSeasonWeekNo = ").append(fsSeasonWeek.getFSSeasonWeekNo() + 1);
            
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) { 
                nextFSSeasonWeekId = crs.getInt("FSSeasonWeekID"); 
            }            
            
            if (nextFSSeasonWeekId > 0) {
                FSSeasonWeek nextFSSeasonWeek = new FSSeasonWeek(nextFSSeasonWeekId);
                nextFSSeasonWeek.setStatus(Status.CURRENT.toString());
                nextFSSeasonWeek.Save(); 
            }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e); 
        }

        return retVal;
    }
    
    public static Map<String, FSSeasonWeek> GetCurrentWeekInfo(int fsSeasonID)
    {
        Map<String,FSSeasonWeek> weeksMap = new HashMap<String,FSSeasonWeek>();
        FSSeasonWeek displayFSSeasonWeek = new FSSeasonWeek();            
        FSSeasonWeek currentFSSeasonWeek = new FSSeasonWeek();
        
        // Retrieve all of the FSSeasonWeek's for the entire FSSeason
        List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(fsSeasonID);

        // See if we can find the week in the allWeeks variable
        weeks1 : for (FSSeasonWeek week : allWeeks) {

            if (week.getStatus().equals(FSSeasonWeek.Status.CURRENT.toString())) {
                if (displayFSSeasonWeek.getFSSeasonWeekID() == null) {
                    int ind = allWeeks.indexOf(week);
                    if (ind > 0)
                    {
                        displayFSSeasonWeek = allWeeks.get(ind - 1);
                    } else
                    {
                        displayFSSeasonWeek = week;
                    }
                }
                currentFSSeasonWeek = week;
                break weeks1;

            }

            // Past season's won't have a Current week so if we're at the final week show this week
            if (week.getWeekType().equals(FSSeasonWeek.WeekType.FINAL.toString())) {
                displayFSSeasonWeek = week;
            }
        }     

        // Set it to be the current week as long as the session object didn't have anything (null)
        if (displayFSSeasonWeek == null)
        {
            displayFSSeasonWeek = currentFSSeasonWeek;
        }
    
        weeksMap.put("displayFSSeasonWeek", displayFSSeasonWeek);
        weeksMap.put("currentFSSeasonWeek", currentFSSeasonWeek);
        
        return weeksMap;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSSeasonWeek", "FSSeasonWeekID", getFSSeasonWeekID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {            
            // DB FIELDS         
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) { setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID")); }            
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonID")) { setFSSeasonID(crs.getInt(prefix + "FSSeasonID")); }            
            if (FSUtils.fieldExists(crs, prefix, "SeasonWeekID")) { setSeasonWeekID(crs.getInt(prefix + "SeasonWeekID")); }            
            if (FSUtils.fieldExists(crs, prefix, "StartersDeadline")) { setStartersDeadline(new AuDate(crs.getTimestamp(prefix + "StartersDeadline"))); }            
            if (FSUtils.fieldExists(crs, prefix, "TransactionDeadline")) { setTransactionDeadline(new AuDate(crs.getTimestamp(prefix + "TransactionDeadline"))); }            
            if (FSUtils.fieldExists(crs, prefix, "StartPlayDate") && crs.getObject(prefix + "StartPlayDate") != null) {
                setStartPlayDate(new AuDate(crs.getTimestamp(prefix + "StartPlayDate")));
            } else {
                if (_SeasonWeek != null) {
                    setStartPlayDate(getSeasonWeek().getStartDate());
                }
            }            
            if (FSUtils.fieldExists(crs, prefix, "EndPlayDate") && crs.getObject(prefix + "EndPlayDate") != null) {
                setEndPlayDate(new AuDate(crs.getTimestamp(prefix + "EndPlayDate")));
            } else {
                if (_SeasonWeek != null) {
                    setEndPlayDate(getSeasonWeek().getEndDate());
                }
            }            
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekNo")) { setFSSeasonWeekNo(crs.getInt(prefix + "FSSeasonWeekNo")); }            
            if (FSUtils.fieldExists(crs, prefix, "Status")) { setStatus(crs.getString(prefix + "Status")); }            
            if (FSUtils.fieldExists(crs, prefix, "WeekType")) { setWeekType(crs.getString(prefix + "WeekType")); }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSSeason$", "FSSeasonID")) { setFSSeason(new FSSeason(crs, "FSSeason$")); }            
            if (FSUtils.fieldExists(crs, "SeasonWeek$", "SeasonWeekID")) { setSeasonWeek(new SeasonWeek(crs, "SeasonWeek$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }        
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSSeasonWeek ");
        sql.append("(FSSeasonWeekID, FSSeasonID, SeasonWeekID, StartersDeadline, TransactionDeadline, StartPlayDate, EndPlayDate, FSSeasonWeekNo, Status, WeekType) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonID()));
        sql.append(FSUtils.InsertDBFieldValue(getSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue((getStartersDeadline()) == null ? null : getStartersDeadline().toString(BGConstants.PLAYDATETIME_PATTERN), true));
        sql.append(FSUtils.InsertDBFieldValue((getTransactionDeadline()) == null ? null : getTransactionDeadline().toString(BGConstants.PLAYDATETIME_PATTERN), true));
        sql.append(FSUtils.InsertDBFieldValue((getStartPlayDate()) == null ? null : getStartPlayDate().toString(BGConstants.PLAYDATETIME_PATTERN), true));
        sql.append(FSUtils.InsertDBFieldValue((getEndPlayDate()) == null ? null : getEndPlayDate().toString(BGConstants.PLAYDATETIME_PATTERN), true));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonWeekNo()));
        sql.append(FSUtils.InsertDBFieldValue(getStatus(), true));
        sql.append(FSUtils.InsertDBFieldValue(getWeekType(), true));        
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE FSSeasonWeek SET ");
        sql.append(FSUtils.UpdateDBFieldValue("FSSeasonID", getFSSeasonID()));
        sql.append(FSUtils.UpdateDBFieldValue("SeasonWeekID", getSeasonWeekID()));
        sql.append(FSUtils.UpdateDBFieldValue("StartersDeadline", (getStartersDeadline() == null) ? null : getStartersDeadline().toString(BGConstants.PLAYDATETIME_PATTERN), true));
        sql.append(FSUtils.UpdateDBFieldValue("TransactionDeadline", (getTransactionDeadline() == null) ? null : getTransactionDeadline().toString(BGConstants.PLAYDATETIME_PATTERN), true));
        sql.append(FSUtils.UpdateDBFieldValue("StartPlayDate", (getStartPlayDate() == null) ? null : getStartPlayDate().toString(BGConstants.PLAYDATETIME_PATTERN), true));
        sql.append(FSUtils.UpdateDBFieldValue("EndPlayDate", (getEndPlayDate() == null) ? null : getEndPlayDate().toString(BGConstants.PLAYDATETIME_PATTERN), true));
        sql.append(FSUtils.UpdateDBFieldValue("FSSeasonWeekNo", getFSSeasonWeekNo()));
        sql.append(FSUtils.UpdateDBFieldValue("Status", getStatus(), true));
        sql.append(FSUtils.UpdateDBFieldValue("WeekType", getWeekType(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSSeasonWeekID = ").append(getFSSeasonWeekID());
        
        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}