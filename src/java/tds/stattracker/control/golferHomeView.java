package tds.stattracker.control;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.control.BaseTeamView;
import tds.stattracker.bo.GolfEvent;
import tds.stattracker.bo.Golfer;
import tds.stattracker.bo.GolferRound;

public class golferHomeView extends BaseTeamView {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        Golfer objGolfer = null;
        List<GolfEvent> objEvents = null;
        List<GolferRound> objRounds = null;
        
        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        nextPage = htmlPage;
        
        // Retrieve the golfer profile
        try {
            objGolfer = new Golfer(_FSTeam.getFSUserID());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }
        
        // Retrieve Tournament Events
        try {
            objEvents = GolfEvent.GetGolfEvents(_FSTeam.getFSUserID());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }
    
        // Retrieve Golfer Rounds
        try {
            objRounds = GolferRound.GetGolferRounds(_FSTeam.getFSUserID());
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }
      
        request.setAttribute("Golfer",objGolfer);
        request.setAttribute("Events",objEvents);
        request.setAttribute("Rounds",objRounds);
        
        return nextPage;
    }
}