package grader.basics.trace;


public class BinaryFolderMade extends FileInfo {

	public BinaryFolderMade(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static BinaryFolderMade newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "Binary folder created: " + aFileName;
		BinaryFolderMade retVal = new BinaryFolderMade(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}
