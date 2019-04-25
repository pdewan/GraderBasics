package grader.basics.trace;

import java.util.Arrays;

public class UserProcessExecutionStarted extends  UserProcessExecutionInfo {
	
	
	public UserProcessExecutionStarted(String aMessage, String aFolderName,
			String anEntryPoint, 
			String aClassPath,
			Object aFinder)  {
		super(aMessage, aFolderName, anEntryPoint, aClassPath,  aFinder);
		// TODO Auto-generated constructor stub
	}

	
	public static UserProcessExecutionStarted newCase(String[] aCommand, String aFolderName,
			String anEntryPoint, 
			String aClassPath,
			Object aFinder) {
		String aCommandString = aCommand == null?"":aCommand[aCommand.length-1];
//		String aMessage = "Process started: " + aCommandString + " folder: " + aFolderName + 
//				" entry point: " + anEntryPoint + 
//				" class path: " + aClassPath;
		String aMessage = "Process started: " + aCommandString + " folder: " + aFolderName + 
				" entry point: " + anEntryPoint + 
				" class path: " + aClassPath;
//		if (anEntryPoint == null) {
//			System.out.println("##### null entrypoint");
//		}
		UserProcessExecutionStarted retVal = new UserProcessExecutionStarted(aMessage, aFolderName, anEntryPoint,  aClassPath, aFinder);
		retVal.announce();		
		return retVal;
	}
}