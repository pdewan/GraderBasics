package gradingTools.shared.testcases.openmp;

import java.util.List;
import java.util.Set;

import gradingTools.shared.testcases.openmp.checks.OpenMPPragmaAttribute;

public interface OpenMPPragma {

	List<String> getOpenMPTokens();

	int getLineNumber();

	List<String> getAnnotatedText();

//	void setAnnotatedText(StringBuffer nextNonEmptyString);

	int getAnnotatedTextStartLineNumber();

	void setAnnotatedTextStartLineNumber(int annotatedLineNumber);

	int getAnnotatedTextEndLineNumber();

	void setAnnotatedTextEndLineNumber(int annotatedTextEndLineNumber);

//	String getReductionVariable();

//	void setReductionVariable(String reductionVariable);
//
//	String getReductionOperation();
//
//	void setReductionOperation(String reductionOperation);
//
//	List<String> getReductionVariableAssignments();
//
//	List<String> getReductionOperationUses();

//	OpenMPKeywordEnum getFirstOpenMPKeyword();
//
//	void setFirstOpenMPKeyword(OpenMPKeywordEnum firstOpenMPKeyword);

//	String getAssignedVariableInCritical();
//
//	void setAssignedVariableInCritical(String assignedVariableInCritical);

	void addToAnnotatedText(String aString, int aLineNumber);

	OpenMPPragma getParent();

	void setParent(OpenMPPragma parent);

	List<OpenMPPragma> getChildren();

	List<String> getOpenMPCalls();

	void addOpenMPCall(String aFileLine, int aLineNumber);

	String[] getSharedVariables();

	String[] getPrivateVariables();

	void setPrivateVariables(String[] privateVariables);

	void setSharedVariables(String[] sharedVariables);

	List<Assignment> getAssignments();
	List<String> getVariableDeclarations();
	List<String> getLocalVariables();

	Set<OpenMPPragmaAttribute> getAttributes();

	List<ForHeader> getForHeaders();

//	List<String> getVariableDeclarationsInParallel();

}