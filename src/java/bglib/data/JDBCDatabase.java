package bglib.data;

import static bglib.util.Application._GLOBAL_LOG;
import bglib.util.AuDate;
import bglib.util.AuUtil;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import sun.jdbc.rowset.CachedRowSet;


public class JDBCDatabase {

    private static final Map<String, JDBCDatabase> _Instances = new HashMap<String, JDBCDatabase>();

    public int queryCount;

    private DataSource _DataSource;
    private String _ConnectionString;

    private JDBCDatabase(String connectionString) throws Exception {
//        _ConnectionString = connectionString;
//        _ConnectionString = "jdbc:mysql://localhost:3306/topdawg?user=root&password=lakers&autoReconnect=true";
        if (AuUtil.isEmpty(connectionString)) {
//            _ConnectionString = "jdbc:mysql://localhost:3307/topdawg?user=topdawg&password=laker$&autoReconnect=true";
            _ConnectionString = "jdbc:mysql://localhost:3306/topdawg?user=webuser&password=lakers55&autoReconnect=true";
//            _ConnectionString = "jdbc:mysql://10.5.0.174:3306/topdawg?user=webuser&password=lakers55&autoReconnect=true";
        } else {
            _ConnectionString = connectionString;
        }
        System.out.println("Using Connection String : " + _ConnectionString);
        initSettings();
    }

    public static JDBCDatabase getInstance(String connectionString) {
        JDBCDatabase obj = _Instances.get(connectionString);
        if (obj==null) {
            try {
                obj = new JDBCDatabase(connectionString);
                _Instances.put(connectionString, obj);
            }
            catch (Exception e) {
                _GLOBAL_LOG.dbError(e);
            }
        }

        return obj;
    }

    private void initSettings() throws Exception {

        // init the DataSource
        Context ctx=null;
        try {
            ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            _DataSource = (DataSource)envCtx.lookup("jdbc/topdawg");
        }
        catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            System.out.println("No Tomcat context found; using db connection string from settings file without connection pooling.");
        }
        finally {
            if (ctx!=null) {
                try { ctx.close(); } catch (Exception ne) {}
            }
        }

        initDriver();
    }

    private void initDriver() throws Exception {
        if (_ConnectionString.indexOf("sqlserver")>=0) {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //Class.forName("com.intersys.jdbc.CacheDriver");
        } else {
            Class.forName("com.mysql.jdbc.Driver");
            //Class.forName("org.gjt.mm.mysql.Driver");
        }
        //Class.forName("com.intersys.jdbc.CacheDriver");
    }

    // Methods without the Connection Object
    public int executeUpdate(String query) throws Exception {
        Connection con = null;

        int success = 0;
        try {
            con = getConn();
            success = executeUpdate(con, query);
        } finally {
            close(con);
            if (con!=null && con.isClosed()==false) {
                System.out.println("CONNECTION NOT CLOSED!!!");
            }
        }

        return success;
    }

    public CachedRowSet executeQuery(String query) throws Exception {
        Connection con = null;
        CachedRowSet crs = null;

        try {
            con = getConn();
            crs = executeQuery(con, query);
        } finally {
            close(con);
            if (con!=null && con.isClosed()==false) {
                System.out.println("CONNECTION NOT CLOSED!!!");
            }
        }

        return crs;
    }

    public int executeInsert(String query) throws Exception {
        Connection con = null;

        int success = 0;
        try {
            con = getConn();
            success = executeInsert(getConn(), query);
        } finally {
            close(con);
            if (con!=null && con.isClosed()==false) {
                System.out.println("CONNECTION NOT CLOSED!!!");
            }
        }

        return success;
    }

    public int executeInsert(Connection con, String query) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;

        int id = 0;

        try {
            stmt = con.createStatement();
            int res = stmt.executeUpdate(query);

            if (res > 0) {
                String identQuery = "select @@identity as lastID";
                rs = stmt.executeQuery(identQuery);
                queryCount++;
                rs.next();
                id = Integer.parseInt(rs.getString("lastID"));
            }
        } catch (SQLException e) {
            throw logSQLException(e, query);
        } finally {
            if (rs!=null) {
                rs.close();
            }
            if (stmt!=null) {
                stmt.close();
            }
        }

        return id;
    }

    private Exception logSQLException(SQLException e, String query) {
        return logSQLExceptionWithArgs(e, query);
    }

    private Exception logSQLExceptionWithArgs(SQLException e, String query, Object ... args) {
        String msg = e.getMessage() + "\nThe sql statement was: \"" + query + "\"\n";
        if (args!=null) {
            for (int i=0; i<args.length; i++) {
                msg += "Arg #" + (i+1) + ": " + args[i] + "\n";
            }
        }
        SQLException s = new SQLException(msg, e.getSQLState(), e.getErrorCode());
        s.setStackTrace(e.getStackTrace());
        return s;
    }

    // Methods with Connection Objects
    public int executeUpdate(Connection con,String query) throws Exception {
        if (con==null) {
            return executeUpdate(query);
        }
        Statement stmt = null;
        int success = 0;

        try {
            long t1 = new Date().getTime();
            stmt = con.createStatement();
            success = stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw logSQLException(e, query);
        } finally {
            if (stmt!=null) {
                stmt.close();
            }
        }

        return success;
    }

    public boolean executeTransaction(List<String> queries) {
        Connection con=null;

        try {
            con=getConn();
            con.setAutoCommit(false);

            for (String query : queries) {
                Statement stmt = con.createStatement();
                int ret = stmt.executeUpdate(query);
                if (ret==0) {
                    con.rollback();
                    return false;
                }
            }
            con.commit();
        } catch (Exception e) {
            try {
                if  (con!=null) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                _GLOBAL_LOG.dbError(e);
            }
            _GLOBAL_LOG.dbError(e);
            return false;
        }

        close(con);

        return true;
    }

    public CachedRowSet executeQuery(Connection con,String query) throws Exception {
        if (con==null) {
            return executeQuery(query);
        }
        Statement stmt = null;
        ResultSet rs = null;
        CachedRowSet crs = null;

        try {
            long t1 = new Date().getTime();
            stmt = con.createStatement();
//            System.out.println("Query : " + query);
            rs = stmt.executeQuery(query);
            queryCount++;
            crs = new CachedRowSet();
            crs.populate(rs);
        } catch (SQLException e) {
            throw logSQLException(e, query);
        } finally {
            if (rs!=null) {
                rs.close();
            }
            if (stmt!=null) {
                stmt.close();
            }
        }

        return crs;
    }

    public CachedRowSet executePreparedStatement(String query, Object ... args) throws Exception {
        Connection con = null;
        CachedRowSet crs = null;

        try {
            con = getConn();
            crs = executePreparedStatement(con, query, args);
        } finally {
            close(con);
            if (con!=null && con.isClosed()==false) {
                System.out.println("CONNECTION NOT CLOSED!!!");
            }
        }

        return crs;
    }

    public CachedRowSet executePreparedStatement(Connection con, String query, Object ... args) throws Exception {
        if (con==null) {
            return executePreparedStatement(query, args);
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CachedRowSet crs = null;

        try {
            stmt = con.prepareStatement(query);
            assignQueryParams(stmt, args);
            rs = stmt.executeQuery();
            queryCount++;
            crs = new CachedRowSet();
            crs.populate(rs);
        } catch (SQLException e) {
            throw logSQLExceptionWithArgs(e, query, args);
        } finally {
            if (rs!=null) {
                rs.close();
            }
            if (stmt!=null) {
                stmt.close();
            }
        }

        return crs;
    }

    public int executePreparedUpdate(String query, Object ... args) throws Exception {
        Connection con = null;
        int ret = 0;

        try {
            con = getConn();
            ret = executePreparedUpdate(con, query, args);
        } finally {
            close(con);
            if (con!=null && con.isClosed()==false) {
                System.out.println("CONNECTION NOT CLOSED!!!");
            }
        }

        return ret;
    }

    public int executePreparedUpdate(Connection con, String query, Object ... args) throws Exception {
        if (con==null) {
            return executePreparedUpdate(query, args);
        }
        PreparedStatement stmt = null;
        int ret = 0;

        try {
            stmt = con.prepareStatement(query);
            assignQueryParams(stmt, args);
            ret = stmt.executeUpdate();
        } catch (SQLException e) {
            throw logSQLExceptionWithArgs(e, query, args);
        } finally {
            if (stmt!=null) {
                stmt.close();
            }
        }

        return ret;
    }

    public int executePreparedInsert(String query, String procCall, Object ... args) throws Exception {
        Connection con = null;
        int ret = 0;

        try {
            con = getConn();
            ret = executePreparedInsert(con, query, procCall, args);
        } finally {
            close(con);
            if (con!=null && con.isClosed()==false) {
                System.out.println("CONNECTION NOT CLOSED!!!");
            }
        }

        return ret;
    }

    public int executePreparedInsert(Connection con, String query, String procCall, Object ... args) throws Exception {
        if (con==null) {
            return executePreparedInsert(query, procCall, args);
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;

        try {
            stmt = con.prepareStatement(query);
            assignQueryParams(stmt, args);
            int res = stmt.executeUpdate();
            if (res > 0) {
//                String identQuery = "select @@identity as lastID";
                String identQuery = "select last_insert_id() as lastID";
                rs = executeQuery(con, identQuery);
                queryCount++;
                rs.next();
                id = rs.getInt("lastID");
            }
        } catch (SQLException e) {
            throw logSQLExceptionWithArgs(e, query, args);
        } finally {
            if (rs!=null) {
                rs.close();
            }
            if (stmt!=null) {
                stmt.close();
            }
        }

        return id;
    }

    private static void assignQueryParams(PreparedStatement stmt, Object ... args) throws Exception {
        for (int i=0; i<args.length; i++) {
            if (args[i]==null) {
                stmt.setString(i+1, null);
                continue;
            }
            if (args[i] instanceof String) {
                stmt.setString(i+1, (String)args[i]);
            } else if (args[i] instanceof Integer || args[i] instanceof Short) {
                stmt.setInt(i+1, (Integer)args[i]);
            } else if (args[i] instanceof Double || args[i] instanceof Float) {
                stmt.setDouble(i+1, (Double)args[i]);
            } else if (args[i] instanceof AuDate) {
                stmt.setTime(i+1, new Time(((AuDate)args[i]).getDateInMillis()));
            } else if (args[i] instanceof Long) {
                stmt.setLong(i+1, (Long)args[i]);
            } else {
                stmt.setString(i+1, args[i].toString());
            }
        }
    }

    public void close(Connection con) {
        try {
            if (con!=null) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("could not close con.");
        }
    }

    public static void closeCRS(CachedRowSet crs) {
        try {
            if (crs!=null) {
                crs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() throws Exception {
        return getConn(true);
    }

    public Connection getConn(boolean autoCommit) throws Exception {
        Connection C = null;
//        System.out.println("Grabbing Connection...");
        // first try and get the connection from the Context
        /*try {
            if (_DataSource!=null) {
                System.out.println("Call to getConnection...");
                C = _DataSource.getConnection();
                System.out.println("returned from getConnection");
                if (C != null) {
                    C.setAutoCommit(autoCommit);
                    return C;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error2 : " + e.getMessage());
            e.printStackTrace();
        }*/

//        System.out.println("Con from pool failed.");

        // now invoke the JDBC driver directly

        for (int x=1; C == null && x<=10; x++) {
            try {
//                System.out.println("Creating DB Conn (not from pool).");
//                C = DriverManager.getConnection(_ConnectionString);
//                C = DriverManager.getConnection(_ConnectionString, "root","lakers");
                C = DriverManager.getConnection(_ConnectionString, "webuser","lakers55");
            } catch (SQLException E) {
//                Log.error(new Exception("Failed to get db connection; connection string = " + conString, E));
                System.out.println("conString = " + _ConnectionString);
                E.printStackTrace();
                System.out.println("SQLException: " + E.getMessage());
                System.out.println("SQLState:     " + E.getSQLState());
                System.out.println("VendorError:  " + E.getErrorCode());
            }
            if (C==null) {
                System.out.println("FAILED ONCE TO GET CONNECTION!");
            }
            if (x==3 || x==6) {
                System.out.println("sleeping for 30 seconds before trying again to obtain a connection...");
                Thread.sleep(30000);
            }
        }


        if (C==null) {
            throw new Exception("Failed 10 times to get db connection; connection string = " + _ConnectionString);
        }

        C.setAutoCommit(autoCommit);
        return C;
    }

}
