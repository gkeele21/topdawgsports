package bglib.control;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import java.util.*;
import java.util.logging.Level;
import java.io.IOException;

import bglib.util.Application;
import bglib.util.AppSettings;
import bglib.util.AuTimer;
import bglib.util.Log;

public class ViewController {

    public static final String ERROR_PAGE = "error";
    private static final Random _RAND = new Random();
    private static final Map<String, BGBaseView> _PAGES = new HashMap<String, BGBaseView>();

    private HttpServlet _Servlet;
    private Application _App;
    private long _StartTime;
    private long _FinishTime;
    private HttpServletRequest _Request;
    private HttpServletResponse _Response;

    ViewController(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, Application app) {
        _Servlet=servlet;
        _App=app;
        _Request=request;
        _Response=response;
    }

    void processRequest() throws ServletException, IOException {

        _StartTime = System.currentTimeMillis();

        String forwardPage = Common.getPage(_Request.getRequestURI());

        // Determine which page and view code we want based on the URL
        String className = _App.getAppDir() + ".control." + forwardPage + Common.viewString;

        // Process the View  code;
        BGBaseView view = null;
        try {
            if (_PAGES.containsKey(className)) {
                view = _PAGES.get(className);
            } else {
                view = (BGBaseView)Class.forName(className).newInstance();
                Application._GLOBAL_LOG.logMessage(Level.FINEST, "ViewController.processRequest: Instantiated View Class: '" + className + "'");                                
            }
        } catch (Exception e) {
            try {
                view = (BGBaseView)Class.forName(_App.getAppDir() + ".control.BaseView").newInstance();
            } catch (Exception e1) {
                Application._GLOBAL_LOG.error(e1);
                forwardPage = ERROR_PAGE;
            }
        }

        if (view!=null) {
            if (!_PAGES.containsKey(className)) {
                _PAGES.put(className, view);
            }
            AuTimer.start("process");
            String newPage = view.process(_Request,_Response);
            Application._GLOBAL_LOG.logMessage(Level.FINE, "process took: " + AuTimer.elapsedTime("process") + " ms");
            if (newPage != null) {
                if (newPage.startsWith(Common.forwardDirective)) {
                    forwardPage = newPage.substring(Common.forwardDirective.length());
                } else {
                    if (newPage.equals("pdf")) {
                        return;
                    }
                    if (newPage.startsWith("csv")) {
                        String[] parts = newPage.split(":");
                        String filename = parts[1];
                        String output = parts[2];
                        try {
                            ServletOutputStream stream = _Response.getOutputStream();
                            _Response.setContentType("application/vnd.ms-excel");
                            _Response.setHeader("content-disposition", "attachment;filename=\"" + filename + "\"");
                            stream.print(output);
                        } catch (IOException e) {
                            Application._GLOBAL_LOG.error(e);
                        }
                        return;
                    }
                    forwardPage = newPage + Common.viewControllerExtension;
                    // add random param to get around browser cache; must do this to ensure that any
                    // error msg is displayed. We do a redirect instead of a forward to make sure the
                    // browser's address bar is correct. And finally, we take the performance hit of
                    // a redirect because it's an error condition.
                    _Response.sendRedirect(forwardPage + "?e=" + _RAND.nextInt());
                    return;
                }
            }
        }

        // note: the + "?" part below breaks the ability to pass request params in the URL via the ";" char, which
        // is actually officially preferred over "?"
        if (forwardPage!=null && !forwardPage.endsWith(Common.jspExtension) && !forwardPage.endsWith(Common.viewControllerExtension)
                && !(forwardPage.indexOf(Common.jspExtension + "?") >= 0) && !(forwardPage.indexOf(Common.viewControllerExtension + "?")>=0)
                && !forwardPage.endsWith(Common.actionControllerExtension) && !(forwardPage.indexOf(Common.actionControllerExtension) >= 0)) {
            forwardPage += Common.jspExtension;
        }

        // Forward to the correct JSP page
        RequestDispatcher rd = _Request.getRequestDispatcher(forwardPage);
        AuTimer.start("forward");
        Log.getInstance(getApplication().getAppPrefix()).logMessage(Level.FINER, "Before forward QueryCount = " + getApplication().getQuickDB().queryCount);
        rd.forward(_Request,_Response);
        Log.getInstance(getApplication().getAppPrefix()).logMessage(Level.FINER, "After forward QueryCount = " + getApplication().getQuickDB().queryCount);
        _App.getLogger().logMessage(Level.FINE, "forward took: " + AuTimer.elapsedTime("forward") + " ms");
        _FinishTime = System.currentTimeMillis();
    }

    public long getDuration() {
        if (_StartTime==0 || _FinishTime==0) {
            return -1;
        }
        return _FinishTime - _StartTime;
    }

    public Application getApplication() { return _App; }

    public String getPageName() { return Common.getPage(_Request.getRequestURI()); }

}
