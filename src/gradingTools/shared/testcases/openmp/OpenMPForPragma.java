package gradingTools.shared.testcases.openmp;

import java.util.List;

public interface OpenMPForPragma extends OpenMPPragma{
	void setReductionVariable(String reductionVariable);

	String getReductionOperation();

	void setReductionOperation(String reductionOperation);

	List<String> getReductionVariableAssignments();

	List<String> getReductionOperationUses();
	
	String getReductionVariable();



}
