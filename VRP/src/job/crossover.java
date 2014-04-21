package job;
// modified order crossover(OX) operator

import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;

public class crossover {		

	public static String[] doCrossover(String can1,String can2) throws IOException 
	{	
		int[] canArray1 = toArray(can1);
		int[] canArray2 = toArray(can2);
		
		Random rnd = new Random();
		int first = rnd.nextInt(canArray1.length);
		int second = rnd.nextInt(canArray1.length);
		
		int tmp;
		for(int i=first+1;i<second;i++)
		{
			tmp = canArray1[i];
			canArray1[i] = canArray2[i];
			canArray2[i] = tmp;
		}
		String[] cans = new String[2];
		
		cans[0] = tostring(mutate(canArray1));
		cans[1] = tostring(mutate(canArray2));
		return cans;
	}
	
	
	static int[] mutate(int[] can)
	{
		int t=100;
		
		Random rnd = new Random();
		rnd.nextInt(100);
		
		if(t<25)
		{
			int i = rnd.nextInt(can.length);
			int j = rnd.nextInt(can.length);
			
			t = can[i];
			can[i] = can[j];
			can[j] = t;
			return can;
		}
		return can;
	}
	
	static int[] toArray(String str)
	{
		StringTokenizer token = new StringTokenizer(str,",");
		int[] array = new int[token.countTokens()];
		int i=0;
		while(token.hasMoreTokens())
			array[i++] = Integer.parseInt(token.nextToken());
		return array;
	}

	static String  tostring(int[] can)
    {
    	StringBuffer buf = new StringBuffer();
    	int i;
    	for(i=0;i<can.length-1;i++)
    		buf.append(can[i]+",");
    	buf.append(can[i]);
    	return buf.toString();
    }
	
}
