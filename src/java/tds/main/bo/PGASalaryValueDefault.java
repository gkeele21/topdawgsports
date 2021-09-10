package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;

import java.io.Serializable;
import java.sql.Connection;

import static tds.data.CTColumnLists._Cols;

public class PGASalaryValueDefault implements Serializable {

    public static final int _DefaultValue = 25000;

    // DB FIELDS
    private int _Rank;
    private int _SalaryValue;

    // CONSTRUCTORS
    public PGASalaryValueDefault() {
    }

    public PGASalaryValueDefault(CachedRowSet fields) {
        initFromCRS(fields, "PGASalaryValueDefault$");
    }

    public PGASalaryValueDefault(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    public PGASalaryValueDefault(int rank)
    {
        this(null, rank);
    }

    public PGASalaryValueDefault(Connection con, int rank)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("PGASalaryValueDefault", "sv.", "PGASalaryValueDefault$"));
        sql.append(" FROM PGASalaryValueDefault sv ");
        sql.append(" WHERE Rank = ").append(rank);

        CachedRowSet crs = null;
        try {
            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "PGASalaryValueDefault$");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }

    // GETTERS
    public int getRank() {return _Rank;}
    public int getSalaryValue() {return _SalaryValue;}

    // SETTERS
    public void setRank(int rank) {_Rank = rank;}
    public void setSalaryValue(int value) {_SalaryValue = value;}

    // PUBLIC METHODS

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {

            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "Rank")) {
                setRank(crs.getInt(prefix + "Rank"));
            }

            if (FSUtils.fieldExists(crs, prefix, "SalaryValue")) {
                setSalaryValue(crs.getInt(prefix + "SalaryValue"));
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

}
