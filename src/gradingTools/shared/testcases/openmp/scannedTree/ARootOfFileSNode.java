package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;

public class ARootOfFileSNode extends AnSNode implements RootOfFileSNode{
	String fileName;
	ExternalMethodSNode  omp_get_thread_num_SNode;	
	ExternalMethodSNode omp_get_num_threads_SNode;
	ExternalMethodSNode omp_get_wtime_SNode;
	public ARootOfFileSNode(String aFileName) {
		super(0);
		fileName = aFileName;
		omp_get_thread_num_SNode = new AnOMPPredefinedExternalMethodSNode("int", "omp_get_thread_num");
		omp_get_num_threads_SNode = new AnOMPPredefinedExternalMethodSNode("int", "omp_get_num_threads");
		omp_get_wtime_SNode = new AnOMPPredefinedExternalMethodSNode("int", "omp_get_wtime");
		omp_get_thread_num_SNode.setParent(this);
		omp_get_num_threads_SNode.setParent(this);
		omp_get_wtime_SNode.setParent(this);
		
		// TODO Auto-generated constructor stub
	}
	@Override
	public ExternalMethodSNode getOmp_get_thread_num_SNode() {
		return omp_get_thread_num_SNode;
	}
	@Override
	public ExternalMethodSNode getOmp_get_num_threads_SNode() {
		return omp_get_num_threads_SNode;
	}
	@Override
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setParent(SNode aParent) {
		super.setParent(aParent);
//		omp_get_thread_num_SNode.setParent(this);
//		omp_get_num_threads_SNode.setParent(this);
	}
	@Override
	public ExternalMethodSNode getOmp_get_wtime_SNode() {
		return omp_get_wtime_SNode;
	}
}
