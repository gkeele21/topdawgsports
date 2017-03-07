package tds.data;

import bglib.util.EmailMessage;
import java.sql.ResultSetMetaData;
import java.util.*;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

public class CTColumnLists {

    private final List<String> _Tables = new ArrayList<String>(Arrays.asList(
            "State",
            "FSUser",
            "FSTeam",
            "FSLeague",
            "FSSeason",
            "FSGame",
            "Sport",
            "Season",
            "FSSeasonWeek",
            "SeasonWeek",
            "FSFootballStandings",
            "FSFootballMatchup",
            "FSFootballTransaction",
            "Player",
            "Position",
            "Country",
            "Team",
            "FSRoster",
            "FSFootballRosterPositions",
            "FSFootballSeasonDetail",
            "FootballStats", 
            "Game",
            "Standings",
            "FSPlayerValue",
            "FSFootballTransactionOrder",
            "FSFootballTransactionRequest",
            "FSFootballMaxRequests",
            "PlayerInjury", 
            "ProPickem",
            "ProLoveLeave",
            "CollegeLoveLeave",
            "CollegePickem",
            "BracketChallenge",
            "BracketChallengeStandings",
            "SeedChallenge",
            "SeedChallengeGroup",
            "SeedChallengeStandings",
            "MarchMadnessTournament",
            "MarchMadnessRound",
            "MarchMadnessRegion",
            "MarchMadnessGame",
            "MarchMadnessLeague",
            "MarchMadnessTeamSeed",            
            "FSFootballDraft",
            "PlayoffTournament",
            "PlayoffLeague",
            "PlayoffRound",
            "PlayoffGame",
            "Course",
            "CourseTeeInfo",
            "GolfCourse",
            "Golfer",
            "GolferRound",
            "GolfEvent",
            "GolfEventGolfer",
            "GolfEventGroup",
            "GolfEventRound",
            "GolfEventRoundFormat",
            "GolfEventRoundFormatGolfer",
            "GolfEventRoundFormatGolferHole",
            "GolfEventRoundFormatPayout",
            "GolfEventRoundGolfer",
            "GolfEventTeam",
            "GolfEventTeamRoundFormat",
            "GolfFormat",
            "GolfScore",
            "GolfTournament",
            "Hole",
            "HoleScore",
            "HoleTeeInfo",            
            "Stroke",
            "Tee",
            "PGATournament",
            "PGATournamentWeek",
            "PGATournamentWeekPlayer",
            "PGASalaryValueDefault",
            "FSGolfStandings"));

    private Map<String, String> _TableColumns = new HashMap<String, String>();

    public static final CTColumnLists _Cols = new CTColumnLists();

    private CTColumnLists() {
        for (String tableName : _Tables) {
            try {
                String sql = "select * from " + tableName + " where 1=0";
                CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql);
                ResultSetMetaData md = crs.getMetaData();
                int numColumns = md.getColumnCount();
                StringBuffer colList = new StringBuffer(" ");
                for (int i=1; i<=numColumns; i++) {
                    String colName = md.getColumnName(i);
                    colList.append("{0}").append(colName).append(" as {1}").append(colName);
                    if (i<numColumns) {
                        colList.append(", ");
                    } else {
                        colList.append(" ");
                    }
                }
                _TableColumns.put(tableName, colList.toString());
                crs.close();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getColumnList(String tableName) {
        return getColumnList(tableName, "", "");
    }

    public String getColumnList(String tableName, String prefix, String as) {
        as = (as==null) ? "" : as;
        return EmailMessage.formatString(_TableColumns.get(tableName), new ArrayList<Object>(Arrays.asList(new String[] {prefix, as})));
    }

}
