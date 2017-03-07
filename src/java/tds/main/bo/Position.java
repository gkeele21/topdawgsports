package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import static tds.main.bo.CTApplication._CT_LOG;

public class Position implements Serializable {

    public static Map<Integer,Position> getPositionCache() {
        return _PositionCache;
    }

    public static Map<String,Position> getPositionStrCache() {
        return _PositionStrCache;
    }

    public static void setPositionCache(Map<Integer,Position> aPositionCache) {
        _PositionCache = aPositionCache;
    }

    public static void setPositionStrCache(Map<String,Position> aPositionStrCache) {
        _PositionStrCache = aPositionStrCache;
    }
    
    // DB FIELDS
    private int _PositionID;
    private int _SportID;    
    private String _PositionName;
    private String _PositionNameLong;
    
    // OBJECTS
    private Sport _Sport;
    
    private static Map<Integer,Position> _PositionCache = new HashMap<Integer,Position>();
    private static Map<String,Position> _PositionStrCache = new HashMap<String,Position>();

    static {
        try {
            
            CachedRowSet crs = null;
            try {
                StringBuilder sql = new StringBuilder();
                sql.append(" SELECT ").append(_Cols.getColumnList("Position", "", ""));
                sql.append(" FROM Position");

                crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
                while (crs.next()) {
                    Position pos = new Position(crs);
                    getPositionCache().put(crs.getInt("PositionID"), pos);
                    getPositionStrCache().put(pos.getPositionName(), pos);
                }
            } catch (Exception e) {
                CTApplication._CT_LOG.error(e);
            } finally {
                JDBCDatabase.closeCRS(crs);
            }

        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    // CONSTRUCTORS
    public Position() {
        
    }
    
    public Position(int positionID) {
        this(null, positionID);
    }

    public Position(Connection con, int positionID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("Position", "p.", ""));
            sql.append(",").append(_Cols.getColumnList("Sport", "s.", ""));
            sql.append(" FROM Position p ");
            sql.append(" INNER JOIN Sport s on s.SportID = p.PositionID ");
            sql.append(" WHERE p.PositionID = ").append(positionID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }
    
    public Position(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public Position(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }
    
    // GETTERS
    public static Position getInstance(int posID) throws Exception {return getPositionCache().get(posID);}
    public static Position getInstance(String posName) throws Exception {return getPositionStrCache().get(posName);}
    public int getPositionID() {return _PositionID;}
    public int getSportID() {return _SportID;}    
    public String getPositionName() {return _PositionName;}
    public String getPositionNameLong() {return _PositionNameLong;}
    public Sport getSport() {if (_Sport == null && _SportID > 0) {_Sport = new Sport(_SportID);}return _Sport;}
    public static Collection<Position> getAllPositions() { return getPositionCache().values(); }
    
    // SETTERS
    public void setPositionID(int PositionID) {_PositionID = PositionID;}
    public void setSportID(int SportID) {_SportID = SportID;}
    public void setPositionName(String PositionName) {_PositionName = PositionName;}
    public void setPositionNameLong(String PositionNameLong) {_PositionNameLong = PositionNameLong;}
    public void setSport(Sport Sport) {_Sport = Sport;}
    
    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {
            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "PositionID")) {
                setPositionID(crs.getInt(prefix + "PositionID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "SportID")) {
                setSportID(crs.getInt(prefix + "SportID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "PositionName")) {
                setPositionName(crs.getString(prefix + "PositionName"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "PositionNameLong")) {
                setPositionNameLong(crs.getString(prefix + "PositionNameLong"));
            }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "Sport$", "SportID")) {
                setSport(new Sport(crs, "Sport$"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
