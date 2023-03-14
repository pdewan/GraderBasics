package byteman.tools;

import java.util.Set;

import grader.basics.junit.NotAutomatableException;
import grader.basics.junit.TestCaseResult;
import grader.basics.project.NotGradableException;
import grader.basics.project.Project;

public abstract class AbstractBytemanUnitTest extends AbstractBytemanIOTest{
	
//	protected abstract Class<?> getTarget();
//	protected abstract Set<String> getTags();
	protected abstract String getMethodName();
	protected abstract Object[] getArgs();
	protected abstract String [] getRegexes();
	protected abstract boolean testMethodReturnMatches(Object returnValue);
	protected abstract String getExpectedResult();
	
	protected String [] getInputs() {
		return new String[] {""};
	}
	
//	Class target;
//	protected Class<?> getTarget() {
//		if (target == null) {
//		try {
//			String aClassName = InjectionTargeterFactory.getInjectionTargeter().getClassName(getTags());
//			target = Class.forName(aClassName);
//		} catch (Exception e) {
//			e.printStackTrace();
////			return null;
//		}
//		}
//		return target;
//		
//	}
	
	
	@Override
	public TestCaseResult test(Project project, boolean autoGrade) throws NotAutomatableException, NotGradableException {
		TestCaseResult firstResult = super.test(project, autoGrade);
		if(firstResult.isFail())
			return firstResult;

		try {
			BytemanDataServerProxy proxy = BytemanRegistryFactory.getBytemanDataServerProxy();
			Object retval = proxy.getResult(getTarget().getName(), getMethodName());
			
			if(testMethodReturnMatches(retval)) {
				return firstResult;
			}
			return fail("The result does not match the expected result of "+getExpectedResult());
		} catch (Exception e) {
			e.printStackTrace();
			return fail(e.getMessage());
		}
	}	
	
	@Override 
	protected String getTraceOut() {
		if(!exportData(getTarget(),getMethodName(), getArgs())) 
			throw new IllegalArgumentException("Error exporting data");
		return runMain(MethodTester.class,new String[] {getTarget().getName()});
	}
	
	protected boolean exportData(Class<?> target, Object [] targetConstructorArgs, String methodName, Object [] methodArgs) {
		try {
			BytemanDataServerProxy proxy = BytemanRegistryFactory.getAndPossiblySetServerProxy();
			BytemanData data = new BytemanData(target,new BytemanMethodData(methodName,methodArgs));
			data.setConstructorArgs(targetConstructorArgs);
			proxy.addClassData(data);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected boolean exportData(Class<?> target, String methodName, Object ... methodArgs) {
		return exportData(target,new Object[]{},methodName,methodArgs);
	}

}
