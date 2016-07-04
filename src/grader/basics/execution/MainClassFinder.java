package grader.basics.execution;

import java.util.Map;

public interface MainClassFinder {
//	public Class mainClass(RootCodeFolder aRootCodeFolder, ProxyClassLoader aProxyClassLoader, String expectedName, Project aProject);
    public Map<String, String> getEntryPoints(grader.basics.project.Project project, String aSpecifiedMainClass) throws NotRunnableException;


}
