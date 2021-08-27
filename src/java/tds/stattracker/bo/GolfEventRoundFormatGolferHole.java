package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class GolfEventRoundFormatGolferHole implements Serializable {
    
    // DB FIELDS
    private Integer _GolfEventRoundFormatGolferID;    
    private Integer _HoleID;
    private Integer _GameScore;
    private Double _Earnings;
    private String _Notes;
    
    // OBJECTS
    private GolfEventRoundFormatGolfer _GolfEventRoundFormatGolfer;
    private Hole _Hole;

    // CONSTRUCTORS
    public GolfEventRoundFormatGolferHole() {
    }
    
    public GolfEventRoundFormatGolferHole(int GolfEventRoundFormatGolferId, int holeId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("GolfEventRoundFormatGolferHole", "", ""));
            sql.append("FROM GolfEventRoundFormatGolferHole");
            sql.append("WHERE GolfEventRoundFormatGolferID = ").append(GolfEventRoundFormatGolferId).append(" AND HoleID = ").append(holeId);

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                InitFromCRS(crs, "");
            }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public GolfEventRoundFormatGolferHole(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolfEventRoundFormatGolferID() {return _GolfEventRoundFormatGolferID;}
    public Integer getHoleID() {return _HoleID;}
    public Integer getGameScore() {return _GameScore;}
    public Double getEarnings() {return _Earnings;}
    public String getNotes() {return _Notes;}
    public GolfEventRoundFormatGolfer getGolfEventRoundFormatGolfer() {return _GolfEventRoundFormatGolfer;} 
    public Hole getHole() {return _Hole;}     
    
    // SETTERS
    public void setGolfEventRoundFormatGolferID(Integer GolfEventRoundFormatGolferID) {_GolfEventRoundFormatGolferID = GolfEventRoundFormatGolferID;}
    public void setHoleID(Integer HoleID) {_HoleID = HoleID;}
    public void setGameScore(Integer GameScore) {_GameScore = GameScore;}
    public void setEarnings(Double Earnings) {_Earnings = Earnings;}
    public void setNotes(String Notes) {_Notes = Notes;}
    public void setGolfEventRoundFormatGolfer(GolfEventRoundFormatGolfer GolfEventRoundFormatGolfer) {_GolfEventRoundFormatGolfer = GolfEventRoundFormatGolfer;}
    public void setHole(Hole Hole) {_Hole = Hole;}

    // PUBLIC METHODS
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("GolfEventRoundFormatGolferHole", "GolfEventRoundFormatGolferID", getGolfEventRoundFormatGolferID(), "HoleID", getHoleID());
        if (doesExist) { Update(); } else { Insert(); }
    }
    
    // PRIVATE METHODS

    /*  This method populates the object from a cached row set.  */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolfEventRoundFormatGolferID")) { setGolfEventRoundFormatGolferID(crs.getInt(prefix + "GolfEventRoundFormatGolferID")); }            
            if (FSUtils.fieldExists(crs, prefix, "HoleID")) { setHoleID(crs.getInt(prefix + "HoleID")); }
            if (FSUtils.fieldExists(crs, prefix, "GameScore")) { setGameScore(crs.getInt(prefix + "GameScore")); }
            if (FSUtils.fieldExists(crs, prefix, "Earnings")) { setEarnings(crs.getDouble(prefix + "Earnings")); }            
            if (FSUtils.fieldExists(crs, prefix, "Notes")) { setNotes(crs.getString(prefix + "Notes")); }
            
            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolfEventRoundFormatGolfer$", "GolfEventRoundFormatGolferID")) { setGolfEventRoundFormatGolfer(new GolfEventRoundFormatGolfer(crs, "GolfEventRoundFormatGolfer$")); }            
            if (FSUtils.fieldExists(crs, "Hole$", "HoleID")) { setHole(new Hole(crs, "Hole$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {
        StringBuilder sql = new StringBuilder();
      
        sql.append("INSERT INTO GolfEventRoundFormatGolferHole ");
        sql.append("(GolfEventRoundFormatGolferID, HoleID, GameScore, Earnings, Notes) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolfEventRoundFormatGolferID())); 
        sql.append(FSUtils.InsertDBFieldValue(getHoleID())); 
        sql.append(FSUtils.InsertDBFieldValue(getGameScore())); 
        sql.append(FSUtils.InsertDBFieldValue(getEarnings())); 
        sql.append(FSUtils.InsertDBFieldValue(getNotes(), true));

        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE GolfEventRoundFormatGolferHole SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GameScore", getGameScore()));
        sql.append(FSUtils.UpdateDBFieldValue("Earnings", getEarnings()));
        sql.append(FSUtils.UpdateDBFieldValue("Notes", getNotes()));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolfEventRoundFormatGolferID = ").append(getGolfEventRoundFormatGolferID()).append(" AND HoleID = ").append(getHoleID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
