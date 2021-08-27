package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class PGATournament implements Serializable {

    static {
    }

    // DB FIELDS
    private Integer _PGATournamentID;
    private String _TournamentName;
    private String _TournamentNameShort;
    private Integer _DefendingChampionID;
    private String _LeaderboardURL;
    private Integer _NumRounds;
    private String _ExternalTournamentID;
    private Integer _Active;
    private String _ExternalTournamentIDFoxSports;

    // CONSTRUCTORS
    public PGATournament() {
    }

    public PGATournament(int tournamentID) {
        this(null, tournamentID);
    }

    public PGATournament(Connection con, int tournamentID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("PGATournament", "t.", ""));
            sql.append("FROM PGATournament t ");
            sql.append("WHERE t.PGATournamentID = ").append(tournamentID);

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

    public PGATournament(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public PGATournament(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getPGATournamentID() {return _PGATournamentID;}
    public String getTournamentName() {return _TournamentName;}
    public String getTournamentNameShort() {return _TournamentNameShort;}
    public Integer getDefendingChampionID() {return _DefendingChampionID;}
    public String getLeaderboardURL() {return _LeaderboardURL;}
    public Integer getNumRounds() {return _NumRounds;}
    public String getExternalTournamentID() {return _ExternalTournamentID;}
    public Integer getActive() { return _Active; }
    public String getExternalTournamentIDFoxSports() {return _ExternalTournamentIDFoxSports;}

    // SETTERS
    public void setPGATournamentID(Integer TournamentID) {_PGATournamentID = TournamentID;}
    public void setTournamentName(String TournamentName) {_TournamentName = TournamentName;}
    public void setTournamentNameShort(String NameShort) {_TournamentNameShort = NameShort;}
    public void setDefendingChampionID(Integer ChampID) {_DefendingChampionID = ChampID;}
    public void setLeaderboardURL(String LeaderboardURL) {_LeaderboardURL = LeaderboardURL;}
    public void setNumRounds(Integer NumRounds) {_NumRounds = NumRounds;}
    public void setExternalTournamentID(String ExtID) {_ExternalTournamentID = ExtID;}
    public void setActive(Integer active) { _Active = active; }
    public void setExternalTournamentIDFoxSports(String ExtID) {_ExternalTournamentIDFoxSports = ExtID;}

    // PUBLIC METHODS

    public static PGATournament getInstance(CachedRowSet crs) throws Exception {
        return new PGATournament(crs);
    }

    public static List<PGATournament> ReadAll() {

        List<PGATournament> tournaments = new ArrayList<PGATournament>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT").append(_Cols.getColumnList("PGATournament", "t.", ""));
        sql.append(" FROM PGATournament t ");
        sql.append(" ORDER BY TournamentName");

        // Call QueryCreator
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                tournaments.add(new PGATournament(crs,""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return tournaments;
    }

    public static List<PGATournament> ReadActive() {

        List<PGATournament> tournaments = new ArrayList<PGATournament>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT").append(_Cols.getColumnList("PGATournament", "t.", "")).append(", ");
        sql.append(" FROM PGATournament t ");
        sql.append(" WHERE Active = 1");
        sql.append(" ORDER BY TournamentName");

        // Call QueryCreator
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                tournaments.add(new PGATournament(crs,""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return tournaments;
   }

    public int Save() throws Exception {
        int success = -1;
        boolean doesExist = FSUtils.DoesARecordExistInDB("PGATournament", "PGATournamentID", getPGATournamentID());
        if (doesExist) { success = Update(); } else { success = Insert(); }
        return success;
    }

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "PGATournamentID")) { _PGATournamentID = crs.getInt(prefix + "PGATournamentID"); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentName")) { _TournamentName = crs.getString(prefix + "TournamentName"); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentNameShort")) { _TournamentNameShort = crs.getString(prefix + "TournamentNameShort"); }
            if (FSUtils.fieldExists(crs, prefix, "DefendingChampionID")) { _DefendingChampionID = crs.getInt(prefix + "DefendingChampionID"); }
            if (FSUtils.fieldExists(crs, prefix, "LeaderboardURL")) { _LeaderboardURL = crs.getString(prefix + "LeaderboardURL"); }
            if (FSUtils.fieldExists(crs, prefix, "NumRounds")) { _NumRounds = crs.getInt(prefix + "NumRounds"); }
            if (FSUtils.fieldExists(crs, prefix, "ExternalTournamentID")) { _ExternalTournamentID = crs.getString(prefix + "ExternalTournamentID"); }
            if (FSUtils.fieldExists(crs, prefix, "Active")) { _Active = crs.getInt(prefix + "Active"); }
            if (FSUtils.fieldExists(crs, prefix, "ExternalTournamentIDFoxSports")) { _ExternalTournamentIDFoxSports = crs.getString(prefix + "ExternalTournamentIDFoxSports"); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private int Insert() throws Exception {
        int newId = -1;
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO PGATournament ");
        sql.append("(TournamentName, TournamentNameShort, DefendingChampionID, LeaderboardURL, NumRounds, ExternalTournamentID, Active, ExternalTournamentIDFoxSports) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getTournamentName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentNameShort(), true));
        sql.append(FSUtils.InsertDBFieldValue(getDefendingChampionID()));
        sql.append(FSUtils.InsertDBFieldValue(getLeaderboardURL(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNumRounds()));
        sql.append(FSUtils.InsertDBFieldValue(getExternalTournamentID(), true));
        sql.append(FSUtils.InsertDBFieldValue(getActive()));
        sql.append(FSUtils.InsertDBFieldValue(getExternalTournamentIDFoxSports(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            newId = CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            throw new Exception("Error Inserting new PGATournament", e);
        }

        return newId;
    }

    private int Update() throws Exception {
        int success = -1;
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE PGATournament SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TournamentName", getTournamentName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("TournamentNameShort", getTournamentNameShort(), true));
        sql.append(FSUtils.UpdateDBFieldValue("DefendingChampionID", getDefendingChampionID()));
        sql.append(FSUtils.UpdateDBFieldValue("LeaderboardURL", getLeaderboardURL(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NumRounds", getNumRounds()));
        sql.append(FSUtils.UpdateDBFieldValue("ExternalTournamentID", getExternalTournamentID(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Active", getActive()));
        sql.append(FSUtils.UpdateDBFieldValue("ExternalTournamentIDFoxSports", getExternalTournamentIDFoxSports(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE PGATournamentID = ").append(getPGATournamentID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
            success = 0;
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            throw new Exception("Error updating PGATournament", e);
        }

        return success;
    }
}
