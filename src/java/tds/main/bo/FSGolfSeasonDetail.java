package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;

import java.io.Serializable;
import java.sql.Connection;

import static tds.data.CTColumnLists._Cols;

public class FSGolfSeasonDetail implements Serializable{

    // DB FIELDS
    private int _FSSeasonID;
    private int _StartingWeekNumber;
    private int _MaxGolfers;
    private int _CountGolfers;
    private Long _MaxSalaryCap;
    private int _NumWorstScoresToRemove;

    // CONSTRUCTORS
    public FSGolfSeasonDetail() {
    }

    public FSGolfSeasonDetail(int fsseasonID) {
        this(null, fsseasonID);
    }

    public FSGolfSeasonDetail(Connection con, int fsseasonID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSGolfSeasonDetail", "sd.", ""));
            sql.append(" FROM FSGolfSeasonDetail sd ");
            sql.append(" WHERE sd.FSSeasonID = ").append(fsseasonID);


            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            initFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }

    public FSGolfSeasonDetail(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FSGolfSeasonDetail(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS
    public int getFSSeasonID() {return _FSSeasonID;}
    public int getStartingWeekNumber() {return _StartingWeekNumber;}
    public int getMaxGolfers() {return _MaxGolfers;}
    public int getCountGolfers() {return _CountGolfers;}
    public Long getMaxSalaryCap() {return _MaxSalaryCap;}
    public int getNumWorstScoresToRemove() {return _NumWorstScoresToRemove;}

    // SETTERS
    public void setFSSeasonID(int FSSeasonID) {_FSSeasonID = FSSeasonID;}
    public void setStartingWeekNumber(int StartingWeekNumber) {_StartingWeekNumber = StartingWeekNumber;}
    public void setMaxGolfers(int MaxGolfers) {_MaxGolfers = MaxGolfers;}
    public void setCountGolfers(int CountGolfers) {_CountGolfers = CountGolfers;}
    public void setMaxSalaryCap(Long MaxSalaryCap) {_MaxSalaryCap = MaxSalaryCap;}
    public void setNumWorstScoresToRemove(int NumWorst) {_NumWorstScoresToRemove = NumWorst;}

    // PRIVATE METHODS

    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonID")) { setFSSeasonID(crs.getInt(prefix + "FSSeasonID")); }
            if (FSUtils.fieldExists(crs, prefix, "StartingWeekNumber")) { setStartingWeekNumber(crs.getInt(prefix + "StartingWeekNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "MaxGolfers")) { setMaxGolfers(crs.getInt(prefix + "MaxGolfers")); }
            if (FSUtils.fieldExists(crs, prefix, "CountGolfers")) { setCountGolfers(crs.getInt(prefix + "CountGolfers")); }
            if (FSUtils.fieldExists(crs, prefix, "MaxSalaryCap")) { setMaxSalaryCap((Long) crs.getLong(prefix + "MaxSalaryCap")); }
            if (FSUtils.fieldExists(crs, prefix, "NumWorstScoresToRemove")) { setNumWorstScoresToRemove(crs.getInt(prefix + "NumWorstScoresToRemove")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSGolfSeasonDetail", "FSSeasonID", getFSSeasonID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSGolfSeasonDetail ");
        sql.append("(FSSeasonID, StartingWeekNumber, MaxGolfers, CountGolfers, NumWorstScoresToRemove, MaxSalaryCap)");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonID()));
        sql.append(FSUtils.InsertDBFieldValue(getStartingWeekNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getMaxGolfers()));
        sql.append(FSUtils.InsertDBFieldValue(getCountGolfers()));
        sql.append(FSUtils.InsertDBFieldValue(getNumWorstScoresToRemove()));
        sql.append(FSUtils.InsertDBFieldValue(getMaxSalaryCap()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE FSGolfSeasonDetail SET ");
        sql.append(FSUtils.UpdateDBFieldValue("StartingWeekNumber", getStartingWeekNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("MaxGolfers", getMaxGolfers()));
        sql.append(FSUtils.UpdateDBFieldValue("CountGolfers", getCountGolfers()));
        sql.append(FSUtils.UpdateDBFieldValue("MaxSalaryCap", getMaxSalaryCap()));
        sql.append(FSUtils.UpdateDBFieldValue("NumWorstScoresToRemove", getNumWorstScoresToRemove()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSSeasonID = ").append(getFSSeasonID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
