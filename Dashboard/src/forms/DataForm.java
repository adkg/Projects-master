package forms;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataForm {

	
	private String txnDate;
	private Date txnJavaDate;
	private String reasonCheckin;
	private String descChange;
	private String testSuite; // Topology impacted
	private String bugNumber;
	private String mergeLog;
	private String txnName;
	private String txnType;
	private String testCaseListing;
	private int testCount;
	private String oraLink;	
	private String txnReviewers;
	private String emailId;	
	private String subjectSeries = "";	
	private String labelMerged = "";
	
	
	
	public void setLabelMerged(String labelMerged) {
		this.labelMerged = labelMerged;
	}

	public String getLabelMerged() {
		return labelMerged;
	}
	
	public String getDescChange() {
		return descChange;
	}

	public void setDescChange(String descChange) {
		this.descChange = descChange;
	}

	public String getSubjectSeries()
	{
		return subjectSeries;
	}	
	
	public String getBugNumber() {
		return bugNumber;
	}
	public void setBugNumber(String string) {
		this.bugNumber = string;
	}
	public String getReasonCheckin() {
		return reasonCheckin;
	}
	public void setReasonCheckin(String reasonCheckin) {
		this.reasonCheckin = reasonCheckin;
	}
	public String getTestSuite() {
		return testSuite;
	}
	public void setTestSuite(String testSuite) {
		this.testSuite = testSuite;
	}
	public String getTestCaseListing() {
		return testCaseListing;
	}
	public void setTestCaseListing(String testCaseListing) {
		this.testCaseListing = testCaseListing.replaceAll("\\n","<br/>");
	}
	public String getMergeLog() {		
		return mergeLog;
	}
	public void setMergeLog(String mergeLog) {
		
		try
		{
			String temp = mergeLog.substring(mergeLog.indexOf("View restored to label ["));		
			//labelMerged = temp.substring(temp.indexOf("WORKP"),temp.indexOf("]"));		
			subjectSeries = "Check-in Report : ["+temp.substring(temp.indexOf(".")+1,temp.indexOf("_G"))+"] ";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.mergeLog = mergeLog.replaceAll("\\n", "<br/>");
	}

	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getTxnDate() {
		return txnDate;
	}
	public Date getTxnJavaDate() {
		return txnJavaDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
		DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
		try {
			txnJavaDate = df.parse(txnDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error Treated by Null");
			txnJavaDate = null;
		}
	}
	public String getTxnReviewers() {
		return txnReviewers;
	}
	public void setTxnReviewers(String txnReviewers) {
		this.txnReviewers = txnReviewers;
	}
	public String getTxnName() {
		return txnName;
	}
	public void setTxnName(String txnName) {
		this.txnName = txnName;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public int getTestCount() {
		return testCount;
	}
	public void setTestCount(int testCount) {
		this.testCount = testCount;
	}
	public String getOraLink() {
		return oraLink;
	}
	public void setOraLink(String oraLink) {
		this.oraLink = oraLink;
	}
	
}
