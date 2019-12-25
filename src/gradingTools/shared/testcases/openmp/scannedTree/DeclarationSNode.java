package gradingTools.shared.testcases.openmp.scannedTree;

public interface DeclarationSNode extends SNode{

	String getTypeName();

	void setTypeName(String typeName);

	String getVariableName();

	void setVariableName(String variableName);

}