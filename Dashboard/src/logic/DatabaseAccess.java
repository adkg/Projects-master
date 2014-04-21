package logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import beans.TableDataBean;



public class DatabaseAccess {

	private PreparedStatement preparedStatement;
	private TableDataBean dataBean;
	private Connection connect;
	
	
	public DatabaseAccess(Connection connect, TableDataBean dataBean)
	{
		this.dataBean = dataBean;
		this.connect = connect;
	}
	
	public void writeDataBase() throws Exception {
		try {
			
					//("jdbc:mysql://adc6260181.us.oracle.com:1521", "wc_qa", "welcome1");
			
			System.err.println("Connect is closed? - "+connect.isClosed());
			java.sql.Date sqlDate = new java.sql.Date(dataBean.getTxnDate().getTime());		
			System.out.println(dataBean.getTxnDate().getTime()+"  "+sqlDate.toString());
			
			String query = "insert into  ADE_TXNS_BACKUP "+
						"(transaction,txn_type,test_count,ora_link,txn_date,reviewers,topology,testcases_list,bug)" +
						"values (?,?,?,?,?,?,?,?,?)";
			
			preparedStatement = connect.prepareStatement(query);
			preparedStatement.setString(1,dataBean.getTxnName());
			preparedStatement.setString(2,dataBean.getTxnType());
			preparedStatement.setInt(3, dataBean.getTestCount());
			preparedStatement.setString(4,dataBean.getOraLink());
			preparedStatement.setDate(5,sqlDate);
			preparedStatement.setString(6,dataBean.getTxnReviewers());
			preparedStatement.setString(7,dataBean.getTestSuite());
			preparedStatement.setString(8,dataBean.getTestCaseListing());
			preparedStatement.setString(9,dataBean.getBug());
			
			int result = preparedStatement.executeUpdate();
			
			
			System.out.println("Result value:---"+result);			

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
				
		}
	}

}
