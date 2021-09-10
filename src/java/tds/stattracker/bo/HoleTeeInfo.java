package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

import java.io.Serializable;

import static tds.data.CTColumnLists._Cols;

public class HoleTeeInfo implements Serializable {

    // DB FIELDS
    private Integer _HoleID;
    private Integer _TeeID;
    private Integer _Yardage;
    private Integer _Par;
    private Integer _Handicap;

    // OBJECTS
    private Hole _Hole;
    private Tee _Tee;

    // CONSTRUCTORS
    public HoleTeeInfo() {
    }

    public HoleTeeInfo(int holeId, int teeId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("HoleTeeInfo", "", ""));
            sql.append("FROM HoleTeeInfo");
            sql.append("WHERE HoleID = ").append(holeId).append(" AND TeeID = ").append(teeId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            while (crs.next()) {
                InitFromCRS(crs, "");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public HoleTeeInfo(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getHoleID() {return _HoleID;}
    public Integer getTeeID() {return _TeeID;}
    public Integer getYardage() {return _Yardage;}
    public Integer getPar() {return _Par;}
    public Integer getHandicap() {return _Handicap;}
    public Hole getHole() {return _Hole;}
    public Tee getTee() {return _Tee;}

    // SETTERS
    public void setHoleID(Integer holeId) {_HoleID = holeId;}
    public void setTeeID(Integer teeId) {_TeeID = teeId;}
    public void setYardage(Integer yardage) {_Yardage = yardage;}
    public void setPar(Integer par) {_Par = par;}
    public void setHandicap(Integer handicap) {_Handicap = handicap;}
    public void setHole(Hole Hole) {_Hole = Hole;}
    public void setTee(Tee Tee) {this._Tee = Tee;}

    // PUBLIC METHODS

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("HoleTeeInfo", "HoleID", getHoleID(), "TeeID", getTeeID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "HoleID")) { setHoleID(crs.getInt(prefix + "HoleID")); }
            if (FSUtils.fieldExists(crs, prefix, "TeeID")) { setTeeID(crs.getInt(prefix + "TeeID")); }
            if (FSUtils.fieldExists(crs, prefix, "Yardage")) { setYardage(crs.getInt(prefix + "Yardage")); }
            if (FSUtils.fieldExists(crs, prefix, "Par")) { setPar(crs.getInt(prefix + "Par")); }
            if (FSUtils.fieldExists(crs, prefix, "Handicap")) { setHandicap(crs.getInt(prefix + "Handicap")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "Hole$", "HoleID")) { setHole(new Hole(crs, "Hole$")); }
            if (FSUtils.fieldExists(crs, "Tee$", "TeeID")) { setTee(new Tee(crs, "Tee$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO HoleTeeInfo");
        sql.append("(HoleID, TeeID, Yardage, Par, Handicap) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getHoleID()));
        sql.append(FSUtils.InsertDBFieldValue(getTeeID()));
        sql.append(FSUtils.InsertDBFieldValue(getYardage()));
        sql.append(FSUtils.InsertDBFieldValue(getPar()));
        sql.append(FSUtils.InsertDBFieldValue(getHandicap()));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE HoleTeeInfo SET ");
        sql.append(FSUtils.UpdateDBFieldValue("Yardage", getYardage()));
        sql.append(FSUtils.UpdateDBFieldValue("Par", getPar()));
        sql.append(FSUtils.UpdateDBFieldValue("Handicap", getHandicap()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE HoleID = ").append(getHoleID()).append(" AND TeeID = ").append(getTeeID());

        // Execute Query
        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
