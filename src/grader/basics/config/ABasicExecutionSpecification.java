package grader.basics.config;

import static grader.basics.config.BasicStaticConfigurationUtils.ARGS;
import static grader.basics.config.BasicStaticConfigurationUtils.CHECK_ALL_SPECIFIED_TAGS;
import static grader.basics.config.BasicStaticConfigurationUtils.CONSTRUCTOR_TIMEOUT;
import static grader.basics.config.BasicStaticConfigurationUtils.DEFAULT_CHECK_ALL_SPECIFIED_TAGS;
import static grader.basics.config.BasicStaticConfigurationUtils.DEFAULT_CONSTRUCTOR_TIME_OUT;
import static grader.basics.config.BasicStaticConfigurationUtils.DEFAULT_FORK_MAIN;
import static grader.basics.config.BasicStaticConfigurationUtils.DEFAULT_METHOD_TIME_OUT;
import static grader.basics.config.BasicStaticConfigurationUtils.DEFAULT_PROCESS_TIME_OUT;
import static grader.basics.config.BasicStaticConfigurationUtils.DEFAULT_REQUIREMENTS_LOCATION;
import static grader.basics.config.BasicStaticConfigurationUtils.DEFAULT_RESOURCE_RELEASE_TIME;
import static grader.basics.config.BasicStaticConfigurationUtils.DEFAULT_USE_PROJECT_CONFIGURATION;
import static grader.basics.config.BasicStaticConfigurationUtils.ENTRY_POINT;
import static grader.basics.config.BasicStaticConfigurationUtils.ENTRY_TAG;
import static grader.basics.config.BasicStaticConfigurationUtils.ENTRY_TAGS;
import static grader.basics.config.BasicStaticConfigurationUtils.FORK_MAIN;
import static grader.basics.config.BasicStaticConfigurationUtils.METHOD_TIMEOUT;
import static grader.basics.config.BasicStaticConfigurationUtils.PROCESS_TIMEOUT;
import static grader.basics.config.BasicStaticConfigurationUtils.RESOURCE_RELEASE_TIME;
import static grader.basics.config.BasicStaticConfigurationUtils.START_TAGS;
import static grader.basics.config.BasicStaticConfigurationUtils.USE_METHOD_CONSTRUCTOR_TIMEOUT;
import static grader.basics.config.BasicStaticConfigurationUtils.USE_PROCESS_TIMEOUT;
import static grader.basics.config.BasicStaticConfigurationUtils.USE_PROJECT_CONFIGURATION;
import static grader.basics.config.BasicStaticConfigurationUtils.WAIT_FOR_METHOD_CONSTRUCTOR_AND_PROCESSES;
import static grader.basics.config.BasicStaticConfigurationUtils.toCompoundProperty;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grader.basics.BasicLanguageDependencyManager;
import grader.basics.execution.BasicRunningProject;
import util.trace.Tracer;
/*
 * We are duplicating code in BasicStaticConfigiration and keeping the same kind of state.
 * So we do not really need BasicStaticConfigiration
 * Let us keep them consistent
 * 
 * Actually we need all of this so we can override instance methods, which you cannot in
 * BasicStaticSpecification.
 */
public class ABasicExecutionSpecification implements BasicExecutionSpecification {
	protected List<String> emptyList = new ArrayList();


//	protected Map<String, String> runtimeGraderStringProperties = new HashMap<>();
//	protected Map<String, String> runtimeStudentStringProperties = new HashMap<>();
//	protected Map<String, Integer> runtimeGraderIntegerProperties = new HashMap<>();
//	protected Map<String, Integer> runtimeStudentIntegerProperties = new HashMap<>();
//	protected Map<String, Boolean> runtimeGraderBooleanProperties = new HashMap<>();
//	protected Map<String, Boolean> runtimeStudentBooleanProperties = new HashMap<>();
//	protected Map<String, List> runtimeGraderListProperties = new HashMap<>();
//	protected Map<String, List> runtimeStudentListProperties = new HashMap<>();
	
	protected Map<String, String> runtimeGraderStringProperties = new TracingHashMap<>();
	protected Map<String, String> runtimeStudentStringProperties = new TracingHashMap<>();
	protected Map<String, Integer> runtimeGraderIntegerProperties = new TracingHashMap<>();
	protected Map<String, Integer> runtimeStudentIntegerProperties = new TracingHashMap<>();
	protected Map<String, Boolean> runtimeGraderBooleanProperties = new TracingHashMap<>();
	protected Map<String, Boolean> runtimeStudentBooleanProperties = new TracingHashMap<>();
	protected Map<String, List> runtimeGraderListProperties = new TracingHashMap<>();
	protected Map<String, List> runtimeStudentListProperties = new TracingHashMap<>();
	
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
    protected String getConfigurationDirectString(String aProperty, String aDefault) {
    	return BasicStaticConfigurationUtils.getConfigurationBasicDirectString(aProperty, aDefault);
    }
    protected List getConfigurationDirectList(String aProperty, List aDefault) {
    	return BasicStaticConfigurationUtils.getConfigurationBasicDirectList(aProperty, aDefault);
    }
    public String getDirectStringProperty(String aProperty, String aDefault) {
    	String retVal = getConfigurationDirectString(aProperty, null);
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
    public List<String> getDirectListProperty(String aProperty, List<String> aDefault) {
    	List<String> retVal = getConfigurationDirectList(aProperty, null);
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
    public List getListProperty(String aProperty, List aDefault) {
    	List retVal = getInheritedListModuleProblemProperty(aProperty, null);
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
    public  List getInheritedListModuleProblemProperty(
			
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
	public Integer getResourceReleaseTime(String aProcess) {
		Integer aRetVal = getIntegerProperty(
				toCompoundProperty(aProcess, RESOURCE_RELEASE_TIME), 
				null);
		if (aRetVal != null)
			return aRetVal;
		return getResourceReleaseTime();
				
//		return graderProcessToSleepTime.get(aProcess);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setSleepTime(java.lang.String, int)
	 */
	@Override
	public void setGraderResourceReleaseTime(String aProcess, int aSleepTime) {
		Tracer.info(this, "Setting grader resource release time for process " + aProcess + " to " + aSleepTime);
//		 graderProcessToSleepTime.put(aProcess, aSleepTime);
		runtimeGraderIntegerProperties.put(
				toCompoundProperty(aProcess, RESOURCE_RELEASE_TIME), 
				aSleepTime);
	}
	@Override
	public void setStudentResourceReleaseTime(String aProcess, int aSleepTime) {
		Tracer.info(this, "Setting grader resource release time for process " + aProcess + " to " + aSleepTime);
//		 graderProcessToSleepTime.put(aProcess, aSleepTime);
		runtimeStudentIntegerProperties.put(
				toCompoundProperty(aProcess, RESOURCE_RELEASE_TIME), 
				aSleepTime);
	}
	@Override
	public Integer getResourceReleaseTime() {
		return getIntegerProperty(
				RESOURCE_RELEASE_TIME, 
				DEFAULT_RESOURCE_RELEASE_TIME);
				
//		return graderProcessToSleepTime.get(aProcess);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setSleepTime(java.lang.String, int)
	 */
	@Override
	public void setGraderResourceReleaseTime(int aSleepTime) {
		Tracer.info(this, "Setting grader resource release time to " + aSleepTime);
//		 graderProcessToSleepTime.put(aProcess, aSleepTime);
		runtimeGraderIntegerProperties.put(
				RESOURCE_RELEASE_TIME, 
				aSleepTime);
	}
	@Override
	public void setStudentResourceReleaseTime (int aSleepTime) {
		Tracer.info(this, "Setting grader resource release time to " + aSleepTime);
//		 graderProcessToSleepTime.put(aProcess, aSleepTime);
		runtimeStudentIntegerProperties.put(
				RESOURCE_RELEASE_TIME, 
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
	public String getEntryPoint() {
		return getStringProperty(ENTRY_POINT, null);

//		return graderProcessToEntryPoint.get(aProcess);
	}
	@Override
	public void setEntryPoint(String anEntryPoint) {
		runtimeGraderStringProperties.put(ENTRY_POINT, anEntryPoint);
//		graderProcessToEntryPoint.put(aProcess, anEntryPoint);
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
//    @Override
//    public void setProcessOutputWaitTime(Integer newVal) {
////    	resortTime = aResortTime;
//    	runtimeGraderIntegerProperties.put(BasicStaticConfigurationUtils.OUTPUT_WAIT_TIME, newVal);
//    	
//    }
    @Override
    public void setProcessTeamOutputWaitTime(Integer newVal) {
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
    public void setProcessOutputSleepTime(Integer newVal) {
//    	resortTime = aResortTime;
    	runtimeGraderIntegerProperties.put(BasicStaticConfigurationUtils.OUTPUT_WAIT_TIME, newVal);
    	
    }
    @Override
    public  Integer getFirstInputDelay() {
    	return getIntegerProperty(BasicStaticConfigurationUtils.FIRST_INPUT_DELAY, BasicStaticConfigurationUtils.DEFAULT_FIRST_INPUT_DELAY);
    }
    @Override
    public void setFirstInputDelay(int newValue) {
    	Tracer.info(this, "Setting first input delay time:" + newValue);
//		 graderProcessToSleepTime.put(aProcess, aSleepTime);
		runtimeGraderIntegerProperties.put(
				BasicStaticConfigurationUtils.FIRST_INPUT_DELAY,
				newValue);
    }
    @Override
    public  Integer getBetweenInputDelay() {
    	return getIntegerProperty(BasicStaticConfigurationUtils.BETWEEN_INPUT_DELAY, BasicStaticConfigurationUtils.DEFAULT_BETWEEN_INPUT_DELAY);
    }
    @Override
    public void setBetweenInputDelay(int newValue) {
    	Tracer.info(this, "Setting between input delay time:" + newValue);
//		 graderProcessToSleepTime.put(aProcess, aSleepTime);
		runtimeGraderIntegerProperties.put(
				BasicStaticConfigurationUtils.BETWEEN_INPUT_DELAY,
				newValue);
    }
    @Override
	public  Integer getProcessTeamOutputSleepTime() {
    	return getIntegerProperty(BasicStaticConfigurationUtils.TEAM_OUTPUT_WAIT_TIME, BasicRunningProject.PROCESS_TEAM_OUTPUT_OUTPUT_SLEEP_TIME);

	}
    @Override
    public  boolean isWaitForResort() {
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
    protected String language = null;
    @Override
    public String getLanguage() {
    	return getStringProperty(BasicStaticConfigurationUtils.LANGUAGE, BasicLanguageDependencyManager.JAVA_LANGUAGE);
//    	if (language == null) {
//    		language = getStringProperty(BasicStaticConfigurationUtils.LANGUAGE, BasicLanguageDependencyManager.JAVA_LANGUAGE);
//    	}
//    	return language;
//    	return getStringProperty(BasicStaticConfigurationUtils.GRADABLE_PROJECT_LOCATION, ".");
    }
    @Override
    public void setLanguage(String aValue) {
//    	System.out.println("Setting language to:" + aValue);
    	runtimeStudentStringProperties.put(BasicStaticConfigurationUtils.LANGUAGE, aValue);
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
    	File aFile = new File(aValue);
    	if (!aFile.exists() || !aFile.isDirectory()) {
    		FileNotFoundException anException = new FileNotFoundException("Did not find project directory:" + aValue);
    		anException.printStackTrace();
    	}
    	
    	runtimeStudentStringProperties.put(BasicStaticConfigurationUtils.GRADABLE_PROJECT_LOCATION, aValue);
    }
protected String requirementsLocation = null;
    
    @Override
    // the default value is null, so this caching will not really help
    public String getRequirementsLocation() {
    	if (requirementsLocation == null) {
    		requirementsLocation = getStringProperty(BasicStaticConfigurationUtils.REQUIREMENTS_LOCATION, DEFAULT_REQUIREMENTS_LOCATION);
    	}
    	return sourceFolderLocation;
//    	return getStringProperty(BasicStaticConfigurationUtils.SOURCE_LOCATION, null);
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
//    protected String language;
//    @Override
    // no caching as each project can have different language
//    public String getLanguage() {
//    	return getStringProperty(BasicStaticConfigurationUtils.LANGUAGE,BasicLanguageDependencyManager.JAVA_LANGUAGE);
////    	if (language == null) {
////    		language = getStringProperty(BasicStaticConfigurationUtils.LANGUAGE,BasicLanguageDependencyManager.JAVA_LANGUAGE);
////    	}
////    	return language;
////    	return getStringProperty(BasicStaticConfigurationUtils.LANGUAGE,BasicLanguageDependencyManager.JAVA_LANGUAGE);
//    }
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
    @Override
    public List<String> getBasicCommand(String aProcess) {
    	String aProperty = toCompoundProperty(aProcess, BasicStaticConfigurationUtils.EXECUTION_COMMAND);
    	List<String> aCommand = getListProperty(aProperty, null);
    	if (aCommand != null && !aCommand.isEmpty()) {
    		return aCommand;
    	}
    	aCommand = getBasicCommand(); // more inheritance
    	if (aCommand != null && !aCommand.isEmpty()) {
    		return aCommand;
    	}
//    	String aLangugage = getLanguage();
    	return BasicLanguageDependencyManager.getMainClassFinder().getDefaultCommand();
    	
    }
    @Override
    public void setGraderBasicCommand(String aProcess, List<String> aCommand) {
    	String aProperty = toCompoundProperty(aProcess, BasicStaticConfigurationUtils.EXECUTION_COMMAND);
    	runtimeGraderListProperties.put(aProperty, aCommand);
    }
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
    public void setGraderBasicCommand(List<String> aCommand) {
    	runtimeGraderListProperties.put(BasicStaticConfigurationUtils.EXECUTION_COMMAND, aCommand);
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
	public  boolean isUseExecutor() {
		return getBooleanProperty(BasicStaticConfigurationUtils.USE_EXECEUTOR, BasicStaticConfigurationUtils.DEFAULT_USE_EXECUTOR);
//		return useMethodAndConstructorTimeOut;
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
	public  boolean isCheckAllSpecifiedTags() {
		return getBooleanProperty(CHECK_ALL_SPECIFIED_TAGS, DEFAULT_CHECK_ALL_SPECIFIED_TAGS);
//		return useMethodAndConstructorTimeOut;
	}
	@Override
	public void setGraderCheckAllSpecifiedTags(
			boolean newVal) {
		runtimeGraderBooleanProperties.put(CHECK_ALL_SPECIFIED_TAGS, newVal);
	}
//		BasicProjectEx
	@Override
	public  boolean isUseProcessTimeOut() {
		return getBooleanProperty(USE_PROCESS_TIMEOUT, true);
	}
	@Override
	public  void setGraderUseProcessTimeOut(
			boolean newVal) {
		runtimeGraderBooleanProperties.put(USE_PROCESS_TIMEOUT, newVal);
	}
	@Override
	public  boolean isForkMain() {
		return getBooleanProperty(FORK_MAIN, DEFAULT_FORK_MAIN);
	}
	@Override
	public  void setGraderForkMain(
			boolean newVal) {
		runtimeGraderBooleanProperties.put(FORK_MAIN, newVal);
	}
	@Override
	public  boolean isUseProjectConfiguration() {
		return getBooleanProperty(USE_PROJECT_CONFIGURATION, DEFAULT_USE_PROJECT_CONFIGURATION);
	}
	@Override
	public  void setUseProjectConfiguration(
			boolean newVal) {
		runtimeGraderBooleanProperties.put(USE_PROJECT_CONFIGURATION, newVal);
	}
	@Override
	public  void setStudentForkMain(
			boolean newVal) {
		runtimeStudentBooleanProperties.put(FORK_MAIN, newVal);
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
//	@Override
//	public String getDynamicExecutionFileName() {
////		return StaticConfigurationUtils.getInheritedStringModuleProblemProperty(AConfigurationManager.DYNAMIC_CONFIG_PROPERTY, AConfigurationManager.DYNAMIC_CONFIGURATION_FILE_NAME);
//		return StaticConfigurationUtils.getDynamicExecutionFileName();
//	}
	 

	@Override
	public String getCObjSuffix() {
		return getDirectStringProperty(BasicStaticConfigurationUtils.C_OBJ, BasicStaticConfigurationUtils.DEFAULT_C_OBJ);
//		return  StaticConfigurationUtils.getInheritedStringModuleProblemProperty(StaticConfigurationUtils.C_OBJ, StaticConfigurationUtils.DEFAULT_C_OBJ);
	}

	@Override
	public String getExecutorDirectory() {
		return getDirectStringProperty(BasicStaticConfigurationUtils.EXECUTOR, BasicStaticConfigurationUtils.DEFAULT_EXECUTOR);
	}
	
	
	
	@Override
    public   void setGraderModules(List<String> aModules) {
//    	resortTime = aResortTime;
    	runtimeGraderListProperties.put(BasicStaticConfigurationUtils.MODULES, aModules);
    	
    }
	@Override
    public   void setGraderModule(String aModule) {
		List<String> aModules = new ArrayList();
		aModules.add(aModule);
//    	resortTime = aResortTime;
    	runtimeGraderListProperties.put(BasicStaticConfigurationUtils.MODULES, aModules);
    	
    }
//	@Override
//	public String getCObjSuffix() {
//		return getDirectStringProperty(BasicStaticConfigurationUtils.C_OBJ, BasicStaticConfigurationUtils.DEFAULT_C_OBJ);
//
////		return  StaticConfigurationUtils.getInheritedStringModuleProblemProperty(StaticConfigurationUtils.C_OBJ, StaticConfigurationUtils.DEFAULT_C_OBJ);
//	}
	@Override
	public List<String> getModules() {
		return getDirectListProperty(BasicStaticConfigurationUtils.MODULES, null);
	}

	@Override
	public  boolean isReRunTests() {
		return getBooleanProperty(BasicStaticConfigurationUtils.RE_RUN_TESTS, BasicStaticConfigurationUtils.DEFAULT_RE_RUN_TESTS);
	}
	@Override
	public  boolean isSubDocuments() {
		return getBooleanProperty(BasicStaticConfigurationUtils.SUB_DOCUMENTS, BasicStaticConfigurationUtils.DEFAULT_SUB_DOCUMENTS);
	}
	@Override
	public  void setSubDocuments(Boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.SUB_DOCUMENTS, newVal);
	}
	@Override
	public  void setReRunTests(
			boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.RE_RUN_TESTS, newVal);
	}
	@Override
	public void setUserPath(String newValue) {
		System.setProperty(BasicStaticConfigurationUtils.USER_PATH, newValue);
		
	}
	@Override
	public String getUserPath() {
		return System.getProperty(BasicStaticConfigurationUtils.USER_PATH);
		
	}
	@Override
	public boolean isCheckStyle() {
		return getBooleanProperty(BasicStaticConfigurationUtils.CHECK_STYLE, BasicStaticConfigurationUtils.DEFAULT_CHECK_STYLE);
	}
	@Override
	public void setCheckStyle(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.CHECK_STYLE, newVal);
	}
	@Override
	public String getCheckStyleConfiguration() {
		return getStringProperty(BasicStaticConfigurationUtils.CHECK_STYLE_FILE, BasicStaticConfigurationUtils.DEFAULT_CONFIGURATION_FILE);

	}
	protected boolean freezeCheckstyleConfiguration = true;
	@Override
	public void setCheckStyleConfiguration(String newVal) {
		if (freezeCheckstyleConfiguration && 
				runtimeGraderStringProperties.get(BasicStaticConfigurationUtils.CHECK_STYLE_FILE) != null) {
			return;
		}
		runtimeGraderStringProperties.put(BasicStaticConfigurationUtils.CHECK_STYLE_FILE, newVal);
	}
	@Override
	public String getCheckStyleConfigurationDirectory() {
		return getStringProperty(BasicStaticConfigurationUtils.CHECK_STYLE_CONFIGURATION_DIRECTORY, BasicStaticConfigurationUtils.DEFAULT_CONFIGURATION_DIRECTORY);

	}
	@Override
	public void setCheckStyleConfigurationDirectory(String newVal) {
		runtimeGraderStringProperties.put(BasicStaticConfigurationUtils.CHECK_STYLE_CONFIGURATION_DIRECTORY, newVal);

		
	}
	@Override
	public String getCheckStyleOutputDirectory() {
		return getStringProperty(BasicStaticConfigurationUtils.CHECK_STYLE_OUTPUT_DIRECTORY, BasicStaticConfigurationUtils.DEFAULT_CHECKSTYLE_OUTPUT_DIRECTORY);

	}
	@Override
	public void setCheckStyleOutputDirectory(String newVal) {
		runtimeGraderStringProperties.put(BasicStaticConfigurationUtils.CHECK_STYLE_OUTPUT_DIRECTORY, newVal);

		
	}
	
	@Override
	public String getValgrindConfiguration() {
		return getStringProperty(BasicStaticConfigurationUtils.VALGRIND_CONFIGURATION, BasicStaticConfigurationUtils.DEFAULT_VALGRIND_CONFIGURATION);
	}
	
	@Override
	public void setValgrindConfiguration(String newVal) {
		runtimeGraderStringProperties.put(BasicStaticConfigurationUtils.VALGRIND_CONFIGURATION, newVal);
	}
	
	@Override
	public String getDockerPath() {
		return getStringProperty(BasicStaticConfigurationUtils.DOCKER_PROGRAM_NAME, BasicStaticConfigurationUtils.DEFAULT_DOCKER_PRORAM_NAME);
	}
	
	@Override
	public void setDockerPath(String newVal) {
		runtimeGraderStringProperties.put(BasicStaticConfigurationUtils.DOCKER_PROGRAM_NAME, newVal);
	}
	@Override
	public String getDockerContainer() {
		return getStringProperty(BasicStaticConfigurationUtils.DOCKER_CONTAINER_NAME, BasicStaticConfigurationUtils.DEFAULT_DOCKER_CONTAINER_NAME);
	}
	
	@Override
	public void setDockerContainer(String newVal) {
		runtimeGraderStringProperties.put(BasicStaticConfigurationUtils.DOCKER_CONTAINER_NAME, newVal);
	}
	@Override
	public String getValgrindConfigurationDirectory() {
		return getStringProperty(BasicStaticConfigurationUtils.VALGRIND_CONFIGURATION_DIRECTORY, BasicStaticConfigurationUtils.DEFAULT_VALGRIND_CONFIGURATION_DRECTORY);
	}
	@Override
	public void setValgrindConfigurationDirectory(String newVal) {
		runtimeGraderStringProperties.put(BasicStaticConfigurationUtils.VALGRIND_CONFIGURATION_DIRECTORY, newVal);

		
	}
	@Override
	public String getValgrindImage() {
		return getStringProperty(BasicStaticConfigurationUtils.VALGRIND_IMAGE, BasicStaticConfigurationUtils.DEFAULT_VALGRIND_IMAGE);
	}
	@Override
	public void setValgrindImage(String newVal) {
		runtimeGraderStringProperties.put(BasicStaticConfigurationUtils.VALGRIND_IMAGE, newVal);

		
	}
	@Override
	public String getValgrindTraceFile() {
		return getStringProperty(BasicStaticConfigurationUtils.VALGRIND_TRACE_FILE, BasicStaticConfigurationUtils.DEFAULT_VALGRIND_TRACE_FILE);
	}
	@Override
	public void setValgrindTraceFile(String newVal) {
		runtimeGraderStringProperties.put(BasicStaticConfigurationUtils.VALGRIND_TRACE_FILE, newVal);

	}
	@Override
	public String getValgrindTraceDirectory() {
		return getStringProperty(BasicStaticConfigurationUtils.VALGRIND_TRACE_DIRECTORY, BasicStaticConfigurationUtils.DEFAULT_VALGRIND_TRACE_DIRECTORY);
	}
	@Override
	public void setValgrindTraceDirectory(String newVal) {
		runtimeGraderStringProperties.put(BasicStaticConfigurationUtils.VALGRIND_TRACE_DIRECTORY, newVal);

	}
	@Override
	public List<String> getDereferencedArgumentTypes() {
		
		return getListProperty(BasicStaticConfigurationUtils.DEREFERENCED_ARGUMENT_TYPES, new ArrayList());
//		return processTeams;
	}
	@Override
     public void addDereferencedArgumentTypes(String[] newVal) {
		List<String> aDereferencedTypes = runtimeStudentListProperties.get(BasicStaticConfigurationUtils.DEREFERENCED_ARGUMENT_TYPES);
		if (aDereferencedTypes == null) {
			aDereferencedTypes = new ArrayList();
			runtimeGraderListProperties.put(BasicStaticConfigurationUtils.DEREFERENCED_ARGUMENT_TYPES, aDereferencedTypes );
		}
		aDereferencedTypes.addAll(Arrays.asList(newVal));
//		return processTeams;
	}

	
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setProcessTeams(java.util.List)
	 */
	@Override
	public void setDereferencedArgumentTypes(List<String> newVal) {
		Tracer.info(this, "Setting dreferenced argument types team: " + newVal);
		runtimeGraderListProperties.put(BasicStaticConfigurationUtils.DEREFERENCED_ARGUMENT_TYPES, newVal);
//		processTeams = aProcessTeamNames;
//		BasicStaticConfigurationUtils.setProcessTeams(processTeams);
	}
	
	
	@Override
	public boolean getTracing() {
		return getBooleanProperty(BasicStaticConfigurationUtils.TRACING, BasicStaticConfigurationUtils.DEFAULT_TRACING);

	}
	@Override
	public void setTracing(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.TRACING, newVal);

		
	}
	@Override
	public int getMaxTraces() {
		return getIntegerProperty(BasicStaticConfigurationUtils.MAX_TRACES, BasicStaticConfigurationUtils.DEFAULT_MAX_TRACES);

	}
	@Override
	public void setMaxTraces(int newVal) {
		runtimeGraderIntegerProperties.put(BasicStaticConfigurationUtils.MAX_TRACES, newVal);		
	}

	@Override
	public int getMaxOutputLines() {
		return getIntegerProperty(BasicStaticConfigurationUtils.MAX_OUTPUT_LINES, BasicStaticConfigurationUtils.DEFAULT_MAX_OUTPUT_LINES);

	}
	@Override
	public void setMaxOutputLines(int newVal) {
		runtimeGraderIntegerProperties.put(BasicStaticConfigurationUtils.MAX_OUTPUT_LINES, newVal);		

		
	}
	@Override
	public int getMaxPrintedTraces() {
		return getIntegerProperty(BasicStaticConfigurationUtils.MAX_PRINTED_TRACES, BasicStaticConfigurationUtils.DEFAULT_MAX_PRINTED_TRACES);

	}
	@Override
	public void setMaxPrintedTraces(int newVal) {
		runtimeGraderIntegerProperties.put(BasicStaticConfigurationUtils.MAX_PRINTED_TRACES, newVal);
		
	}
	
	@Override
	public boolean getSMLIsBat() {
		return getBooleanProperty(BasicStaticConfigurationUtils.SML_IS_BAT, BasicStaticConfigurationUtils.DEFAULT_SML_IS_BAT);

	}
	@Override
	public void setSMLIsBat(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.SML_IS_BAT, newVal);

		
	}
	@Override
	public boolean getForkInProjectFolder() {
		return getBooleanProperty(BasicStaticConfigurationUtils.FORK_IN_PROJECT_FOLDER, BasicStaticConfigurationUtils.DEFAULT_FORK_IN_PROJECT_FOLDER);

	}
	@Override
	public void setForkInProjectFolder(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.FORK_IN_PROJECT_FOLDER, newVal);

		
	}
	
	@Override
	public boolean getBufferTracedMessages() {
		return getBooleanProperty(BasicStaticConfigurationUtils.BUFFER_TRACED_MESSAGES, BasicStaticConfigurationUtils.DEFAULT_BUFFER_TRACED_MESSAGES);

	}
	@Override
	public void setBufferTracedMessages(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.BUFFER_TRACED_MESSAGES, newVal);

		
	}
	
	@Override
	public boolean getShiftAssignmentDates() {
		return getBooleanProperty(BasicStaticConfigurationUtils.SHIFT_ASSIGNMENT_DATES, BasicStaticConfigurationUtils.DEFAULT_SHIFT_ASSIGNMENT_DATES);

	}
	@Override
	public void setShiftAssignmentDates(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.SHIFT_ASSIGNMENT_DATES, newVal);

		
	}
	@Override
	public String getNavigationKind() {
		return getStringProperty(BasicStaticConfigurationUtils.NAVIGATION_KIND, BasicStaticConfigurationUtils.DEFAULT_NAVIGATION_KIND);

	}
	@Override
	public String setNavigationKind(String newVal) {
		return getStringProperty(BasicStaticConfigurationUtils.NAVIGATION_KIND, BasicStaticConfigurationUtils.DEFAULT_NAVIGATION_KIND);

	}
	
	@Override
	public boolean getStarterUI() {
		return getBooleanProperty(BasicStaticConfigurationUtils.STARTER_UI, BasicStaticConfigurationUtils.DEFAULT_CREATE_STARTER_UI);

	}
	@Override
	public void setStarterUI(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.STARTER_UI, newVal);

		
	}
	@Override
	public boolean getSaveInteractiveSettings() {
		return getBooleanProperty(BasicStaticConfigurationUtils.SAVE_INTERACTIVE_SETTINGS, BasicStaticConfigurationUtils.DEFAULT_SAVE_INTERACTIVE_SETTINGS);

	}
	@Override
	public void setSaveInteractiveSettings(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.SAVE_INTERACTIVE_SETTINGS, newVal);

		
	}
	
	@Override
	public boolean getEchoOutput() {
		return getBooleanProperty(BasicStaticConfigurationUtils.ECHO_OUTPUT, 
				BasicStaticConfigurationUtils.DEFAULT_ECHO_OUTPUT);
	}
	@Override
	public void setEchoOutput(boolean newVal) {
//		Tracer.info(this, "Setting " + BasicStaticConfigurationUtils.ECHO_OUTPUT + " to ");
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.ECHO_OUTPUT, newVal);		
	}
	@Override
	public boolean getDockerMountIsCopy() {
		return getBooleanProperty(BasicStaticConfigurationUtils.DOCKER_MOUNT_IS_COPY, 
				BasicStaticConfigurationUtils.DEFAULT_OUTPUT_TRACE);
	}
	@Override
	public void setDockerMountIsCopy(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.DOCKER_MOUNT_IS_COPY, newVal);		
	}
	@Override
	public boolean getOutputValgrindTrace() {
		return getBooleanProperty(BasicStaticConfigurationUtils.OUTPUT_TRACE, 
				BasicStaticConfigurationUtils.DEFAULT_OUTPUT_TRACE);
	}
	@Override
	public void setOutputValgrindTrace(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.OUTPUT_TRACE, newVal);		
	}
	
	@Override
	public boolean getCreateDockerContainer() {
		return getBooleanProperty(BasicStaticConfigurationUtils.CREATE_DOCKER_CONTAINER, 
				BasicStaticConfigurationUtils.DEFAULT_CREATE_DOCKER_CONTAINER);
	}
	@Override
	public void setCreateDockerContainer(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.CREATE_DOCKER_CONTAINER, newVal);		
	}
	@Override
	public boolean getLogTestData() {
		return getBooleanProperty(BasicStaticConfigurationUtils.LOG_TEST_DATA, 
				BasicStaticConfigurationUtils.DEFAULT_LOG_TEST_DATA);
	}
	@Override
	public void setLogTestData(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.LOG_TEST_DATA, newVal);		
	}
	
	@Override
	public boolean getLoadClasses() {
		return getBooleanProperty(BasicStaticConfigurationUtils.LOAD_CLASSES, 
				BasicStaticConfigurationUtils.DEFAULT_LOAD_CLASSES);
	}
	@Override
	public void setLoadClasses(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.LOAD_CLASSES, newVal);		
	}
	
	@Override
	public boolean getInstrumentUsingValgrind() {
		return getBooleanProperty(BasicStaticConfigurationUtils.INSTRUMENT_USING_VALGRIND, 
				BasicStaticConfigurationUtils.DEFAULT_INSTRUMENT_USING_VALGRIND);
	}
	@Override
	public void setInstrumentUsingValgrind(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.INSTRUMENT_USING_VALGRIND, newVal);		
	}
	@Override
	public boolean getEnableInstructorHelp() {
		return getBooleanProperty(BasicStaticConfigurationUtils.ENABLE_INSTRUCTOR_HELP, 
				BasicStaticConfigurationUtils.DEFAULT_ENABLE_INSTRUCTOR_HELP);
	}
	@Override
	public void setEnableInstructorHelp(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.ENABLE_INSTRUCTOR_HELP, newVal);		
	}
	@Override
	public boolean getHideRedirectedOutput() {
		return getBooleanProperty(BasicStaticConfigurationUtils.HIDE_REDIRECTED_OUTPUT, 
				BasicStaticConfigurationUtils.DEFAULT_HIDE_REDIRECTED_OUTPUT);
	}
	@Override
	public void setHideRedirectedOuput(boolean newVal) {
		runtimeGraderBooleanProperties.put(BasicStaticConfigurationUtils.HIDE_REDIRECTED_OUTPUT, newVal);		
	}
	
	static {
		Tracer.setKeywordPrintStatus(TracingHashMap.class, true);

	}

}
