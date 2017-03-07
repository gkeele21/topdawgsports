/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.tags;

import bglib.util.AuUtil;
import tds.main.bo.Player;
import tds.main.bo.PlayerInjury;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author grant.keele
 */
public class PlayerTag extends SimpleTagSupport {

    private Map data;
    private Player player;
    private boolean displayStatsLink = false;
    private boolean displayInjury = false;
    private DisplayName display = DisplayName.FULL;
    private String elementName = "PlayerID";

    private static String playerLink = "javascript:viewPlayerStats('playerstats.htm?pid=**playerid**')";
    
    public void setData(Map data) {
        this.data = data;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setDisplayStatsLink(boolean displayStatsLink) {
        this.displayStatsLink = displayStatsLink;
    }

    public void setDisplayInjury(boolean displayInjury) {
        this.displayInjury = displayInjury;
    }

    public void setDisplayName(DisplayName dname) {
        this.display = dname;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public void doTag() throws JspException, IOException {

        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();
        try {
            Player p = player;
            if (data != null) {
                Object pid = data.get(elementName);
                p = Player.getInstance(Integer.parseInt(""+pid));
            }

            if (p == null) {
                return;
            }
            String pname = "";
            if (display == DisplayName.FULL) {
                pname = p.getFullName();
            } else if (display == DisplayName.FIRST) {
                pname = p.getFirstName();
            } else if (display == DisplayName.LAST) {
                pname = p.getLastName();
            } else if (display == DisplayName.FIRSTINIT) {
                if (p.getPositionID() == 6) {
                    pname = p.getFullName(); // don't abbreviate DEF first name
                } else {
                    pname = p.getFirstName().substring(0,1) + ". " + p.getLastName();
                }
            } else if (display == DisplayName.LASTINIT) {
                pname = p.getFirstName() + " " + p.getLastName().substring(0,1);
            }  else if (display == DisplayName.LASTFIRST) {
                pname = p.getLastName() + ", " + p.getFirstName();
            }

            String appender = "";
            if (displayStatsLink) {
                // add the link
                String defaultLink = playerLink;

                defaultLink = AuUtil.replace(defaultLink,"**playerid**",""+player.getPlayerID());
                sb.append("<a href=\"");
                sb.append(defaultLink);
                sb.append("\" ");
                // check for available tooltip
                if (data != null) {
                    String toolTip = (String)data.get("PlayerToolTip");
                    if (toolTip!=null) {
                        sb.append(" title=\"");
                        sb.append(toolTip);
                        sb.append("\"");
                    }
                }
                sb.append(">");
                appender = "</a>";
            }
            sb.append(pname);
            sb.append(appender);
            if (displayInjury) {
                // check for an injury

                PlayerInjury injury = player.getPlayerInjury();
                if (injury.getInjury() != null && !injury.getInjury().equals("")) {
                    sb.append(" <span class=\"ir\" >" + injury.getInjuryStatus() + "</span>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getJspContext().getOut().print(sb);

    }
}