package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.util.CTReturnCode;

public class FSGame implements Serializable {

    // CONSTANTS
    public final static int HEAD_TO_HEAD = 1;
    public final static int SALARY_CAP = 2;
    public final static int PRO_PLAYER_LOVELEAVE = 3;
    public final static int PRO_PICKEM = 4;
    public final static int PRO_LOVEEMLEAVEEM = 5;
    public final static int COLLEGE_LOVEEMLEAVEEM = 6;
    public final static int COLLEGE_PICKEM = 7;
    public final static int BRACKET_CHALLENGE = 8;
    public final static int SEED_CHALLENGE = 9;
    public final static int FF_PLAYOFF = 10;
    
    // DB FIELDS
    private int _FSGameID;
    private int _SportID;    
    private String _GameName;
    private String _GameNameShort;
    private String _GamePrefix;
    private String _ScoringStyle;
    private String _GroupingStyle;
    private int _CurrentFSSeasonID;
    private String _HomeURL;
    private String _DisplayName;
    private String _HomeURLShort;
    
    // OBJECTS
    private Sport _Sport;

    // CONSTRUCTORS
    public FSGame() {
    }

    public FSGame(int gameID) {
        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSGame", "g.", ""));
            sql.append(" FROM FSGame g ");
            sql.append(" WHERE g.FSGameID = ").append(gameID);

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            crs.next();
            InitFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }
    }
    
    public FSGame(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public FSGame(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }
    
    // GETTERS
    public int getFSGameID() {return _FSGameID;}
    public int getSportID() {return _SportID;}
    public String getGameName() {return _GameName;}
    public String getGameNameShort() {return _GameNameShort;}
    public String getGamePrefix() {return _GamePrefix;}
    public String getScoringStyle() {return _ScoringStyle;}
    public String getGroupingStyle() {return _GroupingStyle;}
    public int getCurrentFSSeasonID() {return _CurrentFSSeasonID;}
    public String getHomeURL() {return _HomeURL;}
    public String getDisplayName() {return _DisplayName;}
    public String getHomeURLShort() {return _HomeURLShort;}
    public Sport getSport() {if (_Sport == null && _SportID > 0) {_Sport = new Sport(_SportID);}return _Sport;}
    
    // SETTERS
    public void setFSGameID(int FSGameID) {_FSGameID = FSGameID;}
    public void setSportID(int SportID) {_SportID = SportID;}
    public void setGameName(String GameName) {_GameName = GameName;}
    public void setGameNameShort(String GameNameShort) {_GameNameShort = GameNameShort;}
    public void setGamePrefix(String GamePrefix) {_GamePrefix = GamePrefix;}
    public void setScoringStyle(String ScoringStyle) {_ScoringStyle = ScoringStyle;}
    public void setGroupingStyle(String GroupingStyle) {_GroupingStyle = GroupingStyle;}
    public void setCurrentFSSeasonID(int CurrentFSSeasonID) {_CurrentFSSeasonID = CurrentFSSeasonID;}
    public void setHomeURL(String HomeURL) {_HomeURL = HomeURL;}
    public void setDisplayName(String DisplayName) {_DisplayName = DisplayName;}
    public void setHomeURLShort(String HomeURLShort) {_HomeURLShort = HomeURLShort;}
    public void setSport(Sport Sport) {_Sport = Sport;}

    public CTReturnCode update() {
        int res = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE FSGame ");
            sql.append(" SET SportID = ").append(getSportID());
            sql.append(", GameName = '").append(getGameName());
            sql.append("', GameNameShort = '").append(getGameNameShort());
            sql.append("', GamePrefix = '").append(getGamePrefix());
            sql.append("', ScoringStyle = '").append(getScoringStyle());
            sql.append("', GroupingStyle = '").append(getGroupingStyle());
            sql.append("', CurrentFSSeasonID = ").append(getCurrentFSSeasonID());
            sql.append(", HomeURL = '").append(getHomeURL());
            sql.append("', DisplayName = '").append(getDisplayName());
            sql.append("', HomeURLShort = '").append(getHomeURLShort()).append("'");
            sql.append(" WHERE FSGameID = ").append(getFSGameID());

            res = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        }
        catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return (res>0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
    }
   
    public static List<FSGame> getFSGames() {
        return getFSGames(0);
    }
    
    public static List<FSGame> getFSGames(int sportID) {
        List<FSGame> fsgames = new ArrayList<FSGame>();

        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            
            sql.append(" SELECT ").append(_Cols.getColumnList("FSGame", "g.", ""));
            sql.append(" FROM FSGame g ");
            
            if (sportID > 0)
            {
                sql.append(" WHERE SportID = ").append(sportID);
            }

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                fsgames.add(new FSGame(crs));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return fsgames;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSGame", "FSGameID", getFSGameID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSGameID")) { setFSGameID(crs.getInt(prefix + "FSGameID")); }            
            if (FSUtils.fieldExists(crs, prefix, "SportID")) { setSportID(crs.getInt(prefix + "SportID")); }            
            if (FSUtils.fieldExists(crs, prefix, "GameName")) { setGameName(crs.getString(prefix + "GameName")); }            
            if (FSUtils.fieldExists(crs, prefix, "GameNameShort")) { setGameNameShort(crs.getString(prefix + "GameNameShort")); }            
            if (FSUtils.fieldExists(crs, prefix, "GamePrefix")) { setGamePrefix(crs.getString(prefix + "GamePrefix")); }            
            if (FSUtils.fieldExists(crs, prefix, "ScoringStyle")) { setScoringStyle(crs.getString(prefix + "ScoringStyle")); }            
            if (FSUtils.fieldExists(crs, prefix, "GroupingStyle")) { setGroupingStyle(crs.getString(prefix + "GroupingStyle")); }            
            if (FSUtils.fieldExists(crs, prefix, "CurrentFSSeasonID")) { setCurrentFSSeasonID(crs.getInt(prefix + "CurrentFSSeasonID")); }            
            if (FSUtils.fieldExists(crs, prefix, "HomeURL")) { setHomeURL(crs.getString(prefix + "HomeURL")); }            
            if (FSUtils.fieldExists(crs, prefix, "HomeURLShort")) { setHomeURLShort(crs.getString(prefix + "HomeURLShort")); }            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "Sport$", "SportID")) { setSport(new Sport(crs, "Sport$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }        
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSGame ");
        sql.append("(FSGameID, SportID, GameName, GameNameShort, GamePrefix, ScoringStyle, GroupingStyle, CurrentFSSeasonID, HomeURL, HomeURLShort)");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSGameID()));
        sql.append(FSUtils.InsertDBFieldValue(getSportID()));
        sql.append(FSUtils.InsertDBFieldValue(getGameName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getGameNameShort(), true));
        sql.append(FSUtils.InsertDBFieldValue(getGamePrefix(), true));
        sql.append(FSUtils.InsertDBFieldValue(getScoringStyle(), true));
        sql.append(FSUtils.InsertDBFieldValue(getGroupingStyle(), true));
        sql.append(FSUtils.InsertDBFieldValue(getGroupingStyle(), true));
        sql.append(FSUtils.InsertDBFieldValue(getCurrentFSSeasonID()));
        sql.append(FSUtils.InsertDBFieldValue(getHomeURL(), true));
        sql.append(FSUtils.InsertDBFieldValue(getHomeURLShort(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE FSGame SET ");
        sql.append(FSUtils.UpdateDBFieldValue("SportID", getSportID()));
        sql.append(FSUtils.UpdateDBFieldValue("GameName", getGameName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("GameNameShort", getGameNameShort(), true));
        sql.append(FSUtils.UpdateDBFieldValue("GamePrefix", getGamePrefix(), true));
        sql.append(FSUtils.UpdateDBFieldValue("ScoringStyle", getScoringStyle(), true));
        sql.append(FSUtils.UpdateDBFieldValue("GroupingStyle", getGroupingStyle(), true));
        sql.append(FSUtils.UpdateDBFieldValue("CurrentFSSeasonID", getCurrentFSSeasonID()));
        sql.append(FSUtils.UpdateDBFieldValue("HomeURL", getHomeURL(), true));
        sql.append(FSUtils.UpdateDBFieldValue("HomeURLShort", getHomeURLShort(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSGameID = ").append(getFSGameID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
