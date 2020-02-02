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
	/**
	 * Why would we want to reuse the runner? Is it a heap issue?
	 * @param aProject
	 * @param aSpecifiedProxyMainClass
	 * @return
	 */
	public static Runner getOrCreateProcessRunner(Project aProject, String aSpecifiedProxyMainClass) {
		Runner aNewValue = factory.getOrCreateProcessRunner(aProject, aSpecifiedProxyMainClass);
		aNewValue.setSpecifiedMainClass(aSpecifiedProxyMainClass); // may use same project with different main classes
		return aNewValue;
	}
	public static Runner createProcessRunner(Project aProject, String aSpecifiedProxyMainClass) {
		Runner aNewValue = factory.createProcessRunner(aProject, aSpecifiedProxyMainClass);
//		aNewValue.setSpecifiedMainClass(aSpecifiedProxyMainClass); // may use same project with different main classes
		return aNewValue;
	}
	public static void setProcessRunner(Runner newValue) {
		factory.setProcessRunner(newValue);
	}
	public static void resetProcessRunner() {
		factory.resetProcessRunner();
	}
	

}
