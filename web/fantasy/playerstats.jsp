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
                    
            <table>
                <tds:tableRows items="${playerStats}" var="stats" highlightRowAttribute="class" highlightRowValue="rowData2">
                    <jsp:attribute name="rowTitle">
                        <tr class="rowTitle">
                            <td colspan="14"><h2>${statPlayer.fullName}</h2></td>
                        </tr>
                    </jsp:attribute>
                    <jsp:attribute name="rowHeader">
                        <tr class="rowHeader">
                            <td>Week</td>
                            <!-- PASSING RUSHING / RECEIVING COLUMN HEADERS -->
                            <c:if test="${statPlayer.position.positionName == 'QB' || statPlayer.position.positionName == 'RB' || statPlayer.position.positionName == 'WR' || statPlayer.position.positionName == 'TE'}">
                                <td>Comp</td>
                                <td>Att</td>
                                <td>Yards</td>
                                <td>TD's</td>
                                <td>Int's</td> 
                                <td>Rush</td>
                                <td>Yards</td>
                                <td>TD's</td>
                                <td>Rec</td>
                                <td>Yards</td>
                                <td>TD's</td>
                            </c:if>
                            <!-- KICKING COLUMN HEADERS -->
                            <c:if test="${statPlayer.position.positionName == 'PK'}">
                                <td>XP</td>
                                <td>XPA</td>
                                <td>FG</td>
                                <td>FGA</td>
                                <td>Distances</td>
                            </c:if>
                            <!-- DEFENSE MAIN HEADER -->
                            <c:if test="${statPlayer.position.positionName == 'DL' || statPlayer.position.positionName == 'LB' || statPlayer.position.positionName == 'DB'}">
                                <td>Solo Tkl</td>
                                <td>Asst Tkl</td>
                                <td>Sacks</td>
                                <td>Int's</td>
                                <td>FF</td>
                                <td>FR</td>
                                <td>TD's</td>
                            </c:if>
                            <td>2 Pt</td>
                            <td>FP</td>
                        </tr>
                    </jsp:attribute>
                    <jsp:attribute name="rowData">
                        <c:if test="${stats.seasonWeek.weekNo >= 1}">
                            <tr ${highlightRow1} class="rowData">
                                <td>${stats.seasonWeek.weekNo}</td>
                                <!-- PASSING / RUSHING / RECEIVING Stats -->
                                <c:if test="${statPlayer.position.positionName == 'QB' || statPlayer.position.positionName == 'RB' || statPlayer.position.positionName == 'WR' || statPlayer.position.positionName == 'TE'}">
                                    <td>${stats.passComp}<tds:addRowTotal name="comp" value="${stats.passComp}" /></td>
                                    <td>${stats.passAtt}<tds:addRowTotal name="att" value="${stats.passAtt}" /></td>
                                    <td>${stats.passYards}<tds:addRowTotal name="passYards" value="${stats.passYards}" /></td>
                                    <td>${stats.passTD}<tds:addRowTotal name="passTD" value="${stats.passTD}" /></td>
                                    <td>${stats.passInt}<tds:addRowTotal name="interceptions" value="${stats.passInt}" /></td>
                                    <td>${stats.rushAtt}<tds:addRowTotal name="rush" value="${stats.rushAtt}" /></td>
                                    <td>${stats.rushYards}<tds:addRowTotal name="rushYards" value="${stats.rushYards}" /></td>
                                    <td>${stats.rushTD}<tds:addRowTotal name="rushTD" value="${stats.rushTD}" /></td>                                    
                                    <td>${stats.recCatches}<tds:addRowTotal name="rec" value="${stats.recCatches}" /></td>
                                    <td>${stats.recYards}<tds:addRowTotal name="recYards" value="${stats.recYards}" /></td>
                                    <td>${stats.recTD}<tds:addRowTotal name="recTD" value="${stats.recTD}" /></td>
                                </c:if>
                                <!-- KICKING Stats -->
                                <c:if test="${statPlayer.position.positionName == 'PK'}">
                                    <td>${stats.XPM}<tds:addRowTotal name="xpm" value="${stats.XPM}" /></td>
                                    <td>${stats.XPA}<tds:addRowTotal name="xpa" value="${stats.XPA}" /></td>
                                    <td>${stats.FGM}<tds:addRowTotal name="fgm" value="${stats.FGM}" /></td>
                                    <td>${stats.FGA}<tds:addRowTotal name="fga" value="${stats.FGA}" /></td>
                                    <td>${stats.TDDistances}</td>
                                </c:if>  
                                <!-- DEFENSE Stats -->
                                <c:if test="${statPlayer.position.positionName == 'DL' || statPlayer.position.positionName == 'LB' || statPlayer.position.positionName == 'DB'}">
                                    <td>${stats.IDPTackles}<tds:addRowTotal name="idptkl" value="${stats.IDPTackles}" /></td>
                                    <td>${stats.IDPAssists}<tds:addRowTotal name="idpast" value="${stats.IDPAssists}" /></td>
                                    <td>${stats.IDPSacks}<tds:addRowTotal name="sacks" value="${stats.IDPSacks}" /></td>
                                    <td>${stats.IDPInterceptions}<tds:addRowTotal name="idpints" value="${stats.IDPInterceptions}" /></td>
                                    <td>${stats.IDPFumblesForced}<tds:addRowTotal name="idpff" value="${stats.IDPFumblesForced}" /></td>
                                    <td>${stats.IDPFumbleRecoveries}<tds:addRowTotal name="idpfr" value="${stats.IDPFumbleRecoveries}" /></td>
                                    <td>${stats.IDPIntReturnsForTD}<tds:addRowTotal name="idptds" value="${stats.IDPIntReturnsForTD}" /></td>
                                </c:if>
                                <td>${stats.passTwoPt + stats.rushTwoPt + stats.recTwoPt}<tds:addRowTotal name="twopt" value="${stats.passTwoPt + stats.rushTwoPt + stats.recTwoPt}" /></td>
                                <td><fmt:formatNumber value="${stats.fantasyPts}" minFractionDigits="2" /><tds:addRowTotal name="fantasyPts" value="${stats.fantasyPts}" /></td>
                            </tr>
                        </c:if>
                    </jsp:attribute>
                    <jsp:attribute name="rowEmpty">
                        <tr>
                            <td colspan="14">No Games Played.</td>
                        </tr>
                    </jsp:attribute>
                    <jsp:attribute name="rowTotal">
                        
                        <tr class="rowHeader">
                            <td>Total</td>
                            <!-- PASSING / RUSHING / RECEIVING Total -->
                            <c:if test="${statPlayer.position.positionName == 'QB' || statPlayer.position.positionName == 'RB' || statPlayer.position.positionName == 'WR' || statPlayer.position.positionName == 'TE'}">
                                <td><fmt:formatNumber minFractionDigits="0" value="${comp.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${att.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${passYards.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${interceptions.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${passTD.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${rush.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${rushYards.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${rushTD.total}"/></td>                                    
                                <td><fmt:formatNumber minFractionDigits="0" value="${rec.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${recYards.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${recTD.total}"/></td>
                            </c:if>
                            <!-- KICKING Total -->
                            <c:if test="${statPlayer.position.positionName == 'PK'}">
                                <td><fmt:formatNumber minFractionDigits="0" value="${xpm.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${xpa.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${fgm.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${fga.total}"/></td>
                                <td>-</td>
                            </c:if>
                            <!-- DEFENSE Total -->
                            <c:if test="${statPlayer.position.positionName == 'DL' || statPlayer.position.positionName == 'LB' || statPlayer.position.positionName == 'DB'}">
                                <td><fmt:formatNumber minFractionDigits="0" value="${idptkl.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${idpast.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${sacks.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${idpints.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${idpff.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${idpfr.total}"/></td>
                                <td><fmt:formatNumber minFractionDigits="0" value="${idptds.total}"/></td>
                            </c:if>
                            <td><fmt:formatNumber minFractionDigits="0" value="${twopt.total}"/></td>    
                            <td><fmt:formatNumber minFractionDigits="2" value="${fantasyPts.total}"/></td>
                        </tr>
                    </jsp:attribute>
                </tds:tableRows>
            </table>
        </div> <!-- inner player stats -->
    </div> <!-- player stats -->

</body>
</html>