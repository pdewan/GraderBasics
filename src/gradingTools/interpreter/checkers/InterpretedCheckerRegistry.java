package gradingTools.interpreter.checkers;

import java.util.HashMap;
import java.util.Map;

public class InterpretedCheckerRegistry {
	public static final String DIFF_NAME = "diffTool";
	public static final String MATCH_NAME = "match";
	public static final String DIFF_MATCH = "diffMatch";
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
		register(DIFF_MATCH, new ADiffPatchChecker());

	}

}
