package gradingTools.shared.testcases.openmp;

public class AnOpenMPCriticalPragma extends AnOpenMPPragma implements OpenMPCriticalPragma {
//	protected String assignedVariableInCritical;// separate subclass for this variable?
//	protected String assignedExpressionInCritical;// separate subclass for this variable?


	
	public AnOpenMPCriticalPragma(int lineNumber) {
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
