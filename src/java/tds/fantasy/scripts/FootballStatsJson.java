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

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class FootballStatsJson implements Harnessable {

    public static final String STATS_DIR_PROP = "tds.Football.stats.directory";
    //public static Properties settings = new Properties();
    public static String _STATS_DIR = Application._GLOBAL_SETTINGS.getProperty(STATS_DIR_PROP);
//    private static final int _FSSeasonID = 20;
//    private static final int _SeasonID = 8;
    public static final String _Year = "2021";
    private static final int _NumColumns = 60;

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
    public static final String FGLENGTHS = "FGLengths";
    public static final String PUNTRETURNTDS = "PuntReturnTDs";
    public static final String KICKRETURNTDS = "KickReturnTDs";

    private static final String[] TEMPPLAYERSTATS = new String[_NumColumns];

//        PLAYERID, PROTEAM, NOTUSED, PROTEAMID, POSITION, NOTUSED, NOTUSED,
//        NOTUSED, NOTUSED,
//
//        INJURYSTATUS,STARTED,PLAYED,INACTIVE,PASSCOMP,PASSATT,PASSYARDS,
//        PASSINT,PASSTD,PASS2PT,SACKED,SACKEDYARDSLOST,RUSHATT,RUSHYARDS,RUSHTD,RUSH2PT,
//        RECTARGETS,RECCATCHES,RECYARDS,RECTD,REC2PT,XPM,XPA,XPBLOCKED,FGM,FGA,FGBLOCKED,FG_29,FG_30_39,FG_40_49,FG50_,
//        FUMBLESLOST, TDDISTANCES };
//
    public FootballStatsJson() {
        System.out.println("Instantiating Logger...");
        _Logger = Logger.global;
        _Logger.setLevel(Level.ALL);

        TEMPPLAYERSTATS[0] = PLAYERNAME;
        TEMPPLAYERSTATS[1] = PLAYERID;
        TEMPPLAYERSTATS[3] = PROTEAM;
        TEMPPLAYERSTATS[4] = POSITION;
        TEMPPLAYERSTATS[10] = FUMBLESLOST;
        TEMPPLAYERSTATS[15] = FGA;
        TEMPPLAYERSTATS[16] = FGM;
        TEMPPLAYERSTATS[19] = XPA;
        TEMPPLAYERSTATS[21] = XPM;
        TEMPPLAYERSTATS[28] = KICKRETURNTDS;
        TEMPPLAYERSTATS[29] = PASSATT;
        TEMPPLAYERSTATS[30] = PASSCOMP;
        TEMPPLAYERSTATS[31] = PASSINT;
        TEMPPLAYERSTATS[32] = PASSTD;
        TEMPPLAYERSTATS[34] = PASS2PT;
        TEMPPLAYERSTATS[35] = PASSYARDS;
        TEMPPLAYERSTATS[45] = PUNTRETURNTDS;
        TEMPPLAYERSTATS[48] = RECCATCHES;
        TEMPPLAYERSTATS[49] = RECTD;
        TEMPPLAYERSTATS[51] = REC2PT;
        TEMPPLAYERSTATS[52] = RECYARDS;
        TEMPPLAYERSTATS[53] = RUSHATT;
        TEMPPLAYERSTATS[56] = RUSHTD;
        TEMPPLAYERSTATS[58] = RUSH2PT;
        TEMPPLAYERSTATS[59] = RUSHYARDS;

// 0    name,id,home,team,pos,
// 5    defense_ast,defense_ffum,defense_int,defense_sk,defense_tkl,
// 10   fumbles_lost,fumbles_rcv,fumbles_tot,fumbles_trcv,fumbles_yds,
// 15   kicking_fga,kicking_fgm,kicking_fgyds,kicking_totpfg,kicking_xpa,
// 20   kicking_xpb,kicking_xpmade,kicking_xpmissed,kicking_xptot,kickret_avg,
// 25   kickret_lng,kickret_lngtd,kickret_ret,kickret_tds,passing_att,
// 30   passing_cmp,passing_ints,passing_tds,passing_twopta,passing_twoptm,
// 35   passing_yds,punting_avg,punting_i20,punting_lng,punting_pts,
// 40   punting_yds,puntret_avg,puntret_lng,puntret_lngtd,puntret_ret,
// 45   puntret_tds,receiving_lng,receiving_lngtd,receiving_rec,receiving_tds,
// 50   receiving_twopta,receiving_twoptm,receiving_yds,rushing_att,rushing_lng,
// 55   rushing_lngtd,rushing_tds,rushing_twopta,rushing_twoptm,rushing_yds
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
            FootballStatsJson stats = new FootballStatsJson();
//            FootballStats.findStatsPlayerIds();

//            runPythonScripts();
//            return;
            int fsseasonweekid = 0;
            if (args != null && args.length > 0)
            {
                try {
                    fsseasonweekid = Integer.parseInt(args[0]);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            stats.run(fsseasonweekid);
//            stats.insertIntoTotalFootballStats(15, 1);
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
        run(0);
    }

    public void run(int fsseasonweekid) {

        int statsofficial = 1;
        System.out.println("Starting Football Stats...");

        FSGame fsGame = new FSGame(1);
        int currentFSSeasonID = fsGame.getCurrentFSSeasonID();
        FSSeason currentFSSeason = new FSSeason(currentFSSeasonID);
        FSSeasonWeek fsseasonweek = currentFSSeason.getCurrentFSSeasonWeek();
        if (fsseasonweekid > 0)
        {
            fsseasonweek = new FSSeasonWeek(fsseasonweekid);
        }

        int currentSeasonID = currentFSSeason.getSeasonID();

        System.out.println("Running Stats for SeasonId : " + currentSeasonID + " and FSSeasonWeekID : " + fsseasonweek.getFSSeasonWeekID());

        try {

            int seasonweeknum = fsseasonweek.getFSSeasonWeekNo();

            System.out.println("Running stats for WeekNo " + seasonweeknum + " and SeasonWeekID : " + fsseasonweek.getSeasonWeekID());

            // get stats for the current week
            String week = seasonweeknum < 10 ? "0"+seasonweeknum : ""+seasonweeknum;
            String extension = "csv";
            String statsfile = "week"+week+"."+extension;
            String kickersfile = "week"+week+"kickers."+extension;

            boolean statsFileExists = FSUtils.fileExists(_STATS_DIR,statsfile,_Logger);
//            boolean statsFileExists = false;
            if (statsFileExists) {
                System.out.println("Stats Files found - Starting stats...");
                insertIntoTempFootballStatsCommaDelimited(_STATS_DIR + statsfile);
                insertKickersIntoTempFootballStats(_STATS_DIR + kickersfile);
                System.out.println("Stats inserted into TempFootballStats...");
            } else {
                System.out.println("Stats Files don't exist... going with what is already in tempfootballstats.");
            }

            insertIntoFootballStats(currentSeasonID,fsseasonweek.getSeasonWeekID(),statsofficial);
            System.out.println("Stats inserted into FootballStats...");
            insertIntoTotalFootballStats(currentSeasonID, statsofficial);
            System.out.println("Stats inserted into TotalFootballStats...");
//                checkNonMatchingIDs(currentSeasonID);
            System.out.println("Done!");
            _ResultCode = ResultCode.RC_SUCCESS;

        } catch (Exception e) {
            _ResultCode = ResultCode.RC_ERROR;
            System.out.println("Exception in FootballStats.run()" + e.getMessage());
        }
    }

    private void insertIntoTempFootballStatsCommaDelimited(String filename) throws Exception {

        _Logger.log(Level.INFO, "Grabbing stats from file : {0}", filename);
        try {
            // clear out any existing rows.
            CTApplication._CT_QUICK_DB.executeUpdate("delete from TempFootballStats");

            FileReader fileReader = new FileReader(filename);
            BufferedReader textReader = new BufferedReader(fileReader);
            String line;
            int linenumber = 0;
            while ((line = textReader.readLine()) != null)
            {
                linenumber++;
                if (linenumber == 1)
                {
                    continue;
                }
                List statscolumns = Arrays.asList(TEMPPLAYERSTATS);

                try {
                    if (line != null && line.length() > 20) {
                        _Logger.log(Level.INFO, "line = {0}", line);
//                        StringTokenizer stLine = new StringTokenizer(line, ",");
                        String[] player = new String[_NumColumns];

                        int x = 0;
//                        while (stLine.hasMoreTokens()) {
                        for (String value: line.split(",")){
//                            String value = stLine.nextToken();
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
                            StringBuilder sql = new StringBuilder();
                            sql.append("insert into TempFootballStats (");
                            for (x = 0;x < statscolumns.size();x++) {
                                String colname = TEMPPLAYERSTATS[x];
                                if (!AuUtil.isEmpty(colname))
                                {
                                    // don't add this column if the value is empty
                                    String value = player[x];
                                    if (!AuUtil.isEmpty(value))
                                    {

                                        if (x > 0) {
                                            sql.append(",");
                                        }
                                        sql.append(colname);
                                    }
                                }
                            }
                            sql.append(") values (");
                            for (x = 0;x < player.length; x++) {
                                String colname = TEMPPLAYERSTATS[x];
                                if (!AuUtil.isEmpty(colname))
                                {
                                    String value = player[x];
                                    if (AuUtil.isEmpty(value))
                                    {
//                                        sql.append(value);
                                    } else
                                    {
                                        if (x > 0) {
                                            sql.append(",");
                                        }
                                        sql.append("'").append(value).append("'");
                                    }

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

    private void insertKickersIntoTempFootballStats(String filename) throws Exception {

        _Logger.log(Level.INFO, "Grabbing kicker stats from file : {0}", filename);
        try {

            FileReader fileReader = new FileReader(filename);
            BufferedReader textReader = new BufferedReader(fileReader);
            String line;
            int linenumber = 0;
            Map<String, Collection<Integer>> players = new HashMap<String, Collection<Integer>>();
            while ((line = textReader.readLine()) != null)
            {
                linenumber++;
//                if (linenumber == 1)
//                {
//                    continue;
//                }

                try {
                    if (line != null) {
                        _Logger.log(Level.INFO, "line = {0}", line);
//                        StringTokenizer stLine = new StringTokenizer(line, ",");

                        int x = 0;
                        String[] playerLine = line.split(",");
                        if (playerLine.length == 2)
                        {
                            String playerId = playerLine[0];
                            Integer fgLength = Integer.parseInt(playerLine[1]);
                            Collection<Integer> currentValues = players.get(playerId);
                            if (currentValues == null)
                            {
                                currentValues = new ArrayList<Integer>();
                                players.put(playerId,currentValues);
                            }
                            currentValues.add(fgLength);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    _Logger.log(Level.SEVERE, "Exception in FootballStats.run()", e);
                }

            } // end while

            // Loop through each player and set their fg length
            Iterator keyValuePairs = players.entrySet().iterator();
            for (int i = 0; i < players.size(); i++) {
                Map.Entry entry = (Map.Entry) keyValuePairs.next();
                Object playerId = entry.getKey();
                Collection<Integer> fgLengths = (Collection<Integer>)entry.getValue();

                String lengths = fgLengths.toString();
                System.out.println("Player Id : " + playerId + ", FGLengths : " + lengths);

                // update an existing record in TempFootballStats
                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE TempFootballStats ");
                sql.append("SET FGLengths = '").append(lengths).append("' ");
                sql.append("WHERE StatsPlayerID = '").append(playerId).append("' ");

                _Logger.info(sql.toString());
                CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

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
            String playerid = crs.getString(PLAYERID);
            if (playerid.equals("00-0032211")) {
                System.out.println("Hey");
            }
            String team = crs.getString(PROTEAM);
            int teamid = crs.getInt(PROTEAMID);
            String position = crs.getString(POSITION);
            if (position == null)
            {
                position = "";
            }
            String injurystatus = crs.getString(INJURYSTATUS);
            if (injurystatus == null)
            {
                injurystatus = "";
            }
            String startedTemp = crs.getString(STARTED);
            int started = startedTemp == null || startedTemp.equals("") ? 0 : Integer.parseInt(startedTemp);
//            int played = crs.getInt(PLAYED);
            int played = 1;
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
            int puntrettd = crs.getInt(PUNTRETURNTDS);
            int kickrettd = crs.getInt(KICKRETURNTDS);
            String tddistances = crs.getString(TDDISTANCES);
            String fgLengthsString = crs.getString(FGLENGTHS);

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

            xtratd += kickrettd + puntrettd;

            // Figure the field goal lengths
            if (!AuUtil.isEmpty(fgLengthsString) && !"0".equals(fgLengthsString))
            {
                fgLengthsString = fgLengthsString.replace("[", "");
                fgLengthsString = fgLengthsString.replace("]", "");

                String[] fgArray = fgLengthsString.split(",");
                if (fgArray.length > 0)
                {
                    for (String len : fgArray)
                    {
                        len = len.trim();
                        int newlength = Integer.parseInt(len);
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

            double fantasypts = 6*((double)rushtd+(double)passtd+(double)rectd+(double)xtratd) + ((double)passyards/25.0) +
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
            _Logger.log(Level.INFO, "PlayerID : {0},Fantasy Pts : {1}", new Object[]{playerid, fantasypts});

            // Insert record into FootballStats table
            sql = new StringBuilder();
            sql.append("insert into FootballStats " +
                        "(StatsPlayerID,SeasonWeekID,SeasonID,TeamID,Position,InjuryStatus,Started,Played," +
                        "Inactive,PassComp,PassAtt,PassYards,PassInt,PassTD,PassTwoPt,Sacked,SackedYardsLost, " +
                        "RushAtt,RushYards,RushTD,RushTwoPt,RecCatches,RecYards,RecTD,RecTwoPt,XPM,XPA,XPBlocked," +
                        "FGM,FGA,FGBlocked,FG29Minus,FG30to39,FG40to49,FG50Plus,FumblesLost,XtraTD," +
                        "TDDistances,StatsOfficial,FantasyPts,SalFantasyPts) " +
                        "values ('" + playerid + "'," +
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

    }

    private void processMissingPlayers() {
        for (MissingPlayer player : missingPlayers) {
            _Logger.log(Level.INFO, "Missing Player : {0} ; Team : {1} ; Position : {2}; StatsID : {3}", new Object[]{player.getFullName(), player.getTeamName(), player.getPosition(), player.getStatsplayerid()});
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
                String playerid = crs.getString(PLAYERID);
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

                int xtratd = crs.getInt("XtraTD");
                double fantasypts = crs.getDouble("FantasyPts");
                double salfantasypts = crs.getDouble("SalFantasyPts");


                _Logger.log(Level.INFO, "PlayerID : {0},Total Fantasy Pts : {1}", new Object[]{playerid, fantasypts});

                // Insert record into FootballStats table for TotalFantasyPts (seasonweekid = 0)
                sql = new StringBuffer();
                sql.append("insert into FootballStats " +
                        "(StatsPlayerID,SeasonWeekID,SeasonID,Started,Played," +
                        "Inactive,PassComp,PassAtt,PassYards,PassInt,PassTD,PassTwoPt,Sacked,SackedYardsLost, " +
                        "RushAtt,RushYards,RushTD,RushTwoPt,RecCatches,RecYards,RecTD,RecTwoPt,XPM,XPA,XPBlocked," +
                        "FGM,FGA,FGBlocked,FG29Minus,FG30to39,FG40to49,FG50Plus,FumblesLost,XtraTD," +
                        "StatsOfficial,FantasyPts,SalFantasyPts) " +
                        "values ('" + playerid + "',0," +
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
                        statsofficial + "," +
                        fantasypts + "," +
                        salfantasypts + ")");
                _Logger.info(sql.toString());
                CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
            }
            con.commit();

        } catch (Exception exception) {
            exception.printStackTrace();
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

            _Logger.log(Level.INFO, "PlayerID : {0},Fantasy Pts : {1}", new Object[]{playerid, fantasypts});

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
