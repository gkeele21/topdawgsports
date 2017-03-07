package bglib.util;

import java.util.*;
import org.apache.commons.lang.StringEscapeUtils;


public class ErrorMessage {
    private String _Message;
    private List<Object> _Args;

    public ErrorMessage(String msg) {
        _Message = FSUtils.noNull(msg);
        _Args = new ArrayList<Object>();
    }

    public ErrorMessage(String msg, Object...args) {
        _Message = FSUtils.noNull(msg);
        _Args = new ArrayList<Object>();
        for (Object a : args) {
            if (a instanceof String) {
                _Args.add(StringEscapeUtils.escapeHtml((String)a));
            } else {
                _Args.add(a);
            }
        }
    }

    public String getFormattedMessage() {
        String ret = EmailMessage.formatString(_Message.replaceAll("'", "''"), _Args);
//        ret = ret.trim().replaceAll("\n", "<br />&#160;&#160;");
//        ret = ret.trim().replaceAll("\n", "\n\r");
        return ret;
    }
}
