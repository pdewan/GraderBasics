package gradingTools.shared.testcases.openmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import gradingTools.shared.testcases.openmp.scannedTree.ADeclarationSNode;
import gradingTools.shared.testcases.openmp.scannedTree.ADeclaringAssignmentSNode;
import gradingTools.shared.testcases.openmp.scannedTree.AForSNode;
import gradingTools.shared.testcases.openmp.scannedTree.ATextSNode;
import gradingTools.shared.testcases.openmp.scannedTree.AnAssignmentSNode;
import gradingTools.shared.testcases.openmp.scannedTree.AnOMPCriticalSNode;
import gradingTools.shared.testcases.openmp.scannedTree.AnOMPForSNode;
import gradingTools.shared.testcases.openmp.scannedTree.AnOMPParallelSNode;
import gradingTools.shared.testcases.openmp.scannedTree.AnOMPSNode;
import gradingTools.shared.testcases.openmp.scannedTree.AnSNode;
import gradingTools.shared.testcases.openmp.scannedTree.AssignmentSNode;
import gradingTools.shared.testcases.openmp.scannedTree.DeclarationSNode;
import gradingTools.shared.testcases.openmp.scannedTree.DeclaringAssignmentSNode;
import gradingTools.shared.testcases.openmp.scannedTree.ForSNode;
import gradingTools.shared.testcases.openmp.scannedTree.OMPForSNode;
import gradingTools.shared.testcases.openmp.scannedTree.OMPSNode;
import gradingTools.shared.testcases.openmp.scannedTree.SNode;

public class OpenMPUtils {

	protected static Map<String, OpenMPKeywordEnum> stringToOpenMPKeyword = new HashMap();
	protected static String[] typeNames = { "double", "float", "int", "short", "long" };
	protected static String[] openMPCalls = { "omp_get_thread_num()", "omp_get_num_threads()" };

	public static List<OpenMPPragma> getOpemMPPragmas(StringBuffer aFileBuffer) {
		String[] aFileLines = aFileBuffer.toString().split("\n");
		return getOpenMPPragmas(aFileLines);
	}
	public static SNode getSNode(StringBuffer aFileBuffer) {
		String[] aFileLines = aFileBuffer.toString().split("\n");
		return getSNode(aFileLines);
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
		return aLine.startsWith("#pragma") && aLine.contains("omp");
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

	public static AssignmentSNode getAssignmentSNode(int aLineNumber, String aString) {
		String[] aTokens = aString.split("=");
		return new AnAssignmentSNode(aLineNumber, aTokens[0], aTokens[1]);
	}
	public static DeclarationSNode getDeclarationSNode(int aLineNumber, String aString) {
		String[] aTokens = aString.split(" ");
		return new ADeclarationSNode(aLineNumber, aTokens[0], aTokens[1]);
	}

	public static DeclaringAssignmentSNode getDeclaringAssignmentSNode(int aLineNumber, String aString) {
		String[] anLHSAndRHS = aString.split("=");
		String[] aTypeAndVariable = anLHSAndRHS[0].split(" ");
		return new ADeclaringAssignmentSNode(aLineNumber, aTypeAndVariable[0], aTypeAndVariable[1], anLHSAndRHS[1]);
	}

	public static boolean isDeclaringAssignment(String aFileLine) {
		return startsWithTypeName(aFileLine) && aFileLine.contains("=");
	}
	public static boolean isVariableDeclaration(String aFileLine) {
		
		return startsWithTypeName(aFileLine) && !aFileLine.contains("(") ;
	}
	
	public static boolean isAssignment(String aFileLine) {
		return aFileLine.contains("=");
	}


	public static boolean isForNode(String aFileLine) {
		return aFileLine.startsWith("for");
	}

	public static ForSNode getForSNode(int aLineNumber, String aFileLine) {

		int aLeftParenIndex = aFileLine.indexOf("(");
		int aRightParenIndex = aFileLine.indexOf(")");
		if (aLeftParenIndex < 0 || aRightParenIndex < 0) {
			return null;
		}
		String aHeaderString = aFileLine.substring(aLeftParenIndex + 1, aRightParenIndex);
		String[] aForComponents = aHeaderString.split(";");
		AssignmentSNode anAssignmentSNode = null;
		String aForInitializaton = aForComponents[0];
		if (!aForInitializaton.isEmpty() && aForInitializaton.contains("=")) {
			if (isDeclaringAssignment(aForInitializaton)) {
				anAssignmentSNode = getDeclaringAssignmentSNode(aLineNumber, aForInitializaton);
			} else {
				anAssignmentSNode = getAssignmentSNode(aLineNumber, aForInitializaton);
			}

		}
		return new AForSNode(aLineNumber, anAssignmentSNode, aForComponents[1], aForComponents[2]);
	}

	public static List<String> getOMPCalls(String aFileLine) {
		List<String> retVal = new ArrayList();
		for (String anOpenMPCall : openMPCalls) {
			if (aFileLine.contains(anOpenMPCall)) {
				retVal.add(anOpenMPCall);
			}
		}
		return retVal;
	}

	public static void incrementStackTop(Stack<Integer> aNumOpenBracesStack) {
		int aTopIndex = aNumOpenBracesStack.size() - 1;
		Integer aCurrentValue = aNumOpenBracesStack.get(aTopIndex);
		aNumOpenBracesStack.set(aTopIndex, aCurrentValue + 1);
	}

	public static void decrementStackTop(Stack<Integer> aNumOpenBracesStack) {
		int aTopIndex = aNumOpenBracesStack.size() - 1;
		Integer aCurrentValue = aNumOpenBracesStack.get(aTopIndex);
		aNumOpenBracesStack.set(aTopIndex, aCurrentValue - 1);
	}

	public static boolean startsWithTypeName(String aLine) {
		for (String aTypeName : typeNames) {
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
					for (OpenMPPragma anOpenMPPragma : newOpenMPPragmas) {
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

	public static void setReductionData(OpenMPPragma lastChild, String aStoredToken, int aLeftParenIndex,
			int aRightParenIndex) {
		int aColonIndex = aStoredToken.indexOf(":");
		if (aLeftParenIndex != -1 && aRightParenIndex != -1 && aColonIndex != -1) {
			String anOperationString = aStoredToken.substring(aLeftParenIndex + 1, aColonIndex).trim();
			String aVariableString = aStoredToken.substring(aColonIndex + 1, aRightParenIndex).trim();
			((OpenMPForPragma) lastChild).setReductionVariable(aVariableString);
			((OpenMPForPragma) lastChild).setReductionOperation(anOperationString);
		}
	}

	public static void setReductionData(OMPForSNode lastChild, String aStoredToken, int aLeftParenIndex,
			int aRightParenIndex) {
		int aColonIndex = aStoredToken.indexOf(":");
		if (aLeftParenIndex != -1 && aRightParenIndex != -1 && aColonIndex != -1) {
			String anOperationString = aStoredToken.substring(aLeftParenIndex + 1, aColonIndex).trim();
			String aVariableString = aStoredToken.substring(aColonIndex + 1, aRightParenIndex).trim();
			((OMPForSNode) lastChild).setReductionVariable(aVariableString);
			((OMPForSNode) lastChild).setReductionOperation(anOperationString);
		}
	}

	public static void setSharedOrPrivateData(OpenMPPragma lastChild, String aStoredToken, int aLeftParenIndex,
			int aRightParenIndex, boolean isShared) {
//		int aColonIndex = aStoredToken.indexOf(":");
		if (aLeftParenIndex != -1 && aRightParenIndex != -1) {
			String aVariableDeclarations = aStoredToken.substring(aLeftParenIndex + 1, aRightParenIndex);
			String[] aVariables = aVariableDeclarations.split(",");
			if (isShared) {
				lastChild.setSharedVariables(aVariables);
			} else {
				lastChild.setPrivateVariables(aVariables);
			}
		}
	}

	public static void setSharedOrPrivateData(OMPSNode lastChild, String aStoredToken, int aLeftParenIndex,
			int aRightParenIndex, boolean isShared) {
//		int aColonIndex = aStoredToken.indexOf(":");
		if (aLeftParenIndex != -1 && aRightParenIndex != -1) {
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
			retVal.add(lastChild);
			break;
		case CRITICAL:
			lastChild = new AnOpenMPCriticalPragma(aLineIndex);
			retVal.add(lastChild);
			break;
		default:
			lastChild = new AnOpenMPPragma(aLineIndex);
			retVal.add(lastChild);

		}
//		retVal.setFirstOpenMPKeyword(stringToOpenMPKeyword.get(aFirstToken));	
//		OpenMPPragma retVal = new AnOpenMPPragma(aLineIndex);

		for (int i = aStartIndex; i < aTokens.length; i++) {
			String aStoredToken = aTokens[i].trim();
			if (aStoredToken.isEmpty()) {
				continue;
			}
			if (aStoredToken.startsWith("reduction") || aStoredToken.startsWith("shared")
					|| aStoredToken.startsWith("private")) {
				// combine all tokens until ")" into one for normalization
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

	public static SNode getSNode(String[] aFileLines) {
		Stack<SNode> anSNodes = new Stack();
//		Stack<Integer> aNumOpenBracesStack = new Stack();
//		OpenMPPragma lastOpenMPPragma = null;
//		String lastReductionVariable = null;
//		String lastReductionOperation = null;
//		int aNumOpenBraces = 0;
//		boolean aNextCodeLineIsAPragmaBlock = false;
//		List<OpenMPPragma> newOpenMPPragmas = null;
		SNode retVal = new AnSNode(0);
		anSNodes.add(retVal);
		SNode previousHeaderNode = null;
		for (int i = 0; i < aFileLines.length; i++) {
			String aFileLine = aFileLines[i].trim();
			if (!isCodeLine(aFileLine)) {
				continue;
			}
			if (isForNode(aFileLine)) {
				ForSNode aForSNode = getForSNode(i, aFileLine);
				aForSNode.setParent(anSNodes.peek());
//				anSNodes.push(aForSNode);
				previousHeaderNode = aForSNode;
				if (aFileLine.endsWith(")")) {
				continue;
				}
			}
			if (isPragmaStart(aFileLine)) {
				OMPSNode anOMPSNode = getOpenMPSNode(i, anSNodes.peek(), aFileLine);
//				anSNodes.push(anOMPSNode);
				previousHeaderNode = anOMPSNode;
				continue;
			}
			if (isBlockStart(aFileLine)) {
				SNode aBlockSNode = new AnSNode(i);
				if (previousHeaderNode != null) {
					aBlockSNode.setParent(previousHeaderNode);
				} else {
					aBlockSNode.setParent(anSNodes.peek());
				}
				anSNodes.push(aBlockSNode);
				previousHeaderNode = null;
				continue;
			}
//			} else 
			if (isBlockEnd(aFileLine)) {
				anSNodes.pop();
				continue;
			}
			SNode aNewLeafNode = null;
			if (isDeclaringAssignment(aFileLine)) {
				aNewLeafNode = getDeclaringAssignmentSNode(i, aFileLine);

			} else if (isAssignment(aFileLine)) {
				aNewLeafNode = getAssignmentSNode(i, aFileLine);
			} else if (isVariableDeclaration(aFileLine)) {
				aNewLeafNode = getDeclarationSNode(i, aFileLine);
			} else {
				aNewLeafNode = new ATextSNode(i, aFileLine);
			}
			if (previousHeaderNode != null) {
				aNewLeafNode.setParent(previousHeaderNode);
			} else {
				aNewLeafNode.setParent(anSNodes.peek());
			}

		}

		return retVal;
	}

	public static OMPSNode getOpenMPSNode(int aLineIndex, SNode aParentNode, String aFileLine) {
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
//		List<OpenMPPragma> retVal = new ArrayList();
		OMPSNode lastChild = null;
		int aStartIndex = 2;
		OMPSNode aNewNode = null;
		OMPForSNode aForChild = null;
		switch (anOpenMPKeyword) {
		case PARALLEL:
			aNewNode = new AnOMPParallelSNode(aLineIndex);
//			if (lastChild != null) {
//				aNewNode.setParent(lastChild);
//			} else {
//				aNewNode.setParent(aParentNode);
//			}
//			lastChild = aNewNode;

//			lastChild.setFirstOpenMPKeyword(stringToOpenMPKeyword.get(aFirstToken));	

//			retVal.add(lastChild);
			if (aTokens.length > 3 && aTokens[3].equals("for")) {
				aForChild = new AnOMPForSNode(aLineIndex);
				aForChild.setParent(aNewNode);
//				retVal.add(aForChild);
//				lastChild = aForChild;
				aStartIndex++;
			}
			break;
		case FOR:
			aNewNode = new AnOMPForSNode(aLineIndex);
			;
			break;
		case CRITICAL:
			aNewNode = new AnOMPCriticalSNode(aLineIndex);
//			retVal.add (lastChild);
			break;
		default:
			aNewNode = new AnOMPSNode(aLineIndex);

		}
//		retVal.setFirstOpenMPKeyword(stringToOpenMPKeyword.get(aFirstToken));	
//		OpenMPPragma retVal = new AnOpenMPPragma(aLineIndex);
		if (lastChild != null) {
			aNewNode.setParent(lastChild);
		} else {
			aNewNode.setParent(aParentNode);
		}
		lastChild = aForChild == null ? aNewNode : aForChild;

		for (int i = aStartIndex; i < aTokens.length; i++) {
			String aStoredToken = aTokens[i].trim();
			if (aStoredToken.isEmpty()) {
				continue;
			}
			if (aStoredToken.startsWith("reduction") || aStoredToken.startsWith("shared")
					|| aStoredToken.startsWith("private")) {
				// combine all tokens until ")" into one for normalization
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
					setReductionData((OMPForSNode) lastChild, aStoredToken, aLeftParenIndex, aRightParenIndex);
				} else if (aStoredToken.startsWith("private")) {
					setSharedOrPrivateData((OMPSNode) lastChild, aStoredToken, aLeftParenIndex, aRightParenIndex,
							false);

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
		return lastChild;

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
