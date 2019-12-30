package gradingTools.shared.testcases.openmp.scannedTree;

public interface RootOfFileSNode extends SNode{

	ExternalMethodSNode getOmp_get_thread_num_SNode();

	ExternalMethodSNode getOmp_get_num_threads_SNode();

	String getFileName();

	ExternalMethodSNode getOmp_get_wtime_SNode();

}