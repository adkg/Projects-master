package job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;

public class PuzzleLogic {

	String finalString;
	String[] finalArray;	
	
	void tokenizeBuildPuzzle(String s)
	{
		StringTokenizer token = new StringTokenizer(s,",");
		int rowLength = token.countTokens();
		StringBuffer s1 = new StringBuffer();
		int i=1;
		while(i!=rowLength)
		{
			s1.append(i++);
			s1.append(',');
		}
		s1.append('x');
		finalString = s1.toString();
		finalArray = convertToArray(finalString);
	}
	
	
	
	String[] convertToArray(String s)
	{
		StringTokenizer token = new StringTokenizer(s,",");
		String[] array = new String[token.countTokens()];
		int i=0;
		while(token.hasMoreTokens())
			array[i++] = token.nextToken();
		return array;
	}	
	
	
	 // find returns actual value-1
	int find(String[] array,String s)
	{
		for(int i=0;i<array.length;i++)
			if(array[i].equals(s)) return i;
		return -1;			
	}
	
	String tostring(String[] array)
	{
		StringBuffer s = new StringBuffer();
		for(String s1:array)
		{
			s.append(s1);
			s.append(',');
		}
		s.deleteCharAt(s.length()-1);
		return s.toString();		
	}
	
	
	String Up (String[] s,int rowLength,int indOfBlank)
	{		
		String[] s1 = Arrays.copyOf(s,s.length);		
		if(indOfBlank+1 > rowLength)
		{
			//System.err.println(indOfBlank+" "+rowLength);
			s1[indOfBlank] = s1[indOfBlank-rowLength];
			s1[indOfBlank-rowLength] = "x";
			return tostring(s1);
		}	
		return null;
	}
	
	String Down (String[] s, int rowLength, int indOfBlank)
	{		
		String[] s1 = Arrays.copyOf(s,s.length);
		if(indOfBlank+1 <= rowLength*(rowLength-1))
		{
			s1[indOfBlank] = s1[indOfBlank+rowLength];
			s1[indOfBlank+rowLength] = "x";
			return tostring(s1);
		}	
		return null;
	}
	
	String Right (String[] s, int rowLength, int indOfBlank)
	{		
		String[] s1 = Arrays.copyOf(s,s.length);

		if((indOfBlank+1)%rowLength!=0)
		{
			s1[indOfBlank] = s1[indOfBlank+1];
			s1[indOfBlank+1] = "x";
			return tostring(s1);
		}	
		return null;
	}
	
	String Left (String[] s, int rowLength, int indOfBlank)
	{		
		String[] s1 = Arrays.copyOf(s,s.length);
		if((indOfBlank+1)%rowLength!=1)
		{
			s1[indOfBlank] = s1[indOfBlank-1];
			s1[indOfBlank-1] = "x";
			return tostring(s1);
		}	
		return null;
	}
	
	String getJobId(String s)
	{
		StringTokenizer token = new StringTokenizer(s,"_");	
		token.nextToken();
		token.nextToken();
		return String.valueOf(Integer.parseInt(token.nextToken())+10);
	}
	
	
	boolean check(String[] s)
	{
		for(int i=0;i<s.length;i++)
			if(!s[i].equals(finalArray[i]))
				return false;		
		return true;
	}
	
	int Max_swap(String[] array)
	{
		String[] s = Arrays.copyOf(array,array.length);
		int[] b = new int[s.length];
		
		int swap=0;
		
		for(int i=0;i<s.length-1;i++)
			b[i] = find(s,String.valueOf(i+1));		
		b[b.length-1] = find(s,"x");
		
		while(!check(s))
		{
			if(b[b.length-1]==(b.length-1))
			{
				randomPut(s,b);
				swap++;
			}
			
			s[b[b.length-1]] = String.valueOf(b[b.length-1]+1);
			s[b[b[b.length-1]]] = "x";
			
			int t = b[b[b.length-1]];
			b[b[b.length-1]] = b[b.length-1];
			b[b.length-1] = t;			
			swap++;
		}
		return swap;
	}
	
	void randomPut(String[] s,int[] b)
	{
		Random rnd = new Random();
		int t=-1;		
		while(true)
		{
			t++;
			if(b[t]!=t)
			{
				//System.err.println(t+" "+b[t]+" "+s[b[t]]);
				s[b.length-1] = s[b[t]];
				s[b[t]]="x";
				b[b.length-1] = b[t];
				b[t] = b.length-1;
				break;
			}				
		}
	}
	
	
	int RCout(String[] s)
	{
		int num=0;
		int rowLength = (int)Math.sqrt(s.length);
		for(int i=0;i<s.length;i++)
		{
			if(!s[i].equals("x") && find(finalArray,s[i])/rowLength != i/rowLength)
				num++;
			if(!s[i].equals("x") && find(finalArray,s[i])%rowLength != i%rowLength)
				num++;
		}
		return num;
	}
	
	
	int MDistPair(String[] s)
	{
		int rowLength = (int)Math.sqrt(s.length);
		return (ManhattanHeuristics(s, rowLength)+distancePair(s, rowLength));
	}	
	
	
	private int ManhattanHeuristics(String[] s, int rowLength)
	{
		int mdist=0;
		
		for(int i=0;i<s.length;i++)
		{
			if(!s[i].equals("x"))
			{
				mdist += Math.abs(i/rowLength - (find(finalArray,s[i])/rowLength)); // row difference
				mdist += Math.abs((i%rowLength)- (find(finalArray,s[i])%rowLength)); // col difference
			}
		}
				
		String[] tmpArr = Arrays.copyOf(s,s.length);
		int rowNumber,numJ,numK;
		for(int i=0;i<tmpArr.length;i=i+rowLength)
		{
			rowNumber = i/rowLength;
			for(int j=rowNumber*rowLength;j<(rowNumber+1)*rowLength-1;j++)
			{
				for(int k=j+1;k<(rowNumber+1)*rowLength;k++)
				{
					if(tmpArr[j].equals("x"))tmpArr[j]=tmpArr.length+"";
					if(tmpArr[k].equals("x"))tmpArr[k]=tmpArr.length+"";
					
					numJ = Integer.parseInt(tmpArr[j]);
					numK = Integer.parseInt(tmpArr[k]);
					if((numJ-1)/rowLength == rowNumber && (numK-1)/rowLength == rowNumber)
					{
						if(numJ>numK)
							mdist+=2;
					}				
				}
			}
		}
		
		return mdist;
	}
	
	int distancePair(String[] s, int rowLength)
	{
		ArrayList<Integer> rowlist = new ArrayList<Integer>();
		int pdist=0;
		for(int i=0;i<s.length/rowLength;i++)
		{
			for(int j=i*rowLength;j<(i+1)*rowLength;j++)
				for(int k=j+1;k<(i+1)*rowLength;k++)
				{
					if(s[j].equals("x") && s[k].equals("x") && rowlist.indexOf(j)==-1 && rowlist.indexOf(k)==-1 && rPair(s,i,j,k,rowLength))
					{
						rowlist.add(j);
						rowlist.add(k);
						pdist+=2;
					}						
				}
		}
		ArrayList<Integer> collist = new ArrayList<Integer>();
				
		for(int i=0;i<s.length/rowLength;i++)
		{
			for(int j=i;j<i+1+rowLength*(rowLength-1);j=j+rowLength)
				for(int k=j+rowLength;k<i+1+rowLength*(rowLength-1);k=k+rowLength)
				{
					if(s[j].equals("x") && s[k].equals("x") && collist.indexOf(j)==-1 && collist.indexOf(k)==-1 && cPair(s,i,j,k,rowLength))
					{
						collist.add(j);
						collist.add(k);
						pdist+=2;
					}						
				}
		}
		return pdist;		
	}
	
	boolean rPair(String[] s,int i,int j,int k,int rowLength)
	{
		String[] finalArray =  convertToArray(finalString);
		
		int x,y;
		if(((x=find(finalArray,s[j])/rowLength) == find(finalArray,s[k])/rowLength) && x==i)
		{
			x = find(finalArray,s[j]);
			y = find(finalArray,s[k]);
			
			if((j>k && x<y)||(j<k && x>y))
				return true;
		}
		return false;
	}
	
	boolean cPair(String[] s,int i,int j,int k,int rowLength)
	{
		String[] finalArray =  convertToArray(finalString);
		
		int x,y;
		if( (x=find(finalArray,s[j])%rowLength) == find(finalArray,s[k])%rowLength && x==i)
		{
			x = find(finalArray,s[j]);
			y = find(finalArray,(s[k]));
			
			if((j>k && x<y)||(j<k && x>y))
				return true;
		}
		return false;
	}
	
}
