package tds.fantasy.scripts;

import tds.main.bo.CTApplication;
import tds.main.bo.FootballStats;
import tds.main.bo.Player;
import tds.main.bo.Season;
import tds.main.bo.football.stats.MySportsFeeds.Stats_PlayerObj;
import tds.main.bo.football.stats.MySportsFeeds.Stats_PlayerStats;

import java.text.DecimalFormat;

public class FootballStats_ProFootballAPI {

    private static final String APIKEY = "d237939b-a86d-4081-8920-3d75d7";
    private static final String DOMAIN = "https://profootballapi.com";
//    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static void insertIntoFootballStatsTable_Offense(Stats_PlayerObj playerObj, Player player, int seasonweekid) throws Exception {
        Stats_PlayerStats playerStats = playerObj.getPlayerStats().get(0);

        double fantasypts = calculateOffensivePlayerFantasyPoints(playerStats);
        double salfantasypts = calculateOffensivePlayerFantasyPoints_SalaryCap(playerStats, fantasypts);

        System.out.println("PlayerID : " + player.getPlayerID() + ",Fantasy Pts : " + fantasypts);
        System.out.println("PlayerID : " + player.getPlayerID() + ",Sal Fantasy Pts : " + salfantasypts);

        int played = playerStats.getSnapCounts().getOffenseSnaps() > 0 ? 1 : 0;
        int passcomp = 0;
        int passatt = 0;
        int passyards = 0;
        int passint = 0;
        int passtd = 0;
        int passsack = 0;
        int passsackyards = 0;
        if (playerStats.getPassing() != null) {
            passcomp = playerStats.getPassing().getPassCompletions();
            passatt = playerStats.getPassing().getPassAttempts();
            passyards = playerStats.getPassing().getPassYards();
            passint = playerStats.getPassing().getPassInt();
            passtd = playerStats.getPassing().getPassTD();
            passsack = playerStats.getPassing().getPassSacks();
            passsackyards = playerStats.getPassing().getPassSackY();
        }
        int rushatt = 0;
        int rushyards = 0;
        int rushtd = 0;
        if (playerStats.getRushing() != null) {
            rushatt = playerStats.getRushing().getRushAttempts();
            rushyards = playerStats.getRushing().getRushYards();
            rushtd = playerStats.getRushing().getRushTD();
        }
        int rec = 0;
        int recyards = 0;
        int rectd = 0;
        if (playerStats.getReceiving() != null) {
            rec = playerStats.getReceiving().getReceptions();
            recyards = playerStats.getReceiving().getRecYards();
            rectd = playerStats.getReceiving().getRecTD();
        }
        int twoPointsPass = 0;
        int twoPointsRush = 0;
        int twoPointsRec = 0;
        if (playerStats.getTwoPointAttempts() != null) {
            twoPointsPass = playerStats.getTwoPointAttempts().getTwoPtPassMade();
            twoPointsRush = playerStats.getTwoPointAttempts().getTwoPtRushMade();
            twoPointsRec = playerStats.getTwoPointAttempts().getTwoPtPassRec();
        }
        int xpm = 0;
        int xpa = 0;
        int xpb = 0;
        if (playerStats.getExtraPointAttempts() != null) {
            xpm = playerStats.getExtraPointAttempts().getXpMade();
            xpa = playerStats.getExtraPointAttempts().getXpAtt();
            xpb = playerStats.getExtraPointAttempts().getXpBlk();
        }
        int fgm = 0;
        int fga = 0;
        int fgb = 0;
        int fg029 = 0;
        int fg3039 = 0;
        int fg4049 = 0;
        int fg50 = 0;
        if (playerStats.getFieldGoals() != null) {
            fgm = playerStats.getFieldGoals().getFgMade();
            fga = playerStats.getFieldGoals().getFgAtt();
            fgb = playerStats.getFieldGoals().getFgBlk();
            fg029 = playerStats.getFieldGoals().getFgMade1_19() + playerStats.getFieldGoals().getFgMade20_29();
            fg3039 = playerStats.getFieldGoals().getFgMade30_39();
            fg4049 = playerStats.getFieldGoals().getFgMade40_49();
            fg50 = playerStats.getFieldGoals().getFgMade50Plus();

        }

        // Insert record into FootballStats table
        StringBuilder sql = new StringBuilder();
        sql.append("insert into FootballStats " +
                "(StatsPlayerID,SeasonWeekID,SeasonID,TeamID,Position,Started,Played," +
                "PassComp,PassAtt,PassYards,PassInt,PassTD,PassTwoPt,Sacked,SackedYardsLost, " +
                "RushAtt,RushYards,RushTD,RushTwoPt,RecCatches,RecYards,RecTD,RecTwoPt,XPM,XPA,XPBlocked," +
                "FGM,FGA,FGBlocked,FG29Minus,FG30to39,FG40to49,FG50Plus,FumblesLost,XtraTD," +
                "FantasyPts,SalFantasyPts,PlayerID) " +
                "values (" + playerObj.getPlayer().getId() + "," +
                seasonweekid + "," +
                Season._CurrentSeasonID + "," +
                player.getTeamID() + ",'" +
                playerObj.getPlayer().getPosition() + "'," +
                playerStats.getMiscellaneous().getGamesStarted() + "," +
                played + "," +
                passcomp + "," +
                passatt + "," +
                passyards + "," +
                passint + "," +
                passtd + "," +
                twoPointsPass + "," +
                passsack + "," +
                passsackyards + "," +
                rushatt + "," +
                rushyards + "," +
                rushtd + "," +
                twoPointsRush + "," +
                rec + "," +
                recyards + "," +
                rectd + "," +
                twoPointsRec + "," +
                xpm + "," +
                xpa + "," +
                xpb + "," +
                fgm + "," +
                fga + "," +
                fgb + "," +
                fg029 + "," +
                fg3039 + "," +
                fg4049 + "," +
                fg50 + "," +
                playerStats.getFumbles().getFumLost() + "," +
                (playerStats.getKickoffReturns().getKrTD() + playerStats.getPuntReturns().getPrTD() + playerStats.getFumbles().getFumTD()) + "," +
                fantasypts + "," +
                salfantasypts + "," +
                player.getPlayerID() + ")");
        System.out.println(sql);
        CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

    }

    public static void saveFootballStatsTable_Offense(Stats_PlayerObj playerObj, Player player, int seasonweekid) throws Exception {

        if (playerObj.getPlayerStats() == null || playerObj.getPlayerStats().size() < 1) {
            return;
        }

        // check to see if a record already exists.  If so, then delete it.
        FootballStats existing = FootballStats.getRecordByPlayerIDSeasonWeekID(player.getPlayerID(), seasonweekid);
        if (existing != null) {
            // delete it first
            existing.Delete();
        }
        insertIntoFootballStatsTable_Offense(playerObj, player, seasonweekid);

    }

    private static double calculateOffensivePlayerFantasyPoints_SalaryCap(Stats_PlayerStats playerStats, double fantasypts) {
        double intfantasypts = 0.00;
        if (playerStats.getPassing() != null) {
            for (int x = 1; x <= playerStats.getPassing().getPassInt(); x++) {
                intfantasypts += x;
            }
        }

        double fumfantasypts = 0.00;
        if (playerStats.getFumbles() != null) {
            for (int x = 1; x <= playerStats.getFumbles().getFumbles(); x++) {
                fumfantasypts += x;
            }
        }

        double salfantasypts = fantasypts - intfantasypts - fumfantasypts;

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        salfantasypts = Double.valueOf(twoDForm.format(salfantasypts));

        return salfantasypts;
    }

    private static double calculateOffensivePlayerFantasyPoints(Stats_PlayerStats playerStats) {
        // calculate Player Fantasy Points
        double fantasypts = 0.00;
        int xtratd = playerStats.getKickoffReturns().getKrTD() + playerStats.getPuntReturns().getPrTD() + playerStats.getFumbles().getFumTD();
        if (playerStats.getPassing() != null) {
            fantasypts += 6*((double)playerStats.getPassing().getPassTD());
            fantasypts += ((double)playerStats.getPassing().getPassYards()/25.0);
        }
        if (playerStats.getRushing() != null) {
            fantasypts += 6*((double)playerStats.getRushing().getRushTD());
            fantasypts += ((double)playerStats.getRushing().getRushYards()/10.0);
        }
        if (playerStats.getReceiving() != null) {
            fantasypts += 6*((double)playerStats.getReceiving().getRecTD());
            fantasypts += ((double)playerStats.getReceiving().getRecYards()/10.0);
            fantasypts += ((double)playerStats.getReceiving().getReceptions()/2);
        }
        fantasypts += 6*((double)xtratd);
        if (playerStats.getTwoPointAttempts() != null)
                fantasypts += 2*(double)playerStats.getTwoPointAttempts().getTwoPtMade();
        if (playerStats.getFieldGoals() != null) {
            fantasypts += ((double)playerStats.getFieldGoals().getFgMade1_19()*3)
                    + ((double)playerStats.getFieldGoals().getFgMade20_29()*3)
                    + ((double)playerStats.getFieldGoals().getFgMade30_39()*3)
                    + ((double)playerStats.getFieldGoals().getFgMade40_49()*4)
                    + ((double)playerStats.getFieldGoals().getFgMade50Plus()*5);
        }
        if (playerStats.getExtraPointAttempts() != null) {
            fantasypts += playerStats.getExtraPointAttempts().getXpMade();
        }

        DecimalFormat twoDForm = new DecimalFormat("#.##");

        fantasypts = Double.valueOf(twoDForm.format(fantasypts));
        return fantasypts;
    }

    private static void insertIntoFootballStats_Defense(Stats_PlayerObj playerObj, Player player, int seasonweekid) throws Exception {


//        // Import IDP Stats
//        sql = new StringBuilder();
//        sql.append("select * from TempFootballStatsIDP");
//
//        crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
//        count = 0;
//        while (crs.next()) {
//
//            count++;
//            int playerid = crs.getInt(PLAYERID);
//            String team = crs.getString(PROTEAM);
//            int teamid = crs.getInt(PROTEAMID);
//            String position = crs.getString(POSITION);
//            String injurystatus = crs.getString(INJURYSTATUS);
//            String started = crs.getString(STARTED);
//            int played = crs.getInt(PLAYED);
//            int inactive = crs.getInt(INACTIVE);
//
//            int tackles = crs.getInt(DEFTACKLES);
//            int assists = crs.getInt(DEFASSISTS);
//            int sacks = crs.getInt(DEFSACKS);
//            int sackyardslost = crs.getInt(DEFSACKYARDSLOST);
//            int passesdefensed = crs.getInt(DEFPASSESDEFENSED);
//            int qbhurries = crs.getInt(DEFQBHURRIES);
//            int interceptions = crs.getInt(DEFINTERCEPTIONS);
//            int intreturnyards = crs.getInt(DEFINTRETURNYARDS);
//            int intreturntd = crs.getInt(DEFINTRETURNTD);
//            int fumblesforced = crs.getInt(DEFFUMBLESFORCED);
//            int fumblerecoveries = crs.getInt(DEFFUMBLESRECOVERED);
//            int fumblereturnyards = crs.getInt(DEFFUMBLERETURNYARDS);
//            int fumblereturnsfortd = crs.getInt(DEFFUMBLERETURNTD);
//            int safeties = crs.getInt(DEFSAFETIES);
//            String tddistances = crs.getString(DEFTDDISTANCES);
//
//            int xtratd = 0;
//
//            // Figure the number of xtra tds
//            if (tddistances != null && !tddistances.equals("")) {
//                StringBuilder st = new StringBuilder(tddistances);
//                for (int x=0;x<st.length();x++) {
//                    char ch = st.charAt(x);
//
//                    if (ch == 'K' || ch == 'P' || ch == 'B') {
//                        xtratd++;
//                    }
//                }
//            }
//
//            // check to see if any of the idp guys already have a record from the offensive stats
//            String tempsql = "select * from FootballStats where SeasonWeekID = " + seasonweekid +
//                    " and StatsPlayerID = " + playerid;
//            CachedRowSet tempcrs = CTApplication._CT_QUICK_DB.executeQuery(tempsql);
//            if (tempcrs.next()) {
//                // update existing record
//
//                double fantasypts = tempcrs.getDouble("FantasyPts");
//                double salfantasypts = tempcrs.getDouble("SalFantasyPts");
//
//                double deffantpts = 6*((double)intreturntd + (double)fumblereturnsfortd + (double)xtratd) +
//                        4*((double)safeties + (double)interceptions + (double)fumblerecoveries) +
//                        3*((double)sacks) +
//                        2*((double)fumblesforced) +
//                        (double)tackles + (double)passesdefensed +
//                        .5*((double)assists);
//
//                // for Tenman & Keeper, we don't add in the Defensive Fantasy Points.
//                //fantasypts += deffantpts;
//                salfantasypts += deffantpts;
//
//                sql = new StringBuilder();
//                sql.append(" update FootballStats " +
//                        " set IDPTackles = " + tackles +
//                        " ,IDPAssists = " + assists +
//                        " ,IDPSacks = " + sacks +
//                        " ,IDPSackYardsLost = " + sackyardslost +
//                        " ,IDPPassesDefensed = " + passesdefensed +
//                        " ,IDPQBHurries = " + qbhurries +
//                        " ,IDPInterceptions = " + interceptions +
//                        " ,IDPIntReturnYards = " + intreturnyards +
//                        " ,IDPIntReturnsForTD = " + intreturntd +
//                        " ,IDPFumblesForced = " + fumblesforced +
//                        " ,IDPFumbleRecoveries = " + fumblerecoveries +
//                        " ,IDPFumbleReturnYards = " + fumblereturnyards +
//                        " ,IDPFumbleReturnsForTD = " + fumblereturnsfortd +
//                        " ,IDPSafeties = " + safeties +
//                        " ,IDPTDDistances = '" + tddistances +
//                        " ',FantasyPts = " + fantasypts +
//                        " ,SalFantasyPts = " + salfantasypts +
//                        " where StatsPlayerID = " + playerid +
//                        " and SeasonWeekID = " + seasonweekid);
//
//                _Logger.info(sql.toString());
//                CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
//
//            } else {
//                // calculate Player Fantasy Points
//                double fantasypts = 0.0;
//                fantasypts = 6*((double)intreturntd + (double)fumblereturnsfortd + (double)xtratd) +
//                        4*((double)safeties + (double)interceptions + (double)fumblerecoveries) +
//                        3*((double)sacks) +
//                        2*((double)fumblesforced) +
//                        (double)tackles + (double)passesdefensed +
//                        .5*((double)assists);
//
//                // Insert record into FootballStats table
//                sql = new StringBuilder();
//                sql.append("insert into FootballStats " +
//                        "(StatsPlayerID,SeasonWeekID,SeasonID,TeamID,Position,InjuryStatus,Started,Played," +
//                        "Inactive,IDPTackles,IDPAssists,IDPSacks,IDPSackYardsLost,IDPPassesDefensed, " +
//                        "IDPQBHurries,IDPInterceptions,IDPIntReturnYards,IDPIntReturnsForTD," +
//                        "IDPFumblesForced,IDPFumbleRecoveries,IDPFumbleReturnYards,IDPFumbleReturnsForTD," +
//                        "IDPSafeties,IDPTDDistances,StatsOfficial,FantasyPts,SalFantasyPts) " +
//                        "values (" + playerid + "," +
//                        seasonweekid + "," +
//                        seasonid + "," +
//                        teamid + ",'" +
//                        position + "','" +
//                        injurystatus + "','" +
//                        started + "'," +
//                        played + "," +
//                        inactive + "," +
//                        tackles + "," +
//                        assists + "," +
//                        sacks + "," +
//                        sackyardslost + "," +
//                        passesdefensed + "," +
//                        qbhurries + "," +
//                        interceptions + "," +
//                        intreturnyards + "," +
//                        intreturntd + "," +
//                        fumblesforced + "," +
//                        fumblerecoveries + "," +
//                        fumblereturnyards + "," +
//                        fumblereturnsfortd + "," +
//                        safeties + ",'" +
//                        tddistances + "'," +
//                        statsofficial + "," +
//                        fantasypts + "," +
//                        fantasypts + ")");
//
//                _Logger.info(sql.toString());
//                CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
//
//            }
//
//            tempcrs.close();
//
//        }
//
//        crs.close();

    }

    public static void main(String[] args) {
        System.out.println("Starting...");
        try {
//            getSchedule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
