package tds.golf.control;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tds.main.bo.*;
import tds.main.control.BaseAction;

public class choosePlayersAction extends BaseAction {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        String prefix = "";
//        if ("local".equals(choosePlayersAction.host))
//        {
//            prefix = "golf/";
//        }
        
        nextPage = prefix + "choosePlayers";

        UserSession userSession = UserSession.getUserSession(request, response);
        HttpSession session = userSession.getHttpSession();
        
        String[] dropPlayers = request.getParameterValues("drop");
        String[] addPlayers = request.getParameterValues("pickup");
        
        try {
            // Create FSTeam obj
            FSTeam team = (FSTeam)session.getAttribute("fsteam");

            if (team == null) {
                userSession.setErrorMessage("Please select a team first.");
                return "../index";
            }

            FSUser user = (FSUser)session.getAttribute("validUser");
            if (team.getFSUserID() != user.getFSUserID()) {
                userSession.setErrorMessage("Error : you do not have access to that team.");
                return "../index";
            }
            int fsSeasonWeekId = Integer.parseInt(request.getParameter("fsSeasonWeekID"));
            
            if (fsSeasonWeekId < 1)
            {
                userSession.setErrorMessage("No Week set.  Please start over.");
                return nextPage;
            }
            
            PGATournamentWeek tournamentWeek = (PGATournamentWeek)session.getAttribute("tournamentWeek");
            
//            boolean deadlinePassed = tournamentWeek.getFSSeasonWeek().GetStartersDeadlineHasPassed();
//            if (deadlinePassed)
//            {
//                userSession.setErrorMessage("The Deadline has already passed.");
//                return nextPage;
//            }
            
            int maxActivePlayers = 6;
            double maxSalary = 1000000;
            int numActivePlayers = 0;
            double currentSalTotal = 0;
            
            // get the team's current roster
            List<FSRoster> curRosterList = team.getRoster(fsSeasonWeekId);
            request.setAttribute("yourRoster", curRosterList);
            
            List<PGATournamentWeekPlayer> curPlayerValues = new ArrayList<PGATournamentWeekPlayer>();
            
            for (FSRoster roster : curRosterList) {
                PGATournamentWeekPlayer temp = new PGATournamentWeekPlayer(tournamentWeek.getPGATournamentID(), fsSeasonWeekId, roster.getPlayerID());
                curPlayerValues.add(temp);
                currentSalTotal += temp.getSalaryValue();
                numActivePlayers++;
            }
            
            double newSalTotal = currentSalTotal;
            
            // subtract the new drops
            if (dropPlayers != null && dropPlayers.length > 0) {
                for (String drop : dropPlayers) {
                    numActivePlayers--;
                }
            }            
            
            // add the new adds
            if (addPlayers != null && addPlayers.length > 0) {
                for (String add : addPlayers)
                {
                    StringTokenizer st = new StringTokenizer(add,"_");
                    String id = st.nextToken();
                    String tempValue = st.nextToken();

                    double value = Double.parseDouble(tempValue);
                    newSalTotal += value;
                    numActivePlayers++;
                }
            }
            
            if (newSalTotal > maxSalary)
            {
                userSession.setErrorMessage("That will put you over the Salary Cap.  Try again.");
                return nextPage;
            }
            
            if (numActivePlayers > maxActivePlayers)
            {
                userSession.setErrorMessage("That will give you more than 6 players.  Try again.");
                return nextPage;
            }
            
            // Go ahead and make these changes now
            // subtract the new drops
            if (dropPlayers != null && dropPlayers.length > 0) {
                for (String drop : dropPlayers) {
                    StringTokenizer st = new StringTokenizer(drop,"_");
                    String id = st.nextToken();
                    String tempValue = st.nextToken();
                    
                    FSRoster roster = new FSRoster(Integer.parseInt(id));
                    
                    if (roster != null)
                    {
                        // check to see if this player has already started the tournament
                        PGATournamentWeekPlayer temp = new PGATournamentWeekPlayer(tournamentWeek.getPGATournamentID(), fsSeasonWeekId, roster.getPlayerID());
                        if (!temp.hasStartedTournament())
                        {
                            roster.Delete();
                        }
                    }
                    
                }
            }            
            
            // add the new adds
            if (addPlayers != null && addPlayers.length > 0) {
                for (String add : addPlayers)
                {
                    StringTokenizer st = new StringTokenizer(add,"_");
                    String id = st.nextToken();
                    String tempValue = st.nextToken();

                    // check to see if this player has already started the tournament
                    PGATournamentWeekPlayer temp = new PGATournamentWeekPlayer(tournamentWeek.getPGATournamentID(), fsSeasonWeekId, Integer.parseInt(id));
                    if (!temp.hasStartedTournament())
                    {
                        FSRoster rosterspot = new FSRoster();

                        rosterspot.setFSSeasonWeekID(fsSeasonWeekId);
                        rosterspot.setActiveState("active");
                        rosterspot.setFSTeamID(team.getFSTeamID());
                        rosterspot.setPlayerID(Integer.parseInt(id));

                        rosterspot.Save();
                    }
                }
            }
        } catch (Exception e) {
//            System.out.println("Error : " + e.getMessage());
            CTApplication._CT_LOG.error(request, e);
            userSession.setErrorMessage("Error processing team changes.");
        }

        return nextPage;
    }
}
