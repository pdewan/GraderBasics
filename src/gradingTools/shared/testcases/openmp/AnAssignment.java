package gradingTools.shared.testcases.openmp;

public class AnAssignment implements Assignment {
	String lhs;
	String operationAndRHS;
	public AnAssignment(String lhs, String operationAndRHS) {
		super();
		this.lhs = lhs;
		this.operationAndRHS = operationAndRHS;
	}
	@Override
	public String getLhs() {
		return lhs;
	}
	@Override
	public String getOperationAndRHS() {
		return operationAndRHS;
	}
	

}
