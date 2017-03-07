package bglib.tags;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Feb 20, 2006
 * Time: 6:20:30 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TableExecutionTag {

    void addRowTotalParameter(String name,Object value, boolean clearFirst);
    int getDisplayRows();
    int getStartingRowNum();
    int getTotalRows();

}
