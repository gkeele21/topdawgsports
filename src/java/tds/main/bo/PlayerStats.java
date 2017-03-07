package tds.main.bo;

import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;

public class PlayerStats implements Serializable {
    
    // VARIABLES
    private Player _Player;
    private FSTeam _FSTeam;

    public PlayerStats() {        
    }
    
    public PlayerStats(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public PlayerStats(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }
    
    // GETTERS
    public Player getPlayer() {return _Player;}
    public FSTeam getFSTeam() {return _FSTeam;}
    
    // SETTERS
    public void setPlayer(Player Player) {_Player = Player;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {
            
            if (FSUtils.fieldExists(crs, "Player$", "PlayerID")) {
                setPlayer(new Player(crs, "Player$"));
            }
            
            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) {
                setFSTeam(new FSTeam(crs, "FSTeam$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
