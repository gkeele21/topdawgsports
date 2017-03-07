package bglib.control;

import bglib.util.Application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.jdbc.rowset.CachedRowSet;

public class Common {

    protected static final String jspExtension = ".jsp";
    protected static final String viewControllerExtension = ".html";
    protected static final String actionControllerExtension = ".do";
    protected static final String viewString = "View";
    protected static final String actionString = "Action";
    protected static final String forwardDirective = "forward:";
    protected static final String fileDirective = "file:";    
    protected static final String xmlDirective = "xml:";    
    protected static final String JSONDirective = "JSON:";    

    public String process(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    protected static String getPage(String uri) {

        String page = null;

        if (uri == null) {
            return page;
        }

        // Find pagename
        int pgindex = uri.lastIndexOf("/");
        if (pgindex >= 0) {
            uri = uri.substring(pgindex+1);
        }

        // Find extension
        int dotindex = uri.lastIndexOf(".");
        if (dotindex <= 0) {
            return page;
        }

        try {
            page = uri.substring(0,dotindex);

        } catch (Exception e) {
            Application._GLOBAL_LOG.error(e);
        }

        return page;
    }

    protected static String getWebAppFromUri(String uri) {
        if (uri==null) return null;

        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }

        int slash = uri.indexOf("/");
        return uri.substring(0, (slash<0) ? uri.length() : slash);
    }

    protected static String getAppPrefixFromDir(String dir) {
        String ret=null;
        try {
            CachedRowSet crs = Application._GLOBAL_QUICK_DB.executeQuery("select ApplicationPrefix from Application " +
                    "where ApplicationDir='" + dir + "'");

            if (crs.next()) {
                ret = crs.getString("ApplicationPrefix");
            }
        } catch (Exception e) {
            Application._GLOBAL_LOG.error(e);
        }

        return ret;
    }

}
