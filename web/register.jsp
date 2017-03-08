<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="tds.main.control.registerAction"%>
<%@ taglib prefix="tds" uri="tds.taglib" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<un:useConstants var="fields" className="tds.control.registerAction" />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="css/styles.css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/registration.css" media="screen" />
<title>TopDawgSports - Register</title>

</head>

<body>
    <div id="container">
        
        <jsp:include page="inc_header.jsp" />

        <jsp:include page="inc_mainmenu.jsp" />
        
        <jsp:include page="inc_errorMessage.jsp" />
        
        <div class="colmask outside">

            <div class="main">
            
                <div class="leftcolumn">
                    
                    <div id="content">
						
                        <br />
                        <h1>TopDawgSports Registration</h1>
                        <br />
                        <div class="regform formdef">
                            Already a member?  Login to the right.
                        	<div class="newmember">
                                <h3>New Member</h3>
                                <span class="asterisk">*</span> = required field
                                <form action="register.do" method="post">
                                    <table border="0" cellspacing="5" cellpadding="0" width="90%">
                                        <tr>
                                            <td width="35%"></td>
                                            <td width="65%"></td>
                                        </tr>
                                        <tr>
                                            <td align="right"><span class="asterisk">*</span> First Name: </td>
                                            <td>&#160;&#160;&#160;<input type="text" name="frmFirstName" class="formdef" /></td>
                                        </tr>
                                        <tr>
                                            <td align="right"><span class="asterisk">*</span> Last Name: </td>
                                            <td>&#160;&#160;&#160;<input type="text" name="frmLastName" class="formdef" /></td>
                                        </tr>
                                        <tr>
                                            <td align="right"><span class="asterisk">*</span> Username: </td>
                                            <td>&#160;&#160;&#160;<input type="text" name="frmUsername" class="formdef" /></td>
                                        </tr>
                                        <tr>
                                            <td align="right"><span class="asterisk">*</span> Password: </td>
                                            <td>&#160;&#160;&#160;<input type="password" name="frmPassword" class="formdef" /></td>
                                        </tr>
                                        <tr>
                                            <td align="right"><span class="asterisk">*</span> Re-enter Password: </td>
                                            <td>&#160;&#160;&#160;<input type="password" name="frmPassword2" class="formdef" /></td>
                                        </tr>
                                        <tr>
                                            <td align="right"><span class="asterisk">*</span> Email: </td>
                                            <td>&#160;&#160;&#160;<input type="text" name="frmEmail" class="formdef" /></td>
                                        </tr>
                                        <tr>
                                            <td align="right">Send Me Email Results: </td>
                                            <td>&#160;&#160;&#160;<input type="checkbox" name="frmSendAlerts" class="formdef" checked="true" /></td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" align="center"><input type="submit" name="frmSubmit" value="Submit"/></td>
                                        </tr>
                                    </table>
                                </form>
                            </div> <!-- newmember -->
                            
                            <div class="existingmember">
                            	<div class="emailCasey">You will pay the entry fees to Grant Keele after signing up.<br /><br />
                                You can reach Bert by : <br />
                                <br />
                                &#160;&#160;&#160;&#160; Email : grantkeele@gmail.com<br />
                                &#160;&#160;&#160;&#160; Phone : 801-830-2350.</div>
                            </div> <!-- existingmember -->
                            
                        </div> <!-- regform -->
                        
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
