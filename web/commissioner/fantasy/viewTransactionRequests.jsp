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
        <link rel="stylesheet" type="text/css" href="../..css/frontpage.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../..css/registration.css" media="screen" />
        <title>TopDawgSports Commissioner - Transaction Requests View</title>
    </head>
    <body>
        <div id="container">
        
            <jsp:include page="../inc_header.jsp" />

            <jsp:include page="../inc_mainmenu.jsp" />

            <%--<jsp:include page="../inc_submenu.jsp" />--%>

            <div class="colmask outside">

                <div class="main">

                    <br />
                    <div align="center" class="rowTitle">(switch weeks)</div>
                    <br />
                    
                    <jsp:include page="../inc_gameMenu.jsp" />
                    
                    <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                        <tds:tableRows items="${requests}" var="request" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowInfo">

                            </jsp:attribute>
                            <jsp:attribute name="rowTitle">
                                <tr align="center" class="rowTitle">
                                    <td colspan="8">
                                        <div><strong><h2>Transaction Requests for Week #${commFSSeasonWeek.FSSeasonWeekNo} (${commFSSeasonWeekID})</h2></strong></div><br />
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="8">
                                    </td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td align="center"><strong>FSTeamID</strong></td>
                                    <td align="center"><strong>Team Name</strong></td>
                                    <td align="center"><strong>Drop Player</strong></td>
                                    <td align="center"><strong>POS</strong></td>
                                    <td align="center"><strong>TM</strong></td>
                                    <td align="center"><strong>Drop Type</strong></td>
                                    <td align="center"><strong>Pickup Player</strong></td>
                                    <td align="center"><strong>POS</strong></td>
                                    <td align="center"><strong>TM</strong></td>
                                    <td align="center"><strong>Pickup Type</strong></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <tr ${highlightRow1} class="rowData">
                                    <td align="center"><c:out value="${request.FSTeamID}" /></td>
                                    <td align="center"><c:out value="${request.FSTeam.teamName}" /></td>
                                    <td align="center"><c:out value="${request.dropPlayer.firstName} ${request.dropPlayer.lastName}" /></td>
                                    <td align="center"><c:out value="${request.dropPlayer.team.abbreviation}" /></td>
                                    <td align="center"><c:out value="${request.dropPlayer.position.positionName}" /></td>
                                    <td align="center"><c:out value="${request.dropType}" /></td>
                                    <td align="center"><c:out value="${request.PUPlayer.firstName} ${request.PUPlayer.lastName}" /></td>
                                    <td align="center"><c:out value="${request.PUPlayer.team.abbreviation}" /></td>
                                    <td align="center"><c:out value="${request.PUPlayer.position.positionName}" /></td>
                                    <td align="center"><c:out value="${request.PUType}" /></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="10">There are no Requests to display.</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowNavigation">
                                <c:set var="colspan" value="2" />
                                <tr><td height="10" colspan="8"><img src="../..images/spacer.gif" /></td></tr>
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
