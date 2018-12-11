package tds.commissioner.control;

import bglib.util.FSUtils;
//import com.google.gson.Gson;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.bo.Team;
import tds.main.control.BaseView;
import tds.mm.bo.MarchMadnessTeamSeed;
import tds.mm.bo.MarchMadnessTournament;

public class marchMadnessFieldView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
       
        String nextPage = super.process(request,response);

        if (nextPage != null) { return nextPage; }

        nextPage = htmlPage;
        
        int reqYear = FSUtils.getIntRequestParameter(request, "yr", Calendar.getInstance().get(Calendar.YEAR));
                
        try {

            List<Team> allTeams = Team.GetTeamsBySport(2);
            MarchMadnessTournament tournament = MarchMadnessTournament.GetTournamentByYear(reqYear);
            List<MarchMadnessTeamSeed> teamSeeds =  MarchMadnessTeamSeed.GetTournamentTeams(tournament.getTournamentID());
            
//            Gson gson = new Gson();        
  
            request.setAttribute("tournament",tournament);
//            request.setAttribute("allTeamsJSON", gson.toJson(allTeams));
            request.setAttribute("teamSeeds",teamSeeds);
         
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        } 
        
        return nextPage;
    }
}