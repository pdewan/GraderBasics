package gradingTools.shared.testcases.openmp.scannedTree;

public class ADeclarationSNode extends AnSNode implements DeclarationSNode {
	String variableName;
	
	String typeName;
	public ADeclarationSNode(int aLineNumber, String aTypeName, String aVariableName) {
		super(aLineNumber);
		typeName = aTypeName;
		variableName = aVariableName;
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
	public String getVariableName() {
		return variableName;
	}
	@Override
	public void setParent(SNode anSNode) {
		super.setParent(anSNode);
		anSNode.getVariableDeclarations().add(this);
		anSNode.getLocalVariables().add(variableName);
	}
	public String toString() {
		return typeName + " " + variableName;
	}
//	@Override
//
//	public void setVariableName(String variableName) {
//		this.variableName = variableName;
//	}
}
