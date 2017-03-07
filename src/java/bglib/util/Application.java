package bglib.util;

import bglib.data.JDBCDatabase;
import java.util.HashMap;
import java.util.Map;
import sun.jdbc.rowset.CachedRowSet;

public class Application {

    public static final String BG_ROOT_GRANT = "c:/Grant/consulting/TopDawgSports";
    public static final String BG_ROOT = "/topdawg";
    public static final String BG_ROOT_BERT = "C:/Bert/Programs/Topdawg/";
    public static final String GLOBAL_STR = "Global";

    private static final Map<String, Application> _Instances = new HashMap<String, Application>();
    private static final Map<Integer, Application> _InstancesByID = new HashMap<Integer, Application>();

    public static final AppSettings _GLOBAL_SETTINGS = AppSettings.getInstance(GLOBAL_STR);
    public static final Log _GLOBAL_LOG = Log.getInstance(GLOBAL_STR);

    static {
        _GLOBAL_QUICK_DB = JDBCDatabase.getInstance(_GLOBAL_SETTINGS.getProperty(AppSettings.DB_CONNECTIONSTRING_PROP));
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

    private Application(String appPrefix) throws Exception {
        if (!appPrefix.equals(GLOBAL_STR)) {
            String query = "select * from Application where ApplicationPrefix = '" + appPrefix + "'";
            System.out.println("Query : " + query);
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

    public AppSettings getAppSettings() { return AppSettings.getInstance(_AppPrefix); }
    public Log getLogger() { return Log.getInstance(_AppPrefix); }
    public JDBCDatabase getQuickDB() { return JDBCDatabase.getInstance(getAppSettings().getProperty(AppSettings.DB_CONNECTIONSTRING_PROP, "default")); }
}