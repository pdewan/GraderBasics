package grader.basics.trace;




public class UserProcessExecutionTimedOut extends  UserProcessExecutionInfo {
	
	
	public UserProcessExecutionTimedOut(String aMessage, String aFolderName,
			String anEntryPoint, 
			String aClassPath,
			Object aFinder)  {
		super(aMessage, aFolderName, anEntryPoint, aClassPath,  aFinder);
		// TODO Auto-generated constructor stub
	}

	
	public static UserProcessExecutionTimedOut newCase(String aFolderName,
			String anEntryPoint, 
			String aClassPath,
			Object aFinder) {
		String aMessage = "Process timed out; folder: " + aFolderName + 
				" entry point: " + anEntryPoint + 
				" class path: " + aClassPath;
		UserProcessExecutionTimedOut retVal = new UserProcessExecutionTimedOut(aMessage, aFolderName, anEntryPoint,  aClassPath, aFinder);
		retVal.announce();		
		return retVal;
	}
}