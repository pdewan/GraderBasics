package grader.basics.project;

import java.io.File;
import java.io.FileNotFoundException;

import grader.basics.config.BasicExecutionSpecification;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.GradingMode;

public class CurrentProjectHolder {
	static Project currentProject;
	static boolean localProject;
	static File projectLocation;
	
	public static boolean isLocalProject() {
		return localProject;
	}
	public static void setLocalProject(boolean localProject) {
		CurrentProjectHolder.localProject = localProject;
	}
	public static Project getOrCreateCurrentProject() {
		if (currentProject == null && !GradingMode.getGraderRun()) {
			currentProject = createCurrentProject(); 
			setLocalProject(true);
		}
		return getCurrentProject();
	}
	public static Project getCurrentProject() {
		return currentProject;
	}
	// need a push project and pop project operation also for efficiency
	public static void setProject(Project aProject) {
		currentProject = aProject;
		BasicProjectIntrospection.clearProjectCaches();// avoid having to make an extra call for this
	}
	public static void setProject(String aSourceFilePattern){
		try {
			setProject(new BasicProject(aSourceFilePattern));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Project createCurrentProject() {
//		String aProjectLocation = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getGradableProjectLocation();
//		setProjectLocation(new File(aProjectLocation));
		try {
			return new BasicProject(null, getProjectLocation(), null, null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static File getProjectLocation() {
		if (projectLocation == null ) {
			String aProjectLocation = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getGradableProjectLocation();
			projectLocation = new File(aProjectLocation);
		}
		return projectLocation;
	}
	public static void setProjectLocation(File projectLocation) {
		CurrentProjectHolder.projectLocation = projectLocation;
	}

}
