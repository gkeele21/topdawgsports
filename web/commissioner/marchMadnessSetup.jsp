<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <title>Commissioner - March Madness Setup</title>
        <style type="text/css">
        </style>
    </head>

    <body>
        <h2>Steps for March Madness Tournament Creation</h2>
        <p>1. Enter the Names of the Regions</p>
        <form action="marchMadnessSetup.do">                
            <table>
                <tr>
                    <td>Region 1:</td>
                    <td><input type="text" name="region1Name" /></td>
                    <td>(Top Left)</td>
                </tr>
                <tr>
                    <td>Region 2:</td>
                    <td><input type="text" name="region2Name" /></td>
                    <td>(Bottom Left)</td>
                </tr>
                <tr>
                    <td>Region 3:</td>
                    <td><input type="text" name="region3Name" /></td>
                    <td>(Top Right)</td>
                </tr>
                <tr>
                    <td>Region 4:</td>
                    <td><input type="text" name="region4Name" /></td>
                    <td>(Bottom Right)</td>
                </tr>
            </table>    
            
            <p>2. Click the magic button to automate the tournament creation</p>
            <button>Create Tournament</button>
            
            <p>3. Enter the March Madness tournament field <a href="marchMadnessField.htm">here</a></p>
        </form>        
    </body>
</html>