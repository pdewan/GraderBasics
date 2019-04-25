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
	public static Runner getOrCreateProcessRunner(Project aProject, String aSpecifiedProxyMainClass) {
		Runner aNewValue = factory.getOrCreateProcessRunner(aProject, aSpecifiedProxyMainClass);
		aNewValue.setSpecifiedMainClass(aSpecifiedProxyMainClass); // may use same project with different main classes
		return aNewValue;
	}

}
