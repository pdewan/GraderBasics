package gradingTools.shared.testcases.openmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class OpenMPUtils {
	
	protected static Map<String, OpenMPKeywordEnum> stringToOpenMPKeyword = new HashMap();
	protected static String[] typeNames = {"double", "float", "int", "short", "long"};
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
	
	public static boolean startsWithTypeName(String aLine) {
		for (String aTypeName:typeNames) {
			if (aLine.startsWith(aTypeName)) {
				return true;
			}
		}
		return false;
	}

	public static List<OpenMPPragma> getOpenMPPragmas(String[] aFileLines) {
		List<OpenMPPragma> retVal = new ArrayList();
		Stack<OpenMPPragma> anOpenMPPragmas = new Stack();
		Stack<Integer> aNumOpenBracesStack = new Stack();
		OpenMPPragma lastOpenMPPragma = null;
		String lastReductionVariable = null;
		String lastReductionOperation = null;
//		int aNumOpenBraces = 0;
		boolean aNextCodeLineIsAPragmaBlock = false;
		for (int i = 0; i < aFileLines.length; i++) {
			String aFileLine = aFileLines[i].trim();
			if (!isCodeLine(aFileLine)) {
				continue;
			}
			addToStack(anOpenMPPragmas, aFileLine); // if it is empty add to none
			if (lastReductionVariable != null) {
				if (aFileLine.startsWith(lastReductionVariable)) {
					lastOpenMPPragma.getReductionVariableAssignments().add(aFileLine);
					if (lastReductionOperation != null) { // can it ever be not null
						if (aFileLine.contains(lastReductionOperation)) {
							lastOpenMPPragma.getReductionOperationUses().add(aFileLine);
						}
					}
				}			
			}
			if (lastOpenMPPragma != null && lastOpenMPPragma.getFirstOpenMPKeyword() == OpenMPKeywordEnum.PARALLEL) {
				if (startsWithTypeName(aFileLine)) {
					lastOpenMPPragma.getVariableDeclarationsInParallel().add(aFileLine);
				}
			} else if (lastOpenMPPragma != null && lastOpenMPPragma.getFirstOpenMPKeyword() == OpenMPKeywordEnum.CRITICAL) {
				String[] aTokens = aFileLine.split("\\s+");
				lastOpenMPPragma.setAssignedVariableInCritical(aTokens[0]);				
			}
			if (isPragmaStart(aFileLine)) {
				
				lastOpenMPPragma = getOpemMPPragma(aFileLine, i);
				if (lastOpenMPPragma != null) {
					anOpenMPPragmas.add(lastOpenMPPragma);
					aNumOpenBracesStack.add(0);
					retVal.add(lastOpenMPPragma);					
					aNextCodeLineIsAPragmaBlock = true;
					lastReductionOperation = lastOpenMPPragma.getReductionOperation();
					lastReductionVariable = lastOpenMPPragma.getReductionVariable();
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
				lastOpenMPPragma = null;
				lastReductionVariable = null;
				lastReductionOperation = null;
				continue;
			}					
		}
		return retVal;
	}
	public static OpenMPPragma getOpemMPPragma(String aFileLine, int aLineIndex) {
		String[] aTokens = aFileLine.split("\\s+");
		if (aTokens.length <= 2) {
			return null;
		}
		if (!aTokens[1].equals("omp")) {
			return null;
		}
		OpenMPPragma retVal = new AnOpenMPPragma(aLineIndex);

		for (int i = 2; i < aTokens.length; i ++) {
			String aStoredToken = aTokens[i].trim();
			if (aStoredToken.isEmpty()) {
				continue;
			}
			if (aStoredToken.startsWith("reduction")) {
				while (!aStoredToken.endsWith(")")) {
					i++;
					if (i >= aTokens.length) {
						break;
					}
					String aNewToken = aTokens[i].trim();
					aStoredToken += aNewToken; 					
				}
				int aLeftParenIndex = aStoredToken.indexOf("(");
				int aRightParenIndex = aStoredToken.indexOf(")");
				int aColonIndex = aStoredToken.indexOf(":");
				if (aLeftParenIndex != -1 && aRightParenIndex != -1 && aColonIndex != -1) {
					String anOperationString = aStoredToken.substring(aLeftParenIndex + 1, aColonIndex).trim();
					String aVariableString = aStoredToken.substring(aColonIndex + 1, aRightParenIndex).trim();
					retVal.setReductionVariable(aVariableString);
					retVal.setReductionOperation(anOperationString);
				}
				
			}
//			aTokens[i] = aStoredToken;
			retVal.getOpenMPTokens().add(aStoredToken);
		}
		String aFirstToken = retVal.getOpenMPTokens().get(0);
		retVal.setFirstOpenMPKeyword(stringToOpenMPKeyword.get(aFirstToken));		
		return retVal;
		
		
	}
	static {
		stringToOpenMPKeyword.put("for", OpenMPKeywordEnum.FOR);
		stringToOpenMPKeyword.put("parallel", OpenMPKeywordEnum.PARALLEL);
		stringToOpenMPKeyword.put("reduce", OpenMPKeywordEnum.REDUCE);
		stringToOpenMPKeyword.put("shared", OpenMPKeywordEnum.SHARED);
		stringToOpenMPKeyword.put("private", OpenMPKeywordEnum.PRIVATE);
		stringToOpenMPKeyword.put("critical", OpenMPKeywordEnum.CRITICAL);





	}
	

}
