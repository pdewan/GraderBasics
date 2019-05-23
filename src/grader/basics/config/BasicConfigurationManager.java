package grader.basics.config;

import java.io.File;

import org.apache.commons.configuration.PropertiesConfiguration;

public interface BasicConfigurationManager {
	public  PropertiesConfiguration getProjectConfiguration() ;
	public void setProjectConfiguration(PropertiesConfiguration aPropertiesConfiguration);
	void createProjectConfiguration(File aProjectDirectory);
}
