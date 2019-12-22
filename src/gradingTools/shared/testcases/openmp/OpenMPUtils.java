package gradingTools.shared.testcases.openmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class OpenMPUtils {
//	protected static Map<String, OpenMPKeywordEnum> stringToOpenMPKeyword = new HashMap();
	public static List<OpenMPPragma> getOpemMPPragmas(StringBuffer aFileBuffer) {
		String[] aFileLines = aFileBuffer.toString().split("\n");
		return getOpenMPPragmas(aFileLines);
	}
	
	public static boolean isCodeLine(String aLine) {
		return !aLine.isEmpty() && !aLine.startsWith("//");
	}
	public static boolean isBlockStart(String aLine) {
		return aLine.contains("{");
	}
	public static boolean isBlockEnd(String aLine) {
		return aLine.contains("}");
	}
	public static boolean isLoopStart(String aLine) {
		return aLine.startsWith("for");
	}
	public static boolean endsWithLoopHeader(String aLine) {
		return aLine.endsWith(")");
	}
	public static boolean isPragmaStart(String aLine) {
		return aLine.startsWith("#pragma");
	}
	public static void addToStack(Stack<OpenMPPragma> anOpenMPPragmas, String aFileLine) {
		for (OpenMPPragma anOpenMPPragma:anOpenMPPragmas) {
			anOpenMPPragma.getAnnotatedText().add(aFileLine);
		}
	}
	
	public static void incrementStackTop (Stack<Integer> aNumOpenBracesStack) {
		int aTopIndex = aNumOpenBracesStack.size() - 1;
		Integer aCurrentValue = aNumOpenBracesStack.get(aTopIndex);
		aNumOpenBracesStack.set(aTopIndex, aCurrentValue + 1);
	}
	public static void decrementStackTop (Stack<Integer> aNumOpenBracesStack) {
		int aTopIndex = aNumOpenBracesStack.size() - 1;
		Integer aCurrentValue = aNumOpenBracesStack.get(aTopIndex);
		aNumOpenBracesStack.set(aTopIndex, aCurrentValue - 1);
	}

	public static List<OpenMPPragma> getOpenMPPragmas(String[] aFileLines) {
		List<OpenMPPragma> retVal = new ArrayList();
		Stack<OpenMPPragma> anOpenMPPragmas = new Stack();
		Stack<Integer> aNumOpenBracesStack = new Stack();
//		int aNumOpenBraces = 0;
		boolean aNextCodeLineIsAPragmaBlock = false;
		for (int i = 0; i < aFileLines.length; i++) {
			String aFileLine = aFileLines[i].trim();
			if (!isCodeLine(aFileLine)) {
				continue;
			}
			addToStack(anOpenMPPragmas, aFileLine); // if it is empty add to none
			if (isPragmaStart(aFileLine)) {
				
				OpenMPPragma anOpenMPPragma = getOpemMPPragma(aFileLine, i);
				if (anOpenMPPragma != null) {
					anOpenMPPragmas.add(anOpenMPPragma);
					aNumOpenBracesStack.add(0);
					retVal.add(anOpenMPPragma);					
					aNextCodeLineIsAPragmaBlock = true;
					continue;
				} 
			}
			if (anOpenMPPragmas.isEmpty()) {
				continue;
			}	
			if (aNextCodeLineIsAPragmaBlock) {				
				anOpenMPPragmas.peek().setAnnotatedTextStartLineNumber(i);				
				aNextCodeLineIsAPragmaBlock = false;
			}
//			anOpenMPPragmas.peek().getAnnotatedText().add(aFileLine);
			if (isBlockStart(aFileLine)) {
				incrementStackTop(aNumOpenBracesStack);
			}
//			} else 
			if (isBlockEnd(aFileLine)) {
				decrementStackTop(aNumOpenBracesStack);

			}
			if (isLoopStart(aFileLine) && endsWithLoopHeader(aFileLine)) {
				continue; // gather following block also
			}
			if (aNumOpenBracesStack.peek() == 0) {
				
				anOpenMPPragmas.peek().setAnnotatedTextEndLineNumber(i);
				anOpenMPPragmas.pop();
				aNumOpenBracesStack.pop();
				continue;
			}					
		}
		return retVal;
	}
	public static OpenMPPragma getOpemMPPragma(String aFileLine, int aLineIndex) {
		String[] aTokens = aFileLine.split(" ");
		if (aTokens.length <= 2) {
			return null;
		}
		if (!aTokens[1].equals("omp")) {
			return null;
		}
		for (int i = 2; i < aTokens.length; i ++) {
			String aStoredToken = aTokens[i].trim().toLowerCase();
			aTokens[i] = aStoredToken;
		}
		OpenMPPragma retVal = new AnOpenMPPragma(aTokens, aLineIndex);
		return retVal;
		
		
	}
	static {
//		stringToOpenMPKeyword.put("for", OpenMPKeywordEnum.FOR);
//		stringToOpenMPKeyword.put("parallel", OpenMPKeywordEnum.PARALLEL);
//		stringToOpenMPKeyword.put("reduce", OpenMPKeywordEnum.REDUCE);
//		stringToOpenMPKeyword.put("shared", OpenMPKeywordEnum.SHARED);
//		stringToOpenMPKeyword.put("private", OpenMPKeywordEnum.PRIVATE);




	}
	

}
