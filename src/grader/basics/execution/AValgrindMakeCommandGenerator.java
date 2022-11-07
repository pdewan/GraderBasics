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
import sun.java2d.pipe.hw.AccelGraphicsConfig;
import util.pipe.InputGenerator;
import util.trace.Tracer;
import valgrindpp.codegen.Parser;
import valgrindpp.codegen.Wrapper;
import valgrindpp.grader.Grader;
import valgrindpp.grader.SimpleGrader;
import valgrindpp.helpers.CommandLineHelper;
import valgrindpp.helpers.CompilerHelper;
import valgrindpp.helpers.DockerHelper;

public class AValgrindMakeCommandGenerator extends AValgrindCommandGenerator  implements CommandGenerator {

	@Override
	protected void compileStudentCode() throws Exception {
		
			ch.make();
		
	}
protected String[] getBasicTraceCommand( ) {
		List<String> aBasicCommandList = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getBasicCommand();
		int numFixedCommands = 4;
		String[] command = new String[aBasicCommandList.size() + 4];
		command[0] = "valgrind";
		command[1]	= "--trace-children=yes";
		command[command.length - 2] = redirection();
		String aRelativeTraceFile = CompilerHelper.toRelativePath(studentDir, traceFileName);

		command[command.length - 1] = aRelativeTraceFile;
		for (int anIndex = 0; anIndex < aBasicCommandList.size(); anIndex++) {
			command[2 + anIndex] = aBasicCommandList.get(anIndex);
		}

	
		return command;
}
//	ch.trace(new String[]{"./lru-mutex-wrapped", "-c", "2"});

	@Override
	void deleteBinary() throws Exception {
		ch.makeClean();
		
	}
	
}