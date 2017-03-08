<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>testBracket</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="css/topDawg.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/topDawgMenu.css" media="screen" />
    <style type="text/css">
        h1
        {
            text-align: center;
        }
        div.contentLeft
        {
            float: left;
            width: 50%;
            margin: 0;
            padding: 1em;
        }
        div.contentRight
        {
            margin-left: 50%;
            border-left: 1px solid gray;
            padding: 1em;
        }
        .ninety
        {
            width: 95%;
            margin-left:auto;
            margin-right:auto;
        }
        .floatLeft
        {
            float: left;
            margin-left:auto;
            margin-right:auto;
            border-left: 1px solid gray;
            padding: 1em;
            width: 26%;
            text-align: center;
        }
        .alignLeft
        {
            text-align: left;
        }
        .team1
        {
            border-bottom-style: solid;
            border-bottom-width: 100%;
            border-bottom-color: black;
        }
        .teamSeed
        {
            float:left;
            width: 10%;
        }
        .teamName
        {
            text-align: center;
            width: 90%;
        }
        .score
        {
            border-right-style: solid;
            border-right-color: black;
            vertical-align: middle;
            text-align: center;
            line-height: 49px;
        }
        .bracketRound1
        {
            height: 90px;
        }

    </style>
  </head>
  <body>

    <div id="container">
        <div id="header">
            <div id="innerHeader">
                <img src="images/banner.png" alt=""/>
            </div>
        </div> <!-- header -->

        <div id="mainMenu">
            <jsp:include page="menu/mainMenu.jsp" />
        </div><!-- main menu -->

        <div id="leftMenu">

            <div id="innerLeftMenu">
                <jsp:include page="menu/inc_leftMenu.jsp" />
            </div>
        </div>

        <div id="content">
            <div id="innerContent">

                <h1>Bracket Test</h1>
                <br />
                <div class="floatLeft">
                    
                    <tds:bracket bracketHeight="200"
                                 bracketWidth="400"
                                 position="right" />


                    <br /><br />
                    <div id="wholeBracket" style="height: 70px;">
                        <div id="team1Div" style="border-bottom-style: solid; border-bottom-width: 100%; border-bottom-color: black;">
                            <span style="float:left; width: 10%;">1</span>
                            <span style="text-align: center; width: 90%;">BYU</span>
                        </div>
                        <div id="scoreDiv" style="border-right-style: solid; border-right-color: black; vertical-align: middle; text-align: center; line-height: 49px;">
                            &#160;&#160;&#160;&#160;67 - 57
                        </div>
                        <div id="team2Div" style="border-bottom-style: solid; border-bottom-width: 100%; border-bottom-color: black; border-right-style: solid; border-right-color: black;">
                            <span style="float:left; width: 10%;">16</span>
                            <span style="text-align: center; width: 90%;">George Washington</span>
                        </div>
                    </div>
                </div>
                
            </div> <!-- inner content -->
        </div> <!-- content -->

    </div> <!-- container-->

  </body>
</html>


