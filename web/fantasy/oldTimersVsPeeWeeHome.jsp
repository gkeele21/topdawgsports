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

    <style>
        table { text-align: center;}
        #content { position: relative; }
        #oldTimerStandings { float:left; margin-left: 50px;}
        #peeWeeStandings { float:left; margin-left: 150px;}
        #playoffBracket { clear: both;}
        #playoffBracket h2 { padding-top: 15px; text-align: center}
        .bracket, .team, .score, .seed, .finalBracket, .champion, .leagueName { position: absolute; }
        .bracket { border: solid black 3px; border-left: none; }
        .finalBracket { border-top: solid black 3px; }
        .leagueName { font-size: 1.1em; text-transform: uppercase; }
    </style>
  </head>
  <body>

    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div> <!-- main menu -->

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div> <!-- left Menu -->

        <div id="content">
            <div id="innerContent">

                <div id="oldTimerStandings">
                    <table>
                        <tr class="rowTitle">
                            <td colspan="4">Old Timer Standings</td>
                        </tr>
                        <tr>
                            <td colspan="4">through Week #13</td>
                        </tr>
                        <tr class="rowHeader">
                            <td>Rank</td>
                            <td>Team</td>
                            <td>Record</td>
                            <td>PF</td>
                        </tr>

                        <tr class="rowData">
                            <td>1</td>
                            <td>Bert</td>
                            <td>12-1</td>
                            <td>1,521.68</td>
                        </tr>

                        <tr class="rowData">
                            <td>2</td>
                            <td>Grant</td>
                            <td>10-3</td>
                            <td>1,434.68</td>
                        </tr>

                        <tr class="rowData">
                            <td>3</td>
                            <td>John</td>
                            <td>8-5</td>
                            <td>1,283.56</td>
                        </tr>

                        <tr class="rowData">
                            <td>4</td>
                            <td>Jordan</td>
                            <td>7-6</td>
                            <td>1,434.98</td>
                        </tr>

                        <tr class="rowData">
                            <td></td>
                            <td>Nathan</td>
                            <td>7-6</td>
                            <td>1,286.50</td>
                        </tr>

                        <tr class="rowData">
                            <td></td>
                            <td>Mike</td>
                            <td>7-6</td>
                            <td>1,281.44</td>
                        </tr>

                        <tr class="rowData">
                            <td></td>
                            <td>Case</td>
                            <td>7-6</td>
                            <td>1,246.50</td>
                        </tr>

                        <tr class="rowData">
                            <td>8</td>
                            <td>Derek</td>
                            <td>4-9</td>
                            <td>1,075.54</td>
                        </tr>

                        <tr class="rowData">
                            <td>9</td>
                            <td>Nick</td>
                            <td>2-11</td>
                            <td>1,158.62</td>
                        </tr>

                        <tr class="rowData">
                            <td>10</td>
                            <td>Dave</td>
                            <td>1-12</td>
                            <td>1,198.62</td>
                        </tr>
                    </table>
                </div>

                <div id="peeWeeStandings">
                    <table>
                        <tr class="rowTitle">
                            <td colspan="4">Pee Wee Standings</td>
                        </tr>
                        <tr>
                            <td colspan="4">through Week #13</td>
                        </tr>
                        <tr class="rowHeader">
                            <td>Rank</td>
                            <td>Team</td>
                            <td>Record</td>
                            <td>PF</td>
                        </tr>

                        <tr class="rowData">
                            <td>1</td>
                            <td>Jackson</td>
                            <td>11-2</td>
                            <td>1,379.88</td>
                        </tr>

                        <tr class="rowData">
                            <td>2</td>
                            <td>Josh</td>
                            <td>10-3</td>
                            <td>1,535.08</td>
                        </tr>

                        <tr class="rowData">
                            <td></td>
                            <td>Ethan</td>
                            <td>10-3</td>
                            <td>1,490.36</td>
                        </tr>

                        <tr class="rowData">
                            <td>4</td>
                            <td>Alex</td>
                            <td>7-6</td>
                            <td>1,282.68</td>
                        </tr>

                        <tr class="rowData">
                            <td>5</td>
                            <td>Luke</td>
                            <td>5-8</td>
                            <td>1,193.12</td>
                        </tr>

                        <tr class="rowData">
                            <td></td>
                            <td>Travis</td>
                            <td>5-8</td>
                            <td>1,148.32</td>
                        </tr>

                        <tr class="rowData">
                            <td></td>
                            <td>Dawson</td>
                            <td>5-8</td>
                            <td>1,051.60</td>
                        </tr>

                        <tr class="rowData">
                            <td></td>
                            <td>Brandon</td>
                            <td>5-8</td>
                            <td>1,041.06</td>
                        </tr>

                        <tr class="rowData">
                            <td>9</td>
                            <td>Cooper</td>
                            <td>4-9</td>
                            <td>1,243.66</td>
                        </tr>

                        <tr class="rowData">
                            <td>10</td>
                            <td>Dallin</td>
                            <td>3-10</td>
                            <td>1,122.10</td>
                        </tr>
                    </table>
                </div>

                <div id="playoffBracket">
                    <h2>2018 Playoffs</h2>

                    <label class="leagueName" style="top:335px; margin-left:100px;">Old Timers</label>

                    <!-- ROUND 1 -->
                    <div class="bracket" style="top: 400px; left: 100px; height: 50px; width: 100px;">
                        <label class="seed" style="top: -27px; left: -25px; width:20px;">1</label>
                        <label style=" top: -27px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=124&wk=14&game=1">Bert</a></label>
                        <label class="score" style="top: 0.0px; width: 100px;"></label>
                        <label class="seed" style="top: 25px; left: -25px; width:20px;">4</label>
                        <label style=" top: 25px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=124&wk=14&game=1">Jordan</a></label>
                    </div>

                    <div class="bracket" style="top: 500px; left: 100px; height: 50px; width: 100px;">
                        <label class="seed" style="top: -27px; left: -25px; width:20px;">2</label>
                        <label style=" top: -27px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=124&wk=14&game=2">Grant</a></label>
                        <label class="score" style="top: 0.0px; width: 100px;"></label>
                        <label class="seed" style="top: 25px; left: -25px; width:20px;">3</label>
                        <label style=" top: 25px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=124&wk=14&game=2">John</a></label>
                    </div>

                    <label class="leagueName" style="top:580px; margin-left:100px;">Pee Wee</label>

                    <div class="bracket" style="top: 650px; left: 100px; height: 50px; width: 100px;">
                        <label class="seed" style="top: -27px; left: -25px; width:20px;">1</label>
                        <label style=" top: -27px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=125&wk=14&game=1">Jackson</a></label>
                        <label class="score" style="top: 0.0px; width: 100px;"></label>
                        <label class="seed" style="top: 25px; left: -25px; width:20px;">4</label>
                        <label style=" top: 25px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=125&wk=14&game=1">Alex</a></label>
                    </div>

                    <div class="bracket" style="top: 750px; left: 100px; height: 50px; width: 100px;">
                        <label class="seed" style="top: -27px; left: -25px; width:20px;">2</label>
                        <label style=" top: -27px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=125&wk=14&game=2">Josh</a></label>
                        <label class="score" style="top: 0.0px; width: 100px;"></label>
                        <label class="seed" style="top: 25px; left: -25px; width:20px;">3</label>
                        <label style=" top: 25px; width: 100px;" class="team"><a href="gameMatchup.htm?lgid=125&wk=14&game=2">Ethan</a></label>
                    </div>

                    <!-- ROUND 2 -->
                    <div class="bracket" style="top: 425px; left: 200px; height: 100px; width: 100px;">
                        <label style=" top: -27px; width: 100px;" class="team">&#160;&#160;<a href="gameMatchup.htm?lgid=124&wk=15&game=1">Bert</a></label>
                        <label class="score" style="top: 0.0px; width: 100px;"></label>
                        <label style=" top: 75px; width: 100px;" class="team">&#160;&#160;<a href="gameMatchup.htm?lgid=124&wk=15&game=1">John</a></label>
                    </div>

                    <div class="bracket" style="top: 675px; left: 200px; height: 100px; width: 100px;">
                        <label style=" top: -27px; width: 100px;" class="team">&#160;&#160;<a href="gameMatchup.htm?lgid=125&wk=15&game=1">Jackson</a></label>
                        <label class="score" style="top: 0.0px; width: 100px;"></label>
                        <label style=" top: 75px; width: 100px;" class="team">&#160;&#160;<a href="gameMatchup.htm?lgid=125&wk=15&game=1">Josh</a></label>
                    </div>

                    <!-- ROUND 3 -->
                    <div class="bracket" style="top: 475px; left: 300px; height: 250px; width: 100px;">
                        <label style=" top: -27px; width: 100px;" class="team">&#160;&#160;John</label>
                        <label class="score" style="top: 0.0px; width: 100px;"></label>
                        <label style=" top: 225px; width: 100px;" class="team">&#160;&#160;Jackson</label>
                    </div>

                    <!-- CHAMPION -->
                    <div class="finalBracket" style="top: 600px; left: 400px; width: 150px;">
                        <label style=" top: -27px; width: 100px;" class="champion"></label>
                    </div>
                </div>

            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container-->
  </body>
</html>
