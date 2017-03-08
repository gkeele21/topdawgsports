package bglib.tags;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class MenuItem implements Serializable, Comparable {

    private String displayName = "";
    private String actionID = "";
    private List<MenuItem> _Submenu = new ArrayList<MenuItem>();

    public MenuItem(String displayName, String actionID, List<MenuItem> submenu) {
        this.displayName = displayName;
        this.actionID = actionID;
        _Submenu.addAll(submenu);
    }

    public MenuItem(String displayName, String actionID) {
        this.displayName = displayName;
        this.actionID = actionID;
    }

    public MenuItem(String displayName) {
        this.displayName = displayName;
        this.actionID = displayName;
    }

    public String getDisplayName() {    return displayName;   }
    public String getActionID() {    return actionID;  }
    public List<MenuItem> getSubMenu() { return _Submenu; }

    public int compareTo(Object other) {
        return ((MenuItem)other).getActionID().compareTo(getActionID());
    }

    public String toString() { return displayName + " - " + actionID; }
}