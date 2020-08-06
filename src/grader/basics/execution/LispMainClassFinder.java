package grader.basics.execution;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.Project;

public class LispMainClassFinder implements MainClassFinder{
	protected static Map emptyMap = new HashMap();
	protected List<String> defaultCommand;
	@Override
	public Map<String, String> getEntryPoints(Project project, String aSpecifiedMainClass) throws NotRunnableException {
		// TODO Auto-generated method stub
		return emptyMap;
	}

	@Override
	public Map<String, String> getEntryPoints(Project project, String[] aSpecifiedMainClasses)
			throws NotRunnableException {
		// TODO Auto-generated method stub
		return null;
	}
	protected StringBuffer command = new StringBuffer();
	public static final String CLISP = "clisp";
	public static final String FILE_FLAG = "-i";
	@Override
	public List<String> getDefaultCommand() {
		if (defaultCommand != null) {
			return defaultCommand;
		}
		return  BasicStaticConfigurationUtils.DEFAULT_BASIC_LISP_COMMAND;
	}

	@Override
	public void setDefaultCommand(List<String> aCommand) {
		defaultCommand = aCommand; 
		
	}

}
