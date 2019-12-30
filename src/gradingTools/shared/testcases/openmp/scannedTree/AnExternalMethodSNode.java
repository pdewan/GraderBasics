package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;

import gradingTools.shared.testcases.openmp.Assignment;

public class AnExternalMethodSNode extends AMethodSNode implements ExternalMethodSNode {

	MethodSNode actualMethodSNode;
	
	public AnExternalMethodSNode(int aLineNumber, String aMethodType, String aMethodName, List<DeclarationSNode> aLocalVariableDeclarations) {
		super(aLineNumber, aMethodType, aMethodName, aLocalVariableDeclarations);
	}
	@Override
	public MethodSNode getActualMethodSNode() {
		if (actualMethodSNode == null) {
			System.err.println("actual method is null. premature call to getCallsToMe?");
		}
		return actualMethodSNode;
	}
	@Override
	public void setActualMethodSNode(MethodSNode actualMethodSNode) {
		this.actualMethodSNode = actualMethodSNode;
	}
	
	@Override
	public List<MethodCall> getLocalCalls() {
		
		return callsToMe;
	}
	public List<MethodCall> getCalls() {
	
		return getActualMethodSNode() .getCalls();
	}
	@Override
	public List<SNode> getChildren() {
		return getActualMethodSNode().getChildren();
	}
	
	
	@Override
	public List<Assignment> getAssignments() {
		return getActualMethodSNode().getAssignments();
	}
	// are the two method below the same? Or is a local variable something that is declared and not shared?
	@Override
	public List<DeclarationSNode> getVariableDeclarations() {
		return getActualMethodSNode().getVariableDeclarations();
	}
//	@Override
//	public List<String> getLocalVariables() {
////		return getActualMethodSNode().getLocalVariables();
//	}
	@Override
	public List<OMPSNode> getOmpSNodes() {
		return getActualMethodSNode().getOmpSNodes();
	}
	@Override
	public int getLineNumber() {
		return getActualMethodSNode().getLineNumber();
	}

//	public String toString() {
//		return methodName + " " + variableDeclarations;
//	}
}
