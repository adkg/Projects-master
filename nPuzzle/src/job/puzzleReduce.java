package job;
import java.io.IOException;

import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;

import job.PuzzleDriver.myCounters;;



public class puzzleReduce extends Reducer<Text, Text, Text, Text> {

		
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException {
		
		String value, minValue=null;
		int minDepth = Integer.MAX_VALUE, tmpDepth;
		
		boolean isParent = false;
		char type;
		
		PuzzleLogic puzzLogic = new PuzzleLogic();	
		puzzLogic.tokenizeBuildPuzzle(key.toString());
		
		
		if(puzzLogic.check(puzzLogic.convertToArray(key.toString())))
			context.getCounter(myCounters.Finished).increment(1);		
		
		try
		{
			for(Text itr:values)
			{
				value = itr.toString();
				
				type = value.charAt(0); //get type
				
				
				if(type=='C' && !isParent)
				{
					context.getCounter(myCounters.numChildBefore).increment(1);
					minValue = value; // contains all data
				}
				else
				{
					isParent = true;
					tmpDepth = Integer.parseInt(value.substring(value.lastIndexOf('\t')+1));
					if(tmpDepth < minDepth)
						minValue = value; // contains all data
				}
			}
			
			context.write(key,new Text(minValue));
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
