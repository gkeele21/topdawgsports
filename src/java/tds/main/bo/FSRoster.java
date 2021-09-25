package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.util.CTReturnCode;

import java.util.ArrayList;
import java.util.List;

import static tds.data.CTColumnLists._Cols;

public class FSRoster {

    // DB FIELDS
    private int _ID;
    private int _FSTeamID;
    private int _PlayerID;
    private int _FSSeasonWeekID;
    private String _StarterState;
    private String _ActiveState;

    // OBJECTS
    private FSTeam _FSTeam;
    private Player _Player;
    private FSSeasonWeek _FSSeasonWeek;
    private PGATournamentWeekPlayer _PGATournamentWeekPlayer;

    // ADDITIONAL FIELDS
    private FootballStats _FootballStats;
    private FootballStats _TotalFootballStats;

    // CONSTRUCTORS
    public FSRoster() {
    }

    public FSRoster(int id) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSRoster", "r.", ""));
            sql.append(",").append(_Cols.getColumnList("Player", "p.", "Player$"));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "sw.", "FSSeasonWeek$"));
            sql.append(" FROM " + "FSRoster r ");
            sql.append(" INNER JOIN Player p ON p.PlayerID = r.PlayerID ");
            sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID ");
            sql.append(" INNER JOIN Team t ON t.TeamID = p.TeamID ");
            sql.append(" INNER JOIN FSSeasonWeek sw ON sw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append(" WHERE r.ID = ").append(id);
            sql.append(" ORDER BY r.ActiveState, r.StarterState DESC, p.PositionID");

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

    public FSRoster(int teamID, int fsseasonweekID, int seasonID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSRoster", "r.", ""));
            sql.append(",").append(_Cols.getColumnList("Player", "p.", "Player$"));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("FootballStats","st.","FootballStats$"));
            sql.append(",").append(_Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$"));
            sql.append(" FROM FSRoster r ");
            sql.append(" INNER JOIN Player p ON p.PlayerID = r.PlayerID ");
            sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID ");
            sql.append(" INNER JOIN Team t ON t.TeamID = p.TeamID ");
            sql.append(" INNER JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append(" INNER JOIN SeasonWeek sw ON sw.SeasonWeekID = fsw.SeasonWeekID ");
//            sql.append(" LEFT JOIN FootballStats st ON st.StatsPlayerID = p.StatsPlayerID AND st.SeasonWeekID = sw.SeasonWeekID ");
//            sql.append(" LEFT JOIN FootballStats tst ON tst.StatsPlayerID = p.StatsPlayerID AND tst.SeasonWeekID = 0 AND tst.SeasonID = ").append(seasonID);
            sql.append(" LEFT JOIN FootballStats st ON st.StatsPlayerID = p.NFLGameID AND st.SeasonWeekID = sw.SeasonWeekID ");
            sql.append(" LEFT JOIN FootballStats tst ON tst.StatsPlayerID = p.NFLGameStatsID AND tst.SeasonWeekID = 0 AND tst.SeasonID = ").append(seasonID);
            sql.append(" WHERE r.FSTeamID = ").append(teamID);
            sql.append("  AND r.FSSeasonWeekID = ").append(fsseasonweekID);
            sql.append(" ORDER BY r.ActiveState, r.StarterState DESC, p.PositionID");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                initFromCRS(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public FSRoster(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FSRoster(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS
    public int getID() {return _ID;}
    public int getFSTeamID() {return _FSTeamID;}
    public int getPlayerID() {return _PlayerID;}
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public String getStarterState() {return _StarterState;}
    public String getActiveState() {return _ActiveState;}
    public FSTeam getFSTeam() {if (_FSTeam == null && _FSTeamID > 0) {_FSTeam = new FSTeam(_FSTeamID);}return _FSTeam;}
    public Player getPlayer() {if (_Player == null && _PlayerID > 0) {_Player = Player.getInstance(_PlayerID);}return _Player;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}
    public FootballStats getFootballStats() {return _FootballStats;}
    public FootballStats getTotalFootballStats() {return _TotalFootballStats;}
    public PGATournamentWeekPlayer getPGATournamentWeekPlayer() {return _PGATournamentWeekPlayer;}

    // SETTERS
    public void setID(int ID) {_ID = ID;}
    public void setFSTeamID(int FSTeamID) {_FSTeamID = FSTeamID;}
    public void setPlayerID(int PlayerID) {_PlayerID = PlayerID;}
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setStarterState(String StarterState) {_StarterState = StarterState;}
    public void setActiveState(String ActiveState) {_ActiveState = ActiveState;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setPlayer(Player Player) {_Player = Player;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}
    public void setFootballStats(FootballStats FootballStats) {_FootballStats = FootballStats;}
    public void setTotalFootballStats(FootballStats TotalFootballStats) {_TotalFootballStats = TotalFootballStats;}
    public void setPGATournamentWeekPlayer(PGATournamentWeekPlayer player){_PGATournamentWeekPlayer = player;}

    // PUBLIC METHODS

    public static List<FSRoster> getRoster(int teamID, int fsseasonweekID) {
        String activeState = "";
        return getRoster(teamID,fsseasonweekID, activeState, true);
    }

    public static List<FSRoster> getRoster(int teamID, int fsseasonweekID, String activeState) {
        return getRoster(teamID,fsseasonweekID, activeState, true);
    }

    public static List<FSRoster> getRoster(int teamID, int fsseasonweekID, String activeState, boolean sortByStarterState) {
        List<FSRoster> roster = new ArrayList<FSRoster>();
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ").append(_Cols.getColumnList("FSRoster", "r.", ""));
            sql.append(",").append(_Cols.getColumnList("Player", "p.", "Player$"));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("FootballStats","st.","FootballStats$"));
            sql.append(",").append(_Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$"));
            sql.append(",").append(_Cols.getColumnList("Country","cnt.","Country$"));
            sql.append(" from FSRoster r ");
            sql.append(" inner join Player p on p.PlayerID = r.PlayerID ");
            sql.append(" left join Country cnt on cnt.CountryID = p.CountryID ");
            sql.append(" inner join Position ps on ps.PositionID = p.PositionID ");
            sql.append(" inner join Team t on t.TeamID = p.TeamID ");
            sql.append(" inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append(" inner join SeasonWeek sw on sw.SeasonWeekID = fsw.SeasonWeekID ");
//            sql.append(" left join FootballStats st on st.StatsPlayerID = p.StatsPlayerID and st.SeasonWeekID = sw.SeasonWeekID ");
//            sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.StatsPlayerID and tst.SeasonWeekID = 0 and tst.SeasonID = sw.SeasonID");
            sql.append(" left join FootballStats st on st.PlayerID = p.PlayerID and st.SeasonWeekID = sw.SeasonWeekID ");
            sql.append(" left join FootballStats tst on tst.PlayerID = p.PlayerID and tst.SeasonWeekID = 0 and tst.SeasonID = sw.SeasonID");
            sql.append(" where r.FSTeamID = ").append(teamID);
            sql.append(" and r.FSSeasonWeekID = ").append(fsseasonweekID);
            if (!FSUtils.isEmpty(activeState)) {
                if (activeState.indexOf(",") > 0) {
                    sql.append(" and r.ActiveState in (").append(activeState).append(") ");
                } else {
                    sql.append(" and r.ActiveState = '").append(activeState).append("' ");
                }
            }
            sql.append(" order by r.ActiveState, ");
            if (sortByStarterState)
            {
                sql.append("r.StarterState desc, ");
            }
            sql.append("p.PositionID, r.ID");

//            System.out.println("Query : " + sql.toString());
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            while (crs.next()) {
                roster.add(new FSRoster(crs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return roster;
    }

    public static FSRoster getRosterByPlayerID(int teamID, int fsseasonweekID, int playerID) {

        FSRoster roster = null;
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ").append(_Cols.getColumnList("FSRoster", "r.", "FSRoster$"));
            sql.append(",").append(_Cols.getColumnList("Player", "p.", "Player$"));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("FootballStats","st.","FootballStats$"));
            sql.append(",").append(_Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$"));
            sql.append(",").append(_Cols.getColumnList("Country","cnt.","Country$"));
            sql.append(" from FSRoster r ");
            sql.append(" inner join Player p on p.PlayerID = r.PlayerID ");
            sql.append(" left join Country cnt on cnt.CountryID = p.CountryID ");
            sql.append(" inner join Position ps on ps.PositionID = p.PositionID ");
            sql.append(" inner join Team t on t.TeamID = p.TeamID ");
            sql.append(" inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append(" inner join SeasonWeek sw on sw.SeasonWeekID = fsw.SeasonWeekID ");
//            sql.append(" left join FootballStats st on st.StatsPlayerID = p.StatsPlayerID and st.SeasonWeekID = sw.SeasonWeekID ");
//            sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.StatsPlayerID and tst.SeasonWeekID = 0 and tst.SeasonID = sw.SeasonID");
            sql.append(" left join FootballStats st on st.PlayerID = p.PlayerID and st.SeasonWeekID = sw.SeasonWeekID ");
            sql.append(" left join FootballStats tst on tst.PlayerID = p.PlayerID and tst.SeasonWeekID = 0 and tst.SeasonID = sw.SeasonID");
            sql.append(" where r.FSTeamID = ").append(teamID);
            sql.append(" and r.FSSeasonWeekID = ").append(fsseasonweekID);
            sql.append(" and r.PlayerID = ").append(playerID);
            sql.append(" order by r.ActiveState, r.StarterState desc, p.PositionID, r.ID");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs != null) {
                crs.next();
                roster = new FSRoster(crs, "FSRoster$");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return roster;
    }

    public CTReturnCode Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSRoster", "ID", getID());
        if (doesExist) { return Update(); } else { return Insert(); }
    }

    public CTReturnCode Delete() {
        int res = 0;
        StringBuilder sql = new StringBuilder();

        sql.append("DELETE FROM FSRoster ");
        sql.append("WHERE ID = ").append(getID());

        try {
            res = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return (res > 0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
    }
//    public CTReturnCode update() {
//
//        int res = 0;
//        try {
//            res = CTApplication._CT_DB.updateDataSet(CTDataSetDef.UPDATE_FSROSTER, getFSTeamID(), getPlayerID(), getFSSeasonWeekID(), getStarterState(), getActiveState(), getID());
//        }
//        catch (Exception e) {
//            CTApplication._CT_LOG.error(e);
//        }
//
//        return (res > 0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
//
//    }

    public static List<FSRoster> getRosterAllTime(int teamID) {
        List<FSRoster> roster = new ArrayList<FSRoster>();

        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ").append(_Cols.getColumnList("FSRoster", "r.", ""));
            sql.append(",").append(_Cols.getColumnList("Player", "p.", "Player$"));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("FootballStats","st.","FootballStats$"));
            sql.append(",").append(_Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$"));
            sql.append(",").append(_Cols.getColumnList("Country","cnt.","Country$"));
            sql.append(" from FSRoster r ");
            sql.append(" inner join Player p on p.PlayerID = r.PlayerID ");
            sql.append(" left join Country cnt on cnt.CountryID = p.CountryID ");
            sql.append(" inner join Position ps on ps.PositionID = p.PositionID ");
            sql.append(" inner join Team t on t.TeamID = p.TeamID ");
            sql.append(" inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append(" inner join SeasonWeek sw on sw.SeasonWeekID = fsw.SeasonWeekID ");
//            sql.append(" left join FootballStats st on st.StatsPlayerID = p.StatsPlayerID and st.SeasonWeekID = sw.SeasonWeekID ");
//            sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.StatsPlayerID and tst.SeasonWeekID = 0 and tst.SeasonID = sw.SeasonID");
            sql.append(" left join FootballStats st on st.StatsPlayerID = p.NFLGameStatsID and st.SeasonWeekID = sw.SeasonWeekID ");
            sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.NFLGameStatsID and tst.SeasonWeekID = 0 and tst.SeasonID = sw.SeasonID");
            sql.append(" where r.FSTeamID = ").append(teamID);
            sql.append(" order by r.ActiveState, ");
            sql.append("p.PositionID, r.ID");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            while (crs.next()) {
                roster.add(new FSRoster(crs));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return roster;
    }

    // PRIVATE METHODS

    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "ID")) {
                setID(crs.getInt(prefix + "ID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) {
                setFSTeamID(crs.getInt(prefix + "FSTeamID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PlayerID")) {
                setPlayerID(crs.getInt(prefix + "PlayerID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) {
                setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "StarterState")) {
                setStarterState(crs.getString(prefix + "StarterState"));
            }

            if (FSUtils.fieldExists(crs, prefix, "ActiveState")) {
                setActiveState(crs.getString(prefix + "ActiveState"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) {
                setFSTeam(new FSTeam(crs, "FSTeam$"));
            }

            if (FSUtils.fieldExists(crs, "Player$", "PlayerID")) {
                setPlayer(new Player(crs, "Player$"));
            }

            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) {
                setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$FSSeasonWeekID"));
            }

            if (FSUtils.fieldExists(crs, "FootballStats$", "PlayerID")) {
                setFootballStats(new FootballStats(crs, "FootballStats$"));
            }

            if (FSUtils.fieldExists(crs, "TotalFootballStats$", "PlayerID")) {
                setTotalFootballStats(new FootballStats(crs, "TotalFootballStats$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private CTReturnCode Insert() {
        int res = 0;
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSRoster ");
        sql.append("(FSTeamID, PlayerID, FSSeasonWeekID, StarterState, ActiveState)");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getPlayerID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getStarterState(), true));
        sql.append(FSUtils.InsertDBFieldValue(getActiveState(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            res = CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return (res > 0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
    }

    private CTReturnCode Update() {
        int res = 0;
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE FSRoster SET ");
        sql.append(FSUtils.UpdateDBFieldValue("FSTeamID", getFSTeamID()));
        sql.append(FSUtils.UpdateDBFieldValue("PlayerID", getPlayerID()));
        sql.append(FSUtils.UpdateDBFieldValue("FSSeasonWeekID", getFSSeasonWeekID()));
        sql.append(FSUtils.UpdateDBFieldValue("StarterState", getStarterState(), true));
        sql.append(FSUtils.UpdateDBFieldValue("ActiveState", getActiveState(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE ID = ").append(getID());

        try {
            res = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return (res > 0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
    }


}
