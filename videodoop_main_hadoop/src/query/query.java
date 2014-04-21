package query;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_LOAD_IMAGE_COLOR;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;
import imageAnalysis.ContourFinder;
import imageAnalysis.CornerDetector;
import imageAnalysis.EdgeFinder;
import imageAnalysis.Histogram;
import imageAnalysis.Initializer;
import imageAnalysis.faceDetection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.terms.TermsFacet;

import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class query {

	/**
	 * @param args
	 */
	
	public void compare()
	{
		String source_dir="/home/user/Pictures/query";
		File directory = new File(source_dir);
		File[] indirs = directory.listFiles();
			
		for(int m=0;m<indirs.length;m++)
		{
			String[] infiles = indirs[m].list();
			for(int i=0;i<infiles.length;i++)
			{
				String query_file=indirs[m]+"/"+infiles[i];
				IplImage img=cvLoadImage(query_file,CV_LOAD_IMAGE_COLOR);
				IplImage imgResized = cvCreateImage(cvSize(Initializer.IMAGE_WIDTH,Initializer.IMAGE_HEIGHT), img.depth(), img.nChannels());
				cvResize(img,imgResized,img.nChannels());				
				
				Initializer.initialize();
				
				String color_name;
				boolean isFace;				
			    long[][] hsv_hist=new long[Initializer.NUM_BINS_HSV_HUE][Initializer.NUM_BINS_HSV_SAT];
			    color_name = Histogram.generateHistHSV(imgResized, hsv_hist);
			    if(!color_name.equals("No"))
			    {	    
			    
			    	isFace = faceDetection.faceDetect(imgResized);
					long[] corner_hist=new long[Initializer.NUM_BINS_DIST_1D];
					CornerDetector.findCorners(imgResized,corner_hist);		
					
					IplImage gray_img=null;
					
					CvSize img_sz=null;
					if(imgResized!=null)
						img_sz = cvGetSize(imgResized);		
					if(img_sz!=null)
						gray_img = cvCreateImage(img_sz, IPL_DEPTH_8U, 1);
					else
						System.out.println("couldnt create image");        
			        cvCvtColor(imgResized, gray_img, CV_BGR2GRAY);		
					
					long[][] apidq_hist =new long[Initializer.NUM_BINS_DIST_APIDQ][Initializer.NUM_BINS_ANGLE_APIDQ];
					Histogram.generateHistAPIDQ(gray_img, apidq_hist);
					
					IplImage img_edge = cvCreateImage(cvGetSize(gray_img), gray_img.depth(), 1);
					long[][] edge_hist=new long[Initializer.NUM_BINS_DIST_2D][Initializer.NUM_BINS_ANGLE_2D];
					EdgeFinder.getegdes(gray_img,img_edge);
					ContourFinder.findContour(img_edge,edge_hist);
	
					String vector = convertToString(color_name, isFace, corner_hist, apidq_hist, hsv_hist, edge_hist);
					Map<String,Object> map = new HashMap<String,Object>();
					System.err.println(vector);
					StringTokenizer token = new StringTokenizer(vector,"\t");
					
					String color = token.nextToken();
					
					map.put("corner", token.nextToken());
					map.put("apidq", token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken());
					map.put("hsv", token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken());
					map.put("edge", token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken());
					//map.put("frameno", 1);
					
					ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
					settings.put("cluster.name","videodoop");
					TransportClient tmp = new TransportClient(settings);
					tmp.addTransportAddress(new InetSocketTransportAddress("10.222.250.201", 9300));
					Client client = tmp;
					
					SearchRequestBuilder builder = client.prepareSearch("movie").setTypes(color);
					QueryBuilder query = QueryBuilders.customScoreQuery(QueryBuilders.queryString("color:"+color)).script("compareScript").lang("native").params(map);
					builder.setQuery(query);
					builder.addFacet(FacetBuilders.termsFacet("filename").field("filename").script("doc.score>0"));
					builder.setExplain(true);	
					builder.execute();
					builder.setSize(100);
					SearchResponse rsp = builder.execute().actionGet();
					//System.err.println(rsp.getHits().getMaxScore());
					
					SearchHit[] docs = rsp.getHits().getHits();
					System.err.println(docs.length);
					/*
					for(SearchHit d1: docs)
					{
						System.err.println(d1.getSource().get("filename").toString()+"   "+d1.getSource().get("frameno").toString()+"  "+ d1.getScore()+"  "+d1.getSource().get("color").toString());
						//if(d1.getScore()>=85.0f)				
							//return -1;
					}
					*/
					
					if(rsp!=null)
					{
						Facets facets = rsp.facets();
						 if (facets != null)
				                for (Facet facet : facets.facets())
				                {
				                	 TermsFacet ff = (TermsFacet) facet;	
				                	 System.err.print(ff.getTotalCount());
				                	 for (TermsFacet.Entry e : ff.entries()) {
				                		 System.err.print(e.getTerm()+"----"+e.getCount()+"\n");
				                	 }
				                }
					}					
			    }
			}
		}
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
		  return buf.toString();		 
	}
		
		
	public static void main(String[] args) {
		query q = new query();
		q.compare();
	}

}
