<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${validUser != null && fsteam != null}" >
    <c:set var="imageURL" value="" />
    <c:set var="menuURL" value="" />
    <c:choose>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 1 && fsteam.FSLeague.draftType == 'keeper'}" >
            <c:set var="imageURL" value="../images/keeper.png" />
            <c:set var="menuURL" value="../menu/inc_keeperMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 1 && fsteam.FSLeague.draftType == 'redraft'}" >
            <c:set var="imageURL" value="../images/tenman.png" />
            <c:set var="menuURL" value="../menu/inc_tenmanMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 1 && fsteam.FSLeague.draftType == 'dynasty'}" >
            <c:set var="imageURL" value="../images/dynasty.png" />
            <c:set var="menuURL" value="../menu/inc_dynastyMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 2}" >
            <c:set var="imageURL" value="../images/salarycap.png" />
            <c:set var="menuURL" value="../menu/inc_salaryCapMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 3}" >
            <c:set var="imageURL" value="../images/salarycap.png" />
            <c:set var="menuURL" value="../menu/inc_salaryCapElimMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 4}" >
            <c:set var="imageURL" value="../images/nflpickem.png" />
            <c:set var="menuURL" value="../menu/inc_proPickemMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 5}" >
            <c:set var="imageURL" value="../images/nflloveemleaveem.png" />
            <c:set var="menuURL" value="../menu/inc_proLoveLeaveMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 6}" >
            <c:set var="imageURL" value="../images/ncaaloveemleaveem.png" />
            <c:set var="menuURL" value="../menu/inc_collegeLoveLeaveMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 7}" >
            <c:set var="imageURL" value="../images/ncaapickem.png" />
            <c:set var="menuURL" value="../menu/inc_collegePickemMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 8}" >
            <c:set var="imageURL" value="../images/bracket_challenge.png" />
            <c:set var="menuURL" value="../menu/inc_mmBracketChallengeMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 9}" >
            <c:set var="imageURL" value="../images/seed_challenge.png" />
            <c:set var="menuURL" value="../menu/inc_mmSeedChallengeMenu.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 10}" >
            <c:set var="imageURL" value="../images/ffplayoff.png" />
            <c:set var="menuURL" value="../menu/inc_playoff.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 11}" >
            <c:set var="menuURL" value="../menu/inc_stattracker.jsp" />
        </c:when>
        <c:when test="${fsteam != null && fsteam.FSLeague.FSSeason.FSGameID == 12}" >
            <c:set var="menuURL" value="../menu/inc_golf.jsp" />
        </c:when>
    </c:choose>
    <ul>
        <c:if test="${menuURL != ''}" >
            <img src="${imageURL}" alt=""/>
            <jsp:include page="${menuURL}" />
        </c:if>
    </ul>
</c:if>
