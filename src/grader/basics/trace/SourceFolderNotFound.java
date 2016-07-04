package grader.basics.trace;


public class SourceFolderNotFound extends UncheckedGraderException {
	String projectFolder;
	
	

	public SourceFolderNotFound(String aMessage, String aFileName, Object aFinder) {
		super(aMessage, aFinder);
		projectFolder = aFileName;
	}
	
	public String getProjectFolder() {
		return projectFolder;
	}

	public void setProjectFolder(String projectFolder) {
		this.projectFolder = projectFolder;
	}
	
	public static SourceFolderNotFound newCase(String aFileName, Object aFinder) {
		String aMessage = "Source folder not found in project folder:" + aFileName;
		SourceFolderNotFound retVal = new SourceFolderNotFound(aMessage, aFileName, aFinder);
		retVal.announce();
		return retVal;
	}


}
