package tds.commissioner.golf.control;

import tds.golf.scripts.FantasyResults;
import tds.main.bo.CTApplication;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class golfCalculateResultsAction extends BaseAction {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        String prefix = "";
        if ("local".equals(this.host))
        {
//            prefix = "commissioner/golf/";
        }

        nextPage = prefix + "golfTournamentWeeks";

        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();

        int fsSeasonWeekID = Integer.parseInt(""+session.getAttribute("commGolfFSSeasonWeekID"));

        try {
            // Recalculate all salary values, if requested
            String recalc = request.getParameter("hfRecalc");
            if ("hfRecalcTrue".equals(recalc)) {

                try {
                    FantasyResults results = new FantasyResults();
                    System.out.println("Calling run to Calculate Results...");
                    results.run(fsSeasonWeekID);
                    System.out.println("Done Calculating Results.");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
//            System.out.println("Error : " + e.getMessage());
            CTApplication._CT_LOG.error(request, e);
            UserSession.getUserSession(request, response).setErrorMessage("Error processing field changes.");
        }

        return nextPage;
    }
}
