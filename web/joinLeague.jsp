<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - Join League</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/topDawgCommon.css" media="screen" />
    <script type="text/javascript" src="js/jquery-1.6.2.min.js"></script>

    <script>
        function endsWith(str, suffix) {
            return str.indexOf(suffix, str.length - suffix.length) !== -1;
        }

	$(function() {
            $("#joinLeague").hide();
            $("#newLeague").hide();
            $("#leaguePass").hide();

            $("#join").click(function () {
                $("#newLeague").hide();
                $("#joinLeague").show();
            });
            $("#new").click(function () {
                $("#joinLeague").hide();
                $("#newLeague").show();
            });

            $("input[name='leagueId']").change(function() {

                fullValue = $(this).val();
                leagueArr = fullValue.split("_");
                scope = leagueArr[1];
                if (scope == 'private') {
                    $("#leaguePass").show();
                } else {
                    $("#leaguePass").hide();
                }
            });

	});
    </script>

    <style type="text/css">

#leagueOptions {
    margin: 20px 0px 30px 0px;
}

#stepInfo {
    margin: 0px 0px 30px 0px;
}

#signup {
    width: 100%;
    margin-top: 30px;
    text-align: center;
}

#leaguePass {
    margin-top: 20px;

}

#leaguePass label {
    font-size: 1.3em;
}

.button {
    padding-top: 25px;
    text-align: center;
}

.newLeagueLabel {
    width: 50%;
    text-align: right;
    font-weight: bold;
}

.newLeagueInput {
    width: 50%;
    text-align: left;
}

.rounded-corners{
    background-color: #FFFFB0;
    font-size: 1.3em;
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

        <div id="gameSignup">
            <div id="innerGameSignup">

                <div id="stepInfo">
                    <label class="bold">Step : </label> <label>3 of 3</label><br />
                    <c:if test="${validUser != null}" >
                        <label class="bold">Username : </label><c:out value="${validUser.username}" /><br />
                    </c:if>
                    <c:if test="${gameSignupFSSeason != null}" >
                        <label class="bold">Game : </label><c:out value="${gameSignupFSSeason.seasonName}" /><br />
                    </c:if>
                    <label class="bold">Team Name : </label><c:out value="${signupTeamName}" />
                </div>

                <jsp:include page="inc_errorMessage.jsp" />

                <div id="leagueOptions">
                    <table>
                        <tr>
                            <c:if test="${genericLeagueId > 0}">
                                <form action="joinLeague.do" method="post">
                                    <td><input type="image" src="images/joinGeneral.png"/></td>
                                    <input type="hidden" name="leagueId" value="${genericLeagueId}_public" />
                                </form>
                            </c:if>
                            <td><img id="join" src="images/joinLeague.png" alt=""></img></td>
                            <td><img id="new" src="images/createLeague.png" alt=""></img></td>                            
                        </tr>
                    </table>
                </div>

                <br />
                <!-- JOIN LEAGUE -->
                <div id="joinLeague">
                    <form action="joinLeague.do" method="post" name="joinForm">
                        <table>
                            <tds:tableRows items="${leaguesList}" var="league" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                <jsp:attribute name="rowHeader">
                                    <tr class="rowHeader">
                                        <td>Join</td>
                                        <td>Name</td>
                                        <td>Type</td>
                                        <td># Teams</td>
                                    </tr>
                                </jsp:attribute>
                                <jsp:attribute name="rowData">
                                    <c:set var="pub" value="public" scope="page" />
                                    <c:if test="${league.isPublic == null || league.isPublic == 0}">
                                        <c:set var="pub" value="private" />
                                    </c:if>
                                    <tr class="rowData">
                                        <td><input type="radio" name="leagueId" value="${league.FSLeagueID}_${pub}"/></td>
                                        <td><c:out value="${league.leagueName}" /></td>
                                        <td><c:out value="${pub}" /></td>
                                        <td><c:out value="${league.numTeams}" /></td>
                                    </tr>
                                </jsp:attribute>
                            </tds:tableRows>
                        </table>

                        <div id="leaguePass">
                            <label>Enter the league's password :</label>
                            <input class="rounded-corners" type="password" name="leaguePassword" />
                        </div>

                        <div id="signup">
                            <input type="image" src="images/signUp.png"/>
                        </div>

                    </form>
                </div>

                <!-- NEW LEAGUE -->
                <div id="newLeague">
                    <form action="createLeague.do" action="post">
                        <table>
                            <tr>
                                <td class="newLeagueLabel">League Name :</td>
                                <td class="newLeagueInput">&nbsp;<input class="rounded-corners" type="text" name="leagueName" /></td>
                            </tr>
                            <tr>
                                <td class="newLeagueLabel">Type :</td>
                                <td class="newLeagueInput">
                                    &nbsp;<select name="publicScope">
                                        <option value="public">Public</option>
                                        <option value="private">Private</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="newLeagueLabel">Description :</td>
                                <td class="newLeagueInput">&nbsp;<input class="rounded-corners" type="textarea" name="description" /></td>
                            </tr>

                            <tr>
                                <td class="newLeagueLabel">Password :</td>
                                <td class="newLeagueInput">&nbsp;<input class="rounded-corners" type="password" name="leaguePassword" /></td>
                            </tr>
                            <tr>
                                <td class="button" colspan="2"><input id="create" type="image" src="images/signUp.png"/></td>
                            </tr>
                        </table>
                    </form>
                </div>

            </div>
        </div><!-- siteImage -->
    </div> <!-- container -->

</body>
</html>