package grader.basics.execution;

import grader.basics.project.Project;

public interface RunnerFactory {
	public void setProcessRunner(Runner newVal);

    public Runner getOrCreateProcessRunner(Project aProject, String aSpecifiedProxyMainClass);
}
