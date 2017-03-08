package tds.main.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: gkeele
 * Date: Feb 16, 2007
 * Time: 11:14:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseCommissionerView extends BaseView {

    protected int menuLevel = 1;
    
    public String process(HttpServletRequest request, HttpServletResponse response, int setMenuLevel) {
        String result = super.process(request,response);

        if (setMenuLevel > 1)
        {
            menuLevel = setMenuLevel;
            _Session.getHttpSession().setAttribute("menuLevel", menuLevel);
        } else
        {
            try {
                setMenuLevel = Integer.parseInt(""+_Session.getHttpSession().getAttribute("menuLevel"));
                menuLevel = setMenuLevel;
            } catch (Exception e) {
                
            }
            
        }
        
        request.setAttribute("menuLevel", menuLevel);
        
        return result;
    }


}
