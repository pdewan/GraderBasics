package valgrindpp.helpers;

import java.io.File;
import java.util.Arrays;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.AValgrindCommandGenerator;
import util.trace.Tracer;

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
	private static boolean createDockerContainer = 
			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCreateDockerContainer();
	
	




	public static int startContainer() throws Exception {
		if (!createDockerContainer) {
			return 0;
		}
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
		if (!createDockerContainer) {
			return 0;
			
		}

		File aFile = new File(mountDir);

			
		
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
		Tracer.info(DockerHelper.class, "Creating Docker Container from command:" + Arrays.toString(command));

		
		return CommandLineHelper.executeInProcessBuilder(command, null, false, null);
	}
	
	public static int stopContainer() throws Exception {
		if (!createDockerContainer) {
			return 0;
		}
		Tracer.info(DockerHelper.class, "Stopping Docker Container");

		String[] command = {
				DOCKER_PATH,
				"stop",
				CONTAINER_NAME
		};
		
		return CommandLineHelper.executeInProcessBuilder(command, null, false, null);
	}
	
	public static int deleteContainer() throws Exception {
		if (!createDockerContainer) {
			return 0;
		}

		String[] command = {
				DOCKER_PATH,
				"rm",
				"-f",
				CONTAINER_NAME
		};
		Tracer.info(DockerHelper.class, "Deleting Docker Container using command:" + Arrays.toString(command));

		
		return CommandLineHelper.executeInProcessBuilder(command, null, false, null);
	}
}
