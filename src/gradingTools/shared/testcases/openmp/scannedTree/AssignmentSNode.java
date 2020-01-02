package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public interface AssignmentSNode extends SNode {

	String getLHS();

	String getOperationAndRHS();
	public String[] getLhsSubscripts();
	public String getVariableName() ;
	public List<String> getLhsOperators() ;
	public List<String> getRhsIdentifiers() ;
	public List<String> getRhsOperators() ;
	public List<String> getRhsNumbers() ;

	List<String> getRhsVariableIdentifiers();

	List<MethodCall> getRhsCalls();

	List<String> getRhsCallIdentifiers();

	String getLhsFirstIdentifier();

	ExpressionSNode getExpressionSNode();
}
