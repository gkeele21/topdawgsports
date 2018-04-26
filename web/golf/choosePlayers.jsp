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

                <div id="vendorContent" class="game">
                  
                    <!-- #BeginEditable "content" --> 
                  <div id="instructions" />
                  Please select up to 6 golfers from the list below. All golfers on your team will
                  count.  Your total golfer salaries may not exceed the salary 
                  cap of $1,000,000. Players will not carry over from week to 
                  week. You can add or remove any player until we have recorded that
                  the player has completed the first hole of the tournament.
                  </div><br />
                  <script>
                      console.log('Starters Deadline : ${tournamentWeek.FSSeasonWeek.startersDeadline.time}');
                      console.log('Deadline Passed? : ${deadlinePassed}');
                      console.log('Today : ${today}');
                  </script>
                  <form action="choosePlayers.do" method="post">
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
                                <td><fmt:formatDate value="${tournamentWeek.startDate.time}" pattern="E MM/dd"/> - <fmt:formatDate value="${tournamentWeek.endDate.time}" pattern="E MM/dd"/></td>
                                <td><fmt:formatDate value="${tournamentWeek.FSSeasonWeek.startersDeadline.time}" pattern="E MM/dd h:mm a"/></td>
                            </tr>
                          </table>
                          <br />
                          <jsp:include page="../inc_errorMessage.jsp" />
                          <br />
                          <input type="hidden" value="${fantasyCurrentWeek.FSSeasonWeekID}" name="fsSeasonWeekID" />
                          <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tds:tableRows items="${yourRoster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                    <jsp:attribute name="rowTitle">
                                        <tr align="center" class="rowTitle">
                                            <td colspan="8">
                                                <strong><h2>Current Roster</h2></strong>
                                            </td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowHeader">
                                        <tr class="rowHeader">
                                            <th width="3%">Drop</th>
                                            <th width="20%">Name</th>
                                            <th width="10%">Value</th>
                                            <th width="10%">Tourn Rank</th>
                                            <th width="10%">Total</th>
                                            <th width="10%">Thru</th>
                                            <th width="5%">Today</th>
                                            <th width="3%">R1</th>
                                            <th width="3%">R2</th>
                                            <th width="3%">R3</th>
                                            <th width="3%">R4</th>
                                            <th width="5%">Strokes</th>
                                            <th width="10%">Tourn Earned</th>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <tr ${highlightRow1} class="rowData">
                                            <td>
                                                <c:if test="${deadlinePassed != true}">
                                                <%--<c:if test="${roster.PGATournamentWeekPlayer.finalScore < 1}">--%>
                                                    <input type="checkbox" name="drop" value="${roster.ID}_${roster.PGATournamentWeekPlayer.salaryValue}"/>
                                                </c:if>
                                            </td>
                                            <td><c:out value="${roster.player.firstName}" /> <c:out value="${roster.player.lastName}" /> (<c:out value="${roster.player.country.country}" />)</td>
                                            <td><fmt:formatNumber type="currency" value="${roster.PGATournamentWeekPlayer.salaryValue}" maxFractionDigits="0" />
                                                <tds:addRowTotal name="teamSalary" value="${roster.PGATournamentWeekPlayer.salaryValue}" />
                                            </td>
                                            <td><c:out value="${roster.PGATournamentWeekPlayer.tournamentRank}" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${roster.PGATournamentWeekPlayer.relativeToPar == 0}">
                                                        <c:out value="E" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${roster.PGATournamentWeekPlayer.relativeToPar}" maxFractionDigits="0" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatNumber value="${roster.PGATournamentWeekPlayer.thru}" maxFractionDigits="0" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${roster.PGATournamentWeekPlayer.todayRound == 0}">
                                                        <c:out value="E" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${roster.PGATournamentWeekPlayer.todayRound}" maxFractionDigits="0" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatNumber value="${roster.PGATournamentWeekPlayer.round1}" maxFractionDigits="0" /></td>
                                            <td><fmt:formatNumber value="${roster.PGATournamentWeekPlayer.round2}" maxFractionDigits="0" /></td>
                                            <td><fmt:formatNumber value="${roster.PGATournamentWeekPlayer.round3}" maxFractionDigits="0" /></td>
                                            <td><fmt:formatNumber value="${roster.PGATournamentWeekPlayer.round4}" maxFractionDigits="0" /></td>
                                            <td><c:out value="${roster.PGATournamentWeekPlayer.finalScore}" /></td>                                            
                                            <td><fmt:formatNumber type="currency" value="${roster.PGATournamentWeekPlayer.moneyEarned}" maxFractionDigits="0" /></td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowTotal">
                                        <tr class="rowHeader">
                                            <td colspan="2">USED</td>
                                            <td><fmt:formatNumber type="currency" maxFractionDigits="0" value="${teamSalary.total}"/></td>
                                        </tr>
                                        <tr class="rowHeader">
                                            <td colspan="2">REMAINING</td>
                                            <td>(<fmt:formatNumber type="currency" maxFractionDigits="0" value="${1000000-teamSalary.total}"/>)</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowEmpty">
                                        <tr>
                                            <td colspan="10">There are no players on your roster.</td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>
                            </table>
                          
                          <br />
                          <input class="smallImage" type="image" name="btnSubmit" src="../images/submit.png" />
                          <br /><br />
                          
                          <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tds:tableRows items="${weekPlayers}" var="puPlayer" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                    <jsp:attribute name="rowTitle">
                                        <tr align="center" class="rowTitle">
                                            <td colspan="8">
                                                <strong><h2>PICK UP PLAYERS</h2></strong>
                                            </td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowHeader">
                                        <tr class="rowHeader">
                                            <th width="10%">Pick Up</th>
                                            <th width="45%">Name</th>
                                            <th width="45%">Value</th>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <tr ${highlightRow1} class="rowData">
                                            <td>
                                                <c:if test="${deadlinePassed != true}">
                                                <%--<c:if test="${puPlayer.finalScore < 1}">--%>
                                                    <input type="checkbox" name="pickup" value="${puPlayer.playerID}_${puPlayer.salaryValue}" />
                                                </c:if>
                                            </td>                                                    
                                            <td><c:out value="${puPlayer.player.firstName}" /> <c:out value="${puPlayer.player.lastName}" /> (<c:out value="${puPlayer.player.country.country}" />)</td>
                                            <td><fmt:formatNumber type="currency" value="${puPlayer.salaryValue}" maxFractionDigits="0" /></td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowEmpty">
                                        <tr>
                                            <td colspan="10">The field should be set the Monday before the tournament starts.</td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>
                            </table>	
                          <input class="smallImage" type="image" name="btnSubmit" src="../images/submit.png" /> 
                          </center>
                  </form>
                  <!-- #EndEditable -->
         	</div>
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->

  </body>
</html>
