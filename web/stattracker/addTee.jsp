<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - Stat Tracker Add Tee</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgMain.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="../css/topDawgCommon.css" media="screen" />
    <style type="text/css">
        #statTracker table {
            width: 40%;
        }
    </style>
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

                <div id="statTracker">

                    <h1>Add Tee</h1>

                    <form action="addTee.do" method="post">
                        <table>
                            <tr>
                                <td>Golfcourse ID:</td>
                                <td><input type="text" name="txtGolfcourseID" /></td>
                            </tr>
                            <tr>
                                <td>Position:</td>
                                <td><input type="text" name="txtPosition" /></td>
                            </tr>
                            <tr>
                                <td>Tee Name:</td>
                                <td><input type="text" name="txtTeeName" /></td>
                            </tr>
                            <tr>
                                <td>Is Mens Tee?</td>
                                <td><input type="checkbox" name="chkIsMensTee" /></td>
                            </tr>
                            <tr>
                                <td>Rating:</td>
                                <td><input type="text" name="txtRating" /></td>
                            </tr>
                            <tr>
                                <td>Slope:</td>
                                <td><input type="text" name="txtSlope" /></td>
                            </tr>
                            <tr>
                                <td colspan="2"><input type="submit" value="Submit" /></td>
                            </tr>
                        </table>                        
                    </form>
                    
                </div>

            </div> <!-- inner content -->
        </div> <!-- content -->
    </div> <!-- container -->

  </body>
</html>