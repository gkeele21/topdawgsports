package tds.main.control;

import bglib.data.JDBCDatabase;
import bglib.util.FSUtils;
import tds.main.bo.CTApplication;
import tds.main.bo.FSUser;
import tds.main.bo.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Apr 24, 2006
 * Time: 11:39:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class indexView extends BaseView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        nextPage = htmlPage;

        UserSession session = UserSession.getUserSession(request, response);

        // Create FSUser obj
        int userID = FSUtils.getIntRequestParameter(request, "userid", 0);
        String authKey = FSUtils.getRequestParameter(request,"authKey","");
        String logoutAction = FSUtils.getRequestParameter(request,"action","");

        if (logoutAction.equals("logout")) {
            session.getHttpSession().removeAttribute("validUser");
            session.getHttpSession().removeAttribute("fsteam");
        }

        try {
            Connection con = JDBCDatabase.getConnection();
            System.out.println("Connection created");
        } catch (Exception e) {
            e.printStackTrace();
        }

        FSUser user = null;
        if (userID > 0) {
            try {
                user = new FSUser(userID);
            } catch (Exception e) {
                CTApplication._CT_LOG.error(request, e);
            }
        } else {
            user = (FSUser)session.getHttpSession().getAttribute("validUser");
        }

        if (!authKey.equals("")) {
            if (!user.getAuthenticationKey().equals(authKey)) {
            session.setErrorMessage("ERROR : You must first register or be logged in to sign up for a specific game.");
            return "/topdawg/register.htm";
            }
        }
        if (user != null) {
            session.getHttpSession().setAttribute("validUser", user);
        }

        return nextPage;
    }

}
