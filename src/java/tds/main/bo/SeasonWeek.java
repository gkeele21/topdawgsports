package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.Application;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;

import java.io.Serializable;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static tds.data.CTColumnLists._Cols;

public class SeasonWeek implements Serializable {

    public enum Status {PENDING, CURRENT, COMPLETED};
    public enum WeekType {INITIAL, MIDDLE, FINAL};

    // DB FIELDS
    private Integer _SeasonWeekID;
    private Integer _SeasonID;
    private Integer _WeekNo;
    private LocalDateTime _StartDate;
    private LocalDateTime _EndDate;
    private String _Status;
    private String _WeekType;

    // OBJECTS
    private Season _Season;

    // CONSTRUCTORS
    public SeasonWeek() {
    }

    public SeasonWeek(int seasonWeekID) {
        this(null, seasonWeekID);
    }

    public SeasonWeek(Connection con, int seasonWeekID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("SeasonWeek", "w.", ""));
            sql.append("FROM SeasonWeek w ");
            sql.append("WHERE w.SeasonWeekID = ").append(seasonWeekID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            InitFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public SeasonWeek(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public SeasonWeek(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getSeasonWeekID() {return _SeasonWeekID;}
    public Integer getSeasonID() {return _SeasonID;}
    public Integer getWeekNo() {return _WeekNo;}
    public LocalDateTime getStartDate() {return _StartDate;}
    public LocalDateTime getEndDate() {return _EndDate;}
    public String getStatus() {return _Status;}
    public String getWeekType() {return _WeekType;}
    public Season getSeason() {if (_Season == null && _SeasonID > 0) {_Season = new Season(_SeasonID);}return _Season;}

    // SETTERS
    public void setSeasonWeekID(Integer SeasonWeekID) {_SeasonWeekID = SeasonWeekID;}
    public void setSeasonID(Integer SeasonID) {_SeasonID = SeasonID;}
    public void setWeekNo(Integer WeekNo) {_WeekNo = WeekNo;}
    public void setStartDate(LocalDateTime StartDate) {_StartDate = StartDate;}
    public void setEndDate(LocalDateTime EndDate) {_EndDate = EndDate;}
    public void setStatus(String Status) {_Status = Status;}
    public void setWeekType(String WeekType) {_WeekType = WeekType;}
    public void setSeason(Season Season) {_Season = Season;}

    // PUBLIC METHODS

    public static void CompleteSeasonWeek(SeasonWeek seasonWeek) {
        CachedRowSet crs = null;
        int nextSeasonWeekId = 0;

        try {
            // Update the seasonWeek to be COMPLETED
            seasonWeek.setStatus(Status.COMPLETED.toString());;
            seasonWeek.Save();

            // Get the next week and set it to be CURRENT
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SeasonWeekID ");
            sql.append("FROM SeasonWeek ");
            sql.append("WHERE SeasonID = ").append(seasonWeek.getSeasonID()).append(" AND WeekNo = ").append(seasonWeek.getWeekNo() + 1);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                nextSeasonWeekId = crs.getInt("SeasonWeekID");
            }

            if (nextSeasonWeekId > 0) {
                SeasonWeek nextSeasonWeek = new SeasonWeek(nextSeasonWeekId);
                nextSeasonWeek.setStatus(Status.CURRENT.toString());
                nextSeasonWeek.Save();
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    public static SeasonWeek GetPriorSeasonWeek(SeasonWeek seasonWeek) {
        SeasonWeek priorWeek = null;
        CachedRowSet crs = null;
        int priorWeekNo = seasonWeek.getWeekNo() - 1;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * ");
            sql.append("FROM SeasonWeek ");
            sql.append("WHERE SeasonID = ").append(seasonWeek.getSeasonID()).append(" AND WeekNo = ").append(priorWeekNo);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                priorWeek = new SeasonWeek(crs, "");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
        finally {
            JDBCDatabase.closeCRS(crs);
        }
        return priorWeek;
    }

    public static List<SeasonWeek> GetSportWeeks(int sportId, int sportYear) {
        List<SeasonWeek> allWeeks = new ArrayList<SeasonWeek>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("SeasonWeek", "sw.", "")).append(", ");
        sql.append(_Cols.getColumnList("Season", "s.", "Season$")).append(", ");
        sql.append(_Cols.getColumnList("Sport", "sp.", "Sport$"));
        sql.append("FROM SeasonWeek sw ");
        sql.append("JOIN Season s ON s.SeasonID = sw.SeasonID ");
        sql.append("JOIN Sport sp ON sp.SportID = s.SportID ");
        sql.append("WHERE sp.sportID = ").append(sportId).append(" AND s.SportYear = ").append(sportYear).append(" ");
        sql.append("ORDER BY sp.SportID, sw.WeekNo");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) { allWeeks.add(new SeasonWeek(crs,"")); }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
        finally {
            JDBCDatabase.closeCRS(crs);
        }
        return allWeeks;
    }

    public static SeasonWeek GetCurrentSeasonWeek(int seasonid) {
        SeasonWeek seasonWeek = null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("SeasonWeek","sw.", "SeasonWeek$"));
        sql.append("FROM SeasonWeek sw ");
        sql.append("WHERE sw.SeasonID = ").append(seasonid).append(" ");
        sql.append("ORDER BY sw.WeekNo");

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            LocalDateTime now = LocalDateTime.now();

            while (crs.next()) {
                SeasonWeek sw = new SeasonWeek(crs,"SeasonWeek$");
                if (now.isBefore(sw.getEndDate())) {
                    seasonWeek = sw;
                    break;
                }
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return seasonWeek;

    }

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("SeasonWeek", "SeasonWeekID", getSeasonWeekID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "SeasonWeekID")) { setSeasonWeekID(crs.getInt(prefix + "SeasonWeekID")); }
            if (FSUtils.fieldExists(crs, prefix, "SeasonID")) { setSeasonID(crs.getInt(prefix + "SeasonID")); }
            if (FSUtils.fieldExists(crs, prefix, "WeekNo")) { setWeekNo(crs.getInt(prefix + "WeekNo")); }
            if (FSUtils.fieldExists(crs, prefix, "StartDate")) {
                LocalDateTime s = (LocalDateTime)crs.getObject(prefix + "StartDate");
                if (s != null) {
                    setStartDate(s);
                }
            }
            if (FSUtils.fieldExists(crs, prefix, "EndDate")) {
                LocalDateTime s = (LocalDateTime)crs.getObject(prefix + "EndDate");
                if (s != null) {
                    setEndDate(s);
                }
            }
            if (FSUtils.fieldExists(crs, prefix, "Status")) { setStatus(crs.getString(prefix + "Status")); }
            if (FSUtils.fieldExists(crs, prefix, "WeekType")) { setWeekType(crs.getString(prefix + "WeekType")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "Season$", "SeasonID")) { setSeason(new Season(crs, "Season$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO SeasonWeek ");
        sql.append("(SeasonWeekID, SeasonID, WeekNo, StartDate, EndDate, Status, WeekType) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getSeasonID()));
        sql.append(FSUtils.InsertDBFieldValue(getWeekNo()));
        sql.append(FSUtils.InsertDBFieldValue((getStartDate() == null) ? null : Application._DATE_TIME_FORMATTER.format(getStartDate()), true));
        sql.append(FSUtils.InsertDBFieldValue((getEndDate() == null) ? null : Application._DATE_TIME_FORMATTER.format(getEndDate()), true));
        sql.append(FSUtils.InsertDBFieldValue(getStatus(), true));
        sql.append(FSUtils.InsertDBFieldValue(getWeekType(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE SeasonWeek SET ");
        sql.append(FSUtils.UpdateDBFieldValue("SeasonID", getSeasonID()));
        sql.append(FSUtils.UpdateDBFieldValue("WeekNo", getWeekNo()));
        sql.append(FSUtils.UpdateDBFieldValue("StartDate", (getStartDate() == null) ? null : Application._DATE_TIME_FORMATTER.format(getStartDate()), true));
        sql.append(FSUtils.UpdateDBFieldValue("EndDate", (getEndDate() == null) ? null : Application._DATE_TIME_FORMATTER.format(getEndDate()), true));
        sql.append(FSUtils.UpdateDBFieldValue("Status", getStatus(), true));
        sql.append(FSUtils.UpdateDBFieldValue("WeekType", getWeekType(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE SeasonWeekID = ").append(getSeasonWeekID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
