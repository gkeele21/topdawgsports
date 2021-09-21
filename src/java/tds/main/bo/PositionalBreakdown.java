package tds.main.bo;

import bglib.util.FSUtils;
import sun.jdbc.rowset.CachedRowSet;

public class PositionalBreakdown {
    
    // DB FIELDS
    private int _WeekNo;
    private String _Opponent;
    private double _QBPts;
    private double _RBPts;
    private double _WRPts;
    private double _TEPts;
    private double _WRTEPts;
    private double _PKPts;
    private double _DLPts;
    private double _LBPts;
    private double _DBPts;
    private double _MyPts;
    private double _OppPts;
    private double _TotalDiff;
    
    // CONSTRUCTORS
    public PositionalBreakdown() {
    }
    
    public PositionalBreakdown(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS
    public int getWeekNo() { return _WeekNo; }
    public String getOpponent() { return _Opponent; }
    public double getQBPts() { return _QBPts; }
    public double getRBPts() { return _RBPts; }
    public double getWRPts() { return _WRPts; }
    public double getTEPts() { return _TEPts; }
    public double getWRTEPts() { return _WRTEPts; }
    public double getPKPts() { return _PKPts; }
    public double getDLPts() { return _DLPts; }
    public double getLBPts() { return _LBPts; }
    public double getDBPts() { return _DBPts; }
    public double getMyPts() { return _MyPts; }
    public double getOppPts() { return _OppPts; }
    public double getTotalDiff() { return _TotalDiff; }
    
    // SETTERS
    public void setWeekNo(int WeekNo) { _WeekNo = WeekNo; }
    public void setOpponent(String Opponent) { _Opponent = Opponent; }
    public void setQBPts(double QBPts) { _QBPts = QBPts; }  
    public void setRBPts(double RBPts) { _RBPts = RBPts; }
    public void setWRPts(double WRPts) { _WRPts = WRPts; }
    public void setTEPts(double TEPts) { _TEPts = TEPts; }
    public void setWRTEPts(double WRTEPts) { _WRTEPts = WRTEPts; }
    public void setPKPts(double PKPts) { _PKPts = PKPts; }
    public void setDLPts(double DLPts) { _DLPts = DLPts; }
    public void setLBPts(double LBPts) { _LBPts = LBPts; }
    public void setDBPts(double DBPts) { _DBPts = DBPts; }
    public void setMyPts(double MyPts) { _MyPts = MyPts; }
    public void setOppPts(double OppPts) { _OppPts = OppPts; }
    public void setTotalDiff(double TotalDiff) { _TotalDiff = TotalDiff; }
    
    // PRIVATE METHODS
    
    /*  This method populates the object from a cached row set.  */
    private void initFromCRS(CachedRowSet crs, String prefix) {

        try {
            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "Week")) {
                setWeekNo(crs.getInt(prefix + "Week"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Opp")) {
                setOpponent(crs.getString(prefix + "Opp"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "QB")) {
                setQBPts(crs.getDouble(prefix + "QB"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "RB")) {
                setRBPts(crs.getDouble(prefix + "RB"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "WR")) {
                setWRPts(crs.getDouble(prefix + "WR"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "TE")) {
                setTEPts(crs.getDouble(prefix + "TE"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "WRTE")) {
                setWRTEPts(crs.getDouble(prefix + "WRTE"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "PK")) {
                setPKPts(crs.getDouble(prefix + "PK"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "DL")) {
                setDLPts(crs.getDouble(prefix + "DL"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "LB")) {
                setLBPts(crs.getDouble(prefix + "LB"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "DB")) {
                setDBPts(crs.getDouble(prefix + "DB"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "MyPts")) {
                setMyPts(crs.getDouble(prefix + "MyPts"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "OppPts")) {
                setOppPts(crs.getDouble(prefix + "OppPts"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "TotalDiff")) {
                setTotalDiff(crs.getDouble(prefix + "TotalDiff"));
            }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
