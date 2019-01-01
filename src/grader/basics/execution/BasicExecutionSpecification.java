package grader.basics.execution;

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
	public abstract Integer getSleepTime(String aProcess);
	public abstract void setSleepTime(String aProcess, int aSleepTime);
	public abstract List<String> getArgs(String aProcess);
	public abstract String getEntryPoint(String aProcess);

	public abstract void setEntryPoint(String aProcess, String anEntryPoint);
	

}
