package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

import java.io.Serializable;

import static tds.data.CTColumnLists._Cols;

public class GolfEventTeamRoundFormat implements Serializable {

    // DB FIELDS
    private Integer _GolfEventTeamID;
    private Integer _GolfEventRoundFormatID;
    private Integer _TotalGameScore;
    private Double _TotalEarnings;
    private Integer _FinalPosition;

    // OBJECTS
    private GolfEventTeam _GolfEventTeam;
    private GolfEventRoundFormat _GolfEventRoundFormat;

    // CONSTRUCTORS
    public GolfEventTeamRoundFormat() {
    }

    public GolfEventTeamRoundFormat(int golfEventTeamID, int golfEventTeamRoundFormatId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventTeamRoundFormat", "", ""));
            sql.append("FROM GolfEventTeamRoundFormat");
            sql.append("WHERE GolfEventTeamID = ").append(golfEventTeamID).append(" AND GolfEventRoundFormatID = ").append(golfEventTeamRoundFormatId);

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

    public GolfEventTeamRoundFormat(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfEventTeamID() {return _GolfEventTeamID;}
    public Integer getGolfEventRoundFormatID() {return _GolfEventRoundFormatID;}
    public Integer getTotalGameScore() {return _TotalGameScore;}
    public Double getTotalEarnings() {return _TotalEarnings;}
    public Integer getFinalPosition() {return _FinalPosition;}
    public GolfEventTeam getGolfEventTeam() {return _GolfEventTeam;}
    public GolfEventRoundFormat getGolfEventRoundFormat() {return _GolfEventRoundFormat;}

    // SETTERS
    public void setGolfEventTeamID(Integer GolfEventTeamID) {_GolfEventTeamID = GolfEventTeamID;}
    public void setGolfEventRoundFormatID(Integer GolfEventRoundFormatID) {_GolfEventRoundFormatID = GolfEventRoundFormatID;}
    public void setTotalGameScore(Integer TotalGameScore) {_TotalGameScore = TotalGameScore;}
    public void setTotalEarnings(Double TotalEarnings) {_TotalEarnings = TotalEarnings;}
    public void setFinalPosition(Integer FinalPosition) {_FinalPosition = FinalPosition;}
    public void setGolfEventTeam(GolfEventTeam golfEventTeam) {_GolfEventTeam = golfEventTeam;}
    public void setGolfEventRoundFormat(GolfEventRoundFormat golfEventRoundFormat) {_GolfEventRoundFormat = golfEventRoundFormat;}

    // PUBLIC METHODS

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEventTeamRoundFormat", "GolfEventTeamID", getGolfEventTeamID(), "GolfEventRoundFormatID", getGolfEventRoundFormatID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfEventTeamID")) { setGolfEventTeamID(crs.getInt(prefix + "GolfEventTeamID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundFormatID")) { setGolfEventRoundFormatID(crs.getInt(prefix + "GolfEventRoundFormatID")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalGameScore")) { setTotalGameScore(crs.getInt(prefix + "TotalGameScore")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalEarnings")) { setTotalEarnings(crs.getDouble(prefix + "TotalEarnings")); }
            if (FSUtils.fieldExists(crs, prefix, "FinalPosition")) { setFinalPosition(crs.getInt(prefix + "FinalPosition")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolfEventTeam$", "GolfEventTeamID")) { setGolfEventTeam(new GolfEventTeam(crs, "GolfEventTeam$")); }
            if (FSUtils.fieldExists(crs, "GolfEventRoundFormat$", "GolfEventRoundFormatID")) { setGolfEventRoundFormat(new GolfEventRoundFormat(crs, "GolfEventRoundFormat$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
       StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfEventTeamRoundFormat ");
        sql.append("(GolfEventTeamID, GolfEventRoundFormatID, TotalGameScore, TotalEarnings, FinalPosition) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventRoundFormatID()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalGameScore()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalEarnings()));
        sql.append(FSUtils.InsertDBFieldValue(getFinalPosition()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfEventTeamRoundFormat SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TotalGameScore", getTotalGameScore()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalEarnings", getTotalEarnings()));
        sql.append(FSUtils.UpdateDBFieldValue("FinalPosition", getFinalPosition()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventTeamID = ").append(getGolfEventTeamID()).append(" AND GolfEventRoundFormatID").append(getGolfEventRoundFormatID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
