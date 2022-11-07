package grader.basics.execution;

import java.io.File;
import java.util.List;
import java.util.Map;

import grader.basics.project.Project;
import util.pipe.InputGenerator;

public interface CommandGenerator {
//	public Class mainClass(RootCodeFolder aRootCodeFolder, ProxyClassLoader aProxyClassLoader, String expectedName, Project aProject);
    public Map<String, String> getEntryPoints(grader.basics.project.Project project, String aSpecifiedMainClass) throws NotRunnableException;

	Map<String, String> getEntryPoints(Project project,
			String[] aSpecifiedMainClasses) throws NotRunnableException;
	public List<String> getDefaultCommand();
	public void setDefaultCommand(List<String> aCommand);
	default public boolean hasBinaryFolder() {
		return true;
	}
	 public  String getUserBinary() ;
	public void setUserBinary(String newVal) ;

	List<String> processedCommands(List<String> basicCommand, Project aProject, String aProcessName, File aBuildFolder,
			String anEntryPoint, String anEntryTagTarget, String[] anArgs);

	void runPreIndividualCommand(RunningProject runner, InputGenerator anOutputBasedInputGenerator, String[] aCommand,
			String input, String[] args, int timeout, String aProcessName, boolean anOnlyProcess)
			throws NotRunnableException;

//	void runPostIndividualCommand(RunningProject runner, InputGenerator anOutputBasedInputGenerator, String[] aCommand,
//			String input, String[] args, int timeout, String aProcessName, boolean anOnlyProcess)
//			throws NotRunnableException;
	void runPostIndividualCommand(RunningProject runner);

	public void  runPreTeamCommands (String aProcessTeam, RunningProject runner, InputGenerator anOutputBasedInputGenerator
			
			) throws NotRunnableException;
		

	public void  runPostTeamCommands (String aProcessTeam, RunningProject runner, InputGenerator anOutputBasedInputGenerator
			
			) throws NotRunnableException;

	String[] getExecutionCommand(Project aProject, File aBuildFolder, String anEntryPoint, String[] anArgs);
	


}
