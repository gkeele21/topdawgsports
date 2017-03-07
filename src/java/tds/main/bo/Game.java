package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.AuDate;
import bglib.util.BGConstants;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class Game implements Serializable {
    
    public static final Game BYE_WEEK;

    static {
        BYE_WEEK = new Game();
        BYE_WEEK._GameDate = new AuDate(0);
        BYE_WEEK._IsByeWeek = true;
    }

    // DB FIELDS
    private Integer _GameID;
    private Integer _SeasonWeekID;    
    private Integer _VisitorID;    
    private Integer _HomeID;    
    private AuDate _GameDate;
    private Integer _WinnerID;
    private Integer _VisitorPts;
    private Integer _HomePts;
    private Integer _FavoredID;
    private double _Spread;    
    private String _GameInfo;
    private Integer _NumOTs;
    
    // OBJECTS
    private SeasonWeek _SeasonWeek;
    private Team _Visitor;
    private Team _Home;
    
    // ADDITIONAL FIELDS
    private boolean _IsByeWeek;
    
    // CONSTRUCTORS
    public Game() {
    }

    public Game(int gameID) {
        this(null, gameID);
    }

    public Game(Connection con, int gameID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Game", "g.", "")).append(", ");
            sql.append(_Cols.getColumnList("Team", "vt.", "VisitorTeam$")).append(", ");
            sql.append(_Cols.getColumnList("Team", "ht.", "HomeTeam$")).append(", ");
            sql.append(_Cols.getColumnList("SeasonWeek", "w.", "SeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("Season", "s.", "Season$"));
            sql.append("FROM Game g ");
            sql.append("JOIN SeasonWeek w ON w.SeasonWeekID = g.SeasonWeekID ");
            sql.append("JOIN Season s ON s.SeasonID = w.SeasonID ");
            sql.append("JOIN Team vt ON vt.TeamID = g.VisitorID ");
            sql.append("JOIN Team ht ON ht.TeamID = g.HomeID ");
            sql.append("WHERE g.GameID = ").append(gameID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                InitFromCRS(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }
    
    public Game(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public Game(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }
    
    // GETTERS
    public Integer getGameID() {return _GameID;}
    public Integer getSeasonWeekID() {return _SeasonWeekID;}    
    public Integer getVisitorID() {return _VisitorID;}    
    public Integer getHomeID() {return _HomeID;}    
    public AuDate getGameDate() {return _GameDate;}
    public Integer getWinnerID() {return _WinnerID;}
    public Team getWinner() {return Team.getInstance(getWinnerID());}
    public Integer getVisitorPts() {return _VisitorPts;}
    public Integer getHomePts() {return _HomePts;}
    public Integer getFavoredID() {return _FavoredID;}
    public double getSpread() {return _Spread;}
    public boolean isIsByeWeek() {return _IsByeWeek;}
    public String getGameInfo() {return _GameInfo;}
    public Integer getNumOTs() {return _NumOTs;}
    public SeasonWeek getSeasonWeek() {if (_SeasonWeek == null && _SeasonWeekID > 0) {_SeasonWeek = new SeasonWeek(_SeasonWeekID);}return _SeasonWeek;}
    public Team getVisitor() {if (_Visitor == null && _VisitorID > 0) {_Visitor = new Team(_VisitorID);}return _Visitor;}    
    public Team getHome() {if (_Home == null && _HomeID > 0) {_Home = new Team(_HomeID);}return _Home;}    

    // SETTERS
    public void setGameID(Integer GameID) {_GameID = GameID;}
    public void setSeasonWeekID(Integer SeasonWeekID) {_SeasonWeekID = SeasonWeekID;}
    public void setVisitorID(Integer VisitorID) {_VisitorID = VisitorID;}
    public void setHomeID(Integer HomeID) {_HomeID = HomeID;}
    public void setGameDate(AuDate GameDate) {_GameDate = GameDate;}
    public void setWinnerID(Integer WinnerID) {_WinnerID = WinnerID;}
    public void setVisitorPts(Integer VisitorPts) {_VisitorPts = VisitorPts;}
    public void setHomePts(Integer HomePts) {_HomePts = HomePts;}
    public void setFavoredID(Integer FavoredID) {_FavoredID = FavoredID;}
    public void setSpread(double Spread) {_Spread = Spread;}
    public void setGameInfo(String GameInfo) {_GameInfo = GameInfo;}
    public void setNumOTs(Integer NumOTs) {_NumOTs = NumOTs;}
    public void setSeasonWeek(SeasonWeek SeasonWeek) {_SeasonWeek = SeasonWeek;}
    public void setVisitor(Team Visitor) {_Visitor = Visitor;}
    public void setHome(Team Home) {_Home = Home;}
    public void setIsByeWeek(boolean IsByeWeek) {_IsByeWeek = IsByeWeek;}
    
    // PUBLIC METHODS
    
    public static Game getInstance(CachedRowSet crs) throws Exception {
        if (crs.getInt("VisitorID")==0 || crs.getInt("HomeID")==0) {
            return BYE_WEEK;
        }
        return new Game(crs);
    }

    public static String getOpponentString(Game game,int teamID) {
        if (game != null) {
            return game.getOpponentString(teamID);
        } else {
            return "";
        }
    }

    public String getOpponentString(int teamID) {
        if (isBye()) {
            return "BYE";
        }
        if (_Home == null)
        {
            return "---";
        }

        if (teamID == _Home.getTeamID()) {
            return _Visitor.getAbbreviation();
        } else {
            return "@" + _Home.getAbbreviation();
        }
    }

    public boolean isBye() { return _IsByeWeek; }

    public boolean getGameHasStarted() {
        AuDate gameDate = getGameDate();
        if (gameDate == null) {
            return false;
        }
        
        return new AuDate().after(gameDate, false);
    }

    public static List<Game> GetWeeklySchedule(int seasonWeekID, boolean justTop25) {

        List<Game> games = new ArrayList<Game>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT").append(_Cols.getColumnList("Game", "g.", "")).append(", ");
        sql.append(_Cols.getColumnList("Team", "vt.", "VisitorTeam$")).append(", ");
        sql.append(_Cols.getColumnList("Team", "ht.", "HomeTeam$")).append(", ");
        sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$"));
        sql.append("FROM Game g ");
        sql.append("JOIN Team vt ON vt.TeamID = g.VisitorID ");
        sql.append("JOIN Team ht ON ht.TeamID = g.HomeID ");
        sql.append("JOIN SeasonWeek sw ON sw.SeasonWeekID = g.SeasonWeekID ");
        if (justTop25) { sql.append("LEFT JOIN Standings vst ON vst.SeasonWeekID = sw.SeasonWeekID AND vst.TeamID = g.VisitorID LEFT JOIN Standings hst ON hst.SeasonWeekID = sw.SeasonWeekID AND hst.TeamID = g.HomeID "); }
        sql.append("WHERE g.seasonWeekID = ").append(seasonWeekID).append(" AND g.visitorID > 0 ");
        if (justTop25) { sql.append("AND (vst.OverallRanking BETWEEN 1 AND 25 OR hst.OverallRanking BETWEEN 1 AND 25) "); }
        sql.append("ORDER BY g.gameDate, g.gameID");

        // Call QueryCreator
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                games.add(new Game(crs,""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return games;
   }

    public static Game getGame(int seasonWeekID, int teamID) {

        Game game = new Game();

        String sql = "SELECT " + _Cols.getColumnList("Game", "g.", "Game$") +
                "," + _Cols.getColumnList("Team", "vt.", "VisitorTeam$") +
                "," + _Cols.getColumnList("Team", "ht.", "HomeTeam$") +
                "," + _Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$") +
                "," + _Cols.getColumnList("Standings", "vst.", "VisitorStandings$") +
                "," + _Cols.getColumnList("Standings", "hst.", "HomeStandings$") +
                "FROM Game g " +
                "INNER JOIN Team vt on vt.TeamID = g.VisitorID " +
                "INNER JOIN Team ht on ht.TeamID = g.HomeID " +
                "INNER JOIN SeasonWeek sw on sw.SeasonWeekID = g.SeasonWeekID " +
                "LEFT JOIN Standings vst ON vst.SeasonWeekID = sw.SeasonWeekID AND vst.TeamID = g.VisitorID " +
                "LEFT JOIN Standings hst ON hst.SeasonWeekID = sw.SeasonWeekID AND hst.TeamID = g.HomeID " +
                "WHERE g.seasonWeekID = " + seasonWeekID +
                " AND (g.visitorID = " + teamID + " or  g.homeID = " + teamID + ") ";

        // Call QueryCreator
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs != null && crs.next()) {
                game = new Game(crs,"Game$");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return game;
   }

    public static List<Game> GetByeTeams(int seasonWeekID, boolean justTop25) {

        List<Game> games = new ArrayList<Game>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT").append(_Cols.getColumnList("Game", "g.", "Game$")).append(",");
        sql.append(_Cols.getColumnList("Team", "ht.", "HomeTeam$")).append(",");
        sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$")).append(",");
        sql.append(_Cols.getColumnList("Standings", "hst.", "HomeStandings$"));
        sql.append("FROM Game g ");
        sql.append("JOIN Team ht ON ht.TeamID = g.HomeID ");
        sql.append("JOIN SeasonWeek sw ON sw.SeasonWeekID = g.SeasonWeekID ");
        sql.append("LEFT JOIN Standings hst ON hst.SeasonWeekID = sw.SeasonWeekID AND hst.TeamID = g.HomeID ");
        sql.append("WHERE g.seasonWeekID = ").append(seasonWeekID).append(" AND g.visitorID = 0 ");
        if (justTop25) { sql.append(" AND (hst.OverallRanking BETWEEN 1 AND 25) "); }
        sql.append("ORDER BY hst.OverallRanking, g.gameDate, g.gameID");

        // Call QueryCreator
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                games.add(new Game(crs,"Game$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return games;
   }

    public static List<Game> GetTeamSchedule(int teamId, int year) {

        List<Game> games = new ArrayList<Game>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT").append(_Cols.getColumnList("Game", "g.", "")).append(",");
        sql.append(_Cols.getColumnList("Team", "vt.", "VisitorTeam$")).append(",");
        sql.append(_Cols.getColumnList("Team", "ht.", "HomeTeam$")).append(",");
        sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$")).append(",");
        sql.append(_Cols.getColumnList("Season", "s.", "Season$"));
        sql.append("FROM Game g ");
        sql.append("JOIN Team vt ON vt.TeamID = g.VisitorID ");
        sql.append("JOIN Team ht ON ht.TeamID = g.HomeID ");
        sql.append("JOIN SeasonWeek sw ON sw.SeasonWeekID = g.SeasonWeekID ");
        sql.append("JOIN Season s ON s.SeasonID = sw.SeasonID ");
        sql.append("WHERE (g.VisitorID = ").append(teamId).append(" OR g.HomeID = ").append(teamId).append(") AND s.SportYear = ").append(year).append(" ");
        sql.append("ORDER BY sw.SeasonWeekID");

        // Call QueryCreator
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                games.add(new Game(crs,""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return games;
   }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("Game", "GameID", getGameID());
        if (doesExist) { Update(); } else { Insert(); }
    }
    
    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GameID")) { _GameID = crs.getInt(prefix + "GameID"); }
            if (FSUtils.fieldExists(crs, prefix, "SeasonWeekID")) { _SeasonWeekID = crs.getInt(prefix + "SeasonWeekID"); }
            if (FSUtils.fieldExists(crs, prefix, "VisitorID")) { _VisitorID = crs.getInt(prefix + "VisitorID"); }
            if (FSUtils.fieldExists(crs, prefix, "HomeID")) { _HomeID = crs.getInt(prefix + "HomeID"); }
            if (FSUtils.fieldExists(crs, prefix, "GameDate")) { _GameDate = new AuDate(crs.getDate(prefix + "GameDate"));
                /* Set date to MST */
                _GameDate.add(Calendar.HOUR_OF_DAY, -2); }
            if (FSUtils.fieldExists(crs, prefix, "WinnerID")) { _WinnerID = crs.getInt(prefix + "WinnerID"); }
            if (FSUtils.fieldExists(crs, prefix, "VisitorPts")) { _VisitorPts = crs.getInt(prefix + "VisitorPts"); }
            if (FSUtils.fieldExists(crs, prefix, "HomePts")) { _HomePts = crs.getInt(prefix + "HomePts"); }
            if (FSUtils.fieldExists(crs, prefix, "FavoredID")) { _FavoredID = crs.getInt(prefix + "FavoredID"); }
            if (FSUtils.fieldExists(crs, prefix, "Spread")) { _Spread = crs.getDouble(prefix + "Spread"); }
            if (FSUtils.fieldExists(crs, prefix, "GameInfo")) { _GameInfo = crs.getString(prefix + "GameInfo"); }
            if (FSUtils.fieldExists(crs, prefix, "NumOTs")) { _NumOTs = crs.getInt(prefix + "NumOTs"); }  
            if (_VisitorID == 0) { _IsByeWeek = true; }                      
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "SeasonWeek$", "SeasonWeekID")) { _SeasonWeek = new SeasonWeek(crs, "SeasonWeek$"); }
            if (FSUtils.fieldExists(crs, "VisitorTeam$", "TeamID")) { _Visitor = new Team(crs, "VisitorTeam$"); }
            if (FSUtils.fieldExists(crs, "HomeTeam$", "TeamID")) { _Home = new Team(crs, "HomeTeam$"); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO Game ");
        sql.append("(GameID, SeasonWeekID, VisitorID, HomeID, GameDate, WinnerID, VisitorPts, HomePts, FavoredID, Spread, GameInfo, NumOTs) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGameID()));
        sql.append(FSUtils.InsertDBFieldValue(getSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getVisitorID()));
        sql.append(FSUtils.InsertDBFieldValue(getHomeID()));
        sql.append(FSUtils.InsertDBFieldValue((getGameDate() == null) ? null : getGameDate().toString(BGConstants.PLAYDATETIME_PATTERN), true));
        sql.append(FSUtils.InsertDBFieldValue(getWinnerID()));
        sql.append(FSUtils.InsertDBFieldValue(getVisitorPts()));
        sql.append(FSUtils.InsertDBFieldValue(getHomePts()));
        sql.append(FSUtils.InsertDBFieldValue(getFavoredID()));
        sql.append(FSUtils.InsertDBFieldValue(getSpread()));
        sql.append(FSUtils.InsertDBFieldValue(getGameInfo(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNumOTs()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE Game SET ");        
        sql.append(FSUtils.UpdateDBFieldValue("SeasonWeekID", getSeasonWeekID()));
        sql.append(FSUtils.UpdateDBFieldValue("VisitorID", getVisitorID()));
        sql.append(FSUtils.UpdateDBFieldValue("HomeID", getHomeID()));
        sql.append(FSUtils.UpdateDBFieldValue("GameDate", (getGameDate() == null) ? null : getGameDate().toString(BGConstants.PLAYDATETIME_PATTERN), true));
        sql.append(FSUtils.UpdateDBFieldValue("WinnerID", getWinnerID()));
        sql.append(FSUtils.UpdateDBFieldValue("VisitorPts", getVisitorPts()));
        sql.append(FSUtils.UpdateDBFieldValue("HomePts", getHomePts()));
        sql.append(FSUtils.UpdateDBFieldValue("FavoredID", getFavoredID()));
        sql.append(FSUtils.UpdateDBFieldValue("Spread", getSpread()));
        sql.append(FSUtils.UpdateDBFieldValue("GameInfo", getGameInfo(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NumOTs", getNumOTs()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GameID = ").append(getGameID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
