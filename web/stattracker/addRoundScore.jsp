<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>TopDawgSports - Stat Tracker Add Round Score</title>
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

                    <h1>Add Round Score</h1>

                    <form action="addRoundScore.do" method="post">
                        <table>
                            <tr>
                                <td>Round ID:</td>
                                <td><input type="text" name="txtRoundID" /></td>
                            </tr>
                            <tr>
                                <td>Hole ID:</td>
                                <td><input type="text" name="txtHoleID" /></td>
                            </tr>
                            <tr>
                                <td>Score ID:</td>
                                <td><input type="text" name="txtScoreID" /></td>
                            </tr>                            
                            <tr>
                                <td>Strokes:</td>
                                <td><input type="text" name="txtStrokes" /></td>
                            </tr>
                            <tr>
                                <td>GIR:</td>
                                <td><input type="checkbox" name="chkGIR" /></td>
                            </tr>
                            <tr>
                                <td>Putts:</td>
                                <td><input type="text" name="txtPutts" /></td>
                            </tr>
                            <tr>
                                <td>Fairway:</td>
                                <td><input type="checkbox" name="chkFairway" /></td>
                            </tr>
                            <tr>
                                <td>Sand Save Opp:</td>
                                <td><input type="checkbox" name="chkSandSaveOpp" /></td>
                            </tr>
                            <tr>
                                <td>Sand Save Comp:</td>
                                <td><input type="checkbox" name="chkSandSaveComp" /></td>
                            </tr>
                            <tr>
                                <td>Up & Down Opp:</td>
                                <td><input type="checkbox" name="chkUpDownOpp" /></td>
                            </tr>
                            <tr>
                                <td>Up & Down Comp:</td>
                                <td><input type="checkbox" name="chkUpDownComp" /></td>
                            </tr>
                            <tr>
                                <td>Penalty Strokes:</td>
                                <td><input type="text" name="txtPenaltyStrokes" /></td>
                            </tr>
                            <tr>
                                <td>Notes:</td>
                                <td><input type="text" name="txtNotes" /></td>
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