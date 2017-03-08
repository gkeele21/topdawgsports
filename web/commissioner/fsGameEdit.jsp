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
        <title>FSGame Edit</title>
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
                        <form method="post" action="fsGameEdit.do">
                            <table width="40%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr class="rowData">
                                    <td align="right">FSGameID : </td>
                                    <td align="left"><c:out value="${commFSGame.FSGameID}" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">SportID : </td>
                                    <td align="left"><input type="text" name="sportID" id="sportID" value="<c:out default="" value="${commFSGame.sportID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">GameName : </td>
                                    <td align="left"><input type="text" name="gameName" id="gameName" value="<c:out default="" value="${commFSGame.gameName}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">GameNameShort : </td>
                                    <td align="left"><input type="text" name="gameNameShort" id="gameNameShort" value="<c:out default="" value="${commFSGame.gameNameShort}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">GamePrefix : </td>
                                    <td align="left"><input type="text" name="gamePrefix" id="gamePrefix" value="<c:out default="" value="${commFSGame.gamePrefix}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">ScoringStyle : </td>
                                    <td align="left"><input type="text" name="scoringStyle" id="scoringStyle" value="<c:out default="" value="${commFSGame.scoringStyle}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">GroupingStyle : </td>
                                    <td align="left"><input type="text" name="groupingStyle" id="groupingStyle" value="<c:out default="" value="${commFSGame.groupingStyle}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">CurrentFSSeasonID : </td>
                                    <td align="left"><input type="text" name="currentFSSeasonID" id="currentFSSeasonID" value="<c:out default="" value="${commFSGame.currentFSSeasonID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">DisplayName : </td>
                                    <td align="left"><input type="text" name="displayName" id="displayName" value="<c:out default="" value="${commFSGame.displayName}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">HomeURL : </td>
                                    <td align="left"><input type="text" name="homeURL" id="homeURL" value="<c:out default="" value="${commFSGame.homeURL}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">HomeURLShort : </td>
                                    <td align="left"><input type="text" name="homeURLShort" id="homeURLShort" value="<c:out default="" value="${commFSGame.homeURLShort}" />" /></td>
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
