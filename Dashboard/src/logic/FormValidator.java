package logic;

import java.util.HashMap;
import java.util.Map;

import forms.DataForm;

public class FormValidator {

	
	public Map<String,String> validate(DataForm dataform)
	{
		Map<String,String> errorMap = new HashMap<String,String> ();
		
		if(dataform.getOraLink() == null || !dataform.getOraLink().contains("orareview")
				|| !dataform.getOraLink().contains("http")
				|| !dataform.getOraLink().contains(".com")
				|| !dataform.getOraLink().contains("oracle")
				|| dataform.getOraLink().equals(""))
			errorMap.put("orareview", "OraReview Link not proper");
		
		if(dataform.getTestCount()==-1)
			errorMap.put("test_count", "Test Count must be entered");
		
		if(dataform.getTxnJavaDate() == null)
			errorMap.put("check_in_date", "Check in date must be entered in mm/dd/yy format");
		
		if(dataform.getTxnName() == null|| dataform.getTxnName().equals(""))
			errorMap.put("transaction_name", "Appropriate Transaction Name must be entered");
		
		if(dataform.getTxnReviewers() == null || dataform.getTxnReviewers().equals(""))
			errorMap.put("reviewers","Reviewers must be entered");
		
		if(dataform.getTxnType() == null || dataform.getTxnType().equals(""))
			errorMap.put("transaction_type", "Transaction type must be selected");
		
		
		return errorMap;
	}
}
