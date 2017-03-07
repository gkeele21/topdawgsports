<%-- 
    Document   : viewFSSeasons
    Created on : Aug 27, 2009, 10:53:43 PM
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
        <link rel="stylesheet" type="text/css" href="../css/styles.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../css/frontpage.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../css/registration.css" media="screen" />
        <title>FSSeasons</title>
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

                    <br />
                    <div align="center" class="rowTitle"><strong><h2>FSGAME : ${commFSGame.gameName}</h2></strong></div>
                    
                    <!-- FSGame Info -->
                    <div align="center">
                        <table width="80%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                            <tr class="rowData2">
                                <td colspan="4" align="right"><a href="fsGameEdit.jsp?fsGameID=${commFSGame.FSGameID}" ><img src="../images/edit-icon.png" height="14"/></a></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">FSGameID : </td><td align="center"><c:out value="${commFSGame.FSGameID}" /></td>
                                <td align="right">SportID : </td><td align="center"><c:out value="${commFSGame.sportID}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">GameName : </td><td align="center"><c:out value="${commFSGame.gameName}" /></td>
                                <td align="right">GameNameShort : </td><td align="center"><c:out value="${commFSGame.gameNameShort}" /></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">ScoringStyle : </td><td align="center"><c:out value="${commFSGame.scoringStyle}" /></td>
                                <td align="right">GroupingStyle : </td><td align="center"><c:out value="${commFSGame.groupingStyle}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">CurrentFSSeasonID : </td><td align="center"><c:out value="${commFSGame.currentFSSeasonID}" /></td>
                                <td align="right">DisplayName : </td><td align="center"><c:out value="${commFSGame.displayName}" /></td>
                            </tr>
                            <tr class="rowData">
                                <td align="right">GamePrefix : </td><td align="center"><c:out value="${commFSGame.gamePrefix}" /></td>
                            </tr>
                            <tr class="rowData2">
                                <td align="right">HomeURL : </td><td align="center"><c:out value="${commFSGame.homeURL}" /></td>
                                <td align="right">HomeURLShort : </td><td align="center"><c:out value="${commFSGame.homeURLShort}" /></td>
                            </tr>
                        </table>
                    </div>
                    
                    <!-- FSSeasons -->
                    <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                        <tds:tableRows items="${fsSeasons}" var="fsSeason" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowInfo">
                                
                            </jsp:attribute>
                            <jsp:attribute name="rowTitle">
                                <tr align="center" class="rowTitle">
                                    <td colspan="8">
                                        <div><strong><h2>FSSeasons</h2></strong></div><br />
                                        <c:if test="${comm_fsgameid > 0}">
                                            (For FSGameID ${comm_fsgameid}) - <a href="fan_viewFSSeasons.htm?fsgameid=0">View All</a>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="8">
                                    </td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td width="8%" align="center"><strong>FSSeasonID</strong></td>
                                    <td width="8%" align="center"><strong>FSGameID</strong></td>
                                    <td width="8%" align="center"><strong>SeasonID</strong></td>
                                    <td width="25%" align="center"><strong>SeasonName</strong></td>
                                    <td width="8%" align="center"><strong>IsActive</strong></td>
                                    <td width="12%" align="center"><strong>DisplayTeams</strong></td>
                                    <td width="15%" align="center"><strong>DisplayStatsYear</strong></td>
                                    <td width="15%" align="center"><strong>CurrentFSSeasonWeekID</strong></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <tr ${highlightRow1} class="rowData">
                                    <td align="center"><c:out value="${fsSeason.FSSeasonID}" /></td>
                                    <td align="center"><c:out value="${fsSeason.FSGameID}" /></td>
                                    <td align="center"><c:out value="${fsSeason.seasonID}" /></td>
                                    <td align="center">
                                        <a href="fsSeasonView.htm?fsSeasonID=${fsSeason.FSSeasonID}">
                                        <c:out value="${fsSeason.seasonName}" /></a>
                                    </td>
                                    <td align="center"><c:out value="${fsSeason.isActive}" /></td>
                                    <td align="center"><c:out value="${fsSeason.displayTeams}" /></td>
                                    <td align="center"><c:out value="${fsSeason.displayStatsYear}" /></td>
                                    <td align="center"><c:out value="${fsSeason.currentFSSeasonWeekID}" /></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="10">There are no FSSeasons to display.</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowNavigation">
                                <c:set var="colspan" value="2" />
                                <tr><td height="10" colspan="8"><img src="../images/spacer.gif"></td></tr>
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

            <jsp:include page="inc_footer.jsp" />

        </div> <!-- container -->

    </body>
</html>
