package grader.basics.config;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.NotImplementedException;

import grader.basics.project.CurrentProjectHolder;
import grader.basics.util.DirectoryUtils;
//import grader.config.AConfigurationManager;
import util.trace.Tracer;

public class ABasicConfigurationManager implements BasicConfigurationManager{
    public static final String PROJECT_CONFIG_FILE = "project.properties";
//    File projectDirectory;

	protected PropertiesConfiguration projectConfiguration;

	public static final String COURSE_FILE = "course.properties";
	public static final String CONFIG_DIR = "config";
    PropertiesConfiguration moduleConfiguration;


	public static final String COURSE_CONFIGURATION_FILE_NAME = "./" + CONFIG_DIR + "/" + COURSE_FILE;

	public ABasicConfigurationManager() {
		setCourseConfiguration(createCourseConfiguration());
	}
	@Override
	public void clear() {
		projectConfiguration = null;
	}

	@Override
	public PropertiesConfiguration getOrCreateProjectConfiguration() {
		if (projectConfiguration == null) {
//			createProjectConfiguration(CurrentProjectHolder.getOrCreateCurrentProject().getProjectFolder());
			createProjectConfiguration(CurrentProjectHolder.getProjectLocation());

		}
		return projectConfiguration;
	}
	@Override
	public PropertiesConfiguration getProjectConfiguration() {		
		return projectConfiguration;
	}
	public void setProjectConfiguration(PropertiesConfiguration aPropertiesConfiguration) {
		projectConfiguration = aPropertiesConfiguration;
	}
//	@Override
//	public void setProjectDirectory(File aProjectDirectory) {
//		projectDirectory = aProjectDirectory;
//	}
//	@Override
//	public File getProjectDirectory() {
//		return projectDirectory;
//	}
	protected PropertiesConfiguration createCourseConfiguration() {
	       
        try {
        	File aFile = new File(ABasicConfigurationManager.COURSE_CONFIGURATION_FILE_NAME);
        	if (!aFile.exists()) {
//        		Tracer.warning (ABasicConfigurationManager.COURSE_CONFIGURATION_FILE_NAME + " does not exist, using defaults");
        		Tracer.info (this, ABasicConfigurationManager.COURSE_CONFIGURATION_FILE_NAME + " does not exist, using defaults");

        		return null;
        	}
			return new PropertiesConfiguration(ABasicConfigurationManager.COURSE_CONFIGURATION_FILE_NAME);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			return null;
		}
        
        // Andrew might need to add stuff like in the method below

}
	@Override
    public PropertiesConfiguration getCourseConfiguration() {
        return moduleConfiguration;
    }
    @Override
    public void setCourseConfiguration(PropertiesConfiguration newVal) {
        this.moduleConfiguration = newVal;
    }
	@Override
	public void createProjectConfiguration (File aProjectDirectory) {	
		File[] aFiles = aProjectDirectory.listFiles();
		File aConfigFile = null;
		for (File aFile: aFiles) {
			if (aFile.getName().toLowerCase().equals(PROJECT_CONFIG_FILE)) {
				aConfigFile = aFile;
				break;
			}
		}
		if (aConfigFile != null) {
			try {
				projectConfiguration = new PropertiesConfiguration(aConfigFile);
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Did not find file " + PROJECT_CONFIG_FILE + " in " + aProjectDirectory.getAbsolutePath());
		}
//		String aConfigFileFullName = aProjectDirectory.getAbsolutePath() + "/" +
	}


}
