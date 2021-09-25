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

                <div id="leagueRoster">
                    <div id="innerLeagueRoster">

                        <!-- IR Roster -->
                        <c:if test="${!empty irRoster}">
                            <table>
                                <tds:tableRows items="${irRoster}" var="roster" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                    <jsp:attribute name="rowTitle">
                                        <tr class="rowTitle">
                                            <td colspan="5">Players on IR</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowHeader">
                                        <tr class="rowHeader">
                                            <td>Owner</td>
                                            <td>Pos</td>
                                            <td>Team</td>
                                            <td>Player</td>
                                            <td>Status</td>
                                            <td>Added Week</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <c:set var="weekOnIR" value="${tds:getWeekPutOnIR(roster.FSTeam.FSLeagueID, roster.FSTeam.FSTeamID, roster.playerID)}" />
                                        <tr ${highlightRow1} class="rowData">
                                            <td><c:out value="${roster.FSTeam.teamName}" /></td>
                                            <td><c:out value="${roster.player.position.positionName}" /></td>
                                            <td><c:out value="${roster.player.team.abbreviation}" /></td>
                                            <td><tds:player player="${roster.player}" displayStatsLink="true" displayInjury="true" /></td>
                                            <td><c:out value="${roster.activeState}" /></td>
                                            <td><c:out value="${weekOnIR.FSSeasonWeekNo}" /></td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>
                            </table>
                        </c:if>
                    </div> <!-- inner League Roster -->
                </div> <!-- league Roster -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->
  </body>
</html>
