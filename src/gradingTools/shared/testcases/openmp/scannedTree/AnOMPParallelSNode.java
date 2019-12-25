package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;

public class AnOMPParallelSNode extends AnOMPSNode {
//	protected List<String> variableDeclarations = new ArrayList();//separate subclass for this variable?
//	protected List<String> localVariables = new ArrayList();//separate subclass for this variable?


	public AnOMPParallelSNode(int lineNumber) {
		super(lineNumber);
		// TODO Auto-generated constructor stub
	}
//	@Override
//	public void addChild(SNode anSNode) {
//		if (anSNode instanceof AssignmentSNode) {
//			
//		}
//		
//		if (OpenMPUtils.startsWithTypeName(aFileLine)) {
//				String[] aTokens = aFileLine.split("\\s+");						
//				getVariableDeclarations().add(aFileLine);
//				getLocalVariables().add(aTokens[1]);
//		}
//		 
//	}
//	@Override
//	public List<String> getVariableDeclarations() {
//		return variableDeclarations;
//	}
//	@Override
//	public List<String> getLocalVariables() {
//		return localVariables;
//	}
}
