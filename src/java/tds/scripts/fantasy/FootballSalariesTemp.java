/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.scripts.fantasy;

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
public class FootballSalariesTemp {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public FootballSalariesTemp() {
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
            updatePlayers();
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in FootballPlayers.run()", e);
        }
    }

    public void updatePlayers() throws Exception {

        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select * from ct_temp.TempSalaryPlayers ");

            _Logger.info(sql.toString());
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                int positionid = crs.getInt("PositionID");
                String name = crs.getString("Name");
                int id = crs.getInt("ID");

                if (name.indexOf("'") > 0) {
                    continue;
                }
                String sql2 = "select * from Player where PositionID = " + positionid +
                            " and String(FirstName,' ',LastName) = '" + name + "'";
                CachedRowSet crs2 = CTApplication._CT_QUICK_DB.executeQuery(sql2);
                if (crs2.next()) {
                    int playerid = crs2.getInt("PlayerID");

                    name = FSUtils.fixupUserInputForDB(name);
                    String sql3 = "update ct_temp.TempSalaryPlayers " +
                                " set PlayerID = " + playerid +
                                " where ID = " + id;
                    _Logger.info(sql3);
                    CTApplication._CT_QUICK_DB.executeUpdate(sql3);
                }

                crs2.close();
            }

            crs.close();

        } catch (Exception e) {
            _Logger.log(Level.SEVERE,"FootballPlayers Update Error : " + e.getMessage());
            e.printStackTrace();
        } finally {
        }

        return;
    }

    public void insertSalaries() throws Exception {

        int fsseasonweekid = 69;
        try {
            CTApplication._CT_QUICK_DB.executeUpdate("delete from FSPlayerValue where FSSeasonWeekID = " + fsseasonweekid);

            StringBuffer sql = new StringBuffer();
            sql.append("select * from ct_temp.TempSalaryPlayers ");

            _Logger.info(sql.toString());
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                int positionid = crs.getInt("PositionID");
                String name = crs.getString("Name");
                int id = crs.getInt("ID");
                int points = crs.getInt("Points");
                int multiplier = 1000;
                switch (positionid) {
                    case 1 :
                        multiplier = 500;
                        break;
                    case 2 :
                        multiplier = 700;
                        break;
                    case 3 :
                        multiplier = 700;
                        break;
                    case 4 :
                        multiplier = 700;
                        break;
                    case 5 :
                        multiplier = 600;
                        break;
                    case 9 :
                        multiplier = 800;
                        break;
                    case 10 :
                        multiplier = 600;
                        break;
                    case 11 :
                        multiplier = 700;
                        break;

                }
                int salary = points * multiplier;
                int playerid = crs.getInt("PlayerID");

                String sql3 = "insert into FSPlayerValue (PlayerID,FSSeasonWeekID,Value) " +
                            " values (" + playerid + "," + fsseasonweekid + "," + salary + ")";
                _Logger.info(sql3);
                CTApplication._CT_QUICK_DB.executeUpdate(sql3);

            }

            crs.close();

        } catch (Exception e) {
            _Logger.log(Level.SEVERE,"FootballPlayers Update Error : " + e.getMessage());
            e.printStackTrace();
        } finally {
        }

        return;
    }

    public static void main(String[] args) {
        try {
            FootballSalariesTemp players = new FootballSalariesTemp();
            //players.run();
            players.insertSalaries();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
