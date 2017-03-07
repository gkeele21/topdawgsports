package bglib.tags;

import bglib.util.*;
import bglib.control.BGServlet;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang.StringEscapeUtils;

public class ColHeaderTag extends SimpleTagSupport {

    private String name="";
    private String display="";
    private String cssClass = "";
    private String defaultSort="false";
    private String desc="false"; // "true" if you want the sort order to be descending the first time the column is clicked

    public void setName(String name) {
        this.name = name.replace(" ", "_");
    }

    public void setDisplay(String name) {
        this.display = name;
    }

    public String getDisplay() {
        if (display.trim().length()>0) {
            return display;
        }
        return name.replace("_", " ");
    }

    public void setCssClass(String name) {
        this.cssClass = name;
    }

    public void setDefaultSort(String ds) {
        defaultSort = ds;
    }

    public void setDesc(String ds) {
        desc = ds;
    }

    public void doTag() throws JspException, IOException {
        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();

        sb.append("<a href=\"");

        String pageName = (String)getJspContext().getAttribute(BGServlet.PAGE_NAME_ATTR, PageContext.REQUEST_SCOPE);
        sb.append(pageName + ".html");
        sb.append("?" + pageName + "Sort=" + name);
        String sort = FSUtils.noNull(((PageContext)getJspContext()).getRequest().getParameter(pageName + "Sort"));
        if (sort.length()==0) {
            sort = FSUtils.noNull((String)getJspContext().getAttribute(pageName + "Sort", PageContext.SESSION_SCOPE));
        }
        if (sort.length()==0 && defaultSort.equals("true")) {
            sort = name;
        }

        if (sort.equals(name) || (!sort.startsWith(name) && desc.equals("true"))) {
            sb.append("_desc");
        }

        sb.append("\"");
        if (cssClass.length()>0) {
            sb.append(" class=\"" + cssClass + "\"");
        }

        sb.append(">" + getDisplay() + "</a>");
        if (sort.startsWith(name)) {
            sb.append("&nbsp;<img src=\"../images/");
            if (sort.equals(name)) {
                sb.append("arrow_up.gif");
            } else if (sort.equals(name + "_desc")) {
                sb.append("arrow_down.gif");
            } else {
                sb.append("spacer.gif");
            }
            sb.append("\" alt=\"\" />");
        }

        getJspContext().getOut().print(sb);
    }

}
