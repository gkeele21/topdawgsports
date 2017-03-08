package bglib.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Feb 20, 2006
 * Time: 9:50:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddRowTotalTag extends SimpleTagSupport {
    private String name;
    private Object value;
    private boolean clearFirst = false;

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public void setClearFirst(boolean value)
    {
        this.clearFirst = value;
    }

    public void doTag() throws JspException, IOException {
        TableExecutionTag parent = (TableExecutionTag) findAncestorWithClass(this,TableExecutionTag.class);
        if (parent == null) {
            throw new JspTagException("The addRowTotal action does not have a 'tableRows' parent tag.");
        }

        parent.addRowTotalParameter(name, value, clearFirst);
    }
}
