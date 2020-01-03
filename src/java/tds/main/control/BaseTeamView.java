package tds.main.control;

import bglib.util.FSUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tds.main.bo.*;

public class BaseTeamView extends BaseView {
    
    protected FSTeam _FSTeam = null;
    protected FSUser _FSUser = null;
    protected FSSeasonWeek _CurrentFSSeasonWeek = null;
    protected FSSeasonWeek _DisplayFSSeasonWeek = null;
    
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String errorPage = super.process(request, response);
        if (errorPage != null) {
            return errorPage;
        }
        
        _Session = UserSession.getUserSession(request, response);

        errorPage = setFSTeam(request);
        
        if (errorPage != null) {
            return errorPage;
        }
        
        // Check that the FSTeam was created
        _FSTeam = (FSTeam)_Session.getHttpSession().getAttribute("fsteam");

        if (_FSTeam == null) {
            _Session.setErrorMessage("Please select a team first.");
            return "../index";
        }

        // Check the User
        _FSUser = (FSUser)_Session.getHttpSession().getAttribute("validUser");
        if (_FSTeam.getFSUserID() != _FSUser.getFSUserID()) {
            _Session.setErrorMessage("Error : you do not have access to that team.");
            return "../index";
        }
        
        setWeeks();
        _CurrentFSSeasonWeek = (FSSeasonWeek)_Session.getHttpSession().getAttribute("fantasyCurrentWeek");
        return null;
    }

    public String setFSTeam(HttpServletRequest request) {

        int reqTeamID = FSUtils.getIntRequestParameter(request, "tid", 0);

        try {
            // Grab the fsTeam from the session
            FSTeam team = (FSTeam)_Session.getHttpSession().getAttribute("fsteam");

            // Populate the fsTeam that was passed in the request
            if (reqTeamID > 0) {
                team = new FSTeam(reqTeamID);
            }

            // If the fsTeam isn't in the session and wasn't passed in then we have a problem
            if (team == null) {
                _Session.setErrorMessage("Illegal team specified: TeamID = " + reqTeamID);
                return "../index";
            }

            FSUser user = (FSUser)_Session.getHttpSession().getAttribute("validUser");
            
            // If it's not part of the session then try and get the user from the fsTeam
            if (user == null) {
                user = team.getFSUser();
                _Session.getHttpSession().setAttribute("validUser", user);
            }

            // Make sure the FSUser logged in and the FSTeam's User are the same
            if (team.getFSUserID() != user.getFSUserID()) {
                _Session.setErrorMessage("Error : you do not have access to that team.");
                return "../index";
            }

            _Session.getHttpSession().setAttribute("fsteam", team);
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(request, e);
        }
            
        return null;
    }
    
    private void setWeeks() {

        if (_DisplayFSSeasonWeek == null ) {
            
            // Retrieve all of the FSSeasonWeek's for the entire FSSeason
            List<FSSeasonWeek> allWeeks = FSSeasonWeek.GetAllFSSeasonWeeks(_FSTeam.getFSLeague().getFSSeasonID());

            FSSeasonWeek prevWeek = null;
            FSSeasonWeek finalWeek = null;
            FSSeasonWeek initialWeek = null;
            weeks1 : for (FSSeasonWeek week : allWeeks) {

                if (week.getStatus().equals("CURRENT")) {
                    if (prevWeek == null) {
                        _DisplayFSSeasonWeek = week;
                    } else {
                        _DisplayFSSeasonWeek = prevWeek;
                    }
                    _CurrentFSSeasonWeek = week;
                }

                if (week.getWeekType().equals(FSSeasonWeek.WeekType.INITIAL.toString())) {
                    initialWeek = week;
                }

                // If we get to the final week then set it to this as long as the week hasn't been found and the session object didn't have anything (null)
                if (week.getWeekType().equals(FSSeasonWeek.WeekType.FINAL.toString())) {
                    finalWeek = week;
                }

                prevWeek = week;
            }     

            // Set it to be the current week as long as the session object didn't have anything (null)
            if (_CurrentFSSeasonWeek == null) {
                _DisplayFSSeasonWeek = finalWeek;
                _CurrentFSSeasonWeek = finalWeek;
            }

            _Session.getHttpSession().setAttribute("fantasyCurrentWeek", _CurrentFSSeasonWeek);
            _Session.getHttpSession().setAttribute("fantasyDisplayWeek", _DisplayFSSeasonWeek);
        }
    }
}