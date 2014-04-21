package servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import forms.DataForm;

import logic.DatabaseAccess;
import logic.EmailGenerator;
import logic.FormValidator;
import logic.SendMailSSL;

import beans.TableDataBean;


/**
 * Servlet implementation class LogicServlet
 */

@WebServlet("/logic-servlet")
public class LogicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Connection connect;

	
	public void init(ServletConfig c) throws ServletException {
		connect = getDatabaseConnection();
	}


	public void destroy() {
		try {
			if (!connect.equals(null))
				connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getDatabaseConnection() {
		Connection connect;
		try {
			// Load sql drivers
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:oracle:thin:wc_qa/welcome1@//adc6260181.us.oracle.com:1521/orcl.us.oracle.com");
		} catch (Exception e) {
			e.printStackTrace();
			connect = null;
		}
		return connect;
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Properties formProp = new Properties();
	
		synchronized(session) {
			
			DataForm dataform = new DataForm();		
			Boolean errorMail = false;
			
			
			/**
			 * Update datafrom for validation
			 */
			
			dataform.setTxnDate(request.getParameter("check_in_date"));
			dataform.setReasonCheckin(request.getParameter("reason_for_checkin"));		
			dataform.setDescChange(request.getParameter("description_change"));
			dataform.setTestSuite(request.getParameter("test_suite"));
			dataform.setBugNumber(request.getParameter("bugno"));
			dataform.setMergeLog(request.getParameter("mergeLog"));
			dataform.setTxnName(request.getParameter("transaction_name"));
			dataform.setTxnType(request.getParameter("transaction_type"));
			dataform.setTestCaseListing(request.getParameter("testcaseListing"));
			
			try{
				int temp;
				temp = Integer.parseInt(request.getParameter("test_count"));
				dataform.setTestCount(temp);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				dataform.setTestCount(-1);
			}	
						
			dataform.setOraLink(request.getParameter("orareview"));
			dataform.setTxnReviewers(request.getParameter("reviewers"));
			dataform.setLabelMerged(request.getParameter("label_merged"));
			
			/**
			 * validate form
			 */
					
			FormValidator validator = new FormValidator();
			Map<String,String> errorMap = validator.validate(dataform);
			
			
			
			/**
			 * Update Bean and database and then send mail
			 */
			String address;
			if(errorMap.isEmpty())
			{			
				TableDataBean dataBean = (TableDataBean)session.getAttribute("dataBean");
				if(dataBean==null)
				{
					dataBean = new TableDataBean();
					session.setAttribute("dataBean", dataBean);
				}
				
				dataBean.setOraLink(dataform.getOraLink());
				dataBean.setTestCount(dataform.getTestCount());
				dataBean.setTxnType(dataform.getTxnType());
				dataBean.setTxnName(dataform.getTxnName());
				dataBean.setTxnReviewers(dataform.getTxnReviewers());
				dataBean.setTxnDate(dataform.getTxnJavaDate());
				dataBean.setTestSuite(dataform.getTestSuite());
				dataBean.setTestCaseListing(dataform.getTestCaseListing());
				dataBean.setBug(dataform.getBugNumber());
				
				DatabaseAccess dbAccess = new DatabaseAccess(connect,dataBean);				
				try{
					dbAccess.writeDataBase();
					
					EmailGenerator emailGen = new EmailGenerator();
					emailGen.addRow("Check in Date:", dataform.getTxnDate());
					emailGen.addRow("Type of Txn:",dataform.getTxnType());					
					emailGen.addRow("Reason for Checkin:",dataform.getReasonCheckin());
					emailGen.addRow("Test Count:",dataform.getTestCount()+"");
					emailGen.addRow("Description of Change:",dataform.getDescChange());
					emailGen.addRow("Topology Impacted:",dataform.getTestSuite());
					emailGen.addRow("Bug No.#",dataform.getBugNumber());					
					emailGen.addRow("Test Case Listing:",dataform.getTestCaseListing());
					emailGen.addRow("ADE End Merge Log:",dataform.getMergeLog());
					emailGen.addRow("Transaction Name:",dataform.getTxnName());
					emailGen.addRow("Reviewers:",dataform.getTxnReviewers());
					emailGen.addRow("Orareview Link:",dataform.getOraLink());
					emailGen.addRow("Label Merged:",dataform.getLabelMerged());
							
					SendMailSSL sendmail = new SendMailSSL(session, dataform);
					errorMail = sendmail.sendMail(emailGen.endMail());		
					System.out.println("Mail Sent");
				}
				catch (Exception e) {				
					e.printStackTrace();
				}	
				
				if (!errorMail)
					address = "/WEB-INF/results/UploadComplete.jsp";
				else
					address = "/Login.jsp";
			}
			else
			{				
				request.setAttribute("errorMap",errorMap);
				request.setAttribute("dataform",dataform);
				address = "/checkInReport.jsp";
			}		
			RequestDispatcher dispatcher =
				        request.getRequestDispatcher(address);
				      dispatcher.forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
