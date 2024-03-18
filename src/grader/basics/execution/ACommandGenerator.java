package grader.basics.execution;

import java.io.File;
import java.util.List;

import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.project.Project;
import grader.basics.util.TimedProcess;
import util.pipe.InputGenerator;

public abstract class ACommandGenerator implements CommandGenerator{
	protected List<String> defaultCommand;

	private  String userBinary = null;
	public  String getUserBinary() {
		return userBinary;
	}
	public void setUserBinary(String newVal) {
		userBinary = newVal;
	}
	protected void maybeChangeBinary(List<String> aCommand) {
		if (userBinary != null && aCommand.size() > 0) {
			aCommand.set(0, userBinary);
		}
	}
	protected List<String> changeableCommand() {
		return null;
	}
	@Override
	public List<String> getDefaultCommand() {
		if (defaultCommand != null) {
			return defaultCommand;
		}
		List<String> aChangeableCommand = changeableCommand();
		if (userBinary != null) {
			aChangeableCommand.set(0, userBinary);
		}
		return  aChangeableCommand;
	}

	@Override
	public void setDefaultCommand(List<String> aCommand) {
		defaultCommand = aCommand; 
		
	}
	@Override
	public  List<String> processedCommands(List<String> originalCommand, Project aProject, String aProcessName,
			File aBuildFolder, String anEntryPoint, String anEntryTagTarget, String[] anArgs) {
		return originalCommand;
	}
	@Override
	public void  runPreIndividualCommand (RunningProject runner, InputGenerator anOutputBasedInputGenerator,
			String[] aCommand, String input, String[] args, int timeout,
			String aProcessName, boolean anOnlyProcess) throws NotRunnableException {
		
	}
	@Override
	public void  runPostIndividualCommand (RunningProject runner)
	{
		
	}
	@Override
	public void  runPreTeamCommands (String aProcessTeam, RunningProject runner, InputGenerator anOutputBasedInputGenerator
		
			) throws NotRunnableException {
		
	}
	@Override
	public void  runPostTeamCommands (String aProcessTeam, RunningProject runner, InputGenerator anOutputBasedInputGenerator
		
			) throws NotRunnableException {
		
	}
	@Override
	// This method needs an extra argument!
	public  String[] getExecutionCommand(Project aProject,
			File aBuildFolder, String anEntryPoint, String[] anArgs) {
//		return StaticConfigurationUtils.getExecutionCommand(aProject, aBuildFolder, anEntryPoint);
//		return BasicStaticConfigurationUtils.getExecutionCommand(aProject, null, aBuildFolder, anEntryPoint, anEntryPoint, anArgs);
		return BasicStaticConfigurationUtils.getExecutionCommand(aProject, null, aBuildFolder, anEntryPoint, anEntryPoint, anArgs);
//		return BasicLanguageDependencyManager.getMainClassFinder().getDefaultCommand();
//		return null;
	}
	
}
