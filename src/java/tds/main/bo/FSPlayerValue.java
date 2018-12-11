package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.text.DecimalFormat;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSPlayerValue implements Serializable {
    
    // DB FIELDS
    private int _PlayerID;
    private int _FSSeasonWeekID;
    private double _Value;    
    
    // OBJECTS
    private Player _Player;
    private FSSeasonWeek _FSSeasonWeek;
    
    // ADDITIONAL FIELDS
    private Game _Game;
    private FootballStats _FootballStats;
    private FootballStats _TotalFootballStats;

    // CONSTRUCTORS
    public FSPlayerValue() {
    }
    
    public FSPlayerValue(int playerID, int fsseasonweekID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("FSPlayerValue", "pv.", "PlayerValue$"));
        sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
        sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek","fsw.", "FSSeasonWeek$"));
            //"," + _Cols.getColumnList("FootballStats","st.", "FootballStats$") +
            //"," + _Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$") +
        sql.append(" FROM FSPlayerValue pv ");
        sql.append(" INNER JOIN Player p ON p.PlayerID = pv.PlayerID ");
        sql.append(" INNER JOIN Team t ON t.TeamID = p.TeamID ");
        sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID ");
        sql.append(" LEFT JOIN Country c ON c.CountryID = p.CountryID ");
        sql.append(" INNER JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = pv.FSSeasonWeekID ");
            //" left join " + "FootballStats st on st.StatsPlayerID = p.StatsPlayerID and st.SeasonWeekID = fsw.SeasonWeekID " +
            //" left join " + "FootballStats tst on tst.StatsPlayerID = p.StatsPlayerID and tst.SeasonWeekID = 0 " +
        sql.append(" WHERE pv.PlayerID = ").append(playerID);
        sql.append(" AND pv.FSSeasonWeekID = ").append(fsseasonweekID);

        CachedRowSet crs = null;
        Connection con = null;
        try {
            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "PlayerValue$");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }
    }
    
    public FSPlayerValue(CachedRowSet fields) {
        initFromCRS(fields, "PlayerValue$");
    }

    public FSPlayerValue(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }
    
    // GETTERS
    public int getPlayerID() {return _PlayerID;}
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public double getValue() {return _Value;}    
    public Player getPlayer() {if (_Player == null && _PlayerID > 0) {_Player = Player.getInstance(_PlayerID);}return _Player;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}
    public Game getGame() {return _Game;}
    public FootballStats getFootballStats() {return _FootballStats;}
    public FootballStats getTotalFootballStats() {return _TotalFootballStats;}
    
    // SETTERS
    public void setPlayerID(int PlayerID) {_PlayerID = PlayerID;}
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setValue(double Value) {_Value = Value;}
    public void setPlayer(Player Player) {_Player = Player;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}
    public void setGame(Game Game) {_Game = Game;}
    public void setFootballStats(FootballStats FootballStats) {_FootballStats = FootballStats;}
    public void setTotalFootballStats(FootballStats TotalFootballStats) {_TotalFootballStats = TotalFootballStats;}
    
    // PUBLIC METHODS

    public static void setPlayerValue(int playerid, int fsseasonweekid, double value) throws Exception {

        try {
            
            StringBuilder sql = new StringBuilder();

            sql.append("DELETE FROM FSPlayerValue ");
            sql.append(" WHERE PlayerID = ").append(playerid);
            sql.append(" AND FSSeasonWeekID = ").append(fsseasonweekid);

            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
            
            sql = new StringBuilder();
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            value = Double.valueOf(twoDForm.format(value));

            sql.append("INSERT INTO FSPlayerValue (PlayerID,FSSeasonWeekID,Value) ");
            sql.append("VALUES (").append(playerid);
            sql.append(",").append(fsseasonweekid);
            sql.append(",").append(value).append(")");

            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
            
        } catch (Exception e) {
            
        }

    }
    
    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        
        try {
            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "PlayerID")) {
                setPlayerID(crs.getInt(prefix + "PlayerID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) {
                setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Value")) {
                setValue(crs.getDouble(prefix + "Value"));
            }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "Player$", "PlayerID")) {
                setPlayer(new Player(crs, "Player$"));
            }

            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) {
                setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$"));
                int tempSeasonWeek = _FSSeasonWeek.getSeasonWeekID();
                if (tempSeasonWeek >= Team._seasonweekid) {
                    tempSeasonWeek--;
                }
            }
            
            if (FSUtils.fieldExists(crs, "TotalFootballStats$", "StatsPlayerID")) {
                setTotalFootballStats(new FootballStats(crs, "TotalFootballStats$"));
            }

            if (FSUtils.fieldExists(crs, "Game$", "GameID")) {
                setGame(new Game(crs, "Game$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    } 
}