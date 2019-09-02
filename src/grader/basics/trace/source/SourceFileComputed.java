package grader.basics.trace.source;


public class SourceFileComputed extends SourceFileInfo {

	public SourceFileComputed(String aMessage, String aFileName, String aText,
			Object aFinder) {
		super(aMessage, aFileName, aText, aFinder);
	}

	public static SourceFileComputed newCase(String aFileName, String aText,
			Object aFinder) {
		String aMessage =  "Source file: " + aFileName + " created with computed text: " + aText;
		SourceFileComputed retVal = new SourceFileComputed(aMessage, aFileName, aText, aFinder);
		retVal.announce();
		return retVal;
	}

	
}
