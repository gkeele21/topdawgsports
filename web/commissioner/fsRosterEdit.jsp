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
        <title>FSRoster Edit</title>
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
                    <div align="center" class="rowTitle"><strong><h2>FSRoster : ${commFSRoster.ID}</h2></strong></div>
                    
                    <!-- FSRoster Info -->
                    <div align="center">
                        <form method="post" action="fsRosterEdit.do">
                            <table width="40%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr class="rowData">
                                    <td align="right">FSRosterID : </td>
                                    <td align="left"><c:out value="${commFSRoster.ID}" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">FSTeamID : </td>
                                    <td align="left"><input type="text" name="fsTeamID" id="fsTeamID" value="<c:out default="" value="${commFSRoster.FSTeamID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">Player : </td>
                                    <td align="left"><tds:listbox items="${playerList}" name="playerID" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">FSSeasonWeekID : </td>
                                    <td align="left"><input type="text" name="fsSeasonWeekID" id="fsSeasonWeekID" value="<c:out default="" value="${commFSRoster.FSSeasonWeekID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">StarterState : </td>
                                    <td align="left"><input type="text" name="starterState" id="starterState" value="<c:out default="" value="${commFSRoster.starterState}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">ActiveState : </td>
                                    <td align="left"><input type="text" name="activeState" id="activeState" value="<c:out default="" value="${commFSRoster.activeState}" />" /></td>
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
