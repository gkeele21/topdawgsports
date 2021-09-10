/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.fantasy.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.Application;
import bglib.util.AuUtil;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;
import tds.main.bo.FSGame;
import tds.main.bo.FSSeason;
import tds.main.bo.FSSeasonWeek;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class FootballStats implements Harnessable {

    public static final String STATS_DIR_PROP = "tds.Football.stats.directory";
    //public static Properties settings = new Properties();
    private static String _STATS_DIR = Application._GLOBAL_SETTINGS.getProperty(STATS_DIR_PROP);
//    private static final int _FSSeasonID = 20;
//    private static final int _SeasonID = 8;
    private static final String _Year = "13";

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    private List<MissingPlayer> missingPlayers = new ArrayList<MissingPlayer>();

    public static final String PLAYERNAME = "Name";
    public static final String PLAYERID = "StatsPlayerID";
    public static final String PROTEAM = "ProTeam";
    public static final String PROTEAMID = "ProTeamID";
    public static final String POSITION = "Position";
    public static final String INJURYSTATUS = "InjuryStatus";
    public static final String STARTED = "Started";
    public static final String PLAYED = "Played";
    public static final String INACTIVE = "Inactive";
    public static final String PASSCOMP = "PassComp";
    public static final String PASSATT = "PassAtt";
    public static final String PASSYARDS = "PassYards";
    public static final String PASSINT = "PassInt";
    public static final String PASSTD = "PassTD";
    public static final String PASS2PT = "PassTwoPt";
    public static final String SACKED = "Sacked";
    public static final String SACKEDYARDSLOST = "SackedYardsLost";
    public static final String RUSHATT = "RushAtt";
    public static final String RUSHYARDS = "RushYards";
    public static final String RUSHTD = "RushTD";
    public static final String RUSH2PT = "RushTwoPt";
    public static final String RECTARGETS = "RecTargets";
    public static final String RECCATCHES = "RecCatches";
    public static final String RECYARDS = "RecYards";
    public static final String RECTD = "RecTD";
    public static final String REC2PT = "RecTwoPt";
    public static final String XPM = "XPM";
    public static final String XPA = "XPA";
    public static final String XPBLOCKED = "XPBlocked";
    public static final String FGM = "FGM";
    public static final String FGA = "FGA";
    public static final String FGBLOCKED = "FGBlocked";
    public static final String FG_29 = "FG29MINUS";
    public static final String FG_30_39 = "FG30TO39";
    public static final String FG_40_49 = "FG40TO49";
    public static final String FG50_ = "FG50PLUS";
    public static final String FUMBLESLOST = "FumblesLost";
    public static final String TDDISTANCES = "TDDistances";

//    public static String DEFTEAMNAME = "TeamName";
//    public static String DEFTEAMSHORT = "TeamShort";
//    public static String DEFTEAMID = "TeamID";
//    public static String DEFOPPONENTNAME = "OpponentName";
//    public static String DEFOPPONENTSHORT = "OpponentShort";
//    public static String DEFOPPONENTID = "OpponentID";
//    public static String DEFPOINTSOWN = "PointsOwn";
//    public static String DEFPOINTSOPP = "PointsOpp";
//    public static String DEFTOTALYARDS = "TotalYards";
//    public static String DEFTOTALPLAYS = "TotalPlays";
//    public static String DEFPASSCOMP = "PassComp";
//    public static String DEFPASSATT = "PassAtt";
//    public static String DEFPASSYARDS = "PassYards";
//    public static String DEFPASSTD = "PassTD";
//    public static String DEFSACKED = "Sacked";
//    public static String DEFSACKEDYARDSLOST = "SackedYardsLost";
    public static final String DEFPASSESDEFENSED = "IDPPassesDefensed";
//    public static String DEFRUSHATT = "RushAtt";
//    public static String DEFRUSHYARDS = "RushYards";
//    public static String DEFRUSHTD = "RushTD";
//    public static String DEFTACKLESFORLOSS = "TacklesForLoss";
//    public static String DEFTFLYARDSLOST = "TFLYardsLost";
    public static final String DEFINTERCEPTIONS = "IDPInterceptions";
    public static final String DEFINTRETURNYARDS = "IDPIntReturnYards";
    public static final String DEFINTRETURNTD = "IDPIntReturnsForTD";
    public static final String DEFFUMBLESFORCED = "IDPFumblesForced";
    public static final String DEFFUMBLESRECOVERED = "IDPFumbleRecoveries";
    public static final String DEFFUMBLERETURNYARDS = "IDPFumbleReturnYards";
    public static final String DEFFUMBLERETURNTD = "IDPFumbleReturnsForTD";
    public static final String DEFSAFETIES = "IDPSafeties";
//    public static String DEFTWOPTCONV = "TwoPtConv";
//    public static String DEFTWOPTATT = "TwoPtAtt";
//    public static String DEFPENALTIES = "Penalties";
//    public static String DEFPENALTYYARDS = "PenaltyYards";
//    public static String DEFPATMADE = "PATMade";
//    public static String DEFPATATT = "PATAtt";
//    public static String DEFGMADE = "FGMade";
//    public static String DEFFGATT = "FGAtt";
//    public static String DEFFIRSTDOWNSTOTAL = "FirstDownsTotal";
//    public static String DEFFIRSTDOWNSRUSHING = "FirstDownsRushing";
//    public static String DEFFIRSTDOWNSPASSING = "FirstDownsPassing";
//    public static String DEFFIRSTDOWNSPENALTY = "FirstDownsPenalty";
//    public static String DEFTHIRDDOWNCONV = "ThirdDownConv";
//    public static String DEFTHIRDDOWNCONVATT = "ThirdDownConvAtt";
//    public static String DEFFOURTHDOWNCONV = "FourthDownConv";
//    public static String DEFFOURTHDOWNCONVATT = "FourthDownConvAtt";
//    public static String DEFTIMEOFPOSSESSION = "TimeOfPossession";
//    public static String DEFTDSCORED = "DefTDScored";
//    public static String DEFPTSSCOREDBYDEF = "PtsScoredByDef";
//    public static String DEFPTSBYOPPDEF = "PtsByOppDef";
//    public static String DEFPTSALLOWED1ST = "PtsAllowedFirst";
//    public static String DEFPTSALLOWED2ND = "PtsAllowedSecond";
//    public static String DEFPTSALLOWED3RD = "PtsAllowedThird";
//    public static String DEFPTSALLOWED4TH = "PtsAllowedFourth";
//    public static String DEFPTSALLOWEDOT = "PtsAllowedOT";
    public static final String DEFTDDISTANCES = "IDPTDDistances";
    public static final String DEFTACKLES = "IDPTackles";
    public static final String DEFASSISTS = "IDPAssists";
    public static final String DEFSACKS = "IDPSacks";
    public static final String DEFSACKYARDSLOST = "IDPSackYardsLost";
    public static final String DEFQBHURRIES = "IDPQBHurries";

    private static final String[] TEMPPLAYERSTATS = new String[] { PLAYERNAME,PLAYERID,PROTEAM,
        PROTEAMID,POSITION,INJURYSTATUS,STARTED,PLAYED,INACTIVE,PASSCOMP,PASSATT,PASSYARDS,
        PASSINT,PASSTD,PASS2PT,SACKED,SACKEDYARDSLOST,RUSHATT,RUSHYARDS,RUSHTD,RUSH2PT,
        RECTARGETS,RECCATCHES,RECYARDS,RECTD,REC2PT,XPM,XPA,XPBLOCKED,FGM,FGA,FGBLOCKED,FG_29,FG_30_39,FG_40_49,FG50_,
        FUMBLESLOST, TDDISTANCES };

    // the stats come in a fixed-width format.  This lists the starting position number for each
    // column that exists above.
    private static final int[] TEMPPLAYERSTATSCOLUMNS = new int[] { 1,27,33,37,42,45,47,49,51,53,
            57,61,65,68,71,74,78,82,86,90,93,96,100,104,108,111,114,118,122,125,129,133,136,140,144,148,152,155};

    private static final String[] TEMPDEFENSESTATS = new String[] { PLAYERNAME,PLAYERID,PROTEAM,
        PROTEAMID,POSITION,INJURYSTATUS,STARTED,PLAYED,INACTIVE,DEFTACKLES,DEFASSISTS,DEFSACKS,
        DEFSACKYARDSLOST,DEFPASSESDEFENSED,DEFQBHURRIES,DEFINTERCEPTIONS,DEFINTRETURNYARDS,
        DEFINTRETURNTD,DEFFUMBLESFORCED,DEFFUMBLESRECOVERED,DEFFUMBLERETURNYARDS,DEFFUMBLERETURNTD,
        DEFSAFETIES,DEFTDDISTANCES };

    private static final int[] TEMPDEFENSESTATSCOLUMNS = new int[] { 1,27,32,36,41,44,46,48,50,
            52,56,60,65,71,74,77,80,84,87,90,93,97,101,104};

    public FootballStats() {
        System.out.println("Instantiating Logger...");
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

    public static void main(String[] args) {
        try {
            FootballStats stats = new FootballStats();
//            FootballStats.findStatsPlayerIds();

            stats.run();
            /*int seasonid = 4;
            int seasonweekid = 33;
            stats.tempInsertIntoFootballStats(seasonid, seasonweekid);
            stats.insertIntoTotalFootballStats(seasonid, 1);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        int statsofficial = 1;
        _Logger.info("Starting...");

        FSGame fsGame = new FSGame(1);
        int currentFSSeasonID = fsGame.getCurrentFSSeasonID();
        FSSeason currentFSSeason = new FSSeason(currentFSSeasonID);
        int fsSeasonWeekID = currentFSSeason.getCurrentFSSeasonWeekID();
//        int fsSeasonWeekID = 431;
        int currentSeasonID = currentFSSeason.getSeasonID();

        try {

//            int weekNo=0;
            FSSeasonWeek fsseasonweek = new FSSeasonWeek(fsSeasonWeekID);
            //int seasonweekid = fsseasonweek.getSeasonWeekID();
            int seasonweeknum = fsseasonweek.getFSSeasonWeekNo();

//
//            if (_Args.length>0) {
//                try {
//                    AuDate playDate = new AuDate(_Args[0], FSConstants.PLAYDATE_PATTERN);
//                    weekNo = WeekInfo.getWeekInfo(Season.getCurrentSeason(), playDate).getWeekNo();
//                }
//                catch (Exception e) {
//                    _Logger.warning("Did not recognize argument '" + _Args[0] + "' as a date; will calculate last week's results.");
//                }
//            }
//
//            if (weekNo==0) {
//                AuDate now = new AuDate();
//                WeekInfo currentweekinfo = WeekInfo.getWeekInfo(season,now);
//                if (now.before(currentweekinfo.getStartDate(),true)) {
//                    currentweekinfo = WeekInfo.getWeekInfo(season,currentweekinfo.getWeekNo()-1);
//                }
//                weekNo = currentweekinfo.getWeekNo();
//            }
//
//            initSettings();
//
//            //WeekInfo wi = WeekInfo.getWeekInfo(season, weekNo);
//            // Check to see if the stats file exists
            String statsdir = _STATS_DIR;
            if (statsdir == null || statsdir.length() < 1) {
                _ResultCode = ResultCode.RC_ERROR;
                _Logger.log(Level.SEVERE, "You must first set the " + STATS_DIR_PROP + " property in  so we know where to grab the stats.");
                return;
            }

            _Logger.info("Running stats for WeekNo " + seasonweeknum);
            //String year = (""+season.getYear()).substring(2);
            String week = seasonweeknum < 10 ? "0"+seasonweeknum : ""+seasonweeknum;
            String extension = "txt";
            String statsfile = _Year+"wk"+week+"co."+extension;
            String statsfiledefense = _Year+"wk"+week+"cd."+extension;

//            boolean file1exists = FSUtils.fileExists(statsdir,statsfile,_Logger);
            boolean file1exists = true;
            boolean file2exists = FSUtils.fileExists(statsdir,statsfiledefense,_Logger);
            //if (file1exists && file2exists) {
            if (file1exists) {
                _Logger.info("Stats Files found - Starting stats...");
                insertIntoTempFootballStatsFixedWidth(currentFSSeasonID,statsdir,statsfile,"TempFootballStats");
                //insertIntoTempFootballStatsFixedWidth(seasonid,statsdir,statsfiledefense,"TempFootballStatsIDP");
                _Logger.info("Stats inserted into TempFootballStats...");
                insertIntoFootballStats(currentSeasonID,fsseasonweek.getSeasonWeekID(),statsofficial);
                _Logger.info("Stats inserted into FootballStats...");
                insertIntoTotalFootballStats(currentSeasonID, statsofficial);
                _Logger.info("Stats inserted into TotalFootballStats...");
                checkNonMatchingIDs(currentSeasonID);
                _Logger.info("Done!");
                _ResultCode = ResultCode.RC_SUCCESS;
            } else {
                _Logger.warning("Stats Files don't exist... exiting.");
                _ResultCode = ResultCode.RC_INCOMPLETE;
            }

        } catch (Exception e) {
            _ResultCode = ResultCode.RC_ERROR;
            _Logger.log(Level.SEVERE, "Exception in FootballStats.run()", e);
        }
    }

    private void insertIntoTempFootballStatsFixedWidth(int seasonid,String dir,String filename,String tablename) throws Exception {

        _Logger.info("Grabbing stats from file : " + dir + "/" + filename);
        try {
            // clear out any existing rows.
            CTApplication._CT_QUICK_DB.executeUpdate("delete from " + tablename);

            InputStream fis = new FileInputStream(dir+"/"+filename);
            byte[] byteArr = new byte[200000];
            fis.read(byteArr);

            String str = new String(byteArr, "utf8");

            List statscolumns = tablename.equals("TempFootballStats") ? Arrays.asList(TEMPPLAYERSTATS) : Arrays.asList(TEMPDEFENSESTATS);
            int[] statscolumnspositions = tablename.equals("TempFootballStats") ? TEMPPLAYERSTATSCOLUMNS : TEMPDEFENSESTATSCOLUMNS;

            StringTokenizer st = new StringTokenizer(str,"\n");
            while (st.hasMoreTokens()) {
                try {
                    String line = st.nextToken();
                    if (line != null && !line.startsWith(";") && line.length() > 100) {
                        _Logger.info("line = " + line);
                        int x = 0;
                        int numpos = statscolumnspositions.length;

                        int size = line.length();
                        String[] player = new String[statscolumns.size()];

                        while (x < numpos) {
                            int startpos = statscolumnspositions[x] - 1;
                            int endpos = x == numpos-1 ? size : statscolumnspositions[x+1]-2;

                            String value = line.substring(startpos,endpos).trim();
                            value = AuUtil.replace(value,"\"","");
                            value = AuUtil.replace(value,"'","''");
                            if (value == null) {
                                value = "";
                            }
                            player[x] = value;
                            x++;
                        }

                        if (player[0] != null && !player[0].equals("")) {
                            // insert this player's data
                            StringBuffer sql = new StringBuffer();
                            sql.append("insert into " + tablename + " (");
                            for (x = 0;x < statscolumns.size();x++) {
                                String colname = tablename.equals("TempFootballStats") ? TEMPPLAYERSTATS[x] : TEMPDEFENSESTATS[x];
                                sql.append(colname);
                                if (x != statscolumns.size() - 1) {
                                    sql.append(",");
                                }
                            }
                            sql.append(") values (");
                            for (x = 0;x < player.length; x++) {
                                String value = player[x];
                                sql.append("'" + value + "'");
                                if (x != player.length - 1) {
                                    sql.append(",");
                                }
                            }

                            sql.append(")");
                            _Logger.info(sql.toString());
                            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    _Logger.log(Level.SEVERE, "Exception in FootballStats.run()", e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            _Logger.log(Level.SEVERE, "Exception in FootballStats.run()", e);
        }

    }

    private void insertIntoFootballStats(int seasonid,int seasonweekid, int statsofficial) throws Exception {

        // clear out existing footballstats
        CTApplication._CT_QUICK_DB.executeUpdate("delete from FootballStats where seasonweekid = " + seasonweekid);

        // Import Offensive Stats
        StringBuilder sql = new StringBuilder();
        sql.append("select * from TempFootballStats");

        CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
        int count = 0;
        while (crs.next()) {

            count++;
            System.out.println("Count : " + count);
            int playerid = crs.getInt(PLAYERID);
            String team = crs.getString(PROTEAM);
            int teamid = crs.getInt(PROTEAMID);
            String position = crs.getString(POSITION);
            String injurystatus = crs.getString(INJURYSTATUS);
            String startedTemp = crs.getString(STARTED);
            int started = startedTemp == null || startedTemp.equals("") ? 0 : Integer.parseInt(startedTemp);
            int played = crs.getInt(PLAYED);
            Boolean obj = crs.getBoolean(INACTIVE);
            int inactive = obj ? 1 : 0;

            int comp = crs.getInt(PASSCOMP);
            int att = crs.getInt(PASSATT);
            int inter = crs.getInt(PASSINT);
            int passyards = crs.getInt(PASSYARDS);
            int passtd = crs.getInt(PASSTD);
            int passtwopt = crs.getInt(PASS2PT);
            int sacked = crs.getInt(SACKED);
            int sackedyardslost = crs.getInt(SACKEDYARDSLOST);
            int rush = crs.getInt(RUSHATT);
            int rushyards = crs.getInt(RUSHYARDS);
            int rushtd = crs.getInt(RUSHTD);
            int rushtwopt = crs.getInt(RUSH2PT);
            int rec = crs.getInt(RECCATCHES);
            int recyards = crs.getInt(RECYARDS);
            int rectd = crs.getInt(RECTD);
            int rectwopt = crs.getInt(REC2PT);
            int xpa = crs.getInt(XPA);
            int xp = crs.getInt(XPM);
            int xpblocked = crs.getInt(XPBLOCKED);
            int fga = crs.getInt(FGA);
            int fgm = crs.getInt(FGM);
            int fgblocked = crs.getInt(FGBLOCKED);
            int fg29under = crs.getInt(FG_29);
            int fg30_39 = crs.getInt(FG_30_39);
            int fg40_49 = crs.getInt(FG_40_49);
            int fgover50 = crs.getInt(FG50_);
            int fumbleslost = crs.getInt(FUMBLESLOST);
            String tddistances = crs.getString(TDDISTANCES);

            int xtratd = 0;
            int fglengths = 0;

            // Figure the number of xtra tds
            if (tddistances != null && !tddistances.equals("")) {
                StringBuilder st = new StringBuilder(tddistances);
                for (int x=0;x<st.length();x++) {
                    char ch = st.charAt(x);

                    if (ch == 'K' || ch == 'P' || ch == 'B' || ch == 'I' || ch == 'F') {
                        xtratd++;
                    } else if (ch == 'G') {
                        int newlength = Integer.parseInt(st.substring((x+2),(x+4)));
                        if (newlength < 30) {
                            newlength = 30;
                        }

                        fglengths = fglengths + newlength;
                    }
                }
            }

            // remove any negative yardage
            if (passyards < 0)
                passyards = 0;
            if (rushyards < 0)
                rushyards = 0;
            if (recyards < 0)
                recyards = 0;

            // calculate Player Fantasy Points

            double fantasypts = 0.00;
            fantasypts = 6*((double)passtd+(double)rushtd+(double)rectd+(double)xtratd) + ((double)passyards/25.0) +
                        ((double)rushyards/10.0) + ((double)recyards/10.0) +
                        2*((double)passtwopt + (double)rushtwopt + (double)rectwopt) +
                        ((double)fglengths/10.0) + ((double)xp);
            DecimalFormat twoDForm = new DecimalFormat("#.##");

            fantasypts = Double.valueOf(twoDForm.format(fantasypts));

            double intfantasypts = 0.00;
            for (int x = 1;x <= inter;x++) {
                intfantasypts += x;
            }

            double fumfantasypts = 0.00;
            for (int x = 1;x <= fumbleslost;x++) {
                fumfantasypts += x;
            }

            double salfantasypts = 6*((double)passtd+(double)rushtd+(double)rectd+(double)xtratd) + ((double)passyards/25.0) +
                        ((double)rushyards/10.0) + ((double)recyards/10.0) +
                        2*((double)passtwopt + (double)rushtwopt + (double)rectwopt) +
                        ((double)fglengths/10.0) + ((double)xp) - intfantasypts - fumfantasypts;

            salfantasypts = Double.valueOf(twoDForm.format(salfantasypts));
            _Logger.info("PlayerID : " + playerid + ",Fantasy Pts : " + fantasypts);

            // Insert record into FootballStats table
            sql = new StringBuilder();
            sql.append("insert into FootballStats " +
                        "(StatsPlayerID,SeasonWeekID,SeasonID,TeamID,Position,InjuryStatus,Started,Played," +
                        "Inactive,PassComp,PassAtt,PassYards,PassInt,PassTD,PassTwoPt,Sacked,SackedYardsLost, " +
                        "RushAtt,RushYards,RushTD,RushTwoPt,RecCatches,RecYards,RecTD,RecTwoPt,XPM,XPA,XPBlocked," +
                        "FGM,FGA,FGBlocked,FG29Minus,FG30to39,FG40to49,FG50Plus,FumblesLost,XtraTD," +
                        "TDDistances,StatsOfficial,FantasyPts,SalFantasyPts) " +
                        "values (" + playerid + "," +
                        seasonweekid + "," +
                        seasonid + "," +
                        teamid + ",'" +
                        position + "','" +
                        injurystatus + "'," +
                        started + "," +
                        played + "," +
                        inactive + "," +
                        comp + "," +
                        att + "," +
                        passyards + "," +
                        inter + "," +
                        passtd + "," +
                        passtwopt + "," +
                        sacked + "," +
                        sackedyardslost + "," +
                        rush + "," +
                        rushyards + "," +
                        rushtd + "," +
                        rushtwopt + "," +
                        rec + "," +
                        recyards + "," +
                        rectd + "," +
                        rectwopt + "," +
                        xp + "," +
                        xpa + "," +
                        xpblocked + "," +
                        fgm + "," +
                        fga + "," +
                        fgblocked + "," +
                        fg29under + "," +
                        fg30_39 + "," +
                        fg40_49 + "," +
                        fgover50 + "," +
                        fumbleslost + "," +
                        xtratd + ",'" +
                        tddistances + "'," +
                        statsofficial + "," +
                        fantasypts + "," +
                        salfantasypts + ")");
            _Logger.info(sql.toString());
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        }
        crs.close();

        // Import IDP Stats

        sql = new StringBuilder();
        sql.append("select * from TempFootballStatsIDP");

        crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
        count = 0;
        while (crs.next()) {

            count++;
            int playerid = crs.getInt(PLAYERID);
            String team = crs.getString(PROTEAM);
            int teamid = crs.getInt(PROTEAMID);
            String position = crs.getString(POSITION);
            String injurystatus = crs.getString(INJURYSTATUS);
            String started = crs.getString(STARTED);
            int played = crs.getInt(PLAYED);
            int inactive = crs.getInt(INACTIVE);

            int tackles = crs.getInt(DEFTACKLES);
            int assists = crs.getInt(DEFASSISTS);
            int sacks = crs.getInt(DEFSACKS);
            int sackyardslost = crs.getInt(DEFSACKYARDSLOST);
            int passesdefensed = crs.getInt(DEFPASSESDEFENSED);
            int qbhurries = crs.getInt(DEFQBHURRIES);
            int interceptions = crs.getInt(DEFINTERCEPTIONS);
            int intreturnyards = crs.getInt(DEFINTRETURNYARDS);
            int intreturntd = crs.getInt(DEFINTRETURNTD);
            int fumblesforced = crs.getInt(DEFFUMBLESFORCED);
            int fumblerecoveries = crs.getInt(DEFFUMBLESRECOVERED);
            int fumblereturnyards = crs.getInt(DEFFUMBLERETURNYARDS);
            int fumblereturnsfortd = crs.getInt(DEFFUMBLERETURNTD);
            int safeties = crs.getInt(DEFSAFETIES);
            String tddistances = crs.getString(DEFTDDISTANCES);

            int xtratd = 0;

            // Figure the number of xtra tds
            if (tddistances != null && !tddistances.equals("")) {
                StringBuilder st = new StringBuilder(tddistances);
                for (int x=0;x<st.length();x++) {
                    char ch = st.charAt(x);

                    if (ch == 'K' || ch == 'P' || ch == 'B') {
                        xtratd++;
                    }
                }
            }

            // check to see if any of the idp guys already have a record from the offensive stats
            String tempsql = "select * from FootballStats where SeasonWeekID = " + seasonweekid +
                        " and StatsPlayerID = " + playerid;
            CachedRowSet tempcrs = CTApplication._CT_QUICK_DB.executeQuery(tempsql);
            if (tempcrs.next()) {
                // update existing record

                double fantasypts = tempcrs.getDouble("FantasyPts");
                double salfantasypts = tempcrs.getDouble("SalFantasyPts");

                double deffantpts = 6*((double)intreturntd + (double)fumblereturnsfortd + (double)xtratd) +
                        4*((double)safeties + (double)interceptions + (double)fumblerecoveries) +
                        3*((double)sacks) +
                        2*((double)fumblesforced) +
                        (double)tackles + (double)passesdefensed +
                        .5*((double)assists);

                // for Tenman & Keeper, we don't add in the Defensive Fantasy Points.
                //fantasypts += deffantpts;
                salfantasypts += deffantpts;

                sql = new StringBuilder();
                sql.append(" update FootballStats " +
                            " set IDPTackles = " + tackles +
                            " ,IDPAssists = " + assists +
                            " ,IDPSacks = " + sacks +
                            " ,IDPSackYardsLost = " + sackyardslost +
                            " ,IDPPassesDefensed = " + passesdefensed +
                            " ,IDPQBHurries = " + qbhurries +
                            " ,IDPInterceptions = " + interceptions +
                            " ,IDPIntReturnYards = " + intreturnyards +
                            " ,IDPIntReturnsForTD = " + intreturntd +
                            " ,IDPFumblesForced = " + fumblesforced +
                            " ,IDPFumbleRecoveries = " + fumblerecoveries +
                            " ,IDPFumbleReturnYards = " + fumblereturnyards +
                            " ,IDPFumbleReturnsForTD = " + fumblereturnsfortd +
                            " ,IDPSafeties = " + safeties +
                            " ,IDPTDDistances = '" + tddistances +
                            " ',FantasyPts = " + fantasypts +
                            " ,SalFantasyPts = " + salfantasypts +
                            " where StatsPlayerID = " + playerid +
                            " and SeasonWeekID = " + seasonweekid);

                _Logger.info(sql.toString());
                CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

            } else {
                // calculate Player Fantasy Points
                double fantasypts = 0.0;
                fantasypts = 6*((double)intreturntd + (double)fumblereturnsfortd + (double)xtratd) +
                        4*((double)safeties + (double)interceptions + (double)fumblerecoveries) +
                        3*((double)sacks) +
                        2*((double)fumblesforced) +
                        (double)tackles + (double)passesdefensed +
                        .5*((double)assists);

                // Insert record into FootballStats table
                sql = new StringBuilder();
                sql.append("insert into FootballStats " +
                            "(StatsPlayerID,SeasonWeekID,SeasonID,TeamID,Position,InjuryStatus,Started,Played," +
                            "Inactive,IDPTackles,IDPAssists,IDPSacks,IDPSackYardsLost,IDPPassesDefensed, " +
                            "IDPQBHurries,IDPInterceptions,IDPIntReturnYards,IDPIntReturnsForTD," +
                            "IDPFumblesForced,IDPFumbleRecoveries,IDPFumbleReturnYards,IDPFumbleReturnsForTD," +
                            "IDPSafeties,IDPTDDistances,StatsOfficial,FantasyPts,SalFantasyPts) " +
                            "values (" + playerid + "," +
                            seasonweekid + "," +
                            seasonid + "," +
                            teamid + ",'" +
                            position + "','" +
                            injurystatus + "','" +
                            started + "'," +
                            played + "," +
                            inactive + "," +
                            tackles + "," +
                            assists + "," +
                            sacks + "," +
                            sackyardslost + "," +
                            passesdefensed + "," +
                            qbhurries + "," +
                            interceptions + "," +
                            intreturnyards + "," +
                            intreturntd + "," +
                            fumblesforced + "," +
                            fumblerecoveries + "," +
                            fumblereturnyards + "," +
                            fumblereturnsfortd + "," +
                            safeties + ",'" +
                            tddistances + "'," +
                            statsofficial + "," +
                            fantasypts + "," +
                            fantasypts + ")");

                _Logger.info(sql.toString());
                CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

            }

            tempcrs.close();

        }

        crs.close();

    }

    private void processMissingPlayers() {
        for (MissingPlayer player : missingPlayers) {
            _Logger.info("Missing Player : " + player.getFullName() + " ; Team : " + player.getTeamName() + " ; Position : " + player.getPosition() + "; StatsID : " + player.getStatsplayerid());
        }
    }

    private void checkNonMatchingIDs(int seasonid) {
        try {
            String sql = "select distinct(s.StatsPlayerID) " +
                    " from FootballStats s " +
                    " left join Player p on p.StatsPlayerID = s.StatsPlayerID " +
                    " where s.SeasonID = " + seasonid + " and p.PlayerID is null";
            CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql);
            while (crs.next()) {
                int statsplayerid = crs.getInt("StatsPlayerID");
                // grab this player's FirstName, LastName, and Team from the tempfootballstats table
                String sql2 = "select * from TempFootballStats where StatsPlayerID = " + statsplayerid;
                CachedRowSet crs2 = CTApplication._CT_QUICK_DB.executeQuery(sql2);
                if (crs2.next()) {
                    String name = crs2.getString("Name");
                    String teamname = crs2.getString("ProTeam");
                    String position = crs2.getString("Position");
                    try {
                        if (name != null && !name.equals("")) {
                            //StringTokenizer st = new StringTokenizer(name,",");
                            //String last = st.nextToken().trim();
                            //String first = st.nextToken().trim();
                            int teamid = 0;
                            //int teamid = tds.main.Team.getIDFromTeamName(teamname);

                            // check to see if this player exists
                            String sql3 = "select * from Player " +
                                        " where QuickStatsName = '" + FSUtils.fixupUserInputForDB(name) + "' " +
                                        " and TeamID = " + teamid +
                                        " and StatsPlayerID = 0";
                            CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery(sql3);
                            if (crs3.next()) {
                                int playerid = crs3.getInt("PlayerID");
                                if (playerid > 0) {
                                    // update this player with the correct statsplayerid and make him active
                                    CTApplication._CT_QUICK_DB.executeUpdate("update Player set StatsPlayerID = " + statsplayerid + ",Active = 1" +
                                                    " where PlayerID = " + playerid);
                                } else {
                                    // Add player to MissingPlayers list
                                    MissingPlayer missing = new MissingPlayer(name,teamname,position,statsplayerid);
                                    missingPlayers.add(missing);
                                }
                            } else {
                                // Add player to MissingPlayers list
                                MissingPlayer missing = new MissingPlayer(name,teamname,position,statsplayerid);
                                missingPlayers.add(missing);
                            }

                            crs3.close();
                        } else {
                            // Add player to MissingPlayers list
                            MissingPlayer missing = new MissingPlayer(name,teamname,position,statsplayerid);
                            missingPlayers.add(missing);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        _Logger.log(Level.SEVERE, "Exception in FootballStats.run()", e);
                    }
                } else {

                    // grab this player's FirstName, LastName, and Team from the tempfootballstats table
                    sql2 = "select * from TempFootballStatsIDP where StatsPlayerID = " + statsplayerid;
                    CachedRowSet crs22 = CTApplication._CT_QUICK_DB.executeQuery(sql2);
                    if (crs22.next()) {
                        String name = crs22.getString("Name");
                        String teamname = crs22.getString("ProTeam");
                        String position = crs22.getString("Position");
                        try {
                            if (name != null && !name.equals("")) {
                                //StringTokenizer st = new StringTokenizer(name,",");
                                //String last = st.nextToken().trim();
                                //String first = st.nextToken().trim();
                                int teamid = 0;
                                //int teamid = tds.main.Team.getIDFromTeamName(teamname);

                                // check to see if this player exists
                                String sql3 = "select * from Player " +
                                            " where QuickStatsName = '" + FSUtils.fixupUserInputForDB(name) + "' " +
                                            " and TeamID = " + teamid +
                                            " and StatsPlayerID = 0";
                                CachedRowSet crs3 = CTApplication._CT_QUICK_DB.executeQuery(sql3);
                                if (crs3.next()) {
                                    int playerid = crs3.getInt("PlayerID");
                                    if (playerid > 0) {
                                        // update this player with the correct statsplayerid and make him active
                                        CTApplication._CT_QUICK_DB.executeUpdate("update Player set StatsPlayerID = " + statsplayerid + ",Active = 1" +
                                                        " where PlayerID = " + playerid);
                                    } else {
                                        // Add player to MissingPlayers list
                                        MissingPlayer missing = new MissingPlayer(name,teamname,position,statsplayerid);
                                        missingPlayers.add(missing);
                                    }
                                } else {
                                    // Add player to MissingPlayers list
                                    MissingPlayer missing = new MissingPlayer(name,teamname,position,statsplayerid);
                                    missingPlayers.add(missing);
                                }

                                crs3.close();
                            } else {
                                // Add player to MissingPlayers list
                                MissingPlayer missing = new MissingPlayer(name,teamname,position,statsplayerid);
                                missingPlayers.add(missing);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            _Logger.log(Level.SEVERE, "Exception in FootballStats.run()", e);
                        }
                    } else {
                        // Add player to MissingPlayers list
                        MissingPlayer missing = new MissingPlayer(statsplayerid);
                        missingPlayers.add(missing);

                    }

                    crs22.close();

                }

                crs2.close();
            }

            crs.close();

            processMissingPlayers();

        } catch (Exception e) {
            e.printStackTrace();
            _Logger.log(Level.SEVERE, "Exception in FootballStats.run()", e);
        }
    }

    private void insertIntoTotalFootballStats(int seasonid, int statsofficial) throws Exception {

        Connection con = null;
        Statement stmt = null;
        ResultSet crs = null;

        try {
            // clear out existing footballstats
            CTApplication._CT_QUICK_DB.executeUpdate("delete from FootballStats where seasonweekid = 0  and seasonid = " + seasonid);

            con = CTApplication._CT_QUICK_DB.getConn(false);

            // Import Offensive Stats
            StringBuffer sql = new StringBuffer();
            sql.append("select s.StatsPlayerID as StatsPlayerID, sum(Started) as Started,sum(Played) as Played,sum(Inactive) as Inactive " +
                    " ,sum(PassComp) as PassComp, sum(PassAtt) as PassAtt, sum(PassInt) as PassInt " +
                    " ,sum(PassYards) as PassYards, sum(PassTD) as PassTD, sum(PassTwoPt) as PassTwoPt " +
                    " ,sum(Sacked) as Sacked, sum(SackedYardsLost) as SackedYardsLost, sum(RushAtt) as RushAtt " +
                    " ,sum(RushYards) as RushYards, sum(RushTD) as RushTD, sum(RushTwoPt) as RushTwoPt " +
                    " ,sum(RecCatches) as RecCatches, sum(RecYards) as RecYards, sum(RecTD) as RecTD " +
                    " ,sum(RecTwoPt) as RecTwoPt, sum(XPA) as XPA, sum(XPM) as XPM, sum(XPBlocked) as XPBlocked " +
                    " ,sum(FGA) as FGA, sum(FGM) as FGM, sum(FGBlocked) as FGBlocked, sum(FG29Minus) as FG29Minus " +
                    " ,sum(FG30to39) as FG30to39, sum(FG40to49) as FG40to49, sum(FG50Plus) as FG50Plus " +
                    " ,sum(FumblesLost) as FumblesLost, sum(IDPTackles) as IDPTackles, sum(IDPAssists) as IDPAssists " +
                    " ,sum(IDPSacks) as IDPSacks, sum(IDPSackYardsLost) as IDPSackYardsLost, sum(IDPPassesDefensed) as IDPPassesDefensed " +
                    " ,sum(IDPQBHurries) as IDPQBHurries, sum(IDPInterceptions) as IDPInterceptions " +
                    " ,sum(IDPIntReturnYards) as IDPIntReturnYards, sum(IDPIntReturnsForTD) as IDPIntReturnsForTD " +
                    " ,sum(IDPFumblesForced) as IDPFumblesForced, sum(IDPFumbleRecoveries) as IDPFumbleRecoveries " +
                    " ,sum(IDPFumbleReturnYards) as IDPFumbleReturnYards, sum(IDPFumbleReturnsForTD) as IDPFumbleReturnsForTD " +
                    " ,sum(IDPSafeties) as IDPSafeties, sum(XtraTD) as XtraTD " +
                    " ,sum(FantasyPts) as FantasyPts, sum(SalFantasyPts) as SalFantasyPts " +
                    " from FootballStats s " +
                    " where s.SeasonID = " + seasonid +
                    " and s.SeasonWeekID > 0 " +
                    " group by s.StatsPlayerID");

            System.out.println("Total Stats : " + sql);
            stmt = con.createStatement();
            crs = stmt.executeQuery(sql.toString());

            int count = 0;
            while (crs.next()) {
                //System.out.println("Processing...");

                count++;
                int playerid = crs.getInt(PLAYERID);
                String started = crs.getString(STARTED);
                int played = crs.getInt(PLAYED);
                int inactive = crs.getInt(INACTIVE);

                int comp = crs.getInt(PASSCOMP);
                int att = crs.getInt(PASSATT);
                int inter = crs.getInt(PASSINT);
                int passyards = crs.getInt(PASSYARDS);
                int passtd = crs.getInt(PASSTD);
                int passtwopt = crs.getInt(PASS2PT);
                int sacked = crs.getInt(SACKED);
                int sackedyardslost = crs.getInt(SACKEDYARDSLOST);
                int rush = crs.getInt(RUSHATT);
                int rushyards = crs.getInt(RUSHYARDS);
                int rushtd = crs.getInt(RUSHTD);
                int rushtwopt = crs.getInt(RUSH2PT);
                int rec = crs.getInt(RECCATCHES);
                int recyards = crs.getInt(RECYARDS);
                int rectd = crs.getInt(RECTD);
                int rectwopt = crs.getInt(REC2PT);
                int xpa = crs.getInt(XPA);
                int xp = crs.getInt(XPM);
                int xpblocked = crs.getInt(XPBLOCKED);
                int fga = crs.getInt(FGA);
                int fgm = crs.getInt(FGM);
                int fgblocked = crs.getInt(FGBLOCKED);
                int fg29under = crs.getInt(FG_29);
                int fg30_39 = crs.getInt(FG_30_39);
                int fg40_49 = crs.getInt(FG_40_49);
                int fgover50 = crs.getInt(FG50_);
                int fumbleslost = crs.getInt(FUMBLESLOST);

                int tackles = crs.getInt(DEFTACKLES);
                int assists = crs.getInt(DEFASSISTS);
                int sacks = crs.getInt(DEFSACKS);
                int sackyardslost = crs.getInt(DEFSACKYARDSLOST);
                int passesdefensed = crs.getInt(DEFPASSESDEFENSED);
                int qbhurries = crs.getInt(DEFQBHURRIES);
                int interceptions = crs.getInt(DEFINTERCEPTIONS);
                int intreturnyards = crs.getInt(DEFINTRETURNYARDS);
                int intreturntd = crs.getInt(DEFINTRETURNTD);
                int fumblesforced = crs.getInt(DEFFUMBLESFORCED);
                int fumblerecoveries = crs.getInt(DEFFUMBLESRECOVERED);
                int fumblereturnyards = crs.getInt(DEFFUMBLERETURNYARDS);
                int fumblereturnsfortd = crs.getInt(DEFFUMBLERETURNTD);
                int safeties = crs.getInt(DEFSAFETIES);

                int xtratd = crs.getInt("XtraTD");
                double fantasypts = crs.getDouble("FantasyPts");
                double salfantasypts = crs.getDouble("SalFantasyPts");


                _Logger.info("PlayerID : " + playerid + ",Total Fantasy Pts : " + fantasypts);

                // Insert record into FootballStats table for TotalFantasyPts (seasonweekid = 0)
                sql = new StringBuffer();
                sql.append("insert into FootballStats " +
                        "(StatsPlayerID,SeasonWeekID,SeasonID,Started,Played," +
                        "Inactive,PassComp,PassAtt,PassYards,PassInt,PassTD,PassTwoPt,Sacked,SackedYardsLost, " +
                        "RushAtt,RushYards,RushTD,RushTwoPt,RecCatches,RecYards,RecTD,RecTwoPt,XPM,XPA,XPBlocked," +
                        "FGM,FGA,FGBlocked,FG29Minus,FG30to39,FG40to49,FG50Plus,FumblesLost,XtraTD," +
                        "IDPTackles,IDPAssists,IDPSacks,IDPSackYardsLost,IDPPassesDefensed, " +
                        "IDPQBHurries,IDPInterceptions,IDPIntReturnYards,IDPIntReturnsForTD," +
                        "IDPFumblesForced,IDPFumbleRecoveries,IDPFumbleReturnYards,IDPFumbleReturnsForTD," +
                        "IDPSafeties" +
                        ",StatsOfficial,FantasyPts,SalFantasyPts) " +
                        "values (" + playerid + ",0," +
                        seasonid + "," +
                        started + "," +
                        played + "," +
                        inactive + "," +
                        comp + "," +
                        att + "," +
                        passyards + "," +
                        inter + "," +
                        passtd + "," +
                        passtwopt + "," +
                        sacked + "," +
                        sackedyardslost + "," +
                        rush + "," +
                        rushyards + "," +
                        rushtd + "," +
                        rushtwopt + "," +
                        rec + "," +
                        recyards + "," +
                        rectd + "," +
                        rectwopt + "," +
                        xp + "," +
                        xpa + "," +
                        xpblocked + "," +
                        fgm + "," +
                        fga + "," +
                        fgblocked + "," +
                        fg29under + "," +
                        fg30_39 + "," +
                        fg40_49 + "," +
                        fgover50 + "," +
                        fumbleslost + "," +
                        xtratd + "," +
                        tackles + "," +
                        assists + "," +
                        sacks + "," +
                        sackyardslost + "," +
                        passesdefensed + "," +
                        qbhurries + "," +
                        interceptions + "," +
                        intreturnyards + "," +
                        intreturntd + "," +
                        fumblesforced + "," +
                        fumblerecoveries + "," +
                        fumblereturnyards + "," +
                        fumblereturnsfortd + "," +
                        safeties + "," +
                        statsofficial + "," +
                        fantasypts + "," +
                        salfantasypts + ")");
                _Logger.info(sql.toString());
                CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
            }
            con.commit();

        } catch (Exception exception) {
        } finally {
            if (crs!=null) {
                crs.close();
            }
            if (stmt!=null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    private void tempInsertIntoFootballStats(int seasonid,int seasonweekid) throws Exception {

        // clear out existing footballstats
        CTApplication._CT_QUICK_DB.executeUpdate("delete from FootballStats where seasonweekid = " + seasonweekid);

        // Import Offensive Stats
        StringBuilder sql = new StringBuilder();
        sql.append("select * from Player where IsActive = 1");

        CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
        int count = 0;
        while (crs.next()) {

            count++;
            int playerid = crs.getInt(PLAYERID);
            double fantasypts = 0;

            _Logger.info("PlayerID : " + playerid + ",Fantasy Pts : " + fantasypts);

            // Insert record into FootballStats table
            sql = new StringBuilder();
            sql.append("insert into FootballStats " +
                        "(StatsPlayerID,SeasonWeekID,SeasonID,FantasyPts,SalFantasyPts) " +
                        "values (" + playerid + "," +
                        seasonweekid + "," +
                        seasonid + "," +
                        fantasypts + "," +
                        fantasypts + ")");
            _Logger.info(sql.toString());
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        }
        crs.close();

    }

    public static void findStatsPlayerIds() throws Exception
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from TempFootballStats ");

        Connection con = CTApplication._CT_QUICK_DB.getConn(false);
        Statement stmt = con.createStatement();
        ResultSet crs = stmt.executeQuery(sql.toString());

        int count = 0;
        while (crs.next()) {
            //System.out.println("Processing...");

            count++;
            String name = crs.getString(PLAYERNAME);
            name = name.substring(1);
            name = name.replace("'", "''");
            String proTeam = crs.getString(PROTEAM);
            proTeam = proTeam.trim();
            int tempId = crs.getInt("TempID");

            sql = new StringBuilder();
            sql.append(" SELECT * FROM Player p ");
            sql.append(" INNER JOIN Team t ON t.TeamID = p.TeamID");
            sql.append(" WHERE concat(firstName,' ',lastName) = '").append(name).append("'");
            sql.append(" AND t.Abbreviation = '").append(proTeam).append("'");

//            System.out.println("SQL : " + sql.toString());
            Statement stmt2 = con.createStatement();
            ResultSet crs2 = stmt2.executeQuery(sql.toString());

            if (crs2.next())
            {
                int playerid = crs2.getInt("playerId");
                int statsPlayerId = crs2.getInt("statsPlayerId");
//                System.out.println("[" + name + "] Stats PlayerID : " + statsPlayerId);

                sql = new StringBuilder();
                sql.append(" UPDATE TempFootballStats set StatsPlayerID = ").append(statsPlayerId);
                sql.append(" WHERE TempID = ").append(tempId);

                System.out.println(sql.toString() + ";");
                CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
//                System.out.println("=================================");

            }
        }
    }

    static class MissingPlayer {
        String fullName = "";
        String teamName = "";
        String position = "";
        int statsplayerid;

        MissingPlayer(String name,String team,String position, int id) {
            fullName = name;
            teamName = team;
            statsplayerid = id;
        }

        MissingPlayer(int id) {
            statsplayerid = id;
        }
        public String getFullName() {    return fullName;    }
        public String getTeamName() {      return teamName;    }
        public String getPosition() {       return position;    }
        public int getStatsplayerid() {      return statsplayerid;    }
    }

}
