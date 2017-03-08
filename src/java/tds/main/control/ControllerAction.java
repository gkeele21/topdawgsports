package tds.main.control;

import bglib.util.AppSettings;
import bglib.util.Application;
import static bglib.util.Application.GLOBAL_STR;
import bglib.util.AuUtil;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import tds.main.bo.URLBase;
/**
 * Created by IntelliJ IDEA.
 * User: gkeele
 * Date: Jun 7, 2007
 * Time: 12:58:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ControllerAction extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String forwardString = "";
        Application _Application = Application.getInstance(GLOBAL_STR);
        AppSettings appSettings = _Application.getAppSettings();
        String domainURL = appSettings.getProperty(AppSettings.DOMAIN_URL, "");

        try {
            // Determine which page and action code we want based on the URL
            URLBase base = new URLBase();
            base.populate(request.getRequestURI());
            String forwardPage = base.getPageName();
            forwardString = forwardPage;
            String packageName = base.getPackage();
            String basePackageName = packageName;
            System.out.println("PackageName : " + packageName);
            if (AuUtil.isEmpty(packageName)) {
                packageName = Common.mainControlPackage;
            } else {
                packageName = Common.mainPackage + "." + packageName + "control";
            }
            System.out.println("Forward Page : " + forwardPage);
            System.out.println("Forward String : " + forwardString);
            String actionName = packageName + "." + forwardPage + Common.actionString;

            // Process the Action code;
//            BaseAction action = (BaseAction) Class.forName(Common.controlPackage+"."+tempaction).newInstance();
            BaseAction action = (BaseAction) Class.forName(actionName).newInstance();
            String newPage = action.process(request,response);

            if (newPage != null) {
                forwardString = newPage;
            }
            if (!forwardString.startsWith(basePackageName)) {
                if (!AuUtil.isEmpty(domainURL)) {
                    if (AuUtil.isEmpty(forwardString))
                    {
                        forwardString = basePackageName + forwardPage;
                    }
                    
                    forwardString = forwardString.replace(".","/");
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR : " + e);
        }

        if (forwardString == null) {
            forwardString = "Unknown";
        }

        if (!forwardString.endsWith(Common.jspExtension) && !forwardString.endsWith(Common.viewControllerExtension)) {
            forwardString += Common.viewControllerExtension;
        }

        if (forwardString.startsWith(Common.forwardDirective)) {
            RequestDispatcher rd = request.getRequestDispatcher(forwardString.substring(Common.forwardDirective.length()));
            System.out.println("Sending Action forward to " + forwardString.substring(Common.forwardDirective.length()));
            rd.forward(request,response);
        } else {
            // Redirect to the correct JSP page
            System.out.println("Sending ControllerAction redirect to " + domainURL + forwardString);
            response.sendRedirect(domainURL + forwardString);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Action Controller Servlet";
    }

}
