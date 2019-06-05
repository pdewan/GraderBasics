package grader.basics.config;

import java.io.File;

import org.apache.commons.configuration.PropertiesConfiguration;

public interface BasicConfigurationManager {
	public  PropertiesConfiguration getOrCreateProjectConfiguration() ;
	public void setProjectConfiguration(PropertiesConfiguration aPropertiesConfiguration);
	void createProjectConfiguration(File aProjectDirectory);
//	void setProjectDirectory(File aProjectDirectory);
//	File getProjectDirectory();
	PropertiesConfiguration getProjectConfiguration();
}
