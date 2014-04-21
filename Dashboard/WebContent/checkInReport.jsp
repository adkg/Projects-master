<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);

	if (request.getSession(false).getAttribute("email_id") == null) {
		response.sendRedirect("Login.jsp");
	}
%>


<!DOCTYPE HTML>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WebForm Oracle Checkin Report Webcenter</title>

<script type="text/javascript"
	src="./common/common_checkin/js/form_init.js" id="form_init_script"
	data-name=""> </script>

<link rel="stylesheet" type="text/css"
	href="./themes/theme_checkin/sky/css/default.css?version=74" id="theme" />	



<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />

<script src="http://code.jquery.com/jquery-1.9.1.js"
	type="text/javascript"></script>
<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"
	type="text/javascript"></script>
<link rel="stylesheet" href="/resources/demos/style.css" />
<script type="text/javascript">
	$(function() {
		$("#datepicker").datepicker();
	});
</script>

</head>

<body onload="init()">
	<script src="./common/common_checkin/js/checkinReport.js"
		type="text/javascript"></script>
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

	<form id="docContainer"
		class="fb-100-item-column fb-leftlabel selected-object" method="POST"
		action="logic-servlet">

		<div style="MIN-HEIGHT: 20px" id="fb-form-header1"
			class="fb-form-header">
			<a id="fb-link-logo1" class="fb-link-logo" href="" target="_blank"><img
				style="DISPLAY: none" id="fb-logo1" class="fb-logo"
				title="Alternative text" alt="Alternative text"
				src="common/images/image_default.png" /></a>
		</div>


		<div id="section1" class="section">
			<div id="column1" class="column ui-sortable">

				<div id="item4" class="fb-item fb-100-item-column">
					<div class="fb-header">
						<h2 style="DISPLAY: inline">Checkin Report</h2>
					</div>
				</div>

				<br /> <br /> <br />
				
				
				
<!-- Check in date -->
				
				<div class="myerror">
					<c:if test="${requestScope.errorMap['check_in_date'] != null}">
						<br />
						<p class="myerror">${requestScope.errorMap['check_in_date']}</p>

					</c:if>
				</div>

				<div id="item5" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">

						<label style="DISPLAY: inline" id="item5_label_0">Check-in
							Date</label>
					</div>
					<div class="fb-input-date">
						<input id="datepicker" class="datepicker" name="check_in_date"
							data-hint="mm/dd/yyyy" placeholder="mm/dd/yyyy" required
							type="text" readonly autocomplete="off" />
					</div>
				</div>


<!-- Reason for Check-in-->

				<div id="item18" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">
						<label style="DISPLAY: inline" id="item18_label_0"> Reason
							for Check-in </label>
					</div>
					<div class="fb-input-box">
						<input id="item18_text_1" maxlength="254"
							name="reason_for_checkin"
							placeholder="E.g. Fixed 1 tests in Favorites and 2 tests in SRM"
							autocomplete="on" data-hint="" type="text"
							value="${dataform.reasonCheckin}" 
							required />
					</div>
				</div>


<!-- Description of Change-->

				<div id="item204" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">
						<label style="DISPLAY: inline" id="item204_label_0">Description
							of Change</label>
					</div>
					<div class="fb-textarea">
						<textarea style="resize: none" id="item204_textarea_1"
							name="description_change"
							placeholder="Briefly describe what changes you made" data-hint=""
							maxlength="10000" cols="" rows="" 
							required value="${dataform.descChange}"></textarea>
					</div>
				</div>
				
				
<!-- Test Suite/Topology Impacted -->


				<div id="item23" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">
						<label style="DISPLAY: inline" id="item23_label_0">Topology Impacted</label>
					</div>
					<div class="fb-input-box">
						<input id="item23_text_1" maxlength="254" name="test_suite"
							placeholder="E.g. LRG_WC_SECURITY_FRAMEWORK" autocomplete="on" data-hint=""
							type="text" required value="${dataform.testSuite}" />
					</div>
				</div>



<!-- Bug Number -->

				<div style="FILTER:" id="item17" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">
						<label style="DISPLAY: inline" id="item17_label_0">Bug #</label>
					</div>
					<div class="fb-input-box">
						<input id="item17_text_1" maxlength="254" name="bugno"
							placeholder="Bug Number" autocomplete="on" data-hint="Bug Number"
							type="text" value="${dataform.bugNumber}" />
					</div>
				</div>
				
				
<!-- End Merge Log/Files affected-->

				<div id="file_affecteddiv" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">
						<label style="DISPLAY: inline" id="item24_label_0">Files
							Affected</label>
					</div>
					<div class="fb-textarea">
						<textarea style="resize: vertical" id="file_affectedid"
							name="mergeLog" placeholder="End Merge log to be pasted here"
							data-hint="" maxlength="10000" cols="" rows="50"
							required value="${dataform.mergeLog}"></textarea>
					</div>
				</div>


<!-- Transaction Name Auto Generated -->

				<div id="item20" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">

						<label style="DISPLAY: inline" id="item20_label_0">Transaction
							Name</label>
					</div>
					<div class="fb-input-box">
						<input id="txn_nameid" maxlength="254" name="transaction_name"
							placeholder="Auto generated from endmerge Log: Name of transaction merged" autocomplete="on"
							data-hint="" type="text" required value="${dataform.txnName}" />
					</div>
				</div>
				
				
<!-- Transaction Type -->

				<c:if test="${requestScope.errorMap['transaction_type'] != null}">
					<br />
					<p class="myerror">
						${requestScope.errorMap['transaction_type']}</p>
					<br />
				</c:if>



				<div style="FILTER:" id="item6" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">

						<label style="DISPLAY: inline" id="item6_label_0">Transaction
							Type</label>
					</div>
					<div class="fb-dropdown">
						<select id="item6_select_1" name="transaction_type" data-hint=""
							required>
							<option id="item6_1_option" value="New Tests">New Tests
							</option>
							<option id="item6_2_option" value="Fixes" selected>
								Fixes</option>
							<option id="item6_3_option" value="Other">Other</option>
						</select>
					</div>
				</div>
				
				


<!-- Test Listing-->

				<div style="FILTER:" id="testcase_listingdiv" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">
						<label style="DISPLAY: inline" id="item19_label_0">Testcase
							Listing</label>
					</div>
					<div class="fb-textarea">
						<textarea style="resize: vertical" id="testcaselisting_itemid"
							name="testcaseListing"
							placeholder="Name of testcases added or modified" data-hint=""
							maxlength="10000" cols="" rows=""
							value="${dataform.testCaseListing}"
							required ></textarea>
					</div>
				</div>
				
<!-- Test Count AuoGenerated-->

				<div style="FILTER:" id="item16" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">

						<label style="DISPLAY: inline" id="item16_label_0">Test
							Count </label>
					</div>
					<div class="fb-input-box">
						<input id="test_countid" maxlength="254" name="test_count"
							placeholder="Auto Generated from Testcase Listing" autocomplete="on" 
								data-hint="Auto Generated from Testcase Listing" type="text" readonly value="${dataform.testCount}" />
					</div>
				</div>				


<!-- Orareview Link -->

				<c:if test="${requestScope.errorMap['orareview'] != null}">
					<br />
					<p class="myerror">${requestScope.errorMap['orareview']}</p>
					<br />
				</c:if>


				<div id="orareview_div" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">

						<label style="DISPLAY: inline" id="item21_label_0">Orareview
							Link</label>
					</div>
					<div class="fb-input-box">
						<input id="ora_reviewid" maxlength="254" name="orareview"
							placeholder="Orareview link" autocomplete="on" data-hint="Orareview link"
							type="text" required value="${dataform.oraLink}" />
					</div>
				</div>


<!-- Reviewers -->

				<c:if test="${requestScope.errorMap['reviewers'] != null}">
					<br />
					<p class="myerror">${requestScope.errorMap['reviewers']}</p>
					<br />
				</c:if>

				<div style="FILTER:" id="item22" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">

						<label style="DISPLAY: inline" id="item22_label_0">Reviewers</label>
					</div>
					<div class="fb-input-box">
						<input id="item22_text_1" maxlength="254" required
							name="reviewers" placeholder="Transaction reviewers"
							autocomplete="on" data-hint="Transaction reviewers" type="text"
							value="${dataform.txnReviewers}" />
					</div>
				</div>

<!-- Label Merged Autogenerated -->

				<div style="FILTER:" id="item16" class="fb-item fb-100-item-column">
					<div class="fb-grouplabel">

						<label style="DISPLAY: inline" id="item16_label_0">Label Merged</label>
					</div>
					<div class="fb-input-box">
						<input id="label_mergedid" maxlength="254" name="label_merged"
							placeholder="Auto Generated from Testcase Listing" 
							data-hint="Auto Generated from Testcase Listing" type="text" readonly value="${dataform.labelMerged}" />
					</div>
				</div>			





			</div>
		</div>
		<div style="DISPLAY: none; CURSOR: default" id="fb-captcha_control"
			class="fb-captcha fb-item-alignment-center">
			<img
				src="file:///C:/Documents%20and%20Settings/rvkulkar/Local%20Settings/Temp/FormBuilder/editordata/images/recaptchawhite.png"
				alt="" />
		</div>
		<div style="MIN-HEIGHT: 1px" id="fb-submit-button-div"
			class="fb-item-alignment-left fb-footer">


			<input id="fb-submit-button" class="fb-button-special" type="submit"
				data-regular="/theme/sky/images/btn_submit.png" />
		</div>
	</form>
</body>
</html>