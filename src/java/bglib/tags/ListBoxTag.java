package bglib.tags;

import bglib.util.FSUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Jul 7, 2006
 * Time: 8:22:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class ListBoxTag extends SimpleTagSupport {

    private List<ListBoxItem> items;
    private String name = "";
    private String cssClass = "";
    private String onChange = "";
    private String readOnly = "";
    private String selectedItemID = "";

    public void setItems(List<ListBoxItem> items) {
        this.items = items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public void setSelectedItemID(String selectedItemID) {
        this.selectedItemID = FSUtils.noNull(selectedItemID);
    }

    public void doTag() throws JspException, IOException {
        if (items == null || items.size() < 1) {
            return;
        }

        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();

        sb.append("<select name=\"" + name + "\"");
        if (cssClass != null && cssClass.length() > 0) {
            sb.append(" class=\"" + cssClass + "\"");
        }
        if (onChange != null && onChange.length() > 0) {
            sb.append(" onchange=\"" + onChange + "\"");
        }
        if (readOnly != null && readOnly.length() > 0 && !readOnly.toLowerCase().equals("false")) {
            sb.append(" disabled=\"" + readOnly + "\"");
        }
        sb.append(">");
        for (ListBoxItem item : items) {
            sb.append("<option value=\"" + item.getValue() + "\"");
            if (item.isSelected() || item.getValue().compareToIgnoreCase(selectedItemID)==0) {
                sb.append(" selected=\"true\"");
            }
            sb.append(">"+item.getDisplayName());
            sb.append("</option>");
        }
        sb.append("</select>");
        getJspContext().getOut().print(sb);

    }

}
