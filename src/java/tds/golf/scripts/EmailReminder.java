/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.golf.scripts;

import bglib.scripts.Harnessable;
import bglib.scripts.ResultCode;
import bglib.util.Application;
import tds.main.bo.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grant.keele
 */
public class EmailReminder implements Harnessable {

    Logger _Logger;
    ResultCode _ResultCode = ResultCode.RC_ERROR;
    String[] _Args;

    public EmailReminder() {
        _Logger = Logger.global;
        _Logger.setLevel(Level.ALL);
    }

    @Override
    public void setLogger(Logger logger) {
        _Logger = logger;
    }

    @Override
    public ResultCode getResultCode() { return _ResultCode; }

    @Override
    public void setScriptArgs(String[] args) { _Args = args; }

    public void run(int fsSeasonId) {
        try {

            _Logger.info("Starting to send Email Reminder...");
            preTournament(fsSeasonId);
            _Logger.info("Done with EmailReminder.");

            _ResultCode = ResultCode.RC_SUCCESS;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Exception in EmailReminder.run()", e);
        }
    }

    public void preTournament(int fsSeasonID) throws Exception {

        try {
            Map<String, FSSeasonWeek> weeksMap = FSSeasonWeek.GetCurrentWeekInfo(fsSeasonID);

            String emails = "";
            FSSeasonWeek currentFSSeasonWeek = weeksMap.get("currentFSSeasonWeek");
            if (currentFSSeasonWeek.getFSSeasonWeekID() > 0)
            {
                // get deadline
                LocalDateTime startersDeadline = currentFSSeasonWeek.getStartersDeadline();
                LocalDateTime today = LocalDateTime.now();
                System.out.println("Starters Deadline : " + startersDeadline);
                System.out.println("Today : " + today);

                // check to see if we are within 24 hours of the deadline.  If so, then send
                // out a reminder email
                today.plusHours(24);
                System.out.println("Today after adding 24 hours: " + today);
                if (today.isAfter(startersDeadline))
                {
                    // send email

                    PGATournamentWeek tournamentWeek = PGATournamentWeek.getTournamentWeek(currentFSSeasonWeek.getFSSeasonWeekID());

                    StringBuilder subject = new StringBuilder();
                    subject.append("TopDawgSports Golf : ").append(tournamentWeek.getPGATournament().getTournamentName());
                    StringBuilder body = new StringBuilder();

                    // grab the list of teams and send the email
                    FSSeason fsseason = new FSSeason(fsSeasonID);
                    List<FSLeague> leagues = fsseason.GetLeagues();

                    for (FSLeague league : leagues)
                    {

                        List<FSTeam> teams = league.GetTeams();
                        for (FSTeam team : teams)
                        {
                            String to = team.getFSUser().getEmail();
//                            String to = "grant@circlepix.com";
                            emails += to + ",";
                            // check to see if they have made picks yet
                            List<FSRoster> teamRoster = team.getRoster(currentFSSeasonWeek.getFSSeasonWeekID());
                            int rosterCount = teamRoster.size();
                            body.append("The data feed that I get to count each tournament doesn't have the money earnings for the Masters yet.  That will be updataed soon.  In the meantime, you can still pick golfers for this week. \n\r\n\r");
                            if (rosterCount == 0)
                            {
                                body.append("Your Fantasy Golf team, ").append(team.getTeamName()).append(", in the league '").append(league.getLeagueName());
                                body.append("' does not have any golfers yet.  The tournament starts soon.  Log in to www.topdawgsports.com to pick your golfers.  The dealine is: \n\r");
//                                body.append("Deadline : ").append(startersDeadline.toString("E MM/dd h:mm a")).append("\n\r\n\r");
                                body.append("Deadline : ").append(Application._DATE_TIME_FORMATTER.format(startersDeadline)).append("\n\r\n\r");
                                body.append("Good Luck,\n\r\n\r");
                                body.append("TopDawgSports Gaming Commission");

                            } else if (rosterCount < 6)
                            {
                                body.append("Your Fantasy Golf team, ").append(team.getTeamName()).append(", in the league '").append(league.getLeagueName());
                                body.append("' only has ").append(rosterCount).append(" golfer");
                                if (rosterCount > 1) { body.append("s"); }
                                body.append(".  You need to pick more golfers so that you have 6.  The tournament starts soon.  Log in to www.topdawgsports.com to pick your golfers.  The dealine is: \n\r");
//                                body.append("Deadline : ").append(startersDeadline.toString("E MM/dd h:mm a")).append("\n\r\n\r");
                                body.append("Deadline : ").append(Application._DATE_TIME_FORMATTER.format(startersDeadline)).append("\n\r\n\r");
                                body.append("Good Luck,\n\r\n\r");
                                body.append("TopDawgSports Gaming Commission");

                            } else
                            {
                                body.append("Your Fantasy Golf team, ").append(team.getTeamName()).append(", in the league '").append(league.getLeagueName());
                                body.append("' is all set and ready for this week's tournament.  Just to remind you, you can still make changes to your team until the deadline.\n\r");
                                body.append("This is a friendly reminder to pick your Golfers this week, if you haven't already.\n\r");
//                                body.append("Deadline : ").append(startersDeadline.toString("E MM/dd h:mm a")).append("\n\r\n\r");
                                body.append("Deadline : ").append(Application._DATE_TIME_FORMATTER.format(startersDeadline)).append("\n\r\n\r");
                                body.append("Good Luck,\n\r\n\r");
                                body.append("TopDawgSports Gaming Commission");
                            }

                            _Logger.info("sending reminder email to " + to);
//                            FSUtils.sendMessageEmail(to, subject.toString(), body.toString());
                        }
                    }
                }
            }

            System.out.println("Emails : " + emails);
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "EmailReminder Error : {0}", e.getMessage());
            e.printStackTrace();
        } finally {

        }

    }

    public static void main(String[] args) {
        try {
            EmailReminder field = new EmailReminder();

            int fsSeasonId = 56;

            if (args != null && args.length > 0) {
                try {
                    fsSeasonId = Integer.parseInt(args[0]);
                } catch (Exception e) {

                }
            }

            if (fsSeasonId == 0)
            {
                System.out.println("Error: You must pass in fsSeasonID as a param");
                return;
            }
            field.run(fsSeasonId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
