package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static tds.data.CTColumnLists._Cols;

public class FSGolfStandings implements Serializable {

    // DB FIELDS
    private int _FSTeamID;
    private int _FSSeasonWeekID;
    private double _WeekMoneyEarned;
    private double _TotalMoneyEarned;
    private int _Rank;
    private double _WeekWinnings;
    private double _TotalWinnings;
    private double _WeekFees;
    private double _TotalFees;
    private boolean _WeekEventEntered;
    private int _TotalEventsEntered;

    // OBJECTS
    private FSTeam _FSTeam;
    private FSSeasonWeek _FSSeasonWeek;

    // CONSTRUCTORS
    public FSGolfStandings() {
    }

    public FSGolfStandings(CachedRowSet fields) {
        InitFromCRS(fields, "FSGolfStandings$");
    }

    public FSGolfStandings(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public int getFSTeamID() {return _FSTeamID;}
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public double getWeekMoneyEarned() {return _WeekMoneyEarned;}
    public double getTotalMoneyEarned() {return _TotalMoneyEarned;}
    public int getRank() {return _Rank;}
    public double getWeekWinnings() {return _WeekWinnings;}
    public double getTotalWinnings() {return _TotalWinnings;}
    public double getWeekFees() {return _WeekFees;}
    public double getTotalFees() {return _TotalFees;}
    public boolean getWeekEventEntered() {return _WeekEventEntered;}
    public int getTotalEventsEntered() {return _TotalEventsEntered;}
    public FSTeam getFSTeam() {if (_FSTeam == null && _FSTeamID > 0) {_FSTeam = new FSTeam(_FSTeamID);}return _FSTeam;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}

    // SETTERS
    public void setFSTeamID(int fsTeamId) {_FSTeamID = fsTeamId;}
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setWeekMoneyEarned(double money) {_WeekMoneyEarned = money;}
    public void setTotalMoneyEarned(double money) {_TotalMoneyEarned = money;}
    public void setRank(int Rank) {_Rank = Rank;}
    public void setWeekWinnings(double money) {_WeekWinnings = money;}
    public void setTotalWinnings(double money) {_TotalWinnings = money;}
    public void setWeekFees(double money) {_WeekFees = money;}
    public void setTotalFees(double money) {_TotalFees = money;}
    public void setWeekEventEntered(boolean entered) {_WeekEventEntered = entered;}
    public void setTotalEventsEntered(int entered) {_TotalEventsEntered = entered;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}

    // PUBLIC METHODS

    public static List<FSGolfStandings> getLeagueStandings(int leagueID, int fsseasonweekID) {
        return getLeagueStandings(leagueID, fsseasonweekID, "");
    }

    public static List<FSGolfStandings> getLeagueStandings(int leagueID, int fsseasonweekID, String sort) {
        return getLeagueStandings(leagueID, fsseasonweekID, sort, true, false);
    }

    public static List<FSGolfStandings> getLeagueStandings(int leagueID, int fsseasonweekID, String sort, boolean requireStandingsRecords, boolean requireParticipation) {

        List<FSGolfStandings> standings = new ArrayList<FSGolfStandings>();
        if (sort == null || sort.equals("")) {
            sort = " s.Rank";
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("FSTeam", "t.", "FSTeam$"));
        sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$"));
        sql.append(",").append(_Cols.getColumnList("FSGolfStandings", "s.", "FSGolfStandings$"));
        sql.append(",").append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
        sql.append(" FROM FSTeam t ");
        sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = t.FSLeagueID ");
        sql.append(" INNER JOIN FSUser u ON u.FSUserID = t.FSUserID ");
        if (requireStandingsRecords)
        {
            sql.append(" INNER JOIN FSGolfStandings s ON s.FSTeamID = t.FSTeamID ");
            sql.append(" AND s.FSSeasonWeekID = ").append(fsseasonweekID);
            sql.append(" INNER JOIN FSSeasonWeek w ON w.FSSeasonWeekID = s.FSSeasonWeekID ");
        } else
        {
            sql.append(" LEFT JOIN FSGolfStandings s ON s.FSTeamID = t.FSTeamID ");
            sql.append(" LEFT JOIN FSSeasonWeek w ON w.FSSeasonWeekID = s.FSSeasonWeekID ");
        }
        sql.append(" WHERE l.FSLeagueID = ").append(leagueID);
        if (requireParticipation) {
            sql.append(" AND s.WeekEventEntered = 1");
        }
        sql.append(" ORDER BY ").append(sort);
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                FSGolfStandings stand = new FSGolfStandings(crs);
                standings.add(stand);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return standings;
    }

    public static List<FSGolfStandings> getLeagueStandingsInProgress(int leagueID, int fsseasonweekID) {

        List<FSGolfStandings> standings = new ArrayList<FSGolfStandings>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT sum(twp.MoneyEarned) as totalMoneyEarned, t.FSTeamID ");
        sql.append(" FROM FSTeam t ");
        sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = t.FSLeagueID ");
        sql.append(" INNER JOIN FSUser u ON u.FSUserID = t.FSUserID ");
        sql.append(" INNER JOIN PGATournamentWeek tw on tw.FSSeasonWeekID = ").append(fsseasonweekID);
        sql.append(" INNER JOIN PGATournamentWeekPlayer twp on twp.PGATournamentID = tw.PGATournamentID and twp.FSSeasonWeekID = ").append(fsseasonweekID);
        sql.append(" INNER JOIN FSRoster r on r.FSTeamID = t.FSTeamID and r.FSSeasonWeekID = tw.FSSeasonWeekID and r.PlayerID = twp.PlayerID ");
        sql.append(" WHERE l.FSLeagueID = ").append(leagueID);
        sql.append(" GROUP BY t.FSTeamID ");
        sql.append(" ORDER BY totalMoneyEarned desc ");

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                double money = crs.getDouble("totalMoneyEarned");
                int teamid = crs.getInt("FSTeamID");

                FSGolfStandings stand = new FSGolfStandings();
                stand.setFSSeasonWeekID(fsseasonweekID);
                stand.setFSTeamID(teamid);
                stand.setWeekMoneyEarned(money);

                standings.add(stand);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return standings;
    }

    public static List<FSGolfStandings> getTeamStandings(int teamID){
        return getTeamStandings(teamID, 0);
    }

    public static List<FSGolfStandings> getTeamStandings(int teamID, int fsseasonweekID) {

        List<FSGolfStandings> standings = new ArrayList<FSGolfStandings>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("FSTeam", "t.", "FSTeam$"));
        sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$"));
        sql.append(",").append(_Cols.getColumnList("FSGolfStandings", "s.", "FSGolfStandings$"));
        sql.append(",").append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
        sql.append(" FROM FSTeam t ");
        sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = t.FSLeagueID ");
        sql.append(" INNER JOIN FSUser u ON u.FSUserID = t.FSUserID ");
        sql.append(" LEFT JOIN FSGolfStandings s ON s.FSTeamID = t.FSTeamID ");
        sql.append(" LEFT JOIN FSSeasonWeek w ON w.FSSeasonWeekID = s.FSSeasonWeekID ");
        sql.append(" WHERE t.FSTeamID = ").append(teamID);
        if (fsseasonweekID > 0)
        {
            sql.append(" AND w.FSSeasonWeekID = ").append(fsseasonweekID);
        }
        sql.append(" ORDER BY w.FSSeasonWeekID");

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                FSGolfStandings stand = new FSGolfStandings(crs);
                standings.add(stand);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return standings;
    }

    public static FSGolfStandings getStandingsRecord(int fsTeamID, int fsSeasonWeekID) {
        FSGolfStandings weekStandings = null;
        List<FSGolfStandings> standings = getTeamStandings(fsTeamID, fsSeasonWeekID);

        if (!standings.isEmpty()) {
            for (FSGolfStandings tempStandings : standings) {
                int tempFSSeasonWeekID = tempStandings.getFSSeasonWeekID();

                if (tempFSSeasonWeekID == fsSeasonWeekID)
                {
                    weekStandings = tempStandings;
                    break;
                }
            }

        }

        return weekStandings;
    }

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSGolfStandings", "FSTeamID", getFSTeamID(), "FSSeasonWeekID", getFSSeasonWeekID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /*  This method populates the object from a cached row set.  */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) { setFSTeamID(crs.getInt(prefix + "FSTeamID")); }
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) { setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID")); }
            if (FSUtils.fieldExists(crs, prefix, "WeekMoneyEarned")) { setWeekMoneyEarned(crs.getDouble(prefix + "WeekMoneyEarned")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalMoneyEarned")) { setTotalMoneyEarned(crs.getDouble(prefix + "TotalMoneyEarned")); }
            if (FSUtils.fieldExists(crs, prefix, "Rank")) { setRank(crs.getInt(prefix + "Rank")); }
            if (FSUtils.fieldExists(crs, prefix, "WeekWinnings")) { setWeekWinnings(crs.getDouble(prefix + "WeekWinnings")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalWinnings")) { setTotalWinnings(crs.getDouble(prefix + "TotalWinnings")); }
            if (FSUtils.fieldExists(crs, prefix, "WeekFees")) { setWeekFees(crs.getDouble(prefix + "WeekFees")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalFees")) { setTotalFees(crs.getDouble(prefix + "TotalFees")); }
            if (FSUtils.fieldExists(crs, prefix, "WeekEventEntered")) { setWeekEventEntered(crs.getBoolean(prefix + "WeekEventEntered")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalEventsEntered")) { setTotalEventsEntered(crs.getInt(prefix + "TotalEventsEntered")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) { setFSTeam(new FSTeam(crs, "FSTeam$")); }
            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) { setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {

        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSGolfStandings ");
        sql.append("(FSTeamID, FSSeasonWeekID, WeekMoneyEarned, TotalMoneyEarned, Rank, WeekWinnings, TotalWinnings, WeekFees, TotalFees, WeekEventEntered, TotalEventsEntered) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getWeekMoneyEarned()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalMoneyEarned()));
        sql.append(FSUtils.InsertDBFieldValue(getRank()));
        sql.append(FSUtils.InsertDBFieldValue(getWeekWinnings()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalWinnings()));
        sql.append(FSUtils.InsertDBFieldValue(getWeekFees()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalFees()));
        sql.append(FSUtils.InsertDBFieldValue(getWeekEventEntered()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalEventsEntered()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE FSGolfStandings SET ");
        sql.append(FSUtils.UpdateDBFieldValue("WeekMoneyEarned", getWeekMoneyEarned()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalMoneyEarned", getTotalMoneyEarned()));
        sql.append(FSUtils.UpdateDBFieldValue("Rank", getRank()));
        sql.append(FSUtils.UpdateDBFieldValue("WeekWinnings", getWeekWinnings()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalWinnings", getTotalWinnings()));
        sql.append(FSUtils.UpdateDBFieldValue("WeekFees", getWeekFees()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalFees", getTotalFees()));
        sql.append(FSUtils.UpdateDBFieldValue("WeekEventEntered", getWeekEventEntered()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalEventsEntered", getTotalEventsEntered()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSTeamID = ").append(getFSTeamID()).append(" AND FSSeasonWeekID = ").append(getFSSeasonWeekID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
