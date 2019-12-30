package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public interface ExternalMethodSNode extends MethodSNode {

	MethodSNode getActualMethodSNode();

	void setActualMethodSNode(MethodSNode actualMethodSNode);

	List<MethodCall> getLocalCalls();

}