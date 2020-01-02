package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public class ADeclaringAssignmentSNode extends AnAssignmentSNode implements DeclaringAssignmentSNode{
	String typeName;
	String typeIdentifier;
	List<String> operators;
	public ADeclaringAssignmentSNode(int aLineNumber, String aTypeName, String lhs, String operationAndRHS) {
		super(aLineNumber, lhs, operationAndRHS);
		typeName = aTypeName;
		typeIdentifier = OMPSNodeUtils.identifiersIn(typeName).get(0);
		operators = lhsOperators;
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
		anSNode.getLocalVariableIdentifiers().add(lhsVariable);
		
	}
	@Override
	public String getVariableIdentifier() {
		return lhsVariable;
	}
	@Override
	public String getTypeIdentifier() {
		return typeIdentifier;
	}
	@Override
	public List<String> getOperators() {
		return null;
	}
	
}
