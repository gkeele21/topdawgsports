<%@ page import="bglib.util.FSUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="css/registration.css" media="screen" />

<div id="rightsidebar">
    <img src="/topdawgsports/images/fatty_fat_fat_football.jpg" width="179" class="rightsidepic"/>
    
    <!-- Check to see if the user is logged in -->
    <c:choose>
        <c:when test="${validUser != null}" >
            <h2>&#160;Logged in as :</h2>
            
            <form action="/topdawgsports/logout.do" method="post">
            <div id="rightsidebartext">
                <ul>
                    <li>${validUser.username}&#160;&#160;&#160;
                    <input type="image" src="/topdawgsports/images/logoutIconText.jpg" border="0" />
                    </li>
                </ul>
            </div> <!-- rightsidebartext -->
            </form>
            <hr /><br />
            <h2>&#160;Your Current Teams:</h2><br />
            <div id="rightsidebartext1">
                <c:forEach items="${validUser.teams}" var="team">
                    <ol>
                        <c:set var="teamClass" value="" />
                        <c:if test="${fsteam != null}">
                            <c:if test="${team.FSTeamID == fsteam.FSTeamID}">
                                <c:set var="teamClass" value="class=rowData" />
                            </c:if>
                        </c:if>
                        <li ${teamClass}><a href="${team.FSLeague.FSSeason.FSGame.homeURLShort}?tid=${team.FSTeamID}&authKey=${validUser.authenticationKey}"><c:out value="${team.FSLeague.leagueName}" /></a></li>
                    </ol>
                </c:forEach>
            </div> <!-- rightsidebartext1 -->
        </c:when>
        <c:otherwise>

            <jsp:include page="inc_errorMessage.jsp" />

            <form action="/topdawgsports/login.do?origURL=<%= FSUtils.getPageName(request.getRequestURI(),true, true) %>" method="post" name="loginForm">
                <img src="/topdawgsports/images/spacer.gif" />
                <%--<c:if test="<%= !FSUtils.getPageName(request.getRequestURI(),false, false).equals("register") %>">--%>
                    <div class="loginTitle">
                        <h3>Not a user?  <a href="/topdawgsports/register.htm">Click here</a> to signup</h3>
                    </div> <!-- loginTitle -->
                <%--</c:if>--%>
                    <div class="loginTitle">
                        <h3>Login</h3>
                    </div> <!-- loginTitle -->
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
                                <td><img src="/topdawgsports/images/spacer.gif"/></td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">&#160;<input type="image" src="/topdawgsports/images/button_go.gif" border="0" /></td>
                            </tr>
                        </table>
                    </div> <!-- loginForm -->
                <img src="/topdawgsports/images/spacer.gif" />
            </form>
            <script language="JavaScript">
                var xx = document.loginForm.txtUserName.focus();
            </script>
        </c:otherwise>
    </c:choose>
</div> <!-- rightsidebar -->
