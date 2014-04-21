package job;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;


public class mapFileGenerator {	
	
	 int numNodes;
	 int capacity;
	 int numVehicles;
	
	 
	 void cust_generate() throws IOException
	 {
		 FileInputStream fstream = new FileInputStream("/home/user/Documents/Hadoop Program Data/VRP/copy.TXT");
		 DataInputStream in = new DataInputStream(fstream);
		 BufferedReader br = new BufferedReader(new InputStreamReader(in));
		 String strLine;		 
		 
		 String uri = "/user/user/user/rohit/input/vrp_input/customerFile.map";
		 Configuration conf = new Configuration();
		 FileSystem fs = FileSystem.get(URI.create(uri), conf);
		 
		 IntWritable key = new IntWritable();
		 IntArrayWritable array = new IntArrayWritable(6);
		 
		 
		 IntWritable[] values = new IntWritable[6];
			
		 for(int i=0;i<6;i++)
			values[i] = new IntWritable(0);
			
		 MapFile.Writer writer = new MapFile.Writer(conf, fs, uri,key.getClass(), array.getClass());

		 strLine = br.readLine();
		 
		 key.set(-2); // No. of Nodes
		 values[0].set(Integer.parseInt(strLine));
		 array.set(values);
		 writer.append(key, array);
		 numNodes = values[0].get();
		 
		 strLine = br.readLine();
		 key.set(-1);
		 
		 int i=0;
		 
		 values[0].set(Integer.parseInt(strLine));		
		 array.set(values);	 
		 writer.append(key, array);

		 capacity = values[0].get();
		 
		 StringTokenizer token; 
		 
		 while ((strLine = br.readLine()) != null)
		 {
			
			key.set(Integer.valueOf(strLine.substring(0,strLine.indexOf("	"))));			
			i=0;
			token = new StringTokenizer(strLine.substring(strLine.indexOf("	")+1));
			while(token.hasMoreTokens())
				 values[i++].set(Integer.parseInt(token.nextToken()));
			array.set(values);	 
			writer.append(key, array);		
		 }
		 		 
		 IOUtils.closeStream(writer);
		 in.close();
		 br.close();
		 fstream.close();
	 }
	 
	 void writeRouteTypes() throws IOException
	 {
		 FileInputStream fstream = new FileInputStream("/home/user/Documents/Hadoop Program Data/VRP/type.txt");
		 DataInputStream in = new DataInputStream(fstream);
		 BufferedReader br = new BufferedReader(new InputStreamReader(in));
		 	 
		 IntWritable key = new IntWritable();
		 IntArrayWritable array = new IntArrayWritable(numNodes);
		 IntWritable[] values = new IntWritable[numNodes];
		
		 String uri = "/user/user/user/rohit/input/vrp_input/type.map";
		 Configuration conf = new Configuration();
		 FileSystem fs = FileSystem.get(URI.create(uri), conf);
		 MapFile.Writer writer = new MapFile.Writer(conf, fs, uri,key.getClass(), array.getClass());	 
		 
		 
		 System.err.println(numNodes);
		 String strLine;
		 StringTokenizer token;
		 int i=0,j=0;
		 while((strLine=br.readLine())!=null)
		 {
			 i=0;
			 token = new StringTokenizer(strLine,",");
			 while(token.hasMoreTokens())
				 values[i++] = new IntWritable(Integer.parseInt(token.nextToken()));
			 array.set(values);
			 key.set(j++);
			 writer.append(key, array);
		 }
		 fstream.close();
		 IOUtils.closeStream(writer);		 
	 }
	 
	 void speed() throws Exception
	 {
		 FileInputStream fstream = new FileInputStream("/home/user/Documents/Hadoop Program Data/VRP/speed.txt");
		 DataInputStream in = new DataInputStream(fstream);
		 BufferedReader br = new BufferedReader(new InputStreamReader(in));
		 	 
		 IntWritable key = new IntWritable();
		 IntArrayWritable array = new IntArrayWritable(4);
		 IntWritable[] values = new IntWritable[4];
		
		 String uri = "/user/user/user/rohit/input/vrp_input/speed.map";
		 Configuration conf = new Configuration();
		 FileSystem fs = FileSystem.get(URI.create(uri), conf);
		 MapFile.Writer writer = new MapFile.Writer(conf, fs, uri,key.getClass(), array.getClass());
		 

		 String strLine;
		 StringTokenizer token;
		 int i=0,j=0;
		 while((strLine=br.readLine())!=null)
		 {
			 i=0;
			 token = new StringTokenizer(strLine,"	");
			 while(token.hasMoreTokens())
				 values[i++] = new IntWritable((int)(10*Float.parseFloat(token.nextToken())));
			 array.set(values);
			 key.set(j++);
			 writer.append(key, array);
		 }
		 fstream.close();
		 IOUtils.closeStream(writer);
	 }
	 
	 void print() throws IOException
	 {
		String uri = "/user/user/user/rohit/input/vrp_input/customerFile.map";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		MapFile.Reader reader = new MapFile.Reader(fs,uri,conf);
		
		IntArrayWritable t = new IntArrayWritable(6);
		IntWritable t1 = new IntWritable();		
		
		for(int i=0;i<numNodes;i++)
		{
			t1.set(i);
			reader.get(t1,t);			
			System.err.println(t.get()[4]);
		}
	 }
}
