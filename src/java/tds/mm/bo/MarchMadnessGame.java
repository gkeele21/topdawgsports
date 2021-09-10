package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;
import tds.main.bo.FSGame;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.SeasonWeek;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static tds.data.CTColumnLists._Cols;

public class MarchMadnessGame implements Serializable {

    public enum Status {UPCOMING, ONGOING, FINAL};

    // DB FIELDS
    private Integer _GameID;
    private Integer _RoundID;
    private Integer _RegionID;
    private Integer _GameNumber;
    private String _Status;
    private Integer _TeamSeed1ID;
    private Integer _TeamSeed2ID;
    private Integer _Team1Pts;
    private Integer _Team2Pts;
    private Integer _WinnerID;
    private Integer _NextGameID;
    private Integer _NextPosition;
    private String _Location;

    // OBJECTS
    private MarchMadnessRound _Round;
    private MarchMadnessRegion _Region;
    private MarchMadnessTeamSeed _TopTeamSeed;
    private MarchMadnessTeamSeed _BottomTeamSeed;
    private MarchMadnessTeamSeed _Winner;


    // CONSTRUCTORS
    public MarchMadnessGame() {
    }

    public MarchMadnessGame(int gameId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessGame", "g.", "")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessRegion", "s.", "MarchMadnessRegion$")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessRound", "rd.", "MarchMadnessRound$")).append(", ");
            sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessTournament", "t.", "MarchMadnessTournament$")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "ts.", "TopTeamSeed$")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "ts2.", "BottomTeamSeed$")).append(", ");
            sql.append(_Cols.getColumnList("Team", "tm.", "TopTeam$")).append(", ");
            sql.append(_Cols.getColumnList("Team", "tm2.", "BottomTeam$")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "tsw.", "TeamSeedWinner$")).append(", ");
            sql.append(_Cols.getColumnList("Team", "w.", "Winner$"));
            sql.append("FROM MarchMadnessGame g ");
            sql.append("INNER JOIN MarchMadnessRegion s ON s.RegionID = g.RegionID ");
            sql.append("INNER JOIN MarchMadnessRound rd ON rd.RoundID = g.RoundID ");
            sql.append("INNER JOIN SeasonWeek sw ON sw.SeasonWeekID = rd.SeasonWeekID ");
            sql.append("INNER JOIN MarchMadnessTournament t ON t.TournamentID = s.TournamentID ");
            sql.append("LEFT JOIN MarchMadnessTeamSeed ts ON ts.TeamSeedID = g.TeamSeed1ID ");
            sql.append("LEFT JOIN MarchMadnessTeamSeed ts2 ON ts2.TeamSeedID = g.TeamSeed2ID ");
            sql.append("LEFT JOIN Team tm ON tm.TeamID = ts.TeamID ");
            sql.append("LEFT JOIN Team tm2 ON tm2.TeamID = ts2.TeamID ");
            sql.append("LEFT JOIN MarchMadnessTeamSeed tsw ON tsw.TeamSeedID = g.WinnerID ");
            sql.append("LEFT JOIN Team w ON w.TeamID = tsw.TeamID ");

            sql.append("WHERE g.GameID = ").append(gameId);

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

    public MarchMadnessGame(CachedRowSet crs, String prefix) {
        InitFromCRS(crs, prefix);
    }

    // GETTERS
    public Integer getGameID() {return _GameID;}
    public Integer getRoundID() {return _RoundID;}
    public Integer getRegionID() {return _RegionID;}
    public Integer getGameNumber() {return _GameNumber;}
    public String getStatus() {return _Status;}
    public Integer getTeamSeed1ID() {return _TeamSeed1ID;}
    public Integer getTeamSeed2ID() {return _TeamSeed2ID;}
    public Integer getTeam1Pts() {return _Team1Pts;}
    public Integer getTeam2Pts() {return _Team2Pts;}
    public Integer getWinnerID() {return _WinnerID;}
    public Integer getNextGameID() {return _NextGameID;}
    public Integer getNextPosition() {return _NextPosition;}
    public String getLocation() {return _Location;}
    public MarchMadnessRound getRound() {return _Round;}
    public MarchMadnessRegion getRegion() {return _Region;}
    public MarchMadnessTeamSeed getTopTeamSeed() {return _TopTeamSeed;}
    public MarchMadnessTeamSeed getBottomTeamSeed() {return _BottomTeamSeed;}
    public MarchMadnessTeamSeed getWinner() {return _Winner;}

    // SETTERS
    public void setGameID(Integer GameID) {_GameID = GameID;}
    public void setRoundID(Integer RoundID) {_RoundID = RoundID;}
    public void setRegionID(Integer RegionID) {_RegionID = RegionID;}
    public void setGameNumber(Integer GameNumber) {_GameNumber = GameNumber;}
    public void setStatus(String Status) {_Status = Status;}
    public void setTeamSeed1ID(Integer TeamSeed1Id) {_TeamSeed1ID = TeamSeed1Id;}
    public void setTeamSeed2ID(Integer TeamSeed2Id) {_TeamSeed2ID = TeamSeed2Id;}
    public void setTeam1Pts(Integer Team1Pts) {_Team1Pts = Team1Pts;}
    public void setTeam2Pts(Integer Team2Pts) {_Team2Pts = Team2Pts;}
    public void setWinnerID(Integer WinnerID) {_WinnerID = WinnerID;}
    public void setNextGameID(Integer NextGameID) {_NextGameID = NextGameID;}
    public void setNextPosition(Integer NextPosition) {_NextPosition = NextPosition;}
    public void setLocation(String Location) {_Location = Location;}
    public void setRound(MarchMadnessRound Round) {_Round = Round;}
    public void setRegion(MarchMadnessRegion Region) {_Region = Region;}
    public void setTopTeamSeed(MarchMadnessTeamSeed TopTeamSeed) {_TopTeamSeed = TopTeamSeed;}
    public void setBottomTeamSeed(MarchMadnessTeamSeed BottomTeamSeed) {_BottomTeamSeed = BottomTeamSeed;}
    public void setWinner(MarchMadnessTeamSeed Winner) {_Winner = Winner;}

    // PUBLIC METHODS

    /* Retrieves a list of March Madness Tournament games for specific rounds */
    public static List<MarchMadnessGame> GetTournamentGames(int tournamentId, int begRoundNum, int endRoundNum) {
        CachedRowSet crs = null;
        List<MarchMadnessGame> games = new ArrayList<MarchMadnessGame>();
        StringBuilder sql = new StringBuilder();

        try {
            sql.append("SELECT").append(_Cols.getColumnList("MarchMadnessGame", "g.", "")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessRegion", "r.", "MarchMadnessRegion$")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessRound", "rd.", "MarchMadnessRound$")).append(", ");
            sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessTournament", "t.", "MarchMadnessTournament$")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "ts.", "TopTeamSeed$")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "ts2.", "BottomTeamSeed$")).append(", ");
            sql.append(_Cols.getColumnList("Team", "tm.", "TopTeam$")).append(", ");
            sql.append(_Cols.getColumnList("Team", "tm2.", "BottomTeam$")).append(", ");
            sql.append(_Cols.getColumnList("MarchMadnessTeamSeed", "w.", "GameWinnerTeamSeed$")).append(", ");
            sql.append(_Cols.getColumnList("Team", "gw.", "GameWinnerTeam$"));
            sql.append("FROM MarchMadnessGame g ");
            sql.append("JOIN MarchMadnessRegion r ON r.RegionID = g.RegionID ");
            sql.append("JOIN MarchMadnessRound rd ON rd.RoundID = g.RoundID ");
            sql.append("JOIN SeasonWeek sw ON sw.SeasonWeekID = rd.SeasonWeekID ");
            sql.append("JOIN MarchMadnessTournament t ON t.TournamentID = r.TournamentID AND t.TournamentID = ").append(tournamentId).append(" ");
            sql.append("LEFT JOIN MarchMadnessTeamSeed ts ON ts.TeamSeedID = g.TeamSeed1ID ");
            sql.append("LEFT JOIN MarchMadnessTeamSeed ts2 ON ts2.TeamSeedID = g.TeamSeed2ID ");
            sql.append("LEFT JOIN Team tm ON tm.TeamID = ts.TeamID ");
            sql.append("LEFT JOIN Team tm2 ON tm2.TeamID = ts2.TeamID ");
            sql.append("LEFT JOIN MarchMadnessTeamSeed w ON w.TeamSeedID = g.WinnerID ");
            sql.append("LEFT JOIN Team gw ON gw.TeamID = w.TeamID ");
            sql.append("WHERE rd.RoundNumber BETWEEN ").append(begRoundNum).append(" AND ").append(endRoundNum).append(" ");
            sql.append("ORDER BY GameID");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                games.add(new MarchMadnessGame(crs, ""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return games;
   }

   /* This retrieves the current game id for a given fsTeam for a specific round.  The purpose is to have it scroll down in the bracket where the fsteam's game is. */
    public static int GetGameIDByRound(int fsTeamId, int roundNum) {
        CachedRowSet crs = null;
        int currentGame = 0;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT GameID ");
            sql.append("FROM MarchMadnessGame g ");
            sql.append("INNER JOIN MarchMadnessRound r ON r.RoundID = g.RoundID ");
            sql.append("INNER JOIN SeasonWeek sw ON sw.SeasonWeekID = r.SeasonWeekID ");
            sql.append("WHERE sw.SeasonWeekNo = ").append(roundNum).append(" AND g.Team1ID = ").append(fsTeamId).append(" OR g.Team2ID = ").append(fsTeamId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                currentGame = crs.getInt("GameID");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return currentGame;
   }

     /*  This method is used to store the March Madness Game data in the DB. */
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("MarchMadnessGame", "GameID", getGameID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // Update the Score/Winner in the MarchMadnessGame table
    public static void UpdateGameResult(int tournamentId, int gameId, SeasonWeek seasonWeek, int topTeamPts, int bottomTeamPts) {
        Integer winnerId = 0;
        Integer loserId = 0;

        // Update the March Madness Game table with the score, winner, and loser
        MarchMadnessGame game = new MarchMadnessGame(gameId);
        game.setTeam1Pts(topTeamPts);
        game.setTeam2Pts(bottomTeamPts);
        game.setStatus(MarchMadnessGame.Status.FINAL.toString());

        if (topTeamPts > bottomTeamPts) { winnerId = game.getTeamSeed1ID(); loserId = game.getTeamSeed2ID(); }
        else if (bottomTeamPts > topTeamPts) { winnerId = game.getTeamSeed2ID(); loserId = game.getTeamSeed1ID(); }

        game.setWinnerID(winnerId);
        game.Save();

        // Update the next round's game with the winner
        if (game.getNextGameID() != null && game.getNextGameID() > 0) {
            MarchMadnessGame nextGame = new MarchMadnessGame(game.getNextGameID());
            if (game.getNextPosition() == 1) {
                nextGame.setTeamSeed1ID(game.getWinnerID());
            }
            else if (game.getNextPosition() == 2) {
                nextGame.setTeamSeed2ID(game.getWinnerID());
            }
            nextGame.Save();
        }

        // Update the loser to show they are out in the TournamentSeed table
        MarchMadnessTeamSeed loser = new MarchMadnessTeamSeed(tournamentId, loserId);
        loser.setTournamentWins(seasonWeek.getWeekNo() -1);
        loser.setStatus(MarchMadnessTeamSeed.Status.OUT.toString());
        loser.Save();

        // Update the winner's total tournament wins to be the round number (week no) and also ensure they are still alive
        MarchMadnessTeamSeed winner = new MarchMadnessTeamSeed(tournamentId, winnerId);
        winner.setTournamentWins(seasonWeek.getWeekNo());
        winner.setStatus(MarchMadnessTeamSeed.Status.IN.toString());
        winner.Save();

        // Update the BracketChallenge standings based on this game
        FSSeasonWeek bcWeek = FSSeasonWeek.GetFSSeasonWeekBySeasonWeekAndGameID(seasonWeek.getSeasonWeekID(), FSGame.BRACKET_CHALLENGE);
        BracketChallengeStandings.CalculateStandingsByGame(bcWeek, gameId, loserId);
        BracketChallengeStandings.CalculateRank(bcWeek.getFSSeasonWeekID());

        // Update the SeedChallenge standings based on this game
        FSSeasonWeek scWeek = FSSeasonWeek.GetFSSeasonWeekBySeasonWeekAndGameID(seasonWeek.getSeasonWeekID(), FSGame.SEED_CHALLENGE);
        SeedChallengeStandings.CalculateStandingsByGame(scWeek, tournamentId, winnerId, loserId);
        SeedChallengeStandings.CalculateRank(scWeek.getFSSeasonWeekID());
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GameID")) { setGameID(crs.getInt(prefix + "GameID")); }
            if (FSUtils.fieldExists(crs, prefix, "RoundID")) { setRoundID(crs.getInt(prefix + "RoundID")); }
            if (FSUtils.fieldExists(crs, prefix, "RegionID")) { setRegionID(crs.getInt(prefix + "RegionID")); }
            if (FSUtils.fieldExists(crs, prefix, "GameNumber")) { setGameNumber(crs.getInt(prefix + "GameNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "Status")) { setStatus(crs.getString(prefix + "Status")); }
            if (FSUtils.fieldExists(crs, prefix, "TeamSeed1ID")) { setTeamSeed1ID(crs.getInt(prefix + "TeamSeed1ID")); }
            if (FSUtils.fieldExists(crs, prefix, "TeamSeed2ID")) { setTeamSeed2ID(crs.getInt(prefix + "TeamSeed2ID")); }
            if (FSUtils.fieldExists(crs, prefix, "Team1Pts")) { setTeam1Pts(crs.getInt(prefix + "Team1Pts")); }
            if (FSUtils.fieldExists(crs, prefix, "Team2Pts")) { setTeam2Pts(crs.getInt(prefix + "Team2Pts")); }
            if (FSUtils.fieldExists(crs, prefix, "WinnerID")) { setWinnerID(crs.getInt(prefix + "WinnerID")); }
            if (FSUtils.fieldExists(crs, prefix, "NextGameID")) { setNextGameID(crs.getInt(prefix + "NextGameID")); }
            if (FSUtils.fieldExists(crs, prefix, "NextPosition")) { setNextPosition(crs.getInt(prefix + "NextPosition")); }
            if (FSUtils.fieldExists(crs, prefix, "Location")) { setLocation(crs.getString(prefix + "Location")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "MarchMadnessRound$", "RoundID")) { setRound(new MarchMadnessRound(crs, "MarchMadnessRound$")); }
            if (FSUtils.fieldExists(crs, "MarchMadnessRegion$", "RegionID")) { setRegion(new MarchMadnessRegion(crs, "MarchMadnessRegion$")); }
            if (FSUtils.fieldExists(crs, "TopTeamSeed$", "TeamSeedID")) { setTopTeamSeed(new MarchMadnessTeamSeed(crs, "TopTeamSeed$")); }
            if (FSUtils.fieldExists(crs, "BottomTeamSeed$", "TeamSeedID")) { setBottomTeamSeed(new MarchMadnessTeamSeed(crs, "BottomTeamSeed$")); }
            if (FSUtils.fieldExists(crs, "GameWinnerTeamSeed$", "TeamSeedID")) { setWinner(new MarchMadnessTeamSeed(crs, "GameWinnerTeamSeed$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

     /*  This method inserts a new record into the DB. */
    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO MarchMadnessGame ");
        sql.append("(GameID, RoundID, RegionID, GameNumber, Status, TeamSeed1ID, TeamSeed2ID, Team1Pts, Team2Pts, WinnerID, NextGameID, NextPosition, Location) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGameID()));
        sql.append(FSUtils.InsertDBFieldValue(getRoundID()));
        sql.append(FSUtils.InsertDBFieldValue(getRegionID()));
        sql.append(FSUtils.InsertDBFieldValue(getGameNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getStatus(), true));
        sql.append(FSUtils.InsertDBFieldValue(getTeamSeed1ID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeamSeed2ID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeam1Pts()));
        sql.append(FSUtils.InsertDBFieldValue(getTeam2Pts()));
        sql.append(FSUtils.InsertDBFieldValue(getWinnerID()));
        sql.append(FSUtils.InsertDBFieldValue(getNextGameID()));
        sql.append(FSUtils.InsertDBFieldValue(getNextPosition()));
        sql.append(FSUtils.InsertDBFieldValue(getLocation(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    /*  This method updates a record in the DB. */
    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE MarchMadnessGame SET ");
        sql.append(FSUtils.UpdateDBFieldValue("RoundID", getRoundID()));
        sql.append(FSUtils.UpdateDBFieldValue("RegionID", getRegionID()));
        sql.append(FSUtils.UpdateDBFieldValue("GameNumber", getGameNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("Status", getStatus(), true));
        sql.append(FSUtils.UpdateDBFieldValue("TeamSeed1ID", getTeamSeed1ID()));
        sql.append(FSUtils.UpdateDBFieldValue("TeamSeed2ID", getTeamSeed2ID()));
        sql.append(FSUtils.UpdateDBFieldValue("Team1Pts", getTeam1Pts()));
        sql.append(FSUtils.UpdateDBFieldValue("Team2Pts", getTeam2Pts()));
        sql.append(FSUtils.UpdateDBFieldValue("WinnerID", getWinnerID()));
        sql.append(FSUtils.UpdateDBFieldValue("NextGameID", getNextGameID()));
        sql.append(FSUtils.UpdateDBFieldValue("NextPosition", getNextPosition()));
        sql.append(FSUtils.UpdateDBFieldValue("Location", getLocation(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GameID = ").append(getGameID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
