package tds.main.control;

import bglib.util.AuDate;
import bglib.util.BGConstants;
import bglib.util.FSUtils;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.collegeloveleave.bo.CollegeLoveLeave;
import tds.collegepickem.bo.CollegePickem;
import tds.main.bo.*;
import tds.mm.bo.*;
import tds.proloveleave.bo.ProLoveLeave;
import tds.propickem.bo.ProPickem;

public class AjaxAction extends HttpServlet {
    
    @Override
    public String getServletInfo() {
        return "Ajax Servlet";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        System.out.println("Made it into AjaxAction.");
        String ret = "";
        try {
            String method = request.getParameter("method");

            if (method.equals("AssignTop25Ranking")) {
                ret = AssignTop25Ranking(request);
            }
            
            else if (method.equals("FinalizeWeek")) {
                ret = FinalizeWeek(request);
            }
            
            else if (method.equals("SaveGameMatchup")) {
                ret = SaveGameMatchup(request);
            }
            
            else if (method.equals("SavePickemConfidencePts")) {
                ret = SavePickemConfidencePts(request);
            }
            
            else if (method.equals("SavePickemPick")) {
                ret = SavePickemPick(request);
            }
            
            else if (method.equals("SubmitBracketChallengePick")) {
                ret = SubmitBracketChallengePick(request);
            }
            
            else if (method.equals("SubmitSeedChallengePick")) {
                ret = SubmitSeedChallengePick(request);
            }
            
            else if (method.equals("UpdateMarchMadnessGameResult")) {
                ret = UpdateMarchMadnessGameResult(request);
            }
            
            else if (method.equals("UpdateTeamSeed")) {
                ret = UpdateTeamSeed(request);
            }
            
        } 
        catch (Exception e) { ret = e.getMessage(); }
       

    }
    
    public String AssignTop25Ranking(HttpServletRequest request) {
        try {
            int seasonWeekId = FSUtils.getIntRequestParameter(request,"sw", 0);                
            int teamId = FSUtils.getIntRequestParameter(request,"tid", 0);
            int rank = FSUtils.getIntRequestParameter(request,"rk", 0);
            
            Standings standings = new Standings(teamId, seasonWeekId);
            standings.setTeamID(teamId);
            standings.setOverallRanking(rank);
            standings.Save();            
        } 
        catch (Exception e) { return "<result>AssignTop25Ranking error : " + e.getMessage() + "</result>"; }

        return "<result>Success</result>";
 
    }
    
    public String FinalizeWeek(HttpServletRequest request) {
        try {
            int seasonWeekId = FSUtils.getIntRequestParameter(request,"sw", 0);                
            int finishOffWeek = FSUtils.getIntRequestParameter(request,"fw", 0);
            int sportId = FSUtils.getIntRequestParameter(request,"spid", 0);

            // Grab all of the FSSeasonWeek's to calculate standings for (most likely for multiple fsGames)
            List<FSSeasonWeek> fsSeasonWeeks = FSSeasonWeek.GetFSSeasonWeeks(seasonWeekId);
                
            for (int i=0; i < fsSeasonWeeks.size(); i++) {                
                
                if (finishOffWeek == 1) { FSSeasonWeek.CompleteFSSeasonWeek(fsSeasonWeeks.get(i)); }
                
                switch(fsSeasonWeeks.get(i).getFSSeason().getFSGameID()) {
                    case FSGame.SALARY_CAP:
                        FSFootballStandings.CalculateRankForAllLeagues(fsSeasonWeeks.get(i), "TotalGamePoints"); break;
                    case FSGame.PRO_PLAYER_LOVELEAVE:
                        FSFootballStandings.CalculateRankForAllLeagues(fsSeasonWeeks.get(i), "TotalFantasyPts"); break;
                    case FSGame.PRO_PICKEM:
                        ProPickem.CalculateStandings(fsSeasonWeeks.get(i)); break;
                    case FSGame.PRO_LOVEEMLEAVEEM:
                        ProLoveLeave.CalculateStandings(fsSeasonWeeks.get(i)); break;
                    case FSGame.COLLEGE_PICKEM:
                        CollegePickem.CalculateStandings(fsSeasonWeeks.get(i)); break;
                    case FSGame.COLLEGE_LOVEEMLEAVEEM:
                        CollegeLoveLeave.CalculateStandings(fsSeasonWeeks.get(i)); break;
                    case FSGame.BRACKET_CHALLENGE:
                        BracketChallengeStandings.CalculateStandingsByRound(fsSeasonWeeks.get(i)); 
                        BracketChallengeStandings.CalculateRank(fsSeasonWeeks.get(i).getFSSeasonWeekID()); break;
                    case FSGame.SEED_CHALLENGE:
                        SeedChallengeStandings.CalculateStandingsByRound(fsSeasonWeeks.get(i));
                        SeedChallengeStandings.CalculateRank(fsSeasonWeeks.get(i).getFSSeasonWeekID()); break;
                }
            }

            if (finishOffWeek == 1) { SeasonWeek.CompleteSeasonWeek(fsSeasonWeeks.get(0).getSeasonWeek()); }
                        
            if (sportId == Sport.PRO_FOOTBALL || sportId == Sport.COLLEGE_FOOTBALL) { 
                Standings.UpdateByeTeamStandings(fsSeasonWeeks.get(0).getSeasonWeek()); 
            }
            
        } 
        catch (Exception e) { return "<result>FinalizeWeek error : " + e.getMessage() + "</result>"; }

        return "<result>Success</result>";
 
    }

    public String SaveGameMatchup(HttpServletRequest request) {
 
        try {
            // Retrieve parameters
            int gameId = FSUtils.getIntRequestParameter(request,"gid", 0);
            AuDate gameDate = AuDate.getInstance(request.getParameter("gd"), BGConstants.PLAYDATETIME_PATTERN);
            int seasonWeekId = FSUtils.getIntRequestParameter(request,"wk", 0);
            
            int visitorId = FSUtils.getIntRequestParameter(request,"vid", 0);
            int visitorRanking = FSUtils.getIntRequestParameter(request,"vr", 0);
            int visitorScore = FSUtils.getIntRequestParameter(request,"vs", 0);
            int visitorWins = FSUtils.getIntRequestParameter(request,"vw", 0);
            int visitorLosses = FSUtils.getIntRequestParameter(request,"vl", 0);
            
            int homeId = FSUtils.getIntRequestParameter(request,"hid", 0);
            int homeRanking = FSUtils.getIntRequestParameter(request,"hr", 0);
            int homeScore = FSUtils.getIntRequestParameter(request,"hs", 0);            
            int homeWins = FSUtils.getIntRequestParameter(request,"hw", 0);
            int homeLosses = FSUtils.getIntRequestParameter(request,"hl", 0);
            
            // Update the Game results
            Game game = new Game(gameId);
            game.setGameDate(gameDate);
            game.setVisitorPts(visitorScore);
            game.setHomePts(homeScore);
               
            if (visitorScore > homeScore) {
                game.setWinnerID(visitorId);
                visitorWins += 1;
                homeLosses += 1;
            }
            else if (homeScore > visitorScore) {
                game.setWinnerID(homeId);
                visitorLosses += 1;
                homeWins += 1;
            }
            
            else {
                game.setWinnerID(0);
            }

            // Save to the DB
            game.Save();
            
            // Update the Visitor Team's Standings
            Standings visitorStandings = new Standings(visitorId, seasonWeekId);
            visitorStandings.setTeamID(visitorId);
            visitorStandings.setSeasonWeekID(seasonWeekId);
            visitorStandings.setOverallRanking(visitorRanking);
            visitorStandings.setWins(visitorWins);
            visitorStandings.setLosses(visitorLosses);
            visitorStandings.Save();
            
            // Update the Home Team's Standings
            Standings homeStandings = new Standings(homeId, seasonWeekId);
            homeStandings.setTeamID(homeId);
            homeStandings.setSeasonWeekID(seasonWeekId);
            homeStandings.setOverallRanking(homeRanking);
            homeStandings.setWins(homeWins);
            homeStandings.setLosses(homeLosses);
            homeStandings.Save();

        } 
        catch (Exception e) { return "<result>SaveGameMatchup error : " + e.getMessage() + "</result>"; }

        return "<result>Success</result>";
    }
    
    public String SavePickemConfidencePts(HttpServletRequest request) {
        
        int secondSaveSuccess = 1;
        
        try {
            // Retrieve parameters
            int fsSeasonWeekId = FSUtils.getIntRequestParameter(request,"wk", 0);
            int fsTeamId = FSUtils.getIntRequestParameter(request,"fst", 0);
            int gameId = FSUtils.getIntRequestParameter(request,"gid", 0);
            int confPts = FSUtils.getIntRequestParameter(request,"pts", 0);
            int srcGameId = FSUtils.getIntRequestParameter(request,"sgid", 0);
            int fsGameId = FSUtils.getIntRequestParameter(request,"fsg", 0);

            // Save to the DB
            switch (fsGameId) {
                case FSGame.PRO_PICKEM:
                    if (ProPickem.SaveConfidencePoints(fsSeasonWeekId, fsTeamId, gameId, confPts) < 1) { return "<result>Error</result>"; }
                    if (srcGameId > 0) { secondSaveSuccess = ProPickem.SaveConfidencePoints(fsSeasonWeekId, fsTeamId, srcGameId, 0); }
                    break;
                case FSGame.COLLEGE_PICKEM:
                    if (CollegePickem.SaveConfidencePoints(fsSeasonWeekId, fsTeamId, gameId, confPts) < 1) { return "<result>Error</result>"; }
                    if (srcGameId > 0) { secondSaveSuccess = CollegePickem.SaveConfidencePoints(fsSeasonWeekId, fsTeamId, srcGameId, 0); }
                    break;
            }

        } 
        catch (Exception e) { return "<result>SavePickemConfidencePts error : " + e.getMessage() + "</result>"; }

        return (secondSaveSuccess > 0) ? "<result>Success</result>" : "<result>Error</result>";
    }
    
    public String SavePickemPick(HttpServletRequest request) {
              
        try {            
            // Retrieve parameters
            int fsSeasonWeekId = FSUtils.getIntRequestParameter(request,"wk", 0);
            int fsTeamId = FSUtils.getIntRequestParameter(request,"fst", 0);
            int gameId = FSUtils.getIntRequestParameter(request,"gid", 0);
            int teamPickedId = FSUtils.getIntRequestParameter(request,"tp", 0);
            int fsGameId = FSUtils.getIntRequestParameter(request,"fsg", 0);
            
            // Save to the DB
            switch (fsGameId) {
                case FSGame.PRO_PICKEM:
                    if (ProPickem.SavePick(fsSeasonWeekId, fsTeamId, gameId, teamPickedId) < 1) { return "<result>Error</result>"; }
                    break;
                case FSGame.COLLEGE_PICKEM:
                    if (CollegePickem.SavePick(fsSeasonWeekId, fsTeamId, gameId, teamPickedId) < 1) { return "<result>Error</result>"; }
                    break;
            }
                        
        } 
        catch (Exception e) { return "<result>SavePickemPick error : " + e.getMessage() + "</result>"; }
        
        return "<result>Success</result>";
    }
    
    public String SubmitBracketChallengePick(HttpServletRequest request) {               
        try {
            int seasonWeekId = FSUtils.getIntRequestParameter(request,"wk", 0);
            int fsTeamId = FSUtils.getIntRequestParameter(request,"fst", 0);
            int gameId = FSUtils.getIntRequestParameter(request,"gid", 0);
            int teamSeedPickedId = FSUtils.getIntRequestParameter(request,"tp", 0);
            
            FSSeasonWeek fsSeasonWeek = FSSeasonWeek.GetFSSeasonWeekBySeasonWeekAndGameID(seasonWeekId, FSGame.BRACKET_CHALLENGE);
            
            if(seasonWeekId == 0 || fsTeamId == 0 || gameId == 0 || teamSeedPickedId == 0 || (fsSeasonWeek==null || fsSeasonWeek.getFSSeasonWeekID() == 0)) { return "<result>Problem retrieving function parameters</result>"; }

            BracketChallenge bracketChallenge = new BracketChallenge();
            bracketChallenge.setFSTeamID(fsTeamId);
            bracketChallenge.setGameID(gameId);
            bracketChallenge.setTeamSeedPickedID(teamSeedPickedId);
            bracketChallenge.Save();
            
        } 
        catch (Exception e) { return "<result>SubmitBracketChallengePick error : " + e.getMessage() + "</result>"; }
        
        return "<result>Success</result>";
    }

    public String SubmitSeedChallengePick(HttpServletRequest request) {        
        try {
            int teamSeedPickedId = FSUtils.getIntRequestParameter(request,"tsid", 0);
            int seedChallengeGroupId = FSUtils.getIntRequestParameter(request,"sgid", 0);
            int fsTeamId = FSUtils.getIntRequestParameter(request,"fst", 0);
            int tournamentId = FSUtils.getIntRequestParameter(request,"tid", 0);
            
            if(teamSeedPickedId == 0 || seedChallengeGroupId == 0 || fsTeamId == 0 || tournamentId == 0) { return "<result>Problem retrieving function parameters</result>"; }
            
            SeedChallenge seedChallenge = new SeedChallenge();
            seedChallenge.setTournamentID(tournamentId);
            seedChallenge.setFSTeamID(fsTeamId);
            seedChallenge.setSeedChallengeGroupID(seedChallengeGroupId);
            seedChallenge.setTeamSeedPickedID(teamSeedPickedId);
            seedChallenge.Save();
 
        } 
        catch (Exception e) { return "<result>SubmitSeedChallengePick error : " + e.getMessage() + "</result>"; }
        
        return "<result>Success</result>";
    }
    
    public String UpdateMarchMadnessGameResult(HttpServletRequest request) {        
        try {
            int tournamentId = FSUtils.getIntRequestParameter(request,"tid", 0);
            int gameId = FSUtils.getIntRequestParameter(request,"gid", 0);
            int seasonWeekId = FSUtils.getIntRequestParameter(request,"sw", 0);
            int team1Pts = FSUtils.getIntRequestParameter(request,"t1pts", 0);
            int team2Pts = FSUtils.getIntRequestParameter(request,"t2pts", 0);

            if(tournamentId == 0 || gameId == 0 || seasonWeekId == 0 || team1Pts == 0 || team2Pts == 0) { return "<result>Problem retrieving function parameters</result>"; }
            
            SeasonWeek seasonWeek = new SeasonWeek(seasonWeekId);
            
            MarchMadnessGame.UpdateGameResult(tournamentId, gameId, seasonWeek, team1Pts, team2Pts);
 
        } 
        catch (Exception e) { return "<result>UpdateMarchMadnessGameResult error : " + e.getMessage() + "</result>"; }
        
        return "<result>Success</result>";
        
    }
    
    public String UpdateTeamSeed(HttpServletRequest request) {        
        try {
            int teamSeedId = FSUtils.getIntRequestParameter(request,"ts", 0);
            int teamId = FSUtils.getIntRequestParameter(request,"tid", 0);
            String record = FSUtils.getRequestParameter(request,"rec", null);

            if(teamSeedId == 0 || teamId == 0 ) { return "<result>Problem retrieving function parameters</result>"; }
            
            MarchMadnessTeamSeed teamSeed = new MarchMadnessTeamSeed(teamSeedId);
            teamSeed.setTeamID(teamId);
            teamSeed.setSeasonRecord(record);
            teamSeed.Save();
 
        } 
        catch (Exception e) { return "<result>Error : " + e.getMessage() + "</result>"; }
        
        return "<result>Success</result>";
        
    }
}