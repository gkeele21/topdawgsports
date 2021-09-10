package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;
import tds.stattracker.bo.GolfEventRound.DisplayOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static tds.data.CTColumnLists._Cols;

public class GolfEventRoundGolfer implements Serializable {

    // DB FIELDS
    private Integer _GolfEventRoundGolferID;
    private Integer _GolfEventRoundID;
    private Integer _GolferRoundID;
    private Integer _GolfEventGroupID;
    private Integer _GolfEventTeamID;
    private Double _Earnings;

    // OBJECTS
    private GolfEventRound _GolfEventRound;
    private GolferRound _GolferRound;
    private GolfEventGroup _GolfEventGroup;
    private GolfEventTeam _GolfEventTeam;

    // ADDITIONAL FIELDS
    private GolfEventGolfer _GolfEventGolfer;

    // CONSTRUCTORS
    public GolfEventRoundGolfer() {
    }

    public GolfEventRoundGolfer(int golfEventRoundGolferId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRoundGolfer", "", ""));
            sql.append("FROM GolfEventRoundGolfer");
            sql.append("WHERE GolfEventRoundGolferID = ").append(golfEventRoundGolferId);

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

    public GolfEventRoundGolfer(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfEventRoundGolferID() {return _GolfEventRoundGolferID;}
    public Integer getGolfEventRoundID() {return _GolfEventRoundID;}
    public Integer getGolferRoundID() {return _GolferRoundID;}
    public Integer getGolfEventGroupID() {return _GolfEventGroupID;}
    public Integer getGolfEventTeamID() {return _GolfEventTeamID;}
    public Double getEarnings() {return _Earnings;}
    public GolfEventRound getGolfEventRound() {return _GolfEventRound;}
    public GolferRound getGolferRound() {return _GolferRound;}
    public GolfEventGroup getGolfEventGroup() {return _GolfEventGroup;}
    public GolfEventTeam getGolfEventTeam() {return _GolfEventTeam;}
    public GolfEventGolfer getGolfEventGolfer() {return _GolfEventGolfer;}

    // SETTERS
    public void setGolfEventRoundGolferID(Integer GolfEventRoundGolferID) {_GolfEventRoundGolferID = GolfEventRoundGolferID;}
    public void setGolfEventRoundID(Integer GolfEventRoundID) {_GolfEventRoundID = GolfEventRoundID;}
    public void setGolferRoundID(Integer GolferRoundID) {_GolferRoundID = GolferRoundID;}
    public void setGolfEventGroupID(Integer GolfEventGroupID) {_GolfEventGroupID = GolfEventGroupID;}
    public void setGolfEventTeamID(Integer GolfEventTeamID) {_GolfEventTeamID = GolfEventTeamID;}
    public void setEarnings(Double Earnings) {_Earnings = Earnings;}
    public void setGolfEventRound(GolfEventRound GolfEventRound) {_GolfEventRound = GolfEventRound;}
    public void setGolferRound(GolferRound GolferRound) {_GolferRound = GolferRound;}
    public void setGolfEventGroup(GolfEventGroup GolfEventGroup) {_GolfEventGroup = GolfEventGroup;}
    public void setGolfEventTeam(GolfEventTeam GolfEventTeam) {_GolfEventTeam = GolfEventTeam;}
    public void setGolfEventGolfer(GolfEventGolfer GolfEventGolfer) {_GolfEventGolfer = GolfEventGolfer;}

    // PUBLIC METHODS

    public static List<GolfEventRoundGolfer> GetGolfEventRoundGolfers(int golfEventRoundId, DisplayOption displayOption) {

        List<GolfEventRoundGolfer> golfers = new ArrayList<GolfEventRoundGolfer>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();
        String selectStatement = "";
        String joinStatement = "";
        String orderByColumn = "";

        try {

            switch (displayOption) {
                case GROUPS:
                    selectStatement = ", "+_Cols.getColumnList("GolfEventGroup", "grp.", "GolfEventGroup$");
                    joinStatement = "LEFT JOIN GolfEventGroup grp ON grp.GolfEventGroupID = erg.GolfEventGroupID";
                    orderByColumn = "grp.GroupName, ";
                    break;
                case TEAMS:
                    selectStatement = ", "+_Cols.getColumnList("GolfEventTeam", "et.", "GolfEventTeam$");
                    joinStatement = "LEFT JOIN GolfEventTeam et ON et.GolfEventTeamID = erg.GolfEventTeamID";
                    orderByColumn = "et.TeamName, ";
                    break;
            }

            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRoundGolfer", "erg.", ""));
            sql.append(_Cols.getColumnList("GolferRound", "r.", "GolferRound$"));
            sql.append(_Cols.getColumnList("GolfEventRound", "er.", "GolfEventRound$"));
            sql.append(_Cols.getColumnList("GolfEvent", "e.", "GolfEvent$"));
            sql.append(_Cols.getColumnList("GolfEventGolfer", "eg.", "GolfEventGolfer$"));
            sql.append(_Cols.getColumnList("Golfer", "g.", "Golfer$"));
            sql.append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
            sql.append(selectStatement);
            sql.append("FROM GolfEventRoundGolfer erg ");
            sql.append("JOIN GolferRound r ON r.GolferRoundID = erg.GolferRoundID ");
            sql.append("JOIN GolfEventRound er ON er.GolfEventRoundID = erg.GolfEventRoundID ");
            sql.append("JOIN GolfEvent e ON e.GolfEventID = er.GolfEventID ");
            sql.append("JOIN GolfEventGolfer eg ON eg.GolfEventID = er.GolfEventID AND eg.GolferID = r.GolferID ");
            sql.append("JOIN Golfer g ON g.GolferID = r.GolferID ");
            sql.append("JOIN FSUser u ON u.FSUserID = g.FSUserID ");
            sql.append(joinStatement).append(" ");
            sql.append("WHERE erg.GolfEventRoundID = ").append(golfEventRoundId).append(" ");
            sql.append("ORDER BY ").append(orderByColumn).append("eg.Rank");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                GolfEventRoundGolfer objGolfer = new GolfEventRoundGolfer(crs,"");
                golfers.add(objGolfer);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return golfers;
    }

    public static List<GolfEventRoundGolfer> GetGolfGroups(int golfEventRoundId) {
        List<GolfEventRoundGolfer> groups = new ArrayList<GolfEventRoundGolfer>();
        CachedRowSet crs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRoundGolfer", "erg.", ""));
            sql.append(_Cols.getColumnList("GolfEventGroup", "grp.", "GolfEventGroup$"));
            sql.append(_Cols.getColumnList("GolferRound", "r.", "GolferRound$"));
            sql.append(_Cols.getColumnList("GolfEventRound", "er.", "GolfEventRound$"));
            sql.append(_Cols.getColumnList("GolfEvent", "e.", "GolfEvent$"));
            sql.append(_Cols.getColumnList("GolfEventGolfer", "eg.", "GolfEventGolfer$"));
            sql.append(_Cols.getColumnList("Golfer", "g.", "Golfer$"));
            sql.append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
            sql.append("FROM GolfEventRoundGolfer erg ");
            sql.append("JOIN GolfEventGroup grp ON grp.GolfEventGroupID = erg.GolfEventGroupID ");
            sql.append("JOIN GolferRound r ON r.GolferRoundID = erg.GolferRoundID ");
            sql.append("JOIN GolfEventRound er ON er.GolfEventRoundID = erg.GolfEventRoundID ");
            sql.append("JOIN GolfEvent e ON e.GolfEventID = er.GolfEventID ");
            sql.append("JOIN GolfEventGolfer eg ON eg.GolfEventID = er.GolfEventID AND eg.GolferID = r.GolferID ");
            sql.append("JOIN Golfer g ON g.GolferID = r.GolferID ");
            sql.append("JOIN FSUser u ON u.FSUserID = g.FSUserID ");
            sql.append("WHERE erg.GolfEventRoundID = ").append(golfEventRoundId).append(" ");
            sql.append("ORDER BY grp.GroupName, eg.Rank");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                GolfEventRoundGolfer objGolfer = new GolfEventRoundGolfer(crs,"");
                groups.add(objGolfer);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return groups;
    }

    public static List<GolfEventRoundGolfer> GetGolfTeams(int golfEventRoundId) {
        List<GolfEventRoundGolfer> teams = new ArrayList<GolfEventRoundGolfer>();
        CachedRowSet crs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRoundGolfer", "erg.", ""));
            sql.append(_Cols.getColumnList("GolfEventTeam", "et.", "GolfEventTeam$"));
            sql.append(_Cols.getColumnList("GolferRound", "r.", "GolferRound$"));
            sql.append(_Cols.getColumnList("GolfEventRound", "er.", "GolfEventRound$"));
            sql.append(_Cols.getColumnList("GolfEvent", "e.", "GolfEvent$"));
            sql.append(_Cols.getColumnList("GolfEventGolfer", "eg.", "GolfEventGolfer$"));
            sql.append(_Cols.getColumnList("Golfer", "g.", "Golfer$"));
            sql.append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
            sql.append("FROM GolfEventRoundGolfer erg ");
            sql.append("JOIN GolfEventTeam et ON et.GolfEventTeamID = erg.GolfEventTeamID ");
            sql.append("JOIN GolferRound r ON r.GolferRoundID = erg.GolferRoundID ");
            sql.append("JOIN GolfEventRound er ON er.GolfEventRoundID = erg.GolfEventRoundID ");
            sql.append("JOIN GolfEvent e ON e.GolfEventID = er.GolfEventID ");
            sql.append("JOIN GolfEventGolfer eg ON eg.GolfEventID = er.GolfEventID AND eg.GolferID = r.GolferID ");
            sql.append("JOIN Golfer g ON g.GolferID = r.GolferID ");
            sql.append("JOIN FSUser u ON u.FSUserID = g.FSUserID ");
            sql.append("WHERE erg.GolfEventRoundID = ").append(golfEventRoundId).append(" ");
            sql.append("ORDER BY et.TeamName, eg.Rank");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                GolfEventRoundGolfer objGolfer = new GolfEventRoundGolfer(crs,"");
                teams.add(objGolfer);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return teams;
    }

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEventRoundGolfer", "GolfEventRoundGolferID", getGolfEventRoundGolferID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundGolferID")) { setGolfEventRoundGolferID(crs.getInt(prefix + "GolfEventRoundGolferID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundID")) { setGolfEventRoundID(crs.getInt(prefix + "GolfEventRoundID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolferRoundID")) { setGolferRoundID(crs.getInt(prefix + "GolferRoundID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfEventGroupID")) { setGolfEventGroupID(crs.getInt(prefix + "GolfEventGroupID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfEventTeamID")) { setGolfEventTeamID(crs.getInt(prefix + "GolfEventTeamID")); }
            if (FSUtils.fieldExists(crs, prefix, "Earnings")) { setEarnings(crs.getDouble(prefix + "Earnings")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolfEventRound$", "GolfEventRoundID")) { setGolfEventRound(new GolfEventRound(crs, "GolfEventRound$")); }
            if (FSUtils.fieldExists(crs, "GolferRound$", "GolferRoundID")) { setGolferRound(new GolferRound(crs, "GolferRound$")); }
            if (FSUtils.fieldExists(crs, "GolfEventGroup$", "GolfEventGroupID")) { setGolfEventGroup(new GolfEventGroup(crs, "GolfEventGroup$")); }
            if (FSUtils.fieldExists(crs, "GolfEventTeam$", "GolfEventTeamID")) { setGolfEventTeam(new GolfEventTeam(crs, "GolfEventTeam$")); }

            // ADDITIONAL FIELDS
            if (FSUtils.fieldExists(crs, "GolfEventGolfer$", "GolfEventID")) {
                setGolfEventGolfer(new GolfEventGolfer(crs, "GolfEventGolfer$"));
            }
            else if (FSUtils.fieldExists(crs, "GolfEvent$", "GolfEventID") && FSUtils.fieldExists(crs, "Golfer$", "GolferID")) {
                setGolfEventGolfer(new GolfEventGolfer(crs.getInt("GolfEvent$GolfEventID"), crs.getInt("Golfer$GolferID")));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfEventRoundGolfer ");
        sql.append("(GolfEventRoundID, GolferRoundID, GolfEventGroupID, GolfEventTeamID, Earnings) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventRoundID()));
        sql.append(FSUtils.InsertDBFieldValue(getGolferRoundID()));
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventGroupID()));
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getEarnings()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfEventRoundGolfer SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolfEventRoundID", getGolfEventRoundID()));
        sql.append(FSUtils.UpdateDBFieldValue("GolferRoundID", getGolferRoundID()));
        sql.append(FSUtils.UpdateDBFieldValue("GolfEventGroupID", getGolfEventGroupID()));
        sql.append(FSUtils.UpdateDBFieldValue("GolfEventTeamID", getGolfEventTeamID()));
        sql.append(FSUtils.UpdateDBFieldValue("Earnings", getEarnings()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventRoundGolferID = ").append(getGolfEventRoundGolferID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
