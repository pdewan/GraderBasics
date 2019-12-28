package gradingTools.shared.testcases.openmp.scannedTree;

public interface DeclaringAssignmentSNode extends AssignmentSNode, DeclarationSNode{

	String getTypeName();

	void setTypeName(String typeName);

}