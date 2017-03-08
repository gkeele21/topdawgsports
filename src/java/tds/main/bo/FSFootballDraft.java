package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSFootballDraft implements Serializable {
    
    // DB FIELDS
    private int _FSLeagueID;    
    private int _Round;
    private int _Place;
    private int _FSTeamID;    
    private int _PlayerID;    
    
    // OBJECTS
    private FSLeague _FSLeague;
    private FSTeam _FSTeam;
    private Player _Player;

    // CONSTRUCTORS
    public FSFootballDraft() {
    }

    public FSFootballDraft(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FSFootballDraft(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    public FSFootballDraft(int leagueID, int round, int place) {

        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("Player", "p.", "Player$"));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("FSTeam","fst.", "FSTeam$"));
            sql.append(",").append(_Cols.getColumnList("FSLeague","fsl.", "FSFootballLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSFootballDraft","fsd.", "FSFootballDraft$"));
            sql.append(" FROM FSFootballDraft fsd ");
            sql.append(" INNER JOIN Player p ON p.PlayerID = fsd.PlayerID ");
            sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID ");
            sql.append(" LEFT JOIN Team t ON t.TeamID = p.TeamID ");
            sql.append(" INNER JOIN FSTeam fst ON fst.FSTeamID = fsd.FSTeamID ");
            sql.append(" INNER JOIN FSLeague fsl ON fsl.FSLeagueID = fst.FSLeagueID ");
            sql.append(" WHERE fsd.FSLeagueID = ").append(leagueID);
            sql.append(" AND fsd.Round = ").append(round);
            sql.append(" AND fsd.Place = ").append(place);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());

            if (crs.next()) {
                initFromCRS(crs,"FSFootballDraft$");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        
    }
    
    // GETTERS
    public int getFSLeagueID() {return _FSLeagueID;}
    public int getRound() {return _Round;}
    public int getPlace() {return _Place;}
    public int getFSTeamID() {return _FSTeamID;}
    public int getPlayerID() {return _PlayerID;}
    public FSLeague getFSLeague() {if (_FSLeague == null && getFSLeagueID() > 0) {_FSLeague = new FSLeague(getFSLeagueID());}return _FSLeague;}
    public FSTeam getFSTeam() {if (_FSTeam == null && _FSTeamID > 0) {_FSTeam = new FSTeam(_FSTeamID);}return _FSTeam;}
    public Player getPlayer() {if (_Player == null && _PlayerID > 0) {_Player = new Player(_PlayerID);}return _Player;}
    
    // SETTERS
    public void setFSLeagueID(int FSLeagueID) {_FSLeagueID = FSLeagueID;}
    public void setRound(int Round) {_Round = Round;}
    public void setPlace(int Place) {_Place = Place;}
    public void setFSTeamID(int FSTeamID) {_FSTeamID = FSTeamID;}
    public void setPlayerID(int PlayerID) {_PlayerID = PlayerID;}
    public void setFSLeague(FSLeague FSLeague) {_FSLeague = FSLeague;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setPlayer(Player Player) {_Player = Player;}

    // PUBLIC METHODS
    
    public static List<FSFootballDraft> getDraftResults(int leagueID) {
        List<FSFootballDraft> results = new ArrayList<FSFootballDraft>();

        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("Player", "p.", "Player$"));
            sql.append(",").append(_Cols.getColumnList("Team", "t.", "Team$"));
            sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
            sql.append(",").append(_Cols.getColumnList("FSTeam","fst.", "FSTeam$"));
            sql.append(",").append(_Cols.getColumnList("FSLeague","fsl.", "FSFootballLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSFootballDraft","fsd.", "FSFootballDraft$"));
            sql.append(" FROM FSFootballDraft fsd ");
            sql.append(" INNER JOIN Player p ON p.PlayerID = fsd.PlayerID ");
            sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID ");
            sql.append(" LEFT JOIN Team t ON t.TeamID = p.TeamID ");
            sql.append(" INNER JOIN FSTeam fst ON fst.FSTeamID = fsd.FSTeamID ");
            sql.append(" INNER JOIN FSLeague fsl ON fsl.FSLeagueID = fst.FSLeagueID ");
            sql.append(" WHERE fsd.FSLeagueID = ").append(leagueID);
            sql.append(" ORDER BY fsd.Round,fsd.Place ");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());

            while (crs.next()) {
                results.add(new FSFootballDraft(crs, "FSFootballDraft$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        
        return results;
    }

    public static int insertPick(int round, int place, int fsTeamId, int playerId, int fsLeagueId) {
        int retVal = 0;

        String sql = "INSERT INTO FSFootballDraft " +
                "(Round, Place, FSTeamID, PlayerID, FSLeagueID) " +
                "VALUES (" + round + ", " + place + ", " + fsTeamId + ", " + playerId + ", " + fsLeagueId + ")";

        // Call QueryCreator
        try {
            retVal = CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }

    public int deletePick() {

        int retVal = -1;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append(" DELETE FROM FSFootballDraft ");
	sql.append(" WHERE FSLeagueID = ").append(this._FSLeagueID);
        sql.append(" AND Round = ").append(this._Round);
        sql.append(" AND Place = ").append(this._Place);

        try {
            retVal = CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }
    
    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSLeagueID")) {
                setFSLeagueID(crs.getInt(prefix + "FSLeagueID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Round")) {
                setRound(crs.getInt(prefix + "Round"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Place")) {
                setPlace(crs.getInt(prefix + "Place"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) {
                setFSTeamID(crs.getInt(prefix + "FSTeamID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "PlayerID")) {
                setPlayerID(crs.getInt(prefix + "PlayerID"));
            }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSLeague$", "FSLeagueID")) {
                setFSLeague(new FSLeague(crs, "FSLeague$"));
            }
            
            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) {
                setFSTeam(new FSTeam(crs, "FSTeam$"));
            }
            
            if (FSUtils.fieldExists(crs, "Player$", "PlayerID")) {
                setPlayer(new Player(crs, "Player$"));
            }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
