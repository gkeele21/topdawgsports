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

                <div id="freeAgents">
                    <div id="innerFreeAgents">

                        <jsp:include page="../inc_errorMessage.jsp" />
                        
                        <!-- DROP Player -->
                        <table>
                            <tr class="rowTitle">
                                <td colspan="7">Drop</td>
                            </tr>
                            <tr class="rowHeader">
                                <td>Action</td>
                                <td>Pos</td>
                                <td>Player</td>
                                <td>Team</td>
                                <td>Ftsy Pts</td>
                                <td>Avg</td>
                                <td>Opp</td>
                            </tr>
                            <tr class="rowData">
                                <c:set var="game" value="${tds:getGame(fantasyCurrentWeek.seasonWeekID,faTransaction.dropPlayer.team.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,faTransaction.dropPlayer.team.teamID)}" />

                                <td><c:out value="${faTransaction.dropType}" /></td>
                                <td><c:out value="${faTransaction.dropPlayer.position.positionName}" /></td>
                                <td><tds:player player="${faTransaction.dropPlayer}" displayStatsLink="true" displayInjury="true" /></td>
                                <td><c:out value="${faTransaction.dropPlayer.team.abbreviation}" /></td>
                                <td><fmt:formatNumber value="${faTransaction.dropPlayer.totalFootballStats.fantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                <td><fmt:formatNumber value="${faTransaction.dropPlayer.totalFootballStats.avgFantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                <td><c:out value="${opp}" /></td>
                            </tr>
                        </table>

                        <!-- Pickup Player -->
                        <table>
                            <tr class="rowTitle">
                                <td colspan="7">Pickup</td>
                            </tr>
                            <tr class="rowHeader">
                                <td>Action</td>
                                <td>Pos</td>
                                <td>Player</td>
                                <td>Team</td>
                                <td>Ftsy Pts</td>
                                <td>Avg</td>
                                <td>Opp</td>
                            </tr>
                            <tr class="rowData">
                                <c:set var="game" value="${tds:getGame(fantasyCurrentWeek.seasonWeekID,faTransaction.PUPlayer.team.teamID)}" />
                                <c:set var="opp" value="${tds:getOpponentString(game,faTransaction.PUPlayer.team.teamID)}" />

                                <td><c:out value="${faTransaction.PUType}" /></td>
                                <td><c:out value="${faTransaction.PUPlayer.position.positionName}" /></td>
                                <td><tds:player player="${faTransaction.PUPlayer}" displayStatsLink="true" displayInjury="true" /></td>
                                <td><c:out value="${faTransaction.PUPlayer.team.abbreviation}" /></td>
                                <td><fmt:formatNumber value="${faTransaction.PUPlayer.totalFootballStats.fantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                <td><fmt:formatNumber value="${faTransaction.PUPlayer.totalFootballStats.avgFantasyPts}" minFractionDigits="1" maxFractionDigits="1" /></td>
                                <td><c:out value="${opp}" /></td>
                            </tr>
                        </table>

                        <br />

                        <c:if test="${afterTransDeadline}">
                            <div id="error">It is already passed the deadline to make any free agent pickups.</div>
                        </c:if>
                        <c:if test="${!afterStartersDeadline}">
                            <form method="post" action="faConfirm.do">
                                <label>Do you want to proceed with the Transaction above?</label>
                                <input type="image" src="../images/button_cancel.gif" name="cancel" />
                                <input type="image" src="../images/button_accept.gif" name="accept" id="testAccept" value="yo" />
                            </form>
                        </c:if>
                            
                    </div> <!-- inner free agents -->
                </div> <!-- free agent -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->

  </body>
</html>