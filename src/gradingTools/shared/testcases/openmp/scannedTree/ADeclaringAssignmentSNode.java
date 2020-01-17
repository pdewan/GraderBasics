package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ADeclaringAssignmentSNode extends AnAssignmentSNode implements DeclaringAssignmentSNode{
	String typeName;
	String typeIdentifier;
	List<String> operators;
	Set<AssignmentSNode> assignmentsToDeclaredVariable = new HashSet();
	Set<AssignmentSNode> assignmentsEffectingDeclaredIdentifier ;


	public ADeclaringAssignmentSNode(int aLineNumber, String aTypeName, String lhs, String operationAndRHS) {
		super(aLineNumber, lhs, operationAndRHS);
		typeName = aTypeName;
		typeIdentifier = OMPSNodeUtils.identifiersIn(typeName).get(0);
		operators = lhsOperators;
		assignmentsToDeclaredVariable.add(this);
		setLhsFirstIdentifierDeclaration(this);
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
		anSNode.getLocalVariableIdentifiers().add(lhsFirstIdentifier);
		
	}
	@Override
	public String getVariableIdentifier() {
		return lhsFirstIdentifier;
	}
	@Override
	public String getTypeIdentifier() {
		return typeIdentifier;
	}
	@Override
	public List<String> getOperators() {
		return null;
	}
	@Override
	public Set<AssignmentSNode> getAssignmentsToDeclaredVariable() {
		return assignmentsToDeclaredVariable;
	}
	public String toString() {
		return getTypeName() + " " + super.toString();
	}
	@Override
	public Set<AssignmentSNode> getAssignmentsEffectingDeclaredIdentifier() {
		return assignmentsEffectingDeclaredIdentifier;
	}
	@Override
	public void setAssignmentsEffectingDeclaredIdentifier(Set<AssignmentSNode> newVal) {
		assignmentsEffectingDeclaredIdentifier = newVal;	
	}
	
}
