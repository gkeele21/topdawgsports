<%-- 
    Document   : golfTournamentEdit
    Created on : Feb 7, 2015, 10:53:43 PM
    Author     : admin
--%>

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
        <link rel="stylesheet" type="text/css" href="../../css/styles.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../../css/frontpage.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../../css/registration.css" media="screen" />
        <title>PGA Tournament Edit</title>
    </head>
    <body>
        <div id="container">
        
            <jsp:include page="../inc_header.jsp" />

            <jsp:include page="../inc_mainmenu.jsp" />

            <%--<jsp:include page="../inc_submenu.jsp" />--%>

            <c:if test="${validUser != null}" >
                <jsp:include page="../inc_errorMessage.jsp" />
            </c:if>

            <div class="colmask outside">

                <div class="main">

                    <jsp:include page="../inc_golfMenu.jsp" />
                    
                    <br />
                    <div align="center" class="rowTitle"><strong><h2>PGA Tournament : ${tournament.tournamentName}</h2></strong></div>
                    
                    <!-- PGA Tournament Info -->
                    <div align="center">
                        <form method="post" action="golfTournamentWeekFieldEdit.do">
                            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tds:tableRows items="${tournamentField}" var="weekPlayer" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                    <jsp:attribute name="rowInfo">
                                    </jsp:attribute>
                                    <jsp:attribute name="rowTitle">
                                        <tr align="center" class="rowTitle">
                                            <td colspan="7">
                                                <div><strong><h2>FIELD</h2></strong></div><br />
                                            </td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowHeader">
                                        <tr class="rowHeader">
                                            <td width="5%" align="center"><strong>DEL</strong></td>
                                            <td width="25%" align="center"><strong>Name</strong></td>
                                            <td width="10%" align="center"><strong>WGR</strong></td>
                                            <td width="15%" align="center"><strong>Value</strong></td>
                                            <td width="10%" align="center"><strong>Rank</strong></td>
                                            <td width="15%" align="center"><strong>$ Earned</strong></td>
                                            <td width="15%" align="center"><strong>Final Score</strong></td>
                                            <td width="15%" align="center"><strong>Rel To Par</strong></td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <tr ${highlightRow1} class="rowData">
                                            <td align="center"><input type="checkbox" name="del" value="${weekPlayer.ID}" /></td>
                                            <td align="center"><c:out value="${weekPlayer.player.firstName}" /> <c:out value="${weekPlayer.player.lastName}" /> (<c:out value="${weekPlayer.player.country.country}" />)</td>
                                            <td align="center"><input type="text" name="wgr_${weekPlayer.ID}" size="3" value="<fmt:formatNumber groupingUsed="false" maxFractionDigits='0' value='${weekPlayer.worldGolfRanking}' />" /></td>
                                            <td align="center"><input type="text" name="sal_${weekPlayer.ID}" size="5" value="<fmt:formatNumber groupingUsed="false" maxFractionDigits='0' value='${weekPlayer.salaryValue}' />" /></td>
                                            <td align="center"><input type="text" name="rank_${weekPlayer.ID}" size="3" value="${weekPlayer.tournamentRank}" /></td>
                                            <td align="center"><input type="text" name="earned_${weekPlayer.ID}" size="5" value="<fmt:formatNumber groupingUsed="false" maxFractionDigits='0' value='${weekPlayer.moneyEarned}' />" /></td>
                                            <td align="center"><input type="text" name="final_${weekPlayer.ID}" size="3" value="${weekPlayer.finalScore}" /></td>
                                            <td align="center"><input type="text" name="rel_${weekPlayer.ID}" size="3" value="<fmt:formatNumber groupingUsed="false" maxFractionDigits='0' value='${weekPlayer.relativeToPar}' />" /></td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowEmpty">
                                        <tr>
                                            <td colspan="10">There are no golfers in the field.</td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>
                            </table>
                            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr colspan="7">
                                    <td colspan="2" align="center"><input type="submit" name="submit" value="Save" /></td>
                                </tr>
                            </table>
                            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tds:tableRows items="${possiblePlayers}" var="player" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                    <jsp:attribute name="rowInfo">
                                    </jsp:attribute>
                                    <jsp:attribute name="rowTitle">
                                        <tr align="center" class="rowTitle">
                                            <td colspan="7">
                                                <div><strong><h2>POSSIBLE PLAYERS</h2></strong></div><br />
                                            </td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowHeader">
                                        <tr class="rowHeader">
                                            <td width="10%" align="center"><strong>ADD</strong></td>
                                            <td width="20%" align="center"><strong>Name</strong></td>
                                            <td width="70%">&#160;</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <tr ${highlightRow1} class="rowData">
                                            <td align="center"><input type="checkbox" name="add" value="${player.playerID}" /></td>
                                            <td align="center"><c:out value="${player.firstName}" /> <c:out value="${player.lastName}" /> (<c:out value="${player.country.country}" />)</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowEmpty">
                                        <tr>
                                            <td colspan="10">There are no golfers to display.</td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>
                            </table>
                        </form>
                    </div>
                    
                </div> <!-- main -->
            </div> <!-- colmask outside -->

            <jsp:include page="../inc_footer.jsp" />

        </div> <!-- container -->

    </body>
</html>
