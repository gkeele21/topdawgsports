package tds.tags;

import bglib.util.AuUtil;
import tds.main.bo.FSTeam;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Grant
 * Date: Feb 15, 2006
 * Time: 8:25:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TeamTag extends SimpleTagSupport {

    private FSTeam teamObj;
    private boolean displayRosterLink = true;
    private String cssClass="";
    private int weekID;
    private String rosterLink = "javascript:viewTeamRoster('teamroster.htm?teamid=**teamid**&wkid=**weekid**')";

    public void setTeamObj(FSTeam teamObj) {
        this.teamObj = teamObj;
    }

    public void setDisplayRosterLink(boolean displayRosterLink) {
        this.displayRosterLink = displayRosterLink;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    
    public void setWeekID(int week) {
        this.weekID = week;
    }

    public void doTag() throws JspException, IOException {

        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();
        try {
            String appender = "";
            if (displayRosterLink) {
                // add the link

                rosterLink = AuUtil.replace(rosterLink,"**teamid**",""+teamObj.getFSTeamID());
                rosterLink = AuUtil.replace(rosterLink,"**weekid**",""+weekID);
                sb.append("<a href=\"");
                sb.append(rosterLink);
                sb.append("\"");
                if (cssClass != null && cssClass.length() > 0) {
                    sb.append(" class=\""+cssClass+"\"");
                }
                sb.append(">");
                appender = "</a>";
            }
            sb.append(teamObj.getTeamName());
            sb.append(appender);
        } catch (Exception e) {
            //
        }

        getJspContext().getOut().print(sb);

    }
}
