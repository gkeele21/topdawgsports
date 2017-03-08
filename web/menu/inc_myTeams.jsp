<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Initialization -->
<c:set var="prevGame" value="" />

<img src="/topdawgsports/images/myteams.png" alt=""/>

<c:forEach items="${allUserTeams}" var="team">

    <!-- MENU SELECTION -->
        <c:set var="teamClass" value="" />
        <c:if test="${fsteam != null}">
            <c:if test="${team.FSTeamID == fsteam.FSTeamID}">
                <c:set var="teamClass" value="class=menuSelection" />
            </c:if>
        </c:if>

        <!-- DISPLAY THE GAME -->
        <c:if test="${prevGame != team.FSLeague.FSSeason.FSGame.FSGameID}">
            <label><c:out value="${team.FSLeague.FSSeason.FSGame.gameNameShort}" /></label>
            <c:set var="prevGame" value="${team.FSLeague.FSSeason.FSGame.FSGameID}" />
        </c:if>

        <!-- DISPLAY THE TEAM NAME -->
        <a ${teamClass} href="${team.FSLeague.FSSeason.FSGame.homeURLShort}?tid=${team.FSTeamID}">
            <c:choose>
                <c:when test="${team.FSLeague.FSSeason.FSGame.FSGameID == 1}">
                    <c:out value="${team.FSLeague.leagueName}" />
                </c:when>
                <c:otherwise>
                    <c:out value="${team.teamName}" />
                </c:otherwise>
            </c:choose>
        </a>

</c:forEach>