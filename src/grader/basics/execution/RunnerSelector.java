package grader.basics.execution;

import grader.basics.project.Project;

public class RunnerSelector {
	static RunnerFactory factory = new BasicProcessRunnerFactory();

	public static RunnerFactory getFactory() {
		return factory;
	}

	public static void setFactory(RunnerFactory factory) {
		RunnerSelector.factory = factory;
	}
	public static Runner createProcessRunner(Project aProject, String aSpecifiedProxyMainClass) {
		return factory.createProcessRunner(aProject, aSpecifiedProxyMainClass);
	}

}
