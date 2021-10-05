package tds.fantasy.scripts;

import tds.main.bo.CTApplication;
import tds.main.bo.FootballStats;
import tds.main.bo.Player;
import tds.main.bo.Season;
import tds.main.bo.football.stats.MySportsFeeds.Stats_PlayerObj;
import tds.main.bo.football.stats.MySportsFeeds.Stats_PlayerStats;

import java.text.DecimalFormat;

public class FootballStats_ProFootballAPI {

    private static void insertIntoFootballStatsTable_Defense(Stats_PlayerObj playerObj, Player player, int seasonweekid) throws Exception {
        Stats_PlayerStats playerStats = playerObj.getPlayerStats().get(0);

        double fantasypts = calculateDefensivePlayerFantasyPoints(playerStats);
        double salfantasypts = fantasypts;

        System.out.println("PlayerID : " + player.getPlayerID() + ",Fantasy Pts : " + fantasypts);
        System.out.println("PlayerID : " + player.getPlayerID() + ",Sal Fantasy Pts : " + salfantasypts);

//        int played = playerStats.getSnapCounts().getDefenseSnaps() > 0 ? 1 : 0;
        int played = 1;
        int tacklesSolo = 0;
        int tacklesAsst = 0;
        double sacks = 0.0;
        int sackYards = 0;
        if (playerStats.getTackles() != null) {
            tacklesSolo = playerStats.getTackles().getTackleSolo();
            tacklesAsst = playerStats.getTackles().getTackleAst();
            sacks = playerStats.getTackles().getSacks();
            sackYards = playerStats.getTackles().getSackYds();
        }
        int interceptions = 0;
        int interceptionTD = 0;
        int intYards = 0;
        int passesDefensed = 0;
        int safeties = 0;
        if (playerStats.getInterceptions() != null) {
            interceptions = playerStats.getInterceptions().getInterceptions();
            interceptionTD = playerStats.getInterceptions().getIntTD();
            intYards = playerStats.getInterceptions().getIntYds();
            passesDefensed = playerStats.getInterceptions().getPassesDefended();
            safeties = playerStats.getInterceptions().getSafeties();
        }
        int fumblesForced = 0;
        int fumblesRec = 0;
        int fumbleTD = 0;
        int fumbleYards = 0;
        if (playerStats.getFumbles() != null) {
            fumblesForced = playerStats.getFumbles().getFumForced();
            fumblesRec = playerStats.getFumbles().getFumOppRec();
            fumbleTD = playerStats.getFumbles().getFumTD();
            fumbleYards = playerStats.getFumbles().getFumRecYds();
        }

        int rushtd = 0;
        if (playerStats.getRushing() != null) {
            rushtd = playerStats.getRushing().getRushTD();
        }
        int rectd = 0;
        if (playerStats.getReceiving() != null) {
            rectd = playerStats.getReceiving().getRecTD();
        }

        // Insert record into FootballStats table
        StringBuilder sql = new StringBuilder();
        sql.append("insert into FootballStats " +
                "(StatsPlayerID,SeasonWeekID,SeasonID,TeamID,Position,Started,Played," +
                "IDPAssists,IDPTackles,IDPSacks,IDPSackYardsLost," +
                "IDPFumbleRecoveries,IDPFumbleReturnsForTD,IDPFumblesForced,IDPFumbleReturnYards," +
                "IDPInterceptions,IDPIntReturnsForTD,IDPIntReturnYards,IDPPassesDefensed,IDPSafeties," +
                "RushTD,RecTD,XtraTD," +
                "FantasyPts,SalFantasyPts,PlayerID) " +
                "values (" + playerObj.getPlayer().getId() + "," +
                seasonweekid + "," +
                Season._CurrentSeasonID + "," +
                player.getTeamID() + ",'" +
                playerObj.getPlayer().getPosition() + "'," +
                playerStats.getMiscellaneous().getGamesStarted() + "," +
                played + "," +
                tacklesAsst + "," +
                tacklesSolo + "," +
                sacks + "," +
                sackYards + "," +
                fumblesRec + "," +
                fumbleTD + "," +
                fumblesForced + "," +
                fumbleYards + "," +
                interceptions + "," +
                interceptionTD + "," +
                intYards + "," +
                passesDefensed + "," +
                safeties + "," +
                rushtd + "," +
                rectd + "," +
                (playerStats.getKickoffReturns().getKrTD() + playerStats.getPuntReturns().getPrTD()) + "," +
                fantasypts + "," +
                salfantasypts + "," +
                player.getPlayerID() + ")");
        System.out.println(sql);
        CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

    }

    private static void insertIntoFootballStatsTable_Offense(Stats_PlayerObj playerObj, Player player, int seasonweekid) throws Exception {
        Stats_PlayerStats playerStats = playerObj.getPlayerStats().get(0);

        double fantasypts = calculateOffensivePlayerFantasyPoints(playerStats);
        double salfantasypts = calculateOffensivePlayerFantasyPoints_SalaryCap(playerStats, fantasypts);

        System.out.println("PlayerID : " + player.getPlayerID() + ",Fantasy Pts : " + fantasypts);
        System.out.println("PlayerID : " + player.getPlayerID() + ",Sal Fantasy Pts : " + salfantasypts);

//        int played = playerStats.getSnapCounts().getOffenseSnaps() > 0 ? 1 : 0;
        int played = 1;
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

    public static void saveFootballStatsTable_Defense(Stats_PlayerObj playerObj, Player player, int seasonweekid) throws Exception {

        if (playerObj.getPlayerStats() == null || playerObj.getPlayerStats().size() < 1) {
            return;
        }

        // check to see if a record already exists.  If so, then delete it.
        FootballStats existing = FootballStats.getRecordByPlayerIDSeasonWeekID(player.getPlayerID(), seasonweekid);
        if (existing != null) {
            // delete it first
            existing.Delete();
        }
        insertIntoFootballStatsTable_Defense(playerObj, player, seasonweekid);

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

    private static double calculateDefensivePlayerFantasyPoints(Stats_PlayerStats playerStats) {
        // calculate Player Fantasy Points
        double fantasypts = 0.00;
        if (playerStats.getRushing() != null) {
            fantasypts += 6*((double)playerStats.getRushing().getRushTD());
        }
        if (playerStats.getReceiving() != null) {
            fantasypts += 6*((double)playerStats.getReceiving().getRecTD());
        }
        if (playerStats.getFumbles() != null) {
            fantasypts += 6*((double)playerStats.getFumbles().getFumTD());
            fantasypts += playerStats.getFumbles().getFumForced();
            fantasypts += 3*((double)playerStats.getFumbles().getFumOppRec());
        }
        if (playerStats.getTackles() != null) {
            fantasypts += playerStats.getTackles().getTackleSolo();
            fantasypts += (double)playerStats.getTackles().getTackleAst()/2;
            fantasypts += 3*(playerStats.getTackles().getSacks());
        }
        if (playerStats.getInterceptions() != null) {
            fantasypts += 6*((double)playerStats.getInterceptions().getIntTD());
            fantasypts += 4*((double)playerStats.getInterceptions().getInterceptions());
        }

        DecimalFormat twoDForm = new DecimalFormat("#.##");

        fantasypts = Double.valueOf(twoDForm.format(fantasypts));
        return fantasypts;
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

    public static void main(String[] args) {
        System.out.println("Starting...");
        try {
//            getSchedule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
