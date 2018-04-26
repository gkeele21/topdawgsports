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
        </div> <!-- main menu -->

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div> <!-- left Menu -->

        <div id="content">
            <div id="innerContent">
                
                <div id="vendorContent" class="game">
		<!-- #BeginEditable "content" --> 
                  
                  <div id="standings">
                   <div id="innerStandings">
                       
                       <%-- Standings --%>
                       <c:if test="${golfleagueStandings.isEmpty()}">
                           <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tds:tableRows items="${golfallLeagueTeams}" var="team" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                    <jsp:attribute name="rowTitle">
                                        <tr align="center" class="rowTitle">
                                            <td colspan="8">
                                                <strong><h2>Teams</h2></strong>
                                            </td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowHeader">
                                        <tr class="rowHeader">
                                            <th width="50%">Team</th>
                                            <th width="50%">Owner</th>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <tr ${highlightRow1} class="rowData">
                                            <td><tds:team teamObj="${team}" displayRosterLink="false" weekID="" /></td>
                                            <td>${team.FSUser.firstName} ${ls.FSTeam.FSUser.lastName}</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowEmpty">
                                        <tr>
                                            <td colspan="10">There are no teams to display.</td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>

                        </table>
                           
                       </c:if>
                           <br /><br />
                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                            <tds:tableRows items="${weekGolfResults}" var="ls" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr align="center" class="rowTitle">
                                        <td colspan="8">
                                            <strong><h2>Results for <c:out value="${lastWeekTournament.PGATournament.tournamentName}" /> </h2></strong>
                                        </td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <th width="5%">Rank</th>
                                        <th width="20%">Team</th>
                                        <th width="15%">User</th>
                                        <th width="15%">Total $</th>
                                        <th width="12%">Winnings</th>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <td><c:out value="${ls.rank}" /></td>
                                        <td><tds:team teamObj="${ls.FSTeam}" displayRosterLink="true" weekID="${displayWeek.FSSeasonWeekID}" /></td>
                                        <td><c:out value="${ls.FSTeam.FSUser.firstName} ${ls.FSTeam.FSUser.lastName}" /></td>
                                        <td><fmt:formatNumber type="currency" value="${ls.weekMoneyEarned}" maxFractionDigits="0" /></td>
                                        <td><c:if test="${ls.weekWinnings > 0}">
                                                <fmt:formatNumber type="currency" value="${ls.weekWinnings}" maxFractionDigits="0" />
                                            </c:if>
                                            </td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="10">There are no teams to display.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>

                       </table>
                       <br /><br />
                       <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                            <tds:tableRows items="${golfleagueStandings}" var="ls" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr align="center" class="rowTitle">
                                        <td colspan="8">
                                            <strong><h2>YEAR Standings</h2></strong>
                                        </td>
                                    </tr>
                                    <c:set value="0" var="rankNum" />
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <th width="5%">Rank</th>
                                        <th width="20%">Team</th>
                                        <th width="15%">User</th>
                                        <th width="7%"># Events</th>
                                        <th width="10%">Total Fees</th>
                                        <th width="12%">Total Winnings</th>
                                        <th width="15%">Total $</th>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <c:set var="rankNum" value="${rankNum+1}" />
                                    <tr ${highlightRow1} class="rowData">
                                        <td>${rankNum}</td>
                                        <td><tds:team teamObj="${ls.FSTeam}" displayRosterLink="true" weekID="${displayWeek.FSSeasonWeekID}" /></td>
                                        <td><c:out value="${ls.FSTeam.FSUser.firstName} ${ls.FSTeam.FSUser.lastName}" /></td>
                                        <td><c:out value="${ls.totalEventsEntered}" /></td>
                                        <td><fmt:formatNumber type="currency" value="${ls.totalFees}" maxFractionDigits="0" /></td>
                                        <td><c:if test="${ls.totalWinnings > 0}">
                                                <fmt:formatNumber type="currency" value="${ls.totalWinnings}" maxFractionDigits="0" />
                                            </c:if></td>
                                        <td><fmt:formatNumber type="currency" value="${ls.totalMoneyEarned}" maxFractionDigits="0" /></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="10">There are no teams to display.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>

                       </table>

                   </div> <!-- inner standings -->
               </div> <!-- standings -->
                  <br /><br />
                  <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                        <tds:tableRows items="${tournamentWeeks}" var="tournamentWeek" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowInfo">

                            </jsp:attribute>
                            <jsp:attribute name="rowTitle">
                                <tr align="center" class="rowTitle">
                                    <td colspan="8">
                                        <div><strong><h2>Tournament Results For Your Team</h2></strong></div><br />
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="8">
                                    </td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td width="10%" valign="bottom" class="GTW10" >WK</td>
                                    <td width="30%" valign="bottom" class="GTW10" ><strong>TOURNAMENT</strong></td>
                                    <td width="20%" valign="bottom" align="center" class="GTW10" ><b>DATES</b></td>
                                    <td width="10%" valign="bottom" align="center" class="GTW10" ><b>Fee</b></td>
                                    <td width="10%" valign="bottom" align="center" class="GTW10" ><b>Rank</b></td>
                                    <td width="10%" valign="bottom" align="center" class="GTW10" ><b>Winnings</b></td>
                                    <td width="10%" valign="bottom" align="center" class="GTW10" ><b>$</b></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <tr ${highlightRow1} class="rowData">
                                    <c:set var="standingsRec" value="${tds:getStandingsRecord(fsTeam.FSTeamID,tournamentWeek.FSSeasonWeek.FSSeasonWeekID)}" />
                                    <td align="center"><c:out value="${tournamentWeek.FSSeasonWeek.FSSeasonWeekNo}" /></td>
                                    <td align="center"><c:out value="${tournamentWeek.PGATournament.tournamentName}" /></td>
                                    <td align="center"><fmt:formatDate value="${tournamentWeek.startDate.time}" pattern="E MM/dd"/> - <fmt:formatDate value="${tournamentWeek.endDate.time}" pattern="E MM/dd"/></td>
                                    <td align="center"><fmt:formatNumber type="currency" value="${tournamentWeek.teamFee}" maxFractionDigits="0" /></td>
                                    <td align="center"><c:out value="${standingsRec.rank}" /></td>
                                    <td align="center"><fmt:formatNumber type="currency" value="${standingsRec.weekWinnings}" maxFractionDigits="0" /></td>
                                    
                                    <td align="center">
                                        <c:if test="${standingsRec != null}">
                                            <fmt:formatNumber type="currency" value="${standingsRec.weekMoneyEarned}" maxFractionDigits="0" />
                                        </c:if>
                                    </td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="10">There are no tournaments to display.</td>
                                </tr>
                            </jsp:attribute>
                        </tds:tableRows>
                    </table>
                    
                    
				  
                  <!-- #EndEditable -->
         	</div>				
			
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->
  </body>
</html>
