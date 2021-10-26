<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - User Profile</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/topDawgCommon.css" media="screen" />

    <style type="text/css">

#teamList {
    margin-left: auto;
    margin-right: auto;
    width: 50%;
}

#teamList h1 {
    text-align: center;
    text-transform: uppercase;
}

#teamList img {
    padding-top: 25px;
}


#teamList ul {
    border: solid medium black;
    list-style: none;
    margin-top: 20px;
    font-weight: bold;
}

#teamList a:hover {
    color: #F2BC57;
}
    </style>
  </head>

<body>
    <div id="container">

        <div id="header">
            <jsp:include page="inc_header.jsp" />
        </div> 
            
        <div id="mainMenu">
            <jsp:include page="menu/mainMenu.jsp" />
        </div>

        <div id="teamList">

            <h1>User Profile</h1>
                       
            <form action="userProfile.htm" method="post">
                
                Year:
                <select name="reqYear" >
                    <c:forEach items="${allYears}" var="year">
                        <option value="${year}"
                            <c:if test="${year == sportYear}">
                                selected="selected"
                            </c:if>
                        >${year}
                        </option>
                    </c:forEach>
                    
                </select>
                
                <input type="submit" value="Go" />

            </form>
            
            <c:if test="${!empty activeTeams}"><br /><h2>ACTIVE TEAMS</h2></c:if>

            <c:forEach items="${activeTeams}" var="activeTeam">
                <ul>
                    <li>Game : ${activeTeam.FSLeague.FSSeason.FSGame.gameName}</li>
                    <li>League : ${activeTeam.FSLeague.leagueName}</li>
                    <li>League Size : ${activeTeam.FSLeague.numTeams}</li>
                    <c:choose>
                        <c:when test="${activeTeam.FSLeague.FSSeason.FSGameID == NFL_WINS_POOL_GAMEID || activeTeam.FSLeague.FSSeason.FSGameID == NBA_WINS_POOL_GAMEID}">
                            <li>Team : <a href="${activeTeam.FSLeague.FSSeason.FSGame.homeURL}" target="_blank">${activeTeam.teamName}</a></li>                    
                        </c:when>
                        <c:otherwise>
                            <li>Team : <a href="${activeTeam.FSLeague.FSSeason.FSGame.homeURLShort}?tid=${activeTeam.FSTeamID}">${activeTeam.teamName}</a></li>                    
                        </c:otherwise>
                    </c:choose>
                </ul>
            </c:forEach>
            
            <c:if test="${!empty inactiveTeams && !empty activeTeams}"><br /><h2>INACTIVE TEAMS</h2></c:if>
            
            <c:forEach items="${inactiveTeams}" var="inactiveTeam">
                <ul>
                    <li>Game : ${inactiveTeam.FSLeague.FSSeason.FSGame.gameName}</li>
                    <li>League : ${inactiveTeam.FSLeague.leagueName}</li>
                    <li>League Size : ${inactiveTeam.FSLeague.numTeams}</li>
                    <li>Team : <a href="${inactiveTeam.FSLeague.FSSeason.FSGame.homeURLShort}?tid=${inactiveTeam.FSTeamID}">${inactiveTeam.teamName}</a></li>                    
                </ul>
            </c:forEach>
        </div>
        
    </div> <!-- container -->

</body>
</html>
