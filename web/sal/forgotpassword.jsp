<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="ksal.util.FSUtils"%>
<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="flag" uri="ksal.taglib" %>

<html><!-- InstanceBegin template="/Templates/master.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
<title>TopDawgSports Salary :: Fantasy Football</title>
<!-- InstanceEndEditable --><link href="css/master.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="js/script.js">&#160;</script>
</head>

<body bgcolor="#333333" topmargin="0">
<table width="772" height="100%" align="center" id="greyfill" cellpadding="0" cellspacing="0" border="0">
	<tr>
    	<td valign="top">
			<table width="770" align="center" bgcolor="#000000" cellpadding="0" cellspacing="0"  border="0">
                <!-- Header/Login Start -->
                <jsp:include page="inc_header.jsp"  />
                <!-- Header/Login End -->

                <!-- Global Navigation Start -->
                <jsp:include page="inc_navigation.jsp" />
                <!-- Global Navigation End -->
				
				<!-- Global Navigation Start -->
                <jsp:include page="inc_subnavigation.jsp" />
                <!-- Global Navigation End -->
				
				<!-- InstanceBeginEditable name="banner" -->
                <jsp:include page="inc_errorMessage.jsp" />
				<!-- InstanceEndEditable -->
				<!-- InstanceBeginEditable name="body" -->
              <% Map oldInputs = (Map)request.getAttribute("oldInputs");
                  if (oldInputs == null) {
                      oldInputs = new HashMap();
                  }
              %>

                <tr>
                  <td colspan="6" align="center" id="whitefill">
				  	<table width="660" cellpadding="0" cellspacing="0" border="0">
						<tr><td height="30" colspan="2"><img src="images/spacer.gif"></td></tr>
						<tr>
							<td width="660">
                                <form action="<c:url value="/fsa/forgotpassword.do" />" method="post" >
                                <table cellpadding="0" cellspacing="0" border="0">
									<tr>
									  <td colspan="3"><span class="title_black_15">Forgot Username/Password </span></td>
									</tr>
									<tr><td height="10" colspan="3"><img src="images/spacer.gif"></td></tr>
									<tr>
										<td width="320" valign="top">
											<table width="320" cellpadding="0" cellspacing="0" border="0" align="center">
												<tr>
													<td align="center" id="subbackfill" colspan="3" height="30"><span class="title_black">Forgot Your Password</span></td>
												</tr>
												<tr><td height="2" colspan="3"><img src="images/spacer.gif"></td></tr>
												<tr>
													<td width="10" id="tdfill"><img src="images/spacer.gif"></td>
													<td height="100" id="tdfill" valign="middle"> Happens all the time. Just enter your Username into the text box below, and we'll email your password to you.
													  <p align="center"><input name="txtUsername" type="text" class="form" size="35" maxlength="35"> 
										<input type="submit" value="Send" /></p></td>
													<td width="10" id="tdfill"><img src="images/spacer.gif"></td>
												</tr>
											</table>
										</td>
										<td width="20"><img src="images/spacer.gif"></td>
										<td width="320" valign="top">
											<table width="320" cellpadding="0" cellspacing="0" border="0" align="center">
												<tr>
													<td align="center" id="subbackfill" colspan="3" height="30"><span class="title_black">Forgot Your Username </span></td>
												</tr>
												<tr><td height="2" colspan="3"><img src="images/spacer.gif"></td></tr>
												<tr>
													<td width="10" id="tdfill"><img src="images/spacer.gif"></td>
													<td height="100" id="tdfill" valign="middle"> Enter your email address in the box below and we'll send you the Username. Note: Email must match address on record.
												      <p align="center"><input name="txtEmail" type="text" class="form" size="35" maxlength="35"> <input type="submit" value="Send" /></p></td>
													<td width="10" id="tdfill"><img src="images/spacer.gif"></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr><td height="10" colspan="3"><img src="images/spacer.gif"></td></tr>
									<tr>
									  <td colspan="3">If you are still having trouble logging in, <a href="mailto:grantkeele@gmail.com">email us</a> and we'll work with you to fix the problem.</td>
									</tr>
									
									<tr><td height="25" colspan="3"><img src="images/spacer.gif"></td></tr>
							  </table>
								</form>
                            </td>
						</tr>
					</table>
				  </td>
			    </tr>
				<!-- InstanceEndEditable -->
				<jsp:include page="inc_bottomticker.jsp" />
				<!-- InstanceBeginEditable name="ticker" -->

				<!-- InstanceEndEditable -->

				<!-- InstanceBeginEditable name="spotlight" -->
				<tr>
                  <td height="25" colspan="6" valign="middle">&nbsp;&nbsp;<img src="images/button_highroller.gif" width="125" height="17" align="absmiddle">&nbsp;&nbsp;<img src="images/button_tailgator.gif" align="absmiddle"></td>
			    </tr>
				<!-- InstanceEndEditable -->
				<jsp:include page="inc_footer.jsp" />

	    </table>
	 </td>
	</tr>
	
	<tr>
	  	<td><img src="images/spacer.gif"></td>
	</tr>
</table>
<span class="title_white">
<c:if test="${session.debug == 'resp'}">
    <hr />
    <h3>DEBUG INFO</h3>
    <hr />
</c:if>
</span>
<flag:debug type="headers" cssKey="class" cssValue="title_white"/>
<flag:debug type="cookies" cssKey="class" cssValue="title_white"/>
<flag:debug type="params" cssKey="class" cssValue="title_white"/>
<flag:debug type="requestScope" cssKey="class" cssValue="title_white"/>
<flag:debug type="requestInfo" cssKey="class" cssValue="title_white"/>
<flag:debug type="pageScope" cssKey="class" cssValue="title_white"/>
<flag:debug type="sessionScope" cssKey="class" cssValue="title_white"/>
<flag:debug type="applicationScope" cssKey="class" cssValue="title_white"/>
</body>
<!-- InstanceEnd --></html>
