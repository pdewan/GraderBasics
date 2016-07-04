package grader.basics.execution;

import grader.basics.project.Project;

public interface RunnerFactory {
    public Runner createProcessRunner(Project aProject, String aSpecifiedProxyMainClass);
}
