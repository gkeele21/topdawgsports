<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>TopDawgSports - What's New</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/topDawgCommon.css" media="screen" />

    <style type="">

#whatsNew ul {
    padding-left: 25px;
}

h2 {
    margin: 15px 0px 15px 0px;
}

h4 {
    margin-top: 15px;
}
    </style>
  </head>
<body>
    <div id="container">
        <div id="header">
            <jsp:include page="inc_header.jsp" />
        </div>

        <div id="mainMenu">
            <jsp:include page="/menu/mainMenu.jsp" />
        </div>

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="/menu/inc_leftMenu.jsp" />
            </div>
        </div>

        <div id="content">
            <div id="innerContent">

               <div id="whatsNew">
                    <h5>* This page is mainly to keep a log of issues that have been fixed or new things available to the site.</h5>

                    <h2>2011</h2>

                    <h4>Wednesday November 30</h4>
                    <ul>
                        <li>Game information added next to the Game Date for the NCAA Pickem and LoveEm games</li>
                        <li>In all Pickem games, it now only indicates a miss (via red X) once the score is updated and a winner declared.</li>
                    </ul>

                    <h4>Monday November 28</h4>
                    <ul>
                        <li>User Profile link added to the banner under the logout button.</li>
                        <li>What's New page taken out of main menu and added to the left menu once logged in.</li>
                    </ul>

                    <h4>Tuesday November 22</h4>
                    <ul>
                        <li>Signups available for new Fantasy Football Playoff Game</li>
                        <li>Every column on the Standings page for all 4 pickem games are now sortable.</li>
                        <li>My Teams section on left menu showing the user's team name broken out separately by each game.</li>
                    </ul>

                    <h4>Monday October 24</h4>
                    <ul>
                        <li>NCAA Team's entire year schedule viewable (by clicking on the Team Name) </li>
                        <li>NFL Team's entire year schedule viewable (by clicking on the Team Name)</li>
                    </ul>

                    <h4>Monday October 3</h4>
                    <ul>
                        <li>Stats Leaders pages working for Keeper and Tenman.</li>
                        <li>Individual player stats popup window displaying yearly stats on a weekly basis.</li>
                    </ul>

                    <h4>Monday September 26</h4>
                    <ul>
                        <li>Ability to hover over the team name column in the Standings pages to see who the actual user is.</li>
                        <li>Now able to click on and view the picks in NCAA Love Em & Leave Em for Stormy's Lightning Bolts and Snick's Pick's teams.</li>
                    </ul>

                    <h4>Thursday September 22</h4>
                    <ul>
                        <li>Actual team records now shown in the weekly schedule pages for both NFL and NCAA teams.</li>
                    </ul>

                    <h4>Friday September 16</h4>
                    <ul>
                        <li>Updated Rules pages in Keeper and Tenman</li>
                        <li>Rules pages for Keeper and Tenman only viewable to league owners</li>
                    </ul>

                    <h4>Tuesday September 13</h4>
                    <ul>
                        <li>Other team's roster viewable in Salary Cap</li>
                        <li>Online transactions available in Keeper and Tenman</li>
                    </ul>

                    <h4>Monday September 12</h4>
                    <ul>
                        <li>Other team's picks available for both Love Em & Leave Em games</li>
                        <li>Result column in Love Em & Leave Em games now showing properly</li>
                        <li>Transactions made before Week #1 added in Tenman</li>
                        <li>Standings now showing proper week in NFL Pickem and NFL Love Em & Leave Em</li>
                    </ul>

                    <h4>Saturday September 10</h4>
                    <ul>
                        <li>Game Matchup viewable in Keeper and Tenman</li>
                        <li>Transactions made before Week #1 added in Keeper</li>
                    </ul>

                    <h4>Friday September 9</h4>
                    <ul>
                        <li>Rosters imported for Keeper and Tenman</li>
                    </ul>

                    <h4>Wednesday September 7</h4>
                    <ul>
                        <li>Main menu now includes a link to the signup page under every game</li>
                        <li>The page you are on inside the game menu is now highlighted</li>
                        <li>Keeper and Tenman league created</li>
                    </ul>

                    <h4>Tuesday September 6</h4>
                    <ul>
                        <li>Fixed issue that was preventing a new user to be created</li>
                        <li>Fixed login authentication issue</li>
                    </ul>

                    <h4>Monday September 5</h4>
                    <ul>
                        <li>Significant performance improvements especially in Salary Cap roster page</li>
                    </ul>
                </div>
            </div> <!-- innner content -->
        </div> <!-- content -->
    </div> <!-- container -->

</body>
</html>
