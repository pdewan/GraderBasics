package gradingTools.shared.testcases.openmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnOpenMPPragma implements OpenMPPragma {
//	protected List<OpenMPKeywordEnum> openMPKeywords = new ArrayList();
	protected String[] openMPTokens;

	protected int lineNumber;
	protected List<String> annotatedText = new ArrayList();
	protected int annotatedTextStartLineNumber;
	protected int annotatedTextEndLineNumber;


	
	
	
	public AnOpenMPPragma(String[] openMPTokens, int lineNumber) {
		super();
		this.openMPTokens = openMPTokens;
		this.lineNumber = lineNumber;
		
//		this.nextNonEmptyString = nextNonEmptyString;
	}
	@Override
	public String[] getOpenMPTokens() {
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
	
	public String toString() {
		return lineNumber + ":" + Arrays.toString(openMPTokens) +
				"--> (" + annotatedTextStartLineNumber + "," + annotatedTextEndLineNumber + "):" + annotatedText;
	}

}
