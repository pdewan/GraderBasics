package grader.basics.config;

import java.util.Arrays;
import java.util.List;


public interface BasicExecutionSpecification {
	public abstract List<String> getProcessTeams();
	public abstract void setProcessTeams(List<String> aProcessTeamNames);
	public List<String> getTerminatingProcesses(String aProcessTeam) ;
	public void setTerminatingProcesses(String aProcessTeam, List<String> aProcesses) ;
	public abstract List<String> getProcesses(String aProcessTeam);

	public abstract void setProcesses(String aProcessTeam,
			List<String> aProcesses);
	public abstract String getEntryTag(String aProcess);
	public abstract void setEntryTag(String aProcess, String anEntryTag);
	
	public abstract List<String> getEntryTags(String aProcess);

	public abstract void setEntryTags(String aProcess, List<String> anEntryTags);
	public abstract void setArgs(String aProcess, List<String> anEntryArgs);
	public List<String> getStartTags(String aProcess);
	public void setStartTags(String aProcess, List<String> aStartTags) ;
	public abstract Integer getResourceReleaseTime(String aProcess);
	public abstract void setGraderResourceReleaseTime(String aProcess, int aSleepTime);
	public abstract List<String> getArgs(String aProcess);
	public abstract String getEntryPoint(String aProcess);

	public abstract void setEntryPoint(String aProcess, String anEntryPoint);
	void resetProcessTeams();
	void setGraderResortTime(Integer aResortTime);
	long getResortTime();
	void setGraderWaitForResort(boolean newVal);
	boolean isWaitForResort();
	void setStudentStringProperty(String aProperty, String aValue);
	void setGraderStringProperty(String aProperty, String aValue);
	String getStringProperty(String aProperty, String aDefault);
	void setStudentGradableProjectLocation(String aValue);
	String getGradableProjectLocation();
	String getLanguage();
	String getSourceFolderLocation();
	String getBinaryFolderLocation();
	List<String> getBasicCommand();
	String getObjectFolderLocation();
	List getListProperty(String aProperty, List aDefault);
	Integer getIntegerProperty(String aProperty, Integer aDefault);
	Boolean getBooleanProperty(String aProperty, Boolean aDefault);
	Integer getProcessOutputSleepTime();
	Integer getProcessTeamOutputSleepTime();
	boolean getWaitForResortProperty();
	void setGraderProcessOutputWaitTime(Integer newVal);
	void setGraderProcessTeamOutputWaitTime(Integer newVal);
	void setGraderProcessTimeOut(int newVal);
	int getProcessTimeOut();
	void setMethodTimeOut(int methodTimeOut);
	int getMethodTimeOut();
	void setConstructorTimeOut(int constructorTimeOut);
	Integer getConstructorTimeOut();
	boolean isUseMethodAndConstructorTimeOut();
	void setGraderUseMethodAndConstructorTimeOut(boolean useMethodAndConstructorTimeOut);
	boolean isUseProcessTimeOut();
	void setGraderUseProcessTimeOut(boolean newVal);
	boolean isWaitForMethodConstructorAndProcesses();
	void setWaitForMethodConstructorAndProcesses(boolean newVal);
	void setStudentProcessTimeOut(int newVal);
//	String getDynamicExecutionFileName();
	String getCObjSuffix();
	String getExecutorDirectory();
	boolean isUseExecutor();
	List<String> getModules();
	void setGraderModules(List<String> aModules);
	void setGraderModule(String aModule);
	List<String> getBasicCommand(String aProcess);
	void setGraderBasicCommand(String aProcess, List<String> aCommand);
	void setGraderBasicCommand(List<String> aCommand);
	void setStudentResourceReleaseTime(String aProcess, int aSleepTime);
	Integer getResourceReleaseTime();
	void setGraderResourceReleaseTime(int aSleepTime);
	void setStudentResourceReleaseTime(int aSleepTime);
	boolean isCheckAllSpecifiedTags();
	void setGraderCheckAllSpecifiedTags(boolean newVal);
	boolean isForkMain();
	void setGraderForkMain(boolean newVal);
	void setStudentForkMain(boolean newVal);
	boolean isUseProjectConfiguration();
	void setUseProjectConfiguration(boolean newVal);
	String getEntryPoint();
	void setEntryPoint(String anEntryPoint);
	String getRequirementsLocation();
	
	

}