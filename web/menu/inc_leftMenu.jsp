<%@ page import="bglib.util.FSUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Check to see if the user is logged in -->
<c:choose>
    <c:when test="${validUser != null}" >

        <a class="whatsNewLink" href="/topdawgsports/whatsNew.jsp">What's New</a>

        <label>Year: ${sportYear}</label>

        <div id="gameMenu">
            <jsp:include page="inc_gameMenu.jsp" />
        </div>

        <div id="myTeams">
            <jsp:include page="inc_myTeams.jsp" />
        </div>

    </c:when>
    <c:otherwise>

        <jsp:include page="../inc_errorMessage.jsp" />

    </c:otherwise>
</c:choose>
    