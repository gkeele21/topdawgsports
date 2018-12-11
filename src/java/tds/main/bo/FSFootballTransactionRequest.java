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

public class FSFootballTransactionRequest {
    
    // DB FIELDS
    private int _RequestID;
    private int _FSSeasonWeekID;    
    private int _FSTeamID;    
    private int _Rank;
    private int _DropPlayerID;    
    private String _DropType;
    private int _PUPlayerID;    
    private String _PUType;
    private AuDate _RequestDate;
    private int _Processed;
    private int _Granted;
    
    // OBJECTS
    private FSSeasonWeek _FSSeasonWeek;
    private FSTeam _FSTeam;
    private Player _DropPlayer;
    private Player _PUPlayer;
    
    // CONSTRUCTORS
    public FSFootballTransactionRequest() {
    }

    public FSFootballTransactionRequest(int transactionID) {
        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSTeam", "tm.", "FSTeam$"));
            sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("FSFootballTransactionRequest", "r.", "FSFootballTransactionRequest$"));
            sql.append(" FROM FSFootballTransactionRequest r ");
            sql.append(" INNER JOIN FSTeam tm ON r.FSTeamID = tm.FSTeamID ");
            sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = tm.FSLeagueID ");
            sql.append(" INNER JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append(" WHERE r.RequestID = ").append(transactionID);

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "FSFootballTransactionRequest$");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

    }

    public FSFootballTransactionRequest(int fsTeamID, int fsSeasonWeekID, int rank) {
        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(_Cols.getColumnList("FSTeam", "tm.", "FSTeam$"));
            sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$"));
            sql.append(",").append(_Cols.getColumnList("FSFootballTransactionRequest", "r.", "FSFootballTransactionRequest$"));
            sql.append(" FROM FSFootballTransactionRequest r ");
            sql.append(" INNER JOIN FSTeam tm ON r.FSTeamID = tm.FSTeamID ");
            sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = tm.FSLeagueID ");
            sql.append(" INNER JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
            sql.append(" WHERE r.FSTeamID = ").append(fsTeamID);
            sql.append(" AND r.FSSeasonWeekID = ").append(fsSeasonWeekID);
            sql.append(" AND r.Rank = ").append(rank);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "FSFootballTransactionRequest$");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }

    public FSFootballTransactionRequest(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FSFootballTransactionRequest(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }
    
    // GETTERS
    public int getRequestID() {return _RequestID;}
    public int getFSSeasonWeekID() {return _FSSeasonWeekID;}
    public int getFSTeamID() {return _FSTeamID;}
    public int getRank() {return _Rank;}
    public int getDropPlayerID() {return _DropPlayerID;}
    public String getDropType() {return _DropType;}
    public int getPUPlayerID() {return _PUPlayerID;}
    public String getPUType() {return _PUType;}
    public AuDate getRequestDate() {return _RequestDate;}
    public int getProcessed() {return _Processed;}
    public int getGranted() {return _Granted;}
    public FSSeasonWeek getFSSeasonWeek() {if (_FSSeasonWeek == null && _FSSeasonWeekID > 0) {_FSSeasonWeek = new FSSeasonWeek(_FSSeasonWeekID);}return _FSSeasonWeek;}
    public FSTeam getFSTeam() {if (_FSTeam == null && _FSTeamID > 0) {_FSTeam = new FSTeam(_FSTeamID);}return _FSTeam;}
    public Player getDropPlayer() {if (_DropPlayer == null && _DropPlayerID > 0) {_DropPlayer = Player.getInstance(_DropPlayerID);}return _DropPlayer;}
    public Player getPUPlayer() {if (_PUPlayer == null && _PUPlayerID > 0) {_PUPlayer = Player.getInstance(_PUPlayerID);}return _PUPlayer;}
    
    // SETTERS
    public void setRequestID(int RequestID) {_RequestID = RequestID;}
    public void setFSSeasonWeekID(int FSSeasonWeekID) {_FSSeasonWeekID = FSSeasonWeekID;}
    public void setFSTeamID(int FSTeamID) {_FSTeamID = FSTeamID;}
    public void setRank(int Rank) {_Rank = Rank;}    
    public void setDropPlayerID(int DropPlayerID) {_DropPlayerID = DropPlayerID;}
    public void setDropType(String DropType) {_DropType = DropType;}    
    public void setPUPlayerID(int PUPlayerID) {_PUPlayerID = PUPlayerID;}  
    public void setPUType(String PUType) {_PUType = PUType;}    
    public void setRequestDate(AuDate RequestDate) {_RequestDate = RequestDate;}    
    public void setProcessed(int Processed) {_Processed = Processed;}    
    public void setGranted(int Granted) {_Granted = Granted;}
    public void setFSSeasonWeek(FSSeasonWeek FSSeasonWeek) {_FSSeasonWeek = FSSeasonWeek;}
    public void setFSTeam(FSTeam FSTeam) {_FSTeam = FSTeam;}
    public void setDropPlayer(Player DropPlayer) {_DropPlayer = DropPlayer;}
    public void setPUPlayer(Player PUPlayer) {_PUPlayer = PUPlayer;}

    // PUBLIC METHODS   

    public static List<FSFootballTransactionRequest> getTransactionRequests(int fsteamID, int fsseasonweekID) {
        return getTransactionRequests(fsteamID, fsseasonweekID, -1);
    }

    public static List<FSFootballTransactionRequest> getTransactionRequests(int fsteamID, int fsseasonweekID, int processed) {
        List<FSFootballTransactionRequest> requests = new ArrayList<FSFootballTransactionRequest>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("FSTeam", "tm.", "FSTeam$"));
        sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$"));
        sql.append(",").append(_Cols.getColumnList("FSFootballTransactionRequest", "r.", "FSFootballTransactionRequest$"));
        sql.append(" FROM FSFootballTransactionRequest r ");
        sql.append(" INNER JOIN FSTeam tm ON r.FSTeamID = tm.FSTeamID ");
        sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = tm.FSLeagueID ");
        sql.append(" INNER JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
        sql.append(" WHERE r.FSTeamID = ").append(fsteamID);
        sql.append(" AND r.FSSeasonWeekID = ").append(fsseasonweekID);
        if (processed >= 0) {
            sql.append(" AND r.Processed = ").append(processed);
        }
        sql.append(" ORDER BY r.Rank");

        CachedRowSet crs = null;
        Connection con = null;
        try {
            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                requests.add(new FSFootballTransactionRequest(crs,"FSFootballTransactionRequest$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return requests;

    }

    public static List<FSFootballTransactionRequest> getLeagueRequests(int fsLeagueID, int fsseasonweekID) {
        List<FSFootballTransactionRequest> requests = new ArrayList<FSFootballTransactionRequest>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("FSTeam", "tm.", "FSTeam$"));
        sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
        sql.append(",").append(_Cols.getColumnList("FSSeasonWeek", "fsw.", "FSSeasonWeek$"));
        sql.append(",").append(_Cols.getColumnList("FSFootballTransactionRequest", "r.", "FSFootballTransactionRequest$"));
        sql.append(" FROM FSFootballTransactionRequest r ");
        sql.append(" INNER JOIN FSTeam tm ON r.FSTeamID = tm.FSTeamID ");
        sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = tm.FSLeagueID ");
        sql.append(" INNER JOIN FSSeasonWeek fsw ON fsw.FSSeasonWeekID = r.FSSeasonWeekID ");
        sql.append(" INNER JOIN FSFootballTransactionOrder fto ON fto.FSTeamID = r.FSTeamID AND fto.FSSeasonWeekID = r.FSSeasonWeekID ");
        sql.append(" WHERE l.FSLeagueID = ").append(fsLeagueID);
        sql.append(" AND r.FSSeasonWeekID = ").append(fsseasonweekID);
        sql.append(" ORDER BY fto.OrderNumber, r.Rank");

        CachedRowSet crs = null;
        Connection con = null;
        try {
            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                requests.add(new FSFootballTransactionRequest(crs,"FSFootballTransactionRequest$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }

        return requests;

    }

    
    public CTReturnCode insert() throws Exception {

        int id = CTApplication._CT_DB.updateDataSet(CTDataSetDef.INSERT_NEW_FSFOOTBALLTRANSACTIONREQUEST, getFSSeasonWeekID(), getFSTeamID(), getRank(), getDropPlayerID(),
                getDropType(), getPUPlayerID(), getPUType(), new java.sql.Timestamp(new AuDate().getDateInMillis()));

        CTReturnCode ret = (id > 0) ? new CTReturnCode(tds.util.CTReturnType.SUCCESS,id) : new CTReturnCode(tds.util.CTReturnType.DB_ERROR,id);

        return ret;
    }

    public CTReturnCode update() throws Exception {

        int id = CTApplication._CT_DB.updateDataSet(CTDataSetDef.UPDATE_FSFOOTBALLTRANSACTIONREQUEST, getFSSeasonWeekID(), getFSTeamID(), getRank(), getDropPlayerID(),
                getDropType(), getPUPlayerID(), getPUType(), new java.sql.Timestamp(new AuDate().getDateInMillis()), getProcessed(), getGranted(), getRequestID());

        CTReturnCode ret = (id > 0) ? new CTReturnCode(tds.util.CTReturnType.SUCCESS,id) : new CTReturnCode(tds.util.CTReturnType.DB_ERROR,id);

        return ret;
    }

    public CTReturnCode delete() throws Exception {

        int id = CTApplication._CT_DB.updateDataSet(CTDataSetDef.DELETE_FSFOOTBALLTRANSACTIONREQUEST, getRequestID());

        CTReturnCode ret = (id > 0) ? new CTReturnCode(tds.util.CTReturnType.SUCCESS,id) : new CTReturnCode(tds.util.CTReturnType.DB_ERROR,id);

        return ret;
    }
    
    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        
        try {
            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "RequestID")) {
                setRequestID(crs.getInt(prefix + "RequestID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "FSSeasonWeekID")) {
                setFSSeasonWeekID(crs.getInt(prefix + "FSSeasonWeekID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "FSTeamID")) {
                setFSTeamID(crs.getInt(prefix + "FSTeamID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Rank")) {
                setRank(crs.getInt(prefix + "Rank"));
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
            
            if (FSUtils.fieldExists(crs, prefix, "RequestDate")) {
                setRequestDate(new AuDate(crs.getTimestamp(prefix + "RequestDate")));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Processed")) {
                setProcessed(crs.getInt(prefix + "Processed"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Granted")) {
                setGranted(crs.getInt(prefix + "Granted"));
            }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "FSSeasonWeek$", "FSSeasonWeekID")) {
                setFSSeasonWeek(new FSSeasonWeek(crs, "FSSeasonWeek$"));
            }
            
            if (FSUtils.fieldExists(crs, "FSTeam$", "FSTeamID")) {
                setFSTeam(new FSTeam(crs, "FSTeam$"));
            }
            
            if (FSUtils.fieldExists(crs, "DropPlayer$", "DropPlayerID")) {
                setDropPlayer(new Player(crs, "DropPlayer$"));
            }
            
            if (FSUtils.fieldExists(crs, "PUPlayer$", "PUPlayerID")) {
                setPUPlayer(new Player(crs, "PUPlayer$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }        
    }   
}
