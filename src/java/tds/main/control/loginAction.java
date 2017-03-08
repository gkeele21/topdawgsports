package tds.main.control;

import bglib.util.AuUtil;
import bglib.util.FSUtils;
import bglib.util.FormField;
import tds.main.bo.FSSeason;
import tds.main.bo.FSTeam;
import tds.main.bo.FSUser;
import tds.main.bo.UserSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Arrays;

public class loginAction extends BaseAction {

    public static final int SESSION_TIMEOUT=86400; // 24 hours
    
    public static final FormField USERNAME = new FormField(1, "txtUsername", "Username", 1, 50);
    public static final FormField PASSWORD = new FormField(2, "txtPassword", "Password", 1, 30);

    public static List<FormField> REG_FIELDS = Arrays.asList(
        USERNAME, PASSWORD
    );

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        String userName = FSUtils.getRequestParameter(request, "txtUserName", "");
        String password = FSUtils.getRequestParameter(request, "txtPassword", "");
        String nextURL = FSUtils.getRequestParameter(request, "nextURL");
        String origURL = FSUtils.getRequestParameter(request, "origURL");
        String strNextPage = origURL;
        if (strNextPage == null && strNextPage.equals("")) {
            strNextPage = FSUtils.stringValue(session.getHttpSession().getAttribute("origURL"),true);
        }
        if (strNextPage==null || strNextPage.length()==0) {
            strNextPage = "index";
        }

        try {

            FSUser user = new FSUser(userName, password);
            System.out.println("Valid Login.");
            
            // Valid login
            session.getHttpSession().setMaxInactiveInterval(SESSION_TIMEOUT);
            UserSession._UserCache.put(session.getHttpSession().getId(), user);
            session.getHttpSession().setAttribute("validUser", user);
            
            // set last login to current timestamp
            user.setLastLogin();
            
            if (!AuUtil.isEmpty(nextURL)) {
                strNextPage = nextURL;
            }
        }
        catch (Exception e) {
            // invalid login
            System.out.println("Error : " + e.getMessage());
            UserSession.getUserSession(request, response).setErrorMessage("Invalid Username or Password");
            if (strNextPage==null || strNextPage.length()==0) {
                nextPage = "index";
            } else {
                //nextPage = "register.htm?origURL=" + strNextPage;
            }
        }

        System.out.println("LoginAction: redirecting to " + strNextPage);
        return strNextPage;
    }
}
