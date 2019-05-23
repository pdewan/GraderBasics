package grader.basics.config;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.NotImplementedException;

import grader.basics.util.DirectoryUtils;

public class ABasicConfigurationManager implements BasicConfigurationManager{
    public static final String PROJECT_CONFIG_FILE = "project.properties";

	protected PropertiesConfiguration projectConfiguration;

	@Override
	public PropertiesConfiguration getProjectConfiguration() {
		return projectConfiguration;
	}
	public void setProjectConfiguration(PropertiesConfiguration aPropertiesConfiguration) {
		projectConfiguration = aPropertiesConfiguration;
	}
	@Override
	public void createProjectConfiguration (File aProjectDirectory) {	
		File[] aFiles = aProjectDirectory.listFiles();
		File aConfigFile = null;
		for (File aFile: aFiles) {
			if (aFile.getName().toLowerCase().equals(PROJECT_CONFIG_FILE)) {
				aConfigFile = aFile;
			}
		}
		if (aConfigFile != null) {
			try {
				projectConfiguration = new PropertiesConfiguration(aConfigFile);
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
//		String aConfigFileFullName = aProjectDirectory.getAbsolutePath() + "/" +
	}


}
