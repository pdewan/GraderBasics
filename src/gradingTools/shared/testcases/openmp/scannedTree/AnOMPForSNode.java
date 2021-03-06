package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;

public class AnOMPForSNode extends AnOMPSNode implements OMPForSNode  {
	protected String reductionVariable;
	protected String reductionOperation;
	protected List<AssignmentSNode> reductionVariableAssignments = new ArrayList();
	protected List<String> reductionOperationUses = new ArrayList();
	protected Boolean indexVariableNotShared;
//	protected boolean inParallel;
	
	public AnOMPForSNode(int lineNumber) {
		super(lineNumber);
	}
	@Override
	public void addChild(SNode aChild) {
		super.addChild(aChild);
		if (aChild instanceof ForSNode) {
			ForSNode aForSNode = (ForSNode) aChild;
			AssignmentSNode anAssignmentSNode = aForSNode.getInitalization();
			if (anAssignmentSNode != null) {
				 indexVariableNotShared = 
						 (anAssignmentSNode instanceof DeclaringAssignmentSNode) ?
								true:
								OMPSNodeUtils.isSharedVariable(this, anAssignmentSNode.getLHS());
			}
			
		}
		
		
//		if (reductionVariable != null) {
//			if (aChild instanceof AssignmentSNode) {
//				AssignmentSNode anAssignmentSNode = (AssignmentSNode) aChild;
//				getReductionVariableAssignments().add(anAssignmentSNode);
//				if (reductionOperation != null) { // can it ever be not null
//					if (anAssignmentSNode.getOperationAndRHS().contains(reductionOperation)) {
//						getReductionOperationUses().add(anAssignmentSNode.getOperationAndRHS());
//					}
//				}
//			}			
//		}
	}
	@Override
	public String getReductionOperation() {
		return reductionOperation;
	}
	@Override
	public void setReductionOperation(String reductionOperation) {
		this.reductionOperation = reductionOperation;
	}
	@Override
	public List<AssignmentSNode> getReductionVariableAssignments() {
		return reductionVariableAssignments;
	}
	@Override
	public List<String> getReductionOperationUses() {
		return reductionOperationUses;
	}
	@Override
	public String getReductionVariable() {
		return reductionVariable;
	}
	@Override
	public void setReductionVariable(String reductionVariable) {
		this.reductionVariable = reductionVariable;
	}
	@Override
	public void setParent (SNode aParent) {
		super.setParent(aParent);
	}
}
