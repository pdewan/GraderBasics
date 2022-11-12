package grader.basics.execution;


import java.util.List;

import grader.basics.config.BasicExecutionSpecificationSelector;

public class AValgrindMakeCommandGenerator extends AValgrindCommandGenerator  implements CommandGenerator {

	@Override
	protected void compileStudentCode() throws Exception {
		
			getCompilerHelper().make();
		
	}
protected String[] getBasicTraceCommand( ) {
		List<String> aBasicCommandList = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getBasicCommand();
		int numFixedCommands = 4;
		String[] command = new String[aBasicCommandList.size() + 4];
		command[0] = "valgrind";
		command[1]	= "--trace-children=yes";
		command[command.length - 2] = redirection();
		String aRelativeTraceFile = getRelativeTraceFileName();
				
//				CompilerHelper.toRelativePath(studentDir, traceDockerFileName);

		command[command.length - 1] = aRelativeTraceFile;
		for (int anIndex = 0; anIndex < aBasicCommandList.size(); anIndex++) {
			command[2 + anIndex] = aBasicCommandList.get(anIndex);
		}

	
		return command;
}
//	ch.trace(new String[]{"./lru-mutex-wrapped", "-c", "2"});

	@Override
	void deleteBinary() throws Exception {
		getCompilerHelper().makeClean();
		
	}
	
}