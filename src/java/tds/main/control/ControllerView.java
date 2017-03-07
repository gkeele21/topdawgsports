package tds.main.control;

import bglib.util.AuUtil;
import tds.main.bo.URLBase;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: gkeele
 * Date: Jun 7, 2007
 * Time: 12:58:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ControllerView extends HttpServlet {

    public static final String ERROR_PAGE = "error";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        System.out.println("In ViewController.");
        URLBase base = new URLBase();
        base.populate(request.getRequestURI());
        String forwardPage = base.getPageName();
        //String forwardPage = Common.getPage(request.getRequestURI())[HTMLPAGENOEXT];

        System.out.println("Forward Page : " + forwardPage);
        // Determine which page and view code we want based on the URL
        String packageName = base.getPackage();
        if (AuUtil.isEmpty(packageName)) {
            packageName = Common.mainControlPackage;
        } else {
            packageName = Common.mainPackage + "." + packageName + "control";
        }
        String className = packageName + "." + forwardPage + Common.viewString;

        // Process the View code;
        BaseView view = null;
        try {
            view = (BaseView)Class.forName(className).newInstance();
        } catch (Exception e) {
            try {
                view = (BaseView)Class.forName(Common.mainControlPackage + ".BaseView").newInstance();
            } catch (Exception e1) {
                e.printStackTrace();
                //_HT_LOG.error(e1);
                forwardPage = ERROR_PAGE;
            }
        }

        if (view!=null) {
            String newPage = view.process(request,response);
            if (newPage != null) {
                forwardPage = newPage;
            }
        }

        if (!forwardPage.endsWith(Common.jspExtension) && !forwardPage.endsWith(Common.viewControllerExtension)) {
            forwardPage += Common.jspExtension;
        }

        // Forward to the correct JSP page
        RequestDispatcher rd = request.getRequestDispatcher(forwardPage);
        rd.forward(request,response);
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
        return "View Controller Servlet";
    }

}

