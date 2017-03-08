<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="banner">
    <div id="innerBanner">
        <a href="/topdawgsports/index.htm"><img src="/topdawgsports/images/banner.png" alt=""/></a>
    </div>
</div> <!-- banner -->

<div id="login">
    <div id="innerLogin">
        <c:choose>
            <c:when test="${validUser == null}" >
                <form method="post" action="login.do?origURL=index.htm&nextURL=userProfile.htm" name="loginForm">
                    <table>
                        <tr>
                            <td>Username:</td>
                            <td><input class="rounded-corners" type="text" name="txtUserName" size="8" /></td>
                        </tr>
                        <tr>
                            <td>Password:</td>
                            <td><input class="rounded-corners" type="password" name="txtPassword" size="8" /></td>
                        </tr>
                        <tr>
                            <td class="loginButton" colspan="2"><input type="image" src="/topdawgsports/images/login.png"/></td>
                        </tr>
                    </table>
                </form>
                <script language="JavaScript">
                    var xx = document.loginForm.txtUserName.focus();
                </script>
            </c:when>
            <c:otherwise>
                <p>Welcome ${validUser.username}!</p>
                <form action="/topdawgsports/logout.do" method="post">
                    <input type="image" src="/topdawgsports/images/logout.png" />
                </form>
                <a href="/topdawgsports/userProfile.htm">User Profile</a>
            </c:otherwise>
        </c:choose>

        
    </div>
</div> <!-- login -->
