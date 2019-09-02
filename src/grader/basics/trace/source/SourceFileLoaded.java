package grader.basics.trace.source;


public class SourceFileLoaded extends SourceFileInfo {

	public SourceFileLoaded(String aMessage, String aFileName, String aText,
			Object aFinder) {
		super(aMessage, aFileName, aText, aFinder);
	}

	public static SourceFileLoaded newCase(String aFileName, String aText,
			Object aFinder) {
		String aMessage =  "Source file: " + aFileName + " loaded with stored text: " + aText;
		SourceFileLoaded retVal = new SourceFileLoaded(aMessage, aFileName, aText, aFinder);
		retVal.announce();
		return retVal;
	}

	
}
