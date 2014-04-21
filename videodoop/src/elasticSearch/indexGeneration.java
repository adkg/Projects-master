package elasticSearch;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import imageAnalysis.ContourFinder;
import imageAnalysis.CornerDetector;
import imageAnalysis.EdgeFinder;
import imageAnalysis.Histogram;
import imageAnalysis.Initializer;
import imageAnalysis.faceDetection;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;


import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class indexGeneration {
	
	public boolean generateIndex(BufferedImage img, String filename,int frameno, Client client,FileWriter file)
	{
		Initializer.initialize();
		
		String color_name;
		boolean isFace;
		IplImage cvimage = IplImage.createFrom(img);
		
	    long[][] hsv_hist=new long[Initializer.NUM_BINS_HSV_HUE][Initializer.NUM_BINS_HSV_SAT];
	    color_name = Histogram.generateHistHSV(cvimage, hsv_hist);
	    if(color_name.equals("No"))
	    	return false;	    
	    isFace = faceDetection.faceDetect(cvimage);
	    
		long[] corner_hist=new long[Initializer.NUM_BINS_DIST_1D];
		CornerDetector.findCorners(cvimage,corner_hist);		
		
		IplImage gray_img=null;
		
		CvSize img_sz=null;
		if(cvimage!=null)
			img_sz = cvGetSize(cvimage);		
		if(img_sz!=null)
			gray_img = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
		else
			System.out.println("couldnt create image");        
        cvCvtColor(cvimage, gray_img, CV_BGR2GRAY);		
		
		long[][] apidq_hist =new long[Initializer.NUM_BINS_DIST_APIDQ][Initializer.NUM_BINS_ANGLE_APIDQ];
		Histogram.generateHistAPIDQ(gray_img, apidq_hist);
		
		IplImage img_edge = cvCreateImage(cvGetSize(gray_img), gray_img.depth(), 1);
		long[][] edge_hist=new long[Initializer.NUM_BINS_DIST_2D][Initializer.NUM_BINS_ANGLE_2D];
		EdgeFinder.getegdes(gray_img,img_edge);
		ContourFinder.findContour(img_edge,edge_hist);
		
		//System.err.println(convertToString(corner_hist, apidq_hist, hsv_hist, edge_hist));
		
		//System.err.println("------------");

		String vector = convertToString(color_name, isFace, corner_hist, apidq_hist, hsv_hist, edge_hist);
		//insertIndexes(vector,filename,frameno, client);
		client.admin().indices().refresh(new RefreshRequest("movie"));
		
		IndexComparison incmp = new IndexComparison();
		if(incmp.compare_reject(filename, frameno, vector, client)!=-1)
		{
			insertIndexes(vector,filename,frameno, client);
	        client.admin().indices().refresh(new RefreshRequest("movie"));
			//System.err.println(filename+"_"+frameno+" ** inserted");
			return true;
		}
		//else System.err.println(filename+"_"+frameno+"-------not inserted");
        client.admin().indices().refresh(new RefreshRequest("movie"));

		return false;

		//insertIndexes(vector,filename,frameno, client);

		//WARNING NOT RELEASED-------------------------------------------------------
		//gray_img..release(); //cvReleaseImage(gray_img);//if(!gray_img.isNull())gray_img.release();
		//img_edge.release(); //cvReleaseImage(img_edge);//if(!img_edge.isNull())img_edge.release();
		//cvimage.release(); //cvReleaseImage(cvimage);//cvimage.release();	
	}
	 
	public String convertToString(String color_name, boolean isFace, long[] corner_hist, long[][] apidq_hist, long[][] hsv_hist,long[][] edge_hist)
	{
		 StringBuffer buf = new StringBuffer("");		
		 
		 buf.append(color_name);
		 buf.append("\t");
		 buf.append(isFace);
		 buf.append("\t");
		 
		 //corner ----------
		 buf.append(corner_hist[0]);
		 for(int k=1;k<corner_hist.length;k++)
			 buf.append("a"+corner_hist[k]);
		 buf.append("\t");		 
		 
		 
		 //APIDQ
		 for(int k=0;k<Initializer.NUM_BINS_DIST_APIDQ;k++)
		  {			
			if(k==0)
				buf.append(apidq_hist[k][0]+"");
			else if(k%2==0)
			{ 
				buf.append("\t");	
				buf.append(apidq_hist[k][0]+"");
			}
			else
				buf.append("a"+apidq_hist[k][0]);
			
			for(int l=1;l<Initializer.NUM_BINS_ANGLE_APIDQ;l++)
				buf.append("a"+apidq_hist[k][l]);	
		  }
		 buf.append("\t");		 
		 
		 //HSV--------------
		 for(int k=0;k<Initializer.NUM_BINS_HSV_HUE;k++)
		  {			
			if(k==0)
				buf.append(hsv_hist[k][0]+"");
			else if(k%2==0)
			{ buf.append("\t");	
			buf.append(hsv_hist[k][0]+"");
			}
			else
				buf.append("a"+hsv_hist[k][0]);
			
			for(int l=1;l<Initializer.NUM_BINS_HSV_SAT;l++)
				buf.append("a"+hsv_hist[k][l]);	
		  }
		 buf.append("\t");
		 
		 //EDGE------------------------------------------------
		 
		 for(int k=0;k<Initializer.NUM_BINS_DIST_2D;k++)
		  {			
			if(k==0)
				buf.append(edge_hist[k][0]+"");
			else if(k%2==0)
			{ buf.append("\t");	
			  buf.append(edge_hist[k][0]+"");
			}
			else
				buf.append("a"+edge_hist[k][0]);
			
			for(int l=1;l<Initializer.NUM_BINS_ANGLE_2D;l++)
				buf.append("a"+edge_hist[k][l]);	
		  }
		 		 /*
		 //EDGE-------
		  for(int k=0;k<Initializer.NUM_BINS_DIST_2D;k++)
		  {			
			if(k==0)
				buf.append(edge_hist[k][0]+"");
			
			for(int l=1;l<Initializer.NUM_BINS_ANGLE_2D;l++)
				buf.append("a"+edge_hist[k][l]);
			buf.append("\t");
		  }		  
		  */
		  return buf.toString();		 
	}
	
	
	void insertIndexes(String str,String filename,int frameno, Client client)
	{		
		//System.err.println(str);
		StringTokenizer token = new StringTokenizer(str,"\t");
		try {
			String color = token.nextToken();
			IndexResponse response = client.prepareIndex("movie", color, filename+"_"+frameno )
			.setSource(jsonBuilder()
			            .startObject()
			            .field("filename", filename.replace(".", ""))
			            .field("color", color)
			            .field("frameno", String.valueOf(frameno))
			            .field("face",  token.nextToken())
			            .field("corner", token.nextToken())
			            .field("apidq1", token.nextToken())
			            .field("apidq2", token.nextToken())	
			            .field("apidq3", token.nextToken())	
			            .field("apidq4", token.nextToken())	
			            .field("apidq5", token.nextToken())	
			            .field("apidq6", token.nextToken())	
			            .field("apidq7", token.nextToken())	
			            .field("apidq8", token.nextToken())
			            .field("hsv1", token.nextToken())
			            .field("hsv2", token.nextToken())
			            .field("hsv3", token.nextToken())
			            .field("hsv4", token.nextToken())
			            .field("edge1", token.nextToken())
			            .field("edge2", token.nextToken())	
			            .field("edge3", token.nextToken())	
			            .field("edge4", token.nextToken())	
			            .field("edge5", token.nextToken())	
			            .field("edge6", token.nextToken())	
			            .field("edge7", token.nextToken())	
			            .field("edge8", token.nextToken())         
			            .endObject()
			          )
			.execute()
			.actionGet();
		} catch (ElasticSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
