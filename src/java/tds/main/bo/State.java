package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.*;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class State implements Serializable, Comparable {

    // DB FIELDS
    private int _StateID;
    private String _State;
    private String _StateFull;

    private static final Map<String, State> _InstancesByName = new HashMap<String, State>();
    private static final Map<Integer, State> _InstancesByID = new HashMap<Integer, State>();
    private static List<State> _All;

    static {

        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("State"));
            sql.append(" FROM State");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                State state = new State(crs);
                _InstancesByID.put(state.getStateID(), state);
                _InstancesByName.put(state.getName(), state);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }

    public static State getInstance(int id) {
        return _InstancesByID.get(id);
    }

    public static State getInstance(String name) {
        return _InstancesByName.get(name.toUpperCase());
    }

    public static List<State> getAll() {
        if (_All==null) {
            _All = new ArrayList<State>(_InstancesByID.values());
            _All.remove(getInstance(0)); // remove the Unknown State
            Collections.sort(_All);
        }

        return _All;
    }

    private State(CachedRowSet crs) {
        initFromCRS(crs, "");
    }
    
    // GETTERS
    public int getStateID() { return _StateID; }
    public String getName() { return _State; }
    public String getFullName() { return _StateFull; }
    
    // PUBLIC METHODS

    public String toString() { return _State; }

    public int compareTo(Object other) {
        return getFullName().compareTo(((State)other).getFullName());
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        try {
            if (FSUtils.fieldExists(crs, prefix, "StateID")) {
                _StateID = crs.getInt(prefix + "StateID");
            }
            if (FSUtils.fieldExists(crs, prefix, "State")) {
                _State = crs.getString(prefix + "State").toUpperCase();
            }
            if (FSUtils.fieldExists(crs, prefix, "StateFull")) {
                _StateFull = crs.getString(prefix + "StateFull");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
