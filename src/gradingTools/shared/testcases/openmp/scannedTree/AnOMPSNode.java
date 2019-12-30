package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.List;

import gradingTools.shared.testcases.openmp.Assignment;
import gradingTools.shared.testcases.openmp.ForHeader;
import gradingTools.shared.testcases.openmp.OpenMPPragma;

public class AnOMPSNode extends AnSNode implements OMPSNode {
	protected static String[] emptyStringArray = {};
	protected List<String> openMPTokens = new ArrayList();

	protected List<String> linesWithOpenMPCalls = new ArrayList();
	
	protected OMPSNode ompParent;
	protected List<OMPSNode> ompChildren = new ArrayList();


	
	protected String[] privateVariables = emptyStringArray; // can occur in for and parallel
	
	
	protected String[]  sharedVariables = emptyStringArray ; // can occur only in parallel, but let us keep them here
	
	public AnOMPSNode(int lineNumber) {
		super(lineNumber);
	}
	@Override
	public OMPSNode getOmpParent() {
		return ompParent;
	}
	@Override
	public void setOmpParent(OMPSNode ompParent) {
		this.ompParent = ompParent;
	}
	@Override
	public List<String> getOpenMPTokens() {
		return openMPTokens;
	}
	@Override
	public void setOpenMPTokens(List<String> openMPTokens) {
		this.openMPTokens = openMPTokens;
	}
	@Override
	public List<String> getLinesWithOpenMPCalls() {
		return linesWithOpenMPCalls;
	}
	@Override
	public List<OMPSNode> getOmpChildren() {
		return ompChildren;
	}
	@Override
	public String[] getPrivateVariables() {
		return privateVariables;
	}
	@Override
	public String[] getSharedVariables() {
		return sharedVariables;
	}
	@Override
	public void setPrivateVariables(String[] privateVariables) {
		this.privateVariables = privateVariables;
	}
	@Override

	public void setSharedVariables(String[] sharedVariables) {
		this.sharedVariables = sharedVariables;
	}
//	@Override
//	public SNode getParent() {
//		return parent;
//	}
//	@Override
//	public void setParent(SNode parent) {
//		this.parent = parent;
//	}
//	@Override
//	public List<SNode> getChildren() {
//		return children;
//	}
}
