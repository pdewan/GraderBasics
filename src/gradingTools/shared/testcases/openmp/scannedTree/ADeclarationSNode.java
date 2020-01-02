package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public class ADeclarationSNode extends AnSNode implements DeclarationSNode {
	String variableName;
	
	String typeName;
	List<String> operators;
	

	String variableIdentifier;
	String typeIdentifier;
	public ADeclarationSNode(int aLineNumber, String aTypeName, String aVariableName) {
		super(aLineNumber);
		typeName = aTypeName;
		variableName = aVariableName;
		operators = OMPSNodeUtils.operatorsIn(typeName);
		operators.addAll(OMPSNodeUtils.operatorsIn(typeName));
		variableIdentifier = OMPSNodeUtils.identifiersIn(variableName).get(0);
		typeIdentifier =  OMPSNodeUtils.identifiersIn(typeName).get(0);
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
//		anSNode.getLocalVariables().add(variableName);
		anSNode.getLocalVariableIdentifiers().add(variableIdentifier);

	}
	public String toString() {
		return typeName + " " + variableName;
	}
	@Override
	public String getVariableIdentifier() {
		return variableIdentifier;
	}
	
	@Override
	public String getTypeIdentifier() {
		return typeIdentifier;
	}
	@Override
	public List<String> getOperators() {
		return operators;
	}
	
//	@Override
//
//	public void setVariableName(String variableName) {
//		this.variableName = variableName;
//	}
}
