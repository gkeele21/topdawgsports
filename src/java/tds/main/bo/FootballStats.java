package tds.main.bo;

import bglib.util.AuUtil;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.util.CTReturnCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static tds.data.CTColumnLists._Cols;

public class FootballStats implements Serializable {

    // DB FIELDS
    private String _StatsPlayerID;
    private LocalDate _BeginPlayDate;
    private LocalDate _EndPlayDate;
    private int _SeasonID;
    private int _TeamID;
    private String _Position;
    private String _InjuryStatus;
    private int _Started;
    private int _Played;
    private int _Inactive;
    private int _PassComp;
    private int _PassAtt;
    private int _PassYards;
    private int _PassInt;
    private int _PassTD;
    private int _PassTwoPt;
    private int _Sacked;
    private int _SackedYardsLost;
    private int _RushAtt;
    private int _RushYards;
    private int _RushTD;
    private int _RushTwoPt;
    private int _RecCatches;
    private int _RecYards;
    private int _RecTD;
    private int _RecTwoPt;
    private int _XPM;
    private int _XPA;
    private int _XPBlocked;
    private int _FGM;
    private int _FGA;
    private int _FGBlocked;
    private int _FG29Minus;
    private int _FG30to39;
    private int _FG40to49;
    private int _FG50Plus;
    private int _FumblesLost;
    private int _XtraTD;
    private String _TDDistances;
    private String _StatsOfficial;
    private int _FootballStatsID;
    private double _FantasyPts;
    private int _IDPAssists;
    private int _IDPFumbleRecoveries;
    private int _IDPFumbleReturnYards;
    private int _IDPFumbleReturnsForTD;
    private int _IDPFumblesForced;
    private int _IDPIntReturnYards;
    private int _IDPIntReturnsForTD;
    private int _IDPInterceptions;
    private int _IDPPassesDefensed;
    private int _IDPQBHurries;
    private int _IDPSackYardsLost;
    private int _IDPSacks;
    private int _IDPSafeties;
    private String _IDPTDDistances;
    private int _IDPTackles;
    private String _OverallRank;
    private String _PositionRank;
    private double _SalFantasyPts;
    private int _SeasonWeekID;
    private int _RecTargets;
    private double _AvgFantasyPts;
    private int _PlayerID;

    // OBJECTS
    private Player _Player;
    private Season _Season;
    private Team _Team;
    private SeasonWeek _SeasonWeek;

    // CONSTRUCTORS
    public FootballStats() {
    }

    public FootballStats(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FootballStats(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS
    public String getStatsPlayerID() {return _StatsPlayerID;}
    public LocalDate getBeginPlayDate() {return _BeginPlayDate;}
    public LocalDate getEndPlayDate() {return _EndPlayDate;}
    public int getSeasonID() {return _SeasonID;}
    public int getTeamID() {return _TeamID;}
    public String getPosition() {return _Position;}
    public String getInjuryStatus() {return _InjuryStatus;}
    public int getStarted() {return _Started;}
    public int getPlayed() {return _Played;}
    public int getInactive() {return _Inactive;}
    public int getPassComp() {return _PassComp;}
    public int getPassAtt() {return _PassAtt;}
    public int getPassYards() {return _PassYards;}
    public int getPassInt() {return _PassInt;}
    public int getPassTD() {return _PassTD;}
    public int getPassTwoPt() {return _PassTwoPt;}
    public int getSacked() {return _Sacked;}
    public int getSackedYardsLost() {return _SackedYardsLost;}
    public int getRushAtt() {return _RushAtt;}
    public int getRushYards() {return _RushYards;}
    public int getRushTD() {return _RushTD;}
    public int getRushTwoPt() {return _RushTwoPt;}
    public int getRecCatches() {return _RecCatches;}
    public int getRecYards() {return _RecYards;}
    public int getRecTD() {return _RecTD;}
    public int getRecTwoPt() {return _RecTwoPt;}
    public int getXPM() {return _XPM;}
    public int getXPA() {return _XPA;}
    public int getXPBlocked() {return _XPBlocked;}
    public int getFGM() {return _FGM;}
    public int getFGA() {return _FGA;}
    public int getFGBlocked() {return _FGBlocked;}
    public int getFG29Minus() {return _FG29Minus;}
    public int getFG30to39() {return _FG30to39;}
    public int getFG40to49() {return _FG40to49;}
    public int getFG50Plus() {return _FG50Plus;}
    public int getFumblesLost() {return _FumblesLost;}
    public int getXtraTD() {return _XtraTD;}
    public String getTDDistances() {return _TDDistances;}
    public String getStatsOfficial() {return _StatsOfficial;}
    public int getFootballStatsID() {return _FootballStatsID;}
    public double getFantasyPts() {return _FantasyPts;}
    public int getIDPAssists() {return _IDPAssists;}
    public int getIDPFumbleRecoveries() {return _IDPFumbleRecoveries;}
    public int getIDPFumbleReturnYards() {return _IDPFumbleReturnYards;}
    public int getIDPFumbleReturnsForTD() {return _IDPFumbleReturnsForTD;}
    public int getIDPFumblesForced() {return _IDPFumblesForced;}
    public int getIDPIntReturnYards() {return _IDPIntReturnYards;}
    public int getIDPIntReturnsForTD() {return _IDPIntReturnsForTD;}
    public int getIDPInterceptions() {return _IDPInterceptions;}
    public int getIDPPassesDefensed() {return _IDPPassesDefensed;}
    public int getIDPQBHurries() {return _IDPQBHurries;}
    public int getIDPSackYardsLost() {return _IDPSackYardsLost;}
    public int getIDPSacks() {return _IDPSacks;}
    public int getIDPSafeties() {return _IDPSafeties;}
    public String getIDPTDDistances() {return _IDPTDDistances;}
    public int getIDPTackles() {return _IDPTackles;}
    public String getOverallRank() {return _OverallRank;}
    public String getPositionRank() {return _PositionRank;}
    public double getSalFantasyPts() {return _SalFantasyPts;}
    public int getSeasonWeekID() {return _SeasonWeekID;}
    public int getRecTargets() {return _RecTargets;}
    public int getPlayerID() {return _PlayerID;}
    public double getAvgFantasyPts() { return _AvgFantasyPts; }
    public Player getPlayer() {if (_Player == null && !AuUtil.isEmpty(_StatsPlayerID)) {_Player = Player.createFromStatsID(_StatsPlayerID);} return _Player;}
    public Season getSeason() {if (_Season == null && _SeasonID > 0) {_Season = new Season(_SeasonID);}return _Season;}
    public Team getTeam() {if (_Team == null && _TeamID > 0) {_Team = new Team(_TeamID);}return _Team;}
    public SeasonWeek getSeasonWeek() {if (_SeasonWeek == null && _SeasonWeekID > 0) {_SeasonWeek = new SeasonWeek(_SeasonWeekID);}return _SeasonWeek;}

    // SETTERS
    public void setStatsPlayerID(String StatsPlayerID) {_StatsPlayerID = StatsPlayerID;}
    public void setBeginPlayDate(LocalDate BeginPlayDate) {_BeginPlayDate = BeginPlayDate;}
    public void setEndPlayDate(LocalDate EndPlayDate) {_EndPlayDate = EndPlayDate;}
    public void setSeasonID(int SeasonID) {_SeasonID = SeasonID;}
    public void setTeamID(int TeamID) {_TeamID = TeamID;}
    public void setPosition(String Position) {_Position = Position;}
    public void setInjuryStatus(String InjuryStatus) {_InjuryStatus = InjuryStatus;}
    public void setStarted(int Started) {_Started = Started;}
    public void setPlayed(int Played) {_Played = Played;}
    public void setInactive(int Inactive) {_Inactive = Inactive;}
    public void setPassComp(int PassComp) {_PassComp = PassComp;}
    public void setPassAtt(int PassAtt) {_PassAtt = PassAtt;}
    public void setPassYards(int PassYards) {_PassYards = PassYards;}
    public void setPassInt(int PassInt) {_PassInt = PassInt;}
    public void setPassTD(int PassTD) {_PassTD = PassTD;}
    public void setPassTwoPt(int PassTwoPt) {_PassTwoPt = PassTwoPt;}
    public void setSacked(int Sacked) {_Sacked = Sacked;}
    public void setSackedYardsLost(int SackedYardsLost) {_SackedYardsLost = SackedYardsLost;}
    public void setRushAtt(int RushAtt) {_RushAtt = RushAtt;}
    public void setRushYards(int RushYards) {_RushYards = RushYards;}
    public void setRushTD(int RushTD) {_RushTD = RushTD;}
    public void setRushTwoPt(int RushTwoPt) {_RushTwoPt = RushTwoPt;}
    public void setRecCatches(int RecCatches) {_RecCatches = RecCatches;}
    public void setRecYards(int RecYards) {_RecYards = RecYards;}
    public void setRecTD(int RecTD) {_RecTD = RecTD;}
    public void setRecTwoPt(int RecTwoPt) {_RecTwoPt = RecTwoPt;}
    public void setXPM(int XPM) {_XPM = XPM;}
    public void setXPA(int XPA) {_XPA = XPA;}
    public void setXPBlocked(int XPBlocked) {_XPBlocked = XPBlocked;}
    public void setFGM(int FGM) {_FGM = FGM;}
    public void setFGA(int FGA) {_FGA = FGA;}
    public void setFGBlocked(int FGBlocked) {_FGBlocked = FGBlocked;}
    public void setFG29Minus(int FG29Minus) {_FG29Minus = FG29Minus;}
    public void setFG30to39(int FG30to39) {_FG30to39 = FG30to39;}
    public void setFG40to49(int FG40to49) {_FG40to49 = FG40to49;}
    public void setFG50Plus(int FG50Plus) {_FG50Plus = FG50Plus;}
    public void setFumblesLost(int FumblesLost) {_FumblesLost = FumblesLost;}
    public void setXtraTD(int XtraTD) {_XtraTD = XtraTD;}
    public void setTDDistances(String TDDistances) {_TDDistances = TDDistances;}
    public void setStatsOfficial(String StatsOfficial) {_StatsOfficial = StatsOfficial;}
    public void setFootballStatsID(int FootballStatsID) {_FootballStatsID = FootballStatsID;}
    public void setFantasyPts(double FantasyPts) {_FantasyPts = FantasyPts;}
    public void setIDPAssists(int IDPAssists) {_IDPAssists = IDPAssists;}
    public void setIDPFumbleRecoveries(int IDPFumbleRecoveries) {_IDPFumbleRecoveries = IDPFumbleRecoveries;}
    public void setIDPFumbleReturnYards(int IDPFumbleReturnYards) {_IDPFumbleReturnYards = IDPFumbleReturnYards;}
    public void setIDPFumbleReturnsForTD(int IDPFumbleReturnsForTD) {_IDPFumbleReturnsForTD = IDPFumbleReturnsForTD;}
    public void setIDPFumblesForced(int IDPFumblesForced) {_IDPFumblesForced = IDPFumblesForced;}
    public void setIDPIntReturnYards(int IDPIntReturnYards) {_IDPIntReturnYards = IDPIntReturnYards;}
    public void setIDPIntReturnsForTD(int IDPIntReturnsForTD) {_IDPIntReturnsForTD = IDPIntReturnsForTD;}
    public void setIDPInterceptions(int IDPInterceptions) {_IDPInterceptions = IDPInterceptions;}
    public void setIDPPassesDefensed(int IDPPassesDefensed) {_IDPPassesDefensed = IDPPassesDefensed;}
    public void setIDPQBHurries(int IDPQBHurries) {_IDPQBHurries = IDPQBHurries;}
    public void setIDPSackYardsLost(int IDPSackYardsLost) {_IDPSackYardsLost = IDPSackYardsLost;}
    public void setIDPSacks(int IDPSacks) {_IDPSacks = IDPSacks;}
    public void setIDPSafeties(int IDPSafeties) {_IDPSafeties = IDPSafeties;}
    public void setIDPTDDistances(String IDPTDDistances) {_IDPTDDistances = IDPTDDistances;}
    public void setIDPTackles(int IDPTackles) {_IDPTackles = IDPTackles;}
    public void setOverallRank(String OverallRank) {_OverallRank = OverallRank;}
    public void setPositionRank(String PositionRank) {_PositionRank = PositionRank;}
    public void setSalFantasyPts(double SalFantasyPts) {_SalFantasyPts = SalFantasyPts;}
    public void setSeasonWeekID(int SeasonWeekID) {_SeasonWeekID = SeasonWeekID;}
    public void setRecTargets(int RecTargets) {_RecTargets = RecTargets;}
    public void setPlayerID(int PlayerID) {_PlayerID = PlayerID;}
    public void setAvgFantasyPts( double avg) { _AvgFantasyPts = avg; }
    public void setPlayer(Player Player) {_Player = Player;}
    public void setSeason(Season Season) {_Season = Season;}
    public void setTeam(Team Team) {_Team = Team;}
    public void setSeasonWeek(SeasonWeek SeasonWeek) {_SeasonWeek = SeasonWeek;}

    // PUBLIC METHODS

    public double getAvgSalFantasyPts() {
        if (getPlayed() < 1) {
            return 0;
        } else {
            return getSalFantasyPts() / getPlayed();
        }
    }

    public CachedRowSet getTopPerformers(FSLeague fsleague, SeasonWeek seasonWeek, FSSeasonWeek fsseasonWeek, String positionName) {

        CachedRowSet crs = null;

        // Retrieve the Top Performers
        StringBuffer teamsStr = new StringBuffer();

        if (fsleague != null && fsleague.getFSLeagueID() > 0 && fsseasonWeek != null) {

            List<FSTeam> teams = fsleague.GetTeams();
            int count = 0;
            for (FSTeam team : teams) {
                count++;
                if (count > 1) {
                    teamsStr.append(",");
                }
                teamsStr.append(team.getFSTeamID());
            }
        }

        if (positionName == null) {
            positionName = "";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(_Cols.getColumnList("FootballStats", "s.", "FootballStats$"));
        sql.append(",").append(_Cols.getColumnList("Player", "p.", "Player$"));
        sql.append(",").append(_Cols.getColumnList("Position", "ps.", "Position$"));
        sql.append(",").append(_Cols.getColumnList("FootballStats","tst.", "TotalFootballStats$"));
        if (teamsStr.length() > 0) {
            sql.append(",").append(_Cols.getColumnList("FSTeam","t.", "FSTeam$"));
            sql.append(",").append(_Cols.getColumnList("FSRoster","r.", "FSRoster$"));
        }
        sql.append(" FROM FootballStats s");
        sql.append(" INNER JOIN Player p ON p.StatsPlayerID = s.StatsPlayerID");
//        sql.append(" INNER JOIN FootballStats tst ON tst.StatsPlayerID = p.StatsPlayerID AND tst.SeasonWeekID = 0");
        sql.append(" INNER JOIN FootballStats tst ON tst.PlayerID = p.PlayerID AND tst.SeasonWeekID = 0");
        sql.append(" INNER JOIN Position ps ON ps.PositionID = p.PositionID");
        if (teamsStr.length() > 0) {
            sql.append(" LEFT JOIN FSRoster r ON r.PlayerID = p.PlayerID AND r.FSTeamID IN (");
            sql.append(teamsStr).append(") AND r.FSSeasonWeekID = ").append(fsseasonWeek.getFSSeasonWeekID());
            sql.append(" LEFT JOIN FSTeam t ON t.FSTeamID = r.FSTeamID");
        }
        sql.append(" WHERE s.SeasonWeekID = ").append(seasonWeek.getSeasonWeekID());
        if (!positionName.equals("")) {
            sql.append(" AND ps.PositionName = '").append(positionName).append("'");
        }
        sql.append(" ORDER BY s.FantasyPts DESC ");

//        System.out.println("Top Performers : " + sql);
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
//            data = FSUtils.crsToList(crs);

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return crs;
    }

    public static FootballStats getRecordByPlayerIDSeasonWeekID(int playerId, int seasonWeekId) throws Exception {
        FootballStats stats = null;

        StringBuilder check = new StringBuilder();
        check.append(" SELECT *");
        check.append(" FROM FootballStats");
        check.append(" WHERE PlayerID = ").append(playerId);
        check.append(" AND SeasonWeekID = ").append(seasonWeekId);

        CachedRowSet crs = CTApplication._CT_QUICK_DB.executeQuery(check.toString());
        if (crs != null) {
            crs.next();
            stats = new FootballStats();
            stats.initFromCRS(crs, "");
        }
        return stats;
    }

    public CTReturnCode Delete() {
        int res = 0;
        StringBuilder sql = new StringBuilder();

        sql.append("DELETE FROM FootballStats ");
        sql.append("WHERE FootballStatsID = ").append(getFootballStatsID());

        try {
            res = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return (res > 0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "StatsPlayerID")) {
                setStatsPlayerID(crs.getString(prefix + "StatsPlayerID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "BeginPlayDate")){
                Timestamp s = crs.getTimestamp(prefix + "BeginPlayDate");
                if (s != null) {
                    setBeginPlayDate(s.toLocalDateTime().toLocalDate());
                }
            }

            if (FSUtils.fieldExists(crs, prefix, "EndPlayDate")) {
                Timestamp s = crs.getTimestamp(prefix + "EndPlayDate");
                if (s != null) {
                    setEndPlayDate(s.toLocalDateTime().toLocalDate());
                }
            }

            if (FSUtils.fieldExists(crs, prefix, "SeasonID")) {
                setSeasonID(crs.getInt(prefix + "SeasonID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TeamID")) {
                setTeamID(crs.getInt(prefix + "TeamID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Position")) {
                setPosition(crs.getString(prefix + "Position"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TeamID")) {
                setInjuryStatus(crs.getString(prefix + "TeamID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Started")) {
                setStarted(crs.getInt(prefix + "Started"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Played")) {
                setPlayed(crs.getInt(prefix + "Played"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Inactive")) {
                setInactive(crs.getInt(prefix + "Inactive"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PassComp")) {
                setPassComp(crs.getInt(prefix + "PassComp"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PassAtt")) {
                setPassAtt(crs.getInt(prefix + "PassAtt"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PassYards")) {
                setPassYards(crs.getInt(prefix + "PassYards"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PassInt")) {
                setPassInt(crs.getInt(prefix + "PassInt"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PassTD")) {
                setPassTD(crs.getInt(prefix + "PassTD"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PassTwoPt")) {
                setPassTwoPt(crs.getInt(prefix + "PassTwoPt"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Sacked")) {
                setSacked(crs.getInt(prefix + "Sacked"));
            }

            if (FSUtils.fieldExists(crs, prefix, "SackedYardsLost")) {
                setSackedYardsLost(crs.getInt(prefix + "SackedYardsLost"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RushAtt")) {
                setRushAtt(crs.getInt(prefix + "RushAtt"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RushYards")) {
                setRushYards(crs.getInt(prefix + "RushYards"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RushTD")) {
                setRushTD(crs.getInt(prefix + "RushTD"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RushTwoPt")) {
                setRushTwoPt(crs.getInt(prefix + "RushTwoPt"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RecCatches")) {
                setRecCatches(crs.getInt(prefix + "RecCatches"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RecYards")) {
                setRecYards(crs.getInt(prefix + "RecYards"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RecTD")) {
                setRecTD(crs.getInt(prefix + "RecTD"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RecTwoPt")) {
                setRecTwoPt(crs.getInt(prefix + "RecTwoPt"));
            }

            if (FSUtils.fieldExists(crs, prefix, "XPM")) {
                setXPM(crs.getInt(prefix + "XPM"));
            }

            if (FSUtils.fieldExists(crs, prefix, "XPA")) {
                setXPA(crs.getInt(prefix + "XPA"));
            }

            if (FSUtils.fieldExists(crs, prefix, "XPBlocked")) {
                setXPBlocked(crs.getInt(prefix + "XPBlocked"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FGM")) {
                setFGM(crs.getInt(prefix + "FGM"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FGA")) {
                setFGA(crs.getInt(prefix + "FGA"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FGBlocked")) {
                setFGBlocked(crs.getInt(prefix + "FGBlocked"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FG29Minus")) {
                setFG29Minus(crs.getInt(prefix + "FG29Minus"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FG30to39")) {
                setFG30to39(crs.getInt(prefix + "FG30to39"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FG40to49")) {
                setFG40to49(crs.getInt(prefix + "FG40to49"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FG50Plus")) {
                setFG50Plus(crs.getInt(prefix + "FG50Plus"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FumblesLost")) {
                setFumblesLost(crs.getInt(prefix + "FumblesLost"));
            }

            if (FSUtils.fieldExists(crs, prefix, "XtraTD")) {
                setXtraTD(crs.getInt(prefix + "XtraTD"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TDDistances")) {
                setTDDistances(crs.getString(prefix + "TDDistances"));
            }

            if (FSUtils.fieldExists(crs, prefix, "StatsOfficial")) {
                setStatsOfficial(crs.getString(prefix + "StatsOfficial"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FootballStatsID")) {
                setFootballStatsID(crs.getInt(prefix + "FootballStatsID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FantasyPts")) {
                setFantasyPts(crs.getDouble(prefix + "FantasyPts"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPAssists")) {
                setIDPAssists(crs.getInt(prefix + "IDPAssists"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPFumbleRecoveries")) {
                setIDPFumbleRecoveries(crs.getInt(prefix + "IDPFumbleRecoveries"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPFumbleReturnYards")) {
                setIDPFumbleReturnYards(crs.getInt(prefix + "IDPFumbleReturnYards"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPFumbleReturnsForTD")) {
                setIDPFumbleReturnsForTD(crs.getInt(prefix + "IDPFumbleReturnsForTD"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPFumblesForced")) {
                setIDPFumblesForced(crs.getInt(prefix + "IDPFumblesForced"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPIntReturnYards")) {
                setIDPIntReturnYards(crs.getInt(prefix + "IDPIntReturnYards"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPIntReturnsForTD")) {
                setIDPIntReturnsForTD(crs.getInt(prefix + "IDPIntReturnsForTD"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPInterceptions")) {
                setIDPInterceptions(crs.getInt(prefix + "IDPInterceptions"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPPassesDefensed")) {
                setIDPPassesDefensed(crs.getInt(prefix + "IDPPassesDefensed"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPQBHurries")) {
                setIDPQBHurries(crs.getInt(prefix + "IDPQBHurries"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPSackYardsLost")) {
                setIDPSackYardsLost(crs.getInt(prefix + "IDPSackYardsLost"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPSacks")) {
                setIDPSacks(crs.getInt(prefix + "IDPSacks"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPSafeties")) {
                setIDPSafeties(crs.getInt(prefix + "IDPSafeties"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPTDDistances")) {
                setIDPTDDistances(crs.getString(prefix + "IDPTDDistances"));
            }

            if (FSUtils.fieldExists(crs, prefix, "IDPTackles")) {
                setIDPTackles(crs.getInt(prefix + "IDPTackles"));
            }

            if (FSUtils.fieldExists(crs, prefix, "OverallRank")) {
                setOverallRank(crs.getString(prefix + "OverallRank"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PositionRank")) {
                setPositionRank(crs.getString(prefix + "PositionRank"));
            }

            if (FSUtils.fieldExists(crs, prefix, "SalFantasyPts")) {
                setSalFantasyPts(crs.getDouble(prefix + "SalFantasyPts"));
            }

            if (FSUtils.fieldExists(crs, prefix, "SeasonWeekID")) {
                setSeasonWeekID(crs.getInt(prefix + "SeasonWeekID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RecTargets")) {
                setRecTargets(crs.getInt(prefix + "RecTargets"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PlayerID")) {
                setPlayerID(crs.getInt(prefix + "PlayerID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "AvgFantasyPts")) {
                setAvgFantasyPts(crs.getDouble(prefix + "AvgFantasyPts"));
            } else {
                if (getPlayed() < 1) {
                    setAvgFantasyPts(0);
                } else {
                    setAvgFantasyPts(getFantasyPts() / getPlayed());
                }
            }

            // OBJECTS
            /*
            if (FSUtils.fieldExists(crs, "Player$", "PlayerID")) {
                _Player = new Player(crs, "Player$");
            }
            */

            if (FSUtils.fieldExists(crs, "Season$", "SeasonID")) {
                setSeason(new Season(crs, "Season$"));
            }

            /*
            if (FSUtils.fieldExists(crs, "Team$", "TeamID")) {
                _Team = new Team(crs, "Team$");
            }
            */

            if (FSUtils.fieldExists(crs, "SeasonWeek$", "SeasonWeekID")) {
                setSeasonWeek(new SeasonWeek(crs, "SeasonWeek$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }


}
