package tds.sal.control;

import bglib.util.FormField;
import tds.main.control.BaseAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Mar 9, 2006
 * Time: 9:55:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class forgotpasswordAction extends BaseAction {

    private static final FormField USER_USERNAME = new FormField(1, "Username", 1, 15);
    private static final FormField USER_EMAIL = new FormField(2, "Email Address", 1, 100);

    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

//        UserSession session = UserSession.getUserSession(request, response);
//
//        String userName = FSUtils.getRequestParameter(request, "txtUsername");
//        String email = FSUtils.getRequestParameter(request, "txtEmail");
//
//        StringBuffer eBfr = new StringBuffer();
//        try {
//
//            if (!USER_USERNAME.isValid(userName,eBfr) && !USER_EMAIL.isValid(email,eBfr)) {
//                session.setErrorMessage("Please enter your username or email address.");
//                return nextPage;
//            }
//            User user;
//            try {
//                user = (userName == null || userName.equals("")) ? User.getInstance("Email",email) : User.getInstance("Username",userName);
//            } catch (Exception e) {
//                session.setErrorMessage("The username or email address was not found. Please enter a valid username or password.");
//                return nextPage;
//            }
//            if (user == null || user.getUserID() < 1) {
//                session.setErrorMessage("The username or email address was not found. Please enter a valid username or password.");
//                return nextPage;
//            }
//
//            // send email
//            List<String> toList = new ArrayList<String>();
//            List<String> params = new ArrayList<String>();
//
//            toList.add(user.getEmail());
//            params.add(0,user.getUsername());
//            params.add(1,user.getPassword());
//
//            EmailMessage em = new EmailMessage(4,params,toList);
//            Thread th = new EmailThread(em);
//            th.start();
//
//            session.setErrorMessage("An email has been sent containing your username and password.");
//        } catch (Exception e) {
//            session.setErrorMessage("An error occurred while trying to access your user data. Please try again.");
//            Log.error(session, e);
//        }

        return nextPage;
    }

}
