package gradingTools.shared.testcases.openmp.scannedTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gradingTools.shared.testcases.openmp.AnAssignment;
import gradingTools.shared.testcases.openmp.Assignment;
import gradingTools.shared.testcases.openmp.ForHeader;
import gradingTools.shared.testcases.openmp.OpenMPPragma;
import gradingTools.shared.testcases.openmp.OpenMPUtils;
import gradingTools.shared.testcases.openmp.checks.OpenMPPragmaAttribute;

public class AnSNode implements SNode {
	protected List<DeclarationSNode> variableDeclarations = new ArrayList();//separate subclass for this variable?
//	protected List<String> localVariables = new ArrayList();//separate subclass for this variable?
	protected List<String> localVariables = new ArrayList();//separate subclass for this variable?
	protected int lineNumber;
	protected List<SNode> children = new ArrayList();
	protected List<Assignment> assignments = new ArrayList();
	protected List<ForSNode> forNodes = new ArrayList();
	protected List<OMPSNode> ompSNodes = new ArrayList();
	protected Set<OpenMPPragmaAttribute> attributes = new HashSet();
	protected SNode parent;
	boolean inParallel = false;
	boolean inCritical = false;

	int numberOfNestingFors;

	public AnSNode(int lineNumber) {
		super();
		this.lineNumber = lineNumber;		
	}
	
	@Override
	public int getLineNumber() {
		return lineNumber;
	}
	
	@Override
	public List<ForSNode> getForNodes() {
		return forNodes;
	}
	//	@Override
//	public void setAnnotatedText(List<String> nextNonEmptyString) {
//		this.annotatedText = nextNonEmptyString;
//	}
	
	@Override
	public SNode getParent() {
		return parent;
	}
	@Override
	public void setParent(SNode parent) {
		this.parent = parent;
		parent.getChildren().add(this);
		inParallel = OMPSNodeUtils.hasParallelAncestor(this);		
		inCritical = OMPSNodeUtils.hasCriticalAncestor(this);	
		numberOfNestingFors = OMPSNodeUtils.numberOfNestingFors(parent);


	}
	@Override
	public List<SNode> getChildren() {
		return children;
	}
	
	
	@Override
	public List<Assignment> getAssignments() {
		return assignments;
	}
	// are the two method below the same? Or is a local variable something that is declared and not shared?
	@Override
	public List<DeclarationSNode> getVariableDeclarations() {
		return variableDeclarations;
	}
	@Override
	public List<String> getLocalVariables() {
		return localVariables;
	}
	@Override
	public List<OMPSNode> getOmpSNodes() {
		return ompSNodes;
	}
	
	@Override
	public void addChild(SNode aChild) {
		this.getChildren().add(aChild);
//		if (aChild instanceof DeclaringAssignmentSNode) {
//			localVariables.add(((DeclaringAssignmentSNode) aChild).getLhsVariable());
//		}
	}
	@Override
	public Set<OpenMPPragmaAttribute> getAttributes() {
		return attributes;
	}
	@Override
	public boolean isInParallel() {
		return inParallel;
	}
	@Override
	public boolean isInCritical() {
		return inCritical;
	}
	@Override
	public boolean isLeaf() {
		return getChildren().isEmpty();
	}

}
