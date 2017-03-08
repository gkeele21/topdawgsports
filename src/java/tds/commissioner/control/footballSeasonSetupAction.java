package tds.commissioner.control;

import bglib.util.AuDate;
import bglib.util.FSUtils;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.*;
import tds.main.control.BaseAction;
public class footballSeasonSetupAction extends BaseAction {
    
    private static final int NUM_PRO_WEEKS = 17;
    private static final int NUM_COLLEGE_WEEKS = 15;
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int[] sportIds = {Sport.PRO_FOOTBALL, Sport.COLLEGE_FOOTBALL };
        int[] fantasyGames = {FSGame.HEAD_TO_HEAD, FSGame.SALARY_CAP, FSGame.PRO_PICKEM, FSGame.PRO_LOVEEMLEAVEEM, FSGame.COLLEGE_LOVEEMLEAVEEM, FSGame.COLLEGE_PICKEM};
        String[] fsSeasonNames = {"Head to Head", "Salary Cap", "Pro Pickem", "Pro Love Leave", "College Love Leave", "College Pickem"};
        
        // TODO : Add a UI so this part doesn't have to be hardcoded
        int[] keeleFantasyLeagueSize = {8,10};        
        int[] keeperUserIds = {1,2,5,6,7,9,10,36};
        int[] tenmanUserIds = {1,2,4,5,6,7,9,10,18,36};
        String[] keeperOwners = {"Grant", "Bert", "Brian", "Mike", "Case", "Ragz", "Jeremy", "John"};
        String[] tenmanOwners = {"Grant", "Bert", "Dave", "Brian", "Mike", "Case", "Ragz", "Jeremy", "Scott", "John"};        
        
        String nextPage = super.process(request,response);
        if (nextPage != null) { return nextPage; }

        nextPage = "commissioner/footballSeasonSetup";
        
        // Figure out the next unique ids
        int seasonId = FSUtils.GetHighestIdNumber("Season", "SeasonID") + 1;
        int seasonWeekId = FSUtils.GetHighestIdNumber("SeasonWeek", "SeasonWeekID") + 1;
        int fsSeasonId = FSUtils.GetHighestIdNumber("FSSeason", "FSSeasonID") + 1;
        int fsSeasonWeekId = FSUtils.GetHighestIdNumber("FSSeasonWeek", "FSSeasonWeekID") + 1;
        int fsLeagueId = FSUtils.GetHighestIdNumber("FSLeague", "FSLeagueID") + 1;
        int fsTeamId = FSUtils.GetHighestIdNumber("FSTeam", "FSTeamID") + 1;
        
        int nextSeasonWeekId = seasonWeekId;        
        for (int i=0; i < sportIds.length; i++ ) {                        
            // SEASON 
            Season season = new Season();
            season.setSeasonID(seasonId + i);
            season.setSportID(sportIds[i]);
            season.setSeasonName((sportIds[i] == Sport.PRO_FOOTBALL) ? "NFL Football" : (sportIds[i] == Sport.COLLEGE_FOOTBALL) ? "NCAA Football" : null);
            season.setIsActive(true);
            season.setSportYear(year);
            season.Save();        
        
            // SEASON WEEK
            int numWeeks = (sportIds[i] == Sport.PRO_FOOTBALL ? NUM_PRO_WEEKS : NUM_COLLEGE_WEEKS);
            for (int j=0; j < numWeeks; j++ ) {  
                SeasonWeek seasonWeek = new SeasonWeek();
                seasonWeek.setSeasonWeekID(nextSeasonWeekId);
                seasonWeek.setSeasonID(seasonId + i);
                seasonWeek.setWeekNo(j + 1);
                seasonWeek.setStatus((j==0) ? SeasonWeek.Status.CURRENT.toString() : SeasonWeek.Status.PENDING.toString());
                seasonWeek.setWeekType((j + 1 == 1) ? SeasonWeek.WeekType.INITIAL.toString() : (j + 1 == numWeeks) ? SeasonWeek.WeekType.FINAL.toString() : SeasonWeek.WeekType.MIDDLE.toString());
                seasonWeek.Save();
                nextSeasonWeekId += 1;
            }  
        }
        
        // FSSEASON
        int nextFSSeasonId = fsSeasonId;
        int nextFSSeasonWeekId = fsSeasonWeekId;
        for (int k=0; k < fantasyGames.length; k++ ) {
            FSSeason fsSeason = new FSSeason();
            fsSeason.setFSSeasonID(nextFSSeasonId);
            fsSeason.setFSGameID(fantasyGames[k]);                
            fsSeason.setSeasonID((fantasyGames[k] == FSGame.COLLEGE_LOVEEMLEAVEEM || fantasyGames[k] == FSGame.COLLEGE_PICKEM ? seasonId + 1 : seasonId));
            fsSeason.setIsActive(true);
            fsSeason.setDisplayTeams(true);
            fsSeason.setSeasonName(year + " " + fsSeasonNames[k]);
            fsSeason.Save();            
            
            // FSSEASONWEEK            
            int numWeeks = (fantasyGames[k] == FSGame.COLLEGE_PICKEM || fantasyGames[k] == FSGame.COLLEGE_LOVEEMLEAVEEM ? NUM_COLLEGE_WEEKS : NUM_PRO_WEEKS);
            for (int m=0; m < numWeeks; m++ ) {   
                FSSeasonWeek fsSeasonWeek = new FSSeasonWeek();
                fsSeasonWeek.setFSSeasonWeekID(nextFSSeasonWeekId);
                fsSeasonWeek.setFSSeasonID(nextFSSeasonId);
                fsSeasonWeek.setSeasonWeekID((fantasyGames[k] == FSGame.COLLEGE_LOVEEMLEAVEEM || fantasyGames[k] == FSGame.COLLEGE_PICKEM ? seasonWeekId + NUM_PRO_WEEKS + m : seasonWeekId + m));
                fsSeasonWeek.setFSSeasonWeekNo(m + 1);
                fsSeasonWeek.setStatus((m==0) ? SeasonWeek.Status.CURRENT.toString() : FSSeasonWeek.Status.PENDING.toString());
                fsSeasonWeek.setWeekType((m + 1 == 1) ? SeasonWeek.WeekType.INITIAL.toString() : (m + 1 == numWeeks) ? SeasonWeek.WeekType.FINAL.toString() : SeasonWeek.WeekType.MIDDLE.toString());
                fsSeasonWeek.Save();
                nextFSSeasonWeekId += 1;
            }
            nextFSSeasonId += 1;
        }
        
        // FSLEAGUE      
        int nextFSLeagueId = fsLeagueId;
        for (int n=0; n < fantasyGames.length; n++ ) {
            if (fantasyGames[n] == FSGame.HEAD_TO_HEAD) {
                FSLeague keeperLeague = new FSLeague();
                keeperLeague.setFSLeagueID(nextFSLeagueId);
                keeperLeague.setFSSeasonID(fsSeasonId);
                keeperLeague.setLeagueName(year + " " + "Keeper");
                keeperLeague.setIsDefaultLeague(1);
                keeperLeague.setNumTeams(keeperUserIds.length);
                keeperLeague.setDraftType("dynasty");
                keeperLeague.setScheduleName("8Team");
                keeperLeague.setStartFSSeasonWeekID(fsSeasonWeekId);
                keeperLeague.setStatus(FSLeague.Status.ACTIVE.toString());
                keeperLeague.Save();
                nextFSLeagueId += 1;
            }
            FSLeague fsLeague = new FSLeague();
            fsLeague.setFSLeagueID(nextFSLeagueId);
            fsLeague.setFSSeasonID(fsSeasonId + n);
            fsLeague.setLeagueName((fantasyGames[n] == FSGame.HEAD_TO_HEAD) ? year + " Tenman" : year + " " + fsSeasonNames[n]);
            fsLeague.setIsDefaultLeague(1);
            fsLeague.setNumTeams((fantasyGames[n] == FSGame.HEAD_TO_HEAD) ? tenmanUserIds.length : null);
            fsLeague.setDraftType((fantasyGames[n] == FSGame.HEAD_TO_HEAD) ? "redraft" : null);
            fsLeague.setScheduleName((fantasyGames[n] == FSGame.HEAD_TO_HEAD) ? "10Team" : null);
            fsLeague.setStartFSSeasonWeekID((fantasyGames[n] == FSGame.HEAD_TO_HEAD) ? fsSeasonWeekId : null);
            fsLeague.setIsCustomLeague((fantasyGames[n] == FSGame.PRO_PICKEM) ? 1 : null);
            fsLeague.setStatus(FSLeague.Status.ACTIVE.toString());
            fsLeague.Save();
            nextFSLeagueId += 1;
        }
        
        // TODO : Will want to take this out when we change the logic of getting the CurrentFSSeasonID instead of storing it in the FSGame table
        
        // FSGame
        for (int o=0; o < fantasyGames.length; o++ ) {
           FSGame fsGame = new FSGame(fantasyGames[o]);
           fsGame.setCurrentFSSeasonID(fsSeasonId + o);
           fsGame.Save(); 
        }

        // TODO: Get a UI for this but for now just create the default leagues and put the keeper league teams and tenman league teams together
        int nextFSTeamID = fsTeamId;
        for (int p=0; p < keeleFantasyLeagueSize.length; p++ ) {
            for (int q=0; q < keeleFantasyLeagueSize[p]; q++ ) {
                FSTeam fsTeam = new FSTeam();
                fsTeam.setFSTeamID(nextFSTeamID);
                fsTeam.setFSLeagueID(fsLeagueId + p);
                fsTeam.setIsActive(true);
                fsTeam.setFSUserID((p==0) ? keeperUserIds[q] : tenmanUserIds[q]);
                fsTeam.setTeamName((p==0) ? keeperOwners[q] : tenmanOwners[q]);
                fsTeam.setTeamNo(q+1);
                fsTeam.setDateCreated(new AuDate());
                fsTeam.Save();
                nextFSTeamID += 1;
            }            
        }

        // FSFOOTBALLSEASONDETAIL & FSFOOTBALLROSTERPOSITIONS
        FSFootballSeasonDetail seasonDetail = new FSFootballSeasonDetail();
        seasonDetail.setFSSeasonID(fsSeasonId);
        seasonDetail.setStartingWeekNumber(1);
        seasonDetail.setTeamsInLeague(10);
        seasonDetail.setNumDivisions(0);
        seasonDetail.setMaxNumStarters(7);
        seasonDetail.setMaxNumReserves(9);
        seasonDetail.setMaxNumInactive(0);
        seasonDetail.setNumWeeksRegSeason(16);
        seasonDetail.setNumWeeksPlayoffs(0);
        seasonDetail.setPlayoffStartWeekNumber(0);

        seasonDetail.Save();

        // QB
        FSFootballRosterPositions pos = new FSFootballRosterPositions();
        pos.setFSSeasonID(fsSeasonId);
        pos.setPositionID(1);
        pos.setMaxStart(1);
        pos.setMinStart(1);
        pos.setMaxNum(10);
        pos.setMinNum(1);
        pos.setDraftNum(1);
        pos.setMinActive(1);
        pos.Save();

        // RB
        pos = new FSFootballRosterPositions();
        pos.setFSSeasonID(fsSeasonId);
        pos.setPositionID(2);
        pos.setMaxStart(2);
        pos.setMinStart(2);
        pos.setMaxNum(11);
        pos.setMinNum(2);
        pos.setDraftNum(2);
        pos.setMinActive(2);
        pos.Save();

        // WR
        pos = new FSFootballRosterPositions();
        pos.setFSSeasonID(fsSeasonId);
        pos.setPositionID(3);
        pos.setMaxStart(3);
        pos.setMinStart(3);
        pos.setMaxNum(12);
        pos.setMinNum(3);
        pos.setDraftNum(3);
        pos.setMinActive(3);
        pos.Save();

        // TE
        pos = new FSFootballRosterPositions();
        pos.setFSSeasonID(fsSeasonId);
        pos.setPositionID(4);
        pos.setMaxStart(3);
        pos.setMinStart(0);
        pos.setMaxNum(12);
        pos.setMinNum(0);
        pos.setDraftNum(0);
        pos.setMinActive(0);
        pos.Save();

        // PK
        pos = new FSFootballRosterPositions();
        pos.setFSSeasonID(fsSeasonId);
        pos.setPositionID(5);
        pos.setMaxStart(1);
        pos.setMinStart(1);
        pos.setMaxNum(10);
        pos.setMinNum(1);
        pos.setDraftNum(1);
        pos.setMinActive(1);
        pos.Save();

        // DEF
        pos = new FSFootballRosterPositions();
        pos.setFSSeasonID(fsSeasonId);
        pos.setPositionID(6);
        pos.setMaxStart(0);
        pos.setMinStart(0);
        pos.setMaxNum(0);
        pos.setMinNum(0);
        pos.setDraftNum(0);
        pos.setMinActive(0);
        pos.Save();

        return nextPage;
    }
}