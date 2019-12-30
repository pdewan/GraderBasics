package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.Map;

public interface RootOfProgramSNode extends SNode{

	Map<String, RootOfFileSNode> getFileNameToSNode();

	Map<String, MethodSNode> getExternalToInternalMethod();

	MethodSNode getOmp_get_thread_num_SNode();

	MethodSNode getOmp_get_num_threads_SNode();

	MethodSNode getOmp_get_wtime_SNode();

}