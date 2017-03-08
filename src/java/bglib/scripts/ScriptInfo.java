package bglib.scripts;

import static bglib.util.Application._GLOBAL_QUICK_DB;
import bglib.util.Application;
import sun.jdbc.rowset.CachedRowSet;

import java.util.logging.Level;

public class ScriptInfo {

    public static final String DEFAULT_LOG_DIR = "c:/fas/log/";
    public static final int DEFAULT_RUN_DELAY = 100000;

    private Application _App;
    private String _ScriptName;
    private String _ClassName;
    private Level _LogFileLogLevel;
    private Level _DBLogLevel;
    private String _LogPath;
    private int _ScriptID;
    private int _NextRunDelay;
    private boolean _RecordEachRun;

    public ScriptInfo(Application app, String scriptName) throws Exception {
        _App = app;
        _ScriptName = scriptName;
        initFromDB(app, scriptName);
    }

    private void initFromDB(Application app, String scriptName) throws Exception {
        String sql = "select * from script where ApplicationID = " + app.getAppID() + " and scriptname='" + scriptName + "'";

        CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(sql);
        if (crs.next()) {
            _ClassName = crs.getString("ClassName");
            _LogFileLogLevel = Level.parse(crs.getString("LogFileLogLevel").toUpperCase());
            _DBLogLevel = Level.parse(crs.getString("DBLogLevel").toUpperCase());
            _ScriptID = crs.getInt("ScriptID");
            _LogPath = crs.getString("LogPath");
            _NextRunDelay = crs.getInt("NextRunDelay");
            _RecordEachRun = crs.getBoolean("RecordEachRun");
        }
    }

    public String getScriptName() { return _ScriptName; }

    public String getClassName() { return _ClassName; }

    public Level getLogFileLogLevel() { return _LogFileLogLevel; }

    public Level getDBLogLevel() { return _DBLogLevel; }

    public String getLogDir() { return (_LogPath==null) ? "c:/fas/" + _App.getAppDir() + "/log/" : _LogPath; }

    public int getScriptID() { return _ScriptID; }

    public int getNextRunDelay() { return (_NextRunDelay>0) ? _NextRunDelay : DEFAULT_RUN_DELAY; }

    public boolean getRecordEachRun() { return _RecordEachRun; }

}

