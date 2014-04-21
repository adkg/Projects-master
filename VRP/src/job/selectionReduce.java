package job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/******************* cum_fit technique*****/
@SuppressWarnings("deprecation")
public class selectionReduce extends MapReduceBase 
implements Reducer<IntWritable, Text, IntWritable, Text>{
	
	private JobConf conf;

	
	@Override
	public void configure(JobConf conf)
	{
		this.conf = conf;
	}
	
	@Override
	public void reduce(IntWritable key, Iterator<Text> values,
			OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException {
		
		
		int fitness=0,max_fitness=0,max_index=0,tmp;
		ArrayList<String> val = new ArrayList<String>();// population
		ArrayList<Integer> cum_fitness =new ArrayList<Integer>();
		
		String temp;
		
		while(values.hasNext())
		{
			temp = values.next().toString();			
			val.add(temp.substring(0,temp.indexOf('\t')));
			
			tmp = Integer.parseInt(temp.substring(temp.indexOf('\t')+1)); //fitness		    
		    if(tmp>max_fitness)
		    {
		    	max_index = val.size()-1;
		    	max_fitness = tmp;
		    }
		    fitness+=tmp;		    	
		    cum_fitness.add(fitness);
		}		
		
		output.collect(key,new Text(val.get(max_index))); // elitism
		
		//roluette wheel 
		
		Random rnd = new Random();		
		int i=0,j=0,k=0;		
	
		while(i++<val.size()-1)
		{
			j=0;k=0;
			tmp = rnd.nextInt(fitness);
			while(cum_fitness.get(j++).compareTo(tmp)<0);
			tmp = rnd.nextInt(fitness);
			while(cum_fitness.get(k++).compareTo(tmp)<0);			
			
			tmp = rnd.nextInt(100);
			
			//island change
			if(tmp<10)
			{
				int key1 = rnd.nextInt(conf.getNumReduceTasks());
				output.collect(new IntWritable(key1), new Text(crossover.doCrossover(val.get(j-1),val.get(k-1))[0]));
				key1 = rnd.nextInt(conf.getNumReduceTasks());
				output.collect(new IntWritable(key1), new Text(crossover.doCrossover(val.get(j-1),val.get(k-1))[1]));	
			}
			
			else
			{
				output.collect(key, new Text(crossover.doCrossover(val.get(j-1),val.get(k-1))[0]));	
				output.collect(key, new Text(crossover.doCrossover(val.get(j-1),val.get(k-1))[1]));	
			}
		}		
		
	}
}
