import java.util.List;
import java.util.Arrays;


public class Tester {
	public static void main (String[] Args) {
		List<Integer> limits = Arrays.asList (-2, 3, 1);
		
		GeneralLoops obiekt = new Loops();
		
		//obiekt.setLowerLimits(limits);
		
		limits = Arrays.asList (1, 1, 2);
		
		obiekt.setUpperLimits(limits);
		
		System.out.println(obiekt.getResult());
	}
}
