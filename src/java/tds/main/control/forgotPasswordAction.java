package tds.main.control;

import bglib.util.AuUtil;
import bglib.util.FSUtils;
import bglib.util.FormField;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSUser;
import tds.main.bo.UserSession;

public class forgotPasswordAction extends BaseAction {

    public static final FormField USERNAME = new FormField(1, "username", "Username", 1, 50);

//    public static List<FormField> REG_FIELDS = Arrays.asList(USERNAME);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        String userName = FSUtils.getRequestParameter(request, "username", "");
        
        String nextURL = FSUtils.getRequestParameter(request, "nextURL");
        String origURL = FSUtils.getRequestParameter(request, "origURL");
        String strNextPage = origURL;
        if (strNextPage == null && strNextPage.equals("")) {
            strNextPage = FSUtils.stringValue(session.getHttpSession().getAttribute("origURL"),true);
        }

        if (!AuUtil.isEmpty(nextURL)) {
            strNextPage = nextURL;
        }

        if (strNextPage==null || strNextPage.length()==0) {
            strNextPage = "forgotPasswordSuccess";
        }

        try {
            if (!AuUtil.isEmpty(userName)) {
                FSUser user = new FSUser(userName);
                if (user.getFSUserID() > 0) {
                    // send out the password email
                    String subject = "TopDawgSports Password Recovery";
                    StringBuilder body = new StringBuilder();
                    body.append("You have requested your password for topdawgsports.com\n\n");
                    body.append("Your password is ");
                    body.append(user.getPassword());
                    FSUtils.sendMessageEmail(user.getEmail(), subject.toString(), body.toString());

                    System.out.println("Forgot Password email sent to " + user.getEmail());
                    return strNextPage;
                } else {
                    System.out.println("Error : No user with username of " + userName + " found.");
                    UserSession.getUserSession(request, response).setErrorMessage("No user found.");
                    return "forgotPassword";
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            UserSession.getUserSession(request, response).setErrorMessage("Invalid Username");
        }

        System.out.println("ForgotPasswordAction: redirecting to " + strNextPage);
        return strNextPage;
    }
}
