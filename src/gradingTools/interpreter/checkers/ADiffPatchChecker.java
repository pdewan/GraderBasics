package gradingTools.interpreter.checkers;

import java.util.LinkedList;

import name.fraser.neil.plaintext.diff_match_patch;

public class ADiffPatchChecker implements InterpretedChecker{
	

	static diff_match_patch diffMatchPatch = new diff_match_patch();

	
	
	public int getNumArgs() {
		return 2;
	}	
	public static CheckerResult check(String aString1, String aString2) {
		try {
			
			LinkedList<diff_match_patch.Diff> diff = diffMatchPatch.diff_main(aString1, aString2);
			diffMatchPatch.diff_cleanupSemantic(diff);	
			String aMessage = diff.toString();
			if (diff.size() == 0) {
				return new ACheckerResult(aMessage, true);
			}
			for (diff_match_patch.Diff aPatch:diff) {
				switch (aPatch.operation) {
				case EQUAL: continue;
				case INSERT: 
				case DELETE: {
					if (aPatch.text.trim().length() == 0) {
						continue;
					} else {
						return new ACheckerResult(aMessage, false);
					}
				}
				}
				
			}
			return new ACheckerResult(aMessage, true);

		} catch (Exception e) {
			e.printStackTrace();
			return new ACheckerResult(e.getMessage(), false);
		}

	}
	@Override
	public CheckerResult check(String[] anArgs) {
		
		try {
			String aString1 = anArgs[0];
			String aString2 = anArgs[1];
			return check(aString1, aString2);
//			LinkedList<diff_match_patch.Diff> diff = diffMatchPatch.diff_main(aString1, aString2);
//			diffMatchPatch.diff_cleanupSemantic(diff);	
//			String aMessage = diff.toString();
//			if (diff.size() == 0) {
//				return new ACheckerResult(aMessage, true);
//			}
//			for (diff_match_patch.Diff aPatch:diff) {
//				switch (aPatch.operation) {
//				case EQUAL: continue;
//				case INSERT: 
//				case DELETE: {
//					if (aPatch.text.trim().length() == 0) {
//						continue;
//					} else {
//						return new ACheckerResult(aMessage, false);
//					}
//				}
//				}
//				
//			}
//			return new ACheckerResult(aMessage, true);

		} catch (Exception e) {
			e.printStackTrace();
			return new ACheckerResult(e.getMessage(), false);
		}

	}


	

}
