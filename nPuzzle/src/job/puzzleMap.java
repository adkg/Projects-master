package job;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class puzzleMap extends Mapper<LongWritable, Text, Text, Text> {
	
	@Override
	public void map(LongWritable key, Text value,Context context)
			throws IOException {
		
		
		//here value = lasts key+value
		//Value = top.box+"P"+'\t'+top.parent+'\t'+puzzLog.MDistPair(array)+'\t'+puzzLog.Max_swap(array)+'\t'+puzzLog.RCout(array)+'\t'+top.depth
		
		StringTokenizer token = new StringTokenizer(value.toString(),"	");
		
		String puzzleConf = token.nextToken(); //puzzle conf
		String type = token.nextToken();	// type P/C
		String Prnt = token.nextToken();	// conf of parent node
		if(token.countTokens()<4)
			System.out.println(value.toString());
		String other = token.nextToken()+'\t'+token.nextToken()+'\t'+token.nextToken()+'\t'+token.nextToken(); 
		
		
		try {
			
			PuzzleLogic puzzLog = new PuzzleLogic();
			puzzLog.tokenizeBuildPuzzle(puzzleConf);
			
			
			String[] array = puzzLog.convertToArray(puzzleConf);
			int ind = puzzLog.find(array,"x");
			
			int lenRow = (int)Math.sqrt(array.length); //length of a row
			int jobDepth = Integer.parseInt(context.getJobID().toString().substring(context.getJobID().toString().lastIndexOf("_")+1))+10;

			if(type.compareTo("C")==0) // if child
			{			
				String tmp;
				Text newKey = new Text();
				Text newValue = new Text();
				
				if((tmp=puzzLog.Down(array, lenRow, ind))!=null)
				{
					newKey.set(tmp);
					newValue.set("C	"+puzzleConf+'\t'+puzzLog.MDistPair(puzzLog.convertToArray(tmp))+'\t'+puzzLog.Max_swap(puzzLog.convertToArray(tmp))+'\t'+puzzLog.RCout(puzzLog.convertToArray(tmp))+'\t'+ jobDepth);
					context.write(newKey,newValue);					
				}
				
				if((tmp=puzzLog.Up(array, lenRow, ind))!=null)
				{
					newKey.set(tmp);
					newValue.set("C	"+puzzleConf+'\t'+puzzLog.MDistPair(puzzLog.convertToArray(tmp))+'\t'+puzzLog.Max_swap(puzzLog.convertToArray(tmp))+'\t'+puzzLog.RCout(puzzLog.convertToArray(tmp))+'\t'+ jobDepth);
					context.write(newKey,newValue);
				}	
				
				if((tmp=puzzLog.Left(array, lenRow, ind))!=null)
				{
					newKey.set(tmp);
					newValue.set("C	"+puzzleConf+'\t'+puzzLog.MDistPair(puzzLog.convertToArray(tmp))+'\t'+puzzLog.Max_swap(puzzLog.convertToArray(tmp))+'\t'+puzzLog.RCout(puzzLog.convertToArray(tmp))+'\t'+ jobDepth);
					context.write(newKey,newValue);
				}
				
				if((tmp=puzzLog.Right(array, lenRow, ind))!=null)
				{
					newKey.set(tmp);
					newValue.set("C	"+puzzleConf+'\t'+puzzLog.MDistPair(puzzLog.convertToArray(tmp))+'\t'+puzzLog.Max_swap(puzzLog.convertToArray(tmp))+'\t'+puzzLog.RCout(puzzLog.convertToArray(tmp))+'\t'+ jobDepth);
					context.write(newKey,newValue);
				}
				
				//Convert it to parent and emmit
			
					context.write(new Text(puzzleConf),new Text("P	"+Prnt+'\t'+other));
			}
			else // if parent
				context.write(new Text(puzzleConf),new Text("P	"+Prnt+'\t'+other));	
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}  //puzzLog.getJobId(conf.get("mapred.task.id"))));			
	}
}
