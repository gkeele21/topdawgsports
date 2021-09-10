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
        <title>PGA Tournament Results</title>
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

                    <form method="post" action="golfCalculateResults.do">
                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                            <tr colspan="7">
                                <input type="hidden" name="hfRecalc" value="hfRecalcTrue" />
                                <td colspan="2" align="center"><input type="submit" name="submit" value="Calculate Final Fantasy Results" /></td>
                            </tr>
                        </table>
                    </form>

                </div> <!-- main -->
            </div> <!-- colmask outside -->

            <jsp:include page="../inc_footer.jsp" />

        </div> <!-- container -->

    </body>
</html>
