package bglib.util;

import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

import bglib.control.BGServlet;

public class Log {

    public static final String EXCEPTIONS_LOG = "exceptions.log";

    public static final String LOG_LEVEL_ALL = "all";
    public static final String LOG_LEVEL_DB = "db";
    public static final String LOG_LEVEL_EXCEPTIONS = "exceptions";

    private String _LogFile;
    private Level _LogLevel;
    private Level _DBLogLevel;
    private List<String> _EmailToList;
    private Level _EmailLogLevel;
    private Application _Application;

    private static final Map<String, Log> _Instances = new HashMap<String, Log>();

    private Log(String appPrefix) {
        _Application = Application.getInstance(appPrefix);
        initSettings();
    }

    public static Log getInstance(String appPrefix) {
        Log ret=_Instances.get(appPrefix);
        if (ret==null) {
            ret = new Log(appPrefix);
        }

        return ret;
    }

    private void initSettings() {
        AppSettings appSettings = _Application.getAppSettings();
        String appHomeDir=appSettings.getProperty(AppSettings.APP_HOME_DIR_NAME_PROP, _Application.getAppDir());
        String root = appSettings.getAppRoot();
        String logDir= root + File.separator + appHomeDir + File.separator + "log";
        File file = new File(logDir);
        file.mkdirs();
        _LogFile = logDir + File.separator + EXCEPTIONS_LOG;

        try {
            _EmailLogLevel=Level.parse(appSettings.getProperty(AppSettings.LOG_EMAIL_LOG_LEVEL_PROP, Level.SEVERE.toString()).toUpperCase());
            _LogLevel = Level.parse(appSettings.getProperty(AppSettings.LOG_LEVEL_PROP, Level.SEVERE.toString()).toUpperCase());
            _DBLogLevel = Level.parse(appSettings.getProperty(AppSettings.LOG_DB_LOG_LEVEL_PROP, Level.SEVERE.toString()).toUpperCase());
            _EmailToList = new ArrayList<String>();
            String email = appSettings.getProperty(AppSettings.EMAIL_EXCEPTIONS_PROP);
            if (email != null && email.trim().length() > 0) {
                StringTokenizer st = new StringTokenizer(email, ",");
                while (st.hasMoreTokens()) {
                    _EmailToList.add(st.nextToken().trim());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void error(HttpSession session, HttpServletRequest request, Throwable e, boolean dbError) {
        try {
            Level level = Level.SEVERE; // errors get a Severe level
            if (e==null) {
                e = new Exception("Unknown error occurred"); // create a new exception so we can get the stack trace
            }

            StringBuffer str = new StringBuffer();

            str.append(_Application.getAppPrefix() + " exception: " + e.toString() + "\n");

            try {
                if (session!=null) {
                    str.append("Session ID: " + session.getId() + "\n");
                    str.append("User: " + session.getAttribute(BGServlet.VALID_USER_ATTR) + "\n");
                }
                if (request!=null) {
                    str.append("Request URL: " + request.getRequestURL() + "\n");
                    str.append("Remote Address: " + request.getRemoteAddr() + "\n");
                    str.append("Remote Host: " + request.getRemoteHost() + "\n");
                    str.append("Request Session ID: " + request.getRequestedSessionId() + "\n");
                }
            }
            catch (Exception e1) {
                 //ignore; keep going
            }

            str.append("**********************************\r\n");
            if (dbError) {
                str.append("DB ERROR\r\n");
            }
            str.append(new java.util.Date().toString()+"\r\n");

            if (e instanceof SQLException) {
                SQLException se = (SQLException)e;
                str.append("SQLState: " + se.getSQLState() + "\r\n");
                str.append("Error Code: " + se.getErrorCode());
            }

            StringWriter sw;
            PrintWriter pw=null;
            try {
                sw = new StringWriter();
                pw = new PrintWriter(sw, true);
                e.printStackTrace(pw);
                str.append(sw.getBuffer());
            }
            finally {
                if (pw!=null) {
                    pw.close();
                }
            }

            // dump extra information at the end for production environments
            if (!Application._GLOBAL_SETTINGS.getProperty(AppSettings.ENV_TEST, "false").equals("true")) {
                if (request!=null) {
                    str.append("\n\n" + FSUtils.dumpRequest(request));
                }
                if (session!=null) {
                    Enumeration attrNames = session.getAttributeNames();
                    str.append("\n***Session Attributes***\n");
                    while (attrNames.hasMoreElements()) {
                        String attr = attrNames.nextElement().toString();
                        str.append(attr + " = " + session.getAttribute(attr) + "\n");
                    }
                }
            }

            logMessage(level, str.toString());
            if (!dbError && level.intValue() >= _DBLogLevel.intValue()) {
                writeToDB(level, str.toString(), e); 
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void logMessage(Level level, String str) {
        try {
            if (level.intValue() >= _LogLevel.intValue()) {
                // write to log file
                FileWriter fw = new FileWriter(_LogFile, true);
                fw.write(str);
                fw.close();

                System.out.println(str);
            }

            if (level.intValue() >= _EmailLogLevel.intValue()) {
                sendEmail(str);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToDB(Level level, String str, Throwable exception) {
        try {
            Thread t = new Thread(new RecordToDB(_Application, level, str, exception));
            t.start();
        } catch (Exception e) {
            dbError(e);
        }
    }

    public void dbError(Throwable e) {
        error(null, null, e, true);
    }

    public void error(Throwable e) {
        error(null, null, new Exception(e), false);
    }

    public void error(HttpSession session, Throwable e) {
        error(session, null, e, false);
    }

    public void error(HttpServletRequest request, Throwable e) {
        error(request.getSession(), request, e, false);
    }

    private void sendEmail(String body) {
        Map values = new HashMap();
        values.put("from", _Application.getAppPrefix());
        List toList = new ArrayList(_EmailToList); // have to make a copy because emailToList is a static var
        values.put("toList", toList);
        values.put("body", body);
        values.put("subject", body.substring(0, body.indexOf("\n")));
//        SendEmail.sendEmail(values);
    }

}

class RecordToDB implements Runnable {
    Application app; Level level; String str; Throwable exception;

    RecordToDB(Application app, Level level, String str, Throwable exception) {
        this.app=app; this.level=level; this.str=str; this.exception=exception;
    }

    public void run() {
        try {
            Application._GLOBAL_QUICK_DB.executeInsert("insert into LogEntry(ApplicationID, LogLevel, TimeOfLog," +
                    "Message, Thrown) values (" + app.getAppID() + ", '" + level + "', '" +
                    new AuDate().toString(BGConstants.PLAYDATETIME_PATTERN) + "', '" +
                    StringEscapeUtils.escapeSql(str) + "', '" + StringEscapeUtils.escapeSql(exception.toString()) + "')");
        } catch (Exception e) {
            Application._GLOBAL_LOG.dbError(e);
        }
    }
}