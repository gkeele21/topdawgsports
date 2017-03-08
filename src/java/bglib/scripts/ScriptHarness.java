package bglib.scripts;

import static bglib.util.Application._GLOBAL_LOG;
import static bglib.util.Application._GLOBAL_QUICK_DB;
import bglib.util.AuDate;
import bglib.util.BGConstants;
import bglib.util.FSUtils;
import bglib.util.Application;
import sun.jdbc.rowset.CachedRowSet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.*;

public class ScriptHarness {

    public static final String DEFAULT_PACKAGE = "bglib.scripts";

    ScriptInfo _ScriptInfo;
    String[] _ScriptArgs;
    Logger _ScriptLogger;
    String _LogFileName;
    int _ScriptRunID;
    Harnessable _Script;
    AuDate _InitTime;
    AuDate _StartTime;
    AuDate _EndTime;
    ResultCode _ResultCode;
    int _LastReportedWarning;
    Application _App;

    public ScriptHarness(String appName, String scriptName, String[] scriptArgs) throws Exception {
        _App = Application.getInstance(appName);
        _ScriptInfo = new ScriptInfo(_App, scriptName);
        _ScriptArgs = scriptArgs;
    }

    public void chain(Logger logger) throws Exception {
        _ScriptLogger = logger;
        initHarnessable();
        doRunScript();
    }

    public void runScript() {
        try {
            if (_ScriptInfo.getRecordEachRun()) {
                addRunRecord();
            }
            initScriptLogger();
            initHarnessable();
            doRunScript();
        }
        finally {
            finish();
        }
    }

    private void finish() {
        flushHandlers();
        // - write result to scriptrun
        if (_ScriptInfo.getRecordEachRun()) {
            writeScriptRunResults();
        }
        // - send emails, if nec.
        emailScriptRunResults();
    }

    private void doRunScript() {
        _ResultCode = ResultCode.RC_INCOMPLETE;
        while (_ResultCode == ResultCode.RC_INCOMPLETE || _ResultCode == ResultCode.RC_INCOMPLETE_AND_FLUSH) {
            Thread scriptThread = new Thread(_Script);
            _StartTime = new AuDate();
            scriptThread.start();
            try {
                scriptThread.join();
                _EndTime = new AuDate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            _ResultCode = _Script.getResultCode();
            if (_ResultCode == ResultCode.RC_INCOMPLETE_AND_FLUSH) {
                sendOutWarnings();
            }
            if (_ResultCode == ResultCode.RC_INCOMPLETE || _ResultCode == ResultCode.RC_INCOMPLETE_AND_FLUSH) {
                try {
                    Thread.sleep(_ScriptInfo.getNextRunDelay());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initHarnessable() {
        try {
            String className = _ScriptInfo.getClassName();
            if (className.indexOf(".")<0) {
                className = _App.getAppDir() + ".scripts." + className;
            }
            Class theClass = Class.forName(className);
            _Script = (Harnessable)theClass.newInstance();
            _Script.setScriptArgs(_ScriptArgs);
            _Script.setLogger(_ScriptLogger);
        } catch (Exception e) {
            _GLOBAL_LOG.error(e);
        }
    }

    private void initScriptLogger() {
        _ScriptLogger = Logger.getLogger(_ScriptInfo.getScriptName());
        _ScriptLogger.setLevel(Level.ALL); // filter log messages at the Handler level, rather than the Logger level

        try {
            _InitTime = new AuDate();
            File logFile = new File(_ScriptInfo.getLogDir() + _ScriptInfo.getScriptName() + "/");
            logFile.mkdirs();
            _LogFileName = _ScriptInfo.getScriptName() + "." + _InitTime.toString("yyyyMMddHHmmss")+".txt";
            FileOutputStream fos = new FileOutputStream(logFile.getPath() + "\\" + _LogFileName);
            StreamHandler fileHandler = new StreamHandler(fos, new SimpleFormatter());
            fileHandler.setLevel(_ScriptInfo.getLogFileLogLevel());
            _ScriptLogger.addHandler(fileHandler);

            // get a FSAHandler
            if (_ScriptInfo.getRecordEachRun()) {
                Handler fsaHandler = new LogHandler(_ScriptRunID);
                fsaHandler.setLevel(_ScriptInfo.getDBLogLevel());
                _ScriptLogger.addHandler(fsaHandler);
            }
        }
        catch (Exception e) {
            _GLOBAL_LOG.error(e);
        }
    }

    public static void main(String[] args) {
        if (args.length<2) {
            System.out.println("Usage: java ScriptHarness AppName [ScriptName] <ScriptArg1...>");
            return;
        }

        String appName = args[0];

        String[] scriptArgs = new String[args.length-2];
        for (int i=0; i<scriptArgs.length; i++) {
            scriptArgs[i] = args[i+2];
        }

        try {
            ScriptHarness harness = new ScriptHarness(args[0], args[1], scriptArgs);
            harness.runScript();
        }
        catch (Exception e) {
            _GLOBAL_LOG.error(e);
        }
    }

    void writeScriptRunResults() {
        try {
            String sql = "update scriptrun set TimeStarted='" + _StartTime.toString(BGConstants.PLAYDATETIME_PATTERN) +
                         "', TimeEnded='" + _EndTime.toString(BGConstants.PLAYDATETIME_PATTERN) + "', ScriptArguments='" + argsToString(_ScriptArgs) +
                         "', LogFileName='" + _LogFileName + "', ResultCode=" + _ResultCode.getCode() + " where ScriptRunID = " + _ScriptRunID;
            _GLOBAL_QUICK_DB.executeUpdate(sql);
        }
        catch (Exception e) {
            _GLOBAL_LOG.error(e);
        }
    }

    static String argsToString(String[] args) {
        StringBuffer ret = new StringBuffer();
        for (int i=0; i<args.length; i++) {
            ret.append(args[i]);
            if (i<args.length-1) {
                ret.append("|");
            }
        }

        return ret.toString();
    }

    private void addRunRecord() {
        try {
            String sql = "insert into scriptrun (ScriptID) values (" + _ScriptInfo.getScriptID() + ")";
            _ScriptRunID = _GLOBAL_QUICK_DB.executeInsert(sql);
        }
        catch (Exception e) {
            _GLOBAL_LOG.error(e);
        }
    }

    private void flushFSAHandlers() {
        for (Handler handler : _ScriptLogger.getHandlers()) {
            if (handler instanceof LogHandler) {
                handler.flush();
            }
        }
    }

    private void sendOutWarnings() {
        try {
            flushFSAHandlers();
            Level highestScriptRunLevel=getHighestScriptRunLogLevel(_LastReportedWarning);
            if (highestScriptRunLevel.intValue() < Level.WARNING.intValue()) {
                return;
            }

            String sql = "select * from emailscriptresults r, employeeemail e " +
                         "where r.employeeemailid = e.employeeemailid and r.scriptid = " + _ScriptInfo.getScriptID();
            CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(sql);

            while (crs.next()) {
                boolean alwaysSend = crs.getBoolean("AlwaysEmail");
                Level emailSendLogLevel = Level.parse(crs.getString("MinimumLogLevel").toUpperCase());
                if (!alwaysSend) {
                    if (highestScriptRunLevel.intValue() < emailSendLogLevel.intValue()) {
                        continue;
                    }
                }

                // if we got to here, we're sending the email. Note that we send only Warning or higher here.
                sendEmailSummary(crs.getString("EmailAddress"), Level.WARNING, highestScriptRunLevel, crs.getString("AttachLogFile").equals("y"));
            }
            _LastReportedWarning = getLastWarningLogEntryID();
        }
        catch (Exception e) {
            _GLOBAL_LOG.error(e);
        }
    }

    private void emailScriptRunResults() {
        try {
            Level highestScriptRunLevel=getHighestScriptRunLogLevel(0);
            String sql = "select * from emailscriptresults r, employeeemail e " +
                         "where r.employeeemailid = e.employeeemailid and r.scriptid = " + _ScriptInfo.getScriptID();
            CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(sql);

            while (crs.next()) {
                boolean alwaysSend = crs.getBoolean("AlwaysEmail");
                Level emailSendLogLevel = Level.parse(crs.getString("MinimumLogLevel").toUpperCase());
                if (!alwaysSend) {
                    if (highestScriptRunLevel.intValue() < emailSendLogLevel.intValue()) {
                        continue;
                    }
                }

                // if we got to here, we're sending the email.
                sendEmailSummary(crs.getString("EmailAddress"), emailSendLogLevel, highestScriptRunLevel, crs.getString("AttachLogFile").equals("y"));
            }
        }
        catch (Exception e) {
            _GLOBAL_LOG.error(e);
        }
    }

    private void sendEmailSummary(String to, Level minLogLevel, Level highestScriptRunLevel,  boolean attachLogFile) {
        try {
            StringBuffer logMsgs = new StringBuffer();

            String sql = "select * from logentry " +
                         "where ScriptRunID = " + _ScriptRunID + " order by TimeOfLog";
            CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(sql);
            while (crs.next()) {
                if (minLogLevel.intValue() <= Level.parse(crs.getString("LogLevel").toUpperCase()).intValue()) {
                    logMsgs.append(crs.getString("TimeOfLog") + " " + crs.getString("LogLevel") + " " + crs.getString("Message") + "\n");
                    String thrown = crs.getString("Thrown");
                    if (thrown!=null && thrown.length()>0) {
                        logMsgs.append(thrown + "\n");
                    }
                }
            }

            String subject = "Script '" + _ScriptInfo.getScriptName() + "' has completed";
            if (highestScriptRunLevel!=null && highestScriptRunLevel.intValue() >= Level.WARNING.intValue()) {
                subject = "ATTENTION! " + subject + " with " + highestScriptRunLevel + " messages!";
            }

            StringBuffer body = new StringBuffer();
            body.append("Script '"  + _ScriptInfo.getScriptName() + "' has completed with Result Code '" + _ResultCode + "'\n\n");
            body.append("Time Started: " + _StartTime + "\n");
            body.append("Time Ended: " + _EndTime + "\n");
            body.append("Duration: " + calculateDuration(_StartTime, _EndTime) + "\n\n");
            String strArgs = argsToString(_ScriptArgs);
            body.append("Script Arguments: " + ((strArgs.length()>0) ? strArgs : "[None]") + "\n");
            body.append("Log File: " + _LogFileName + "\n\n");
            if (logMsgs.length()==0) {
                body.append("No log messages to report.");
            } else {
                body.append(logMsgs);
            }

            String attachment = (attachLogFile) ? (ScriptInfo.DEFAULT_LOG_DIR + _ScriptInfo.getScriptName() + "/" + _LogFileName) : null;
            FSUtils.sendMessageEmail(to, subject, body.toString(), attachment);
        }
        catch (Exception e) {
            _GLOBAL_LOG.error(e);
        }
    }

    private Level getHighestScriptRunLogLevel(int minLogEntryID) {
        Level ret = Level.FINEST; // start with min value

        try {
            String sql = "select max(LogEntryID) as maxid, LogLevel from logentry " +
                         "where ScriptRunID = " + _ScriptRunID + " group by LogLevel having max(LogEntryID) > " + minLogEntryID;
            CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(sql);
            while (crs.next()) {
                Level curLevel = Level.parse(crs.getString("LogLevel").toUpperCase());
                if (curLevel.intValue() > ret.intValue()) {
                    ret = curLevel;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    private int getLastWarningLogEntryID() {
        int ret=0;

        try {
            String sql = "select max(LogEntryID) as MaxID from logentry where ScriptRunID = " + _ScriptRunID +
                         " and (LogLevel = 'Warning' or LogLevel = 'Severe')";
            CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(sql);
            if (crs.next()) {
                ret = crs.getInt("MaxID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    private void flushHandlers() {
        Handler[] handlers = _ScriptLogger.getHandlers();

        for (Handler handler : handlers) {
            handler.flush();
            handler.close();
        }

        File logFile = new File(_ScriptInfo.getLogDir() + _ScriptInfo.getScriptName() + "/" + _LogFileName);
        if (logFile.length()==0) {
            logFile.delete();
        }
    }

    private static String calculateDuration(AuDate startTime, AuDate endTime) {
        if (startTime==null || endTime==null) return null;

        long length = endTime.getDateInMillis() - startTime.getDateInMillis();

        int dayMS = 24*60*60*1000;
        long days = (int)(length / dayMS);
        length -= (days*dayMS);
        int hourMS = 60*60*1000;
        long hours = (int)(length / hourMS);
        length -= (hours*hourMS);
        int minuteMS = 60*1000;
        long minutes = (int)(length / minuteMS);
        length -= (minutes*minuteMS);
        long seconds = (int)(length / 1000);

        String ret = ((days > 0) ? "" + days + " days " : "") +
                     ((hours > 0) ? "" + hours + " hours " : "") +
                     ((minutes > 0) ? "" + minutes + " minutes " : "") + seconds + " seconds";

        return ret;
    }
}

