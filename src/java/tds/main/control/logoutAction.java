package tds.main.control;

import tds.main.control.BaseAction;
import bglib.util.ErrorMessage;
import bglib.util.FSUtils;
import bglib.util.FormError;
import bglib.util.FormField;
import bglib.util.FormFieldType;
import tds.main.bo.FSTeam;
import tds.main.bo.FSUser;
import tds.main.bo.State;
import tds.main.bo.UserSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.ArrayList;

public class logoutAction extends BaseAction {

    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        session.getHttpSession().removeAttribute("validUser");
        session.getHttpSession().removeAttribute("fsteam");
        session.getHttpSession().invalidate();
        
        nextPage = "index.htm";
        
        System.out.println("LogoutAction: redirecting to " + nextPage);
        return nextPage;
    }
}
