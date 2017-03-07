<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <script type="text/javascript" src="../js/script.js" ></script>
  </head>
  <body>

    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div><!-- main menu -->

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div>

        <div id="content">
            <div id="innerContent">

                <div id="transactions">
                    <div id="innerTransactions">

                        <form action="transactionRequests.do" method="post">
                            <label>If you don't want the system to try and process all of the transactions listed, please specify the exact amount of transactions
                                that you would like it to process (0 means it will try and process all of them). </label>
                            <input type="text" name="txtNumTrades" size="1" maxlength="2" value="${maxRequests}" />
                            <input class="smallImage" type="image" name="btnSubmit" src="../images/submit.png" />

                            <p><a class="standardLink" href="faAcquirePlayer.htm">Add a Free Agent</a></p>

                            <table>
                                <tds:tableRows items="${teamRequests}" var="request" tableNumber="1" highlightRowAttribute="class" highlightRowValue="rowData2">
                                    <jsp:attribute name="rowTitle">
                                        <tr class="rowTitle">
                                            <td colspan="10">Transaction Requests</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowHeader">
                                        <tr>
                                            <td colspan="6">Drop</td>
                                            <td colspan="4">Pickup</td>
                                        </tr>
                                        <tr class="rowHeader">
                                              <td>Del</td>
                                              <td></td>
                                              <td>Pos</td>
                                              <td>Team</td>
                                              <td>Player</td>
                                              <td>Type</td>
                                              <td>Pos</td>
                                              <td>Team</td>
                                              <td>Player</td>
                                              <td>Type</td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowData">
                                        <tr class="rowData">
                                              <td><input name="ckDelete" type="checkbox" value="${request.requestID}" /></td>
                                              <td>
                                                  <c:if test="${request.rank < teamRequestsSize}">
                                                      <a href="transactionRequests.htm?id=${request.requestID}&rank=${request.rank+1}" name="downhref">
                                                          <img name="down" src="/topdawgsports/images/down.gif" />
                                                      </a>
                                                  </c:if>
                                                  <c:if test="${request.rank > 1}">
                                                      <a href="transactionRequests.htm?id=${request.requestID}&rank=${request.rank-1}" name="uphref">
                                                          <img name="up" src="/topdawgsports/images/up.gif" />
                                                      </a>
                                                  </c:if>
                                              </td>
                                              <td><c:out value="${request.dropPlayer.position.positionName}" /></td>
                                              <td><c:out value="${request.dropPlayer.team.abbreviation}" /></td>
                                              <td><tds:player player="${request.dropPlayer}" /></td>
                                              <td><c:out value="${request.dropType}" /></td>
                                              <td><c:out value="${request.PUPlayer.position.positionName}" /></td>
                                              <td><c:out value="${request.PUPlayer.team.abbreviation}" /></td>
                                              <td><tds:player player="${request.PUPlayer}" /></td>
                                              <td><c:out value="${request.PUType}" /></td>
                                        </tr>
                                    </jsp:attribute>
                                    <jsp:attribute name="rowEmpty">
                                        <tr>
                                            <td colspan="10">You have no requests for this week. </td>
                                        </tr>
                                    </jsp:attribute>
                                </tds:tableRows>
                            </table>

                            <c:if test="${!empty teamRequests}">
                                <p><input class="smallImage" type="image" name="btnDelete" src="../images/submit.png" /></p>
                            </c:if>
                        </form>

                    </div> <!-- inner transactions -->
                </div> <!-- transactions -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->

  </body>
</html>
