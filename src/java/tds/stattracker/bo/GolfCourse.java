package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

import java.io.Serializable;

import static tds.data.CTColumnLists._Cols;

public class GolfCourse implements Serializable {

    // DB FIELDS
    private Integer _GolfCourseID;
    private String _GolfCourseName;
    private String _Address;
    private String _City;
    private String _State;
    private String _Zip;
    private String _Phone;
    private String _Website;

    // CONSTRUCTORS
    public GolfCourse() {
    }

    public GolfCourse(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    public GolfCourse(int golfCourseId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfCourse", "", ""));
            sql.append("FROM GolfCourse");
            sql.append("WHERE GolfCourseID = ").append(golfCourseId);

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

    // GETTERS
    public Integer getGolfCourseID() {return _GolfCourseID;}
    public String getGolfCourseName() {return _GolfCourseName;}
    public String getAddress() {return _Address;}
    public String getCity() {return _City;}
    public String getState() {return _State;}
    public String getZip() {return _Zip;}
    public String getPhone() {return _Phone;}
    public String getWebsite() {return _Website;}

    // SETTERS
    public void setGolfCourseID(Integer GolfCourseID) {_GolfCourseID = GolfCourseID;}
    public void setGolfCourseName(String name) {_GolfCourseName = name;}
    public void setAddress(String address) {_Address = address;}
    public void setCity(String city) {_City = city;}
    public void setState(String state) {_State = state;}
    public void setZip(String zip) {_Zip = zip;}
    public void setPhone(String phone) {_Phone = phone;}
    public void setWebsite(String website) {_Website = website;}

   // PUBLIC METHODS

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfCourse", "GolfCourseID", getGolfCourseID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfCourseID")) { setGolfCourseID(crs.getInt(prefix + "GolfCourseID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfCourseName")) { setGolfCourseName(crs.getString(prefix + "GolfCourseName")); }
            if (FSUtils.fieldExists(crs, prefix, "Address")) { setAddress(crs.getString(prefix + "Address")); }
            if (FSUtils.fieldExists(crs, prefix, "City")) { setCity(crs.getString(prefix + "City")); }
            if (FSUtils.fieldExists(crs, prefix, "State")) { setState(crs.getString(prefix + "State")); }
            if (FSUtils.fieldExists(crs, prefix, "Zip")) { setState(crs.getString(prefix + "Zip")); }
            if (FSUtils.fieldExists(crs, prefix, "Phone")) { setPhone(crs.getString(prefix + "Phone")); }
            if (FSUtils.fieldExists(crs, prefix, "Website")) { setWebsite(crs.getString(prefix + "Website")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfCourse ");
        sql.append("(GolfCourseName, Address, City, State, Zip, Phone, Website) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfCourseName()));
        sql.append(FSUtils.InsertDBFieldValue(getAddress(), true));
        sql.append(FSUtils.InsertDBFieldValue(getCity(), true));
        sql.append(FSUtils.InsertDBFieldValue(getState(), true));
        sql.append(FSUtils.InsertDBFieldValue(getZip(), true));
        sql.append(FSUtils.InsertDBFieldValue(getPhone(), true));
        sql.append(FSUtils.InsertDBFieldValue(getWebsite(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfCourse SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolfCourseName", getGolfCourseName()));
        sql.append(FSUtils.UpdateDBFieldValue("Address", getAddress(), true));
        sql.append(FSUtils.UpdateDBFieldValue("City", getCity(), true));
        sql.append(FSUtils.UpdateDBFieldValue("State", getState(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Zip", getZip(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Phone", getPhone(), true));
        sql.append(FSUtils.UpdateDBFieldValue("Website", getWebsite(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfCourseID = ").append(getGolfCourseID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
