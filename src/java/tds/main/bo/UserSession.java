package tds.main.bo;

import bglib.util.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static tds.main.bo.CTApplication._CT_LOG;

public class UserSession implements BGUserSession {

    public enum UserSessionState { ACTIVE, USER_LOGGED_OUT }
    
    private HttpServletRequest _Request;
    private HttpServletResponse _Response;
    private HttpSession _Session;
    private int _SessionID;
    private String _JSessionID;
    private AuDate _SessionStarted;
    private AuDate _SessionExpired;
    private int _ExpirationTypeID;
    private String _IPAddress;
    private String _CountryCode;
    private List<String> _SessionAttributes = new ArrayList<String>();

    private static final int _SessionMinutes = 30;

    public static Map<String, FSUser> _UserCache = new HashMap<String, FSUser>();

    // PUBLIC METHODS

    public FSUser getLoggedInUser() {
        return (FSUser)_Session.getAttribute("validUser");
    }
    
    public FSTeam getLoggedInTeam() {
        return (FSTeam)_Session.getAttribute("fsteam");
    }
    
    public FSSeasonWeek getCurrentWeek() {
        return (FSSeasonWeek)_Session.getAttribute("fsseasonweek");
    }

    public FSSeasonWeek getCurrentSalWeek() {
        System.out.println("Howdy");
        return (FSSeasonWeek)_Session.getAttribute("salfsseasonweek");
    }
    
    public HttpServletRequest getRequest() { return _Request; } 
    public int getSessionID() {   return _SessionID;   }
    public String getJSessionID() {    return _JSessionID;   }
    public AuDate getSessionStarted() {    return _SessionStarted;   }
    public AuDate getSessionExpired() {    return _SessionExpired;   }
    public int getExpirationTypeID() {    return _ExpirationTypeID;   }
    public String getIPAddress() {    return _IPAddress;   }
    public String getCountryCode() {    return _CountryCode;   }

    public void setJSessionID(String id) {    _JSessionID = id;   }
    public void setSessionExpired(AuDate dt) {    _SessionExpired = dt;   }
    public void setExpirationTypeID(int temp) {    _ExpirationTypeID = temp;   }

    public boolean isValid() {
        return (_ExpirationTypeID == UserSessionState.ACTIVE.ordinal());
    }

    public void getAttributes() {
        if (_SessionAttributes != null && _SessionAttributes.size() > 0) {

        }
    }

    public HttpSession getHttpSession() { return _Session; }

    public void clearErrorMessage() {
        _Session.setAttribute(BGConstants.ERROR_MESSAGE_ATTR, null);
    }
    
    @Override
    public void setErrorMessage(String msg) {
        setErrorMessage(new ErrorMessage(msg));
    }

    public void setErrorMessage(ErrorMessage msg) {
        _Session.setAttribute(BGConstants.ERROR_MESSAGE_ATTR, (msg==null) ? null : msg.getFormattedMessage());
    }

    public void setErrorMessage(FormError form) {
        setErrorMessage(form.getCompositeErrorMessage());
        _Session.setAttribute(BGConstants.FORM_ERROR_ATTR, form);
    }

    public void refreshUser() {
        try {
            FSUser user = getLoggedInUser();
            if (user != null) {
                _Session.setAttribute("validUser", user);
            }
        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    public static final Map<HttpSession, UserSession> _UserSessions = new HashMap<HttpSession, UserSession>();
    public static final Random _Random = new Random();

    public static UserSession getUserSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession httpSession = request.getSession();
        UserSession session = _UserSessions.get(httpSession);
        if (session==null) {
            session = new UserSession(request, response);
            _UserSessions.put(httpSession, session);
        }

        // one out of every 10 times we clean up the _UserSessions map
        //if (_Random.nextInt(10)==5) {
            cleanUpSessions();
        //}

        return session;
    }
    
    // PRIVATE METHODS
    
    private UserSession(HttpServletRequest request, HttpServletResponse response) {
        _Request = request;
        _Response = response;
        _Session = request.getSession();
        try {
            FSUser user = _UserCache.get(_Session.getId());
            if (user !=null) {
                _Session.setAttribute("validUser", user);
            }
        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    private static void cleanUpSessions() {
        long now = new AuDate().getDateInMillis();
        List<HttpSession> toRemove = new ArrayList<HttpSession>();
        for (HttpSession httpSession : _UserSessions.keySet()) {
            try {
                if ((now-httpSession.getLastAccessedTime()) > (httpSession.getMaxInactiveInterval()*1000)) {
                    toRemove.add(httpSession);
                }
            } catch (Exception e) {
                toRemove.add(httpSession);
            }
        }

        // must actually remove the items separately to avoid concurrency errors
        for (HttpSession httpSession : toRemove) {
            _UserSessions.remove(httpSession);
        }
    }

}

