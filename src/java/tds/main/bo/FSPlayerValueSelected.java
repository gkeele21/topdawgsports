package tds.main.bo;

import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;

public class FSPlayerValueSelected implements Serializable {
    
    private int _Count = 0;
    private FSPlayerValue _FSPlayerValue;
    
    // CONSTRUCTORS
    public FSPlayerValueSelected() {
    }

    public FSPlayerValueSelected(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FSPlayerValueSelected(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }
    
    // GETTERS
    public int getCount() {return _Count;}
    public FSPlayerValue getFSPlayerValue() {return _FSPlayerValue;}

    // SETTERS
    public void setCount(int Count) {_Count = Count;}
    public void setFSPlayerValue(FSPlayerValue FSPlayerValue) {_FSPlayerValue = FSPlayerValue;}

    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        
        try {
            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "Count")) {
                _Count = crs.getInt(prefix + "Count");
            }
            
            if (FSUtils.fieldExists(crs, "FSPlayerValue$", "PlayerID")) {
                _FSPlayerValue = new FSPlayerValue(crs, "FSPlayerValue$");
            }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
