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
        <title>FSLeague Edit</title>
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
                    <div align="center" class="rowTitle"><strong><h2>FSLEAGUE : ${commFSLeague.leagueName}</h2></strong></div>
                    
                    <!-- FSLeague Info -->
                    <div align="center">
                        <form method="post" action="fsLeagueEdit.do">
                            <table width="40%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr class="rowData">
                                    <td align="right">FSLeagueID : </td>
                                    <td align="left"><c:out value="${commFSLeague.FSLeagueID}" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">FSSeasonID : </td>
                                    <td align="left"><input type="text" name="fsSeasonID" id="fsSeasonID" value="<c:out default="" value="${commFSLeague.FSSeasonID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">LeagueName : </td>
                                    <td align="left"><input type="text" name="leagueName" id="leagueName" value="<c:out default="" value="${commFSLeague.leagueName}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">LeaguePassword : </td>
                                    <td align="left"><input type="text" name="leaguePassword" id="leaguePassword" value="<c:out default="" value="${commFSLeague.leaguePassword}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">IsFull : </td>
                                    <td align="left"><input type="text" name="isFull" id="isFull" value="<c:out default="" value="${commFSLeague.isFull}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">IsPublic : </td>
                                    <td align="left"><input type="text" name="isPublic" id="isPublic" value="<c:out default="" value="${commFSLeague.isPublic}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">NumTeams : </td>
                                    <td align="left"><input type="text" name="numTeams" id="numTeams" value="<c:out default="" value="${commFSLeague.numTeams}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">Description : </td>
                                    <td align="left"><input type="text" name="description" id="description" value="<c:out default="" value="${commFSLeague.description}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">IsGeneral : </td>
                                    <td align="left"><input type="text" name="isGeneral" id="isGeneral" value="<c:out default="" value="${commFSLeague.isGeneral}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">StartFSSeasonWeekID : </td>
                                    <td align="left"><input type="text" name="startFSSeasonWeekID" id="startFSSeasonWeekID" value="<c:out default="" value="${commFSLeague.startFSSeasonWeekID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">VendorID : </td>
                                    <td align="left"><input type="text" name="vendorID" id="vendorID" value="<c:out default="" value="${commFSLeague.vendorID}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">DraftType : </td>
                                    <td align="left"><input type="text" name="draftType" id="draftType" value="<c:out default="" value="${commFSLeague.draftType}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">DraftDate : </td>
                                    <td align="left"><input type="text" name="draftDate" id="draftDate" value="<fmt:formatDate value="${commFSLeague.draftDate.time}" pattern="yyyy-MM-dd"/>" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">HasPaid : </td>
                                    <td align="left"><input type="text" name="hasPaid" id="hasPaid" value="<c:out default="" value="${commFSLeague.hasPaid}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">IsDraftComplete : </td>
                                    <td align="left"><input type="text" name="isDraftComplete" id="isDraftComplete" value="<c:out default="" value="${commFSLeague.isDraftComplete}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">CommissionerUserID : </td>
                                    <td align="left"><input type="text" name="commissionerUserID" id="commissionerUserID" value="<c:out default="" value="${commFSLeague.commissionerUserID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">IsCustomLeague : </td>
                                    <td align="left"><input type="text" name="isCustomLeague" id="isCustomLeague" value="<c:out default="" value="${commFSLeague.isCustomLeague}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">ScheduleName : </td>
                                    <td align="left"><input type="text" name="scheduleName" id="scheduleName" value="<c:out default="" value="${commFSLeague.scheduleName}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">IsDefaultLeague : </td>
                                    <td align="left"><input type="text" name="isDefaultLeague" id="isDefaultLeague" value="<c:out default="" value="${commFSLeague.isDefaultLeague}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">SignupType : </td>
                                    <td align="left"><input type="text" name="signupType" id="signupType" value="<c:out default="" value="${commFSLeague.signupType}" />" /></td>
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
