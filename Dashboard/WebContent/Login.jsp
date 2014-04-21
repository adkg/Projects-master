<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<%
		response.setHeader( "Pragma", "no-cache" );
		response.setHeader( "Cache-Control","no-store, no-cache");
		response.setDateHeader( "Expires", 0 );
		
		String userId="";
		if(request.getSession(false).getAttribute("email_id")!= null)
		{ 
			response.sendRedirect("checkInReport.jsp");
		}		
%>

<!DOCTYPE HTML>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<script type="text/javascript" src="./common/common_login/js/form_init.js" data-name=""
	id="form_init_script">
	
</script>
<link rel="stylesheet" type="text/css"
	href="./themes/theme_login/default_responsive/css/default.css?version=903" id="theme" />

<title>login</title>
</head>

<body>

	<style type="text/css">
#docContainer .fb_cond_applied {
	display: none;
}
</style>
	<noscript>
		<style type="text/css">
#docContainer .fb_cond_applied {
	display: inline-block;
}
</style>
	</noscript>
	
	<form style="BACKGROUND-COLOR: rgb(246, 246, 246);" id="docContainer"
		class="fb-toplabel fb-100-item-column selected-object"
		method="post" action="Login-Servlet"
		data-form="manual_iframe">
		
		
		<div style="MIN-HEIGHT: 20px" id="fb-form-header1"
			class="fb-form-header">
			<a style="MAX-WIDTH: 104px" id="fb-link-logo1" class="fb-link-logo"
				href="" target="_blank"><img style="WIDTH: 100%; DISPLAY: none"
				id="fb-logo1" class="fb-logo" title="Alternative text"
				alt="Alternative text" src="common/images/image_default.png" /></a>
		</div>
		<div id="section1" class="section">
			<div id="column1" class="column ui-sortable">
				<div style="MIN-HEIGHT: 200px; DISPLAY: none" id="fb_confirm_inline">
				</div>
				<div style="DISPLAY: none" id="fb_error_report"></div>
				<div style="FILTER:" id="item7" class="fb-item fb-100-item-column">
					<div class="fb-header">
						<h2 style="DISPLAY: inline">Login - Check In Report</h2>
					</div>
				</div>
				<div style="HEIGHT: 10px" id="item6"
					class="fb-item fb-100-item-column">
					<div class="fb-spacer">
						<div id="item6_div_0"></div>
					</div>
				</div>
				<div
					style="FILTER:; PADDING-BOTTOM: 0px; PADDING-LEFT: 500px; PADDING-TOP: 0px"
					id="item4" class="fb-item fb-33-item-column">
					<div class="fb-static-text fb-item-alignment-center">
						<p
							style="FONT-STYLE: italic; FONT-FAMILY: georgia; COLOR: #000000; FONT-WEIGHT: normal">
							Stbeehive Email id and Password</p>
					</div>
				</div>
				
				
					
				<div class="myerror">
					<c:if test="${requestScope.login_error != null}">
						<br/>
						<p class="myerror">${requestScope.login_error}</p>
						<br/>
					</c:if>
				</div>
				
				
				
				<div id="item2" class="fb-item">
					<div class="fb-sectionbreak">
						<hr style="MAX-WIDTH: 960px">
					</div>
				</div>
				
			
				
				
				<div style="FILTER:; PADDING-BOTTOM: 0px" id="item1"
					class="fb-item fb-25-item-column">
					<div class="fb-grouplabel">
						<br/>
						<label style="DISPLAY: inline" id="item1_label_0">Email</label>
					</div>
					<div class="fb-input-box">
						<input id="item1_email_1" class="" maxlength="254" name="email_id"
							autofocus required data-hint="" autocomplete="on"
							placeholder="john.doe@oracle.com" type="email" />
					</div>
				</div>
				<div style="FILTER:; HEIGHT: 1px; PADDING-TOP: 0px" id="item11"
					class="fb-item fb-100-item-column">
					<div class="fb-spacer">
						<div id="item11_div_0"></div>
					</div>
				</div>
				<div style="FILTER:; PADDING-TOP: 0px" id="item10"
					class="fb-item fb-33-item-column">
					<div class="fb-grouplabel">
						<label id="item10_label_0">Password</label>
					</div>
					<div class="fb-input-box">
						<input id="item10_password_1" maxlength="254" type="password"
							name="password" required data-hint="" autocomplete="off"
							placeholder="" />
					</div>
				</div>
			</div>
		</div>
		<div style="MIN-HEIGHT: 1px" id="fb-submit-button-div"
			class="fb-item-alignment-left fb-footer">
			<input
				style='BACKGROUND-IMAGE: ./themes/theme_login/default_responsive/images/btn_submit.png'
				id="fb-submit-button" class="fb-button-special" type="submit"
				data-regular="" value="Submit" />
		</div>
		<input type="hidden" name="fb_form_custom_html" /> <input
			type="hidden" name="fb_form_embedded" /> <input id="fb_js_enable"
			type="hidden" name="fb_js_enable" />
	</form>
</body>
</html>