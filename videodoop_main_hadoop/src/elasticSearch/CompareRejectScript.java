package elasticSearch;


import imageAnalysis.Initializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;


public class CompareRejectScript implements NativeScriptFactory {
	
	private static long[] edge_vec;
	private static long[] hsv_vec;
	private static long[] apidq_vec;
	private static long[] cor_vec;
	
	static long[] toArray(String s,long[] arr) 
	{
        int i=0;
		StringTokenizer token = new StringTokenizer(s, "a");
		int ct=1;
		
        while(token.hasMoreTokens())
        {
        	String t = token.nextToken(); 
               	arr[i++] = Long.parseLong(t);               	
 		}
		
		return arr;        
	}
	
	@Override public ExecutableScript newScript(@Nullable Map<String, Object> params) {

		cor_vec = new long[8];        
        toArray(params.get("corner").toString(), cor_vec);    
		
        apidq_vec= new long[128];        
        toArray(params.get("apidq").toString(), apidq_vec);   
		
        hsv_vec= new long[64];
        toArray(params.get("hsv").toString(), hsv_vec);  
        
        edge_vec = new long[128];        
        toArray(params.get("edge").toString(), edge_vec);         
        
		return new MySearchScript(params);
    }
	
	public static float compareHist2D(long[] hist1,long[] hist2)
	{
		long min,sum=0;
		for(int i=0;i<hist1.length;i++)
		{
			if(hist1[i]<hist2[i])
				min=hist1[i];
			else
				min=hist2[i];
			 sum+=min;
		}
		return sum/10000;
	}
	
	public static float compareHist(long[] hist1,long[] hist2)
	{
		
		long min=0,sum=0;
		for(int i=0;i<hist1.length;i++)
		{
			if(hist1[i]<hist2[i])
				min=hist1[i];
			else
				min=hist2[i];
			sum+=min;
		}
		return (float)(sum*100.0/Initializer.TOTAL_PIXELS);
	}
	
	public static float compareHist1D(long[] hist1,long[] hist2)
	{
		double min,sum=0;
		for(int i=0;i<Initializer.NUM_BINS_DIST_1D;i++)
		{
			if(hist1[i]<hist2[i])
				min=hist1[i];
			else
				min=hist2[i];
			sum+=min;
		}
		return (float)(sum/10000.0);
	}
	
	
	static class MySearchScript extends AbstractFloatSearchScript {		
		
		Map<String, Object> params;
		MySearchScript(Map<String, Object> params)
		{
			this.params = params;
		}
		
		String toString(long[] vec)
		{
			StringBuffer buf = new StringBuffer();
			for(int i=0;i<vec.length;i++)
				buf.append(vec[i]+",");
			return buf.toString();
		}
		
		
		public float computeCorner()
		{
			long[] vec = new long[8];
			StringBuffer buf = new StringBuffer("");
			buf.append(doc().field("corner").getStringValue());
			toArray(buf.toString(),vec);
			return compareHist2D(cor_vec, vec);
		}
		
		public float computeAPIDQ()
		{
			long[] vec = new long[128];
			StringBuffer buf = new StringBuffer("");
			for(int i=1;i<=8;i++)
				buf.append(doc().field("apidq"+i).getStringValue()+"a");
			buf.deleteCharAt(buf.length()-1);
			toArray(buf.toString(),vec);
			return compareHist(apidq_vec, vec);
		}
		
		public float computeHSV()
		{
			long[] vec = new long[64];
			StringBuffer buf = new StringBuffer("");
			for(int i=1;i<=4;i++)
				buf.append(doc().field("hsv"+i).getStringValue()+"a");
			buf.deleteCharAt(buf.length()-1);
			toArray(buf.toString(),vec);
			return compareHist(hsv_vec, vec);
		}
		
		public float computeEdge()
		{
			
			
			long[] vec = new long[128];
			StringBuffer buf = new StringBuffer("");
			for(int i=1;i<=8;i++)
					buf.append(doc().field("edge"+i).getStringValue()+"a");
			
			buf.deleteCharAt(buf.length()-1);
				 
			toArray(buf.toString(),vec);
			
			return compareHist2D(edge_vec, vec);
		
			
		}
				
		
		@Override
		  public float runAsFloat() {
			
			//String frame1 = doc().field("frameno").getStringValue();
			//String frame2 = params.get("frameno").toString();
			//float sim_apidq = computeAPIDQ(),sim_color = computeHSV(), sim_edge = computeEdge(frame1,frame2);
			
			float sim_cor = computeCorner(),sim_apidq ,sim_color, sim_edge;
			
			/*
			try {
				FileWriter file = new FileWriter("/home/user/Desktop/abc.txt",true);
				file.append(doc().field("frameno").getStringValue()+"  "+params.get("frameno")+"  "+sim_cor+"  "+sim_apidq+"  "+sim_color+"  "+sim_edge+"  "+(float)(sim_apidq+sim_color+sim_edge)/3+"\n");
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
					
			//return (float)(sim_cor +sim_apidq+sim_color+sim_edge)/4;
			
			if(sim_cor>=60)
			{
				sim_apidq = computeAPIDQ();
				if(sim_apidq>=75)
				{
					sim_color = computeHSV();
					if(sim_color>=65)
					{
						sim_edge = computeEdge();
						if(sim_edge>=60)
							return (float)((0.3*sim_cor+0.8*sim_apidq+sim_color+0.6*sim_edge)/2.7);
					}
				}
			}	
			
			return 0;
			
			//return computeEdge();	  //65   // kum for reges 115-64
			//return computeCorner(); //70
			//return computeHSV();  //65
			//return computeAPIDQ();  //75
			//return doc().field("color1").getStringValue().lastIndexOf('a');
		}
    }
}
