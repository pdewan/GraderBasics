package gradingTools.shared.testcases.openmp.scannedTree;

public class AnAssignmentSNode extends AnSNode implements AssignmentSNode {
	String lhs;
	String operationAndRHS;
	public AnAssignmentSNode(int aLineNumber, String lhs, String operationAndRHS) {
		super(aLineNumber);
		this.lhs = lhs;
		this.operationAndRHS = operationAndRHS;
	}
	@Override
	public String getLHS() {
		return lhs;
	}
	@Override
	public String getOperationAndRHS() {
		return operationAndRHS;
	}
	

}
