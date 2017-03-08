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
        <title>PGA Player Edit</title>
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
                    <div align="center" class="rowTitle"><strong><h2>PGA Player</h2></strong></div>
                    
                    <!-- Player Info -->
                    <div align="center">
                        <form method="post" action="golfPlayerEdit.do">
                            <table width="40%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr class="rowData">
                                    <td align="right">PlayerID : </td>
                                    <td align="left"><c:out value="${player.playerID}" /><input type="hidden" name="playerID" value="${player.playerID}" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">First Name : </td>
                                    <td align="left"><input type="text" name="firstName" id="firstName" value="<c:out default="" value="${player.firstName}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">Last Name : </td>
                                    <td align="left"><input type="text" name="lastName" id="lastName" value="<c:out default="" value="${player.lastName}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">CountryID: </td>
                                    <td align="left">
                                        <select name="countryID">
                                            <c:forEach items="${countries}" var="country"><c:choose><c:when test="${player.countryID == country.countryID}"><c:set var="selectMe" value="selected" /></c:when><c:otherwise><c:set var="selectMe" value="" /></c:otherwise></c:choose>
                                                <option value="${country.countryID}" ${selectMe}><c:out value="${country.country}"/></option>
                                            </c:forEach>
                                        </select>
                                    
                                    </td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">Stats ID: </td>
                                    <td align="left"><input type="text" name="externalID" id="externalID" value="<c:out default="" value="${player.statsPlayerID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">Active: </td>
                                    <td align="left"><input type="text" name="active" id="active" value="<c:out default="" value="${player.isActive}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td colspan="2" align="center"><br /><input type="submit" name="submit" value="Save" /></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    
                </div> <!-- main -->
            </div> <!-- colmask outside -->

            <jsp:include page="../inc_footer.jsp" />

        </div> <!-- container -->

    </body>
</html>
