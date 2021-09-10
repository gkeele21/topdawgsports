/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.scripts.ncaa;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.Application;
import bglib.util.AuUtil;
import tds.main.bo.CTApplication;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class NCAAFootballPlayers implements Harnessable {

    public static final String NCAAROSTER_DIR_PROP = "tds.Football.ncaaroster.directory";
    //public static Properties settings = new Properties();
    private static String _PLAYERS_FILE = Application._GLOBAL_SETTINGS.getProperty(NCAAROSTER_DIR_PROP);
    private static final int _FSSeasonID = 8;
    private static final int _SeasonID = 4;

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public static String INSTITUTIONID = "InstitutionID";
    public static String INSTITUTION = "Institution";
    public static String UNIFORMNUMBER = "UniformNumber";
    public static String LASTNAME = "LastName";
    public static String FIRSTNAME = "FirstName";
    public static String POSITION = "Position";
    public static String YEAR = "Year";
    public static String PLAYERID = "StatsPlayerID";

    private static final String[] TEMPPLAYERS = new String[] { INSTITUTIONID, INSTITUTION,
        UNIFORMNUMBER, LASTNAME, FIRSTNAME, POSITION, YEAR, PLAYERID };

    public NCAAFootballPlayers() {
        _Logger = Logger.global;
    }

    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    public ResultCode getResultCode() { return _ResultCode; }

    public void setScriptArgs(String[] args) { _Args = args; }

    public static void main(String[] args) {
        try {
            NCAAFootballPlayers players = new NCAAFootballPlayers();

            players.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {

        try {

            _Logger.info("Starting to update NCAA player list...");
            importPlayers();
            updateNCAAPlayers();
            _Logger.info("Done.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in NCAAFootballPlayers.run()", e);
        }

    }

    private void importPlayers() {
        System.out.println("Reading from file.");

        try {
            // clear out any existing rows.
            CTApplication._CT_QUICK_DB.executeUpdate("delete from ct_temp.TempNCAAPlayer");

            InputStream fis = new FileInputStream(_PLAYERS_FILE);
            byte[] byteArr = new byte[1000000];
            fis.read(byteArr);

            String str = new String(byteArr, "utf8");

            List playerscolumns = Arrays.asList(TEMPPLAYERS);

            StringTokenizer st = new StringTokenizer(str,"\n");
            int lineNumber = 0;
            while (st.hasMoreTokens()) {
                try {
                    String line = st.nextToken();
                    lineNumber++;
                    if (line != null && lineNumber > 2) {
                        _Logger.info("line = " + line);
                        int x = 0;
                        System.out.println("==NEW PLAYER==");
                        String[] player = new String[playerscolumns.size()];

                        StringTokenizer stComma = new StringTokenizer(line,"\t");
                        while (stComma.hasMoreTokens()) {
                            String fieldValue = stComma.nextToken();
                            fieldValue = AuUtil.replace(fieldValue,"\"","");
                            fieldValue = AuUtil.replace(fieldValue,"\'","''");

                            System.out.println("Field : " + fieldValue);
                            if (x < player.length) {
                                player[x] = fieldValue;
                            }

                            x++;

                        }

                        if (player[2] != null && !player[2].equals("")) {
                            // insert this player's data
                            StringBuffer sql = new StringBuffer();
                            sql.append("insert into ct_temp.TempNCAAPlayer (");
                            for (x = 0;x < playerscolumns.size();x++) {
                                String colname = TEMPPLAYERS[x];
                                sql.append(colname);
                                if (x != playerscolumns.size() - 1) {
                                    sql.append(",");
                                }
                            }
                            sql.append(") values (");
                            for (x = 0;x < player.length; x++) {
                                String value = player[x];
                                if (x == 7) {
                                    sql.append(value);
                                } else {
                                    sql.append("'" + value + "'");
                                }
                                if (x != player.length - 1) {
                                    sql.append(",");
                                }
                            }

                            sql.append(")");
                            _Logger.info(sql.toString());
                            System.out.println(sql.toString());
                            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    _Logger.log(Level.SEVERE, "Exception in NCAAPlayer.run()", e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            _Logger.log(Level.SEVERE, "Exception in NCAAPlayer.run()", e);
        }

    }

    private void updateNCAAPlayers() {


    }



}
