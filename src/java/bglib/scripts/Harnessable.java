package bglib.scripts;

import java.util.logging.Logger;

public interface Harnessable extends Runnable {

    void setScriptArgs(String[] args);

    void setLogger(Logger logger);

    ResultCode getResultCode();
}
