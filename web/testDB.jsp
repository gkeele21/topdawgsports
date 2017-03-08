<%-- 
    Document   : testDB
    Created on : Aug 30, 2011, 11:22:14 AM
    Author     : gkeele
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*" %>
<%@page import="java.io.*" %>
<%@page import="java.util.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        
        <%
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/topdawg", "webuser", "b0tt0mcat");

                Statement stmt = con.createStatement();
                String sql = "select * from Application";

                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String name = rs.getString("ApplicationName");
                    out.println("App Name : " + name);
                }
            } catch (Exception e) {
                out.println("Error : " + e.getMessage());
            }
        %>
    </body>
</html>
