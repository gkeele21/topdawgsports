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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class FootballPlayers implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public FootballPlayers() {
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
            importPlayers();
            updateNFLPlayers();
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in FootballPlayers.run()", e);
        }
    }

    public void importPlayers() throws Exception {

//        final String playersurl = "http://www.quickstats.com/nfl/offid.htm";
        final String playersurl = "http://home.comcast.net/~dmuellenberg/ffa/players.nfl";
        //final String playersurl = "http://65.182.213.106/nfl/offid.htm";
//        final String begmark = "<pre>";
//        final String endmark = "</pre>";
        final int playeridpos = 26;
        final int teampos = 32;
        final int teamidpos = 36;
        final int positionpos = 41;

        try {
            // clear out the temp table
            StringBuilder clear = new StringBuilder();
            clear.append("delete from TempProPlayer");

            CTApplication._CT_QUICK_DB.executeUpdate(clear.toString());

            URL url = new URL(playersurl);

            InputStream uin = url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(uin));
            String line;

            boolean ok = false;
            int linenum = 0;
            while ((line = in.readLine()) != null) {
                System.out.println("Line : " + line);
                if (line.indexOf(";") != 0 && line.length() > 10)
                {
                    ok = true;
                } else
                {
                    ok = false;
                }
//                if (line.indexOf(begmark) >= 0) {
//                    ok = true;
//                } else if (line.indexOf(endmark) >= 0) {
//                    ok = false;
//                }

                if (ok) {
                    linenum++;
                    if (linenum > 0) {

                        // PLAYER NAME
                        String playername = line.substring(0,playeridpos-1);
                        String firstname = "";
                        String lastname = "";

                        StringTokenizer tk = new StringTokenizer(playername,",");
                        lastname = tk.nextToken();
                        firstname = tk.nextToken();

                        lastname = lastname.trim();
                        firstname = firstname.trim();

                        if (lastname.equals("Celek") && !firstname.equals("Brent"))
                        {
                            continue;
                        }
                        // PLAYER ID
                        String playerid = line.substring(playeridpos,teampos-1);
                        playerid = playerid.trim();

                        // PLAYER TEAM
                        String team = line.substring(teampos,teamidpos-1);
                        team = team.trim();

                        // TEAM ID
                        String teamid = line.substring(teamidpos,positionpos-1);
                        teamid = teamid.trim();

                        // PLAYER POSITION
                        String position = line.substring(positionpos);
                        // remove asterisk
                        position = position.trim();
                        if (position.endsWith("*")) {
                            position = position.replace("*", "");
                        }
                        position = position.trim();

                        StringBuilder sql = new StringBuilder();
                        sql.append("insert into TempProPlayer ");
                        sql.append("(StatsPlayerID,TeamID,Position,LastName,FirstName,FullName) ");
                        sql.append(" values (").append(playerid);
                        sql.append(", ").append(teamid);
                        sql.append(", '").append(position).append("' ");
                        sql.append(", '").append(FSUtils.fixupUserInputForDB(lastname)).append("' ");
                        sql.append(", '").append(FSUtils.fixupUserInputForDB(firstname)).append("' ");
                        sql.append(", '").append(FSUtils.fixupUserInputForDB(playername.trim())).append("' )");

                        _Logger.info(sql.toString());
                        CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

                    }
                }
            }

            in.close();

            System.out.println("Players updated.");
        } catch (Exception e) {
            _Logger.log(Level.SEVERE,"FootballPlayers Update Error : " + e.getMessage());
            e.printStackTrace();
        } finally {
        }

        return;
    }

    public void updateNFLPlayers() throws Exception {

        try {
            CTApplication._CT_QUICK_DB.executeUpdate("update Player set isActive = 0");

            StringBuilder sql = new StringBuilder();
            sql.append("select * from TempProPlayer where StatsPlayerID <> 0 order by StatsPlayerid");

            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());

            while (crs.next()) {
                String playerid = crs.getString("StatsPlayerID");
                int teamid = crs.getInt("TeamID");
                // Quickstats starts the teamids at 9000, we start at 100
                teamid = teamid > 0 ? teamid - 9000 + 1 : teamid;
                String pos = crs.getString("Position");
                String first = crs.getString("FirstName");
                String last = crs.getString("LastName");
                String quickstatsname = crs.getString("FullName");

                if (Integer.parseInt(playerid) > 12103) {
                    System.out.println("New player");
                }
                StringBuilder tempSql2 = new StringBuilder();
                tempSql2.append("select * from ").append(CTApplication.TBL_PREF).append("Player where StatsPlayerID = ").append(playerid);
                _Logger.info(tempSql2.toString());
                CachedRowSet crs2 = CTApplication._CT_QUICK_DB.executeQuery(tempSql2.toString());
                if (crs2.next()) {

                    int currentteam = crs2.getInt("TeamID");
                    sql = new StringBuilder();
                    sql.append("update ").append(CTApplication.TBL_PREF).append("Player set IsActive = 1");

                    if (currentteam != teamid) {

                        sql.append(" ,TeamID = ").append(teamid);
                    }

                    sql.append(" where StatsPlayerID = ").append(playerid);
                    CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
                    _Logger.info(sql.toString());
                } else {

                    // Get the PositionID based on the position
                    CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery("select * from " + CTApplication.TBL_PREF + "Position where PositionName = '" + pos + "'");
                    if (crs3.next()) {
                        String posid = crs3.getString("PositionID");
                        //int active = teamid > 0 ? 1 : 0;
                        int active = 1;
                        sql = new StringBuilder();
                        sql.append("insert into ").append(CTApplication.TBL_PREF).append("Player(TeamID,PositionID,FirstName,LastName,QuickStatsName,StatsPlayerID,IsActive) ");
                        sql.append("values(").append(teamid).append(", ");
                        sql.append(posid).append(", '");
                        sql.append(FSUtils.fixupUserInputForDB(first)).append("', '");
                        sql.append(FSUtils.fixupUserInputForDB(last)).append("', '").append(FSUtils.fixupUserInputForDB(quickstatsname)).append("',").append(playerid).append(",").append(active).append(")");

                        _Logger.info(sql.toString());
                        CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

                    }

                    crs3.close();
                }

                crs2.close();
            }

            crs.close();
        } catch (Exception e) {
            e.printStackTrace();
            _Logger.log(Level.SEVERE,"Error in FootballPlayers : " + e.getMessage());
        } finally {
        }
    }

    public static void main(String[] args) {
        try {
            FootballPlayers players = new FootballPlayers();
            players.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
