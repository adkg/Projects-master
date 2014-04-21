package job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import job.generateClass.Node;

import org.apache.hadoop.io.IntWritable;

public class calculateFitness2Opt {

	int customer[][];
	int distance[][];
	int type[][];	
	float speed[][];
	int numNodes,cap;
	
	int opt_dist;//distance for opt
	
	int fit,veh,distf;
	
	public calculateFitness2Opt(int customer[][],int distance[][],int type[][],float speed[][],int cap) throws IOException {
		this.customer = customer;
		this.distance = distance;
		this.type = type;
		this.speed = speed;
		numNodes = customer.length;
		this.cap = cap;	
	}
	
	String getNumVeh()
	{
		return ""+fit+'\t'+distf+'\t'+veh;
	}
	
	void printarray(int[] array)
	{
		for(int k=0;k<array.length;k++)
			System.err.print(array[k]+" ");
		System.err.println();
	}
	
	int getFitness(int[] array, boolean opt) throws Exception
	{	
		int t_veh=0;
		int tot_fitness=0;
		int tot_dist=0,dist=0;
		t_veh++;
		Random rnd = new Random();
		
		int i = array[0];		
		Node n = new Node(i);
		n.cap_left -= customer[i][2];
		n.time_dep = customer[i][3]+customer[i][5];
		dist += distance[0][n.value];
		
		ArrayList<Node> list = new ArrayList<Node>();
		list.add(n);
		
		int tmp,first=0,last=0;
		
		for(int k=1;k<array.length;k++)
		{
			tmp = check_feasibility(list.get(list.size()-1),array[k]);			
			if(tmp!=-1)
			{
				last = k;
				i = array[k];
				n = new Node(i);
				n.cap_left = list.get(list.size()-1).cap_left - customer[i][2];
				n.time_dep = tmp + customer[i][5];
				dist+=distance[list.get(list.size()-1).value][i];
				list.add(n);
				
			}
			if(tmp==-1 || k == array.length-1)
			{
				int prb = rnd.nextInt(100);
				
				if(!opt && prb<40)
				{
					//printarray(array);
					//System.err.println("-----2opt----");
					int p[];
					p = TwoOpt(array,first,last,list.get(list.size()-1).time_dep, dist);
					tot_fitness += p[0];					
					dist =p[1];
				}
				else
				{
					tot_fitness +=getTime(list.get(list.size()-1),0);
					dist+=distance[list.get(list.size()-1).value][0];
				}
				
				last = first = last+1;				
				list.clear();	
				
				
				tot_dist += dist;
				if(tmp==-1 || k != array.length-1)
				{
					i = array[k];
					n = new Node(i);
					n.cap_left -= customer[i][2];
					n.time_dep = customer[i][3]+customer[i][5];
					list.add(n);
					if(k==array.length-1)
					{
						tot_fitness+=n.time_dep;
						dist += distance[0][n.value];
						dist += distance[0][n.value];
						tot_dist+=dist;
					}
				//	System.err.println(tot_dist);
					dist=0;
					dist += distance[0][n.value];
					t_veh++;					
				}
			}				
		}
		
		this.veh = t_veh;		
		this.fit = tot_fitness;
		this.distf = tot_dist;
		
		if(opt)
		{
			opt_dist = tot_dist;
			return tot_fitness;
		}
		else
			return (int) ((10000*numNodes*customer[0][4])/((float)t_veh*customer[0][4]+tot_fitness));		
	}
	
	
	int[] TwoOpt(int array[],int first,int last,int orig_fitness,int orig_dist) throws Exception
	{
		int t=0;
		int[] dummy = new int[last-first+1];	
		int[] tmp = new int[last-first+1];
		int fit1 = orig_fitness;
		
		
		for(int i=first;i<last;i++)
			for(int j=i+1;j<=last;j++)
			{
				t=0;
				for(int k=first;k<=i;k++) //first-a
					dummy[t++] = array[k]; 
				
				dummy[t++] = array[j];  // b
				
				for(int k=i+2;k<j;k++)  // c-b
					dummy[t++] = array[k];
				
				if(i+1<j)
					dummy[t++] = array[i+1]; //c
				for(int k=j+1;k<=last;k++) //d-last
					dummy[t++] = array[k];
				
				t = getFitness(dummy,true);
				
				if(t<fit1 && veh==1)
				{
					tmp = Arrays.copyOf(dummy,dummy.length);
					fit1 = t;
				}
			}
		
		if(fit1<orig_fitness)
		{
			t=0;
			for(int k=first;k<=last;k++)
				array[k] = tmp[t++];
		}
		else
			opt_dist = orig_dist;
		
		int[] ret_val = new int[2];
		ret_val[0] = fit1;
		ret_val[1] = opt_dist;
		
		return ret_val;
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
				Node tmpN = new Node(m);
				tmpN.cap_left = n.cap_left - customer[m][2];
				tmpN.time_dep = time + customer[m][5];
				if(getTime(tmpN,0)>customer[0][4])
					return -1;				
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
		int lastInd;
		
		public Node(int v) {
			value = v;
			time_dep = 0;
			cap_left = cap;
		}
	}	
}
