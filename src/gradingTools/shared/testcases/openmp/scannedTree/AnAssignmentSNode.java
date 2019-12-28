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
	List<String> rhsVariableIdentifiers;
	List<String> rhsOperators;
	List<String> rhsNumbers;
	List<MethodCall> rhsCalls;
	
	List<String> rhsCallIdentifiers;
	
	
	public AnAssignmentSNode(int aLineNumber, String lhs, String operationAndRHS) {
		super(aLineNumber);
		this.lhs = lhs;
		this.operationAndRHS = operationAndRHS;
		lhsVariable = OMPSNodeUtils.identifiersIn(lhs).get(0);
		lhsOperators =  OMPSNodeUtils.operatorsIn(lhs);		
		lhsSubscripts = OMPSNodeUtils.subscriptsIn(lhs);
		rhsVariableIdentifiers = OMPSNodeUtils.identifiersIn(operationAndRHS);
		rhsNumbers = OMPSNodeUtils.numbersIn(operationAndRHS);
		rhsOperators = OMPSNodeUtils.operatorsIn(operationAndRHS);	
		rhsCalls = OMPSNodeUtils.callsIn(operationAndRHS);
		if (rhsCalls == null) {
			return;
		}
		rhsCallIdentifiers = new ArrayList();
		for (MethodCall aMethodCall:rhsCalls) {
			String aMethodName = aMethodCall.getMethodName();
			rhsCallIdentifiers.add(aMethodName);
			if (rhsVariableIdentifiers.contains(aMethodName)) {
				rhsVariableIdentifiers.remove(aMethodName);
			}
		}
		
	}
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
		return rhsVariableIdentifiers;
	}
	public List<String> getRhsOperators() {
		return rhsOperators;
	}
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
	public String getLhsVariable() {
		return lhsVariable;
	}

}
