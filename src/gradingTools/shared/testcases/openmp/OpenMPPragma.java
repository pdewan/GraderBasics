package gradingTools.shared.testcases.openmp;

import java.util.List;

public interface OpenMPPragma {

	String[] getOpenMPTokens();

	int getLineNumber();

	List<String> getAnnotatedText();

//	void setAnnotatedText(StringBuffer nextNonEmptyString);

	int getAnnotatedTextStartLineNumber();

	void setAnnotatedTextStartLineNumber(int annotatedLineNumber);

	int getAnnotatedTextEndLineNumber();

	void setAnnotatedTextEndLineNumber(int annotatedTextEndLineNumber);

}