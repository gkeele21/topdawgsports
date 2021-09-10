package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.util.CTReturnCode;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static tds.data.CTColumnLists._Cols;

public class PGATournamentWeekPlayer implements Serializable {

    // DB FIELDS
    private int _ID;
    private int _PGATournamentID;
    private int _FSSeasonWeekID;
    private int _PlayerID;
    private String _FinalScore;
    private int _RelativeToPar;
    private double _MoneyEarned;
    private String _TournamentRank;
    private String _Round1;
    private String _Round2;
    private String _Round3;
    private String _Round4;
    private String _Round5;
    private int _WorldGolfRanking;
    private double _SalaryValue;
    private int _SortOrder;
    private int _Thru;
    private int _TodayRound;

    // OBJECTS
    private Player _Player;
    private FSSeasonWeek _FSSeasonWeek;
    private PGATournament _PGATournament;
    private List<FSTeam> _Owners = new ArrayList();

    // CONSTRUCTORS
    public PGATournamentWeekPlayer() {
    }

    public PGATournamentWeekPlayer(CachedRowSet fields) {
        initFromCRS(fields, "PGATournamentWeekPlayer$");
    }

    public PGATournamentWeekPlayer(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    public PGATournamentWeekPlayer(int ID)
    {
        this(null, ID);
    }

    public PGATournamentWeekPlayer(Connection con, int ID)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("PGATournamentWeekPlayer", "twp.", "PGATournamentWeekPlayer$"));
        sql.append(",").append(_Cols.getColumnList("Player", "p.", "Player$"));
        sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek","fsw.", "FSSeasonWeek$"));
        sql.append(" FROM PGATournamentWeekPlayer twp ");
        sql.append(" INNER JOIN Player p ON p.PlayerID = twp.PlayerID ");
        sql.append(" LEFT JOIN Country c ON c.CountryID = p.CountryID ");
        sql.append(" INNER JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = twp.FSSeasonWeekID ");
        sql.append(" WHERE ID = ").append(ID);

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "PGATournamentWeekPlayer$");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }

    public PGATournamentWeekPlayer(int tournamentID, int fsSeasonWeekID, int playerID)
    {
        this(null, tournamentID, fsSeasonWeekID, playerID);
    }

    public PGATournamentWeekPlayer(Connection con, int tournamentID, int fsSeasonWeekID, int playerID)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("PGATournamentWeekPlayer", "twp.", "PGATournamentWeekPlayer$"));
        sql.append(",").append(_Cols.getColumnList("Player", "p.", "Player$"));
        sql.append(",").append(_Cols.getColumnList("Country", "c.", ""));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek","fsw.", "FSSeasonWeek$"));
        sql.append(" FROM PGATournamentWeekPlayer twp ");
        sql.append(" INNER JOIN Player p ON p.PlayerID = twp.PlayerID ");
        sql.append(" LEFT JOIN Country c ON c.CountryID = p.CountryID ");
        sql.append(" INNER JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = twp.FSSeasonWeekID ");
        sql.append(" WHERE twp.PGATournamentID = ").append(tournamentID);
        sql.append(" AND twp.FSSeasonWeekID = ").append(fsSeasonWeekID);
        sql.append(" AND twp.PlayerID = ").append(playerID);

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "PGATournamentWeekPlayer$");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }

    // GETTERS
    public int getID() {return _ID;}
    public int getPGATournamentID() {return _PGATournamentID;}
    public int getPlayerID() {return _PlayerID;}
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public String getFinalScore() {return _FinalScore;}
    public int getRelativeToPar() {return _RelativeToPar;}
    public double getMoneyEarned() {return _MoneyEarned;}
    public String getTournamentRank() {return _TournamentRank;}
    public String getRound1() {return _Round1;}
    public String getRound2() {return _Round2;}
    public String getRound3() {return _Round3;}
    public String getRound4() {return _Round4;}
    public String getRound5() {return _Round5;}
    public int getWorldGolfRanking() {return _WorldGolfRanking;}
    public double getSalaryValue() {return _SalaryValue;}
    public int getSortOrder() {return _SortOrder;}
    public int getThru() {return _Thru;}
    public int getTodayRound() {return _TodayRound;}

    public Player getPlayer() {if (_Player == null && _PlayerID > 0) {_Player = new Player(_PlayerID);} return _Player;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}
    public PGATournament getPGATournament() {if (_PGATournament == null && _PGATournamentID > 0) {_PGATournament = new PGATournament(_PGATournamentID);}return _PGATournament;}
    public List<FSTeam> getOwners() {if (_Owners == null) {_Owners = new ArrayList();}return _Owners;}

    // SETTERS
    public void setID(int ID) {_ID = ID;}
    public void setPlayerID(int PlayerID) {_PlayerID = PlayerID;}
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setPGATournamentID(int PGATournamentID) {_PGATournamentID = PGATournamentID;}
    public void setFinalScore(String FinalScore) {_FinalScore = FinalScore;}
    public void setRelativeToPar(int RelativeToPar) {_RelativeToPar = RelativeToPar;}
    public void setMoneyEarned(double MoneyEarned) {_MoneyEarned = MoneyEarned;}
    public void setTournamentRank(String TournamentRank) {_TournamentRank = TournamentRank;}
    public void setRound1(String Round1) {_Round1 = Round1;}
    public void setRound2(String Round2) {_Round2 = Round2;}
    public void setRound3(String Round3) {_Round3 = Round3;}
    public void setRound4(String Round4) {_Round4 = Round4;}
    public void setRound5(String Round5) {_Round5 = Round5;}
    public void setWorldGolfRanking(int WGR) {_WorldGolfRanking = WGR;}
    public void setSalaryValue(double value) {_SalaryValue = value;}
    public void setSortOrder(int order) {_SortOrder = order;}
    public void setThru(int thru) {_Thru = thru;}
    public void setTodayRound(int todayRound) {_TodayRound = todayRound;}

    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}
    public void setPGATournament(PGATournament tourney) {_PGATournament = tourney;}
    public void setPlayer(Player player) {_Player = player;}

    // PUBLIC METHODS
    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "ID")) {
                setID(crs.getInt(prefix + "ID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PlayerID")) {
                setPlayerID(crs.getInt(prefix + "PlayerID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")){
                setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "PGATournamentID")){
                setPGATournamentID(crs.getInt(prefix + "PGATournamentID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "FinalScore")) {
                setFinalScore(crs.getString(prefix + "FinalScore"));
            }

            if (FSUtils.fieldExists(crs, prefix, "RelativeToPar")) {
                setRelativeToPar(crs.getInt(prefix + "RelativeToPar"));
            }

            if (FSUtils.fieldExists(crs, prefix, "MoneyEarned")) {
                setMoneyEarned(crs.getDouble(prefix + "MoneyEarned"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TournamentRank")) {
                setTournamentRank(crs.getString(prefix + "TournamentRank"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Round1")) {
                setRound1(crs.getString(prefix + "Round1"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Round2")) {
                setRound2(crs.getString(prefix + "Round2"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Round3")) {
                setRound3(crs.getString(prefix + "Round3"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Round4")) {
                setRound4(crs.getString(prefix + "Round4"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Round5")) {
                setRound5(crs.getString(prefix + "Round5"));
            }

            if (FSUtils.fieldExists(crs, prefix, "WorldGolfRanking")) {
                setWorldGolfRanking(crs.getInt(prefix + "WorldGolfRanking"));
            }

            if (FSUtils.fieldExists(crs, prefix, "SalaryValue")) {
                setSalaryValue(crs.getDouble(prefix + "SalaryValue"));
            }

            if (FSUtils.fieldExists(crs, prefix, "SortOrder")) {
                setSortOrder(crs.getInt(prefix + "SortOrder"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Thru")) {
                setThru(crs.getInt(prefix + "Thru"));
            }

            if (FSUtils.fieldExists(crs, prefix, "TodayRound")) {
                setTodayRound(crs.getInt(prefix + "TodayRound"));
            }

            // OBJECTS

            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) {
                setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$"));
            }

            if (FSUtils.fieldExists(crs, "PGATournament$", "PGATournamentID")) {
                setPGATournament(new PGATournament(crs, "PGATournament$"));
            }

            if (FSUtils.fieldExists(crs, "Player$", "PlayerID")) {
                setPlayer(new Player(crs, "Player$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    public void Save() throws Exception {
        boolean doesExist = FSUtils.DoesARecordExistInDB("PGATournamentWeekPlayer", "ID", getID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    public CTReturnCode Delete() {
        int res = 0;
        StringBuilder sql = new StringBuilder();

        sql.append("DELETE FROM PGATournamentWeekPlayer ");
        sql.append("WHERE ID = ").append(getID());

        try {
            res = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return (res > 0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
    }

    private int Insert() throws Exception {
        int newId = -1;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO PGATournamentWeekPlayer ");
        sql.append("(PGATournamentID, FSSeasonWeekID, PlayerID, RelativeToPar, FinalScore, MoneyEarned, TournamentRank, Round1, Round2, Round3, Round4, Round5, WorldGolfRanking, SalaryValue, SortOrder, Thru, TodayRound) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getPGATournamentID()));
        sql.append(FSUtils.InsertDBFieldValue(getFSSeasonWeekID()));
        sql.append(FSUtils.InsertDBFieldValue(getPlayerID()));
        sql.append(FSUtils.InsertDBFieldValue(getFinalScore(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRelativeToPar()));
        sql.append(FSUtils.InsertDBFieldValue(getMoneyEarned()));
        sql.append(FSUtils.InsertDBFieldValue(getTournamentRank(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRound1(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRound2(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRound3(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRound4(), true));
        sql.append(FSUtils.InsertDBFieldValue(getRound5(), true));
        sql.append(FSUtils.InsertDBFieldValue(getWorldGolfRanking()));
        sql.append(FSUtils.InsertDBFieldValue(getSalaryValue()));
        sql.append(FSUtils.InsertDBFieldValue(getSortOrder()));
        sql.append(FSUtils.InsertDBFieldValue(getThru()));
        sql.append(FSUtils.InsertDBFieldValue(getTodayRound()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            newId = CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            throw new Exception("Error Inserting new PGATournamentWeekPlayer", e);
        }

        return newId;
    }

    private int Update() throws Exception {
        int success = -1;
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE PGATournamentWeekPlayer SET ");
        sql.append(FSUtils.UpdateDBFieldValue("PGATournamentID", getPGATournamentID()));
        sql.append(FSUtils.UpdateDBFieldValue("FSSeasonWeekID", getFSSeasonWeekID()));
        sql.append(FSUtils.UpdateDBFieldValue("PlayerID", getPlayerID()));
        sql.append(FSUtils.UpdateDBFieldValue("FinalScore", getFinalScore(), true));
        sql.append(FSUtils.UpdateDBFieldValue("RelativeToPar", getRelativeToPar()));
        sql.append(FSUtils.UpdateDBFieldValue("MoneyEarned", getMoneyEarned()));
        sql.append(FSUtils.UpdateDBFieldValue("TournamentRank", getTournamentRank(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Round1", getRound1(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Round2", getRound2(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Round3", getRound3(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Round4", getRound4(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Round5", getRound5(), true));
        sql.append(FSUtils.UpdateDBFieldValue("WorldGolfRanking", getWorldGolfRanking()));
        sql.append(FSUtils.UpdateDBFieldValue("SalaryValue", getSalaryValue()));
        sql.append(FSUtils.UpdateDBFieldValue("SortOrder", getSortOrder()));
        sql.append(FSUtils.UpdateDBFieldValue("Thru", getThru()));
        sql.append(FSUtils.UpdateDBFieldValue("TodayRound", getTodayRound()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE ID = ").append(getID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
            success = 0;
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            throw new Exception("Error updating PGATournamentWeekPlayer", e);
        }

        return success;
    }

    public void populateOwners(int fsLeagueID)
    {
        _Owners.clear();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("PGATournamentWeekPlayer", "twp.", "PGATournamentWeekPlayer$"));
        sql.append(",").append(_Cols.getColumnList("FSTeam", "t.", "FSTeam$"));
        sql.append(",").append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
        sql.append(" FROM PGATournamentWeekPlayer twp ");
        sql.append(" INNER JOIN FSRoster r ON r.PlayerID = twp.PlayerID and r.FSSeasonWeekID = twp.FSSeasonWeekID ");
        sql.append(" INNER JOIN FSTeam t ON t.FSTeamID = r.FSTeamID and t.FSLeagueID = ").append(fsLeagueID);
        sql.append(" INNER JOIN FSUser u ON u.FSUserID = t.FSUserID ");
        sql.append(" WHERE twp.PGATournamentID = ").append(getPGATournamentID());
        sql.append(" AND twp.FSSeasonWeekID = ").append(getFSSeasonWeekID());
        sql.append(" AND twp.PlayerID = ").append(getPlayerID());

        if (getPlayerID() == 17972)
        {
            System.out.println("Found player");
        }
        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                FSTeam team = new FSTeam(crs, "FSTeam$");
                if (team != null)
                {
                    _Owners.add(team);
                }
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            e.printStackTrace();
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }

    public boolean hasStartedTournament()
    {
        boolean started = false;
        int score = 0;
        try
        {
            Integer.parseInt(this.getFinalScore());
        } catch (Exception e)
        {
        }

        if (score > 0)
        {
            started = true;
        }

        return started;
    }

}
