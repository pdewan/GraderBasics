package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;
import java.util.Set;

import gradingTools.shared.testcases.openmp.Assignment;
import gradingTools.shared.testcases.openmp.checks.OpenMPPragmaAttribute;

public interface SNode {

	int getLineNumber();

	SNode getParent();

	void setParent(SNode parent);

	List<Assignment> getAssignments();

	List<DeclarationSNode> getVariableDeclarations();

	List<String> getLocalVariableIdentifiers();

	List<ForSNode> getForNodes();

	List<SNode> getChildren();

	List<OMPSNode> getOmpSNodes();

	void addChild(SNode aChild);

	Set<OpenMPPragmaAttribute> getAttributes();

	boolean isInParallel();

	boolean isLeaf();

	boolean isInCritical();

}
