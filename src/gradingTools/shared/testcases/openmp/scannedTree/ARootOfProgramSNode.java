package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.HashMap;
import java.util.Map;

public class ARootOfProgramSNode extends AnSNode implements RootOfProgramSNode{
	MethodSNode  omp_get_thread_num_SNode;
	
	MethodSNode omp_get_num_threads_SNode;
	MethodSNode omp_get_wtime_SNode;
	Map<String, RootOfFileSNode> fileNameToSNode = new HashMap<>();
	
	Map<String, MethodSNode> externalToInternalMethod = new HashMap();
	public ARootOfProgramSNode() {
		super(-1);
		omp_get_thread_num_SNode = new AnOMPPredefinedMethodSNode("int", "omp_get_thread_num");
		omp_get_num_threads_SNode = new AnOMPPredefinedMethodSNode("int", "omp_get_num_threads");
		omp_get_wtime_SNode = new AnOMPPredefinedMethodSNode("int", "omp_get_wtime");
	}
	@Override
	public Map<String, RootOfFileSNode> getFileNameToSNode() {
		return fileNameToSNode;
	}
	@Override
	public Map<String, MethodSNode> getExternalToInternalMethod() {
		return externalToInternalMethod;
	}
	@Override
	public MethodSNode getOmp_get_thread_num_SNode() {
		return omp_get_thread_num_SNode;
	}
	@Override
	public MethodSNode getOmp_get_num_threads_SNode() {
		return omp_get_num_threads_SNode;
	}
	@Override
	public MethodSNode getOmp_get_wtime_SNode() {
		return omp_get_wtime_SNode;
	}

}
