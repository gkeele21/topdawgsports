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
    <style type="text/css">
        #weekNumLinks a.currWeek { color: #BF8339; font-size: 1.6em; text-decoration: none; }
    </style>
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

                <%-- Week Number Links --%>
                <div id="weekNumLinks">
                    <label>Week #</label>
                    <c:forEach items="${allWeeks}" var="week">
                        <a <c:if test="${week.FSSeasonWeekID == displayWeek.FSSeasonWeekID}">class="currWeek"</c:if>
                            href="transactions.htm?wk=${week.FSSeasonWeekID}">${week.FSSeasonWeekNo}
                        </a>
                    </c:forEach>
                </div>

                <div id="leagueTransactions">
                    <div id="innerLeagueTransactions">
                        <!-- Current Transactions -->
                        <table>
                            <tds:tableRows items="${currentTransactions}" var="transaction" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowTitle">
                                    <tr class="rowTitle">
                                        <td colspan="10">Transactions</td>
                                    </tr>
                                    <tr>
                                        <td colspan="10">for Week #${displayWeek.FSSeasonWeekNo}</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>DATE</td>
                                        <td>TEAM</td>
                                        <td>TYPE</td>
                                        <td>PLAYER</td>
                                        <td>POS</td>
                                        <td>TM</td>
                                        <td>TYPE</td>
                                        <td>PLAYER</td>
                                        <td>POS</td>
                                        <td>TM</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <tr ${highlightRow1} class="rowData">
                                        <td>
                                            <fmt:parseDate  value="${transaction.transactionDate}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="transactionDate" />
                                            <fmt:formatDate value="${transactionDate}" pattern="E MM/dd HH:mm" timeZone="America/Denver" />
                                        </td>
                                        <td><c:out value="${transaction.FSTeam.teamName}" /></td>
                                        <td><c:out value="${transaction.dropType}" /></td>
                                        <td><tds:player player="${transaction.dropPlayer}" displayStatsLink="true" /></td>
                                        <td><c:out value="${transaction.dropPlayer.position.positionName}" /></td>
                                        <td><c:out value="${transaction.dropPlayer.team.abbreviation}" /></td>
                                        <td><c:out value="${transaction.PUType}" /></td>
                                        <td><tds:player player="${transaction.PUPlayer}" displayStatsLink="true" /></td>
                                        <td><c:out value="${transaction.PUPlayer.position.positionName}" /></td>
                                        <td><c:out value="${transaction.PUPlayer.team.abbreviation}" /></td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowEmpty">
                                    <tr>
                                        <td colspan="10">There are no transactions for this week.</td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>

                    </div> <!-- inner League Transactions -->
                </div> <!-- league Transactions -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->
  </body>
</html>
