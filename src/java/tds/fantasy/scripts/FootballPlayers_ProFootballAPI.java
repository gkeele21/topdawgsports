/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.fantasy.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;
import tds.main.bo.MySportsFeeds_ProFootballAPI;
import tds.main.bo.football.stats.MySportsFeeds.Players_PlayerObj;
import tds.main.bo.football.stats.MySportsFeeds.Players_PlayerInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class FootballPlayers_ProFootballAPI implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public FootballPlayers_ProFootballAPI() {
        _Logger = Logger.global;
        _Logger.setLevel(Level.ALL);
    }

    @Override
    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    @Override
    public ResultCode getResultCode() { return _ResultCode; }

    @Override
    public void setScriptArgs(String[] args) { _Args = args; }

    @Override
    public void run() {
        try {

            _Logger.info("Starting to update player list...");

            importPlayers_MySportsFeeds();
            updateNFLPlayers();
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in FootballPlayers.run()", e);
        }
    }

    public void importPlayers_MySportsFeeds() throws Exception {

        try {
            // clear out the temp table
            StringBuilder clear = new StringBuilder();
            clear.append("DELETE FROM TempProPlayer");

            CTApplication._CT_QUICK_DB.executeUpdate(clear.toString());

            List<Players_PlayerObj> players = MySportsFeeds_ProFootballAPI.getPlayers();

            if (players.size() > 0) {
                int playerCount = 0;
                for (Players_PlayerObj playerList : players) {

                    playerCount++;

                    Players_PlayerInfo player = playerList.getPlayer();

                    String newPlayerId = String.valueOf(player.getId());

                    int yearsPro = player.isRookie() ? 0 : 1;
                    String firstName = player.getFirstName();
                    String lastName = player.getLastName();
                    String position = player.getPrimaryPosition();
                    Object teamObj = player.getCurrentTeam();
                    System.out.println("Player : " + player.getFirstName() + " " + player.getLastName());
                    if (teamObj == null) {
                        continue;
                    }
                    Object statusObj = player.getCurrentRosterStatus();
                    if (statusObj == null) {
                        continue;
                    }
                    LinkedHashMap<Integer,String> teamNode = (LinkedHashMap<Integer, String>) teamObj;
                    String team = teamNode.get("abbreviation");

                    int active = 1;

                    if ("FB".equals(position))
                    {
                        position = "RB";
                    } else if ("K".equals(position))
                    {
                        position = "PK";
                    } else if ("FS".equals(position))
                    {
                        position = "DB";
                    } else if ("CB".equals(position))
                    {
                        position = "DB";
                    } else if ("DT".equals(position))
                    {
                        position = "DL";
                    } else if ("SS".equals(position))
                    {
                        position = "DL";
                    } else if ("OLB".equals(position))
                    {
                        position = "LB";
                    } else if ("DE".equals(position))
                    {
                        position = "DL";
                    } else if ("MLB".equals(position))
                    {
                        position = "LB";
                    } else if ("ILB".equals(position))
                    {
                        position = "LB";
                    } else if ("NT".equals(position))
                    {
                        position = "DL";
                    } else if ("LS".equals(position))
                    {
                        position = "DB";
                    }

                    if (team.equals("SD")) {
                        team = "LAC";
                    }
                    if (team.equals("LA")) {
                        team = "LAR";
                    }
                    if (team.equals("JAX")) {
                        team = "JAC";
                    }

                    if (active == 0) {
                        continue;
                    }

                    // grab the TeamId
                    int teamId = 0;
                    CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery("SELECT * FROM " + CTApplication.TBL_PREF + "Team WHERE Abbreviation = '" + team + "' AND SportID = 1");
                    if (crs3.next()) {
                        teamId = crs3.getInt("TeamID");
                    }
                    int positionId = 0;
                    crs3 = CTApplication._CT_QUICK_DB.executeQuery("SELECT * FROM Position WHERE PositionName = '" + position + "' AND SportID = 1");
                    if (crs3.next()) {
                        positionId = crs3.getInt("PositionID");
                    }

                    if (positionId == 0) {
                        continue;
                    }

                    // grab the playerId
                    int playerId = 0;
                    StringBuilder query = new StringBuilder();
                    query.append("SELECT * FROM Player WHERE StatsPlayerID2 = '").append(newPlayerId).append("'");
                    crs3 = CTApplication._CT_QUICK_DB.executeQuery(query.toString());
                    _Logger.info(query.toString());
                    if (crs3.next()) {
                        playerId = crs3.getInt("PlayerID");
                    } else
                    {

                        query = new StringBuilder();
                        query.append(" SELECT * ");
                        query.append(" FROM Player ");
                        query.append(" WHERE FirstName = '").append(FSUtils.fixupUserInputForDB(firstName)).append("' ");
                        query.append(" AND LastName = '").append(FSUtils.fixupUserInputForDB(lastName)).append("' ");
                        query.append(" AND PositionID = ").append(positionId);

                        _Logger.info(query.toString());
                        crs3 = CTApplication._CT_QUICK_DB.executeQuery(query.toString());
                        int count = 0;
                        while (crs3.next()) {
                            count++;
                            playerId = crs3.getInt("PlayerID");
                        }

                        if (count > 1)
                        {
                            playerId = -1;
                            _Logger.info("Too many players found with that name and position to grab the correct one");
                        }
                    }

                    StringBuilder sql = new StringBuilder();
                    sql.append("INSERT INTO TempProPlayer ");
                    sql.append("(StatsPlayerID,Position,LastName,FirstName,Active,TeamID,PositionId,YearsPro,PlayerId,Team) ");
                    sql.append(" values (").append(newPlayerId);
                    sql.append(", '").append(position).append("' ");
                    sql.append(", '").append(FSUtils.fixupUserInputForDB(lastName)).append("' ");
                    sql.append(", '").append(FSUtils.fixupUserInputForDB(firstName)).append("' ");
                    sql.append(", ").append(active);
                    sql.append(", ").append(teamId);
                    sql.append(", ").append(positionId);
                    sql.append(", ").append(yearsPro);
                    sql.append(", ").append(playerId);
                    sql.append(", '").append(team).append("' )");

                    _Logger.info(sql.toString());
                    CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
                }
            }
            System.out.println("Players inserted into TempProPlayer.");
        } catch (Exception e) {
            _Logger.log(Level.SEVERE,"FootballPlayers Update Error : " + e.getMessage());
            e.printStackTrace();
        } finally {
        }

    }

    public void updateNFLPlayers() throws Exception {

        try {
//            CTApplication._CT_QUICK_DB.executeUpdate("UPDATE Player p " +
//                    "INNER JOIN Position pt ON pt.PositionID = p.PositionID " +
//                    "SET p.isActive = 0  " +
//                    "WHERE pt.SportID = 1");

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM TempProPlayer WHERE StatsPlayerID <> 0 ORDER BY StatsPlayerID");

            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            int numNew = 0, numUpdated = 0, numTotal = 0;
            while (crs.next()) {
                numTotal++;

                String statsPlayerId = crs.getString("StatsPlayerID");
                int playerId = crs.getInt("PlayerId");
                int teamid = crs.getInt("TeamID");
                String positionId = crs.getString("PositionId");
                String first = crs.getString("FirstName");
                String last = crs.getString("LastName");
                int yearsPro = crs.getInt("YearsPro");

                if (playerId > 0) {
                    // update
                    sql = new StringBuilder();
                    sql.append("UPDATE ").append(CTApplication.TBL_PREF).append("Player ");
                    sql.append(" SET IsActive = 1");
                    sql.append(" ,TeamID = ").append(teamid);
                    sql.append(" ,StatsPlayerID2 = ").append(statsPlayerId);
                    sql.append(" WHERE PlayerID = ").append(playerId);
                    System.out.println(sql);
                    CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
                    _Logger.info(sql.toString());
                    numUpdated++;
                    continue;
                }

                StringBuilder tempSql2 = new StringBuilder();
                tempSql2.append("SELECT * FROM ").append(CTApplication.TBL_PREF).append("Player WHERE StatsPlayerID2 = ").append(statsPlayerId);
                _Logger.info(tempSql2.toString());
                CachedRowSet crs2 = CTApplication._CT_QUICK_DB.executeQuery(tempSql2.toString());
                if (crs2.next()) {

                    int currentteam = crs2.getInt("TeamID");
                    playerId = crs2.getInt("PlayerID");
                    sql = new StringBuilder();
                    sql.append("UPDATE ").append(CTApplication.TBL_PREF).append("Player SET IsActive = 1");

                    if (currentteam != teamid) {

                        sql.append(" ,TeamID = ").append(teamid);
                    }

                    sql.append(" WHERE PlayerID = ").append(playerId);
                    System.out.println(sql);
                    CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
                    _Logger.info(sql.toString());
                    numUpdated++;
                } else {

                    sql = new StringBuilder();
                    sql.append("INSERT INTO ").append(CTApplication.TBL_PREF).append("Player(TeamID,PositionID,FirstName,LastName,StatsPlayerID2,YearsPro,IsActive) ");
                    sql.append("VALUES(").append(teamid);
                    sql.append(", ").append(positionId);
                    sql.append(", '").append(FSUtils.fixupUserInputForDB(first));
                    sql.append("', '").append(FSUtils.fixupUserInputForDB(last));
                    sql.append("',").append(statsPlayerId);
                    sql.append(",").append(yearsPro);
                    sql.append(", 1)");

                    System.out.println(sql);
                    CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
                    numNew++;
                }

                crs2.close();
            }

            crs.close();

            System.out.println("Num New: " + numNew);
            System.out.println("Num Updated: " + numUpdated);
        } catch (Exception e) {
            e.printStackTrace();
            _Logger.log(Level.SEVERE,"Error in FootballPlayers : " + e.getMessage());
        } finally {
        }
    }

    public static void main(String[] args) {
        try {
            FootballPlayers_ProFootballAPI players = new FootballPlayers_ProFootballAPI();
            players.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
