package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ADeclarationSNode extends AnSNode implements DeclarationSNode {
	String variableName;
	
	String typeName;
	List<String> operators;
	

	String variableIdentifier;
	String typeIdentifier;
	Set<AssignmentSNode> assignmentsToDeclaredVariable = new HashSet();
	Set<AssignmentSNode> assignmentsEffectingDeclaredIdentifier;

	
	public ADeclarationSNode(int aLineNumber, String aTypeName, String aVariableName) {
		super(aLineNumber);
		typeName = aTypeName;
		if (aVariableName.endsWith(",") || aVariableName.endsWith(";")) {
			aVariableName = aVariableName.substring(0, aVariableName.length() - 1);
		}
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
		if (!anSNode.getVariableDeclarations().contains(this)) {
		anSNode.getVariableDeclarations().add(this);
//		anSNode.getLocalVariables().add(variableName);
		anSNode.getLocalVariableIdentifiers().add(variableIdentifier);
		}

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
	@Override
	public Set<AssignmentSNode> getAssignmentsToDeclaredVariable() {
		return assignmentsToDeclaredVariable;
	}
//	@Override
//
//	public void setVariableName(String variableName) {
//		this.variableName = variableName;
//	}
	@Override
	public Set<AssignmentSNode> getAssignmentsEffectingDeclaredIdentifier() {
		return assignmentsEffectingDeclaredIdentifier;
	}
	@Override
	public void setAssignmentsEffectingDeclaredIdentifier(Set<AssignmentSNode> assignmentsEffectingDeclaredIdentifier) {
		this.assignmentsEffectingDeclaredIdentifier = assignmentsEffectingDeclaredIdentifier;
	}
}
