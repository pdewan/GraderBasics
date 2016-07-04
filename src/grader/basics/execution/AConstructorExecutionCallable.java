package grader.basics.execution;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

import util.misc.Common;

public class AConstructorExecutionCallable implements Callable{
	Constructor constructor;
//	Object object;
	Object[] args;
	public AConstructorExecutionCallable(Constructor aConstructor, Object[] anArgs) {
		constructor = aConstructor;
//		object = anObject;
		args = anArgs;
	}
	@Override
	public Object call() throws Exception {
		System.out.println ("calling constructor: " + constructor + " " + Common.toString(args));
		Object retVal = constructor.newInstance(args);
		System.out.println ("called constructor: " + constructor + " " + Common.toString(args));

		return retVal;
	}
	

}
