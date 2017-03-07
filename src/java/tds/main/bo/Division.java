package tds.main.bo;

import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;

public class Division {
    
    // DB FIELDS
    private int _DivisionID;
    private String _DisplayName;
    
    // CONSTRUCTORS
    public Division() {
    }
    
    public Division(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS
    public int getDivisionID() {return _DivisionID;}
    public String getDisplayName() {return _DisplayName;}
    
    // SETTERS
    public void setDivisionID(int DivisionID) {_DivisionID = DivisionID;}    
    public void setDisplayName(String DisplayName) {_DisplayName = DisplayName;}
    
    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {
            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "DivisionID")) {
                setDivisionID(crs.getInt(prefix + "DivisionID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "DisplayName")) {
                setDisplayName(crs.getString(prefix + "DisplayName"));
            }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
