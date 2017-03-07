package bglib.util;

import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.Serializable;

public class FormField implements Serializable, Comparable {
    public static final String NEWLINE = "\n";
    public static FormField DEFAULT_FIELD = new FormField("default");

    private String _HtmlName;
    private String _FriendlyName;
    private int _FieldNo;
    private int _MaxValue, _MinValue;
    private boolean _NonEscapedCharsOnly=true;
    private FormFieldType _Type = FormFieldType.STRING;
    private List<String> _DateFormats = new ArrayList<String>();

    public static class Builder {
        private static int _NextFieldNo=0;
        private String _HtmlName;
        private String _FriendlyName;
        private int _FieldNo;
        private int _MaxValue, _MinValue;
        private boolean _NonEscapedCharsOnly=true;
        private FormFieldType _Type = FormFieldType.STRING;
        private List<String> _DateFormats = new ArrayList<String>();

        public Builder(String htmlName) {
            _HtmlName = htmlName;
            _FriendlyName = htmlName;
            _FieldNo = ++_NextFieldNo;
        }

        public String getHtmlName() { return _HtmlName; }
        public String getFriendlyName() { return _FriendlyName; }
        public int getFieldNo() { return _FieldNo; }
        public int getMaxValue() { return _MaxValue; }
        public int getMinValue() { return _MinValue; }
        public boolean getNonEscapedCharsOnly() { return _NonEscapedCharsOnly; }
        public FormFieldType getType() { return _Type; }
        public List<String> getDateFormats() { return _DateFormats; }

        public Builder friendlyName(String s) {
            _FriendlyName=s;
            return this;
        }

        public Builder fieldNo(int n) {
            _FieldNo=n;
            return this;
        }

        public Builder maxValue(int n) {
            _MaxValue=n;
            return this;
        }

        public Builder minValue(int n) {
            _MinValue=n;
            return this;
        }

        public Builder nonEscapedCharsOnly(boolean b) {
            _NonEscapedCharsOnly=b;
            return this;
        }

        public Builder type(FormFieldType t) {
            _Type=t;
            return this;
        }

        public Builder dateFormats(List<String> df) {
            _DateFormats.addAll(df);
            return this;
        }

        public FormField build() {
            return new FormField(this);
        }
    }

    private FormField(Builder builder) {
        _FieldNo = builder.getFieldNo();
        _HtmlName = builder.getHtmlName();
        _FriendlyName = builder.getFriendlyName();
        _MaxValue = builder.getMaxValue();
        _MinValue = builder.getMinValue();
        _Type = builder.getType();
        _DateFormats.addAll(builder.getDateFormats());
    }

    public FormField(String htmlName) {
        _HtmlName = htmlName;
    }

    public FormField(int fieldNo, String friendlyName, int minLength, int maxLength) {
        _FieldNo = fieldNo;
        _FriendlyName = friendlyName;
        _HtmlName = friendlyName;
        _MaxValue = maxLength;
        _MinValue = minLength;
    }

    public FormField(int fieldNo, String htmlName, String friendlyName, int minLength, int maxLength) {
        _FieldNo = fieldNo;
        _HtmlName = htmlName;
        _FriendlyName = friendlyName;
        _MaxValue = maxLength;
        _MinValue = minLength;
    }

    public FormField(int fieldNo, String htmlName, String friendlyName, FormFieldType type) {
        _FieldNo = fieldNo;
        _HtmlName = htmlName;
        _FriendlyName = friendlyName;
        _MaxValue = Integer.MAX_VALUE;
        _MinValue = Integer.MIN_VALUE;
        _Type = type;
    }

    public FormField(int fieldNo, String htmlName, String friendlyName, int minLength, int maxLength, FormFieldType type) {
        _FieldNo = fieldNo;
        _HtmlName = htmlName;
        _FriendlyName = friendlyName;
        _MaxValue = maxLength;
        _MinValue = minLength;
        _Type = type;
    }

    public FormField(int fieldNo, String htmlName, String friendlyName, int minLength, FormFieldType type, List<String> dateFormats) {
        _FieldNo = fieldNo;
        _HtmlName = htmlName;
        _FriendlyName = friendlyName;
        _MinValue = minLength;
        _Type = type;
        _DateFormats = new ArrayList<String>(dateFormats);
    }

    public FormField(int fieldNo, String htmlName, String friendlyName, int minLength, int maxLength, boolean nonEscapedCharsOnly) {
        _FieldNo = fieldNo;
        _HtmlName = htmlName;
        _FriendlyName = friendlyName;
        _MaxValue = maxLength;
        _MinValue = minLength;
        _NonEscapedCharsOnly = nonEscapedCharsOnly;
    }

    public FormField(int fieldNo, String htmlName, String friendlyName, int minLength, int maxLength, boolean nonEscapedCharsOnly, FormFieldType type) {
        _FieldNo = fieldNo;
        _HtmlName = htmlName;
        _FriendlyName = friendlyName;
        _MaxValue = maxLength;
        _MinValue = minLength;
        _NonEscapedCharsOnly = nonEscapedCharsOnly;
        _Type = type;
    }

    public boolean isValid(String val, StringBuffer emsg) {
        return isValid(val, emsg, true);
    }

    public boolean isValid(String val, StringBuffer emsg, boolean checkConstraints) {
        FormError error = new FormError();
        boolean ret = isValid(val, error, checkConstraints);
        emsg.append(error.getCompositeErrorMessage());
        return ret;
    }

    public boolean isValid(String val, FormError error) {
        return isValid(val, error, true);
    }

    public boolean isValid(String val, FormError error, boolean checkConstraints) {
        val = (val==null) ? "" : val;

        switch (_Type) {
            case STRING:
                return validateString(val, error);
            case DATE:
                return validateDate(val, error);
            case INT:
            case NULLABLE_INT:
                return validateInt(val, error, checkConstraints);
            case DOUBLE:
            case NULLABLE_DOUBLE:
                return validateDouble(val, error, checkConstraints);
            default:
                return false;
        }
    }

    private boolean validateString(String val, FormError error) {
        if (val.length() < _MinValue) {
            if (_MinValue ==1) {
                error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' is required."));
            } else {
                error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' must be at least " + _MinValue + " characters long."));
            }
            return false;
        }

        if (_MaxValue > 0 && val.length() > _MaxValue) {
            error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' has a maximum length of " + _MaxValue + " characters."));
            return false;
        }

        if (_NonEscapedCharsOnly) {
            if (val.equals(StringEscapeUtils.escapeHtml(val))==false) {
                error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' contains illegal characters."));
                return false;
            }
        }

        return true;
    }

    private boolean validateDate(String val, FormError error) {
        if (val.length()==0 && _MinValue == 0) {
            return true;
        }
        if (_DateFormats.size()==0) {
            _DateFormats.add(BGConstants.PLAYDATE_PATTERN);
            _DateFormats.add("M/d/yyyy");
        }

        for (String format : _DateFormats) {
            AuDate test = new AuDate(val, format);
            if (test.isNull()==false && test.get(Calendar.YEAR) >= 1900 && test.get(Calendar.YEAR) < 2500) {
                return true;
            }
        }

        StringBuffer emsg = new StringBuffer();
        emsg.append("Error: Field '" + _FriendlyName + "' is not in a valid date format. Example of valid format: ");
        AuDate now = new AuDate();
        for (String format: _DateFormats) {
            emsg.append("'" + now.toString(format) + "'; ");
        }
        emsg.delete(emsg.length()-2, emsg.length());
        error.add(this, new ErrorMessage(emsg.toString()));

        return false;
    }

    private boolean validateInt(String val, FormError error, boolean checkConstraints) {
        if (_Type == FormFieldType.NULLABLE_INT && val.trim().length()==0) {
            return true;
        }
        if (val.trim().length()==0) {
            error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' is required."));
            return false;
        }

        try {
            int num = Integer.parseInt(val);
            if (checkConstraints && (num > _MaxValue || num < _MinValue)) {
                if (_MinValue==1) {
                    error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' is required."));
                } else if (_MaxValue==Integer.MAX_VALUE) {
                    error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' must be greater than or equal to " + _MinValue + "."));
                } else {
                    error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' must be between " + _MinValue + " and " + _MaxValue + "."));
                }
                return false;
            }
        } catch (NumberFormatException e) {
            error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' is required to be an integer value."));
            return false;
        }

        return true;
    }

    private boolean validateDouble(String val, FormError error, boolean checkConstraints) {
        if ((!checkConstraints || _Type == FormFieldType.NULLABLE_DOUBLE) && val.trim().length()==0) {
            return true;
        }
        if (checkConstraints && val.trim().length()==0) {
            error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' is required."));
            return false;
        }

        try {
            val = val.replace("$", "").replaceAll(",", "");
            double num = Double.parseDouble(val);
            if (checkConstraints && (num > _MaxValue || num < _MinValue)) {
                if (_MinValue==1) {
                    error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' is required."));
                } else if (_MaxValue==Double.MAX_VALUE) {
                    error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' must be greater than " + _MinValue + "."));
                } else {
                    error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' must be between " + _MinValue + " and " + _MaxValue + "."));
                }
                return false;
            }
        } catch (NumberFormatException e) {
            error.add(this, new ErrorMessage("Error: Field '" + _FriendlyName + "' is required to be a number."));
            return false;
        }

        return true;
    }

    public String getFriendlyName() { return _FriendlyName; }

    public String getHtmlName() { return _HtmlName; }

    public FormFieldType getType() { return _Type; }

    public int getMinValue() { return _MinValue; }

    public int getMaxValue() { return _MaxValue; }

    public int compareTo(Object f) {
        return _FieldNo - ((FormField)f)._FieldNo;
    }

    // returns a Map with all FormFields and the values submitted via the request
    public static Map<FormField, String> getAndValidateFormFields(HttpServletRequest request, List<FormField> fields, StringBuffer emsg) {
        Map<FormField, String> ret = new HashMap<FormField, String>();
        FormError error = new FormError();
        for (FormField field : fields) {
            String val = FSUtils.getRequestParameter(request, field.getHtmlName());
            if (field.getType().isNumber()) {
                val = val.replace("$", "").replaceAll(",", "");
            }
            ret.put(field, val);
            field.isValid(val, error); // ignore output; calling function determines failure if emsg.length() > 0
        }

        emsg.append(error.getCompositeErrorMessage());

        return ret;
    }

    // returns a Map with all FormFields and the values submitted via the request
    public static SortedMap<FormField, String> getAndValidateFormFields(HttpServletRequest request, List<FormField> fields, FormError error) {
        SortedMap<FormField, String> ret = new TreeMap<FormField, String>();
        for (FormField field : fields) {
            String val = FSUtils.getRequestParameter(request, field.getHtmlName());
            if (field.getType().isNumber()) {
                val = val.replace("$", "").replaceAll(",", "");
            }
            ret.put(field, val);
            field.isValid(val, error); // ignore output; calling function determines failure if emsg.length() > 0
        }

        return ret;
    }

    public String toString() { return _HtmlName; }

    public boolean equals(Object other) {
        if (other==null || other instanceof FormField == false) {
            return false;
        }
        return ((FormField)other).getHtmlName().equals(getHtmlName());
    }

    public int hashCode() {
        return (getHtmlName()==null) ? 0 : getHtmlName().hashCode();
    }

}

