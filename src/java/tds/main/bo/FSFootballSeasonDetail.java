package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;

import java.io.Serializable;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static tds.data.CTColumnLists._Cols;

public class FSFootballSeasonDetail implements Serializable{

    // DB FIELDS
    private int _FSSeasonID;
    private int _StartingWeekNumber;
    private LocalDate _TradesEndDate;
    private int _TeamsInLeague;
    private int _NumDivisions;
    private int _MaxNumStarters;
    private int _MaxNumReserves;
    private int _MaxNumInactive;
    private int _NumWeeksRegSeason;
    private int _NumWeeksPlayoffs;
    private int _PlayoffStartWeekNumber;
    private Long _MaxSalaryCap;

    // CONSTRUCTORS
    public FSFootballSeasonDetail() {
    }

    public FSFootballSeasonDetail(int seasonID) {
        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSFootballSeasonDetail", "sd.", ""));
            sql.append(" FROM FSFootballSeasonDetail sd ");
            sql.append(" WHERE sd.FSSeasonID = ").append(seasonID);

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            crs.next();
            initFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

    }

    public FSFootballSeasonDetail(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FSFootballSeasonDetail(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS
    public int getFSSeasonID() {return _FSSeasonID;}
    public int getStartingWeekNumber() {return _StartingWeekNumber;}
    public LocalDate getTradesEndDate() {return _TradesEndDate;}
    public int getTeamsInLeague() {return _TeamsInLeague;}
    public int getNumDivisions() {return _NumDivisions;}
    public int getMaxNumStarters() {return _MaxNumStarters;}
    public int getMaxNumReserves() {return _MaxNumReserves;}
    public int getMaxNumInactive() {return _MaxNumInactive;}
    public int getNumWeeksRegSeason() {return _NumWeeksRegSeason;}
    public int getNumWeeksPlayoffs() {return _NumWeeksPlayoffs;}
    public int getPlayoffStartWeekNumber() {return _PlayoffStartWeekNumber;}
    public Long getMaxSalaryCap() {return _MaxSalaryCap;}

    // SETTERS
    public void setFSSeasonID(int FSSeasonID) {_FSSeasonID = FSSeasonID;}
    public void setStartingWeekNumber(int StartingWeekNumber) {_StartingWeekNumber = StartingWeekNumber;}
    public void setTradesEndDate(LocalDate TradesEndDate) {_TradesEndDate = TradesEndDate;}
    public void setTeamsInLeague(int TeamsInLeague) {_TeamsInLeague = TeamsInLeague;}
    public void setNumDivisions(int NumDivisions) {_NumDivisions = NumDivisions;}
    public void setMaxNumStarters(int MaxNumStarters) {_MaxNumStarters = MaxNumStarters;}
    public void setMaxNumReserves(int MaxNumReserves) {_MaxNumReserves = MaxNumReserves;}
    public void setMaxNumInactive(int MaxNumInactive) {_MaxNumInactive = MaxNumInactive;}
    public void setNumWeeksRegSeason(int NumWeeksRegSeason) {_NumWeeksRegSeason = NumWeeksRegSeason;}
    public void setNumWeeksPlayoffs(int NumWeeksPlayoffs) {_NumWeeksPlayoffs = NumWeeksPlayoffs;}
    public void setPlayoffStartWeekNumber(int PlayoffStartWeekNumber) {_PlayoffStartWeekNumber = PlayoffStartWeekNumber;}
    public void setMaxSalaryCap(Long MaxSalaryCap) {_MaxSalaryCap = MaxSalaryCap;}

    // PRIVATE METHODS

    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonID")) {
                setFSSeasonID(crs.getInt(prefix + "FSSeasonID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "StartingWeekNumber")) {
                setStartingWeekNumber(crs.getInt(prefix + "StartingWeekNumber"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TradesEndDate")) {
                LocalDateTime s = (LocalDateTime)crs.getObject(prefix + "TradesEndDate");
                if (s != null) {
                    setTradesEndDate(s.toLocalDate());
                }
            }

            if (FSUtils.fieldExists(crs, prefix, "TeamsInLeague")) {
                setTeamsInLeague(crs.getInt(prefix + "TeamsInLeague"));
            }

            if (FSUtils.fieldExists(crs, prefix, "NumDivisions")) {
                setNumDivisions(crs.getInt(prefix + "NumDivisions"));
            }

            if (FSUtils.fieldExists(crs, prefix, "MaxNumStarters")) {
                setMaxNumStarters(crs.getInt(prefix + "MaxNumStarters"));
            }

            if (FSUtils.fieldExists(crs, prefix, "MaxNumReserves")) {
                setMaxNumReserves(crs.getInt(prefix + "MaxNumReserves"));
            }

            if (FSUtils.fieldExists(crs, prefix, "MaxNumInactive")) {
                setMaxNumInactive(crs.getInt(prefix + "MaxNumInactive"));
            }

            if (FSUtils.fieldExists(crs, prefix, "NumWeeksRegSeason")) {
                setNumWeeksRegSeason(crs.getInt(prefix + "NumWeeksRegSeason"));
            }

            if (FSUtils.fieldExists(crs, prefix, "NumWeeksPlayoffs")) {
                setNumWeeksPlayoffs(crs.getInt(prefix + "NumWeeksPlayoffs"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PlayoffStartWeekNumber")) {
                setPlayoffStartWeekNumber(crs.getInt(prefix + "PlayoffStartWeekNumber"));
            }

            if (FSUtils.fieldExists(crs, prefix, "MaxSalaryCap")) {
                setMaxSalaryCap((Long) crs.getLong(prefix + "MaxSalaryCap"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSFootballSeasonDetail", "FSSeasonID", getFSSeasonID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSFootballSeasonDetail ");
        sql.append("(FSSeasonID, StartingWeekNumber, TradesEndDate, TeamsInLeague, NumDivisions, MaxNumStarters, MaxNumReserves, MaxNumInactive, NumWeeksRegSeason, NumWeeksPlayoffs, PlayoffStartWeekNumber, MaxSalaryCap)");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonID()));
        sql.append(FSUtils.InsertDBFieldValue(getStartingWeekNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getTradesEndDate(), true));
        sql.append(FSUtils.InsertDBFieldValue(getTeamsInLeague()));
        sql.append(FSUtils.InsertDBFieldValue(getNumDivisions()));
        sql.append(FSUtils.InsertDBFieldValue(getMaxNumStarters()));
        sql.append(FSUtils.InsertDBFieldValue(getMaxNumReserves()));
        sql.append(FSUtils.InsertDBFieldValue(getMaxNumInactive()));
        sql.append(FSUtils.InsertDBFieldValue(getNumWeeksRegSeason()));
        sql.append(FSUtils.InsertDBFieldValue(getNumWeeksPlayoffs()));
        sql.append(FSUtils.InsertDBFieldValue(getPlayoffStartWeekNumber()));
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

        sql.append("UPDATE FSFootballSeasonDetail SET ");
        sql.append(FSUtils.UpdateDBFieldValue("StartingWeekNumber", getStartingWeekNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("TradesEndDate", getTradesEndDate(), true));
        sql.append(FSUtils.UpdateDBFieldValue("TeamsInLeague", getTeamsInLeague()));
        sql.append(FSUtils.UpdateDBFieldValue("NumDivisions", getNumDivisions()));
        sql.append(FSUtils.UpdateDBFieldValue("MaxNumStarters", getMaxNumStarters()));
        sql.append(FSUtils.UpdateDBFieldValue("MaxNumReserves", getMaxNumReserves()));
        sql.append(FSUtils.UpdateDBFieldValue("MaxNumInactive", getMaxNumInactive()));
        sql.append(FSUtils.UpdateDBFieldValue("NumWeeksRegSeason", getNumWeeksRegSeason()));
        sql.append(FSUtils.UpdateDBFieldValue("NumWeeksPlayoffs", getNumWeeksPlayoffs()));
        sql.append(FSUtils.UpdateDBFieldValue("PlayoffStartWeekNumber", getPlayoffStartWeekNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("MaxSalaryCap", getMaxSalaryCap()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSSeasonID = ").append(getFSSeasonID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
