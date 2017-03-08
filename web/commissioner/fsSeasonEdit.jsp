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
        <title>FSSeason Edit</title>
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
                    <div align="center" class="rowTitle"><strong><h2>FSSEASON : ${commFSSeason.seasonName}</h2></strong></div>
                    
                    <!-- FSSeason Info -->
                    <div align="center">
                        <form method="post" action="fsSeasonEdit.do">
                            <table width="40%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr class="rowData">
                                    <td align="right">FSSeasonID : </td>
                                    <td align="left"><c:out value="${commFSSeason.FSSeasonID}" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">FSGameID : </td>
                                    <td align="left"><input type="text" name="fsGameID" id="fsGameID" value="<c:out default="" value="${commFSSeason.FSGameID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">SeasonID : </td>
                                    <td align="left"><input type="text" name="seasonID" id="seasonID" value="<c:out default="" value="${commFSSeason.seasonID}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">SeasonName : </td>
                                    <td align="left"><input type="text" name="seasonName" id="seasonName" value="<c:out default="" value="${commFSSeason.seasonName}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">IsActive : </td>
                                    <td align="left"><input type="text" name="isActive" id="isActive" value="<c:out default="" value="${commFSSeason.isActive}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">DisplayTeams : </td>
                                    <td align="left"><input type="text" name="displayTeams" id="displayTeams" value="<c:out default="" value="${commFSSeason.displayTeams}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">DisplayStatsYear : </td>
                                    <td align="left"><input type="text" name="displayStatsYear" id="displayStatsYear" value="<c:out default="" value="${commFSSeason.displayStatsYear}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">CurrentFSSeasonWeekID : </td>
                                    <td align="left"><input type="text" name="currentFSSeasonWeekID" id="currentFSSeasonWeekID" value="<c:out default="" value="${commFSSeason.currentFSSeasonWeekID}" />" /></td>
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
