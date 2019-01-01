package grader.basics.execution;

import grader.basics.project.Project;

public class BasicProcessRunnerFactory implements RunnerFactory{
    protected Runner processRunner;
	
	public void setProcessRunner(Runner newVal) {
		processRunner = newVal;
	}
	
	@Override
	public Runner getOrCreateProcessRunner(Project aProject,
			String aSpecifiedProxyMainClass) {
		if (processRunner == null)
			processRunner = new BasicProcessRunner(aProject, aSpecifiedProxyMainClass);
		return processRunner;
	}
}
