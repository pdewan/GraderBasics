package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;

public class AnOMPPredefinedExternalMethodSNode extends AnExternalMethodSNode {
	protected static List<DeclarationSNode> emptyVariableDeclarations = new ArrayList();


	public AnOMPPredefinedExternalMethodSNode(String aMethodType, String aMethodName) {
		super(-1, aMethodType, aMethodName, emptyVariableDeclarations);
	}

}
