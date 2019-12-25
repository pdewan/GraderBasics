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

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
}
