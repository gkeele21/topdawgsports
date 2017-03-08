package tds.stattracker.bo;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.main.bo.CTApplication;

public class HoleScore implements Serializable {

    // DB FIELDS
    private Integer _GolferRoundID;
    private Integer _HoleID;
    private Integer _GolfScoreID;
    private Integer _Strokes;
    private Integer _Putts;
    private Integer _HitFairway;
    private Integer _GIR;
    private Integer _FinalPuttDistance;
    private Integer _PenaltyStrokes;
    private Integer _UpDownAtt;
    private Integer _UpDownMade;
    private Integer _SandSaveAtt;
    private Integer _SandSaveMade;    
    private String _Notes;

    // OBJECTS
    private GolferRound _GolferRound;
    private Hole _Hole;
    private GolfScore _GolfScore;

    // CONSTRUCTORS
    public HoleScore() {
    }
    
    public HoleScore(int golferRoundId, int holeId) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("HoleScore", "", ""));
            sql.append("FROM HoleScore");
            sql.append("WHERE GolferRoundID = ").append(golferRoundId).append(" AND HoleID = ").append(holeId);

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

    public HoleScore(CachedRowSet fields, String prefix) {
        InitFromCRS(fields, prefix);
    }

    // GETTERS
    public Integer getGolferRoundID() {return _GolferRoundID;}    
    public Integer getHoleID() {return _HoleID;}
    public Integer getGolfScoreID() {return _GolfScoreID;}
    public Integer getStrokes() {return _Strokes;}
    public Integer getPutts() {return _Putts;}
    public Integer getHitFairway() {return _HitFairway;} 
    public Integer getGIR() {return _GIR;}
    public Integer getFinalPuttDistance() {return _FinalPuttDistance;}
    public Integer getPenaltyStrokes() {return _PenaltyStrokes;}
    public Integer getUpDownAtt() {return _UpDownAtt;}
    public Integer getUpDownMade() {return _UpDownMade;}
    public Integer getSandSaveAtt() {return _SandSaveAtt;}
    public Integer getSandSaveMade() {return _SandSaveMade;}
    public String getNotes() {return _Notes;}
    public GolferRound getGolferRound() {return _GolferRound;} 
    public Hole getHole() {return _Hole;} 
    public GolfScore getGolfScore() {return _GolfScore;} 
    
    // SETTERS
    public void setGolferRoundID(Integer golferRoundID) {_GolferRoundID = golferRoundID;}
    public void setHoleID(Integer HoleID) {_HoleID = HoleID;}
    public void setGolfScoreID(Integer GolfScoreID) {_GolfScoreID = GolfScoreID;}
    public void setStrokes(Integer Strokes) {_Strokes = Strokes;}
    public void setPutts(Integer Putts) {_Putts = Putts;}
    public void setHitFairway(Integer HitFairway) {_HitFairway = HitFairway;}
    public void setGIR(Integer GIR) {_GIR = GIR;}
    public void setFinalPuttDistance(Integer FinalPuttDistance) {_FinalPuttDistance = FinalPuttDistance;}
    public void setPenaltyStrokes(Integer PenaltyStrokes) {_PenaltyStrokes = PenaltyStrokes;}
    public void setUpDownAtt(Integer UpDownAtt) {_UpDownAtt = UpDownAtt;}
    public void setUpDownMade(Integer UpDownMade) {_UpDownMade = UpDownMade;}
    public void setSandSaveAtt(Integer SandSaveAtt) {_SandSaveAtt = SandSaveAtt;}
    public void setSandSaveMade(Integer SandSaveMade) {_SandSaveMade = SandSaveMade;}    
    public void setNotes(String Notes) {_Notes = Notes;}
    public void setGolferRound(GolferRound GolferRound) {_GolferRound = GolferRound;}
    public void setHole(Hole Hole) {_Hole = Hole;}
    public void setGolfScore(GolfScore GolfScore) {_GolfScore = GolfScore;}

    // PUBLIC METHODS
    
    /* Retrieves all of the scores for each hole of a particular Round of a Golfer */
    public static List<HoleScore> GetGolferRoundScores(Integer golferRoundId) {
        List<HoleScore> scores = new ArrayList<HoleScore>();
        CachedRowSet crs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("Hole", "h.", "Hole$")).append(", ");
            sql.append(_Cols.getColumnList("GolferRound", "r.", "GolferRound$")).append(", ");
            sql.append(_Cols.getColumnList("HoleScore", "hs.", "")).append(", ");
            sql.append(_Cols.getColumnList("GolfScore", "gs.", "GolfScore$"));
            sql.append("FROM Hole h ");
            sql.append("JOIN Course c ON c.CourseID = h.CourseID ");
            sql.append("JOIN GolferRound r ON r.CourseID = c.CourseID ");
            sql.append("LEFT JOIN HoleScore hs ON hs.GolferRoundID = r.GolferRoundID AND hs.HoleID = h.HoleID ");
            sql.append("LEFT JOIN GolfScore gs ON gs.GolfScoreID = hs.GolfScoreID ");
            sql.append("WHERE r.GolferRoundID = ").append(golferRoundId).append(" ");
            sql.append("ORDER BY h.HoleNumber");

            crs = CTApplication._CT_QUICK_DB.executeQuery(CTApplication._CT_DB.getConn(false), sql.toString());
            while (crs.next()) {
                HoleScore objHoleScore = new HoleScore(crs,"");
                scores.add(objHoleScore);
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return scores;
    }
    
    public void Save() {
        boolean doesExist = FSUtils.DoesARecordExistInDB("HoleScore", "GolferRoundID", getGolferRoundID(), "HoleID", getHoleID());
        if (doesExist) { Update(); } else { Insert(); }
    }
   
    // PRIVATE METHODS
   
    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void InitFromCRS(CachedRowSet crs, String prefix) {        
        try {
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "GolferRoundID")) { setGolferRoundID(crs.getInt(prefix + "GolferRoundID")); }
            if (FSUtils.fieldExists(crs, prefix, "HoleID")) { setHoleID(crs.getInt(prefix + "HoleID")); }            
            if (FSUtils.fieldExists(crs, prefix, "Strokes")) { setStrokes(crs.getInt(prefix + "Strokes")); }
            if (FSUtils.fieldExists(crs, prefix, "GolfScoreID")) { setGolfScoreID(crs.getInt(prefix + "GolfScoreID")); }
            if (FSUtils.fieldExists(crs, prefix, "GIR")) { setGIR(crs.getInt(prefix + "GIR")); }
            if (FSUtils.fieldExists(crs, prefix, "Putts")) { setPutts(crs.getInt(prefix + "Putts")); }
            if (FSUtils.fieldExists(crs, prefix, "HitFairway")) { setHitFairway(crs.getInt(prefix + "HitFairway")); }            
            if (FSUtils.fieldExists(crs, prefix, "UpDownAtt")) { setUpDownAtt(crs.getInt(prefix + "UpDownAtt")); }
            if (FSUtils.fieldExists(crs, prefix, "UpDownMade")) { setUpDownMade(crs.getInt(prefix + "UpDownMade")); }
            if (FSUtils.fieldExists(crs, prefix, "SandSaveAtt")) { setSandSaveAtt(crs.getInt(prefix + "SandSaveAtt")); }
            if (FSUtils.fieldExists(crs, prefix, "SandSaveMade")) { setSandSaveMade(crs.getInt(prefix + "SandSaveMade")); }
            if (FSUtils.fieldExists(crs, prefix, "PenaltyStrokes")) { setPenaltyStrokes(crs.getInt(prefix + "PenaltyStrokes")); }            
            if (FSUtils.fieldExists(crs, prefix, "FinalPuttDistance")) { setFinalPuttDistance(crs.getInt(prefix + "FinalPuttDistance")); }
            if (FSUtils.fieldExists(crs, prefix, "Notes")) { setNotes(crs.getString(prefix + "Notes")); }            

            // OBJECTS
            if (FSUtils.fieldExists(crs, "GolferRound$", "GolferRoundID")) { setGolferRound(new GolferRound(crs, "GolferRound$")); }
            if (FSUtils.fieldExists(crs, "Hole$", "HoleID")) { setHole(new Hole(crs, "Hole$")); }
            if (FSUtils.fieldExists(crs, "GolfScore$", "GolfScoreID")) { setGolfScore(new GolfScore(crs, "GolfScore$")); }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
    
    private void Insert() {       
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO HoleScore ");
        sql.append("(GolferRoundID, HoleID, GolfScoreID, Strokes, Putts, HitFairway, GIR, FinalPuttDistance, PenaltyStrokes, UpDownAtt, UpDownMade, SandSaveAtt, SandSaveMade, Notes) ");
        sql.append("VALUES (");
        sql.append(FSUtils.InsertDBFieldValue(getGolferRoundID()));
        sql.append(FSUtils.InsertDBFieldValue(getHoleID()));
        sql.append(FSUtils.InsertDBFieldValue(getGolfScoreID()));
        sql.append(FSUtils.InsertDBFieldValue(getStrokes()));
        sql.append(FSUtils.InsertDBFieldValue(getPutts()));
        sql.append(FSUtils.InsertDBFieldValue(getHitFairway()));
        sql.append(FSUtils.InsertDBFieldValue(getGIR()));
        sql.append(FSUtils.InsertDBFieldValue(getFinalPuttDistance()));
        sql.append(FSUtils.InsertDBFieldValue(getPenaltyStrokes()));
        sql.append(FSUtils.InsertDBFieldValue(getUpDownAtt()));
        sql.append(FSUtils.InsertDBFieldValue(getUpDownMade()));
        sql.append(FSUtils.InsertDBFieldValue(getSandSaveAtt()));
        sql.append(FSUtils.InsertDBFieldValue(getSandSaveMade()));
        sql.append(FSUtils.InsertDBFieldValue(getNotes()));
        sql.deleteCharAt(sql.length()-1).append(")");
        
        try {
            CTApplication._CT_QUICK_DB.executeInsert(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }    

    private void Update() {
        StringBuilder sql = new StringBuilder();

        sql.append("UPDATE HoleScore SET ");
        sql.append(FSUtils.UpdateDBFieldValue("GolfScoreID", getGolfScoreID()));
        sql.append(FSUtils.UpdateDBFieldValue("Strokes", getStrokes()));
        sql.append(FSUtils.UpdateDBFieldValue("Putts", getPutts()));
        sql.append(FSUtils.UpdateDBFieldValue("HitFairway", getHitFairway()));    
        sql.append(FSUtils.UpdateDBFieldValue("GIR", getGIR()));
        sql.append(FSUtils.UpdateDBFieldValue("FinalPuttDistance", getFinalPuttDistance()));
        sql.append(FSUtils.UpdateDBFieldValue("PenaltyStrokes", getPenaltyStrokes()));
        sql.append(FSUtils.UpdateDBFieldValue("UpDownAtt", getUpDownAtt()));
        sql.append(FSUtils.UpdateDBFieldValue("UpDownMade", getUpDownMade()));
        sql.append(FSUtils.UpdateDBFieldValue("SandSaveAtt", getSandSaveAtt()));
        sql.append(FSUtils.UpdateDBFieldValue("SandSaveMade", getSandSaveMade()));
        sql.append(FSUtils.UpdateDBFieldValue("Notes", getNotes(), true));
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append("WHERE GolferRoundID = ").append(getGolferRoundID()).append(" AND HoleID = ").append(getHoleID());

        try {
            CTApplication._CT_QUICK_DB.executeUpdate(CTApplication._CT_DB.getConn(true), sql.toString());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}