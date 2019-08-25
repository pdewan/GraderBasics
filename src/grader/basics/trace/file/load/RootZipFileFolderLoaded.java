package grader.basics.trace.file.load;

import grader.basics.trace.FileInfo;

public class RootZipFileFolderLoaded extends FileInfo {

	public RootZipFileFolderLoaded(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static RootZipFileFolderLoaded newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "Root zip file folder loaded: " + aFileName;
		RootZipFileFolderLoaded retVal = new RootZipFileFolderLoaded(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}
