package job;

public class solvable 
{
	public boolean solv(String puzzleConf)
	{
		PuzzleLogic puzzLogic = new PuzzleLogic();
		puzzLogic.tokenizeBuildPuzzle(puzzleConf);
		
		String[] box = puzzLogic.convertToArray(puzzleConf);
		boolean[] chck = new boolean[box.length-1];
		int num,inv=0;
		
		for(String sTemp:box)
		{
			if(!sTemp.equals("x"))
			{
				num = Integer.parseInt(sTemp);
				chck[num-1]=true;
				while(num--!=1)
					if(!chck[num-1])inv++;
			}
		}
		
		int numRow = (int)Math.sqrt(box.length);
		int xRow = puzzLogic.find(box,"x")/numRow; // find returns actual value-1
		
		
		if((numRow%2==1 && inv%2==0) || (numRow%2==0 && (numRow-1-xRow)%2==1 && inv%2==1) || (numRow%2==0 && (numRow-1-xRow)%2==0 && inv%2==0))
			return true;
		return false;		
	}
}