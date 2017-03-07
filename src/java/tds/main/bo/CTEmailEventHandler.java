package tds.main.bo;

import bglib.util.AppSettings;
import bglib.util.EmailEvent;
import bglib.util.EmailMessage;
import bglib.util.SendEmail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import static tds.main.bo.CTApplication._CT_APP_SETTINGS;
import static tds.main.bo.CTApplication._CT_LOG;

public class CTEmailEventHandler implements Runnable, Serializable {

    public static final String ANNETTE_EMAIL = "alowder@flagshipfinancialgroup.com";
    public static final String RACHEL_EMAIL = "rcottle@flagshipfinancialgroup.com";
    public static final String SARAH_EMAIL = "sturner@flagshipfinancialgroup.com";
    public static final String LIZ_EMAIL = "lharris@flagshipfinancialgroup.com";

    public static final String PROP_EMAIL_OFFICE_CHANGED = "Email.OfficeChanged.Recipient";
    public static final String PROP_EMAIL_STATUS_CHANGED = "Email.StatusChanged.Recipient";

    private EmailEvent _Event;
    private List<Object> _Args;
    private List<String> _ToList;
    private FileItem _AttachmentFileItem;
    private boolean _AsBCC;
    private String _ReplyTo;

    private CTEmailEventHandler(EmailEvent event, List<String> toList, List<Object> args, FileItem attachment) {
        _Event = event;
        _Args = new ArrayList<Object>(args);
        _AttachmentFileItem =attachment;
        if (toList==null) {
            toList = new ArrayList<String>();
        }
        _ToList = new ArrayList<String>(toList);
        if (_ToList.size()==0) {
            _ToList.add(_CT_APP_SETTINGS.getProperty(AppSettings.EMAIL_DEFAULT_RECIPIENT, "bruhansen@gmail.com"));
        }
    }

    private CTEmailEventHandler(EmailEvent event, List<String> toList, List<Object> args, FileItem attachment, boolean asBCC, String replyTo) {
        _Event = event;
        _Args = new ArrayList<Object>(args);
        _AttachmentFileItem =attachment;
        _ReplyTo=replyTo;
        if (toList==null) {
            toList = new ArrayList<String>();
        }
        _ToList = new ArrayList<String>(toList);
        if (_ToList.size()==0) {
            _ToList.add(_CT_APP_SETTINGS.getProperty(AppSettings.EMAIL_DEFAULT_RECIPIENT, "bruhansen@gmail.com"));
        }
        _AsBCC=asBCC;
    }

    public void run() {
        try {
            EmailMessage em = new EmailMessage(_Event.getApplication(), _Event.getEmailMessageName(), _Args, _ToList, _AttachmentFileItem,
                    _AsBCC);
            SendEmail.sendEmail(em.toMap());
        } catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    public static void fire(EmailEvent event, List<String> toList, List<Object> args, FileItem attachment) {
        try {
            Thread thread = new Thread(new CTEmailEventHandler(event, toList, args, attachment));
            thread.start();
        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    public static void fireAsBCC(EmailEvent event, List<String> toList, List<Object> args, FileItem attachment, String replyTo) {
        try {
            Thread thread = new Thread(new CTEmailEventHandler(event, toList, args, attachment, true, replyTo));
            thread.start();
        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    public static void fire(EmailEvent event, List<String> toList, List<Object> args) {
        try {
            Thread thread = new Thread(new CTEmailEventHandler(event, toList, args, null));
            thread.start();
        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

    public static void fireAsBCC(EmailEvent event, List<String> toList, List<Object> args) {
        try {
            Thread thread = new Thread(new CTEmailEventHandler(event, toList, args, null, true, null));
            thread.start();
        }
        catch (Exception e) {
            _CT_LOG.error(e);
        }
    }

}


