package tds.commissioner.fantasy.control;

import tds.fantasy.scripts.FootballDraft;
import tds.main.bo.CTApplication;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class fsSeason_createRostersAction extends BaseAction {

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

        nextPage = prefix + "/topdawgsports/commissioner/fsSeasonView";

        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();

        try {
            // Update the team rosters, if requested
            String update = request.getParameter("hfUpdate");
            if ("hfUpdateTrue".equals(update)) {

                try {
                    FootballDraft draft = new FootballDraft();
                    System.out.println("Calling run to Create Rosters...");
                    draft.run();
                    System.out.println("Done Creating Rosters.");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            CTApplication._CT_LOG.error(request, e);
            UserSession.getUserSession(request, response).setErrorMessage("Error creating roster.");
        }

        return nextPage;
    }
}
