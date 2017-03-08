package tds.stattracker.control;

import bglib.util.FSUtils;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.CTApplication;
import tds.main.control.BaseTeamView;
import tds.stattracker.bo.GolfEvent;
import tds.stattracker.bo.GolfEventRound;
import tds.stattracker.bo.GolfEventRoundGolfer;

public class eventInfoView extends BaseTeamView {

    StringBuilder eventGolfCourses = new StringBuilder();
    Integer prevGolfCourseID = 0;
    StringBuilder eventDates = new StringBuilder();
    Date prevDate = null;
        
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        Integer golfEventId = 0;
        GolfEvent golfEvent = null;
        List<GolfEventRound> eventRounds = null;              
        Map<Integer, List<String>> golfTeams = new HashMap<Integer, List<String>>();
        Map<Integer, List<String>> golfGroups = new HashMap<Integer, List<String>>();

        String nextPage = super.process(request,response);

        if (nextPage != null) {
            return nextPage;
        }

        nextPage = htmlPage;
      
        golfEventId = FSUtils.getIntRequestParameter(request, "geid", 0);
        
        if (golfEventId == 0) {
            golfEvent = (GolfEvent)_Session.getHttpSession().getAttribute("golfEvent");
            golfEventId = golfEvent.getGolfEventID();
        }
        
        // Retrieve information about the entire Event
        try {
            
            if (golfEvent == null) {
                golfEvent = new GolfEvent(golfEventId);
            }
            
            eventRounds = GolfEventRound.GetGolfEventRounds(golfEventId);      
        
            // Loop through all of the Rounds of the Event to retrieve specific information
            for (int roundCount=0; roundCount <= eventRounds.size()-1; roundCount++) {
                eventGolfCourses = GetEventGolfCourses(roundCount, eventRounds);            
                eventDates = GetEventDates(roundCount, eventRounds);            
                golfTeams.put(eventRounds.get(roundCount).getGolfEventRoundID(), GetEventRoundTeams(roundCount, eventRounds));           
                golfGroups.put(eventRounds.get(roundCount).getGolfEventRoundID(), GetEventRoundGroups(roundCount, eventRounds));
            }
            
            _Session.getHttpSession().setAttribute("golfEvent", golfEvent);
            _Session.getHttpSession().setAttribute("eventRounds", eventRounds);
            _Session.getHttpSession().setAttribute("golfTeams", golfTeams);
            _Session.getHttpSession().setAttribute("golfGroups", golfGroups);
            request.setAttribute("eventGolfCourses", eventGolfCourses);            
            request.setAttribute("eventDates", eventDates);

        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }

        return nextPage;
    }
    
    // PRIVATE METHODS 
    
    /* This methods finds the range of dates for the Event after looking at each specific round */
    private StringBuilder GetEventDates(Integer roundCount, List<GolfEventRound> eventRounds) {
                    
        SimpleDateFormat sd = new SimpleDateFormat("MMMM d, yyyy");
        
        // Get the Dates for this round of the event
        if (prevDate == null) {
            eventDates.append(sd.format(eventRounds.get(roundCount).getRoundDate()));
        }
        else {
            if (sd.format(prevDate).compareTo(sd.format(eventRounds.get(roundCount).getRoundDate())) != 0) {
                eventDates.append(" - ");
                eventDates.append(sd.format(eventRounds.get(roundCount).getRoundDate()));
            }
        }
        prevDate = eventRounds.get(roundCount).getRoundDate();
        
        return eventDates;
    }
    
    /* This methods finds all of the GolfCourses being played for the Event after looking at each specific round */
    private StringBuilder GetEventGolfCourses(Integer roundCount, List<GolfEventRound> eventRounds) {

        // Get the GolfCourses played for this round of the event
        if (prevGolfCourseID != eventRounds.get(roundCount).getCourse().getGolfCourseID()) {
            if (prevGolfCourseID > 0) {
                eventGolfCourses.append(", ");
            }
            eventGolfCourses.append(eventRounds.get(roundCount).getCourse().getGolfCourse().getGolfCourseName());
            prevGolfCourseID = eventRounds.get(roundCount).getCourse().getGolfCourseID(); 
        }
        
        return eventGolfCourses;
    }
    
    /* This methods finds all of the GolfTeams for each specific round and formats them in a particular way.
       It returns a Map object so that we can retrieve each of the Teams on the UT for each round. */
    private List<String> GetEventRoundGroups(Integer roundCount, List<GolfEventRound> eventRounds) {

        List<GolfEventRoundGolfer> eventRoundGolfers = null;
        StringBuilder displayGroup = null;
        List<String> displayGroups = new ArrayList<String>();
        Integer prevGroup =0;
        SimpleDateFormat sd = new SimpleDateFormat("H:mm");
        
        // Get the Teams for this round of the event            
        eventRoundGolfers = GolfEventRoundGolfer.GetGolfGroups(eventRounds.get(roundCount).getGolfEventRoundID());

        if (eventRoundGolfers.size() > 0) {     
        
            for (int golferCount=0; golferCount <= eventRoundGolfers.size()-1; golferCount++) {
                if (prevGroup != eventRoundGolfers.get(golferCount).getGolfEventGroupID()) {
                    if (prevGroup > 0) {
                        displayGroup.deleteCharAt(displayGroup.length()-1);
                        displayGroups.add(displayGroup.toString());
                    }
                    displayGroup = new StringBuilder();
                    displayGroup.append(sd.format(eventRoundGolfers.get(golferCount).getGolfEventGroup().getTeeTime())).append(" -");
                }

                displayGroup.append(" ").append(eventRoundGolfers.get(golferCount).getGolferRound().getGolfer().getFSUser().getFirstName()).append(",");

                prevGroup = eventRoundGolfers.get(golferCount).getGolfEventGroupID();

            }

            displayGroup.deleteCharAt(displayGroup.length()-1);
            displayGroups.add(displayGroup.toString());        

        }    
        
        return displayGroups;
    }
    
    /* This methods finds all of the GolfTeams for each specific round and formats them in a particular way.
       It returns a Map object so that we can retrieve each of the Teams on the UT for each round. */
    private List<String> GetEventRoundTeams(Integer roundCount, List<GolfEventRound> eventRounds) {

        List<GolfEventRoundGolfer> eventRoundGolfers = null;
        StringBuilder displayTeam = null;
        List<String> displayTeams = new ArrayList();
        Integer prevTeam =0;
        
        // Get the Teams for this round of the event            
        eventRoundGolfers = GolfEventRoundGolfer.GetGolfTeams(eventRounds.get(roundCount).getGolfEventRoundID());

        if (eventRoundGolfers.size() > 0) {     
        
            for (int golferCount=0; golferCount <= eventRoundGolfers.size()-1; golferCount++) {
                if (prevTeam != eventRoundGolfers.get(golferCount).getGolfEventTeamID()) {
                    if (prevTeam > 0) {
                        displayTeam.deleteCharAt(displayTeam.length()-1);
                        displayTeams.add(displayTeam.toString());
                    }
                    displayTeam = new StringBuilder();
                    displayTeam.append(eventRoundGolfers.get(golferCount).getGolfEventTeam().getTeamName());
                    if (eventRoundGolfers.get(golferCount).getGolfEventTeam().getTotalHandicap() != null && eventRoundGolfers.get(golferCount).getGolfEventTeam().getTotalHandicap() > 0) {
                        displayTeam.append(" (").append(Math.round(eventRoundGolfers.get(golferCount).getGolfEventTeam().getTotalHandicap())).append(")");
                    }
                    displayTeam.append(" - ");                    
                }

                displayTeam.append(" ").append(eventRoundGolfers.get(golferCount).getGolferRound().getGolfer().getFSUser().getFirstName());
                if (eventRoundGolfers.get(golferCount).getGolfEventGolfer().getHandicap() != null && eventRoundGolfers.get(golferCount).getGolfEventGolfer().getHandicap() > 0) {
                    displayTeam.append(" (").append(eventRoundGolfers.get(golferCount).getGolfEventGolfer().getHandicap()).append(")");
                }
                displayTeam.append(",");
                prevTeam = eventRoundGolfers.get(golferCount).getGolfEventTeamID();

            }

            displayTeam.deleteCharAt(displayTeam.length()-1);
            displayTeams.add(displayTeam.toString());        

        }    
        
        return displayTeams;
    } 
}