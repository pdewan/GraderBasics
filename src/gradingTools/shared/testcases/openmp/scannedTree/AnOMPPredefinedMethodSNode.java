package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;

public class AnOMPPredefinedMethodSNode extends AMethodSNode {
	protected static List<DeclarationSNode> emptyVariableDeclarations = new ArrayList();


	public AnOMPPredefinedMethodSNode(String aMethodType, String aMethodName) {
		super(-1, aMethodType, aMethodName, emptyVariableDeclarations);
	}

}
