package grader.basics.config;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.NotImplementedException;

import grader.basics.project.CurrentProjectHolder;
import grader.basics.util.DirectoryUtils;

public class ABasicConfigurationManager implements BasicConfigurationManager{
    public static final String PROJECT_CONFIG_FILE = "project.properties";
//    File projectDirectory;

	protected PropertiesConfiguration projectConfiguration;
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
