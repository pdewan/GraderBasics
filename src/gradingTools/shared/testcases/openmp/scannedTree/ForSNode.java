package gradingTools.shared.testcases.openmp.scannedTree;

public interface ForSNode extends SNode{

	AssignmentSNode getInitalization();

	String getCondition();

	AssignmentSNode getIncrement();

	ExpressionSNode getConditionSNode();

//	SNode getBody();

}