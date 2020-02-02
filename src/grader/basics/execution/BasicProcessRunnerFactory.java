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
		if (processRunner == null) {
//			processRunner = new BasicProcessRunner(aProject, aSpecifiedProxyMainClass);
			// why reuse the nunner?
			processRunner = createProcessRunner(aProject, aSpecifiedProxyMainClass);
		}
		return processRunner;
	}

	@Override
	public void resetProcessRunner() {
		processRunner = null;
		
	}

	@Override
	public Runner createProcessRunner(Project aProject, String aSpecifiedProxyMainClass) {
		// TODO Auto-generated method stub
		return new BasicProcessRunner(aProject, aSpecifiedProxyMainClass);
	}
}
