package tds.data;

import java.util.List;
import java.util.Map;
import java.sql.Connection;
import sun.jdbc.rowset.CachedRowSet;

public interface  CTDataSource {

//    List<Map<String, Object>> getDataSet(CTDataSetDef dsd, Object ... args);
//    List<Map<String, Object>> getDataSet(Connection con, CTDataSetDef dsd, Object ... args);
    CachedRowSet getDataSet(CTDataSetDef dsd, Object ... args);
    CachedRowSet getDataSet(Connection con, CTDataSetDef dsd, Object ... args);
    int updateDataSet(CTDataSetDef dsd, Object ... args);
    int updateDataSet(Connection con, CTDataSetDef dsd, Object ... args);
    int insertDataSet(CTDataSetDef dsd, String storedProcedureCall, Object ... args);
    int insertDataSet(Connection con, CTDataSetDef dsd, String storedProcedureCall, Object ... args);

    Connection getConn(boolean autoCommit);

}
