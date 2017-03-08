package bglib.scripts;

public class ResultCode {

    // static instances
    public static final ResultCode RC_SUCCESS=new ResultCode(0);
    public static final ResultCode RC_ERROR=new ResultCode(1);
    public static final ResultCode RC_INCOMPLETE=new ResultCode(2);
    public static final ResultCode RC_INCOMPLETE_AND_FLUSH=new ResultCode(3);

    private int _Code;

    private ResultCode(int val) {
        _Code=val;
    }

    public int getCode() { return _Code; }

    public String toString() {
        if (_Code==RC_SUCCESS.getCode()) return "Success";
        if (_Code==RC_ERROR.getCode()) return "Error";
        if (_Code==RC_INCOMPLETE.getCode()) return "Incomplete";
        if (_Code==RC_INCOMPLETE_AND_FLUSH.getCode()) return "Incomplete and Flush";

        return "Unknown";
    }
}
