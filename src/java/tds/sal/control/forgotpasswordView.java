package tds.sal.control;

import tds.main.control.BaseView;
import tds.main.bo.UserSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Apr 24, 2006
 * Time: 11:39:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class forgotpasswordView extends BaseView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        nextPage = htmlPage;
        
        return nextPage;
    }

}
