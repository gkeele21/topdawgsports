<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="ct.control.registerAction"%>
<%@ taglib prefix="fas" uri="ct.taglib" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<un:useConstants var="fields" className="ct.control.registerAction" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="css/styles.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/registration.css" media="screen" />
<title>Coltarama - contact us</title>

</head>

<body>
    <div id="container">
        
        <jsp:include page="inc_header.jsp" />

        <jsp:include page="inc_mainmenu.jsp" />
        
        <jsp:include page="inc_submenu.jsp" />

        <jsp:include page="inc_errorMessage.jsp" />
        
        <div class="colmask outside">

            <div class="main">
            
                <div class="leftcolumn">
                    
                    <div id="content">
						
                        <br />
                        <h1>Coltarama Contact Information</h1>
                        <br />
                        If you have any problems with the website, or suggestions you'd like to make, feel free to contact us.<br /><br />
                        <table border="0" width="80%" cellpadding="1" cellspacing="1" class="emailCasey">
                            <tr>
                                <td width="5%"></td>
                                <td width="95%"></td>
                            </tr>
                            <tr>
                                <td colspan="2">WebMaster : Grant Keele</td>
                            </tr>
                            <tr>
                                <td align="right"></td>
                                <td>Email : <a href="mailto:grantkeele@gmail.com">grantkeele@gmail.com</a></td>
                            </tr>
                            <tr>
                                <td colspan="2"><img src="images/spacer.gif" height="10" /></td>
                            </tr>
                            <tr>
                                <td colspan="2">Engineer : Bert Keele</td>
                            </tr>
                            <tr>
                                <td align="right"></td>
                                <td>Email : <a href="mailto:bertkeele@gmail.com">bertkeele@gmail.com</a></td>
                            </tr>
                            <tr>
                                <td colspan="2"><img src="images/spacer.gif" height="10" /></td>
                            </tr>
                        </table>
                        
                    </div> <!-- content -->

                </div> <!-- left column -->
                <div class="rightcolumn">

                    <jsp:include page="inc_rightsidebar.jsp" />
                    
                </div> <!-- right column -->
            </div> <!-- main -->
        </div> <!-- colmask outside -->

        <jsp:include page="inc_footer.jsp" />
        
    </div> <!-- container -->
</body>
</html>
