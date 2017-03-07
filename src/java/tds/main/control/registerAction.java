package tds.main.control;

import bglib.util.ErrorMessage;
import bglib.util.FormError;
import bglib.util.FormField;
import bglib.util.FormFieldType;
import tds.main.bo.FSUser;
import tds.main.bo.UserSession;
import static tds.main.bo.CTApplication._CT_LOG;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

public class registerAction extends BaseAction {

    public static final FormField REG_FIRSTNAME = new FormField(1, "frmFirstName", "First Name", 1, 100);
    public static final FormField REG_LASTNAME = new FormField(2, "frmLastName", "Last Name", 1, 100);
    public static final FormField REG_USERNAME = new FormField(3, "frmUsername", "Username", 1, 50);
    public static final FormField REG_PASSWORD = new FormField(4, "frmPassword", "Password", 1, 30);
    public static final FormField REG_PASSWORD_CONFIRM = new FormField(5, "frmPassword2", "Confirm Password", 1, 30);
    public static final FormField REG_EMAIL = new FormField(6, "frmEmail", "Email Address", 1, 150);
    public static final FormField REG_PHONE = new FormField(7, "frnTelephone", "Phone Number", 0, 20);
    public static final FormField REG_ALT_PHONE = new FormField(8, "frmAltTelephone", "Alt. Phone Number", 0, 20);
    public static final FormField REG_ADDRESS = new FormField(9, "frmAddress", "Address", 0, 255, false);
    public static final FormField REG_CITY = new FormField(10, "frmCity", "City", 1, 150);
    public static final FormField REG_STATE = new FormField(11, "frmState", "State", 1, 1000, FormFieldType.INT);
    public static final FormField REG_ZIP = new FormField(12, "frmZip", "Zip", 1, 20);
    public static final FormField REG_COUNTRY = new FormField(13, "frmCountry", "Country", 0, 150);
    public static final FormField REG_SENDALERTS = new FormField(14, "frmSendAlerts", "Send Me Alerts", 0, 10);

    public static List<FormField> REG_FIELDS = Arrays.asList(
        REG_FIRSTNAME, REG_LASTNAME, REG_USERNAME, REG_PASSWORD, REG_PASSWORD_CONFIRM, REG_EMAIL, REG_SENDALERTS
    );

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String page = super.process(request,response);
        if (page != null) {
            return page;
        }

        UserSession session = UserSession.getUserSession(request, response);

        //page="forward:BranchAdd";
        page = "gameSignup";
        FormError error = new FormError();
        Map<FormField, String> inputs = FormField.getAndValidateFormFields(request, REG_FIELDS, error);
        request.setAttribute("inputs", inputs);

        if (inputs.get(REG_FIRSTNAME).length()==0) {
            error.add(REG_FIRSTNAME, new ErrorMessage("Error: Field 'First Name' is required."));
        }
        if (inputs.get(REG_LASTNAME).length()==0) {
            error.add(REG_LASTNAME, new ErrorMessage("Error: Field 'Last Name' is required."));
        }
        if (inputs.get(REG_USERNAME).length()==0) {
            error.add(REG_USERNAME, new ErrorMessage("Error: Field 'Username' is required."));
        }
        if (inputs.get(REG_EMAIL).length()==0) {
            error.add(REG_EMAIL, new ErrorMessage("Error: Field 'Email' is required."));
        }
        if (inputs.get(REG_PASSWORD).equals(inputs.get(REG_PASSWORD_CONFIRM))==false) {
            error.add(REG_PASSWORD, new ErrorMessage("Error: 'Password' field does not match the 'Confirm Password' field."));
            error.add(REG_PASSWORD_CONFIRM);
        }
        if (inputs.get(REG_EMAIL).length()>0 && FSUser.usernameExists(inputs.get(REG_USERNAME))) {
            error.add(REG_USERNAME, new ErrorMessage("Error: The 'User Name' entered already exists. Please choose another user name."));
        }

        if (error.getErrorCount() > 0) {
            session.setErrorMessage(error);
            return page;
        }

        try {
            int actionAlerts = 0;
            String actions = inputs.get(REG_SENDALERTS);
            if (actions != null && actions.equals("on")) {
                actionAlerts = 1;
            }
            String authenticationKey = FSUser.generateAuthenticationKey();
            
            int fsUserID = FSUser.addNewUser(inputs.get(REG_USERNAME), inputs.get(REG_PASSWORD), inputs.get(REG_FIRSTNAME),
                inputs.get(REG_LASTNAME), inputs.get(REG_EMAIL), "","","","","","","","","","",actionAlerts,authenticationKey);
            if (fsUserID > 0) {
                page = "createTeam";
                
                FSUser user = new FSUser(fsUserID);
                
                // Valid login
                session.getHttpSession().setMaxInactiveInterval(tds.main.control.loginAction.SESSION_TIMEOUT);
                UserSession._UserCache.put(session.getHttpSession().getId(), user);
                session.getHttpSession().setAttribute("validUser", user);

                // set last login to current timestamp
                user.setLastLogin();
//                page = "joinLeague";
            } else {
                session.setErrorMessage("Error : could not create a user for you.  Please contact Grant at grantkeele@gmail.com");
                return page;
            }
        } catch (Exception e) {
            _CT_LOG.error(e);
        }
        
        return page;
    }
}
