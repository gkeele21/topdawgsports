package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.AuDate;
import java.util.HashMap;
import java.util.Map;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import static tds.main.bo.CTApplication._CT_LOG;

public class SeasonSchedule {
    
    private int _SeasonID;
    private Map<String, Game> _Games = new HashMap<String, Game>();

    private static final Map<Integer, SeasonSchedule> _SeasonSchedules = new HashMap<Integer, SeasonSchedule>();

    // CONSTRUCTORS
    public SeasonSchedule() {
    }

    private SeasonSchedule(int seasonid) throws Exception {
        _SeasonID = seasonid;
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" select ").append(_Cols.getColumnList("Game", "g.", ""));
            sql.append(", ").append(_Cols.getColumnList("Team", "vt.", "VisitorTeam$"));
            sql.append(", ").append(_Cols.getColumnList("Team", "ht.", "HomeTeam$"));
            sql.append(", ").append(_Cols.getColumnList("SeasonWeek", "w.", "SeasonWeek$"));
            sql.append(", ").append(_Cols.getColumnList("Season", "s.", "Season$"));
            sql.append(" FROM Game g ");
            sql.append(" INNER JOIN SeasonWeek w ON w.SeasonWeekID = g.SeasonWeekID ");
            sql.append(" INNER JOIN Season s ON s.SeasonID = w.SeasonID ");
            sql.append(" INNER JOIN Team vt ON vt.StatsTeamID = g.VisitorID ");
            sql.append(" INNER JOIN Team ht ON ht.StatsTeamID = g.HomeID ");
            sql.append(" WHERE s.SeasonID = ").append(seasonid);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                Game game = Game.getInstance(crs);
                int seasonweekID = crs.getInt("SeasonWeekID");
                _Games.put(""+ seasonweekID + "_" + crs.getInt("HomeID"), game);
                _Games.put(""+ seasonweekID + "_" + crs.getInt("VisitorID"), game);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }
    
    // PUBLIC METHODS

    public static SeasonSchedule getInstance(int seasonweekid) {
        SeasonSchedule ret = _SeasonSchedules.get(seasonweekid);
        if (ret==null) {
            try {
                ret = new SeasonSchedule(seasonweekid);
                _SeasonSchedules.put(seasonweekid, ret);
            }
            catch (Exception e) {
                CTApplication._CT_LOG.error(e);
               _CT_LOG.error(e);
            }

        }
        return ret;
    }

    public Game getSeasonGame(int seasonweekID, int teamID) {
        Team team = Team.getInstance(teamID);
        int statsteamID = team.getStatsTeamID();
        Game ret = _Games.get(""+seasonweekID+"_"+statsteamID);
        if (ret==null) {
            ret = Game.BYE_WEEK;
        }

        return ret;
    }

    public static Game getGame(int seasonID, int seasonweekID, int teamID) {

        return getInstance(seasonID).getSeasonGame(seasonweekID, teamID);
    }
}
