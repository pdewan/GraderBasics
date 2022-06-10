package gradingTools.shared.testcases;

import java.util.Arrays;

public class AssignmentSuiteSkeleton {
	static String mainClass;
	public static String getMainClass() {
		return mainClass;
	}
	public static void processArgs(String[] args) {
		if (args.length > 1) {
			System.err.println("More than one main argument " + Arrays.toString(args) +  "\nProvide only the main class name as an argument");
			System.exit(-1);
		}
		if (args.length == 1) {
			mainClass = args[0];
		}
	}
}
