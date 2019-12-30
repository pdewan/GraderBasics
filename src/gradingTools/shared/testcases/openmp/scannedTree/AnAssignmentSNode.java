package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AnAssignmentSNode extends AnSNode implements AssignmentSNode {
	String lhs;
	String[] lhsSubscripts;
	String lhsVariable;
	List<String> lhsOperators;
	String operationAndRHS;
	ExpressionSNode expressionSNode;
//	List<String> rhsVariableIdentifiers;
//	List<String> rhsOperators;
//	List<String> rhsNumbers;
//	List<MethodCall> rhsCalls;
	
//	List<String> rhsCallIdentifiers;
	
	
	public AnAssignmentSNode(int aLineNumber, String lhs, String operationAndRHS) {
		super(aLineNumber);
		this.lhs = lhs;
		this.operationAndRHS = operationAndRHS;
		lhsVariable = OMPSNodeUtils.identifiersIn(lhs).get(0);
		lhsOperators =  OMPSNodeUtils.operatorsIn(lhs);		
		lhsSubscripts = OMPSNodeUtils.subscriptsIn(lhs);
		expressionSNode = new AnExpressionSNode(aLineNumber, operationAndRHS);
//		expressionSNode.setParent(this);
//		rhsVariableIdentifiers = OMPSNodeUtils.identifiersIn(operationAndRHS);
//		rhsNumbers = OMPSNodeUtils.numbersIn(operationAndRHS);
//		rhsOperators = OMPSNodeUtils.operatorsIn(operationAndRHS);	
//		rhsCalls = OMPSNodeUtils.callsIn(aLineNumber, operationAndRHS, this);
//		if (rhsCalls == null) {
//			return;
//		}
//		rhsCallIdentifiers = new ArrayList();
//		for (MethodCall aMethodCall:rhsCalls) {
//			MethodSNode aMethodSNode = OMPSNodeUtils.getDeclarationOfCalledMethod(this, aMethodCall);
//			if (aMethodSNode != null) {
//				aMethodSNode.getCallsToMe().add(aMethodCall);
//			}
//			String aMethodName = aMethodCall.getMethodName();
//			rhsCallIdentifiers.add(aMethodName);
//			if (rhsVariableIdentifiers.contains(aMethodName)) {
//				rhsVariableIdentifiers.remove(aMethodName);
//			}
//		}
		
	}
//	protected void setCalledMethodData() {
//		if (rhsCalls == null) {
//			return;
//		}
//		rhsCallIdentifiers = new ArrayList();
//		for (MethodCall aMethodCall:rhsCalls) {
//			MethodSNode aMethodSNode = OMPSNodeUtils.getDeclarationOfCalledMethod(this, aMethodCall);
//			if (aMethodSNode != null) {
//				aMethodSNode.getCalls().add(aMethodCall);
//			}
//			String aMethodName = aMethodCall.getMethodName();
//			rhsCallIdentifiers.add(aMethodName);
//			if (rhsVariableIdentifiers.contains(aMethodName)) {
//				rhsVariableIdentifiers.remove(aMethodName);
//			}
//		}
//	}
	@Override
	public String getLHS() {
		return lhs;
	}
	@Override
	public String getOperationAndRHS() {
		return operationAndRHS;
	}
	public String[] getLhsSubscripts() {
		return lhsSubscripts;
	}
	public String getVariableName() {
		return lhsVariable;
	}
	public List<String> getLhsOperators() {
		return lhsOperators;
	}
	public List<String> getRhsIdentifiers() {
//		return rhsVariableIdentifiers;
		return expressionSNode.getRhsIdentifiers();
	}
	public List<String> getRhsOperators() {
//		return rhsOperators;
		return expressionSNode.getRhsOperators();
	}
	public List<String> getRhsNumbers() {
//		return rhsNumbers;
		return expressionSNode.getRhsNumbers();
	}
	@Override
	public List<String> getRhsVariableIdentifiers() {
		return expressionSNode.getRhsVariableIdentifiers();
//		return rhsVariableIdentifiers;
	}
	@Override
	public List<MethodCall> getRhsCalls() {
//		return rhsCalls;
		return expressionSNode.getRhsCalls();
	}
	@Override
	public List<String> getRhsCallIdentifiers() {
//		return rhsCallIdentifiers;
		return expressionSNode.getRhsCallIdentifiers();
	}
	@Override
	public String getLhsVariable() {
		return lhsVariable;
	}
	@Override
	public void setParent (SNode aParent) {
		super.setParent(aParent);
		expressionSNode.setParent(this);
//		setCalledMethodData();
	}
	@Override
	public ExpressionSNode getExpressionSNode() {
		return expressionSNode;
	}

}
