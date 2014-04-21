package servlets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import xuggler.frameExtractor;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;


public class file_upload extends HttpServlet {
	
		private static final String TMP_DIR_PATH = "/home/user/tmpData/tmp";
		private File tmpDir;
		private static final String DESTINATION_DIR_PATH ="/home/user/tmpData/files";
		private File destinationDir;
		@Override
		public void init(ServletConfig config) throws ServletException 
		{			
			super.init(config);		
			tmpDir = new File(TMP_DIR_PATH);
			if(!tmpDir.exists()) {
				throw new ServletException(TMP_DIR_PATH +" does not exists");
			}
			
			//String realPath = getServletContext().getRealPath(DESTINATION_DIR_PATH);
			destinationDir = new File(DESTINATION_DIR_PATH);
			
			if(!destinationDir.exists()) {
				throw new ServletException(DESTINATION_DIR_PATH+" is not a directory");
			}
		}
		
		
		
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
		{			
			Map<String,Integer> outMap=null ;
			SortedMap sortedData = null;
		  //  PrintWriter out = response.getWriter();
		    response.setContentType("text/plain");
		   // out.println("<h1>Servlet File Upload Example using Commons File Upload</h1>");
		   // out.println();
		    String curDir = System.getProperty("user.dir");
			//out.println("<h1>"+curDir+"</h1>");
	 
			
			DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory ();		
				
			fileItemFactory.setSizeThreshold(20*1024*1024); //1 MB
			
			fileItemFactory.setRepository(tmpDir);
	 
			ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
			
			try {
				
				List items = uploadHandler.parseRequest(request);
				Iterator itr = items.iterator();
				while(itr.hasNext()) {
					
					FileItem item = (FileItem) itr.next();
					
					if(item.isFormField()) {
						//out.println("File Name = "+item.getFieldName()+", Value = "+item.getString());
					} else {
						//Handle Uploaded files.
						/*out.println("Field Name = "+item.getFieldName()+
							", File Name = "+item.getName()+
							", Content type = "+item.getContentType()+
							", File Size = "+item.getSize()+
							", Path = "+DESTINATION_DIR_PATH+"/"+item.getName());
						*/
						File file = new File(destinationDir,item.getName());
						item.write(file);
					}
				
					
					String format = item.getName().substring(item.getName().lastIndexOf(".")+1);
					
					IContainerFormat containerFormat = IContainerFormat.make();
			        containerFormat.setInputFormat(format);
					
					
					File file = new File(DESTINATION_DIR_PATH+"/"+item.getName());
					FileInputStream fis = new FileInputStream(file);  
					
					IContainer container = IContainer.make();
					container.setInputBufferLength(fis.available());        
			        container.open(fis,containerFormat);
			        outMap  = new HashMap<String,Integer>();
			        frameExtractor fext = new frameExtractor();
			        //out.println("cstarted1---");

			        fext.Extractor(container, item.getName(), outMap);
			        
			        sortedData = new TreeMap(new file_upload.ValueComparer(outMap));
			        sortedData.putAll(outMap);
			        /*
			        for (Iterator iter = sortedData.keySet().iterator(); iter.hasNext();) {
				       // out.println("*");
			        	String key = (String) iter.next();
						//out.println("Value/key:"+sortedData.get(key)+"/"+key);
					}
			    	//out.close();			    	
			    	 */
			}					
		    	
			}catch(FileUploadException ex) {
				log("Error encountered while parsing the request",ex);
				//out.close();
			} catch(Exception ex) {
				
				log("Error encountered while uploading file",ex);
			}
			
			if(!sortedData.isEmpty())
			{
				PrintWriter out = response.getWriter();
				out.println(getServletContext().toString());
				//out.close();
				request.setAttribute("map", sortedData);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/result.jsp");
				rd.forward(request, response);
				return;
			}
		}
		
		/** inner class to do soring of the map **/
		private static class ValueComparer implements Comparator {
			private Map  _data = null;
			public ValueComparer (Map data){
				super();
				_data = data;
			}

	         public int compare(Object o1, Object o2) {
	        	 Integer e1 = (Integer) _data.get(o1);
	        	 Integer e2 = (Integer) _data.get(o2);
	             return -e1.compareTo(e2);
	         }
		}
}
