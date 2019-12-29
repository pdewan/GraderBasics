package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.Map;

public interface RootOfProgramSNode extends SNode{

	Map<String, RootOfFileSNode> getFileNameToSNode();

	Map<String, MethodSNode> getExternalToInternalMethod();

}