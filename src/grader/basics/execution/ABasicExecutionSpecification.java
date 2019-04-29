package grader.basics.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grader.basics.config.BasicStaticConfigurationUtils;
import util.trace.Tracer;
/*
 * We are duplicating code in BasicStaticConfigiration and keepin the same kind of state.
 * So we do not really need BasicStaticConfigiration
 * Let us keep them consistent
 */
public class ABasicExecutionSpecification implements BasicExecutionSpecification {
	protected List<String> processTeams = new ArrayList<>();
	protected Map<String, List<String>> processTeamToProcesses = new HashMap<>();
	protected Map<String, List<String>> processTeamToTerminatingProcesses = new HashMap<>();
	protected Map<String, Integer> processToSleepTime = new HashMap<>();
	protected Map<String, String> processToEntryTag = new HashMap<>();
	protected Map<String, List<String>> processToEntryTags = new HashMap<>();
	protected Map<String, String> processToEntryPoint = new HashMap<>();
	protected Map<String, List<String>> processToArgs = new HashMap<>();
	protected Map<String, List<String>> processToStartTags = new HashMap<>();
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#getProcessTeams()
	 */
	@Override
	public List<String> getProcessTeams() {
		return processTeams;
	}
	@Override
	public void resetProcessTeams() {
		Tracer.info(this, "reSetting process team: " );
		processTeams = new ArrayList<>();
		BasicStaticConfigurationUtils.setProcessTeams(processTeams);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setProcessTeams(java.util.List)
	 */
	@Override
	public void setProcessTeams(List<String> aProcessTeamNames) {
		Tracer.info(this, "Setting process team: " + aProcessTeamNames);
		processTeams = aProcessTeamNames;
		BasicStaticConfigurationUtils.setProcessTeams(processTeams);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#getProcesses(java.lang.String)
	 */
	@Override
	public List<String> getProcesses(String aProcessTeam) {
		return processTeamToProcesses.get(aProcessTeam);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setProcesses(java.lang.String, java.util.List)
	 */
	@Override
	public void setProcesses(String aProcessTeam, List<String> aProcesses) {
		Tracer.info(this, "Setting processes: " + aProcessTeam + " = " + aProcesses);
		processTeamToProcesses.put(aProcessTeam, aProcesses);
	}
	
	@Override
	public List<String> getTerminatingProcesses(String aProcessTeam) {
		return processTeamToTerminatingProcesses.get(aProcessTeam);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setProcesses(java.lang.String, java.util.List)
	 */
	@Override
	public void setTerminatingProcesses(String aProcessTeam, List<String> aProcesses) {
		Tracer.info(this, "Setting terminating processes: " + aProcessTeam + " - " + aProcesses);
		processTeamToTerminatingProcesses.put(aProcessTeam, aProcesses);
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#getSleepTime(java.lang.String)
	 */
	@Override
	public Integer getSleepTime(String aProcess) {
		return processToSleepTime.get(aProcess);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setSleepTime(java.lang.String, int)
	 */
	@Override
	public void setSleepTime(String aProcess, int aSleepTime) {
		Tracer.info(this, "Setting sleep time: " + aSleepTime);
		 processToSleepTime.put(aProcess, aSleepTime);
	}
	
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#getEntrytag(java.lang.String)
	 */
	@Override
	public String getEntryTag(String aProcess) {
		return processToEntryTag.get(aProcess);
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setEntryTag(java.lang.String, java.lang.String)
	 */
	@Override
	public void setEntryTag(String aProcess, String anEntryTag) {
		Tracer.info(this, "Setting entry tag processes: " + aProcess + " - " + anEntryTag);
		 processToEntryTag.put(aProcess, anEntryTag);
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#getArgs(java.lang.String)
	 */
	@Override
	public List<String> getArgs(String aProcess) {
		return processToArgs.get(aProcess);
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setArgs(java.lang.String, java.util.List)
	 */
	@Override
	public void setArgs(String aProcess, List<String> anEntryArgs) {
		Tracer.info(this, "Setting processes args: " + aProcess + " - " + anEntryArgs);
		 processToArgs.put(aProcess, anEntryArgs);
	}
	
	@Override
	public List<String> getStartTags(String aProcess) {
		return processToStartTags.get(aProcess);
	}
	/* (non-Javadoc)
	 * @see grader.execution.ExecutionSpecification#setArgs(java.lang.String, java.util.List)
	 */
	@Override
	public void setStartTags(String aProcess, List<String> aStartTags) {
		Tracer.info(this, "Setting start tag processes: " + aProcess + " - " + aStartTags);
		processToStartTags.put(aProcess, aStartTags);
	}

	@Override
	public String getEntryPoint(String aProcess) {
		return processToEntryPoint.get(aProcess);
	}

	@Override
	public void setEntryPoint(String aProcess, String anEntryPoint) {
		Tracer.info(this, "Setting entry point processes: " + aProcess + " - " + anEntryPoint);
		processToEntryPoint.put(aProcess, anEntryPoint);
	}

	@Override
	public List<String> getEntryTags(String aProcess) {
		return processToEntryTags.get(aProcess);
	}

	@Override
	public void setEntryTags(String aProcess, List<String> anEntryTags) {
		Tracer.info(this, "Setting entry tags processes: " + aProcess + " - " + anEntryTags);
		processToEntryTags.put(aProcess, anEntryTags);
		
	}

}
