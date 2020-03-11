package bglib.util;

import static bglib.util.Application._GLOBAL_LOG;
import java.io.FileInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

/**
 * This class is for global application settings that can't be stored in the database (e.g., the db connection
 * string, and info for the Logger (because we want to log errors when we can't reach the db). All other app
 * settings should be stored in the Globals table in the db and accessed via the __Globals class.
 */
public class AppSettings
{
//    private static final String WEBAPP_PROPERTIES = "tdssettings.txt";
    private static final String linuxHomeDir = "/";

    // properties
//    public static final String LOG_LEVEL_PROP = "Log.LogLevel";
//    public static final String EMAIL_EXCEPTIONS_PROP = "Log.EmailExceptionsTo";
//    public static final String LOG_EMAIL_LOG_LEVEL_PROP = "Log.EmailLogLevel";
//    public static final String LOG_DB_LOG_LEVEL_PROP = "Log.DBLogLevel";
//    public static final String EMAIL_ONLY_TO_PROP = "Email.EmailOnlyTo";
//    public static final String EMAIL_HOST_PROP = "Email.Host";
//    public static final String EMAIL_USER_PROP = "Email.User";
//    public static final String EMAIL_PASSWORD_PROP = "Email.Password";
//    public static final String EMAIL_FROM_PROP = "Email.From";
//    public static final String EMAIL_FROMPERSONAL_PROP = "Email.FromPersonal";
//    public static final String EMAIL_DEFAULT_RECIPIENT = "Email.Default.Recipient";
//    public static final String EMAIL_CONTACT_US = "Email.ContactUs.Recipient";
//    public static final String EMAIL_NEW_EMPLOYEE = "Email.NewEmployee.Recipient";
//    public static final String EMAIL_NEW_CUSTOMER = "Email.NewCustomer.Recipient";
//    public static final String EMAIL_TERMINATED_EMPLOYEE = "Email.TerminatedEmployee.Recipient";
//    public static final String DB_CONNECTIONSTRING_PROP = "Database.ConnectionString";
//    public static final String APP_HOME_DIR_NAME_PROP = "Application.HomeDirName";
//    public static final String APP_LEADS_DIR = "Application.LeadsDir";
//    public static final String APP_PREFIX_PROP = "Application.Prefix";
//    public static final String ENV_TEST = "Env.Test";
//    public static final String CLASS_CACHE = "Class.Cache";
//    public static final String CC_API_LOGINID = "CC.ApiLoginID";
//    public static final String CC_TRANSACTION_KEY = "CC.TransactionKey";
//    public static final String CC_TEST_MODE = "CC.TestMode";
    public static final String DOMAIN_URL = "DOMAIN_URL";
    public static final String PYTHON_PATH = "PYTHON_PATH";
    public static final String PYTHON_COMMAND = "PYTHON_COMMAND";
    public static final String LOG_DIR = "LOG_DIR";

    // Global app-wide settings var
    private Properties _Properties = new Properties();
    private String _AppPrefix;
    private String _ConnectionString = "";
    private static final Map<String, AppSettings> _Instances = new HashMap<String, AppSettings>();

    private AppSettings(String appPrefix) {
        _AppPrefix=appPrefix;
//        initSettings();
    }

    public static AppSettings getInstance(String appPrefix) {
        AppSettings instance = _Instances.get(appPrefix);
        if (instance==null) {
            instance = new AppSettings(appPrefix);
            _Instances.put(appPrefix, instance);
        }

        return instance;
    }
    
//    public String getAppRoot()
//    {
//        String root = System.getenv("TOPDAWG_HOME");
//        if (AuUtil.isEmpty(root)) {
//            root = "/home/gkeele/topdawg";
//        }
//        
////        String root = Application.BG_ROOT;
////        File f = new File(root);
////        if (!f.exists())
////        {
////            root = Application.BG_ROOT_GRANT;
////            f = new File(root);
////            if (!f.exists())
////            {
////                root = Application.BG_ROOT_BERT;
////            }
////        }
//        return root;
//    }

//    private void initSettings() {
//        String root = getAppRoot();
//        
//        String propsFile = root + File.separator + WEBAPP_PROPERTIES;
//
//        // load the properties
//        FileInputStream sf=null;
//        try {
//            sf = new FileInputStream(propsFile);
//            _Properties.load(sf);
//        }
//        catch (Exception e) {
//            propsFile = linuxHomeDir + WEBAPP_PROPERTIES;
//
//            try {
//                sf = new FileInputStream(propsFile);
//                _Properties.load(sf);
//            } catch (Exception e2) {
//                throw new RuntimeException(e);
//            }
//        } finally {
//            try {
//                if (sf!=null) {
//                    sf.close();
//                }
//            } catch (Exception io) {
//                _GLOBAL_LOG.error(io);
//            }
//        }
//    }
    
    public String getDBConnectionString() {
        if (_ConnectionString.equals("")) {
            String domain = System.getenv("TOPDAWG_DB_DOMAIN");
            String port = System.getenv("TOPDAWG_DB_PORT");
            String username = System.getenv("TOPDAWG_DB_USERNAME");
            String password = System.getenv("TOPDAWG_DB_PASSWORD");
            String dbname = System.getenv("TOPDAWG_DB_NAME");

            _ConnectionString = "jdbc:mysql://" + domain + ":" + port + "/" + dbname + "?user=" + username + "&password=" + password + "&autoReconnect=true";            
        }
        
        return _ConnectionString;
    }

    public String getProperty(String prop) {
        return getProperty(prop, null);
    }

    public String getProperty(String prop, String defaultValue) {
//        String val = _Properties.getProperty(_AppPrefix + "." + prop);
//        if (FSUtils.isEmpty(val)) {
//            val = _Properties.getProperty(Application.GLOBAL_STR + "." + prop);
//            if (FSUtils.isEmpty(val)) {
//                val = _Properties.getProperty(prop, defaultValue);
//            }
//        }
//
        String val = System.getenv(prop);
        if (FSUtils.isEmpty(val)) {
            val = defaultValue;
        }
        return (val==null) ? "" : val;
    }
}
