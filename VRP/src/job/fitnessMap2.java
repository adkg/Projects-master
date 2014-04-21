package job;
import java.io.IOException;
import java.net.URI;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Counters.Counter;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;


@SuppressWarnings("deprecation")
public class fitnessMap2 extends MapReduceBase 
implements Mapper<Text, Text, IntWritable, Text>
{
	int customer[][];
	int distance[][];
	int type[][];	
	float speed[][];
	int numNodes,cap;	
	calculateFitness2Opt cft;
	
	JobConf conf;	
	/*
	public void configure(JobConf conf){
		this.conf=conf;
		try
		{
			int i=0,j=0,k=0,l=0;
			Path[] cachefiles= DistributedCache.getLocalCacheArchives(conf);
			FileSystem fs = FileSystem.getLocal(new Configuration());
			MapFile.Reader reader;			
			
			String s;
			if(cachefiles!= null && cachefiles.length > 0)
			{
				s = cachefiles[l++].getName();
				if(s.equals("customerFile.map.tar.gz"))i=l;
				else if(s.equals("type.map.tar.gz"))j=l;
				else k=l;
				
				reader = new MapFile.Reader(fs,cachefiles[i].toString()+"/customerFile.map",conf);
				createCustDistArray(reader);
				reader = new MapFile.Reader(fs,cachefiles[j].toString()+"/type.map",conf);
				createTypeArray(reader);
				reader = new MapFile.Reader(fs,cachefiles[k].toString()+"/speed.map",conf);
				createSpeedArray(reader);
				cft = new calculateFitness2Opt(customer,distance,type,speed,cap);
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
*/
	
	public void configure(JobConf conf){
		this.conf=conf;
		try
		{			
				MapFile.Reader reader;
				FileSystem fs = FileSystem.get(URI.create("/user/user/user/rohit/input/vrp_input/customerFile.map"), conf);
				reader = new MapFile.Reader(fs,"/user/user/user/rohit/input/vrp_input/customerFile.map",conf);
				createCustDistArray(reader);
				reader = new MapFile.Reader(fs,"/user/user/user/rohit/input/vrp_input/type.map",conf);
				createTypeArray(reader);
				reader = new MapFile.Reader(fs,"/user/user/user/rohit/input/vrp_input/speed.map",conf);
				createSpeedArray(reader);
				cft = new calculateFitness2Opt(customer,distance,type,speed,cap);	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
	
	void createCustDistArray(MapFile.Reader customerFile) throws IOException
	{		
		IntArrayWritable atcust  = new IntArrayWritable(6);
		customerFile.get(new IntWritable(-2),atcust);
		numNodes = atcust.get()[0].get();
		customerFile.get(new IntWritable(-1),atcust);
		cap = atcust.get()[0].get();
		
		customer = new int[numNodes][6];
		
		for(int i=0;i<numNodes;i++)
		{
			customerFile.get(new IntWritable(i),atcust);
			for(int j=0;j<6;j++)
				customer[i][j] = atcust.get()[j].get();
		}
		createDistanceArray();
	}
	
	void createDistanceArray()
	{		
		distance = new int[numNodes][numNodes];
		
		for(int i=0;i<numNodes;i++)
			for(int j=0;j<numNodes;j++)
				if(i==j)
					distance[i][j]=0;
				else
					distance[i][j]=distance[j][i]= (int) Math.sqrt((customer[i][0] - customer[j][0])*(customer[i][0] - customer[j][0]) + (customer[i][1] - customer[j][1])*(customer[i][1] - customer[j][1]));
					
	}
		
	void createTypeArray(MapFile.Reader reader) throws IOException
	{
		type = new int[numNodes][numNodes];
		IntArrayWritable arr  = new IntArrayWritable(numNodes);
		for(int i=0;i<numNodes;i++)
		{
			reader.get(new IntWritable(i),arr);
			for(int j=0;j<numNodes;j++)
				type[i][j] = arr.get()[j].get();
		}		
	}
	
	void createSpeedArray(MapFile.Reader reader) throws IOException
	{
		speed = new float[6][5];
		IntArrayWritable arr  = new IntArrayWritable(4);
		for(int i=1;i<=5;i++)
		{
			reader.get(new IntWritable(i-1),arr);
			for(int j=1;j<=4;j++)
				speed[i][j] = arr.get()[j-1].get()/10.0f;
		}		
	}
	
	static int[] toArray(String str)
	{
		StringTokenizer token = new StringTokenizer(str,",");
		int[] array = new int[token.countTokens()];
		int i=0;
		while(token.hasMoreTokens())
			array[i++] = Integer.parseInt(token.nextToken());
		return array;
	}
	
	@Override
	public void map(Text key, Text value,
			OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException {

		int[] array = toArray(value.toString());	
		
		int fitness;
		
		try {		
			fitness = cft.getFitness(array,false);			
			Counter counter = reporter.getCounter(vrp.myCounters.max_fitness);
			
			if(fitness>counter.getValue())
			{
				reporter.incrCounter(vrp.myCounters.max_fitness, -counter.getValue());
				reporter.incrCounter(vrp.myCounters.max_fitness, fitness);
			}
		
			output.collect(new IntWritable(Integer.parseInt(key.toString())),new Text(value.toString()+'\t'+fitness));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}