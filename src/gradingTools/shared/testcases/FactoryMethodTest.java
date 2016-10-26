package gradingTools.shared.testcases;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;

import util.annotations.IsExtra;
import util.annotations.MaxValue;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.GradingMode;
import grader.basics.junit.NotesAndScore;
import grader.basics.project.BasicProjectIntrospection;
import gradingTools.shared.testcases.shapes.interfaces.TestLine;
import gradingTools.shared.testcases.shapes.interfaces.TestLocatable;
import gradingTools.shared.testcases.shapes.interfaces.TestPolarLine;
import gradingTools.shared.testcases.shapes.interfaces.TestRotatingLine;

public abstract class FactoryMethodTest extends ProxyTest{
	
	protected String[] factoryClassTags = new String[] {"SingletonsCreator"};
	protected String[] factoryMethodTags = new String[] {};
	protected boolean foundFactoryMethod;
	protected boolean foundFactoryClass;
	protected boolean nullInstantiation;
	protected boolean correctInstantiatedClass;
	protected boolean singletonCheckPassed;
	protected Object[] emptyObjectArgs = new Object[] {};
	protected String factoryMessage = "";
	protected static final double FACTORY_CLASS_CREDIT = 0.2;
	protected static final double FACTORY_METHOD_CREDIT = 0.2;
	protected static final double FACTORY_OBJECT_CREDIT = 0.2;
	protected static final double FACTORY_OBJECT_CLASS_CREDIT = 0.2;
	protected static final double FACTORY_OBJECT_SINGLETON_CREDIT = 0.2;
	boolean tryConstructor = false;

	protected double factoryCredit;
	
	protected boolean tryConstructor() {
		return true;
	}

	protected double factoryClassCredit() {
		return FACTORY_CLASS_CREDIT;
	}
	protected double factoryMethodCredit() {
		return FACTORY_METHOD_CREDIT;
	}
	protected double factoryObjectCredit() {
		return FACTORY_OBJECT_CREDIT;
	}
	protected double factoryObjectClassCredit() {
		return FACTORY_OBJECT_CLASS_CREDIT;
	}
	protected double factoryObjectSingletonCredit() {
		return FACTORY_OBJECT_SINGLETON_CREDIT;
	}
	
	protected String[] factoryClassTags() {
		return factoryClassTags;
	}
	
	protected String[] factoryMethodTags() {
		return factoryMethodTags;
	}
	protected boolean doSingletonCheck(Object aFirstInstantiation) {
		Object aSecondCreation = createUsingFactoryMethod();
		singletonCheckPassed = aSecondCreation == aFirstInstantiation;
		if (!singletonCheckPassed) {
			factoryMessage = "First factory method return value " + 
						aFirstInstantiation + " != " + aSecondCreation;
		} else {
			factoryCredit += factoryObjectSingletonCredit();

		}
		return singletonCheckPassed;
	}
	
	protected boolean doFactoryMethodTest() {
		createUsingFactoryMethod();
		doSingletonCheck(rootProxy());
		fractionComplete = factoryCredit;
		if (factoryCredit != 1.0) {
			assertTrue(factoryMessage, false);
			
		}
		
		return true;
	}
	
	protected Object maybeUseConstructor(Class aProxyClass){
		if (tryConstructor()) {
			System.out.println (factoryMessage);
		return assertingCreateInstance(aProxyClass);
		} else {
			assertTrue(factoryMessage, false );
			return null;
		}
	}
	
	protected Object assertingCreateInstance(Class aProxyClass) {
		System.out.println("Trying parameterless constructor");

		Object retVal = BasicProjectIntrospection.createInstance(aProxyClass);
		maybeAssertNoClass(aProxyClass, retVal);
		return retVal;
	}
	
	protected Object getOrCreateObject (String[] factoryClassTag, String[] factoryMethodTag, Class instantiatedTypeClass) {
		Class<?> factoryClass = BasicProjectIntrospection.findClassByTags(factoryClassTag);
		foundFactoryClass = factoryClass != null;
		factoryMessage = "";
		factoryCredit = 0;
		if (!foundFactoryClass) {
			factoryMessage = 
				factoryMessage = "Factory class:" + Arrays.toString(factoryClassTag) + " not found.";	
			return maybeUseConstructor(instantiatedTypeClass);
		}
		factoryCredit += factoryClassCredit();
		Method factoryMethod =	BasicProjectIntrospection.findUniqueMethodByTag(
				factoryClass, factoryMethodTag);
		foundFactoryMethod = factoryMethod != null;
		if (!foundFactoryMethod) {
			factoryMessage = "Factory method:" + Arrays.toString(factoryMethodTag) + " not found.";			
			return maybeUseConstructor(instantiatedTypeClass);
			
			
		}
		factoryCredit += factoryMethodCredit();

		
		Object anInstance = BasicProjectExecution.timedInvoke(factoryClass, factoryMethod, emptyObjectArgs);
		nullInstantiation = anInstance == null;
		if (nullInstantiation) {
			factoryMessage = "Factory method returns null object";			
			return maybeUseConstructor(instantiatedTypeClass);
//			return assertingCreateInstance(instantiatedTypeClass);
			
		}
		factoryCredit += factoryObjectCredit();

		Class aReturnedClass = anInstance.getClass();
		
		Class anExpectedClass = BasicProjectIntrospection.findClass(instantiatedTypeClass);
		
		correctInstantiatedClass = aReturnedClass == null || anExpectedClass == null ||
				anExpectedClass.isInstance(anInstance);
//					aReturnedClass.equals(anExpectedClass);
		
		if (!correctInstantiatedClass) {
			factoryMessage = "Factory method returns instance of" + aReturnedClass + " instead of " + anExpectedClass;			
			return maybeUseConstructor(instantiatedTypeClass);

//			return assertingCreateInstance(instantiatedTypeClass);
		}
		factoryCredit += factoryObjectClassCredit();

		Object aProxy = BasicProjectExecution.maybeReturnProxy(anInstance, instantiatedTypeClass);
	
		return aProxy;
		
	}
	protected Object createUsingFactoryMethod() {
		rootProxy = getOrCreateObject(factoryClassTags(), factoryMethodTags(), proxyClass());
		return rootProxy;
	}	
	@Override
	protected void executeOperations(Object aProxy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void setActual(Object aProxy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected boolean checkOutput(Object aLocatable) {
		// TODO Auto-generated method stub
		return false;
	}



}
