<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="ct.control.registerAction"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ct" uri="ct.taglib" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<un:useConstants var="fields" className="ct.control.registerAction" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="../css/styles.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../css/registration.css" media="screen" />
<title>Coltarama - Week's Rosters</title>
<script type="text/javascript" src="../js/script.js" ></script>

</head>

<body>
    <div id="container">
        
        <jsp:include page="../inc_header.jsp" />

        <jsp:include page="../inc_mainmenu.jsp" />
        
        <jsp:include page="../inc_submenu.jsp" />
        
        <jsp:include page="../inc_errorMessage.jsp" />
        
        <div class="colmask outside">

            <div class="main">
            
                <div class="leftcolumn">
                    
                    <div id="content">
                        <br />
                        <c:if test="${tenmanMatchup != null}">
                            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr>
                                    <td colspan="5" align="center" class="rowTitleGreen"><h1>Tenman</h1></td>
                                </tr>
                                <tr>
                                    <td colspan="3"><img src="../images/spacer.gif" height="10" /></td>
                                </tr>
                                <tr>
                                    <td colspan="3" align="center" class="rowTitleGreen">
                                        <h2>&#160;&#160;Wk #${tenmanMatchup.FSSeasonWeek.FSSeasonWeekNo}&#160;&#160;</h2>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3"><img src="../images/spacer.gif" height="10" /></td>
                                </tr>
                                <tr class="rowHeader">
                                    <td align="center">Team 1</td>
                                    <td align="center">vs</td>
                                    <td align="center">Team 2</td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="center">${tenmanMatchup.FSTeam1.teamName} : <fmt:formatNumber value="${tenmanMatchup.team1Pts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                    <td></td>
                                    <td align="center">${tenmanMatchup.FSTeam2.teamName} : <fmt:formatNumber value="${tenmanMatchup.team2Pts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                </tr>
                                <tr>
                                    <td width="43%">
                                        <!-- Team 1 -->
                                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                            <ct:tableRows items="${tenmanTeam1roster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                                <jsp:attribute name="rowTitle">
                                                    <tr align="center" class="rowTitle">
                                                        <td colspan="8">
                                                            <div><strong>${tenmanMatchup.FSTeam1.teamName}</strong></div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="8"></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowHeader">
                                                    <tr class="rowHeader">
                                                        <td width="20%" align="center"><strong>POS</strong></td>
                                                        <td width="40%" align="center"><strong>PLAYER</strong></td>
                                                        <td width="20%" align="center"><strong>TEAM</strong></td>
                                                        <td width="20%" align="center"><strong>FANT PTS</strong></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowData">
                                                    <c:set var="highClass" value="rowData" />
                                                    <c:if test="${roster.activeState == 'inactive'}">
                                                        <c:set var="highClass" value="rowDataRed" />
                                                        <c:set var="highlightRow1" value="rowDataRed" />
                                                    </c:if>
                                                    <c:if test="${roster.starterState == 'starter'}">
                                                        <c:set var="highClass" value="rowDataYellow" />
                                                        <c:set var="highlightRow1" value="rowDataYellow" />
                                                    </c:if>
                                                    <tr ${highlightRow1} class="${highClass}">
                                                        <td align="center"><c:out value="${roster.player.position.positionName}" /></td>
                                                        <td align="center"><ct:player player="${roster.player}" displayStatsLink="true" /></td>
                                                        <td align="center"><c:out value="${roster.player.team.teamName}" /></td>
                                                        <td align="center"><fmt:formatNumber value="${roster.footballStats.fantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowEmpty">
                                                    <tr>
                                                        <td colspan="8">This team has no active roster for this week.</td>
                                                    </tr>
                                                </jsp:attribute>
                                            </ct:tableRows>
                                        </table>
                                        <!-- End Team 1 -->
                                    </td>
                                    <td width="14%"></td>
                                    <td width="43%">
                                        <!-- Team 2 -->
                                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                            <ct:tableRows items="${tenmanTeam2roster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                                <jsp:attribute name="rowTitle">
                                                    <tr align="center" class="rowTitle">
                                                        <td colspan="8">
                                                            <div><strong>${tenmanMatchup.FSTeam2.teamName}</strong></div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="8"></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowHeader">
                                                    <tr class="rowHeader">
                                                        <td width="20%" align="center"><strong>POS</strong></td>
                                                        <td width="40%" align="center"><strong>PLAYER</strong></td>
                                                        <td width="20%" align="center"><strong>TEAM</strong></td>
                                                        <td width="20%" align="center"><strong>FANT PTS</strong></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowData">
                                                    <c:set var="highClass" value="rowData" />
                                                    <c:if test="${roster.activeState == 'inactive'}">
                                                        <c:set var="highClass" value="rowDataRed" />
                                                        <c:set var="highlightRow1" value="rowDataRed" />
                                                    </c:if>
                                                    <c:if test="${roster.starterState == 'starter'}">
                                                        <c:set var="highClass" value="rowDataYellow" />
                                                        <c:set var="highlightRow1" value="rowDataYellow" />
                                                    </c:if>
                                                    <tr ${highlightRow1} class="${highClass}">
                                                        <td align="center"><c:out value="${roster.player.position.positionName}" /></td>
                                                        <td align="center"><ct:player player="${roster.player}" displayStatsLink="true" /></td>
                                                        <td align="center"><c:out value="${roster.player.team.teamName}" /></td>
                                                        <td align="center"><fmt:formatNumber value="${roster.footballStats.fantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowEmpty">
                                                    <tr>
                                                        <td colspan="8">This team has no active roster for this week.</td>
                                                    </tr>
                                                </jsp:attribute>
                                            </ct:tableRows>
                                        </table>
                                        <!-- End Team 2 -->
                                    </td>
                                </tr>
                            </table>
                        </c:if>
                        <!-- Keeper -->
                        <c:if test="${keeperMatchup != null}">
                            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr>
                                    <td colspan="5" align="center" class="rowTitleGreen"><h1>Keeper</h1></td>
                                </tr>
                                <tr>
                                    <td colspan="3"><img src="../images/spacer.gif" height="10" /></td>
                                </tr>
                                <tr>
                                    <td colspan="3" align="center" class="rowTitleGreen">
                                        <h2>&#160;&#160;Wk #${keeperMatchup.FSSeasonWeek.FSSeasonWeekNo}&#160;&#160;</h2>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3"><img src="../images/spacer.gif" height="10" /></td>
                                </tr>
                                <tr class="rowHeader">
                                    <td align="center">Team 1</td>
                                    <td align="center">vs</td>
                                    <td align="center">Team 2</td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="center">${keeperMatchup.FSTeam1.teamName} : <fmt:formatNumber value="${keeperMatchup.team1Pts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                    <td></td>
                                    <td align="center">${keeperMatchup.FSTeam2.teamName} : <fmt:formatNumber value="${keeperMatchup.team2Pts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                </tr>
                                <tr>
                                    <td width="43%">
                                        <!-- Team 1 -->
                                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                            <ct:tableRows items="${keeperTeam1roster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                                <jsp:attribute name="rowTitle">
                                                    <tr align="center" class="rowTitle">
                                                        <td colspan="8">
                                                            <div><strong>${keeperMatchup.FSTeam1.teamName}</strong></div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="8"></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowHeader">
                                                    <tr class="rowHeader">
                                                        <td width="20%" align="center"><strong>POS</strong></td>
                                                        <td width="40%" align="center"><strong>PLAYER</strong></td>
                                                        <td width="20%" align="center"><strong>TEAM</strong></td>
                                                        <td width="20%" align="center"><strong>FANT PTS</strong></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowData">
                                                    <c:set var="highClass" value="rowData" />
                                                    <c:if test="${roster.activeState == 'inactive'}">
                                                        <c:set var="highClass" value="rowDataRed" />
                                                        <c:set var="highlightRow1" value="rowDataRed" />
                                                    </c:if>
                                                    <c:if test="${roster.starterState == 'starter'}">
                                                        <c:set var="highClass" value="rowDataYellow" />
                                                        <c:set var="highlightRow1" value="rowDataYellow" />
                                                    </c:if>
                                                    <tr ${highlightRow1} class="${highClass}">
                                                        <td align="center"><c:out value="${roster.player.position.positionName}" /></td>
                                                        <td align="center"><ct:player player="${roster.player}" displayStatsLink="true" /></td>
                                                        <td align="center"><c:out value="${roster.player.team.teamName}" /></td>
                                                        <td align="center"><fmt:formatNumber value="${roster.footballStats.fantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowEmpty">
                                                    <tr>
                                                        <td colspan="8">This team has no active roster for this week.</td>
                                                    </tr>
                                                </jsp:attribute>
                                            </ct:tableRows>
                                        </table>
                                        <!-- End Team 1 -->
                                    </td>
                                    <td width="14%"></td>
                                    <td width="43%">
                                        <!-- Team 2 -->
                                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                            <ct:tableRows items="${keeperTeam2roster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                                <jsp:attribute name="rowTitle">
                                                    <tr align="center" class="rowTitle">
                                                        <td colspan="8">
                                                            <div><strong>${keeperMatchup.FSTeam2.teamName}</strong></div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="8"></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowHeader">
                                                    <tr class="rowHeader">
                                                        <td width="20%" align="center"><strong>POS</strong></td>
                                                        <td width="40%" align="center"><strong>PLAYER</strong></td>
                                                        <td width="20%" align="center"><strong>TEAM</strong></td>
                                                        <td width="20%" align="center"><strong>FANT PTS</strong></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowData">
                                                    <c:set var="highClass" value="rowData" />
                                                    <c:if test="${roster.activeState == 'inactive'}">
                                                        <c:set var="highClass" value="rowDataRed" />
                                                        <c:set var="highlightRow1" value="rowDataRed" />
                                                    </c:if>
                                                    <c:if test="${roster.starterState == 'starter'}">
                                                        <c:set var="highClass" value="rowDataYellow" />
                                                        <c:set var="highlightRow1" value="rowDataYellow" />
                                                    </c:if>
                                                    <tr ${highlightRow1} class="${highClass}">
                                                        <td align="center"><c:out value="${roster.player.position.positionName}" /></td>
                                                        <td align="center"><ct:player player="${roster.player}" displayStatsLink="true" /></td>
                                                        <td align="center"><c:out value="${roster.player.team.teamName}" /></td>
                                                        <td align="center"><fmt:formatNumber value="${roster.footballStats.fantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowEmpty">
                                                    <tr>
                                                        <td colspan="8">This team has no active roster for this week.</td>
                                                    </tr>
                                                </jsp:attribute>
                                            </ct:tableRows>
                                        </table>
                                        <!-- End Team 2 -->
                                    </td>
                                </tr>
                            </table>
                        </c:if>
                        <!-- Salary Cap Roster -->
                        <c:if test="${salRoster != null}">
                            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr>
                                    <td colspan="5" align="center" class="rowTitleGreen"><h1>Salary Cap</h1></td>
                                </tr>
                                <tr>
                                    <td colspan="3"><img src="../images/spacer.gif" height="10" /></td>
                                </tr>
                                <tr>
                                    <td colspan="3" align="center" class="rowTitleGreen">
                                        <h2>&#160;&#160;Wk #${tenmanMatchup.FSSeasonWeek.FSSeasonWeekNo}&#160;&#160;</h2>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3"><img src="../images/spacer.gif" height="10" /></td>
                                </tr>
                                <tr>
                                    <td width="43%">
                                        <!-- Team 1 -->
                                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                            <ct:tableRows items="${salRoster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                                <jsp:attribute name="rowHeader">
                                                    <tr class="rowHeader">
                                                        <td width="20%" align="center"><strong>POS</strong></td>
                                                        <td width="40%" align="center"><strong>PLAYER</strong></td>
                                                        <td width="20%" align="center"><strong>TEAM</strong></td>
                                                        <td width="20%" align="center"><strong>FANT PTS</strong></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowData">
                                                    <c:set var="highClass" value="rowData" />
                                                    <c:if test="${roster.activeState == 'inactive'}">
                                                        <c:set var="highClass" value="rowDataRed" />
                                                        <c:set var="highlightRow1" value="rowDataRed" />
                                                    </c:if>
                                                    <c:if test="${roster.starterState == 'starter'}">
                                                        <c:set var="highClass" value="rowDataYellow" />
                                                        <c:set var="highlightRow1" value="rowDataYellow" />
                                                    </c:if>
                                                    <tr ${highlightRow1} class="${highClass}">
                                                        <td align="center"><c:out value="${roster.player.position.positionName}" /></td>
                                                        <td align="center"><ct:player player="${roster.player}" displayStatsLink="true" /></td>
                                                        <td align="center"><c:out value="${roster.player.team.teamName}" /></td>
                                                        <td align="center"><fmt:formatNumber value="${roster.footballStats.fantasyPts}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                                    </tr>
                                                </jsp:attribute>
                                                <jsp:attribute name="rowEmpty">
                                                    <tr>
                                                        <td colspan="8">This team has no active roster for this week.</td>
                                                    </tr>
                                                </jsp:attribute>
                                            </ct:tableRows>
                                        </table>
                                        <!-- End Team 1 -->
                                    </td>
                                    <td width="14%"></td>
                                    <td width="43%"></td>
                                </tr>
                            </table>
                        </c:if>

                        <span class="rowDataYellow">Starter</span><br />
                        <span class="rowDataRed">Inactive</span>

                    </div> <!-- content -->

                </div> <!-- left column -->
                <div class="rightcolumn">
                    
                    <jsp:include page="../inc_rightsidebar.jsp" />
                    
                </div> <!-- right column -->
            </div> <!-- main -->
        </div> <!-- colmask outside -->

        <jsp:include page="../inc_footer.jsp" />
        
    </div> <!-- container -->
</body>
</html>
