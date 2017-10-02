package gradingTools.shared.testcases;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.GradingMode;
import grader.basics.junit.NotesAndScore;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.Project;
import gradingTools.shared.testcases.shapes.LocatableTest;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;

import bus.uigen.visitors.HasUncreatedChildrenVisitor;

//import com.sun.org.apache.xpath.internal.FoundIndex;

import util.misc.Common;
import util.trace.Tracer;

public abstract class BeanExecutionTest extends LocatableTest {
	protected Map<String, Object> outputPropertyValues = new HashMap();
	// The missing and wrong properties are not set right now
	protected Set<String> missingGetters = new HashSet();
	protected Set<String> missingSetters = new HashSet();
	protected Set<String> wrongInputProperties = new HashSet();
	protected Set<String> wrongOutputProperties = new HashSet();
	protected boolean hasConstructor = false;
	protected boolean hasCorrectType = false;
	protected boolean givenObject = false;	
	protected boolean foundUniqueClass = false;


	protected boolean invokeSetters = true;
	Object[] constructorArgs;
//	protected Object beanObject;
	protected Object[] expectedOutputValues;
	Map<String, Object> inputPropertyValues;

	protected String[] getBeanDescriptions() {
		return getClassNames();
	}

	public boolean isInvokeSetters() {
		return invokeSetters;
	}
	


	public void setInvokeSetters(boolean newValue) {
		invokeSetters = newValue;
	}

	public Class[] getConstructorArgTypes() {
		Object[] anArgs = getConstructorArgs();
		return toArgTypes(anArgs);

	}

	public void setConstructorArgs(Object[] newVal) {
		constructorArgs = newVal;
	}

	protected Object[] getConstructorArgs() {
		if (constructorArgs != null) {
			return constructorArgs;
		}
		if (GradingMode.getGraderRun())
			return getGraderConstructorArgs();

		return getStudentConstructorArgs();
	}

	protected Object[] getStudentConstructorArgs() {
		return emptyObjectArray;
	}

	protected Object[] getGraderConstructorArgs() {
		return getStudentConstructorArgs();
	}

	static String[] emptyStringArray = {};

	protected String[] getOutputPropertyNames() {
		return emptyStringArray;
	}

	static Map<String, Object> emptyStringObjectMap = new HashMap();
	
	public void setInputPropertyValues(Map<String, Object> newVal) {
		inputPropertyValues = newVal;
	}
	public Map<String, Object> getInputPropertyValues() {
		if (inputPropertyValues != null) {
			return inputPropertyValues;
		}
		if (GradingMode.getGraderRun())
			return getGraderInputPropertyValues();

		return getStudentInputPropertyValues();
	}

	public Map<String, Object> getStudentInputPropertyValues() {
		return emptyStringObjectMap;
	}

	public Map<String, Object> getGraderInputPropertyValues() {
		return getStudentInputPropertyValues();
	}

	public Map<String, Object> getOutputPropertyValues() {
		return outputPropertyValues;
	}

	static Object[] emptyObjectArray = {};

	public Object[] getExpectedOutputValues() {
		if (expectedOutputValues != null) {
			return expectedOutputValues;
		}
		if (GradingMode.getGraderRun()) {
			return getExpectedGraderOutputValues();
		}
		return getExpectedStudentOutputValues();

	}

	public void setExpectedOutputValues(Object[] newVal) {
		expectedOutputValues = newVal;
	}

	public Object[] getExpectedStudentOutputValues() {
		return emptyObjectArray;
	}

	protected Object[] getExpectedGraderOutputValues() {
		return getExpectedStudentOutputValues();
	}

	protected void compareOutputWithExpected() {
		Map<String, Object> anInputs = getInputPropertyValues();

		String[] anOutputProperties = getOutputPropertyNames();
		Object[] anExpectedValues = getExpectedOutputValues();
		Map<String, Object> anActualOutputs = getOutputPropertyValues();
		anActualOutputs.put(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL, true);
		for (int i = 0; i < anOutputProperties.length; i++) {
			if (i >= anExpectedValues.length)
				return;
			Object anExpectedOutput = anExpectedValues[i];
			Object anActualOutput;
			String outputProperty = anOutputProperties[i];
			anActualOutput = outputProperty == null ? null : anActualOutputs
					.get(outputProperty);
			// anActualOutput = anActualOutputs.get(outputProperty);
			if (!Common.equal(anExpectedOutput, anActualOutput)) {
				anActualOutputs.put(
						BasicProjectExecution.EXPECTED_EQUAL_ACTUAL, false);
				anActualOutputs.put(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL
						+ "." + anOutputProperties[i], false);
				Tracer.info(this,"For property:" + outputProperty
						+ " expected value:" + anExpectedOutput
						+ ", but actual output:" + anActualOutput + ".");
				wrongOutputProperties.add(outputProperty);
			}

		}
	}

	public void testBean() throws Throwable {
		executeBean();
		processBeanExecution();
	}

	// protected Map<String, Object> executeBean() throws Throwable {
	// String anOutput;
	// Map<String, Object> anActualOutputs = new HashMap();
	// outputPropertyValues = anActualOutputs;
	// String[] aBeanDescriptions = getBeanDescriptions();
	// Class[] aConstructorArgTypes = getConstructorArgTypes();
	// Object[] aConstructorArgs = getConstructorArgs();
	// Map<String, Object> anInputs = getInputPropertyValues();
	// String[] anOutputProperties = getOutputPropertyNames();
	// try {
	// // String[] aBeanDescriptions = aBeanDescription.split(",");
	// // if (aBeanDescriptions.length != 4) {
	// //
	// Tracer.error("Bean description  in testBean should have 4 elements instead of: "
	// // + aBeanDescriptions.length);
	// // }
	// // BasicProjectExecution.redirectOutput();
	// // Tracer.info(this,"Testcase:" + aCheckName);
	// // Tracer.info(this,"Finding classes matching:"
	// // + Common.toString(aBeanDescriptions));
	// // Class aClass = BasicProjectIntrospection.findClass(aProject,
	// // aBeanDescriptions[0], aBeanDescriptions[1],
	// // aBeanDescriptions[2], aBeanDescriptions[3]);
	//
	// Class aClass = getTargetClass();
	//
	// if (aClass == null) {
	// Tracer.info(this,"No class matching: "
	// +aClass.getSimpleName());
	// anActualOutputs.put(BasicProjectExecution.MISSING_CLASS, true);
	// // anActualOutputs = null;
	// } else {
	// Tracer.info(this,"Finding constructor matching:"
	// + Common.toString(aConstructorArgTypes));
	// // anActualOutputs.put(CLASS_MATCHED,
	// // aClass.getCanonicalName());
	// anActualOutputs.put(BasicProjectExecution.CLASS_MATCHED, aClass);
	//
	// Constructor aConstructor = aClass
	// .getConstructor(aConstructorArgTypes);
	// if (aConstructor == null) {
	// outputPropertyValues.put(BasicProjectExecution.MISSING_CONSTRUCTOR,
	// true);
	// }
	// aConstructor = aClass.getConstructor();
	//
	// Object anObject = BasicProjectExecution.timedInvoke(aConstructor,
	// aConstructorArgs,
	// BasicProjectExecution.getMethodTimeOut());
	// for (String aPropertyName : anInputs.keySet()) {
	// if (aPropertyName == null)
	// continue;
	// PropertyDescriptor aProperty = BasicProjectIntrospection
	// .findProperty(aClass, aPropertyName);
	// if (aProperty == null) {
	// anActualOutputs.put(BasicProjectExecution.MISSING_PROPERTY, true);
	// anActualOutputs.put(BasicProjectExecution.MISSING_PROPERTY + "."
	// + aPropertyName, true);
	// Tracer.info(this,"Property " + aPropertyName + "not found in " +
	// aClass.getSimpleName());
	// continue;
	// }
	// Method aWriteMethod = aProperty.getWriteMethod();
	// if (aWriteMethod == null) {
	// anActualOutputs.put(BasicProjectExecution.MISSING_WRITE, true);
	// anActualOutputs.put(
	// BasicProjectExecution.MISSING_WRITE + "." + aPropertyName, true);
	// Tracer.info(this,"Missing write method for property "
	// + aPropertyName);
	// continue;
	// }
	// Object aValue = anInputs.get(aPropertyName);
	// // timedInvoke(anObject, aWriteMethod, getMethodTimeOut(),
	// // new Object[] { aValue });
	// // BasicProjectExecution.timedInvoke(anObject, aWriteMethod,
	// // new Object[] { aValue }, BasicProjectExecution.getMethodTimeOut());
	// //
	// invokeMethod(anObject, aWriteMethod, new Object[] { aValue });
	//
	// }
	// for (String anOutputPropertyName : anOutputProperties) {
	// if (anOutputPropertyName == null)
	// continue;
	// PropertyDescriptor aProperty = BasicProjectIntrospection
	// .findProperty(aClass, anOutputPropertyName);
	// if (aProperty == null) {
	//
	// // Tracer.info(this,"Property " + aPropertyName +
	// // "not found");
	// continue;
	// }
	// Method aReadMethod = aProperty.getReadMethod();
	// if (aReadMethod == null) {
	// Tracer.info(this,"Missing read method for property "
	// + anOutputPropertyName);
	// anActualOutputs.put(BasicProjectExecution.MISSING_READ, true);
	// anActualOutputs.put(BasicProjectExecution.MISSING_READ + "."
	// + anOutputPropertyName, true);
	// continue;
	// }
	// // Object result = timedInvoke(anObject, aReadMethod,
	// // getMethodTimeOut(), emptyArgs);
	// // Object result = BasicProjectExecution.timedInvoke(anObject,
	// aReadMethod,
	// // BasicProjectExecution.emptyArgs,
	// BasicProjectExecution.getMethodTimeOut());
	// invokeMethod(anObject, aReadMethod, BasicProjectExecution.emptyArgs);
	// anActualOutputs.put(anOutputPropertyName, returnValue);
	// }
	// }
	//
	// } catch (NoSuchMethodException e) {
	// Tracer.info(this,"Constructor not found:" + e.getMessage());
	// anActualOutputs.put(BasicProjectExecution.MISSING_CONSTRUCTOR, true);
	// // e.printStackTrace();
	// } catch (SecurityException e) {
	// // TODO Auto-generated catch block
	// anActualOutputs = null;
	//
	// e.printStackTrace();
	// } finally {
	// // anOutput =
	// BasicProjectExecution.restoreOutputAndGetRedirectedOutput();
	// // if (anOutput != null && !anOutput.isEmpty()) {
	// // ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
	// // anOutput);
	// // }
	// // anActualOutputs.put(BasicProjectExecution.PRINTS, anOutput);
	//
	// }
	// boolean getsReturnSets = getsReturnedSets(
	// anInputs, anActualOutputs);
	// anActualOutputs.put(BasicProjectExecution.GETS_EQUAL_SETS,
	// getsReturnSets);
	// compareOutputWithExpected();
	// return anActualOutputs;
	// }
	protected Constructor getRequiredConstructor() {
		Class[] aConstructorArgTypes = getConstructorArgTypes();
		Class aClass = getTargetClass();
		Constructor aConstructor;
		try {
//			Tracer.info(this,"Finding constructor matching:"
//					+ Common.toString(aConstructorArgTypes));
			aConstructor = aClass
					.getConstructor(aConstructorArgTypes);
		} catch (NoSuchMethodException e) {
			if (aConstructorArgTypes.length == 0) {
				//no other constructor to try, fail
				assertNoConstructor(e);
			}
			Tracer.info(this,"Could not find in " + aClass + " required public constructor matching arg types:"
					+ Arrays.toString(aConstructorArgTypes));
			return null;
		} catch (SecurityException e) {
		
		e.printStackTrace();
		return null;
	}
		return aConstructor;
	}
	@Override
	protected Object create() {
//		Class[] aConstructorArgTypes = getConstructorArgTypes();
		Object[] aConstructorArgs = getConstructorArgs();
		// Map<String, Object> anInputs = getInputPropertyValues();
		// String[] anOutputProperties = getOutputPropertyNames();
		try {

			Class aClass = getTargetClass();

			if (aClass == null) {
				Tracer.info(this,"No class matching: "
						+ Arrays.toString(getClassNames()));
				outputPropertyValues.put(BasicProjectExecution.MISSING_CLASS, true);
				assertMissingClass(getClassNames());
//				Assert.assertTrue("No class matching: "
//						+ Arrays.toString(getClassNames()) + NotesAndScore.PERCENTAGE_MARKER + 0.0, false);
				// anActualOutputs = null;
			} else {
//				Tracer.info(this,"Finding constructor matching:"
//						+ Common.toString(aConstructorArgTypes));
				// anActualOutputs.put(CLASS_MATCHED,
				// aClass.getCanonicalName());
				outputPropertyValues
						.put(BasicProjectExecution.CLASS_MATCHED, aClass);

//				Constructor aConstructor = aClass
//						.getConstructor(aConstructorArgTypes);
				hasConstructor = true;

				Constructor aConstructor = getRequiredConstructor();
//				if (aConstructorArgs.length == 0) {
//					
//				}
				if (aConstructor == null) {
					outputPropertyValues.put(
							BasicProjectExecution.MISSING_CONSTRUCTOR, true);
					System.out
							.println("Trying to find public parameterless constructor");
					aConstructor = aClass.getConstructor();
					aConstructorArgs = emptyObjectArray;
					hasConstructor = false;
					

				}

				Object anObject = BasicProjectExecution.timedInvoke(
						aConstructor, aConstructorArgs,
						BasicProjectExecution.getMethodTimeOut());
				return anObject;
//				anActualOutputs = executeBean(anObject);

			}

		} catch (NoSuchMethodException e) {
			assertNoConstructor(e);
//			String aMessage = "Public constructor not found:" + e.getMessage();
//			Tracer.info(this,aMessage);
//			hasConstructor = false;
//
//			outputPropertyValues
//					.put(BasicProjectExecution.MISSING_CONSTRUCTOR, true);
//			Assert.assertTrue(aMessage + NotesAndScore.PERCENTAGE_MARKER + 0.0,
//					false);
			// e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
//			anActualOutputs = null;
//
//			e.printStackTrace();
			throw e;
		} finally {

		}
		return null;
	}
	protected void assertNoConstructor (NoSuchMethodException e) {
		String aMessage = "Public constructor not found:" + e.getMessage();
		Tracer.info(this,aMessage);
		hasConstructor = false;

		outputPropertyValues
				.put(BasicProjectExecution.MISSING_CONSTRUCTOR, true);
		Assert.assertTrue(aMessage + NotesAndScore.PERCENTAGE_MARKER + 0.0,
				false);
	}
	public Map<String, Object> executeBean() throws Throwable {
		Object anObject = create();
		givenObject = false;
		hasCorrectType = true;

		return internalExecuteBean(anObject);
		
	}
	public Map<String, Object> oldExecuteBean() throws Throwable {
		String anOutput;
//		Map<String, Object> anActualOutputs = new HashMap();
		// outputPropertyValues = anActualOutputs;
		// String[] aBeanDescriptions = getBeanDescriptions();
		Class[] aConstructorArgTypes = getConstructorArgTypes();
		Object[] aConstructorArgs = getConstructorArgs();
		// Map<String, Object> anInputs = getInputPropertyValues();
		// String[] anOutputProperties = getOutputPropertyNames();
		try {

			Class aClass = getTargetClass();

			if (aClass == null) {
				Tracer.info(this,"No class matching: "
						+ Arrays.toString(getClassNames()));
				outputPropertyValues.put(BasicProjectExecution.MISSING_CLASS, true);
				assertMissingClass(getClassNames());
//				Assert.assertTrue("No class matching: "
//						+ Arrays.toString(getClassNames()) + NotesAndScore.PERCENTAGE_MARKER + 0.0, false);
				// anActualOutputs = null;
			} else {
				Tracer.info(this,"Finding constructor matching:"
						+ Common.toString(aConstructorArgTypes));
				// anActualOutputs.put(CLASS_MATCHED,
				// aClass.getCanonicalName());
				outputPropertyValues
						.put(BasicProjectExecution.CLASS_MATCHED, aClass);

				Constructor aConstructor = aClass
						.getConstructor(aConstructorArgTypes);
				if (aConstructor == null) {
					outputPropertyValues.put(
							BasicProjectExecution.MISSING_CONSTRUCTOR, true);
					System.out
							.println("Trying to find public parameterless constructor");
					aConstructor = aClass.getConstructor();

				}

				Object anObject = BasicProjectExecution.timedInvoke(
						aConstructor, aConstructorArgs,
						BasicProjectExecution.getMethodTimeOut());
				outputPropertyValues = executeBean(anObject);

			}

		} catch (NoSuchMethodException e) {
			Tracer.info(this,"Constructor not found:" + e.getMessage());
			outputPropertyValues
					.put(BasicProjectExecution.MISSING_CONSTRUCTOR, true);
			// e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			outputPropertyValues = null;

			e.printStackTrace();
		} finally {

		}
		// boolean getsReturnSets = getsReturnedSets(
		// anInputs, anActualOutputs);
		// anActualOutputs.put(BasicProjectExecution.GETS_EQUAL_SETS,
		// getsReturnSets);
		// compareOutputWithExpected();
		return outputPropertyValues;
	}
	protected void assertNoProperty(String aProperty) {
		Assert.assertTrue("Missing property:" + aProperty +  " in " +lastTargetObject.getClass() + NotesAndScore.PERCENTAGE_MARKER + fractionComplete, false);
	}
	protected void invokeGetter(Object anObject, String anOutputPropertyName)
			throws Throwable {
		Map<String, Object> anActualOutputs = outputPropertyValues;

		try {
			String[] anOutputProperties = getOutputPropertyNames();
			Class aClass = lastTargetObject.getClass();

			if (anOutputPropertyName == null)
				return;
			PropertyDescriptor aProperty = BasicProjectIntrospection
					.findProperty(aClass, anOutputPropertyName);
			if (aProperty == null) {
				missingGetters.add(anOutputPropertyName);
				anActualOutputs.put(BasicProjectExecution.MISSING_PROPERTY, true);
				anActualOutputs.put(BasicProjectExecution.MISSING_PROPERTY + "."
						+ anOutputPropertyName, true);
				Tracer.warning("Property " + anOutputPropertyName +
				 " not found in " + aClass + "\nDefine a getter for the property (named: get"+ anOutputPropertyName );
				assertNoProperty(anOutputPropertyName);
				return;
			}
			Method aReadMethod = aProperty.getReadMethod();
			if (aReadMethod == null) {
				Tracer.warning("Missing read method for property "
						+ anOutputPropertyName + " in " + aClass);
				missingGetters.add(anOutputPropertyName);
				anActualOutputs.put(BasicProjectExecution.MISSING_READ, true);
				anActualOutputs.put(BasicProjectExecution.MISSING_READ + "."
						+ anOutputPropertyName, true);
				return;
			}
			// Object result = timedInvoke(anObject, aReadMethod,
			// getMethodTimeOut(), emptyArgs);
			// Object result = BasicProjectExecution.timedInvoke(anObject,
			// aReadMethod,
			// BasicProjectExecution.emptyArgs,
			// BasicProjectExecution.getMethodTimeOut());
			noOutput = true; // by default getters have no output
			invokeMethod(anObject, aReadMethod, BasicProjectExecution.emptyArgs);
			noOutput = false;
			anActualOutputs.put(anOutputPropertyName, returnValue);

		} catch (NoSuchMethodException e) {
			Tracer.info(this,"Constructor not found:" + e.getMessage());
			anActualOutputs
					.put(BasicProjectExecution.MISSING_CONSTRUCTOR, true);
			// e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			anActualOutputs = null;

			e.printStackTrace();
		} finally {
			// anOutput =
			// BasicProjectExecution.restoreOutputAndGetRedirectedOutput();
			// if (anOutput != null && !anOutput.isEmpty()) {
			// ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
			// anOutput);
			// }
			// anActualOutputs.put(BasicProjectExecution.PRINTS, anOutput);

		}
	}
	
	protected void invokeGetters (Object anObject) throws Throwable {
		Map<String, Object> anActualOutputs = outputPropertyValues;
		Map<String, Object> anInputs = getInputPropertyValues();

		try {
			String[] anOutputProperties = getOutputPropertyNames();
			Class aClass = lastTargetObject.getClass();
			for (String aPropertyName : anInputs.keySet()) {
				invokeGetter(anObject, aPropertyName );
			}
			
			for (String anOutputPropertyName : anOutputProperties) {
				invokeGetter(anObject, anOutputPropertyName );
			}

		
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			anActualOutputs = null;

			e.printStackTrace();
		} finally {
			// anOutput =
			// BasicProjectExecution.restoreOutputAndGetRedirectedOutput();
			// if (anOutput != null && !anOutput.isEmpty()) {
			// ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
			// anOutput);
			// }
			// anActualOutputs.put(BasicProjectExecution.PRINTS, anOutput);

		}
	}
	protected void invokeSetters (Object anObject) throws Throwable {
		Map<String, Object> anActualOutputs = outputPropertyValues;
		Class aClass = lastTargetObject.getClass();
		Map<String, Object> anInputs = getInputPropertyValues();


		try {
			if (invokeSetters) {
				for (String aPropertyName : anInputs.keySet()) {
					invokeSetter(anObject, aPropertyName);

				}
			}
			

		
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			anActualOutputs = null;

			e.printStackTrace();
		} finally {
			// anOutput =
			// BasicProjectExecution.restoreOutputAndGetRedirectedOutput();
			// if (anOutput != null && !anOutput.isEmpty()) {
			// ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
			// anOutput);
			// }
			// anActualOutputs.put(BasicProjectExecution.PRINTS, anOutput);

		}
	}
	protected void invokeSetter (Object anObject, String aPropertyName) throws Throwable {
		Map<String, Object> anActualOutputs = outputPropertyValues;
		Class aClass = lastTargetObject.getClass();
		Map<String, Object> anInputs = getInputPropertyValues();


		try {
			if (invokeSetters) {
					if (aPropertyName == null)
						return;
					PropertyDescriptor aProperty = BasicProjectIntrospection
							.findProperty(aClass, aPropertyName);
					if (aProperty == null) {
						missingSetters.add(aPropertyName);
						anActualOutputs.put(
								BasicProjectExecution.MISSING_PROPERTY, true);
						anActualOutputs.put(
								BasicProjectExecution.MISSING_PROPERTY + "."
										+ aPropertyName, true);
						Tracer.warning("Property " + aPropertyName
								+ " not found in " + aClass + 
								"\nDefine a getter for the property (named: get"+ aPropertyName + ")");
						return;
					}
					Method aWriteMethod = aProperty.getWriteMethod();
					if (aWriteMethod == null) {
						anActualOutputs.put(
								BasicProjectExecution.MISSING_WRITE, true);
						anActualOutputs.put(BasicProjectExecution.MISSING_WRITE
								+ "." + aPropertyName, true);
						Tracer.warning("Missing write method for property "
								+ aPropertyName);
						return;
					}
					Object aValue = anInputs.get(aPropertyName);
					// timedInvoke(anObject, aWriteMethod, getMethodTimeOut(),
					// new Object[] { aValue });
					// BasicProjectExecution.timedInvoke(anObject, aWriteMethod,
					// new Object[] { aValue },
					// BasicProjectExecution.getMethodTimeOut());
					//
					invokeMethod(anObject, aWriteMethod,
							new Object[] { aValue });

				}
			
			

		
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			anActualOutputs = null;

			e.printStackTrace();
		} finally {
			// anOutput =
			// BasicProjectExecution.restoreOutputAndGetRedirectedOutput();
			// if (anOutput != null && !anOutput.isEmpty()) {
			// ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
			// anOutput);
			// }
			// anActualOutputs.put(BasicProjectExecution.PRINTS, anOutput);

		}
	}
	
	protected void clearOutputs() {
		outputPropertyValues.clear();
		missingGetters.clear();
		missingSetters.clear();
		wrongInputProperties.clear();
		wrongOutputProperties.clear();
	}
	public static Class getActualClass (Object anObjectOrProxy) {
		if (anObjectOrProxy instanceof Proxy) {
			return BasicProjectIntrospection.getRealObject(anObjectOrProxy).getClass();
		} else {
			return anObjectOrProxy.getClass();
		}
	}
	
	public Map<String, Object> executeBean(Object anObject) throws Throwable {
		Class aTargetClass = getTargetClass();
		foundUniqueClass = aTargetClass != null;
		hasConstructor = true;
		givenObject = true;
		if (anObject != null && aTargetClass != null) {
			hasCorrectType = aTargetClass.equals(getActualClass(anObject));
		} else {
		hasCorrectType = false;
		}
		hasCorrectType = true;

		return internalExecuteBean(anObject);
	}
	protected void invokePropertyMethods(Object anObject) throws Throwable {
		invokeSetters(anObject);
		invokeGetters(anObject);
	}
	public Map<String, Object> internalExecuteBean(Object anObject) throws Throwable {
		// Cannot do reflection onproxies
		if (anObject instanceof Proxy) {
			lastTargetObject = BasicProjectIntrospection.getRealObject(anObject);
			anObject = lastTargetObject;
		} else {
			lastTargetObject = anObject;
			anObject = lastTargetObject;
		}
			
		String anOutput;
		if (anObject ==  null) {
			outputPropertyValues.put(BasicProjectExecution.NULL_OBJECT, true);
			return outputPropertyValues;
		}

//		outputPropertyValues.clear();
		clearOutputs();
//		Map<String, Object> anActualOutputs = new HashMap();
		Map<String, Object> anActualOutputs = outputPropertyValues;

//		outputPropertyValues = anActualOutputs;
//		Class aClass = getTargetClass();
		Class aClass = lastTargetObject.getClass();

		Map<String, Object> anInputs = getInputPropertyValues();
		String[] anOutputProperties = getOutputPropertyNames();
		try {
//			invokeSetters(anObject);
//			invokeGetters(anObject);
			invokePropertyMethods(anObject);

		
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			anActualOutputs = null;

			e.printStackTrace();
		} finally {
			// anOutput =
			// BasicProjectExecution.restoreOutputAndGetRedirectedOutput();
			// if (anOutput != null && !anOutput.isEmpty()) {
			// ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
			// anOutput);
			// }
			// anActualOutputs.put(BasicProjectExecution.PRINTS, anOutput);

		}
		boolean getsReturnSets = getsReturnedSets(anInputs, anActualOutputs) && hasReadMethod();
		anActualOutputs.put(BasicProjectExecution.GETS_EQUAL_SETS,
				getsReturnSets);
		compareOutputWithExpected();
		return anActualOutputs;
	}
	public Map<String, Object> oldExecuteBean(Object anObject) throws Throwable {
		// Cannot do reflection onproxies
		if (anObject instanceof Proxy) {
			lastTargetObject = BasicProjectIntrospection.getRealObject(anObject);
			anObject = lastTargetObject;
		} else {
			lastTargetObject = anObject;
			anObject = lastTargetObject;
		}
		String anOutput;
		outputPropertyValues.clear();
//		Map<String, Object> anActualOutputs = new HashMap();
		Map<String, Object> anActualOutputs = outputPropertyValues;

//		outputPropertyValues = anActualOutputs;
//		Class aClass = getTargetClass();
		Class aClass = lastTargetObject.getClass();

		Map<String, Object> anInputs = getInputPropertyValues();
		String[] anOutputProperties = getOutputPropertyNames();
		try {
			if (invokeSetters) {
				for (String aPropertyName : anInputs.keySet()) {
					if (aPropertyName == null)
						continue;
					PropertyDescriptor aProperty = BasicProjectIntrospection
							.findProperty(aClass, aPropertyName);
					if (aProperty == null) {
						anActualOutputs.put(
								BasicProjectExecution.MISSING_PROPERTY, true);
						anActualOutputs.put(
								BasicProjectExecution.MISSING_PROPERTY + "."
										+ aPropertyName, true);
						Tracer.info(this,"Property " + aPropertyName
								+ " not found in " + aClass.getSimpleName() + 
								"\nDefine a getter for the property (named: get"+ aPropertyName + ")");
						continue;
					}
					Method aWriteMethod = aProperty.getWriteMethod();
					if (aWriteMethod == null) {
						anActualOutputs.put(
								BasicProjectExecution.MISSING_WRITE, true);
						anActualOutputs.put(BasicProjectExecution.MISSING_WRITE
								+ "." + aPropertyName, true);
						Tracer.info(this,"Missing write method for property "
								+ aPropertyName);
						continue;
					}
					Object aValue = anInputs.get(aPropertyName);
					// timedInvoke(anObject, aWriteMethod, getMethodTimeOut(),
					// new Object[] { aValue });
					// BasicProjectExecution.timedInvoke(anObject, aWriteMethod,
					// new Object[] { aValue },
					// BasicProjectExecution.getMethodTimeOut());
					//
					invokeMethod(anObject, aWriteMethod,
							new Object[] { aValue });

				}
			}
			for (String anOutputPropertyName : anOutputProperties) {
				if (anOutputPropertyName == null)
					continue;
				PropertyDescriptor aProperty = BasicProjectIntrospection
						.findProperty(aClass, anOutputPropertyName);
				if (aProperty == null) {

					// Tracer.info(this,"Property " + aPropertyName +
					// "not found");
					continue;
				}
				Method aReadMethod = aProperty.getReadMethod();
				if (aReadMethod == null) {
					Tracer.info(this,"Missing read method for property "
							+ anOutputPropertyName);
					anActualOutputs.put(BasicProjectExecution.MISSING_READ,
							true);
					anActualOutputs.put(BasicProjectExecution.MISSING_READ
							+ "." + anOutputPropertyName, true);
					continue;
				}
				// Object result = timedInvoke(anObject, aReadMethod,
				// getMethodTimeOut(), emptyArgs);
				// Object result = BasicProjectExecution.timedInvoke(anObject,
				// aReadMethod,
				// BasicProjectExecution.emptyArgs,
				// BasicProjectExecution.getMethodTimeOut());
				invokeMethod(anObject, aReadMethod,
						BasicProjectExecution.emptyArgs);
				anActualOutputs.put(anOutputPropertyName, returnValue);
			}

		} catch (NoSuchMethodException e) {
			Tracer.info(this,"Constructor not found:" + e.getMessage());
			anActualOutputs
					.put(BasicProjectExecution.MISSING_CONSTRUCTOR, true);
			anActualOutputs.put(BasicProjectExecution.NULL_OBJECT, true);
			// e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			anActualOutputs = null;

			e.printStackTrace();
		} finally {
			// anOutput =
			// BasicProjectExecution.restoreOutputAndGetRedirectedOutput();
			// if (anOutput != null && !anOutput.isEmpty()) {
			// ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
			// anOutput);
			// }
			// anActualOutputs.put(BasicProjectExecution.PRINTS, anOutput);

		}
		boolean getsReturnSets = getsReturnedSets(anInputs, anActualOutputs);
		anActualOutputs.put(BasicProjectExecution.GETS_EQUAL_SETS,
				getsReturnSets);
		compareOutputWithExpected();
		return anActualOutputs;
	}
	protected String getsEqualsSetsErrorMessage() {
		return " Gets do not return sets for:" + wrongInputProperties;
	}

	protected String expectedEqualsActualErrorMessage() {
		return " Output property wrong:" + wrongOutputProperties;
	}
	
	protected String nullObjectMessage() {
		
		return " Null target object";
		
			
	}
	protected boolean isNullObject() {
		Boolean isNull = (Boolean) outputPropertyValues.get(BasicProjectExecution.NULL_OBJECT);
		return isNull != null && isNull;
	}
	

	

	public double completeCredit() {
		
		if (isNullObject())
			return 0;
		double aReturnValue = getsEqualSets() ? getsEqualsSetsCredit() : 0;
		aReturnValue += expectedEqualActual() ? expectedEqualsActualCredit()
				: 0;
		if (!givenObject) {
		aReturnValue += hasConstructor() ? correctConstructorCredit() : 0;
		} else {
			aReturnValue += this.hasCorrectType() ? correctTyeCredit() : 0;
		}
		aReturnValue += hasWriteMethod() ? correctWriteMethodCredit() : 0;
		aReturnValue += hasReadMethod() ? correctReadMethodCredit() : 0;
		return aReturnValue;
	}

	protected double correctConstructorCredit() {
		return 0.2;
	}
	protected double correctTyeCredit() {
		return correctConstructorCredit();
	}
	
	protected double correctWriteMethodCredit() {
		return 0.1;
	}
	protected double correctReadMethodCredit() {
		return 0.1;
	}

	public boolean hasConstructor() {
		Boolean aMissingConstructor = (Boolean) outputPropertyValues
				.get(BasicProjectExecution.MISSING_CONSTRUCTOR);
		return hasConstructor && ( aMissingConstructor == null || !aMissingConstructor);

	}
	public boolean hasCorrectType() {
		return hasCorrectType;
	}

	
	protected boolean hasWriteMethod() {
		Boolean aMissingWrite = (Boolean) outputPropertyValues
				.get(BasicProjectExecution.MISSING_WRITE);
		return aMissingWrite == null || !aMissingWrite;

	}
	protected boolean hasReadMethod() {
		Boolean aMissingRead = (Boolean) outputPropertyValues
				.get(BasicProjectExecution.MISSING_PROPERTY);
		return aMissingRead == null || !aMissingRead;

	}

	protected String missingConstructorMessage() {
		return hasConstructor() ? "" : " Constructor not found with args:"
				+ Arrays.toString(getConstructorArgTypes());
	}
	protected String wrongTypeMessage() {
		return hasCorrectType ? "" : " Bean of wrong type";
	}
	protected String missingWriteMessage() {
		return hasWriteMethod() ? "" :
			"Setters not found for:" + missingSetters
			;
	}
	protected String missingReadMessage() {
		return hasReadMethod() ? "" :
			"Geters not found for:" + missingGetters
			;
	}
	
	protected String missingUniqueClassMessage() {
		return "Unique class not found for: " + Arrays.toString(getBeanDescriptions()) + ". See console.";
	}

	public String completeMessage() {
		if (isNullObject())
			return nullObjectMessage();
		String result = "";
		result += foundUniqueClass ? "" : missingUniqueClassMessage();
		result +=		getsEqualSets() ? "" : getsEqualsSetsErrorMessage();
		result += expectedEqualActual() ? ""
				: expectedEqualsActualErrorMessage();
		if (!givenObject) {
		result += hasConstructor() ? "" : missingConstructorMessage();
		} else {
			result += hasCorrectType?"":wrongTypeMessage();
		}
		result += hasCorrectType?"":wrongTypeMessage();
		result += hasWriteMethod() ? "" : missingWriteMessage();
		result += hasReadMethod() ? "" : missingReadMessage();


		return result;
	}

	protected void processGetsSets() {
		double aCredit = completeCredit();
		String aMessage = completeMessage();
		// if (aMessage.isEmpty())
		// return;
		Assert.assertTrue(aMessage + NotesAndScore.PERCENTAGE_MARKER + aCredit,
				false);
	}
	
	protected double getsEqualsSetsCredit() {
		return 0.2;
	}

	protected double expectedEqualsActualCredit() {
		return 0.4;
	}

	protected void processBeanExecution() {
		double aCredit = completeCredit();
		String aMessage = completeMessage();
		if (aCredit == 1.0)
			return;
		Assert.assertTrue(aMessage + NotesAndScore.PERCENTAGE_MARKER + aCredit,
				false);
		// boolean aGetsEqualsSets = getsEqualSets();
		// boolean anExpectedEqualsActual = expectedEqualActual();
		// if (aGetsEqualsSets && anExpectedEqualsActual ) {
		// return;
		// }
		// if (aGetsEqualsSets) {
		// processGetsSets();
		// } else if (anExpectedEqualsActual) {
		// processGetsAndSetsAfterSucessfulOutput();
		// }
	}

	protected void processGetsAndSetsAfterSucessfulOutput() {
		if (!getsEqualSets()) {
			Assert.assertTrue(getsEqualsSetsErrorMessage()
					+ NotesAndScore.PERCENTAGE_MARKER
					+ (1.0 - getsEqualsSetsCredit()), false);
		}
	}

	protected void processExpectedAndActualsAfterSucessfulOutput() {
		if (!getsEqualSets()) {
			Assert.assertTrue(getsEqualsSetsErrorMessage()
					+ NotesAndScore.PERCENTAGE_MARKER
					+ (1.0 - expectedEqualsActualCredit()), false);
		}
	}

	protected boolean getsEqualSets() {
		Map<String, Object> anOutputPropertyValues = getOutputPropertyValues();
		Object aGettersEqualsSetters = anOutputPropertyValues
				.get(BasicProjectExecution.GETS_EQUAL_SETS);
		return (aGettersEqualsSetters != null && aGettersEqualsSetters
				.equals(true));
	}

	protected boolean expectedEqualActual() {
		Map<String, Object> anOutputPropertyValues = getOutputPropertyValues();
		Object aExectedEqualsExpected = anOutputPropertyValues
				.get(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL);
		return (aExectedEqualsExpected != null && aExectedEqualsExpected
				.equals(true));
	}

	public static Map<String, Object> testBean(String aFeatureName,
			String aCheckName, Project aProject, String[] aBeanDescriptions,
			Class[] aConstructorArgTypes, Object[] aConstructorArgs,
			Map<String, Object> anInputs, String[] anOutputProperties) {
		String anOutput;
		Map<String, Object> anActualOutputs = new HashMap();

		try {
			// String[] aBeanDescriptions = aBeanDescription.split(",");
			if (aBeanDescriptions.length != 4) {
				Tracer.error("Bean description  in testBean should have 4 elements instead of: "
						+ aBeanDescriptions.length);
			}
			BasicProjectExecution.redirectOutput();
			Tracer.info(BeanExecutionTest.class,"Testcase:" + aCheckName);
			Tracer.info(BeanExecutionTest.class,"Finding class matching:"
					+ Common.toString(aBeanDescriptions));
			Class aClass = BasicProjectIntrospection.findClass(aProject,
					aBeanDescriptions[0], aBeanDescriptions[1],
					aBeanDescriptions[2], aBeanDescriptions[3]);

			if (aClass == null) {
				Tracer.error("No class matching: "
						+ Common.toString(aBeanDescriptions));
				anActualOutputs.put(BasicProjectExecution.MISSING_CLASS, true);
				// anActualOutputs = null;
			} else {
				Tracer.info(BeanExecutionTest.class,"Finding constructor matching:"
						+ Common.toString(aConstructorArgTypes));
				// anActualOutputs.put(CLASS_MATCHED,
				// aClass.getCanonicalName());
				anActualOutputs
						.put(BasicProjectExecution.CLASS_MATCHED, aClass);

				Constructor aConstructor = aClass
						.getConstructor(aConstructorArgTypes);
				Object anObject = BasicProjectExecution.timedInvoke(
						aConstructor, aConstructorArgs, 600);
				for (String aPropertyName : anInputs.keySet()) {
					if (aPropertyName == null)
						continue;
					PropertyDescriptor aProperty = BasicProjectIntrospection
							.findProperty(aClass, aPropertyName);
					if (aProperty == null) {
						anActualOutputs.put(
								BasicProjectExecution.MISSING_PROPERTY, true);
						anActualOutputs.put(
								BasicProjectExecution.MISSING_PROPERTY + "."
										+ aPropertyName, true);
						// Tracer.info(this,"Property " + aPropertyName +
						// "not found");
						continue;
					}
					Method aWriteMethod = aProperty.getWriteMethod();
					if (aWriteMethod == null) {
						anActualOutputs.put(
								BasicProjectExecution.MISSING_WRITE, true);
						anActualOutputs.put(BasicProjectExecution.MISSING_WRITE
								+ "." + aPropertyName, true);
						Tracer.error("Missing write method for property "
								+ aPropertyName);
						continue;
					}
					Object aValue = anInputs.get(aPropertyName);
					// timedInvoke(anObject, aWriteMethod, getMethodTimeOut(),
					// new Object[] { aValue });
					try {
						BasicProjectExecution.timedInvoke(anObject, aWriteMethod,
								new Object[] { aValue },
								BasicProjectExecution.getMethodTimeOut());
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						anActualOutputs.put(
								BasicProjectExecution.MISSING_WRITE, true);
						anActualOutputs.put(BasicProjectExecution.MISSING_WRITE
								+ "." + aPropertyName, true);
						Tracer.error("Erroneous write method for property "
								+ aPropertyName);
						continue;
					}
				}
				for (String anOutputPropertyName : anOutputProperties) {
					if (anOutputPropertyName == null)
						continue;
					PropertyDescriptor aProperty = BasicProjectIntrospection
							.findProperty(aClass, anOutputPropertyName);
					if (aProperty == null) {

						// Tracer.info(this,"Property " + aPropertyName +
						// "not found");
						continue;
					}
					Method aReadMethod = aProperty.getReadMethod();
					if (aReadMethod == null) {
						Tracer.error ("Missing read method for property "
								+ anOutputPropertyName);
						anActualOutputs.put(BasicProjectExecution.MISSING_READ,
								true);
						anActualOutputs.put(BasicProjectExecution.MISSING_READ
								+ "." + anOutputPropertyName, true);
						continue;
					}
					// Object result = timedInvoke(anObject, aReadMethod,
					// getMethodTimeOut(), emptyArgs);
					Object result = BasicProjectExecution.timedInvoke(anObject,
							aReadMethod, BasicProjectExecution.emptyArgs,
							BasicProjectExecution.getMethodTimeOut());
					anActualOutputs.put(anOutputPropertyName, result);
				}
			}

		} catch (NoSuchMethodException e) {
			Tracer.error("Constructor not found:" + e.getMessage());
			anActualOutputs
					.put(BasicProjectExecution.MISSING_CONSTRUCTOR, true);
			// e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			anActualOutputs = null;

			e.printStackTrace();
		} finally {
			anOutput = BasicProjectExecution
					.restoreOutputAndGetRedirectedOutput();
			// if (anOutput != null && !anOutput.isEmpty()) {
			// ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
			// anOutput);
			// }
			anActualOutputs.put(BasicProjectExecution.PRINTS, anOutput);

		}
		boolean getsReturnSets = BasicProjectExecution.getsReturnedSets(
				anInputs, anActualOutputs);
		anActualOutputs.put(BasicProjectExecution.GETS_EQUAL_SETS,
				getsReturnSets);
		return anActualOutputs;
	}

	public  boolean getsReturnedSets(Map<String, Object> anInputs,
			Map<String, Object> anActualOutputs) {
		if (anInputs == null || anActualOutputs == null)
			return false;
//		if (!invokeSetters) {
//			return true;
//		}
//		Set<String> anOutputProperties = anActualOutputs.keySet();
		Set<String> anOutputPropertyNames = new HashSet (Arrays.asList(getOutputPropertyNames()));
		for (String anInputProperty : anInputs.keySet()) {
//			if (!anOutputProperties.contains(anInputProperty))
//			if (!anOutputPropertyNames.contains(anInputProperty))
//
//				continue;
			// check all input properties
			Object aGetterValue = anActualOutputs.get(anInputProperty);
			Object aSetterValue = anInputs.get(anInputProperty);
			if (!Common.equal(aGetterValue, aSetterValue)) {
				Tracer.info(this,"For property:" + anInputProperty
						+ " getter returned:" + aGetterValue + ", instead of:"
						+ aSetterValue);
				wrongInputProperties.add(anInputProperty);
				return false;
			}
		}
		return true;

	}

	public static Map<String, Object> testBean(String aFeatureName,
			String aTestCase, Project aProject, String[] aBeanDescriptions,
			Class[] aConstructorArgTypes, Object[] aConstructorArgs,
			Map<String, Object> anInputs, String[] anOutputProperties,
			Object[] anExpectedValues) {
		if (anOutputProperties.length != anExpectedValues.length) {
			Tracer.error("output properties length not the same as expected values length");
			return null;
		}
		Map<String, Object> anActualOutputs = testBean(aFeatureName, aTestCase,
				aProject, aBeanDescriptions, aConstructorArgTypes,
				aConstructorArgs, anInputs, anOutputProperties);
		for (int i = 0; i < anOutputProperties.length; i++) {
			Object anExpectedOutput = anExpectedValues[i];
			Object anActualOutput;
			Object outputProperty = anOutputProperties[i];
			anActualOutput = outputProperty == null ? null : anActualOutputs
					.get(outputProperty);
			// anActualOutput = anActualOutputs.get(outputProperty);
			if (!Common.equal(anExpectedOutput, anActualOutput)) {
				anActualOutputs.put(
						BasicProjectExecution.EXPECTED_EQUAL_ACTUAL, false);
				anActualOutputs.put(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL
						+ "." + anOutputProperties[i], false);
				System.out.println ("Actua value of proeprty:" + outputProperty + 
						"is:" +anActualOutput+ ", instead of:" + anExpectedOutput + ".");
			}

		}
		return anActualOutputs;

	}

	public static Map<String, Object> testBeanWithStringConstructor(
			String aFeatureName, String aTestCase, Project aProject,
			String[] aBeanDescriptions, String aConstructorArg,
			String anIndependentPropertyName, Object anIndepentValue,
			String anOutputPropertyName, Object anExpectedOutputValue) {
		Class[] aConstructorArgTypes = new Class[] { String.class };
		Object[] aConstructorArgs = new String[] { aConstructorArg };
		String[] anOutputProperties = new String[] { anOutputPropertyName };
		Object[] anExpectedValue = new Object[] { anExpectedOutputValue };
		Map<String, Object> anInputs = new HashMap();
		anInputs.put(anIndependentPropertyName, anIndepentValue);
		return testBean(aFeatureName, aTestCase, aProject, aBeanDescriptions,
				aConstructorArgTypes, aConstructorArgs, anInputs,
				anOutputProperties);

	}

	public static Map<String, Object> testBeanWithNoConstructor(
			String aFeatureName, String aTestCase, Project aProject,
			String[] aBeanDescriptions, String anIndependentPropertyName,
			Object anIndepentValue, String anOutputPropertyName,
			Object anExpectedOutputValue) {
		Class[] aConstructorArgTypes = new Class[] { String.class };
		Object[] aConstructorArgs = new Object[] {};
		String[] anOutputProperties = new String[] { anOutputPropertyName };
		Object[] anExpectedValue = new Object[] { anExpectedOutputValue };
		Map<String, Object> anInputs = new HashMap();
		anInputs.put(anIndependentPropertyName, anIndepentValue);
		return testBean(aFeatureName, aTestCase, aProject, aBeanDescriptions,
				aConstructorArgTypes, aConstructorArgs, anInputs,
				anOutputProperties);

	}

	public static Map<String, Object> testBeanWithStringConstructor(
			String aFeatureName, String aTestCase, Project aProject,
			String[] aBeanDescriptions, String aConstructorArg) {
		Class[] aConstructorArgTypes = new Class[] { String.class };
		Object[] aConstructorArgs = new String[] { aConstructorArg };
		String[] anOutputProperties = new String[] {};
		Object[] anExpectedValue = new Object[] {};
		Map<String, Object> anInputs = new HashMap();
		return testBean(aFeatureName, aTestCase, aProject, aBeanDescriptions,
				aConstructorArgTypes, aConstructorArgs, anInputs,
				anOutputProperties);

	}
	@Override
	protected Class proxyClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void executeOperations(Object aLocatable) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void setActual(Object aLocatable) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected boolean checkOutput(Object aLocatable) {
		// TODO Auto-generated method stub
		return false;
	}
}
