package tds.fantasy.control;

import tds.main.bo.*;
import tds.main.control.BaseAction;
import tds.util.CTReturnCode;
import tds.util.CTReturnType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class faConfirmAction extends BaseAction {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String nextPage = super.process(request,response);
        if (nextPage != null) {
            return nextPage;
        }

        System.out.println("After parent call.");
        nextPage = "yourPlayers";
        UserSession session = UserSession.getUserSession(request, response);

        // check to see if they hit cancel or accept
        String cancel = request.getParameter("cancel.x");
        if (cancel != null) {
            System.out.println("User hit Cancel button");
            session.getHttpSession().removeAttribute("dropRosterSpoot");
            session.getHttpSession().removeAttribute("dropType");
            session.getHttpSession().removeAttribute("addType");
            session.getHttpSession().removeAttribute("puPlayer");
            session.getHttpSession().removeAttribute("faTransaction");

            return "faAcquirePlayer";
        }

        FSFootballTransaction transaction = (FSFootballTransaction)session.getHttpSession().getAttribute("faTransaction");

        if (transaction == null) {
            System.out.println("Transaction obj is null - returning to page again...");
            return nextPage;
        }

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

            if (team.getFSTeamID() != transaction.getFSTeamID()) {
                session.setErrorMessage("Error : you do not have access to that team.");
                return "../index";
            }

            int fsseasonid = team.getFSLeague().getFSSeasonID();
            FSFootballSeasonDetail seasonDetail = new FSFootballSeasonDetail(fsseasonid);

            FSSeasonWeek currFSSeasonWeek = (FSSeasonWeek)session.getHttpSession().getAttribute("fantasyCurrentWeek");
            // do the validation for this transaction to be valid

            boolean valid = true;

            Map<Integer,Integer> rosterPositions = new HashMap<Integer,Integer>();

            List<FSRoster> activeRoster = team.getRoster(currFSSeasonWeek.getFSSeasonWeekID());
            for (FSRoster roster : activeRoster) {
                int posid = roster.getPlayer().getPositionID();
                // if league does not use TE, then treat them as WR
                if (team.getFSLeague().getIncludeTEasWR() == 1) {
                    if (posid == 4) {
                        posid = 3;
                    }
                }
                int currnum = 0;
                if (rosterPositions.containsKey(posid)) {
                    currnum = rosterPositions.get(posid);
                }

                currnum++;
                rosterPositions.put(posid,currnum);
            }

            Player puPlayer = transaction.getPUPlayer();
            int puposid = puPlayer.getPositionID();
            if (team.getFSLeague().getIncludeTEasWR() == 1) {
                if (puposid == 4) {
                    puposid = 3;
                }
            }

            int numPlayers = 0;
            if (rosterPositions.containsKey(puposid)) {
                numPlayers = rosterPositions.get(puposid);
            }
            numPlayers++;
            rosterPositions.put(puposid,numPlayers);

            Player dropPlayer = transaction.getDropPlayer();
            int dropposid = puPlayer.getPositionID();
            if (team.getFSLeague().getIncludeTEasWR() == 1) {
                if (dropposid == 4) {
                    dropposid = 3;
                }
            }

            numPlayers = rosterPositions.get(dropposid);
            numPlayers--;

           // check if the player being picked up will make too many players for a given position.
           FSFootballRosterPositions position = new FSFootballRosterPositions(currFSSeasonWeek.getFSSeasonID(),puposid,team.getFSLeagueID());
           if (rosterPositions.containsKey(puposid)) {
               numPlayers = rosterPositions.get(puposid);
           }
            if (numPlayers > position.getMaxNum()) {
                valid = false;
                errorMsg = "ERROR : You can only carry " + position.getMaxNum() + " " + puPlayer.getPosition().getPositionName() + "s.";
            }

            // check if the player being dropped will make too few players for a given position.

            position = new FSFootballRosterPositions(currFSSeasonWeek.getFSSeasonID(),dropposid, team.getFSLeagueID());
            numPlayers = rosterPositions.get(dropposid);
            // for each league
            int minnum = position.getMinNum();
            //if (team.getFSLeagueID() == 10 && position.getPositionID() == 4) {
                //minnum = 0;
            //}
            if (numPlayers < minnum) {
                valid = false;
                errorMsg = "ERROR : You must carry at least " + position.getMinNum() + " " + dropPlayer.getPosition().getPositionName() + "s.";
            }

            // TODO: check to see if either player involved has started their game for this week.rags

            if (valid) {

                // check to see if the transaction deadline has passed.  If not, add this transaction into the requests table.
                // otherwise, add the transaction into the normal FSFootballTransactions table
                System.out.println("Right before the date check...");
                LocalDateTime dt = currFSSeasonWeek.getTransactionDeadline();
                LocalDateTime now = LocalDateTime.now();
                if (now.isBefore(dt)) {
                    FSFootballTransactionRequest trequest = new FSFootballTransactionRequest();
                    trequest.setDropPlayerID(transaction.getDropPlayerID());
                    trequest.setDropType(transaction.getDropType());
                    trequest.setFSSeasonWeekID(transaction.getFSSeasonWeekID());
                    trequest.setFSTeamID(transaction.getFSTeamID());
                    trequest.setGranted(0);
                    trequest.setPUPlayerID(transaction.getPUPlayerID());
                    trequest.setPUType(transaction.getPUType());
                    trequest.setProcessed(0);

                    // find last rank for this team
                    int lastrank = 0;
                    List<FSFootballTransactionRequest> teamrequests = FSFootballTransactionRequest.getTransactionRequests(team.getFSTeamID(), transaction.getFSSeasonWeekID());
                    for (FSFootballTransactionRequest teamrequest : teamrequests) {
                        lastrank = teamrequest.getRank();
                    }

                    trequest.setRank(lastrank+1);
                    trequest.setRequestDate(LocalDateTime.now());

                    CTReturnCode ret = trequest.insert();
                    if (ret.isSuccess()) {
                        // clear out all the session stuff
                        session.getHttpSession().removeAttribute("dropRosterSpot");
                        session.getHttpSession().removeAttribute("dropType");
                        session.getHttpSession().removeAttribute("addType");
                        session.getHttpSession().removeAttribute("faTransaction");
                        session.getHttpSession().removeAttribute("puPlayer");
                        nextPage = "transactionRequests";
                    } else {
                        errorMsg = "Error : could not insert your transaction request.  Please try again.";
                    }

                } else {
                    CTReturnCode rc = transaction.insert();
                    if (rc.isSuccess()) {
                        CTReturnCode retPU = new CTReturnCode(CTReturnType.SUCCESS);
                        CTReturnCode retDROP;
                        FSRoster dropRosterSpot = (FSRoster) session.getHttpSession().getAttribute("dropRosterSpot");

                        // For the player being 'picked up'
                        if ("OFFIR".equals(transaction.getPUType())) {
                            // find the roster spot for this player
                            FSRoster puRoster = FSRoster.getRosterByPlayerID(transaction.getFSTeamID(), transaction.getFSSeasonWeekID(), transaction.getPUPlayerID());
                            if (puRoster != null) {
                                puRoster.setActiveState("active");
                                retPU = puRoster.Save();
                            }
                        } else {
                            // PUType = 'PU'
                            // add new roster spot based on the old spot
                            FSRoster newRoster = new FSRoster();

                            newRoster.setActiveState(dropRosterSpot.getActiveState());
                            newRoster.setFSSeasonWeekID(dropRosterSpot.getFSSeasonWeekID());
                            newRoster.setFSTeamID(dropRosterSpot.getFSTeamID());
                            newRoster.setPlayerID(transaction.getPUPlayerID());
                            newRoster.setStarterState(dropRosterSpot.getStarterState());
                            retPU = newRoster.Save();
                        }

                        // For the player being 'dropped'
                        if ("ONIR".equals(transaction.getDropType())) {
                            // set existing record to ir
                            dropRosterSpot.setActiveState("ir");
                            retDROP = dropRosterSpot.Save();
                        } else if ("ONIR-COVID".equals(transaction.getDropType())) {
                            // set existing record to ir
                            dropRosterSpot.setActiveState("ir-covid");
                            retDROP = dropRosterSpot.Save();
                        } else {
                            // dropType = 'DROP'
                            retDROP = dropRosterSpot.Delete();
                        }

                        if (retDROP.isSuccess() && retPU.isSuccess()) {
                            // clear out all the session stuff
                            session.getHttpSession().removeAttribute("dropRosterSpot");
                            session.getHttpSession().removeAttribute("faTransaction");
                            session.getHttpSession().removeAttribute("puPlayer");
                            session.getHttpSession().removeAttribute("dropType");
                            session.getHttpSession().removeAttribute("addType");
                        } else {
                            errorMsg = retDROP + " : " + retPU.toString();
                        }

                    } else {
                        errorMsg = "Error : could not process your transaction.  Please try again.";
                    }

                }
            }

        } catch (Exception e) {
//            CTApplication._CT_LOG.error(e);
            e.printStackTrace();
            //System.out.println("Error : " + e.getMessage());
            errorMsg = "Error processing team changes.";
        }

        if (!errorMsg.equals("")) {
            session.setErrorMessage(errorMsg);
            return "faConfirm";
        }

        return nextPage;
    }
}
