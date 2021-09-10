/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.sal.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class FootballSalariesTemp implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public FootballSalariesTemp() {
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
            System.out.println("Starting script...");
            updatePlayers();
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in FootballPlayers.run()", e);
        }
    }

    public void updatePlayers() throws Exception {

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select * from TempSalaryPlayers ");

            _Logger.info(sql.toString());
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                int positionid = crs.getInt("PositionID");
                String name = crs.getString("Name").trim();
                String team = crs.getString("Team").trim();
                int id = crs.getInt("TempID");

                if (name.indexOf("'") > 0) {
                    continue;
                }
                String sql2 = "select * from Player p" +
                            " inner join Team t on t.TeamID = p.TeamID" +
                            " where p.PositionID = " + positionid +
                            " and CONCAT(p.FirstName,' ',p.LastName) = '" + name.trim() + "'" +
                            " and p.IsActive = 1" +
                            " and t.Abbreviation = '" + team + "'";
                CachedRowSet crs2 = CTApplication._CT_QUICK_DB.executeQuery(sql2);
                if (crs2.next()) {
                    int playerid = crs2.getInt("PlayerID");

                    name = FSUtils.fixupUserInputForDB(name);
                    String sql3 = "update TempSalaryPlayers " +
                                " set PlayerID = " + playerid +
                                " where TempID = " + id;
                    _Logger.info(sql3);
                    CTApplication._CT_QUICK_DB.executeUpdate(sql3);
                } else {
                    _Logger.warning(sql2);
                }

                crs2.close();
            }

            crs.close();

        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "FootballPlayers Update Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {
        }

        return;
    }

    public void insertSalaries() throws Exception {

//        int fsseasonweekid = 447;
        int fsseasonweekid = 1298;
        try {
            CTApplication._CT_QUICK_DB.executeUpdate("delete from FSPlayerValue where FSSeasonWeekID = " + fsseasonweekid);

            StringBuilder sql = new StringBuilder();
            sql.append("select * from TempSalaryPlayers where PlayerID is not null");

            _Logger.info(sql.toString());
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                int positionid = crs.getInt("PositionID");
//                String name = crs.getString("Name");
//                int id = crs.getInt("TempID");
                int points = crs.getInt("Points");
//                String team = crs.getString("Team");

//                int multiplier = 1000;
//                switch (positionid) {
//                    case 1 :
//                        //multiplier = 500;
//                        multiplier = 700;
//                        break;
//                    case 2 :
//                        //multiplier = 700;
//                        multiplier = 900;
//                        break;
//                    case 3 :
//                        //multiplier = 700;
//                        multiplier = 900;
//                        break;
//                    case 4 :
//                        //multiplier = 700;
//                        multiplier = 900;
//                        break;
//                    case 5 :
//                        //multiplier = 600;
//                        multiplier = 800;
//                        break;
//                    case 9 :
//                        multiplier = 800;
//                        break;
//                    case 10 :
//                        multiplier = 600;
//                        break;
//                    case 11 :
//                        multiplier = 700;
//                        break;
//
//                }
                int multiplier = 800;
                switch (positionid) {
                    case 1 :
                        //multiplier = 500;
//                        multiplier = 700;
                        break;
                    case 2 :
                        //multiplier = 700;
//                        multiplier = 900;
                        break;
                    case 3 :
                        //multiplier = 700;
//                        multiplier = 900;
                        break;
                    case 4 :
                        multiplier = 700;
                        //multiplier = 900;
                        break;
                    case 5 :
                        //multiplier = 600;
                        multiplier = 600;
                        break;
                    case 9 :
//                        multiplier = 800;
                        break;
                    case 10 :
//                        multiplier = 600;
                        break;
                    case 11 :
//                        multiplier = 700;
                        break;

                }
                int salary = points * multiplier;
                int playerid = crs.getInt("PlayerID");

                StringBuilder sql3 = new StringBuilder();
                sql3.append("insert into FSPlayerValue (PlayerID,FSSeasonWeekID,Value) ");
                sql3.append(" values (").append(playerid);
                sql3.append(",").append(fsseasonweekid);
                sql3.append(",").append(salary).append(")");
                _Logger.info(sql3.toString());
                CTApplication._CT_QUICK_DB.executeUpdate(sql3.toString());

            }

            crs.close();

        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "FootballPlayers Update Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {
        }

        return;
    }

    public void newWeek(int oldfsseasonweekid) throws Exception {

        int newfsseasonweekid = oldfsseasonweekid + 1;
        try {
            CTApplication._CT_QUICK_DB.executeUpdate("delete from FSPlayerValue where FSSeasonWeekID = " + newfsseasonweekid);

            StringBuilder sql = new StringBuilder();
            sql.append("select * from FSPlayerValue where FSSeasonWeekID = ").append(oldfsseasonweekid);

            _Logger.info(sql.toString());
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                int playerid = crs.getInt("PlayerID");
                double value = crs.getDouble("Value");


                StringBuilder sql3 = new StringBuilder();
                sql3.append("insert into FSPlayerValue (PlayerID,FSSeasonWeekID,Value) ");
                sql3.append(" values (").append(playerid);
                sql3.append(",").append(newfsseasonweekid);
                sql3.append(",").append(value).append(")");
                _Logger.info(sql3.toString());
                CTApplication._CT_QUICK_DB.executeUpdate(sql3.toString());

            }

            crs.close();

        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "FootballPlayers Update Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {
        }

        return;
    }

    public static void main(String[] args) {
        try {
            FootballSalariesTemp players = new FootballSalariesTemp();
//            players.run();
            players.insertSalaries();
            //int oldfsseasonweekid = 71;
            //players.newWeek(oldfsseasonweekid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
