package grader.basics.execution;

import grader.basics.project.Project;

public interface RunnerFactory {
	public void resetProcessRunner();
	public void setProcessRunner(Runner newVal);
	public Runner createProcessRunner(Project aProject, String aSpecifiedProxyMainClass);

    public Runner getOrCreateProcessRunner(Project aProject, String aSpecifiedProxyMainClass);
}
