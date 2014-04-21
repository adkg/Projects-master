<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Videodoop</title>
	<link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>

	<header id="site_head">
		<div class="header_cont">
        	<h1><a href="#">Videodoop</a></h1>          
                <nav class="head_nav">
                <ul>
                    <li><a href="#">Doop it!</a></li>
                    <li><a href="#">About Us</a></li>
                </ul>
                </nav>        
		</div>
	</header>
	<%	Map outmap = (Map)request.getAttribute("map");%>
	
	<div id="main_content">
		<center>
		<div class="result">
			<% if (!outmap.isEmpty()){%>
				<table border="0" cellpadding="20">
				<fieldset id="field">
				<legend align="center"> Results </legend>
				<%  for (Iterator iter = outmap.keySet().iterator(); iter.hasNext();) {
				       	String key = (String) iter.next();
						out.println("<tr><td>"+key.substring(0,key.length()-3)+"."+key.substring(key.length()-3)+"</td>"+"<td>"+outmap.get(key)+"</td></tr>");
				}%>
				<%out.println("</fieldset></table>");%>		
			<% } else out.println("Sorry no match found!!!");%>			
		</div>
		</center>
	</div>

</body>
</html>