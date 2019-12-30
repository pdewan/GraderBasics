package gradingTools.shared.testcases.openmp.scannedTree;

public class AForSNode extends AnSNode implements ForSNode {
	AssignmentSNode initalization;
	String condition;	
	ExpressionSNode conditionSNode;
	AssignmentSNode increment;
	public AForSNode(int lineNumber, AssignmentSNode initalization, String condition, AssignmentSNode increment) {
		super(lineNumber);
		this.initalization = initalization;
		this.condition = condition;
		this.increment = increment;
		if (initalization instanceof DeclarationSNode) {
			variableDeclarations.add((DeclarationSNode) initalization);
			localVariables.add(initalization.getVariableName());
		}
		conditionSNode = new AnExpressionSNode(lineNumber, condition);
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
	public AssignmentSNode getIncrement() {
		return increment;
	}
	@Override
	public void setParent(SNode parent) {
		super.setParent(parent);
		conditionSNode.setParent(this);
	}
	@Override
	public ExpressionSNode getConditionSNode() {
		return conditionSNode;
	}
}
