package tds.main.bo;

import bglib.util.AuUtil;
import tds.main.control.Common;

public class URLBase {

    // VARIABLES
    private String _URI = "";
    private String _Package = "";
    private String _PageName = "";
    private String _HTMLPage = "";
    private String _HTMLPageWithExtension = "";

    // CONSTRUCTORS
    public URLBase() {
    }

    // GETTERS
    public String getURI() {return _URI;}
    public String getPackage() {return _Package;}
    public String getPageName() {return _PageName;}
    public String getHTMLPage() {return _HTMLPage;}
    public String getHTMLPageWithExtension() {return _HTMLPageWithExtension;}

    // SETTERS
    public void setURI(String URI) {this._URI = URI;}
    public void setPackage(String Package) {this._Package = Package;}
    public void setPageName(String PageName) {this._PageName = PageName;}
    public void setHTMLPage(String HTMLPage) {this._HTMLPage = HTMLPage;}
    public void setHTMLPageWithExtension(String HTMLPageWithExtension) {this._HTMLPageWithExtension = HTMLPageWithExtension;}

    // PUBLIC METHODS
    public void populate(String uri) {
        System.out.println("Here in URLBase.populate with uri '" + uri + "'");
        this.setURI(uri);
        if (uri == null) {
            return;
        }

        // Find package
        String pckg = "";
        int pkindex = uri.indexOf("/",2);
        if (pkindex > 0) {
            int last = uri.lastIndexOf("/");
            if (last > pkindex) {
                pckg = uri.substring(pkindex+1,last+1);
                pckg = AuUtil.replace(pckg,"/",".");
            }
        }
        System.out.println("Package : " + pckg);
        this.setPackage(pckg);

        // Find pagename
        int pgindex = uri.lastIndexOf("/");
        if (pgindex >= 0) {
            uri = uri.substring(pgindex+1);
        }

        this.setPageName(uri);
        this.setHTMLPage(uri);

        // Find extension
        int dotindex = uri.lastIndexOf(".");
        if (dotindex <= 0) {
            return;
        }

        try {
            this.setPageName(uri.substring(0,dotindex));
            this.setHTMLPage(this.getPageName() + Common.jspExtension);
//            System.out.println("In here.");

        } catch (Exception e) {
            CTApplication._CT_LOG.error(e);
        }
    }
}
