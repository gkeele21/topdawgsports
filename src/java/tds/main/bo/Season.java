package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class Season implements Serializable {
    
    public static final int _CurrentSeasonID = 36;
        
    // DB FIELDS
    private Integer _SeasonID;
    private Integer _SportID;    
    private String _SeasonName;
    private boolean _IsActive;
    private Integer _SportYear;
    
    // OBJECTS
    private Sport _Sport;
    
    // CONSTRUCTORS
    public Season() {
    }
    
    public Season(int seasonID) {
        this(null, seasonID);
    }

    public Season(Connection con, int seasonID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("Season", "s.", ""));
            sql.append(" FROM Season s ");
            sql.append(" WHERE s.SeasonID = ").append(seasonID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            InitFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }
    
    public Season(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public Season(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }
    
    // GETTERS
    public Integer getSeasonID() {return _SeasonID;}
    public Integer getSportID() {return _SportID;}    
    public String getSeasonName() {return _SeasonName;}
    public boolean isIsActive() {return _IsActive;}
    public Integer getSportYear() {return _SportYear;}
    public Sport getSport() {if (_Sport == null && _SportID > 0) {_Sport = new Sport(_SportID);}return _Sport;}
    
    // SETTERS
    public void setSeasonID(Integer SeasonID) {_SeasonID = SeasonID;}
    public void setSportID(Integer SportID) {_SportID = SportID;}
    public void setSeasonName(String SeasonName) {_SeasonName = SeasonName;}
    public void setIsActive(boolean IsActive) {_IsActive = IsActive;}
    public void setSportYear(Integer SportYear) {_SportYear = SportYear;}
    public void setSport(Sport Sport) {_Sport = Sport;}
    
    // PUBLIC METHODS
    
    public static List<Integer> GetAllSportYears() {
        List<Integer> years = new ArrayList<Integer>();        
        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT distinct SportYear ");
            sql.append("FROM Season ");
            sql.append("WHERE SportYear IS NOT NULL ");
            sql.append("ORDER BY SportYear desc");

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());            
            while (crs.next()) {
                years.add(crs.getInt("SportYear"));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }
        return years;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("Season", "SeasonID", getSeasonID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "SeasonID")) { setSeasonID(crs.getInt(prefix + "SeasonID")); }            
            if (FSUtils.fieldExists(crs, prefix, "SportID")) { setSportID(crs.getInt(prefix + "SportID")); }            
            if (FSUtils.fieldExists(crs, prefix, "SeasonName")) { setSeasonName(crs.getString(prefix + "SeasonName")); }            
            if (FSUtils.fieldExists(crs, prefix, "IsActive")) { setIsActive(crs.getBoolean(prefix + "IsActive")); }            
            if (FSUtils.fieldExists(crs, prefix, "SportYear")) { setSportYear(crs.getInt(prefix + "SportYear")); }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "Sport$", "SportID")) { setSport(new Sport(crs, "Sport$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO Season ");
        sql.append("(SeasonID, SportID, SeasonName, IsActive, SportYear) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getSeasonID()));
        sql.append(FSUtils.InsertDBFieldValue(getSportID()));
        sql.append(FSUtils.InsertDBFieldValue(getSeasonName(), true));
        sql.append(FSUtils.InsertDBFieldValue(isIsActive()));
        sql.append(FSUtils.InsertDBFieldValue(getSportYear()));        
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE Season SET "); 
        sql.append(FSUtils.UpdateDBFieldValue("SportID", getSportID()));
        sql.append(FSUtils.UpdateDBFieldValue("SeasonName", getSeasonName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("IsActive", isIsActive()));
        sql.append(FSUtils.UpdateDBFieldValue("SportYear", getSportYear()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE SeasonID = ").append(getSeasonID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}