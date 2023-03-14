package byteman.tools.exampleTestCases.mvcSummer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import byteman.tools.AbstractBytemanIOTest;

public class MVCParallelAdderTest extends AbstractBytemanIOTest{

	String [] regexes = {
			
			".*Model.*init.*",
			
			".*View.*init.*",
			".*Model.*params:.*View.*",
			
			".*View.*init.*",
			".*Model.*params:.*View.*",
			
			".*Controller.*init.*params:.*Model.*",
			".*Controller.*start.*",
			
			".*Thread[0-9].*called.*finish.*(?:31375|93875|156375|218875).*",
			".*Thread[0-9].*called.*finish.*(?:31375|93875|156375|218875).*",
			".*Thread[0-9].*called.*finish.*(?:31375|93875|156375|218875).*",
			".*Thread[0-9].*called.*finish.*(?:31375|93875|156375|218875).*",
			".*(?:Thread[0-9].*exited finish|main.*exited join.*500500).*",
			".*(?:Thread[0-9].*exited finish|main.*exited join.*500500).*",
			".*(?:Thread[0-9].*exited finish|main.*exited join.*500500).*",
			".*(?:Thread[0-9].*exited finish|main.*exited join.*500500).*",
			".*(?:Thread[0-9].*exited finish|main.*exited join.*500500).*",
			
			".*View.*propertyChange.*",		
			".*View.*propertyChange.*",
	};

//	@Override
//	protected Class<?> getTarget() {
//		return getRegistry().getMVCParallelMain();
//	}

	@Override
	protected Object[] getArgs() {
		return null;
	}

	@Override
	protected String[] getRegexes() {
		return regexes;
	}

	@Override
	protected String[] getInputs() {
		StringBuilder retval = new StringBuilder();
		for(int i=1;i<=1000;i++)
			retval.append(i+",");
		retval.deleteCharAt(retval.length()-1);
		return new String [] {retval.toString(),"quit"};
	}
	String[] tagsArray = {
			"MVCParallelMain"
	};
	Set<String> tagsSet = new HashSet(Arrays.asList(tagsArray));
	@Override
	protected Set<String> getTags() {
		return tagsSet;
	}
}
