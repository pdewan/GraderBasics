package grader.basics.project;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import org.junit.Assert;

import grader.basics.execution.BasicProjectExecution;
import util.trace.Tracer;

public class AGradedClassProxyInvocationHandler implements InvocationHandler {
	Class actualClass;
	Object actualObject;
	public AGradedClassProxyInvocationHandler (Object anActualObject) {
		actualObject = anActualObject;
		if (actualObject != null) {
		actualClass = anActualObject.getClass();
		}
	}

	@Override
	public Object invoke(Object proxy, Method aProxyMethod, Object[] args)
			throws Throwable {
		Class aReturnType = aProxyMethod.getReturnType();
		Method anActualMethod = BasicProjectIntrospection.findMethod(actualClass, aProxyMethod);
		Assert.assertTrue("Method matching " + aProxyMethod.getName() + " not found in class: " + actualClass, anActualMethod != null);
		// This will never be executed
//		if (anActualMethod == null) {
//			return null;
//		}
		if (args != null) {
		Integer[] aPermutedIndices= BasicProjectIntrospection.getArgIndices(actualClass, aProxyMethod);
		if (aPermutedIndices != null) {
			Object[] anOriginalArgs = Arrays.copyOf(args, args.length);
			for (int i = 0; i < args.length; i++) {
				args[i] = anOriginalArgs[aPermutedIndices[i]];
				
			}
		}
		for (int i = 0; i < args.length; i++) {
			args[i] = BasicProjectExecution.maybeGetActual(args[i]);
//			args[i] = BasicProjectExecution.maybeGetProxy(args[i]);
//			if (args[i] instanceof Proxy) {
//				Object anActualObject = BasicProjectIntrospection.getRealObject(args[i]);
//				if (anActualObject == null) {
//					Tracer.error("Could not get real object for proxy:" + args[i]);
//				}
//				args[i] = anActualObject;
//			}
		}
		}
		Object aRetVal = BasicProjectExecution.timedInvoke(actualObject, anActualMethod, args);
		if (aRetVal == null)
			return aRetVal;
		 
		if (!BasicProjectIntrospection.isPredefinedType(aRetVal.getClass())) {
			Object aProxy = BasicProjectIntrospection.getProxyObject(aRetVal);
			if (aProxy != null)
				return aProxy;
			if (aReturnType.isArray()) {
				   return BasicProjectExecution.returnArrayOfProxies(aRetVal, aReturnType);
			   }
			 aProxy = BasicProjectIntrospection.createProxy(aProxyMethod.getReturnType(), aRetVal);
			 if (aProxy != null) // it should always be non null
				 return aProxy;
			 
			
			
		}
		return aRetVal;
		
	}

}
