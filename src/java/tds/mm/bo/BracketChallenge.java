package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;
import tds.main.bo.FSTeam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static tds.data.CTColumnLists._Cols;

public class BracketChallenge implements Serializable {

    public static final int MAX_SCORE = 192;

    // DB FIELDS
    private Integer _FSTeamID;
    private Integer _GameID;
    private Integer _TeamSeedPickedID;

    // OBJECTS
    private FSTeam _FSTeam;
    private MarchMadnessGame _Game;
    private MarchMadnessTeamSeed _TeamSeedPicked;

    // ADDITIONAL FIELDS
    private BracketChallenge _PrevTopGamePick;
    private BracketChallenge _PrevBottomGamePick;

    // CONSTRUCTORS
    public BracketChallenge() {
    }

    public BracketChallenge(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getFSTeamID() {return _FSTeamID;}
    public Integer getGameID() {return _GameID;}
    public Integer getTeamSeedPickedID() {return _TeamSeedPickedID;}
    public FSTeam getFSTeam() {return _FSTeam;}
    public MarchMadnessGame getGame() {return _Game;}
    public MarchMadnessTeamSeed getTeamSeedPicked() {return _TeamSeedPicked;}
    public BracketChallenge getPrevTopGamePick() {return _PrevTopGamePick;}
    public BracketChallenge getPrevBottomGamePick() {return _PrevBottomGamePick;}

    // SETTERS
    public void setFSTeamID(Integer FSTeamID) {_FSTeamID = FSTeamID;}
    public void setGameID(Integer GameID) {_GameID = GameID;}
    public void setTeamSeedPickedID(Integer TeamPickedID) {_TeamSeedPickedID = TeamPickedID;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setGame(MarchMadnessGame Game) {_Game = Game;}
    public void setTeamSeedPicked(MarchMadnessTeamSeed TeamSeedPicked) {_TeamSeedPicked = TeamSeedPicked;}
    public void setPrevTopGamePick(BracketChallenge PrevTopGamePick) {_PrevTopGamePick = PrevTopGamePick;}
    public void setPrevBottomGamePick(BracketChallenge PrevBottomGamePick) {_PrevBottomGamePick = PrevBottomGamePick;}

    // PUBLIC METHODS

    public static List<BracketChallenge> GetPicks(int tournamentId, int fsTeamId, int roundNumber) {
        List<BracketChallenge> picks = new ArrayList<BracketChallenge>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessGame", "g.", "MarchMadnessGame$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessRegion", "r.", "MarchMadnessRegion$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessRound", "rd.", "MarchMadnessRound$")).append(", ");
        sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessTournament", "t.", "MarchMadnessTournament$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "tts.", "TopTeamSeed$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "bts.", "BottomTeamSeed$")).append(", ");
        sql.append(_Cols.getColumnList("Team", "tm.", "TopTeam$")).append(", ");
        sql.append(_Cols.getColumnList("Team", "tm2.", "BottomTeam$")).append(", ");
        sql.append(_Cols.getColumnList("BracketChallenge", "bc.", "")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "ts.", "MarchMadnessTeamSeed$")).append(", ");
        sql.append(_Cols.getColumnList("Team", "tp.", "PredictedTeam$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "w.", "GameWinnerTeamSeed$")).append(", ");
        sql.append(_Cols.getColumnList("Team", "gw.", "GameWinnerTeam$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessGame", "ptg.", "PreviousTopGame$")).append(", ");
        sql.append(_Cols.getColumnList("BracketChallenge", "p.", "PreviousTopGamePick$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "tsp.", "PreviousTopGameTeamSeedPicked$")).append(", ");
        sql.append(_Cols.getColumnList("Team", "ptgt.", "PreviousTopGameTeamPicked$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessGame", "pbg.", "PreviousBottomGame$")).append(", ");
        sql.append(_Cols.getColumnList("BracketChallenge", "p2.", "PreviousBottomGamePick$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "tsp2.", "PreviousBottomGameTeamSeedPicked$")).append(", ");
        sql.append(_Cols.getColumnList("Team", "pbgt.", "PreviousBottomGameTeamPicked$"));
        sql.append("FROM MarchMadnessGame g ");
        sql.append("JOIN MarchMadnessRegion r ON r.RegionID = g.RegionID ");
        sql.append("JOIN MarchMadnessRound rd ON rd.RoundID = g.RoundID ");
        sql.append("JOIN SeasonWeek sw ON sw.SeasonWeekID = rd.SeasonWeekID ");
        sql.append("JOIN MarchMadnessTournament t ON t.TournamentID = r.TournamentID ");
        // Game Matchup
        sql.append("LEFT JOIN MarchMadnessTeamSeed tts ON tts.TeamSeedID = g.TeamSeed1ID ");
        sql.append("LEFT JOIN MarchMadnessTeamSeed bts ON bts.TeamSeedID = g.TeamSeed2ID ");
        sql.append("LEFT JOIN Team tm ON tm.TeamID = tts.TeamID ");
        sql.append("LEFT JOIN Team tm2 ON tm2.TeamID = bts.TeamID ");
        // Game Prediction
        sql.append("LEFT JOIN BracketChallenge bc ON bc.GameID = g.gameID AND bc.FSTeamID = ").append(fsTeamId).append(" ");
        sql.append("LEFT JOIN MarchMadnessTeamSeed ts ON ts.TeamSeedID = bc.TeamSeedPickedID ");
        sql.append("LEFT JOIN Team tp ON tp.TeamID = ts.TeamID ");
        // Game Winner
        sql.append("LEFT JOIN MarchMadnessTeamSeed w ON w.TeamSeedID = g.WinnerID ");
        sql.append("LEFT JOIN Team gw ON gw.TeamID = w.TeamID ");
        // Previous Top Game
        sql.append("LEFT JOIN MarchMadnessGame ptg on ptg.NextGameID = g.GameID AND ptg.nextPosition = 1 ");
        sql.append("LEFT JOIN BracketChallenge p ON p.GameID = ptg.GameID AND p.FSTeamID = ").append(fsTeamId).append(" ");
        sql.append("LEFT JOIN MarchMadnessTeamSeed tsp ON tsp.TeamSeedID = p.TeamSeedPickedID ");
        sql.append("LEFT JOIN Team ptgt ON ptgt.TeamID = tsp.TeamID ");
        // Previous Bottom Game
        sql.append("LEFT JOIN MarchMadnessGame pbg on pbg.NextGameID = g.GameID AND pbg.nextPosition = 2 ");
        sql.append("LEFT JOIN BracketChallenge p2 ON p2.GameID = pbg.GameID AND p2.FSTeamID = ").append(fsTeamId).append(" ");
        sql.append("LEFT JOIN MarchMadnessTeamSeed tsp2 ON tsp2.TeamSeedID = p2.TeamSeedPickedID ");
        sql.append("LEFT JOIN Team pbgt ON pbgt.TeamID = tsp2.TeamID ");
        sql.append("WHERE t.TournamentID = ").append(tournamentId).append(" ");
        if (roundNumber > 0) { sql.append(" AND rd.RoundNumber = ").append(roundNumber).append(" "); }
        sql.append("ORDER BY g.gameNumber");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new BracketChallenge(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return picks;
    }

    public static List<BracketChallenge> GetPicksByGame(int gameId) {
        List<BracketChallenge> picks = new ArrayList<BracketChallenge>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessGame", "g.", "MarchMadnessGame$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessRound", "rd.", "MarchMadnessRound$")).append(", ");
        sql.append(_Cols.getColumnList("BracketChallenge", "p.", ""));
        sql.append("FROM MarchMadnessGame g ");
        sql.append("JOIN MarchMadnessRound rd ON rd.RoundID = g.RoundID ");
        sql.append("JOIN BracketChallenge p ON p.GameID = g.GameID ");
        sql.append("WHERE g.GameID  = ").append(gameId).append(" ");
        sql.append("ORDER BY p.FSTeamID");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new BracketChallenge(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return picks;
    }

    public static List<BracketChallenge> GetFutureTeamSeedPicksFromGameLoser(int teamSeedId, int roundNumber) {
        List<BracketChallenge> picks = new ArrayList<BracketChallenge>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("BracketChallenge", "bc.", "")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessGame", "g.", "MarchMadnessGame$")).append(", ");
        sql.append(_Cols.getColumnList("MarchMadnessRound", "rd.", "MarchMadnessRound$"));
        sql.append("FROM BracketChallenge bc ");
        sql.append("JOIN MarchMadnessGame g ON g.GameID = bc.GameID ");
        sql.append("JOIN MarchMadnessRound rd ON rd.RoundID = g.RoundID ");
        sql.append("WHERE bc.TeamSeedPickedID  = ").append(teamSeedId).append(" AND rd.RoundNumber >= ").append(roundNumber).append(" ");
        sql.append("ORDER BY bc.FSTeamID, rd.RoundNumber");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                picks.add(new BracketChallenge(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return picks;
    }

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("BracketChallenge", "FSTeamID", getFSTeamID(), "GameID", getGameID());
        if (doesExist) Update(); else Insert();
    }

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) { setFSTeamID(crs.getInt(prefix + "FSTeamID")); }
            if (FSUtils.fieldExists(crs, prefix, "GameID")) { setGameID(crs.getInt(prefix + "GameID")); }
            if (FSUtils.fieldExists(crs, prefix, "TeamSeedPickedID")) { setTeamSeedPickedID(crs.getInt(prefix + "TeamSeedPickedID")); }

            // OBJECTS
            if (prefix.equals("") && FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) { setFSTeam(new FSTeam(crs, "FSTeam$")); }
            if (prefix.equals("") && FSUtils.fieldExists(crs, "MarchMadnessGame$", "GameID")) { setGame(new MarchMadnessGame(crs, "MarchMadnessGame$")); }
            if (prefix.equals("PreviousTopGamePick$") && FSUtils.fieldExists(crs, "PreviousTopGame$", "GameID")) { setGame(new MarchMadnessGame(crs, "PreviousTopGame$")); }
            if (prefix.equals("PreviousBottomGamePick$") && FSUtils.fieldExists(crs, "PreviousBottomGame$", "GameID")) { setGame(new MarchMadnessGame(crs, "PreviousBottomGame$")); }
            if (prefix.equals("") && FSUtils.fieldExists(crs, "MarchMadnessTeamSeed$", "TeamSeedID")) { setTeamSeedPicked(new MarchMadnessTeamSeed(crs, "MarchMadnessTeamSeed$")); }
            if (prefix.equals("PreviousTopGamePick$") && FSUtils.fieldExists(crs, "PreviousTopGameTeamSeedPicked$", "TeamSeedID")) { setTeamSeedPicked(new MarchMadnessTeamSeed(crs, "PreviousTopGameTeamSeedPicked$")); }
            if (prefix.equals("PreviousBottomGamePick$") && FSUtils.fieldExists(crs, "PreviousBottomGameTeamSeedPicked$", "TeamSeedID")) { setTeamSeedPicked(new MarchMadnessTeamSeed(crs, "PreviousBottomGameTeamSeedPicked$")); }

            // ADDITIONAL FIELDS
            if (prefix.equals("") && FSUtils.fieldExists(crs, "PreviousTopGamePick$", "FSTeamID")) { setPrevTopGamePick(new BracketChallenge (crs, "PreviousTopGamePick$")); }
            if (prefix.equals("") && FSUtils.fieldExists(crs, "PreviousBottomGamePick$", "FSTeamID")) { setPrevBottomGamePick(new BracketChallenge (crs, "PreviousBottomGamePick$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    /*  This method inserts a new record into the DB. */
    public void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO BracketChallenge ");
        sql.append("(FSTeamID, GameID, TeamSeedPickedID) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getGameID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeamSeedPickedID()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    /*  This method updates a record in the DB. */
    public void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE BracketChallenge SET ");
        sql.append(FSUtils.UpdateDBFieldValue("FSTeamID", getFSTeamID()));
        sql.append(FSUtils.UpdateDBFieldValue("GameID", getGameID()));
        sql.append(FSUtils.UpdateDBFieldValue("TeamSeedPickedID", getTeamSeedPickedID()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSTeamID = ").append(getFSTeamID()).append(" AND GameID = ").append(getGameID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
