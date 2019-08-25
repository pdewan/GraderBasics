package gradingTools.interpreter.checkers;


public interface InterpretedChecker {
//	public boolean isExpandFiles();
	public int getNumArgs();
	public InterpretedCheckerResult check (String[] anArgs);

}
