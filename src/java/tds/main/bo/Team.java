package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.AuDate;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import static tds.main.bo.CTApplication._CT_LOG;

public class Team implements Serializable {

    // DB FIELDS
    private Integer _TeamID;
    private String _DisplayName;    
    private String _Abbreviation;
    private String _FullName;
    private String _Mascot;
    private Integer _LiveStatsTeamID;
    private Integer _StatsTeamID;
    private Integer _SportID;
    
    // OBJECTS
    private Sport _Sport;

    private static final Map<Integer,Team> _TeamCache = new HashMap<Integer,Team>();
    private static final int _CurrentSeasonID = 18;
    private static final int _CurrentSalFSSeasonID = 50;
    public static int _seasonweekid = 77;
    private static FSSeason _FSSeason;
    
    static {
        try {
            
            CachedRowSet crs = null;
            try {
                StringBuilder sql = new StringBuilder();
                sql.append(" SELECT ").append(_Cols.getColumnList("Team", "", ""));
                sql.append(" FROM Team");

                crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
                while (crs.next()) {
                    Team team = new Team(crs);
                    if (team != null) {
                        getTeamCache().put(team.getTeamID(), team);
                    }
                }
            } catch (Exception e) {
                CTApplication._CT_LOG.error(e);
            } finally {
                JDBCDatabase.closeCRS(crs);
            }
            setFSSeason(new FSSeason(_CurrentSalFSSeasonID));
        } catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    // CONSTRUCTORS
    public Team() {
    }

    public Team(int teamID) {
        this(null, teamID);
    }

    public Team(Connection con, int teamID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("Team", "t.", ""));
            sql.append(" FROM Team t ");
            sql.append(" WHERE TeamID = ").append(teamID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            crs.next();
            InitFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }
    
    public Team(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public Team(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getTeamID() {return _TeamID;}
    public String getDisplayName() {return _DisplayName;}
    public String getAbbreviation() {return _Abbreviation;}
    public String getFullName() {return _FullName;}
    public String getMascot() {return _Mascot;}
    public Integer getLiveStatsTeamID() {return _LiveStatsTeamID;}
    public Integer getStatsTeamID() {return _StatsTeamID;}
    public Integer getSportID() {return _SportID;}
    
    public static Map<Integer,Team> getTeamCache() {return _TeamCache;}
    public static int getCurrentSeasonID() {return _CurrentSeasonID;}
    public static FSSeason getFSSeason() {return _FSSeason;}
    public Sport getSport() {if (_Sport == null && _SportID > 0) {_Sport = new Sport(_SportID);}return _Sport;}
    
    // SETTERS
    public void setTeamID(Integer TeamID) {_TeamID = TeamID;}
    public void setDisplayName(String DisplayName) {_DisplayName = DisplayName;}
    public void setAbbreviation(String Abbreviation) {_Abbreviation = Abbreviation;}
    public void setFullName(String FullName) {_FullName = FullName;}
    public void setMascot(String Mascot) {_Mascot = Mascot;}
    public void setLiveStatsTeamID(Integer LiveStatsTeamID) {_LiveStatsTeamID = LiveStatsTeamID;}
    public void setStatsTeamID(Integer StatsTeamID) {_StatsTeamID = StatsTeamID;}
    public void setSportID(Integer SportID) {_SportID = SportID;}
    public void setSport(Sport Sport) {_Sport = Sport;}
    public static void setFSSeason(FSSeason aFSSeason) {_FSSeason = aFSSeason;}
    
    // PUBLIC METHODS

    public static Team getInstance(int teamID) {
        Team ret = getTeamCache().get(teamID);
        try {
            if (ret==null) {
                ret = new Team(teamID);
                getTeamCache().put(teamID, ret);
            }
        }
        catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return ret;
    }

    // tld function
    public static Game getGame(Team team, int seasonid, int seasonweekid) {
        Game game = null;
        if (team != null) {
            game = team.getGame(seasonid,seasonweekid);
        }
        return game;
    }

    public Game getGame(int seasonid, int seasonweekid) {
        SeasonSchedule schedule = SeasonSchedule.getInstance(seasonid);
        Game game = schedule.getSeasonGame(seasonweekid, getTeamID());
        return game;
    }

    public String getOpponentString() {
        int seasonid = getCurrentSeasonID();
        return getOpponentString(seasonid, _seasonweekid);
    }

    public String getOpponentString(int seasonid, int seasonweekID) {
        return getGame(seasonid, seasonweekID).getOpponentString(getTeamID());
    }

    public boolean getGameHasStarted() {
        Game game = getGame(getCurrentSeasonID(), _seasonweekid);
        AuDate gameDate = game.getGameDate();
        AuDate now = new AuDate();
        return now.after(gameDate, false);
    }

    public static int getIDFromDisplayName( String displayName) throws Exception {
        int id = 0;
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT teamid ");
            sql.append(" FROM Team ");
            sql.append(" WHERE DisplayName = '").append(displayName).append("'");
            
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                id = crs.getInt("teamid");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        
        return id;
    }
    
    public static List<Team> GetTeamsBySport(int sportId) throws Exception {
        
        List<Team> teams = new ArrayList<Team>();
        CachedRowSet crs = null;
        
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Team", "", ""));
            sql.append("FROM Team ");
            sql.append("WHERE SportID = ").append(sportId).append(" ");
            sql.append("ORDER BY FullName");
            
            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                teams.add(new Team(crs, ""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        
        return teams;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("Team", "TeamID", getTeamID());
        if (doesExist) { Update(); } else { Insert(); }
    }
    
    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "TeamID")) { setTeamID(crs.getInt(prefix + "TeamID")); }            
            if (FSUtils.fieldExists(crs, prefix, "DisplayName")) { setDisplayName(crs.getString(prefix + "DisplayName")); }            
            if (FSUtils.fieldExists(crs, prefix, "Abbreviation")) { setAbbreviation(crs.getString(prefix + "Abbreviation")); }            
            if (FSUtils.fieldExists(crs, prefix, "FullName")) { setFullName(crs.getString(prefix + "FullName")); }            
            if (FSUtils.fieldExists(crs, prefix, "Mascot")) { setMascot(crs.getString(prefix + "Mascot")); }            
            if (FSUtils.fieldExists(crs, prefix, "LiveStatsTeamID")) { setLiveStatsTeamID(crs.getInt(prefix + "LiveStatsTeamID")); }            
            if (FSUtils.fieldExists(crs, prefix, "StatsTeamID")) { setStatsTeamID(crs.getInt(prefix + "StatsTeamID")); }            
            if (FSUtils.fieldExists(crs, prefix, "SportID")) { setSportID(crs.getInt(prefix + "SportID")); }
            
            // OBJECTS            
            if (FSUtils.fieldExists(crs, "Sport$", "SportID")) { setSport(new Sport(crs, "Sport$")); }            

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }        
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO Team ");
        sql.append("(TeamID, DisplayName, Abbreviation, FullName, Mascot, LiveStatsTeamID, StatsTeamID, SportID) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getDisplayName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getAbbreviation(), true));
        sql.append(FSUtils.InsertDBFieldValue(getFullName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getMascot(), true));
        sql.append(FSUtils.InsertDBFieldValue(getLiveStatsTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getStatsTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getSportID()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE Team SET ");
        sql.append(FSUtils.UpdateDBFieldValue("DisplayName", getDisplayName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Abbreviation", getAbbreviation(), true));
        sql.append(FSUtils.UpdateDBFieldValue("FullName", getFullName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Mascot", getMascot(), true));
        sql.append(FSUtils.UpdateDBFieldValue("LiveStatsTeamID", getLiveStatsTeamID()));
        sql.append(FSUtils.UpdateDBFieldValue("StatsTeamID", getStatsTeamID()));
        sql.append(FSUtils.UpdateDBFieldValue("SportID", getSportID()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE TeamID = ").append(getTeamID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
