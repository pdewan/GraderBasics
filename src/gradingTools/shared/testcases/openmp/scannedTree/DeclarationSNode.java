package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;
import java.util.Set;

public interface DeclarationSNode extends SNode{

	String getTypeName();

	void setTypeName(String typeName);

	String getVariableName();

	String getVariableIdentifier();

	String getTypeIdentifier();

	List<String> getOperators();

	Set<AssignmentSNode> getAssignmentsToDeclaredVariable();

	Set<AssignmentSNode> getAssignmentsEffectingDeclaredIdentifier();

	void setAssignmentsEffectingDeclaredIdentifier(Set<AssignmentSNode> assignmentsEffectingDeclaredIdentifier);

//	void setVariableName(String variableName);

}