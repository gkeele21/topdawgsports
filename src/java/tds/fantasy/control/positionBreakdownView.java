package tds.fantasy.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.FSTeam;
import tds.main.control.BaseTeamView;
import tds.main.bo.PositionalBreakdown;

public class positionBreakdownView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }

        page = htmlPage;
        
        FSTeam displayTeam = null;
        boolean isWRTECombo = _FSTeam.getFSLeague().getIncludeTEasWR() == 1;
        boolean isDynasty = _FSTeam.getFSLeague().getDraftType()!= null &&_FSTeam.getFSLeague().getDraftType().equals("dynasty");

        // Get request/session objects
        int reqFSTeamId = FSUtils.getIntRequestParameter(request, "dtid", 0);      

        // Get all the teams in the league based off the FSTeam (user logged in)
        List<FSTeam> allLeagueTeams = FSTeam.GetLeagueTeams(_FSTeam.getFSLeagueID());

        // Figure out the display team
        if (reqFSTeamId > 0) {
            for (FSTeam team : allLeagueTeams) {
                if (team.getFSTeamID() == reqFSTeamId) {
                    displayTeam = team;
                }            
            }                
        }

        else { if (displayTeam == null) { displayTeam = _FSTeam; } }
            
        // Retrieve league info
        List<PositionalBreakdown> posStats = FSTeam.GetPositionalBreakdown(displayTeam.getFSTeamID(), 0);
        
        //Retrieve stats      
        request.setAttribute("isWRTECombo",isWRTECombo);
        request.setAttribute("isDynasty",isDynasty);
        request.setAttribute("displayTeam",displayTeam);
        request.setAttribute("allLeagueTeams",allLeagueTeams);
        request.setAttribute("posStats",posStats);        
       
        return page;
    }    
}