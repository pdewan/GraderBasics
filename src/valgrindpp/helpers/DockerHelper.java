package valgrindpp.helpers;

public class DockerHelper {
	public static final String IMAGE_NAME = "nalingaddis/valgrind";
	public static final String CONTAINER_NAME = "grader-container";
	public static final String DOCKER_PATH = "/usr/local/bin/docker";
	
	public static int startContainer() throws Exception {
		return createContainer(System.getProperty("user.dir"));
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
