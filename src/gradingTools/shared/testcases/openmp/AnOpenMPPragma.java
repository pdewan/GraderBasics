package gradingTools.shared.testcases.openmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnOpenMPPragma implements OpenMPPragma {
//	protected List<OpenMPKeywordEnum> openMPKeywords = new ArrayList();
	protected List<String> openMPTokens = new ArrayList();
//	protected OpenMPKeywordEnum firstOpenMPKeyword;
	
//	protected List<String> variableDeclarationsInParallel = new ArrayList();//separate subclass for this variable?
	
//	protected String assignedVariableInCritical;// separate subclass for this variable?
	
	protected int lineNumber;
	protected List<String> annotatedText = new ArrayList();
	protected List<String> openMPCalls = new ArrayList();
	
	protected int annotatedTextStartLineNumber;
	protected int annotatedTextEndLineNumber;
	protected OpenMPPragma parent;
	

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
	public void addToAnnotatedText(String aFileLine) {
		annotatedText.add(aFileLine);
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
	public void addOpenMPCall(String aFileLine) {
		openMPCalls.add(aFileLine);
	}
	public String toString() {
		return lineNumber + ":" + openMPTokens +
				"--> (" + annotatedTextStartLineNumber + "," + annotatedTextEndLineNumber + "):" + annotatedText;
	}
}
