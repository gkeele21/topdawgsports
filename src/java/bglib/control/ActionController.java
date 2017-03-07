package bglib.control;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.io.*;

import bglib.util.Application;
import bglib.util.Log;
import bglib.util.AppSettings;

public class ActionController {

    private static final Map<String, BGBaseAction> _ACTIONS = new HashMap<String, BGBaseAction>();

    private HttpServlet _Servlet;
    private Application _App;
    private HttpServletRequest _Request;
    private HttpServletResponse _Response;
    private long _StartTime;
    private long _FinishTime;

    ActionController(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, Application app) {
        _Servlet=servlet;
        _App=app;
        _Request=request;
        _Response=response;
    }

    protected void processRequest() throws ServletException, IOException {
        _StartTime = System.currentTimeMillis();
        String forwardPage = null;

        // Determine which page and action code we want based on the URL
        forwardPage = Common.getPage(_Request.getRequestURI());

        String className = _App.getAppDir() + ".control." + forwardPage + Common.actionString;

        // Process the View code;
        BGBaseAction action = null;
        try {
            if (Application._GLOBAL_APPLICATION.getAppSettings().getProperty(AppSettings.CLASS_CACHE, "true").equals("true") && _ACTIONS.containsKey(className)) {
                action = _ACTIONS.get(className);
            } else {
                action = (BGBaseAction)Class.forName(className).newInstance();
                Application._GLOBAL_LOG.logMessage(Level.FINEST, "ActionController.processRequest: Instantiated Action Class: '" + className + "'");                
            }
        } catch (Exception e) {
            try {
                action = (BGBaseAction)Class.forName(_App.getAppDir() + ".control.BaseAction").newInstance();
            } catch (Exception e1) {
                Application._GLOBAL_LOG.error(e1);
                forwardPage = ViewController.ERROR_PAGE;
            }
        }

        if (action!=null) {
            if (!_ACTIONS.containsKey(className)) {
                _ACTIONS.put(className, action);
            }
            String newPage = action.process(_Request,_Response);
            if (newPage != null) {
                forwardPage = newPage;
            }
        }

        if (forwardPage == null) {
            forwardPage = "Unknown";
        }

        if (forwardPage.indexOf('.')<0 && forwardPage.indexOf(Common.jspExtension)<0 && forwardPage.indexOf(Common.viewControllerExtension)<0 &&
                forwardPage.indexOf("JSON")<0) {
            forwardPage += Common.viewControllerExtension;
        }

        if (forwardPage.startsWith(Common.forwardDirective)) {
            RequestDispatcher rd = _Request.getRequestDispatcher(forwardPage.substring(Common.forwardDirective.length()));
            rd.forward(_Request,_Response);
        } else if (forwardPage.startsWith(Common.fileDirective)) {
            String fname = forwardPage.substring(Common.fileDirective.length());
            File file = new File(fname);
            InputStream in = new FileInputStream(file);
            _Response.setContentType("text/plain");
            _Response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            PrintWriter out = _Response.getWriter();

            try {
                int bit = in.read();
				while ((bit) >= 0) {
                    out.write(bit);
                    bit = in.read();
                }
			} catch (IOException ioe) {
				Application._GLOBAL_LOG.error(ioe);
			}
			out.flush();
			out.close();
			in.close();
        } else if (forwardPage.startsWith(Common.JSONDirective)) {
            String json = forwardPage.substring(Common.JSONDirective.length());
            _Response.addHeader("X-JSON", json);
        } else {
            // Redirect to the correct JSP page
            _Response.sendRedirect(forwardPage);
        }
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
