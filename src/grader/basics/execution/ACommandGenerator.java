package grader.basics.execution;

import java.util.List;

import grader.basics.config.BasicStaticConfigurationUtils;

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

}
