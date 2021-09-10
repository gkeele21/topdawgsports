package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

import java.io.Serializable;

import static tds.data.CTColumnLists._Cols;

public class GolfScore implements Serializable {

    // DB FIELDS
    private Integer _GolfScoreID;
    private String _ScoreName;
    private Integer _RelativeToPar;

    // CONSTRUCTORS
    public GolfScore() {
    }

    public GolfScore(int golfScoreId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfScore", "", ""));
            sql.append("FROM GolfScore");
            sql.append("WHERE GolfScoreID = ").append(golfScoreId);

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

    public GolfScore(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfScoreID() {return _GolfScoreID;}
    public String getScoreName() {return _ScoreName;}
    public Integer getRelativeToPar() {return _RelativeToPar;}

    // SETTERS
    public void setGolfScoreID(Integer GolfScoreID) {_GolfScoreID = GolfScoreID;}
    public void setScoreName(String ScoreName) {_ScoreName = ScoreName;}
    public void setRelativeToPar(Integer RelativeToPar) {_RelativeToPar = RelativeToPar;}

    // PUBLIC METHODS

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfScore", "GolfScoreID", getGolfScoreID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfScoreID")) { setGolfScoreID(crs.getInt(prefix + "GolfScoreID")); }
            if (FSUtils.fieldExists(crs, prefix, "ScoreName")) { setScoreName(crs.getString(prefix + "ScoreName")); }
            if (FSUtils.fieldExists(crs, prefix, "RelativeToPar")) { setRelativeToPar(crs.getInt(prefix + "RelativeToPar")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfScore ");
        sql.append("(ScoreName, RelativeToPar) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getScoreName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRelativeToPar()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfScore SET ");
        sql.append(FSUtils.UpdateDBFieldValue("ScoreName", getScoreName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("RelativeToPar", getRelativeToPar()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfScoreID = ").append(getGolfScoreID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
