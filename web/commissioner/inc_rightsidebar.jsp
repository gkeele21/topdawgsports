<%@ page import="bglib.util.FSUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="css/registration.css" media="screen" />

<div id="rightsidebar">
    <img src="/coltarama/images/peyton.jpg" width="179" class="rightsidepic"/>
    
    <!-- Check to see if the user is logged in -->
    <c:choose>
        <c:when test="${validUser != null}" >
            <h2>Your Teams</h2>
            
            <div id="rightsidebartext">
                <ul>
                    <li>Username : ${validUser.username}</li>
                </ul>
            </div>
            
            <c:set var="currentGameName" value="" />
            <c:forEach items="${validUser.teams}" var="team">
                <c:if test="${team.FSLeague.FSSeason.FSGame.gameName != currentGameName}">
                    <h3><c:out value="${team.FSLeague.FSSeason.FSGame.gameName}" /></h3>
                    <div id="rightsidebartext1">
                        <ul>
                    <c:set var="currentGameName" value="${team.FSLeague.FSSeason.FSGame.gameName}" />
                </c:if>
                
                <li><a href="${team.FSLeague.FSSeason.FSGame.homeURL}?tid=${team.FSTeamID}"><c:out value="${team.FSLeague.leagueName}" /></a></li>

                <c:if test="${team.FSLeague.FSSeason.FSGame.gameName != currentGameName}">
                        </ul>
                    </div>
                </c:if>

            </c:forEach>
        </c:when>
        <c:otherwise>

            <jsp:include page="inc_errorMessage.jsp" />

            <form action="login.do?origURL=<%= FSUtils.getPageName(request.getRequestURI(), true) %>" method="post" name="loginForm">
                <img src="/coltarama/images/spacer.gif" />
                <c:if test="<%= !FSUtils.getPageName(request.getRequestURI(), false).equals("register") %>">
                    <div class="loginTitle">
                        <h3>Login</h3>
                    </div>
                    <div class="loginForm">
                        <table>
                            <tr>
                                <td class="formdef">User Name: </span></td>
                                <td align="right"><input type="text" name="txtUserName" size="13" /></td>
                            </tr>
                            <tr>
                                <td class="formdef">Password: </span></td>
                                <td align="right"><input type="password" name="txtPassword" size="13" /></td>
                            </tr>
                            <tr>
                                <td><img src="/coltarama/images/spacer.gif"/></td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">&#160;<input type="image" src="../images/button_go.gif" border="0" /></td>
                            </tr>
                        </table>
                    </div>
                </c:if>
                <img src="/coltarama/images/spacer.gif" />
            </form>
            <script language="JavaScript">
                var xx = document.loginForm.txtUserName.focus();
            </script>
        </c:otherwise>
    </c:choose>
</div>
