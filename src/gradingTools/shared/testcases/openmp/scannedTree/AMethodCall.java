package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public class AMethodCall extends AnSNode implements MethodCall {
	String methodName;
	List<String> methodActuals;
//	int lineNumber;
	
	

	public AMethodCall (int aLineNumber, String aMethodName, List<String> aMethodActuals) {
		super(aLineNumber);;
		methodName = aMethodName;
		methodActuals = aMethodActuals;
	}
	
	@Override
	public String getMethodName() {
		return methodName;
	}

	@Override
	public List<String> getMethodActuals() {
		return methodActuals;
	}
	
	public String toString() {
		return methodName + methodActuals;
	}
	@Override
	public void setParent(SNode parent) {
		super.setParent(parent);
		MethodSNode aMethodSNode = OMPSNodeUtils.getDeclarationOfCalledMethod(this, this);
		if (aMethodSNode != null) {
			aMethodSNode.getCallsToMe().add(this);
		}


	}
//	public int getLineNumber() {
//		return lineNumber;
//	}
	
}
