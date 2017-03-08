package tds.stattracker.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.control.BaseView;

public class enterHoleScoresView extends BaseView {
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request, response);
        if (page != null) {
            return page;
        }
        
        page = htmlPage;
 /*       
        GolferRound golferRound = null;
        int golferRoundID = FSUtils.getIntRequestParameter(request, "golferRoundID", 0);
        if (golferRoundID > 0) {
            // clear out some session info
            _Session.getHttpSession().removeAttribute("golfGroup");
            _Session.getHttpSession().removeAttribute("currentHoleNumber");
        } else {
            Object golferRoundObj = _Session.getHttpSession().getAttribute("golferRoundID");
            if (golferRoundObj != null) {
                golferRoundID = (Integer)_Session.getHttpSession().getAttribute("golferRoundID");
            }
            
            if (golferRoundID <= 0) {
                return page;
            } else {
                golferRound = (GolferRound)_Session.getHttpSession().getAttribute("golferRound");
            }
        }
        
        if (golferRound == null) {
            golferRound = new GolferRound(golferRoundID);
        }
        FSUser user = (FSUser)_Session.getHttpSession().getAttribute("validUser");

        // grab the number of holes for this round
        int numHoles = golferRound.getNumHoles();
        int currentHoleNumber = FSUtils.getIntRequestParameter(request, "currentHoleNumber", 0);
        Hole currentHole = null;
        if (currentHoleNumber <= 0) {
            if (_Session.getHttpSession().getAttribute("currentHoleNumber") != null) {
                try {
                    currentHoleNumber = Integer.parseInt(_Session.getHttpSession().getAttribute("currentHoleNumber").toString());
                } catch (Exception e) {
                    currentHoleNumber = 1;
                }
            }
            if (currentHoleNumber < 1) {
                currentHoleNumber = 1;
            }
            
        }
        
        int golfTournamentRoundId = golferRound.getGolfTournamentRoundID();
        _Session.getHttpSession().setAttribute("golfTournamentRoundID", golfTournamentRoundId);
//        int golfCourseId = 8;
        
        List<Hole> holes = golferRound.getCourseHoles("");
        System.out.println("# of Holes : " + holes.size());
        if (holes.size() > 0) {
            int count = 0;
            for (Hole hole : holes) {
                count++;
                if (count == currentHoleNumber) {
                    currentHole = hole;
                    break;
                }
            }
        }
        _Session.getHttpSession().setAttribute("golferRoundID",golferRoundID);
        _Session.getHttpSession().setAttribute("golferRound", golferRound);
        _Session.getHttpSession().setAttribute("roundHoles", holes);
        _Session.getHttpSession().setAttribute("numHoles", holes.size());
        _Session.getHttpSession().setAttribute("currentHole", currentHole);
        _Session.getHttpSession().setAttribute("currentHoleID", currentHole.getHoleID());
        _Session.getHttpSession().setAttribute("currentHoleNumber", currentHoleNumber);
        
        // grab all the users in the GolfEventGroup that belong with this user.
        GolfEventGroup group = golferRound.getGolfTournamentGroup();
        List<GolferRound> players = (List<GolferRound>)_Session.getHttpSession().getAttribute("golfGroup");
        if (group == null && players == null) {
            players = new ArrayList<GolferRound>();
            players.add(golferRound);
        } else {
            if (group != null) {
                players = group.getGolferRounds("");
            }
        }
        
        _Session.getHttpSession().setAttribute("golfGroup", players);
//        if (user != null) {
//            session.getHttpSession().setAttribute("validUser", user);
//        }
        
  */      
        return page;
    }
    
}
