package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public interface OMPForSNode extends OMPSNode{

	String getReductionOperation();

	void setReductionOperation(String reductionOperation);

	List<AssignmentSNode> getReductionVariableAssignments();

	List<String> getReductionOperationUses();

	String getReductionVariable();

	void setReductionVariable(String reductionVariable);

}