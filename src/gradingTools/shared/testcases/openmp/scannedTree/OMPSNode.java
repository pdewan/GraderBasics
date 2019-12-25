package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

import gradingTools.shared.testcases.openmp.OpenMPPragma;

public interface OMPSNode extends SNode{

	OMPSNode getOmpParent();

	void setOmpParent(OMPSNode ompParent);

	List<String> getOpenMPTokens();

	void setOpenMPTokens(List<String> openMPTokens);

	List<String> getLinesWithOpenMPCalls();

	List<OMPSNode> getOmpChildren();

	String[] getPrivateVariables();

	String[] getSharedVariables();

	void setPrivateVariables(String[] privateVariables);

	void setSharedVariables(String[] sharedVariables);

}
