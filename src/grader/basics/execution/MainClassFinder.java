package grader.basics.execution;

import grader.basics.project.Project;

import java.util.Map;

public interface MainClassFinder {
//	public Class mainClass(RootCodeFolder aRootCodeFolder, ProxyClassLoader aProxyClassLoader, String expectedName, Project aProject);
    public Map<String, String> getEntryPoints(grader.basics.project.Project project, String aSpecifiedMainClass) throws NotRunnableException;

	Map<String, String> getEntryPoints(Project project,
			String[] aSpecifiedMainClasses) throws NotRunnableException;


}
