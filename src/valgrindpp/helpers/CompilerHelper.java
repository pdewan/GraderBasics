package valgrindpp.helpers;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CompilerHelper {
	static final String CC = "gcc";
	static final String FLAGS = "-pthread";
	public static final String EXEC_NAME = "WrappedStudentCode";
	static  String fullExecName;
	
	private String studentDir, objFile, cFile, traceFile;
	private String src, bin;
	
	public static String toRelativePath (String aParentAbsolutePath, String aChildAbsolutePath) {
		URI aParentFile = new File(aParentAbsolutePath).toURI() ;
		URI aChildFile = new File(aChildAbsolutePath).toURI();
		return aParentFile.relativize(aChildFile).getPath();
		
		
	}
	
	public CompilerHelper(String configFile, String studentDir, String traceFile) {
		this.studentDir = studentDir;
		this.objFile = configFile + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".o";
		this.cFile = configFile + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".c";
		this.traceFile = traceFile;
	}
	public CompilerHelper(String anSrc, String aBin, String configFile, String studentDir, String traceFile) {
//		src = toRelativePath(studentDir, aSourceFolder) ;
//		bin = toRelativePath(studentDir, aBuildFolder);
		src = anSrc;
		bin = aBin;
		
		this.studentDir = studentDir;
		this.objFile = bin  + configFile + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".o";
		
		this.cFile = src +  configFile + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".c";
//		this.cFile =  configFile + valgrindpp.main.Main.WRAPPER_FILE_SUFFIX + ".c";
		this.traceFile = traceFile;
	}
	
	public int compileWrapper() throws Exception {	
//		File aFile = new File(cFile);
//		String aFileName = cFile.replaceAll("\\\\", "/");
		String[] command = {CC, "-c", FLAGS, cFile, "-o", objFile};
//		String[] command = {"cd", src, ";", 
//				CC, "-c", FLAGS, cFile, "-o", objFile, ";",
//				"cd", studentDir};

//		String[] command = {CC, "-c", FLAGS, aFileName, "-o", objFile};

		
		return CommandLineHelper.executeInDocker(command);
	}
	
	public int compileStudentCode() throws Exception {
//		File dir = new File(studentDir);
		File dir = new File(studentDir + "/" + src);

		
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

		return CommandLineHelper.executeInDocker(command);
	}
	
	public int deleteWrapperCFile() throws Exception {
		return CommandLineHelper.delete(Paths.get(studentDir, cFile));
	}
	
	public int deleteWrapperObjFile() throws Exception {
		return CommandLineHelper.delete(Paths.get(studentDir, objFile));
	}
	
	public int deleteBinary() throws Exception {
		return CommandLineHelper.delete(Paths.get(studentDir, EXEC_NAME));
	}
	
	public int deleteTraces() throws Exception {
		return CommandLineHelper.delete(Paths.get(studentDir, traceFile));
	}
	
//	public String[] getTraceCommand( ) {
//		String aRelativeTraceFile = toRelativePath(studentDir, traceFile);
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
		String aRelativeTraceFile = toRelativePath(studentDir, traceFile);
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
		
		return CommandLineHelper.executeInDocker(command);
	}
	
	public int trace(String testfile) throws Exception {
		InputStream stream = CompilerHelper.class.getResourceAsStream("/"+testfile);
		
		String[] command = {
				"valgrind",
				"--trace-children=yes",
				"./"+EXEC_NAME,
				">",
				traceFile
		};
		
		return CommandLineHelper.executeInDocker(command, false, stream);
	}
	
	public int trace(String[] command) throws Exception {
		String[] fullCommand = new String[command.length + 4];
		
		fullCommand[0] = "valgrind";
		fullCommand[1] = "--trace-children=yes";
		for(int i=0; i<command.length; i++) {
			fullCommand[i+2] = command[i];
		}
		fullCommand[command.length+2] = ">";
		fullCommand[command.length+3] = traceFile;
		
		return CommandLineHelper.executeInDocker(fullCommand);
	}
	
	public int make() throws Exception {
		String[] command = {
				"make",
				"wrapped"
		};
		
		return CommandLineHelper.executeInDocker(command);
	}
	
	public int makeClean() throws Exception {
		String[] command = {
				"make",
				"clean"
		};
		
		return CommandLineHelper.executeInDocker(command);
	}
}
	
