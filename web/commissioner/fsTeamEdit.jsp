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
        <title>FSTeam Edit</title>
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
                    <div align="center" class="rowTitle"><strong><h2>FSTEAM : ${commFSTeam.teamName}</h2></strong></div>

                    <!-- FSTeam Info -->
                    <div align="center">
                        <form method="post" action="fsTeamEdit.do">
                            <table width="40%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr class="rowData">
                                    <td align="right">FSTeamID : </td>
                                    <td align="left"><c:out value="${commFSTeam.FSTeamID}" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">FSLeagueID : </td>
                                    <td align="left"><input type="text" name="fsLeagueID" id="fsLeagueID" value="<c:out default="" value="${commFSTeam.FSLeagueID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">FSUserID : </td>
                                    <td align="left"><input type="text" name="fsUserID" id="fsUserID" value="<c:out default="" value="${commFSTeam.FSUserID}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">DateCreated : </td>
                                    <td align="left">
                                        <fmt:parseDate  value="${commFSTeam.dateCreated}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="dateCreated" />
                                        <input type="text" name="dateCreated" id="dateCreated" value="<fmt:formatDate value="${dateCreated}" pattern="yyyy-MM-dd" timeZone="America/Denver" />" />
                                    </td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">TeamName : </td>
                                    <td align="left"><input type="text" name="teamName" id="teamName" value="<c:out default="" value="${commFSTeam.teamName}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">IsActive : </td>
                                    <td align="left"><input type="text" name="isActive" id="isActive" value="<c:out default="" value="${commFSTeam.isActive}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">Division : </td>
                                    <td align="left"><input type="text" name="division" id="division" value="<c:out default="" value="${commFSTeam.division}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">TeamNo : </td>
                                    <td align="left"><input type="text" name="teamNo" id="teamNo" value="<c:out default="" value="${commFSTeam.teamNo}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">ScheduleTeamNo : </td>
                                    <td align="left"><input type="text" name="scheduleTeamNo" id="scheduleTeamNo" value="<c:out default="" value="${commFSTeam.scheduleTeamNo}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">LastAccessed : </td>
                                    <td align="left">
                                        <fmt:parseDate  value="${commFSTeam.lastAccessed}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="lastAccessed" />
                                        <input type="text" name="lastAccessed" id="lastAccessed" value="<fmt:formatDate value="${lastAccessed}" pattern="yyyy-MM-dd" timeZone="America/Denver" />" />
                                    </td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">RankDraftMode : </td>
                                    <td align="left"><input type="text" name="rankDraftMode" id="rankDraftMode" value="<c:out default="" value="${commFSTeam.rankDraftMode}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">IsAlive : </td>
                                    <td align="left"><input type="text" name="isAlive" id="isAlive" value="<c:out default="" value="${commFSTeam.isAlive}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td colspan="2" align="center"><br /><input type="submit" name="submit" value="Update" /></td>
                                </tr>
                            </table>
                        </form>
                    </div>

                </div> <!-- main -->
            </div> <!-- colmask outside -->

            <jsp:include page="inc_footer.jsp" />

        </div> <!-- container -->

    </body>
</html>
