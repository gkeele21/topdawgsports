<%@ page import="tds.main.bo.UserSession"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${!empty emsg}">
    <div id="error" colspan="6">
        &nbsp;&nbsp;<c:out value="${emsg}" /><% tds.main.bo.UserSession.getUserSession(request, response).clearErrorMessage(); %>
    </div>
</c:if>