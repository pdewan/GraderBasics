package grader.basics.execution;

public class AResultWithOutput implements ResultWithOutput {
	
	Object result;
	String output;
	
	

	public AResultWithOutput(Object result, String output) {
		super();
		this.result = result;
		this.output = output;
	}

	@Override
	public Object getResult() {
		return result;
	}

	@Override
	public String getOutput() {
		// TODO Auto-generated method stub
		return output;
	}
	

}
