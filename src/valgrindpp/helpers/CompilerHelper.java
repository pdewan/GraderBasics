package valgrindpp.helpers;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.trace.Tracer;

public class CompilerHelper {
	static final String CC = "gcc";
	static final String FLAGS = "-pthread";
	public static final String EXEC_NAME = "WrappedStudentCode";
	private static  String fullExecName;
	
	private String executionDirectory, objFile, cFile, traceFile;
	private String src, bin;
	
	
	public static String toRelativePath (String aParentAbsolutePath, String aChildAbsolutePath) {
		URI aParentFile = new File(aParentAbsolutePath).toURI() ;
		URI aChildFile = new File(aChildAbsolutePath).toURI();
		return aParentFile.relativize(aChildFile).getPath();
		
		
	}
	
	public CompilerHelper(String configFile, String anExecutionDir, String traceFile) {
		this.executionDirectory = anExecutionDir;
		this.objFile = configFile + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".o";
		this.cFile = configFile + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".c";
		this.traceFile = traceFile;
	}
	public CompilerHelper(String anSrc, String aBin, String configFile, String studentDir, String traceFile) {
//		src = toRelativePath(executionDirectory, aSourceFolder) ;
//		bin = toRelativePath(executionDirectory, aBuildFolder);
		src = anSrc;
		bin = aBin;
		
		this.executionDirectory = studentDir;
		this.objFile = bin  + configFile + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".o";
		
		this.cFile = src +  configFile + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".c";
//		this.cFile =  configFile + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".c";
		this.traceFile = traceFile;
	}
	
	public int compileWrapper() throws Exception {	

		String[] command = {CC, "-c", FLAGS, cFile, "-o", objFile};

		Tracer.info(this, "Compiling wrapper in " + executionDirectory + " using command:" + Arrays.toString(command));
		
		return CommandLineHelper.execute(command, executionDirectory);
	}
	
	public int compileStudentCode() throws Exception {
		File dir = new File(executionDirectory + "/" + src);

		
		List<String> srcFiles = new ArrayList<String>();
		
		for(String filename: dir.list()) {
			if(filename.endsWith(".c")) {
				srcFiles.add(src + filename);
			}
		}
		
		String[] command = new String[srcFiles.size() + 5];
		
		command[0] = CC;
		command[1] = FLAGS;
		command[2] = objFile;
		command[3] = "-o";
		fullExecName = bin + EXEC_NAME;
//		command[4] = EXEC_NAME;
		command[4] = fullExecName;

		
		for(int i=0; i<srcFiles.size(); i++) {
			command[i+5] = srcFiles.get(i);
		}
		Tracer.info(this, "Compiling student code in " + executionDirectory + " using command:" + command);
		return CommandLineHelper.execute(command, executionDirectory);
	}
	
	public int deleteWrapperCFile() throws Exception {
		return CommandLineHelper.delete(Paths.get(executionDirectory, cFile));
	}
	
	public int deleteWrapperObjFile() throws Exception {
		return CommandLineHelper.delete(Paths.get(executionDirectory, objFile));
	}
	
	public int deleteBinary() throws Exception {
		return CommandLineHelper.delete(Paths.get(executionDirectory, EXEC_NAME));
	}
	
	public int deleteTraces() throws Exception {
		return CommandLineHelper.delete(Paths.get(executionDirectory, traceFile));
	}
	
//	public String[] getTraceCommand( ) {
//		String aRelativeTraceFile = toRelativePath(executionDirectory, traceFile);
//		String[] command = {
//				"valgrind",
//				"--trace-children=yes",
////				"./"+EXEC_NAME,
//				"./" + fullExecName,
//				"| tee",
////				">",
////				traceFile
//				aRelativeTraceFile
//		};
//		return CommandLineHelper.getDockerCommand(command, silent, input)
//	}
	
	public int trace() throws Exception {
		String aRelativeTraceFile = toRelativePath(executionDirectory, traceFile);
		String[] command = {
				"valgrind",
				"--trace-children=yes",
//				"./"+EXEC_NAME,
				"./" + fullExecName,
				"| tee",
//				">",
//				traceFile
				aRelativeTraceFile
		};
		
		return CommandLineHelper.execute(command, executionDirectory);
	}
	
//	public int trace(String testfile) throws Exception {
//		InputStream stream = CompilerHelper.class.getResourceAsStream("/"+testfile);
//		
//		String[] command = {
//				"valgrind",
//				"--trace-children=yes",
//				"./"+EXEC_NAME,
//				">",
//				traceFile
//		};
//		
//		return CommandLineHelper.executeInDocker(command, false, stream);
//	}
//	
	public int trace(String[] command) throws Exception {
		String[] fullCommand = new String[command.length + 4];
		
		fullCommand[0] = "valgrind";
		fullCommand[1] = "--trace-children=yes";
		for(int i=0; i<command.length; i++) {
			fullCommand[i+2] = command[i];
		}
		fullCommand[command.length+2] = ">";
		fullCommand[command.length+3] = traceFile;
		
		return CommandLineHelper.executeInDocker(fullCommand, false, null);
	}
	
	public int make() throws Exception {
		String[] command = {
				"make",
				"wrapped"
		};
		Tracer.info(this, "Executing in " + executionDirectory + "make command: " +  Arrays.toString(command));
		
		return CommandLineHelper.execute(command, executionDirectory);
	}
	
	public int makeClean() throws Exception {
		String[] command = {
				"make",
				"clean"
		};
		Tracer.info(this, "Executing in " + executionDirectory + "make command: " +  Arrays.toString(command));

		return CommandLineHelper.execute(command, executionDirectory);
	}
}
	
