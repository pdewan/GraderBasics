package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public interface DeclarationSNode extends SNode{

	String getTypeName();

	void setTypeName(String typeName);

	String getVariableName();

	String getVariableIdentifier();

	String getTypeIdentifier();

	List<String> getOperators();

//	void setVariableName(String variableName);

}