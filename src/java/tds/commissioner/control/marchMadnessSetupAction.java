package tds.commissioner.control;

import bglib.util.FSUtils;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.*;
import tds.main.control.BaseAction;
import tds.mm.bo.*;
public class marchMadnessSetupAction extends BaseAction {
    
    private static final int NUM_TEAMS = 64;
    private static final int NUM_ROUNDS = 6;
    private static final int NUM_REGIONS = 4;
    private static final int SEED_CHALLENGE_ROSTER_SIZE = 8;
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int[] fantasyGames = {FSGame.BRACKET_CHALLENGE, FSGame.SEED_CHALLENGE};        
        String[] roundNames = { "Round 1", "Round 2", "Sweet Sixteen", "Elite Eight", "Final Four", "Championship" };
        int[] seedOrder = {1, 16, 8, 9, 5, 12, 4, 13, 6, 11, 3, 14, 7, 10, 2, 15 };
        int[] seedGroupStartingNumberOrder = { 1, 2, 3, 4, 6, 8, 11, 13 };       
        int[] seedGroupEndingNumberOrder = { 1, 2, 3, 5, 7, 10, 12, 16 };
        
        String nextPage = super.process(request,response);
        if (nextPage != null) { return nextPage; }

        nextPage = "commissioner/marchMadnessSetup";
        
        // Figure out the next unique ids
        int seasonId = FSUtils.GetHighestIdNumber("Season", "SeasonID") + 1;
        int seasonWeekId = FSUtils.GetHighestIdNumber("SeasonWeek", "SeasonWeekID") + 1;
        int fsSeasonId = FSUtils.GetHighestIdNumber("FSSeason", "FSSeasonID") + 1;
        int fsSeasonWeekId = FSUtils.GetHighestIdNumber("FSSeasonWeek", "FSSeasonWeekID") + 1;
        int fsLeagueId = FSUtils.GetHighestIdNumber("FSLeague", "FSLeagueID") + 1;
        int tournamentId = FSUtils.GetHighestIdNumber("MarchMadnessTournament", "TournamentID") + 1;
        int roundId = FSUtils.GetHighestIdNumber("MarchMadnessRound", "RoundID") + 1;
        int regionId = FSUtils.GetHighestIdNumber("MarchMadnessRegion", "RegionID") + 1;
        int gameId = FSUtils.GetHighestIdNumber("MarchMadnessGame", "GameID") + 1;
        int teamSeedId = FSUtils.GetHighestIdNumber("MarchMadnessTeamSeed", "TeamSeedID") + 1;
        int seedChallengeGroupId = FSUtils.GetHighestIdNumber("SeedChallengeGroup", "SeedChallengeGroupID") + 1;
        
        // Retrieve all of the request parameters
        String[] regions = { FSUtils.getRequestParameter(request, "region1Name", null), FSUtils.getRequestParameter(request, "region2Name", null), FSUtils.getRequestParameter(request, "region3Name", null), FSUtils.getRequestParameter(request, "region4Name", null), "Final Four" };

        // SEASON 
        Season season = new Season();
        season.setSeasonID(seasonId);
        season.setSportID(Sport.COLLEGE_BASKETBALL);
        season.setSeasonName(year + " March Madness");
        season.setIsActive(true);
        season.setSportYear(year);
        season.Save();
        
        // SEASON WEEK        
        for (int i=0; i < NUM_ROUNDS; i++ ) {  
            SeasonWeek seasonWeek = new SeasonWeek();
            seasonWeek.setSeasonWeekID(seasonWeekId + i);
            seasonWeek.setSeasonID(seasonId);
            seasonWeek.setWeekNo(i + 1);
            seasonWeek.setStatus((i + 1 == 1) ? SeasonWeek.Status.CURRENT.toString() : SeasonWeek.Status.PENDING.toString());
            seasonWeek.setWeekType((i + 1 == 1) ? SeasonWeek.WeekType.INITIAL.toString() : (i + 1 == NUM_ROUNDS) ? SeasonWeek.WeekType.FINAL.toString() : SeasonWeek.WeekType.MIDDLE.toString());
            seasonWeek.Save();
        }
        
        // FSSEASON        
        for (int i=0; i < fantasyGames.length; i++ ) {
            FSSeason fsSeason = new FSSeason();
            fsSeason.setFSSeasonID(fsSeasonId + i);
            fsSeason.setFSGameID(fantasyGames[i]);
            fsSeason.setSeasonID(seasonId);
            fsSeason.setIsActive(true);
            fsSeason.setDisplayTeams(true);
            fsSeason.setSeasonName((fantasyGames[i] == FSGame.BRACKET_CHALLENGE) ? year + " Bracket Challenge" : (fantasyGames[i] == FSGame.SEED_CHALLENGE) ? year + " Seed Challenge" : null);
            fsSeason.Save();
        }
        
        // FSSEASON WEEK
        int nextWeekId = fsSeasonWeekId;
        for (int i=0; i < fantasyGames.length; i++ ) {
            for (int j=0; j < NUM_ROUNDS; j++ ) {   
                FSSeasonWeek fsSeasonWeek = new FSSeasonWeek();
                fsSeasonWeek.setFSSeasonWeekID(nextWeekId);
                fsSeasonWeek.setFSSeasonID(fsSeasonId + i);
                fsSeasonWeek.setSeasonWeekID(seasonWeekId + j);
                fsSeasonWeek.setFSSeasonWeekNo(j + 1);
                fsSeasonWeek.setStatus((j + 1 == 1) ? FSSeasonWeek.Status.CURRENT.toString() : FSSeasonWeek.Status.PENDING.toString());
                fsSeasonWeek.setWeekType((j + 1 == 1) ? SeasonWeek.WeekType.INITIAL.toString() : (j + 1 == NUM_ROUNDS) ? SeasonWeek.WeekType.FINAL.toString() : SeasonWeek.WeekType.MIDDLE.toString());
                fsSeasonWeek.Save();
                nextWeekId += 1;
            }
        }
        
        // FSLEAGUE        
        for (int i=0; i < fantasyGames.length; i++ ) {
            FSLeague fsLeague = new FSLeague();
            fsLeague.setFSLeagueID(fsLeagueId + i);
            fsLeague.setFSSeasonID(fsSeasonId + i);
            fsLeague.setLeagueName((fantasyGames[i] == FSGame.BRACKET_CHALLENGE) ? "Bracket Challenge" : (fantasyGames[i] == FSGame.SEED_CHALLENGE) ? "Seed Challenge" : null);
            fsLeague.setIsDefaultLeague(1);
            fsLeague.setStatus(FSLeague.Status.ACTIVE.toString());
            fsLeague.Save();
        }
        
        // FSGAME
        for (int i=0; i < fantasyGames.length; i++ ) {
           FSGame fsGame = new FSGame(fantasyGames[i]);
           fsGame.setCurrentFSSeasonID(fsSeasonId + i);
           fsGame.Save(); 
        }
        
        // MARCH MADNESS TOURNAMENT
        MarchMadnessTournament tournament = new MarchMadnessTournament();
        tournament.setTournamentID(tournamentId);
        tournament.setTournamentName("March Madness "+year);
        tournament.setSportYear(year);
        tournament.setStatus(MarchMadnessTournament.Status.UPCOMING.toString());
        tournament.setNumTeams(NUM_TEAMS);
        tournament.setNumRounds(NUM_ROUNDS);
        tournament.setNumRegions(NUM_REGIONS);
        tournament.Save();
        
        // MARCH MADNESS LEAGUE
        for (int i=0; i < fantasyGames.length; i++ ) {
            MarchMadnessLeague.Insert(fsLeagueId + i, tournamentId);
        }        
        
        // MARCH MADNESS ROUND
        int roundTeams = NUM_TEAMS;        
        for (int i=0; i < NUM_ROUNDS; i++ ) {         
            MarchMadnessRound round = new MarchMadnessRound();
            round.setRoundID(roundId + i);
            round.setTournamentID(tournamentId);
            round.setSeasonWeekID(seasonWeekId + i);
            round.setRoundNumber(i + 1);
            round.setRoundName(roundNames[i]);
            round.setNumTeams (roundTeams);
            round.Save();            
            roundTeams /= 2;
        }
        
        // MARCH MADNESS REGION
        for (int i=0; i < (NUM_REGIONS + 1); i++ ) {
            MarchMadnessRegion region = new MarchMadnessRegion();
            region.setRegionID(regionId + i);
            region.setTournamentID(tournamentId);
            region.setRegionName(regions[i]);
            region.setRegionNumber(i + 1);
            region.setStartingRoundID((i < NUM_REGIONS) ? roundId : roundId + NUM_REGIONS);
            region.setType((i < NUM_REGIONS) ? (i % 2 == 0) ? MarchMadnessRegion.Type.UPPER.toString() : MarchMadnessRegion.Type.LOWER.toString() : MarchMadnessRegion.Type.FINAL.toString());
            region.setNextRegionID((i < NUM_REGIONS) ? regionId + NUM_REGIONS : null);
            region.Save();
        }        
        
        // MARCH MADNESS TEAM SEED
        int seedId = teamSeedId;
        for (int i=0; i < NUM_REGIONS; i++ ) {
            for (int j=0; j < (NUM_TEAMS / NUM_REGIONS); j++ ) {
                MarchMadnessTeamSeed teamSeed = new MarchMadnessTeamSeed();
                teamSeed.setTeamSeedID(seedId);
                teamSeed.setTournamentID(tournamentId);
                teamSeed.setRegionID(regionId + i);
                teamSeed.setSeedNumber(seedOrder[j]);
                teamSeed.setStatus(MarchMadnessTeamSeed.Status.IN.toString());
                teamSeed.setTournamentWins(0);
                teamSeed.Save();
                seedId += 1;
            }            
        }
        
        // MARCH MADNESS GAME
        int cumulativeGameId = gameId;
        int cumulativeTeamSeedId = teamSeedId;
        int gameNumber = 1;
        int numTeams =  NUM_TEAMS;
        int matchupsInRegionThisRound = 0;
        int nextGameId = gameId + (NUM_TEAMS / 2);
        for (int i=0; i < NUM_ROUNDS; i++ ) { 
            numTeams /= 2;
            matchupsInRegionThisRound = numTeams / NUM_REGIONS;

            for (int j=0; j < numTeams; j++) {
                MarchMadnessGame game = new MarchMadnessGame();
                game.setGameID(cumulativeGameId);
                game.setRoundID(roundId + i);
                game.setRegionID((matchupsInRegionThisRound == 0) ? regionId + NUM_REGIONS: regionId + (j / matchupsInRegionThisRound));
                game.setGameNumber(gameNumber);
                if (i==0) { game.setTeamSeed1ID(cumulativeTeamSeedId); cumulativeTeamSeedId += 1; }
                if (i==0) { game.setTeamSeed2ID(cumulativeTeamSeedId); }
                game.setStatus(MarchMadnessGame.Status.UPCOMING.toString());
                game.setNextGameID((gameNumber == NUM_TEAMS - 1) ? null : nextGameId);
                game.setNextPosition((gameNumber == NUM_TEAMS - 1) ? null : (gameNumber % 2 == 0) ? 2 : 1);
                game.Save();
              
                cumulativeGameId += 1;
                cumulativeTeamSeedId += 1;
                gameNumber += 1;
                nextGameId = (gameNumber % 2 == 0) ? nextGameId : nextGameId + 1;
            }
        }

        // SEED CHALLENGE GROUP
        for (int i=0; i < SEED_CHALLENGE_ROSTER_SIZE; i++ ) {
            SeedChallengeGroup group = new SeedChallengeGroup();
            group.setSeedChallengeGroupID(seedChallengeGroupId + i);
            group.setTournamentID(tournamentId);
            group.setStartingSeedNumber(seedGroupStartingNumberOrder[i]);
            group.setEndingSeedNumber(seedGroupEndingNumberOrder[i]);
            group.Save();
        }

        return nextPage;
    }
}