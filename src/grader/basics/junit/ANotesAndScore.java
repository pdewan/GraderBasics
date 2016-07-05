package grader.basics.junit;

public class ANotesAndScore implements NotesAndScore {
	String notes;
	double percentage;
	
	public ANotesAndScore (String aNotes, double aPercentage) {
		notes = aNotes;
		percentage = aPercentage;
		
	}
	
	@Override
	public String getNotes() {
		return notes;
	}
	@Override
	public void setNotes(String notes) {
		this.notes = notes;
	}
	@Override
	public double getPercentage() {
		return percentage;
	}
	@Override
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
}
