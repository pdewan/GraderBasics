package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
				aForSNode.setParent(anSNodes.peek());
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
				List<MethodCall> aCalls = callsIn(i, aFileLine);
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
		if (anSNode.getLocalVariables().contains(anLHS)) {
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
	public static List<MethodCall> callsIn(int aLineNumber, String aString) {
		if (aString == null)
			return null;
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
				aParameterList.add(aParameter.trim());
			}
			retVal.add(new AMethodCall(aLineNumber, aMethodName, aParameterList));

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
	public static String[] subscriptsIn(String aString) {
		if (aString == null)
			return null;
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
		int retVal = numberOfNestingFors (anSNode.getParent()) ;

		if (anSNode instanceof ForSNode) {
			retVal++;
		}
		return retVal;
	}
	public static boolean dependsOn (AssignmentSNode anAssignmentSNode, String aVariable, String aCallIdentifier) {
		// This assignment does not change aVariable
		if (!aVariable.equals(anAssignmentSNode.getLhsVariable())) {
			return false;
		}
		List<String> aCallIdentifiers = anAssignmentSNode.getRhsCallIdentifiers();
		boolean retVal = false;
		if (aCallIdentifiers != null && aCallIdentifiers.contains(aCallIdentifier)) {
			return true; // this assignment has aCallIdentifier in the rhs
		}
		// check if some referenced rhs variable depends on aCallIdentifier
		List<String> aReferencedVariableIdentifiers = anAssignmentSNode.getRhsVariableIdentifiers();
		SNode anAssignmentParent = anAssignmentSNode.getParent();
		if (anAssignmentParent == null) { // should never be trye
			return false;
		}
		int aLineNumber = anAssignmentSNode.getLineNumber();
		for (String aReferencedVariableIdentifier:aReferencedVariableIdentifiers ) {
			if (dependsOn (anAssignmentParent, aLineNumber, aReferencedVariableIdentifier, aCallIdentifier)) {
				return true;
			}
		}
		return false;
	}
	public static boolean dependsOn (SNode anSNode, int aVariableLineNumber, String aVariable, String aCallIdentifier) {
		List<SNode> aListSNodes = anSNode.getChildren();
		boolean retVal = false;
		for (int i = aVariableLineNumber; i >= 0; i--) {
			SNode anSNodeChild = aListSNodes.get(i);
			if (anSNodeChild instanceof AssignmentSNode) {
				// does this statement in anSNode directly invoke aCallIdentifier 
				if (dependsOn((AssignmentSNode) anSNodeChild, aVariable, aCallIdentifier))
					return true;
			} else if (!anSNode.isLeaf()) {
					// child is overriding the variable so forget checking its assignment statements
					if (anSNodeChild.getLocalVariables().contains(aVariable)) {
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
			int aParameterNumber = aMethodSNode.getLocalVariables().indexOf(aVariable);
			if (aParameterNumber != -1) {
				// need to find all callers of method and see if any of the aliases for the variable in these
				// calls depend on aCallIndentifier
			}			
			
		} else if (anSNode.getLocalVariables().contains(aVariable)) {
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
		return aMethodSNode.getMethodName().equals(aMethodCall.getMethodName()) && aMethodSNode.getLocalVariables().size() == aMethodCall.getMethodActuals().size();
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
			RootOfFileSNode anSNode = OMPSNodeUtils.getSNode(aFileName, aFileContents);
			retVal.getFileNameToSNode().put(aFileName, anSNode);
			anSNode.setParent(retVal);
//			System.out.println("file name:" + aFileName);
//			System.out.println("pragmas:" + anSNode);
		}
		processExternalMethodSNodes(retVal);
		return retVal;
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
			
		}

	}
	

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
