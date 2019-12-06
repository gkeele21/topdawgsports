<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script type="text/javascript" src="../js/script.js" ></script>
  </head>
  <body>

    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div><!-- main menu -->

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div>

        <div id="content">
            <div id="innerContent">

                <div id="playerStats">
                    <div id="innerPlayerStats">

                        <div id="position">
                            <label>Choose a Position :</label>
                            <a href="totalStatsQB.htm">QB</a>
                            <a href="totalStatsRB.htm">RB</a>
                            <a href="totalStatsWR.htm">WR</a>
                            <a href="totalStatsTE.htm">TE</a>
                            <a href="totalStatsPK.htm">PK</a>
                        </div>

                        <table>
                            <tds:tableRows items="${players}" var="playerstats" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2" displayRows="50" startingRowNum="${startingRowNum}">
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="12">Tight End Leaders</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td colspan="4"></td>
                                        <td colspan="3">Receiving</td>
                                        <td colspan="3">Rushing</td>
                                        <td colspan="2">Ftsy Pts</td>
                                    </tr>
                                    <tr class="rowHeader">
                                        <td>#</td>
                                        <td>Owner</td>
                                        <td>Team</td>
                                        <td>Player</td>
                                        <td><a href="totalStatsTE.htm?orderBy=tst.RecCatches">Rec</a></td>
                                        <td><a href="totalStatsTE.htm?orderBy=tst.RecYards">Yards</a></td>
                                        <td><a href="totalStatsTE.htm?orderBy=tst.RecTD">Td</a></td>
                                        <td><a href="totalStatsTE.htm?orderBy=tst.RushAtt">Att</a></td>
                                        <td><a href="totalStatsTE.htm?orderBy=tst.RushYards">Yards</a></td>
                                        <td><a href="totalStatsTE.htm?orderBy=tst.RushTD">Td</a></td>
                                        <td><a href="totalStatsTE.htm?orderBy=tst.FantasyPts">Total</a></td>
                                        <td><a href="totalStatsTE.htm?orderBy=TotalFootballStats$AvgFantasyPts">Avg</a></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <td>${currentRowOverall1}.</td>
                                        <td>
                                            <c:if test="${playerstats.FSTeam != null && playerstats.FSTeam.FSTeamID > 0}">
                                                <c:out value="${playerstats.FSTeam.teamName}" />
                                            </c:if>
                                        </td>
                                        <td><c:out value="${playerstats.player.team.displayName}" /></td>
                                        <td><tds:player player="${playerstats.player}" displayStatsLink="true" displayInjury="false" /></td>
                                        <td><fmt:formatNumber value="${playerstats.player.totalFootballStats.recCatches}"/></td>
                                        <td><fmt:formatNumber value="${playerstats.player.totalFootballStats.recYards}"/></td>
                                        <td><fmt:formatNumber value="${playerstats.player.totalFootballStats.recTD}"/></td>
                                        <td><fmt:formatNumber value="${playerstats.player.totalFootballStats.rushAtt}"/></td>
                                        <td><fmt:formatNumber value="${playerstats.player.totalFootballStats.rushYards}"/></td>
                                        <td><fmt:formatNumber value="${playerstats.player.totalFootballStats.rushTD}"/></td>
                                        <td><fmt:formatNumber value="${playerstats.player.totalFootballStats.fantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                        <td><fmt:formatNumber value="${playerstats.player.totalFootballStats.avgFantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="12">There are no stats available for this week.</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowNavigation">
                                    <tr>
                                        <td colspan="12"><hr /></td>
                                    </tr>
                                    <tr class="rowData2">
                                        <td colspan="2"><tds:navFirst link="totalStatsTE.htm">FIRST</tds:navFirst></td>
                                        <td colspan="1"><tds:navPrev link="totalStatsTE.htm"><< PREV</tds:navPrev></td>
                                        <td colspan="3">${fromRows1} to ${toRows1} of ${totalRows1}</td>
                                        <td colspan="3"><tds:navNext link="totalStatsTE.htm">NEXT >></tds:navNext></td>
                                        <td colspan="3"><tds:navLast link="totalStatsTE.htm">LAST</tds:navLast></td>
                                    </tr>
                               </jsp:attribute>
                            </tds:tableRows>
                        </table>

                    </div> <!-- inner Player Stats -->
                </div> <!-- Player Stats -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->

  </body>
</html>
