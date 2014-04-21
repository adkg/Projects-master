package logic;

public class EmailGenerator {
	
	StringBuffer emailHTML;
	
	private String defaulHeader = 
			"<html>"
					+"\n  <head>"
					+"\n    <meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\">"
					+"\n  </head>"
					+"\n  <body text=\"#000000\" bgcolor=\"#FFFFFF\">"
					+"\n    <span style=\"color:#1F497D\"><o:p>&nbsp;</o:p></span>"
					+"\n    <div class=\"moz-forward-container\">"
					+"\n      <div class=\"WordSection1\"><br>"
					+"\n        <table class=\"MsoNormalTable\" style=\"border-collapse:collapse\""
					+"\n          border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
					+"\n          <tbody>";
	
	
	private String defaulFooter = 
			"</tbody>"
					+"\n        </table>"
					+"\n        <p class=\"MsoNormal\"><o:p>&nbsp;</o:p></p>"
					+"\n      </div>"
					+"\n      <br>"
					+"\n    </div>"
					+"\n    <br>"
					+"\n  </body>"
					+"\n</html>";
	
	private String rowDetailBeforeKey = 
			"<tr>"
					+"\n              <td style=\"width:100.25pt;border:solid windowtext 1.0pt;background:#DAEEF3;padding:0in 5.4pt 0in 5.4pt\""
					+"\n                valign=\"top\" width=\"134\">"
					+"\n                <p class=\"MsoNormal\"><b><span style=\"color:#0070C0\">";
	
	
	private String rowDetailAfterKey =
			"<o:p></o:p></span></b></p>"
					+"\n              </td>"
					+"\n              <td style=\"width:884.85pt;border:solid windowtext"
					+"\n                1.0pt;border-left:none;padding:0in 5.4pt 0in 5.4pt\""
					+"\n                valign=\"top\" width=\"1180\">"
					+"\n                <p class=\"MsoNormal\">";
					
	private String rowDetailAfterValue =			
			"<o:p></o:p></p>"
					+"\n              </td>"
					+"\n            </tr>";
	
	
	public EmailGenerator()
	{
		emailHTML = new StringBuffer("");
		emailHTML.append(defaulHeader);
	}
	
	public void addRow(String key,String value)
	{
		emailHTML.append(rowDetailBeforeKey);
		emailHTML.append(key);
		emailHTML.append(rowDetailAfterKey);
		emailHTML.append(value);
		emailHTML.append(rowDetailAfterValue);	
	}
	
	
	public String endMail()
	{
		emailHTML.append(defaulFooter);
		return emailHTML.toString();
	}
	
}
