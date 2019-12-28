package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public class AnAssignmentSNode extends AnSNode implements AssignmentSNode {
	String lhs;
	String[] lhsSubscripts;
	String lhsVariable;
	List<String> lhsOperators;
	String operationAndRHS;
	List<String> rhsIdentifiers;
	List<String> rhsOperators;
	List<String> rhsNumbers;
	public AnAssignmentSNode(int aLineNumber, String lhs, String operationAndRHS) {
		super(aLineNumber);
		this.lhs = lhs;
		this.operationAndRHS = operationAndRHS;
		lhsVariable = OMPSNodeUtils.identifiersIn(lhs).get(0);
		lhsOperators =  OMPSNodeUtils.operatorsIn(lhs);		
		lhsSubscripts = OMPSNodeUtils.subscriptsIn(lhs);
		rhsIdentifiers = OMPSNodeUtils.identifiersIn(operationAndRHS);
		rhsNumbers = OMPSNodeUtils.numbersIn(operationAndRHS);
		rhsOperators = OMPSNodeUtils.operatorsIn(operationAndRHS);		
	}
	@Override
	public String getLHS() {
		return lhs;
	}
	@Override
	public String getOperationAndRHS() {
		return operationAndRHS;
	}
	public String[] getLhsSubscripts() {
		return lhsSubscripts;
	}
	public String getVariableName() {
		return lhsVariable;
	}
	public List<String> getLhsOperators() {
		return lhsOperators;
	}
	public List<String> getRhsIdentifiers() {
		return rhsIdentifiers;
	}
	public List<String> getRhsOperators() {
		return rhsOperators;
	}
	public List<String> getRhsNumbers() {
		return rhsNumbers;
	}
	

}
