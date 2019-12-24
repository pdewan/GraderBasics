package gradingTools.shared.testcases.openmp;

import java.util.ArrayList;
import java.util.List;

public class AnOpenMPParallelPragma extends AnOpenMPPragma implements OpenMPParallelPragma{
//	protected List<String> variableDeclarations = new ArrayList();//separate subclass for this variable?
//	protected List<String> localVariables = new ArrayList();//separate subclass for this variable?


	public AnOpenMPParallelPragma(int lineNumber) {
		super(lineNumber);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void addToAnnotatedText(String aFileLine, int aLineNumber) {
		super.addToAnnotatedText(aFileLine, aLineNumber);
		
		if (OpenMPUtils.startsWithTypeName(aFileLine)) {
				String[] aTokens = aFileLine.split("\\s+");						
				getVariableDeclarations().add(aFileLine);
				getLocalVariables().add(aTokens[1]);
		}
		 
	}
//	@Override
//	public List<String> getVariableDeclarations() {
//		return variableDeclarations;
//	}
//	@Override
//	public List<String> getLocalVariables() {
//		return localVariables;
//	}
}
