package byteman.tools.exampleTestCases.mvcSummer;

import byteman.tools.AbstractBytemanIOTest;

public class SequentialIOCorrectnessTest extends AbstractBytemanIOTest{
	String [] regexes = {
			".*[Pp]lease.*list.*integers.*",
			"500500"
	};

	@Override
	protected Class<?> getTarget() {
		return getRegistry().getMVCSequentialMain();
	}

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
}
