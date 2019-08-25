package gradingTools.interpreter.checkers;

public class ACheckerResult implements InterpretedCheckerResult {
	String notes = "";
	boolean succeeded;
	
	public ACheckerResult(String notes, boolean succeeded) {
		super();
		this.notes = notes;
		this.succeeded = succeeded;
	}
	public String getNotes() {
		return notes;
	}
	
	public boolean isSucceeded() {
		return succeeded;
	}
	
	

}
