package bglib.control;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import java.util.*;
import java.util.logging.Level;
import java.io.IOException;
import bglib.util.*;
import org.apache.commons.lang.StringEscapeUtils;
import sun.jdbc.rowset.CachedRowSet;

public class BGServlet extends HttpServlet {

    public static final String VALID_USER_ATTR = "validUser";
    public static final String PAGE_NAME_ATTR = "pageName";
    public static final String ERROR_PAGE = "ErrorPage" + Common.jspExtension;
    private static final Map<String, Application> _WEBAPPS = new HashMap<String, Application>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        AuTimer.init();
        Application theApp=null;

        String webapp = FSUtils.noNull(request.getContextPath());
        if (webapp.length()>0) {
            webapp = webapp.substring(1);
        } else {
            webapp = request.getSession().getServletContext().getServletContextName();
        }
        if (_WEBAPPS.containsKey(webapp)) {
            theApp = _WEBAPPS.get(webapp);
        } else {
            try {
                theApp = Application.getInstance(Common.getAppPrefixFromDir(webapp));
                _WEBAPPS.put(webapp, theApp);
                Log.getInstance(theApp.getAppPrefix()).logMessage(Level.FINEST, "BGServlet.processRequest: Instantiated application '" + webapp + "'");
            } catch (Throwable e) {
                Application._GLOBAL_LOG.error(e);
            }
        }

        Log.getInstance(theApp.getAppPrefix()).logMessage(Level.FINER, "Begin QueryCount = " + theApp.getQuickDB().queryCount);

        if (theApp==null) { // shouldn't happen, because otherwise Tomcat shouldn't have sent us this page
            RequestDispatcher rd = request.getRequestDispatcher(ERROR_PAGE);
            rd.forward(request,response);
            return;
        }

        try {
            request.setAttribute(PAGE_NAME_ATTR, Common.getPage(request.getRequestURI()));
            if (request.getRequestURI().endsWith(".do")) {
                ActionController ac = new ActionController(this, request, response, theApp);
                ac.processRequest();
                recordAction(ac);
            } else {
                ViewController vc = new ViewController(this, request, response, theApp);
                vc.processRequest();
                recordPageView(vc);
            }
        } catch (Throwable e) {
            Log.getInstance(theApp.getAppPrefix()).error(request, e);
            request.setAttribute("exception", e);
            System.out.println("isCommitted=" + response.isCommitted());
            if (!response.isCommitted()) {
                response.sendRedirect("ErrorPage.jsp");
            }
        }

        Log.getInstance(theApp.getAppPrefix()).logMessage(Level.FINER, "End QueryCount = " + theApp.getQuickDB().queryCount);
    }

    private void recordPageView(ViewController vc) {
        vc.getApplication().getLogger().logMessage(Level.FINE, "page " + vc.getPageName() + " took: " + vc.getDuration() + " ms.");
        Level logLevel = Level.parse(vc.getApplication().getAppSettings().getProperty(AppSettings.LOG_LEVEL_PROP, "Severe").toUpperCase());
        if (logLevel.intValue() <= Level.INFO.intValue()) {
            try {
                Application._GLOBAL_QUICK_DB.executeUpdate("insert into PageView(ApplicationID, PageName, ProcessTime) " +
                        "values (" + vc.getApplication().getAppID() + ", '" + StringEscapeUtils.escapeSql(vc.getPageName()) + "', " +
                        vc.getDuration() + ")");
            } catch (Exception e) {
                Application._GLOBAL_LOG.error(e);
            }
        }
    }

    private void recordAction(ActionController ac) {
        ac.getApplication().getLogger().logMessage(Level.FINE, "page " + ac.getPageName() + " took: " + ac.getDuration() + " ms.");
        Level logLevel = Level.parse(ac.getApplication().getAppSettings().getProperty(AppSettings.LOG_LEVEL_PROP, "Severe").toUpperCase());
        if (logLevel.intValue() <= Level.INFO.intValue()) {
            try {
                Application._GLOBAL_QUICK_DB.executeUpdate("insert into PageView(ApplicationID, PageName, ProcessTime) " +
                        "values (" + ac.getApplication().getAppID() + ", '" + StringEscapeUtils.escapeSql(ac.getPageName()) + "', " +
                        ac.getDuration() + ")");
            } catch (Exception e) {
                Application._GLOBAL_LOG.error(e);
            }
        }
    }

    private void recordPageView(Application app, String type, String pageName, long duration) {
        Level logLevel = Level.parse(app.getAppSettings().getProperty(AppSettings.LOG_LEVEL_PROP, "Severe").toUpperCase());
        if (logLevel.intValue() <= Level.FINE.intValue()) {
            System.out.println("page " + pageName + " took: " + duration + " ms.");
        }
        if (logLevel.intValue() <= Level.INFO.intValue()) {
            try {
                Thread t = new Thread(new RecordPageView(app, type, pageName, duration));
                t.start();
            } catch (Exception e) {
                Application._GLOBAL_LOG.error(e);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "BG Servlet";
    }

    public static void main(String[] args) {
        try {
            int minCount=10;
            if (args.length>0) {
                minCount = Integer.parseInt(args[0]);
            }

            CachedRowSet crs = Application._GLOBAL_QUICK_DB.executeQuery("SELECT PageName, ProcessTime " +
                    "FROM         PageView\n" +
                    "WHERE     (ProcessTime <> - 1)\n");

            Map<String, List<Long>> pageMap = new HashMap<String, List<Long>>();
            while (crs.next()) {
//                System.out.println("pagename = " + crs.getString("PageName") + ", " + crs.getLong("ProcessTime"));
                if (!pageMap.containsKey(crs.getString("PageName"))) {
                    pageMap.put(crs.getString("PageName"), new ArrayList<Long>());
                }
                pageMap.get(crs.getString("PageName")).add(crs.getLong("ProcessTime"));
            }

            List<PageData> medianData = new ArrayList<PageData>();
            for (String str : pageMap.keySet()) {
                List<Long> list = pageMap.get(str);
                Collections.sort(list);
                medianData.add(new PageData(str, list.get(list.size()/2), list.size()));
            }

            Collections.sort(medianData);
            for (PageData pd : medianData) {
                if (pd.count > minCount)
                    System.out.println(pd);
            }


        } catch (Exception e) {
            Application._GLOBAL_LOG.error(e);
        }
    }
}

class PageData implements Comparable {
    String name;
    long median;
    int count;

    PageData(String n, long m, int c) {
        name = n;
        median = m;
        count = c;
    }

    public int compareTo(Object other) {
        return (int)(((PageData)other).median - median);
    }

    public String toString() { return name + ": count = " + count + ", median = " + median; }
}

class RecordPageView implements Runnable {
    Application app;
    String type;
    String pageName;
    long duration;

    RecordPageView(Application a, String t, String p, long d) {
        app=a; type=t; pageName=p; duration=d;
    }

    public void run() {
        try {
            Application._GLOBAL_QUICK_DB.executeUpdate("insert into PageView(ApplicationID, PageName, Type, ProcessTime) " +
                    "values (" + app.getAppID() + ", '" + StringEscapeUtils.escapeSql(pageName) + "', '" + type + "', " + duration + ")");
        } catch (Exception e) {
            Application._GLOBAL_LOG.error(e);
        }
    }
}