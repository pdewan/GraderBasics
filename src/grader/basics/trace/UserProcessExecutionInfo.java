package grader.basics.trace;


public class UserProcessExecutionInfo extends GraderInfo{
	String entryPoint;
	String classPath;
	String folderName;
	
	public UserProcessExecutionInfo(String aMessage, 
			String aFolderName,
			String anEntryPoint, 
			String aClassPath,
			Object aFinder) {
		super(aMessage, aFinder);
		entryPoint = anEntryPoint;
		classPath = aClassPath;
		folderName = aFolderName;
	}

	public String getEntryPoint() {
		return entryPoint;
	}

	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}

	

}
