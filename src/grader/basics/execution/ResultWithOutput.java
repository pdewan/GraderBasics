package grader.basics.execution;

public interface ResultWithOutput {
	Object getResult();
	String getOutput();
	String getError();
	void setResult(Object result);

}
