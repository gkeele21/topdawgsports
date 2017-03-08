package bglib.tags;

import static bglib.util.BGConstants.CURRENTROW;
import static bglib.util.BGConstants.CURRENTROWOVERALL;
import bglib.util.AuTimer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlagshipTableTag extends SimpleTagSupport implements TableExecutionTag {

    private static final String TOTAL = "total";
    private static final String COUNT = "count";
    private static final String HIGHLIGHTROW = "highlightRow";

    private List items;
    private String var;
    private List showOnEmpty = Arrays.asList("rowTitle","rowHeader","rowEmpty");
    private List showOrder = Arrays.asList("rowTitle","rowInfo","rowHeader","rowData",
            "rowTotal","rowNavigation");
    private int displayRows = 10000;
    private int startingRowNum = 1;
    private int totalRows = 0;
    private int tableNumber = 1;
    private int dataRowsPerTableRow = 1;
    private boolean displayAll=false;
    private String highlightRowAttribute = "";
    private String highlightRowValue = "";
    private JspFragment rowTitle;
    private JspFragment rowHeader;
    private JspFragment rowInfo;
    private JspFragment rowData;
    private JspFragment rowTotal;
    private JspFragment rowEmpty;
    private JspFragment rowNavigation;
    private Map<String,Object> rowTotalElements;

    public void setItems(List items) {
        this.items = items;
    }

    public void setDisplayAll(boolean da) {
        displayAll=da;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setShowOnEmpty(List showOnEmpty) {
        this.showOnEmpty = showOnEmpty;
    }

    public void setShowOrder(List showOrder) {
        this.showOrder = showOrder;
    }

    public void setDisplayRows(int displayRows) {
        if (displayRows > 0)
            this.displayRows = displayRows;
    }

    public void setStartingRowNum(int startingRowNum) {
        if (startingRowNum > 0)
            this.startingRowNum = startingRowNum;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public void setTableNumber(int tableNumber) {
        if (tableNumber > 0)
            this.tableNumber = tableNumber;
    }

    public void setDataRowsPerTableRow(int num) {
        if (num>0) {
            dataRowsPerTableRow = num;
        }
    }

    public void setHighlightRowAttribute(String highAtt) {
        this.highlightRowAttribute = highAtt;
    }

    public void setHighlightRowValue(String highVal) {
        this.highlightRowValue = highVal;
    }

    public int getDisplayRows() {
        return this.displayRows;
    }

    public int getTotalRows() {
        if (totalRows>0) {
            return totalRows;
        }
        return items.size();
    }

    public boolean getDisplayAll() {
        return displayAll;
    }

    public int getStartingRowNum() {
        return this.startingRowNum;
    }

    public int getTableNumber() {
        return this.tableNumber;
    }

    public int getDataRowsPerTableRow() {
        return dataRowsPerTableRow;
    }

    public String getHighlightRowAttribute() {
        return this.highlightRowAttribute;
    }

    public String getHighlightRowValue() {
        return this.highlightRowValue;
    }

    public String getHighlightRow() {
        if (highlightRowAttribute != null && !highlightRowAttribute.equals("")) {
            return this.highlightRowAttribute+"=\""+highlightRowValue+"\"";
        } else {
            return "";
        }
    }
    public void setRowData(JspFragment rowData) {
        this.rowData = rowData;
    }

    public void setRowTotal(JspFragment rowTotal) {
        this.rowTotal = rowTotal;
    }

    public void setRowEmpty(JspFragment rowEmpty) {
        this.rowEmpty = rowEmpty;
    }

    public void setRowTitle(JspFragment rowTitle) {
        this.rowTitle = rowTitle;
    }

    public void setRowHeader(JspFragment rowHeader) {
        this.rowHeader = rowHeader;
    }

    public void setRowInfo(JspFragment rowInfo) {
        this.rowInfo = rowInfo;
    }

    public void setRowNavigation(JspFragment rowNavigation) {
        this.rowNavigation = rowNavigation;
    }

    public void addRowTotalParameter(String name,Object value,boolean clearFirst) {

        double total = 0;
        int count = 0;
        double newval = 0;

        if (rowTotalElements == null) {
            rowTotalElements = new HashMap<String,Object>();
        }
        Map<String,Object> map;

        try {
            newval = Double.parseDouble(value.toString());
        } catch (Exception e) {
            // NewValue will be 0, but we don't want to exit because we still want to increment count
        }

        if (rowTotalElements.containsKey(name)) {
            map = (Map<String,Object>)rowTotalElements.get(name);
            try {
                total = Double.parseDouble(map.get(TOTAL).toString());
                count = Integer.parseInt(map.get(COUNT).toString());
            } catch (Exception e) {
                // Failed, so initial values should be 0;
            }
        } else {
            map = new HashMap<String,Object>();
        }

        count++;
        if (clearFirst)
        {
            total = 0;
        }
        
        total += newval;

        map.put(TOTAL,total);
        map.put(COUNT,count);

        rowTotalElements.put(name,map);
    }

    public void doTag() throws JspException, IOException {
        if (rowData == null) {
            throw new JspTagException("Table tag must include a rowData fragment.");
        }

        if (items == null || items.size() == 0) {
            for (Object name : showOnEmpty) {
                if (name.equals("rowTitle")) {
                    doRowTitle();
                } else if (name.equals("rowHeader")) {
                    doRowHeader();
                } else if (name.equals("rowEmpty")) {
                    doRowEmpty();
                } else if (name.equals("rowInfo")) {
                    doRowInfo();
                } else if (name.equals("rowNavigation")) {
                    doRowNavigation();
                }
            }
        } else {
            for (Object name : showOrder) {
                if (name.equals("rowTitle")) {
                    doRowTitle();
                } else if (name.equals("rowHeader")) {
                    doRowHeader();
                } else if (name.equals("rowEmpty")) {
                    doRowEmpty();
                } else if (name.equals("rowInfo")) {
                    doRowInfo();
                } else if (name.equals("rowNavigation")) {
                    doRowNavigation();
                } else if (name.equals("rowData")) {
                    doRowData();
                } else if (name.equals("rowTotal")) {
                    doRowTotal(true);
                }
            }
        }

        getJspContext().removeAttribute(var);
    }

    private void doRowData() throws JspException, IOException {
        try {
            if (rowData != null) {
                int currentrowoverall = 0;
                int currentrow = 0;
                for (int i=0; i<items.size(); i++) {
                    Object current = items.get(i);
                    currentrowoverall++;
                    if ((displayAll) || (currentrowoverall >= startingRowNum && currentrowoverall < (startingRowNum + displayRows))) {
                        currentrow++;
                        getJspContext().setAttribute(CURRENTROWOVERALL+tableNumber,currentrowoverall);
                        getJspContext().setAttribute(CURRENTROW+tableNumber,currentrow);
                        if (dataRowsPerTableRow>1) {
                            for (int j=0; j<dataRowsPerTableRow; j++, i++) {
                                if (i<items.size()) {
                                    int offset = j*((int)Math.ceil((double)items.size()/dataRowsPerTableRow));
                                    offset += (i/dataRowsPerTableRow);
                                    getJspContext().setAttribute(var + (j+1), items.get(offset));
                                } else {
                                    getJspContext().removeAttribute(var + (j+1));
                                }
                            }
                            i--;
                        } else {
                            getJspContext().setAttribute(var, current);
                        }
                        String highlight = currentrow % 2 == 0 ? "" : getHighlightRow();
                        getJspContext().setAttribute(HIGHLIGHTROW+tableNumber,highlight);
                        rowData.invoke(null);
                        doRowTotal(false);
                    }
                }
            }
            getJspContext().removeAttribute(var);
        } catch (JspException e) {
            e.printStackTrace();
            throw new JspException(e);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void doRowEmpty() throws JspException, IOException {
        if (rowEmpty != null) {
            rowEmpty.invoke(null);
        }
    }

    private void doRowTitle() throws JspException, IOException {
        if (rowTitle != null) {
            rowTitle.invoke(null);
        }
    }

    private void doRowHeader() throws JspException, IOException {
        if (rowHeader != null) {
            rowHeader.invoke(null);
        }
    }

    private void doRowInfo() throws JspException, IOException {
        if (rowInfo != null) {
            // set the attribute values for the result display numbers
            getJspContext().setAttribute("fromRows"+tableNumber,startingRowNum);
            int totalRows = getTotalRows();
            getJspContext().setAttribute("totalRows"+tableNumber,totalRows);
            int remaining = startingRowNum + displayRows - 1;
            if (startingRowNum + displayRows > totalRows) {
                remaining = totalRows;
            }
            getJspContext().setAttribute("toRows"+tableNumber,remaining);

            rowInfo.invoke(null);
        }
    }

    private void doRowTotal(boolean printOut) throws JspException, IOException {
        if (rowTotal != null) {

            if (rowTotalElements != null && rowTotalElements.keySet().size() > 0) {
                for (Object o : rowTotalElements.keySet()) {
                    String key = (String) o;
                    Map map = (Map) rowTotalElements.get(key);

                    getJspContext().setAttribute(key, map);
                }
            }
            if (printOut)
            {
                rowTotal.invoke(null);
            }
        }
    }

    private void doRowNavigation() throws JspException, IOException {
        if (rowNavigation != null) {
            rowNavigation.invoke(null);
        }
    }
}
