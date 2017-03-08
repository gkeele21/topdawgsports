package tds.main.control;

import bglib.util.FSUtils;
import tds.main.bo.FSLeague;
import tds.main.bo.FSSeason;
import tds.main.bo.FSUser;
import tds.main.bo.UserSession;
import static tds.main.bo.CTApplication._CT_LOG;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class registerGamesAction extends BaseAction {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request,response);
        if (page != null) {
            return page;
        }

        UserSession session = UserSession.getUserSession(request, response);

        //page="forward:BranchAdd";
        page = "registerGames";

        try {
            FSUser user = (FSUser)session.getHttpSession().getAttribute("validUser");

            double fantasypts = 0;
            double totalfantasypts = 0;
            double gamepoints = 0;
            double totalgamepoints = 0;
            int salaryspent = 0;
            int wins = 0;
            int losses = 0;
            int ties = 0;
            double winpercentage = 0;
            double fantasyptsagainst = 0;
            int hiscore = 0;
            int totalhiscores = 0;
            int gamescorrect = 0;
            int totalgamescorrect = 0;
            int gameswrong = 0;
            int totalgameswrong = 0;
            int rank = 0;
            int currentstreak = 0;
            String lastfive = "";

            // SALARY CAP
            String salCapSelected = FSUtils.getRequestParameter(request, "salarycap");
            
            if (salCapSelected != null && salCapSelected.equals("on")) {
                // add salary cap game
                int fsleagueid = 20;
                FSLeague league = new FSLeague(fsleagueid);
                String teamname = user.getFullName();
                int fsteamid = league.AddTeam(user, teamname);

                FSSeason season = league.getFSSeason();
                int fsseasonweekid = season.getCurrentFSSeasonWeekID();

                if (fsteamid > 0) {
                    
                    // success - insert a record into the standings table
                    league.InsertIntoStandings(fsteamid,fsseasonweekid,fantasypts,totalfantasypts,
                            gamepoints,totalgamepoints,salaryspent,wins,losses,ties,winpercentage,
                            fantasyptsagainst,hiscore,totalhiscores,gamescorrect,totalgamescorrect,
                            gameswrong,totalgameswrong,rank,currentstreak,lastfive);
                    
                } else {
                    session.setErrorMessage("Error : could not create a Salary Cap team for you.  Please try again or contact Bert for help.");
                    return page;
                }
            }

            // PRO PICKEM
            String nflPredSelected = FSUtils.getRequestParameter(request, "nflpickem");
            
            if (nflPredSelected != null && nflPredSelected.equals("on")) {
                // add pro predictions game
                int fsleagueid = 21;
                FSLeague league = new FSLeague(fsleagueid);
                String teamname = user.getFullName();
                int fsteamid = league.AddTeam(user, teamname);

                FSSeason season = league.getFSSeason();
                int fsseasonweekid = season.getCurrentFSSeasonWeekID();
                
                if (fsteamid > 0) {
                    // success - insert a record into the standings table
                    league.InsertIntoStandings(fsteamid,fsseasonweekid,fantasypts,totalfantasypts,
                            gamepoints,totalgamepoints,salaryspent,wins,losses,ties,winpercentage,
                            fantasyptsagainst,hiscore,totalhiscores,gamescorrect,totalgamescorrect,
                            gameswrong,totalgameswrong,rank,currentstreak,lastfive);

                } else {
                    session.setErrorMessage("Error : could not create an NFL Predictions team for you.  Please try again or contact Bert for help.");
                    return page;
                }
            }

            // COLLEGE PICKEM
            String ncaaPredSelected = FSUtils.getRequestParameter(request, "ncaapickem");
            
            if (ncaaPredSelected != null && ncaaPredSelected.equals("on")) {
                // add College predictions game
                int fsleagueid = 24;
                FSLeague league = new FSLeague(fsleagueid);
                String teamname = user.getFullName();
                int fsteamid = league.AddTeam(user, teamname);

                FSSeason season = league.getFSSeason();
                int fsseasonweekid = season.getCurrentFSSeasonWeekID();
                
                if (fsteamid > 0) {
                    // success - insert a record into the standings table
                    league.InsertIntoStandings(fsteamid,fsseasonweekid,fantasypts,totalfantasypts,
                            gamepoints,totalgamepoints,salaryspent,wins,losses,ties,winpercentage,
                            fantasyptsagainst,hiscore,totalhiscores,gamescorrect,totalgamescorrect,
                            gameswrong,totalgameswrong,rank,currentstreak,lastfive);

                } else {
                    session.setErrorMessage("Error : could not create an Pro Predictions team for you.  Please try again or contact Bert for help.");
                    return page;
                }
            }

            // PRO LOVE EM AND LEAVE EM
            String nflLoveEmSelected = FSUtils.getRequestParameter(request, "nflloveem");
            
            if (nflLoveEmSelected != null && nflLoveEmSelected.equals("on")) {
                // add nfl love em game
                int fsleagueid = 22;
                FSLeague league = new FSLeague(fsleagueid);
                String teamname = user.getFullName();
                int fsteamid = league.AddTeam(user, teamname);

                FSSeason season = league.getFSSeason();
                int fsseasonweekid = season.getCurrentFSSeasonWeekID();

                if (fsteamid > 0) {
                    // success - insert a record into the standings table
                    league.InsertIntoStandings(fsteamid,fsseasonweekid,fantasypts,totalfantasypts,
                            gamepoints,totalgamepoints,salaryspent,wins,losses,ties,winpercentage,
                            fantasyptsagainst,hiscore,totalhiscores,gamescorrect,totalgamescorrect,
                            gameswrong,totalgameswrong,rank,currentstreak,lastfive);
                } else {
                    session.setErrorMessage("Error : could not create an Pro Love 'Em and Leave 'Em team for you.  Please try again or contact Bert for help.");
                    return page;
                }
            }

            // COLLEGE LOVE EM AND LEAVE EM
            String collLoveEmSelected = FSUtils.getRequestParameter(request, "collegeloveem");
            
            if (collLoveEmSelected != null && collLoveEmSelected.equals("on")) {
                // add college love em game
                int fsleagueid = 23;
                FSLeague league = new FSLeague(fsleagueid);
                String teamname = user.getFullName();
                int fsteamid = league.AddTeam(user, teamname);
                
                FSSeason season = league.getFSSeason();
                int fsseasonweekid = season.getCurrentFSSeasonWeekID();

                if (fsteamid > 0) {
                    // success - insert a record into the standings table
                    league.InsertIntoStandings(fsteamid,fsseasonweekid,fantasypts,totalfantasypts,
                            gamepoints,totalgamepoints,salaryspent,wins,losses,ties,winpercentage,
                            fantasyptsagainst,hiscore,totalhiscores,gamescorrect,totalgamescorrect,
                            gameswrong,totalgameswrong,rank,currentstreak,lastfive);
                } else {
                    session.setErrorMessage("Error : could not create a College Love 'em and Leave em team for you.  Please try again or contact Bert for help.");
                    return page;
                }
            }

            page = "registerComplete";
            
        } catch (Exception e) {
            e.printStackTrace();
            _CT_LOG.error(e);
        }
        
        return page;
    }
}
