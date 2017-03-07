package tds.main.bo;

import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;

public class Conference {

    // DB FIELDS
    private int _ConferenceID;
    private String _DisplayName;
    private String _FullName;

    // CONSTRUCTORS
    public Conference() {
    }
    
    public Conference(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS
    public int getConferenceID() {return _ConferenceID;}
    public String getDisplayName() {return _DisplayName;}
    public String getFullName() {return _FullName;}

    // SETTERS
    public void setConferenceID(int ConferenceID) {_ConferenceID = ConferenceID;}
    public void setDisplayName(String DisplayName) {_DisplayName = DisplayName;}
    public void setFullName(String FullName) {_FullName = FullName;}
    
    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        
        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "ConferenceID")) {
                setConferenceID(crs.getInt(prefix + "ConferenceID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "DisplayName")) {
                setDisplayName(crs.getString(prefix + "DisplayName"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "FullName")) {
                setFullName(crs.getString(prefix + "FullName"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
