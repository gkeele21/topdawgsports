package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;

public class Sport implements Serializable {

    // CONSTANTS
    public final static int PRO_FOOTBALL = 1;
    public final static int COLLEGE_FOOTBALL = 2;
    public final static int COLLEGE_BASKETBALL = 3;

    // DB FIELDS
    private int _SportID;
    private String _DisplayName;
    private String _Prefix;

    // CONSTRUCTORS
    public Sport() {

    }

    public Sport(int sportID) {
        this(null, sportID);
    }

    public Sport(Connection con, int sportID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Sport", "s.", ""));
            sql.append("FROM Sport s ");
            sql.append("WHERE s.SportID = ").append(sportID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            initFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }

    public Sport(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public Sport(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS
    public int getSportID() {return _SportID;}
    public String getDisplayName() {return _DisplayName;}
    public String getPrefix() {return _Prefix;}

    // SETTERS
    public void setSportID(int SportID) {_SportID = SportID;}
    public void setDisplayName(String DisplayName) {_DisplayName = DisplayName;}
    public void setPrefix(String Prefix) {_Prefix = Prefix;}

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "SportID")) {
                setSportID(crs.getInt(prefix + "SportID"));
            }

            if (FSUtils.fieldExists(crs, prefix, "DisplayName")) {
                setDisplayName(crs.getString(prefix + "DisplayName"));
            }

            if (FSUtils.fieldExists(crs, prefix, "Prefix")) {
                setPrefix(crs.getString(prefix + "Prefix"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
