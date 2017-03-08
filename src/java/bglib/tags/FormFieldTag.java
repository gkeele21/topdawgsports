package bglib.tags;

import bglib.util.*;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.text.ParseException;

public class FormFieldTag extends SimpleTagSupport {

    public static final String EXTRA_ATTR = "fasffExtra";

    private FormField field;
    private String cssClass = "";
    private String dirtyCheck = "true";
    private String onChange = "";
    private String onClick = "";
    private String onBlur = "";
    private String readOnly = "";
    private String defaultValue = "";
    private String label = "";
    private String size = "";
    private String alwaysShowZero = "";
    private String format = "";
    private String cols = "";
    private String rows = "";
    private List<ListBoxItem> items;
    private FormInputType type = FormInputType.TEXT;

    // these are attributes passed in via the request or session. Good for easily setting an attr for
    // every FormField tag on a page or site
    private Map<String, String> extraAttrsMap;

    public void setField(FormField field) {
        if (field==null) {
            throw new RuntimeException("Error: no such field");
        }
        this.field = field;
    }

    public void setType(String typeStr) {
        type = FormInputType.valueByName(typeStr);
        if (type==null) {
            throw new RuntimeException("Illegal type for FormFieldTag: " + typeStr);
        }
    }

    public void setCols(String cols) { this.cols = cols; }
    public void setRows(String cols) { this.rows = cols; }

    public void setItems(List<ListBoxItem> items) {
        this.items = items;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public void setOnBlur(String onClick) {
        this.onBlur = onClick;
    }

    public void setDirtyCheck(String onChange) {
        this.dirtyCheck = onChange;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public void setDefaultValue(String defValue) { defaultValue = defValue; }

    public void setLabel(String lbl) { label = lbl; }

    public void setName(String name) {
        field = new FormField(0, name, name, 0, 0);
    }

    public void setSize(String sz) { size = sz; }

    public void setAlwaysShowZero(String sz) { alwaysShowZero = sz; }

    public void setFormat(String sz) { format = sz; }

    public void doTag() throws JspException, IOException {
        extraAttrsMap = getExtraAttrs();

        switch (type) {
            case CHECKBOX:
                doCheckboxTag();
                break;
            case LISTBOX:
                doListboxTag();
                break;
            case RADIO_BUTTON:
                doRadioTag();
                break;
            case RADIO_GROUP:
                doRadioGroupTag();
                break;
            case TEXT_AREA:
                doTextAreaTag();
                break;
            case TEXT:
            case PASSWORD:
                doTextTag();
        }
    }

    private Map<String, String> getExtraAttrs() {
        Map<String, String> ret = new HashMap<String, String>();
        try {
            String extraAttrs = (FSUtils.noNull((String)(getJspContext().getAttribute(EXTRA_ATTR, PageContext.REQUEST_SCOPE))) + " " +
                    FSUtils.noNull((String)getJspContext().getAttribute(EXTRA_ATTR, PageContext.SESSION_SCOPE))).trim().toLowerCase();
            while (extraAttrs.trim().length()>0) {
                String curAttr = extraAttrs.substring(0, extraAttrs.indexOf("="));
                extraAttrs = extraAttrs.substring(extraAttrs.indexOf("\"")+1);
                String curVal = extraAttrs.substring(0, extraAttrs.indexOf("\""));
                extraAttrs = extraAttrs.substring(extraAttrs.indexOf("\"")+1).trim();
                if (!ret.containsKey(curAttr)) {
                    ret.put(curAttr, "");
                }
                ret.put(curAttr, ret.get(curAttr) + " " + curVal);
            }
        } catch (Exception e) {
            Application._GLOBAL_LOG.error(e); // log and continue
        }

        return ret;
    }

    private void doTextTag() throws JspException, IOException {
        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();

        sb.append("<input");
        if (type==FormInputType.PASSWORD) {
            sb.append(" type=\"password\"");
        }
        doNameAttr(sb);
        doOnChangeAttr(sb);
        doSizeAttr(sb);

        String val = defaultValue;
        Map inputs = getInputsMap();
        if (inputs!=null && inputs.containsKey(field)) {
            val = (String)inputs.get(field);
        }
        if (format!=null && format.length()>0) {
            if (format.compareToIgnoreCase("currency")==0) {
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                try {
                    val = nf.format(nf.parse((val.startsWith("$") ? "" : "$") + val));
                } catch (ParseException e) {
                    // ignore; val keeps old value
                }
            }
        }
        if (val != null && val.length() > 0) {
            sb.append(" value=\"" + val + "\"");
        }

        doDisabledAttr(sb);
        if (field!=null && field.getMaxValue()>0) {
            sb.append(" maxlength=\"" + field.getMaxValue() + "\"");
        } else {
            sb.append(" maxlength=\"100\"");
        }

        doClassAttr(sb);
        doExtraAttrs(sb);

        sb.append(" />");
        getJspContext().getOut().print(sb);

    }

    private void doTextAreaTag() throws JspException, IOException {
        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();

        sb.append("<textarea");
        doNameAttr(sb);
        doOnChangeAttr(sb);

        if (cols!=null) {
            sb.append(" cols=\"" + cols + "\"");
        }
        if (rows!=null) {
            sb.append(" rows=\"" + rows + "\"");
        }

        String val = defaultValue;
        Map inputs = getInputsMap();
        if (inputs!=null && inputs.containsKey(field)) {
            val = (String)inputs.get(field);
        }
        doDisabledAttr(sb);

        doClassAttr(sb);
        doExtraAttrs(sb);

        sb.append(">" + val + "</textarea>");
        getJspContext().getOut().print(sb);

    }

    private void doListboxTag() throws JspException, IOException {
        if (items == null || items.size() < 1) {
            return;
        }

        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();

        sb.append("<select ");

        doNameAttr(sb);
        doOnChangeAttr(sb);
        doClassAttr(sb);
        doDisabledAttr(sb);
        doExtraAttrs(sb);

        sb.append(">");

        String val = defaultValue;
        Map inputs = getInputsMap();
        if (inputs!=null && inputs.containsKey(field)) {
            val = (String)inputs.get(field);
        }

        for (ListBoxItem item : items) {
            if (item.getValue().equals("0") && !val.equals("0") && !val.equals("") && !alwaysShowZero.equals("true")) {
                continue; // skip the "Choose a blah..." item if something else is already selected
            }
            sb.append("<option value=\"" + item.getValue() + "\"");
            if (item.isSelected() || item.getValue().compareToIgnoreCase(val)==0) {
                sb.append(" selected=\"true\"");
            }
            sb.append(">"+item.getDisplayName());
            sb.append("</option>");
        }
        sb.append("</select>");
        getJspContext().getOut().print(sb);

    }

    private Map getInputsMap() {
        Map ret = (Map)getJspContext().getAttribute("inputs", PageContext.REQUEST_SCOPE);
        if (ret==null) {
            ret = (Map)getJspContext().getAttribute("inputs", PageContext.SESSION_SCOPE);
        }

        return ret;
    }

    private void doRadioTag() throws JspException, IOException {
        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();

        sb.append("<input type=\"radio\"");
        doNameAttr(sb);
        doOnChangeAttr(sb);

        String val = defaultValue;
        Map inputs = getInputsMap();
        if (inputs!=null && inputs.containsKey(field)) {
            val = (String)inputs.get(field);
        }
        if ("false".equals(val) && label.toLowerCase().equals("no")) val="no";
        if ("true".equals(val) && label.toLowerCase().equals("yes")) val="yes";
        if (val != null && val.length() > 0 && val.compareToIgnoreCase(label)==0) {
            sb.append(" checked");
        }
        doDisabledAttr(sb);

        FormError error = (FormError)getJspContext().getAttribute(BGConstants.FORM_ERROR_ATTR, PageContext.SESSION_SCOPE);
        if (error!=null && error.getErrorMap().containsKey(field)) {
            sb.insert(0, "<span class=\"" + BGConstants.ERROR_CLASS_ATTR + "\">*</span>");
            error.getErrorMap().remove(field);
        }

        if (cssClass!=null && cssClass.length()>0) {
            sb.append(" class=\"" + cssClass + "\"");
        }

        sb.append(" value=\"" + label + "\"");
        doExtraAttrs(sb);

        sb.append(" />" + label);
        getJspContext().getOut().print(sb);
    }

    private void doRadioGroupTag() throws JspException, IOException {
        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();

        for (ListBoxItem item : items) {
            sb.append("<input type=\"radio\"");
            doNameAttr(sb);
            doOnChangeAttr(sb);

            String val = defaultValue;
            Map inputs = getInputsMap();
            if (inputs!=null && inputs.containsKey(field)) {
                val = (String)inputs.get(field);
            }
            if (val != null && val.length() > 0 && val.equals(item.getValue())) {
                sb.append(" checked");
            }
            doDisabledAttr(sb);

            FormError error = (FormError)getJspContext().getAttribute(BGConstants.FORM_ERROR_ATTR, PageContext.SESSION_SCOPE);
            if (error!=null && error.getErrorMap().containsKey(field)) {
                sb.insert(0, "<span class=\"" + BGConstants.ERROR_CLASS_ATTR + "\">*</span>");
                error.getErrorMap().remove(field);
            }

            if (cssClass!=null && cssClass.length()>0) {
                sb.append(" class=\"" + cssClass + "\"");
            }

            sb.append(" value=\"" + item.getValue() + "\"");
            doExtraAttrs(sb);

            sb.append(" />" + item.getDisplayName() + "&nbsp;&nbsp;&nbsp;");
        }
        getJspContext().getOut().print(sb);
    }

    private void doCheckboxTag() throws JspException, IOException {
        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();

        sb.append("<input type=\"checkbox\"");
        doNameAttr(sb);
        doOnChangeAttr(sb);

        String val = defaultValue;
        Map inputs = getInputsMap();
        if (inputs!=null && inputs.containsKey(field)) {
            val = (String)inputs.get(field);
        }
        if (val != null && val.length() > 0 && val.equals("on")) {
            sb.append(" checked");
        }
        doDisabledAttr(sb);

        FormError error = (FormError)getJspContext().getAttribute(BGConstants.FORM_ERROR_ATTR, PageContext.SESSION_SCOPE);
        if (error!=null && error.getErrorMap().containsKey(field)) {
            sb.insert(0, "<span class=\"" + BGConstants.ERROR_CLASS_ATTR + "\">*</span>");
            error.getErrorMap().remove(field);
        }

        if (cssClass!=null && cssClass.length()>0) {
            sb.append(" class=\"" + cssClass + "\"");
        }
        doExtraAttrs(sb);

        sb.append(" />" + label);
        getJspContext().getOut().print(sb);

    }

    private void doClassAttr(StringBuffer sb) {
        FormError error = (FormError)getJspContext().getAttribute(BGConstants.FORM_ERROR_ATTR, PageContext.SESSION_SCOPE);
        String css = cssClass;
        if (error!=null && error.getErrorMap().containsKey(field)) {
            css += " " + BGConstants.ERROR_CLASS_ATTR;
            error.getErrorMap().remove(field);
        }
        if (extraAttrsMap.containsKey("class")) {
            css += " " + extraAttrsMap.get("class");
        }
        if (css!=null && css.length()>0) {
            sb.append(" class=\"" + css + "\"");
        }
    }

    private void doNameAttr(StringBuffer sb) {
        sb.append(" name=\"" + field.getHtmlName() + "\"");
    }

    private void doOnChangeAttr(StringBuffer sb) {
        String attrBody="";
        if (dirtyCheck.equals("true")) {
            attrBody += "pageDirty=true;";
        }
        if (onChange != null) {
            attrBody += onChange;
        }
        if (extraAttrsMap.containsKey("onchange")) {
            attrBody += " " + extraAttrsMap.get("onchange");
        }
        if (attrBody.length()>0) {
            sb.append(" onChange=\"" + attrBody + "\"");
        }

        attrBody = onClick;
        if (extraAttrsMap.containsKey("onclick")) {
            attrBody += " " + extraAttrsMap.get("onclick");
        }
        if (attrBody.length()>0) {
            sb.append(" onClick=\"" + attrBody + "\"");
        }

        attrBody = onBlur;
        if (extraAttrsMap.containsKey("onblur")) {
            attrBody += " " + extraAttrsMap.get("onblur");
        }
        if (attrBody.length()>0) {
            sb.append(" onBlur=\"" + attrBody + "\"");
        }
    }

    private void doSizeAttr(StringBuffer sb) {
        String theSize = size;
        if (extraAttrsMap.containsKey("size")) {
            theSize = extraAttrsMap.get("size");
        }
        if (theSize != null && theSize.length() > 0) {
            sb.append(" size=\"" + theSize + "\"");
        }
    }

    private void doDisabledAttr(StringBuffer sb) {
        String ro = "";
        if (extraAttrsMap.containsKey("readonly")) {
            ro = extraAttrsMap.get("readonly");
        }
        if (readOnly.length()>0) {
            ro = readOnly;
        }

        if (ro!=null && ro.toLowerCase().equals("false")) { // ro="false" overrides all else
            return;
        }

        if (ro != null && ro.length() > 0 && !ro.toLowerCase().equals("false")) {
            if (type==FormInputType.TEXT) {
                sb.append(" readonly");
            } else {
                sb.append(" disabled");
            }
        }

        String disabled = (String)getJspContext().getAttribute("formDisabled", PageContext.PAGE_SCOPE);
        if (disabled!=null && disabled.equals("disabled")) {
            sb.append(" disabled");
        }
    }

    private void doExtraAttrs(StringBuffer sb) {
        for (String key : extraAttrsMap.keySet()) {
            if (!key.equals("class") && !key.equals("readonly") && !key.equals("onchange") && !key.equals("onblur") &&
                    !key.equals("onclick") && !key.equals("size")) {
                sb.append(" " + key + "=\"" + extraAttrsMap.get(key) + "\"");
            }
        }
    }
}
