package logic;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import forms.DataForm;
 
public class SendMailSSL {
	
	//String sendTomailid="rohit.kondekar@oracle.com";
	String sendTomailid="rohit.kondekar@oracle.com";
	HttpSession Psession;
	DataForm dataform;
	
	public SendMailSSL(HttpSession session, DataForm dataform)
	{
		this.Psession = session;
		this.dataform = dataform;
	}
	
	public boolean sendMail(String mailString) {
		
		boolean error = false;
		Properties props = new Properties();
		props.put("mail.smtp.host", "stbeehive.oracle.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication((String)Psession.getAttribute("email_id"),(String)Psession.getAttribute("password_word"));
				}
			});

		try {
 
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress((String)Psession.getAttribute("email_id")));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(sendTomailid));
			message.setSubject(dataform.getSubjectSeries()+dataform.getReasonCheckin());
			message.setContent(mailString,"text/html");
			System.err.println("---------------- Sending email to " + sendTomailid);
			Transport.send(message); 
			System.err.println("Done");
 
		} catch (MessagingException e) {
			e.printStackTrace();

			error = true;
		}
		
		return error;
	}
}