package beans;

import java.io.Serializable;
import java.util.Date;

public class TableDataBean implements Serializable {

	String txnName;
	String txnType;
	int testCount;
	String oraLink;
	Date txnDate;
	String txnReviewers;
	String testSuite;
	String testCaseListing;
	String bug;
	
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
		this.testCaseListing = testCaseListing;
	}
	public String getBug() {
		return bug;
	}
	public void setBug(String bug) {
		this.bug = bug;
	}
	public Date getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Date date) {
		this.txnDate = date;
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
