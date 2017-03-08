package bglib.util;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Used in conjunction with the ErrorMessage class
 */
public class AnchorArg {
    private String _Body;
    private String _Link;

    public AnchorArg(String body, String link) {
        _Body = StringEscapeUtils.escapeHtml(FSUtils.noNull(body));
        _Link = StringEscapeUtils.escapeHtml(FSUtils.noNull(link));
    }

    public String toString() {
        return "<a href=\"" + _Link + "\">" + _Body + "</a>";
    }
}
