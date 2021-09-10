package tds.commissioner.fantasy.control;

import tds.fantasy.scripts.FootballMatchup;
import tds.main.bo.CTApplication;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class fsSeason_createMatchupsAction extends BaseAction {

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

        nextPage = prefix + "fsSeasonView";

        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();

        try {
            // Create team matchups, if requested
            String update = request.getParameter("hfUpdate");
            if ("hfUpdateTrue".equals(update)) {

                try {
                    FootballMatchup matchup = new FootballMatchup();
                    System.out.println("Calling run to Create Matchups...");
                    matchup.run();
                    System.out.println("Done Creating Matchups.");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            CTApplication._CT_LOG.error(request, e);
            UserSession.getUserSession(request, response).setErrorMessage("Error creating matchups.");
        }

        return nextPage;
    }
}
