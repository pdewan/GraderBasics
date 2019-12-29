package gradingTools.shared.testcases.openmp.scannedTree;

public interface RootOfFileSNode extends SNode{

	MethodSNode getOmp_get_thread_num_SNode();

	MethodSNode getOmp_get_num_threads_SNode();

	String getFileName();

}