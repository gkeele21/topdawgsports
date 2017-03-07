/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.scripts.fantasy;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.AppSettings;
import bglib.util.Application;
import bglib.util.FSUtils;
import bglib.util.FileUtils;
import tds.main.bo.CTApplication;
import tds.main.bo.Season;
import tds.main.bo.SeasonWeek;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.jdbc.rowset.CachedRowSet;

/**
 *
 * @author grant.keele
 */
public class FootballInjuryReport implements Harnessable {

    public static final String INJURIES_DIR_PROP = "tds.Football.injuries.directory";
    public static final Application _CT_APP = Application.getInstance(CTApplication.APP_PREFIX);
    public static final AppSettings _CT_APP_SETTINGS = _CT_APP.getAppSettings();
    public static final int _SeasonID = 4;
    private static String _INJURIES_DIR = _CT_APP_SETTINGS.getProperty(INJURIES_DIR_PROP);
    
    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public static String PLAYERNAME = "PlayerName";
    public static String STATSPLAYERID = "StatsPlayerID";
    public static String PROTEAM = "Team";
    public static String PROTEAMID = "TeamID";
    public static String POSITION = "Position";
    public static String INJURYSTATUS = "InjuryStatus";
    public static String INJURYSTATUSLONG = "InjuryStatusLong";
    public static String INJURY = "Injury";

    private static final String[] TEMPPLAYER = new String[] { PLAYERNAME,STATSPLAYERID,PROTEAM,
        PROTEAMID,POSITION,INJURYSTATUS,INJURYSTATUSLONG,INJURY };

    // the stats come in a fixed-width format.  This lists the starting position number for each
    // column that exists above.
    private static final int[] TEMPPLAYERCOLUMNS = new int[] { 1,27,32,36,41,44,46,63 };

    public FootballInjuryReport() {
        _Logger = Logger.global;
    }

    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    public ResultCode getResultCode() { return _ResultCode; }

    public void setScriptArgs(String[] args) { _Args = args; }

    public void run() {
        try {
            int seasonweekid = 0;
            Season season = new Season(_SeasonID);

            if (_Args.length>0) {
                try {
                    seasonweekid = Integer.parseInt(_Args[0]);
                }
                catch (Exception e) {
                    _Logger.warning("Did not recognize argument '" + _Args[0] + "' as an integer; will use last week's.");
                }
            }

            if (seasonweekid == 0) {
                SeasonWeek sw = SeasonWeek.GetCurrentSeasonWeek(_SeasonID);
                seasonweekid = sw.getSeasonWeekID();
            }

            SeasonWeek seasonWeek = new SeasonWeek(seasonweekid);

            // Check to see if the injury dir exists
            String injdir = _CT_APP_SETTINGS.getProperty(INJURIES_DIR_PROP);
            if (injdir == null || injdir.length() < 1) {
                _ResultCode = ResultCode.RC_ERROR;
                _Logger.log(Level.SEVERE, "You must first set the " + INJURIES_DIR_PROP + " property so we know where to grab the injury report files.");
                return;
            }

            _Logger.info("Running injury report for seasonweekid : " + seasonweekid);
            String year = (""+season.getSportYear()).substring(2);
            int weekNo = seasonWeek.getWeekNo();
            String week = weekNo < 10 ? "0"+weekNo : ""+weekNo;
            String extension = "txt";
            String day1 = "f";
            String day2 = "w";
            String injuryfilef = year+"inj"+week+"-"+day1+"."+extension;
            String injuryfilew = year+"inj"+week+"-"+day2+"."+extension;
            String irfilef = year+"ir"+week+"-"+day1+"."+extension;
            String irfilew = year+"ir"+week+"-"+day2+"."+extension;

            boolean fileexistsf = FSUtils.fileExists(injdir,injuryfilef,_Logger);
            boolean fileexistsw = FSUtils.fileExists(injdir,injuryfilew,_Logger);
            boolean irfileexistsf = FSUtils.fileExists(injdir,irfilef,_Logger);
            boolean irfileexistsw = FSUtils.fileExists(injdir,irfilew,_Logger);

            String file = fileexistsf ? injuryfilef : injuryfilew;
            String irfile = irfileexistsf ? irfilef : irfilew;

            // if the irfile doesn't exist, we really don't care.
            if (fileexistsf || fileexistsw || irfileexistsf || irfileexistsw) {
                _Logger.info("Injury Files found - Starting...");
                int startinglinenumber = 3;
                int minimumLineLength = 40;
                String badChar = ";";
                if (fileexistsf || fileexistsw)
                    FileUtils.importFromFixedWidth(injdir,file,"ct_temp.TempFootballInjury",true,startinglinenumber,minimumLineLength,badChar,TEMPPLAYER,TEMPPLAYERCOLUMNS,_Logger);
                if (irfileexistsf || irfileexistsw)
                    FileUtils.importFromFixedWidth(injdir,irfile,"ct_temp.TempFootballInjury",false,startinglinenumber,minimumLineLength,badChar,TEMPPLAYER,TEMPPLAYERCOLUMNS,_Logger);
                _Logger.info("Injuries inserted into TempFootballInjury...");
                insertIntoPlayerInjury();
                _Logger.info("Done!");
                _ResultCode = ResultCode.RC_SUCCESS;
            } else {
                _Logger.warning("Injury Files don't exist... exiting.");
                _ResultCode = ResultCode.RC_INCOMPLETE;
            }

        } catch (Exception e) {
            _ResultCode = ResultCode.RC_ERROR;
            _Logger.log(Level.SEVERE, "Exception in FootballInjuryReport.run()", e);
        }

    }

    public void insertIntoPlayerInjury() throws Exception {

        Connection con = null;
        try {
            con = CTApplication._CT_QUICK_DB.getConn(false);

            CTApplication._CT_QUICK_DB.executeUpdate(con,"delete from PlayerInjury");

            StringBuffer sql = new StringBuffer();
            sql.append("select * from ct_temp.TempFootballInjury order by StatsPlayerid");

            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(con,sql.toString());

            while (crs.next()) {
                int playerid = crs.getInt("StatsPlayerID");
                String status = crs.getString("InjuryStatus").trim();
                String statuslong = crs.getString("InjuryStatusLong").trim();
                String injury = crs.getString("Injury").trim();

                if (status.equals("P") && !statuslong.equals("Probable")) {
                    status = "PUP";
                }
                if (status.equals("I")) {
                    status = "IR";
                }
                if (status.equals("A") || status.equals("B") || status.equals("C")) {
                    continue;
                }
                
                // Grab the ProPlayerID based on the playerid (StatsPlayerID) from the temptable.
                CachedRowSet crs2 = CTApplication._CT_QUICK_DB.executeQuery(con,"select * from Player where StatsPlayerID = " + playerid);
                if (crs2.next()) {

                    int proplayerid = crs2.getInt("PlayerID");

                    sql = new StringBuffer();
                    sql.append("insert into PlayerInjury ");
                    sql.append(" (PlayerID,InjuryStatus,InjuryStatusLong,Injury) ");
                    sql.append(" values (" + proplayerid + ",'" + FSUtils.fixupUserInputForDB(status) + "' ");
                    sql.append(" ,'" + FSUtils.fixupUserInputForDB(statuslong) + "' ");
                    sql.append(" ,'" + FSUtils.fixupUserInputForDB(injury) + "')");

                    try {
                        CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());
                        _Logger.info(sql.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        // player injury record already exists, so run an update
                        sql = new StringBuffer();
                        sql.append("update PlayerInjury ");
                        sql.append(" set InjuryStatus = '" + FSUtils.fixupUserInputForDB(status) + "' ");
                        sql.append(" ,InjuryStatusLong = '" + FSUtils.fixupUserInputForDB(statuslong) + "' ");
                        sql.append(" ,Injury = '" + FSUtils.fixupUserInputForDB(injury) + "' ");
                        sql.append(" where PlayerID = " + proplayerid);

                        int num = CTApplication._CT_QUICK_DB.executeUpdate(con,sql.toString());
                        System.out.println("Num updated : " + num);
                        _Logger.info(sql.toString());
                    }

                } else {
                    // The statsplayerid doesn't exist in the db, so we don't care about
                    // entering in this injury.
                }

                crs2.close();
            }

            crs.close();

            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            _Logger.log(Level.SEVERE,"Error in FootballInjuryReport : " + e.getMessage());
        } finally {
            con.close();
        }
    }

    public static void main(String[] args) {
        try {
            FootballInjuryReport report = new FootballInjuryReport();
            report.setScriptArgs(args);
            report.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
