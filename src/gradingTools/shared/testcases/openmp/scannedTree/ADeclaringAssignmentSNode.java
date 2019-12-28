package gradingTools.shared.testcases.openmp.scannedTree;

public class ADeclaringAssignmentSNode extends AnAssignmentSNode implements DeclaringAssignmentSNode{
	String typeName;
	public ADeclaringAssignmentSNode(int aLineNumber, String aTypeName, String lhs, String operationAndRHS) {
		super(aLineNumber, lhs, operationAndRHS);
		typeName = aTypeName;
	}
	@Override
	public String getTypeName() {
		return typeName;
	}
	@Override
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Override
	public void setParent(SNode anSNode) {
		super.setParent(anSNode);
		anSNode.getVariableDeclarations().add(this);
		anSNode.getLocalVariables().add(lhsVariable);
	}
	
}
