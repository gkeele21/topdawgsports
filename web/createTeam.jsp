<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - Create Team</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/topDawgCommon.css" media="screen" />

    <style type="text/css">
table {
    margin: 50px 0px 0px 0px;
}

.button {
    padding-top: 25px;
}

.teamName {
    font-size: 2em;
    text-align: right;
}

.rounded-corners {
    font-size: 36px;
    background-color: #FFFFB0;
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
        </div><!-- main menu -->

        <div id="createTeam">
            <div id="innerCreateTeam">

                <div id="stepInfo">
                    <label class="bold">Step : </label> <label>2 of 3</label><br />
                    <c:if test="${validUser != null}" >
                        <label class="bold">Username : </label><c:out value="${validUser.username}" /><br />
                    </c:if>
                    <c:if test="${gameSignupFSSeason != null}" >
                        <label class="bold">Game : </label><c:out value="${gameSignupFSSeason.seasonName}" />
                    </c:if>                    
                </div>

                <!-- NEW TEAM -->
                <div id="newTeam">
                    <form action="joinLeague.htm" action="post">
                        <table>
                            <tr>
                                <td class="teamName">Team Name :</td>
                                <td><input class="rounded-corners" type="text" name="teamName" /></td>
                            </tr>
                            <tr>
                                <td class="button" colspan="2"><input type="image" src="images/continue.png"/></td>
                            </tr>
                        </table>
                    </form>
                </div>

            </div>
        </div><!-- createTeam -->
    </div> <!-- container -->

</body>
</html>
