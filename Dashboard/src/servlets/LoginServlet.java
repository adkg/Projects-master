package servlets;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login-Servlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String host =  "stbeehive.oracle.com";
	private String port = "465";
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    private boolean validateLogin(HttpServletRequest request)
    {
    	boolean status = false;
    	try
    	{
	    	Properties props = new Properties();
	    	props.put("mail.smtp.host", host);
			props.put("mail.smtp.socketFactory.port", port);
	    	props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			
			Session session = Session.getInstance(props, null);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, 465, request.getParameter("email_id"), request.getParameter("password"));
			transport.close();
			status = true;			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();    		
    	}
    	return status;
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		synchronized(session) {			
				
			String emailid = request.getParameter("email_id");
					
			if(emailid!=null && !emailid.equals("") && validateLogin(request))
			{				
				session.setAttribute("email_id",emailid);
				session.setAttribute("password_word", request.getParameter("password"));
				RequestDispatcher dispatcher =
				        request.getRequestDispatcher("checkInReport.jsp");
				      dispatcher.forward(request, response);
			}
			else
			{	
				request.setAttribute("login_error", "Invalid Email-id or Password");				
				RequestDispatcher dispatcher =
				        request.getRequestDispatcher("Login.jsp");
				      dispatcher.forward(request, response);
			}
		}		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("Done---");
		doGet(request,response);
	}

}
