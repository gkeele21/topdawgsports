package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.util.CTReturnCode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static tds.data.CTColumnLists._Cols;


public class FSFootballMatchup {

    // DB FIELDS
    private int _FSLeagueID;
    private int _FSSeasonWeekID;
    private int _GameNo;
    private double _Team1Pts;
    private double _Team2Pts;
    private int _Winner;
    private int _Team1ID;
    private int _Team2ID;

    // OBJECTS
    private FSLeague _FSLeague;
    private FSSeasonWeek _FSSeasonWeek;
    private FSTeam _FSTeam1;
    private FSTeam _FSTeam2;

    // CONSTRUCTORS
    public FSFootballMatchup() {
    }

    public FSFootballMatchup(int matchupID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSFootballMatchup", "m.", ""));
            sql.append(",").append(_Cols.getColumnList("FSTeam", "t1.", "FSTeam1$"));
            sql.append(",").append(_Cols.getColumnList("FSTeam", "t2.", "FSTeam2$"));
            sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$"));
            sql.append(" FROM FSFootballMatchup m ");
            sql.append(" INNER JOIN FSTeam t1 ON t1.FSTeamID = m.Team1ID ");
            sql.append(" INNER JOIN FSTeam t2 ON t2.FSTeamID = m.Team2ID ");
            sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = t1.FSLeagueID ");
            sql.append(" INNER JOIN FSSeasonWeek w ON w.FSSeasonWeekID = m.FSSeasonWeekID ");
            sql.append(" WHERE m.ID = ").append(matchupID);

            crs = CTApplication._CT_QUICK_DB.executeQuery( sql.toString());
//            System.out.println("Matchup : " + sql);
            if (crs.next()) {
                initFromCRS(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public FSFootballMatchup( int fsleagueid, int fsseasonweekid, int gameNo) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSFootballMatchup", "m.", ""));
            sql.append(",").append(_Cols.getColumnList("FSTeam", "t1.", "FSTeam1$"));
            sql.append(",").append(_Cols.getColumnList("FSTeam", "t2.", "FSTeam2$"));
            sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$"));
            sql.append(" FROM FSFootballMatchup m ");
            sql.append(" INNER JOIN FSTeam t1 ON t1.FSTeamID = m.Team1ID ");
            sql.append(" INNER JOIN FSTeam t2 ON t2.FSTeamID = m.Team2ID ");
            sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = t1.FSLeagueID ");
            sql.append(" INNER JOIN FSSeasonWeek w ON w.FSSeasonWeekID = m.FSSeasonWeekID ");
            sql.append(" WHERE m.FSLeagueID = ").append(fsleagueid);
            sql.append(" AND m.FSSeasonWeekID = ").append(fsseasonweekid);
            sql.append(" AND m.GameNo = ").append(gameNo);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public FSFootballMatchup(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FSFootballMatchup(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS
    public int getFSLeagueID() {return _FSLeagueID;}
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public int getGameNo() {return _GameNo;}
    public double getTeam1Pts() {return _Team1Pts;}
    public double getTeam2Pts() {return _Team2Pts;}
    public int getWinner() {return _Winner;}
    public int getTeam1ID() {return _Team1ID;}
    public int getTeam2ID() {return _Team2ID;}
    public FSLeague getFSLeague() {if (_FSLeague == null && _FSLeagueID > 0) {_FSLeague = new FSLeague(_FSLeagueID);}return _FSLeague;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}
    public FSTeam getFSTeam1() {if (_FSTeam1 == null && _Team1ID > 0) {setFSTeam1(new FSTeam(_Team1ID));}return _FSTeam1;}
    public FSTeam getFSTeam2() {if (_FSTeam2 == null && _Team2ID > 0) {setFSTeam2(new FSTeam(_Team2ID));}return _FSTeam2;}

    // SETTERS
    public void setFSLeagueID(int FSLeagueID) {_FSLeagueID = FSLeagueID;}
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setGameNo(int GameNo) {_GameNo = GameNo;}
    public void setTeam1Pts(double Team1Pts) {_Team1Pts = Team1Pts;}
    public void setTeam2Pts(double Team2Pts) {_Team2Pts = Team2Pts;}
    public void setWinner(int Winner) {_Winner = Winner;}
    public void setTeam1ID(int Team1ID) {_Team1ID = Team1ID;}
    public void setTeam2ID(int Team2ID) {_Team2ID = Team2ID;}
    public void setFSLeague(FSLeague FSLeague) {_FSLeague = FSLeague;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}
    public void setFSTeam1(FSTeam FSTeam1) {_FSTeam1 = FSTeam1;}
    public void setFSTeam2(FSTeam FSTeam2) {_FSTeam2 = FSTeam2;}

    // PUBLIC METHODS

    public static List<FSFootballMatchup> getLeagueResults(int leagueID, int fsseasonweekID) {
        List<FSFootballMatchup> matchups = new ArrayList<FSFootballMatchup>();

        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSFootballMatchup", "m.", ""));
            sql.append(",").append(_Cols.getColumnList("FSTeam", "t1.", "FSTeam1$"));
            sql.append(",").append(_Cols.getColumnList("FSTeam", "t2.", "FSTeam2$"));
            sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$"));
            sql.append(" FROM  FSFootballMatchup m ");
            sql.append(" INNER JOIN FSTeam t1 ON t1.FSTeamID = m.Team1ID ");
            sql.append(" INNER JOIN FSTeam t2 ON t2.FSTeamID = m.Team2ID ");
            sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = t1.FSLeagueID ");
            sql.append(" INNER JOIN FSSeasonWeek w ON w.FSSeasonWeekID = m.FSSeasonWeekID ");
            sql.append(" WHERE m.FSLeagueID = ").append(leagueID).append(" and m.FSSeasonWeekID = ").append(fsseasonweekID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            while (crs.next()) {
                FSFootballMatchup match = new FSFootballMatchup(crs);
                matchups.add(match);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return matchups;
    }

    public CTReturnCode update() {
        int res = 0;
        try {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            setTeam1Pts((double) Double.valueOf(twoDForm.format(getTeam1Pts())));
            setTeam2Pts((double) Double.valueOf(twoDForm.format(getTeam2Pts())));

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE FSFootballMatchup ");
            sql.append(" SET Team1Pts = ").append(getTeam1Pts()).append(", Team2Pts = ").append(getTeam2Pts()).append(", Winner = " + getWinner());
            sql.append(" WHERE FSLeagueID = ").append(getFSLeagueID()).append(" AND FSSeasonWeekID = ").append(getFSSeasonWeekID()).append(" AND GameNo = ").append(getGameNo());

            res = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        }
        catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return (res>0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
    }

    // PRIVATE METHODS

    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSLeagueID")) {
                setFSLeagueID(crs.getInt(prefix + "FSLeagueID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) {
                setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "GameNo")) {
                setGameNo(crs.getInt(prefix + "GameNo"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Team1ID")) {
                setTeam1ID(crs.getInt(prefix + "Team1ID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Team1Pts")) {
                setTeam1Pts(crs.getDouble(prefix + "Team1Pts"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Team2ID")) {
                setTeam2ID(crs.getInt(prefix + "Team2ID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Team2Pts")) {
                setTeam2Pts(crs.getDouble(prefix + "Team2Pts"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Winner")) {
                setWinner(crs.getInt(prefix + "Winner"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSLeague$", "FSLeagueID")) {
                setFSLeague(new FSLeague(crs, "FSLeague$"));
            }

            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) {
                setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$"));
            }

            if (FSUtils.fieldExists(crs, "FSTeam1$", "FSTeamID")) {
                setFSTeam1(new FSTeam(crs, "FSTeam1$"));
            }

            if (FSUtils.fieldExists(crs, "FSTeam2$", "FSTeamID")) {
                setFSTeam2(new FSTeam(crs, "FSTeam2$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
