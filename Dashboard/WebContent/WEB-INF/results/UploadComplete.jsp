<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);

	if (request.getSession(false).getAttribute("email_id") == null) {
		response.sendRedirect("Login.jsp");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Result</title>

<link rel="stylesheet" type="text/css"
	href="./common/result.css"/>

<script type="text/javascript">
	
	window.onload = function() {
		var result=confirm("\tCheckin report successfully submitted \n Press Ok to Logout or Cancel to submit another checkin report");
		if (result==true)
		{
		 <%
		 request.getSession(false).removeAttribute("email_id");
		 request.getSession(false).removeAttribute("password_word");
		 session.removeAttribute("dataBean");
		 %>
		 window.location.href=("Login.jsp");  
	
		}
		else
	  	{
			<%
			 session.removeAttribute("dataBean");
			 %>
			 window.location.href=("checkInReport.jsp");			
	  	} 	
	}
	
	function UrlExists(url)
	{
	    var http = new XMLHttpRequest();
	    http.open('HEAD', url, false);
	    http.send();
	    return http.status!=404;
	}
</script>
	
</head>
<body>

<h1>Check in report successfully submitted and mailed</h1>
<br/>
<p>${dataBean.txnName}/<p>
<p>${dataBean.txnType}</p>
<p>${dataBean.testCount}</p>
<p>${dataBean.oraLink}</p>
<p>${dataBean.txnReviewers}</p>

	



</body>
</html>