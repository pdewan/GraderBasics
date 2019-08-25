package grader.basics.trace.file.load;

import grader.basics.trace.FileInfo;

public class RootFileSystemFolderLoaded extends FileInfo {

	public RootFileSystemFolderLoaded(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static RootFileSystemFolderLoaded newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "Root folder loaded: " + aFileName;
		RootFileSystemFolderLoaded retVal = new RootFileSystemFolderLoaded(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}
