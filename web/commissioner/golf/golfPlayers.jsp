<%-- 
    Document   : viewFSGames
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
        <link rel="stylesheet" type="text/css" href="../../css/styles.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../../css/frontpage.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="../../css/registration.css" media="screen" />
        <title>PGA Golfers</title>
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

                    <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                        <tds:tableRows items="${players}" var="player" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowInfo">
                            </jsp:attribute>
                            <jsp:attribute name="rowTitle">
                                <tr class="rowTitle">
                                    <td><a href="golfPlayerEdit.htm"><img width="24px;" src="../../images/plus.png" /></a></td>
                                    <td align="center" colspan="5">
                                        <div><strong><h2>PGA Players</h2></strong></div><br />
                                    </td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td width="8%" align="center"><strong>ID</strong></td>
                                    <td width="25%" align="center"><strong>First Name</strong></td>
                                    <td width="25%" align="center"><strong>Last Name</strong></td>
                                    <td width="8%" align="center"><strong>Country</strong></td>
                                    <td width="8%" align="center"><strong>Active</strong></td>
                                    <td width="12%" align="center"><strong>External ID</strong></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <tr ${highlightRow1} class="rowData">
                                    <td align="center"><c:out value="${player.playerID}" /></td>
                                    <td align="center">
                                        <a href="golfPlayerEdit.htm?playerID=${player.playerID}">
                                            <c:out value="${player.firstName}" />
                                        </a>
                                    </td>
                                    <td align="center"><c:out value="${player.lastName}" /></td>
                                    <td align="center"><c:out value="${player.country.country}" /></td>
                                    <td align="center"><c:out value="${player.isActive}" /></td>
                                    <td align="center"><c:out value="${player.statsPlayerID}" /></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="10">There are no Players to display.</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowNavigation">
                                <c:set var="colspan" value="2" />
                                <tr><td height="10" colspan="8">&#160;</td></tr>
                                <tr>
                                    <td colspan="8">
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                                <tr class="rowData2">
                                                    <td width="65" align="center"><ct:navFirst link="golfPlayers.htm">&#160;FIRST</ct:navFirst></td>
                                                    <td width="65" align="center"><ct:navPrev link="golfPlayers.htm"><< PREV</ct:navPrev></td>
                                                    <td width="260" align="center">&#160;${fromRows1} to ${toRows1} of ${totalRows1}</td>
                                                    <td width="65" align="center"><ct:navNext link="golfPlayers.htm">NEXT >></ct:navNext></td>
                                                    <td width="65" align="center"><ct:navLast link="golfPlayers.htm">LAST</ct:navLast></td>
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
