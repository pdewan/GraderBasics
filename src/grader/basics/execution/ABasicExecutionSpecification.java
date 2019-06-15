package grader.basics.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.configuration.resolver.EntityRegistry;

import grader.basics.BasicLanguageDependencyManager;
import grader.basics.config.BasicConfigurationManagerSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.project.Project;
import jdk.internal.dynalink.beans.StaticClass;
import util.trace.Tracer;
import static grader.basics.config.BasicStaticConfigurationUtils.*;
/*
 * We are duplicating code in BasicStaticConfigiration and keepin the same kind of state.
 * So we do not really need BasicStaticConfigiration
 * Let us keep them consistent
 * 
 * Actually we need all of this.
 */
public class ABasicExecutionSpecification implements BasicExecutionSpecification {
	protected List<String> emptyList = new ArrayList();
//	protected List<String> processTeams = new ArrayList<>();
//	protected List<String> processTeams = new ArrayList<>();

//	protected Map<String, List<String>> graderProcessTeamToProcesses = new HashMap<>();
//	protected Map<String, List<String>> graderProcessTeamToTerminatingProcesses = new HashMap<>();
//	protected Map<String, Integer> graderProcessToSleepTime = new HashMap<>();
//	protected Map<String, Integer> studentProcessToSleepTime = new HashMap<>();
//	protected Map<String, String> graderProcessToEntryTag = new HashMap<>();
//	protected Map<String, String> studentProcessToEntryTag = new HashMap<>();
//	protected Map<String, List<String>> graderProcessToEntryTags = new HashMap<>();
//	protected Map<String, List<String>> studentProcessToEntryTags = new HashMap<>();
//	protected Map<String, String> graderProcessToEntryPoint = new HashMap<>();
//	protected Map<String, String> studentProcessToEntryPoint = new HashMap<>();
//	protected Map<String, List<String>> graderProcessToArgs = new HashMap<>();
//	protected Map<String, List<String>> studentProcessToArgs = new HashMap<>();
//	protected Map<String, List<String>> graderProcessToStartTags = new HashMap<>();
//	protected Map<String, List<String>> studentProcessToStartTags = new HashMap<>();

	protected Map<String, String> runtimeGraderStringProperties = new HashMap<>();
	protected Map<String, String> runtimeStudentStringProperties = new HashMap<>();
	protected Map<String, Integer> runtimeGraderIntegerProperties = new HashMap<>();
	protected Map<String, Integer> runtimeStudentIntegerProperties = new HashMap<>();
	protected Map<String, Boolean> runtimeGraderBooleanProperties = new HashMap<>();
	protected Map<String, Boolean> runtimeStudentBooleanProperties = new HashMap<>();
	protected Map<String, List<String>> runtimeGraderListProperties = new HashMap<>();
	protected Map<String, List<String>> runtimeStudentListProperties = new HashMap<>();
	
	@Override
    public Integer getIntegerProperty(String aProperty, Integer aDefault) {
    	Integer retVal = getInheritedIntegerModuleProblemProperty(aProperty, null);
//		retVal = BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration().getString(aProperty);  
		if (retVal != null) {
			return retVal;
		}
    	 retVal = runtimeStudentIntegerProperties.get(aProperty);
    	if (retVal != null) {
    		return retVal;
    	}
//    	if (BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
    		    
//    	}
    	retVal = runtimeGraderIntegerProperties.get(aProperty);
    	if (retVal != null) {
    		return retVal;
    	}
    	return aDefault;
    }
    @Override
    public Boolean getBooleanProperty(String aProperty, Boolean aDefault) {
    	Boolean retVal = getInheritedBooleanModuleProblemProperty(aProperty, null);
//		retVal = BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration().getString(aProperty);  
		if (retVal != null) {
			return retVal;
		}
    	 retVal = runtimeStudentBooleanProperties.get(aProperty);
    	if (retVal != null) {
    		return retVal;
    	}
//    	if (BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
    		    
//    	}
    	retVal = runtimeGraderBooleanProperties.get(aProperty);
    	if (retVal != null) {
    		return retVal;
    	}
    	return aDefault;
    }
    
    @Override
    public String getStringProperty(String aProperty, String aDefault) {
    	String retVal = getInheritedStringModuleProblemProperty(aProperty, null);
//		retVal = BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration().getString(aProperty);  
		if (retVal != null) {
			return retVal;
		}
    	 retVal = runtimeStudentStringProperties.get(aProperty);
    	if (retVal != null) {
    		return retVal;
    	}
//    	if (BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
    		    
//    	}
    	retVal = runtimeGraderStringProperties.get(aProperty);
    	if (retVal != null) {
    		return retVal;
    	}
    	return aDefault;
    }
    @Override
    public List<String> getListProperty(String aProperty, List<String> aDefault) {
    	List<String> retVal = getInheritedListModuleProblemProperty(aProperty, null);
//		retVal = BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration().getString(aProperty);  
		if (retVal != null) {
			return retVal;
		}
    	retVal = runtimeStudentListProperties.get(aProperty);
    	if (retVal != null) {
    		return retVal;
    	}
//    	if (BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
    		    
//    	}
    	retVal = runtimeGraderListProperties.get(aProperty);
    	if (retVal != null) {
    		return retVal;
    	}
    	return aDefault;
    }
    @Override
    public void setStudentStringProperty(String aProperty, String aValue) {
    	runtimeStudentStringProperties.put(aProperty, aValue);
    }
   
    @Override
    public void setGraderStringProperty(String aProperty, String aValue) {
    	runtimeGraderStringProperties.put(aProperty, aValue);
    }
    public  Integer getInheritedIntegerModuleProblemProperty(
			
			String aProperty, Integer defaultValue) {
    	return BasicStaticConfigurationUtils.getBasicInheritedIntegerModuleProblemProperty(aProperty, defaultValue);
//    	if (!BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
//    		return defaultValue;
//    	}
//		return BasicStaticConfigurationUtils.getInheritedStringModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), BasicStaticConfigurationUtils.getModule(), BasicStaticConfigurationUtils.getProblem(), aProperty, defaultValue);

		
	}
    public  List<String> getInheritedListModuleProblemProperty(
			
			String aProperty, List<String> defaultValue) {
    	return BasicStaticConfigurationUtils.getBasicInheritedListModuleProblemProperty(aProperty, defaultValue);
//    	if (!BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
//    		return defaultValue;
//    	}
//		return BasicStaticConfigurationUtils.getInheritedStringModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), BasicStaticConfigurationUtils.getModule(), BasicStaticConfigurationUtils.getProblem(), aProperty, defaultValue);

		
	}
    public  String getInheritedStringModuleProblemProperty(
			
			String aProperty, String defaultValue) {
    	return BasicStaticConfigurationUtils.getBasicInheritedStringModuleProblemProperty(aProperty, defaultValue);
//    	if (!BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
//    		return defaultValue;
//    	}
//		return BasicStaticConfigurationUtils.getInheritedStringModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), BasicStaticConfigurationUtils.getModule(), BasicStaticConfigurationUtils.getProblem(), aProperty, defaultValue);

		
	}
    public  Boolean getInheritedBooleanModuleProblemProperty(
			
			String aProperty, Boolean defaultValue) {
    	return BasicStaticConfigurationUtils.getBasicInheritedBooleanModuleProblemProperty(aProperty, defaultValue);
//    	if (!BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
//    		return defaultValue;
//    	}
//		return BasicStaticConfigurationUtils.getInheritedStringModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), BasicStaticConfigurationUtils.getModule(), BasicStaticConfigurationUtils.getProblem(), aProperty, defaultValue);

		
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#getProcessTeams()
	 */
	@Override
	public List<String> getProcessTeams() {
		
		return getListProperty(BasicStaticConfigurationUtils.PROCESS_TEAMS, emptyList);
//		return processTeams;
	}
	@Override
	public void resetProcessTeams() {
		Tracer.info(this, "resetting process team: " );
		runtimeGraderListProperties.put(BasicStaticConfigurationUtils.PROCESS_TEAMS, emptyList);
//		processTeams = new ArrayList<>();
//		BasicStaticConfigurationUtils.setProcessTeams(processTeams);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setProcessTeams(java.util.List)
	 */
	@Override
	public void setProcessTeams(List<String> aProcessTeamNames) {
		Tracer.info(this, "Setting process team: " + aProcessTeamNames);
		runtimeGraderListProperties.put(BasicStaticConfigurationUtils.PROCESS_TEAMS, aProcessTeamNames);
//		processTeams = aProcessTeamNames;
//		BasicStaticConfigurationUtils.setProcessTeams(processTeams);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#getProcesses(java.lang.String)
	 */
	@Override
	public List<String> getProcesses(String aProcessTeam) {
		return getListProperty(aProcessTeam, emptyList);
//		return graderProcessTeamToProcesses.get(aProcessTeam);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setProcesses(java.lang.String, java.util.List)
	 */
	@Override
	public void setProcesses(String aProcessTeam, List<String> aProcesses) {
		Tracer.info(this, "Setting processes: " + aProcessTeam + " = " + aProcesses);
		runtimeGraderListProperties.put(aProcessTeam, aProcesses);
//		graderProcessTeamToProcesses.put(aProcessTeam, aProcesses);
	}
	
	@Override
	public List<String> getTerminatingProcesses(String aProcessTeam) {
		return getListProperty(
				BasicStaticConfigurationUtils.toCompoundProperty(
						aProcessTeam, BasicStaticConfigurationUtils.TERMINATING), emptyList);
//		return graderProcessTeamToTerminatingProcesses.get(aProcessTeam);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setProcesses(java.lang.String, java.util.List)
	 */
	@Override
	public void setTerminatingProcesses(String aProcessTeam, List<String> aProcesses) {
		Tracer.info(this, "Setting terminating processes: " + aProcessTeam + " - " + aProcesses);
		runtimeGraderListProperties.put(
				BasicStaticConfigurationUtils.toCompoundProperty(aProcessTeam, BasicStaticConfigurationUtils.TERMINATING),
				aProcesses);
		
//		graderProcessTeamToTerminatingProcesses.put(aProcessTeam, aProcesses);
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#getSleepTime(java.lang.String)
	 */
	@Override
	public Integer getSleepTime(String aProcess) {
		return getIntegerProperty(
				toCompoundProperty(aProcess, SLEEP_TIME), 
				DEFAULT_SLEEP_TIME);
				
//		return graderProcessToSleepTime.get(aProcess);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setSleepTime(java.lang.String, int)
	 */
	@Override
	public void setSleepTime(String aProcess, int aSleepTime) {
		Tracer.info(this, "Setting sleep time for process " + aProcess + " to " + aSleepTime);
//		 graderProcessToSleepTime.put(aProcess, aSleepTime);
		runtimeGraderIntegerProperties.put(
				toCompoundProperty(aProcess, SLEEP_TIME), 
				aSleepTime);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#getEntrytag(java.lang.String)
	 */
	@Override
	public String getEntryTag(String aProcess) {
		return getStringProperty(
				toCompoundProperty(aProcess, ENTRY_TAG), null);
		
//		return graderProcessToEntryTag.get(aProcess);
		
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setEntryTag(java.lang.String, java.lang.String)
	 */
	@Override
	public void setEntryTag(String aProcess, String anEntryTag) {
		Tracer.info(this, "Setting entry tag processes: " + aProcess + " - " + anEntryTag);
		runtimeGraderStringProperties.put(toCompoundProperty(aProcess, ENTRY_TAG), anEntryTag);
//		 graderProcessToEntryTag.put(aProcess, anEntryTag);
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#getArgs(java.lang.String)
	 */
	@Override
	public List<String> getArgs(String aProcess) {
		return getListProperty(toCompoundProperty(aProcess, ARGS), emptyList);
//		return graderProcessToArgs.get(aProcess);
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setArgs(java.lang.String, java.util.List)
	 */
	@Override
	public void setArgs(String aProcess, List<String> anArgs) {
		Tracer.info(this, "Setting processes args: " + aProcess + " - " + anArgs);
		runtimeGraderListProperties.put(toCompoundProperty(aProcess, ARGS), anArgs);
//		 graderProcessToArgs.put(aProcess, anEntryArgs);
	}
	
	@Override
	public List<String> getStartTags(String aProcess) {
		return getListProperty(toCompoundProperty(aProcess, START_TAGS), emptyList);

//		return graderProcessToStartTags.get(aProcess);
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setArgs(java.lang.String, java.util.List)
	 */
	@Override
	public void setStartTags(String aProcess, List<String> newVal) {
		Tracer.info(this, "Setting start tag processes: " + aProcess + " - " + newVal);
		runtimeGraderListProperties.put(toCompoundProperty(aProcess, START_TAGS), newVal);
//		graderProcessToStartTags.put(aProcess, aStartTags);
	}

	@Override
	public String getEntryPoint(String aProcess) {
		return getStringProperty(toCompoundProperty(aProcess, ENTRY_POINT), null);

//		return graderProcessToEntryPoint.get(aProcess);
	}

	@Override
	public void setEntryPoint(String aProcess, String anEntryPoint) {
		Tracer.info(this, "Setting entry point processes: " + aProcess + " - " + anEntryPoint);
		runtimeGraderStringProperties.put(toCompoundProperty(aProcess, ENTRY_POINT), anEntryPoint);
//		graderProcessToEntryPoint.put(aProcess, anEntryPoint);
	}

	@Override
	public List<String> getEntryTags(String aProcess) {
		return getListProperty(toCompoundProperty(aProcess, ENTRY_TAGS), emptyList);
//		return graderProcessToEntryTags.get(aProcess);
	}

	@Override
	public void setEntryTags(String aProcess, List<String> anEntryTags) {
		Tracer.info(this, "Setting entry tags processes: " + aProcess + " - " + anEntryTags);
		runtimeGraderListProperties.put(toCompoundProperty(aProcess, ENTRY_TAGS), anEntryTags);
//		graderProcessToEntryTags.put(aProcess, anEntryTags);
		
	}
	
	public static int RESORT_TIME = 100;
	public static boolean WAIT_FOR_RESORT = true;

//	protected  long resortTime = RESORT_TIME;
//    protected  boolean waitForResort = true;
    @Override
    public   void setGraderResortTime(Integer aResortTime) {
//    	resortTime = aResortTime;
    	runtimeGraderIntegerProperties.put(BasicStaticConfigurationUtils.RESORT_TIME, aResortTime);
    	
    }
    @Override
    public void setGraderProcessOutputWaitTime(Integer newVal) {
//    	resortTime = aResortTime;
    	runtimeGraderIntegerProperties.put(BasicStaticConfigurationUtils.OUTPUT_WAIT_TIME, newVal);
    	
    }
    @Override
    public void setGraderProcessTeamOutputWaitTime(Integer newVal) {
//    	resortTime = aResortTime;
    	runtimeGraderIntegerProperties.put(BasicStaticConfigurationUtils.TEAM_OUTPUT_WAIT_TIME, newVal);
    	
    }
    @Override
    public  long getResortTime() {
    	return getIntegerProperty(BasicStaticConfigurationUtils.RESORT_TIME, RESORT_TIME);
    }
    @Override
    public  Integer getProcessOutputSleepTime() {
    	return getIntegerProperty(BasicStaticConfigurationUtils.OUTPUT_WAIT_TIME, BasicRunningProject.PROCESS_OUTPUT_SLEEP_TIME);
    }
    @Override
	public  Integer getProcessTeamOutputSleepTime() {
    	return getIntegerProperty(BasicStaticConfigurationUtils.TEAM_OUTPUT_WAIT_TIME, BasicRunningProject.PROCESS_TEAM_OUTPUT_OUTPUT_SLEEP_TIME);

	}
    @Override
    public  boolean getWaitForResort() {
//    	return BasicStaticConfigurationUtils.isTeamProcess() && waitForResort;
    	return BasicStaticConfigurationUtils.isTeamProcess() && getWaitForResortProperty();

    }
    @Override
    public  void setGraderWaitForResort(boolean newVal) {
//    	waitForResort = newVal;
    	runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.WAIT_FOR_RESORT, newVal);

    }
    @Override
    public boolean getWaitForResortProperty() {
    	return getBooleanProperty(BasicStaticConfigurationUtils.WAIT_FOR_RESORT, WAIT_FOR_RESORT);
    }
    
 
    protected String gradableProjectLocation = null;
    @Override
    public String getGradableProjectLocation() {
    	if (gradableProjectLocation == null) {
    		gradableProjectLocation = getStringProperty(BasicStaticConfigurationUtils.GRADABLE_PROJECT_LOCATION, ".");
    	}
    	return gradableProjectLocation;
//    	return getStringProperty(BasicStaticConfigurationUtils.GRADABLE_PROJECT_LOCATION, ".");
    }
    @Override
    public void setStudentGradableProjectLocation(String aValue) {
    	runtimeStudentStringProperties.put(BasicStaticConfigurationUtils.GRADABLE_PROJECT_LOCATION, aValue);
    }
    protected String sourceFolderLocation = null;

    
    @Override
    // the default value is null, so this caching will not really help
    public String getSourceFolderLocation() {
    	if (sourceFolderLocation == null) {
    		sourceFolderLocation = getStringProperty(BasicStaticConfigurationUtils.SOURCE_LOCATION, null);
    	}
    	return sourceFolderLocation;
//    	return getStringProperty(BasicStaticConfigurationUtils.SOURCE_LOCATION, null);
    }
    protected String binaryFolderLocation = null;

    @Override
    public String getBinaryFolderLocation() {
    	if (binaryFolderLocation == null) {
    		binaryFolderLocation = getStringProperty(BasicStaticConfigurationUtils.BINARY_LOCATION, null);
    	}
    	return binaryFolderLocation;
//    	return getStringProperty(BasicStaticConfigurationUtils.BINARY_LOCATION, null);
    }
    protected String objectFolderLocation = null;

    @Override
    public String getObjectFolderLocation() {
    	if (objectFolderLocation == null) {
    		objectFolderLocation = getStringProperty(BasicStaticConfigurationUtils.OBJECT_LOCATION, null);
    	}
    	return objectFolderLocation;
//    	return getStringProperty(BasicStaticConfigurationUtils.BINARY_LOCATION, null);
    }
    // should we cache these values?
    protected String language;
    @Override
    public String getLanguage() {
    	if (language == null) {
    		language = getStringProperty(BasicStaticConfigurationUtils.LANGUAGE,BasicLanguageDependencyManager.JAVA_LANGUAGE);
    	}
    	return language;
//    	return getStringProperty(BasicStaticConfigurationUtils.LANGUAGE,BasicLanguageDependencyManager.JAVA_LANGUAGE);
    }
//    // no caching as 
//    @Override
//    public List<String> getBasicCommand() {
//    	List<String> aCommand = getInheritedListModuleProblemProperty(BasicStaticConfigurationUtils.EXECUTION_COMMAND, null);
//    	if (aCommand != null && !aCommand.isEmpty()) {
//    		return aCommand;
//    	}
////    	String aLangugage = getLanguage();
//    	return BasicLanguageDependencyManager.getMainClassFinder().getDefaultCommand();
//    	
//    }
    // no caching as 
    @Override
    public List<String> getBasicCommand() {
    	List<String> aCommand = getListProperty(BasicStaticConfigurationUtils.EXECUTION_COMMAND, null);
    	if (aCommand != null && !aCommand.isEmpty()) {
    		return aCommand;
    	}
//    	String aLangugage = getLanguage();
    	return BasicLanguageDependencyManager.getMainClassFinder().getDefaultCommand();
    	
    }
    @Override
    public  Integer getConstructorTimeOut() {
		return getIntegerProperty(CONSTRUCTOR_TIMEOUT, DEFAULT_CONSTRUCTOR_TIME_OUT);
	}
    @Override
	public  void setConstructorTimeOut(int constructorTimeOut) {
		runtimeGraderIntegerProperties.put(CONSTRUCTOR_TIMEOUT, constructorTimeOut);
//		BasicProjectExecution.constructorTimeOut = constructorTimeOut;
	}
    @Override
	public  int getMethodTimeOut() {
		return getIntegerProperty(METHOD_TIMEOUT, DEFAULT_METHOD_TIME_OUT);
	}
	@Override
	public  void setMethodTimeOut(int methodTimeOut) {
		runtimeGraderIntegerProperties.put(METHOD_TIMEOUT, methodTimeOut);

	}
	@Override
	public  int getProcessTimeOut() {
		 return getIntegerProperty(PROCESS_TIMEOUT, DEFAULT_PROCESS_TIME_OUT);
	}	
	
	@Override
	public  void setGraderProcessTimeOut(int newVal) {
		runtimeGraderIntegerProperties.put(PROCESS_TIMEOUT, newVal);

	}
	@Override
	public  void setStudentProcessTimeOut(int newVal) {
		runtimeStudentIntegerProperties.put(PROCESS_TIMEOUT, newVal);

	}
	@Override
	public  boolean isUseMethodAndConstructorTimeOut() {
		return getBooleanProperty(USE_METHOD_CONSTRUCTOR_TIMEOUT, true);
//		return useMethodAndConstructorTimeOut;
	}
	@Override
	public void setGraderUseMethodAndConstructorTimeOut(
			boolean useMethodAndConstructorTimeOut) {
		runtimeGraderBooleanProperties.put(USE_METHOD_CONSTRUCTOR_TIMEOUT, useMethodAndConstructorTimeOut);
//		BasicProjectExecution.useMethodAndConstructorTimeOut = useMethodAndConstructorTimeOut;
	}
	public void setStudentUseMethodAndConstructorTimeOut(
			boolean useMethodAndConstructorTimeOut) {
		runtimeStudentBooleanProperties.put(USE_METHOD_CONSTRUCTOR_TIMEOUT, useMethodAndConstructorTimeOut);
//		BasicProjectExecution.useMethodAndConstructorTimeOut = useMethodAndConstructorTimeOut;
	}
	@Override
	public  boolean isUseProcessTimeOut() {
		return getBooleanProperty(USE_PROCESS_TIMEOUT, true);
	}
	@Override
	public  void setUseProcessTimeOut(
			boolean newVal) {
		runtimeGraderBooleanProperties.put(USE_PROCESS_TIMEOUT, newVal);
	}
	@Override
	public  boolean isWaitForMethodConstructorAndProcesses() {
		return getBooleanProperty(WAIT_FOR_METHOD_CONSTRUCTOR_AND_PROCESSES, true);
//		return useMethodAndConstructorTimeOut;
	}
	@Override
	public void setWaitForMethodConstructorAndProcesses(
			boolean newVal) {
		runtimeGraderBooleanProperties.put(WAIT_FOR_METHOD_CONSTRUCTOR_AND_PROCESSES, newVal);
//		BasicProjectExecution.useMethodAndConstructorTimeOut = useMethodAndConstructorTimeOut;
	}

    
}
