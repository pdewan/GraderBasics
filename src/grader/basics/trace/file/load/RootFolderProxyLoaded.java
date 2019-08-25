package grader.basics.trace.file.load;

import grader.basics.trace.FileInfo;

public class RootFolderProxyLoaded extends FileInfo {

	public RootFolderProxyLoaded(String aMessage, String aFileName,
			Object aFinder) {
		super(aMessage, aFileName, aFinder);
	}
	
	public static RootFolderProxyLoaded newCase(String aFileName,
			Object aFinder) {
		String aMessage =  "Root file-system folder loaded: " + aFileName;
		RootFolderProxyLoaded retVal = new RootFolderProxyLoaded(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}

}
