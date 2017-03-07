package tds.main.control;

import bglib.control.BGBaseAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSUser;
import tds.main.bo.UserSession;

public class BaseAction extends Common implements BGBaseAction {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        return process(request, response, true);
    }

    public String process(HttpServletRequest request, HttpServletResponse response, boolean checkLogin) {

        super.process(request,response);
        System.out.println("Here in BaseAction");
        
        UserSession userSession = UserSession.getUserSession(request, response);
        if (checkLogin) {
            FSUser user = userSession.getLoggedInUser();
            if (user==null) {
                //userSession.setErrorMessage("You must first login to access that page");
                //return mapping.findForward("join");
                //return "join";
            }
        }

        return null;
    }

}
