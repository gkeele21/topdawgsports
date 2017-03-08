package tds.commissioner.golf.control;

import bglib.util.FSUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.constants.Position;
import tds.constants.Team;
import tds.main.bo.Player;
import tds.main.bo.UserSession;
import tds.main.control.BaseAction;

public class golfPlayerEditAction extends BaseAction {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        UserSession session = UserSession.getUserSession(request, response);

        String prefix = "";
        if ("local".equals(this.host))
        {
            prefix = "commissioner/golf/";
        }
        
        nextPage = prefix + "golfPlayers";
        
        int playerID = FSUtils.getIntRequestParameter(request,"playerID", 0);
        
        Player player;
        if (playerID > 0)
        {
            player = new Player(playerID);
        } else
        {
            player = new Player();
        }
        
        String firstName = FSUtils.getRequestParameter(request, "firstName", null);
        String lastName = FSUtils.getRequestParameter(request, "lastName", null);
        int countryID = FSUtils.getIntRequestParameter(request, "countryID", 0);
        int externalID = FSUtils.getIntRequestParameter(request, "externalID", 0);
        String active = FSUtils.getRequestParameter(request, "active", "true");

        if (firstName != null)
        {
            player.setFirstName(firstName);
        }
        if (lastName != null)
        {
            player.setLastName(lastName);
        }
        if (countryID > 0)
        {
            player.setCountryID(countryID);
        }
        player.setIsActive("1".equals(active) || "true".equals(active) ? true : false);
        if (externalID > 0)
        {
            player.setStatsPlayerID(externalID);
        }
        
        player.setTeamID(Team.PGATOUR);
        player.setPositionID(Position.GOLFER);
        
        try {
            player.Save();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            session.setErrorMessage("Error saving Player.");
        }
            

        return nextPage;
    }
}