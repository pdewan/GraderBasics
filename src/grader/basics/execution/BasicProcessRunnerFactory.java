package grader.basics.execution;

import grader.basics.project.Project;

public class BasicProcessRunnerFactory implements RunnerFactory{

	@Override
	public Runner createProcessRunner(Project aProject,
			String aSpecifiedProxyMainClass) {
		return new BasicProcessRunner(aProject, aSpecifiedProxyMainClass);
	}

}
