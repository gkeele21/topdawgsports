package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class FSFootballStandings implements Serializable {

    // DB FIELDS
    private int _FSTeamID;    
    private int _FSSeasonWeekID;
    private double _FantasyPts;
    private double _TotalFantasyPts;
    private double _GamePoints;
    private double _TotalGamePoints;
    private int _SalarySpent;
    private int _Wins;
    private int _Losses;
    private int _Ties;
    private double _WinPercentage;
    private double _FantasyPtsAgainst;
    private double _TotalFantasyPtsAgainst;
    private int _HiScore;
    private int _TotalHiScores;
    private int _GamesCorrect;
    private int _TotalGamesCorrect;
    private int _GamesWrong;
    private int _TotalGamesWrong;
    private int _Rank;
    private int _CurrentStreak;
    private String _LastFive;
    private String _Notes;

    // OBJECTS
    private FSTeam _FSTeam;
    private FSSeasonWeek _FSSeasonWeek;
    
    // CONSTRUCTORS
    public FSFootballStandings() {
    }

    public FSFootballStandings(CachedRowSet fields) {
        InitFromCRS(fields, "FSFootballStandings$");
    }

    public FSFootballStandings(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }    

    // GETTERS
    public int getFSTeamID() {return _FSTeamID;}    
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public double getFantasyPts() {return _FantasyPts;}
    public double getTotalFantasyPts() {return _TotalFantasyPts;}
    public double getGamePoints() {return _GamePoints;}
    public double getTotalGamePoints() {return _TotalGamePoints;}
    public int getSalarySpent() {return _SalarySpent;}
    public int getWins() {return _Wins;}
    public int getLosses() {return _Losses;}
    public int getTies() {return _Ties;}
    public double getWinPercentage() {return _WinPercentage;}    
    public double getFantasyPtsAgainst() {return _FantasyPtsAgainst;}
    public double getTotalFantasyPtsAgainst() {return _TotalFantasyPtsAgainst;}
    public int getHiScore() {return _HiScore;}
    public int getTotalHiScores() {return _TotalHiScores;}
    public int getGamesCorrect() {return _GamesCorrect;}
    public int getTotalGamesCorrect() {return _TotalGamesCorrect;}
    public int getGamesWrong() {return _GamesWrong;}
    public int getTotalGamesWrong() {return _TotalGamesWrong;}
    public int getRank() {return _Rank;}
    public int getCurrentStreak() {return _CurrentStreak;}    
    public String getLastFive() {return _LastFive;}
    public String getNotes() {return _Notes;}
    public FSTeam getFSTeam() {if (_FSTeam == null && _FSTeamID > 0) {_FSTeam = new FSTeam(_FSTeamID);}return _FSTeam;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}

    // SETTERS
    public void setFSTeamID(int fsTeamId) {_FSTeamID = fsTeamId;}    
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setFantasyPts(double FantasyPts) {_FantasyPts = FantasyPts;}
    public void setTotalFantasyPts(double TotalFantasyPts) {_TotalFantasyPts = TotalFantasyPts;}
    public void setGamePoints(double GamePoints) {_GamePoints = GamePoints;}
    public void setTotalGamePoints(double TotalGamePoints) {_TotalGamePoints = TotalGamePoints;}
    public void setSalarySpent(int SalarySpent) {_SalarySpent = SalarySpent;}
    public void setWins(int Wins) {_Wins = Wins;}
    public void setLosses(int Losses) {_Losses = Losses;}
    public void setTies(int Ties) {_Ties = Ties;}
    public void setWinPercentage(double WinPercentage) {_WinPercentage = WinPercentage;}
    public void setFantasyPtsAgainst(double FantasyPtsAgainst) {_FantasyPtsAgainst = FantasyPtsAgainst;}
    public void setTotalFantasyPtsAgainst(double TotalFantasyPtsAgainst) {_TotalFantasyPtsAgainst = TotalFantasyPtsAgainst;}
    public void setHiScore(int HiScore) {_HiScore = HiScore;}
    public void setTotalHiScores(int TotalHiScores) {_TotalHiScores = TotalHiScores;}
    public void setGamesCorrect(int GamesCorrect) {_GamesCorrect = GamesCorrect;}
    public void setTotalGamesCorrect(int TotalGamesCorrect) {_TotalGamesCorrect = TotalGamesCorrect;}
    public void setGamesWrong(int GamesWrong) {_GamesWrong = GamesWrong;}
    public void setTotalGamesWrong(int TotalGamesWrong) {_TotalGamesWrong = TotalGamesWrong;}
    public void setRank(int Rank) {_Rank = Rank;}
    public void setCurrentStreak(int CurrentStreak) {_CurrentStreak = CurrentStreak;}
    public void setLastFive(String LastFive) {_LastFive = LastFive;}
    public void setNotes(String Notes) {_Notes = Notes;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}

    // PUBLIC METHODS

    public static void CalculateRank(int fsLeagueId, int fsSeasonWeekId, String RankSortBy) {     
        CalculateRank(fsLeagueId,fsSeasonWeekId, RankSortBy, "TotalGamePoints");
    }

    public static void CalculateRank(int fsLeagueId, int fsSeasonWeekId, String RankSortBy, String sortColumn) {
        int rank = 0;
        double prevTotal = 0;
        
        List<FSFootballStandings> standings = FSFootballStandings.getLeagueStandings(fsLeagueId, fsSeasonWeekId, RankSortBy);
        for (int i=0; i < standings.size(); i++) {
             double value = standings.get(i).getTotalGamePoints();
             if (sortColumn.equals("TotalFantasyPts")) {
                 value = standings.get(i).getTotalFantasyPts();
             }
             if (value != prevTotal) {
                 rank = i+1;                 
             }
             standings.get(i).setRank(rank);
             standings.get(i).Save();
             prevTotal = value;
        }
    }

    public static void CalculateRankForAllLeagues(FSSeasonWeek week, String sortColumn) {
        try {            
            List<FSLeague> fsLeagues = FSLeague.GetLeagues(week.getFSSeasonID());
            for (int i=0; i < fsLeagues.size(); i++) {
                CalculateRank(fsLeagues.get(i).getFSLeagueID(), week.getFSSeasonWeekID(), sortColumn + " desc", sortColumn);
            }
        }        
        catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } 
    }

    public static List<FSFootballStandings> getLeagueStandings(int leagueID, int fsseasonweekID) {
        return getLeagueStandings(leagueID, fsseasonweekID, "");
    }
    
    public static List<FSFootballStandings> getLeagueStandings(int leagueID, int fsseasonweekID, String sort) {
        return getLeagueStandings(leagueID, fsseasonweekID, sort, true);
    }
    public static List<FSFootballStandings> getLeagueStandings(int leagueID, int fsseasonweekID, String sort, boolean requireStandingsRecords) {

        List<FSFootballStandings> standings = new ArrayList<FSFootballStandings>();
        if (sort == null || sort.equals("")) {
            sort = " s.Rank";
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("FSTeam", "t.", "FSTeam$"));
        sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$"));
        sql.append(",").append(_Cols.getColumnList("FSFootballStandings", "s.", "FSFootballStandings$"));
        sql.append(",").append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
        sql.append(" FROM FSTeam t ");
        sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = t.FSLeagueID ");
        sql.append(" INNER JOIN FSUser u ON u.FSUserID = t.FSUserID ");
        if (requireStandingsRecords)
        {
            sql.append(" INNER JOIN FSFootballStandings s ON s.FSTeamID = t.FSTeamID ");
            sql.append(" AND s.FSSeasonWeekID = ").append(fsseasonweekID);
            sql.append(" INNER JOIN FSSeasonWeek w ON w.FSSeasonWeekID = s.FSSeasonWeekID ");
        } else
        {
            sql.append(" LEFT JOIN FSFootballStandings s ON s.FSTeamID = t.FSTeamID ");
            sql.append(" LEFT JOIN FSSeasonWeek w ON w.FSSeasonWeekID = s.FSSeasonWeekID ");
        }
        sql.append(" WHERE l.FSLeagueID = ").append(leagueID);
        sql.append(" ORDER BY ").append(sort);

        CachedRowSet crs = null;
        Connection con = null;
        try {
            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                FSFootballStandings stand = new FSFootballStandings(crs);
                standings.add(stand);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }
        
        return standings;
    }

    public static List<FSFootballStandings> GetWeeklyStandings(int fsSeasonWeekId) {

        List<FSFootballStandings> standings = new ArrayList<FSFootballStandings>();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSFootballStandings", "st.", "")).append(", ");
        sql.append(_Cols.getColumnList("FSSeasonWeek", "fssw.", "FSSeasonWeek$")).append(", ");
        sql.append(_Cols.getColumnList("FSTeam", "t.", "FSTeam$")).append(", ");
        sql.append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
        sql.append("FROM FSFootballStandings st ");
        sql.append("JOIN FSSeasonWeek fssw ON fssw.FSSeasonWeekID = st.FSSeasonWeekID ");
        sql.append("JOIN FSTeam t ON t.FSTeamID = st.FSTeamID ");
        sql.append("JOIN FSUser u ON u.FSUserID = t.FSUserID ");
        sql.append("WHERE fssw.FSSeasonWeekID = ").append(fsSeasonWeekId);

        // Call QueryCreator
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                standings.add(new FSFootballStandings(crs,""));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return standings;
    }
    
    public static FSFootballStandings GetWeekStandingsForFSTeam(int fsSeasonWeekId, int fsTeamId) {

        FSFootballStandings standings = new FSFootballStandings();
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append("SELECT").append(_Cols.getColumnList("FSFootballStandings", "", "")).append(" ");
        sql.append("FROM FSFootballStandings ");
        sql.append("WHERE FSSeasonWeekID = ").append(fsSeasonWeekId).append(" AND FSTeamID = ").append(fsTeamId);

        // Call QueryCreator
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                standings = new FSFootballStandings(crs,"");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return standings;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("FSFootballStandings", "FSTeamID", getFSTeamID(), "FSSeasonWeekID", getFSSeasonWeekID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /*  This method populates the object from a cached row set.  */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) { setFSTeamID(crs.getInt(prefix + "FSTeamID")); }
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) { setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID")); }
            if (FSUtils.fieldExists(crs, prefix, "FantasyPts")) { setFantasyPts(crs.getDouble(prefix + "FantasyPts")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalFantasyPts")) { setTotalFantasyPts(crs.getDouble(prefix + "TotalFantasyPts")); }
            if (FSUtils.fieldExists(crs, prefix, "GamePoints")) { setGamePoints(crs.getDouble(prefix + "GamePoints")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalGamePoints")) { setTotalGamePoints(crs.getDouble(prefix + "TotalGamePoints")); }
            if (FSUtils.fieldExists(crs, prefix, "SalarySpent")) { setSalarySpent(crs.getInt(prefix + "SalarySpent")); }
            if (FSUtils.fieldExists(crs, prefix, "Wins")) { setWins(crs.getInt(prefix + "Wins")); }
            if (FSUtils.fieldExists(crs, prefix, "Losses")) { setLosses(crs.getInt(prefix + "Losses")); }
            if (FSUtils.fieldExists(crs, prefix, "Ties")) { setTies(crs.getInt(prefix + "Ties")); }
            if (FSUtils.fieldExists(crs, prefix, "WinPercentage")) { setWinPercentage(crs.getDouble(prefix + "WinPercentage")); }
            if (FSUtils.fieldExists(crs, prefix, "FantasyPtsAgainst")) { setFantasyPtsAgainst(crs.getDouble(prefix + "FantasyPtsAgainst")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalFantasyPtsAgainst")) { setTotalFantasyPtsAgainst(crs.getDouble(prefix + "TotalFantasyPtsAgainst")); }
            if (FSUtils.fieldExists(crs, prefix, "HiScore")) { setHiScore(crs.getInt(prefix + "HiScore")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalHiScores")) { setTotalHiScores(crs.getInt(prefix + "TotalHiScores")); }
            if (FSUtils.fieldExists(crs, prefix, "GamesCorrect")) { setGamesCorrect(crs.getInt(prefix + "GamesCorrect")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalGamesCorrect")) { setTotalGamesCorrect(crs.getInt(prefix + "TotalGamesCorrect")); }
            if (FSUtils.fieldExists(crs, prefix, "GamesWrong")) { setGamesWrong(crs.getInt(prefix + "GamesWrong")); }
            if (FSUtils.fieldExists(crs, prefix, "TotalGamesWrong")) { setTotalGamesWrong(crs.getInt(prefix + "TotalGamesWrong")); }
            if (FSUtils.fieldExists(crs, prefix, "Rank")) { setRank(crs.getInt(prefix + "Rank")); }
            if (FSUtils.fieldExists(crs, prefix, "CurrentStreak")) { setCurrentStreak(crs.getInt(prefix + "CurrentStreak")); }
            if (FSUtils.fieldExists(crs, prefix, "LastFive")) { setLastFive(crs.getString(prefix + "LastFive")); }
            if (FSUtils.fieldExists(crs, prefix, "Notes")) { setNotes(crs.getString(prefix + "Notes")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) { setFSTeam(new FSTeam(crs, "FSTeam$")); }
            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) { setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO FSFootballStandings ");
        sql.append("(FSTeamID, FSSeasonWeekID, FantasyPts, TotalFantasyPts, GamePoints, TotalGamePoints, SalarySpent, Wins, Losses, Ties, WinPercentage, FantasyPtsAgainst, ");
        sql.append("TotalFantasyPtsAgainst, HiScore, TotalHiScores, GamesCorrect, TotalGamesCorrect, GamesWrong, TotalGamesWrong, Rank, CurrentStreak, LastFive, Notes) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getFSTeamID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getFantasyPts()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalFantasyPts()));
        sql.append(FSUtils.InsertDBFieldValue(getGamePoints()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalGamePoints()));
        sql.append(FSUtils.InsertDBFieldValue(getSalarySpent()));
        sql.append(FSUtils.InsertDBFieldValue(getWins()));
        sql.append(FSUtils.InsertDBFieldValue(getLosses()));
        sql.append(FSUtils.InsertDBFieldValue(getTies()));
        sql.append(FSUtils.InsertDBFieldValue(getWinPercentage()));
        sql.append(FSUtils.InsertDBFieldValue(getFantasyPtsAgainst()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalFantasyPtsAgainst()));
        sql.append(FSUtils.InsertDBFieldValue(getHiScore()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalHiScores()));
        sql.append(FSUtils.InsertDBFieldValue(getGamesCorrect()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalGamesCorrect()));
        sql.append(FSUtils.InsertDBFieldValue(getGamesWrong()));
        sql.append(FSUtils.InsertDBFieldValue(getTotalGamesWrong()));
        sql.append(FSUtils.InsertDBFieldValue(getRank()));
        sql.append(FSUtils.InsertDBFieldValue(getCurrentStreak()));
        sql.append(FSUtils.InsertDBFieldValue(getLastFive(), true));
        sql.append(FSUtils.InsertDBFieldValue(getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE FSFootballStandings SET ");
        sql.append(FSUtils.UpdateDBFieldValue("FantasyPts", getFantasyPts()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalFantasyPts", getTotalFantasyPts()));
        sql.append(FSUtils.UpdateDBFieldValue("GamePoints", getGamePoints()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalGamePoints", getTotalGamePoints()));
        sql.append(FSUtils.UpdateDBFieldValue("SalarySpent", getSalarySpent()));
        sql.append(FSUtils.UpdateDBFieldValue("Wins", getWins()));
        sql.append(FSUtils.UpdateDBFieldValue("Losses", getLosses()));
        sql.append(FSUtils.UpdateDBFieldValue("Ties", getTies()));
        sql.append(FSUtils.UpdateDBFieldValue("WinPercentage", getWinPercentage()));
        sql.append(FSUtils.UpdateDBFieldValue("FantasyPtsAgainst", getFantasyPtsAgainst()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalFantasyPtsAgainst", getTotalFantasyPtsAgainst()));
        sql.append(FSUtils.UpdateDBFieldValue("HiScore", getHiScore()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalHiScores", getTotalHiScores()));
        sql.append(FSUtils.UpdateDBFieldValue("GamesCorrect", getGamesCorrect()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalGamesCorrect", getTotalGamesCorrect()));
        sql.append(FSUtils.UpdateDBFieldValue("GamesWrong", getGamesWrong()));
        sql.append(FSUtils.UpdateDBFieldValue("TotalGamesWrong", getTotalGamesWrong()));
        sql.append(FSUtils.UpdateDBFieldValue("Rank", getRank()));
        sql.append(FSUtils.UpdateDBFieldValue("CurrentStreak", getCurrentStreak()));
        sql.append(FSUtils.UpdateDBFieldValue("LastFive", getLastFive(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Notes", getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE FSTeamID = ").append(getFSTeamID()).append(" AND FSSeasonWeekID = ").append(getFSSeasonWeekID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}