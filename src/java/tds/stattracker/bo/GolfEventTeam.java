package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class GolfEventTeam implements Serializable {

    // DB FIELDS
    private Integer _GolfEventTeamID;
    private String _TeamName;
    private Integer _TeamNumber;
    private Double _AvgHandicap;
    private Double _TotalHandicap;

    // CONSTRUCTORS
    public GolfEventTeam() {
    }
    
    public GolfEventTeam(int golfEventTeamId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventTeam", "", ""));
            sql.append("FROM GolfEventTeam");
            sql.append("WHERE GolfEventTeamID = ").append(golfEventTeamId);

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

    public GolfEventTeam(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfEventTeamID() {return _GolfEventTeamID;}
    public String getTeamName() {return _TeamName;}
    public Integer getTeamNumber() {return _TeamNumber;}
    public Double getAvgHandicap() {return _AvgHandicap;}
    public Double getTotalHandicap() {return _TotalHandicap;}
    
    // SETTERS
    public void setGolfEventTeamID(Integer GolfEventTeamID) {_GolfEventTeamID = GolfEventTeamID;}
    public void setTeamName(String TeamName) {_TeamName = TeamName;}
    public void setTeamNumber(Integer TeamNumber) {_TeamNumber = TeamNumber;}
    public void setAvgHandicap(Double AvgHandicap) {_AvgHandicap = AvgHandicap;}
    public void setTotalHandicap(Double TotalHandicap) {_TotalHandicap = TotalHandicap;}

    // PUBLIC METHODS
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEventTeam", "GolfEventTeamID", getGolfEventTeamID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfEventTeamID")) { setGolfEventTeamID(crs.getInt(prefix + "GolfEventTeamID")); }
            if (FSUtils.fieldExists(crs, prefix, "TeamName")) { setTeamName(crs.getString(prefix + "TeamName")); }            
            if (FSUtils.fieldExists(crs, prefix, "TeamNumber")) { setTeamNumber(crs.getInt(prefix + "TeamNumber")); }            
            if (FSUtils.fieldExists(crs, prefix, "AvgHandicap")) { setAvgHandicap(crs.getDouble(prefix + "AvgHandicap")); }            
            if (FSUtils.fieldExists(crs, prefix, "TotalHandicap")) { setTotalHandicap(crs.getDouble(prefix + "TotalHandicap")); }            

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfEventTeam ");
        sql.append("(TeamName, TeamNumber, AvgHandicap, TotalHandicap, GamePoints) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getTeamName(), true));        
        sql.append(FSUtils.InsertDBFieldValue(getTeamNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getAvgHandicap()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalHandicap()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfEventTeam SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TeamName", getTeamName(), true));       
        sql.append(FSUtils.UpdateDBFieldValue("TeamNumber", getTeamNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("AvgHandicap", getAvgHandicap()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalHandicap", getTotalHandicap()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventTeamID = ").append(getGolfEventTeamID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    } 
}
