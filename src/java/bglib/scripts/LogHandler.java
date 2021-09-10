package bglib.scripts;

import bglib.util.AuDate;
import bglib.util.BGConstants;
import bglib.util.FSUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static bglib.util.Application._GLOBAL_LOG;
import static bglib.util.Application._GLOBAL_QUICK_DB;

public class LogHandler extends Handler {

    List<LogRecord> _LogRecords = new ArrayList<LogRecord>();
    int _ScriptRunID;

    public LogHandler(int scriptRunID) {
        _ScriptRunID = scriptRunID;
    }

    public void publish(LogRecord record) {
        if (record.getLevel().intValue() >= getLevel().intValue()) {
            _LogRecords.add(record);
        }
    }

    public void flush() {
        Connection con=null;

        try {
            if (_LogRecords==null) {
                return;
            }
            con = _GLOBAL_QUICK_DB.getConn();
            con.setAutoCommit(false);

            for (LogRecord lr : _LogRecords) {
                Throwable t = lr.getThrown();
                String stackTrace="";
                if (t!=null) {
                    stackTrace= FSUtils.fixupUserInputForDB(getStackTrace(t));
                }
                String sql = "insert into logentry (ScriptRunID, LogLevel, TimeOfLog, Message, Thrown) " +
                             "values (" + _ScriptRunID + ", '" + lr.getLevel().toString() + "', '" +
                             new AuDate(lr.getMillis()).toString(BGConstants.PLAYDATETIME_PATTERN) + "', '" +
                             FSUtils.fixupUserInputForDB(lr.getMessage()) + "', '" + stackTrace + "')";
                _GLOBAL_QUICK_DB.executeUpdate(sql);
            }
            con.commit();
            _LogRecords.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (con!=null) {
                    con.close();
                }
            }
            catch (SQLException e) {
                _GLOBAL_LOG.error(e);
            }
        }
    }

    public void close() {
        flush();
        _LogRecords=null;
    }

    private static String getStackTrace(Throwable t) {
        StringBuffer str;
        PrintWriter pw=null;
        try {
            str = new StringBuffer();
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw, true);
            t.printStackTrace(pw);

            str.append(sw.getBuffer());
        }
        finally {
            if (pw!=null) pw.close();
        }

        return str.toString();
    }
}

