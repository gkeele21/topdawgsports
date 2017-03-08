/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.scripts.fantasy;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.FSUtils;
import tds.main.bo.CTApplication;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.jdbc.rowset.CachedRowSet;

/**
 *
 * @author grant.keele
 */
public class FootballIDPPlayers {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    private final List<String> _ValidDLPOS = new ArrayList<String>(Arrays.asList("DE", "DT"));
    private final List<String> _ValidLBPOS = new ArrayList<String>(Arrays.asList("LB"));
    private final List<String> _ValidDBPOS = new ArrayList<String>(Arrays.asList("S", "CB"));

    public FootballIDPPlayers() {
        _Logger = Logger.global;
    }

    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    public ResultCode getResultCode() { return _ResultCode; }

    public void setScriptArgs(String[] args) { _Args = args; }

    public void run() {
        try {

            _Logger.info("Starting to update player list...");
            importPlayers();
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in FootballIDPPlayers.run()", e);
        }
    }

    public void importPlayers() throws Exception {

        final String playersurl = "http://www.quickstats.com/nfl/defid.htm";
        final String begmark = "<pre>";
        final String endmark = "</pre>";
        final int playeridpos = 26;
        final int teampos = 31;
        final int teamidpos = 35;
        final int positionpos = 40;

        Connection con = null;
        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);

            // clear out the temp table
            StringBuffer clear = new StringBuffer();
            clear.append("delete from ct_temp.TempProPlayer");

            CTApplication._CT_QUICK_DB.executeUpdate(con,clear.toString());

            URL url = new URL(playersurl);

            InputStream uin = url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(uin));
            String line;

            boolean ok = false;
            int linenum = 0;
            while ((line = in.readLine()) != null) {
                //System.out.println("Line : " + line);
                if (line.indexOf(begmark) >= 0) {
                    ok = true;
                } else if (line.indexOf(endmark) >= 0) {
                    ok = false;
                }

                if (ok) {
                    linenum++;
                    if (linenum > 2) {

                        // PLAYER NAME
                        String playername = line.substring(0,playeridpos-1);
                        String firstname = "";
                        String lastname = "";

                        StringTokenizer tk = new StringTokenizer(playername,",");
                        lastname = tk.nextToken();
                        firstname = tk.nextToken();

                        lastname = lastname.trim();
                        firstname = firstname.trim();

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
                        position = position.trim();

                        StringBuffer sql = new StringBuffer();
                        sql.append("insert into ct_temp.TempProPlayer " +
                                    "(StatsPlayerID,TeamID,Position,LastName,FirstName,FullName) " +
                                    " values (" + playerid +
                                    ", " + teamid +
                                    ", '" + position + "' " +
                                    ", '" + FSUtils.fixupUserInputForDB(lastname) + "' " +
                                    ", '" + FSUtils.fixupUserInputForDB(firstname) + "' " +
                                    ", '" + FSUtils.fixupUserInputForDB(playername.trim()) + "' )");

                        _Logger.info(sql.toString());
                        CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());

                    }
                }
            }

            in.close();

            con.commit();
        } catch (Exception e) {
            _Logger.log(Level.SEVERE,"FootballPlayers Update Error : " + e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
        }

        updateNFLPlayers();

        return;
    }

    public void updateNFLPlayers() throws Exception {

        Connection con = null;
        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);

            StringBuffer sql = new StringBuffer();
            sql.append("select * from ct_temp.TempProPlayer where StatsPlayerID <> 0 order by StatsPlayerid");

            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());

            while (crs.next()) {
                String playerid = crs.getString("StatsPlayerID");
                int teamid = crs.getInt("TeamID");
                // Quickstats starts the teamids at 9000, we start at 100
                teamid = teamid > 0 ? teamid - 9000 + 1 : teamid;
                String pos = crs.getString("Position");
                String first = crs.getString("FirstName");
                String last = crs.getString("LastName");
                String quickstatsname = crs.getString("FullName");

                CachedRowSet crs2 = CTApplication._CT_QUICK_DB.executeQuery(con,"select * from " + CTApplication.TBL_PREF + "Player where StatsPlayerID = " + playerid);
                if (crs2.next()) {

                    int currentteam = crs2.getInt("TeamID");
                    if (currentteam != teamid) {

                        sql = new StringBuffer();
                        sql.append("update " + CTApplication.TBL_PREF + "Player set TeamID = " + teamid + " where StatsPlayerID = " + playerid);
                        CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());
                        _Logger.info(sql.toString());
                    }
                } else {

                    // Retrieve the IDP positionName
                    if (_ValidDLPOS.contains(pos)) {
                        pos = "DL";
                    } else if (_ValidLBPOS.contains(pos)) {
                        pos = "LB";
                    } else if (_ValidDBPOS.contains(pos)) {
                        pos = "DB";
                    }
                    
                    // Get the PositionID based on the position
                    CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery(con,"select * from " + CTApplication.TBL_PREF + "Position where PositionName = '" + pos + "'");
                    if (crs3.next()) {
                        String posid = crs3.getString("PositionID");
                        int active = teamid > 0 ? 1 : 0;
                        sql = new StringBuffer();
                        sql.append("insert into " + CTApplication.TBL_PREF + "Player(TeamID,PositionID,FirstName,LastName,QuickStatsName,StatsPlayerID,IsActive) " +
                                    "values(" + teamid + ", " +
                                    posid + ", '" +
                                    FSUtils.fixupUserInputForDB(first) + "', '" +
                                    FSUtils.fixupUserInputForDB(last) + "', '" + FSUtils.fixupUserInputForDB(quickstatsname) + "'," + playerid + "," + active + ")");

                        _Logger.info(sql.toString());
                        CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());

                    } else {
                        _Logger.warning("Position [" + pos + "] is not in DB...skipping player");
                    }

                    crs3.close();
                }

                crs2.close();
            }

            crs.close();

            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            _Logger.log(Level.SEVERE,"Error in FootballIDPPlayers : " + e.getMessage());
        } finally {
            con.close();
        }
    }

    public static void main(String[] args) {
        try {
            FootballIDPPlayers players = new FootballIDPPlayers();
            players.run();
        } catch (Exception e) {
        }
    }
}
