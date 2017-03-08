package tds.main.bo;

import bglib.util.Application;
import bglib.util.EmailEvent;

public enum CTEmailEvent implements EmailEvent { CONTACT_US("ContactUs"), FORGOT_PASSWORD_USERNAME("ForgotPasswordUsername"),
    FORGOT_PASSWORD_EMAIL("ForgotPasswordEmail"), NEW_EMPLOYEE("NewEmployee"), TERMINATED_EMPLOYEE("TerminatedEmployee"),
    OFFICE_INFO_CHANGED("OfficeInfoChanged"), STATUS_CHANGED("StatusChanged"), GENERAL_EMAIL("GeneralEmail");

    String _EmailMessageName;

    CTEmailEvent(String name) { _EmailMessageName = name; }

    public Application getApplication() { return CTApplication._CT_APP; }

    public String getEmailMessageName() { return _EmailMessageName; }
}
