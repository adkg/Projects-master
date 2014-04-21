package job;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class IntArrayWritable extends ArrayWritable 
{
	IntWritable[] array;
	
	public IntArrayWritable(int n) {
		
		super(IntWritable.class);
		array = new IntWritable[n];
		for(int i=0;i<array.length;i++)
			array[i] = new IntWritable();
	}
	
	@Override
	public void set(Writable[] values)
	{
		
		for(int i=0;i<array.length;i++)
			array[i] = (IntWritable)values[i];	
	}
	
	@Override
	public IntWritable[] get()
	{			
		return array;
	}
	
	public void readFields(DataInput in) throws IOException
	{
		for(int i=0;i<array.length;i++)
			array[i].readFields(in);
	}
	
	public void write(DataOutput out) throws IOException {
		
		for(int i=0;i<array.length;i++)
			array[i].write(out);
	}

	
}
