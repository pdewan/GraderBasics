package gradingTools.shared.testcases.openmp;

public class AForHeader implements ForHeader{
	String initialization;
	String conditon;
	String increment;
	int lineNumber;
	public int getLineNumber() {
		return lineNumber;
	}
	public AForHeader(String initialization, String conditon, String increment, int aLineNumber) {
		super();
		this.initialization = initialization;
		this.conditon = conditon;
		this.increment = increment;
		lineNumber = aLineNumber;
	}
	public String getInitialization() {
		return initialization;
	}
	public String getConditon() {
		return conditon;
	}
	public String getIncrement() {
		return increment;
	}
	

}
