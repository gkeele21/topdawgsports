package bglib.tags;

import java.io.Serializable;

public class ListBoxItem implements Serializable, Comparable {

    private String displayName = "";
    private String value = "";
    private boolean selected;

    public ListBoxItem() {

    }

    public ListBoxItem(String displayName, String value, boolean selected) {
        this.displayName = displayName;
        this.value = value;
        this.selected = selected;
    }

    public ListBoxItem(String displayName, String value) {
        this.displayName = displayName;
        this.value = value;
        this.selected = false;
    }

    public ListBoxItem(String displayName) {
        this.displayName = displayName;
        this.value = displayName;
        this.selected = false;
    }

    public String getDisplayName() {    return displayName;   }
    public void setDisplayName(String displayName) {    this.displayName = displayName;   }
    public String getValue() {    return value;  }
    public void setValue(String value) {     this.value = value;   }
    public boolean isSelected() {     return selected;   }
    public void setSelected(boolean selected) {    this.selected = selected;   }

    public int compareTo(Object other) {
        return ((ListBoxItem)other).getValue().compareTo(getValue());
    }

    public String toString() { return displayName + " - " + value; }

    public boolean equals(Object other) {
        if (((ListBoxItem)other).getValue().compareToIgnoreCase(getValue())==0) {
            return true;
        }
        return false;
    }
}
