package gradingTools.shared.testcases.openmp.scannedTree;

public interface ForSNode extends SNode{

	AssignmentSNode getInitalization();

	String getCondition();

	String getIncrement();

//	SNode getBody();

}