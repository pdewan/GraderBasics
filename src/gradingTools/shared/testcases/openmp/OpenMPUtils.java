package gradingTools.shared.testcases.openmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class OpenMPUtils {
	
	protected static Map<String, OpenMPKeywordEnum> stringToOpenMPKeyword = new HashMap();
	protected static String[] typeNames = {"double", "float", "int", "short", "long"};
	protected static String[] openMPCalls = {"omp_get_thread_num()", "omp_get_num_threads()"};
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
	public static void addToStack(Stack<OpenMPPragma> anOpenMPPragmas, String aFileLine, int aLineNumber) {
		if (anOpenMPPragmas.size() > 0) {
			anOpenMPPragmas.peek().addToAnnotatedText(aFileLine, aLineNumber);
		}
//		for (OpenMPPragma anOpenMPPragma:anOpenMPPragmas) {
//			anOpenMPPragma.addToAnnotatedText(aFileLine, aLineNumber);				
//		}
		
	}
	public static ForHeader getForHeader(String aFileLine, int aLineNumber) {
		if (!aFileLine.startsWith("for"))
			return null;
		int aLeftParenIndex = aFileLine.indexOf("(");
		int aRightParenIndex = aFileLine.indexOf(")");
		if (aLeftParenIndex < 0 || aRightParenIndex < 0) {
			return null;
		}
		String aHeaderString = aFileLine.substring(aLeftParenIndex + 1, aRightParenIndex);
		String[] aForComponents = aHeaderString.split(";");
		return new AForHeader(aForComponents[0], aForComponents[1], aForComponents[2], aLineNumber);
	}
	public static List<String> getOMPCalls(String aFileLine) {
		List<String> retVal = new ArrayList();
		for (String anOpenMPCall:openMPCalls) {
			if (aFileLine.contains(anOpenMPCall)) {
				retVal.add(anOpenMPCall);
			}
		}
		return retVal;
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
//		OpenMPPragma lastOpenMPPragma = null;
//		String lastReductionVariable = null;
//		String lastReductionOperation = null;
//		int aNumOpenBraces = 0;
		boolean aNextCodeLineIsAPragmaBlock = false;
		List<OpenMPPragma> newOpenMPPragmas = null;
		for (int i = 0; i < aFileLines.length; i++) {
			String aFileLine = aFileLines[i].trim();
			if (!isCodeLine(aFileLine)) {
				continue;
			}
			addToStack(anOpenMPPragmas, aFileLine, i); // if it is empty add to none
//			if (lastReductionVariable != null) {
//				if (aFileLine.startsWith(lastReductionVariable)) {
//					lastOpenMPPragma.getReductionVariableAssignments().add(aFileLine);
//					if (lastReductionOperation != null) { // can it ever be not null
//						if (aFileLine.contains(lastReductionOperation)) {
//							lastOpenMPPragma.getReductionOperationUses().add(aFileLine);
//						}
//					}
//				}			
//			}
//			if (lastOpenMPPragma != null && lastOpenMPPragma.getFirstOpenMPKeyword() == OpenMPKeywordEnum.PARALLEL) {
//				if (startsWithTypeName(aFileLine)) {
//					lastOpenMPPragma.getVariableDeclarationsInParallel().add(aFileLine);
//				}
//			} 
//			else if (lastOpenMPPragma != null && lastOpenMPPragma.getFirstOpenMPKeyword() == OpenMPKeywordEnum.CRITICAL) {
//				String[] aTokens = aFileLine.split("\\s+");
//				lastOpenMPPragma.setAssignedVariableInCritical(aTokens[0]);				
//			}
			if (isPragmaStart(aFileLine)) {
				
				newOpenMPPragmas = getOpenMPPragmas(aFileLine, i);
				if (newOpenMPPragmas.size() != 0) {
					for (OpenMPPragma anOpenMPPragma:newOpenMPPragmas) {
						if (anOpenMPPragmas.size() > 0) {
						    anOpenMPPragma.setParent(anOpenMPPragmas.peek());
						} else {
							retVal.add(anOpenMPPragma); // add only top level prgamas

						}
						anOpenMPPragmas.push(anOpenMPPragma);
						aNumOpenBracesStack.push(0);
//						retVal.add(anOpenMPPragma);

					}
//					anOpenMPPragmas.addAll(newOpenMPPragmas);
//					aNumOpenBracesStack.add(0);
//					retVal.add(lastOpenMPPragma);					
					aNextCodeLineIsAPragmaBlock = true;
//					String lastReductionOperation = lastOpenMPPragma.getReductionOperation();
//					String lastReductionVariable = lastOpenMPPragma.getReductionVariable();
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
				for (int j = 0; j < newOpenMPPragmas.size(); j++) {
					anOpenMPPragmas.pop();
					aNumOpenBracesStack.pop();
				}
//				lastOpenMPPragma = null;
//				lastReductionVariable = null;
//				lastReductionOperation = null;
				continue;
			}					
		}
		return retVal;
	}
	public static void setReductionData (OpenMPPragma lastChild, String aStoredToken, int aLeftParenIndex, int aRightParenIndex) {
		int aColonIndex = aStoredToken.indexOf(":");
		if (aLeftParenIndex != -1 && aRightParenIndex != -1 && aColonIndex != -1) {
			String anOperationString = aStoredToken.substring(aLeftParenIndex + 1, aColonIndex).trim();
			String aVariableString = aStoredToken.substring(aColonIndex + 1, aRightParenIndex).trim();
			((OpenMPForPragma) lastChild).setReductionVariable(aVariableString);
			((OpenMPForPragma) lastChild).setReductionOperation(anOperationString);
		}
	}
	public static void setSharedOrPrivateData (OpenMPPragma lastChild, String aStoredToken, int aLeftParenIndex, int aRightParenIndex, boolean isShared) {
//		int aColonIndex = aStoredToken.indexOf(":");
		if (aLeftParenIndex != -1 && aRightParenIndex != -1 ) {
			String aVariableDeclarations = aStoredToken.substring(aLeftParenIndex + 1, aRightParenIndex);
			String[] aVariables = aVariableDeclarations.split(",");
			if (isShared) {
				lastChild.setSharedVariables(aVariables);
			} else {
				lastChild.setPrivateVariables(aVariables);
			}			
		}
	}
	public static List<OpenMPPragma> getOpenMPPragmas(String aFileLine, int aLineIndex) {
		String[] aTokens = aFileLine.split("\\s+");
		if (aTokens.length <= 2) {
			return null;
		}
		if (!aTokens[1].equals("omp")) {
			return null;
		}
		if (aTokens.length < 3) {
			return null;
		}
		String aFirstToken = aTokens[2];
		OpenMPKeywordEnum anOpenMPKeyword = stringToOpenMPKeyword.get(aFirstToken);
		List<OpenMPPragma> retVal = new ArrayList();
		OpenMPPragma lastChild;
		int aStartIndex = 2;
		switch (anOpenMPKeyword) {
		case PARALLEL:
			lastChild = new AnOpenMPParallelPragma(aLineIndex);
			
//			lastChild.setFirstOpenMPKeyword(stringToOpenMPKeyword.get(aFirstToken));	

			retVal.add(lastChild);
			if (aTokens.length > 3 && aTokens[3].equals("for")) {
				OpenMPPragma aForChild = new AnOpenMPForPragma(aLineIndex);
				retVal.add(aForChild);
				lastChild = aForChild;
				aStartIndex++;
			}
			break;
		case FOR:
			lastChild = new AnOpenMPForPragma(aLineIndex);
			retVal.add (lastChild);
			break;
		case CRITICAL:
			lastChild = new AnOpenMPCriticalPragma(aLineIndex);
			retVal.add (lastChild);
			break;
			default: 
				lastChild = new AnOpenMPPragma(aLineIndex);
				retVal.add(lastChild);
			
		}
//		retVal.setFirstOpenMPKeyword(stringToOpenMPKeyword.get(aFirstToken));	
//		OpenMPPragma retVal = new AnOpenMPPragma(aLineIndex);


		for (int i = aStartIndex; i < aTokens.length; i ++) {
			String aStoredToken = aTokens[i].trim();
			if (aStoredToken.isEmpty()) {
				continue;
			}
			if (aStoredToken.startsWith("reduction") || aStoredToken.startsWith("shared") || aStoredToken.startsWith("private")) {
				//combine all tokens until ")" into one for normalization
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
				if (aStoredToken.startsWith("reduction")) {
					setReductionData(lastChild, aStoredToken, aLeftParenIndex, aRightParenIndex);
				} else if (aStoredToken.startsWith("private")) {
					setSharedOrPrivateData(lastChild, aStoredToken, aLeftParenIndex, aRightParenIndex, false);

				} else if (aStoredToken.startsWith("shared")) {
					setSharedOrPrivateData(lastChild, aStoredToken, aLeftParenIndex, aRightParenIndex, true);

				}
//				int aColonIndex = aStoredToken.indexOf(":");
//				if (aLeftParenIndex != -1 && aRightParenIndex != -1 && aColonIndex != -1) {
//					String anOperationString = aStoredToken.substring(aLeftParenIndex + 1, aColonIndex).trim();
//					String aVariableString = aStoredToken.substring(aColonIndex + 1, aRightParenIndex).trim();
//					((OpenMPForPragma) lastChild).setReductionVariable(aVariableString);
//					((OpenMPForPragma) lastChild).setReductionOperation(anOperationString);
//				}
				
			}
			
//			aTokens[i] = aStoredToken;
			lastChild.getOpenMPTokens().add(aStoredToken);
		}
//		String aFirstToken = retVal.getOpenMPTokens().get(0);
//		retVal.setFirstOpenMPKeyword(stringToOpenMPKeyword.get(aFirstToken));		
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
