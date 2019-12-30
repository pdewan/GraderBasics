package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AnExpressionSNode extends AnSNode implements ExpressionSNode  {

	String operationAndRHS;
	List<String> rhsVariableIdentifiers;
	List<String> rhsOperators;
	List<String> rhsNumbers;
	List<MethodCall> rhsCalls;
	
	List<String> rhsCallIdentifiers;
	
	
	public AnExpressionSNode(int aLineNumber,  String operationAndRHS) {
		super(aLineNumber);
		
		rhsVariableIdentifiers = OMPSNodeUtils.identifiersIn(operationAndRHS);
		rhsNumbers = OMPSNodeUtils.numbersIn(operationAndRHS);
		rhsOperators = OMPSNodeUtils.operatorsIn(operationAndRHS);	
		rhsCalls = OMPSNodeUtils.callsIn(aLineNumber, operationAndRHS, this);
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
	protected void setCalledMethodData() {
		if (rhsCalls == null) {
			return;
		}
		rhsCallIdentifiers = new ArrayList();
		for (MethodCall aMethodCall:rhsCalls) {
			MethodSNode aMethodSNode = OMPSNodeUtils.getDeclarationOfCalledMethod(this, aMethodCall);
			if (aMethodSNode != null && !(aMethodSNode instanceof ExternalMethodSNode)) {
				aMethodSNode.getCalls().add(aMethodCall);
			} else if (aMethodSNode != null) {
				((ExternalMethodSNode) aMethodSNode).getLocalCalls().add(aMethodCall);
			}
			String aMethodName = aMethodCall.getMethodName();
			rhsCallIdentifiers.add(aMethodName);
			if (rhsVariableIdentifiers.contains(aMethodName)) {
				rhsVariableIdentifiers.remove(aMethodName);
			}
		}
	}
	
	@Override
	public List<String> getRhsIdentifiers() {
		return rhsVariableIdentifiers;
	}
	@Override
	public List<String> getRhsOperators() {
		return rhsOperators;
	}
	@Override
	public List<String> getRhsNumbers() {
		return rhsNumbers;
	}
	@Override
	public List<String> getRhsVariableIdentifiers() {
		return rhsVariableIdentifiers;
	}
	@Override
	public List<MethodCall> getRhsCalls() {
		return rhsCalls;
	}
	@Override
	public List<String> getRhsCallIdentifiers() {
		return rhsCallIdentifiers;
	}
	
	@Override
	public void setParent (SNode aParent) {
		// parent does not have to see me as a child
//		super.setParent(aParent); 
		parent = aParent;
		setCalledMethodData();
	}

}
