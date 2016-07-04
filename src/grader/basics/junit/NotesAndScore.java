package grader.basics.junit;

public class NotesAndScore {
	public final String notes;
	public final double percentage;
	public NotesAndScore (String aNotes, double aPercentage) {
		notes = aNotes;
		percentage = aPercentage;
		
	}
	public static NotesAndScore create (String aMessage) {
		String[] aNotesAndScore = aMessage.split(":");
		String aNotes = aNotesAndScore[0];
		double aPercentage = 0;
		if (aNotesAndScore.length == 1) { // assume zero percentage
			aPercentage = 0;
		} else {
			String aPercentageString = aNotesAndScore[aNotesAndScore.length - 1].trim();
			try {
			aPercentage = Double.parseDouble(aPercentageString);

			} catch (Exception e) {
				aPercentage = 0;
				
			}
			
		}
		return new NotesAndScore(aNotes, aPercentage);
	}

}
