<%@ page import="ksal.util.Log"%>
<%@ page contentType="text/html" %>
<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="flag" uri="ksal.taglib" %>

<html><!-- InstanceBegin template="/Templates/master.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
<title>Coltarama Salary :: Fantasy Football</title>
<!-- InstanceEndEditable --><link href="css/master.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="js/script.js">&#160;</script>
    <%
        if (exception!=null) {
            Log.error((Exception)exception);
        }
    %>
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
				<tr>
					<td colspan="6" valign="top">
						<table width="770" cellpadding="0" cellspacing="0"  border="0">
							<tr id="whitefill"><td height="20" colspan="3"><img src="images/spacer.gif"></td></tr>
							<tr id="whitefill">
								<td width="50">&nbsp;</td>
								<td class="text"><p class="title_black_15">404 ERROR:</p>
							    <p>..or in other words: “Ah crap.”</p>

                                <p>Sorry. The page you’re looking for seems to have gone missing. If you've got the time, <a href="mailto:fsa@frameaction.com">send us</a> a threatening email and tell us where the problem lies. That way we can fix it. </p>
                              <p>If you would rather get back to the Action, head on over to the <a href="http://www.fantasysportsaction.com">FSA homepage</a> or go <a href="javascript:history.back();">back to the last page you were on</a>.</p>
                              <p>Thanks<br>FSA Dev Team </p>                             </td>
								<td width="50"><img src="images/spacer.gif"></td>
							</tr>
					  </table>
				  </td>
				</tr>
				<tr><td height="30" bgcolor="#FFFFFF" colspan="6"><img src="images/spacer.gif"></td></tr>
				<!-- InstanceEndEditable -->
				<!-- InstanceBeginEditable name="body" -->
				
				<!-- InstanceEndEditable -->
				<jsp:include page="inc_bottomticker.jsp" />
				<!-- InstanceBeginEditable name="ticker" -->
				
				<tr><td height="1" colspan="6"><img src="images/line_grey.gif" width="770" height="1"></td></tr>
				<!-- InstanceEndEditable -->
				<jsp:include page="inc_spotlight.jsp" />
				<!-- InstanceBeginEditable name="spotlight" -->
				
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
