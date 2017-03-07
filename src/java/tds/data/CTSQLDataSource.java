package tds.data;

import bglib.data.JDBCDatabase;
import static tds.main.bo.CTApplication._CT_LOG;
import sun.jdbc.rowset.CachedRowSet;

import java.sql.Connection;
import java.util.*;

import tds.main.bo.CTApplication;

public class  CTSQLDataSource implements CTDataSource {

    private JDBCDatabase _SQLDB;
    private static final Map<String, CTSQLDataSource> _Instances = new HashMap<String, CTSQLDataSource>();

    private CTSQLDataSource(String connectionString) throws Exception {
        _SQLDB = JDBCDatabase.getInstance(connectionString);
    }

    public static CTSQLDataSource getInstance(String connectionString) {
        CTSQLDataSource ret=_Instances.get(connectionString);
        if (ret==null) {
            try {
                ret = new CTSQLDataSource(connectionString);
                _Instances.put(connectionString, ret);
            }
            catch (Exception e) {
                _CT_LOG.error(e);
                throw new RuntimeException(e);
            }
        }

        return ret;
    }

    public JDBCDatabase getJDBCDatabase() { return _SQLDB; }

    public Connection getConn(boolean autoCommit) {
        Connection ret=null;
        try {
            ret = _SQLDB.getConn(autoCommit);
        }
        catch (Exception e) {
            _CT_LOG.error(e);
            throw new RuntimeException(e);
        }

        return ret;
    }

    public int updateDataSet(CTDataSetDef dsd, Object ... args) {
        return updateDataSet((Connection)null, dsd, args);
    }

    public int updateDataSet(Connection con, CTDataSetDef dsd, Object ... args) {
        int ret = 0;

        try {
            ret = _SQLDB.executePreparedUpdate(con, dsd.getQuery(), args);
        }
        catch (Exception e) {
            _CT_LOG.error(e);
            throw new RuntimeException(e);
        }

        return ret;
    }

    public int insertDataSet(CTDataSetDef dsd, String storedProcedureCall, Object ... args) {
        return insertDataSet((Connection)null, dsd, storedProcedureCall, args);
    }

    public int insertDataSet(Connection con, CTDataSetDef dsd, String storedProcedureCall, Object ... args) {
        int ret = 0;

        try {
            ret = _SQLDB.executePreparedInsert(con, dsd.getQuery(), storedProcedureCall, args);
        }
        catch (Exception e) {
            _CT_LOG.error(e);
            throw new RuntimeException(e);
        }

        return ret;
    }

/*    public List<Map<String, Object>> getDataSet(CTDataSetDef dsd, Object ... args) {
        return getDataSet((Connection)null, dsd, args);
    }

    public List<Map<String, Object>> getDataSet(Connection con, CTDataSetDef dsd, Object ... args) {
        List<Map<String, Object>> ret;
        try {
            CachedRowSet crs = new CachedRowSet();
            if (CTApplication.DISPLAYQUERIES) {
                System.out.println("Query : " + dsd.getQuery());
                System.out.println("Args : " + args.toString());
            }
            crs = _SQLDB.executePreparedStatement(con, dsd.getQuery(), args);
//            if (dsd.getBreakOnID() != null && dsd.getBreakOnID().length()>0) {
//                FSUtils.dumpCRStoCSV(crs, "c:/fas/" + dsd.toString() + ".csv");
//            }
            ret = FSUtils.crsToList(crs, "", dsd.getBreakOnID());
            if (crs!=null) {
                crs.close();
            }
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
            throw new RuntimeException(e);
        }

        return ret;
    }
*/
    
    public CachedRowSet getDataSet(CTDataSetDef dsd, Object ... args) {
        return getDataSet((Connection)null, dsd, args);
    }

    public CachedRowSet getDataSet(Connection con, CTDataSetDef dsd, Object ... args) {
        CachedRowSet crs;
        try {
            crs = new CachedRowSet();
            if (CTApplication.DISPLAYQUERIES) {
                System.out.println("Query : " + dsd.getQuery());
                System.out.println("Args : " + args.toString());
            }
            crs = _SQLDB.executePreparedStatement(con, dsd.getQuery(), args);
//            if (dsd.getBreakOnID() != null && dsd.getBreakOnID().length()>0) {
//                FSUtils.dumpCRStoCSV(crs, "c:/fas/" + dsd.toString() + ".csv");
//            }
        }
        catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
            throw new RuntimeException(e);
        }

        return crs;
    }

}
