package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.util.CTReturnCode;

import java.io.Serializable;
import java.sql.Connection;
import java.util.*;

import static tds.data.CTColumnLists._Cols;
import static tds.main.bo.CTApplication._CT_LOG;

public class Player implements Serializable {

    public static Map<Integer,Player> getPlayerCache() {
        return _PlayerCache;
    }

    public static void setPlayerCache(Map<Integer,Player> aPlayerCache) {
        _PlayerCache = aPlayerCache;
    }

    // DB FIELDS
    private int _PlayerID;
    private int _TeamID;
    private int _PositionID;
    private String _FirstName;
    private String _LastName;
    private boolean _IsActive;
    private int _StatsPlayerID;
    private String _LiveStatsName;
    private int _CountryID;
    private String _QuickStatsName;
    private String _NFLGameStatsID;
    private int _YearsPro;
    private int _StatsPlayerID2;

    // OBJECTS
    private Team _Team;
    private Position _Position;
    private Country _Country;

    // ADDITIONAL FIELDS
    private PlayerInjury _PlayerInjury;
    private static Map<Integer,Player> _PlayerCache = new HashMap<Integer,Player>();
    /// The Integer Key for the following map represents an FSSeasonWeekID
    private Map<Integer,FSPlayerValue> _PlayerValueCache = new HashMap<Integer,FSPlayerValue>();
    /// The Integer Key for the following map represents a SeasonWeekID
    private Map<Integer,FootballStats> _WeeklyStats = new TreeMap<Integer,FootballStats>();

    static {
        try {
            //buildPlayerCache();
        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    // CONSTRUCTORS
    public Player() {

    }

    public Player(int playerID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("Player", "p.", ""));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
            sql.append(" FROM Player p ");
            sql.append(" INNER JOIN Team t ON t.TeamID = p.TeamID ");
            sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID ");
            sql.append(" LEFT JOIN Country c ON c.CountryID = p.CountryID ");
            sql.append(" WHERE p.PlayerID = ").append(playerID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            initFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }

    public Player(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public Player(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
        _PlayerCache.put(getPlayerID(), this);
    }

    // GETTERS
    public int getPlayerID() {return _PlayerID;}
    public int getTeamID() {return _TeamID;}
    public int getPositionID() {return _PositionID;}
    public String getFirstName() {return _FirstName;}
    public String getLastName() {return _LastName;}
    public boolean isIsActive() {return _IsActive;}
    public int getStatsPlayerID() {return _StatsPlayerID;}
    public String getLiveStatsName() {return _LiveStatsName;}
    public int getCountryID() {return _CountryID;}
    public String getQuickStatsName() {return _QuickStatsName;}
    public String getNFLGameStatsID() {return _NFLGameStatsID;}
    public int getYearsPro() {return _YearsPro;}
    public int getStatsPlayerID2() {return _StatsPlayerID2;}

    public String getFullName() {return getFirstName() + " " + getLastName();}
    public Team getTeam() {if (_Team == null && _TeamID > 0) {_Team = new Team(_TeamID);}return _Team;}
    public Position getPosition() {if (_Position == null && _PositionID > 0) {_Position= new Position(_PositionID);}return _Position;}
    public Country getCountry() {if (_Country == null && _CountryID > 0) {_Country = new Country(_CountryID);}return _Country;}
    public Map<Integer,FSPlayerValue> getPlayerValueCache() {return _PlayerValueCache;}
    public Map<Integer,FootballStats> getWeeklyStats() {return _WeeklyStats;}

    // SETTERS
    public void setPlayerID(int PlayerID) {_PlayerID = PlayerID;}
    public void setTeamID(int TeamID) {_TeamID = TeamID;}
    public void setTeam(Team Team) {_Team = Team;}
    public void setPositionID(int PositionID) {_PositionID = PositionID;}
    public void setPosition(Position Position) {_Position = Position;}
    public void setFirstName(String FirstName) {_FirstName = FirstName;}
    public void setLastName(String LastName) {_LastName = LastName;}
    public void setIsActive(boolean IsActive) {_IsActive = IsActive;}
    public void setStatsPlayerID(int StatsPlayerID) {_StatsPlayerID = StatsPlayerID;}
    public void setLiveStatsName(String LiveStatsName) {_LiveStatsName = LiveStatsName;}
    public void setCountryID(int CountryID) {_CountryID = CountryID;}
    public void setCountry(Country Country) {_Country = Country;}
    public void setQuickStatsName(String QuickStatsName) {_QuickStatsName = QuickStatsName;}
    public void setNFLGameStatsID(String NFLGameStatsID) {_NFLGameStatsID = NFLGameStatsID;}
    public void setYearsPro(int yearsPro) {_YearsPro = yearsPro;}
    public void setStatsPlayerID2(int StatsPlayerID) {_StatsPlayerID2 = StatsPlayerID;}

    public void setPlayerValueCache(Map<Integer,FSPlayerValue> PlayerValueCache) {_PlayerValueCache = PlayerValueCache;}
    public void setWeeklyStats(Map<Integer,FootballStats> WeeklyStats) {_WeeklyStats = WeeklyStats;}

    // PUBLIC METHODS

    public static Player createFromStatsID(String statsID) {
        Player player = null;
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("Player", "p.", ""));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
            sql.append(" FROM Player p ");
            sql.append(" INNER JOIN Team t ON t.TeamID = p.TeamID ");
            sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID ");
            sql.append(" LEFT JOIN Country c ON c.CountryID = p.CountryID ");
            sql.append(" WHERE p.NFLGameStatsID = ").append(statsID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            player.initFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return player;
    }

    public static Player createFromStatsID2(String statsID) {
        Player player = null;
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("Player", "p.", ""));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
            sql.append(" FROM Player p ");
            sql.append(" INNER JOIN Team t ON t.TeamID = p.TeamID ");
            sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID ");
            sql.append(" LEFT JOIN Country c ON c.CountryID = p.CountryID ");
            sql.append(" WHERE p.StatsPlayerID2 = ").append(statsID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            player.initFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return player;

    }


    public static Player getInstance(int playerID) {

        Player player = getPlayerCache().get(playerID);
        if (player == null) {

            CachedRowSet crs = null;
            try {
                StringBuilder sql = new StringBuilder();
                sql.append(" SELECT ").append(_Cols.getColumnList("Player", "p.", ""));
                sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
                sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
                sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
                sql.append(",").append(_Cols.getColumnList("Sport", "s.", "Sport$"));
                sql.append(" FROM Player p ");
                sql.append(" INNER JOIN Team t ON t.TeamID = p.TeamID ");
                sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID ");
                sql.append(" INNER JOIN Sport s ON s.SportID = t.SportID ");
                sql.append(" LEFT JOIN Country c ON c.CountryID = p.CountryID ");
                sql.append(" WHERE p.PlayerID = ").append(playerID);

                crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
                if (crs.next()) {
                    player = new Player(crs);
                    getPlayerCache().put(player.getPlayerID(), player);
                }

            } catch (Exception e) {
                CTApplication._CT_LOG.error(e);
            } finally {
                JDBCDatabase.closeCRS(crs);
            }
        }

        return player;
    }

    public FSPlayerValue getFSPlayerValue(int fsseasonweekid) {

        FSPlayerValue playerValue = null;
        if (getPlayerValueCache() == null) {
            setPlayerValueCache(new HashMap<Integer,FSPlayerValue>());
        }
        if (getPlayerValueCache().containsKey(fsseasonweekid)) {
            playerValue = getPlayerValueCache().get(fsseasonweekid);
        } else {
            playerValue = new FSPlayerValue(getPlayerID(),fsseasonweekid);
            if (playerValue != null) {
                getPlayerValueCache().put(fsseasonweekid,playerValue);
            }
        }

        return playerValue;
    }

    public static List<FSPlayerValue> getPlayerValues(FSSeasonWeek fsseasonweek, int posID, String orderby, List<FSPlayerValue> exceptPlayers) throws Exception {
        List<FSPlayerValue> players = new ArrayList<FSPlayerValue>();
        int fsseasonweekId = fsseasonweek.getFSSeasonWeekID();
        int seasonid = fsseasonweek.getFSSeason().getSeasonID();
        int seasonweekid = fsseasonweek.getSeasonWeekID();

        if (orderby == null || orderby.equals("")) {
            orderby = " pv.Value desc";
        }
        String exceptStr = "";
        if (exceptPlayers!=null && exceptPlayers.size()>0) {
            for (FSPlayerValue playerValue : exceptPlayers) {
                exceptStr += String.valueOf(playerValue.getPlayerID()) + ",";
            }
            exceptStr = exceptStr.substring(0,exceptStr.length()-1 );
        } else {
            exceptStr = "-1";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(_Cols.getColumnList("Player", "p.", "Player$"));
        sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
        sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
        sql.append(",").append(_Cols.getColumnList("FSPlayerValue","pv.", "PlayerValue$"));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek","fsw.", "FSSeasonWeek$"));
        sql.append(",").append(_Cols.getColumnList("SeasonWeek","sw.", "SeasonWeek$"));
        sql.append(",").append(_Cols.getColumnList("FSSeason","fss.", "FSSeason$"));
        sql.append(",").append(_Cols.getColumnList("Season","s.", "Season$"));
        sql.append(",").append(_Cols.getColumnList("FSGame","fsg.", "FSGame$"));
        sql.append(",").append(_Cols.getColumnList("Sport","sp.", "Sport$"));
        sql.append(",").append(_Cols.getColumnList("Game","g.", "Game$"));
        sql.append(",").append(_Cols.getColumnList("Team","ht.", "HomeTeam$"));
        sql.append(",").append(_Cols.getColumnList("Team","vt.", "VisitorTeam$"));
        sql.append(",").append(_Cols.getColumnList("Standings","hst.", "HomeStandings$"));
        sql.append(",").append(_Cols.getColumnList("Standings","vst.", "VisitorStandings$"));
//        sql.append(",").append(_Cols.getColumnList("FootballStats","st.", "FootballStats$"));
        sql.append(",").append(_Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$"));
        sql.append(" from Player p ");
        sql.append(" inner join Team t on t.TeamID = p.TeamID ");
        sql.append(" inner join Position ps on ps.PositionID = p.PositionID ");
        sql.append(" left join Country c on c.CountryID = p.CountryID ");
        sql.append(" inner join FSPlayerValue pv on pv.PlayerID = p.PlayerID ");
        sql.append(" inner join FSSeasonWeek fsw on fsw.FSSeasonWeekID = pv.FSSeasonWeekID ");
        sql.append(" INNER JOIN SeasonWeek sw on sw.SeasonWeekID = fsw.SeasonWeekID ");
        sql.append(" INNER JOIN FSSeason fss on fss.FSSeasonID = fsw.FSSeasonID");
        sql.append(" INNER JOIN Season s on s.SeasonID = fss.SeasonID");
        sql.append(" LEFT JOIN FSGame fsg on fsg.FSGameID = fss.FSGameID");
        sql.append(" INNER JOIN Sport sp on sp.SportID = fsg.SportID");
        sql.append(" inner join Game g on (g.HomeId = t.TeamID or g.VisitorID = t.TeamID) and g.SeasonWeekID = ").append(seasonweekid);
        sql.append(" left join Team ht on ht.TeamId = g.HomeId ");
        sql.append(" left join Team vt on vt.TeamId = g.VisitorId ");
        sql.append(" LEFT JOIN Standings hst on hst.TeamId = ht.TeamId and hst.SeasonWeekID = ").append(seasonweekid);
        sql.append(" LEFT JOIN Standings vst on vst.TeamId = vt.TeamId and vst.SeasonWeekID = ").append(seasonweekid);
//        sql.append(" left join FootballStats st on st.StatsPlayerID = p.StatsPlayerID and st.SeasonWeekID = fsw.SeasonWeekID ");
//        sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.StatsPlayerID and tst.SeasonWeekID = 0 and tst.SeasonID = ").append(seasonid);
        sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.NFLGameStatsID and tst.SeasonWeekID = 0 and tst.SeasonID = ").append(seasonid);
        sql.append(" where pv.FSSeasonWeekID = ").append(fsseasonweekId);
        sql.append(" and p.PositionID = ").append(posID);
        sql.append(" and p.PlayerID not in (").append(exceptStr).append(")");
        sql.append(" order by ").append(orderby);

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                FSPlayerValue pv = new FSPlayerValue(crs, "PlayerValue$");
                players.add(pv);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return players;
    }

    public List<FootballStats> getFootballStatsList() {
        List<FootballStats> list = new ArrayList<FootballStats>();
        if (getWeeklyStats() == null || getWeeklyStats().size() <= 1) {
            buildWeeklyStats(null);
        }

        if (getWeeklyStats() != null) {
            Iterator keyValuePairs = getWeeklyStats().entrySet().iterator();
            for (int i = 0; i < getWeeklyStats().size(); i++) {
                Map.Entry entry = (Map.Entry) keyValuePairs.next();
                Object key = entry.getKey();
                FootballStats value = (FootballStats)entry.getValue();

                list.add(value);
            }

        }
        return list;
    }

    public FootballStats getFootballStats(int seasonweekid) {
        FootballStats stats = null;
        if (getWeeklyStats() != null && getWeeklyStats().containsKey(seasonweekid)) {
            stats = getWeeklyStats().get(seasonweekid);
        } else {

            Connection con = null;
            try {
                con = CTApplication._CT_QUICK_DB.getConn();
                //System.out.println("Building Weekly Stats from scratch for Week #" + seasonweekid);
                buildWeeklyStats(con,seasonweekid);
                stats = getWeeklyStats().get(seasonweekid);
            } catch (Exception e) {
                _CT_LOG.error(e);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (Exception e) {}
                }
            }
        }

        return stats;
    }

    public FootballStats getTotalFootballStats() {
//        System.out.println("Here in getTotalFootballStats for playerid : " + this.getPlayerID());
        return getFootballStats(0);
    }

    public double getAvgFantasyPts() {
        //return 0;
        FootballStats stats = getTotalFootballStats();
        if (stats != null) {
            return stats.getAvgFantasyPts();
        } else {
            return 0;
        }
    }

    public double getAvgSalFantasyPts() {
        //return 0;
        FootballStats stats = getTotalFootballStats();
        if (stats != null) {
            return stats.getAvgSalFantasyPts();
        } else {
            return 0;
        }
    }

    public static void buildPlayerCache() throws Exception {

        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("Player", "p.", ""));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
            sql.append(" FROM Player p ");
            sql.append(" INNER JOIN Team t ON t.TeamID = p.TeamID ");
            sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID ");
            sql.append(" LEFT JOIN Country c on c.CountryID = p.CountryID ");
            sql.append(" WHERE p.IsActive = 1");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                Player player = new Player(crs);
                System.out.println("Player ID " + player.getPlayerID() + " created.");
                System.out.println("Weekly stats generated.");
                getPlayerCache().put(crs.getInt("PlayerID"), player);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public void buildWeeklyStats(Connection con) {
        CachedRowSet crs = null;
        try {
            if (getWeeklyStats() == null) {
                setWeeklyStats(new TreeMap<Integer,FootballStats>());
            }
            if (con == null) {
                con = CTApplication._CT_QUICK_DB.getConn();
            }

            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FootballStats", "st.", "FootballStats$"));
            sql.append(",").append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("Season", "ss.", "Season$"));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(" FROM FootballStats st ");
            sql.append(" LEFT JOIN SeasonWeek sw ON sw.SeasonWeekID = st.SeasonWeekID ");
            sql.append(" LEFT JOIN Season ss ON ss.SeasonID = sw.SeasonID ");
            sql.append(" LEFT JOIN Team t ON t.StatsTeamID = st.TeamID ");
//            sql.append(" WHERE st.StatsPlayerID = ").append(getStatsPlayerID());
            sql.append(" WHERE st.StatsPlayerID = '").append(getNFLGameStatsID()).append("'");
            sql.append(" AND st.SeasonID = ").append(Season._CurrentSeasonID);
            sql.append(" ORDER BY st.SeasonWeekID");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                FootballStats stats = new FootballStats(crs,"FootballStats$");
                getWeeklyStats().put(stats.getSeasonWeekID(),stats);
            }
        } catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    public void buildWeeklyStats(Connection con, int seasonWeekID) {
        CachedRowSet crs = null;
        try {
            if (getWeeklyStats() == null) {
                setWeeklyStats(new TreeMap<Integer,FootballStats>());
            }

            if (!"0".equals(getNFLGameStatsID())) {

                StringBuilder sql = new StringBuilder();
                sql.append(" SELECT ").append(_Cols.getColumnList("FootballStats", "st.", "FootballStats$"));
                sql.append(",").append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$"));
                sql.append(",").append(_Cols.getColumnList("Season", "ss.", "Season$"));
                sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
                sql.append(" FROM FootballStats st ");
                sql.append(" LEFT JOIN SeasonWeek sw ON sw.SeasonWeekID = st.SeasonWeekID ");
                sql.append(" LEFT JOIN Season ss ON ss.SeasonID = sw.SeasonID ");
                sql.append(" LEFT JOIN Team t ON t.StatsTeamID = st.TeamID ");
    //            sql.append(" WHERE st.StatsPlayerID = ").append(getStatsPlayerID());
                sql.append(" WHERE st.StatsPlayerID = '").append(getNFLGameStatsID()).append("'");
                sql.append(" AND st.SeasonID = ").append(Season._CurrentSeasonID);
                sql.append(" ORDER BY st.SeasonWeekID");

                crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
                while (crs.next()) {
                    FootballStats stats = new FootballStats(crs,"FootballStats$");
                    if (stats.getSeasonWeekID() == seasonWeekID) {
                        getWeeklyStats().put(stats.getSeasonWeekID(),stats);
                    }
                }
            }
        } catch (Exception e) {
            _CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public void buildWeeklyStats(int seasonWeekID, CachedRowSet crs, String prefix) {

        try {
            if (getWeeklyStats() == null) {
                setWeeklyStats(new TreeMap<Integer,FootballStats>());
            }
            FootballStats stats = new FootballStats(crs, prefix);
            if (stats.getSeasonWeekID() == seasonWeekID) {
                getWeeklyStats().put(stats.getSeasonWeekID(),stats);
            }
        } catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    public static List<Player> getPlayerList(Position pos,String orderby, List<Player> exceptPlayers,int seasonid, boolean includeStats) throws Exception {
        List<Player> players = new ArrayList<Player>();

        if (orderby == null || orderby.equals("")) {
            orderby = " FantasyPts desc";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(_Cols.getColumnList("Player", "p.", "Player$"));
        if (includeStats) {
            sql.append(",").append(_Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$"));
        }
        sql.append(",").append(_Cols.getColumnList("Position","ps.", "Position$"));
        sql.append(",").append(_Cols.getColumnList("Team","tm.", "Team$"));
        sql.append(",").append(_Cols.getColumnList("Country","cnt.", "Country$"));
        sql.append(" from Player p ");
        sql.append(" left join Country cnt on cnt.CountryID = p.CountryID ");
        sql.append(" inner join Position ps on ps.PositionID = p.PositionID ");
        sql.append(" inner join Team tm on tm.TeamID = p.TeamID ");
        if (includeStats) {
//            sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.StatsPlayerID and tst.SeasonWeekID = 0 and tst.SeasonId = " + seasonid);
            sql.append(" left join FootballStats tst on tst.StatsPlayerID = p.NFLGameStatsID and tst.SeasonWeekID = 0 and tst.SeasonId = " + seasonid);
        }
        sql.append(" where p.TeamID <> 0 and p.TeamID <> 132 and p.IsActive = 1 ");
        if (pos != null && pos.getPositionID() > 0) {
            sql.append(" and p.PositionID = ").append(pos.getPositionID());
        } else {
            sql.append(" and p.PositionID < 6 ");
        }

        if (exceptPlayers!=null && exceptPlayers.size()>0) {
            String exceptStr = "";
            for (Player player : exceptPlayers) {
                exceptStr += String.valueOf(player.getPlayerID()) + ",";
            }
            sql.append((sql.indexOf("where")<0) ? " where " : " and ");
            sql.append("p.PlayerID not in (").append(exceptStr.substring(0,exceptStr.length()-1 )).append(")");
        }
        sql.append(" order by ").append(orderby);

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                Player player = new Player(crs, "Player$");
                players.add(player);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return players;
    }

    public static List<Player> ReadGolfers() throws Exception {
        return ReadGolfers(null);
    }

    public static List<Player> ReadGolfers(String orderby) throws Exception {
        return ReadGolfers(orderby, false);
    }

    public static List<Player> ReadGolfers(String orderby, boolean activeOnly) throws Exception {
        List<Player> players = new ArrayList<Player>();

        if (orderby == null || orderby.equals("")) {
            orderby = " LastName asc, FirstName";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(_Cols.getColumnList("Player", "p.", "Player$"));
        sql.append(",").append(_Cols.getColumnList("Position","ps.", "Position$"));
        sql.append(",").append(_Cols.getColumnList("Team","tm.", "Team$"));
        sql.append(",").append(_Cols.getColumnList("Country","cnt.", "Country$"));
        sql.append(" from Player p ");
        sql.append(" left join Country cnt on cnt.CountryID = p.CountryID ");
        sql.append(" inner join Position ps on ps.PositionID = p.PositionID ");
        sql.append(" inner join Team tm on tm.TeamID = p.TeamID ");
        sql.append(" where p.PositionID = 12");
        if (activeOnly)
        {
            sql.append(" and p.IsActive = 1 ");
        }
        sql.append(" order by ").append(orderby);

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                Player player = new Player(crs, "Player$");
                players.add(player);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return players;
    }

    public static List<FSTeam> getTeamsWithPlayer(int playerid,int fsseasonweekid,String leagueTeams) throws Exception {
        List<FSTeam> list = new ArrayList<FSTeam>();

        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(_Cols.getColumnList("FSTeam", "t.", "FSTeam$"));
        sql.append(",").append(_Cols.getColumnList("FSUser","u.", "FSUser$"));
        sql.append(" from FSRoster r ");
        sql.append(" INNER JOIN FSTeam t on t.FSTeamID = r.FSTeamID ");
        sql.append(" INNER JOIN FSUser u on u.FSUserID = t.FSUserID ");
        sql.append(" where r.FSSeasonWeekID = ").append(fsseasonweekid);
        sql.append(" and r.PlayerID = ").append(playerid);
        if (leagueTeams != null && leagueTeams.length() > 0) {
            sql.append(" and r.FSTeamID in (").append(leagueTeams).append(")");
        }
        sql.append(" order by t.FSTeamID");

        CachedRowSet crs = null;
        try {

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            while (crs.next()) {
                FSTeam team = new FSTeam(crs,"FSTeam$");
                list.add(team);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return list;
    }

    public PlayerInjury getPlayerInjury() {
        if (_PlayerInjury == null) {
            _PlayerInjury = new PlayerInjury(getPlayerID());
        }

        return _PlayerInjury;
    }

    public void setPlayerInjury(PlayerInjury PlayerInjury) {
        this._PlayerInjury = PlayerInjury;
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            if (FSUtils.fieldExists(crs, prefix, "PlayerID")) {
                setPlayerID(crs.getInt(prefix + "PlayerID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TeamID")) {
                setTeamID(crs.getInt(prefix + "TeamID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PositionID")) {
                setPositionID(crs.getInt(prefix + "PositionID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FirstName")) {
                setFirstName(crs.getString(prefix + "FirstName"));
            }

            if (FSUtils.fieldExists(crs, prefix, "LastName")) {
                setLastName(crs.getString(prefix + "LastName"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IsActive")) {
                setIsActive(crs.getBoolean(prefix + "IsActive"));
            }

            if (FSUtils.fieldExists(crs, prefix, "StatsPlayerID")) {
                setStatsPlayerID(crs.getInt(prefix + "StatsPlayerID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "LiveStatsName")) {
                setLiveStatsName(crs.getString(prefix + "LiveStatsName"));
            }

            if (FSUtils.fieldExists(crs, prefix, "CountryID")) {
                setCountryID(crs.getInt(prefix + "CountryID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "QuickStatsName")) {
                setQuickStatsName(crs.getString(prefix + "QuickStatsName"));
            }

            if (FSUtils.fieldExists(crs, prefix, "NFLGameStatsID")) {
                setNFLGameStatsID(crs.getString(prefix + "NFLGameStatsID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "YearsPro")) {
                setYearsPro(crs.getInt(prefix + "YearsPro"));
            }

            if (FSUtils.fieldExists(crs, prefix, "StatsPlayerID2")) {
                setStatsPlayerID2(crs.getInt(prefix + "StatsPlayerID2"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "Position$", "PositionID")) {
                setPosition(new Position(crs, "Position$"));
            }

            if (FSUtils.fieldExists(crs, "Team$", "TeamID")) {
                setTeam(new Team(crs, "Team$"));
            }
            if (FSUtils.fieldExists(crs, "DropPlayerTeam$", "TeamID")) {
                setTeam(new Team(crs, "DropPlayerTeam$"));
            }
            if (FSUtils.fieldExists(crs, "PUPlayerTeam$", "TeamID")) {
                setTeam(new Team(crs, "PUPlayerTeam$"));
            }
            if (FSUtils.fieldExists(crs, "Position$", "PositionID")) {
                setPosition(new Position(crs, "Position$"));
            }
            if (FSUtils.fieldExists(crs, "DropPlayerPosition$", "PositionID")) {
                setPosition(new Position(crs, "DropPlayerPosition$"));
            }
            if (FSUtils.fieldExists(crs, "PUPlayerPosition$", "PositionID")) {
                setPosition(new Position(crs, "PUPlayerPosition$"));
            }
            if (FSUtils.fieldExists(crs, "Country$", "CountryID")) {
                setCountry(new Country(crs, "Country$"));
            }

            if (FSUtils.fieldExists(crs, "TotalFootballStats$", "StatsPlayerID")) {
                buildWeeklyStats(0, crs, "TotalFootballStats$");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    public void Save() throws Exception {
        boolean doesExist = FSUtils.DoesARecordExistInDB("Player", "PlayerID", getPlayerID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    public CTReturnCode Delete() {
        int res = 0;
        StringBuilder sql = new StringBuilder();

        sql.append("DELETE FROM Player ");
        sql.append("WHERE PlayerID = ").append(getPlayerID());

        try {
            res = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return (res > 0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
    }

    private int Insert() throws Exception {
        int newId = -1;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO Player ");
        sql.append("(TeamID, PositionID, FirstName, LastName, IsActive, StatsPlayerID, LiveStatsName, CountryID, QuickStatsName, NFLGameStatsID, YearsPro, StatsPlayerID2) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getPositionID()));
        sql.append(FSUtils.InsertDBFieldValue(getFirstName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getLastName(), true));
        sql.append(FSUtils.InsertDBFieldValue(isIsActive() ? 1 : 0, true));
        sql.append(FSUtils.InsertDBFieldValue(getStatsPlayerID()));
        sql.append(FSUtils.InsertDBFieldValue(getLiveStatsName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getCountryID()));
        sql.append(FSUtils.InsertDBFieldValue(getQuickStatsName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNFLGameStatsID(), true));
        sql.append(FSUtils.InsertDBFieldValue(getYearsPro()));
        sql.append(FSUtils.InsertDBFieldValue(getStatsPlayerID2()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            newId = CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            throw new Exception("Error Inserting new Player", e);
        }

        return newId;
    }

    private int Update() throws Exception {
        int success = -1;
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE Player SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TeamID", getTeamID()));
        sql.append(FSUtils.UpdateDBFieldValue("PositionID", getPositionID()));
        sql.append(FSUtils.UpdateDBFieldValue("FirstName", getFirstName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("LastName", getLastName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("IsActive", isIsActive() ? 1 : 0, true));
        sql.append(FSUtils.UpdateDBFieldValue("StatsPlayerID", getStatsPlayerID()));
        sql.append(FSUtils.UpdateDBFieldValue("LiveStatsName", getLiveStatsName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("CountryID", getCountryID()));
        sql.append(FSUtils.UpdateDBFieldValue("QuickStatsName", getQuickStatsName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NFLGameStatsID", getNFLGameStatsID(), true));
        sql.append(FSUtils.UpdateDBFieldValue("YearsPro", getYearsPro()));
        sql.append(FSUtils.UpdateDBFieldValue("StatsPlayerID2", getStatsPlayerID2()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE PlayerID = ").append(getPlayerID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
            success = 0;
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            throw new Exception("Error updating Player", e);
        }

        return success;
    }
}
