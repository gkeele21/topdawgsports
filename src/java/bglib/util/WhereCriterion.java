package bglib.util;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Apr 24, 2006
 * Time: 11:39:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class WhereCriterion {
    public String _FieldName, _Operator, _Value;

    public WhereCriterion(String fieldName, String operator, String value) {
        _FieldName = fieldName;
        _Operator = operator;
        _Value = value;
    }

    public String toString() {
        return _FieldName + " " + _Operator + " " + _Value;
    }
}
