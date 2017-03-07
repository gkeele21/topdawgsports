package tds.fantasy.control;

import tds.main.control.BaseAction;
import tds.main.bo.FSFootballRosterPositions;
import tds.main.bo.FSFootballSeasonDetail;
import tds.main.bo.FSRoster;
import tds.main.bo.FSTeam;
import tds.main.bo.FSUser;
import tds.main.bo.Game;
import tds.main.bo.Player;
import tds.main.bo.Position;
import tds.main.bo.UserSession;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.ListIterator;

public class yourPlayersAction extends BaseAction {

    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        nextPage = "yourPlayers";
        System.out.println("Made it in yourPlayersAction!");
        UserSession session = UserSession.getUserSession(request, response);

        String[] activeStates = request.getParameterValues("selActiveState");
        String[] hiddenStates = request.getParameterValues("hfState");
        String errorMsg = "";
        
        try {
            // Create FSTeam obj
            FSTeam team = (FSTeam)session.getHttpSession().getAttribute("fsteam");

            if (team == null) {
                session.setErrorMessage("Please select a team first.");
                return "../index";
            }

            FSUser user = (FSUser)session.getHttpSession().getAttribute("validUser");
            if (team.getFSUserID() != user.getFSUserID()) {
                session.setErrorMessage("Error : you do not have access to that team.");
                return "../index";
            }
            
            int fsseasonid = team.getFSLeague().getFSSeasonID();
            FSFootballSeasonDetail seasonDetail = new FSFootballSeasonDetail(fsseasonid);
            
            int maxinactive = seasonDetail.getMaxNumInactive();
            int maxactive = seasonDetail.getMaxNumReserves() + seasonDetail.getMaxNumStarters();
            
            boolean valid = true;
            
            int numinactives = 0;
            int numactives = 0;
            
            // Positions
            boolean includeTEasWR = true;
//            if (team.getFSLeagueID() == 10) {
//                includeTEasWR =  true;
//            }
            
            List list = includeTEasWR ? Arrays.asList(new String[]{"1","2","3,4","5"}) : Arrays.asList(new String[]{"1","2","3","4","5"});
            Map<Integer,Integer> posMap = new HashMap<Integer,Integer>();
            
            // cycle through the rows with listboxes and make sure the new roster is valid
            for (String state : activeStates) {
                // process this roster spot with the new activeState
                StringTokenizer st = new StringTokenizer(state,"_");
                String id = st.nextToken();
                String newstate = st.nextToken();
                
                if (newstate.equals("inactive")) {
                    numinactives++;
                } else if (newstate.equals("active")) {
                    numactives++;
                }
                
                FSRoster rosterspot = new FSRoster(Integer.parseInt(id));

                // check to see if this player's game has already started
                Player player = rosterspot.getPlayer();
                if (player.getTeam().getGameHasStarted() && player.getTeam().getGame(team.getFSLeague().getFSSeason().getSeasonID(),rosterspot.getFSSeasonWeekID()) != Game.BYE_WEEK) {
                    valid = false;
                    errorMsg = "ERROR : A game has already started for 1 of the players you're trying to change. ";
                }

                Player rosterPlayer = rosterspot.getPlayer();
                int posid = rosterPlayer.getPositionID();
                if (includeTEasWR && posid == 4) {
                    posid = 3;
                }
                
                int currnum = 0;
                if (posMap.containsKey(posid)) {
                    currnum = posMap.get(posid);
                }
                if (newstate.equals("active")) {
                    currnum++;
                }
                
                posMap.put(posid, currnum);

            }

            // cycle through the rows with hiddenfields and make sure the new roster is valid
            if (hiddenStates != null && hiddenStates.length > 0) {
                for (String state : hiddenStates) {
                    // process this roster spot with the new activeState
                    StringTokenizer st = new StringTokenizer(state,"_");
                    String id = st.nextToken();
                    String newstate = st.nextToken();

                    if (newstate.equals("inactive")) {
                        numinactives++;
                    } else if (newstate.equals("active")) {
                        numactives++;
                    }

                    FSRoster rosterspot = new FSRoster(Integer.parseInt(id));

                    Player rosterPlayer = rosterspot.getPlayer();
                    int posid = rosterPlayer.getPositionID();
                    if (includeTEasWR && posid == 4) {
                        posid = 3;
                    }

                    int currnum = 0;
                    if (posMap.containsKey(posid)) {
                        currnum = posMap.get(posid);
                    }
                    if (newstate.equals("active")) {
                        currnum++;
                    }

                    posMap.put(posid, currnum);

                }
                
            }

            // I'm checking the TOTAL # of players that can be active, but I also
            // need to check that for each position.
            for (ListIterator i=list.listIterator();i.hasNext();) {
                String pos = (String)i.next();

                int posid = 0;
                if (includeTEasWR && pos.startsWith("3")) {
                    posid = 3;
                } else {
                    posid = Integer.parseInt(pos);
                }

                //int posid = pos.startsWith("3") ? 3 : Integer.parseInt(pos);

                FSFootballRosterPositions rosterPositions = new FSFootballRosterPositions(fsseasonid, posid);
                int minactive = rosterPositions.getMinActive();

                int numactive = posMap.get(posid);
                if (numactive < minactive) {
                    valid = false;
                    Position position = Position.getInstance(posid);
                    errorMsg = "ERROR : You must have at least " + minactive + " players at " + position.getPositionName() + " on the Active list.";
                }
            }
            
            if (numinactives > maxinactive) {
                valid = false;
                errorMsg = "ERROR : You can only have " + maxinactive + " players on the Inactive list.";
            } else if (numactives > maxactive) {
                valid = false;
                errorMsg = "ERROR : You can only have " + maxactive + " players Active.";
            }
            
            if (valid) {
                // cycle through and make sure the new roster is valid
                for (String state : activeStates) {
                    // process this roster spot with the new activeState
                    StringTokenizer st = new StringTokenizer(state,"_");
                    String id = st.nextToken();
                    String newstate = st.nextToken();

                    FSRoster rosterspot = new FSRoster(Integer.parseInt(id));
                    
                    rosterspot.setActiveState(newstate);
                    
                    rosterspot.Save();
                }

            } else {
                UserSession.getUserSession(request, response).setErrorMessage(errorMsg);
            }

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            UserSession.getUserSession(request, response).setErrorMessage("Error processing team changes.");
        }

        return nextPage;
    }
}
