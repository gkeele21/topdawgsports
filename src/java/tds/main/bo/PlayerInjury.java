package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class PlayerInjury implements Serializable {
    
    // DB FIELDS
    private int _PlayerInjuryID;
    private String _InjuryStatus;
    private String _InjuryStatusLong;
    private String _Injury;
    private int _PlayerID;
    
    // OBJECTS
    private Player _Player;
    
    // CONSTRUCTORS
    public PlayerInjury() {        
    }
    
    public PlayerInjury(int playerID) {
        this(null, playerID);
    }

    public PlayerInjury(Connection con, int playerID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" select ").append(_Cols.getColumnList("Player", "p.", "Player$"));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
            sql.append(",").append(_Cols.getColumnList("PlayerInjury", "pi.", "PlayerInjury$"));
            sql.append(" FROM PlayerInjury pi ");
            sql.append(" INNER JOIN Player p on p.PlayerID = pi.PlayerID ");
            sql.append(" INNER JOIN Team t on t.TeamID = p.TeamID ");
            sql.append(" INNER JOIN Position ps on ps.PositionID = p.PositionID ");
            sql.append(" INNER JOIN Country c on c.CountryID = p.CountryID ");
            sql.append(" WHERE p.PlayerID = ").append(playerID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            crs.next();
            initFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }
    
    public PlayerInjury(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public PlayerInjury(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }
    
    // GETTERS
    public int getPlayerInjuryID() {return _PlayerInjuryID;}
    public String getInjuryStatus() {return _InjuryStatus;}
    public String getInjuryStatusLong() {return _InjuryStatusLong;}
    public String getInjury() {return _Injury;}
    public int getPlayerID() {return _PlayerID;}
    public Player getPlayer() {if (_Player == null && _PlayerID > 0) {_Player = Player.getInstance(_PlayerID);}return _Player;}
    
    // SETTERS
    public void setPlayerInjuryID(int PlayerInjuryID) {_PlayerInjuryID = PlayerInjuryID;}
    public void setInjuryStatus(String InjuryStatus) {_InjuryStatus = InjuryStatus;}
    public void setInjuryStatusLong(String InjuryStatusLong) {_InjuryStatusLong = InjuryStatusLong;}
    public void setInjury(String Injury) {_Injury = Injury;}
    public void setPlayerID(int PlayerID) {_PlayerID = PlayerID;}
    public void setPlayer(Player Player) {_Player = Player;}
    
    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        
        try {
            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "PlayerInjuryID")) {
                setPlayerInjuryID(crs.getInt(prefix + "PlayerInjuryID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "InjuryStatus")) {
                setInjuryStatus(crs.getString(prefix + "InjuryStatus"));
            }
            if (FSUtils.fieldExists(crs, prefix, "InjuryStatusLong")) {
                setInjuryStatusLong(crs.getString(prefix + "InjuryStatusLong"));
            }
            if (FSUtils.fieldExists(crs, prefix, "Injury")) {
                setInjury(crs.getString(prefix + "Injury"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "PlayerID")) {
                setPlayerID(crs.getInt(prefix + "PlayerID"));
            }
            
            // OBJECTS
            setPlayer(Player.getInstance(getPlayerID()));

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    

    

    

}
