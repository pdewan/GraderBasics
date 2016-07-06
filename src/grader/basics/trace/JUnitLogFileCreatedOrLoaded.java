package grader.basics.trace;


public class JUnitLogFileCreatedOrLoaded extends SerializableFileInfo {

	public JUnitLogFileCreatedOrLoaded(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static JUnitLogFileCreatedOrLoaded newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "Interaction log data folder created or opened for append: " + aFileName;
		JUnitLogFileCreatedOrLoaded retVal = new JUnitLogFileCreatedOrLoaded(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}
