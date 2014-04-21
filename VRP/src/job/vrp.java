package job;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapred.Counters.Counter;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



public class vrp extends Configured implements Tool{
	
	public static boolean  last;
	public static int ch,count;public static long last_fitness;
    
	public static enum myCounters {
		max_fitness;
	}
	
	
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new vrp(), args);
		System.exit(exitCode);
	}
	
	
	@SuppressWarnings("deprecation")
	private JobConf getJobConf(int ch) throws URISyntaxException {
	    JobConf conf = new JobConf(getConf(), getClass());
	    
	    if(ch==0)
	    {
	    	conf.setInputFormat(TextInputFormat.class);
			conf.setMapperClass(fitnessMap_phase1.class);
			//conf.setReducerClass(selectionReduce.class);
			conf.setMapOutputKeyClass(IntWritable.class);
			conf.setMapOutputValueClass(Text.class);
			conf.setOutputKeyClass(IntWritable.class);
			conf.setOutputValueClass(Text.class);
			conf.setOutputFormat(TextOutputFormat.class);
			conf.set("mapred.textoutputformat.separator", ">");
	    }
	    else if(ch<1 && !last)
	    {
	    	System.err.println(ch+"   "+last);
	    	conf.set("key.value.separator.in.input.line", ">");
	    	conf.setInputFormat(KeyValueTextInputFormat.class);
			conf.setMapperClass(fitnessMap2.class);
			conf.setReducerClass(selectionReduce.class);
			conf.setMapOutputKeyClass(IntWritable.class);
			conf.setMapOutputValueClass(Text.class);
			conf.setOutputKeyClass(IntWritable.class);
			conf.setOutputValueClass(Text.class);
			conf.setOutputFormat(TextOutputFormat.class);
			conf.set("mapred.textoutputformat.separator", ">");
	    }
	    else
	    {
	    	System.err.println("-----------"+ch+"   "+last);
	    	conf.set("key.value.separator.in.input.line", ">");
	    	conf.setInputFormat(KeyValueTextInputFormat.class);
			conf.setMapperClass(fitnessMap3.class);
			conf.setMapOutputKeyClass(IntWritable.class);
			conf.setMapOutputValueClass(Text.class);
			conf.setOutputKeyClass(IntWritable.class);
			conf.setOutputValueClass(Text.class);
			conf.setOutputFormat(TextOutputFormat.class);
	    }
	    return conf;
	 
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public int run(String[] arg0) throws Exception {
		
		JobConf conf;
		FileSystem fs;
		while(ch==0)
		{
			System.err.println("Iteration -- "+ch);
			System.err.println("Min -- "+last_fitness);
			
			conf=getJobConf(ch);
			fs = FileSystem.get(conf);
			if(ch==0)
			{
				fs.delete(new Path("/user/user/user/rohit/input/vrp_output"));
				FileInputFormat.addInputPath(conf, new Path("/user/user/user/rohit/input/vrp_input/pop.txt"));
				FileOutputFormat.setOutputPath(conf, new Path("/user/user/user/rohit/input/vrp_output/"+ch));
			}
			else
			{
				FileInputFormat.addInputPath(conf, new Path("/user/user/user/rohit/input/vrp_output/"+(ch-1)));
				FileOutputFormat.setOutputPath(conf, new Path("/user/user/user/rohit/input/vrp_output/"+ch));
			}
			if(ch>1);
			//	fs.delete(new Path("/user/user/user/rohit/input/vrp_output/"+(ch-2)));
		
			
			
			//URI archiveUri = new URI("/user/user/user/rohit/input/vrp_input/customerFile.map.tar.gz"); 
			//DistributedCache.addCacheArchive(archiveUri, conf); 
			
			//archiveUri = new URI("/user/user/user/rohit/input/vrp_input/type.map.tar.gz"); 
			//DistributedCache.addCacheArchive(archiveUri, conf);
			
			//archiveUri = new URI("/user/user/user/rohit/input/vrp_input/speed.map.tar.gz"); 
			//DistributedCache.addCacheArchive(archiveUri, conf);
			
		
			RunningJob job; 
			job = JobClient.runJob(conf);
			job.waitForCompletion();
			
			ch++;
			
			Counters counters = job.getCounters();
			Counter fit = counters.findCounter(myCounters.max_fitness);
			if(fit.getValue()==last_fitness)
				count++;
			else
			{
				count=0;
				last_fitness=fit.getValue();
			}
			if(last)break;
			
			if(count==5 || ch>0)last=true;
			
			
			
		}
		return 0;
	}

}
