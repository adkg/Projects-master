package job;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class KeyHeu implements WritableComparable<KeyHeu> {

	private IntWritable numRed;
	private IntWritable MD_heuristic;
	private IntWritable Max_Swap_heuristic;
	private IntWritable Row_col_heuristic;
	
	
	public IntWritable getNum()
	{
		return numRed;
	}
	
	public IntWritable getMDHeuristic()
	{
		return MD_heuristic;
	}
	
	public IntWritable getMaxSwapHeuristic()
	{
		return Max_Swap_heuristic;
	}
	
	public IntWritable getRowColHeuristic()
	{
		return Row_col_heuristic;
	}
	
	public KeyHeu()
	{
		this.numRed = new IntWritable();
		this.MD_heuristic = new IntWritable();
		this.Max_Swap_heuristic = new IntWritable();
		this.Row_col_heuristic = new IntWritable();
	}
	
	public KeyHeu(int num1,int heur1,int heur2,int heur3)
	{
		this.numRed = new IntWritable(num1);
		this.MD_heuristic = new IntWritable(heur1);
		this.Max_Swap_heuristic = new IntWritable(heur3);
		this.Row_col_heuristic = new IntWritable(heur3);
	}	
	
	@Override
	public void readFields(DataInput in) throws IOException {
		numRed.readFields(in);
		MD_heuristic.readFields(in);
		Max_Swap_heuristic.readFields(in);
		Row_col_heuristic.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		numRed.write(out);
		MD_heuristic.write(out);
		Max_Swap_heuristic.write(out);
		Row_col_heuristic.write(out);
	}
	
	@Override
	public int compareTo(KeyHeu key) {		
		int cmp =  numRed.compareTo(key.getNum());
		
		if(cmp!=0) // not in same reducer
			return cmp;
		
		
		float heu,keyHeu;
		float finalHeu;		
		
		//Giving weightage to different heuristics
		
		
		/*
		 *To make values comparable
		 *comparevalue * [(this-key)/max(this,key)]		
		*/		
		/*
		cmp = MD_heuristic.compareTo(key.getMDHeuristic());
		heu = MD_heuristic.get();
		keyHeu = key.getMDHeuristic().get();
		
		finalHeu = 5*((heu>keyHeu)?(heu-keyHeu)/heu:(keyHeu-heu)/keyHeu)*cmp;
		
		cmp = Max_Swap_heuristic.compareTo(key.getMaxSwapHeuristic());
		heu = Max_Swap_heuristic.get();
		keyHeu = key.getMaxSwapHeuristic().get();
		
		finalHeu += 3*((heu>keyHeu)?(heu-keyHeu)/heu:(keyHeu-heu)/keyHeu)*cmp;
		
		cmp = Row_col_heuristic.compareTo(key.getRowColHeuristic());	
		heu = Row_col_heuristic.get();
		keyHeu = key.getRowColHeuristic().get();
		
		finalHeu += 2*((heu>keyHeu)?(heu-keyHeu)/heu:(keyHeu-heu)/keyHeu)*cmp;
		
		if(finalHeu>0)return 1;
		else if(finalHeu==0) return 0;		
		return -1;
		*/
		
		cmp = MD_heuristic.compareTo(key.getMDHeuristic());
		if(cmp!=0)
			return cmp;
		
		cmp = Max_Swap_heuristic.compareTo(key.getMaxSwapHeuristic());
		if(cmp!=0)
			return cmp;
		
		return Row_col_heuristic.compareTo(key.getRowColHeuristic());		
		
	}
}
