package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.List;

public class AMethodCall implements MethodCall {
	String methodName;
	List<String> methodActuals;
	
	public AMethodCall (String aMethodName, List<String> aMethodActuals) {
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

	
}
