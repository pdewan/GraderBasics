package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;

public class AnExternalMethodSNode extends AMethodSNode implements ExternalMethodSNode {

	MethodSNode actualMethodSNode;
	
	public AnExternalMethodSNode(int aLineNumber, String aMethodType, String aMethodName, List<DeclarationSNode> aLocalVariableDeclarations) {
		super(aLineNumber, aMethodType, aMethodName, aLocalVariableDeclarations);
	}
	@Override
	public MethodSNode getActualMethodSNode() {
		return actualMethodSNode;
	}
	@Override
	public void setActualMethodSNode(MethodSNode actualMethodSNode) {
		this.actualMethodSNode = actualMethodSNode;
	}
//	public String toString() {
//		return methodName + " " + variableDeclarations;
//	}
}
