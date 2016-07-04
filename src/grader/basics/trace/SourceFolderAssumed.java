package grader.basics.trace;


public class SourceFolderAssumed extends FileInfo {

	public SourceFolderAssumed(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static SourceFolderAssumed newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "Source folder assumed: " + aFileName;
		SourceFolderAssumed retVal = new SourceFolderAssumed(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}
