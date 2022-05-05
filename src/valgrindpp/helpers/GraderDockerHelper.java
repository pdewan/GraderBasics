package valgrindpp.helpers;

public class GraderDockerHelper {
	public static final String IMAGE_NAME = "nalingaddis/valgrind";
	public static final String CONTAINER_NAME = "grader-container";
	//public static final String DOCKER_PATH = "/usr/local/bin/docker";
	public static final String DOCKER_PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker.exe";	
	public static int startContainer() throws Exception {
		return createContainer(System.getProperty("user.dir"));
	}
	public static String dockerPath() {
		return DOCKER_PATH;
	}
	public static String containerName() {
		return CONTAINER_NAME;
	}
	public static String imageName() {
		return IMAGE_NAME;
	}
	
	
	public static int createContainer(String mountDir) throws Exception {
		String[] command = new String[]{
				dockerPath(), 
				"run",
				"-it",
				"-d",
				"--mount", 
				"type=bind,src="+mountDir+",target=/home",
				"--name",
				containerName(),
				imageName()
		};
		
		return CommandLineHelper.execute(command);
	}
	
	public static int stopContainer() throws Exception {
		String[] command = {
				dockerPath(),
				"stop",
				containerName()
		};
		
		return CommandLineHelper.execute(command);
	}
	
	public static int deleteContainer() throws Exception {
		String[] command = {
				dockerPath(),
				"rm",
				"-f",
				containerName()
		};
		
		return CommandLineHelper.execute(command);
	}
}
