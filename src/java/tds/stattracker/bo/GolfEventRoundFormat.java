package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

import java.io.Serializable;

import static tds.data.CTColumnLists._Cols;

public class GolfEventRoundFormat implements Serializable {

    // CONSTANTS
    public enum FormatStyle {Team, Individual};

    // DB FIELDS
    private Integer _GolfEventRoundFormatID;
    private Integer _GolfFormatID;
    private Integer _GolfEventRoundID;
    private String _FormatStyle;
    private Integer _StartingHoleNumber;
    private Integer _NumHoles;
    private Double _Fee;
    private String _Notes;

    // OBJECTS
    private GolfFormat _GolfFormat;
    private GolfEventRound _GolfEventRound;

    // CONSTRUCTORS
    public GolfEventRoundFormat() {
    }

    public GolfEventRoundFormat(int golfEventRoundFormatId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRoundFormat", "", ""));
            sql.append("FROM GolfEventRoundFormat");
            sql.append("WHERE GolfEventRoundFormatID = ").append(golfEventRoundFormatId);

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

    public GolfEventRoundFormat(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfEventRoundFormatID() {return _GolfEventRoundFormatID;}
    public Integer getGolfFormatID() {return _GolfFormatID;}
    public Integer getGolfEventRoundID() {return _GolfEventRoundID;}
    public String getFormatStyle() {return _FormatStyle;}
    public Integer getStartingHoleNumber() {return _StartingHoleNumber;}
    public Integer getNumHoles() {return _NumHoles;}
    public Double getFee() {return _Fee;}
    public String getNotes() {return _Notes;}
    public GolfFormat getGolfFormat() {return _GolfFormat;}
    public GolfEventRound getGolfEventRound() {return _GolfEventRound;}

    // SETTERS
    public void setGolfEventRoundFormatID(Integer GolfEventRoundFormatID) {_GolfEventRoundFormatID = GolfEventRoundFormatID;}
    public void setGolfFormatID(Integer GolfFormatID) {_GolfFormatID = GolfFormatID;}
    public void setGolfEventRoundID(Integer GolfEventRoundID) {_GolfEventRoundID = GolfEventRoundID;}
    public void setFormatStyle(String FormatStyle) {_FormatStyle = FormatStyle;}
    public void setStartingHoleNumber(Integer StartingHoleNumber) {_StartingHoleNumber = StartingHoleNumber;}
    public void setNumHoles(Integer NumHoles) {_NumHoles = NumHoles;}
    public void setFee(Double Fee) {_Fee = Fee;}
    public void setNotes(String Notes) {_Notes = Notes;}
    public void setGolfFormat(GolfFormat GolfFormat) {_GolfFormat = GolfFormat;}
    public void setGolfEventRound(GolfEventRound GolfEventRound) {_GolfEventRound = GolfEventRound;}

    // PUBLIC METHODS

    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEventRoundFormat", "GolfEventRoundFormatID", getGolfEventRoundFormatID());
        if (doesExist) { Update(); } else { Insert(); }
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundFormatID")) { setGolfEventRoundFormatID(crs.getInt(prefix + "GolfEventRoundFormatID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfFormatID")) { setGolfFormatID(crs.getInt(prefix + "GolfFormatID")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundID")) { setGolfEventRoundID(crs.getInt(prefix + "GolfEventRoundID")); }
            if (FSUtils.fieldExists(crs, prefix, "FormatStyle")) { setFormatStyle(crs.getString(prefix + "FormatStyle")); }
            if (FSUtils.fieldExists(crs, prefix, "StartingHoleNumber")) { setStartingHoleNumber(crs.getInt(prefix + "StartingHoleNumber")); }
            if (FSUtils.fieldExists(crs, prefix, "NumHoles")) { setNumHoles(crs.getInt(prefix + "NumHoles")); }
            if (FSUtils.fieldExists(crs, prefix, "Fee")) { setFee(crs.getDouble(prefix + "Fee")); }
            if (FSUtils.fieldExists(crs, prefix, "Notes")) { setNotes(crs.getString(prefix + "Notes")); }

            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolfFormat$", "GolfFormatID")) { setGolfFormat(new GolfFormat(crs, "GolfFormat$")); }
            if (FSUtils.fieldExists(crs, "GolfEventRound$", "GolfEventRoundID")) { setGolfEventRound(new GolfEventRound(crs, "GolfEventRound$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Insert() {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO GolfEventRoundFormat ");
        sql.append("(GolfFormatID, GolfEventRoundID, FormatStyle, StartingHoleNumber, NumHoles, Fee, Notes) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfFormatID()));
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventRoundID()));
        sql.append(FSUtils.InsertDBFieldValue(getFormatStyle(), true));
        sql.append(FSUtils.InsertDBFieldValue(getStartingHoleNumber()));
        sql.append(FSUtils.InsertDBFieldValue(getNumHoles()));
        sql.append(FSUtils.InsertDBFieldValue(getFee()));
        sql.append(FSUtils.InsertDBFieldValue(getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(")");

        try {
            CTApplication._CT_QUICK_DB.executeInsert(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfEventRoundFormat SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolfFormatID", getGolfFormatID()));
        sql.append(FSUtils.UpdateDBFieldValue("GolfEventRoundID", getGolfEventRoundID()));
        sql.append(FSUtils.UpdateDBFieldValue("FormatStyle", getFormatStyle(), true));
        sql.append(FSUtils.UpdateDBFieldValue("StartingHoleNumber", getStartingHoleNumber()));
        sql.append(FSUtils.UpdateDBFieldValue("NumHoles", getNumHoles()));
        sql.append(FSUtils.UpdateDBFieldValue("Fee", getFee()));
        sql.append(FSUtils.UpdateDBFieldValue("Notes", getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventRoundFormatID = ").append(getGolfEventRoundFormatID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }


}
