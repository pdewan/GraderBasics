package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import grader.basics.project.source.ABasicTextManager;
import gradingTools.shared.testcases.openmp.OpenMPKeywordEnum;
import gradingTools.shared.testcases.openmp.OpenMPParallelPragma;
import gradingTools.shared.testcases.openmp.OpenMPUtils;
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

public class OMPSNodeUtils extends OpenMPUtils {

	public static RootOfFileSNode getSNode(String aFileName, StringBuffer aFileBuffer) {
		String[] aFileLines = aFileBuffer.toString().split("\n");
		return getSNode(aFileName, aFileLines);
	}

	public static AssignmentSNode getAssignmentSNode(int aLineNumber, String aString) {
		if (aString == null) {
			return null;
		}
		String[] aTokens = aString.split("=");
		if (aTokens.length == 1) {
			return new AnAssignmentSNode(aLineNumber, aTokens[0], null);
		} else {
		return new AnAssignmentSNode(aLineNumber, aTokens[0], aTokens[1]);
		}
	}

	public static DeclarationSNode[] getDeclarationSNode(int aLineNumber, String aString) {
		String[] aTokens = aString.split(" ");
		DeclarationSNode[] retVal = new DeclarationSNode[aTokens.length - 1];
		for (int i = 1; i < aTokens.length; i++) {
			retVal[i-1] = new ADeclarationSNode(aLineNumber, aTokens[0], aTokens[i]);
		}
		return retVal;
		
		//return new ADeclarationSNode(aLineNumber, aTokens[0], aTokens[1]);
	}

	public static DeclaringAssignmentSNode getDeclaringAssignmentSNode(int aLineNumber, String aString) {
		String[] anLHSAndRHS = aString.split("=");
		String[] aTypeAndVariable = anLHSAndRHS[0].split(" ");
//		return new ADeclaringAssignmentSNode(aLineNumber, aTypeAndVariable[0], aTypeAndVariable[1], anLHSAndRHS[1]);
		// may have a * at the start which we are ignoring
		return new ADeclaringAssignmentSNode(aLineNumber, aTypeAndVariable[aTypeAndVariable.length - 2], aTypeAndVariable[aTypeAndVariable.length - 1], anLHSAndRHS[1]);

	}
	public static ConstDeclarationSNode getConstDeclarationSNode(int aLineNumber, String aString) {
		String anAssignmentString = aString.substring(CONST.length()).trim();
		String[] anLHSAndRHS = anAssignmentString.split("=");
		String[] aTypeAndVariable = anLHSAndRHS[0].split(" ");
		return new AConstDeclarationSNode(aLineNumber, aTypeAndVariable[0], aTypeAndVariable[1], anLHSAndRHS[1]);
	}

	public static boolean isDeclaringAssignment(String aFileLine) {
		return (startsWithTypeName(aFileLine) ||startsWithPointerAndTypeName(aFileLine))
				
				&& aFileLine.contains("=");
	}
	public static boolean isConstDeclaration(String aFileLine) {
		return aFileLine.startsWith(CONST);
	}
	public static boolean isMethodDeclaration(String aFileLine) {
		return startsWithTypeName(aFileLine) && 
				aFileLine.contains("(") &&
				!aFileLine.contains("=");
	}
	public static boolean isExternalMethodDeclaration(String aFileLine) {
		return isMethodDeclaration(aFileLine) && aFileLine.endsWith(";");
	}


	public static boolean isVariableDeclaration(String aFileLine) {

		return startsWithTypeName(aFileLine) && !aFileLine.contains("(");
	}

	public static boolean isAssignment(String aFileLine) {
		return aFileLine.contains("=") && !aFileLine.contains("==");
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
		return new AForSNode(aLineNumber, anAssignmentSNode, aForComponents[1], getAssignmentSNode(aLineNumber, aForComponents[2]));
	}
	static String[] emptyArray = {};
	public static MethodSNode getMethodSNode(int aLineNumber, String aFileLine, boolean isInternal) {

		int aLeftParenIndex = aFileLine.indexOf("(");
		int aRightParenIndex = aFileLine.indexOf(")");
		if (aLeftParenIndex < 0 || aRightParenIndex < 0) {
			return null;
		}
		String aMethodNameAndType = aFileLine.substring(0, aLeftParenIndex);
		String[] aMethodNameAndTypeTokens = aMethodNameAndType.split("\\s+"); 
		
		String aMethodParameters = aFileLine.substring(aLeftParenIndex +1 , aRightParenIndex);
		String[] aMethodParameterTokens = aMethodParameters.isEmpty()?emptyArray:
		 aMethodParameters.split(",");
		
//		String[] aMethodParameterTypes = new String[aMethodParameterTokens.length];
//		String[] aMethodParameterNames = new String[aMethodParameterTokens.length];
		List<DeclarationSNode> aDeclarationSNodeList = new ArrayList();
		for (int i = 0; i < aMethodParameterTokens.length; i++ ) {
			String[] aTypeAndName = aMethodParameterTokens[i].trim().split("\\s+");
			if (aTypeAndName.length < 2) {
				continue;
			}
			aDeclarationSNodeList.add(new ADeclarationSNode(aLineNumber, aTypeAndName[0].trim(), aTypeAndName[1].trim()));
//			aMethodParameterTypes[i] = aTypeAndName[0].trim();
//			aMethodParameterNames[i] = aTypeAndName[1].trim();
		}
		if (isInternal)
//		return new AMethodSNode(aLineNumber, aMethodNameAndTypeTokens[0], aMethodNameAndTypeTokens[1], aMethodParameterTypes,aMethodParameterNames );
			return new AMethodSNode(aLineNumber, aMethodNameAndTypeTokens[0], aMethodNameAndTypeTokens[1], aDeclarationSNodeList );
		else
			return new AnExternalMethodSNode(aLineNumber, aMethodNameAndTypeTokens[0], aMethodNameAndTypeTokens[1], aDeclarationSNodeList );
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

	public static RootOfFileSNode getSNode(String aFileName, String[] aFileLines) {
		Stack<SNode> anSNodes = new Stack();

		RootOfFileSNode retVal = new ARootOfFileSNode(aFileName);
		anSNodes.add(retVal);
		SNode previousHeaderNode = null;
		for (int i = 0; i < aFileLines.length; i++) {
			String aFileLine = aFileLines[i].trim();
			if (!isCodeLine(aFileLine)) {
				continue;
			}
			if (isForNode(aFileLine)) {
				ForSNode aForSNode = getForSNode(i, aFileLine);
				SNode aParent = previousHeaderNode != null?previousHeaderNode:anSNodes.peek();
				aForSNode.setParent(anSNodes.peek());
				aForSNode.setParent(aParent);

//				anSNodes.push(aForSNode);
				previousHeaderNode = aForSNode;
				if (aFileLine.endsWith(")")) {
					continue;
				}
			}
			if (isExternalMethodDeclaration(aFileLine)) {
				MethodSNode aMethodSNode = getMethodSNode(i, aFileLine, false);
				aMethodSNode.setParent(anSNodes.peek());
//				anSNodes.push(aForSNode);
				previousHeaderNode = aMethodSNode;
				if (aFileLine.endsWith(")")) {
					continue;
				}
			}
			if (isMethodDeclaration(aFileLine)) {
				MethodSNode aMethodSNode = getMethodSNode(i, aFileLine, true);
				aMethodSNode.setParent(anSNodes.peek());
//				anSNodes.push(aForSNode);
				previousHeaderNode = aMethodSNode;
				if (aFileLine.endsWith(")")) {
					continue;
				}
			}
			if (isPragmaStart(aFileLine)) {
				OMPSNode anOMPSNode = getOMPSNode(i, anSNodes.peek(), aFileLine);
//				anSNodes.push(anOMPSNode);
				previousHeaderNode = anOMPSNode;
				continue;
			}
			if (isBlockStart(aFileLine)) {
				SNode aBlockSNode = new ABlockSNode(i);
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
			if (isConstDeclaration(aFileLine)) {
				aNewLeafNode = getConstDeclarationSNode(i, aFileLine);

			} else if (isDeclaringAssignment(aFileLine)) {
				aNewLeafNode = getDeclaringAssignmentSNode(i, aFileLine);

			} else if (isAssignment(aFileLine)) {
				aNewLeafNode = getAssignmentSNode(i, aFileLine);
			} else if (isVariableDeclaration(aFileLine)) {
				DeclarationSNode[] aDeclarations = getDeclarationSNode(i, aFileLine);
				for (DeclarationSNode aNode:aDeclarations) {
					if (previousHeaderNode != null) {
						aNode.setParent(previousHeaderNode);
					} else {
						aNode.setParent(anSNodes.peek());
					}
				}
				continue;
			} else {
				List<MethodCall> aCalls = callsIn(i, aFileLine, null); // parent will be assigned below
				if (aCalls != null && aCalls.size() == 1) {
					aNewLeafNode = aCalls.get(0);
				} else {
					aNewLeafNode = new ATextSNode(i, aFileLine);
				}
			}
//			else {
//				aNewLeafNode = new ATextSNode(i, aFileLine);
//			}
			if (previousHeaderNode != null) {
				aNewLeafNode.setParent(previousHeaderNode);
			} else {
				aNewLeafNode.setParent(anSNodes.peek());
			}

		}

		return retVal;
	}

	public static OMPSNode getOMPSNode(int aLineIndex, SNode aParentNode, String aFileLine) {
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

			if (aTokens.length > 3 && aTokens[3].equals("for")) {
				aForChild = new AnOMPForSNode(aLineIndex);
				aForChild.setParent(aNewNode);

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

			}

			lastChild.getOpenMPTokens().add(aStoredToken);
		}

		return lastChild;

	}

	public static boolean hasParallelAncestor(SNode anSNode) {
		if (anSNode == null) {
			return false;
		}
		if (anSNode instanceof OMPParallelSNode) {
			return true;
		}
		return hasParallelAncestor(anSNode.getParent());
	}
	public static boolean hasCriticalAncestor(SNode anSNode) {
		if (anSNode == null) {
			return false;
		}
		if (anSNode instanceof OMPCriticalSNode) {
			return true;
		}
		return hasCriticalAncestor(anSNode.getParent());
	}


	public static Boolean isDeclaredShared(OMPSNode anOMPSNode, String anLHS) {
		if (Arrays.asList(anOMPSNode.getSharedVariables()).contains(anLHS)) {
			return true;
		}
		if (Arrays.asList(anOMPSNode.getPrivateVariables()).contains(anLHS)) {
			return false;
		}
		return null;

	}

	public static boolean isSharedVariable(SNode anSNode, String anLHS) {
		if (anSNode == null) {
			return false;
		}
		if (anSNode.getLocalVariableIdentifiers().contains(anLHS)) {
			if (!(anSNode instanceof OMPSNode)) { // no shared and private declarations
				return !hasParallelAncestor(anSNode); // shared if not inParallel
			}
			OMPSNode anOMPSNode = (OMPSNode) anSNode;
			return (Arrays.asList(anOMPSNode.getSharedVariables()).contains(anLHS)); // private unless declared shared
		}
		if (anSNode instanceof OMPSNode) {
			OMPSNode anOMPSNode = (OMPSNode) anSNode;
			// the variable is global to this scope, check if it has have been made shared
			// or private by this scope
			Boolean isDeclaredShared = isDeclaredShared(anOMPSNode, anLHS);
			if (isDeclaredShared != null) {
				return isDeclaredShared;
			}
		}
		// check the parent
		return isSharedVariable(anSNode.getParent(), anLHS);
	}

	public static List<SNode> getAllLeafNodes(SNode aTopNode) {
		List<SNode> retVal = new ArrayList();
		addLeafNodes(aTopNode, retVal);
		return retVal;
	}

	public static void addLeafNodes(SNode aTopNode, List<SNode> retVal) {
		for (SNode aChild : aTopNode.getChildren()) {
			if (aChild.isLeaf()) {
				retVal.add(aChild);
			} else {
				addLeafNodes(aChild, retVal);
			}
		}
	}

	public static List<SNode> getNonParallelLeafNodes(SNode aTopNode) {
		List<SNode> retVal = new ArrayList();
		addNonParallelLeafNodes(aTopNode, retVal);
		return retVal;
	}

	public static void addNonParallelLeafNodes(SNode aTopNode, List<SNode> retVal) {
		if (aTopNode.isInParallel()) {
			return;
		}
		for (SNode aChild : aTopNode.getChildren()) {
			if (aChild.isLeaf()) {
				retVal.add(aChild);
			} else {
				addLeafNodes(aChild, retVal);
			}
		}
	}
	static String callRegex = "([a-zA-Z_$][a-zA-Z_$0-9]*)\\(.*?\\)";
	static Pattern callPattern = Pattern.compile(callRegex);

	static String identifierRegex = "[a-zA-Z_$][a-zA-Z_$0-9]*";
	static Pattern identifierPattern = Pattern.compile(identifierRegex);
	public static List<String> identifiersIn(String aString) {
		if (aString == null)
			return null;
		boolean hasStar = false;
		if (aString.startsWith("*")) {
			aString = aString.substring(1).trim(); //we have no memory of it being a pointer
			
		}
//		Pattern mypattern = Pattern.compile("[a-zA-Z_$][a-zA-Z_$0-9]*");
//		Matcher mymatcher = mypattern.matcher(aString);
		Matcher mymatcher = identifierPattern.matcher(aString);
		List<String> retVal = new ArrayList();
		while (mymatcher.find()) {
			String find = mymatcher.group(0);
			retVal.add(find);
		}
		return retVal;
	}
	public static List<MethodCall> callsIn(int aLineNumber, String aString, SNode aParent) {
		if (aString == null)
			return emptyList;
		if (isMethodDeclaration(aString)) {
			return emptyList;
		}
//		Pattern mypattern = Pattern.compile("[a-zA-Z_$][a-zA-Z_$0-9]*");
//		Matcher mymatcher = mypattern.matcher(aString);
		Matcher mymatcher = callPattern.matcher(aString);
		List<MethodCall> retVal = new ArrayList();
		while (mymatcher.find()) {
			String find = mymatcher.group(0);
			int aLeftParenIndex = find.indexOf("(");
			int aRightParenIndex = find.indexOf(")");
			String aMethodName = find.substring(0, aLeftParenIndex).trim();
			String aParameters = find.substring(aLeftParenIndex + 1, aRightParenIndex);
			List<String> aParameterList = new ArrayList();
			String[] aParameterTokens = aParameters.split(",");
			for (String aParameter:aParameterTokens) {
				String aParameterTrimmed = aParameter.trim();
				if (!aParameterTrimmed.isEmpty())
				aParameterList.add(aParameterTrimmed);
			}
			retVal.add(new AMethodCall(aLineNumber, aMethodName, aParameterList, aParent));

//			aCallStrings.add(find);
		}
		return retVal;
	}
	static String numberRegex = "(\\d+\\.\\d+)|(\\d+)";
	static Pattern numberPattern = Pattern.compile(numberRegex);
	public static List<String> numbersIn(String aString) {
		if (aString == null)
			return null;
//		String regex = "(\\d+\\.\\d+)|(\\d+)";
//
//		Matcher m = Pattern.compile(regex).matcher(aString);
		Matcher m =numberPattern.matcher(aString);


		List<String> retVal = new ArrayList();

		while (m.find()) {
			retVal.add(m.group());
		}
		return retVal;
	}

	public static String subscriptIn(String aString) {
		if (aString == null)
			return null;
		return StringUtils.substringBetween(aString, "[", "]");
	}
	static String[] emptyStringArray = {};
	static List emptyList = new ArrayList();

	public static String[] subscriptsIn(String aString) {
		if (aString == null)
			return emptyStringArray;
		return StringUtils.substringsBetween(aString, "[", "]");
	}
	static  String operatorRegex = "([+-/*///^])|([/(/)])";
	static Pattern operatorPattern = Pattern.compile(operatorRegex);

	public static List<String> operatorsIn(String aString) {
		if (aString == null)
			return null;
//		String operatorRegex = "([+-/*///^])|([/(/)])";
//
//		Matcher m = Pattern.compile(operatorRegex).matcher(aString);
		
		Matcher m = operatorPattern.matcher(aString);


		List<String> retVal = new ArrayList();

		while (m.find()) {
			retVal.add(m.group());
		}
		return retVal;
	}
	
	public static int numberOfNestingFors (SNode anSNode) {
		if (anSNode == null) {
			return 0;
		}
		int retVal = anSNode.getNumberOfNestingFors();
		if ( retVal >= 0) {
			return retVal;
		}
		retVal = numberOfNestingFors (anSNode.getParent()) ;

		if (anSNode instanceof ForSNode) {
			retVal++;
		} else if (anSNode instanceof MethodSNode) {
			List<MethodCall> aMethodCalls = ((MethodSNode) anSNode).getCalls();
			int aMaxNestingLevel = 0;
			for (MethodCall aMethodCall:aMethodCalls) {
				MethodSNode aCallerMethodSNode = getDeclarationOfCalledMethod(anSNode, aMethodCall);
				int aCallerNumberOfNestingFors = numberOfNestingFors(aCallerMethodSNode);
				aMaxNestingLevel = Math.max(aCallerNumberOfNestingFors, aMaxNestingLevel);				
			}
			return retVal + aMaxNestingLevel;
		}
		return retVal;
	}
	public static boolean hasOperator (SNode anSNode, String anOperator) {
		boolean retVal = false;
		if (anSNode instanceof AssignmentSNode) {
			AssignmentSNode anAssignmentSNode = (AssignmentSNode) anSNode;
			 retVal = (anAssignmentSNode.getLhsOperators().contains(anOperator) || 
					anAssignmentSNode.getRhsOperators().contains(anOperator)) ;
			if (retVal) {
				return retVal;
			} else {
				List<MethodCall> aMethodCalls = anAssignmentSNode.getRhsCalls();
				for (MethodCall aMethodCall:aMethodCalls) {
					MethodSNode aMethodSNode = getDeclarationOfCalledMethod(anSNode, aMethodCall);
					retVal = hasOperator(aMethodSNode, anOperator);
					if (retVal) {
						return retVal;
					}
					
				}
				return false;
			}
			
		}
		for (SNode aChild:anSNode.getChildren()) {
			retVal = hasOperator(aChild, anOperator);
			if (retVal) {
				return retVal;
			}
		}
		return false;
	}
	public static boolean hasMethodCall (SNode anSNode, String anOperator) {
		boolean retVal = false;
		if (anSNode instanceof AssignmentSNode) {
			AssignmentSNode anAssignmentSNode = (AssignmentSNode) anSNode;
			 retVal = (anAssignmentSNode.getLhsOperators().contains(anOperator) || 
					anAssignmentSNode.getRhsOperators().contains(anOperator)) ;
			if (retVal) {
				return retVal;
			} else {
				List<MethodCall> aMethodCalls = anAssignmentSNode.getRhsCalls();
				for (MethodCall aMethodCall:aMethodCalls) {
					MethodSNode aMethodSNode = getDeclarationOfCalledMethod(anSNode, aMethodCall);
					retVal = hasOperator(aMethodSNode, anOperator);
					if (retVal) {
						return retVal;
					}
					
				}
				return false;
			}
			
		}
		for (SNode aChild:anSNode.getChildren()) {
			retVal = hasOperator(aChild, anOperator);
			if (retVal) {
				return retVal;
			}
		}
		return false;
	}
	public static boolean dependsOn (AssignmentSNode anAssignmentSNode, String aVariable, String aCallIdentifier) {
		// This assignment does not change aVariable
		if (!aVariable.equals(anAssignmentSNode.getLhsFirstIdentifier())) {
			return false;
		}
		return dependsOn(anAssignmentSNode.getExpressionSNode(), aCallIdentifier);
//		List<String> aCallIdentifiers = anAssignmentSNode.getRhsCallIdentifiers();
//		boolean retVal = false;
//		if (aCallIdentifiers != null && aCallIdentifiers.contains(aCallIdentifier)) {
//			return true; // this assignment has aCallIdentifier in the rhs
//		}
//		// check if some referenced rhs variable depends on aCallIdentifier
//		List<String> aReferencedVariableIdentifiers = anAssignmentSNode.getRhsVariableIdentifiers();
//		SNode anAssignmentParent = anAssignmentSNode.getParent();
//		if (anAssignmentParent == null) { // should never be trye
//			return false;
//		}
//		int aLineNumber = anAssignmentSNode.getLineNumber();
//		for (String aReferencedVariableIdentifier:aReferencedVariableIdentifiers ) {
//			if (dependsOn (anAssignmentParent, aLineNumber, aReferencedVariableIdentifier, aCallIdentifier)) {
//				return true;
//			}
//		}
//		return false;
	}
	public static boolean dependsOn (ExpressionSNode anExpressionSNode, String aCallIdentifier) {
		// This assignment does not change aVariable
		
		List<String> aCallIdentifiers = anExpressionSNode.getRhsCallIdentifiers();
		boolean retVal = false;
		if (aCallIdentifiers != null && aCallIdentifiers.contains(aCallIdentifier)) {
			return true; // this assignment has aCallIdentifier in the rhs
		}
		// check if some referenced rhs variable depends on aCallIdentifier
		List<String> aReferencedVariableIdentifiers = anExpressionSNode.getRhsVariableIdentifiers();
		SNode anAssignmentParent = anExpressionSNode.getParent().getParent();
		if (anAssignmentParent == null) { // should never be trye
			return false;
		}
		int aLineNumber = anExpressionSNode.getLineNumber();
		for (String aReferencedVariableIdentifier:aReferencedVariableIdentifiers ) {
			if (dependsOn (anAssignmentParent, aLineNumber, aReferencedVariableIdentifier, aCallIdentifier)) {
				return true;
			}
		}
		return false;
	}
	public static Set<AssignmentSNode> assignmentsToSharedVariables(SNode anSNode) {
		Set<AssignmentSNode> retVal =  new HashSet();
		fillAssignmentsToShared(anSNode, retVal);
		return retVal;
		
	}
	public static Set<AssignmentSNode> assignmentsToSharedArrays(SNode anSNode) {
		Set<AssignmentSNode> anAllSharedAssignments =  assignmentsToSharedVariables(anSNode);
		Set<AssignmentSNode> aRetVal = new HashSet();
		for (AssignmentSNode anAssignmentSNode:anAllSharedAssignments) {
			if (anAssignmentSNode.getLhsSubscripts().length > 0) {
				aRetVal.add(anAssignmentSNode);
			}
		}
		return aRetVal;
		
	}
	public static Set<AssignmentSNode> assignmentsToParallelCriticalSharedVariables(SNode anSNode) {
		Set<AssignmentSNode> anAllSharedAssignments =  assignmentsToSharedVariables(anSNode);
		Set<AssignmentSNode> aRetVal = new HashSet();
		for (AssignmentSNode anAssignmentSNode:anAllSharedAssignments) {
			if (anAssignmentSNode.isInParallel() &&
					anAssignmentSNode.isInCritical()) {
				aRetVal.add(anAssignmentSNode);
			}
		}
		return aRetVal;
		
	}
	public static Set<AssignmentSNode> assignmentsToNonParallelCriticalSharedVariables(SNode anSNode) {
		Set<AssignmentSNode> anAllSharedAssignments =  assignmentsToSharedVariables(anSNode);
		Set<AssignmentSNode> aRetVal = new HashSet();
		for (AssignmentSNode anAssignmentSNode:anAllSharedAssignments) {
			if (!anAssignmentSNode.isInParallel() &&
					anAssignmentSNode.isInCritical()) {
				aRetVal.add(anAssignmentSNode);
			}
		}
		return aRetVal;
		
	}
	public static Set<AssignmentSNode> assignmentsToParallelNonCriticalSharedVariables(SNode anSNode) {
		Set<AssignmentSNode> anAllSharedAssignments =  assignmentsToSharedVariables(anSNode);
		Set<AssignmentSNode> aRetVal = new HashSet();
		for (AssignmentSNode anAssignmentSNode:anAllSharedAssignments) {
			if (anAssignmentSNode.isInParallel() &&
					!anAssignmentSNode.isInCritical()) {
				aRetVal.add(anAssignmentSNode);
			}
		}
		return aRetVal;
		
	}
    public static void fillAssignmentsToShared(SNode anSNode, Set<AssignmentSNode> retVal) {
    	if (anSNode instanceof AssignmentSNode) {
			AssignmentSNode anAssignmentSNode = (AssignmentSNode) anSNode;
			String anLHS = anAssignmentSNode.getLhsFirstIdentifier();
			if (isSharedVariable(anSNode, anLHS)) {
				retVal.add(anAssignmentSNode);
				return;
			}
		}
    	for (SNode aChild:anSNode.getChildren()) {
    		fillAssignmentsToShared(aChild, retVal);
    	}
		
	}
    public static void fillInstancesOfNodeType(Class<? extends SNode> aNodeType, SNode anSNode, Set<SNode> retVal) {
    	if (aNodeType.isAssignableFrom(anSNode.getClass())) {
			
				retVal.add(anSNode);
				return;
			}
		
    	for (SNode aChild:anSNode.getChildren()) {
    		fillInstancesOfNodeType(aNodeType, aChild, retVal);
    	}
		
	}
    public static Set<AssignmentSNode>  assignmentSNodes(SNode anSNode) {
    	Set retVal = new HashSet();    	
    	fillInstancesOfNodeType(AssignmentSNode.class, anSNode, retVal );
    	return retVal;
	}
   
    
    public static Set<OMPParallelSNode>  ompParallelSNodes(SNode anSNode) {
    	Set retVal = new HashSet();    	
    	fillInstancesOfNodeType(OMPParallelSNode.class, anSNode, retVal );
    	return retVal;
	}
//    public static void fillOMPParallelSNodes(SNode anSNode, Set<OMPParallelSNode> retVal) {
//    	if (anSNode instanceof OMPParallelSNode) {
//    		retVal.add((OMPParallelSNode) anSNode);
//		}
//    	for (SNode aChild:anSNode.getChildren()) {
//    		fillOMPParallelSNodes(aChild, retVal);
//    	}		
//	}
    public static Set<ForSNode>  forSNodes(SNode anSNode) {
    	Set retVal = new HashSet();    	
    	fillInstancesOfNodeType(ForSNode.class, anSNode, retVal );
    	return retVal;
	}
//    public static void fillForSNodes(SNode anSNode, Set<ForSNode> retVal) {
//    	if (anSNode instanceof ForSNode) {
//    		retVal.add((ForSNode) anSNode);
//		}
//    	for (SNode aChild:anSNode.getChildren()) {
//    		fillForSNodes(aChild, retVal);
//    	}		
//	}
    public static Set<OMPForSNode>  ompForSNodes(SNode anSNode) {
    	Set retVal = new HashSet();    	
    	fillInstancesOfNodeType(OMPForSNode.class, anSNode, retVal );
    	return retVal;
	}
//    public static void fillOMPForSNodes(SNode anSNode, Set<OMPForSNode> retVal) {
//    	if (anSNode instanceof OMPForSNode) {
//    		retVal.add((OMPForSNode) anSNode);
//		}
//    	for (SNode aChild:anSNode.getChildren()) {
//    		fillOMPForSNodes(aChild, retVal);
//    	}		
//	}
    public static Set<OMPForSNode> ompReducingForNodes(SNode anSNode) {
		Set<OMPForSNode> anAllOMPForSNodes =  ompForSNodes(anSNode);
		Set<OMPForSNode> aRetVal = new HashSet();
		for (OMPForSNode anOMPForSNode:anAllOMPForSNodes) {
			if (anOMPForSNode.getReductionOperation() != null) {
				aRetVal.add(anOMPForSNode);
			}
		}
		return aRetVal;		
	}
    public static Set<AssignmentSNode> assignmentsToOMPReducingForNode(OMPForSNode anOMPForSNode) {
    	String aReductionVariable = anOMPForSNode.getReductionVariable();
    	return directAssignmentsOfVariableAndItsAliases(anOMPForSNode, aReductionVariable)	;
    	
    }
	public static boolean dependsOn (SNode anSNode, int aVariableLineNumber, String aVariable, String aCallIdentifier) {
		List<SNode> aListSNodes = anSNode.getChildren();
		boolean retVal = false;
		/*
		 * Should probably ignore line number as it assumes straight line code
		 */
		for (int i = aVariableLineNumber; i >= 0; i--) {
			SNode anSNodeChild = aListSNodes.get(i);
			if (anSNodeChild instanceof AssignmentSNode) {
				// does this statement in anSNode directly invoke aCallIdentifier 
				if (dependsOn((AssignmentSNode) anSNodeChild, aVariable, aCallIdentifier))
					return true;
			} else if (!anSNode.isLeaf()) {
					// child is overriding the variable so forget checking its assignment statements
					if (anSNodeChild.getLocalVariableIdentifiers().contains(aVariable)) {
						continue;
					}
					// some subblock of anSNode that has access to aVariable changes variable

					if (dependsOn(anSNodeChild, anSNodeChild.getChildren().size() - 1, aVariable, aCallIdentifier)) {
						return true;
					}
			}
			
		}
		// none of the statements in anSNode or its descendents  have the required call
		// is the variable a  method parameter 
		if (anSNode instanceof MethodSNode) {
			MethodSNode aMethodSNode = (MethodSNode) anSNode;
			
			int aParameterNumber = aMethodSNode.getLocalVariableIdentifiers().indexOf(aVariable);
			if (aParameterNumber != -1) {
				List <MethodCall> aCalls = aMethodSNode.getCalls();
				for (MethodCall aCall:aCalls) {
//					if (!aCall.getMethodActuals().contains(aVariable))
//						continue;
//					
					MethodSNode aCallerSNode = getDeclarationOfCalledMethod(aMethodSNode, aCall);
					if (dependsOn(aMethodSNode, aCallerSNode.getLineNumber(), aCallerSNode.getLocalVariableIdentifiers().get(aParameterNumber), aCallIdentifier));
//				    if (aCallerDepends) {
				    	return true;
//				    }
				}
				return false;
				// need to find all callers of method and see if any of the aliases for the variable in these
				// calls depend on aCallIndentifier
			}			
			
		} else if (anSNode.getLocalVariableIdentifiers().contains(aVariable)) {
			// before going to the parent node, let us see if aVariable is declared here as a non  parameter 

			return false; // no point going to parent
		}
		// not a local variable (parameter or declarated variable)
		SNode anSNodeParent = anSNode.getParent() ;
		if (anSNodeParent == null)  {
			return false;
		}
		int anSNodeLineNumber = anSNodeParent.getChildren().indexOf(anSNode);
		return dependsOn(anSNodeParent, anSNodeLineNumber, aVariable, aCallIdentifier);
		
	}
	public static Set<AssignmentSNode> assignmentsOfVariableAliases (SNode anSNode,  String aVariable) {
		Set<AssignmentSNode> retVal = new HashSet();
		fillDirectAssignmentsOfVariableAliases(anSNode, aVariable, retVal);
		return retVal;
	}
	public static void fillDirectAssignmentsOfVariableAliases (SNode anSNode,  String aVariable, Set<AssignmentSNode> retVal) {
		if (anSNode instanceof MethodCall) {
			MethodCall aMethodCall = (MethodCall) anSNode;
			int aParameterNumber = aMethodCall.getMethodActualIdentifiers().indexOf(aVariable);
			if (aParameterNumber < 0) {
				return;
			}
			MethodSNode aDeclaringMethodSNode = getDeclarationOfCalledMethod(anSNode, aMethodCall);
			String aFormalParameter = aDeclaringMethodSNode.getLocalVariableIdentifiers().get(aParameterNumber);
//			Set<AssignmentSNode> aCallAssignments = ;
			fillDirectAssignmentsOfVariableAliases(aDeclaringMethodSNode, aFormalParameter, retVal);
		} else if (anSNode instanceof MethodSNode)	{
			MethodSNode aMethodSNode = (MethodSNode) anSNode;
			int aParameterNumber = aMethodSNode.getLocalVariableIdentifiers().indexOf(aVariable);
			if (aParameterNumber < 0)
				return;
			List<MethodCall> aCalls = aMethodSNode.getCalls();
			for (MethodCall aMethodCall:aCalls) {
				MethodSNode aCallerMethodSNode = getDeclarationOfCalledMethod(anSNode, aMethodCall);
				if (aCallerMethodSNode != null) {
					String anAlias = aCallerMethodSNode.getLocalVariableIdentifiers().get(aParameterNumber);
					fillDirectAssignmentsOfVariableAliases(aCallerMethodSNode, anAlias, retVal);

				}

			}
		}
		
		else {
			List<SNode> aChildren = anSNode.getChildren();
			for (SNode aChild:aChildren) {
				fillDirectAssignmentsOfVariableAliases(aChild, aVariable, retVal);
			}
		} 	
		
	}
	
	/*
	 * will not consider assignments to variables in the RHS of these assignments
	 */
	public static Set<AssignmentSNode> directAssignmentsOfVariableAndItsAliases (SNode anSNode,  String aVariable) {
		Set<AssignmentSNode> retVal = new HashSet();
		fillDirectAssignmentsOfVariableAndItsAliases(anSNode, aVariable, retVal);
		return retVal;
	}
	
//	public static Set<AssignmentSNode> assignmentsEffectingVariableAndItsAliases (SNode anSNode,  String aVariable) {
//		Set<AssignmentSNode> retVal = directAssignmentsOfVariableAndItsAliases(anSNode, aVariable);
//		
//	}
	public static void fillDirectAssignmentsOfVariableAndItsAliases (SNode anSNode,  String aVariable, Set<AssignmentSNode> retVal) {
		if (anSNode instanceof AssignmentSNode) {
			AssignmentSNode anAssignmentSNode = (AssignmentSNode) anSNode;
			if (anAssignmentSNode.getLhsFirstIdentifier().equals(aVariable)) {
				retVal.add(anAssignmentSNode);
			}
		}
		
		else if (anSNode instanceof MethodCall) {
			MethodCall aMethodCall = (MethodCall) anSNode;
			int aParameterNumber = aMethodCall.getMethodActualIdentifiers().indexOf(aVariable);
			if (aParameterNumber < 0) {
				return;
			}
			MethodSNode aDeclaringMethodSNode = getDeclarationOfCalledMethod(anSNode, aMethodCall);
			String aFormalParameter = aDeclaringMethodSNode.getLocalVariableIdentifiers().get(aParameterNumber);
//			Set<AssignmentSNode> aCallAssignments = ;
			fillDirectAssignmentsOfVariableAndItsAliases(aDeclaringMethodSNode, aFormalParameter, retVal);
		} else {
			List<SNode> aChildren = anSNode.getChildren();
			for (SNode aChild:aChildren) {
				fillDirectAssignmentsOfVariableAndItsAliases(aChild, aVariable, retVal);
			}
		}		
		
	}
	
	public static RootOfFileSNode getRootOfFileNode(SNode aCurrentSNode ) {
		if (aCurrentSNode instanceof RootOfFileSNode) {
			return (RootOfFileSNode) aCurrentSNode;
		}
		SNode aParentSNode = aCurrentSNode.getParent() ;
		
		if (aParentSNode == null) {
			return null;
		}
		
		return getRootOfFileNode(aParentSNode);
		
	}
	public static boolean match (MethodSNode aMethodSNode, MethodCall aMethodCall) {
		return aMethodSNode.getMethodName().equals(aMethodCall.getMethodName()) 
				&& aMethodSNode.getLocalVariableIdentifiers().size() == aMethodCall.getMethodActuals().size();
	}
	public static DeclarationSNode getDeclarationOfVariableIdentifier(SNode aCurrentSNode, String anIdentifier) {
		if (aCurrentSNode == null) {
			return null;
		}
		
		
		int anIndex = aCurrentSNode.getLocalVariableIdentifiers().indexOf(anIdentifier);
		if (anIndex < 0) {
			return getDeclarationOfVariableIdentifier(aCurrentSNode.getParent(), anIdentifier);
		}
		DeclarationSNode retVal = aCurrentSNode.getVariableDeclarations().get(anIndex);
//		retVal.getAssignmentsToDeclaredVariable().add(anAssignmentSNode);
//		anAssignmentSNode.setLhsFirstIdentifierDeclaration(retVal);
		return retVal;
	}
	
	public static DeclarationSNode getDeclarationOfAssignedVariable(SNode aCurrentSNode, AssignmentSNode anAssignmentSNode) {
		if (aCurrentSNode == null) {
			return null;
		}
		if (anAssignmentSNode instanceof ADeclaringAssignmentSNode) {
			return (ADeclaringAssignmentSNode) anAssignmentSNode;
		}
		int anIndex = aCurrentSNode.getLocalVariableIdentifiers().indexOf(anAssignmentSNode.getLhsFirstIdentifier());
		if (anIndex < 0) {
			return getDeclarationOfAssignedVariable(aCurrentSNode.getParent(), anAssignmentSNode);
		}
		DeclarationSNode retVal = aCurrentSNode.getVariableDeclarations().get(anIndex);
//		retVal.getAssignmentsToDeclaredVariable().add(anAssignmentSNode);
//		anAssignmentSNode.setLhsFirstIdentifierDeclaration(retVal);
		return retVal;
	}
	
	public static MethodSNode getDeclarationOfCalledMethod(SNode aCurrentSNode, MethodCall aMethodCall ) {
		SNode aRootNode = getRootOfFileNode(aCurrentSNode);
		for (SNode aChild:aRootNode.getChildren()) {
			if (aChild instanceof MethodSNode) {
				MethodSNode aMethodSNode = (MethodSNode) aChild;
				if (match (aMethodSNode, aMethodCall)) {
					return aMethodSNode;
				}
			}
		}
		return null; // this should never happen;
	}
	
	public static RootOfProgramSNode getRootOfProgramSNode(String aSource) {
		RootOfProgramSNode retVal = new ARootOfProgramSNode();
		Map<String, StringBuffer> aFileNameToContents = ABasicTextManager.extractFileContents(aSource);
		for (String aFileName:aFileNameToContents.keySet()) {
			StringBuffer aFileContents = aFileNameToContents.get(aFileName);
//			List<OpenMPPragma> anOpenMPPragmas = OpenMPUtils.getOpemMPPragmas(aFileContents);
			System.out.println("Processing file:" + aFileName);
			RootOfFileSNode anSNode = OMPSNodeUtils.getSNode(aFileName, aFileContents);
			retVal.getFileNameToSNode().put(aFileName, anSNode);
			anSNode.setParent(retVal);
//			System.out.println("file name:" + aFileName);
//			System.out.println("pragmas:" + anSNode);
		}
		processExternalMethodSNodes(retVal);
		setParentOfParameterDeclarations(retVal);
		processIndirectAssignments(retVal);
		return retVal;
	}
//	public static void processIndirectAssignments (S aRootOfProgramSNode, RootOfFileSNode aRootOfFileSNode) {
//		for (SNode anSNode:aRootOfFileSNode.getChildren()) {
//			-----
//		}
//	}
	public static void setParentOfParameterDeclarations(RootOfProgramSNode aRootOfProgramSNode, RootOfFileSNode aRootOfFileSNode) {
		for (SNode anSNode:aRootOfFileSNode.getChildren()) {
			if (anSNode instanceof MethodSNode) {
				setParentOfParameterDeclarations(aRootOfProgramSNode, aRootOfFileSNode, (MethodSNode) anSNode);
			}
		}
	}
	public static void setParentOfParameterDeclarations (RootOfProgramSNode aRootOfProgramSNode, RootOfFileSNode aRootOfFileSNode, MethodSNode aMethodSNode) {
		List<DeclarationSNode> aDeclarations = aMethodSNode.getVariableDeclarations();
		for (DeclarationSNode aDeclaration:aDeclarations) {
			if (aDeclaration.getParent() == null) {
				aDeclaration.setParent(aMethodSNode);
			}
		}
	}
	public static void setParentOfParameterDeclarations (RootOfProgramSNode aRootOfProgramSNode) {
		for (String aFileName:aRootOfProgramSNode.getFileNameToSNode().keySet()) {
			RootOfFileSNode aRootOfFileSNode = aRootOfProgramSNode.getFileNameToSNode().get(aFileName);
			setParentOfParameterDeclarations(aRootOfProgramSNode, aRootOfFileSNode);
			
		}

	}
	public static void processExternalMethodSNodes (RootOfProgramSNode aRootOfProgramSNode, RootOfFileSNode aRootOfFileSNode) {
		for (SNode anSNode:aRootOfFileSNode.getChildren()) {
			if (anSNode instanceof ExternalMethodSNode) {
				processExternalMethodSNode(aRootOfProgramSNode, aRootOfFileSNode, (ExternalMethodSNode) anSNode);
			}
		}
	}

	public static void processExternalMethodSNode (RootOfProgramSNode aRootOfProgramSNode, RootOfFileSNode aRootOfFileSNode, ExternalMethodSNode anExternalMethodSNode) {
		MethodSNode aMethodSNode = aRootOfProgramSNode.getExternalToInternalMethod().get(anExternalMethodSNode.toString());
		if (aMethodSNode == null) {
			aMethodSNode = findMethodSNode(aRootOfProgramSNode, aRootOfFileSNode, anExternalMethodSNode);
			if (aMethodSNode != null) {
				aRootOfProgramSNode.getExternalToInternalMethod().put(anExternalMethodSNode.toString(),aMethodSNode );
			}
		}
		if (aMethodSNode != null) {
			anExternalMethodSNode.setActualMethodSNode(aMethodSNode);
			aMethodSNode.getCalls().addAll(anExternalMethodSNode.getLocalCalls());
		}
	}
	
	public static MethodSNode findMethodSNode (RootOfProgramSNode aRootOfProgramSNode, RootOfFileSNode aRootOfFileSNode, ExternalMethodSNode anExternalMethodSNode) {
//		MethodSNode foundMethodSNode = null;
		for (String aFileName:aRootOfProgramSNode.getFileNameToSNode().keySet()) {
			if (aFileName.equals(aRootOfFileSNode.getFileName()))
				continue;
			
			RootOfFileSNode aSearchedRootOfFileSNode = aRootOfProgramSNode.getFileNameToSNode().get(aFileName);
			 for (SNode anSNode:aSearchedRootOfFileSNode.getChildren()) {
				if (anSNode instanceof MethodSNode && !(anSNode instanceof ExternalMethodSNode)) {
					if (anSNode.toString().equals(anExternalMethodSNode.toString())) {
						return (MethodSNode) anSNode;
						
					}
//					processExternalMethodSNode(aRootOfProgramSNode, aRootOfFileSNode, (ExternalMethodSNode) anSNode);
				}
			}
		}
		return null;
	}
	public static void processExternalMethodSNodes (RootOfProgramSNode aRootOfProgramSNode) {
		for (String aFileName:aRootOfProgramSNode.getFileNameToSNode().keySet()) {
			RootOfFileSNode aRootOfFileSNode = aRootOfProgramSNode.getFileNameToSNode().get(aFileName);
			processExternalMethodSNodes(aRootOfProgramSNode, aRootOfFileSNode);
			aRootOfFileSNode.getOmp_get_num_threads_SNode().
				setActualMethodSNode(aRootOfProgramSNode.getOmp_get_num_threads_SNode());
			aRootOfFileSNode.getOmp_get_thread_num_SNode().
				setActualMethodSNode(aRootOfProgramSNode.getOmp_get_thread_num_SNode());
			aRootOfFileSNode.getOmp_get_wtime_SNode().
			setActualMethodSNode(aRootOfProgramSNode.getOmp_get_wtime_SNode());
		}

	}
	public static void processIndirectAssignments (SNode anSNode) {
		if (anSNode instanceof ConstDeclarationSNode) {
			return;
		}
		if (anSNode instanceof DeclarationSNode) {
			DeclarationSNode aDeclarationSNode = (DeclarationSNode) anSNode;
			Set<AssignmentSNode> anIndirectAssignments = aDeclarationSNode.getAssignmentsEffectingDeclaredIdentifier();
			if (anIndirectAssignments != null) {
				return; // already assigned
			}
			anIndirectAssignments = new HashSet<>();
			aDeclarationSNode.setAssignmentsEffectingDeclaredIdentifier(anIndirectAssignments);
			Set<AssignmentSNode> aDirectAssignments = aDeclarationSNode.getAssignmentsToDeclaredVariable();
			anIndirectAssignments.addAll(aDirectAssignments);
//			Set<AssignmentSNode> anAliasAssignments = assignmentsOfVariableAliases(aDeclarationSNode.getParent(), aDeclarationSNode.getVariableName());
//			anIndirectAssignments.addAll(anAliasAssignments);
			for (AssignmentSNode aDirectAssignment:aDirectAssignments) {
				List<String> aVariableIdentifiers = aDirectAssignment.getRhsVariableIdentifiers();
				for (String aVariableIdentifier:aVariableIdentifiers) {
					DeclarationSNode aDependeeDeclarationSNode = getDeclarationOfVariableIdentifier(aDirectAssignment, aVariableIdentifier);
					if (aDependeeDeclarationSNode == null) {
						if (typeNamesSet.contains(aVariableIdentifier)) {
//							continue;
							return;
						}
						if (aVariableIdentifier.equals("new")) {
//							continue;
							return;
						}
						int aCommentStart = aDirectAssignment.toString().indexOf("//");
						if (aCommentStart != -1) {
							int aVariableStart = aDirectAssignment.toString().indexOf(aVariableIdentifier + " ");
							if (aVariableStart == -1 || aVariableStart > aCommentStart) {
								continue;
							}
						}
						System.err.println("Could not find declaration of:" + aVariableIdentifier + " referenced in:" + aDirectAssignment );
						continue;
					}
					processIndirectAssignments(aDependeeDeclarationSNode);
					Set<AssignmentSNode> aDependeeIndirectAssignments = aDeclarationSNode.getAssignmentsEffectingDeclaredIdentifier();
					anIndirectAssignments.addAll(aDependeeIndirectAssignments);
					
				}
			}
		} else {
			for (SNode aChild: anSNode.getChildren()) {
				processIndirectAssignments(aChild);
			}
		}
		
	}
	
//	public static void fillIndirectAssignments (SNode anSNode, Set<AssignmentSNode> retVal ) {
//		
//	}
	

//	public static void main(String[] args) {
//		List<MethodCall> aTokens = callsIn("foo(bar, hgf)/foo2()*foo3(b, a c,)");
//		System.out.println(aTokens);
//		
//		
////		List<String> aTokens = identifiersIn("a[i] + b*2/3");
////		System.out.println(aTokens);
////		aTokens = numbersIn("a[i] + b*2/3");
////		System.out.println(aTokens);
////		aTokens = operatorsIn("a[i] + b*2/3");
////		System.out.println(aTokens);
////
////		System.out.println(StringUtils.substringBetween("a", "[", "]"));
//	}

}
