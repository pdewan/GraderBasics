package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public class AMethodCall extends AnSNode implements MethodCall {
	String methodName;
	List<String> methodActuals;
//	int lineNumber;
	
	

	public AMethodCall (int aLineNumber, String aMethodName, List<String> aMethodActuals, SNode aParent) {
		super(aLineNumber);;
		methodName = aMethodName;
		methodActuals = aMethodActuals;
		parent = aParent; // not calling setParent, because we may not be child of assignment node, considered a leaf, probably a mistake.

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
		if (aMethodSNode != null && !(aMethodSNode instanceof ExternalMethodSNode)) {
			aMethodSNode.getCalls().add(this);
		} else if (aMethodSNode != null ) {
			((ExternalMethodSNode) aMethodSNode).getLocalCalls().add(this);
		}


	}
//	public int getLineNumber() {
//		return lineNumber;
//	}
	
}
