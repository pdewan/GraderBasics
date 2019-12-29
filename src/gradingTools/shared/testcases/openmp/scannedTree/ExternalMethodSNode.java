package gradingTools.shared.testcases.openmp.scannedTree;

public interface ExternalMethodSNode extends MethodSNode {

	MethodSNode getActualMethodSNode();

	void setActualMethodSNode(MethodSNode actualMethodSNode);

}