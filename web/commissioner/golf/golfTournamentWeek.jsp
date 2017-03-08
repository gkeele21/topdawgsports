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
        <title>TopDawgSports - PGA Tournament Week</title>
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
                    <div align="center" class="rowTitle"><strong><h2>PGA Tournament Week : ${commFSSeason.seasonName}</h2></strong></div>
                    <br />
                    
                    <!-- PGATournamentWeek Info -->
                    <div align="center">
                        <table width="80%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                            <tr class="rowData2">
                                <td colspan="4" align="right"><!--<a href="fsSeasonEdit.jsp?fsSeasonID=${commFSSeason.FSSeasonID}" ><img src="../../images/edit-icon.png" height="14"/></a>--></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">PGATournamentID : </td><td align="center"><c:out value="${commGolfTournamentWeek.PGATournamentID}" /></td>
                                <td align="right">Name : </td><td align="center"><c:out value="${commGolfTournamentWeek.PGATournament.tournamentName}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">FSSeasonWeekID : </td><td align="center"><c:out value="${commGolfTournamentWeek.FSSeasonWeekID}" /></td>
                                <td align="right">Week # : </td><td align="center"><c:out value="${commGolfTournamentWeek.FSSeasonWeek.FSSeasonWeekNo}" /></td>
                            </tr>
                        </table>
                    </div>
                    
                    <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                        <tds:tableRows items="${tournamentField}" var="weekPlayer" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowInfo">
                            </jsp:attribute>
                            <jsp:attribute name="rowTitle">
                                <tr align="center" class="rowTitle">
                                    <td colspan="7">
                                        <div><strong><h2>FIELD</h2></strong></div><br />
                                    </td>
                                    <td><a href="golfTournamentWeekFieldEdit.htm"><img width="24px" src="../../images/edit-icon.png"/></a></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td width="30%" align="center"><strong>Name</strong></td>
                                    <td width="20%" align="center"><strong>Value</strong></td>
                                    <td width="10%" align="center"><strong>Rank</strong></td>
                                    <td width="15%" align="center"><strong>$ Earned</strong></td>
                                    <td width="15%" align="center"><strong>Final Score</strong></td>
                                    <td width="10%" align="center"><strong>Rel to Par</strong></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <tr ${highlightRow1} class="rowData">
                                    <td align="center"><c:out value="${weekPlayer.player.firstName}" /> <c:out value="${weekPlayer.player.lastName}" /> (<c:out value="${weekPlayer.player.country.country}" />)</td>
                                    <td align="center"><fmt:formatNumber type="currency" maxFractionDigits="0" value="${weekPlayer.salaryValue}" /></td>
                                    <td align="center"><c:out value="${weekPlayer.tournamentRank}" /></td>
                                    <td align="center"><fmt:formatNumber type="currency" maxFractionDigits="0" value="${weekPlayer.moneyEarned}" /></td>
                                    <td align="center"><c:out value="${weekPlayer.finalScore}" /></td>
                                    <td align="center"><c:out value="${weekPlayer.relativeToPar}" /></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="10">There are no golfers in the field.</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowNavigation">
                                <c:set var="colspan" value="2" />
                                <tr><td height="10" colspan="8"><img src="../../images/spacer.gif" /></td></tr>
                                <tr>
                                    <td colspan="8">
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                            <tr class="rowData2">
                                                <td width="65" align="center"><ct:navFirst link="fan_viewFSSeasons.htm?fsgameid=${comm_fsgameid}">&#160;FIRST</ct:navFirst></td>
                                                <td width="65" align="center"><ct:navPrev link="fan_viewFSSeasons.htm?fsgameid=${comm_fsgameid}"><< PREV</ct:navPrev></td>
                                                <td width="260" align="center">&#160;${fromRows1} to ${toRows1} of ${totalRows1}</td>
                                                <td width="65" align="center"><ct:navNext link="fan_viewFSSeasons.htm?fsgameid=${comm_fsgameid}">NEXT >></ct:navNext></td>
                                                <td width="65" align="center"><ct:navLast link="fan_viewFSSeasons.htm?fsgameid=${comm_fsgameid}">LAST</ct:navLast></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </jsp:attribute>
                        </tds:tableRows>
                    </table>


                </div> <!-- main -->
            </div> <!-- colmask outside -->

            <jsp:include page="../inc_footer.jsp" />

        </div> <!-- container -->

    </body>
</html>
