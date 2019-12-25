package gradingTools.shared.testcases.openmp.scannedTree;

public class AnOMPCriticalSNode extends AnOMPSNode {


	
	public AnOMPCriticalSNode(int lineNumber) {
		super(lineNumber);
	}
//	@Override
//	public void addToAnnotatedText(String aFileLine, int aLineNumber) {
//		super.addToAnnotatedText(aFileLine, aLineNumber);
//		String[] aTokens = aFileLine.split("\\s+");
//		setAssignedVariableInCritical(aTokens[0]);
//		
////		String anExpressionString = aFileLine.substring(aFileLine.indexOf("=") + 1, aFileLine.length());
//		String anExpressionString = aFileLine.substring(aTokens[0].length(), aFileLine.length()); // keep the assignment operation
//
//		setAssignedExpressionInCritical(anExpressionString);
//	}
//	@Override
//	public String getAssignedVariableInCritical() {
//		return assignedVariableInCritical;
//	}
//	@Override
//	public void setAssignedVariableInCritical(String assignedVariableInCritical) {
//		this.assignedVariableInCritical = assignedVariableInCritical;
//	}
//	@Override
//	public String getAssignedExpressionInCritical() {
//		return assignedExpressionInCritical;
//	}
//	@Override
//	public void setAssignedExpressionInCritical(String assignedExpressionInCritical) {
//		this.assignedExpressionInCritical = assignedExpressionInCritical;
//	}
}
