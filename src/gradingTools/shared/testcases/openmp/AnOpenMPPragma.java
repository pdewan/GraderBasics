package gradingTools.shared.testcases.openmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gradingTools.shared.testcases.openmp.checks.OpenMPPragmaAttribute;

public class AnOpenMPPragma implements OpenMPPragma {
	protected List<String> variableDeclarations = new ArrayList();//separate subclass for this variable?
	protected List<String> localVariables = new ArrayList();//separate subclass for this variable?
	protected Set<OpenMPPragmaAttribute> attributes = new HashSet();


	//	protected List<OpenMPKeywordEnum> openMPKeywords = new ArrayList();
	protected List<String> openMPTokens = new ArrayList();
//	protected OpenMPKeywordEnum firstOpenMPKeyword;
	
//	protected List<String> variableDeclarationsInParallel = new ArrayList();//separate subclass for this variable?
	
//	protected String assignedVariableInCritical;// separate subclass for this variable?
	
	protected int lineNumber;
	protected List<String> annotatedText = new ArrayList();
	protected List<String> openMPCalls = new ArrayList();
	protected List<Assignment> assignments = new ArrayList();
	
	protected List<String> linesWithOpenMPCalls = new ArrayList();
	protected List<ForHeader> forHeaders = new ArrayList();
	
	protected int annotatedTextStartLineNumber;
	protected int annotatedTextEndLineNumber;
	protected OpenMPPragma parent;
	protected String[] privateVariables ; // can occur in for and parallel
	
	protected String[]  sharedVariables ; // can occur only in parallel, but let us keep them here
	

	

	protected List<OpenMPPragma> children = new ArrayList();

	//	protected String reductionVariable;
//	protected String reductionOperation;
//	protected List<String> reductionVariableAssignments = new ArrayList();
//	protected List<String> reductionOperationUses = new ArrayList();
//	
	public AnOpenMPPragma(int lineNumber) {
		super();
//		this.openMPTokens = openMPTokens;
		this.lineNumber = lineNumber;
		
//		this.nextNonEmptyString = nextNonEmptyString;
	}
	@Override
	public List<String> getOpenMPTokens() {
		return openMPTokens;
	}
	@Override
	public int getLineNumber() {
		return lineNumber;
	}
	@Override
	public List<String> getAnnotatedText() {
		return annotatedText;
	}
	@Override
	public void addToAnnotatedText(String aFileLine, int aLineNumber) {
		annotatedText.add(aFileLine);
		String[] aTokens = aFileLine.split("\\s+");
		if (OpenMPUtils.startsWithTypeName(aFileLine)) {
//			String[] aTokens = aFileLine.split("\\s+");						
			getVariableDeclarations().add(aFileLine);
			getLocalVariables().add(aTokens[1]);
	   } else if (aTokens.length >= 2 && aTokens[1].contains("=")) {
			String anExpressionString = aFileLine.substring(aTokens[0].length(), aFileLine.length()); // keep the assignment operation
			assignments.add(new AnAssignment(aTokens[0], anExpressionString));			
		}
		List<String> anOpenMPCalls = OpenMPUtils.getOMPCalls(aFileLine);
		
			for (String anOpenMPCall:anOpenMPCalls) {
				addOpenMPCall(anOpenMPCall, aLineNumber);
			}
			if (anOpenMPCalls.size() > 0) {
				linesWithOpenMPCalls.add(aFileLine);
			}
			ForHeader aForHeader = OpenMPUtils.getForHeader(aFileLine, aLineNumber);
			if (aForHeader != null) {
				forHeaders.add(aForHeader);
			}
			
	}
	@Override
	public List<ForHeader> getForHeaders() {
		return forHeaders;
	}
	//	@Override
//	public void setAnnotatedText(List<String> nextNonEmptyString) {
//		this.annotatedText = nextNonEmptyString;
//	}
	@Override
	public int getAnnotatedTextStartLineNumber() {
		return annotatedTextStartLineNumber;
	}
	@Override
	public void setAnnotatedTextStartLineNumber(int annotatedLineNumber) {
		this.annotatedTextStartLineNumber = annotatedLineNumber;
	}
	
	@Override
	public int getAnnotatedTextEndLineNumber() {
		return annotatedTextEndLineNumber;
	}
	@Override
	public void setAnnotatedTextEndLineNumber(int annotatedTextEndLineNumber) {
		this.annotatedTextEndLineNumber = annotatedTextEndLineNumber;
	}
//	@Override
//	public String getReductionVariable() {
//		return reductionVariable;
//	}
//	@Override
//	public void setReductionVariable(String reductionVariable) {
//		this.reductionVariable = reductionVariable;
//	}
//	@Override
//	public String getReductionOperation() {
//		return reductionOperation;
//	}
//	@Override
//	public void setReductionOperation(String reductionOperation) {
//		this.reductionOperation = reductionOperation;
//	}
//	@Override
//	public List<String> getReductionVariableAssignments() {
//		return reductionVariableAssignments;
//	}
//	@Override
//	public List<String> getReductionOperationUses() {
//		return reductionOperationUses;
//	}
//	@Override
//	public OpenMPKeywordEnum getFirstOpenMPKeyword() {
//		return firstOpenMPKeyword;
//	}
//	@Override
//	public void setFirstOpenMPKeyword(OpenMPKeywordEnum firstOpenMPKeyword) {
//		this.firstOpenMPKeyword = firstOpenMPKeyword;
//	}
//	@Override
//	public String getAssignedVariableInCritical() {
//		return assignedVariableInCritical;
//	}
//	@Override
//	public void setAssignedVariableInCritical(String assignedVariableInCritical) {
//		this.assignedVariableInCritical = assignedVariableInCritical;
//	}
	@Override
	public OpenMPPragma getParent() {
		return parent;
	}
	@Override
	public void setParent(OpenMPPragma parent) {
		this.parent = parent;
		parent.getChildren().add(this);
	}
	@Override
	public List<OpenMPPragma> getChildren() {
		return children;
	}
	@Override
	public List<String> getOpenMPCalls() {
		return openMPCalls;
	}
	@Override
	public void addOpenMPCall(String aFileLine, int aLineNumber) {
		openMPCalls.add(aFileLine);
	}
	@Override
	public String[] getSharedVariables() {
		return sharedVariables;
	}
	@Override
	public String[] getPrivateVariables() {
		return privateVariables;
	}
	@Override
	public void setPrivateVariables(String[] privateVariables) {
		this.privateVariables = privateVariables;
	}
	@Override
	public void setSharedVariables(String[] sharedVariables) {
		this.sharedVariables = sharedVariables;
	}
	@Override
	public List<Assignment> getAssignments() {
		return assignments;
	}
	@Override
	public List<String> getVariableDeclarations() {
		return variableDeclarations;
	}
	@Override
	public List<String> getLocalVariables() {
		return localVariables;
	}
	public String toString() {
		return lineNumber + ":" + openMPTokens +
				"--> (" + annotatedTextStartLineNumber + "," + annotatedTextEndLineNumber + "):" + annotatedText;
	}
	@Override
	public Set<OpenMPPragmaAttribute> getAttributes() {
		return attributes;
	}
}
