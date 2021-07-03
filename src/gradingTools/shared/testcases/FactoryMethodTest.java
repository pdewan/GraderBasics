package gradingTools.shared.testcases;

import java.lang.reflect.Method;
import java.util.Arrays;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.project.BasicProjectIntrospection;
import util.trace.Tracer;

public abstract class FactoryMethodTest extends ProxyTest{
	
	protected String[] factoryClassTags = new String[] {"SingletonsCreator"};
	protected Class factoryClass;
	protected String[] factoryMethodTags = new String[] {};
	protected String factoryMethodName = "";
	protected Method factoryMethod;
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
	
	protected boolean isSingleton() {
		return true;
	}

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
	
	protected Class getFactoryClass() {
		if (factoryClass == null) {
			factoryClass = factoryClass();
		}
		return factoryClass;
	}
	
	protected Class factoryClass() {
		return factoryClass;
	}	
	protected String[] factoryMethodTags() {
		return factoryMethodTags;
	}
	protected String factoryMethodName() {
		return factoryMethodName;
	}
	protected Method getFactoryMethod() {
		if (factoryMethod == null) {
			factoryMethod = factoryMethod();
		}
		return factoryMethod;
	}
	protected Method factoryMethod() {
		if (factoryClass == null) {
			return null;
		}
		try {
			return factoryClass.getMethod(factoryMethodName());
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}
	protected boolean doSingletonCheck(Object aFirstInstantiation) {
		if (!isSingleton()) {
			return true;
		}
//		Object aSecondCreation = createUsingFactoryClassAndMethodTags();
		Object aSecondCreation = createUsingFactory();

		singletonCheckPassed = (aSecondCreation != null )  && ( aSecondCreation == aFirstInstantiation);
		if (!singletonCheckPassed) {
			factoryMessage = "Factory class is not a singleton, first factory method return value " + 
						aFirstInstantiation + " != second one" + aSecondCreation;
		} else {
			factoryCredit += factoryObjectSingletonCredit();

		}
		return singletonCheckPassed;
	}
	
	protected void addFactoryMethodCredit() {
		fractionComplete = factoryCredit;
	}
	
	 
	protected void doCheck() {
		addFactoryMethodCredit();
		if (factoryCredit != 1.0 && !factoryMessage.isEmpty()) {
			assertTrue(factoryMessage, false);
			
		}
	}
	protected boolean doFactoryMethodTest() {
//		createUsingFactoryClassAndMethodTags();
//		factoryMethodTags = factoryMethodTags();
		createUsingFactory();
		doSingletonCheck(rootProxy());
//		fractionComplete = factoryCredit;
		doCheck();
//		addFactoryMethodCredit();
//		if (factoryCredit != 1.0) {
//			assertTrue(factoryMessage, false);
//			
//		}
		
		return true;
	}
	
	protected Object maybeUseConstructor(Class aProxyClass){
		if (tryConstructor()) {
			Tracer.info (this, factoryMessage);
		return assertingCreateInstance(aProxyClass);
		} else {
			assertTrue(factoryMessage, false );
			return null;
		}
	}
	
	static Object[] anEmptyArgs = {};
	
	protected Object assertingCreateInstance(Class aProxyClass) {
		Tracer.info (this, "Trying parameterless constructor");

		Object retVal = BasicProjectIntrospection.createInstance(aProxyClass);
		maybeAssertNoClass(aProxyClass, anEmptyArgs, retVal);
		return retVal;
	}
	public static String[] emptyStringArray = {};
	protected String[] proxyClassTags() {
		System.err.println ("proxsClassTags should be overridden");
		return new String[] {proxyClass().getSimpleName()};
	}
	protected Object createInstance() {
		return BasicProjectIntrospection.createInstance(proxyClass(),  proxyClassTags());
		
	}
	/*
	 * This seems to be create, not get! Should not store anyway, as projects change
	 */
	protected Object oldGetObjectFromFactory (String[] factoryClassTag, String[] factoryMethodTag, Class instantiatedTypeClass) {
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
			if (instantiatedTypeClass.isInterface()) {
			return maybeUseConstructor(instantiatedTypeClass);
			}
			else {
				assertTrue(factoryMessage, false);
			}
			
			
		}
		factoryCredit += factoryMethodCredit();

		
		Object anInstance;
		try {
			anInstance = BasicProjectExecution.timedInvoke(factoryClass, factoryMethod, emptyObjectArgs);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			anInstance = null;
		}
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
			factoryMessage = "Factory method returns instance of " + aReturnedClass + " instead of " + anExpectedClass;			
			return maybeUseConstructor(instantiatedTypeClass);

//			return assertingCreateInstance(instantiatedTypeClass);
		}
		factoryCredit += factoryObjectClassCredit();

		Object aProxy = BasicProjectExecution.maybeReturnProxy(anInstance, instantiatedTypeClass);
	
		return aProxy;
		
	}
	protected Object getObjectFromFactory (String[] factoryClassTag, String[] factoryMethodTag, Class instantiatedTypeClass) {
		Class<?> factoryClass = BasicProjectIntrospection.findClassByTags(factoryClassTag);
//		foundFactoryClass = factoryClass != null;
//		factoryMessage = "";
//		factoryCredit = 0;
//		if (!foundFactoryClass) {
//			factoryMessage = 
//				factoryMessage = "Factory class:" + Arrays.toString(factoryClassTag) + " not found.";	
//			return maybeUseConstructor(instantiatedTypeClass);
//		}
//		factoryCredit += factoryClassCredit();
		return getObjectFromFactory(Arrays.toString(factoryClassTag), factoryClass,  factoryMethodTag, instantiatedTypeClass);
//		Method factoryMethod =	BasicProjectIntrospection.findUniqueMethodByTag(
//				factoryClass, factoryMethodTag);
//		foundFactoryMethod = factoryMethod != null;
//		if (!foundFactoryMethod) {
//			factoryMessage = "Factory method:" + Arrays.toString(factoryMethodTag) + " not found.";	
//			if (instantiatedTypeClass.isInterface()) {
//			return maybeUseConstructor(instantiatedTypeClass);
//			}
//			else {
//				assertTrue(factoryMessage, false);
//			}
//			
//			
//		}
//		factoryCredit += factoryMethodCredit();
//
//		
//		Object anInstance;
//		try {
//			anInstance = BasicProjectExecution.timedInvoke(factoryClass, factoryMethod, emptyObjectArgs);
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			anInstance = null;
//		}
//		nullInstantiation = anInstance == null;
//		if (nullInstantiation) {
//			factoryMessage = "Factory method returns null object";			
//			return maybeUseConstructor(instantiatedTypeClass);
////			return assertingCreateInstance(instantiatedTypeClass);
//			
//		}
//		factoryCredit += factoryObjectCredit();
//
//		Class aReturnedClass = anInstance.getClass();
//		
//		Class anExpectedClass = BasicProjectIntrospection.findClass(instantiatedTypeClass);
//		
//		correctInstantiatedClass = aReturnedClass == null || anExpectedClass == null ||
//				anExpectedClass.isInstance(anInstance);
////					aReturnedClass.equals(anExpectedClass);
//		
//		if (!correctInstantiatedClass) {
//			factoryMessage = "Factory method returns instance of " + aReturnedClass + " instead of " + anExpectedClass;			
//			return maybeUseConstructor(instantiatedTypeClass);
//
////			return assertingCreateInstance(instantiatedTypeClass);
//		}
//		factoryCredit += factoryObjectClassCredit();
//
//		Object aProxy = BasicProjectExecution.maybeReturnProxy(anInstance, instantiatedTypeClass);
//	
//		return aProxy;
		
	}
	protected Object getObjectFromFactory (String factoryClassDescription, Class<?> factoryClass, String[] factoryMethodTag, Class instantiatedTypeClass) {
//		Class<?> factoryClass = BasicProjectIntrospection.findClassByTags(factoryClassTag);
		foundFactoryClass = factoryClass != null;
		factoryMessage = "";
		factoryCredit = 0;
		if (!foundFactoryClass) {
			factoryMessage = 
				factoryMessage = "Factory class:" + factoryClassDescription + " not found.";	
			return maybeUseConstructor(instantiatedTypeClass);
		}
		factoryCredit += factoryClassCredit();
		Method factoryMethod =	BasicProjectIntrospection.findUniqueMethodByTag(
				factoryClass, factoryMethodTag);
		return getObjectFromFactory(Arrays.toString(factoryMethodTag), factoryClass, factoryMethod, instantiatedTypeClass);
//		foundFactoryMethod = factoryMethod != null;
//		if (!foundFactoryMethod) {
//			factoryMessage = "Factory method:" + Arrays.toString(factoryMethodTag) + " not found.";	
//			if (instantiatedTypeClass.isInterface()) {
//			return maybeUseConstructor(instantiatedTypeClass);
//			}
//			else {
//				assertTrue(factoryMessage, false);
//			}
//			
//			
//		}
//		factoryCredit += factoryMethodCredit();
//
//		
//		Object anInstance;
//		try {
//			anInstance = BasicProjectExecution.timedInvoke(factoryClass, factoryMethod, emptyObjectArgs);
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			anInstance = null;
//		}
//		nullInstantiation = anInstance == null;
//		if (nullInstantiation) {
//			factoryMessage = "Factory method returns null object";			
//			return maybeUseConstructor(instantiatedTypeClass);
////			return assertingCreateInstance(instantiatedTypeClass);
//			
//		}
//		factoryCredit += factoryObjectCredit();
//
//		Class aReturnedClass = anInstance.getClass();
//		
//		Class anExpectedClass = BasicProjectIntrospection.findClass(instantiatedTypeClass);
//		
//		correctInstantiatedClass = aReturnedClass == null || anExpectedClass == null ||
//				anExpectedClass.isInstance(anInstance);
////					aReturnedClass.equals(anExpectedClass);
//		
//		if (!correctInstantiatedClass) {
//			factoryMessage = "Factory method returns instance of " + aReturnedClass + " instead of " + anExpectedClass;			
//			return maybeUseConstructor(instantiatedTypeClass);
//
////			return assertingCreateInstance(instantiatedTypeClass);
//		}
//		factoryCredit += factoryObjectClassCredit();
//
//		Object aProxy = BasicProjectExecution.maybeReturnProxy(anInstance, instantiatedTypeClass);
//	
//		return aProxy;
		
	}
	protected boolean matchProxyActualClass() {
		return true;
	}
	protected Object getObjectFromFactory (String factoryMethodDescription, Class<?> factoryClass, Method factoryMethod, Class instantiatedTypeClass) {
//		Class<?> factoryClass = BasicProjectIntrospection.findClassByTags(factoryClassTag);
//		foundFactoryClass = factoryClass != null;
//		factoryMessage = "";
//		factoryCredit = 0;
//		if (!foundFactoryClass) {
//			factoryMessage = 
//				factoryMessage = "Factory class:" + Arrays.toString(factoryClassTag) + " not found.";	
//			return maybeUseConstructor(instantiatedTypeClass);
//		}
		foundFactoryMethod = factoryMethod != null;
		if (!foundFactoryMethod) {
			factoryMessage = "Factory method:" + factoryMethodDescription + " not found.";	
			if (instantiatedTypeClass.isInterface()) {
			return maybeUseConstructor(instantiatedTypeClass);
			}
			else {
				assertTrue(factoryMessage, false);
			}
			
			
		}
		factoryCredit += factoryMethodCredit();
		
//		factoryCredit += factoryMethodCredit();

		
		Object anInstance;
		try {
			anInstance = BasicProjectExecution.timedInvoke(factoryClass, factoryMethod, emptyObjectArgs);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			anInstance = null;
		}
		nullInstantiation = anInstance == null;
		if (nullInstantiation) {
			factoryMessage = "Factory method returns null object";			
			return maybeUseConstructor(instantiatedTypeClass);
//			return assertingCreateInstance(instantiatedTypeClass);
			
		}
		factoryCredit += factoryObjectCredit();
		if (matchProxyActualClass()) {

		Class aReturnedClass = anInstance.getClass();
		Class anExpectedClass = BasicProjectIntrospection.isPredefinedType(instantiatedTypeClass)?
				instantiatedTypeClass:
					BasicProjectIntrospection.findClass(instantiatedTypeClass);
		
//		Class anExpectedClass = BasicProjectIntrospection.findClass(instantiatedTypeClass);
		
		correctInstantiatedClass = aReturnedClass == null || anExpectedClass == null ||
				anExpectedClass.isInstance(anInstance);
//					aReturnedClass.equals(anExpectedClass);
	
		
		if (!correctInstantiatedClass) {
			factoryMessage = "Factory method returns instance of " + aReturnedClass + " instead of " + anExpectedClass;			
			return maybeUseConstructor(instantiatedTypeClass);

//			return assertingCreateInstance(instantiatedTypeClass);
		}
		}
		factoryCredit += factoryObjectClassCredit();

		Object aProxy = BasicProjectExecution.maybeReturnProxy(anInstance, instantiatedTypeClass);
	
		return aProxy;
		
	}
	protected Object createUsingFactory() {
		rootProxy = createUsingFactoryClassAndMethodTags();
		return rootProxy;
	}
	
	protected Object createUsingFactoryClassAndMethodTags() {
		rootProxy = getObjectFromFactory(factoryClassTags(), factoryMethodTags(), proxyClass());
		return rootProxy;
	}	
	protected Object createUsingFactoryClassAndMethodName() {
		String aFactoryMethodName = factoryMethodName();
		Class aFactoryClass = getFactoryClass();
		if (aFactoryClass == null) {
			assertTrue("No factory class for method:" + aFactoryMethodName, false) ;
		}
		Method aFactoryMethod = getFactoryMethod();
		if (aFactoryMethod == null) {
			assertTrue("In factory class, " + aFactoryClass + " no public parameterless factory method with name:" + aFactoryMethodName + " public methods found: " + Arrays.toString(aFactoryClass.getMethods()), false) ;
		}
		Class aProxyClass = proxyClass();
		rootProxy = getObjectFromFactory(aFactoryMethodName, aFactoryClass, aFactoryMethod, aProxyClass);

//		rootProxy = getObjectFromFactory(factoryMethodName(), getFactoryClass(), getFactoryMethod(), proxyClass());
		return rootProxy;
	}
	@Override
	protected void executeOperations(Object aProxy) throws Exception {
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
