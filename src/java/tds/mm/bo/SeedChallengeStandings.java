package tds.mm.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;
import tds.main.bo.FSLeague;
import tds.main.bo.FSSeasonWeek;
import tds.main.bo.FSTeam;

public class SeedChallengeStandings implements Serializable {

    // DB FIELDS
    private Integer _FSTeamID;    
    private Integer _FSSeasonWeekID;
    private Integer _Rank;
    private Integer _RoundPoints;
    private Integer _TotalPoints;
    private Integer _TeamsLeft;
    private String _Scenario;
    
    // OBJECTS
    private FSTeam _FSTeam;
    private FSSeasonWeek _FSSeasonWeek;
    
    // CONSTRUCTORS
    public SeedChallengeStandings() {
    }

    public SeedChallengeStandings(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }    

    // GETTERS
    public Integer getFSTeamID() { return _FSTeamID; }    
    public Integer getFSSeasonWeekID() { return _FSSeasonWeekID; }
    public Integer getRank() { return _Rank; }
    public Integer getRoundPoints() { return _RoundPoints; }
    public Integer getTotalPoints() { return _TotalPoints; }
    public Integer getTeamsLeft() { return _TeamsLeft; }    
    public String getScenario() { return _Scenario; }
    public FSTeam getFSTeam() { return _FSTeam; }
    public FSSeasonWeek getFSSeasonWeek() { return _FSSeasonWeek; }

    // SETTERS
    public void setFSTeamID(Integer fsTeamId) {_FSTeamID = fsTeamId;}    
    public void setFSSeasonWeekID(Integer FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setRank(Integer Rank) {_Rank = Rank;}
    public void setRoundPoints(Integer RoundPoints) {_RoundPoints = RoundPoints;}
    public void setTotalPoints(Integer TotalPoints) {_TotalPoints = TotalPoints;}
    public void setTeamsLeft(Integer TeamsLeft) {_TeamsLeft = TeamsLeft;}
    public void setScenario(String Scenario) {_Scenario = Scenario;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}

    // PUBLIC METHODS
    
    public static void CalculateRank(int fsSeasonWeekId) {     
        int rank = 0;
        int prevTotal = -1;
        
        List<SeedChallengeStandings> standings = SeedChallengeStandings.GetStandings(fsSeasonWeekId);
        for (int i=0; i < standings.size(); i++) {
             if (standings.get(i).getTotalPoints() != prevTotal) {
                 rank = i+1;                 
             }
             standings.get(i).setRank(rank);
             standings.get(i).Save();
             prevTotal = standings.get(i).getTotalPoints();
        }
    }
    
    /*  This method saves the standings after each game if the user had chosen the losing team */
    public static void CalculateStandingsByGame(FSSeasonWeek week, int tournamentId, int winningTeamId, int losingTeamId) {                           
        
        // Grab all user's standings records for the week
        List<SeedChallengeStandings> allTeamsPriorStandings = GetStandings(week.getFSSeasonWeekID());

        // If the prior standings for the week is empty than try and get the prior standing for the last fsSeasonWeek
        if (allTeamsPriorStandings.isEmpty()) {
            FSSeasonWeek priorWeek = new FSSeasonWeek(week.getFSSeasonID(), week.getFSSeasonWeekNo() - 1);
            allTeamsPriorStandings = (priorWeek.getFSSeasonWeekID() == null) ?  null : SeedChallengeStandings.GetStandings(priorWeek.getFSSeasonWeekID());
        }

        // Grab all the active leagues for the bracket challenge season
        List<FSLeague> fsLeagues = FSLeague.GetLeagues(week.getFSSeasonID());
        for (int i=0; i < fsLeagues.size(); i++) {

            // Grab all of the teams in the league
            List<FSTeam> fsTeams = FSTeam.GetLeagueTeams(fsLeagues.get(i).getFSLeagueID());                
            for (int j=0; j < fsTeams.size(); j++) {

                int roundPoints = 0;
                int teamsEliminated = 0;

                SeedChallengeStandings fsTeamPriorStandings = new SeedChallengeStandings();
                
                // Grab the user's previous standings record
                if (allTeamsPriorStandings != null) {
                    for (int k=0; k < allTeamsPriorStandings.size(); k++) {
                        
                        // Make sure we have the right fsTeam
                        if (fsTeams.get(j).getFSTeamID().equals(allTeamsPriorStandings.get(k).getFSTeamID())) {
                            fsTeamPriorStandings = allTeamsPriorStandings.get(k);
                            
                            // If we grabbed the standings from the prior week, reset the round related fields to 0
                            if (!fsTeamPriorStandings.getFSSeasonWeekID().equals(week.getFSSeasonWeekID())) {
                                fsTeamPriorStandings.setRoundPoints(0);
                            }
                            break;
                        }
                    }
                }

                // Grab all of the user's seed challenge game picks
                List<SeedChallenge> picks = SeedChallenge.GetSeedChallengePicks(tournamentId, fsTeams.get(j).getFSTeamID());
                for (int l=0; l < picks.size(); l++) {
                    if (picks.get(l).getTeamSeedPickedID() == null) {
                        break;
                    }
                    if (picks.get(l).getTeamSeedPickedID() == winningTeamId) {
                        roundPoints += (Math.pow(10, (week.getFSSeasonWeekNo() -1)));
                    }
                    if (picks.get(l).getTeamSeedPickedID() == losingTeamId) {
                        teamsEliminated += 1;                        
                    }
                }

                // Set the Standings object                    
                SeedChallengeStandings standingsRecord = new SeedChallengeStandings();
                standingsRecord.setFSTeamID(fsTeams.get(j).getFSTeamID());
                standingsRecord.setFSSeasonWeekID(week.getFSSeasonWeekID());
                standingsRecord.setRoundPoints((fsTeamPriorStandings.getFSSeasonWeekID() == null) ? roundPoints : roundPoints + FSUtils.ToInt(fsTeamPriorStandings.getRoundPoints()));
                standingsRecord.setTotalPoints((fsTeamPriorStandings.getFSSeasonWeekID() == null) ? roundPoints : roundPoints + FSUtils.ToInt(fsTeamPriorStandings.getTotalPoints()));
                standingsRecord.setTeamsLeft((fsTeamPriorStandings.getFSSeasonWeekID() == null) ? SeedChallengeGroup.GetSeedGroups(tournamentId).size() : FSUtils.ToInt(fsTeamPriorStandings.getTeamsLeft() - teamsEliminated));                
                standingsRecord.Save();
            }
        }
    }
    
    /*  This method calculates the standings for the seed challenge game for a given week (round) */
    public static void CalculateStandingsByRound(FSSeasonWeek week) {
        
        // Grab all of the standings records for the prior week
        FSSeasonWeek priorWeek = new FSSeasonWeek(week.getFSSeasonID(), week.getFSSeasonWeekNo() - 1);
        List<SeedChallengeStandings> priorStandings = (priorWeek.getFSSeasonWeekID() == null) ?  null : SeedChallengeStandings.GetStandings(priorWeek.getFSSeasonWeekID());
        
        // Grab all the active leagues for the seed challenge season
        List<FSLeague> fsLeagues = FSLeague.GetLeagues(week.getFSSeasonID());
        for (int i=0; i < fsLeagues.size(); i++) {

            // Grab all of the teams in the league
            List<FSTeam> fsTeams = FSTeam.GetLeagueTeams(fsLeagues.get(i).getFSLeagueID());                
            for (int j=0; j < fsTeams.size(); j++) {
                
                int roundPoints = 0;
                int teamsLeft = 0;
                SeedChallengeStandings priorStandingsForFSTeam = null;

                // Grab the user's previous standings record
                if (priorStandings != null) {
                    for (int l=0; l < priorStandings.size(); l++) {
                        // Make sure we have the right fsTeam
                        if (fsTeams.get(j).getFSTeamID().equals(priorStandings.get(l).getFSTeamID())) {
                            priorStandingsForFSTeam = priorStandings.get(l);
                            // If we grabbed the standings from the prior week, reset the round related fields to 0
                            if (!priorStandingsForFSTeam.getFSSeasonWeekID().equals(week.getFSSeasonWeekID())) {
                                priorStandingsForFSTeam.setRoundPoints(0);
                            }
                            break;
                        }
                    }
                }

                // Grab the user's picks for the round
                List<SeedChallenge> picks = SeedChallenge.GetSeedChallengePicks(new MarchMadnessLeague(fsLeagues.get(i).getFSLeagueID()).getTournamentID(), fsTeams.get(j).getFSTeamID());
                for (int k=0; k < picks.size(); k++) {
                    if (picks.get(k).getTeamSeedPickedID() != null && picks.get(k).getTeamSeedPicked().getStatus().equals(MarchMadnessTeamSeed.Status.IN.toString())) {
                        teamsLeft += 1;
                        roundPoints += (Math.pow(10, (week.getFSSeasonWeekNo() -1)));
                    }
                }

                // Set the Standings object                    
                SeedChallengeStandings standingsRecord = new SeedChallengeStandings();
                standingsRecord.setFSTeamID(fsTeams.get(j).getFSTeamID());
                standingsRecord.setFSSeasonWeekID(week.getFSSeasonWeekID());
                standingsRecord.setRoundPoints((priorStandingsForFSTeam == null) ? roundPoints : roundPoints + FSUtils.ToInt(priorStandingsForFSTeam.getRoundPoints()));
                standingsRecord.setTotalPoints((priorStandingsForFSTeam == null) ? roundPoints : roundPoints + FSUtils.ToInt(priorStandingsForFSTeam.getTotalPoints()));
                standingsRecord.setTeamsLeft(teamsLeft);
                standingsRecord.Save();
            }
        }
    }
    
    public static List<SeedChallengeStandings> GetStandings(Integer fsSeasonWeekId) {
        List<SeedChallengeStandings> standings = new ArrayList<SeedChallengeStandings>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("SeedChallengeStandings", "s.", "")).append(",");
        sql.append(_Cols.getColumnList("FSTeam", "t.", "FSTeam$")).append(",");
        sql.append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$"));
        sql.append("FROM SeedChallengeStandings s ");
        sql.append("JOIN FSTeam t ON t.FSTeamID = s.FSTeamID ");
        sql.append("JOIN FSSeasonWeek w ON w.FSSeasonWeekID = s.FSSeasonWeekID ");
        sql.append("WHERE s.FSSeasonWeekID = ").append(fsSeasonWeekId).append(" ");
        sql.append("ORDER BY TotalPoints desc, TeamsLeft desc");

        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                standings.add(new SeedChallengeStandings(crs, ""));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return standings;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("SeedChallengeStandings", "FSTeamID", getFSTeamID(), "FSSeasonWeekID", getFSSeasonWeekID());
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
            if (FSUtils.fieldExists(crs, prefix, "TeamsLeft")) { setTeamsLeft(crs.getInt(prefix + "TeamsLeft")); }            
            if (FSUtils.fieldExists(crs, prefix, "Scenario")) { setScenario(crs.getString(prefix + "Scenario")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) { setFSTeam(new FSTeam(crs, "FSTeam$")); }            
            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) { setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO SeedChallengeStandings ");
        sql.append("(FSTeamID, FSSeasonWeekID, Rank, RoundPoints, TotalPoints, TeamsLeft, Scenario) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getRank()));
        sql.append(FSUtils.InsertDBFieldValue(getRoundPoints()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalPoints()));
        sql.append(FSUtils.InsertDBFieldValue(getTeamsLeft()));
        sql.append(FSUtils.InsertDBFieldValue(getScenario(), true));        
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {        
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE SeedChallengeStandings SET ");
        sql.append(FSUtils.UpdateDBFieldValue("Rank", getRank()));
        sql.append(FSUtils.UpdateDBFieldValue("RoundPoints", getRoundPoints()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalPoints", getTotalPoints()));  
        sql.append(FSUtils.UpdateDBFieldValue("TeamsLeft", getTeamsLeft()));
        sql.append(FSUtils.UpdateDBFieldValue("Scenario", getScenario(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSTeamID = ").append(getFSTeamID()).append(" AND FSSeasonWeekID = ").append(getFSSeasonWeekID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}