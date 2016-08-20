package grader.basics.junit;

public interface NotesAndScore {

	public static final  char PERCENTAGE_MARKER = '%';
	public abstract String getNotes();

	public abstract void setNotes(String notes);

	public abstract double getPercentage();

	public abstract void setPercentage(double percentage);
	public static ANotesAndScore create (String aMessage) {
		String aNotes = aMessage;
		double aPercentage = 0.0;
		int aPercentageIndex = aMessage.lastIndexOf(PERCENTAGE_MARKER);
		if (aPercentageIndex >= 0) {
		
			String aPercentageString = aMessage.substring(aPercentageIndex + 1).trim();
			try {
				aPercentage = Double.parseDouble(aPercentageString);
				aNotes = aMessage.substring(0, aPercentageIndex);

				} catch (Exception e) {
					
					
				}			
			
		}		
			
		
		return new ANotesAndScore(aNotes, aPercentage);
	}

}