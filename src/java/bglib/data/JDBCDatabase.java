package bglib.data;

import bglib.util.AuDate;
import bglib.util.AuUtil;
import org.apache.tomcat.jdbc.pool.DataSource;
import sun.jdbc.rowset.CachedRowSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.*;
import java.util.Date;
import java.util.*;

import static bglib.util.Application._GLOBAL_LOG;

public class JDBCDatabase {

    private static final Map<String, JDBCDatabase> _Instances = new HashMap<String, JDBCDatabase>();

    public int queryCount;
    private static int maxConnections = 20;

    private static DataSource _DataSource;
    private String _ConnectionString;

    private JDBCDatabase(String connectionString) throws Exception {
//        _ConnectionString = connectionString;
//        _ConnectionString = "jdbc:mysql://localhost:3306/topdawg?user=root&password=root&autoReconnect=true";
//        _ConnectionString = "jdbc:mysql://localhost:3306/topdawg?user=gkeele&password=w$3sZ#W^3oIV&autoReconnect=true";
        if (AuUtil.isEmpty(connectionString)) {
//            _ConnectionString = "jdbc:mysql://localhost:3307/topdawg?user=topdawg&password=laker$&autoReconnect=true";
            System.out.println("Connection string passed in is empty - using default hard-coded one");
            _ConnectionString = "jdbc:mysql://localhost:3306/topdawg?user=root&password=Lakers55&autoReconnect=true&characterEncoding=utf8";
//            _ConnectionString = "jdbc:mysql://localhost:3306/topdawg?user=gkeele&password=w$3sZ#W^3oIV&autoReconnect=true";
//            _ConnectionString = "jdbc:mysql://topdawg.circlepix.com:3306/topdawg?user=webuser&password=lakers55&autoReconnect=true";
        } else {
            _ConnectionString = connectionString;
        }
        System.out.println("Using Connection String : " + _ConnectionString);
        initSettings();
    }

    public static JDBCDatabase getInstance(String connectionString) {

        System.out.println("Here in getInstance");
        Random rand = new Random();

        int conNum = rand.nextInt(maxConnections);
        if (_Instances.containsKey(""+conNum)) {
            JDBCDatabase obj = _Instances.get(connectionString);
            return obj;
        }

        try {
            System.out.println("Creating new JDBCDatabase instance.");
            JDBCDatabase obj = new JDBCDatabase(connectionString);
            _Instances.put(""+conNum, obj);
            return obj;
        }
        catch (Exception e) {
            _GLOBAL_LOG.dbError(e);
        }

        return null;
    }

    private void initSettings() throws Exception {

        // init the DataSource
//        Context ctx=null;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            _DataSource = new DataSource();
            _DataSource = (DataSource) envCtx.lookup("jdbc/topdawg");

//            ctx = new InitialContext();
//            Context envCtx = (Context) ctx.lookup("java:comp/env");

//            PoolProperties p = new PoolProperties();
//            p.setUrl("jdbc:mysql://" + AppSettings.getDomain() + ":" + AppSettings.getPort() + "/" + AppSettings.getDBName());
//            p.setDriverClassName("com.mysql.jdbc.Driver");
//            p.setUsername(AppSettings.getUsername());
//            p.setPassword(AppSettings.getPassword());
//            p.setJmxEnabled(true);
//            p.setTestWhileIdle(false);
//            p.setTestOnBorrow(true);
//            p.setValidationQuery("SELECT 1");
//            p.setTestOnReturn(false);
//            p.setValidationInterval(30000);
//            p.setTimeBetweenEvictionRunsMillis(30000);
//            p.setMaxActive(30);
//            p.setInitialSize(10);
//            p.setMaxWait(10000);
//            p.setRemoveAbandonedTimeout(10);
//            p.setMinEvictableIdleTimeMillis(30000);
//            p.setMaxIdle(50);
//            p.setMinIdle(10);
//            p.setLogAbandoned(true);
//            p.setRemoveAbandoned(true);
//            p.setJdbcInterceptors(
//                    "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
//                    "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
//            _DataSource = new DataSource();
//            _DataSource.setPoolProperties(p);

            // Test
            Connection con = null;
            try {
                con = _DataSource.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select * from FSUser");
                int cnt = 1;
                while (rs.next()) {
                    System.out.println(cnt++ + " First : " + rs.getString("FirstName"));
                }
                rs.close();
                st.close();
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (Exception ignore) {

                    }
                }
            }
//            System.out.println("Created connection from pool");
//            _DataSource = (DataSource)envCtx.lookup("jdbc/topdawg");
        }
        catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            System.out.println("No Tomcat context found; using db connection string from settings file without connection pooling.");
        }

        initDriver();
    }

    private void initDriver() throws Exception {
//        if (_ConnectionString.indexOf("sqlserver")>=0) {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        } else {
//            Class.forName("com.mysql.jdbc.Driver");
//        }
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
//            con = getConn();
            con = _DataSource.getConnection();
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
            con = _DataSource.getConnection();
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
            close(con);
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

    public static void close(Connection con) {
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

    public static Connection getConnection() throws Exception {
        return _DataSource.getConnection();
    }

    public Connection getConn() throws Exception {
        return getConn(true);
    }

    public Connection getConn(boolean autoCommit) throws Exception {
        Connection C = null;
//        System.out.println("Grabbing Connection...");
        // first try and get the connection from the Context
        try {
            C = _DataSource.getConnection();
            return C;

/*
            JDBCDatabase database = getInstance(_ConnectionString);
            if (database != null) {
                return database.getConn();
            }
            if (_DataSource!=null) {
                Random ran = new Random();
                int conNum = ran.nextInt(maxConnections);
                if (_Instances.containsKey(""+conNum)) {
                    JDBCDatabase dataB = _Instances.get(""+conNum);
                    C = dataB.getConn();
                    return C;
                }

//                C = (Connection)JDBCDatabase.getInstance(_ConnectionString);
                if (C != null) {
                    System.out.println("Returning Connection from getInstance");
                    return C;
                }
                System.out.println("Call to getConnection...");
//                C = ((javax.sql.PooledConnection)_DataSource.getConnection()).getConnection();
                C = _DataSource.getConnection();
                System.out.println("returned from getConnection");
                if (C != null) {
                    C.setAutoCommit(autoCommit);
                    return C;
                } else {
                    System.out.println();
                }
            }
*/
        }
        catch (Exception e) {
            System.out.println("Error2 : " + e.getMessage());
            e.printStackTrace();
        }

//        System.out.println("Con from pool failed.");

        // now invoke the JDBC driver directly

        for (int x=1; C == null && x<=5; x++) {
            try {
//                System.out.println("Creating DB Conn (not from pool).");
                C = DriverManager.getConnection(_ConnectionString);
//                C = DriverManager.getConnection(_ConnectionString, "root","lakers");
//                C = DriverManager.getConnection(_ConnectionString, "webuser","lakers55");
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
