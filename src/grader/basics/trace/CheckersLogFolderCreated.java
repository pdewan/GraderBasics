package grader.basics.trace;


public class CheckersLogFolderCreated extends SerializableFileInfo {

	public CheckersLogFolderCreated(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static CheckersLogFolderCreated newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "Interaction log data folder created: " + aFileName;
		CheckersLogFolderCreated retVal = new CheckersLogFolderCreated(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}
