package job;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class PuzzleDriver{	
	
	
	static String puzzInpPath = "/home/legend/Work/hadoop/hadooptmp/hadoopInput";//"/user/user/user/rohit/input/puzzleinp";
	static String inpOutPath = "/home/legend/Work/hadoop/hadooptmp/hadoopOutput/";//"/user/user/user/rohit/output/outputPuzzle/"
	static String tmpInpOutPath = "/home/legend/Work/hadoop/hadooptmp/hadoopOutput_tmp/";//"/user/user/user/rohit/output/outputPuzzle_tmp/";
	
	public static enum myCounters {
		Finished,
		numChildBefore,
		numChildAfter,
	}
		
	public static void main(String[] args) throws Exception {

		Job job1=null,job2=null;
		FileSystem fs;
		int jobIterations=1;
		boolean flag=false;
		
		
		PuzzleDriver puzzDriver = new PuzzleDriver();
		try{
			while(true)
			{	
				if(jobIterations==1)
				{
					job1 = puzzDriver.getJobType(0);
				}else
				{
					job1 = puzzDriver.getJobType(1);
				}
				
				fs = FileSystem.get(job1.getConfiguration());		
				System.err.println("---"+jobIterations);
				
				if(jobIterations==1)
					FileInputFormat.addInputPath(job1, new Path(puzzInpPath));
				else
				{
					if(flag)
						FileInputFormat.addInputPath(job1, new Path(tmpInpOutPath+(jobIterations-1)));
					else
						FileInputFormat.addInputPath(job1, new Path(inpOutPath+(jobIterations-1)));
				}
				
				flag = false;
				
				if(jobIterations>3)
				{
					fs.delete(new Path(inpOutPath+(jobIterations-2)),true);
					fs.delete(new Path(tmpInpOutPath+(jobIterations-2)),true);
				}
				
				FileOutputFormat.setOutputPath(job1, new Path(inpOutPath+jobIterations));
				
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				//------------run-------------
				job1.waitForCompletion(true);
				//---------------------------
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				
				Counters counters = job1.getCounters();
				Counter finished = counters.findCounter(myCounters.Finished);
				Counter numChild = counters.findCounter(myCounters.numChildBefore);

				System.err.println("finished = "+finished.getValue());
				System.err.println("numChild = "+numChild.getValue());
				
				
				if(finished.getValue()==1)
					break;
				if(numChild.getValue()==0)
					break;
				
				// Not executed first time after stack map
			
				if(numChild.getValue()>=400000)
				{
					job2 = puzzDriver.getJobType(2);
					flag=true;			
					
					FileInputFormat.addInputPath(job2, new Path(inpOutPath+jobIterations));
					FileOutputFormat.setOutputPath(job2, new Path(tmpInpOutPath+jobIterations));
					job2.waitForCompletion(true);
					counters = job2.getCounters();
				}
				
				System.err.println("Counter CS= "+numChild.getValue());
				numChild = counters.findCounter(myCounters.numChildAfter);
				System.err.println("Counter CS= "+numChild.getValue());
				jobIterations++;	

			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			fs = FileSystem.get(job1.getConfiguration());
			fs.delete(new Path("inpOutPath"),true);
			fs.delete(new Path("tmpInpOutPath"),true);
		}	
	}		
		
		
	private Job getJobType(int choice) throws URISyntaxException {
		
		Job job = null;
		
		try
		{
			job = new Job();
		    job.setJarByClass(PuzzleDriver.class);
		    job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			job.setOutputFormatClass(TextOutputFormat.class);
			
		   if(choice==0) // First Mapper
		   {
			   job.setJobName("FirstMapper");
			   job.setInputFormatClass(TextInputFormat.class);
			   job.setMapperClass(generateMap.class);	
			   job.setReducerClass(puzzleReduce.class);
		   }
		   else if(choice==1)
		   {
			   job.setJobName("SecondMapper");
			   job.setInputFormatClass(MyCombineFileInputFormat.class);
			   job.setMapperClass(puzzleMap.class);
			   job.setReducerClass(puzzleReduce.class);	
			   
		   }
		   else
		   {
			   job.setJobName("ThirdMapper");
			   job.setInputFormatClass(MyCombineFileInputFormat.class);		   
			   job.setMapperClass(HeuristicMap.class);
			   job.setReducerClass(HeuristicReduce.class);
			   job.setPartitionerClass(randomPartitioner.class);
			   job.setMapOutputKeyClass(KeyHeu.class);  
			   job.setGroupingComparatorClass(GroupComparator.class);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		 return job;
	}	


	public static class randomPartitioner extends Partitioner<KeyHeu, Text>
	{
		@Override
		public int getPartition(KeyHeu key, Text value, int numPartition) {
			return key.getNum().get();
		}	
	}
	
	
	public static class GroupComparator extends WritableComparator {
		
		protected GroupComparator()
		{
			super(KeyHeu.class,true);
		}
		public int compare(WritableComparable w1, WritableComparable w2)
		{
			KeyHeu k1 = (KeyHeu)w1;
			KeyHeu k2 = (KeyHeu)w2;			
			return(k1.getNum().compareTo(k2.getNum()));			
		}
	}	
	
}
