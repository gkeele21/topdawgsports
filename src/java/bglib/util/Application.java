package bglib.util;

import bglib.data.JDBCDatabase;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Application {

    public static final String BG_ROOT_GRANT = "c:/Grant/consulting/TopDawgSports";
    public static final String BG_ROOT = "/topdawg";
    public static final String BG_ROOT_BERT = "C:/Bert/Programs/Topdawg/";
    public static final String GLOBAL_STR = "Global";

    private static final Map<String, Application> _Instances = new HashMap<String, Application>();
    private static final Map<Integer, Application> _InstancesByID = new HashMap<Integer, Application>();

    public static final AppSettings _GLOBAL_SETTINGS = AppSettings.getInstance(BG_ROOT);
    public static final Log _GLOBAL_LOG = Log.getInstance(GLOBAL_STR);
    public static final DateTimeFormatter _DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter _DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        _GLOBAL_QUICK_DB = JDBCDatabase.getInstance(_GLOBAL_SETTINGS.getDBConnectionString());
    }

    public static final Application _GLOBAL_APPLICATION = getInstance(GLOBAL_STR);
    public static JDBCDatabase _GLOBAL_QUICK_DB;


    private String _AppName;
    private String _AppDir;
    private String _AppPrefix;
    private int _AppID;
    private String _EmailFooter;
    private String _SupportEmail;
    private String _CompanyName;
    private LocalDateTime _LastCronHit;

    private Application(String appPrefix) throws Exception {
        System.out.println("Global connection string: " + _GLOBAL_SETTINGS.getDBConnectionString());
//        _GLOBAL_LOG.logMessage(Level.WARNING, "Global connection string: " + _GLOBAL_SETTINGS.getDBConnectionString());
        if (!appPrefix.equals(GLOBAL_STR)) {
            String query = "select * from Application where ApplicationPrefix = '" + appPrefix + "'";
            System.out.println("Query : " + query);
//            _GLOBAL_LOG.logMessage(Level.WARNING,"Query : " + query);
            CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(query);
            if (crs.next()) {
                initFromCRS(crs);
            } else {
                throw new RuntimeException("No application with app prefix '" + appPrefix + "' found. Please ensure the Application table " +
                                           "has an entry for this app prefix.");
            }
        } else {
            _AppName = "Global";
            _AppDir = "Global";
            _AppPrefix = "Global";
        }
    }

    private Application(int id) throws Exception {
        String query = "select * from Application where ApplicationID = " + id;
        Connection con = JDBCDatabase.getConnection();
        CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(query);
        if (crs.next()) {
            initFromCRS(crs);
        } else {
            throw new RuntimeException("No application with app id '" + id + "' found. Please ensure the Application table " +
                                       "has an entry for this app id.");
        }
    }

    private void initFromCRS(CachedRowSet crs) throws Exception {
        _AppName = crs.getString("ApplicationName");
        _AppDir = crs.getString("ApplicationDir");
        _AppPrefix = crs.getString("ApplicationPrefix");
        _AppID = crs.getInt("ApplicationID");
        _EmailFooter = crs.getString("EmailFooter");
        _SupportEmail = crs.getString("SupportEmail");
        _CompanyName = crs.getString("CompanyName");
        Timestamp s = crs.getTimestamp("LastCronHit");
        if (s != null) {
            _LastCronHit = s.toLocalDateTime();
        }
    }

    public static Application getInstance(String appName) {
        Application ret = _Instances.get(appName);
        if (ret==null) {
            try {
                ret = new Application(appName);
                _Instances.put(ret.getAppPrefix(), ret);
                _InstancesByID.put(ret.getAppID(), ret);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            _Instances.put(appName, ret);
        }

        return ret;
    }

    public static Application getInstance(int id) {
        Application ret = _InstancesByID.get(id);
        if (ret==null) {
            try {
                ret = new Application(id);
                _Instances.put(ret.getAppPrefix(), ret);
                _InstancesByID.put(ret.getAppID(), ret);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            _InstancesByID.put(id, ret);
        }

        return ret;
    }

    public String getAppDir() { return _AppDir; }
    public String getAppPrefix() { return _AppPrefix; }
    public String getAppName() { return _AppName; }
    public int getAppID() { return _AppID; }
    public String getEmailFooter() { return _EmailFooter; }
    public String getSupportEmail() { return _SupportEmail; }
    public String getCompanyName() { return _CompanyName; }
    public LocalDateTime getLastCronHit() { return _LastCronHit; }

    public AppSettings getAppSettings() { return AppSettings.getInstance(_AppPrefix); }
    public Log getLogger() { return Log.getInstance(_AppPrefix); }
    public JDBCDatabase getQuickDB() {
        return JDBCDatabase.getInstance(getAppSettings().getDBConnectionString());
    }

    public int updateLastCronHit() {

        int retVal = -1;
        StringBuilder sql = new StringBuilder();

        // Create SQL statement
        sql.append(" UPDATE Application ");
        sql.append(" SET LastCronHit = now() ");
        sql.append(" WHERE ApplicationID = ").append(this._AppID);

        try {
            retVal = CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }

        return retVal;
    }

}
