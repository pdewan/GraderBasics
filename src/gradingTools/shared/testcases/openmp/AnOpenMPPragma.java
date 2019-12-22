package gradingTools.shared.testcases.openmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnOpenMPPragma implements OpenMPPragma {
//	protected List<OpenMPKeywordEnum> openMPKeywords = new ArrayList();
	protected List<String> openMPTokens = new ArrayList();

	protected int lineNumber;
	protected List<String> annotatedText = new ArrayList();
	protected int annotatedTextStartLineNumber;
	protected int annotatedTextEndLineNumber;
	protected OpenMPPragma parent;
	protected List<OpenMPPragma> children = new ArrayList();
	protected List<String[]> variableDeclarations = new ArrayList();
	protected String reductionVariable;
	

	protected String reductionOperation;
	
	
	
	
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
	@Override
	public String getReductionVariable() {
		return reductionVariable;
	}
	@Override
	public void setReductionVariable(String reductionVariable) {
		this.reductionVariable = reductionVariable;
	}
	@Override
	public String getReductionOperation() {
		return reductionOperation;
	}
	@Override
	public void setReductionOperation(String reductionOperation) {
		this.reductionOperation = reductionOperation;
	}
	
	public String toString() {
		return lineNumber + ":" + openMPTokens +
				"--> (" + annotatedTextStartLineNumber + "," + annotatedTextEndLineNumber + "):" + annotatedText;
	}

}
