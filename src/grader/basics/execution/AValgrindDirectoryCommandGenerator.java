package grader.basics.execution;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
//import framework.project.ClassDescription;
//import framework.project.ClassesManager;
//import framework.project.Project;
import java.util.Map;

import grader.basics.config.BasicConfigurationManagerSelector;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.project.Project;
//import sun.java2d.pipe.hw.AccelGraphicsConfig;
import util.pipe.InputGenerator;
import util.trace.Tracer;
import valgrindpp.codegen.Parser;
import valgrindpp.codegen.Wrapper;
import valgrindpp.grader.Grader;
import valgrindpp.grader.SimpleGrader;
import valgrindpp.helpers.CommandLineHelper;
import valgrindpp.helpers.CompilerHelper;
import valgrindpp.helpers.DockerHelper;

public class AValgrindDirectoryCommandGenerator extends AValgrindCommandGenerator  implements CommandGenerator {

	@Override
	protected void compileStudentCode() throws Exception {
		
			getCompilerHelper().compileStudentCode();
		
	}
	protected String getCommandExecutor() {
		int aNumProcesses = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getNumberOfProcesses();
		if (aNumProcesses == 1) {
			return null;
		}
		String anMPIRunCommand = "mpirun -np " + aNumProcesses;
		return anMPIRunCommand;
	}
protected String[] getBasicTraceCommand( ) {
		
//		String aRelativeTraceFile = CompilerHelper.toRelativePath(studentDir, traceFileName);
		String fullExecName = getBin() + CompilerHelper.EXEC_NAME;
//		String aRedirection = 
//				Tracer.showInfo()?
//						"| tee":
//							">";
		String aTraceFile = getRelativeTraceFileName();
		if (!isCreateDockerContainer()) {
			aTraceFile = getProjectDirectory() + "/" + aTraceFile;
	
		}
		String aCommandExecutor = getCommandExecutor();
		String[] command;
		if (aCommandExecutor != null) {
			command = new String[] {
					"valgrind",
					"--trace-children=yes",
					aCommandExecutor,
//					"mpirun -np 3",
//					"./"+EXEC_NAME,
					"./" + fullExecName,
					redirection(),
					aTraceFile
	//getRelativeTraceFileName()
			};
		} else {
			command = new String[] {
					"valgrind",
					"--trace-children=yes",
//					"mpirun -np 3",
//					"./"+EXEC_NAME,
					"./" + fullExecName,
					redirection(),
					aTraceFile
		};
		}
		
//		String[] command = {
//				"valgrind",
//				"--trace-children=yes",
//				anMPIRunCommand,
////				"mpirun -np 3",
////				"./"+EXEC_NAME,
//				"./" + fullExecName,
//				redirection(),
//				aTraceFile
////getRelativeTraceFileName()
//		};
		return command;
}

	@Override
	void deleteBinary() throws Exception {
		getCompilerHelper().deleteBinary();
		
	}
	
}