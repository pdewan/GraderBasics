package grader.basics.config;

public class BasicConfigurationManagerSelector {
	static BasicConfigurationManager configurationManager;

	public static BasicConfigurationManager getConfigurationManager() {
		if (configurationManager == null) {
			configurationManager = new ABasicConfigurationManager();
		}
		return configurationManager;
	}

	public static void setConfigurationManager(
			BasicConfigurationManager configurationManager) {
		BasicConfigurationManagerSelector.configurationManager = configurationManager;
	}
	

}
