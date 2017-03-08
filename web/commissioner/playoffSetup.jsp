<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <title>Commissioner - Playoff Game Setup</title>
        <style type="text/css">
            p { font-weight: bold; text-transform: uppercase; }
        </style>
    </head>

    <body>
        <h2>Playoff Game Setup</h2>
        <form method="post" action="playoffSetup.do?step=1">
            <p>1. Create a New Tournament & League</p>
            <label>Season ID: </label>
            <input type="text" name="sid" size="3" />            
            <button>Create League</button>
        </form>
        <form method="post" action="playoffSetup.do?step=2">
            <p>2. Create the Playoff Bracket</p>
            <label>FS League ID: </label>
            <input type="text" name="lid" size="3" />
            <label>Starting Season Week ID: </label>
            <input type="text" name="swid" size="3" />
            <label># of Teams (including Average Joe: </label>
            <input type="text" name="nt" size="3" />
            <button>Create Bracket</button>            
        </form>  
    </body>
</html>