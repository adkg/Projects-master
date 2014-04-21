package job;

import java.io.IOException;
import java.util.Iterator;

import job.PuzzleDriver.myCounters;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class HeuristicReduce extends Reducer<KeyHeu, Text, Text, Text> {
  
	public void reduce(KeyHeu key, Iterable<Text> values,
			Context context) throws IOException {
		
		int count = 0;
		Text newKey = new Text();
		Text newValue = new Text();
		
		try
		{
			for(Text itr:values)
			{
				String sTemp = itr.toString();
				if(count<2500  ||sTemp.charAt(sTemp.indexOf('\t')+1)=='P')
				{
					if(sTemp.charAt(sTemp.indexOf('\t')+1)=='C')
					{
						count++;
						context.getCounter(myCounters.numChildAfter).increment(1);
					}
					newKey.set(sTemp.substring(0,sTemp.indexOf('\t')));
					newValue.set(sTemp.substring(sTemp.indexOf('\t')+1));
					context.write(newKey,newValue);
				}
			}
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
