package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.Application;
import bglib.util.FSUtils;
import tds.data.CTDataSetDef;
import tds.util.CTReturnCode;

import sun.jdbc.rowset.CachedRowSet;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import static tds.data.CTColumnLists._Cols;
import static tds.main.bo.CTApplication._CT_DB;
import static tds.util.CTReturnCode.RC_DB_ERROR;
import static tds.util.CTReturnType.SUCCESS;

public class FSTeam implements Serializable {

    // DB FIELDS
    private Integer _FSTeamID;
    private Integer _FSLeagueID;
    private Integer _FSUserID;
    private LocalDateTime _DateCreated;
    private String _TeamName;
    private boolean _IsActive;
    private Integer _Division;
    private Integer _TeamNo;
    private Integer _ScheduleTeamNo;
    private LocalDateTime _LastAccessed;
    private String _RankDraftMode;
    private boolean _IsAlive;

    // OBJECTS
    private FSLeague _FSLeague;
    private FSUser _FSUser;

    // CONSTRUCTORS
    public FSTeam() {
    }

    public FSTeam(int teamID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSTeam", "t.", ""));
            sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSSeason", "s.", "FSSeason$"));
            sql.append(",").append(_Cols.getColumnList("FSGame", "g.", "FSGame$"));
            sql.append(",").append(_Cols.getColumnList("Sport", "sp.", "Sport$"));
            sql.append(",").append(_Cols.getColumnList("Season", "se.", "Season$"));
            sql.append(",").append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
            sql.append(",").append(_Cols.getColumnList("FSFootballSeasonDetail", "fsd.", "FSFootballSeasonDetail$"));
            sql.append(" FROM FSTeam t ");
            sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = t.FSLeagueID ");
            sql.append(" INNER JOIN FSSeason s ON s.FSSeasonID = l.FSSeasonID ");
            sql.append(" INNER JOIN FSGame g ON g.FSGameID = s.FSGameID ");
            sql.append(" INNER JOIN Sport sp ON sp.SportID = g.SportID ");
            sql.append(" INNER JOIN Season se ON se.SeasonID = s.SeasonID ");
            sql.append(" INNER JOIN FSUser u ON u.FSUserID = t.FSUserID");
            sql.append(" LEFT JOIN FSFootballSeasonDetail fsd ON fsd.FSSeasonID = s.FSSeasonID");
            sql.append(" WHERE t.FSTeamID = ").append(teamID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            InitFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public FSTeam(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public FSTeam(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getFSTeamID() {return _FSTeamID;}
    public Integer getFSLeagueID() {return _FSLeagueID;}
    public Integer getFSUserID() {return _FSUserID;}
    public LocalDateTime getDateCreated() {return _DateCreated;}
    public String getTeamName() {return _TeamName;}
    public boolean isIsActive() {return _IsActive;}
    public Integer getDivision() {return _Division;}
    public Integer getTeamNo() {return _TeamNo;}
    public Integer getScheduleTeamNo() {return _ScheduleTeamNo;}
    public LocalDateTime getLastAccessed() {return _LastAccessed;}
    public String getRankDraftMode() {return _RankDraftMode;}
    public boolean getIsAlive() {return _IsAlive;}
    public FSLeague getFSLeague() {if (_FSLeague == null && _FSLeagueID > 0) {_FSLeague = new FSLeague(_FSLeagueID);}return _FSLeague;}
    public FSUser getFSUser() {if (_FSUser == null && _FSUserID > 0) {_FSUser = new FSUser(_FSUserID);}return _FSUser;}

    // SETTERS
    public void setFSTeamID(int FSTeamID) {_FSTeamID = FSTeamID;}
    public void setFSLeagueID(int FSLeagueID) {_FSLeagueID = FSLeagueID;}
    public void setFSUserID(int FSUserID) {_FSUserID = FSUserID;}
    public void setDateCreated(LocalDateTime DateCreated) {_DateCreated = DateCreated;}
    public void setTeamName(String TeamName) {_TeamName = TeamName;}
    public void setIsActive(boolean IsActive) {_IsActive = IsActive;}
    public void setDivision(int Division) {_Division = Division;}
    public void setTeamNo(int TeamNo) {_TeamNo = TeamNo;}
    public void setScheduleTeamNo(int ScheduleTeamNo) {_ScheduleTeamNo = ScheduleTeamNo;}
    public void setLastAccessed(LocalDateTime LastAccessed) {_LastAccessed = LastAccessed;}
    public void setRankDraftMode(String RankDraftMode) {_RankDraftMode = RankDraftMode;}
    public void setIsAlive(boolean IsAlive) {_IsAlive = IsAlive;}
    public void setFSLeague(FSLeague FSLeague) {_FSLeague = FSLeague;}
    public void setFSUser(FSUser FSUser) {_FSUser = FSUser;}

    // PUBLIC METHODS

    public List<FSRoster> getRoster(int fsseasonweekID) {
        return FSRoster.getRoster(_FSTeamID,fsseasonweekID);
    }

    public List<FSRoster> getRoster(int fsseasonweekID,String activeState) {
        return FSRoster.getRoster(_FSTeamID,fsseasonweekID,activeState);
    }

    public List<FSRoster> getRoster(int fsseasonweekID,String activeState,boolean sortByStarterState) {
        return FSRoster.getRoster(_FSTeamID,fsseasonweekID,activeState,sortByStarterState);
    }

    public static int addNewTeam(int fsleagueid, int fsuserid, String teamname) throws Exception {

        String procCall = "FSTeam_lastFSTeam";

        int id = _CT_DB.insertDataSet(CTDataSetDef.INSERT_NEW_FSTEAM, procCall, fsleagueid, fsuserid, teamname,
                LocalDateTime.now());

        System.out.println("New fsteamid id : " + id);
        CTReturnCode ret = (id>0) ? new CTReturnCode(SUCCESS, id) : RC_DB_ERROR;

        if (ret.isSuccess()) {
            // send out an email or something.
        }

        return id;
    }

    public CTReturnCode insertOrUpdateRoster(int fsseasonweekID, List<FSRoster> players) throws Exception {

        try {
            int id = _CT_DB.updateDataSet(CTDataSetDef.DELETE_FSROSTER_BY_FSTEAMID_FSSEASONWEEKID, _FSTeamID, fsseasonweekID);

            for (FSRoster player : players) {
                _CT_DB.updateDataSet(CTDataSetDef.INSERT_FSROSTER, _FSTeamID, player.getPlayerID(),fsseasonweekID,
                        player.getStarterState(),player.getActiveState());
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            return RC_DB_ERROR;
        } finally {
        }

        return new CTReturnCode(SUCCESS);
    }

    public double getWeekFantasyPoints(int fsseasonweekid,int seasonid) {
        return getWeekFantasyPoints(fsseasonweekid,"s.FantasyPts",seasonid);
    }
    public double getWeekFantasyPoints(int fsseasonweekid,String fantptscolname,int seasonid) {
        double totalpts = 0;
        try {
            List<FSRoster> rosters = getRoster(fsseasonweekid);
            for (FSRoster roster : rosters) {
                StringBuilder ptssql = new StringBuilder();
                ptssql.append("select " + fantptscolname + " as totalfantasypts ");
                ptssql.append(" from FSTeam t ");
                ptssql.append(" inner join FSRoster r on t.FSTeamID=r.FSTeamID and r.FSSeasonWeekID = ").append(fsseasonweekid);
                ptssql.append(" inner join Player p on p.PlayerID=r.PlayerID ");
                ptssql.append(" inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
                ptssql.append(" inner join SeasonWeek sw on sw.SeasonWeekID = fsw.SeasonWeekID ");
//                ptssql.append(" inner join FootballStats s on s.StatsPlayerID = p.StatsPlayerID and s.SeasonWeekID = sw.SeasonWeekID ");
                ptssql.append(" inner join FootballStats s on s.PlayerID = p.PlayerID and s.SeasonWeekID = sw.SeasonWeekID ");
                ptssql.append(" where t.FSTeamID = ").append(_FSTeamID);
                ptssql.append(" and r.StarterState = 'starter' ");
                ptssql.append(" and p.PlayerID = ").append(roster.getPlayerID());

                CachedRowSet ptscrs = CTApplication._CT_QUICK_DB.executeQuery(ptssql.toString());
                double pts = 0;
                if (ptscrs.next()) {
                    pts = ptscrs.getDouble("totalfantasypts");
                }

                totalpts += pts;

                ptscrs.close();

            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
        return totalpts;
    }

    public void figureBestStarters(int fsseasonweekid, boolean includeTEasWR, int fsleagueid) throws Exception {

        // Positions
        List list = includeTEasWR ? Arrays.asList(new String[]{"1","2","3,4","5"}) : Arrays.asList(new String[]{"1","2","3","4","5"});

        CTApplication._CT_QUICK_DB.executeUpdate("update FSRoster " +
                            " set StarterState = 'bench' " +
                            " where FSTeamID = " + _FSTeamID +
                            " and FSSeasonWeekID = " + fsseasonweekid);

        // Figure best possible values.

        for (ListIterator i=list.listIterator();i.hasNext();) {
            String pos = (String)i.next();

            int fsseasonid = getFSLeague().getFSSeasonID();

            int posid = pos.startsWith("3") ? 3 : Integer.parseInt(pos);

            FSFootballRosterPositions rosterPositions = new FSFootballRosterPositions(fsseasonid, posid, fsleagueid);
            int maxstarters = rosterPositions.getMaxStart();

            StringBuilder sql = new StringBuilder();
            sql.append("  select * ");
            sql.append(" from FSRoster r ");
            sql.append(" INNER JOIN Player p on p.PlayerID = r.PlayerID ");
            sql.append(" INNER JOIN Position s on s.PositionID = p.PositionID ");
            sql.append(" INNER JOIN FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append(" INNER JOIN SeasonWeek sw on sw.SeasonWeekID = fsw.SeasonWeekID ");
//            sql.append(" LEFT JOIN FootballStats st on st.StatsPlayerID = p.StatsPlayerID and st.SeasonWeekID = sw.SeasonWeekID ");
            sql.append(" LEFT JOIN FootballStats st on st.PlayerID = p.PlayerID and st.SeasonWeekID = sw.SeasonWeekID ");
            sql.append(" where r.FSTeamID = ").append(_FSTeamID);
            sql.append(" and r.FSSeasonWeekID = ").append(fsseasonweekid);
            sql.append(" and r.ActiveState = 'active' ");
            sql.append(" and s.PositionID in (").append(pos).append(") ");
            sql.append(" order by st.FantasyPts desc");

            //System.out.println(sql);
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            int numstarters = 0;
            while (crs.next()) {
                if (numstarters < maxstarters) {
                    numstarters++;
                    int playerid = crs.getInt("PlayerID");

                    sql = new StringBuilder();
                    sql.append("update FSRoster " +
                                " set StarterState = 'starter' " +
                                " where PlayerID = " + playerid +
                                " and FSSeasonWeekID = " + fsseasonweekid +
                                " and FSTeamID = " + _FSTeamID);
                    CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
                }
            }
            crs.close();
        }

    }

    public List<FSFootballTransactionRequest> getTransactionRequests(int fsseasonweekid) {
        return FSFootballTransactionRequest.getTransactionRequests(_FSTeamID, fsseasonweekid);
    }

    public List<FSFootballTransactionRequest> getTransactionRequests(int fsseasonweekid,int processed) {
        return FSFootballTransactionRequest.getTransactionRequests(_FSTeamID, fsseasonweekid, processed);
    }

    public void updateMaxRequests(int fsseasonweekid, int num) {
        CTApplication._CT_DB.updateDataSet(CTDataSetDef.UPDATE_FSFOOTBALLMAXREQUESTS, num, getFSTeamID(), fsseasonweekid);
    }

    public int getMaxRequests(int fsseasonweekID) {
        int num = 0;
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSFootballMaxRequests", "r.", ""));
            sql.append(" FROM FSFootballMaxRequests r ");
            sql.append(" WHERE r.FSTeamID = ").append(getFSTeamID());
            sql.append(" AND r.FSSeasonWeekID = ").append(fsseasonweekID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                num = crs.getInt("MaxRequests");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return num;
    }

    public static String getFSTeamName(int fsTeamId) {
        String teamName = "";
        CachedRowSet crs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT teamName ");
            sql.append("FROM FSTeam ");
            sql.append("WHERE FSTeamID = ").append(fsTeamId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                teamName = crs.getString("teamName");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return teamName;
    }

    public static List<FSTeam> GetLeagueTeams(int fsLeagueId) {

        List<FSTeam> teams = new ArrayList<FSTeam>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSTeam", "", ""));
        sql.append("FROM FSTeam ");
        sql.append("WHERE FSLeagueID = ").append(fsLeagueId);

        // Call QueryCreator
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                teams.add(new FSTeam(crs,""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return teams;
   }

    /*public int update() {

        int retVal = 0;
        StringBuilder sql = new StringBuilder();
        try {

            if (_FSTeamID > 0) {
                // Create SQL statement
                sql.append(" UPDATE FSTeam ");
                sql.append(" SET FSLeagueID = ").append(_FSLeagueID);
                sql.append(" , FSUserID = ").append(_FSUserID);
                sql.append(" , DateCreated = ");
                if (_DateCreated != null) {
                    sql.append(" '").append(new java.sql.Timestamp(_DateCreated.getDateInMillis())).append("'");
                } else {
                    sql.append(" null");
                }
                sql.append(", TeamName = ");
                if (_TeamName != null) {
                    sql.append(" '").append(StringEscapeUtils.escapeSql(_TeamName)).append("'");
                } else {
                    sql.append(" null");
                }
                sql.append(" , IsActive = ").append(_IsActive);
                sql.append(" , Division = ").append(_Division);
                sql.append(" , TeamNo = ").append(_TeamNo);
                sql.append(" , ScheduleTeamNo = ").append(_ScheduleTeamNo);
                sql.append(" , LastAccessed = ");
                if (_LastAccessed != null) {
                    sql.append(" '").append(new java.sql.Timestamp(_LastAccessed.getDateInMillis())).append("'");
                } else {
                    sql.append(" null");
                }
                sql.append(" , RankDraftMode = ");
                if (_RankDraftMode != null) {
                    sql.append(" '").append(StringEscapeUtils.escapeSql(_RankDraftMode)).append("'");
                } else {
                    sql.append(" null");
                }
                sql.append(" , IsAlive = ").append(_IsAlive);
                sql.append(" WHERE FSTeamID = ").append(_FSTeamID);

                retVal = CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());

            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }*/

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSTeam", "FSTeamID", getFSTeamID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    public static boolean isLoveEmPlayerStillAvailable(int fsTeamId, int playerId) {
        boolean available = true;

        try {
            List<FSRoster> allPlayers = FSRoster.getRosterAllTime(fsTeamId);

            if (allPlayers.size() > 0) {
                for (FSRoster roster : allPlayers) {
                    if (playerId == roster.getPlayerID()) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
        return available;
    }
    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) { setFSTeamID(crs.getInt(prefix + "FSTeamID")); }
            if (FSUtils.fieldExists(crs, prefix, "FSLeagueID")) { setFSLeagueID(crs.getInt(prefix + "FSLeagueID")); }
            if (FSUtils.fieldExists(crs, prefix, "FSUserID")) { setFSUserID(crs.getInt(prefix + "FSUserID")); }
            if (FSUtils.fieldExists(crs, prefix, "TeamName")) { setTeamName(crs.getString(prefix + "TeamName")); }
            if (FSUtils.fieldExists(crs, prefix, "DateCreated")) { setDateCreated(LocalDateTime.now()); }
            if (FSUtils.fieldExists(crs, prefix, "LastAccessed")) { setLastAccessed(LocalDateTime.now()); }
            if (FSUtils.fieldExists(crs, prefix, "IsActive")) { setIsActive(crs.getBoolean(prefix + "IsActive")); }
            if (FSUtils.fieldExists(crs, prefix, "Division")) { setDivision(crs.getInt(prefix + "Division")); }
            if (FSUtils.fieldExists(crs, prefix, "TeamNo")) { setTeamNo(crs.getInt(prefix + "TeamNo")); }
            if (FSUtils.fieldExists(crs, prefix, "ScheduleTeamNo")) { setScheduleTeamNo(crs.getInt(prefix + "ScheduleTeamNo")); }
            if (FSUtils.fieldExists(crs, prefix, "RankDraftMode")) { setRankDraftMode(crs.getString(prefix + "RankDraftMode")); }
            if (FSUtils.fieldExists(crs, prefix, "IsAlive")) { setIsAlive(crs.getBoolean(prefix + "IsAlive")); }
            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSUser$", "FSUserID")) { setFSUser(new FSUser(crs, "FSUser$")); }
            if (FSUtils.fieldExists(crs, "FSLeague$", "FSLeagueID")) { setFSLeague(new FSLeague(crs, "FSLeague$")); }
         } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSTeam ");
        sql.append("(FSTeamID, FSLeagueID, FSUserID, TeamName, DateCreated, LastAccessed, IsActive, Division, TeamNo, ScheduleTeamNo, RankDraftMode, IsAlive)");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSLeagueID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSUserID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeamName(), true));
        sql.append(FSUtils.InsertDBFieldValue((getDateCreated() == null) ? null : Application._DATE_TIME_FORMATTER.format(getDateCreated()), true));
        sql.append(FSUtils.InsertDBFieldValue((getLastAccessed() == null) ? null : Application._DATE_TIME_FORMATTER.format(getLastAccessed()), true));
        sql.append(FSUtils.InsertDBFieldValue(isIsActive()));
        sql.append(FSUtils.InsertDBFieldValue(getDivision()));
        sql.append(FSUtils.InsertDBFieldValue(getTeamNo()));
        sql.append(FSUtils.InsertDBFieldValue(getScheduleTeamNo()));
        sql.append(FSUtils.InsertDBFieldValue(getRankDraftMode(), true));
        sql.append(FSUtils.InsertDBFieldValue(getIsAlive()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE FSTeam SET ");
        sql.append(FSUtils.UpdateDBFieldValue("FSLeagueID", getFSLeagueID()));
        sql.append(FSUtils.UpdateDBFieldValue("FSUserID", getFSUserID()));
        sql.append(FSUtils.UpdateDBFieldValue("TeamName", getTeamName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("DateCreated", (getDateCreated() == null) ? null : Application._DATE_TIME_FORMATTER.format(getDateCreated()), true));
        sql.append(FSUtils.UpdateDBFieldValue("LastAccessed", (getLastAccessed() == null) ? null : Application._DATE_TIME_FORMATTER.format(getLastAccessed()), true));
        sql.append(FSUtils.UpdateDBFieldValue("IsActive", isIsActive()));
        sql.append(FSUtils.UpdateDBFieldValue("Division", getDivision()));
        sql.append(FSUtils.UpdateDBFieldValue("TeamNo", getTeamNo()));
        sql.append(FSUtils.UpdateDBFieldValue("ScheduleTeamNo", getScheduleTeamNo()));
        sql.append(FSUtils.UpdateDBFieldValue("RankDraftMode", getRankDraftMode(), true));
        sql.append(FSUtils.UpdateDBFieldValue("IsAlive", getIsAlive()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSTeamID = ").append(getFSTeamID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
