package bglib.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static bglib.util.Application._GLOBAL_QUICK_DB;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Nov 17, 2006
 * Time: 11:03:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileUtils {

    public static void importFromFixedWidth(String dir,String filename,String tablename,boolean truncateTableFirst,int startingLineNumber,int minimumLineLength,String badlineString,String[] columnnames,int[] columnpositions, Logger logger) throws Exception {

        logger.info("Importing rows from file : " + dir + "/" + filename);
        try {
            // clear out any existing rows.
            if (truncateTableFirst) {
                _GLOBAL_QUICK_DB.executeUpdate("delete from " + tablename);
            }

            InputStream fis = new FileInputStream(dir+"/"+filename);
            byte[] byteArr = new byte[100000];
            fis.read(byteArr);

            String str = new String(byteArr, "utf8");

            List columns = Arrays.asList(columnnames);

            StringTokenizer st = new StringTokenizer(str,"\n");
            int linenumber = 1;
            while (st.hasMoreTokens()) {
                try {
                    String line = st.nextToken();
                    if (line != null && linenumber >= startingLineNumber && !line.startsWith(badlineString) && line.length() > minimumLineLength) {
                        logger.info("line = " + line);
                        int x = 0;
                        int numpos = columnpositions.length;

                        int size = line.length();
                        String[] player = new String[columns.size()];

                        while (x < numpos) {
                            int startpos = columnpositions[x] - 1;
                            int endpos = x == numpos-1 ? size-1 : columnpositions[x+1]-2;

                            String value = line.substring(startpos,endpos).trim();
                            if (value == null) {
                                value = "";
                            }
                            player[x] = value;
                            x++;
                        }

                        if (player[0] != null && !player[0].equals("")) {
                            // insert this player's data
                            StringBuffer sql = new StringBuffer();
                            sql.append("insert into " + tablename + " (");
                            for (x = 0;x < columns.size();x++) {
                                String colname = columnnames[x];
                                sql.append(colname);
                                if (x != columns.size() - 1) {
                                    sql.append(",");
                                }
                            }
                            sql.append(") values (");
                            for (x = 0;x < player.length; x++) {
                                String value = player[x];
                                sql.append("'" + FSUtils.fixupUserInputForDB(value) + "'");
                                if (x != player.length - 1) {
                                    sql.append(",");
                                }
                            }

                            sql.append(")");
                            logger.info(sql.toString());
                            _GLOBAL_QUICK_DB.executeUpdate(sql.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE, "Exception", e);
                }
                linenumber++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Exception", e);
        }

        logger.info("Done importing rows.");

    }

}
