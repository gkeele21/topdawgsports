package bglib.util;

import static bglib.util.Application._GLOBAL_LOG;
import static bglib.util.Application._GLOBAL_SETTINGS;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.fileupload.FileItem;

public class SendEmail implements Runnable {

   // SMTP Server INFO
    private static final String _host = _GLOBAL_SETTINGS.getProperty(AppSettings.EMAIL_HOST_PROP);
    private static final String _user = _GLOBAL_SETTINGS.getProperty(AppSettings.EMAIL_USER_PROP);
    private static final String _password = _GLOBAL_SETTINGS.getProperty(AppSettings.EMAIL_PASSWORD_PROP);

    // Website INFO
    private static final String _FromEmail = _GLOBAL_SETTINGS.getProperty(AppSettings.EMAIL_FROM_PROP);
    private static final String _CompanyName = _GLOBAL_SETTINGS.getProperty(AppSettings.EMAIL_FROMPERSONAL_PROP);

    private Map _Values = new HashMap();

    private SendEmail(Map hash) {
        _Values = hash;
    }

    public static void sendEmail(Map values) {
        SendEmail se = new SendEmail(values);
        new Thread(se).start();
    }

    public void run() {

        try  {
            Thread.sleep(5);
            unpackValues();
        } catch (InterruptedException e) {

        }
    }

    private void unpackValues() {
        String from = (String)_Values.get("from");
        String fromPersonal = (String)_Values.get("fromPersonal");
        if (from==null || from.length() == 0) {
            from = _FromEmail;
            fromPersonal = _CompanyName;
        }

        String to = (String)_Values.get("to");
        List toList;
        if (to==null || to.length() == 0) {
            toList = (List)_Values.get("toList");
        } else {
            toList = Arrays.asList(to);
        }

        boolean asBCC = false;
        if (_Values.containsKey("asBCC")) {
            asBCC = (Boolean)_Values.get("asBCC");
        }

        String attachFile = (String)_Values.get("attachment");
        FileItem attachFileItem = (FileItem)_Values.get("attachmentFileItem");

        String subject = (String)_Values.get("subject");
        if (subject==null || subject.length() == 0) {
            subject = "BG Dev email message";
        }

        String body = (String)_Values.get("body");

        if (attachFile!=null) {
//            doEmailWithAttachment(from, to, subject, body, attachFile);
            doEmailMultipart(from, fromPersonal, toList, null, subject, body, null, attachFile);
        } else if (attachFileItem!=null) {
            doEmailMultipart(from, fromPersonal, toList, null, subject, body, null, attachFileItem);
        } else {
            doEmail(from, fromPersonal, toList, subject, body, asBCC);
        }
    }

   private static void doEmail(String from, String fromPersonal, List toList, String subject, String body, boolean asBCC) {
        doEmail(from, fromPersonal, toList, null, subject, body, null, asBCC);
    }

    private static void doEmail(String from, String fromPersonal, String to, String subject, String body) {
        List toList = new ArrayList();
        toList.add(to);
        doEmail(from, fromPersonal, toList, null, subject, body, null, false);
    }

    private static void doEmail(String from, String fromPersonal, List toList, List recipientTypeList, String subject, String body, String mime, boolean asBCC) {
        // Define message
        MimeMessage message = createMimeMessage(from, fromPersonal, toList, recipientTypeList, subject, asBCC);
        if (message==null) {
            return;
        }

        try {
            if (mime==null) {
                message.setText(body, "utf8");
            } else {
                if (mime.indexOf("charset")==-1) {
                    mime += "; charset=utf8";
                }
                message.setContent(body, mime);
            }

            // Send message
            message.saveChanges(); // implicit with send()
            Transport.send(message);
//            System.out.println("just sent this message: |" + message.getContent() + "|");
            System.out.println("sent email to: |" + toList.toString() + "|");
        } catch (SendFailedException sf) {
            sf.printStackTrace();
            handleSendFailedException(sf, subject, toList);
        }
        catch (Exception e) {
            e.printStackTrace(); // can't Log.error() here because that results in an infinite loop
        }

    }

    private static MimeMessage createMimeMessage(String from, String fromPersonal, List toList, List recipientTypeList, String subject, boolean asBCC) {
        if (toList.size() == 0) {
            return null;
        }

        MimeMessage message = null;
        
        // Setup mail server
        Properties p = new Properties();
        p.put("mail.smtp.host", _host);
        p.put("mail.smtp.port", "25");
        p.put("mail.smtp.auth", "true");
//            p.put("mail.debug", "true");
        if (_host.equals("smtp.gmail.com")) {
            p.put("mail.smtp.port", "465");
            p.put("mail.smtp.auth", "true");
            p.put("mail.debug", "true");
            p.put("mail.smtp.socketFactory.port", "465");
            p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            p.put("mail.smtp.socketFactory.fallback", "false");
        }
        try {
            
            Authenticator auth = new javax.mail.Authenticator() {
                                                         protected PasswordAuthentication
                                                         getPasswordAuthentication() {
                                                             PasswordAuthentication passAuth = new PasswordAuthentication(_user, _password);
                                                             System.out.println("passAuth");
                                                             return passAuth;
                                                         }
                                                     };
//            Session session = Session.getDefaultInstance(p,auth);
            Session session = Session.getInstance(p, new GMailAuthenticator(_user, _password));
//        session.setDebug(true);

        // Define message
            message = new MimeMessage(session);
        
            message.setFrom(new InternetAddress(from, fromPersonal));

            if (asBCC) {
                recipientTypeList = new ArrayList();
                for (int i=0; i<toList.size(); i++) {
                    recipientTypeList.add(javax.mail.Message.RecipientType.BCC);
                }
            }
            String to = buildToList(message, toList, recipientTypeList, asBCC);
            if (to.length() > 0) {
                subject = "To: " + to + " | " + subject;
            }

            message.setSubject(subject, "utf8");
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return message;
    }

    private static void handleSendFailedException(SendFailedException sf, String subject, List toList) {
        StringBuffer msg = new StringBuffer();
        Address[] invalid = sf.getInvalidAddresses();
        msg.append("An email message failed to send properly.\n\n");
        msg.append("Subject: " + subject + "\n");
        msg.append("ToList: ");
        Iterator to = toList.iterator();
        while (to.hasNext()) {
            msg.append((String)to.next());
            if (to.hasNext()) {
                msg.append(";");
            }
        }
        if (invalid!=null && invalid.length>0) {
            msg.append("\n\nInvalid addresses: ");
            for (int i=0; i<invalid.length; i++) {
                msg.append(invalid[i].toString());
                if (i<(invalid.length-1)) {
                    msg.append(";");
                }
            }
        }
        sf.printStackTrace();
        _GLOBAL_LOG.logMessage(Level.OFF, msg.toString()); // must turn Level Off to avoid infinite loop of emails
    }

    private static String buildToList(MimeMessage message, List toList, List recipientTypeList, boolean asBCC) {
        String ret = "";
        String sendEmailsTo = _GLOBAL_SETTINGS.getProperty(AppSettings.EMAIL_ONLY_TO_PROP, "");
        for (int i=0; i<toList.size(); i++) {
            try {
                String recipient = fixEmails((String)toList.get(i));
                if (sendEmailsTo.length() == 0) {
                    String[] recipients = recipient.split(",");
                    for (String rec : recipients) {
                        if (recipientTypeList==null) {
                            addRecipient(message, rec);
                        } else {
                            message.addRecipient((javax.mail.Message.RecipientType)recipientTypeList.get(i), new InternetAddress(rec));
                        }
                    }
                } else {
                    addRecipient(message, sendEmailsTo);
                    ret += recipient + "; ";
                }
            }
            catch (Exception e) {
                System.out.println("bad To email address: " + toList.get(i)); // log and continue; we don't want a bad address to derail the process
            }
        }

        return ret;
    }

    private static String fixEmails(String email) {
       email = email.replace(' ','_');
       email = email.replace(':','.');
       email = email.replace(';','.');

       return email;
   }

    private static void addRecipient(MimeMessage message,String email) {

        try {
            if (email.length()>0) {
                message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(email));
            }
        } catch (Exception e) {
            _GLOBAL_LOG.error(new SendEmailException(e));
        }
    }

    private void doEmailWithAttachment(String from, String to, String subject, String bodytext, String attachment) {
/*        try {
            com.jscape.inet.email.EmailMessage msg = new EmailMessage(to, from, subject, bodytext);
            File attachFile = new File(attachment);
            if (attachFile.exists()) {
                msg.addAttachment(new Attachment(new File(attachment)));
            }
            Smtp smtp = new Smtp(_host);
            smtp.connect();
            smtp.login(_user, _password);
            smtp.send(msg);
            smtp.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void doEmailMultipart(String from, String fromPersonal, List toList, List recipientTypeList, String subject, String bodytext, String bodyhtml, String attachment) {
        // Define message
        MimeMessage message = createMimeMessage(from, fromPersonal, toList, recipientTypeList, subject, false);
        if (message==null) {
            return;
        }

        try {
            Multipart multipart = new MimeMultipart();

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(bodytext, "text/plain; charset=utf8");
            multipart.addBodyPart(messageBodyPart);

            if (bodyhtml != null && !bodyhtml.equals("")) {
                // Create second body part
                messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(bodyhtml,"text/html; charset=utf8");

                multipart.addBodyPart(messageBodyPart);
            }

            if (attachment!=null) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(attachment.substring(attachment.lastIndexOf('\\')+1));
                attachmentBodyPart.setDisposition(Part.ATTACHMENT);
                multipart.addBodyPart(attachmentBodyPart);
            }

            // Put parts in message
            message.setContent(multipart);

            // Send message
            message.saveChanges(); // implicit with send()
            Transport.send(message);
            System.out.println("sent email to: |" + toList.toString() + "| with attachment " + attachment);
        } catch (SendFailedException sf) {
            handleSendFailedException(sf, subject, toList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doEmailMultipart(String from, String fromPersonal, List toList, List recipientTypeList, String subject, String bodytext, String bodyhtml, FileItem attachmentFileItem) {
        // Define message
        MimeMessage message = createMimeMessage(from, fromPersonal, toList, recipientTypeList, subject, false);
        if (message==null) {
            return;
        }

        try {
            Multipart multipart = new MimeMultipart();

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(bodytext, "text/plain; charset=utf8");
            multipart.addBodyPart(messageBodyPart);

            if (bodyhtml != null && !bodyhtml.equals("")) {
                // Create second body part
                messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(bodyhtml,"text/html; charset=utf8");

                multipart.addBodyPart(messageBodyPart);
            }

            if (attachmentFileItem!=null) {
//                MimeBodyPart attachmentBodyPart = new MimeBodyPart(new InternetHeaders(), attachmentFileItem.get());
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(attachmentFileItem.get(), "application/octet-stream");
//                attachmentBodyPart.setContent();
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(attachmentFileItem.getName().substring(attachmentFileItem.getName().lastIndexOf('\\')+1));
                attachmentBodyPart.setDisposition(Part.ATTACHMENT);
                multipart.addBodyPart(attachmentBodyPart);
            }

            // Put parts in message
            message.setContent(multipart);

            // Send message
            message.saveChanges(); // implicit with send()
            Transport.send(message);
            System.out.println("sent email to: |" + toList.toString() + "| with uploaded attachment " + attachmentFileItem.getName());
        } catch (SendFailedException sf) {
            handleSendFailedException(sf, subject, toList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Folder getFolder(Store store,Map map) {

        Folder folder = null;
        try {
            // Properties
            String host = map.get("host").toString();
            String username = map.get("username").toString();
            String password = map.get("password").toString();

            if (host == null || username == null || password == null) {
                System.out.println("Host,Username,or Password not supplied.");
                return null;
            }

            System.out.println("Map : " + map.toString());
            // Connect to store
            store.connect(host, username, password);
            // Get folder
            folder = store.getFolder("INBOX");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return folder;
    }

    public static Store getStore(Map map,String hosttype) {

        Store store = null;
        try {
            // Create empty properties
            //Properties props = new Properties();
            // Get system properties
            Properties props = System.getProperties();

            // Get session
//            Session session = Session.getDefaultInstance(props, null);

            // Setup mail server
            String host = "";
            String user = map.get("username").toString();
            String pass = map.get("password").toString();

            if (hosttype != null && hosttype.equals("receive")) {
                host = map.get("host").toString();
            } else {
                host = map.get("smtphost").toString();
            }
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user",user);
            props.put("mail.smtp.password",pass);
            props.put("mail.smtp.auth", "true");

            //Authenticator auth = new MyAuthenticator();
            // create the authenticator
              Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(_user,_password);
                }
              };

            // Get session
            Session session = Session.getDefaultInstance(props, auth);

            // Get the store
            store = session.getStore("pop3");

        } catch (javax.mail.NoSuchProviderException e) {
            e.printStackTrace();
        }

        return store;
    }

    public static Map getGrantEmailMap(String server) throws Exception {

        Map map = new HashMap();

        if (server.equals("bullysports")) {
            map.put("host","10.10.9.21");
            //map.put("host","ex1");
            map.put("username","gkeele@flagship.com");
            map.put("password","laker.1");
        } else if (server.equals("byu")) {
            map.put("host","mail.byu.edu");
            map.put("smtphost","mail.byu.edu");
            map.put("username","gk");
            map.put("password","lakers55");
        } else if (server.equals("gmail")) {
            map.put("host","pop.gmail.com");
            map.put("smtphost","smtp.gmail.com");
            map.put("username","grantkeele@gmail.com");
            map.put("password","laker.1");
        }
        return map;

    }

    public static void closeBox(Store store,Folder folder) throws Exception {
        try {
            if (folder.isOpen()) {
                folder.close(true);
            }
            if (store.isConnected()) {
                store.close();
            }
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public static javax.mail.Message[] getEmailBox(Store store,Folder folder) throws Exception {

        try {
            // Open read-only
            folder.open(Folder.READ_WRITE);

            // Get directory
            javax.mail.Message message[] = folder.getMessages();

            int totalMessages = folder.getMessageCount();
            if (totalMessages == 0)	{
                System.out.println(folder + " is empty");
                folder.close(false);
                store.close();
                return null;
            }

            return message;

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public static boolean printMessages(javax.mail.Message[] message,Map map,AuDate playdate) throws Exception {

        boolean statsdownloaded = false;
        if (message == null) {
            return statsdownloaded;
        }

        for (int i=0, n=message.length; i<n; i++) {

           // Display from field and subject
            String from = message[i].getFrom()[0].toString();
            String subject = message[i].getSubject();

            //System.out.println("From : " + from);
            System.out.println(i + ": " + from + "\t" + subject);

            // Display message content
            Object content = message[i].getContent();

        }

        return statsdownloaded;

    }

    public static boolean saveMessage(javax.mail.Message message,String key,Map map,boolean deleteafter) throws Exception {

        boolean ok = false;
        String statsdir = map.get("statsdir").toString();

        if (map.containsKey(key)) {
            String file = map.get(key).toString();
            ok = saveMessageToFile(message,statsdir + file,deleteafter);
        }

        return ok;
    }

    public static boolean saveMessageToFile(javax.mail.Message message,String file,boolean deleteafter) throws Exception {

        boolean ok = false;

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file,false)));
        out.println(message.getContent());
        out.close();

        ok = true;

        if (deleteafter) {
            message.setFlag(Flags.Flag.DELETED,true);
        }

        return ok;
    }

}
/**
* Esta clase implementa un DataSource para un byte[]
* @author elveru
*/
class ByteArrayDataSource implements DataSource {
private byte[] data; // data
private String type; // content-type


public ByteArrayDataSource(byte[] data, String type) {
this.data = data;
this.type = type;
}

public InputStream getInputStream() throws IOException {
if (data == null)
throw new IOException("No hay datos");
return new ByteArrayInputStream(data);
}

public OutputStream getOutputStream() throws IOException {
throw new IOException("Esto no se puede");
}

public String getContentType() {
return type;
}

public String getName() {
return "elveru";
}
}

