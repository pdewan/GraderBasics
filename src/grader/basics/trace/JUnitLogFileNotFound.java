package grader.basics.trace;


public class JUnitLogFileNotFound extends SerializableFileInfo {

	public JUnitLogFileNotFound(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static JUnitLogFileNotFound newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "Interaction log file not found: " + aFileName;
		JUnitLogFileNotFound retVal = new JUnitLogFileNotFound(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}
