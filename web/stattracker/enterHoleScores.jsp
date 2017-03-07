<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - Stat Tracker Enter Hole Scores</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <style type="text/css">
        #statTracker table {
            width: 40%;
        }
    </style>
  </head>

  <body>
    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div>

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div>

        <div id="content">
            <div id="innerContent">

                <div id="statTracker">

                    <a href="tournamentResults.htm">Tournament Results</a><br />
                    <c:if test="${currentHoleNumber > 1}">
                        <a href="enterHoleScores.htm?currentHoleNumber=${currentHoleNumber-1}">
                            <img src="../images/leftArrow.png" height="30"/>&#160;&#160;
                        </a>
                    </c:if>
                    <span class="bold" style="font-size: 30px;">Hole # ${currentHoleNumber}</span>
                    <c:if test="${currentHoleNumber < numHoles}">
                        <a href="enterHoleScores.htm?currentHoleNumber=${currentHoleNumber+1}">
                            &#160;&#160;<img src="../images/rightArrow.png" height="30"/>
                        </a>
                    </c:if>
                    <br />
                    <br />
                    <hr />
                    
                    <form action="enterHoleScores.do" method="post">
                        <table>
                            <tr>
                                <td colspan="2"><input type="submit" value="Submit" /></td>
                            </tr>
                            <tds:tableRows items="${golfGroup}" var="golferRound" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowHeader">
                                    <tr>
                                        <td colspan="2" class="bold">GROUP SCORES</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <c:set var="holeScore" value="${tds:getHoleScore(golferRound,currentHole)}" />
                                    <c:set var="holeStrokes" value="" />
                                    <c:if test="${holeScore.strokes != null}">
                                        <c:set var="holeStrokes" value="${holeScore.strokes}" />
                                    </c:if>
                                    <c:if test="${golferRound.FSUserID != validUser.FSUserID}">
                                        <tr>
                                            <td align="right">${golferRound.FSUser.firstName}: &#160;</td>
                                            <td align="left">&#160;<input type="text" id="score_${golferRound.golferRoundID}" name="score_${golferRound.golferRoundID}" size="2" value="${holeStrokes}"/></td>
                                        </tr>
                                    </c:if>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>                        
                        <br /><hr />
                        <table>
                            <tds:tableRows items="${golfGroup}" var="golferRound" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowHeader">
                                    <tr>
                                        <td colspan="2" class="bold">YOUR SCORES</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <c:set var="holeScore" value="${tds:getHoleScore(golferRound,currentHole)}" />
                                    <c:set var="holeStrokes" value="" />
                                    <c:if test="${holeScore.strokes != null}">
                                        <c:set var="holeStrokes" value="${holeScore.strokes}" />
                                    </c:if>
                                    <c:if test="${golferRound.FSUserID == validUser.FSUserID}">
                                        <tr>
                                            <td align="right">${golferRound.FSUser.firstName}: &#160;</td>
                                            <td align="left">&#160;<input type="text" id="score_${golferRound.golferRoundID}" name="score_${golferRound.golferRoundID}" size="2" value="${holeStrokes}"/></td>
                                        </tr>
                                        <tr>
                                            <td align="right" valign="top">GIR: &#160;</td>
                                            <c:set var="naChecked" value="checked" />
                                            <c:if test="${holeScore.GIR == 1}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="yesChecked" value="checked" />
                                            </c:if>
                                            <c:if test="${holeScore.GIR == 0}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="noChecked" value="checked" />
                                            </c:if>
                                            <td align="left">
                                                <input type="radio" id="GIR_${golferRound.golferRoundID}" name="GIR_${golferRound.golferRoundID}" value="" ${naChecked} /> N/A<br />
                                                <input type="radio" id="GIR_${golferRound.golferRoundID}" name="GIR_${golferRound.golferRoundID}" value="1" ${yesChecked} /> yes<br />
                                                <input type="radio" id="GIR_${golferRound.golferRoundID}" name="GIR_${golferRound.golferRoundID}" value="0" ${noChecked} /> no
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right"># Putts: &#160;</td>
                                            <c:set var="puttsValue" value="" />
                                            <c:if test="${holeScore.putts != null}">
                                                <c:set var="puttsValue" value="${holeScore.putts}" />
                                            </c:if>
                                            <td align="left">
                                                <input type="text" id="putts_${golferRound.golferRoundID}" name="putts_${golferRound.golferRoundID}" size="2" value="${puttsValue}"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right">Final Putt Dist: &#160;</td>
                                            <c:set var="distValue" value="" />
                                            <c:if test="${holeScore.finalPuttDistance != null}">
                                                <c:set var="distValue" value="${holeScore.finalPuttDistance}" />
                                            </c:if>
                                            <td align="left">
                                                <input type="text" id="puttDistance_${golferRound.golferRoundID}" name="puttDistance_${golferRound.golferRoundID}" size="2" value="${distValue}"/>&#160; (in inches)
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right">Penalty Strokes: &#160;</td>
                                            <c:set var="penaltyValue" value="" />
                                            <c:if test="${holeScore.penaltyStrokes != null}">
                                                <c:set var="penaltyValue" value="${holeScore.penaltyStrokes}" />
                                            </c:if>
                                            <td align="left">
                                                <input type="text" id="penaltyStrokes_${golferRound.golferRoundID}" name="penaltyStrokes_${golferRound.golferRoundID}" size="2" value="${penaltyValue}"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right" valign="top">Hit Fairway: &#160;</td>
                                            <c:set var="naChecked" value="checked" />
                                            <c:if test="${holeScore.hitFairway == 1}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="yesChecked" value="checked" />
                                            </c:if>
                                            <c:if test="${holeScore.hitFairway == 0}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="noChecked" value="checked" />
                                            </c:if>
                                            <td align="left">
                                                <input type="radio" id="fairway_${golferRound.golferRoundID}" name="fairway_${golferRound.golferRoundID}" value="" ${naChecked} />N/A<br />
                                                <input type="radio" id="fairway_${golferRound.golferRoundID}" name="fairway_${golferRound.golferRoundID}" value="1" ${yesChecked} />yes<br />
                                                <input type="radio" id="fairway_${golferRound.golferRoundID}" name="fairway_${golferRound.golferRoundID}" value="0" ${noChecked} />no<br />
                                                
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right" valign="top">Sand Save Att: &#160;</td>
                                            <c:set var="naChecked" value="checked" />
                                            <c:if test="${holeScore.sandSaveOpp == 1}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="yesChecked" value="checked" />
                                            </c:if>
                                            <c:if test="${holeScore.sandSaveOpp == 0}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="noChecked" value="checked" />
                                            </c:if>
                                            <td align="left">
                                                <input type="radio" id="sandSaveOpp_${golferRound.golferRoundID}" name="sandSaveOpp_${golferRound.golferRoundID}" value="" ${naChecked} />N/A<br />
                                                <input type="radio" id="sandSaveOpp_${golferRound.golferRoundID}" name="sandSaveOpp_${golferRound.golferRoundID}" value="1" ${yesChecked}/>yes<br />
                                                <input type="radio" id="sandSaveOpp_${golferRound.golferRoundID}" name="sandSaveOpp_${golferRound.golferRoundID}" value="0" ${noChecked}/>no<br />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right" valign="top">Sand Save Made: &#160;</td>
                                            <c:set var="naChecked" value="checked" />
                                            <c:if test="${holeScore.sandSaveComp == 1}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="yesChecked" value="checked" />
                                            </c:if>
                                            <c:if test="${holeScore.sandSaveComp == 0}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="noChecked" value="checked" />
                                            </c:if>
                                            <td align="left">
                                                <input type="radio" id="sandSaveComp_${golferRound.golferRoundID}" name="sandSaveComp_${golferRound.golferRoundID}" value="" ${naChecked} />N/A<br />
                                                <input type="radio" id="sandSaveComp_${golferRound.golferRoundID}" name="sandSaveComp_${golferRound.golferRoundID}" value="1" ${yesChecked} />yes<br />
                                                <input type="radio" id="sandSaveComp_${golferRound.golferRoundID}" name="sandSaveComp_${golferRound.golferRoundID}" value="0" ${noChecked} />no<br />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right" valign="top">Up & Down Att: &#160;</td>
                                            <c:set var="naChecked" value="checked" />
                                            <c:if test="${holeScore.upDownOpp == 1}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="yesChecked" value="checked" />
                                            </c:if>
                                            <c:if test="${holeScore.upDownOpp == 0}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="noChecked" value="checked" />
                                            </c:if>
                                            <td align="left">
                                                <input type="radio" id="upDownOpp_${golferRound.golferRoundID}" name="upDownOpp_${golferRound.golferRoundID}" value="" ${naChecked} />N/A<br />
                                                <input type="radio" id="upDownOpp_${golferRound.golferRoundID}" name="upDownOpp_${golferRound.golferRoundID}" value="1" ${yesChecked} />yes<br />
                                                <input type="radio" id="upDownOpp_${golferRound.golferRoundID}" name="upDownOpp_${golferRound.golferRoundID}" value="0" ${noChecked} />no<br />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right" valign="top">Up & Down Made:</td>
                                            <c:set var="naChecked" value="checked" />
                                            <c:if test="${holeScore.upDownComp == 1}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="yesChecked" value="checked" />
                                            </c:if>
                                            <c:if test="${holeScore.upDownComp == 0}">
                                                <c:set var="naChecked" value="" />
                                                <c:set var="noChecked" value="checked" />
                                            </c:if>
                                            <td align="left">
                                                <input type="radio" id="upDownComp_${golferRound.golferRoundID}" name="upDownComp_${golferRound.golferRoundID}" value="" ${naChecked} />N/A<br />
                                                <input type="radio" id="upDownComp_${golferRound.golferRoundID}" name="upDownComp_${golferRound.golferRoundID}" value="1" ${yesChecked} />yes<br />
                                                <input type="radio" id="upDownComp_${golferRound.golferRoundID}" name="upDownComp_${golferRound.golferRoundID}" value="0" ${noChecked} />no<br />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right">Notes: &#160;</td>
                                            <c:set var="notesValue" value="" />
                                            <c:if test="${holeScore.notes != null}">
                                                <c:set var="notesValue" value="${holeScore.notes}" />
                                            </c:if>
                                            <td align="left">
                                                <input type="text" id="notes_${golferRound.golferRoundID}" name="notes_${golferRound.golferRoundID}" value="${notesValue}" />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">&#160;</td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><input type="submit" value="Submit" /></td>
                                        </tr>
                                    </c:if>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>                        
                        <input type="hidden" id="currentHoleID" name="currentHoleID" value="${currentHoleID}" />
                    </form>
                    
                </div>

            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>