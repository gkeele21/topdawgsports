package tds.main.control;

import bglib.control.BGBaseView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static tds.main.bo.CTApplication._CT_LOG;
import tds.main.bo.FSUser;
import tds.main.bo.URLBase;
import tds.main.bo.UserSession;

/**
 * Created by IntelliJ IDEA.
 * User: gkeele
 * Date: Feb 16, 2007
 * Time: 11:14:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseView extends Common implements BGBaseView {

    protected String htmlPage = null;
    protected UserSession _Session = null;
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        super.process(request,response);

        URLBase base = new URLBase();
        base.populate(request.getRequestURI());
        String page = base.getPageName();
        htmlPage = base.getHTMLPage();
        
        //String page = pageData[HTMLPAGENOEXT];
        System.out.println("IN BaseView with page '" + page + "'");
        if (page == null) {
            page = "index";
        }
        request.setAttribute("pageName", page);

        _Session = UserSession.getUserSession(request, response);
        _Session.getHttpSession().setAttribute("UserSession", _Session);
        _Session.getHttpSession().setAttribute("pageName", base.getHTMLPage());
        try {
            FSUser validUser = (FSUser)_Session.getHttpSession().getAttribute("validUser");
            if (validUser != null) {
//                session.getHttpSession().setAttribute("validUser", new FSUser(validUser.getFSUserID()));
            }

            /*if (!AccessLevel.NONE.encompasses(requiredLevel)) {
                Login user = session.getLoggedInUser();
                if (user==null) {
                    session.setErrorMessage("You must first login to access that page");
                    return "index";
                } else if (!user.hasAccessLevel(requiredLevel)) {
                    session.setErrorMessage("You don't have the required access level to view that page");
                    return "index";
                }
            }*/

            String debug = request.getParameter("debug");
            if (debug != null) {
                if (debug.equals("off")) {
                    _Session.getHttpSession().removeAttribute("debug");
                } else {
                    _Session.getHttpSession().setAttribute("debug",debug);
                }
            }

        } catch (Exception e) {
            _CT_LOG.error(e);
        }

        return null;
    }

//    public static String setTeam(HttpServletRequest request, HttpServletResponse response) {
//
//        UserSession session = UserSession.getUserSession(request, response);
//
//        // Create FSTeam obj
//        int teamID = FSUtils.getIntRequestParameter(request, "tid", 0);
//        String authKey = FSUtils.getRequestParameter(request,"authKey","");
//
//        System.out.println("TeamID : " + teamID);
//        FSTeam team = null;
//        if (teamID > 0) {
//            try {
//                team = new FSTeam(teamID);
//            } catch (Exception e) {
//                CTApplication._CT_LOG.error(request, e);
//            }
//        } else {
//            team = (FSTeam)session.getHttpSession().getAttribute("fsteam");
//        }
//
//        if (team == null) {
//            session.setErrorMessage("Illegal team specified: TeamID = " + teamID);
//            return "../index";
//        }
//
//        FSUser user = (FSUser)session.getHttpSession().getAttribute("validUser");
//        if (user == null && !authKey.equals("")) {
//            user = team.getFSUser();
//            session.getHttpSession().setAttribute("validUser", user);
//        }
//
//        if (team.getFSUserID() != user.getFSUserID()) {
//            session.setErrorMessage("Error : you do not have access to that team.");
//            return "../index";
//        }
//
//        if (!authKey.equals("")) {
//            if (!user.getAuthenticationKey().equals(authKey)) {
//                session.setErrorMessage("Error : you don't have access to that team.");
//                return "../index";
//            }
//        }
//
//        session.getHttpSession().setAttribute("fsteam", team);
//
//        return "";
//    }
}
