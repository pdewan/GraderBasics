package valgrindpp.helpers;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

import grader.basics.config.BasicExecutionSpecificationSelector;

public class CommandLineHelper {
	private static boolean createDockerContainer = 
			BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getCreateDockerContainer();
	
//	public static int execute(String[] command) throws Exception {
//		return execute(command, false, null);
//	}
	
	public static int executeInProcessBuilder(String[] command, String anExecutionDirectory,  boolean silent, InputStream input) throws Exception{
		if(!silent) System.out.println("Executing: " + String.join(" ", command));
		
		ProcessBuilder pb = new ProcessBuilder();
		if (anExecutionDirectory != null && command[0] != DockerHelper.DOCKER_PATH) {
			pb.directory(new File(anExecutionDirectory));
		}
		
		if(silent) {
			pb.command(command);
		} else {
			pb.command(command).inheritIO();
		}
		
		Process process = pb.start();
		
		if(input != null) {
//			input.transferTo(process.getOutputStream());
			System.setIn(input);
			input.close();
		}
		
		return process.waitFor();
	}
	
	public static int execute(String[] command, String anExecutionDirectory) throws Exception {
		String[] anExecutionCommand = getExecutionCommand(command, anExecutionDirectory);
		return executeInProcessBuilder(anExecutionCommand, anExecutionDirectory, false, null);
//		return executeInDocker(command, false, null);
	}
	
	
	public static int executeInDocker(String[] command, boolean silent, InputStream input) throws Exception {
		String[] dockerCommand = {
				DockerHelper.DOCKER_PATH,
				"exec",
				DockerHelper.CONTAINER_NAME,
				"sh",
				"-c",
				String.join(" ", command)
		};
		
		return executeInProcessBuilder(dockerCommand,  null, silent, input);
	}
	public static  String[] getExecutionCommand(String[] command, String anExecutionDirectory) throws Exception {
	if (createDockerContainer)	{
		return CommandLineHelper.getDockerCommand(command);
	}
	return CommandLineHelper.getDirectCommand(command, anExecutionDirectory);
	}
	public static String[] getDockerCommand(String[] command) throws Exception {
		String[] dockerCommand = {
				DockerHelper.DOCKER_PATH,
				"exec",
				DockerHelper.CONTAINER_NAME,
				"sh",
				"-c",
				String.join(" ", command)};
		
		return dockerCommand;
		};
		public static String[] getDirectCommand(String[] command, String anExecutionDirectory) throws Exception {
//			String[] retVal = new String[command.length + 3];
//			retVal[0] = "cd";
//			retVal[1] = anExecutionDirectory;
//			retVal[2] = ";";
//			for (int anIndex = 0; anIndex < command.length; anIndex++) {
//				retVal[3 + anIndex] = command[anIndex];
//			}
			return command;
			
		};
//	public static String[] getExecutionCommand(String[] command, String anExecutionDirectory) throws Exception {
//		String[] dockerCommand = {
//				DockerHelper.DOCKER_PATH,
//				"exec",
//				DockerHelper.CONTAINER_NAME,
//				"sh",
//				"-c",
//				String.join(" ", command)
//		};
//		
//		return dockerCommand;
//	}
	
	public static int delete(Path filepath) throws Exception {
		File aFile = filepath.toFile();
		aFile.delete();
		return 0;
	}
	
//	public static int deleteUsingRm(Path filepath) throws Exception {
//		String[] command = {
//				"rm",
//				"-f",
//				filepath.toString()
//		};
//		
//		return execute(command);
//	}
	
	
//	public static int deleteInDocker(Path filepath) throws Exception {
//		String[] command = {
//				"rm",
//				"-f",
//				filepath.toString()
//		};
//		
//		return execute(command);
//	}
}
