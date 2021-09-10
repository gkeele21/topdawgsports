<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>Golf Leaderboard</title>
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

                <div id="vendorContent" class="game">

                    <!-- #BeginEditable "content" -->
                    <center>
                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                            <tr class="rowHeader">
                                <td width="10%"><strong>Week #</strong></td>
                                <td width="40%"><strong>Tournament</strong></td>
                                <td width="25%"><strong>Dates</strong></td>
                                <td width="25%"><strong>Deadline</strong></td>
                            </tr>
                            <tr class="rowData">
                                <td><c:out value="${fantasyCurrentWeek.FSSeasonWeekNo}" /><script>console.log('FSSeasonWeekID : ' + ${fantasyCurrentWeek.FSSeasonWeekID});</script></td>
                                <td><c:out value="${tournamentWeek.PGATournament.tournamentName}" /><script>console.log('PGATournamentID : ' + ${tournamentWeek.PGATournament.PGATournamentID});</script></td>
                                <td>
                                    <fmt:parseDate  value="${tournamentWeek.startDate}" type="date" pattern="yyyy-MM-dd" var="startDate" />
                                    <fmt:parseDate  value="${tournamentWeek.endDate}" type="date" pattern="yyyy-MM-dd" var="endDate" />
                                    <fmt:formatDate value="${startDate}" pattern="E MM/dd"/> - <fmt:formatDate value="${endDate}" pattern="E MM/dd"/>
                                </td>
                                <td>
                                    <fmt:parseDate  value="${tournamentWeek.FSSeasonWeek.startersDeadline}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="startersDeadline" />
                                    <fmt:formatDate value="${startersDeadline}" pattern="E MM/dd h:mm a" timeZone="America/Denver" />
                                </td>
                            </tr>
                          </table>
                          <br />
                          <jsp:include page="../inc_errorMessage.jsp" />
                          <br />

                          <table width="60%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tds:tableRows items="${leagueStandings}" var="standing" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                    <jsp:attribute name="rowTitle">
                                        <tr align="center" class="rowTitle">
                                            <td colspan="8">
                                                <strong><h2>Current League Leaders</h2></strong>
                                            </td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowHeader">
                                        <tr class="rowHeader">
                                            <th width="25%">Team</th>
                                            <th width="25%">User</th>
                                            <th width="10%">$</th>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <tr ${highlightRow1} class="rowData">
                                            <td><c:out value="${standing.FSTeam.teamName}" /></td>
                                            <td><c:out value="${standing.FSTeam.FSUser.firstName} ${fn:substring(standing.FSTeam.FSUser.lastName,0,1)}." /></td>
                                            <td><fmt:formatNumber type="currency" maxFractionDigits="0" value="${standing.weekMoneyEarned}" /></td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowEmpty">
                                        <tr>
                                            <td colspan="10">There are no results for the league yet.</td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>
                            </table>

                          <br /><br />

                          <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tds:tableRows items="${weekPlayers}" var="weekPlayer" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                    <jsp:attribute name="rowTitle">
                                        <tr align="center" class="rowTitle">
                                            <td colspan="18">
                                                <strong><h2>Current Leaderboard</h2></strong>
                                            </td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowHeader">
                                        <tr class="rowHeader">
                                            <th width="25%">Name</th>
                                            <th width="10s%">Tourn Rank</th>
                                            <th width="10%">Total</th>
                                            <th width="10%">Thru</th>
                                            <th width="10%">Today</th>
                                            <th width="3%">R1</th>
                                            <th width="3%">R2</th>
                                            <th width="3%">R3</th>
                                            <th width="3%">R4</th>
                                            <th width="5%">Strokes</th>
                                            <th width="10%">Tourn Earned</th>
                                            <th width="10%">Owners</th>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <tr ${highlightRow1} class="rowData">
                                            <td><c:out value="${weekPlayer.player.firstName}" /> <c:out value="${weekPlayer.player.lastName}" /> (<c:out value="${weekPlayer.player.country.country}" />)</td>
                                            <td><c:out value="${weekPlayer.tournamentRank}" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${weekPlayer.relativeToPar == 0}">
                                                        <c:out value="E" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${weekPlayer.relativeToPar}" maxFractionDigits="0" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatNumber value="${weekPlayer.thru}" maxFractionDigits="0" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${weekPlayer.todayRound == 0}">
                                                        <c:out value="E" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${weekPlayer.todayRound}" maxFractionDigits="0" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatNumber value="${weekPlayer.round1}" maxFractionDigits="0" /></td>
                                            <td><fmt:formatNumber value="${weekPlayer.round2}" maxFractionDigits="0" /></td>
                                            <td><fmt:formatNumber value="${weekPlayer.round3}" maxFractionDigits="0" /></td>
                                            <td><fmt:formatNumber value="${weekPlayer.round4}" maxFractionDigits="0" /></td>
                                            <td><c:out value="${weekPlayer.finalScore}" /></td>
                                            <td><fmt:formatNumber type="currency" value="${weekPlayer.moneyEarned}" maxFractionDigits="0" /></td>
                                            <td>
                                                <c:if test='${weekPlayer.owners != null}'>
                                                    <c:forEach items="${weekPlayer.owners}" var="owner">
                                                        <c:out value="${owner.FSUser.firstName} ${fn:substring(owner.FSUser.lastName,0,1)}." /><br />
                                                    </c:forEach>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowEmpty">
                                        <tr>
                                            <td colspan="10">There are no players on the leaderboard.</td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>
                            </table>

                          </center>
                  </form>
                  <!-- #EndEditable -->
         	</div>
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->

  </body>
</html>
