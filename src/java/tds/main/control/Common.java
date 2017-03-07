package tds.main.control;


import bglib.util.Application;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: gkeele
 * Date: Feb 16, 2007
 * Time: 11:14:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class Common {

    public static final String jspExtension = ".jsp";
    public static final String viewControllerExtension = ".htm";
    public static final String actionControllerExtension = ".do";
    public static final String viewString = "View";
    public static final String actionString = "Action";
    public static final String mainPackage = "tds";
    public static final String mainControlPackage = "tds.main.control";
    public static final String forwardDirective = "forward:";
    public static final String host = Application._GLOBAL_SETTINGS.getProperty("tds.Host");
    
    public static final int PACKAGE = 0;
    public static final int HTMLPAGE = 1;
    public static final int HTMLPAGENOEXT = 2;
    
//    protected String pageData[];
//    protected String htmlPage = "";
//    protected String thisPage = "";
    
    public String process(HttpServletRequest request, HttpServletResponse response) {
        //thisPage = getPage(request.getRequestURI());
        //htmlPage = getHTMLPage(request.getRequestURI());
        //pageData = getPageData(request.getRequestURI());
        
        return null;
    }

//    protected static String[] getPageData(String uri) {
//        System.out.println("Here in Common.getPage with uri '" + uri + "'");
//        String pageData[] = {"","",""};
//
//        if (uri == null) {
//            return pageData;
//        }
//        
//        // Find package
//        String pckg = "";
//        int pkindex = uri.indexOf("/",2);
//        if (pkindex > 0) {
//            int last = uri.lastIndexOf("/");
//            if (last > pkindex) {
//                pckg = uri.substring(pkindex+1,last+1);
//                pckg = AuUtil.replace(pckg,"/",".");
//            }
//        }
//        System.out.println("Package : " + pckg);
//        pageData[PACKAGE] = pckg;
//        
//        // Find pagename
//        int pgindex = uri.lastIndexOf("/");
//        if (pgindex >= 0) {
//            uri = uri.substring(pgindex+1);
//        }
//
//        pageData[HTMLPAGE] = uri;
//        
//        // Find extension
//        int dotindex = uri.lastIndexOf(".");
//        if (dotindex <= 0) {
//            return pageData;
//        }
//
//        try {
//            pageData[HTMLPAGENOEXT] = pckg+uri.substring(0,dotindex);
//            System.out.println("In Common Page HTMLPAGENOEXT : " + pageData[HTMLPAGENOEXT]);
//
//        } catch (Exception e) {
//            _CT_LOG.error(e);
//        }
//
//        return pageData;
//    }

}
