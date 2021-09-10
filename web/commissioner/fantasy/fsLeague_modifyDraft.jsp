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
        <title>TopDawgSports - Modify Draft</title>
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
                    <h3>
                        <a href="fsLeagueView.htm">Back to League View</a>
                    </h3>
                    <br />
                    <div align="center" class="rowTitle"><strong><h2>DRAFT RESULTS : ${commFSLeague.leagueName}</h2></strong></div>
                    <form action="fsLeague_modifyDraft.do" method="post">
                        <table width="50%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                            <tr class="rowTitle">
                                <td align="right">Round : </td>
                                <td align="left"><tds:listbox items="${roundList}" name="round" /></td>
                            </tr>
                            <tr class="rowTitle">
                                <td align="right">Place : </td>
                                <td align="left"><%--<tds:listbox items="${placeList}" name="place" />--%>
                                    <input type="text" name="place" />
                                </td>
                            </tr>
                            <tr class="rowTitle">
                                <td align="right">Team : </td>
                                <td align="left"><tds:listbox items="${teamList}" name="team" /></td>
                            </tr>
                            <tr class="rowTitle">
                                <td align="right">Player : </td>
                                <td align="left"><tds:listbox items="${playerList}" name="player" /></td>
                            </tr>
                            <tr class="rowTitle">
                                <td align="right" colspan="2"><input type="submit" name="Submit" /></td>
                            </tr>
                        </table>
                    </form>

                    <table width="100%" border="0" cellpadding="0" cellspacing="1" class="ctTable">
                        <tds:tableRows items="${leagueDraft}" var="draft" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                            <jsp:attribute name="rowInfo">

                            </jsp:attribute>
                            <jsp:attribute name="rowTitle">
                                <tr align="center" class="rowTitle">
                                    <td colspan="8">
                                        <div><strong><h2>Draft Results</h2></strong></div><br />
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="8">
                                    </td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowHeader">
                                <tr class="rowHeader">
                                    <td width="8%" align="center"><strong>Round</strong></td>
                                    <td width="8%" align="center"><strong>Pick #</strong></td>
                                    <td width="8%" align="center"><strong>Team</strong></td>
                                    <td width="25%" align="center"><strong>Player</strong></td>
                                    <td width="8%" align="center"><strong>Pos</strong></td>
                                    <td width="8%" align="center"><strong>Team</strong></td>
                                    <td width="8%" align="center">&#160;</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowData">
                                <tr ${highlightRow1} class="rowData">
                                    <td align="center"><c:out value="${draft.round}" /></td>
                                    <td align="center"><c:out value="${draft.place}" /></td>
                                    <td align="center"><c:out value="${draft.FSTeam.teamName}" /></td>
                                    <td align="center"><c:out value="${draft.player.fullName}" /></td>
                                    <td align="center"><c:out value="${draft.player.position.positionName}" /></td>
                                    <td align="center"><c:out value="${draft.player.team.displayName}" /></td>
                                    <td align="center"><a href="fsLeague_modifyDraft.htm?action=delete&round=${draft.round}&place=${draft.place}">delete</a></td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowEmpty">
                                <tr>
                                    <td colspan="10">There are no draft records to display.</td>
                                </tr>
                            </jsp:attribute>
                            <jsp:attribute name="rowNavigation">
                                <c:set var="colspan" value="2" />
                                <tr><td height="10" colspan="8"><img src="../../images/spacer.gif" /></td></tr>
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

            <jsp:include page="../inc_footer.jsp" />

        </div> <!-- container -->

    </body>
</html>
