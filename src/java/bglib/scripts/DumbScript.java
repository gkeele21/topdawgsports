package bglib.scripts;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DumbScript implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;

    public DumbScript() {
        _Logger = Logger.global;
    }

    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    public ResultCode getResultCode() { return _ResultCode; }

    public void setScriptArgs(String[] args) {}

    public void run() {
        int total = 0;

        for (int i=0; i<100; i++) {
            total += (i+1);
            _Logger.log(Level.INFO, "i=" + i + ", total so far: " + total);
            if (i==50) {
                try {
                    String blah=null;
                    blah += blah.substring(5);
                }
                catch (Exception e) {
                    _Logger.log(Level.INFO, "Something bad happenededed", e);
                    break;
                }
            }
        }

        _ResultCode = ResultCode.RC_SUCCESS;
    }

}

