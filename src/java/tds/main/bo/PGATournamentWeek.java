
package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.AuDate;
import bglib.util.BGConstants;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class PGATournamentWeek implements Serializable {
    
    static {
    }

    // DB FIELDS
    private Integer _PGATournamentID;
    private Integer _FSSeasonWeekID;
    private AuDate _StartDate;    
    private AuDate _EndDate;
    
    // OBJECTS
    private FSSeasonWeek _FSSeasonWeek;
    private PGATournament _PGATournament;
    
    // CONSTRUCTORS
    public PGATournamentWeek() {
    }

    public PGATournamentWeek(int tournamentID, int fsSeasonWeekID) {
        this(null, tournamentID, fsSeasonWeekID);
    }

    public PGATournamentWeek(Connection con, int tournamentID, int fsSeasonWeekID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("PGATournamentWeek", "tw.", "")).append(", ");
            sql.append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("SeasonWeek", "w.", "SeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("Season", "s.", "Season$")).append(", ");
            sql.append(_Cols.getColumnList("PGATournament", "t.", "PGATournament$"));
            sql.append(" FROM PGATournamentWeek tw ");
            sql.append(" JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = tw.FSSeasonWeekID ");
            sql.append(" JOIN SeasonWeek w ON w.SeasonWeekID = fsw.SeasonWeekID ");
            sql.append(" JOIN Season s ON s.SeasonID = w.SeasonID ");
            sql.append(" JOIN PGATournament t ON t.PGATournamentID = tw.PGATournamentID ");
            sql.append(" WHERE tw.PGATournamentID = ").append(tournamentID);
            sql.append(" AND tw.FSSeasonWeekID = ").append(fsSeasonWeekID);

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
    
    public PGATournamentWeek(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public PGATournamentWeek(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }
    
    // GETTERS
    public Integer getPGATournamentID() {return _PGATournamentID;}
    public Integer getFSSeasonWeekID() {return _FSSeasonWeekID;}    
    public AuDate getStartDate() {return _StartDate;}
    public AuDate getEndDate() { return _EndDate; }
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}
    public PGATournament getPGATournament() {if (_PGATournament == null && _PGATournamentID > 0) {_PGATournament = new PGATournament(_PGATournamentID);}return _PGATournament;}

    // SETTERS
    public void setPGATournamentID(Integer TournamentID) {_PGATournamentID = TournamentID;}
    public void setFSSeasonWeekID(Integer FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setStartDate(AuDate StartDate) {_StartDate = StartDate;}
    public void setEndDate(AuDate EndDate) {_EndDate = EndDate;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}
    public void setPGATournament(PGATournament PGATournament) {_PGATournament = PGATournament;}
    
    // PUBLIC METHODS
    
    public static PGATournamentWeek getInstance(CachedRowSet crs) throws Exception {
        return new PGATournamentWeek(crs);
    }

    public boolean getTournamentHasStarted() {
        AuDate gameDate = getStartDate();
        if (gameDate == null) {
            return false;
        }
        
        return new AuDate().after(gameDate, false);
    }

    public static List<PGATournamentWeek> ReadAllByFSSeason(int fsSeasonID) {

        List<PGATournamentWeek> tournaments = new ArrayList<PGATournamentWeek>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT").append(_Cols.getColumnList("PGATournamentWeek", "tw.", "")).append(", ");
        sql.append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("PGATournament", "t.", "PGATournament$"));
        sql.append(" FROM PGATournamentWeek tw ");
        sql.append(" JOIN PGATournament t ON t.PGATournamentID = tw.PGATournamentID ");
        sql.append(" JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = tw.FSSeasonWeekID ");
        sql.append(" JOIN SeasonWeek sw ON sw.SeasonWeekID = fsw.SeasonWeekID ");
        sql.append(" WHERE fsw.fsSeasonID = ").append(fsSeasonID);
        sql.append(" ORDER BY fsw.FSSeasonWeekNo");

        // Call QueryCreator
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                tournaments.add(new PGATournamentWeek(crs,""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return tournaments;
   }

    public static PGATournamentWeek getTournamentWeek(int fsSeasonWeekID) {

        PGATournamentWeek tournamentWeek = new PGATournamentWeek();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT").append(_Cols.getColumnList("PGATournamentWeek", "tw.", "")).append(", ");
        sql.append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("PGATournament", "t.", "PGATournament$"));
        sql.append(" FROM PGATournamentWeek tw ");
        sql.append(" INNER JOIN FSSeasonWeek fsw on fsw.FSSeasonWeekID = tw.FSSeasonWeekID ");
        sql.append(" INNER JOIN PGATournament t on t.PGATournamentID = tw.PGATournamentID ");
        sql.append(" WHERE tw.FSSeasonWeekID = ").append(fsSeasonWeekID);

        // Call QueryCreator
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs != null && crs.next()) {
                tournamentWeek = new PGATournamentWeek(crs,"");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return tournamentWeek;
    }

    public List<PGATournamentWeekPlayer> GetField() {
        return GetField(null, null, false, 0);
    }
    
    public List<PGATournamentWeekPlayer> GetField(String orderBy) {
        return GetField(null, orderBy, false, 0);
    }
    
    public List<PGATournamentWeekPlayer> GetField(List<PGATournamentWeekPlayer> excludePlayers, String orderBy) {
        return GetField(excludePlayers, orderBy, false, 0);
    }

    public List<PGATournamentWeekPlayer> GetField(List<PGATournamentWeekPlayer> excludePlayers, String orderBy, boolean includeOwners) {
        return GetField(excludePlayers, orderBy, false, 0);
    }

    public List<PGATournamentWeekPlayer> GetField(List<PGATournamentWeekPlayer> excludePlayers, String orderBy, boolean includeOwners, int fsLeagueID) {

        List<PGATournamentWeekPlayer> players = new ArrayList<PGATournamentWeekPlayer>();

        String excludeStr = "";
        if (excludePlayers !=null && excludePlayers.size() > 0) {
            for (PGATournamentWeekPlayer playerValue : excludePlayers) {
                excludeStr += String.valueOf(playerValue.getPlayerID()) + ",";
            }
            excludeStr = excludeStr.substring(0,excludeStr.length()-1 );
        } else {
            excludeStr = "-1";
        }
        
        if (orderBy == null)
        {
            orderBy = "twp.SalaryValue desc, p.lastName";
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT").append(_Cols.getColumnList("PGATournamentWeekPlayer", "twp.", "PGATournamentWeekPlayer$")).append(", ");
        sql.append(_Cols.getColumnList("Player", "p.", "Player$")).append(", ");
        sql.append(_Cols.getColumnList("Country", "cnt.", "Country$")).append(", ");
        sql.append(_Cols.getColumnList("PGATournament", "t.", "PGATournament$"));
        sql.append(" FROM PGATournamentWeekPlayer twp ");
        sql.append(" JOIN Player p ON p.PlayerID = twp.PlayerID ");
        sql.append(" LEFT JOIN Country cnt on cnt.CountryID = p.CountryID ");
        sql.append(" JOIN PGATournament t ON t.PGATournamentID = twp.PGATournamentID");
        sql.append(" WHERE twp.FSSeasonWeekID = ").append(getFSSeasonWeekID());
        sql.append(" AND twp.PGATournamentID = ").append(getPGATournamentID());
        sql.append(" AND p.PlayerID not in (").append(excludeStr).append(")");
        sql.append(" ORDER BY ").append(orderBy);

        // Call QueryCreator
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                PGATournamentWeekPlayer player = new PGATournamentWeekPlayer(crs,"PGATournamentWeekPlayer$");
                if (includeOwners)
                {
                    player.populateOwners(fsLeagueID);
                }
                
                players.add(player);
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return players;
    }
    
    public List<Player> GetPossibleField()
    {
        return GetPossibleField(null);
    }
    
    public List<Player> GetPossibleField(List<PGATournamentWeekPlayer> excludePlayers) {

        List<Player> players = new ArrayList<Player>();

        String excludeStr = "";
        if (excludePlayers !=null && excludePlayers.size() > 0) {
            for (PGATournamentWeekPlayer playerValue : excludePlayers) {
                excludeStr += String.valueOf(playerValue.getPlayerID()) + ",";
            }
            excludeStr = excludeStr.substring(0,excludeStr.length()-1 );
        } else {
            excludeStr = "-1";
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT").append(_Cols.getColumnList("Player", "p.", "Player$"));
        sql.append(", ").append(_Cols.getColumnList("Country", "cnt.", "Country$"));
        sql.append(" FROM Player p ");
        sql.append(" LEFT JOIN Country cnt on cnt.CountryID = p.CountryID ");
        sql.append(" WHERE p.IsActive = 1");
        sql.append(" AND p.PlayerID not in (").append(excludeStr).append(")");
        sql.append(" ORDER BY p.lastName, p.firstName");

        // Call QueryCreator
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                Player p = new Player(crs, "Player$");
                players.add(p);
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return players;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("PGATournamentWeek", "PGATournamentID", getPGATournamentID(), "FSSeasonWeekID", getFSSeasonWeekID());
        if (doesExist) { Update(); } else { Insert(); }
    }
    
    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "PGATournamentID")) { _PGATournamentID = crs.getInt(prefix + "PGATournamentID"); }
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) { _FSSeasonWeekID = crs.getInt(prefix + "FSSeasonWeekID"); }
            if (FSUtils.fieldExists(crs, prefix, "StartDate")) { _StartDate = new AuDate(crs.getDate(prefix + "StartDate")); }
            if (FSUtils.fieldExists(crs, prefix, "EndDate")) { _EndDate = new AuDate(crs.getDate(prefix + "EndDate")); }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) { _FSSeasonWeek = new FSSeasonWeek(crs, "FSSeasonWeek$"); }
            if (FSUtils.fieldExists(crs, "PGATournament$", "PGATournamentID")) { _PGATournament = new PGATournament(crs, "PGATournament$"); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO PGATournamentWeek ");
        sql.append("(PGATournamentID, FSSeasonWeekID, StartDate, EndDate) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getPGATournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue((getStartDate() == null) ? null : getStartDate().toString(BGConstants.PLAYDATE_PATTERN), true));
        sql.append(FSUtils.InsertDBFieldValue((getEndDate() == null) ? null : getEndDate().toString(BGConstants.PLAYDATE_PATTERN), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE PGATournamentWeek SET ");        
        sql.append(FSUtils.UpdateDBFieldValue("PGATournamentID", getPGATournamentID()));
        sql.append(FSUtils.UpdateDBFieldValue("FSSeasonWeekID", getFSSeasonWeekID()));
        sql.append(FSUtils.UpdateDBFieldValue("StartDate", (getStartDate() == null) ? null : getStartDate().toString(BGConstants.PLAYDATE_PATTERN), true));
        sql.append(FSUtils.UpdateDBFieldValue("EndDate", (getEndDate() == null) ? null : getEndDate().toString(BGConstants.PLAYDATE_PATTERN), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append(" WHERE PGATournamentID = ").append(getPGATournamentID());
        sql.append(" AND FSSeasonWeekID = ").append(getFSSeasonWeekID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
