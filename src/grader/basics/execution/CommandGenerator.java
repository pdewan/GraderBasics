package grader.basics.execution;

import java.util.List;
import java.util.Map;

import grader.basics.project.Project;

public interface CommandGenerator {
//	public Class mainClass(RootCodeFolder aRootCodeFolder, ProxyClassLoader aProxyClassLoader, String expectedName, Project aProject);
    public Map<String, String> getEntryPoints(grader.basics.project.Project project, String aSpecifiedMainClass) throws NotRunnableException;

	Map<String, String> getEntryPoints(Project project,
			String[] aSpecifiedMainClasses) throws NotRunnableException;
	public List<String> getDefaultCommand();
	public void setDefaultCommand(List<String> aCommand);
	default public boolean hasBinaryFolder() {
		return true;
	}
	 public  String getUserBinary() ;
	public void setUserBinary(String newVal) ;
	


}
