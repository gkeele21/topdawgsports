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
        <title>PGA Tournament Edit</title>
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
                    <div align="center" class="rowTitle"><strong><h2>PGA Tournament : ${tournament.tournamentName}</h2></strong></div>
                    
                    <!-- PGA Tournament Info -->
                    <div align="center">
                        <form method="post" action="golfTournamentEdit.do">
                            <table width="40%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                                <tr class="rowData">
                                    <td align="right">TournamentID : </td>
                                    <td align="left"><c:out value="${tournament.PGATournamentID}" /><input type="hidden" name="PGATournamentID" value="${tournament.PGATournamentID}" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">Name : </td>
                                    <td align="left"><input type="text" name="tournamentName" id="tournamentName" value="<c:out default="" value="${tournament.tournamentName}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">Name Short : </td>
                                    <td align="left"><input type="text" name="tournamentNameShort" id="tournamentNameShort" value="<c:out default="" value="${tournament.tournamentNameShort}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">Active: </td>
                                    <td align="left"><input type="text" name="active" id="active" value="<c:out default="" value="${tournament.active}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">Defending Champion ID: </td>
                                    <td align="left"><input type="text" name="defendingChampionID" id="defendingChampionID" value="<c:out default="" value="${tournament.defendingChampionID}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">Leaderboard URL: </td>
                                    <td align="left"><input type="text" name="leaderboardURL" id="leaderboardURL" value="<c:out default="" value="${tournament.leaderboardURL}" />" /></td>
                                </tr>
                                <tr class="rowData2">
                                    <td align="right">Number of Rounds : </td>
                                    <td align="left"><input type="text" name="numRounds" id="numRounds" value="<c:out default="" value="${tournament.numRounds}" />" /></td>
                                </tr>
                                <tr class="rowData">
                                    <td align="right">External Tournament ID : </td>
                                    <td align="left"><input type="text" name="externalTournamentID" id="externalTournamentID" value="<c:out default="" value="${tournament.externalTournamentID}" />" /></td>
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
