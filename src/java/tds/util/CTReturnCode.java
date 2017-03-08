package tds.util;

import bglib.util.ReturnCode;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bhansen
 * Date: Jan 19, 2006
 * Time: 2:55:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CTReturnCode implements ReturnCode {

    // Common return codes. Not Singletons!
    public static final CTReturnCode RC_SUCCESS = new CTReturnCode(CTReturnType.SUCCESS);
    public static final CTReturnCode RC_UNKNOWN = new CTReturnCode(CTReturnType.UNKNOWN);
    public static final CTReturnCode RC_DB_ERROR = new CTReturnCode(CTReturnType.DB_ERROR);

    CTReturnType _ReturnType;
    List _Params;
    Object _Value;
    Exception _Exception;

    public CTReturnCode(CTReturnType returnType) {
        _ReturnType = returnType;
    }

    public CTReturnCode(CTReturnType returnType, Object value) {
        _ReturnType = returnType;
        _Value = value;
    }

    public CTReturnCode(CTReturnType returnType, Exception exception) {
        _ReturnType = returnType;
        _Exception = exception;
    }

    public CTReturnCode(CTReturnType returnType, Object value, List params) {
        _ReturnType = returnType;
        _Params = params;
        _Value = value;
    }

    public CTReturnType getReturnType() { return _ReturnType; }

    public Object getValue() { return _Value; }

    public Exception getException() { return _Exception; }

    public List getParams() { return _Params; }

    public boolean isSuccess() { return _ReturnType==CTReturnType.SUCCESS || _ReturnType==CTReturnType.SUCCESS_WITH_WARNINGS; }

    public String toString() { return _ReturnType.toString(); }
}
