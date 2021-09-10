package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;
import tds.main.bo.SeasonWeek;

import java.io.Serializable;

import static tds.data.CTColumnLists._Cols;

public class MarchMadnessRound implements Serializable {

    // DB FIELDS
    private Integer _RoundID;
    private Integer _TournamentID;
    private Integer _SeasonWeekID;
    private Integer _RoundNumber;
    private String _RoundName;
    private Integer _NumTeams;

    // OBJECTS
    private MarchMadnessTournament _Tournament;
    private SeasonWeek _SeasonWeek;

    // CONSTRUCTORS
    public MarchMadnessRound() {
    }

    public MarchMadnessRound(int roundId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessRound", "", ""));
            sql.append("FROM MarchMadnessRound ");
            sql.append("WHERE RoundID = ").append(roundId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                InitFromCRS(crs, "");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public MarchMadnessRound(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }

    // GETTERS
    public Integer getRoundID() {return _RoundID;}
    public Integer getTournamentID() {return _TournamentID;}
    public Integer getSeasonWeekID() {return _SeasonWeekID;}
    public Integer getRoundNumber() {return _RoundNumber;}
    public String getRoundName() {return _RoundName;}
    public Integer getNumTeams() {return _NumTeams;}
    public MarchMadnessTournament getTournament() {return _Tournament;}
    public SeasonWeek getSeasonWeek() {return _SeasonWeek;}

    // SETTERS
    public void setRoundID(Integer RoundID) {_RoundID = RoundID;}
    public void setTournamentID(Integer TournamentID) {_TournamentID = TournamentID;}
    public void setSeasonWeekID(Integer SeasonWeekID) {_SeasonWeekID = SeasonWeekID;}
    public void setRoundNumber(Integer RoundNumber) {_RoundNumber = RoundNumber;}
    public void setRoundName(String RoundName) {_RoundName = RoundName;}
    public void setNumTeams(Integer NumTeams) {_NumTeams = NumTeams;}
    public void setTournament(MarchMadnessTournament Tournament) {_Tournament = Tournament;}
    public void setSeasonWeek(SeasonWeek SeasonWeek) {_SeasonWeek = SeasonWeek;}

    // PUBLIC METHODS
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("MarchMadnessRound", "RoundID", getRoundID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "RoundID")) { setRoundID(crs.getInt(prefix + "RoundID")); }
            if (FSUtils.fieldExists(crs, prefix, "TournamentID")) { setTournamentID(crs.getInt(prefix + "TournamentID")); }
            if (FSUtils.fieldExists(crs, prefix, "SeasonWeekID")) { setSeasonWeekID(crs.getInt(prefix + "SeasonWeekID")); }
            if (FSUtils.fieldExists(crs, prefix, "RoundNumber")) { setRoundNumber(crs.getInt(prefix + "RoundNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "RoundName")) { setRoundName(crs.getString(prefix + "RoundName")); }
            if (FSUtils.fieldExists(crs, prefix, "NumTeams")) { setNumTeams(crs.getInt(prefix + "NumTeams")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "MarchMadnessTournament$", "TournamentID")) { setTournament(new MarchMadnessTournament(crs, "MarchMadnessTournament$")); }
            if (FSUtils.fieldExists(crs, "SeasonWeek$", "SeasonWeekID")) { setSeasonWeek(new SeasonWeek(crs, "SeasonWeek$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO MarchMadnessRound ");
        sql.append("(RoundID, TournamentID, SeasonWeekID, RoundNumber, RoundName, NumTeams) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getRoundID()));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getRoundNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getRoundName(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNumTeams()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE MarchMadnessRound SET ");
        sql.append(FSUtils.UpdateDBFieldValue("TournamentID", getTournamentID()));
        sql.append(FSUtils.UpdateDBFieldValue("SeasonWeekID", getSeasonWeekID()));
        sql.append(FSUtils.UpdateDBFieldValue("RoundNumber", getRoundNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("RoundName", getRoundName(), true));
        sql.append(FSUtils.UpdateDBFieldValue("NumTeams", getNumTeams()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE RoundID = ").append(getRoundID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
