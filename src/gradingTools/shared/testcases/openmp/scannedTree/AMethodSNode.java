package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;

public class AMethodSNode extends AnSNode implements MethodSNode {
	String methodType;
	String methodName;
	List<MethodCall> callsToMe = new ArrayList();
	StringBuffer signature = new StringBuffer();
//	String[] methodParameterTypes;
//	String[] methodParameterNames;
	
	
	
	public AMethodSNode(int lineNumber, String aMethodType, String aMethodName, List<DeclarationSNode> aLocalVariableDeclarations) {
		super(lineNumber);
		methodType = aMethodType;
		methodName = aMethodName;
//		methodParameterTypes = aMethodParameterTypes;
//		methodParameterNames = aMethodParameterNames;
		variableDeclarations = aLocalVariableDeclarations;
		signature.append(methodName + "(");
		for (int i = 0; i < variableDeclarations.size(); i++) {
			DeclarationSNode aDeclarationSNode = variableDeclarations.get(i);					
			localVariables.add(aDeclarationSNode.getVariableIdentifier());
			signature.append(aDeclarationSNode.getTypeName());
			if (i < variableDeclarations.size() -1) {
				signature.append(", ");
			}
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
	public List<MethodCall> getCalls() {
		return callsToMe;
	}
	public String toString() {
		return signature.toString();
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
