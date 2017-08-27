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
        <h1>Create New User</h1>
        <form action="createUser.do" method="post">
            <label>First Name:</label><input type="text" name="fName" /><br />
            <label>Last Name:</label><input type="text" name="lName" /><br />
            <label>Email:</label><input type="text" name="email" /><br />
            <label>Username:</label><input type="text" name="username" /><br />
            <label>Password:</label><input type="password" name="pass" /><br />
            <label>Cell:</label><input type="text" name="cell" /><br />
            <br />
            <input type="hidden" name="nextURL" value="userProfile" />
            <input type="submit" value="Submit">
        </form>
    </body>
</html>
