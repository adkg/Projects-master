package job;

import java.io.IOException;
import java.util.Stack;
import java.util.StringTokenizer;

import job.PuzzleDriver.myCounters;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class generateMap extends Mapper<LongWritable, Text, Text, Text> {
	
	public static class Node {	
		int depth;
		String puzzleConf;
		String parent;
		int mdist, maxSwap, rcOut;
		
		Node(int d,String b,String p, int mdist,int maxSwap, int rcOut)
		{
			depth = d;
			puzzleConf = b;
			parent = p;
			this.mdist = mdist;
			this.maxSwap = maxSwap;
			this.rcOut = rcOut;
		}
	}	
	
	@Override
	public void map (LongWritable key, Text value,Context context)
			throws IOException {
		
		StringTokenizer token = new StringTokenizer(value.toString(),"	");
		System.out.println(token.countTokens()+"-----"+value.toString());
		String puzzleConf = token.nextToken(); //Puzzle configuration
		String type = token.nextToken(); //is Parent or child
		String Prnt = token.nextToken(); //nodes parent configuration
		
		int count=0;
		
		PuzzleLogic puzzLogic = new PuzzleLogic();
		puzzLogic.tokenizeBuildPuzzle(Prnt);
		String[] tmpArray = puzzLogic.convertToArray(Prnt);
		
		
		Stack<Node> stck = new Stack<Node>();		
		stck.push(new Node (0,puzzleConf,Prnt,puzzLogic.MDistPair(tmpArray),puzzLogic.Max_swap(tmpArray),puzzLogic.RCout(tmpArray)));
		
		String[] array;
		Text newKey = new Text();
		Text newValue = new Text();
		Node top = stck.pop();		
		
		
		//check if puzzle solvable
		solvable sol = new solvable();
		
		if(!sol.solv(puzzleConf))
		{
			context.getCounter(myCounters.Finished).increment(1);
			System.exit(0);
		}
		//----
		
		try
		{
			while(true)
			{
				if(top.depth<=10)
				{
								
					array = puzzLogic.convertToArray(top.puzzleConf);
					
					int rowLength = (int)Math.sqrt(array.length); // length of a row of a sqaure matrix
					int indOfBlank = puzzLogic.find(array,"x");
					
					String tmp;
					
					if((tmp=puzzLogic.Down(array, rowLength, indOfBlank))!=null)
					{
						//System.out.println(tmp);
						tmpArray = puzzLogic.convertToArray(tmp);
						stck.push(new Node(top.depth+1,tmp,top.puzzleConf,puzzLogic.MDistPair(tmpArray),puzzLogic.Max_swap(tmpArray),puzzLogic.RCout(tmpArray)));
					}
					if((tmp=puzzLogic.Up(array, rowLength, indOfBlank))!=null)
					{
						//System.out.println(tmp);
						tmpArray = puzzLogic.convertToArray(tmp);
						stck.push(new Node(top.depth+1,tmp,top.puzzleConf,puzzLogic.MDistPair(tmpArray),puzzLogic.Max_swap(tmpArray),puzzLogic.RCout(tmpArray)));
					}			
					
					if((tmp=puzzLogic.Left(array, rowLength, indOfBlank))!=null)
					{
						//System.out.println(tmp);
						tmpArray = puzzLogic.convertToArray(tmp);
						stck.push(new Node(top.depth+1,tmp,top.puzzleConf,puzzLogic.MDistPair(tmpArray),puzzLogic.Max_swap(tmpArray),puzzLogic.RCout(tmpArray)));
					}	
					
					if((tmp=puzzLogic.Right(array, rowLength, indOfBlank))!=null)
					{
						//System.out.println(tmp);
						tmpArray = puzzLogic.convertToArray(tmp);
						stck.push(new Node(top.depth+1,tmp,top.puzzleConf,puzzLogic.MDistPair(tmpArray),puzzLogic.Max_swap(tmpArray),puzzLogic.RCout(tmpArray)));
					}
					
					newKey.set(top.puzzleConf);
					newValue.set("P"+'\t'+top.parent+'\t'+top.mdist+'\t'+top.maxSwap+'\t'+top.rcOut+'\t'+top.depth);
					context.write(newKey,newValue);
				}
				else
				{
					newKey.set(top.puzzleConf);
					newValue.set("C"+'\t'+top.parent+'\t'+top.mdist+'\t'+top.maxSwap+'\t'+top.rcOut+'\t'+top.depth);
					context.getCounter(myCounters.numChildBefore).increment(1);
					context.write(newKey,newValue);	
				}
							
				if(!stck.empty())top = stck.pop(); 
				else break;
			}	
		}
		 catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

}
