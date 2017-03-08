<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="/topdawgsports/css/menu.css" media="screen" />

<c:set var="appendUserData" value="" />
<c:if test="${validUser != null}" >
    <c:set var="appendUserData" value="?userid=${validUser.FSUserID}&authKey=${validUser.authenticationKey}" />
</c:if>

<%-- Decide which Game to highlight --%>
<c:set var="highlight" value="fantasy" />
<c:choose>
    <c:when test="${fsteam == null && (pageName == 'sal.index' || pageName == 'sal.rules')}">
        <c:set var="highlight" value="sal" />
    </c:when>
    <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 2}" >
        <c:set var="highlight" value="sal" />
    </c:when>
</c:choose>

<div id="menu_container">

    <ul id="pmenu">
        <%--<li><a href="/topdawgsports/index.htm">Home</a></li>--%>
        <c:set var="highClass" value="" />

        <c:if test="${highlight == 'fantasy'}">
            <c:set var="highClass" value="mainHighlight" />
        </c:if>
        <li class="${highClass}">
            <c:if test="${highlight == 'fantasy'}">
                <span class="mainHighlight">
            </c:if>
            <a href="/topdawgsports/fantasy/index.htm">Keele Fantasy</a>
            <c:if test="${highlight == 'fantasy'}">
                </span>
            </c:if>
        </li>
        <c:set var="highClass" value="" />

        <c:if test="${highlight == 'sal'}">
            <c:set var="highClass" value="mainHighlight" />
        </c:if>
        <li class="${highClass}">
            <c:if test="${highlight == 'sal'}">
                <span class="mainHighlight">
            </c:if>
            <a href="/topdawgsports/sal/index.htm">Salary Cap</a>
            <c:if test="${highlight == 'sal'}">
                </span>
            </c:if>
        </li>
        <li><a href="/topdawgsports/registerGames.htm">Pro Pickem</a></li>
        <li><a href="/topdawgsports/registerGames.htm">College Pickem</a></li>
        <li><a href="/topdawgsports/registerGames.htm">Pro Love Em & Leave Em</a></li>
        <li><a href="/topdawgsports/registerGames.htm">College Love Em & Leave Em</a></li>
        <%--<li><a href="/topdawgsports/contactus.htm">Contact us</a></li>--%>
    </ul>
</div> <!-- menu_container -->


