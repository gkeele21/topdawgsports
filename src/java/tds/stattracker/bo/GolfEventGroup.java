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

public class GolfEventGroup implements Serializable {

    // DB FIELDS
    private Integer _GolfEventGroupID;
    private String _GroupName;
    private Integer _GroupNumber;
    private Date _TeeTime;

    // CONSTRUCTORS
    public GolfEventGroup() {
    }
    
    public GolfEventGroup(int golfEventGroupId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventGroup", "", ""));
            sql.append("FROM GolfEventGroup");
            sql.append("WHERE GolfEventGroupID = ").append(golfEventGroupId);

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

    public GolfEventGroup(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfEventGroupID() {return _GolfEventGroupID;}
    public String getGroupName() {return _GroupName;}
    public Integer getGroupNumber() {return _GroupNumber;}
    public Date getTeeTime() {return _TeeTime;}
    
    // SETTERS
    public void setGolfEventGroupID(Integer GolfEventGroupID) {_GolfEventGroupID = GolfEventGroupID;}
    public void setGroupName(String GroupName) {_GroupName = GroupName;}
    public void setGroupNumber(Integer GroupNumber) {_GroupNumber = GroupNumber;}
    public void setTeeTime(Date TeeTime) {_TeeTime = TeeTime;}

    // PUBLIC METHODS
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEventGroup", "GolfEventGroupID", getGolfEventGroupID());
        if (doesExist) { Update(); } else { Insert(); }
    }
    
    public static List<GolfEventGroup> GetGolfEventGroups(Integer golfEventRoundId) {
        List<GolfEventGroup> teams = new ArrayList<GolfEventGroup>();
        CachedRowSet crs = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventGroup", "grp.", "")).append(", ");
            sql.append(_Cols.getColumnList("GolfEventRoundGolfer", "erg.", "GolfEventRoundGolfer$")).append(", ");
            sql.append(_Cols.getColumnList("GolferRound", "r.", "GolferRound$")).append(", ");
            sql.append(_Cols.getColumnList("GolfEventRound", "er.", "GolfEventRound$")).append(", ");
            sql.append(_Cols.getColumnList("GolfEventGolfer", "eg.", "GolfEventGolfer$")).append(", ");
            sql.append(_Cols.getColumnList("Golfer", "g.", "Golfer$")).append(", ");
            sql.append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
            sql.append("FROM GolfEventGroup grp ");
            sql.append("JOIN GolfEventRoundGolfer erg ON erg.GolfEventGroupID = grp.GolfEventGroupID ");
            sql.append("JOIN GolferRound r ON r.GolferRoundID = erg.GolferRoundID ");
            sql.append("JOIN GolfEventRound er ON er.GolfEventRoundID = erg.GolfEventRoundID ");
            sql.append("JOIN GolfEventGolfer eg ON eg.GolfEventID = er.GolfEventID AND eg.GolferID = r.GolferID ");
            sql.append("JOIN Golfer g ON g.GolferID = r.GolferID ");
            sql.append("JOIN FSUser u ON u.FSUserID = g.FSUserID ");
            sql.append("WHERE erg.GolfEventRoundID = ").append(golfEventRoundId).append(" ");        
            sql.append("ORDER BY GroupName, eg.Rank");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                GolfEventGroup objGroup = new GolfEventGroup(crs,"");
                teams.add(objGroup);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return teams;
    }
    
    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfEventGroupID")) { setGolfEventGroupID(crs.getInt(prefix + "GolfEventGroupID")); }
            if (FSUtils.fieldExists(crs, prefix, "GroupName")) { setGroupName(crs.getString(prefix + "GroupName")); }            
            if (FSUtils.fieldExists(crs, prefix, "GroupNumber")) { setGroupNumber(crs.getInt(prefix + "GroupNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "TeeTime")) { setTeeTime(crs.getDate(prefix + "TeeTime")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfEventGroup ");
        sql.append("(GroupName, GroupNumber, TeeTime) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGroupName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getGroupNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getTeeTime(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfEventGroup SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GroupName", getGroupName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("GroupNumber", getGroupNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("TeeTime", getTeeTime(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventGroupID = ").append(getGolfEventGroupID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }   
}