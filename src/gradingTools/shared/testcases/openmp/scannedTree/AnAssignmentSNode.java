package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AnAssignmentSNode extends AnSNode implements AssignmentSNode {
	static String[] emptyStringArray = {};
	String lhs;
	String[] lhsSubscripts = emptyStringArray;
	String lhsFirstIdentifier;
	List<String> lhsOperators;
	String operationAndRHS;
	ExpressionSNode expressionSNode;
	DeclarationSNode lhsFirstIdentifierDeclaration;
//	List<String> rhsVariableIdentifiers;
//	List<String> rhsOperators;
//	List<String> rhsNumbers;
//	List<MethodCall> rhsCalls;
	
//	List<String> rhsCallIdentifiers;
	
	

	public AnAssignmentSNode(int aLineNumber, String lhs, String operationAndRHS) {
		super(aLineNumber);
		this.lhs = lhs;
		this.operationAndRHS = operationAndRHS;
		lhsFirstIdentifier = OMPSNodeUtils.identifiersIn(lhs).get(0);
		lhsOperators =  OMPSNodeUtils.operatorsIn(lhs);		
		lhsSubscripts = OMPSNodeUtils.subscriptsIn(lhs);
		if (lhsSubscripts == null) {
			lhsSubscripts = emptyStringArray;
		}
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
		return lhsFirstIdentifier;
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
	public String getLhsFirstIdentifier() {
		return lhsFirstIdentifier;
	}
	@Override
	public void setParent (SNode aParent) {
		super.setParent(aParent);
		expressionSNode.setParent(this);
		DeclarationSNode aDeclarationSNode = OMPSNodeUtils.getDeclarationOfAssignedVariable(this, this);
		if (aDeclarationSNode == null) {
			System.err.println("Null declaration node for assignment:" + this);
		}
		aDeclarationSNode.getAssignmentsToDeclaredVariable().add(this);
		setLhsFirstIdentifierDeclaration(aDeclarationSNode);
//		setCalledMethodData();
	}
	@Override
	public ExpressionSNode getExpressionSNode() {
		return expressionSNode;
	}
	public String toString() {
		return lhs + "=" + operationAndRHS;
	}
	@Override
	public DeclarationSNode getLhsFirstIdentifierDeclaration() {
		return lhsFirstIdentifierDeclaration;
	}
	@Override
	public void setLhsFirstIdentifierDeclaration(DeclarationSNode lhsFirstIdentifierDeclaration) {
		this.lhsFirstIdentifierDeclaration = lhsFirstIdentifierDeclaration;
	}
	
}
