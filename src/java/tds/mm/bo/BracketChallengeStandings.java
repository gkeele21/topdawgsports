package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;
import tds.main.bo.FSLeague;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.FSTeam;

public class BracketChallengeStandings implements Serializable {

    // DB FIELDS
    private Integer _FSTeamID;    
    private Integer _FSSeasonWeekID;
    private Integer _Rank;
    private Integer _RoundPoints;
    private Integer _TotalPoints;
    private Integer _MaxPossible;
    
    // OBJECTS
    private FSTeam _FSTeam;
    private FSSeasonWeek _FSSeasonWeek;
    
    // ADDITIONAL FIELDS    
    private String _ChampWinner;
    private String _Region1Winner;
    private String _Region2Winner;
    private String _Region3Winner;
    private String _Region4Winner;
    
    // CONSTRUCTORS
    public BracketChallengeStandings() {
    }

    public BracketChallengeStandings(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }    

    // GETTERS
    public Integer getFSTeamID() { return _FSTeamID; }    
    public Integer getFSSeasonWeekID() { return _FSSeasonWeekID; }
    public Integer getRank() { return _Rank; }
    public Integer getRoundPoints() { return _RoundPoints; }
    public Integer getTotalPoints() { return _TotalPoints; }
    public Integer getMaxPossible() { return _MaxPossible; }
    public FSTeam getFSTeam() { return _FSTeam; }
    public FSSeasonWeek getFSSeasonWeek() { return _FSSeasonWeek; }    
    public String getChampWinner() { return _ChampWinner; }
    public String getRegion1Winner() { return _Region1Winner; }
    public String getRegion2Winner() { return _Region2Winner; }
    public String getRegion3Winner() { return _Region3Winner; }
    public String getRegion4Winner() { return _Region4Winner; }

    // SETTERS
    public void setFSTeamID(Integer fsTeamId) {_FSTeamID = fsTeamId;}    
    public void setFSSeasonWeekID(Integer FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setRank(Integer Rank) {_Rank = Rank;}
    public void setRoundPoints(Integer RoundPoints) {_RoundPoints = RoundPoints;}
    public void setTotalPoints(Integer TotalPoints) {_TotalPoints = TotalPoints;}
    public void setMaxPossible(Integer MaxPossible) {_MaxPossible = MaxPossible;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}    
    public void setChampWinner(String ChampWinner) {_ChampWinner = ChampWinner;}
    public void setRegion1Winner(String Region1Winner) {_Region1Winner = Region1Winner;}
    public void setRegion2Winner(String Region2Winner) {_Region2Winner = Region2Winner;}
    public void setRegion3Winner(String Region3Winner) {_Region3Winner = Region3Winner;}
    public void setRegion4Winner(String Region4Winner) {_Region4Winner = Region4Winner;}
    
    // PUBLIC METHODS
    
    public static void CalculateRank(int fsSeasonWeekId) {     
        int rank = 0;
        int prevTotal = 0;
        
        List<BracketChallengeStandings> standings = BracketChallengeStandings.GetStandings(fsSeasonWeekId);
        for (int i=0; i < standings.size(); i++) {
             if (standings.get(i).getTotalPoints() != prevTotal) {
                 rank = i+1;                 
             }
             standings.get(i).setRank(rank);
             standings.get(i).Save();
             prevTotal = standings.get(i).getTotalPoints();
        }
    }
    
    /*  This method calculates the standings for the bracket challenge game for a given week (round) */
    public static void CalculateStandingsByGame(FSSeasonWeek week, int gameId, int losingTeamSeedId) {
        
        // Grab all fsTeam's picks regardless of league for the specified game matchup
        List<BracketChallenge> allTeamsPicks = BracketChallenge.GetPicksByGame(gameId);
        
        // Grab all fsTeam's picks of the losing teams seed for any future round picks (needed to calculate the max possible score in standings)
        List<BracketChallenge> allTeamsFuturePicksOfLosingTeam = BracketChallenge.GetFutureTeamSeedPicksFromGameLoser(losingTeamSeedId, week.getFSSeasonWeekNo());
        
        // Grab all user's standings records for the week
        List<BracketChallengeStandings> allTeamsPriorStandings = BracketChallengeStandings.GetStandings(week.getFSSeasonWeekID());

        // If the prior standings for the week is empty than try and get the prior standing for the last fsSeasonWeek
        if (allTeamsPriorStandings.isEmpty()) {
            FSSeasonWeek priorWeek = new FSSeasonWeek(week.getFSSeasonID(), week.getFSSeasonWeekNo() - 1);
            allTeamsPriorStandings = (priorWeek.getFSSeasonWeekID() == null) ?  null : BracketChallengeStandings.GetStandings(priorWeek.getFSSeasonWeekID());
        }

        // Grab all the active leagues for the bracket challenge season
        List<FSLeague> fsLeagues = FSLeague.GetLeagues(week.getFSSeasonID());
        for (int i=0; i < fsLeagues.size(); i++) { 

            // Grab all of the teams in the league
            List<FSTeam> fsTeams = FSTeam.GetLeagueTeams(fsLeagues.get(i).getFSLeagueID()); 
            for (int j=0; j < fsTeams.size(); j++) {

                int gameMatchupPoints = 0;
                int maxPointsToMinus = 0;
                BracketChallengeStandings fsTeamPriorStandings = new BracketChallengeStandings();
                
                // Loop through to find this specific fsTeam's previous standings record
                if (allTeamsPriorStandings != null) {
                    for (int k=0; k < allTeamsPriorStandings.size(); k++) {
                        
                        // Make sure we have the right fsTeam and if so set the priorStandingsForFSTeam variable to their prior standings record
                        if (fsTeams.get(j).getFSTeamID().equals(allTeamsPriorStandings.get(k).getFSTeamID())) {
                            fsTeamPriorStandings = allTeamsPriorStandings.get(k);
                            // Since we grabbed the standings from the prior week, reset the round related fields to 0
                            if (!fsTeamPriorStandings.getFSSeasonWeekID().equals(week.getFSSeasonWeekID())) {
                                fsTeamPriorStandings.setRoundPoints(0);
                            }
                            break;
                        }
                    }
                }
                
                // Calculate the points for the specified game
                for (int l=0; l < allTeamsPicks.size(); l++) {                    
                    if (allTeamsPicks.get(l).getFSTeamID().equals(fsTeams.get(j).getFSTeamID())) {
                        if (allTeamsPicks.get(l).getTeamSeedPickedID() != null && allTeamsPicks.get(l).getTeamSeedPickedID().equals(allTeamsPicks.get(l).getGame().getWinnerID())) {
                            gameMatchupPoints += 1 * (Math.pow(2, (week.getFSSeasonWeekNo() -1))); 
                        }
                        break;
                    }
                }
                
                // Calculate the max possible
                for (int m=0; m < allTeamsFuturePicksOfLosingTeam.size(); m++) {
                    if (allTeamsFuturePicksOfLosingTeam.get(m).getFSTeamID().equals(fsTeams.get(j).getFSTeamID())) {
                        maxPointsToMinus += Math.pow(2, allTeamsFuturePicksOfLosingTeam.get(m).getGame().getRound().getRoundNumber() - 1);
                    }
                }
                
                // Create the standings record with the updated fields
                BracketChallengeStandings standingsRecord = new BracketChallengeStandings();
                standingsRecord.setFSTeamID(fsTeams.get(j).getFSTeamID());
                standingsRecord.setFSSeasonWeekID(week.getFSSeasonWeekID());
                standingsRecord.setRoundPoints((fsTeamPriorStandings.getFSSeasonWeekID() == null) ? gameMatchupPoints : gameMatchupPoints + FSUtils.ToInt(fsTeamPriorStandings.getRoundPoints()));
                standingsRecord.setTotalPoints((fsTeamPriorStandings.getFSSeasonWeekID() == null) ? gameMatchupPoints : gameMatchupPoints + FSUtils.ToInt(fsTeamPriorStandings.getTotalPoints()));
                standingsRecord.setMaxPossible((fsTeamPriorStandings.getFSSeasonWeekID() == null) ? BracketChallenge.MAX_SCORE - maxPointsToMinus : FSUtils.ToInt(fsTeamPriorStandings.getMaxPossible()) - maxPointsToMinus);
                standingsRecord.Save();
            }
        }
    }
    
    /*  This method calculates the standings for the bracket challenge game for a given week (round) */
    public static void CalculateStandingsByRound(FSSeasonWeek week) {
        // Grab all of the standings records for the prior week
        FSSeasonWeek priorWeek = new FSSeasonWeek(week.getFSSeasonID(), week.getFSSeasonWeekNo() - 1);
        List<BracketChallengeStandings> allTeamsPriorStandings = (priorWeek.getFSSeasonWeekID() == null) ?  null : BracketChallengeStandings.GetStandings(priorWeek.getFSSeasonWeekID());

        // Grab all the active leagues for the bracket challenge season
        List<FSLeague> fsLeagues = FSLeague.GetLeagues(week.getFSSeasonID());
        for (int i=0; i < fsLeagues.size(); i++) {

            // Grab all of the teams in the league
            List<FSTeam> fsTeams = FSTeam.GetLeagueTeams(fsLeagues.get(i).getFSLeagueID());        
            for (int j=0; j < fsTeams.size(); j++) {

                int roundPoints = 0;
                BracketChallengeStandings fsTeamPriorStandings = null;

                // Grab the user's previous standings record
                if (allTeamsPriorStandings != null) {
                    for (int l=0; l < allTeamsPriorStandings.size(); l++) {
                        // Make sure we have the right fsTeam
                        if (fsTeams.get(j).getFSTeamID().equals(allTeamsPriorStandings.get(l).getFSTeamID())) {
                            fsTeamPriorStandings = allTeamsPriorStandings.get(l);
                            // If we grabbed the standings from the prior week, reset the round related fields to 0
                            if (!fsTeamPriorStandings.getFSSeasonWeekID().equals(week.getFSSeasonWeekID())) {
                                fsTeamPriorStandings.setRoundPoints(0);
                            }
                            break;
                        }
                    }
                }

                // Grab the user's picks for the round and calculate points
                int tournamentId = new MarchMadnessLeague(fsLeagues.get(i).getFSLeagueID()).getTournamentID();
                List<BracketChallenge> picks = BracketChallenge.GetPicks(tournamentId, fsTeams.get(j).getFSTeamID(), week.getFSSeasonWeekNo());
                for (int k=0; k < picks.size(); k++) {
                    if (picks.get(k).getTeamSeedPickedID() != null && (picks.get(k).getTeamSeedPickedID().equals(picks.get(k).getGame().getWinnerID()))) {
                        roundPoints += 1 * (Math.pow(2, (week.getFSSeasonWeekNo() -1))); 
                    }
                }     

                // Create the standings record with the updated fields
                BracketChallengeStandings standingsRecord = new BracketChallengeStandings();
                standingsRecord.setFSTeamID(fsTeams.get(j).getFSTeamID());
                standingsRecord.setFSSeasonWeekID(week.getFSSeasonWeekID());
                standingsRecord.setRoundPoints((fsTeamPriorStandings == null) ? roundPoints : roundPoints + FSUtils.ToInt(fsTeamPriorStandings.getRoundPoints()));
                standingsRecord.setTotalPoints((fsTeamPriorStandings == null) ? roundPoints : roundPoints + FSUtils.ToInt(fsTeamPriorStandings.getTotalPoints()));
                standingsRecord.setMaxPossible(GetMaxPossibleScore(fsTeams.get(j).getFSTeamID(), tournamentId, week.getFSSeasonWeekNo()));
                standingsRecord.Save();
            }
        }
    }
    
    public static Integer GetMaxPossibleScore(int fsTeamId, int tournamentId, int roundNumber) {
        Integer maxScore = null;
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("SUM(CASE WHEN Status='").append(MarchMadnessTeamSeed.Status.OUT.toString()).append("' ");
        sql.append("AND TournamentWins < ").append(roundNumber).append(" ");
        sql.append("AND TournamentWins < Round ");
        sql.append("THEN POWER(2,TournamentWins) -1 ELSE POWER(2,Round) -1 END) As MaxPossible ");
        sql.append("FROM (SELECT TeamSeedID, Status, TournamentWins, ");
        sql.append("(SELECT max(rd.RoundNumber) FROM BracketChallenge bc JOIN MarchMadnessGame g ON g.GameID = bc.GameID JOIN MarchMadnessRound rd ON rd.roundID = g.roundID ");
        sql.append("WHERE bc.TeamSeedPickedID = ts.TeamSeedID AND bc.FSTeamID = ").append(fsTeamId).append(") as Round ");
        sql.append("FROM MarchMadnessTeamSeed ts ");
        sql.append("WHERE TournamentID = ").append(tournamentId).append(") Alias");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            maxScore = (int)crs.getDouble("MaxPossible");

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }        
        
        return maxScore;
    }
    
    public static List<BracketChallengeStandings> GetStandings(int fsSeasonWeekId) {
        List<BracketChallengeStandings> standings = new ArrayList<BracketChallengeStandings>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("BracketChallengeStandings", "s.", "")).append(",");
        sql.append(_Cols.getColumnList("FSTeam", "t.", "FSTeam$")).append(",");
        sql.append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$")).append(",");
        sql.append("(SELECT DisplayName FROM BracketChallenge bc JOIN MarchMadnessGame g ON g.GameID = bc.GameID JOIN MarchMadnessTeamSeed ts ON ts.TeamSeedID = bc.TeamSeedPickedID JOIN Team t ON t.TeamID = ts.TeamID WHERE bc.FSTeamID = s.FSTeamID AND g.GameNumber = 63) as ChampWinner, ");
        sql.append("(SELECT DisplayName FROM BracketChallenge bc JOIN MarchMadnessGame g ON g.GameID = bc.GameID JOIN MarchMadnessTeamSeed ts ON ts.TeamSeedID = bc.TeamSeedPickedID JOIN Team t ON t.TeamID = ts.TeamID WHERE bc.FSTeamID = s.FSTeamID AND g.GameNumber = 57) as Region1Winner, ");
        sql.append("(SELECT DisplayName FROM BracketChallenge bc JOIN MarchMadnessGame g ON g.GameID = bc.GameID JOIN MarchMadnessTeamSeed ts ON ts.TeamSeedID = bc.TeamSeedPickedID JOIN Team t ON t.TeamID = ts.TeamID WHERE bc.FSTeamID = s.FSTeamID AND g.GameNumber = 58) as Region2Winner, ");
        sql.append("(SELECT DisplayName FROM BracketChallenge bc JOIN MarchMadnessGame g ON g.GameID = bc.GameID JOIN MarchMadnessTeamSeed ts ON ts.TeamSeedID = bc.TeamSeedPickedID JOIN Team t ON t.TeamID = ts.TeamID WHERE bc.FSTeamID = s.FSTeamID AND g.GameNumber = 59) as Region3Winner, ");
        sql.append("(SELECT DisplayName FROM BracketChallenge bc JOIN MarchMadnessGame g ON g.GameID = bc.GameID JOIN MarchMadnessTeamSeed ts ON ts.TeamSeedID = bc.TeamSeedPickedID JOIN Team t ON t.TeamID = ts.TeamID WHERE bc.FSTeamID = s.FSTeamID AND g.GameNumber = 60) as Region4Winner ");
        sql.append("FROM BracketChallengeStandings s ");
        sql.append("JOIN FSTeam t ON t.FSTeamID = s.FSTeamID ");
        sql.append("JOIN FSSeasonWeek w ON w.FSSeasonWeekID = s.FSSeasonWeekID ");
        sql.append("WHERE s.FSSeasonWeekID = ").append(fsSeasonWeekId).append(" ");
        sql.append("ORDER BY TotalPoints desc, MaxPossible desc, RoundPoints desc");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                standings.add(new BracketChallengeStandings(crs, ""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return standings;
    }
    
    public static Map<Integer, Integer> GetAllRoundStandingsForFSTeam(int tournamentId, int fsTeamId) {
        Map<Integer, Integer> roundStandings = new HashMap<Integer, Integer>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT").append(_Cols.getColumnList("BracketChallengeStandings", "s.", "")).append(",");
        sql.append(_Cols.getColumnList("SeasonWeek", "sw.", "SeasonWeek$"));
        sql.append("FROM SeasonWeek sw ");
        sql.append("JOIN MarchMadnessRound rd ON rd.SeasonWeekID = sw.SeasonWeekID ");
        sql.append("JOIN FSSeasonWeek fssw ON fssw.SeasonWeekID = sw.SeasonWeekID ");
        sql.append("JOIN BracketChallengeStandings s ON s.FSSeasonWeekID = fssw.FSSeasonWeekID AND s.FSTeamID = ").append(fsTeamId).append(" ");
        sql.append("WHERE rd.TournamentID = ").append(tournamentId);

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                roundStandings.put(crs.getInt("SeasonWeek$SeasonWeekID"), crs.getInt("RoundPoints"));
                roundStandings.put(-1, crs.getInt("TotalPoints"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return roundStandings;
    }
        
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("BracketChallengeStandings", "FSTeamID", getFSTeamID(), "FSSeasonWeekID", getFSSeasonWeekID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /*  This method populates the object from a cached row set.  */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) { setFSTeamID(crs.getInt(prefix + "FSTeamID")); }           
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) { setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID")); }           
            if (FSUtils.fieldExists(crs, prefix, "Rank")) { setRank(crs.getInt(prefix + "Rank")); }           
            if (FSUtils.fieldExists(crs, prefix, "RoundPoints")) { setRoundPoints(crs.getInt(prefix + "RoundPoints")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalPoints")) { setTotalPoints(crs.getInt(prefix + "TotalPoints")); }         
            if (FSUtils.fieldExists(crs, prefix, "MaxPossible")) { setMaxPossible(crs.getInt(prefix + "MaxPossible")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) { setFSTeam(new FSTeam(crs, "FSTeam$"));}           
            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) { setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$")); }
			
            // ADDITIONAL FIELDS
            if (FSUtils.fieldExists(crs, prefix, "ChampWinner")) { setChampWinner(crs.getString(prefix + "ChampWinner"));}           
            if (FSUtils.fieldExists(crs, prefix, "Region1Winner")) { setRegion1Winner(crs.getString(prefix + "Region1Winner")); }           
            if (FSUtils.fieldExists(crs, prefix, "Region2Winner")) { setRegion2Winner(crs.getString(prefix + "Region2Winner")); }           
            if (FSUtils.fieldExists(crs, prefix, "Region3Winner")) { setRegion3Winner(crs.getString(prefix + "Region3Winner")); }           
            if (FSUtils.fieldExists(crs, prefix, "Region4Winner")) { setRegion4Winner(crs.getString(prefix + "Region4Winner")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO BracketChallengeStandings ");
        sql.append("(FSTeamID, FSSeasonWeekID, Rank, RoundPoints, TotalPoints, MaxPossible) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getRank()));
        sql.append(FSUtils.InsertDBFieldValue(getRoundPoints()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalPoints()));
        sql.append(FSUtils.InsertDBFieldValue(getMaxPossible()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE BracketChallengeStandings SET ");
        sql.append(FSUtils.UpdateDBFieldValue("Rank", getRank()));
        sql.append(FSUtils.UpdateDBFieldValue("RoundPoints", getRoundPoints()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalPoints", getTotalPoints()));        
        sql.append(FSUtils.UpdateDBFieldValue("MaxPossible", getMaxPossible()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSTeamID = ").append(getFSTeamID()).append(" AND FSSeasonWeekID = ").append(getFSSeasonWeekID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}