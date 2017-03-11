package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class Standings implements Serializable {
    
    private int _TeamID;
    private int _SeasonWeekID;
    private int _Wins;
    private int _Losses;
    private int _Ties;
    private double _Percentage;
    private int _PointsFor;
    private int _PointsAgainst;
    private int _NetPoints;
    private int _DivisionRanking;
    private int _OverallRanking;
    private int _TDs;
    private String _HomeRecord;
    private String _RoadRecord;
    private String _DivisionRecord;
    private double _DivisionPercentage;
    private String _ConferenceRecord;
    private double _ConferencePercentage;
    private String _NonConferenceRecord;
    private String _CurrentStreak;
    private String _LastFive;
    
    // OBJECTS
    private Team _Team;
    
    // CONSTRUCTORS
    public Standings() {
        
    }
    
    public Standings(int teamID, int seasonWeekID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Standings", "s.", ""));
            sql.append("FROM Standings s ");
            sql.append("WHERE s.TeamID = ").append(teamID).append(" AND s.SeasonWeekID = ").append(seasonWeekID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            crs.next();
            InitFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    
    }
    
    public Standings(CachedRowSet fields) {
        InitFromCRS(fields, "");
    }

    public Standings(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }
    
    // GETTERS
    public int getTeamID() {return _TeamID;}
    public int getSeasonWeekID() {return _SeasonWeekID;}
    public int getWins() {return _Wins;}
    public int getLosses() {return _Losses;}
    public int getTies() {return _Ties;}
    public double getPercentage() {return _Percentage;}
    public int getPointsFor() {return _PointsFor;}
    public int getPointsAgainst() {return _PointsAgainst;}
    public int getNetPoints() {return _NetPoints;}
    public int getDivisionRanking() {return _DivisionRanking;}
    public int getOverallRanking() {return _OverallRanking;}
    public int getTDs() {return _TDs;}
    public String getHomeRecord() {return _HomeRecord;}
    public String getRoadRecord() {return _RoadRecord;}
    public String getDivisionRecord() {return _DivisionRecord;}
    public double getDivisionPercentage() {return _DivisionPercentage;}
    public String getConferenceRecord() {return _ConferenceRecord;}
    public double getConferencePercentage() {return _ConferencePercentage;}
    public String getNonConferenceRecord() {return _NonConferenceRecord;}
    public String getCurrentStreak() {return _CurrentStreak;}
    public String getLastFive() {return _LastFive;}
    public Team getTeam() {if (_Team == null && _TeamID > 0) {_Team = new Team(_TeamID);}return _Team;}
        
    // SETTERS
    public void setTeamID(int TeamID) {_TeamID = TeamID;}
    public void setSeasonWeekID(int SeasonWeekID) {_SeasonWeekID = SeasonWeekID;}
    public void setWins(int Wins) {_Wins = Wins;}
    public void setLosses(int Losses) {_Losses = Losses;}
    public void setTies(int Ties) {_Ties = Ties;}
    public void setPercentage(double Percentage) {_Percentage = Percentage;}
    public void setPointsFor(int PointsFor) {_PointsFor = PointsFor;}
    public void setPointsAgainst(int PointsAgainst) {_PointsAgainst = PointsAgainst;}
    public void setNetPoints(int NetPoints) {_NetPoints = NetPoints;}
    public void setDivisionRanking(int DivisionRanking) {_DivisionRanking = DivisionRanking;}
    public void setOverallRanking(int OverallRanking) {_OverallRanking = OverallRanking;}
    public void setTDs(int TDs) {_TDs = TDs;}
    public void setHomeRecord(String HomeRecord) {_HomeRecord = HomeRecord;}
    public void setRoadRecord(String RoadRecord) {_RoadRecord = RoadRecord;}
    public void setDivisionRecord(String DivisionRecord) {_DivisionRecord = DivisionRecord;}
    public void setDivisionPercentage(double DivisionPercentage) {_DivisionPercentage = DivisionPercentage;}
    public void setConferenceRecord(String ConferenceRecord) {_ConferenceRecord = ConferenceRecord;}
    public void setConferencePercentage(double ConferencePercentage) {_ConferencePercentage = ConferencePercentage;}
    public void setNonConferenceRecord(String NonConferenceRecord) {_NonConferenceRecord = NonConferenceRecord;}
    public void setCurrentStreak(String CurrentStreak) {_CurrentStreak = CurrentStreak;}
    public void setLastFive(String LastFive) {_LastFive = LastFive;}
    public void setTeam(Team Team) {_Team = Team;}
    
    // PUBLIC METHODS
    
    public static List<Standings> GetStandings(int seasonWeekId) {

        List<Standings> standings = new ArrayList<Standings>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("Standings", "s.", "")).append(", ");
        sql.append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append("FROM Standings s ");
        sql.append("JOIN Team t ON t.TeamID = s.TeamID ");
        sql.append("WHERE s.SeasonWeekID = ").append(seasonWeekId);

        // Call QueryCreator
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                standings.add(new Standings(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return standings;
    }
    
    public static List<Standings> GetTop25Rankings(int seasonWeekId) {

        List<Standings> standings = new ArrayList<Standings>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("Standings", "s.", "")).append(", ");
        sql.append(_Cols.getColumnList("Team", "t.", "Team$"));
        sql.append("FROM Standings s ");
        sql.append("JOIN Team t ON t.TeamID = s.TeamID ");
        sql.append("WHERE s.SeasonWeekID = ").append(seasonWeekId).append(" AND s.OverallRanking BETWEEN 1 AND 25 ");
        sql.append("ORDER BY s.OverallRanking");

        // Call QueryCreator
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                standings.add(new Standings(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return standings;
    }

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("Standings", "TeamID", getTeamID(), "SeasonWeekID", getSeasonWeekID());
        if (doesExist) { Update(); } else { Insert(); }
    }
    
    public static void UpdateByeTeamStandings(SeasonWeek week) {
        List<Game> byeTeams = Game.GetByeTeams(week.getSeasonWeekID(), false);
        SeasonWeek priorWeek = SeasonWeek.GetPriorSeasonWeek(week);
            for (int j=0; j < byeTeams.size(); j++) {
                Standings weekStandings = new Standings(byeTeams.get(j).getHomeID(), week.getSeasonWeekID());
                Standings priorStandings = (priorWeek == null) ? null : new Standings(byeTeams.get(j).getHomeID(), priorWeek.getSeasonWeekID());
                weekStandings.setTeamID(byeTeams.get(j).getHomeID());
                weekStandings.setSeasonWeekID(week.getSeasonWeekID());
                weekStandings.setWins((priorStandings == null) ? 0 : priorStandings.getWins());
                weekStandings.setLosses((priorStandings == null) ? 0 : priorStandings.getLosses());
                weekStandings.Save();        
            }
    }

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "TeamID")) {setTeamID(crs.getInt(prefix + "TeamID")); }
            if (FSUtils.fieldExists(crs, prefix, "SeasonWeekID")) {setSeasonWeekID(crs.getInt(prefix + "SeasonWeekID")); }
            if (FSUtils.fieldExists(crs, prefix, "Wins")) {setWins(crs.getInt(prefix + "Wins")); }
            if (FSUtils.fieldExists(crs, prefix, "Losses")) {setLosses(crs.getInt(prefix + "Losses")); }
            if (FSUtils.fieldExists(crs, prefix, "Ties")) {setTies(crs.getInt(prefix + "Ties")); }
            if (FSUtils.fieldExists(crs, prefix, "Percentage")) {setPercentage(crs.getDouble(prefix + "Percentage")); }
            if (FSUtils.fieldExists(crs, prefix, "PointsFor")) {setPointsFor(crs.getInt(prefix + "PointsFor")); }
            if (FSUtils.fieldExists(crs, prefix, "PointsAgainst")) {setPointsAgainst(crs.getInt(prefix + "PointsAgainst")); }
            if (FSUtils.fieldExists(crs, prefix, "NetPoints")) {setNetPoints(crs.getInt(prefix + "NetPoints")); }            
            if (FSUtils.fieldExists(crs, prefix, "DivisionRanking")) {setDivisionRanking(crs.getInt(prefix + "DivisionRanking")); }
            if (FSUtils.fieldExists(crs, prefix, "OverallRanking")) {setOverallRanking(crs.getInt(prefix + "OverallRanking")); }
            if (FSUtils.fieldExists(crs, prefix, "TDs")) {setTDs(crs.getInt(prefix + "TDs")); }
            if (FSUtils.fieldExists(crs, prefix, "HomeRecord")) {setHomeRecord(crs.getString(prefix + "HomeRecord")); }
            if (FSUtils.fieldExists(crs, prefix, "RoadRecord")) {setRoadRecord(crs.getString(prefix + "RoadRecord")); }
            if (FSUtils.fieldExists(crs, prefix, "DivisionRecord")) {setDivisionRecord(crs.getString(prefix + "DivisionRecord")); }
            if (FSUtils.fieldExists(crs, prefix, "DivisionPercentage")) {setDivisionPercentage(crs.getDouble(prefix + "DivisionPercentage")); }
            if (FSUtils.fieldExists(crs, prefix, "ConferenceRecord")) {setConferenceRecord(crs.getString(prefix + "ConferenceRecord")); }
            if (FSUtils.fieldExists(crs, prefix, "ConferencePercentage")) {setConferencePercentage(crs.getDouble(prefix + "ConferencePercentage")); }
            if (FSUtils.fieldExists(crs, prefix, "NonConferenceRecord")) {setNonConferenceRecord(crs.getString(prefix + "NonConferenceRecord")); }
            if (FSUtils.fieldExists(crs, prefix, "CurrentStreak")) {setCurrentStreak(crs.getString(prefix + "CurrentStreak")); }
            if (FSUtils.fieldExists(crs, prefix, "LastFive")) {setLastFive(crs.getString(prefix + "LastFive")); }
            // OBJECTS
            if (FSUtils.fieldExists(crs, "Team$", "TeamID")) {setTeam(new Team(crs, "Team$")); }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {        
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO Standings ");
        sql.append("(TeamID, SeasonWeekID, Wins, Losses, Ties, Percentage, PointsFor, PointsAgainst, NetPoints, DivisionRanking, OverallRanking, TDs, ");
        sql.append("HomeRecord, RoadRecord, DivisionRecord, DivisionPercentage, ConferenceRecord, ConferencePercentage, NonConferenceRecord, CurrentStreak, LastFive) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getWins()));
        sql.append(FSUtils.InsertDBFieldValue(getLosses()));
        sql.append(FSUtils.InsertDBFieldValue(getTies()));
        sql.append(FSUtils.InsertDBFieldValue(getPercentage()));
        sql.append(FSUtils.InsertDBFieldValue(getPointsFor()));
        sql.append(FSUtils.InsertDBFieldValue(getPointsAgainst()));
        sql.append(FSUtils.InsertDBFieldValue(getNetPoints()));
        sql.append(FSUtils.InsertDBFieldValue(getDivisionRanking()));
        sql.append(FSUtils.InsertDBFieldValue(getOverallRanking()));
        sql.append(FSUtils.InsertDBFieldValue(getTDs()));
        sql.append(FSUtils.InsertDBFieldValue(getHomeRecord(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRoadRecord(), true));
        sql.append(FSUtils.InsertDBFieldValue(getDivisionRecord(), true));
        sql.append(FSUtils.InsertDBFieldValue(getDivisionPercentage()));
        sql.append(FSUtils.InsertDBFieldValue(getConferenceRecord(), true));
        sql.append(FSUtils.InsertDBFieldValue(getConferencePercentage()));
        sql.append(FSUtils.InsertDBFieldValue(getNonConferenceRecord(), true));
        sql.append(FSUtils.InsertDBFieldValue(getCurrentStreak(), true));
        sql.append(FSUtils.InsertDBFieldValue(getLastFive(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE Standings SET ");
        sql.append(FSUtils.UpdateDBFieldValue("Wins", getWins()));
        sql.append(FSUtils.UpdateDBFieldValue("Losses", getLosses()));
        sql.append(FSUtils.UpdateDBFieldValue("Ties", getTies()));
        sql.append(FSUtils.UpdateDBFieldValue("Percentage", getPercentage()));
        sql.append(FSUtils.UpdateDBFieldValue("PointsFor", getPointsFor()));
        sql.append(FSUtils.UpdateDBFieldValue("PointsAgainst", getPointsAgainst()));
        sql.append(FSUtils.UpdateDBFieldValue("NetPoints", getNetPoints()));
        sql.append(FSUtils.UpdateDBFieldValue("DivisionRanking", getDivisionRanking()));        
        sql.append(FSUtils.UpdateDBFieldValue("OverallRanking", getOverallRanking()));
        sql.append(FSUtils.UpdateDBFieldValue("TDs", getTDs()));
        sql.append(FSUtils.UpdateDBFieldValue("HomeRecord", getHomeRecord(), true));
        sql.append(FSUtils.UpdateDBFieldValue("RoadRecord", getRoadRecord(), true));
        sql.append(FSUtils.UpdateDBFieldValue("DivisionRecord", getDivisionRecord(), true));
        sql.append(FSUtils.UpdateDBFieldValue("DivisionPercentage", getDivisionPercentage()));
        sql.append(FSUtils.UpdateDBFieldValue("ConferenceRecord", getConferenceRecord(), true));
        sql.append(FSUtils.UpdateDBFieldValue("ConferencePercentage", getConferencePercentage()));
        sql.append(FSUtils.UpdateDBFieldValue("NonConferenceRecord", getNonConferenceRecord(), true));
        sql.append(FSUtils.UpdateDBFieldValue("CurrentStreak", getCurrentStreak(), true));
        sql.append(FSUtils.UpdateDBFieldValue("LastFive", getLastFive(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE TeamID = ").append(getTeamID()).append(" AND SeasonWeekID = ").append(getSeasonWeekID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
