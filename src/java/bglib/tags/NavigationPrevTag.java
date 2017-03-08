package bglib.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Feb 28, 2006
 * Time: 9:58:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class NavigationPrevTag extends SimpleTagSupport {

    private String link;

    public void setLink(String link) {
        this.link = link;
    }

    public void doTag() throws JspException, IOException {

        JspFragment body = getJspBody();

        TableExecutionTag parent = (TableExecutionTag) findAncestorWithClass(this,TableExecutionTag.class);
        if (parent == null) {
            throw new JspTagException("The NavPrev action does not have a 'tableRows' parent tag.");
        }

        int origstartingRowNum = parent.getStartingRowNum();
        int displayRows = parent.getDisplayRows();
        if (origstartingRowNum > 1) {
            StringWriter evalResult = new StringWriter();
            StringBuffer sb = evalResult.getBuffer();
            sb.append("<a href=\"");
            sb.append(link);
            if (link.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            sb.append("start=");
            sb.append(origstartingRowNum - displayRows);
            sb.append("\">");
            body.invoke(evalResult);
            sb.append("</a>");
            getJspContext().getOut().print(sb);
        }
    }

}
