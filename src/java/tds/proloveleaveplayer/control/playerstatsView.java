package tds.proloveleaveplayer.control;

import bglib.util.FSUtils;
import tds.main.bo.FootballStats;
import tds.main.bo.Player;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.control.BaseView;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Apr 24, 2006
 * Time: 11:39:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class playerstatsView extends BaseView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request,response);

        if (page != null) {
            return page;
        }

        page = htmlPage;

//        ServletContext application  = request.getSession().getServletContext();

        int playerID = FSUtils.getIntRequestParameter(request,"pid",0);
        Player player = Player.getInstance(playerID);
        if (playerID > 0) {
            _Session.getHttpSession().setAttribute("statPlayer", player);
        } else {
            return page;
        }
        
        List<FootballStats> playerStats = player.getFootballStatsList();
        request.setAttribute("playerStats", playerStats);
        
//        int maxYear = Math.max(FSGlobals._FSGlobals.getStatsYear(), 2003); // ensure no infinite loop in next line
//        for (int year=maxYear; year>=2003; year--) {
//            list.add(new ListBoxItem(String.valueOf(year), String.valueOf(year), year==statYear));
//        }
//        session.getHttpSession().setAttribute("listItems",list);
//        session.getHttpSession().setAttribute("statYear", statYear);


        return page;
    }

}
