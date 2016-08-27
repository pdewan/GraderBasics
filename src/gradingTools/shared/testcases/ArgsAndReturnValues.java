package gradingTools.shared.testcases;

import java.util.ArrayList;

public class ArgsAndReturnValues {
	ArrayList<Object[]> argSets = new ArrayList<Object[]>();
	ArrayList<Object> retVals = new ArrayList<Object>();

	public void addArgsAndReturnValues(Object[] args, Object retVal) {
		argSets.add(args);
		retVals.add(retVal);
	}

	public Object[][] getArgumentSets() {
		return argSets.toArray(new Object[0][0]);
	}

	public Object[] getReturnValues() {
		return retVals.toArray();
	}
}
