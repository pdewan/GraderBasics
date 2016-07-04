package grader.basics.trace;




public class UserProcessExecutionStarted extends  UserProcessExecutionInfo {
	
	
	public UserProcessExecutionStarted(String aMessage, String aFolderName,
			String anEntryPoint, 
			String aClassPath,
			Object aFinder)  {
		super(aMessage, aFolderName, anEntryPoint, aClassPath,  aFinder);
		// TODO Auto-generated constructor stub
	}

	
	public static UserProcessExecutionStarted newCase(String aFolderName,
			String anEntryPoint, 
			String aClassPath,
			Object aFinder) {
		String aMessage = "Process started; folder: " + aFolderName + 
				" entry point: " + anEntryPoint + 
				" class path: " + aClassPath;
		UserProcessExecutionStarted retVal = new UserProcessExecutionStarted(aMessage, aFolderName, anEntryPoint,  aClassPath, aFinder);
		retVal.announce();		
		return retVal;
	}
}