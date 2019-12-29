package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;

public class AMethodSNode extends AnSNode implements MethodSNode {
	String methodType;
	String methodName;
	List<MethodCall> callsToMe = new ArrayList();
//	String[] methodParameterTypes;
//	String[] methodParameterNames;
	
	
	
	public AMethodSNode(int lineNumber, String aMethodType, String aMethodName, List<DeclarationSNode> aLocalVariableDeclarations) {
		super(lineNumber);
		methodType = aMethodType;
		methodName = aMethodName;
//		methodParameterTypes = aMethodParameterTypes;
//		methodParameterNames = aMethodParameterNames;
		variableDeclarations = aLocalVariableDeclarations;
		for (DeclarationSNode aDeclarationSNode:variableDeclarations) {
			localVariables.add(aDeclarationSNode.getVariableName());
		}
	}
	@Override
	public String getMethodType() {
		return methodType;
	}
	@Override
	public String getMethodName() {
		return methodName;
	}
	@Override
	public List<MethodCall> getCallsToMe() {
		return callsToMe;
	}
	public String toString() {
		return methodName + " " + variableDeclarations;
	}
//	@Override
//	public String[] getMethodParameterNames() {
//		return methodParameterNames;
//	}
//	@Override
//	public String[] getMethodParameterTypes() {
//		return methodParameterTypes;
//	}
	
	
}
