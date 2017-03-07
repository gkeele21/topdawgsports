package tds.stattracker.control;

import bglib.util.FSUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.control.BaseTeamView;
import tds.stattracker.bo.GolfEventRound.DisplayOption;
import tds.stattracker.bo.*;

public class eventResultsView extends BaseTeamView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        Integer eventRoundId = 0;
        Integer roundNumber = 0;
        Integer currentRoundNumber =0;
        DisplayOption selectedOption = DisplayOption.FIELD;;        
        List<DisplayOption> availableOptions = new ArrayList();        
        GolfEventRound golfEventRound = null;        
        List<GolfEventRoundGolfer> eventRoundGolfers = null;        
        List<HoleTeeInfo> holeInfo = null;
        List<HoleScore> holeScores = null;
        Map<Integer, List<HoleScore>> golferRoundScores = new HashMap<Integer, List<HoleScore>>();
        
        
        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        nextPage = htmlPage;
        
        try {
    
            // GolfEventRoundID parameter
            eventRoundId = FSUtils.getIntRequestParameter(request, "gerid", 0);

            if (eventRoundId == 0) {
                roundNumber = FSUtils.getIntRequestParameter(request, "rn", 0);
                if (roundNumber > 0) {
                    golfEventRound = new GolfEventRound(FSUtils.getIntRequestParameter(request, "ge", 0), roundNumber);
                    eventRoundId = golfEventRound.getGolfEventRoundID();
                }
                else {
                    eventRoundId = (Integer)_Session.getHttpSession().getAttribute("eventRoundID");
                }
            }

            // This is needed to grab hole information (Course and Tee) as well as the currentRoundNumber
            if (golfEventRound == null) {
                golfEventRound = new GolfEventRound(eventRoundId);            
            }

            currentRoundNumber = golfEventRound.getRoundNumber();

            // Grab the displayOption parameter or default it to FIELD
            if (!FSUtils.getRequestParameter(request, "dop").isEmpty()) {
                selectedOption = DisplayOption.valueOf(FSUtils.getRequestParameter(request, "dop"));
            }

            availableOptions.add(DisplayOption.FIELD);

            // Retrieve the golfers that are part of the event round
            eventRoundGolfers = GolfEventRoundGolfer.GetGolfEventRoundGolfers(eventRoundId, selectedOption);            
            
            // Retrieve all of the hole information
            holeInfo = Hole.GetHoleInfo(golfEventRound.getCourseID(), golfEventRound.getTeeID());
                        
            // Loop through all of the golfers to get all of their scores on each hole
            for (int golferCount=0; golferCount <= eventRoundGolfers.size()-1; golferCount++) { 
                holeScores = HoleScore.GetGolferRoundScores(eventRoundGolfers.get(golferCount).getGolferRoundID());
                golferRoundScores.put(eventRoundGolfers.get(golferCount).getGolferRoundID(), holeScores);
                
                // Figure out the available display options                
                if (!availableOptions.contains(DisplayOption.GROUPS)) {
                    if (eventRoundGolfers.get(golferCount).getGolfEventGroupID() != null && eventRoundGolfers.get(golferCount).getGolfEventGroupID() > 0) {
                        availableOptions.add(DisplayOption.GROUPS);
                    }
                }
                
                if (!availableOptions.contains(DisplayOption.TEAMS)) {
                    if (eventRoundGolfers.get(golferCount).getGolfEventTeamID() != null && eventRoundGolfers.get(golferCount).getGolfEventTeamID() > 0) {
                        availableOptions.add(DisplayOption.TEAMS);
                    }
                }
            }
            
            // Defaulting back to Event Field if this round doesn't have the passed in selectedOption available.  This is a possibility when just changing the round number
            if (!availableOptions.contains(selectedOption)) {
                selectedOption = DisplayOption.FIELD;
            }
            
            request.setAttribute("selectedOption", selectedOption);
            request.setAttribute("availableOptions", availableOptions);
            request.setAttribute("eventRoundGolfers", eventRoundGolfers); 
            request.setAttribute("golferRoundScores", golferRoundScores);
            request.setAttribute("currentRoundNumber", currentRoundNumber);
            _Session.getHttpSession().setAttribute("eventRoundID",eventRoundId);
            _Session.getHttpSession().setAttribute("holeInfo",holeInfo);        
            

        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }
     
        return nextPage;
    }
}