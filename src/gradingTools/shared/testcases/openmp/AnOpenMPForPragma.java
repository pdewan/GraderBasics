package gradingTools.shared.testcases.openmp;

import java.util.ArrayList;
import java.util.List;

public class AnOpenMPForPragma extends AnOpenMPPragma implements OpenMPForPragma {
	protected String reductionVariable;
	protected String reductionOperation;
	protected List<String> reductionVariableAssignments = new ArrayList();
	protected List<String> reductionOperationUses = new ArrayList();
	
	public AnOpenMPForPragma(int lineNumber) {
		super(lineNumber);
	}
	@Override
	public void addToAnnotatedText(String aFileLine, int aLineNumber) {
		super.addToAnnotatedText(aFileLine, aLineNumber);
		if (reductionVariable != null) {
			if (aFileLine.startsWith(reductionVariable)) {
				getReductionVariableAssignments().add(aFileLine);
				if (reductionOperation != null) { // can it ever be not null
					if (aFileLine.contains(reductionOperation)) {
						getReductionOperationUses().add(aFileLine);
					}
				}
			}			
		}
	}
	@Override
	public String getReductionOperation() {
		return reductionOperation;
	}
	@Override
	public void setReductionOperation(String reductionOperation) {
		this.reductionOperation = reductionOperation;
	}
	@Override
	public List<String> getReductionVariableAssignments() {
		return reductionVariableAssignments;
	}
	@Override
	public List<String> getReductionOperationUses() {
		return reductionOperationUses;
	}
	@Override
	public String getReductionVariable() {
		return reductionVariable;
	}
	@Override
	public void setReductionVariable(String reductionVariable) {
		this.reductionVariable = reductionVariable;
	}

}
