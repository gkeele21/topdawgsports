package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.*;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSSeason implements Serializable {
    
    // DB FIELDS
    private Integer _FSSeasonID;
    private Integer _FSGameID;    
    private Integer _SeasonID;    
    private String _SeasonName;
    private boolean _IsActive;
    private boolean _DisplayTeams;
    private String _DisplayStatsYear;
    private Integer _CurrentFSSeasonWeekID;    
    
    // OBJECTS
    private FSGame _FSGame;
    private Season _Season;
    private FSSeasonWeek _CurrentFSSeasonWeek;
    
    // ADDITIONAL FIELDS
    private FSFootballSeasonDetail _FSFootballSeasonDetail;
    private Map<Integer, FSFootballRosterPositions> _FSFootballRosterPositions;
    private Map<Integer, FSSeasonWeek> _CurrentFSSeasonWeeks;    

    // CONSTRUCTORS
    public FSSeason() {
    }

    public FSSeason(int seasonID) {
        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSSeason", "s.", ""));
            sql.append(" FROM FSSeason s ");
            sql.append(" WHERE s.FSSeasonID = ").append(seasonID);

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            crs.next();
            InitFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }
    }
    
    public FSSeason(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public FSSeason(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getFSSeasonID() {return _FSSeasonID;}
    public void setFSSeasonID(int FSSeasonID) {_FSSeasonID = FSSeasonID;}
    public Integer getFSGameID() {return _FSGameID;}    
    public Integer getSeasonID() {return _SeasonID;}    
    public String getSeasonName() {return _SeasonName;}
    public boolean isIsActive() {return _IsActive;}
    public boolean isDisplayTeams() {return _DisplayTeams;}
    public String getDisplayStatsYear() {return _DisplayStatsYear;}
    public Integer getCurrentFSSeasonWeekID() { return _CurrentFSSeasonWeekID; }
    public FSGame getFSGame() {if (_FSGame == null && _FSGameID > 0) {_FSGame = new FSGame(_FSGameID);}return _FSGame;}
    public Season getSeason() {if (_Season == null && _SeasonID > 0) {_Season = new Season(_SeasonID);}return _Season;}
    
    public FSSeasonWeek getCurrentFSSeasonWeek() {
        // check for the current week
        List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(getFSSeasonID());

        for (FSSeasonWeek week : allWeeks) {
            String status = week.getStatus();
            if ("CURRENT".equals(status))
            {
                _CurrentFSSeasonWeek = week;
                _CurrentFSSeasonWeekID = week.getFSSeasonWeekID();
                return _CurrentFSSeasonWeek;
            }
        }

        if (_CurrentFSSeasonWeek == null && _CurrentFSSeasonWeekID != null && _CurrentFSSeasonWeekID > 0)
        {
            _CurrentFSSeasonWeek = new FSSeasonWeek(_CurrentFSSeasonWeekID);
        }
        return _CurrentFSSeasonWeek;
    }
    
    public FSFootballSeasonDetail getFSFootballSeasonDetail() {if (_FSFootballSeasonDetail == null &&_FSSeasonID > 0) {_FSFootballSeasonDetail = new FSFootballSeasonDetail(_FSSeasonID);}return _FSFootballSeasonDetail;}
    
    // SETTERS
    public void setFSGameID(Integer FSGameID) {_FSGameID = FSGameID;}
    public void setSeasonID(Integer SeasonID) {_SeasonID = SeasonID;}
    public void setSeasonName(String SeasonName) {_SeasonName = SeasonName;}
    public void setIsActive(boolean IsActive) {_IsActive = IsActive;}
    public void setDisplayTeams(boolean DisplayTeams) {_DisplayTeams = DisplayTeams;}
    public void setDisplayStatsYear(String DisplayStatsYear) {_DisplayStatsYear = DisplayStatsYear;}
    public void setCurrentFSSeasonWeekID(Integer CurrentFSSeasonWeekID) {_CurrentFSSeasonWeekID = CurrentFSSeasonWeekID;}
    public void setFSGame(FSGame FSGame) {_FSGame = FSGame;}
    public void setSeason(Season Season) {_Season = Season;}
    public void setFSFootballSeasonDetail(FSFootballSeasonDetail FSFootballSeasonDetail) {_FSFootballSeasonDetail = FSFootballSeasonDetail;}
    public void setCurrentFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_CurrentFSSeasonWeek = FSSeasonWeek;}
    public void setFSFootballRosterPositions(Map<Integer, FSFootballRosterPositions> FSFootballRosterPositions) {_FSFootballRosterPositions = FSFootballRosterPositions;}
    public void setCurrentFSSeasonWeeks(Map<Integer, FSSeasonWeek> CurrentFSSeasonWeeks) {_CurrentFSSeasonWeeks = CurrentFSSeasonWeeks;}
    
    // PUBLIC METHODS
    
    public Map<Integer, FSSeasonWeek> GetCurrentFSSeasonWeeks() {
        if (_CurrentFSSeasonWeeks == null || _CurrentFSSeasonWeeks.size() == 0) {
            setCurrentFSSeasonWeeks(new HashMap<Integer, FSSeasonWeek>());

            CachedRowSet crs = null;
            Connection con = null;
            try {
                StringBuilder sql = new StringBuilder();
                sql.append(" SELECT ").append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$"));
                sql.append(",").append(_Cols.getColumnList("SeasonWeek","sw.", "SeasonWeek$"));
                sql.append(" from FSSeasonWeek w ");
                sql.append(" inner join SeasonWeek sw on sw.SeasonWeekID = w.SeasonWeekID ");
                sql.append(" where w.FSSeasonID = ").append(getFSSeasonID());

                con = CTApplication._CT_DB.getConn(false);
                crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
                while (crs.next()) {
                    FSSeasonWeek week = new FSSeasonWeek(crs, "FSSeasonWeek$");
                    _CurrentFSSeasonWeeks.put(week.getFSSeasonWeekNo(),week);
                }
            } catch (Exception e) {
                CTApplication._CT_LOG.error(e);
            } finally {
                JDBCDatabase.closeCRS(crs);
                JDBCDatabase.close(con);
            }
        }
        
        return _CurrentFSSeasonWeeks;
    }    

    public Map<Integer, FSFootballRosterPositions> GetFSFootballRosterPositions() {
        if (_FSFootballRosterPositions == null || _FSFootballRosterPositions.isEmpty()) {
            
            setFSFootballRosterPositions(new HashMap<Integer, FSFootballRosterPositions>());

            CachedRowSet crs = null;
            Connection con = null;
            Collection<Position> allPositions = Position.getAllPositions();

            for (Position pos : allPositions) {
                try {
                    StringBuilder sql = new StringBuilder();
                    sql.append("SELECT ").append(_Cols.getColumnList("FSFootballRosterPositions", "rp.", ""));
                    sql.append(" FROM FSFootballRosterPositions rp ");
                    sql.append(" WHERE rp.FSSeasonID = ").append(getFSSeasonID());
                    sql.append(" AND rp.PositionID = ").append(pos.getPositionID());

                    con = CTApplication._CT_DB.getConn(false);
                    crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
                    while (crs.next()) {
                        FSFootballRosterPositions position = new FSFootballRosterPositions(crs, "");
                        _FSFootballRosterPositions.put(pos.getPositionID(),position);
                    }
                } catch (Exception e) {
                    CTApplication._CT_LOG.error(e);
                } finally {
                    JDBCDatabase.closeCRS(crs);
                    JDBCDatabase.close(con);
                }
                
            }

        }
        
        return _FSFootballRosterPositions;
    }

    public List<FSLeague> GetLeagues() {    
        List<FSLeague> leagues = new ArrayList<FSLeague>();

        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSLeague", "l.", ""));
            sql.append(" FROM FSLeague l ");
            sql.append(" WHERE l.FSSeasonID = ").append(getFSSeasonID());

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                leagues.add(new FSLeague(crs));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return leagues;
    }

    public static List<FSSeason> GetFSSeasons(int fsGameID) {
        List<FSSeason> fsseasons = new ArrayList<FSSeason>();

        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            
            if (fsGameID > 0) {
                sql.append(" SELECT ").append(_Cols.getColumnList("FSSeason", "s.", ""));
                sql.append(" FROM FSSeason s ");
                sql.append(" WHERE s.FSGameID = ").append(fsGameID);
            } else {
                sql.append(" select ").append(_Cols.getColumnList("FSSeason", "s.", ""));
                sql.append(" FROM FSSeason s");
            }

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                fsseasons.add(new FSSeason(crs));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return fsseasons;
    }
 
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSSeason", "FSSeasonID", getFSSeasonID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /*  This method populates the object from a cached row set.  */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonID")) { setFSSeasonID(crs.getInt(prefix + "FSSeasonID")); }  
            if (FSUtils.fieldExists(crs, prefix, "FSGameID")) { setFSGameID(crs.getInt(prefix + "FSGameID")); }  
            if (FSUtils.fieldExists(crs, prefix, "SeasonID")) { setSeasonID(crs.getInt(prefix + "SeasonID")); }  
            if (FSUtils.fieldExists(crs, prefix, "SeasonName")) { setSeasonName(crs.getString(prefix + "SeasonName")); }  
            if (FSUtils.fieldExists(crs, prefix, "IsActive")) { setIsActive(crs.getBoolean(prefix + "IsActive")); }  
            if (FSUtils.fieldExists(crs, prefix, "DisplayTeams")) { setDisplayTeams(crs.getBoolean(prefix + "DisplayTeams")); }  
            if (FSUtils.fieldExists(crs, prefix, "DisplayStatsYear")) { setDisplayStatsYear(crs.getString(prefix + "DisplayStatsYear")); }  
            if (FSUtils.fieldExists(crs, prefix, "FSGameID")) { setFSGameID(crs.getInt(prefix + "FSGameID")); }  
            if (FSUtils.fieldExists(crs, prefix, "DisplayTeams")) { setDisplayTeams(crs.getBoolean(prefix + "DisplayTeams")); }  
            if (FSUtils.fieldExists(crs, prefix, "DisplayStatsYear")) { setDisplayStatsYear(crs.getString(prefix + "DisplayStatsYear")); }  
            if (FSUtils.fieldExists(crs, prefix, "CurrentFSSeasonWeekID")) { setCurrentFSSeasonWeekID(crs.getInt(prefix + "CurrentFSSeasonWeekID")); }  
            
            // OBJECTS 
            if (FSUtils.fieldExists(crs, "FSGame$", "FSGameID")) { setFSGame(new FSGame(crs, "FSGame$")); }  
            if (FSUtils.fieldExists(crs, "Season$", "SeasonID")) { setSeason(new Season(crs, "Season$")); }  
            if (FSUtils.fieldExists(crs, "FSFootballSeasonDetail$", "FSSeasonID")) { setFSFootballSeasonDetail(new FSFootballSeasonDetail(crs, "FSFootballSeasonDetail$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }        
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSSeason ");
        sql.append("(FSSeasonID, FSGameID, SeasonID, SeasonName, IsActive, DisplayTeams, DisplayStatsYear, CurrentFSSeasonWeekID) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSGameID()));
        sql.append(FSUtils.InsertDBFieldValue(getSeasonID()));
        sql.append(FSUtils.InsertDBFieldValue(getSeasonName(), true));
        sql.append(FSUtils.InsertDBFieldValue(isIsActive()));
        sql.append(FSUtils.InsertDBFieldValue(isDisplayTeams()));
        sql.append(FSUtils.InsertDBFieldValue(getDisplayStatsYear(), true));
        sql.append(FSUtils.InsertDBFieldValue(getCurrentFSSeasonWeekID()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE FSSeason SET ");
        sql.append(FSUtils.UpdateDBFieldValue("FSGameID", getFSGameID()));
        sql.append(FSUtils.UpdateDBFieldValue("SeasonID", getSeasonID()));
        sql.append(FSUtils.UpdateDBFieldValue("SeasonName", getSeasonName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("IsActive", isIsActive()));
        sql.append(FSUtils.UpdateDBFieldValue("DisplayTeams", isDisplayTeams()));
        sql.append(FSUtils.UpdateDBFieldValue("DisplayStatsYear", getDisplayStatsYear(), true));
        sql.append(FSUtils.UpdateDBFieldValue("CurrentFSSeasonWeekID", getCurrentFSSeasonWeekID()));
        sql.append("WHERE FSSeasonID = ").append(getFSSeasonID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}