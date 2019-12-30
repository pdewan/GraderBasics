package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public interface ExpressionSNode extends SNode {

	List<String> getRhsIdentifiers();

	List<String> getRhsOperators();

	List<String> getRhsNumbers();

	List<String> getRhsVariableIdentifiers();

	List<MethodCall> getRhsCalls();

	List<String> getRhsCallIdentifiers();

	void setParent(SNode aParent);

}