package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.AppSettings;
import bglib.util.Application;
import bglib.util.Log;
import javax.servlet.http.HttpSession;
import tds.data.CTDataSource;
import tds.data.CTSQLDataSource;

public class CTApplication {

    public static final String APP_PREFIX = "tds";
    public static final String APP_DIR = "topdawgsports";
    public static final String TBL_PREF = "";
    public static final String GLOBALS_TBL_PREF = "";
    public static final String TEMP_TBL_PREF = "topdawg.";
    public static final boolean DISPLAYQUERIES = false;

    public static final Application _CT_APP = Application.getInstance(APP_PREFIX);
    public static final AppSettings _CT_APP_SETTINGS = _CT_APP.getAppSettings();
    public static final Log _CT_LOG = Log.getInstance(APP_PREFIX);

    public static CTDataSource _CT_DB;
    public static JDBCDatabase _CT_QUICK_DB;

    static {
        try {
            String connectionString = _CT_APP_SETTINGS.getProperty(AppSettings.DB_CONNECTIONSTRING_PROP, "default");
            System.out.println("In CTApplication - using connection string : " + connectionString);
            CTSQLDataSource defaultInstance = CTSQLDataSource.getInstance(connectionString);
            _CT_DB = defaultInstance;
            _CT_QUICK_DB = defaultInstance.getJDBCDatabase();
        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    private CTApplication() {}

    public static String getWebappDir(HttpSession session) {
        String indexPath = session.getServletContext().getRealPath("index.jsp");
        return indexPath.substring(0, indexPath.indexOf("index.jsp"));
    }
}