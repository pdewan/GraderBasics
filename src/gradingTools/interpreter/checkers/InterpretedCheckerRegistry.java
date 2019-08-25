package gradingTools.interpreter.checkers;

import java.util.HashMap;
import java.util.Map;

public class InterpretedCheckerRegistry {
	public static final String DIFF_NAME = "diff";
	public static final String MATCH_NAME = "match";
	static Map<String, InterpretedChecker> nameToChecker = new HashMap<>();
	public static void register(String aName, InterpretedChecker aChecker) {
		nameToChecker.put(aName, aChecker);
	}
	
	public static InterpretedChecker getInterpretedChecker(String aName) {
		return nameToChecker.get(aName);
	}
	
	static {
		register(DIFF_NAME, new AnExternalDiffChecker());
		register(MATCH_NAME, new AMatchChecker());
	}

}
