package gradingTools.shared.testcases;

import grader.basics.execution.BasicProjectExecution;
import grader.basics.execution.GradingMode;
import grader.basics.junit.NotesAndScore;
import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.Project;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;

import util.misc.Common;
import util.trace.Tracer;

public class BeanExecutionTest extends MethodExecutionTest{
	protected Map<String, Object> outputPropertyValues;
	protected String[] getBeanDescriptions() {
		return getClassNames();
	}
	protected Class[] getConstructorArgTypes() {
		return null;
	}
	protected Object[] getConstructorArgs() {
		if (GradingMode.getGraderRun())
			return getGraderConstructorArgs();
		else
			return getStudentConstructorArgs();
	}
	protected Object[] getStudentConstructorArgs() {
		return null;
	}
	protected Object[] getGraderConstructorArgs() {
		return getStudentConstructorArgs();
	}
	
	protected String[] getOutputPropertyNames() {
		return null;
	}
	protected Map<String, Object> getInputPropertyValues() {
		return null;
	}
	protected Map<String, Object> getStudentInputPropertyValues() {
		return null;
	}
	protected Map<String, Object> getGraderInputPropertyValues() {
		return getStudentInputPropertyValues();
	}
	protected Map<String, Object> getOutputPropertyValues() {
		return outputPropertyValues;
	}
	static Object[] emptyObjectArray = {};
	protected Object[] getExpectedOutputValues() {
		if (GradingMode.getGraderRun()) {
			return getExpectedGraderOutputValues();
		} else {
			return getExpectedStudentOutputValues();
		}
	}
	protected Object[] getExpectedStudentOutputValues() {
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
		for (int i = 0; i < anOutputProperties.length; i++) {
			Object anExpectedOutput = anExpectedValues[i];
			Object anActualOutput;
			Object outputProperty = anOutputProperties[i];
			anActualOutput = outputProperty == null ? null : anActualOutputs
					.get(outputProperty);
			// anActualOutput = anActualOutputs.get(outputProperty);
			if (!Common.equal(anExpectedOutput, anActualOutput)) {
				anActualOutputs.put(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL, false);
				anActualOutputs.put(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL + "."
						+ anOutputProperties[i], false);
				System.out.println ("Property:" +outputProperty + " expected value:" + anExpectedOutput + " actual output:" + anActualOutput );
			}

		}
	}
	protected  void testBean() throws Throwable {
		String anOutput;
		Map<String, Object> anActualOutputs = new HashMap();
		outputPropertyValues = anActualOutputs;
		String[] aBeanDescriptions = getBeanDescriptions();
		Class[] aConstructorArgTypes = getConstructorArgTypes();
		Object[] aConstructorArgs = getConstructorArgs();
		Map<String, Object> anInputs = getInputPropertyValues();
		String[] anOutputProperties = getOutputPropertyNames();
		try {
			// String[] aBeanDescriptions = aBeanDescription.split(",");
//			if (aBeanDescriptions.length != 4) {
//				Tracer.error("Bean description  in testBean should have 4 elements instead of: "
//						+ aBeanDescriptions.length);
//			}
//			BasicProjectExecution.redirectOutput();
//			System.out.println("Testcase:" + aCheckName);
//			System.out.println("Finding classes matching:"
//					+ Common.toString(aBeanDescriptions));
//			Class aClass = BasicProjectIntrospection.findClass(aProject,
//					aBeanDescriptions[0], aBeanDescriptions[1],
//					aBeanDescriptions[2], aBeanDescriptions[3]);
			
			Class aClass = getTargetClass();

			if (aClass == null) {
				System.out.println("No class matching: "
						+ Common.toString(aBeanDescriptions));
				anActualOutputs.put(BasicProjectExecution.MISSING_CLASS, true);
				// anActualOutputs = null;
			} else {
				System.out.println("Finding constructor matching:"
						+ Common.toString(aConstructorArgTypes));
				// anActualOutputs.put(CLASS_MATCHED,
				// aClass.getCanonicalName());
				anActualOutputs.put(BasicProjectExecution.CLASS_MATCHED, aClass);

				Constructor aConstructor = aClass
						.getConstructor(aConstructorArgTypes);
				Object anObject = BasicProjectExecution.timedInvoke(aConstructor, aConstructorArgs,
						BasicProjectExecution.getMethodTimeOut());
				for (String aPropertyName : anInputs.keySet()) {
					if (aPropertyName == null)
						continue;
					PropertyDescriptor aProperty = BasicProjectIntrospection
							.findProperty(aClass, aPropertyName);
					if (aProperty == null) {
						anActualOutputs.put(BasicProjectExecution.MISSING_PROPERTY, true);
						anActualOutputs.put(BasicProjectExecution.MISSING_PROPERTY + "."
								+ aPropertyName, true);
						 System.out.println("Property " + aPropertyName + "not found in " + aClass.getSimpleName());
						continue;
					}
					Method aWriteMethod = aProperty.getWriteMethod();
					if (aWriteMethod == null) {
						anActualOutputs.put(BasicProjectExecution.MISSING_WRITE, true);
						anActualOutputs.put(
								BasicProjectExecution.MISSING_WRITE + "." + aPropertyName, true);
						System.out.println("Missing write method for property "
								+ aPropertyName);
						continue;
					}
					Object aValue = anInputs.get(aPropertyName);
//					timedInvoke(anObject, aWriteMethod, getMethodTimeOut(),
//							new Object[] { aValue });
//					BasicProjectExecution.timedInvoke(anObject, aWriteMethod, 
//							new Object[] { aValue }, BasicProjectExecution.getMethodTimeOut());
//				
					invokeMethod(anObject, aWriteMethod, new Object[] { aValue });
				
				}
				for (String anOutputPropertyName : anOutputProperties) {
					if (anOutputPropertyName == null)
						continue;
					PropertyDescriptor aProperty = BasicProjectIntrospection
							.findProperty(aClass, anOutputPropertyName);
					if (aProperty == null) {

						// System.out.println("Property " + aPropertyName +
						// "not found");
						continue;
					}
					Method aReadMethod = aProperty.getReadMethod();
					if (aReadMethod == null) {
						System.out.println("Missing read method for property "
								+ anOutputPropertyName);
						anActualOutputs.put(BasicProjectExecution.MISSING_READ, true);
						anActualOutputs.put(BasicProjectExecution.MISSING_READ + "."
								+ anOutputPropertyName, true);
						continue;
					}
//					Object result = timedInvoke(anObject, aReadMethod,
//							getMethodTimeOut(), emptyArgs);
//					Object result = BasicProjectExecution.timedInvoke(anObject, aReadMethod,
//							BasicProjectExecution.emptyArgs, BasicProjectExecution.getMethodTimeOut());
					invokeMethod(anObject, aReadMethod, BasicProjectExecution.emptyArgs);
					anActualOutputs.put(anOutputPropertyName, returnValue);
				}
			}

		} catch (NoSuchMethodException e) {
			System.out.println("Constructor not found:" + e.getMessage());
			anActualOutputs.put(BasicProjectExecution.MISSING_CONSTRUCTOR, true);
			// e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			anActualOutputs = null;

			e.printStackTrace();
		} finally {
//			anOutput = BasicProjectExecution.restoreOutputAndGetRedirectedOutput();
//			if (anOutput != null && !anOutput.isEmpty()) {
//				ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
//						anOutput);
//			}
//			anActualOutputs.put(BasicProjectExecution.PRINTS, anOutput);

		}
		boolean getsReturnSets = getsReturnedSets(
				anInputs, anActualOutputs);
		anActualOutputs.put(BasicProjectExecution.GETS_EQUAL_SETS, getsReturnSets);
		compareOutputWithExpected();
//		return anActualOutputs;
	}
	
	
	
	protected String getsEqualsSetsErrorMessage() {
		return "One or more fets does not return set";
	}
	protected String expectedEqualsActualErrorMessage() {
		return "One or more dependent properties is erroneous";
	}
	protected double getsSetsCredits() {
		
		double aReturnValue = getsEqualSets()?getsEqualsSetsCredit():0;
		aReturnValue += expectedEqualActual()?expectedEqualsActualCredit():0;
		return aReturnValue;
	}
	protected String getsSetsMessage() {
		
		String result = getsEqualSets()?getsEqualsSetsErrorMessage():"";
		result += expectedEqualActual()?expectedEqualsActualErrorMessage():"";
		return result;
	}
	protected void processGetsSets() {
		double aCredit = getsSetsCredits();
		String aMessage = getsSetsMessage();
		if (aMessage.isEmpty())
			return;
		Assert.assertTrue(aMessage
				+ NotesAndScore.PERCENTAGE_MARKER
				+ aCredit, false);
	}
	protected double getsEqualsSetsCredit() {
		return 0.2;
	}
	protected double expectedEqualsActualCredit() {
		return 1.0 - getsEqualsSetsCredit();
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
		Object aGettersEqualsSetters = anOutputPropertyValues.get(BasicProjectExecution.GETS_EQUAL_SETS);
		return(aGettersEqualsSetters != null && aGettersEqualsSetters.equals(true)) ;
	}
	protected boolean expectedEqualActual() {
		Map<String, Object> anOutputPropertyValues = getOutputPropertyValues();
		Object aExectedEqualsExpected = anOutputPropertyValues.get(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL);
		return(aExectedEqualsExpected != null && aExectedEqualsExpected.equals(true)) ;
	}
	public static  Map<String, Object> testBean(String aFeatureName,
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
			System.out.println("Testcase:" + aCheckName);
			System.out.println("Finding class matching:"
					+ Common.toString(aBeanDescriptions));
			Class aClass = BasicProjectIntrospection.findClass(aProject,
					aBeanDescriptions[0], aBeanDescriptions[1],
					aBeanDescriptions[2], aBeanDescriptions[3]);

			if (aClass == null) {
				System.out.println("No class matching: "
						+ Common.toString(aBeanDescriptions));
				anActualOutputs.put(BasicProjectExecution.MISSING_CLASS, true);
				// anActualOutputs = null;
			} else {
				System.out.println("Finding constructor matching:"
						+ Common.toString(aConstructorArgTypes));
				// anActualOutputs.put(CLASS_MATCHED,
				// aClass.getCanonicalName());
				anActualOutputs.put(BasicProjectExecution.CLASS_MATCHED, aClass);

				Constructor aConstructor = aClass
						.getConstructor(aConstructorArgTypes);
				Object anObject = BasicProjectExecution.timedInvoke(aConstructor, aConstructorArgs,
						600);
				for (String aPropertyName : anInputs.keySet()) {
					if (aPropertyName == null)
						continue;
					PropertyDescriptor aProperty = BasicProjectIntrospection
							.findProperty(aClass, aPropertyName);
					if (aProperty == null) {
						anActualOutputs.put(BasicProjectExecution.MISSING_PROPERTY, true);
						anActualOutputs.put(BasicProjectExecution.MISSING_PROPERTY + "."
								+ aPropertyName, true);
						// System.out.println("Property " + aPropertyName +
						// "not found");
						continue;
					}
					Method aWriteMethod = aProperty.getWriteMethod();
					if (aWriteMethod == null) {
						anActualOutputs.put(BasicProjectExecution.MISSING_WRITE, true);
						anActualOutputs.put(
								BasicProjectExecution.MISSING_WRITE + "." + aPropertyName, true);
						System.out.println("Missing write method for property "
								+ aPropertyName);
						continue;
					}
					Object aValue = anInputs.get(aPropertyName);
//					timedInvoke(anObject, aWriteMethod, getMethodTimeOut(),
//							new Object[] { aValue });
					BasicProjectExecution.timedInvoke(anObject, aWriteMethod, 
							new Object[] { aValue }, BasicProjectExecution.getMethodTimeOut());
				}
				for (String anOutputPropertyName : anOutputProperties) {
					if (anOutputPropertyName == null)
						continue;
					PropertyDescriptor aProperty = BasicProjectIntrospection
							.findProperty(aClass, anOutputPropertyName);
					if (aProperty == null) {

						// System.out.println("Property " + aPropertyName +
						// "not found");
						continue;
					}
					Method aReadMethod = aProperty.getReadMethod();
					if (aReadMethod == null) {
						System.out.println("Missing read method for property "
								+ anOutputPropertyName);
						anActualOutputs.put(BasicProjectExecution.MISSING_READ, true);
						anActualOutputs.put(BasicProjectExecution.MISSING_READ + "."
								+ anOutputPropertyName, true);
						continue;
					}
//					Object result = timedInvoke(anObject, aReadMethod,
//							getMethodTimeOut(), emptyArgs);
					Object result = BasicProjectExecution.timedInvoke(anObject, aReadMethod,
							BasicProjectExecution.emptyArgs, BasicProjectExecution.getMethodTimeOut());
					anActualOutputs.put(anOutputPropertyName, result);
				}
			}

		} catch (NoSuchMethodException e) {
			System.out.println("Constructor not found:" + e.getMessage());
			anActualOutputs.put(BasicProjectExecution.MISSING_CONSTRUCTOR, true);
			// e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			anActualOutputs = null;

			e.printStackTrace();
		} finally {
			anOutput = BasicProjectExecution.restoreOutputAndGetRedirectedOutput();
//			if (anOutput != null && !anOutput.isEmpty()) {
//				ARunningProject.appendToTranscriptFile(aProject, aFeatureName,
//						anOutput);
//			}
			anActualOutputs.put(BasicProjectExecution.PRINTS, anOutput);

		}
		boolean getsReturnSets = BasicProjectExecution.getsReturnedSets(
				anInputs, anActualOutputs);
		anActualOutputs.put(BasicProjectExecution.GETS_EQUAL_SETS, getsReturnSets);
		return anActualOutputs;
	}
	 public static boolean getsReturnedSets(Map<String, Object> anInputs, 
			 Map<String, Object> anActualOutputs) {
		 if (anInputs == null || anActualOutputs == null) 
			 return false;
		 Set<String> anOutputProperties = anActualOutputs.keySet();
		 for (String anInputProperty:anInputs.keySet()) {
			 if (!anOutputProperties.contains(anInputProperty))
				 continue;
			 Object aGetterValue = anActualOutputs.get(anInputProperty);
			 Object aSetterValue = anInputs.get(anInputProperty);
			if (! Common.equal(aGetterValue, aSetterValue) ) {
				System.out.println ("For property:" + anInputProperty + " getter returned:" + aGetterValue + 
						" instead of:" + aSetterValue);
				return false;
			}
		 }
		 return true;
	    	
	    }

	public static  Map<String, Object> testBean(String aFeatureName,
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
				anActualOutputs.put(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL, false);
				anActualOutputs.put(BasicProjectExecution.EXPECTED_EQUAL_ACTUAL + "."
						+ anOutputProperties[i], false);
			}

		}
		return anActualOutputs;

	}

	public static  Map<String, Object> testBeanWithStringConstructor(
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

	public static  Map<String, Object> testBeanWithNoConstructor(
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
}
