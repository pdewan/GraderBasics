package byteman.tools.exampleTestCases.mvcSummer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import byteman.tools.AbstractBytemanIOTest;

public class ParallelIOCorrectnessTest extends AbstractBytemanIOTest{

	String [] regexes = {
			".*[Pp]lease.*list.*integers.*",
			"500500"
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
