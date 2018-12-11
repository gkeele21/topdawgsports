package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.AuDate;
import bglib.util.FSUtils;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.data.CTDataSetDef;
import tds.util.CTReturnCode;

public class FSFootballTransaction {
    
    // DB FIELDS
    private int _FSTransactionID;    
    private int _FSTeamID;    
    private int _FSSeasonWeekID;    
    private AuDate _TransactionDate;
    private int _DropPlayerID;   
    private String _DropType;
    private int _PUPlayerID;   
    private String _PUType;
    private String _TransactionType;
    private int _FSLeagueID;
    
    // OBJECTS
    private FSTeam _FSTeam;
    private FSSeasonWeek _FSSeasonWeek;
    private Player _DropPlayer;
    private Player _PUPlayer;    
    
    // CONSTRUCTORS
    public FSFootballTransaction() {
    }
   
    public FSFootballTransaction(int transactionID) {
        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSFootballTransaction", "t.", ""));
            sql.append(",").append(_Cols.getColumnList("FSTeam", "tm.", "FSTeam$"));
            sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("Player", "dp.", "DropPlayer$"));
            sql.append(",").append(_Cols.getColumnList("Player", "pp.", "PUPlayer$"));
            sql.append(",").append(_Cols.getColumnList("Position", "dps.", "DropPlayerPosition$"));
            sql.append(",").append(_Cols.getColumnList("Position", "pps.", "PUPlayerPosition$"));
            sql.append(",").append(_Cols.getColumnList("Team", "dt.", "DropPlayerTeam$"));
            sql.append(",").append(_Cols.getColumnList("Team", "pt.", "PUPlayerTeam$"));
            sql.append(" FROM FSFootballTransaction t ");
            sql.append(" INNER JOIN FSTeam tm ON t.FSTeamID = tm.FSTeamID ");
            sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = tm.FSLeagueID ");
            sql.append(" INNER JOIN FSSeasonWeek w ON w.FSSeasonWeekID = t.FSSeasonWeekID ");
            sql.append(" INNER JOIN Player dp ON dp.PlayerID = t.DropPlayerID ");
            sql.append(" INNER JOIN Player pp ON pp.PlayerID = t.PUPlayerID ");
            sql.append(" INNER JOIN Position dps ON dps.PositionID = dp.PositionID ");
            sql.append(" INNER JOIN Position pps ON pps.PositionID = pp.PositionID ");
            sql.append(" INNER JOIN Team dt ON dt.TeamID = dp.TeamID ");
            sql.append(" INNER JOIN Team pt ON pt.TeamID = pp.TeamID ");
            sql.append(" WHERE t.FSTransactionID = ").append(transactionID);

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }
    }

    public FSFootballTransaction(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FSFootballTransaction(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }
    
    // GETTERS
    public int getFSTransactionID() {return _FSTransactionID;}    
    public int getFSTeamID() {return _FSTeamID;}    
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}   
    public AuDate getTransactionDate() {return _TransactionDate;}
    public int getDropPlayerID() {return _DropPlayerID;}    
    public String getDropType() {return _DropType;}
    public int getPUPlayerID() {return _PUPlayerID;}    
    public String getPUType() {return _PUType;}
    public String getTransactionType() {return _TransactionType;}
    public int getFSLeagueID() {return _FSLeagueID;}
    public FSTeam getFSTeam() {if (_FSTeam == null && _FSTeamID > 0) {_FSTeam = new FSTeam(_FSTeamID);}return _FSTeam;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}
    public Player getDropPlayer() {if (_DropPlayer == null && _DropPlayerID > 0) {_DropPlayer = Player.getInstance(_DropPlayerID);}return _DropPlayer;}
    public Player getPUPlayer() {if (_PUPlayer == null && _PUPlayerID > 0) {_PUPlayer = Player.getInstance(_PUPlayerID);}return _PUPlayer;}
    
    // SETTERS
    public void setFSTransactionID(int FSTransactionID) {_FSTransactionID = FSTransactionID;}    
    public void setFSTeamID(int FSTeamID) {_FSTeamID = FSTeamID;}    
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}    
    public void setTransactionDate(AuDate TransactionDate) {_TransactionDate = TransactionDate;}
    public void setDropPlayerID(int DropPlayerID) {_DropPlayerID = DropPlayerID;}    
    public void setDropType(String DropType) {_DropType = DropType;}
    public void setPUPlayerID(int PUPlayerID) {_PUPlayerID = PUPlayerID;}    
    public void setPUType(String PUType) {_PUType = PUType;}
    public void setTransactionType(String TransactionType) {_TransactionType = TransactionType;}
    public void setFSLeagueID(int FSLeagueID) {_FSLeagueID = FSLeagueID;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}
    public void setDropPlayer(Player DropPlayer) {_DropPlayer = DropPlayer;}
    public void setPUPlayer(Player PUPlayer) {_PUPlayer = PUPlayer;}
    
    // PUBLIC METHODS

    public static List<FSFootballTransaction> getTransactions(int leagueID, int fsseasonweekID) {
        List<FSFootballTransaction> transactions = new ArrayList<FSFootballTransaction>();

        CachedRowSet crs = null;
        Connection con = null;
        try {
            
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSFootballTransaction", "t.", ""));
            sql.append(",").append(_Cols.getColumnList("FSTeam", "tm.", "FSTeam$"));
            sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "w.", "FSSeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("Player", "dp.", "DropPlayer$"));
            sql.append(",").append(_Cols.getColumnList("Player", "pp.", "PUPlayer$"));
            sql.append(",").append(_Cols.getColumnList("Position", "dps.", "DropPlayerPosition$"));
            sql.append(",").append(_Cols.getColumnList("Position", "pps.", "PUPlayerPosition$"));
            sql.append(",").append(_Cols.getColumnList("Team", "dt.", "DropPlayerTeam$"));
            sql.append(",").append(_Cols.getColumnList("Team", "pt.", "PUPlayerTeam$"));
            sql.append(" FROM FSFootballTransaction t ");
            sql.append(" INNER JOIN FSTeam tm ON t.FSTeamID = tm.FSTeamID ");
            sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = tm.FSLeagueID ");
            sql.append(" INNER JOIN FSSeasonWeek w ON w.FSSeasonWeekID = t.FSSeasonWeekID ");
            sql.append(" INNER JOIN Player dp ON dp.PlayerID = t.DropPlayerID ");
            sql.append(" INNER JOIN Player pp ON pp.PlayerID = t.PUPlayerID ");
            sql.append(" INNER JOIN Position dps ON dps.PositionID = dp.PositionID ");
            sql.append(" INNER JOIN Position pps ON pps.PositionID = pp.PositionID ");
            sql.append(" INNER JOIN Team dt ON dt.TeamID = dp.TeamID ");
            sql.append(" INNER JOIN Team pt ON pt.TeamID = pp.TeamID ");
            sql.append(" WHERE t.FSLeagueID = ").append(leagueID);
            sql.append(" AND t.FSSeasonWeekID = ").append(fsseasonweekID);

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                transactions.add(new FSFootballTransaction(crs));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return transactions;
    }

    public CTReturnCode insert() throws Exception {

        int id = CTApplication._CT_DB.updateDataSet(CTDataSetDef.INSERT_NEW_FSFOOTBALLTRANSACTION, getFSLeagueID(), getFSTeamID(), getFSSeasonWeekID(), new java.sql.Timestamp(new AuDate().getDateInMillis()), getDropPlayerID(), getDropType(), getPUPlayerID(), getPUType(), getTransactionType());
        
        CTReturnCode ret = (id > 0) ? new CTReturnCode(tds.util.CTReturnType.SUCCESS,id) : new CTReturnCode(tds.util.CTReturnType.DB_ERROR,id);

        return ret;
    }

    public static CTReturnCode insert(FSFootballTransactionRequest request) throws Exception {

        int id = CTApplication._CT_DB.updateDataSet(CTDataSetDef.INSERT_NEW_FSFOOTBALLTRANSACTION, request.getFSTeam().getFSLeagueID(), request.getFSTeamID(),
                request.getFSSeasonWeekID(), new java.sql.Timestamp(new AuDate().getDateInMillis()), request.getDropPlayerID(),
                request.getDropType(), request.getPUPlayerID(), request.getPUType(), "PU");
        
        CTReturnCode ret = (id > 0) ? new CTReturnCode(tds.util.CTReturnType.SUCCESS,id) : new CTReturnCode(tds.util.CTReturnType.DB_ERROR,id);

        return ret;
    }

    public static List<FSTeam> getTransactionOrder(int fsleagueID, int fsseasonweekID) {
        List<FSTeam> order = new ArrayList<FSTeam>();

        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSTeam", "tm.", "FSTeam$"));
            sql.append(",").append(_Cols.getColumnList("FSFootballTransactionOrder", "t.", ""));
            sql.append(" FROM FSFootballTransactionOrder t ");
            sql.append(" INNER JOIN FSTeam tm ON t.FSTeamID = tm.FSTeamID ");
            sql.append(" WHERE t.FSLeagueID = ").append(fsleagueID);
            sql.append(" AND t.FSSeasonWeekID = ").append(fsseasonweekID);
            sql.append(" ORDER BY t.OrderNumber");

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                order.add(new FSTeam(crs,"FSTeam$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return order;
    }

    public static void createTransactionOrder(int fsleagueID, int fsseasonweekID) {

        // Grab last week's transaction order
        FSSeasonWeek thisWeek = new FSSeasonWeek(fsseasonweekID);
        int lastWeekNo = thisWeek.getFSSeasonWeekNo() - 1;
        int lastWeekId = FSSeasonWeek.GetFSSeasonWeekID(fsleagueID, lastWeekNo);

        ArrayList newOrder = new ArrayList();

        List<FSTeam> oldOrder = FSFootballTransaction.getTransactionOrder(fsleagueID, lastWeekId);

        for (FSTeam team : oldOrder) {
            newOrder.add(team.getFSTeamID());
        }

        // go through last week's transactions and create the new order
        List<FSFootballTransaction> transactions = FSFootballTransaction.getTransactions(fsleagueID, lastWeekId);

        while (transactions.size() > 0) {

            for (FSTeam team : oldOrder) {
                int oldTeamId = team.getFSTeamID();
                
                // check the transactions for this team;
                if (transactions.size() > 0) {
                    for (FSFootballTransaction transaction : transactions) {
                        int transInd = transactions.indexOf(transaction);
                        String type = transaction.getDropType();
                        if (type.equals("TRADE")) {
                            transactions.remove(transInd);
                            continue;
                        }

                        int tempTeamId = transaction.getFSTeamID();
                        int ind = newOrder.indexOf(tempTeamId);

                        // move this team to the end
                        if (tempTeamId == oldTeamId && ind > 0) {
                            newOrder.remove(ind);
                            newOrder.add(tempTeamId);
                            transactions.remove(transInd);
                        }
                    }
                }
            }
        }
        

        int count = 0;
        for (Object teamId : newOrder) {
            count++;

            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO FSFootballTransactionOrder ");
            sql.append(" (FSLeagueID, FSSeasonWeekID, OrderNumber, FSTeamID) ");
            sql.append(" VALUES (").append(fsleagueID).append(",");
            sql.append(fsseasonweekID).append(",");
            sql.append(count).append(",");
            sql.append(teamId).append(")");

            try {
                CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

            } catch (Exception e) {
                CTApplication._CT_LOG.error(e);
            }

        }
    }

    public static FSSeasonWeek getWeekPutOnIR(int fsLeagueID, int fsTeamID, int playerID) {
        FSSeasonWeek week = null;

        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSFootballTransaction", "t.", ""));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$"));
            sql.append(" FROM FSFootballTransaction t ");
            sql.append(" INNER JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = t.FSSeasonWeekID ");
            sql.append(" WHERE t.FSLeagueID = ").append(fsLeagueID);
            sql.append(" AND t.FSTeamID = ").append(fsTeamID);
            sql.append(" AND t.DropPlayerID = ").append(playerID);
            sql.append(" AND t.DropType = 'ONIR'");

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            if (crs.next()) {
                week = new FSSeasonWeek(crs,"FSSeasonWeek$");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return week;
    }

    public static void main(String[] args) {
        int fsleagueid = 18;
        int fsseasonweekid = 220;

        FSFootballTransaction.createTransactionOrder(fsleagueid, fsseasonweekid);
        
    }
    
    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        
        try {
            
            if (FSUtils.fieldExists(crs, prefix, "FSTransactionID")) {
                setFSTransactionID(crs.getInt(prefix + "FSTransactionID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) {
                setFSTeamID(crs.getInt(prefix + "FSTeamID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) {
                setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "TransactionDate")) {
                setTransactionDate(new AuDate(crs.getTimestamp(prefix + "TransactionDate")));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "DropPlayerID")) {
                setDropPlayerID(crs.getInt(prefix + "DropPlayerID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "DropType")) {
                setDropType(crs.getString(prefix + "DropType"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "PUPlayerID")) {
                setPUPlayerID(crs.getInt(prefix + "PUPlayerID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "PUType")) {
                setPUType(crs.getString(prefix + "PUType"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "TransactionType")) {
                setTransactionType(crs.getString(prefix + "TransactionType"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "FSLeagueID")) {
                setFSLeagueID(crs.getInt(prefix + "FSLeagueID"));
            }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) {
                setFSTeam(new FSTeam(crs, "FSTeam$"));
            }
            
            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "$FSSeasonWeekID")) {
                setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$"));
            }
            
            if (FSUtils.fieldExists(crs, "DropPlayer$", "$PlayerID")) {
                setDropPlayer(new Player(crs, "DropPlayer$"));
            }
            
            if (FSUtils.fieldExists(crs, "PUPlayer$", "$PlayerID")) {
                setPUPlayer(new Player(crs, "PUPlayer$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }        
    } 
}
