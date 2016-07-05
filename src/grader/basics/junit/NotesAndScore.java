package grader.basics.junit;

public class NotesAndScore {
	public static final char PERECTAGE_CHARACTER = '%';
	public final String notes;
	public final double percentage;
	public NotesAndScore (String aNotes, double aPercentage) {
		notes = aNotes;
		percentage = aPercentage;
		
	}
	public static NotesAndScore create (String aMessage) {
		String aNotes = aMessage;
		double aPercentage = 0.0;
		int aPercentageIndex = aMessage.lastIndexOf(PERECTAGE_CHARACTER);
		if (aPercentageIndex >= 0) {
		
			String aPercentageString = aMessage.substring(aPercentageIndex + 1).trim();
			try {
				aPercentage = Double.parseDouble(aPercentageString);
				aNotes = aMessage.substring(0, aPercentageIndex);

				} catch (Exception e) {
					
					
				}			
			
		}		
			
		
		return new NotesAndScore(aNotes, aPercentage);
	}

}
