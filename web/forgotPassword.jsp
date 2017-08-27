<%-- 
    Document   : createUser
    Created on : Aug 17, 2017, 12:36:04 PM
    Author     : grant
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Forgot Password</h1>
        <form action="forgotPassword.do" method="post">
            <label>Username:</label><input type="text" name="username" /><br /><br />
            <br />
            <input type="hidden" name="nextURL" value="forgotPasswordSuccess" />
            <input type="submit" value="Submit">
        </form>
    </body>
</html>
