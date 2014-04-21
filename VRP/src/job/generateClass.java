package job;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;

import job.calculateFitness2Opt.Node;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class generateClass
{
	
	int customer[][];
	int distance[][];
	int type[][];	
	float speed[][];	
	int numNodes,cap;
	
	//numNodes includes count of depot
	//constructor
	public generateClass() throws Exception {
		
		FileInputStream fstream = new FileInputStream("/home/user/Documents/Hadoop Program Data/VRP/copy.TXT");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));		
		
		String line;
		StringTokenizer token;
		this.numNodes = Integer.parseInt(br.readLine());
		
		customer = new int[numNodes][6];
		cap = Integer.parseInt(br.readLine());
		
		int i=0,j;
		while((line=br.readLine())!=null)
		{
			j=0;
			token = new StringTokenizer(line,"	");
			token.nextToken();
			while(token.hasMoreTokens())
				customer[i][j++] = Integer.parseInt(token.nextToken());
			i++;
		}
		generate_distance();
		get_speed();		
	}
	
	void generate_distance() throws Exception
	{
		FileWriter fstream = new FileWriter("/home/user/Documents/Hadoop Program Data/VRP/type.txt");
		BufferedWriter out = new BufferedWriter(fstream);		
		Random rnd = new Random();
		
		FileWriter fstreamd = new FileWriter("/home/user/Documents/Hadoop Program Data/VRP/distance.txt");
		BufferedWriter outd = new BufferedWriter(fstreamd);		
		
		
		distance = new int[numNodes][numNodes];
		type = new int[numNodes][numNodes];
		
		StringBuffer str = new StringBuffer();
		StringBuffer strd = new StringBuffer();
		
		
		for(int i=0;i<numNodes;i++)
		{
			for(int j=0;j<numNodes;j++)
			{
				if(i==j)
				{
					distance[i][j]=0;
					type[i][j] = 0;
					str.append(type[i][j]+",");
					strd.append(distance[i][j]+",");
				}
				else
				{
					if(distance[j][i]==0)
						distance[i][j] = (int) Math.sqrt((customer[i][0] - customer[j][0])*(customer[i][0] - customer[j][0]) + (customer[i][1] - customer[j][1])*(customer[i][1] - customer[j][1]));
					else
						distance[i][j] = distance[j][i];
					
					if(type[j][i]!=0)
						type[i][j]=type[j][i];
					else
						type[i][j]=type[j][i] = rnd.nextInt(5)+1;
					str.append(type[i][j]+",");
					strd.append(distance[i][j]+",");
				}
			}
			str.deleteCharAt(str.length()-1);
			str.append('\n');
			
			strd.deleteCharAt(strd.length()-1);
			strd.append('\n');
			
		}
		out.write(str.toString());
		out.close();
		fstream.close();
		
		outd.write(strd.toString());
		outd.close();
		fstreamd.close();
	}
	
	void get_speed() throws IOException
	{
		 FileInputStream fstream = new FileInputStream("/home/user/Documents/Hadoop Program Data/VRP/speed.txt");
		 DataInputStream in = new DataInputStream(fstream);
		 BufferedReader br = new BufferedReader(new InputStreamReader(in));
		 String line;
		 
		 speed = new float[6][5];
		 StringTokenizer token;
		 
		 int i=1,j=1;
		 
		 while((line=br.readLine())!=null)
		 {
			 j=1;
			 token = new StringTokenizer(line);
			 while(token.hasMoreTokens())
				 speed[i][j++]=Float.parseFloat(token.nextToken());
			 i++;			 
		 }		 
	}
	
	public void generate_pop(int numReducers) throws IOException
	{		
		FileWriter fstream = new FileWriter("/home/user/Documents/Hadoop Program Data/VRP/pop_out.txt");
		BufferedWriter out = new BufferedWriter(fstream);

		int[] array = new int[numNodes];
		for(int i=1;i<numNodes;i++)
			array[i] = i;
		
		int[] array_1 = new int[numNodes];

		Random rnd = new Random();
		int t1=0,t2,t3;
		
		// 1/3 Random		
		for(int i=1;i<=numReducers*20;i++)
		{
			array_1 = Arrays.copyOf(array, array.length);
			for(int j=1;j<numNodes;j++)
			{
				t1 = rnd.nextInt(numNodes-1)+1;
				t2 = rnd.nextInt(numNodes-1)+1;
				t3 = array_1[t1];
				array_1[t1] = array_1[t2];
				array_1[t2] = t3;			
			}
			
			if(i<numReducers*5)
			{
				for(t1=1;t1<numNodes-1;t1++)
					out.append(array_1[t1]+",");
				out.append(""+array_1[t1]+'\n');
			}
			else if(i<numReducers*10)
				out.append(savings_generate(array_1));
			else
				out.append(NNC_generate(array_1));
			
		}
		out.flush();
		out.close();
		
		String dst = "/user/user/user/rohit/input/vrp_input/pop.txt";		
		InputStream in = new BufferedInputStream(new FileInputStream("/home/user/Documents/Hadoop Program Data/VRP/pop_out.txt"));
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		OutputStream out1 = fs.create(new Path(dst));		
		IOUtils.copyBytes(in, out1, 4096, true);
	}
	
	String NNC_generate(int[] array)
	{		
		boolean[] taken = new boolean[numNodes];		
		int count = 0;
		
		int i = array[1];
		Node n = new Node(i);
		n.cap_left -= customer[i][2];
		n.time_dep = customer[i][3]+customer[i][5];
		
		ArrayList<Node> list = new ArrayList<Node>();
		list.add(n);
		taken[i] = true;
		count++;
		
		int min,tmp,val=-1;
		
		while(count!=numNodes-1)
		{
			min = Integer.MAX_VALUE;
			val =-1;
			for(int k=1;k<numNodes;k++)
			{
				if(!taken[k])
				{
					tmp = check_feasibility(list.get(list.size()-1),k);
					if(tmp!=-1 && tmp<min)
					{ min=tmp; val = k;}
				}
			}
			
			if(val!=-1)
			{
				n = new Node(val);
				n.cap_left = list.get(list.size()-1).cap_left - customer[i][2];
				n.time_dep = min + customer[i][5];
				list.add(n);
				taken[val]=true;
				count++;
			}
			else if(count!=numNodes)
			{
				for(int k=1;k<numNodes;k++)
					if(!taken[array[k]])
					{
						i = array[k];
						n = new Node(i);
						n.cap_left -= customer[i][2];
						n.time_dep = customer[i][3]+customer[i][5];
						list.add(n);
						taken[i] = true;
						count++;
						break;
					}
			}
		}
		
		StringBuffer st = new StringBuffer();
		for(int k=0;k<list.size()-1;k++)
			st.append(list.get(k).value+",");
		st.append(""+list.get(list.size()-1).value+'\n');

		return st.toString();		
	}
	
	
	void printarray(int[] array)
	{
		for(int k=1;k<array.length;k++)
			System.err.print(array[k]+" ");
	}
	
	
	
	String savings_generate(int[] array)
	{
		ArrayList<ArrayList<Node>> routes = new ArrayList<ArrayList<Node>>();
		ArrayList<Node> tmp = null;
		Node n=null,m=null;		
		boolean[] taken = new boolean[numNodes];
		int time;		
		
		for(int i=1;i<array.length;i++)
		{
			if(!taken[array[i]])
			{
				tmp = new ArrayList<Node>();
				n = new Node(array[i]);
				n.time_dep = customer[n.value][3]+customer[n.value][5];
				n.cap_left -= customer[array[i]][2];
				tmp.add(n);		
				taken[array[i]] = true;
			
				for(int j=i+1;j<array.length;j++)
				{
					if(!taken[array[j]])
					{
						
						time = check_feasibility(tmp.get(tmp.size()-1),array[j]);
						if(time!=-1)
						{
							m = new Node(array[j]);
							m.cap_left -= customer[array[j]][2];
							m.time_dep = time+customer[array[j]][5];
							tmp.add(m);
							taken[array[j]] = true;
							n=m;
						}				
					}
				}
				routes.add(tmp);tmp=null;
			}
		}		
		
		StringBuffer s= new StringBuffer();		
		for(int i=0;i<routes.size();i++)
		{
			tmp = routes.get(i);
			for(int j=0;j<tmp.size();j++)
				s.append(tmp.get(j).value+",");
		}
		s.deleteCharAt(s.length()-1);
		s.append('\n');
		return s.toString();
	}
	

	int check_feasibility(Node n,int m)
	{
		int time;
		if(n.time_dep > customer[m][4] || n.cap_left < customer[m][2] ) return -1;
		else
		{
			time = getTime(n, m);			
			
			if(time>customer[m][4] || time==-1)
				return -1;
			else 
			{
				return time;
			}
		}			
	}
	
	int getTime(Node n,int m)
	{
		
		int div = customer[0][4]/4;
		int typer = type[n.value][m];
		int zone = (int)(n.time_dep/div)+1;		
		
		int dist = distance[n.value][m];	
		int ctime = n.time_dep;
		
		while(dist!=0 && zone<= type[1].length)
		{
			if((((zone*div)-ctime)*speed[typer][zone])>=dist)
			{
				ctime = ctime + (int)(dist/speed[typer][zone]);
				if(ctime<customer[m][3])
					return customer[m][3];
				else
					return ctime;
			}
			else
			{
				dist-=((zone*div)-ctime)*speed[typer][zone];
				ctime = (zone*div);
				zone++;
			}
		}
		return -1;
	}
	
	class Node
	{
		int value;
		int time_dep;
		int cap_left;
		
		public Node(int v) {
			value = v;
			time_dep = 0;
			cap_left = cap;
		}
	}

}
