package tds.main.control;

import bglib.util.AuUtil;
import bglib.util.FSUtils;
import bglib.util.FormField;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSUser;
import tds.main.bo.UserSession;

public class createUserAction extends BaseAction {

    public static final int SESSION_TIMEOUT=86400; // 24 hours
    
    public static final FormField USERNAME = new FormField(1, "username", "Username", 1, 50);
    public static final FormField PASSWORD = new FormField(2, "pass", "Password", 1, 30);
    public static final FormField EMAIL = new FormField(3, "email", "Email", 1, 100);
    public static final FormField FIRSTNAME = new FormField(4, "fName", "FirstName", 1, 30);
    public static final FormField LASTNAME = new FormField(5, "lName", "LastName", 1, 30);
    public static final FormField CELL = new FormField(6, "cell", "Cell", 1, 15);

    public static List<FormField> REG_FIELDS = Arrays.asList(
        USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME
    );

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        String userName = FSUtils.getRequestParameter(request, "username", "");
        String password = FSUtils.getRequestParameter(request, "pass", "");
        String email = FSUtils.getRequestParameter(request, "email", "");
        String fName = FSUtils.getRequestParameter(request, "fName", "");
        String lName = FSUtils.getRequestParameter(request, "lName", "");
        String cell = FSUtils.getRequestParameter(request, "cell", "");
        
        String nextURL = FSUtils.getRequestParameter(request, "nextURL");
        String origURL = FSUtils.getRequestParameter(request, "origURL");
        String strNextPage = origURL;
        if (strNextPage == null && strNextPage.equals("")) {
            strNextPage = FSUtils.stringValue(session.getHttpSession().getAttribute("origURL"),true);
        }
        if (strNextPage==null || strNextPage.length()==0) {
            strNextPage = "createUser";
        }

        try {

            // check if the username requested already exists
            if (FSUser.usernameExists(userName)) {
                System.out.println("Error creating user : Username of " + userName + " already exists.");
                UserSession.getUserSession(request, response).setErrorMessage("Username already exists");
                return strNextPage;
            }

            String authenticationKey = FSUser.generateAuthenticationKey();
            int id = FSUser.addNewUser(userName, password, fName, lName, email, cell, "", "", "", "", "", "", "", "", "", 0, authenticationKey);
 
            FSUser user = new FSUser(id);
            
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
        }

        System.out.println("CreateUserAction: redirecting to " + strNextPage);
        return strNextPage;
    }
}
