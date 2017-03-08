/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tds.tags;

import bglib.util.AuUtil;
import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author grant.keele
 */
public class BracketTag extends SimpleTagSupport {

    private int bracketHeight = 0;
    private int bracketWidth = 0;
    private String position = "left";
    private int tournamentGameID;

    private String bracketCSSClass = "";

    private String team1CSSClass = "";
    private static String team1Style = "border-bottom-style: solid; border-bottom-width: 100%; border-bottom-color: black;";
    private String team1SeedClass = "";
    private static String team1SeedStyleLeft = "float:left; width: 10%;";
    private static String team1SeedStyleRight = "float:right; width: 10%;";
    private String team1NameClass = "";
    private static String team1NameStyle = "text-align: center; width: 90%;";

    private String scoreCSSClass = "";
    private static String scoreStyleLeft = "border-right-style: solid; border-right-color: black; vertical-align: middle; text-align: center;";
    private static String scoreStyleRight = "border-left-style: solid; border-left-color: black; vertical-align: middle; text-align: center;";
    private String scorePrepender = "&#160;&#160;&#160;&#160;";

    private String team2CSSClass = "";
    private static String team2StyleLeft = "border-bottom-style: solid; border-bottom-width: 100%; border-bottom-color: black; border-right-style: solid; border-right-color: black;";
    private static String team2StyleRight = "border-bottom-style: solid; border-bottom-width: 100%; border-bottom-color: black; border-left-style: solid; border-left-color: black;";
    private String team2SeedClass = "";
    private static String team2SeedStyleLeft = "float:left; width: 10%;";
    private static String team2SeedStyleRight = "float:right; width: 10%;";
    private String team2NameClass = "";
    private static String team2NameStyle = "text-align: center; width: 90%;";

    private int lineHeight = 0;
    
    public void setBracketHeight(int value) {
        this.bracketHeight = value;
        if (value > 0) {
            this.lineHeight = (int)(.7 * value);
        }
    }

    public void setBracketWidth(int value) {
        this.bracketWidth = value;
    }

    public void setPosition(String value) {
        this.position = value;
    }

    public void setTournamentGameID(int value) {
        this.tournamentGameID = value;
    }
    
    public void setBracketCSSClass(String value) {
        this.bracketCSSClass = value;
    }

    public void setTeam1CSSClass(String value) {
        this.team1CSSClass = value;
        this.team2CSSClass = value;
    }

    public void setTeam1SeedClass(String value) {
        this.team1SeedClass = value;
        this.team2SeedClass = value;
    }

    public void setTeam1NameClass(String value) {
        this.team1NameClass = value;
        this.team2NameClass = value;
    }

    public void setScoreCSSClass(String value) {
        this.scoreCSSClass = value;
    }

    public void setScorePrepender(String value) {
        this.scorePrepender = value;
    }

    public void setTeam2CSSClass(String value) {
        this.team2CSSClass = value;
    }

    public void setTeam2SeedClass(String value) {
        this.team2SeedClass = value;
    }

    public void setTeam2NameClass(String value) {
        this.team2NameClass = value;
    }

    @Override
    public void doTag() throws JspException, IOException {

        StringWriter evalResult = new StringWriter();
        StringBuffer sb = evalResult.getBuffer();
        StringBuilder wholeDivBeg = new StringBuilder();
        try {
//            Player p = player;
//            if (data != null) {
//                Object pid = data.get(elementName);
//                p = Player.getInstance(Integer.parseInt(""+pid));
//            }
//
//            if (p == null) {
//                return;
//            }

            // Parent Div Beginning
            wholeDivBeg.append("<div id=\"wholeBracket\"");
            // check for cssClass
            if (!AuUtil.isEmpty(this.bracketCSSClass)) {
                wholeDivBeg.append(" class=\"").append(this.bracketCSSClass).append("\"");
            } else {

                StringBuilder styleString = new StringBuilder();
                if (this.bracketHeight > 0) {
                    styleString.append("height: ").append(this.bracketHeight).append("px;");
                }
                if (this.bracketWidth > 0) {
                    styleString.append("width: ").append(this.bracketWidth).append("px;");
                }

                if (styleString.length() > 0) {
                    wholeDivBeg.append(" style=\"").append(styleString).append("\"");
                }
            }

            wholeDivBeg.append(">");
            sb.append(wholeDivBeg);
/*
            TournamentGame game = new TournamentGame(this.tournamentGameID);

            // Team 1 Div
            StringBuilder team1Div = new StringBuilder();
            team1Div.append("<div id=\"team1Div\"");
            if (!AuUtil.isEmpty(this.team1CSSClass)) {
                team1Div.append(" class=\"").append(this.team1CSSClass).append("\"");
            } else {
                team1Div.append(" style=\"").append(this.team1Style).append("\"");
            }
            team1Div.append(">");

            //    Team 1 Seed
            team1Div.append("<span ");
            if (!AuUtil.isEmpty(this.team1SeedClass)) {
                team1Div.append(" class=\"").append(this.team1SeedClass).append("\"");
            } else {
                team1Div.append(" style=\"");
                if ("left".equals(this.position)) {
                    team1Div.append(this.team1SeedStyleLeft);
                } else if ("right".equals(this.position)) {
                    team1Div.append(this.team1SeedStyleRight);
                }
                team1Div.append(" style=\"");
            }
            team1Div.append(">");
            if (game != null) {
                try {
                    team1Div.append("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            team1Div.append("</span>");

            //    Team 1 Name
            team1Div.append("<span ");
            if (!AuUtil.isEmpty(this.team1NameClass)) {
                team1Div.append(" class=\"").append(this.team1NameClass).append("\"");
            } else {
                team1Div.append(" style=\"").append(this.team1NameStyle).append("\"");
            }
            team1Div.append(">");
            team1Div.append("BYU");
            team1Div.append("</span>");

            team1Div.append("</div>");
            sb.append(team1Div);

            // Score Div
            StringBuilder scoreDiv = new StringBuilder();
            scoreDiv.append("<div id=\"scoreDiv\"");
            if (!AuUtil.isEmpty(this.scoreCSSClass)) {
                scoreDiv.append(" class=\"").append(this.scoreCSSClass).append("\"");
            } else {
                scoreDiv.append(" style=\"");
                if ("left".equals(this.position)) {
                    scoreDiv.append(this.scoreStyleLeft);
                } else if ("right".equals(this.position)) {
                    scoreDiv.append(this.scoreStyleRight);
                }
                scoreDiv.append("line-height: ").append(this.lineHeight).append("px;");
                scoreDiv.append("\"");

            }
            scoreDiv.append(">");
            scoreDiv.append(this.scorePrepender);
            scoreDiv.append("102 - 78");

            scoreDiv.append("</div>");
            sb.append(scoreDiv);

            // Team 2 Div
            StringBuilder team2Div = new StringBuilder();
            team2Div.append("<div id=\"team2Div\"");
            if (!AuUtil.isEmpty(this.team2CSSClass)) {
                team2Div.append(" class=\"").append(this.team2CSSClass).append("\"");
            } else {
                team2Div.append(" style=\"");
                if ("left".equals(this.position)) {
                    team2Div.append(this.team2StyleLeft);
                } else if ("right".equals(this.position)) {
                    team2Div.append(this.team2StyleRight);
                }
                team2Div.append("\"");
            }
            team2Div.append(">");

            //    Team 2 Seed
            team2Div.append("<span ");
            if (!AuUtil.isEmpty(this.team2SeedClass)) {
                team2Div.append(" class=\"").append(this.team2SeedClass).append("\"");
            } else {
                team2Div.append(" style=\"");
                if ("left".equals(this.position)) {
                    team2Div.append(this.team2SeedStyleLeft);
                } else if ("right".equals(this.position)) {
                    team2Div.append(this.team2SeedStyleRight);
                }
                team2Div.append(" style=\"");
            }
            team2Div.append(">");
            team2Div.append("16");
            team2Div.append("</span>");

            //    Team 2 Name
            team2Div.append("<span ");
            if (!AuUtil.isEmpty(this.team2NameClass)) {
                team2Div.append(" class=\"").append(this.team2NameClass).append("\"");
            } else {
                team2Div.append(" style=\"").append(this.team2NameStyle).append("\"");
            }
            team2Div.append(">");
            team2Div.append("George Washington");
            team2Div.append("</span>");

            team2Div.append("</div>");
            sb.append(team2Div);

            // Parent Div Ending
            String wholeDivEnd = "</div>";
            sb.append(wholeDivEnd);
            
//              <div id="wholeBracket" style="height: 70px;">
//                        <div id="team1Div" style="border-bottom-style: solid; border-bottom-width: 100%; border-bottom-color: black;">
//                            <span style="float:left; width: 10%;">1</span>
//                            <span style="text-align: center; width: 90%;">BYU</span>
//                        </div>
//                        <div id="scoreDiv" style="border-right-style: solid; border-right-color: black; vertical-align: middle; text-align: center;line-height: 49px;">
//                            &#160;&#160;&#160;&#160;67 - 57
//                        </div>
//                        <div id="team2Div" style="border-bottom-style: solid; border-bottom-width: 100%; border-bottom-color: black; border-right-style: solid; border-right-color: black;">
//                            <span style="float:left; width: 10%;">16</span>
//                            <span style="text-align: center; width: 90%;">George Washington</span>
//                        </div>
//                    </div>
*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        getJspContext().getOut().print(sb);

    }
}