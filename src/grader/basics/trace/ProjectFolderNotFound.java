package grader.basics.trace;


public class ProjectFolderNotFound extends UncheckedGraderException {
	String fileName, onyen, student;
	
	public ProjectFolderNotFound(String aMessage, String aFileName, Object aFinder) {
		super(aMessage, aFinder);
		fileName = aFileName;
	}
	
	public ProjectFolderNotFound(String aMessage, String anOnyen, String aStudent, Object aFinder) {
		super(aMessage, aFinder);
		onyen = anOnyen;
		student = aStudent;
	}
	
	public static ProjectFolderNotFound newCase(String aFileName, Object aFinder) {
		String aMessage = "Project folder not found in submission folder:" + aFileName;
		ProjectFolderNotFound retVal = new ProjectFolderNotFound(aMessage, aFileName, aFinder);
		return retVal;
	}
	
	public static ProjectFolderNotFound newCase(String anOnyen, String aStudent, Object aFinder) {
		String aMessage ="No project folder found for:"
				+ anOnyen+ " "
				+ aStudent;
		ProjectFolderNotFound retVal = new ProjectFolderNotFound(aMessage, anOnyen,  aStudent, aFinder);
		return retVal;
	}


}
