package gradingTools.shared.testcases.openmp.scannedTree;

public class AnOMPCriticalSNode extends AnOMPSNode implements OMPCriticalSNode {
	boolean assignedVariableShared = false;

	
	public AnOMPCriticalSNode(int lineNumber) {
		super(lineNumber);
	}

	@Override
	public boolean isAssignedVariableShared() {
		return assignedVariableShared;
	}

	@Override
	public void setParent (SNode aParent) {
		super.setParent(aParent);
		inParallel = OMPSNodeUtils.hasParallelAncestor(this);
	}
	@Override
	public void addChild(SNode aChild) {
		super.addChild(aChild);
		if (aChild instanceof AssignmentSNode && !assignedVariableShared) { // do not override a shared variable assignment, there might be multiple
			AssignmentSNode anAssignmentSNode = (AssignmentSNode) aChild;
			String anLHS = anAssignmentSNode.getLHS();
			String anRHS = anAssignmentSNode.getOperationAndRHS();
			assignedVariableShared = OMPSNodeUtils.isSharedVariable(this, anLHS);
		}
	}

}
