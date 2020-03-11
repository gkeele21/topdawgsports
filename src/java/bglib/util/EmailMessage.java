package bglib.util;

import static bglib.util.Application._GLOBAL_QUICK_DB;
import sun.jdbc.rowset.CachedRowSet;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

public class EmailMessage extends Thread {

    private static final MessageFormat _Formatter = new MessageFormat("");

    private Application _App;
    private String _Subject;
    private String _Body;
    private boolean _UseFooter;
    private List<Object> _Parameters;
    private List<String> _ToList;
    private FileItem _AttachmentFileItem;
    private String _AttachmentFilename;
    private boolean _AsBCC;

    public EmailMessage(Application app, String msgName, List<Object> params, List<String> toList) throws Exception {
        _App=app;
        String sql = "select * from EmailMessage where ApplicationID=" + app.getAppID() + " and EmailMessageName='" + msgName + "'";
        CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(sql);
        initFromCRS(crs);
        _Parameters = new ArrayList<Object>(params);
        _ToList = new ArrayList<String>(toList);
    }

    public EmailMessage(Application app, String msgName, List<Object> params, List<String> toList, boolean asBCC) throws Exception {
        _App=app;
        String sql = "select * from EmailMessage where ApplicationID=" + app.getAppID() + " and EmailMessageName='" + msgName + "'";
        CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(sql);
        initFromCRS(crs);
        _Parameters = new ArrayList<Object>(params);
        _ToList = new ArrayList<String>(toList);
        _AsBCC=asBCC;
    }

    public EmailMessage(Application app, String msgName, List<Object> params, List<String> toList, FileItem attachmentFileItem, boolean asBCC) throws Exception {
        _App=app;
        String sql = "select * from EmailMessage where ApplicationID=" + app.getAppID() + " and EmailMessageName='" + msgName + "'";
        CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(sql);
        initFromCRS(crs);
        _Parameters = new ArrayList<Object>(params);
        _ToList = new ArrayList<String>(toList);
        _AttachmentFileItem =attachmentFileItem;
        _AsBCC=asBCC;
    }

    public EmailMessage(Application app, String msgName, List<Object> params, List<String> toList, String attachmentFilename, boolean asBCC) throws Exception {
        _App=app;
        String sql = "select * from EmailMessage where ApplicationID=" + app.getAppID() + " and EmailMessageName='" + msgName + "'";
        CachedRowSet crs = _GLOBAL_QUICK_DB.executeQuery(sql);
        initFromCRS(crs);
        _Parameters = new ArrayList<Object>(params);
        _ToList = new ArrayList<String>(toList);
        _AttachmentFilename = attachmentFilename;
        _AsBCC=asBCC;
    }

    private void initFromCRS(CachedRowSet crs) throws Exception {
        if (crs.next()) {
            _Subject = crs.getString("Subject");
            _Body = crs.getString("Body");
            _UseFooter = crs.getBoolean("UseFooter");
        }
    }

    public String getSubject() { return formatString(_Subject, _Parameters); }

    public List<String> getToList() { return _ToList; }

    public boolean getAsBCC() { return _AsBCC; }

    public String getBody() {
        String body = formatString(_Body, _Parameters);
        if (_UseFooter) {
            body += "\n" + _App.getEmailFooter();
        }

        return body;
    }

    public Map toMap() {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("toList", getToList());
        values.put("asBCC", getAsBCC());
        values.put("subject", getSubject());
        values.put("body", getBody());
//        values.put("from", _App.getAppSettings().getProperty(AppSettings.EMAIL_FROM_PROP));
//        values.put("fromPersonal", _App.getAppSettings().getProperty(AppSettings.EMAIL_FROMPERSONAL_PROP));
        if (_AttachmentFileItem!=null && _AttachmentFileItem.getSize()>0) {
            values.put("attachmentFileItem", _AttachmentFileItem);
        }
        if (_AttachmentFilename!=null) {
            values.put("attachment", _AttachmentFilename);
        }

        return values;
    }

    public static String formatString(String str, List<Object> args) {
        _Formatter.applyPattern(FSUtils.noNull(str));
        return _Formatter.format(args.toArray());
    }
}
