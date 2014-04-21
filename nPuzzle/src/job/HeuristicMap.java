package job;

import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class HeuristicMap extends Mapper<LongWritable, Text, KeyHeu, Text> {
	
	@Override
	public void map(LongWritable key, Text value,Context context)
			throws IOException {
		

		int numReducer = context.getNumReduceTasks();	    
		
		StringTokenizer token = new StringTokenizer(value.toString(),"	");
		String box = token.nextToken();
		String type = token.nextToken();
		String parent = token.nextToken();
		int heur1,heur2,heur3,depth;

		heur1 = Integer.parseInt(token.nextToken());
		heur2 = Integer.parseInt(token.nextToken());
		heur3 = Integer.parseInt(token.nextToken());
		depth = Integer.parseInt(token.nextToken());
		
		
		try {
			Random rnd = new Random();
			int num = rnd.nextInt(numReducer); // the number of reducer
			
			if(type.equals("P"))
				context.write(new KeyHeu(num,heur1,heur2,heur3),new Text(box +'\t'+type+'\t'+parent+'\t'+heur1+'\t'+heur2+'\t'+heur3+'\t'+depth));
			else
				context.write(new KeyHeu(num,heur1,heur2,heur3),new Text(box +'\t'+type+'\t'+parent+'\t'+heur1+'\t'+heur2+'\t'+heur3+'\t'+depth));
			
		}catch (InterruptedException e) {
					// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
