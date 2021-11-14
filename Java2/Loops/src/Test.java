	import java.util.List;

	public class Test {

	  // if false skips test with 15 nested loops
	  static final boolean doLargeTest = true;

	  static int errorCount = 0;
	  static int testCount = 0;

	  public static void pError(List<Integer> lower, List<Integer> upper, String expected, String result) {
	    System.err.println("Error: test " + testCount + " failed:");
	    System.err.println("lower:\t" + lower);
	    System.err.println("upper:\t" + upper);
	    System.err.println("result: \t" + result);
	    System.err.println("expected: \t" + expected);
	    errorCount++;
	  }

	  public static void testString(List<Integer> lower, List<Integer> upper, String expected) {
	    Loops loopTest = new Loops();
	    if (lower != null)
	      loopTest.setLowerLimits(lower);
	    if (upper != null)
	      loopTest.setUpperLimits(upper);
	    String result = loopTest.getResult().toString();
	    if (!result.equals(expected)) {
	      pError(lower, upper, expected, result);
	    }
	    testCount++;
	  }

	  public static void objectTests() {
	    Loops loopTest = new Loops();

	    loopTest.setUpperLimits(List.of(1, 1, 2));
	    String result1 = loopTest.getResult().toString();
	    String expected1 = "[[0, 0, 0], [0, 0, 1], [0, 0, 2], [0, 1, 0], [0, 1, 1], [0, 1, 2], [1, 0, 0], [1, 0, 1], [1, 0, 2], [1, 1, 0], [1, 1, 1], [1, 1, 2]]";

	    String result2 = loopTest.getResult().toString();
	    // should match expected1

	    loopTest.setUpperLimits(List.of(1, 1, 2, 0));
	    String result3 = loopTest.getResult().toString();
	    String expected3 = "[[0, 0, 0, 0], [0, 0, 1, 0], [0, 0, 2, 0], [0, 1, 0, 0], [0, 1, 1, 0], [0, 1, 2, 0], [1, 0, 0, 0], [1, 0, 1, 0], [1, 0, 2, 0], [1, 1, 0, 0], [1, 1, 1, 0], [1, 1, 2, 0]]";

	    loopTest.setLowerLimits(List.of(-1, -1, 1, 0));

	    String result4 = loopTest.getResult().toString();
	    String expected4 = "[[-1, -1, 1, 0], [-1, -1, 2, 0], [-1, 0, 1, 0], [-1, 0, 2, 0], [-1, 1, 1, 0], [-1, 1, 2, 0], [0, -1, 1, 0], [0, -1, 2, 0], [0, 0, 1, 0], [0, 0, 2, 0], [0, 1, 1, 0], [0, 1, 2, 0], [1, -1, 1, 0], [1, -1, 2, 0], [1, 0, 1, 0], [1, 0, 2, 0], [1, 1, 1, 0], [1, 1, 2, 0]]";

	    loopTest.setLowerLimits(List.of(0, 0, 0, 0));

	    String result5 = loopTest.getResult().toString();
	    // should match expected3

	    // test 1 normal method call
	    if (!result1.equals(expected1)) {
	      pError(null, List.of(1, 1, 2), expected1, result1);
	    }
	    testCount++;

	    // call getResult() twice in a row
	    if (!result2.equals(expected1)) {
	      pError(null, List.of(1, 1, 2), expected1, result2);
	    }
	    testCount++;

	    // update upperLimit on existing object
	    if (!result3.equals(expected3)) {
	      pError(null, List.of(1, 1, 2, 0), expected3, result3);
	    }
	    testCount++;

	    // set lowerLimit after getResult() was once called
	    if (!result4.equals(expected4)) {
	      pError(List.of(-1, -1, 1, 0), List.of(1, 1, 2, 0), expected4, result4);
	    }
	    testCount++;

	    // update lowerLimit after getResult() was once called
	    if (!result5.equals(expected3)) {
	      pError(List.of(0, 0, 0, 0), List.of(1, 1, 2, 0), expected4, result5);
	    }
	    testCount++;

	  }

	  public static void main(String[] args) {
	    // test from docs
	    testString(List.of(0, 0, 1), List.of(1, 2, 2),
	        "[[0, 0, 1], [0, 0, 2], [0, 1, 1], [0, 1, 2], [0, 2, 1], [0, 2, 2], [1, 0, 1], [1, 0, 2], [1, 1, 1], [1, 1, 2], [1, 2, 1], [1, 2, 2]]");

	    // positive numbers test
	    testString(List.of(0, 1, 2), List.of(3, 3, 3),
	        "[[0, 1, 2], [0, 1, 3], [0, 2, 2], [0, 2, 3], [0, 3, 2], [0, 3, 3], [1, 1, 2], [1, 1, 3], [1, 2, 2], [1, 2, 3], [1, 3, 2], [1, 3, 3], [2, 1, 2], [2, 1, 3], [2, 2, 2], [2, 2, 3], [2, 3, 2], [2, 3, 3], [3, 1, 2], [3, 1, 3], [3, 2, 2], [3, 2, 3], [3, 3, 2], [3, 3, 3]]");
	    // negative number test
	    testString(List.of(-1, -1, -1), List.of(0, 1, 0),
	        "[[-1, -1, -1], [-1, -1, 0], [-1, 0, -1], [-1, 0, 0], [-1, 1, -1], [-1, 1, 0], [0, -1, -1], [0, -1, 0], [0, 0, -1], [0, 0, 0], [0, 1, -1], [0, 1, 0]]");
	    testString(List.of(-10, -11, -12), List.of(-9, -10, -11),
	        "[[-10, -11, -12], [-10, -11, -11], [-10, -10, -12], [-10, -10, -11], [-9, -11, -12], [-9, -11, -11], [-9, -10, -12], [-9, -10, -11]]");
	    // no setLowerLimits call
	    testString(null, List.of(2, 2), "[[0, 0], [0, 1], [0, 2], [1, 0], [1, 1], [1, 2], [2, 0], [2, 1], [2, 2]]");
	    testString(null, List.of(2, 1, 0), "[[0, 0, 0], [0, 1, 0], [1, 0, 0], [1, 1, 0], [2, 0, 0], [2, 1, 0]]");
	    testString(null, List.of(0, 0, 0), "[[0, 0, 0]]");
	    testString(null, List.of(0, 0, 2), "[[0, 0, 0], [0, 0, 1], [0, 0, 2]]");
	    // no setUpperLimits call
	    testString(List.of(0, 0, 0), null, "[[0, 0, 0]]");
	    testString(List.of(-1, -1, -2), null,
	        "[[-1, -1, -2], [-1, -1, -1], [-1, -1, 0], [-1, 0, -2], [-1, 0, -1], [-1, 0, 0], [0, -1, -2], [0, -1, -1], [0, -1, 0], [0, 0, -2], [0, 0, -1], [0, 0, 0]]");
	    testString(List.of(-3, -2, -1), null,
	        "[[-3, -2, -1], [-3, -2, 0], [-3, -1, -1], [-3, -1, 0], [-3, 0, -1], [-3, 0, 0], [-2, -2, -1], [-2, -2, 0], [-2, -1, -1], [-2, -1, 0], [-2, 0, -1], [-2, 0, 0], [-1, -2, -1], [-1, -2, 0], [-1, -1, -1], [-1, -1, 0], [-1, 0, -1], [-1, 0, 0], [0, -2, -1], [0, -2, 0], [0, -1, -1], [0, -1, 0], [0, 0, -1], [0, 0, 0]]");
	    // no method calls
	    testString(null, null, "[[0]]");

	    // runs multiple tests on the same class object
	    objectTests();

	    // print results
	    if (errorCount == 0)
	      System.out.println("All " + testCount + " tests passed succesfully");
	    else
	      System.out.println(errorCount + " Tests failed");

	    if (!doLargeTest)
	      return;
	    // test with 15 nested loops
	    System.out.println("\n------\nTesting with 15 nested loops - this might take a moment ( ram="
	        + String.format("%.02f", Runtime.getRuntime().maxMemory() / 1073741824f) + "GB )");
	    Loops loopTest = new Loops();
	    loopTest.setLowerLimits(List.of(1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1));
	    loopTest.setUpperLimits(List.of(3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 3, 3, 3, 2, 2));
	    if (loopTest.getResult().toString().length() != 473651712) {
	      System.err.println("15 loops test failed, but program managed to not crash");
	    } else {
	      System.out.println("Program managed to not crash with 15 loops and the result length seems ok");
	    }
	  }
	}
