package tds.commissioner.golf.control;

import bglib.util.FSUtils;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.CTApplication;
import tds.main.bo.PGATournamentWeekPlayer;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;

public class golfTournamentWeekFieldEditAction extends BaseAction {
    
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
        
        nextPage = prefix + "golfTournamentWeek";
        
        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();
        
        int tournamentID = Integer.parseInt(""+session.getAttribute("commGolfPGATournamentID"));
        int fsSeasonWeekID = Integer.parseInt(""+session.getAttribute("commGolfFSSeasonWeekID"));
        
        String[] dropPlayers = request.getParameterValues("del");
        String[] addPlayers = request.getParameterValues("add");
        
        try {

            // update Player tournaent data
            Enumeration paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String)paramNames.nextElement();
                
                if (paramName.indexOf("_") > 0) {
                    StringTokenizer st = new StringTokenizer(paramName, "_");
                    String type = st.nextToken();
                    String ID = st.nextToken();
                    String value = FSUtils.getRequestParameter(request,paramName);

                    PGATournamentWeekPlayer p = new PGATournamentWeekPlayer(Integer.parseInt(ID));
                    if (p != null && p.getID() > 0)
                    {
                        if (paramName.startsWith("wgr_")) {
                            p.setWorldGolfRanking(Integer.parseInt(value));
                        } else if (paramName.startsWith("sal_")) {
                            p.setSalaryValue(Double.parseDouble(value));
                        } else if (paramName.startsWith("earned_")) {
                            p.setMoneyEarned(Double.parseDouble(value));
                        } else if (paramName.startsWith("final_")) {
                            p.setFinalScore(value);
                        } else if (paramName.startsWith("rel_")) {
                            p.setRelativeToPar(Integer.parseInt(value));
                        } else if (paramName.startsWith("rank_")) {
                            p.setTournamentRank(value);
                        } 
                        p.Save();
                    }
                }
                    
            }
            // subtract the new drops
            if (dropPlayers != null && dropPlayers.length > 0) {
                for (String drop : dropPlayers) {
                    PGATournamentWeekPlayer player = new PGATournamentWeekPlayer(Integer.parseInt(drop));
                    
                    if (player != null)
                    {
                        player.Delete();    
                    }
                    
                }
            }            
            
            // add the new adds
            if (addPlayers != null && addPlayers.length > 0) {
                for (String add : addPlayers)
                {
                    PGATournamentWeekPlayer player = new PGATournamentWeekPlayer();
                    
                    player.setFSSeasonWeekID(fsSeasonWeekID);
                    player.setPGATournamentID(tournamentID);
                    player.setPlayerID(Integer.parseInt(add));

                    player.Save();
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