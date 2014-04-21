package job;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.CombineFileRecordReader;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

//import org.apache.hadoop.mapred.FileSplit;
//import org.apache.hadoop.mapred.InputSplit;
//import org.apache.hadoop.mapred.JobConf;
//import org.apache.hadoop.mapred.LineRecordReader;
//import org.apache.hadoop.mapred.RecordReader;
//import org.apache.hadoop.mapred.Reporter;
//import org.apache.hadoop.mapred.lib.CombineFileInputFormat;
//import org.apache.hadoop.mapred.lib.CombineFileRecordReader;
//import org.apache.hadoop.mapred.lib.CombineFileSplit;



public class MyCombineFileInputFormat extends CombineFileInputFormat<LongWritable,Text>
{
	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException {
		
		
		return new CombineFileRecordReader<LongWritable, Text>((CombineFileSplit) split, context, myCombineFileRecordReader.class);
	}

	
	public static class myCombineFileRecordReader extends RecordReader<LongWritable,Text>
	{
		private LineRecordReader lineRecord;
		private CombineFileSplit split;
		private TaskAttemptContext context;
		private int index;
		//private RecordReader< Text, Text > recReader;
		
		
		public myCombineFileRecordReader(CombineFileSplit split, TaskAttemptContext context, Integer index) throws IOException
		{			
			 this.split = split;
			 this.context = context;
			 this.index = index;
			 lineRecord = new LineRecordReader();
//			
//			FileSplit filesplit = new FileSplit(split.getPath(index), split.getOffset(index), split.getLength(index), split.getLocations());
//			linerecord = new LineRecordReader(conf, filesplit);			
		}
		
		
		@Override
		public void initialize(InputSplit genericSplit, TaskAttemptContext context)
		 throws IOException, InterruptedException {
		 
			this.split = (CombineFileSplit) genericSplit;
			this.context = context;
		 
			if (null == lineRecord) {
				lineRecord = new LineRecordReader();
			}
		 
			FileSplit fileSplit = new FileSplit(this.split.getPath(index), 
		                                      this.split.getOffset(index), 
		                                      this.split.getLength(index), 
		                                      this.split.getLocations());
			this.lineRecord.initialize(fileSplit, this.context);
		 }
			
		@Override
		public void close() throws IOException {
			lineRecord.close();
			
		}

		public boolean nextKeyValue() throws IOException, InterruptedException {
			return lineRecord.nextKeyValue();
		}

		@Override
		public LongWritable getCurrentKey() throws IOException, InterruptedException {
			return lineRecord.getCurrentKey();
		}

		@Override
		public Text getCurrentValue() throws IOException, InterruptedException {
			return lineRecord.getCurrentValue();
		}
		
		@Override
		 public float getProgress() throws IOException, InterruptedException {
		  return lineRecord.getProgress();
		 }
		
	}
}
