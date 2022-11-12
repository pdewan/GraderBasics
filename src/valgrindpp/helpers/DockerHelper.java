package valgrindpp.helpers;

import java.io.File;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.AValgrindCommandGenerator;

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
//		mountDir = AValgrindCommandGenerator.toSingleSlash(mountDir);
//		mountDir = "D:\\dewan_backup\\C\\ValgrindExamples\\Assignment5\\\\\"All, Correct\\\"\\(acorrect\\)\\\\\"Submission attachment\\\"\\(s\\)\\CorrectMutexLRU-No-Config\\CorrectMutexLRU-No-Config"; 
//		mountDir = "D:\\dewan_backup\\C\\ValgrindExamples\\Link"; 
		File aFile = new File(mountDir);
//		if (!aFile.exists()) {
//			System.out.println(" No link");
//		}

//		mountDir = "D:/dewan_backup/C/ValgrindExamples/Assignment5/\"All, Correct\"/\\(acorrect\\)/\"Submission attachment\\(s\\)\"/CorrectMutexLRU-No-Config/CorrectMutexLRU-No-Config";
//		mountDir = "\"D:\\dewan_backup\\C\\ValgrindExamples\\Assignment5\\\\\'All, Correct (acorrect)\\\'\\\\\"Submission attachment(s)\\\"\\CorrectMutexLRU-No-Config\\CorrectMutexLRU-No-Config\\SimpleAdderConfig-wrapper.c\""; 

	//			mountDir = mountDir.replaceAll("/", "\\ ");
//			mountDir = "\'" + mountDir + "'";
//			mountDir = mountDir.replaceAll(" ", "\\\\ ");
//		}
			
		
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
		
		return CommandLineHelper.executeInProcessBuilder(command, null, false, null);
	}
	
	public static int stopContainer() throws Exception {
		if (!createDockerContainer) {
			return 0;
		}
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
		
		return CommandLineHelper.executeInProcessBuilder(command, null, false, null);
	}
}
