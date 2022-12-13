package byteman.tools.exampleTestCases.mvcSummer;

import byteman.tools.AbstractBytemanIOTest;

public class MVCSequentialAdderTest extends AbstractBytemanIOTest{

	String [] regexes = {
			".*Model.*init.*",
			
			".*View.*init.*",
			".*Model.*params:.*View.*",
			
			".*View.*init.*",
			".*Model.*params:.*View.*",
			
			".*Controller.*init.*params:.*Model.*",
			".*Controller.*start.*",

			".*View.*propertyChange.*",		
			".*View.*propertyChange.*",
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
	