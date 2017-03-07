<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/styles.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../css/frontpage.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../css/registration.css" media="screen" />
        <title>TopDawgSports - FSTeam View</title>
    </head>
    <body>
        <div id="container">
        
            <jsp:include page="inc_header.jsp" />

            <jsp:include page="inc_mainmenu.jsp" />

            <%--<jsp:include page="../inc_submenu.jsp" />--%>

            <c:if test="${validUser != null}" >
                <jsp:include page="inc_errorMessage.jsp" />
            </c:if>

            <div class="colmask outside">

                <div class="main">

                    <h3>
                        <a href="fsLeagueView.htm">Back to League View</a>
                    </h3>
                    <br />
                    <jsp:include page="inc_gameMenu.jsp" />

                    <br />
                    <div align="center" class="rowTitle"><strong><h2>FSTEAM : ${commFSTeam.teamName}</h2></strong></div>
                    
                    <!-- FSTeam Info -->
                    <div align="center">
                        <table width="80%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                            <tr class="rowData2">
                                <td colspan="4" align="right"><a href="fsTeamEdit.jsp?fsTeamID=${commFSTeam.FSTeamID}" ><img src="../images/edit-icon.png" height="14"/></a></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">FSTeamID : </td><td align="center"><c:out value="${commFSTeam.FSTeamID}" /></td>
                                <td align="right">FSLeagueID : </td><td align="center"><c:out value="${commFSTeam.FSLeagueID}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">FSUserID : </td><td align="center"><c:out value="${commFSTeam.FSUserID}" /></td>
                                <td align="right">DateCreated : </td><td align="center"><c:out value="${commFSTeam.dateCreated}" /></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">TeamName : </td><td align="center"><c:out value="${commFSTeam.teamName}" /></td>
                                <td align="right">IsActive : </td><td align="center"><c:out value="${commFSTeam.isActive}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">Division : </td><td align="center"><c:out value="${commFSTeam.division}" /></td>
                                <td align="right">TeamNo : </td><td align="center"><c:out value="${commFSTeam.teamNo}" /></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">ScheduleTeamNo : </td><td align="center"><c:out value="${commFSTeam.scheduleTeamNo}" /></td>
                                <td align="right">LastAccessed : </td><td align="center"><c:out value="${commFSTeam.lastAccessed}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">RankDraftMode : </td><td align="center"><c:out value="${commFSTeam.rankDraftMode}" /></td>
                                <td align="right">IsAlive : </td><td align="center"><c:out value="${commFSTeam.isAlive}" /></td>
                            </tr>
                        </table>
                    </div>

                    <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                        <tr class="rowData2" align="center">
                            <td width="100%" colspan="10">Select Week # <c:forEach items="${commAllWeeks}" var="week">
                                <a class="currWeek" href="fsTeamView.htm?weekid=${week.FSSeasonWeekID}">${week.FSSeasonWeekNo}
                                </a>&#160;
                            </c:forEach></td>
                        </tr>
                        <tr class="rowData2">
                            <td>&#160;</td>
                        </tr>
                        <tds:tableRows items="${commFSRoster}" var="fsRoster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowInfo">

                            </jsp:attribute>
                            <jsp:attribute name="rowTitle">
                                <tr align="center" class="rowTitle">
                                    <td colspan="8">
                                        <div><strong><h2>Roster for Week #<c:out value="${commFSSeasonWeek.FSSeasonWeekNo}" /> 
                                                    (<c:out value="${commFSSeasonWeek.FSSeasonWeekID}" />)</h2></strong></div><br />
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="8"><a href="fsRosterInsert.htm" ><img src="../images/plus.png" height="14"/></a>
                                    </td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td width="8%" align="center"><strong>FSRosterID</strong></td>
                                    <td width="8%" align="center"><strong>POS</strong></td>
                                    <td width="12%" align="center"><strong>Player</strong></td>
                                    <td width="14%" align="center"><strong>Team</strong></td>
                                    <td width="8%" align="center"><strong>StarterState</strong></td>
                                    <td width="6%" align="center"><strong>ActiveState</strong></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <tr ${highlightRow1} class="rowData">
                                    <td align="center"><c:out value="${fsRoster.ID}" /><a href="fsRosterEdit.htm?fsRosterID=${fsRoster.ID}" ><img src="../images/edit-icon.png" height="14"/></a></td>
                                    <td align="center"><c:out value="${fsRoster.player.position.positionName}" /></td>
                                    <td align="center"><c:out value="${fsRoster.player.firstName} ${fsRoster.player.lastName}" /></td>
                                    <td align="center"><c:out value="${fsRoster.player.team.displayName}" /></td>
                                    <td align="center"><c:out value="${fsRoster.starterState}" /></td>
                                    <td align="center"><c:out value="${fsRoster.activeState}" /></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="10">There are no players to display.</td>
                                </tr>
                            </jsp:attribute>
                        </tds:tableRows>
                    </table>


                </div> <!-- main -->
            </div> <!-- colmask outside -->

            <jsp:include page="inc_footer.jsp" />

        </div> <!-- container -->

    </body>
</html>
