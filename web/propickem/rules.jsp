<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - Pro Pickem Rules</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
  </head>

  <body>
    <div id="container">
        <div id="header">
            <jsp:include page="../inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="../menu/mainMenu.jsp" />
        </div>

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="../menu/inc_leftMenu.jsp" />
            </div>
        </div>

        <div id="content">
            <div id="innerContent">

                <div id="rules">

                    <h1>Pro Pickem Rules</h1>

                    <h3>Game Play</h3>
                    <p>Predict the winners of all Pro games for the entire 17 week regular season.</p>

                    <h3>Season Duration</h3>
                    <p>The season will last all the way through the entire regular season but will not include any playoff games.</p>

                    <h3>Entry Fee</h3>
                    <p>This game costs $3 per entry per person (General League only).</p>

                    <h3>Player Limit</h3>
                    <p>This game can have an unlimited amount of players.</p>

                    <h3>Scoring</h3>
                    <p>You receive the point value assigned each pick that you get right.  You have to correctly pick the team in order to get points.  One point will be awarded for a correct pick that doesn't have a point value assigned.</p>

                    <h3>Champion</h3>
                    <p>The team(s) with the most accumulated game points at the conclusion of the regular season.</p>
                    
                    <h3>Grand Prize</h3>
                    <p>The champion will receive a prize worth the total amount of entry fees.</p>

                    <h3>Tiebreaker</h3>
                    <p>There is no tiebreaker, the prizes will be split equally among all tied winners.</p>

                    <h3>Help</h3>
                    <p>If you come across any problems or errors, please help out by sending us an email. We really appreciate any extra help we can get in adding to the quality of our product. Also, any feedback you can give us or ways to improve the site would sure be welcomed.</p>
                    
                </div> <!-- rules -->
            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>