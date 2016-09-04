package grader.basics.execution;

public class AResultWithOutput implements ResultWithOutput {
	
	Object result;
	
	String output;
	String error = "";
	
	

	public AResultWithOutput(Object result, String output) {
		super();
		this.result = result;
		this.output = output;
	}
	public AResultWithOutput(Object result, String output, String error) {
		super();
		this.result = result;
		this.output = output;
		this.error = error;
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
	@Override
	public String getError() {
		return error;
	}
	@Override
	public void setResult(Object result) {
		this.result = result;
	}

}
