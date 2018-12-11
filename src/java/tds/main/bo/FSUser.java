package tds.main.bo;

import bglib.data.JDBCDatabase;
import bglib.util.AuDate;
import bglib.util.FSUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang.StringEscapeUtils;
import sun.jdbc.rowset.CachedRowSet;
import static tds.data.CTColumnLists._Cols;
import tds.data.CTDataSetDef;
import static tds.main.bo.CTApplication._CT_DB;
import static tds.main.bo.CTApplication._CT_LOG;
import tds.util.CTReturnCode;
import static tds.util.CTReturnCode.RC_DB_ERROR;
import static tds.util.CTReturnType.SUCCESS;

public class FSUser implements Serializable {

    // DB FIELDS
    private int _FSUserID;
    private String _Username;
    private String _Password;
    private AuDate _DateCreated;
    private String _FirstName;
    private String _LastName;
    private String _Email;
    private String _Telephone;
    private String _Address;
    private String _City;
    private String _State;
    private String _Zip;
    private String _Country;
    private AuDate _Birthdate;
    private AuDate _LastLogin;
    private boolean _BadEmailAddress;
    private boolean _SendActionAlerts;
    private String _Address2;
    private String _AltTelephone;
    private String _AuthenticationKey;
    private boolean _IsActive;
    private String _LeadSource;
    
    // OTHER FIELDS
    private List<FSTeam> _FSTeams;
    
    // CONSTRUCTORS
    public FSUser() throws Exception {

    }

    public FSUser(int userID) {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSUser", "u.", ""));
            sql.append(" FROM FSUser u ");
            sql.append(" WHERE u.FSUserID = ").append(userID);

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            crs.next();
            initFromCRS(crs, "");
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

    }

    public FSUser(String username) throws Exception {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSUser", "u.", ""));           
            sql.append("FROM FSUser u ");
            sql.append("WHERE UserName='").append(StringEscapeUtils.escapeSql(username)).append("'");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "");
            } else {
                throw new Exception("Invalid username combination - Username [" + username + "]");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            throw new Exception(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public FSUser(String username,String password) throws Exception {
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSUser", "u.", ""));           
            sql.append("FROM FSUser u ");
            sql.append("WHERE UserName='").append(StringEscapeUtils.escapeSql(username)).append("'");
            sql.append("AND Password='").append(StringEscapeUtils.escapeSql(password)).append("'");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                initFromCRS(crs, "");
            } else {
                throw new Exception("Invalid username/password combination - Username [" + username + "] Password [" + password + "]");
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
            throw new Exception(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }
    }

    public FSUser(CachedRowSet fields) {
        initFromCRS(fields, "");
    }

    public FSUser(CachedRowSet fields, String prefix) {
        initFromCRS(fields, prefix);
    }

    // GETTERS    
    public int getFSUserID() { return _FSUserID; }
    public String getUsername() { return _Username; }
    public String getPassword() { return _Password; }
    public AuDate getDateCreated() { return _DateCreated; }
    public String getFirstName() { return _FirstName; }
    public String getLastName() { return _LastName; }
    public String getFullName() { return _FirstName + " " + _LastName; }
    public String getEmail() { return _Email; }
    public String getTelephone() { return _Telephone; }
    public String getAltTelephone() { return _AltTelephone; }
    public String getAddress() { return _Address; }
    public String getAddress2() { return _Address2; }
    public String getCity() { return _City; }
    public String getState() { return _State; }
    public String getZip() { return _Zip; }
    public String getCountry() { return _Country; }
    public String getLeadSource() { return _LeadSource; }
    public AuDate getBirthdate() { return _Birthdate; }
    public AuDate getLastLogin() { return _LastLogin; }
    public boolean isBadEmailAddress() { return _BadEmailAddress; }
    public boolean isActive() { return _IsActive; }
    public boolean isSendActionAlerts() { return _SendActionAlerts; }
    public String getAuthenticationKey() { return _AuthenticationKey; }
    public List<FSTeam> getFSTeams() {return _FSTeams;}
    
    // SETTERS    
    public void setFSUserID(int FSUserID) {_FSUserID = FSUserID;}
    public void setUsername(String Username) {_Username = Username;}
    public void setPassword(String Password) {_Password = Password;}
    public void setDateCreated(AuDate DateCreated) {_DateCreated = DateCreated;}
    public void setFirstName(String FirstName) {_FirstName = FirstName;}
    public void setLastName(String LastName) {_LastName = LastName;}
    public void setEmail(String Email) {_Email = Email;}
    public void setTelephone(String Telephone) {_Telephone = Telephone;}
    public void setAddress(String Address) {_Address = Address;}
    public void setCity(String City) {_City = City;}
    public void setState(String State) {_State = State;}
    public void setZip(String Zip) {_Zip = Zip;}
    public void setCountry(String Country) {_Country = Country;}
    public void setBirthdate(AuDate Birthdate) {_Birthdate = Birthdate;}
    public void setLastLogin(AuDate LastLogin) {_LastLogin = LastLogin;}
    public void setBadEmailAddress(boolean BadEmailAddress) {_BadEmailAddress = BadEmailAddress;}
    public void setSendActionAlerts(boolean SendActionAlerts) {_SendActionAlerts = SendActionAlerts;}
    public void setAddress2(String Address2) {_Address2 = Address2;}
    public void setAltTelephone(String AltTelephone) {_AltTelephone = AltTelephone;}
    public void setAuthenticationKey(String AuthenticationKey) {_AuthenticationKey = AuthenticationKey;}
    public void setIsActive(boolean IsActive) {_IsActive = IsActive;}
    public void setLeadSource(String LeadSource) {_LeadSource = LeadSource;}
    public void setFSTeams(List<FSTeam> FSTeams) {_FSTeams = FSTeams;}
    
    // PUBLIC METHODS

    public CTReturnCode updateUserFields(String firstName, String lastName, String address,
                                         String city, String phone, String password, String email) {
        int res = 0;
        try {
            res = _CT_DB.updateDataSet(CTDataSetDef.UPDATE_FSUSER, firstName, lastName, address, city, phone, password, email, getFSUserID());
        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }

        return (res>0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
    }

    public CTReturnCode setLastLogin() {
        int res = 0;
        try {
            res = _CT_DB.updateDataSet(CTDataSetDef.UPDATE_LAST_LOGIN, new java.sql.Timestamp(new AuDate().getDateInMillis()), getFSUserID());
        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }

        return (res>0) ? CTReturnCode.RC_SUCCESS : CTReturnCode.RC_DB_ERROR;
    }

    public static int addNewUser(String username, String password, String firstName, String lastName, String email,
                                          String phone, String altPhone, String address, String address2, String city,
                                          String state, String zip, String country, String birthDate,
                                          String leadSource, int sendActionAlerts,String authKey) throws Exception {

        System.out.println("adding new user");
        String procCall = CTApplication.TBL_PREF + "FSUser_lastFSUser";
        int id = _CT_DB.insertDataSet(CTDataSetDef.INSERT_NEW_FSUSER, procCall, username, password, firstName, lastName, email,
                phone, altPhone, address, address2, city, state, zip, country, leadSource, sendActionAlerts,
                new java.sql.Timestamp(new AuDate().getDateInMillis()),authKey);
        
        System.out.println("New user id : " + id);
        CTReturnCode ret = (id>0) ? new CTReturnCode(SUCCESS, id) : RC_DB_ERROR;

        if (ret.isSuccess()) {
//            for (int msgID : NEW_USER_MESSAGES) {
//                int rows = _CT_DB.updateDataSet(CTDataSetDef.STATUS_ALL, msgID, id);
//                if (rows==0) {
//                    ret = new CTReturnCode(CTReturnType.SUCCESS_WITH_WARNINGS, id,
//                            Arrays.asList(new String[] { "The user was successfully added, but there was an error sending the New User message. Please notify a developer."}));
//                }
//            }
        }

        return id;
    }

    public List<FSTeam> getTeams(int sportYear) {
        setFSTeams(new ArrayList<FSTeam>());
        CachedRowSet crs = null;
        Connection con = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT").append(_Cols.getColumnList("FSTeam", "t.", ""));
            sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
            sql.append(",").append(_Cols.getColumnList("FSSeason", "fss.", "FSSeason$"));
            sql.append(",").append(_Cols.getColumnList("FSGame", "g.", "FSGame$"));
            sql.append(",").append(_Cols.getColumnList("Season", "s.", "Season$"));
            sql.append(",").append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
            sql.append("FROM FSTeam t ");
            sql.append("JOIN FSLeague l ON l.FSLeagueID = t.FSLeagueID ");
            sql.append("LEFT JOIN FSSeason fss ON fss.FSSeasonID = l.FSSeasonID ");
            sql.append("LEFT JOIN FSGame g ON g.FSGameID = fss.FSGameID ");
            sql.append("LEFT JOIN Season s ON s.SeasonID = fss.SeasonID ");
            sql.append("LEFT JOIN FSUser u ON u.FSUserID = t.FSUserID ");
            sql.append("WHERE t.FSUserID = ").append(getFSUserID()).append(" AND (s.SportYear = ").append(sportYear).append(" OR s.SportYear IS NULL) ");
            sql.append("ORDER BY fss.FSGameID, l.FSLeagueID, t.FSTeamID ");

            con = CTApplication._CT_DB.getConn(false);
            crs = CTApplication._CT_QUICK_DB.executeQuery(con, sql.toString());
            while (crs.next()) {
                getFSTeams().add(new FSTeam(crs));
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
            JDBCDatabase.close(con);
        }
            
        return getFSTeams();
    }   

    public static void main(String[] args) {

        String username = "stealth";
        String password = "lakers";

        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSUser", "u.", ""));
            sql.append(" FROM FSUser u ");
            sql.append(" WHERE UserName='").append(StringEscapeUtils.escapeSql(username)).append("'");
            sql.append(" AND Password='").append(StringEscapeUtils.escapeSql(password)).append("'");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            if (crs.next()) {
                try {
                    FSUser user = new FSUser(crs);
                    System.out.println("FSuserID = " + user.getFSUserID());
                    System.out.println("username = " + user.getUsername());
                    System.out.println("firstname = " + user.getFirstName());

                    // Grab User Teams
                    List<FSTeam> teams = new ArrayList<FSTeam>();
                    sql = new StringBuilder();
                    sql.append(" SELECT ").append(_Cols.getColumnList("FSTeam", "t.", ""));
                    sql.append(",").append(_Cols.getColumnList("FSLeague", "l.", "FSLeague$"));
                    sql.append(",").append(_Cols.getColumnList("FSSeason", "s.", "FSSeason$"));
                    sql.append(",").append(_Cols.getColumnList("FSGame", "g.", "FSGame$"));
                    sql.append(",").append(_Cols.getColumnList("Sport", "sp.", "Sport$"));
                    sql.append(",").append(_Cols.getColumnList("Season", "se.", "Season$"));
                    sql.append(",").append(_Cols.getColumnList("FSUser", "u.", "FSUser$"));
                    sql.append(" FROM FSTeam t ");
                    sql.append(" INNER JOIN FSLeague l ON l.FSLeagueID = t.FSLeagueID ");
                    sql.append(" INNER JOIN FSSeason s ON s.FSSeasonID = l.FSSeasonID ");
                    sql.append(" INNER JOIN FSGame g ON g.FSGameID = s.FSGameID ");
                    sql.append(" INNER JOIN Sport sp ON sp.SportID = g.SportID ");
                    sql.append(" INNER JOIN Season se ON se.SeasonID = s.SeasonID ");
                    sql.append(" INNER JOIN FSUser u ON u.FSUserID = t.FSUserID");
                    sql.append(" WHERE t.FSUserID = ").append(user.getFSUserID());
                    sql.append(" AND t.isActive = 1 ");
                    sql.append(" AND s.DisplayTeams = 1");
                    sql.append(" ORDER BY s.FSSeasonID, l.FSLeagueID, t.FSTeamID ");

                    CachedRowSet crs2 = null;
                    try {
                        crs2 = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
//                        List<Map<String, Object>> data = FSUtils.crsToList(crs2);
                        int count = 0;
                        while (crs2.next()) {
                            count++;
                            FSTeam team = new FSTeam(crs2);
                            teams.add(team);
                        }

                        System.out.println("# of Teams " + teams.size());
                        //CTReturnCode ret = null;
                        //user.updateUserFields("LendingClub", "User", "dannyboy", "test2's email @yahoo.com");

                        //if (ret.isSuccess()) {
                            //System.out.println("successfully updated the record, doncha know.");
                        //}
                    } catch (Exception e) {
                        _CT_LOG.error(e);
                    } finally {
                        JDBCDatabase.closeCRS(crs2);
                    }
                }
                catch (Exception e) {
                    _CT_LOG.error(e);
                }
            } else {
                System.out.println("no user found");
            }

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        System.out.println("life goes on");

    }

    public static boolean usernameExists(String username) {

        boolean exists = false;
        CachedRowSet crs = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ").append(_Cols.getColumnList("FSUser", "u.", ""));
            sql.append(" FROM FSUser u ");
            sql.append(" WHERE u.Username = '").append(username).append("'");

            crs = CTApplication._CT_QUICK_DB.executeQuery(sql.toString());
            
            if (crs.size() > 0) {
                exists = true;
            }
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        } finally {
            JDBCDatabase.closeCRS(crs);
        }

        return exists;
    }
    
    public static String generateAuthenticationKey() {
        
        StringBuilder key = new StringBuilder();
        Random random = new Random();
        for (int x=1;x<=5;x++) {
            key.append(random.nextInt(100));
        }
        
        return key.toString();
    }

    public void clearTeams() {
        setFSTeams(null);
    }

    // PRIVATE METHODS

    /* This method populates the constructed object with all the fields that are part of a queried result set */
    private void initFromCRS(CachedRowSet crs, String prefix) {
        
        try {
            
            // DB FIELDS
            if (FSUtils.fieldExists(crs, prefix, "FSUserID")) {
                setFSUserID(crs.getInt(prefix + "FSUserID"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Username")) {
                setUsername(crs.getString(prefix + "Username"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Password")) {
                setPassword(crs.getString(prefix + "Password"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "DateCreated")) {
                setDateCreated(new AuDate(crs.getTimestamp(prefix + "DateCreated")));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "FirstName")) {
                setFirstName(crs.getString(prefix + "FirstName"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "LastName")) {
                setLastName(crs.getString(prefix + "LastName"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Email")) {
                setEmail(crs.getString(prefix + "Email"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Telephone")) {
                setTelephone(crs.getString(prefix + "Telephone"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "AlternateTelephone")) {
                setAltTelephone(crs.getString(prefix + "AlternateTelephone"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Address")) {
                setAddress(crs.getString(prefix + "Address"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Address2")) {
                setAddress2(crs.getString(prefix + "Address2"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "City")) {
                setCity(crs.getString(prefix + "City"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "State")) {
                setState(crs.getString(prefix + "State"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Zip")) {
                setZip(crs.getString(prefix + "Zip"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Country")) {
                setCountry(crs.getString(prefix + "Country"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "LeadSource")) {
                setLeadSource(crs.getString(prefix + "LeadSource"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "Birthdate")) {
                setBirthdate(new AuDate(crs.getTimestamp(prefix + "Birthdate")));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "LastLogin")) {
                setLastLogin(new AuDate(crs.getTimestamp(prefix + "LastLogin")));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "BadEmailAddress")) {
                setBadEmailAddress(crs.getBoolean(prefix + "BadEmailAddress"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "SendActionAlerts")) {
                setSendActionAlerts(crs.getBoolean(prefix + "SendActionAlerts"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "IsActive")) {
                setIsActive(crs.getBoolean(prefix + "IsActive"));
            }
            
            if (FSUtils.fieldExists(crs, prefix, "AuthenticationKey")) {
                setAuthenticationKey(crs.getString(prefix + "AuthenticationKey"));
            }
            
        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }        
    }
}
