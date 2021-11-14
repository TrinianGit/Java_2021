import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

class Loops implements GeneralLoops {
	
	private List<Integer> LowerLimits = new ArrayList<Integer>(Arrays.asList(0));
	private List<Integer> UpperLimits = new ArrayList<Integer>(Arrays.asList(0));
	private List<Integer> Iterator_Result = new ArrayList<Integer> ();
	private List<List<Integer>> Result;

	@Override
	public void setLowerLimits(List<Integer> limits) {
		LowerLimits = new ArrayList<Integer> (limits);
		
	}

	@Override
	public void setUpperLimits(List<Integer> limits) {
		UpperLimits = new ArrayList<Integer> (limits);
	}

	@Override
	public List<List<Integer>> getResult() {
		Result = new ArrayList<List<Integer>>();
		if (LowerLimits.size() < UpperLimits.size())  {
			setLowerLimits(new ArrayList<Integer>(Collections.nCopies(UpperLimits.size(), 0)));
		}
		else if (LowerLimits.size() > UpperLimits.size()) {
			setUpperLimits(new ArrayList<Integer>(Collections.nCopies(LowerLimits.size(), 0)));
		}
		Iterator(LowerLimits.get(0), UpperLimits.get(0), 0);
		return Result;
	}
	
	private void Iterator (Integer LowerLimit, Integer UpperLimit, Integer n) {
		for (Integer counter = LowerLimit; counter <= UpperLimit; counter++) {
			if (n == UpperLimits.size()) {
				Result.add(new ArrayList<Integer>(Iterator_Result));
			}
			else {
				Iterator_Result.add(counter);
				if (n+1 < UpperLimits.size())	Iterator(LowerLimits.get(n+1), UpperLimits.get(n+1), n+1);
				else Iterator(0, 0, UpperLimits.size());
				Iterator_Result.remove(Iterator_Result.size()-1);
			}
		}
	}

}
