package valgrindpp.helpers;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;

public class DockerHelper {
//	public static final String IMAGE_NAME = "nalingaddis/valgrind";
	public static final String IMAGE_NAME = BasicExecutionSpecificationSelector.
			getBasicExecutionSpecification().getValgrindImage();

//	public static final String CONTAINER_NAME = "grader-container";
	public static final String CONTAINER_NAME =  BasicExecutionSpecificationSelector.
			getBasicExecutionSpecification().getDockerContainer();
	//public static final String DOCKER_PATH = "/usr/local/bin/docker";
//	public static final String DOCKER_PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker.exe";	
//	public static final String DOCKER_PATH = "docker";
	public static final String DOCKER_PATH =
			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getDockerPath();
	




	public static int startContainer() throws Exception {
		return createContainer(System.getProperty("user.dir"));
	}
	
	protected String dockerPath() {
		return DOCKER_PATH;
	}
	protected String containerName() {
		return CONTAINER_NAME;
	}
	protected String imageName() {
		return IMAGE_NAME;
	}
	
	public static int createContainer(String mountDir) throws Exception {
		String[] command = new String[]{
				DOCKER_PATH, 
				"run",
				"-it",
				"-d",
				"--mount", 
				"type=bind,src="+mountDir+",target=/home",
				"--name",
				CONTAINER_NAME,
				IMAGE_NAME
		};
		
		return CommandLineHelper.execute(command);
	}
	
	public static int stopContainer() throws Exception {
		String[] command = {
				DOCKER_PATH,
				"stop",
				CONTAINER_NAME
		};
		
		return CommandLineHelper.execute(command);
	}
	
	public static int deleteContainer() throws Exception {
		String[] command = {
				DOCKER_PATH,
				"rm",
				"-f",
				CONTAINER_NAME
		};
		
		return CommandLineHelper.execute(command);
	}
}
