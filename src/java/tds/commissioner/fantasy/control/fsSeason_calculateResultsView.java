/*
 * registerView.java
 *
 * Created on July 3, 2008, 10:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tds.commissioner.fantasy.control;

import tds.main.control.BaseCommissionerView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author grant.keele
 */
public class fsSeason_calculateResultsView extends BaseCommissionerView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;

//        UserSession userSession = UserSession.getUserSession(request, response);
//        HttpSession session = userSession.getHttpSession();

        return page;
    }

}
