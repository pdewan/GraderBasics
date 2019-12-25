package gradingTools.shared.testcases.openmp.scannedTree;

public class AForSNode extends AnSNode implements ForSNode {
	AssignmentSNode initalization;
	String condition;	
	String increment;
	public AForSNode(int lineNumber, AssignmentSNode initalization, String condition, String increment) {
		super(lineNumber);
		this.initalization = initalization;
		this.condition = condition;
		this.increment = increment;
	}
	@Override
	public AssignmentSNode getInitalization() {
		return initalization;
	}
	@Override
	public String getCondition() {
		return condition;
	}
	@Override
	public String getIncrement() {
		return increment;
	}

	

}
