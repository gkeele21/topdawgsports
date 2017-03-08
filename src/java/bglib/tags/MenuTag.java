package bglib.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class MenuTag extends SimpleTagSupport {

    private List<MenuItem> _Items;
    private String id = "";
    private String action = "";
    private String cssClass = "";
    private String submenuClass = "";

    public void setItems(List<MenuItem> _Items) {
        this._Items = _Items;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getID() {
        if (id.length()==0) {
            return "MenuBar1";
        }
        return id;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        if (cssClass.length()==0) {
            return "MenuBarHorizontal";
        }
        return cssClass;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setSubmenuClass(String action) {
        submenuClass = action;
    }

    public String getSubmenuClass() {
        if (submenuClass.length()==0) {
            return "MenuBarItemSubmenu";
        }
        return submenuClass;
    }

    public void doTag() throws JspException, IOException {
        if (_Items == null || _Items.size() < 1) {
            return;
        }

        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();

        // <ul class="mainlinks">
        sb.append("<ul id=\"" + getID() + "\" class=\"" + getCssClass() + "\">");
        sb.append(doSubmenu(0, _Items));
        sb.append("</ul>");
        getJspContext().getOut().print(sb);

    }

    private String doSubmenu(int level, List<MenuItem> items) {
        StringBuffer sb=new StringBuffer();

        for (MenuItem item : items) {
            List<MenuItem> subItems = item.getSubMenu();
            if (level==0) {
                // <img src="../images/icon_checkboxsm.gif" align="absmiddle" border="0"/>
                sb.append("<li class=\"mainlinks\">");
            } else {
                sb.append("<li>");
            }
            if (subItems.size()>0) {
                String img = (level==0) ? "<img src=\"../images/icon_checkboxsm.gif\" align=\"absmiddle\" border=\"0\"/>" : "";
                sb.append("<a class=\"" + getSubmenuClass() + "\" href=\"#\">" + img +
                        item.getDisplayName() + "</a>\n<ul>\n");
                sb.append(doSubmenu(level+1, subItems));
                sb.append("</ul>\n");
            } else {
                if (action.length()==0) {
                    sb.append("<a onclick=\"doSubmit('" + item.getActionID() + "')\" href=\"#\">" + item.getDisplayName() + "</a>");
                } else {
                    sb.append("<a href=\"" + action + "?action=" + item.getActionID() + "\">" + item.getDisplayName() + "</a>");
                }
            }
            sb.append("</li>\n");
        }

        return sb.toString();
    }

}