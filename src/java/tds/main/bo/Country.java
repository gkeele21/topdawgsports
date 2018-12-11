package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.data.CTDataSetDef;

public class Country {
    
    // DB FIELDS
    private int _CountryID;
    private String _Country;
    private String _CountryFull;
    
    // CONSTRUCTORS
    public Country() {        
    }

    public Country(int countryID) {
        CachedRowSet userData = CTApplication._CT_DB.getDataSet(CTDataSetDef.COUNTRY_BY_COUNTRYID, countryID);
        initFromCRS(userData, "");
    }

    public Country(Connection con, int countryID) {
        CachedRowSet userData = CTApplication._CT_DB.getDataSet(con, CTDataSetDef.COUNTRY_BY_COUNTRYID, countryID);
        initFromCRS(userData, "");
    }

    public Country(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public Country(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }
    
    // GETTERS
    public int getCountryID() {return _CountryID;}
    public String getCountry() {return _Country;}
    public String getCountryFull() {return _CountryFull;}
    
    // SETTERS
    public void setCountryID(int CountryID) {_CountryID = CountryID;}    
    public void setCountry(String Country) {_Country = Country;}    
    public void setCountryFull(String CountryFull) {_CountryFull = CountryFull;}
    
    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {
            
            // DB FIELDS            
            if (FSUtils.fieldExists(crs, prefix, "CountryID")) {
                setCountryID(crs.getInt(prefix + "CountryID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Country")) {
                setCountry(crs.getString(prefix + "Country"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "CountryFull")) {
                setCountryFull(crs.getString(prefix + "CountryFull"));
            }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    public static List<Country> ReadAll(String orderby) throws Exception {
        List<Country> countries = new ArrayList<Country>();

        if (orderby == null || orderby.equals("")) {
            orderby = " Country asc";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(_Cols.getColumnList("Country","cnt.", "Country$"));
        sql.append(" FROM Country cnt");
        sql.append(" ORDER BY ").append(orderby);

        CachedRowSet crs = null;
        Connection con = null;
        try {
            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                Country country = new Country(crs, "Country$");
                countries.add(country);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }
        
        return countries;
    }
}