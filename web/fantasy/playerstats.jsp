<%@ page errorPage="error.jsp" %>
<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>

<html>
<head>
    <title>TopDawgSports Player Stats :: ${statPlayer.fullName}</title>
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
</head>

<body>
    <div id="playerStats">
        <div id="innerPlayerStats">
            
            <c:choose>
                <c:when test="${statPlayer.position.positionName == 'PK'}">
                    <!-- Stats K -->
                    <table>
                        <tds:tableRows items="${playerStats}" var="stats" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowTitle">
                                <tr class="rowTitle">
                                    <td colspan="11"><h2>${statPlayer.fullName}</h2></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                  <td>Week</td>
                                  <td>XPM</td>
                                  <td>XPA</td>
                                  <td>FGM</td>
                                  <td>FGA</td>
                                  <td>0-29</td>
                                  <td>30-39</td>
                                  <td>40-49</td>
                                  <td>50+</td>
                                  <td>Blocked</td>
                                  <td>TD</td>
                                  <td>FP</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <c:if test="${stats.seasonWeek.weekNo >= 1}">
                                    <tr ${highlightRow1} class="rowData">
                                        <td>${stats.seasonWeek.weekNo}</td>
                                        <td><fmt:formatNumber value="${stats.XPM}" /><tds:addRowTotal name="xpm" value="${stats.XPM}" /></td>
                                        <td><fmt:formatNumber value="${stats.XPA}" /><tds:addRowTotal name="xpa" value="${stats.XPA}" /></td>
                                        <td><fmt:formatNumber value="${stats.FGM}" /><tds:addRowTotal name="fga" value="${stats.FGM}" /></td>
                                        <td><fmt:formatNumber value="${stats.FGA}" /><tds:addRowTotal name="fga" value="${stats.FGA}" /></td>
                                        <td><fmt:formatNumber value="${stats.FG29Minus}" /><tds:addRowTotal name="fg29" value="${stats.FG29Minus}" /></td>
                                        <td><fmt:formatNumber value="${stats.FG30to39}" /><tds:addRowTotal name="fg30" value="${stats.FG30to39}" /></td>
                                        <td><fmt:formatNumber value="${stats.FG40to49}" /><tds:addRowTotal name="fg40" value="${stats.FG40to49}" /></td>
                                        <td><fmt:formatNumber value="${stats.FG50Plus}" /><tds:addRowTotal name="fg50" value="${stats.FG50Plus}" /></td>
                                        <td><fmt:formatNumber value="${stats.FGBlocked}" /><tds:addRowTotal name="fgBlocked" value="${stats.FGBlocked}" /></td>
                                        <td><fmt:formatNumber value="${stats.xtraTD}" /><tds:addRowTotal name="xtraTD" value="${stats.xtraTD}" /></td>
                                        <td><fmt:formatNumber value="${stats.fantasyPts}" minFractionDigits="2" /><tds:addRowTotal name="fantasyPts" value="${stats.fantasyPts}" /></td>
                                    </tr>
                                </c:if>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="11">No Games Played.</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowTotal">
                            <tr class="rowHeader">
                                <td>Total</td>
                                <td><fmt:formatNumber maxFractionDigits="0" value="${xpm.total}"/></td>
                                <td><fmt:formatNumber maxFractionDigits="0" value="${xpa.total}"/></td>
                                <td><fmt:formatNumber maxFractionDigits="0" value="${fgm.total}"/></td>
                                <td><fmt:formatNumber maxFractionDigits="0" value="${fga.total}"/></td>
                                <td><fmt:formatNumber maxFractionDigits="0" value="${fg29.total}"/></td>
                                <td><fmt:formatNumber maxFractionDigits="0" value="${fg30.total}"/></td>
                                <td><fmt:formatNumber maxFractionDigits="0" value="${fg40.total}"/></td>
                                <td><fmt:formatNumber maxFractionDigits="0" value="${fg50.total}"/></td>
                                <td><fmt:formatNumber maxFractionDigits="0" value="${fgBlocked.total}"/></td>
                                <td><fmt:formatNumber maxFractionDigits="0" value="${xtraTD.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="2" value="${fantasyPts.total}"/></td>
                            </tr>
                            <%--
                            <tr>
                                <td>Avg</td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${xpm.total / xpm.count}"/></td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${xpa.total / xpa.count}"/></td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${fgm.total / fgm.count}"/></td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${fga.total / fga.count}"/></td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${fg29.total / fg29.count}"/></td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${fg30.total / fg30.count}"/></td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${fg40.total / fg40.count}"/></td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${fg50.total / fg50.count}"/></td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${fgBlocked.total / fgBlocked.count}"/></td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${xtraTD.total / xtraTD.count}"/></td>
                                <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${fantasyPts.total / fantasyPts.count}"/></td>
                            </tr>
                            --%>
                        </jsp:attribute>
                        </tds:tableRows>
                    </table>
                </c:when>
                <c:otherwise>

                    <!-- Stats for QB, RB, WR, TE -->
                    <table>
                        <tds:tableRows items="${playerStats}" var="stats" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowTitle">
                                <tr class="rowTitle">
                                    <td colspan="18"><h2>${statPlayer.fullName}</h2></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                  <td></td>
                                  <td colspan="6">Passing</td>
                                  <td colspan="4">Rushing</td>
                                  <td colspan="4">Receiving</td>
                                  <td>Fum</td>
                                  <td>Other</td>
                                  <td>Total</td>
                                </tr>
                                <tr class="rowHeader">
                                  <td>Week</td>
                                  <td>Comp</td>
                                  <td>Att</td>
                                  <td>Yards</td>
                                  <td>Int</td>
                                  <td>TD</td>
                                  <td>2PT</td>
                                  <td>Att</td>
                                  <td>Yards</td>
                                  <td>TD</td>
                                  <td>2PT</td>
                                  <td>Rec</td>
                                  <td>Yards</td>
                                  <td>TD</td>
                                  <td>2PT</td>
                                  <td>Lost</td>
                                  <td>TD</td>
                                  <td>FP</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <c:if test="${stats.seasonWeek.weekNo >= 1}">
                                    <tr ${highlightRow1} class="rowData">
                                      <td>${stats.seasonWeek.weekNo}</td>
                                      <td><fmt:formatNumber value="${stats.passComp}" /><tds:addRowTotal name="comp" value="${stats.passComp}" /></td>
                                      <td><fmt:formatNumber value="${stats.passAtt}" /><tds:addRowTotal name="att" value="${stats.passAtt}" /></td>
                                      <td><fmt:formatNumber value="${stats.passYards}" /><tds:addRowTotal name="passYards" value="${stats.passYards}" /></td>
                                      <td><fmt:formatNumber value="${stats.passInt}" /><tds:addRowTotal name="interceptions" value="${stats.passInt}" /></td>
                                      <td><fmt:formatNumber value="${stats.passTD}" /><tds:addRowTotal name="passTD" value="${stats.passTD}" /></td>
                                      <td><fmt:formatNumber value="${stats.passTwoPt}" /><tds:addRowTotal name="pass2Pt" value="${stats.passTwoPt}" /></td>
                                      <td><fmt:formatNumber value="${stats.rushAtt}" /><tds:addRowTotal name="rush" value="${stats.rushAtt}" /></td>
                                      <td><fmt:formatNumber value="${stats.rushYards}" /><tds:addRowTotal name="rushYards" value="${stats.rushYards}" /></td>
                                      <td><fmt:formatNumber value="${stats.rushTD}" /><tds:addRowTotal name="rushTD" value="${stats.rushTD}" /></td>
                                      <td><fmt:formatNumber value="${stats.rushTwoPt}" /><tds:addRowTotal name="rush2Pt" value="${stats.rushTwoPt}" /></td>
                                      <td><fmt:formatNumber value="${stats.recCatches}" /><tds:addRowTotal name="rec" value="${stats.recCatches}" /></td>
                                      <td><fmt:formatNumber value="${stats.recYards}" /><tds:addRowTotal name="recYards" value="${stats.recYards}" /></td>
                                      <td><fmt:formatNumber value="${stats.recTD}" /><tds:addRowTotal name="recTD" value="${stats.recTD}" /></td>
                                      <td><fmt:formatNumber value="${stats.recTwoPt}" /><tds:addRowTotal name="rec2Pt" value="${stats.recTwoPt}" /></td>
                                      <td><fmt:formatNumber value="${stats.fumblesLost}" /><tds:addRowTotal name="fumLost" value="${stats.fumblesLost}" /></td>
                                      <td><fmt:formatNumber value="${stats.xtraTD}" /><tds:addRowTotal name="xtraTD" value="${stats.xtraTD}" /></td>
                                      <td><fmt:formatNumber minFractionDigits="2" value="${stats.fantasyPts}" /><tds:addRowTotal name="fantasyPts" value="${stats.fantasyPts}" /></td>
                                     </tr>
                                </c:if>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="18">No Games Played.</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowTotal">
                                <tr class="rowHeader">
                                    <td>Total</td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${comp.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${att.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${passYards.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${interceptions.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${passTD.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${pass2Pt.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${rush.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${rushYards.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${rushTD.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${rush2Pt.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${rec.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${recYards.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${recTD.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${rec2Pt.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${fumLost.total}"/></td>
                                    <td><fmt:formatNumber maxFractionDigits="0" value="${xtraTD.total}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="2" value="${fantasyPts.total}"/></td>
                                </tr>
                                <%--
                                <tr>
                                    <td>Avg</td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${comp.total / comp.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${att.total / att.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${passYards.total / passYards.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${interceptions.total / interceptions.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${passTD.total / passTD.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${pass2Pt.total / pass2Pt.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${rush.total / rush.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${rushYards.total / rushYards.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${rushTD.total / rushTD.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${rush2Pt.total / rush2Pt.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${rec.total / rec.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${recYards.total / recYards.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${recTD.total / recTD.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${rec2Pt.total / rec2Pt.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${fumLost.total / fumLost.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${xtraTD.total / xtraTD.count}"/></td>
                                    <td><fmt:formatNumber minFractionDigits="1" maxFractionDigits="1" value="${fantasyPts.total / fantasyPts.count}"/></td>
                                </tr>
                                --%>
                            </jsp:attribute>
                        </tds:tableRows>
                    </table>
                </c:otherwise>
            </c:choose>
                    
        </div> <!-- inner player stats -->
    </div> <!-- player stats -->

</body>
</html>