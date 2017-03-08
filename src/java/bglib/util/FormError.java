package bglib.util;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;

public class FormError {
    private Map<FormField, ErrorMessage> _Errors = new HashMap<FormField, ErrorMessage>();

    public FormError() {
    }

    public void add(FormField field, ErrorMessage msg) {
        _Errors.put(field, msg);
    }

    public void add(FormField field, String msg) {
        _Errors.put(field, new ErrorMessage(msg));
    }

    public void add(FormField field) {
        _Errors.put(field, new ErrorMessage(""));
    }

    public void add(String msg) {
        ErrorMessage emsg = getErrorMessage(FormField.DEFAULT_FIELD);
        if (emsg!=null) {
            emsg = new ErrorMessage(emsg.getFormattedMessage() + "\n" + msg);
        } else {
            emsg = new ErrorMessage(msg);
        }
        add(FormField.DEFAULT_FIELD, emsg);
    }

    public int getErrorCount() {
        return _Errors.keySet().size();
    }

    public Map<FormField, ErrorMessage> getErrorMap() { return _Errors; }

    public ErrorMessage getErrorMessage(FormField field) {
        return _Errors.get(field);
    }

    public String getCompositeErrorMessage() {
        StringBuffer emsg = new StringBuffer();

        List<FormField> sorted = new ArrayList<FormField>(getErrorMap().keySet());
        Collections.sort(sorted);
        for (FormField f : sorted) {
            String msg = _Errors.get(f).getFormattedMessage();
            if (msg!=null && msg.length()>0) {
                emsg.append(_Errors.get(f).getFormattedMessage() + "\n");
            }
        }
        return emsg.toString();
    }
}
