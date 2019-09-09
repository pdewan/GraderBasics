package gradingTools.interpreter.checkers;


public interface InterpretedChecker {
//	public boolean isExpandFiles();
	public int getNumArgs();
	public CheckerResult check (String[] anArgs);

}
