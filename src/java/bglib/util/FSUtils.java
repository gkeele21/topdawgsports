package bglib.util;

import bglib.data.JDBCDatabase;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;
import sun.jdbc.rowset.CachedRowSet;
import tds.main.bo.CTApplication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import static bglib.util.Application._GLOBAL_LOG;

/**
 * Class declaration
 *
 * Collection of static functions; useful utilities for bully pages
 */
public class FSUtils
{
    public static final String lf = "<br />";
    public static final int charsPerLine = 60;
    public static final String NOTRASHTALK = "No trash talk entered.";

    private FSUtils() {} // cannot instantiate

    public static boolean columnExists(CachedRowSet crs, String colName) {
        try {
            ResultSetMetaData md = crs.getMetaData();
            for (int i=1; i<=md.getColumnCount(); i++) {
                if (colName.compareToIgnoreCase(md.getColumnName(i))==0) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void runExternalCommand(String command, boolean displayOutput, boolean displayErrors)
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            if (displayOutput)
            {
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(proc.getInputStream()));
                String line;
                while ((line = br.readLine()) != null)
                {
                    System.out.println(line);
                }
            }
            if (displayErrors)
            {
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(proc.getErrorStream()));
                String line;
                while ((line = br.readLine()) != null)
                {
                    System.out.println("Error : " + line);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static boolean fileExists(String dir, String filename, Logger logger) throws Exception {
        File direct = new File(dir);

        String[] children = direct.list();
        if (children == null) {
            logger.info("File does not exist on the filesystem. (Dir : '" + dir + "' ;File : '" + filename + "')");
            return false;
        } else {
            for (String tempfilename : children) {
                // Get filename of file or directory
                if (filename.equals(tempfilename)) {
                    return true;
                }
            }
        }
        logger.info("File does not exist on the filesystem. (Dir : '" + dir + "' ;File : '" + filename + "')");
        return false;

    }

    public static void dumpMap(Map theMap) {
        Iterator keys = theMap.keySet().iterator();
        while (keys.hasNext()) {
            Object curKey = keys.next();
            if (theMap.get(curKey) instanceof Map) {
                System.out.println("***Nested map for key '" + curKey + "'");
                dumpMap((Map)theMap.get(curKey));
            }
            System.out.println("key: " + curKey + " = " + theMap.get(curKey));
        }
    }

    public static String sortedMapToString(SortedMap theMap) {
        StringBuffer ret = new StringBuffer();

        Iterator keys = theMap.keySet().iterator();
        while (keys.hasNext()) {
            Object curKey = keys.next();
            if (theMap.get(curKey) instanceof SortedMap) {
                ret.append("***Nested map for key '" + curKey + "'\n");
                ret.append(sortedMapToString((SortedMap)theMap.get(curKey)));
            }
            ret.append(curKey.toString() + ": " + theMap.get(curKey) + "\n");
        }

        return ret.toString();
    }

    public static String noNull(String value) {
        return (value==null) ? "" : value;
    }

    public static void sendMessageEmail(String to, String subject, String body) throws Exception {
        sendMessageEmail(to, subject, body, null);
    }

    public static void sendMessageEmail(String to, String subject, String body, String attachment) throws Exception {
        HashMap values = new HashMap();
        values.put("body", body);
        values.put("to", to);
        values.put("subject", subject);

        if (attachment!=null) {
            values.put("attachment", attachment);
        }

        SendEmail.sendEmail(values);
    }


    // makes a string that lists the integers in List in a SQL-friendly form. For example, in the query:
    // "select * from Team where TeamID in (3948, 4014, 4119)"
    public static String makeInList(List list) {
        if (list==null || list.size()==0) {
            return "(0)";
        }

        Iterator items = list.iterator();
        StringBuffer ret = new StringBuffer("(");

        while (items.hasNext()) {
            ret.append(items.next() + ",");
        }

        return ret.toString().substring(0, ret.length()-1) + ")";
    }

    // makes a string that lists the Strings in List in a SQL-friendly form. For example, in the query:
    // "select * from BaseballPosition where PositionName in ('1B', '2B', 'OF')"
    public static String makeInListForStrings(List list) {
        if (list==null || list.size()==0) {
            return "('')";
        }

        Iterator items = list.iterator();
        StringBuffer ret = new StringBuffer();

        while (items.hasNext()) {
            ret.append("'" + items.next() + "',");
        }

        return ret.toString().substring(0, ret.length()-1);
    }

    // returns the integer value of key, or 0 if key is missing or invalid
    public static int getIntFromMap(Map map, Object key) {
        int ret = 0;
        if (map.containsKey(key)) {
            try {
                Object val = map.get(key);
                if (val!=null) {
                    if (val instanceof Integer) {
                        ret = (Integer)val;
                    } else {
                        ret = Integer.parseInt(map.get(key).toString());
                    }
                }
            }
            catch (NumberFormatException e) {}
            catch (NullPointerException npe) {}
        }

        return ret;
    }

    public static Integer getIntegerFromMap(Map map, Object key) {
        Integer ret = null;
        if (map.containsKey(key)) {
            try {
                Object val = map.get(key);
                if (val!=null) {
                    if (val instanceof Integer) {
                        ret = (Integer)val;
                    } else {
                        ret = Integer.parseInt(map.get(key).toString());
                    }
                }
            }
            catch (NumberFormatException e) {}
            catch (NullPointerException npe) {}
        }

        return ret;
    }

    // returns the double value of key, or 0 if key is missing or invalid
    public static double getDoubleFromMap(Map map, Object key) {
        double ret = 0;
        if (map.containsKey(key)) {
            try {
                Object val = map.get(key);
                if (val!=null) {
                    if (val instanceof Number) {
                        ret = ((Number)val).doubleValue();
                    } else {
                        ret = Double.parseDouble(map.get(key).toString().replace(",", ""));
                    }
                }
            }
            catch (NumberFormatException e) {}
            catch (NullPointerException npe) {}
        }

        return ret;
    }

    public static Double getDoubleObjFromMap(Map map, Object key) {
        Double ret = null;
        if (map.containsKey(key)) {
            try {
                Object val = map.get(key);
                if (val!=null) {
                    if (val instanceof Number) {
                        ret = ((Number)val).doubleValue();
                    } else {
                        ret = Double.parseDouble(map.get(key).toString().replace(",", ""));
                    }
                }
            }
            catch (NumberFormatException e) {}
            catch (NullPointerException npe) {}
        }

        return ret;
    }

    // returns the double value of key, or 0 if key is missing or invalid
    public static double getBigDecimalFromMap(Map map, String key) {
        double ret = 0;
        if (map.containsKey(key)) {
            try {
                Object val = map.get(key);
                if (val!=null) {
                    if (val instanceof Number) {
                        ret = ((Number)val).doubleValue();
                    } else {
                        ret = Double.parseDouble(map.get(key).toString());
                    }
                }
            }
            catch (NumberFormatException e) {}
            catch (NullPointerException npe) {}
        }

        return ret;
    }

    public static boolean getBooleanFromMap(Map map, String key) {
        boolean ret = false;
        if (map.containsKey(key)) {
            try {
                Object val = map.get(key);
                if (val!=null) {
                    if (val instanceof Boolean) {
                        ret = (Boolean)map.get(key);
                    } else {
                        ret = map.get(key).toString().toLowerCase().equals("true");
                    }
                }
            }
            catch (NumberFormatException e) {}
            catch (NullPointerException npe) {}
        }

        return ret;
    }

    public static Boolean getBooleanObjFromMap(Map map, String key) {
        Boolean ret = null;
        if (map.containsKey(key)) {
            try {
                Object val = map.get(key);
                if (val!=null) {
                    if (val instanceof Boolean) {
                        ret = (Boolean)map.get(key);
                    } else {
                        ret = map.get(key).toString().toLowerCase().equals("true");
                    }
                }
            }
            catch (NumberFormatException e) {}
            catch (NullPointerException npe) {}
        }

        return ret;
    }

    // returns empty string, rather than null, if key isn't in map
    public static String getStringFromMap(Map map, String key) {
        String ret = "";
        if (map.containsKey(key)) {
            Object obj = map.get(key);
            if (obj != null) {
                ret = obj.toString();
            }
        }

        return ret;
    }

    public static AuDate getDateFromMap(Map map, String key) {
        AuDate ret=null;
        if (map.containsKey(key) && map.get(key)!=null) {
            ret = new AuDate(((java.sql.Timestamp)map.get(key)).getTime());
        }

        return ret;
    }

    public static boolean mapValueEmpty(Map map, String key) {
        return getStringFromMap(map,key).equals("");
    }

    public static AuDate getLastMonday() {
        AuDate ret = new AuDate();
        ret.set(Calendar.DAY_OF_WEEK, 2);
        ret.set(Calendar.HOUR_OF_DAY, 0);
        ret.set(Calendar.MINUTE, 0);
        ret.set(Calendar.SECOND, 0);
        return ret;
    }

    public static AuDate getLastMonday(AuDate from) {
        AuDate ret = new AuDate(from);

        int dow = ret.get(Calendar.DAY_OF_WEEK)-2;
        if (dow<0) {
            dow=6;
        }
        ret.add(Calendar.DAY_OF_YEAR, -dow);
        return ret;
    }

    public static void dumpString(String str) {
        dumpString(str, null);
    }

    public static void dumpString(String str, String encoding) {
        byte[] theBytes = null;
        if (encoding==null) {
            theBytes=str.getBytes();
        } else {
            try {
                theBytes = str.getBytes(encoding);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (theBytes!=null) {
            for (int i=0; i<theBytes.length; i++) {
                System.out.println("byte = " + Integer.toHexString((int)theBytes[i]));
            }
        }
    }

    public static void finishTransaction(Connection con, ReturnCode rc) {
        try {
            if (con!=null) {
                if (rc!=null && rc.isSuccess()) {
                    con.commit();
                } else {
                    try {
                        con.rollback();
                    } catch (SQLException e) {
                        _GLOBAL_LOG.error(e); // don't re-throw, because we don't care on a rollback
                    }
                }
            }
        } catch (Exception e1) {
            _GLOBAL_LOG.error(e1);
            throw new RuntimeException(e1);
        }
        finally {
            try {
                if (con!=null) {
                    con.close();
                }
            } catch (Exception e) {
                _GLOBAL_LOG.error(e);
            }
        }
    }

    public static List<Map<String, Object>> rsToList(ResultSet rs) throws Exception {
        // Returns a List of Maps.  Each Map represents one row returned from the DB.
        // The Maps have the keys of the columnnames (or as specified in the SQL Statement)

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        ResultSetMetaData md = rs.getMetaData();
        int numColumns = md.getColumnCount();
        while (rs.next()) {

            Map<String, Object> map = new HashMap<String, Object>();
            for (int x=1;x<=numColumns;x++) {
                String colname = md.getColumnName(x);
                Object value = rs.getObject(x);

                map.put(colname,value);

            }

            list.add(map);
        }

        return list;
    }

    public static List<Map<String, Object>> crsToList(CachedRowSet crs) throws Exception {
        // Returns a List of Maps.  Each Map represents one row returned from the DB.
        // The Maps have the keys of the columnnames (or as specified in the SQL Statement)

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        ResultSetMetaData md = crs.getMetaData();
        int numColumns = md.getColumnCount();
        while (crs.next()) {

            Map<String, Object> map = new HashMap<String, Object>();
            for (int x=1;x<=numColumns;x++) {
                String colname = md.getColumnName(x);
                Object value = crs.getObject(x);

                map.put(colname,value);

            }

            list.add(map);
        }

        crs.close();

        return list;
    }

    public static List<Map<String, Object>> crsToList(ResultSet crs, String prefix, String idCol) throws Exception {
        // Returns a List of Maps.  Each Map represents one row returned from the DB.
        // The Maps have the keys of the columnnames (or as specified in the SQL Statement)
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        ResultSetMetaData md = crs.getMetaData();
        int numColumns = md.getColumnCount();
        String preIdCol = idCol;
        if (preIdCol.indexOf('_')>=0) {
            preIdCol = idCol.substring(0, idCol.indexOf('_'));
        }
        String postIdCol = idCol.substring(Math.min(idCol.length(), preIdCol.length()+1));
        int id=0;
        if (preIdCol.length() > 0 && crs.next()) {
            id = crs.getInt(prefix + preIdCol);
            crs.previous();
        }
        while (crs.next()) {
            // first check if we've gotten to the next key id for the parent table
            if (preIdCol.length() > 0 && crs.getInt(prefix + preIdCol)!=id) {
                break;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            String subprefix="";
            for (int x=1;x<=numColumns;x++) {
                String colname = md.getColumnName(x);
                if (colname.startsWith(prefix)) {
                    String subcolname = colname.substring(prefix.length());
                    if (subcolname.contains("_")) {
                        if (subprefix.length()==0) {
                            subprefix = prefix + subcolname.substring(0, subcolname.indexOf('_'));
                        }
                    } else {
                        Object val=crs.getObject(x);
                        if (val!=null) {
                            map.put(colname, crs.getObject(x));
                        }
                    }
                }
            }

            if (subprefix.length()>0) {
                crs.previous();
                List<Map<String, Object>> subList = crsToList(crs, subprefix+"_", postIdCol);
                crs.previous();
                map.put(subprefix, subList);
            }

            if (map.keySet().size()>0) {
                list.add(map);
            }
        }

        return list;
    }

    public static List<Map<String, Object>> crsToListNewfangled(ResultSet crs, String prefix, String idCol) throws Exception {
        // Returns a List of Maps.  Each Map represents one row returned from the DB.
        // The Maps have the keys of the columnnames (or as specified in the SQL Statement)
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        ResultSetMetaData md = crs.getMetaData();
        int numColumns = md.getColumnCount();
        String preIdCol = idCol;
        if (preIdCol.indexOf('_')>=0) {
            preIdCol = idCol.substring(0, idCol.indexOf('_'));
        }
        String postIdCol = idCol.substring(Math.min(idCol.length(), preIdCol.length()+1));
        int id=0;
        if (preIdCol.length() > 0 && crs.next()) {
            try {
                id = crs.getInt(prefix + preIdCol);
            } catch (SQLException e) {
                id=-1;
                // ignore; must apply to different table join
            }
            crs.previous();
        }
        while (crs.next()) {
            // first check if we've gotten to the next key id for the parent table
            if (id==-1 || (preIdCol.length() > 0 && crs.getInt(prefix + preIdCol)!=id)) {
                break;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            List<String> subprefixes = new ArrayList<String>();
            for (int x=1;x<=numColumns;x++) {
                String colname = md.getColumnName(x);
                if (colname.startsWith(prefix)) {
                    String subcolname = colname.substring(prefix.length());
                    if (subcolname.contains("_")) {
                        String subprefix = prefix + subcolname.substring(0, subcolname.indexOf('_'));
                        if (subprefixes.contains(subprefix)==false) {
                            subprefixes.add(subprefix);
                        }
                    } else {
                        Object val=crs.getObject(x);
                        if (val!=null) {
                            map.put(colname, crs.getObject(x));
                        }
                    }
                }
            }

            for (String subprefix : subprefixes) {
                crs.previous();
                List<Map<String, Object>> subList = crsToList(crs, subprefix+"_", postIdCol);
                crs.previous();
                map.put(subprefix, subList);
            }

            if (map.keySet().size()>0) {
                list.add(map);
            }
        }

        return list;
    }

    public static String getRequestParameter(HttpServletRequest request, String paramName) {
        return getRequestParameter(request, paramName, "");
    }

    public static String getRequestParameter(HttpServletRequest request, String paramName, String defaultValue) {
        String param = noNull(request.getParameter(paramName)).trim();
        if (param.length()==0) param=defaultValue;
        return param;
    }

    public static int getIntRequestParameter(HttpServletRequest request, String paramName, int defaultValue) {
        int ret = defaultValue;

        try {
            ret = Integer.parseInt(request.getParameter(paramName));
        }
        catch (NumberFormatException e) {} // no big deal

        return ret;
    }

    public static int getIntSessionAttribute(HttpSession session, String paramName, int defaultValue) {
        int ret = defaultValue;

        try {
            Object attr = session.getAttribute(paramName);
            if (attr!=null) {
                ret = Integer.parseInt(attr.toString());
            }
        }
        catch (NumberFormatException e) {} // no big deal

        return ret;
    }

    public static double getDoubleSessionAttribute(HttpSession session, String paramName, double defaultValue) {
        double ret = defaultValue;

        try {
            Object attr = session.getAttribute(paramName);
            if (attr!=null) {
                ret = Double.parseDouble(attr.toString());
            }
        }
        catch (NumberFormatException e) {} // no big deal

        return ret;
    }

    public static double getDoubleRequestParameter(HttpServletRequest request, String paramName, double defaultValue) {
        double ret = defaultValue;

        try {
            ret = Double.parseDouble(request.getParameter(paramName));
        }
        catch (Exception e) {} // no big deal

        return ret;
    }

    public static Integer getIntegerSessionAttribute(HttpSession session, String paramName, Integer defaultValue) {
        Integer ret = defaultValue;

        try {
            Object attr = session.getAttribute(paramName);
            if (attr!=null) {
                ret = Integer.valueOf(attr.toString());
            }
        }
        catch (NumberFormatException e) {} // no big deal

        return ret;
    }

    public static Integer getIntegerRequestParameter(HttpServletRequest request, String paramName, Integer defaultValue) {
        Integer ret = defaultValue;

        try {
            ret = Integer.valueOf(request.getParameter(paramName));
        }
        catch (Exception e) {} // no big deal

        return ret;
    }

    public static String getPageName(String url,boolean includefullPath,boolean includeext) {
        System.out.println("Retrieving page name");
        String frompage = url;
        if (url != null) {
            if (!includefullPath) {
                int lastindex = url.lastIndexOf("/");
                if (lastindex > 0) {
                    frompage = url.substring(lastindex+1);
                }
            }

            // Strip parameters
            int beg = frompage.indexOf("?");
            if (beg > 0) {
                frompage = frompage.substring(0,beg);
            }

            if (!includeext) {
                int next = frompage.lastIndexOf(".");
                if (next > 0) {
                    frompage = frompage.substring(0,next);
                }
            }
        } else {
            frompage = "";
        }

        return frompage;
    }

    public static String dumpRequest(HttpServletRequest request) {
        StringBuilder ret = new StringBuilder();
        Enumeration attrNames = request.getParameterNames();
        ret.append("***Parameters***\n");
        while (attrNames.hasMoreElements()) {
            String attr = attrNames.nextElement().toString();
            ret.append(attr + " = " + request.getParameter(attr) + "\n");
        }

        attrNames = request.getAttributeNames();
        ret.append("\n***Attributes***\n");
        while (attrNames.hasMoreElements()) {
            String attr = attrNames.nextElement().toString();
            ret.append(attr + " = " + request.getAttribute(attr) + "\n");
        }

        Cookie[] cookies = request.getCookies();
        ret.append("\n***Cookies***\n");
        for (Cookie cookie: cookies) {
            ret.append(cookie.getName() + " = " + cookie.getValue() + "\n");
        }

        attrNames = request.getHeaderNames();
        ret.append("\n***Headers***\n");
        while (attrNames.hasMoreElements()) {
            String attr = attrNames.nextElement().toString();
            ret.append(attr + " = " + request.getHeader(attr) + "\n");
        }

        ret.append("\n***Other***\n");
        ret.append("authType = " + request.getAuthType() + "\n");
        ret.append("char encoding = " + request.getCharacterEncoding() + "\n");
        ret.append("content length = " + request.getContentLength() + "\n");
        ret.append("content type = " + request.getContentType() + "\n");
        ret.append("context path = " + request.getContextPath() + "\n");

        ret.append("local addr = " + request.getLocalAddr() + "\n");
        ret.append("local name = " + request.getLocalName() + "\n");
        ret.append("local port = " + request.getLocalPort() + "\n");
        ret.append("method = " + request.getMethod() + "\n");

        ret.append("path info = " + request.getPathInfo() + "\n");
        ret.append("path translated = " + request.getPathTranslated() + "\n");
        ret.append("protocol = " + request.getProtocol() + "\n");
        ret.append("query string = " + request.getQueryString() + "\n");
        ret.append("remote addr = " + request.getRemoteAddr() + "\n");
        ret.append("remote host = " + request.getRemoteHost() + "\n");
        ret.append("remote port = " + request.getRemotePort() + "\n");
        ret.append("remote user = " + request.getRemoteUser() + "\n");
        ret.append("requested session id = " + request.getRequestedSessionId() + "\n");
        ret.append("request URI = " + request.getRequestURI() + "\n");
        ret.append("request URL = " + request.getRequestURL() + "\n");
        ret.append("scheme = " + request.getScheme() + "\n");
        ret.append("server name = " + request.getServerName() + "\n");
        ret.append("server port = " + request.getServerPort() + "\n");
        ret.append("servlet path = " + request.getServletPath() + "\n");

        return ret.toString();
    }

    public static String formatTrashTalkMessage(String message) {

        if (message == null || message.equals("")) {
            return NOTRASHTALK + lf + "&#160;";
        }

        // In order for the message to display within the bubble properly,
        // we need to put a max # of chars on each line, AND the bubble
        // doesn't look good unless it has 2 and only 2 lines of text
        String result = WordUtils.wrap(message,charsPerLine,lf,false);
        return result.indexOf("<br />") > 0 ? result : result + lf + "&#160;";

    }

    // changes all trouble characters (", ', \) to their escape chars
    public static String fixupUserInputForDB(String input) {
        return StringEscapeUtils.escapeSql(input);
    }

    public static boolean isEmpty(String str) { return (str==null || str.trim().length()==0); }

    public static String getHtmlSafeString(String str) {
        return StringEscapeUtils.escapeHtml(str).replace("\n", "<br />");
    }

    public static boolean validateFields(BGUserSession session, SortedMap<FormField, String> fields) {
        StringBuffer eBfr = new StringBuffer();
        for (FormField field : fields.keySet()) {
            String val = StringEscapeUtils.unescapeHtml(fields.get(field));
            if (StringEscapeUtils.escapeHtml(val).equals(val)==false) {
                eBfr.append("Field '" + field.getFriendlyName() + "' contains illegal characters.");
                return false;
            }
            if (field.isValid(fields.get(field), eBfr)==false) {
                session.setErrorMessage(eBfr.toString());
                return false;
            }
        }

        return true;
    }

    public static String insertInStringIntoQuery(String query, String inString) {
        return query.replaceFirst("in \\(\\?", "in (" + inString);
    }

    public static void closeCRS(CachedRowSet crs) {
        if (crs!=null) {
            try {
                crs.close();
            }
            catch (SQLException e) {
                Application._GLOBAL_LOG.error(e);
            }
        }
    }

    public static void dumpCRS(CachedRowSet crs) {

        try {
            ResultSetMetaData meta = crs.getMetaData();
            int count=0;
            while (crs.next()) {
                System.out.println("***ROW #" + ++count + "***");
                for (int i=1; i<=meta.getColumnCount(); i++) {
                    System.out.println("Column '" + meta.getColumnName(i) + "' = " + crs.getObject(i));
                }
            }
            crs.beforeFirst();
        } catch (SQLException e) {
            Application._GLOBAL_LOG.error(e);
        }
    }

    public static void dumpCRStoCSV(CachedRowSet crs, String filename) {
        CsvWriter csv=null;
        try {
            csv = new CsvWriter(filename);
            ResultSetMetaData meta = crs.getMetaData();
            for (int i=1; i<=meta.getColumnCount(); i++) {
                csv.write(meta.getColumnName(i));
            }
            csv.endRecord();

            while (crs.next()) {
                for (int i=1; i<=meta.getColumnCount(); i++) {
                    Object obj = crs.getObject(i);
                    csv.write((obj!=null) ? obj.toString() : "NULL");
                }
                csv.endRecord();
            }
            crs.beforeFirst();

            csv.close();
        } catch (Exception e) {
//            Application._GLOBAL_LOG.error(e);
        }
        finally {
            if (csv!=null) {
                csv.close();
            }
        }
    }

    public static String getDefaultFieldValue(Map inputs, FormField field) {
        return getDefaultFieldValue(inputs, field, "");
    }

    public static String getDefaultFieldValue(Map inputs, FormField field, String def) {
        if (inputs==null || inputs.containsKey(field)==false) {
            return noNull(def);
        }

        return (String)inputs.get(field);
    }

    public static AuDate toDate(String dateStr, String format) {
        return new AuDate(dateStr, format);
    }

    public static AuDate toDate(String dateStr, List<String> formats) {
        if (dateStr==null) return null;

        for (String format : formats) {
            AuDate ret = new AuDate(dateStr, format);
            if (!ret.isNull() && ret.isValid()) {
                return ret;
            }
        }
        return null;
    }

    public static String getFormattedDate(AuDate date, String format) {
        if (date==null || date.isNull()) {
            return "";
        }
         return date.toString(format);
     }

    public static int compareDoubles(double d1, double d2) {
        double d3 = d2-d1;
        if (d3 > 0.001) {
            return 1;
        }
        if (d3 < -0.001) {
            return -1;
        }
        return 0;
    }

    public static DateRange parseDateRange(String input) {
        if (input==null || input.trim().length()==0) {
            return null;
        }

        AuDate fromDate=null, toDate=null;
        try {
            boolean monthOnlySpecified=false;
            String[] dates = input.split("-");
            fromDate = new AuDate(dates[0], "M/d/yyyy");
            if (fromDate.isNull()) {
                fromDate = new AuDate(dates[0], "M/yyyy");
                monthOnlySpecified=true;
            }
            if (dates.length>1) {
                toDate = new AuDate(dates[1], "M/d/yyyy");
                if (toDate.isNull()) {
                    toDate = new AuDate(dates[1], "M/yyyy");
                    if (!toDate.isNull()) {
                        toDate.add(Calendar.MONTH, 1);
                        toDate.add(Calendar.MILLISECOND, -1);
                    }
                } else {
                    toDate.add(Calendar.DAY_OF_YEAR, 1);
                    toDate.add(Calendar.MILLISECOND, -1);
                }
            } else {
                if (monthOnlySpecified) {
                    toDate = new AuDate(fromDate);
                    toDate.add(Calendar.MONTH, 1);
                    toDate.add(Calendar.MILLISECOND, -1);
                } else {
                    toDate = new AuDate(fromDate);
                    toDate.add(Calendar.DAY_OF_YEAR, 1);
                    toDate.add(Calendar.MILLISECOND, -1);
                }
            }
            if (fromDate.isNull() || toDate.isNull() || !fromDate.isValid() || !toDate.isValid()) {
                return null;
            }
        } catch (Exception e) {
            return null; // fail silently
        }

        return new DateRange(fromDate, toDate);
    }

    public static boolean buttonPressed(HttpServletRequest request, String buttonName) {
        return (request.getParameter(buttonName + ".x")!=null) || (request.getParameter(buttonName)!=null);
    }

    public static Object getMapValue(Map map, String key) {
        return map.get(key);
    }

    public static String getFieldValue(FormField field, Map<FormField, String> inputs, String defaultVal) {
        String ret = (inputs==null) ? defaultVal : inputs.get(field);

        return FSUtils.noNull(ret);
    }

    public static Object getObjectForPage(HttpServletRequest request, Class c, String param) {
        int id = FSUtils.getIntRequestParameter(request, param + "ID", 0);
        Object ret = null;

        try {
            if (id==0) {
                ret = request.getSession().getAttribute(param);
                if (ret!=null) {
                    Method getID;
                    try {
                        getID = c.getMethod("getID");
                    } catch (NoSuchMethodException e) {
                        getID = c.getMethod("get" + c.getSimpleName() + "ID");
                    }
                    id = (Integer)getID.invoke(ret);
                }
            }

            if (id>0) {
                Constructor con = c.getConstructor(Integer.TYPE);
                ret = con.newInstance(id);
                request.getSession().setAttribute(param, ret);
            }
        } catch (Exception e) {
            _GLOBAL_LOG.error(request, e);
        }

        return ret;
    }

    public static String getAppParameter(HttpServletRequest request, String param) {
        return getAppParameter(request, param, "");
    }

    public static String getAppParameter(HttpServletRequest request, String requestParam, String defaultValue) {
        return getAppParameter(request, requestParam, requestParam, defaultValue);
    }

    public static String getAppParameter(HttpServletRequest request, String requestParam, String sessionAttr, String defaultValue) {
        String ret = FSUtils.getRequestParameter(request, requestParam);
        if (ret.length()==0) {
            ret = (String)request.getSession().getAttribute(sessionAttr);
            if (ret==null || ret.trim().length()==0) {
                ret = defaultValue;
            }
        }

        ret = StringEscapeUtils.escapeHtml(ret);
        request.getSession().setAttribute(sessionAttr, ret);

        return ret;
    }

    public static boolean hasAccessLevel(BGAccessLevel a, BGAccessLevel b) {
        return (a!=null && a.encompasses(b));
    }

    public static double tryParseDouble(String in) {
        double ret=0;
        try {
            ret = Double.parseDouble(in.replace("$", "").replaceAll(",", ""));
        } catch (NumberFormatException e) {
            // ignore
        }

        return ret;
    }

    public static int tryParseInt(String in) {
        int ret=0;
        try {
            ret = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            // ignore
        }

        return ret;
    }

    public static int compareDates(AuDate a, AuDate b) {
        int diff;

        long ldiff = (a.getDateInMillis() - b.getDateInMillis());
        if (ldiff<0) {
            diff=-1;
        } else if (ldiff>0) {
            diff = 1;
        } else {
            diff = 0;
        }
        return diff;
    }

    // for calling from jsps
    public static Boolean isInList(Object o, List l) {
        return l.contains(o);
    }

    public static String canonizePhoneNumber(String str) {
        str = noNull(str).replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\-", "").replace(" ", "");
        String postfix="";
        if (str.length()>10) {
            postfix = ' ' + str.substring(10);
            str = str.substring(0, 10);
        }
        return str + postfix;
    }

    public static String stringValue(Object value,boolean returnNulls) {
        try {
            String val = value.toString();
            return val;
        } catch (Exception e) {
            if (returnNulls) {
                return null;
            } else {
                return "";
            }
        }
    }

    public static boolean fieldExists(Map fields, String prefix, String colName) {
        if (fields.containsKey(prefix + colName) && fields.get(prefix + colName) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean fieldExists(CachedRowSet crs, String prefix, String colName) {
        try {

            Object obj = crs.getObject(prefix + colName);
            if (obj != null) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }

    }

    public static String InsertDBFieldValue(Object value) {
        return InsertDBFieldValue(value, false);
    }

    public static String InsertDBFieldValue(Object value, boolean needsQuotes) {
        if (value == null) { return null + ","; }

        StringBuilder sql = new StringBuilder();
        if (needsQuotes == true) { sql.append("'"); }
        sql.append(StringEscapeUtils.escapeSql(value.toString()));
        if (needsQuotes == true) { sql.append("'"); }
        sql.append(",");
        return sql.toString();
    }

    public static String UpdateDBFieldValue(String fieldName, Object value) {
        return UpdateDBFieldValue(fieldName, value, false);
    }

    public static String UpdateDBFieldValue(String fieldName, Object value, boolean needsQuotes) {
        if (value == null) { return ""; }

        StringBuilder sql = new StringBuilder();
        sql.append(fieldName).append(" = ");
        if (needsQuotes == true) { sql.append("'"); }
        sql.append(value.toString());
        if (needsQuotes == true) { sql.append("'"); }
        sql.append(",");
        return sql.toString();
    }

    public static int GetHighestIdNumber(String tableName, String fieldName) {

        int lastId = 0;
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        try {
            sql.append("SELECT max(").append(fieldName).append(") ");
            sql.append("FROM ").append(tableName);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                lastId = crs.getInt(1);
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return lastId;
    }

    /* This method removes a record out of the DB. */
    public static void Delete (String tableName, String columnName, int value) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(tableName).append(" ");
        sql.append("WHERE ").append(columnName).append(" = ").append(value);
        try {
            CTApplication._CT_QUICK_DB.executeUpdate(sql.toString());
        } catch (Exception e) { CTApplication._CT_LOG.error(e); }
    }

    /*  This method determines if a record already exists in the DB. */
    public static boolean DoesARecordExistInDB(String tableName, String columnName, Integer value) {
        return DoesARecordExistInDB(tableName, columnName, value, null, null);
    }

    /*  This method determines if a record already exists in the DB. */
    public static boolean DoesARecordExistInDB(String tableName, String column1Name, Integer value1, String column2Name, Integer value2) {
        boolean exists = false;
        CachedRowSet crs = null;
        StringBuilder sql = new StringBuilder();

        try {
            sql.append("SELECT 1 ");
            sql.append("FROM ").append(tableName).append(" ");
            sql.append("WHERE ").append(column1Name).append(" = ").append(value1);
            if (column2Name != null || value2 != null) sql.append(" AND ").append(column2Name).append(" = ").append(value2);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                exists = true;
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
        return exists;
    }

    public static int ToInt (Integer value) {
        return (value == null) ? 0 : value.intValue();
    }
}
