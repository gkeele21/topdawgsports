/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.golf.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.AuUtil;
import bglib.util.FSUtils;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import sun.jdbc.rowset.CachedRowSet;
import tds.constants.Position;
import tds.constants.Team;
import tds.main.bo.CTApplication;

/**
 *
 * @author grant.keele
 */
public class GolfPlayers implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public GolfPlayers() {
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
            updatePGAPlayers();
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in GolfPlayers.run()", e);
        }
    }

    public void importPlayers() throws Exception {

        final String playersFilePath = "http://www.pgatour.com/data/players/player.json";
//        final String playersFilePath = "http://202.78.83.137/data/players/player.json";
        StringBuilder sb = new StringBuilder();
        Connection con = null;
        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);
            String s;

            URL url = new URL(playersFilePath);
            InputStream istream = url.openStream();
            DataInputStream dis = new DataInputStream(new BufferedInputStream(istream));
            
            while ((s = dis.readLine()) != null) {
                sb.append(s);
            }
            
            JSONObject objs = (JSONObject) JSONValue.parse(sb.toString());
            
            // clear out the temp table
            StringBuilder clear = new StringBuilder();
            clear.append("delete from TempProPlayer");

            CTApplication._CT_QUICK_DB.executeUpdate(con,clear.toString());

            JSONArray playersArr = (JSONArray)objs.get("plrs");

            playerLoop: for (Object p : playersArr)
            {
                JSONObject player = (JSONObject) p;
                String firstName = player.get("nameF").toString();
                if (AuUtil.isEmpty(firstName))
                {
                    continue;
                }
                
                // check to see if he played in 2014 or 2015
                JSONArray yearsArr = (JSONArray)player.get("yrs");

                boolean active = false;
                for (Object yearObj : yearsArr)
                {
                    if ("2015".equals(yearObj) || "2016".equals(yearObj))
                    {
                        active = true;
                    }
                }
                
                if (!active)
                {
                    continue playerLoop;
                }
                
                String lastName = player.get("nameL").toString();
                String country = player.get("ct").toString();
                int externalPlayerId = Integer.parseInt(player.get("pid").toString());
                
                // grab the playerId
                int playerId = 0;
                StringBuilder query = new StringBuilder();
                query.append("select * from Player where StatsPlayerId = '").append(externalPlayerId).append("'").append(" AND TeamID = ").append(Team.PGATOUR);
                CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery(con,query.toString());
                _Logger.info(query.toString());
                if (crs3.next()) {
                    playerId = crs3.getInt("PlayerID");
                } 

                StringBuilder sql = new StringBuilder();
                sql.append("insert into TempProPlayer ");
                sql.append("(StatsPlayerId,LastName,FirstName,Team,PlayerId) ");
                sql.append(" values ('").append(externalPlayerId).append("' ");
                sql.append(", '").append(FSUtils.fixupUserInputForDB(lastName)).append("' ");
                sql.append(", '").append(FSUtils.fixupUserInputForDB(firstName)).append("' ");
                sql.append(", '").append(country).append("' ");
                sql.append(", ").append(playerId).append(" )");

                _Logger.info(sql.toString());
                CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());

            }
            con.commit();
            System.out.println("Players updated.");
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "FootballPlayers Update Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {
            con.close();
        }

    }

    public void updatePGAPlayers() throws Exception {

        Connection con = null;
        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);

            CTApplication._CT_QUICK_DB.executeUpdate(con,"update Player set isActive = 0");

            StringBuilder sql = new StringBuilder();
            sql.append("select * from TempProPlayer order by PlayerId");

            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());

            while (crs.next()) {
                int playerid = crs.getInt("PlayerId");
                String country = crs.getString("Team");
                String first = crs.getString("FirstName");
                String last = crs.getString("LastName");
                int statsId = crs.getInt("StatsPlayerId");
                int countryId = 0;
                int positionId = Position.GOLFER;

                StringBuilder tempSql2 = new StringBuilder();
                tempSql2.append("select * from ").append(CTApplication.TBL_PREF).append("Player where PlayerID = ").append(playerid);
                _Logger.info(tempSql2.toString());
                CachedRowSet crs2 = CTApplication._CT_QUICK_DB.executeQuery(con,tempSql2.toString());
                if (crs2.next() && playerid > 0) {
                      // no need to update currently
                } else {

                    // get the countryId based on the country
                    if (!AuUtil.isEmpty(country))
                    {
                        StringBuilder tempSql3 = new StringBuilder();
                        tempSql3.append("select * from ").append(CTApplication.TBL_PREF).append("Country where Country = '").append(country).append("'");
                        _Logger.info(tempSql3.toString());
                        CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery(con,tempSql3.toString());
                        if (crs3.next()) {
                            countryId = crs3.getInt("CountryID");
                        } else
                        {
                            // create a new country
                            sql = new StringBuilder();
                            sql.append("insert into ").append(CTApplication.TBL_PREF).append("Country(Country) ");
                            sql.append("values('").append(FSUtils.fixupUserInputForDB(country)).append("')");

                            _Logger.info(sql.toString());
                            CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());
                            
                            tempSql3 = new StringBuilder();
                            tempSql3.append("select * from ").append(CTApplication.TBL_PREF).append("Country where Country = '").append(country).append("'");
                            _Logger.info(tempSql3.toString());
                            crs3 = CTApplication._CT_QUICK_DB.executeQuery(con,tempSql3.toString());
                            if (crs3.next()) {
                                countryId = crs3.getInt("CountryID");
                            }
                            
                        }
                    }
                    
                    int active = 1;
                    sql = new StringBuilder();
                    sql.append("insert into ").append(CTApplication.TBL_PREF).append("Player(TeamID,FirstName,LastName,StatsPlayerID,CountryID,PositionID,IsActive) ");
                    sql.append("values(").append(Team.PGATOUR);
                    sql.append(", '").append(FSUtils.fixupUserInputForDB(first)).append("' ");
                    sql.append(", '").append(FSUtils.fixupUserInputForDB(last)).append("' ");
                    sql.append(", ").append(statsId);
                    sql.append(", ").append(countryId);
                    sql.append(", ").append(positionId);
                    sql.append(", ").append(active).append(")");

                    _Logger.info(sql.toString());
                    CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());

                }

                crs2.close();
            }

            crs.close();

            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            _Logger.log(Level.SEVERE, "Error in GolfPlayers : {0}", e.getMessage());
        } finally {
            con.close();
        }
    }

    public static void main(String[] args) {
        try {
            GolfPlayers players = new GolfPlayers();
            players.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
