package grader.basics.project;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.junit.Assert;

import grader.basics.config.BasicConfigurationManagerSelector;
import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.config.BasicStaticConfigurationUtils;
import grader.basics.execution.BasicProjectExecution;
import grader.basics.junit.BasicJUnitUtils;
//import framework.grading.testing.TestCase;
import grader.basics.junit.GradableJUnitSuite;
import grader.basics.util.Permutations;
import util.annotations.Tags;
import util.introspect.JavaIntrospectUtility;
import util.models.Hashcodetable;
import util.trace.Tracer;

public class BasicProjectIntrospection {

	static boolean uniqueMainRun = false;
	static Map<String, Class> keyToClass = new HashMap();
	static Map<String, Class> keyToInterface = new HashMap();

	static Map<String, Method> keyToMethod = new HashMap();
	static Map<String, Integer[]> methodKeysToArgIndices = new HashMap(); // should
																			// be
																			// combined
																			// with
																			// revious
																			// hashmap
	static Map<Object, Object> userObjects = new HashMap();
	static Hashcodetable<Object, Object> reverseProxyToObject = new Hashcodetable<>();
	static Hashcodetable<Object, Object> reverseObjectToProxy = new Hashcodetable<>();

	static Hashcodetable<Object, Object> proxyToObject = new Hashcodetable<>();
	static Hashcodetable<Object, Object> objectToProxy = new Hashcodetable<>();
	static Hashcodetable<Class, Object> classToProxy = new Hashcodetable<>();
	static Hashcodetable<Class, Object> reverseClassToProxy = new Hashcodetable<>();
	static Map<Class, Class> reverseProxyClassToClass = new HashMap();

	static Map<Class, Class> classToType = new HashMap();

	static Set<String> predefinedPackages = new HashSet();
	static Set<GradableJUnitSuite> topLevelSuites = null;
	static Class mainClass;
	static boolean useMainClass = false;
	
	
	public static boolean isUseMainClass() {
		return useMainClass;
	}
	public static void setUseMainClass(boolean useMainClass) {
		BasicProjectIntrospection.useMainClass = useMainClass;
	}
	public static void setMainClass (Class aMainClass) {
		mainClass = aMainClass;
	}
	
	public static Class getMainClass() {
		if (!isUseMainClass()) {
			return null;
		}
		return mainClass;
	}

	public static void clearProjectCaches() {
		mainClass = null;
		proxyToObject.clear();
		classToMethodMatch.clear();
		classToProxy.clear();
		keyToClass.clear();
		keyToInterface.clear();
		keyToMethod.clear();
		methodKeysToArgIndices.clear();
		objectToProxy.clear();
		reverseClassToProxy.clear();
		reverseObjectToProxy.clear();
		reverseProxyToObject.clear();
		topLevelSuites = null;
		uniqueMainRun = false;
		userObjects.clear();
		
		if (BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
				BasicConfigurationManagerSelector.getConfigurationManager().clear();
		}
		// classToType.clear(); // why clear this?
	}

	public static boolean inUniqueMainRun() {
		return uniqueMainRun;
	}

	public static void setUniqueMainRun(boolean newVal) {
		uniqueMainRun = newVal;
	}

	public static Class<?> getClassForInterface(Project project, Class<?> target) {
		Set<ClassDescription> classes = project.getClassesManager().get()
				.getClassDescriptions();
		for (ClassDescription classDescription : classes) {
			Class<?> clazz = classDescription.getJavaClass();
			if (target.isAssignableFrom(clazz)) {
				return clazz;
			}
		}
		return null;
	}

	public static Set<Class<?>> getClassesForInterface(Project project,
			Class<?> target) {
		Set<ClassDescription> classes = project.getClassesManager().get()
				.getClassDescriptions();
		Set<Class<?>> ret = new HashSet<>();
		for (ClassDescription classDescription : classes) {
			Class<?> clazz = classDescription.getJavaClass();
			if (!target.equals(clazz) && target.isAssignableFrom(clazz)) {
				if (clazz.isInterface()) {
					ret.addAll(getClassesForInterface(project, clazz));
				}
				ret.add(clazz);
			}
		}
		return ret;
	}

	static String[] emptyStrings = {};

	public static Class findClass(Project aProject, String aName, String aTag,
			String aNameMatch, String aTagMatch) {
		String[] aTags = (aTag == null) ? emptyStrings : new String[] { aTag };

		return findClass(aProject, aName, aTags, aNameMatch, aTagMatch);
	}

	public static Class findInterface(Project aProject, String aName,
			String aTag, String aNameMatch, String aTagMatch) {
		String[] aTags = (aTag == null) ? emptyStrings : new String[] { aTag };

		return findInterface(aProject, aName, aTags, aNameMatch, aTagMatch);
	}

	// not unique class
	// why is this not calling fincClasses
	// public static Class findClass(Project aProject, String aName,
	// String[] aTag, String aNameMatch, String aTagMatch) {
	// int i = 0;
	// List<ClassDescription> aClasses = aProject.getClassesManager().get()
	// .findClass(aName, aTag, aNameMatch, aTagMatch);
	// for (ClassDescription aClass : aClasses) {
	// if (aClass.getJavaClass().isInterface())
	// continue;
	// return aClass.getJavaClass();
	// }
	// return null;
	//
	// // if (aClasses.size() != 1) {
	// // return null;
	// // }
	// // return aClasses.get(0).getJavaClass();
	//
	// }
	public static Set<Class> findClassesWithFewestTags(Set<Class> aClasses) {
		if (aClasses.size() <= 1) return aClasses;
		
		Integer minTags = null;
		for (Class aClass:aClasses) {
			int aNumTags = getTags(aClass).length;
			if (minTags == null) {
				minTags = aNumTags;
			} else if (aNumTags < minTags) {
				minTags = aNumTags;
			}
		}
		
		Set<Class> aRetVal = new HashSet();
		for (Class aClass:aClasses) {
			int aNumTags = getTags(aClass).length;
			if (aNumTags == minTags) {
				aRetVal.add(aClass);
			}
		}
		return aRetVal;
		
	}
	public static Class findClassByExistingSupertype(Project aProject, Class anExistingType) {
		Tracer.info(BasicProjectIntrospection.class,
				"Looking for unique class of existing type " + anExistingType);
		Set<Class> aClasses = findClassesByExistingSupertype(aProject, new Class[] {anExistingType});
				
		
		aClasses = removeSuperTypes(aClasses);
		if (aClasses.size() != 1) {
			if (aClasses.size() > 1) {

				Tracer.info(BasicProjectIntrospection.class,
						"Found multiple matching classes:"
								+ aClasses);
				Class aClass =  aClasses.iterator().next();
				Tracer.info(BasicProjectIntrospection.class, "Choosing " + aClass);
				return aClass;
			} else {
				Tracer.info(BasicProjectIntrospection.class,
						"Found no matching class."
								);
			}
			return null;
		}
		Class aClass = aClasses.iterator().next();
		Tracer.info(BasicProjectIntrospection.class,"Found type:" + aClass);
		return aClass;
//		return aClasses.iterator().next();

	}
	public static Class findClass(Project aProject, String aName,
			String[] aTag, String aNameMatch, String aTagMatch) {
		String aTagsString = aTag == null ? "" : Arrays.toString(aTag);
		Tracer.info(BasicProjectIntrospection.class,
				"Looking for unique class matching " + "name " + aName
						+ " tags " + aTagsString + " name regex " + aNameMatch
						+ " tag regex " + aTagMatch);
		Set<Class> aClasses = findClasses(aProject, aName, aTag, aNameMatch,
				aTagMatch);
		/*
		 * Switched order so we can find super class if it has fewer tags
		 */
		aClasses = findClassesWithFewestTags(aClasses);
		aClasses = removeSuperTypes(aClasses);
		if (aClasses.size() != 1) {
			if (aClasses.size() > 1) {

				Tracer.info(BasicProjectIntrospection.class,
						"Found multiple matching classes matching descriptor:"
								+ aClasses);
			} else {
				Tracer.info(BasicProjectIntrospection.class,
						"Found no class matching descriptor."
								);
			}
			return null;
		}
		Class aClass = aClasses.iterator().next();
		Tracer.info(BasicProjectIntrospection.class,"Found type:" + aClass);
		return aClass;
//		return aClasses.iterator().next();

	}

	public static Class findInterface(Project aProject, String aName,
			String[] aTag, String aNameMatch, String aTagMatch) {
		String aTagsDisplay = aTag == null ? "" : Arrays.toString(aTag);

		Tracer.info(BasicProjectIntrospection.class,
				"Looking for unique interface matching " + "name " + aName
						+ " tags " + aTagsDisplay + " name regex " + aNameMatch
						+ " tag regex " + aTagMatch);

		Set<Class> aClasses = findInterfaces(aProject, aName, aTag, aNameMatch,
				aTagMatch);

		aClasses = removeSuperTypes(aClasses);
//		if (aClasses.size() != 1 && aTag != null) {
//			Tracer.error("Tag:" + Arrays.toString(aTag)
//					+ "matched by zero or multiple interfaces:" + aClasses);
//			return null;
//		}
		if (aClasses.size() < 1  && aTag != null) {
			Tracer.error("Tag:" + Arrays.toString(aTag)
					+ "matched by no interface");
			return null;
		}
		if (aClasses.size() >  1 && aTag != null) {
			Tracer.error("Tag:" + Arrays.toString(aTag)
					+ "matched by multiple interfaces:" + aClasses);
			return null;
		}
		Class aClass = aClasses.iterator().next();
		Tracer.info(BasicProjectIntrospection.class,"Found type:" + aClass);
		return aClass;
//		return aClasses.iterator().next();

	}

	public static Class findClassByMethods(Project aProject, Method[] aMethods,
			boolean findClass) {
		Tracer.info(
				BasicProjectIntrospection.class,
				"Looking for unique class matching methods "
						+ Arrays.toString(aMethods));

		Set<Class> aClasses = findClassesByMethods(aProject, aMethods,
				findClass);

		// int i = 0;
		// List<ClassDescription> aClasses = aProject.getClassesManager().get()
		// .findClass(aName, aTag, aNameMatch, aTagMatch);
		// for (ClassDescription aClass : aClasses) {
		// if (aClass.getJavaClass().isInterface())
		// continue;
		// return aClass.getJavaClass();
		// }
		// return null;
		aClasses = removeSuperTypes(aClasses);
		if (aClasses.size() != 1) {
			return null;
		}
		Class aClass = aClasses.iterator().next();
		Tracer.info(BasicProjectIntrospection.class, "Found type:" + aClass);
		return aClass;
//		return aClasses.iterator().next();

	}

	public static Class findBestClassByMethods(Project aProject,
			Method[] aMethods, boolean findClass) {
		Tracer.info(
				BasicProjectIntrospection.class,
				"Looking for best class matching methods "
						+ Arrays.toString(aMethods));

		Set<Class> aClasses = findBestClassesByMethods(aProject, aMethods,
				findClass);

		// int i = 0;
		// List<ClassDescription> aClasses = aProject.getClassesManager().get()
		// .findClass(aName, aTag, aNameMatch, aTagMatch);
		// for (ClassDescription aClass : aClasses) {
		// if (aClass.getJavaClass().isInterface())
		// continue;
		// return aClass.getJavaClass();
		// }
		// return null;
		aClasses = removeSuperTypes(aClasses);
		if (aClasses.size() != 1) {
			return null;
		}
		Class aClass = aClasses.iterator().next();
		Tracer.info(BasicProjectIntrospection.class,"Found type:" + aClass);

		return aClass;

//		return aClasses.iterator().next();

	}

	public static boolean implementsOneOf(Class aClass, Set<Class> anInterfaces) {
		// recursively get all interfaces
		Vector<Class> aClassInterfaces = new Vector();
		JavaIntrospectUtility.addInterfaces(aClassInterfaces, aClass);
		Set<Class> aClassInterfacesSet = new HashSet(aClassInterfaces);
		aClassInterfacesSet.retainAll(anInterfaces);
		return aClassInterfacesSet.size() != 0;
	}

	public static boolean isSuperTypeOfOneOf(Class aClass, Set<Class> aTypes) {
		// recursively get all interfaces
		for (Class aType : aTypes) {
			Vector<Class> aSuperTypes = JavaIntrospectUtility.getTypes(aType);
			aSuperTypes.remove(aType);
			if (aSuperTypes.contains(aClass))
				return true;
		}

		return false;
	}

	public static Set<Class> removeSuperTypes(Set<Class> anOriginal) {
		if (anOriginal.size() <= 1)
			return anOriginal;
		Set<Class> aRetVal = new HashSet();
		for (Class aClass : anOriginal) {
			if (!isSuperTypeOfOneOf(aClass, anOriginal)) {
				aRetVal.add(aClass);
			}
		}
		return aRetVal;
	}

	public static boolean matchCandidateClass(Class aCandidate, Class aProxy) {
		return findProjectClass(aProxy) == aCandidate;

	}

	public static boolean matchCandidateInterface(Class aCandidate, Class aProxy) {
		if (aCandidate.equals(reverseProxyClassToClass.get(aProxy)))
			return true;
		return findProjectInterface(aProxy) == aCandidate;

	}

	public static boolean matchesParameterTypes(Class aCandidate, Class aProxy) {

		if (aCandidate.equals(aProxy))
			return true;
		if (aProxy.getName().startsWith("java"))
			return false; // not a user defined class, should have a list of
							// prefixes
		if (aCandidate.getName().startsWith("java"))
			return false; // would not need proxy for predefine class
		if (aProxy.isPrimitive()) {
			return false; // they should have been equal
		}
		if (aCandidate.isPrimitive() || aCandidate.equals(String.class)) {
			return false;  // they should have been equal
		}
		// types in two different systems
		if (aCandidate.isInterface()) {
			return matchCandidateInterface(aCandidate, aProxy);
		} else if (aCandidate.isArray()) {
			if (!aProxy.isArray())
				return false;
			return matchesParameterTypes(aCandidate.getComponentType(),
					aProxy.getComponentType());
		}

		else {
			return matchCandidateClass(aCandidate, aProxy);
		}
		//
		// String aProxySimpleName = aProxies[anIndex].getSimpleName();
		// String aCandidateSimpleName = aCandidates[anIndex].getSimpleName();
		// if (
		// BasicProjectIntrospection.getCachedClass(aProxySimpleName) !=
		// aCandidates[anIndex] &&
		// !aProxySimpleName.equals(aCandidateSimpleName)) { // if am inteface
		// and the class has not been looked up
		// return false;
		// }

		// if (aProxies[anIndex].getCanonicalName().startsWith("java")) {
		// return false; // means this is not a user defined class
		// }

	}

	public static boolean matchesParameters(Class[] aCandidates,
			Class[] aProxies) {
		if (aCandidates.length != aProxies.length)
			return false;
		for (int anIndex = 0; anIndex < aProxies.length; anIndex++) {
			Class aCandidate = aCandidates[anIndex];
			Class aProxy = aProxies[anIndex];
			if (!matchesParameterTypes(aCandidate, aProxy))
				return false;
			// if (aCandidate == aProxy)
			// continue;
			// // types in two different systems
			// if (aCandidate.isInterface()) {
			// return matchCandidateInterface(aCandidate, aProxy);
			// } else if (aCandidate.isArray()) {
			// if (!aProxy.isArray())
			// return false;
			// return aCandidate.getComponentType().eqaProxy.getComponentType();
			// }
			//
			// else {
			// return matchCandidateClass(aCandidates[anIndex],
			// aProxies[anIndex]);
			// }
			//
			// String aProxySimpleName = aProxies[anIndex].getSimpleName();
			// String aCandidateSimpleName =
			// aCandidates[anIndex].getSimpleName();
			// if (
			// BasicProjectIntrospection.getCachedClass(aProxySimpleName) !=
			// aCandidates[anIndex] &&
			// !aProxySimpleName.equals(aCandidateSimpleName)) { // if am
			// inteface and the class has not been looked up
			// return false;
			// }

			// if (aProxies[anIndex].getCanonicalName().startsWith("java")) {
			// return false; // means this is not a user defined class
			// }
		}
		return true;

	}

	public static Set<Class> getAllClasses(Project aProject) {
		Set<ClassDescription> aClassDescriptions = aProject.getClassesManager()
				.get().getClassDescriptions();
		Set<Class> aResult = new HashSet();
		for (ClassDescription aClassDescription : aClassDescriptions) {
			Class aClass = aClassDescription.getClass();
			if (!aClass.isInterface()) {
				aResult.add(aClass);
				;
			}

		}
		return aResult;
	}

	public static Set<Class> getAllInterfaces() {
		return getAllInterfaces(CurrentProjectHolder
				.getOrCreateCurrentProject());
	}

	public static Set<Class> getAllClasses() {
		return getAllClasses(CurrentProjectHolder.getOrCreateCurrentProject());
	}

	public static Set<Class> getAllClassesAndInterfaces() {
		return getAllClassesAndInterfaces(CurrentProjectHolder
				.getOrCreateCurrentProject());
	}

	public static Set<Class> getAllInterfaces(Project aProject) {
		Set<ClassDescription> aClassDescriptions = aProject.getClassesManager()
				.get().getClassDescriptions();
		Set<Class> aResult = new HashSet();
		for (ClassDescription aClassDescription : aClassDescriptions) {
			Class aClass = aClassDescription.getClass();
			if (aClass.isInterface()) {
				aResult.add(aClass);
			}

		}
		return aResult;
	}

	public static Set<Class> getAllClassesAndInterfaces(Project aProject) {
		Set<ClassDescription> aClassDescriptions = aProject.getClassesManager()
				.get().getClassDescriptions();
		Set<Class> aResult = new HashSet();
		for (ClassDescription aClassDescription : aClassDescriptions) {
			Class aClass = aClassDescription.getClass();
			aResult.add(aClass);

		}
		return aResult;
	}

	static Set<String> pendingMatches = new HashSet();

	public static String constructKey(Class aClass, Method[] aMethods) {
		return aClass.getName() + ":" + Arrays.toString(aMethods);
	}

	static Method[] emptyMethodArray = {};

	public static Method[] getNonObjectMethods(Class aClass) {
		List<Method> retVal = new ArrayList();
		for (Method aMethod : aClass.getMethods()) {
			if (aMethod.getDeclaringClass() != Object.class) {
				retVal.add(aMethod);
			}
		}
		return retVal.toArray(emptyMethodArray);

	}
/**
 * It returns the first class, which is probably the right thing to do
 * for efficiency.
 */
	public static Set<Class> findBestClassesByMethods(Project aProject,
			Method[] aMethods, boolean findClass) {

		Set<ClassDescription> aClassDescriptions = aProject.getClassesManager()
				.get().getClassDescriptions();
		Set<Class> aResult = new HashSet();
		if (aMethods.length == 0) { // e.g. an array
			return aResult;
		}
		double aMaxDegree = 0;
		for (ClassDescription aClassDescription : aClassDescriptions) {
			Class aClass = aClassDescription.getJavaClass();
			if (aClass.isInterface() && findClass || !aClass.isInterface()
					&& !findClass) {
				continue;
			}
			// String aMatchKey = aClass.getName() + ":" +
			// Arrays.toString(aMethods);
			String aMatchKey = constructKey(aClass, aMethods);

			if (pendingMatches.contains(aMatchKey)) {
				aResult.add(aClass); // assume this one will work, at the top
										// level it might fail, do not want
										// infinite recursion
				return aResult;
			} else {
				pendingMatches.add(aMatchKey);
				double aCurrentDegree = degreeMethodMatches(aClass, aMethods);

				// if (matchesMethods(aClass, aMethods)) {
				if (aCurrentDegree >= 0.5 && aCurrentDegree == aMaxDegree) {
					aResult.add(aClass);
				} else if (aCurrentDegree > aMaxDegree) {
					aResult.clear();
					aResult.add(aClass);
					aMaxDegree = aCurrentDegree;
				}

			}

		}
		for (ClassDescription aClassDescription : aClassDescriptions) {
			Class aClass = aClassDescription.getJavaClass();
			String aMatchKey = constructKey(aClass, aMethods);
			pendingMatches.remove(aMatchKey); // remove all might remove searchs
												// for other classes
		}
		return aResult;
	}

	public static Set<Class> findClassesByMethods(Project aProject,
			Method[] aMethods, boolean findClass) {
		Set<ClassDescription> aClassDescriptions = aProject.getClassesManager()
				.get().getClassDescriptions();
		Set<Class> aResult = new HashSet();
		for (ClassDescription aClassDescription : aClassDescriptions) {
			Class aClass = aClassDescription.getJavaClass();
			if (aClass.isInterface() && findClass || !aClass.isInterface()
					&& !findClass) {
				continue;
			}
			// String aMatchKey = aClass.getName() + ":" +
			// Arrays.toString(aMethods);
			String aMatchKey = constructKey(aClass, aMethods);

			if (pendingMatches.contains(aMatchKey)) {
				aResult.add(aClass); // assume this one will work, at the top
										// level it might fail, do not want
										// infinite recursion
				return aResult;
			} else {
				pendingMatches.add(aMatchKey);
				if (matchesMethods(aClass, aMethods)) {

					aResult.add(aClass);
				}
				pendingMatches.remove(aMatchKey);

			}

		}

		return aResult;
	}

	// why are these not all sets?
	public static Set<Class> findClasses(Project aProject, String aName,
			String aTag, String aNameMatch, String aTagMatch) {
		Set<Class> result = new HashSet();
		Set<ClassDescription> aClasses = aProject.getClassesManager().get()
				.findClassesAndInterfaces(aName, aTag, aNameMatch, aTagMatch);
		Set<Class> aMatchedInterfaces = new HashSet();
		for (ClassDescription aClass : aClasses) {
			if (aClass.getJavaClass().isInterface()) {
				aMatchedInterfaces.add(aClass.getJavaClass());
				continue;
			}
			result.add(aClass.getJavaClass());
		}
		if (!result.isEmpty())
			return result;
		if (aMatchedInterfaces.isEmpty())
			return result;
		// now see if ant of the classes implement any of the matched interfaces
		for (ClassDescription aClassDescription : aClasses) {
			Class aClass = aClassDescription.getJavaClass();
			if (aClass.isInterface()) {
				continue;
			}
			if (implementsOneOf(aClass, aMatchedInterfaces)) {
				result.add(aClassDescription.getJavaClass());
			}
		}
		return result;
		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}
//	public static Set<Class> findClasses(Project aProject, String[] aTag) {
//		return findClassesWithFewestTags(findClasses(aProject,  null,
//				aTag, null, null));
//	}


	// why are these not all sets?
	public static Set<Class> findClasses(Project aProject, String aName,
			String[] aTag, String aNameMatch, String aTagMatch) {
//		if (aName == null && aTag != null && aTag.length > 0) {
//			aName = aTag[0];
//		}
		Set<Class> result = new HashSet();
		ClassesManager aClassManager = aProject.getClassesManager().get();
		if (aClassManager.getClassDescriptions().size() == 0) {
			Assert.assertTrue(
					"No compiled classes found in bin, see transcript for compile errors",
					false);
		}

		// Set<ClassDescription> aClasses = aProject.getClassesManager().get()
		// .findClassAndInterfaces(aName, aTag, aNameMatch, aTagMatch);
		Set<ClassDescription> aClasses = aClassManager.findClassAndInterfaces(
				aName, aTag, aNameMatch, aTagMatch);
		// if (aClasses == null || aClasses.size() == 0) {
		// Assert.assertTrue("No classes found, see console for compile errors",
		// false);
		// }
		Set<Class> aMatchedInterfaces = new HashSet();
		for (ClassDescription aClass : aClasses) {
			if (aClass.getJavaClass().isInterface()) {
				aMatchedInterfaces.add(aClass.getJavaClass());
				continue;
			}
			result.add(aClass.getJavaClass());
		}
		if (!result.isEmpty() && result.size() == 1)
			return result;
		if (aMatchedInterfaces.isEmpty())
			return result;
		// now see if ant of the classes implement any of the matched interfaces
		for (ClassDescription aClassDescription : aProject.getClassesManager()
				.get().getClassDescriptions()) {
			Class aClass = aClassDescription.getJavaClass();
			if (aClass.isInterface()) {
				continue;
			}
			String[] aClassTags = getTags(aClass);
			if (aClassTags != null && aClassTags.length > 0) {
			  continue; // class has its own tags, no need to derive them
			}
			if (implementsOneOf(aClass, aMatchedInterfaces)) {
				result.add(aClassDescription.getJavaClass());
			}
		}
		if (result.isEmpty()) {
		  return aMatchedInterfaces; // maybe we are looking for interfaces also
		}
		return result;
		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}
	public static Set<Class> findClassesByExistingSupertype(Project aProject, Class[] aSupertypes) {
//		if (aName == null && aTag != null && aTag.length > 0) {
//			aName = aTag[0];
//		}
		Set<Class> result = new HashSet();
		ClassesManager aClassManager = aProject.getClassesManager().get();
		if (aClassManager.getClassDescriptions().size() == 0) {
			Assert.assertTrue(
					"No compiled classes found in bin, see transcript for compile errors",
					false);
		}

		// Set<ClassDescription> aClasses = aProject.getClassesManager().get()
		// .findClassAndInterfaces(aName, aTag, aNameMatch, aTagMatch);
		Set<ClassDescription> aClassDescriptions = aClassManager.findBySupertypes(aSupertypes);
		
		Set<Class> aRetVal = new HashSet<>(aClassDescriptions.size());
		for (ClassDescription aClassDescription:aClassDescriptions) {
			aRetVal.add(aClassDescription.getJavaClass());
		}
		return aRetVal;
		

	}

	public static Class find(Project aProject, String aName, String aTag,
			String aNameMatch, String aTagMatch) {
		Set<ClassDescription> aClasses = aProject.getClassesManager().get()
				.findClassesAndInterfaces(aName, aTag, aNameMatch, aTagMatch);
		for (ClassDescription aClass : aClasses) {
			if (aClass.getJavaClass().isInterface())
				continue;
			return aClass.getJavaClass();
		}
		return null;

		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}

	protected static String toClassDescriptor(String aClassName) {
		return aClassName + "." + "class";
	}

	protected static String toObjectDescriptor(String aTag) {
		return aTag + "." + "instance";
	}

	protected static String toClassesDescriptor(String aClassName) {
		return aClassName + "." + "classes";
	}

	protected static String toInterfacesDescriptor(String aClassName) {
		return aClassName + "." + "interfaces";
	}

	protected static String toInterfaceDescriptor(String aClassName) {
		return aClassName + "." + "interface";
	}

	// public static void putInstance(Project aProject, TestCase aTestCase,
	// String aClassName, Object anObject) {
	// aTestCase.getCheckable().getRequirements()
	// .putUserObject(toObjectDescriptor(aClassName), anObject);
	//
	// }

	// public static Object getInstance(Project aProject, TestCase aTestCase,
	// String aClassName) {
	// return aTestCase.getCheckable().getRequirements()
	// .getUserObject(toObjectDescriptor(aClassName));
	//
	// }

	// public static Class getOrFindClass(Project aProject, TestCase aTestCase,
	// String aClassName) {
	// Class aClassObject = getClass(aProject, aTestCase, aClassName);
	// // Class aClassObject =
	// //
	// (Class)aTestCase.getCheckable().getRequirements().getUserObject(toClassDescriptor(aClassName));
	// if (aClassObject == null) {
	// aClassObject = BasicProjectIntrospection.findClass(aProject, aClassName);
	// putClass(aProject, aTestCase, aClassName, aClassObject);
	// //
	// aTestCase.getCheckable().getRequirements().putUserObject(toClassDescriptor(aClassName),
	// // aClassObject);
	// }
	// return aClassObject;
	// }
	//
	// public static Class getClass(Project aProject, TestCase aTestCase,
	// String aClassName) {
	// return (Class) aTestCase.getCheckable().getRequirements()
	// .getUserObject(toClassDescriptor(aClassName));
	//
	// }
	//
	// public static void putClass(Project aProject, TestCase aTestCase,
	// String aClassName, Class aClassObject) {
	// aTestCase.getCheckable().getRequirements()
	// .putUserObject(toClassDescriptor(aClassName), aClassObject);
	//
	// }
	//
	// public static Set<Class> getOrFindClasses(Project aProject,
	// TestCase aTestCase, String aClassName) {
	// Set<Class> aClassObject = (Set<Class>) aTestCase.getCheckable()
	// .getRequirements()
	// .getUserObject(toClassesDescriptor(aClassName));
	// if (aClassObject == null) {
	// aClassObject = BasicProjectIntrospection.findClasses(aProject,
	// aClassName);
	// aTestCase
	// .getCheckable()
	// .getRequirements()
	// .putUserObject(toClassesDescriptor(aClassName),
	// aClassObject);
	// }
	// return aClassObject;
	// }
	//
	// public static Set<Class> getOrFindInterfaces(Project aProject,
	// TestCase aTestCase, String aClassName) {
	// Set<Class> aClassObject = (Set<Class>) aTestCase.getCheckable()
	// .getRequirements()
	// .getUserObject(toInterfacesDescriptor(aClassName));
	// if (aClassObject == null) {
	// aClassObject = BasicProjectIntrospection.findInterfaces(aProject,
	// aClassName);
	// aTestCase
	// .getCheckable()
	// .getRequirements()
	// .putUserObject(toInterfacesDescriptor(aClassName),
	// aClassObject);
	// }
	// return aClassObject;
	// }
	//
	// public static Class getOrFindInterface(Project aProject,
	// TestCase aTestCase, String aClassName) {
	// Class aClassObject = (Class) aTestCase.getCheckable().getRequirements()
	// .getUserObject(toInterfaceDescriptor(aClassName));
	// if (aClassObject == null) {
	// aClassObject = BasicProjectIntrospection
	// .findInterface(aProject, aClassName);
	// aTestCase
	// .getCheckable()
	// .getRequirements()
	// .putUserObject(toInterfaceDescriptor(aClassName),
	// aClassObject);
	// }
	// return aClassObject;
	// }
	//
	// public static List<Method> getOrFindMethodList(Project aProject,
	// TestCase aTestCase, Class aClass, String name, String[] aTag) {
	// String aDescriptor = aClass.toString() + "." + name + "." + "list";
	// List<Method> aMethodObject = (List<Method>) aTestCase.getCheckable()
	// .getRequirements().getUserObject(aDescriptor);
	// if (aMethodObject == null) {
	// aMethodObject = BasicProjectIntrospection.findMethod(aClass, name, aTag);
	// aTestCase.getCheckable().getRequirements()
	// .putUserObject(aDescriptor, aMethodObject);
	// }
	// return aMethodObject;
	// }
	//
	// public static List<Method> getOrFindMethodList(Project aProject,
	// TestCase aTestCase, Class aClass, String name, String aTag) {
	// return getOrFindMethodList(aProject, aTestCase, aClass, name,
	// new String[] { aTag });
	// }
	//
	// public static List<Method> getOrFindMethodList(Project aProject,
	// TestCase aTestCase, Class aClass, String aTag) {
	// String aDescriptor = aClass.toString() + "." + aTag + "." + "list";
	// List<Method> aMethodObject = (List<Method>) aTestCase.getCheckable()
	// .getRequirements().getUserObject(aDescriptor);
	// if (aMethodObject == null) {
	// aMethodObject = BasicProjectIntrospection.findMethod(aClass, aTag);
	// aTestCase.getCheckable().getRequirements()
	// .putUserObject(aDescriptor, aMethodObject);
	// }
	// return aMethodObject;
	// }
	//
	// public static Method getOrFindUniqueMethod(Project aProject,
	// TestCase aTestCase, Class aClass, String aTag) {
	// List<Method> retVal = getOrFindMethodList(aProject, aTestCase, aClass,
	// aTag);
	// if (retVal.isEmpty() || retVal.size() > 1)
	// return null;
	// return retVal.get(0);
	// }

	protected static String toMethodDescriptor(Class aClass, String aTag) {
		return aClass.getCanonicalName() + "." + aTag;
	}

	// public static Method getMethod(Project aProject, TestCase aTestCase,
	// Class aClass, String aTag) {
	//
	// return (Method) aTestCase.getCheckable().getRequirements()
	// .getUserObject(toMethodDescriptor(aClass, aTag));
	//
	// }
	//
	// public static Method putMethod(Project aProject, TestCase aTestCase,
	// Class aClass, String aTag, Method aMethod) {
	//
	// return (Method) aTestCase.getCheckable().getRequirements()
	// .getUserObject(toMethodDescriptor(aClass, aTag));
	//
	// }

	public static String toRegex(String aName) {
		if (aName == null)
			return null;
		return ".*" + aName + ".*";

	}

	public static String[] getInterfaceNames(Class aClass) {
		Class[] anInterfaces = getInterfaces(aClass);
		String[] aResult = new String[anInterfaces.length];
		for (int i = 0; i < anInterfaces.length; i++) {
			aResult[i] = aClass.getName();
		}
		return aResult;
	}

	public static String[] getComputedInterfaceTags(Class aClass) {
		Class[] anInterfaces = getInterfaces(aClass);
		Set<String> aTagSet = new HashSet();
		for (Class anInterface : anInterfaces) {
			aTagSet.add(anInterface.getSimpleName());
			String[] aTags = getTags(anInterface);
			aTagSet.addAll(Arrays.asList(aTags));
		}
		Set<String> aTagList = new HashSet(aTagSet);
		String[] aResult = aTagList.toArray(emptyStringArray);
		return aResult;
	}

	public static Class findProjectClass(Class aProxyClass) {
		return findClass(CurrentProjectHolder.getCurrentProject(), aProxyClass);
	}

	public static Class findProjectInterface(Class aProxyClass) {
		return findInterface(CurrentProjectHolder.getCurrentProject(),
				aProxyClass);
	}

	public static Class findClassByKeyword(String aKey) {
		return findClassByKeyword(
				CurrentProjectHolder.getOrCreateCurrentProject(), aKey);
	}

	public static Class findClassByKeyword(Project aProject, String aKey) {
		Class aCachedClass = keyToClass.get(aKey);
		if (aCachedClass == null) {
			aCachedClass = findClassByTag(aProject, aKey); // tag or name match

			if (aCachedClass == null) {
				aCachedClass = findClassOrInterfaceByName(aProject, aKey);
			}
			if (aCachedClass == null) {
				aCachedClass = findClassByTagMatch(aProject, aKey);
			}
			if (aCachedClass == null) {
				aCachedClass = findClassByNameMatch(aProject, aKey);
			}
			if (aCachedClass == null) {
				aCachedClass = Object.class;
			}
			keyToClass.put(aKey, aCachedClass);

		}
		if (aCachedClass == Object.class)
			return null;
		return aCachedClass;
	}

	public static Class findClass(Class aProxyClass) {
		return findClass(CurrentProjectHolder.getOrCreateCurrentProject(),
				aProxyClass);
	}

	public static Class findInterface(Class aProxyClass) {
		return findInterface(CurrentProjectHolder.getOrCreateCurrentProject(),
				aProxyClass);
	}
	
	protected static Map<String, Class> classToMethodMatch = new HashMap();
	
	protected static void addDummyMethodMatchClass(Class aClass) {
		classToMethodMatch.put(aClass.getName(), Object.class);
	}
	
	protected static boolean isDummyMethodClass(Class aClass) {
		return Object.class.equals(classToMethodMatch.get(aClass.getName()));
	}

	public static Class findClass(Project aProject, Class aProxyClass) {
		// System.out.println(("finding class:" + aProxyClass.getName()));
		if (isPredefinedType(aProxyClass) && !aProxyClass.isInterface())
			return aProxyClass;
		if (isDummyMethodClass(aProxyClass)) { // we have already visited this class in method match, the last alternative
			return null;
		}
		Class aCachedClass = keyToClass.get(aProxyClass.getName());
		if (aCachedClass == null) {
			String[] aTags = getTags(aProxyClass);
			Class retVal = findClassByTags(aProject, aTags);
			if (retVal == null)
				retVal = findClassOrInterfaceByName(aProject,
						aProxyClass.getCanonicalName());
			if (retVal == null)
				retVal = findClassOrInterfaceByName(aProject, aProxyClass.getSimpleName());
			if (retVal == null && aTags.length == 1) {
				retVal = findClassByTagMatch(aProject, aTags[0]);
			}
			if (retVal == null) {
				retVal = findClassByNameMatch(aProject,
						aProxyClass.getSimpleName());
			}
			if (retVal == null) {
				// see if the interfaces of the desired class match
				// do not need this as we are already checking for interfaces in
				// findClass(aProxyClass.getSimpleName()), which uses the
				// namealso as a tag, but keep this
				String[] aComputedTags = getComputedInterfaceTags(aProxyClass);
				retVal = findClassByTags(aProject, aComputedTags);
			}
			if (retVal == null)

				// retVal = findClassByMethods(aProject,
				// aProxyClass.getMethods(), true);
				// retVal = findBestClassByMethods(aProject,
				// aProxyClass.getMethods(), true);
				retVal = findBestClassByMethods(aProject,
						getNonObjectMethods(aProxyClass), true);

			String aKey = null;

			if (retVal == null) {
				retVal = Object.class;
				
				// System.err.println("Could not find class matching:" +
				// Arrays.toString(aTags));
			} else {
				aKey = constructKey(retVal, aProxyClass.getMethods());
			}
			aCachedClass = retVal;

			if (aKey != null && !pendingMatches.contains(aKey)) {

				// aCachedClass = retVal;

				keyToClass.put(aProxyClass.getName(), aCachedClass);
			}
		}
		if (aCachedClass == Object.class) {
			addDummyMethodMatchClass(aProxyClass);
			return null;
		}
		return aCachedClass;

		// if (retVal == null)
		// retVal = Object.class;
		// aCachedClass = retVal;
		//
		// keyToClass.put(aProxyClass.getName(), aCachedClass);
		// }
		// if (aCachedClass == Object.class)
		// return null;
		// return aCachedClass;
	}

	public static Class findInterface(Project aProject, Class aProxyClass) {
		Class aCachedClass = keyToInterface.get(aProxyClass.getName());
		if (aCachedClass == null) {
			String[] aTags = getTags(aProxyClass);
			Class retVal = findInterfaceByTags(aProject, aTags);
			if (retVal == null) {
				Tracer.info(BasicProjectIntrospection.class, "Finding interface by canonical name");

				retVal = findInterface(aProject, aProxyClass.getCanonicalName());
			}
			if (retVal == null) {
				Tracer.info(BasicProjectIntrospection.class, "Finding interface by simple name");
				retVal = findInterface(aProject, aProxyClass.getSimpleName());
			}
			if (retVal == null) {
				// see if the interfaces of the desired class match
				// do not need this as we are already checking for interfaces in
				// findClass(aProxyClass.getSimpleName()), which uses the
				// namealso as a tag, but keep this
				Tracer.info(BasicProjectIntrospection.class, "Finding interface by computed tags");
				String[] aComputedTags = getComputedInterfaceTags(aProxyClass);
				retVal = findInterfaceByTags(aProject, aComputedTags);
			}
			if (retVal == null)

				// retVal = findClassByMethods(aProject,
				// aProxyClass.getMethods(), false);
				retVal = findBestClassByMethods(aProject,
						getNonObjectMethods(aProxyClass), false);

			String aKey = null;

			if (retVal == null)
				retVal = Object.class;
			else
				aKey = constructKey(retVal, aProxyClass.getMethods());
			aCachedClass = retVal;

			if (aKey != null && !pendingMatches.contains(aKey)) {

				// aCachedClass = retVal;

				keyToInterface.put(aProxyClass.getName(), aCachedClass);
			}
		}
		if (aCachedClass == Object.class)
			return null;
		return aCachedClass;
	}

	// public static Class findClass(Project aProject, Class aProxyClass) {
	// Class aCachedClass = keyToClass.get(aProxyClass.getName());
	// if (aCachedClass == null) {
	// String[] aTags = getTags(aProxyClass);
	// Class retVal = findClassByTags(aProject, aTags);
	// if (retVal != null)
	// return retVal;
	// retVal = findClass(aProject, aProxyClass.getCanonicalName());
	// if (retVal != null)
	// return retVal;
	// retVal = findClass(aProject, aProxyClass.getSimpleName());
	// if (retVal != null)
	// return retVal;
	// // see if the interfaces of the desired class match
	// // do not need this as we are already checking for interfaces in
	// findClass(aProxyClass.getSimpleName()), which uses the namealso as a tag,
	// but keep this
	// String[] aComputedTags = getComputedInterfaceTags(aProxyClass);
	// retVal = findClassByTags(aProject, aComputedTags);
	//
	// return findClassByMethods(aProject, aProxyClass.getMethods());
	// }

	public static Class getCachedClass(String aName) {
		return keyToClass.get(aName);
	}

	public static Class findClassCaching(Project aProject, String aName) {
		Class aClass = keyToClass.get(aName);
		if (aClass == null) {
			aClass = findClass(aProject, aName, aName, aName, aName);
			if (aClass == null) {
				aClass = Object.class;
			}
			keyToClass.put(aName, aClass);
		}
		if (aClass == Object.class) {
			return null;
		}
		return aClass;
		// return findClass(aProject, null, aName, toRegex(aName),
		// toRegex(aName) );
		// return findClass(aProject, aName, aName, aName, aName );
	}

	public static Class findClass(Project aProject, String aName) {
		Class aCachedClass = keyToClass.get(aName);
		if (aCachedClass != null) {
			if (Object.class.equals(aCachedClass))
				return null;
			else
				return aCachedClass;
		}

		Class retVal = findClass(aProject, aName, aName, aName, aName);
		if (retVal == null) {
			keyToClass.put(aName, Object.class);
		} else {
			keyToClass.put(aName, retVal);

		}
		return retVal;
		// return findClass(aProject, null, aName, toRegex(aName),
		// toRegex(aName) );
		// return findClass(aProject, aName, aName, aName, aName );
	}

	public static Class findClassByNameMatch(Project aProject, String aName) {
		return findClass(aProject, (String) null, (String) null,
//				toRegex(aName), null);
		aName, null);

		
		
		// return findClass(aProject, null, aName, toRegex(aName),
		// toRegex(aName) );
		// return findClass(aProject, aName, aName, aName, aName );
	}
	public static Class findClassByName(Project aProject, String aName) {
		return findClass(aProject, aName, (String) null,
//				toRegex(aName), null);
		(String) null, null);

		
		
		// return findClass(aProject, null, aName, toRegex(aName),
		// toRegex(aName) );
		// return findClass(aProject, aName, aName, aName, aName );
	}

	public static Class findClassByTagMatch(Project aProject, String aName) {
		return findClass(aProject, (String) null, (String) null, null,
				toRegex(aName));
		// return findClass(aProject, null, aName, toRegex(aName),
		// toRegex(aName) );
		// return findClass(aProject, aName, aName, aName, aName );
	}

	public static Class findInterfaceCaching(Project aProject, String aName) {
		Class aClass = keyToInterface.get(aName);
		if (aClass == null) {
			aClass = findInterface(aProject, aName, aName, aName, aName);
			if (aClass == null) {
				aClass = Object.class;
			}
			keyToInterface.put(aName, aClass);
		}
		if (aClass == Object.class) {
			return null;
		}
		return aClass;
		// return findClass(aProject, null, aName, toRegex(aName),
		// toRegex(aName) );
		// return findClass(aProject, aName, aName, aName, aName );
	}

	public static Class findInterface(Project aProject, String aName) {
		// Class aClass = keyToInterface.get(aName);
		// if (aClass == null) {
		Tracer.info(BasicProjectIntrospection.class, "Finding interface by name:" + aName);

		return findInterface(aProject, aName, aName, aName, aName);

		// return findClass(aProject, null, aName, toRegex(aName),
		// toRegex(aName) );
		// return findClass(aProject, aName, aName, aName, aName );
	}

	public static Set<Class> findClasses(Project aProject, String aName) {
		return findClasses(aProject, null, aName, toRegex(aName),
				toRegex(aName));

	}

	// public static Class findInterface(Project aProject, String aName) {
	// return findInterface(aProject, null, new String[] { aName },
	// toRegex(aName),
	// toRegex(aName));
	//
	// }

	public static Set<Class> findInterfaces(Project aProject, String aName) {
		return findInterfaces(aProject, null, new String[] { aName },
				toRegex(aName), toRegex(aName));

	}

	public static Set<ClassDescription> findClassesByTag(Project aProject,
			String aTag) {
		return aProject
				.getClassesManager()
				.get()
				.findClassAndInterfaces(null, new String[] { aTag }, null, null);

		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}
	public static Set<ClassDescription> findClassesByTags(Project aProject,
			String[] aTags) {
		return 
				aProject
				.getClassesManager()
				.get()
				.findClassAndInterfaces(null, aTags, null, null);

		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}

	public static void putUserObject(Object aKey, Object aValue) {
		userObjects.put(aKey, aValue);
	}

	public static Object getUserObject(Object aKey) {
		return userObjects.get(aKey);
	}

	public static void clearUserObjects() {
		userObjects.clear();
	}

	public static Class findUniqueClassByTag(Project aProject, Class aProxy) {
		String[] aTags = getTags(aProxy);
		return findClassByTags(aProject, aTags);
	}

	public static Class findUniqueClassByTag(Project aProject, String aTag) {
		return findClassByTags(aProject, new String[] { aTag });
	}

	public static Class findClassByTags(String... aTags) {
		Class retVal = findClassByTags(
				CurrentProjectHolder.getOrCreateCurrentProject(), aTags);
		if (retVal == null && aTags.length > 0) {
			Tracer.info(BasicProjectIntrospection.class,
					"Could not find unique class tagged:" + Arrays.toString(aTags));
		}
		// return
		// findClassByTags(CurrentProjectHolder.getOrCreateCurrentProject(),
		// aTags);
		return retVal;
	}

	public static Class findClassByTags(Project aProject, String... aTags) {
		if (aTags.length == 0) {
			return null;
		}
		Arrays.sort(aTags);
		String aKey = Arrays.toString(aTags);
		return findClass(aProject, null, aTags, null, null);
	}

	public static Class findClassByTagsCaching(Project aProject,
			String... aTags) {
		if (aTags.length == 0) {
			return null;
		}
		Arrays.sort(aTags);
		String aKey = Arrays.toString(aTags);
		Class aCachedClass = keyToClass.get(aKey);
		if (aCachedClass == null) {
			
			
//			Class aClass = findClass(aProject, null, aTags, null, null);
			Class aClass = findClass(aProject, aTags[0].replaceAll("\\s",""), aTags, null, null);


			if (aClass == null) {
				aCachedClass = Object.class;

			}

			aCachedClass = aClass;
			keyToClass.put(aKey, aCachedClass);

		}
		if (aCachedClass == Object.class) {
			return null;
		}
		return aCachedClass;
	}

	public static Class findInterfaceByTagsCaching(Project aProject,
			String[] aTags) {
		if (aTags.length == 0) {
			return null;
		}
		Arrays.sort(aTags);
		String aKey = Arrays.toString(aTags);
		Class aCachedClass = keyToClass.get(aKey);
		if (aCachedClass == null) {
			Class aClass = findClass(aProject, null, aTags, null, null);

			if (aClass == null) {
				aCachedClass = Object.class;

			}

			aCachedClass = aClass;
			keyToClass.put(aKey, aCachedClass);

		}
		if (aCachedClass == Object.class) {
			return null;
		}
		return aCachedClass;
	}

	public static Class findInterfaceByTags(Project aProject, String[] aTags) {
		if (aTags.length == 0) {
			return null;
		}
		Tracer.info(BasicProjectIntrospection.class, "Finding interface by tags:" + Arrays.asList(aTags));
		Arrays.sort(aTags);
		String aKey = Arrays.toString(aTags);
		return findInterface(aProject, null, aTags, null, null);

	}

	// public static Class findClassByTags(Project aProject, String[] aTags) {
	// if (aTags.length == 0) {
	// return null;
	// }
	// Arrays.sort(aTags);
	// String aKey = Arrays.toString(aTags);
	// Class aCachedClass = keyToClass.get(aKey);
	// if (aCachedClass == null) {
	//
	// // ClassDescription aClassDescription = findClassDescriptionByTag(
	// // aProject, aTags);
	// // if (aClassDescription == null) {
	// // aCachedClass = Object.class;
	// //
	// // }
	//
	// Class aClass = findClassByTag(
	// aProject, aTags);
	// if (aProject == null) {
	// aCachedClass = Object.class;
	//
	// }
	//
	// aCachedClass = aClass;
	// keyToClass.put(aKey, aCachedClass);
	//
	// }
	// if (aCachedClass == Object.class) {
	// return null;
	// }
	// return aCachedClass;
	// }

	public static ClassDescription findClassDescriptionByTag(Project aProject,
			String[] aTags) {
		Set<ClassDescription> aClasses = findClassDescriptionsByTag(aProject,
				aTags);
		if (aClasses.size() != 1)
			return null;
		return aClasses.iterator().next();

		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}

	public static Class findClassByTag(Project aProject, String... aTags) {
		Set<Class> aClasses = findClassesByTag(aProject, aTags);
		aClasses = removeSuperTypes(aClasses);
		if (aClasses.size() != 1)
			return null;
		Class aClass = aClasses.iterator().next();
		Tracer.info(BasicProjectIntrospection.class,"Found type:" + aClass);
		return aClass;
//		return aClasses.iterator().next();

		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}

	public static Class findClassOrInterfaceByName(Project aProject, String aName) {
		Set<Class> aClasses = findClassesOrInterfacesByName(aProject, aName);
		aClasses = removeSuperTypes(aClasses);
		if (aClasses.size() != 1)
			return null;
		Class aClass = aClasses.iterator().next();
		Tracer.info(BasicProjectIntrospection.class,"Found type:" + aClass);
		return aClass;
//		return aClasses.iterator().next();

		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}

	public static Set<ClassDescription> findClassDescriptionsByTag(
			Project aProject, String[] aTag) {
		// List<String> aSortableTagList = new ArrayList(Arrays.asList(aTag));

		return aProject.getClassesManager().get()
				.findClassAndInterfaces(null, aTag, null, null);

		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}

	public static Set<Class> findClassesByTag(Project aProject, String[] aTag) {
		// List<String> aSortableTagList = new ArrayList(Arrays.asList(aTag));

		Set<ClassDescription> aClassDescriptions = aProject.getClassesManager()
				.get().findClassAndInterfaces(null, aTag, null, null);
		Set<Class> aResult = new HashSet();
		for (ClassDescription aDescription : aClassDescriptions) {
			aResult.add(aDescription.getJavaClass());
		}
		
		return findClassesWithFewestTags(aResult);

		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}

	public static Set<Class> findClassesOrInterfacesByName(Project aProject, String aName) {
		// List<String> aSortableTagList = new ArrayList(Arrays.asList(aTag));

		Set<ClassDescription> aClassDescriptions = aProject.getClassesManager()
				.get().findClassAndInterfaces(aName, null, null, null);
		Set<Class> aResult = new HashSet();
		for (ClassDescription aDescription : aClassDescriptions) {
		  // if it is by name, an interface should be fine
//			if (aDescription.getJavaClass().isInterface())
//				continue;
			aResult.add(aDescription.getJavaClass());
		}
		return aResult;

		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}
	

	// public static Class findInterface(Project aProject, String aName,
	// String aTag, String aNameMatch, String aTagMatch) {
	// Set<ClassDescription> aClasses = aProject.getClassesManager().get()
	// .findClassesAndInterfaces(aName, aTag, aNameMatch, aTagMatch);
	// for (ClassDescription aClass : aClasses) {
	// if (!aClass.getJavaClass().isInterface())
	// continue;
	// return aClass.getJavaClass();
	// }
	// return null;
	//
	// // if (aClasses.size() != 1) {
	// // return null;
	// // }
	// // return aClasses.get(0).getJavaClass();
	//
	// }
	// public static Class findInterface(Project aProject, String aName,
	// String aTag, String aNameMatch, String aTagMatch) {
	// Set<Class> anInterfaces = findInterfaces(aProject, aName, aTag,
	// aNameMatch, aTagMatch);
	// if (anInterface)
	//
	// // if (aClasses.size() != 1) {
	// // return null;
	// // }
	// // return aClasses.get(0).getJavaClass();
	//
	// }

	public static Set<Class> findInterfaces(Project aProject, String aName,
			String aTag, String aNameMatch, String aTagMatch) {
		return findInterfaces(aProject, aName, new String[] { aTag },
				aNameMatch, aTagMatch);
	}

	public static Set<Class> findInterfaces(Project aProject, String aName,
			String[] aTag, String aNameMatch, String aTagMatch) {
		Set<Class> result = new HashSet();
		Set<ClassDescription> aClasses = aProject.getClassesManager().get()
				.findClassAndInterfaces(aName, aTag, aNameMatch, aTagMatch);
		for (ClassDescription aClass : aClasses) {
			if (!aClass.getJavaClass().isInterface())
				continue;
			result.add(aClass.getJavaClass());
		}
		return result;

		// if (aClasses.size() != 1) {
		// return null;
		// }
		// return aClasses.get(0).getJavaClass();

	}

	public static PropertyDescriptor findProperty(Class aClass,
			String aPropertyName) {
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(aClass);

			for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
				if (descriptor.getName().equalsIgnoreCase(aPropertyName)) {
					return descriptor;
				}
			}
		} catch (IntrospectionException e) {
			Tracer.info(BasicProjectIntrospection.class, "Property "
					+ aPropertyName + " not found");

			return null;
		}
		Tracer.info(BasicProjectIntrospection.class, "Property "
				+ aPropertyName + " not found");

		return null;
	}

	public static String[] getTags(Method aMethod) {
		try {
			return aMethod.getAnnotation(Tags.class).value();
		} catch (Exception e) {
			return new String[] {};
		}
	}

	// public static <T> T[] removeDuplicates (T[] anOriginal) {
	//
	// }

	public static String[] getTags(Class<?> aClass) {
		try {
			return aClass.getAnnotation(Tags.class).value();
		} catch (Exception e) {
			return new String[] {};
		}
	}

	public static Method findUniqueMethodByTag(Class aClass, Method aProxy) {
		String[] aTags = getTags(aProxy);
		Class[] aParameterTypes = aProxy.getParameterTypes();
		return findUniqueMethodByTag(aClass, aTags, aParameterTypes);
	}

	static Class[] emptyClassArray = {};
	static Class[] emptyObjectArray = {};

	public static Method findUniqueMethodByTag(Class aClass,
			String aSpecification, Class[] aParameterTypes) {
		return findUniqueMethodByTag(aClass, new String[] { aSpecification },
				aParameterTypes);
	}

	public static Method findUniqueMethodByTag(Class aClass,
			String aSpecification) {
		return findUniqueMethodByTag(aClass, aSpecification, emptyClassArray);
	}

	public static Method findUniqueMethodByTag(Class aClass,
			String[] aSpecification) {
		return findUniqueMethodByTag(aClass, aSpecification, emptyClassArray);
	}

	public static Method findUniqueMethodByTag(Class aClass,
			String[] aSpecification, Class[] aParameterTypes) {
		try {
			Arrays.sort(aSpecification);
			// String aKey = aClass.getName() + ":"
			// + Arrays.toString(aSpecification) + ":"
			// + Arrays.toString(aParameterTypes);
			String aKey = toMethodTag(aClass, aSpecification, aParameterTypes);
			Method aMethod = keyToMethod.get(aKey);
			if (aMethod == null) {
				List<Method> aMethods = findMethodsByTag(aClass, aSpecification);
				aMethod = selectMethodAndCacheIndices(aKey, aMethods,
						aParameterTypes);

				if (aMethod == null) {

					aMethod = Object.class.getMethod("wait", emptyClassArray);
				}
				keyToMethod.put(aKey, aMethod);
			}
			if (aMethod.equals(Object.class.getMethod("wait", emptyClassArray))) {
				return null;
			}
			return aMethod;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// if (aMethods.size() != 1)
		// return null;
		// return aMethods.get(0);
	}
	
	public static boolean nameIsUniqueTag(Method aMethod, String[] aSpecification) {
		
		return aSpecification.length == 1 && aMethod.getName().equalsIgnoreCase(aSpecification[0]);
	}

	/**
	 * Looks for all methods with a particular tag
	 *
	 * @param aSpecification
	 *            The tag to search for
	 * @return The set of matching class descriptions
	 */
	public static List<Method> findMethodsByTag(Class aClass,
			String[] aSpecification) {
		// Set aSpecificationSet = new HashSet(Arrays.asList(aSpecification));
		normalizeTags(aSpecification);
		List aSpecificationList = Arrays.asList(aSpecification);

		List<Method> result = new ArrayList<>();
		for (Method aMethod : aClass.getMethods()) {

			if (matchesTags(aSpecificationList, getTags(aMethod)) ||  
					nameIsUniqueTag(aMethod, aSpecification)) {
				result.add(aMethod);
			}

		}
		if (result.isEmpty()) {
			for (Method aMethod : aClass.getDeclaredMethods()) {

				if (matchesTags(aSpecificationList, getTags(aMethod))) {
					result.add(aMethod);
					aMethod.setAccessible(true);
				}

			}
		}
		return result;
	}

	public static void normalizeTags(String[] aTags) {

		for (int i = 0; i < aTags.length; i++) {
			aTags[i] = aTags[i].replaceAll("\\s", "").toLowerCase();
		}
	}

//	static boolean checkAllSpecifiedTags = false;

	public static boolean isCheckAllSpecifiedTags() {
//		return checkAllSpecifiedTags;
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().isCheckAllSpecifiedTags();
	}
    // should be a property
	public static void setCheckAllSpecifiedTags(boolean newVal) {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setGraderCheckAllSpecifiedTags(newVal);
//		checkAllSpecifiedTags = newVal;
	}

	public static boolean matchAllTags(Set<String> anActualSet,
			List<String> aSpecifications) {
		if (aSpecifications == null || aSpecifications.size() == 0)
			return true;
		return (anActualSet.containsAll(aSpecifications));
	}

	public static boolean matchSomeTag(Set<String> anActualSet,
			List<String> aSpecifications) {
		if (aSpecifications == null || aSpecifications.size() == 0)
			return true;
		for (String aSpecification : aSpecifications) {
			if (anActualSet.contains(aSpecification))
				return true;
		}
		return false;
	}

	public static boolean matchesTags(List<String> aSpecification,
			String[] anActual) {
		if (aSpecification == null || aSpecification.size() == 0)
			return true;
		if (anActual.length == 0) {
			return false;
		}
		String[] anActualsClone = anActual.clone();
		normalizeTags(anActualsClone);
		Set anActualSet = new HashSet(Arrays.asList(anActualsClone));
		if (isCheckAllSpecifiedTags()) {
			return matchAllTags(anActualSet, aSpecification);
		}
		return matchSomeTag(anActualSet, aSpecification);
		// return (anActualSet.containsAll(aSpecification));
	}

	/**
	 * Looks for all methods with a particular tag match
	 *
	 * @param aSpecification
	 *            The tag to search for
	 * @return The set of matching class descriptions
	 */
	public static List<Method> findMethodByTagMatch(Class aClass,
			String aSpecification) {
		List<Method> result = new ArrayList<>();
		for (Method aMethod : aClass.getMethods()) {

			for (String t : getTags(aMethod)) {
				if (t.matches(aSpecification)) {
					result.add(aMethod);
				}
			}
		}
		if (result.isEmpty()) {
			for (Method aMethod : aClass.getDeclaredMethods()) {

				for (String t : getTags(aMethod)) {
					if (t.matches(aSpecification)) {
						result.add(aMethod);
						aMethod.setAccessible(true);
					}
				}

			}
		}
		return result;
	}

	/**
	 * Looks for all methods with a particular name
	 * 
	 */
	public static List<Method> findMethodByNameMatch(Class aClass,
			String aSpecification) {
		List<Method> result = new ArrayList<>();
		for (Method aMethod : aClass.getMethods()) {

			if (aMethod.getName().matches(aSpecification)) {
				result.add(aMethod);
			}

		}
		if (result.isEmpty()) {
			for (Method aMethod : aClass.getDeclaredMethods()) {

				if (aMethod.getName().matches(aSpecification)) {
					result.add(aMethod);
					aMethod.setAccessible(true);
				}

			}
		}
		return result;
	}

	/**
	 * Looks for all methods with a particular name match
	 * 
	 */
	public static List<Method> findMethodByName(Class aClass,
			String aSpecification) {
		if (aClass == null) {
			return null;
		}
		List<Method> result = new ArrayList<>();
		for (Method aMethod : aClass.getMethods()) {

			if (aMethod.getName().equalsIgnoreCase(aSpecification)) {
				result.add(aMethod);
			}

		}
		if (result.isEmpty()) {
			for (Method aMethod : aClass.getDeclaredMethods()) {

				if (aMethod.getName().equalsIgnoreCase(aSpecification)) {
					result.add(aMethod);
					Tracer.warning("Found and using non pubic method:"
							+ aMethod);
					aMethod.setAccessible(true);
				}

			}
		}

		return result;
	}

	public static <T> Map<T, List<Integer>> findElementsIndices(T[] anElements) {
		Map<T, List<Integer>> elementToIndices = new HashMap();
		for (int i = 0; i < anElements.length; i++) {
			List<Integer> aList = elementToIndices.get(anElements[i]);
			if (aList == null) {
				aList = new ArrayList();
				elementToIndices.put(anElements[i], aList);
			}
			aList.add(i);
		}
		return elementToIndices;
	}

	public static void removeDuplicates(Integer[] anIndices,
			Class[] aTargetParameterTypes) {
		Map<Integer, List<Integer>> indexToIndices = findElementsIndices(anIndices);
		Map<Class, List<Integer>> classToIndices = findElementsIndices(aTargetParameterTypes);

		for (Integer anIndex : indexToIndices.keySet()) {
			List<Integer> anOccurences = indexToIndices.get(anIndex);
			if (anOccurences.size() == 1)
				continue;
			Class aClass = aTargetParameterTypes[anIndex];
			List<Integer> classIndices = classToIndices.get(aClass);
			for (int anOccurenceIndex = 0; anOccurenceIndex < anOccurences
					.size(); anOccurenceIndex++) {
				int aDuplicatedIndex = anOccurences.get(anOccurenceIndex);
				anIndices[aDuplicatedIndex] = classIndices
						.get(anOccurenceIndex);

			}

		}

	}

	// we have tried equals
	public static Integer[] isPermutationOf(Class[] anActualParameterTypes,
			Class[] aTargetParameterTypes) {
		Integer[] retVal = null;
		if (anActualParameterTypes.length != aTargetParameterTypes.length)
			return retVal;
		if (aTargetParameterTypes.length == 1)
			return retVal; // no new permutations
		Permutations<Class> permutations = new Permutations<Class>(
				aTargetParameterTypes);
		List<Integer[]> aMatchedndices = new ArrayList();
		while (permutations.hasNext()) {
			retVal = permutations.getIndices();
			Class[] aPermutedTargetParameterTypes = permutations.next();
			if (matchesParameters(anActualParameterTypes,
					aPermutedTargetParameterTypes)) {// we have alrady tried
														// this
				aMatchedndices.add(retVal);
			}
		}
		if (aMatchedndices.size() != 1) {
			return null;
		}
		Integer[] aMatch = aMatchedndices.get(0);
		removeDuplicates(aMatch, aTargetParameterTypes);
		return aMatch;
	}

	public static Method getElaboration(Method aMethod1, Method aMethod2) {
		if (!aMethod1.getName().equals(aMethod2.getName())
				|| aMethod1.getParameterTypes().equals(
						aMethod2.getParameterTypes()))
			return null;
		return aMethod1.getReturnType().isAssignableFrom(
				aMethod2.getReturnType()) ? aMethod2 : aMethod1;

	}

	public static Method selectMethodAndCacheIndices(String aTag,
			List<Method> aMethods, Class[] aParameterTypes) {
		Method aRetVal = null;
		for (Method aMethod : aMethods) {
			// if (Arrays.equals(aMethod.getParameterTypes(), aParameterTypes))
			// {
			if (matchesParameters(aMethod.getParameterTypes(), aParameterTypes)) {
				if (aRetVal != null) {
					aRetVal = getElaboration(aRetVal, aMethod);
					if (aRetVal == null)
						return null;
				} else {
					aRetVal = aMethod;
				}
				// return aMethod; // there can be other matching methods
			}
		}
		if (aRetVal != null) {
			return aRetVal;
		}
		Integer[] anIndices = null;
		// now we should try all permutations of method parameter types
		for (Method aMethod : aMethods) {
			anIndices = isPermutationOf(aMethod.getParameterTypes(),
					aParameterTypes);
			// if (isPermutationOf(aMethod.getParameterTypes(),
			// aParameterTypes)) {
			if (anIndices != null) {

				if (aRetVal != null)
					return null;
				aRetVal = aMethod;
				methodKeysToArgIndices.put(aTag, anIndices);

				// return aMethod; // there can be other matching methods
			}
		}
		return aRetVal;
	}

	public static Method findMethod(Class aJavaClass, Method aProxy) {
		Method retVal = findMethod(aJavaClass, aProxy.getName(),
				aProxy.getParameterTypes());
		if (retVal == null) {
			String[] aTags = getTags(aProxy);
			if (aTags.length != 0) {
				retVal = findUniqueMethodByTag(aJavaClass, aProxy);
			}
			if (retVal == null && aTags.length == 1
					&& !aTags[0].equalsIgnoreCase(aProxy.getName())) {
				retVal = findMethod(aJavaClass, aTags[0],
						aProxy.getParameterTypes()); // just in case the name of
														// the method is the
														// unique tag
			}
		}
		// this is pretty expensive if methods o fall classes are being looked
		// up to find the class
		if (retVal == null) {
			// Method[] aMethods = aJavaClass.getMethods();
			Method[] aMethods = getNonObjectMethods(aJavaClass); // object
																	// methods
																	// would
																	// have been
																	// already
																	// found

			String aTag = toMethodKey(aJavaClass, aProxy.getName(),
					aProxy.getParameterTypes());

			retVal = selectMethodAndCacheIndices(aTag, Arrays.asList(aMethods),
					aProxy.getParameterTypes());
		}
		return retVal;
	}

	public static Integer[] getArgIndices(Class aJavaClass, Method aProxy) {
		return getMethodArgIndices(aJavaClass, aProxy.getName(),
				aProxy.getParameterTypes());
	}

	public static boolean matchesMethods(Class aJavaClass,
			Method[] aProxyMethods) {
		for (Method aProxyMethod : aProxyMethods) {
			if (aProxyMethod.getDeclaringClass() == Object.class)
				continue;
			if (findMethod(aJavaClass, aProxyMethod) == null)
				return false;
		}
		return true;
	}

	public static int numMethodMatches(Class aJavaClass, Method[] aProxyMethods) {
		int numMatches = 0;
		for (Method aProxyMethod : aProxyMethods) {
			// if (aProxyMethod.getDeclaringClass() == Object.class)
			// continue;
			if (findMethod(aJavaClass, aProxyMethod) != null)
				numMatches++;
		}
		return numMatches;
	}

	public static double degreeMethodMatches(Class aJavaClass,
			Method[] aProxyMethods) {
		double aLength = aProxyMethods.length;
		return aLength == 0 ? 1.0 : numMethodMatches(aJavaClass, aProxyMethods)
				/ aLength;
	}

	public static String toMethodTag(Class aClass, String[] aMethodTags,
			Class[] aParameterTypes) {
		return aClass.getName() + ":" + Arrays.toString(aMethodTags) + ":"
				+ Arrays.toString(aParameterTypes);
	}

	public static String toMethodKey(Class aClass, String aMethodName,
			Class[] aParameterTypes) {
		String[] aMethodTags = new String[] { aMethodName };
		return toMethodTag(aClass, aMethodTags, aParameterTypes);
	}

	public static Integer[] getMethodArgIndices(Class aClass,
			String aMethodName, Class[] aParameterTypes) {

		String aKey = toMethodKey(aClass, aMethodName, aParameterTypes);
		return methodKeysToArgIndices.get(aKey);

	}

	// returns the only matching method
	public static Method findMethod(Class aJavaClass, String aName,
			Class[] aParameterTypes) {
		try {
			// String aTag = aName + ":" + Arrays.toString(aParameterTypes);
			String aTag = toMethodKey(aJavaClass, aName, aParameterTypes);
			Method aMethod = keyToMethod.get(aTag);
			if (aMethod == null) {

				List<Method> aMethods = findMethod(aJavaClass, aName);
				aMethod = selectMethodAndCacheIndices(aTag, aMethods,
						aParameterTypes);
				if (aMethod == null) {
					// should we now try again with null name and select all
					// methods
					// and use argument types instead
					aMethod = Object.class.getMethod("wait");
				}
				keyToMethod.put(aTag, aMethod);
			}
			if (aMethod.equals(Object.class.getMethod("wait"))) {
				return null;
			}
			return aMethod;
			// Method aRetVal = null;
			// for (Method aMethod: aMethods) {
			// if (Arrays.equals(aMethod.getParameterTypes(), aParameterTypes))
			// {
			// return aMethod; // there can be other matching methods
			// }
			// }
			// return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Method> findMethod(Class aJavaClass, String aName) {
		// return findMethod(aJavaClass, null, aName, toRegex(aName),
		// toRegex(aName)); //why was there no name,
		String[] aTags = aName == null ? emptyStringArray
				: new String[] { aName };
		return findMethod(aJavaClass, aName, aTags,
		// new String[] { aName },
				toRegex(aName), toRegex(aName)); // to be consistent with clases
													// should not have regexes

	}

	public static String[] emptyStringArray = {};

	public static List<Method> findMethod(Class aJavaClass, String aName,
			String[] tag) {

		return findMethod(aJavaClass, aName, tag, toRegex(aName),
				tag.length == 0 ? "" : toRegex(tag[0]));
	}

	public static List<Method> findMethod(Class aJavaClass, String aName,
			String aTag, String aNameMatch, String aTagMatch) {
		return findMethod(aJavaClass, aName, new String[] { aTag }, aNameMatch,
				aTagMatch);
	}

	public static Class[] toClasses(Object[] anObjects) {
		Class[] aClasses = new Class[anObjects.length];
		for (int i = 0; i < anObjects.length; i++) {
			aClasses[i] = anObjects[i].getClass();
		}
		return aClasses;
	}

	public static void toPrimitiveTypes(Class[] aClasses) {
		for (int i = 0; i < aClasses.length; i++) {
			Class aMaybePrimitive = classToType.get(aClasses[i]);
			if (aMaybePrimitive != null) {
				aClasses[i] = aMaybePrimitive;
			}
		}
	}
	public static Object createInstance(Class aProxyClass, String[] aTags) {
		return createInstance(aProxyClass, emptyClassArray, emptyObjectArray, aTags);
	}

	public static Object createInstance(Class aProxyClass) {
		return createInstance(aProxyClass, emptyClassArray, new Object[] {});
	}

	public static Object createOrGetLastInstance(Class aProxyClass,
			Object[] anArgs) {
		Object result = classToProxy.get(aProxyClass);
		return result != null ? result : createInstance(aProxyClass, anArgs);
	}

	public static Object createInstance(Class aProxyClass, Object[] anArgs) {
		Class[] aParameterTypes = toClasses(anArgs);
		toPrimitiveTypes(aParameterTypes);
		return createInstance(aProxyClass, aParameterTypes, anArgs);
	}
	
	public static Object createInstanceOfPredefinedSupertype(Class aSuperType) {
		return createInstanceFromPredefinedSupertype(aSuperType, emptyObjectArray );
	}

	public static Object createInstanceFromPredefinedSupertype(Class aSuperType, Object[] anArgs) {
		Class[] aParameterTypes = toClasses(anArgs);
		toPrimitiveTypes(aParameterTypes);
		Project aProject = CurrentProjectHolder.getCurrentProject();
		Class anActualClass = BasicProjectIntrospection.
				findClassByExistingSupertype(aProject, aSuperType);
		if (anActualClass == null) {
			return null;
		}
		return createInstance(aSuperType, anActualClass, aParameterTypes, anArgs);
	}
	
	public static Object createInstanceAndProxy(Class anActualClass, Class aSuperType) {
	  if (anActualClass == null) {
	    return null;
	  }
	  return createInstance(aSuperType, anActualClass, emptyClassArray, emptyObjectArray);
	}

	public static Class[] getInterfaces(Class aClassOrInterface) {
		Class[] anInterfaces = null;
		if (aClassOrInterface.isInterface())
			anInterfaces = new Class[] { aClassOrInterface };
		else
			anInterfaces = aClassOrInterface.getInterfaces();
		return anInterfaces;
	}
	
	public static void removeProxy(Class<?> clazz, Object object) {
		if (object instanceof Proxy) {
			Object realObject = getRealObject(object);
			if (proxyToObject.containsKey(object)) {
				proxyToObject.remove(object);
			}
			if (objectToProxy.containsKey(realObject)) {
				objectToProxy.remove(realObject);
			}
			if (classToProxy.containsKey(clazz)) {
				Object proxy = classToProxy.get(clazz);
				if (classToProxy.get(clazz) == object) {
					classToProxy.remove(clazz);
				}
			}
		} else {
			if (objectToProxy.containsKey(object)) {
				Object realProxy = objectToProxy.get(object);
				objectToProxy.remove(object);
				if (proxyToObject.containsKey(realProxy)) {
					proxyToObject.remove(realProxy);
				}
				if (classToProxy.get(object.getClass()) == realProxy) {
					classToProxy.remove(object.getClass());
				}
			}
			
		}
	}
	
	public static Object createTimingOutProxy(Class aProxyClass, Object anActualObject) {
		Object aCachedValue = objectToProxy.get(anActualObject);
		if (aCachedValue != null) {
			return aCachedValue;
		}
		InvocationHandler aHandler = new AGradedClassProxyInvocationHandler(
				anActualObject);
		Class[] anInterfaces = null;
		// if (aProxyClass.isInterface())
		// anInterfaces = new Class[] {aProxyClass};
		// else
		// anInterfaces = aProxyClass.getInterfaces();
		// return Proxy.newProxyInstance(aProxyClass.getClassLoader(),
		// aProxyClass.getInterfaces(), aHandler);
		Object aProxy = Proxy.newProxyInstance(aProxyClass.getClassLoader(),
				getInterfaces(aProxyClass), aHandler);
		proxyToObject.put(aProxy, anActualObject);
		objectToProxy.put(anActualObject, aProxy);
		classToProxy.put(aProxyClass, aProxy);
		return aProxy;
	}

	public static Object createProxy(Class aProxyClass, Object anActualObject) {
		// redundant
		if (anActualObject instanceof Proxy && !isReverseProxy(anActualObject)) {
			return anActualObject;
		}
		if (aProxyClass.isInstance(anActualObject)) { // not a proxy class
			return anActualObject;
		}
		
		return createTimingOutProxy(aProxyClass, anActualObject);

//		InvocationHandler aHandler = new AGradedClassProxyInvocationHandler(
//				anActualObject);
//		Class[] anInterfaces = null;
//		
//		Object aProxy = Proxy.newProxyInstance(aProxyClass.getClassLoader(),
//				getInterfaces(aProxyClass), aHandler);
//		proxyToObject.put(aProxy, anActualObject);
//		objectToProxy.put(anActualObject, aProxy);
//		classToProxy.put(aProxyClass, aProxy);
//		return aProxy;
	}

	public static Object createReverseProxy(Class aReverseClass,
			Class aProxyClass, Object anActualObject) {
		Object aRetVal = createReverseProxy(aProxyClass, anActualObject);

		reverseProxyClassToClass.put(aReverseClass, aProxyClass);
		return aRetVal;

	}

	public static Object createReverseProxy(Class aProxyClass,
			Object anActualObject) {
		// redundant
		if (anActualObject instanceof Proxy) {
			return anActualObject;
		}
		if (aProxyClass.isInstance(anActualObject)) { // not a proxy class
			return null;
		}

		InvocationHandler aHandler = new AGradedClassProxyInvocationHandler(
				anActualObject);
		Class[] anInterfaces = null;
		// if (aProxyClass.isInterface())
		// anInterfaces = new Class[] {aProxyClass};
		// else
		// anInterfaces = aProxyClass.getInterfaces();
		// return Proxy.newProxyInstance(aProxyClass.getClassLoader(),
		// aProxyClass.getInterfaces(), aHandler);
		Object aProxy = Proxy.newProxyInstance(aProxyClass.getClassLoader(),
				getInterfaces(aProxyClass), aHandler);
		reverseProxyToObject.put(aProxy, anActualObject);
		reverseObjectToProxy.put(anActualObject, aProxy);
		reverseClassToProxy.put(aProxyClass, aProxy);
		return aProxy;
	}

	public static boolean isReverseProxy(Object anObject) {
		return reverseProxyToObject.get(anObject) != null;

	}

	public static Set<GradableJUnitSuite> findTopLevelGradabableTrees() {
		if (topLevelSuites == null) {
			Set<Class> aClasses = getAllClasses();
			topLevelSuites = BasicJUnitUtils.findGradableTrees(aClasses);

		}
		return topLevelSuites;

	}
	
	public static Object createInstance(Class aProxyClass,
			Class[] aConctructArgsTypes, Object[] anArgs, String[] aClassTags) {
		Class anActualClass = BasicProjectIntrospection.findClassByTagsCaching(
				CurrentProjectHolder.getOrCreateCurrentProject(),
				aClassTags);
		Constructor aConstructor;
		if (anActualClass == null) {
			Assert.fail("Did not find class matching:" + Arrays.toString(aClassTags));
		}
//		if (aProxyClass.isAssignableFrom(anActualClass)) {
//			Assert.fail("Class " + anActualClass + " not subytpe of " + aProxyClass);
//		}
		try {
			aConstructor = anActualClass
					.getConstructor(aConctructArgsTypes);
			Object anActualObject = BasicProjectExecution.timedInvoke(
					aConstructor, anArgs);
			return createProxy(aProxyClass, anActualObject);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Object anActualObject = aConstructor.newInstance(anArgs);
		
		return null;
	}

	public static Object createInstance(Class aProxyClass,
			Class[] aConctructArgsTypes, Object[] anArgs) {
		Class anActualClass = null;
		try {
			if (!aProxyClass.isInterface()) {
				System.out
						.println("cannot create proxy for non interface class");
				return null;
			}
			anActualClass = BasicProjectIntrospection.findClass(
					CurrentProjectHolder.getOrCreateCurrentProject(),
					aProxyClass);
			if (anActualClass == null) {
				System.out.println("Could not find unique class with tags:"
						+ Arrays.toString(getTags(aProxyClass)));
				return null;
			}
			
			return createInstance(aProxyClass, anActualClass, aConctructArgsTypes, anArgs);

//			Constructor aConstructor = anActualClass
//					.getConstructor(aConctructArgsTypes);
//			
//			Object anActualObject = BasicProjectExecution.timedInvoke(
//					aConstructor, anArgs);
//			return createProxy(aProxyClass, anActualObject);
			
			
			
			
//		} catch (NoSuchMethodException e) {
//			Tracer.info(
//					BasicProjectIntrospection.class,
//					"Cound not find " + "in " + anActualClass
//							+ " a public constructor with args:"
//							+ Arrays.toString(aConctructArgsTypes));
//			System.out.println(e);
//			return null;

		} catch (Exception e) {
			Tracer.info(BasicProjectIntrospection.class,
					"Class instantiation failed:" + e);
			e.printStackTrace();
			return null;
		}

	}
	public static Object createInstance(Class aReturnValueType, Class anActualClass,
			Class[] aConctructArgsTypes, Object[] anArgs) {
		try {
					Constructor aConstructor = anActualClass
					.getConstructor(aConctructArgsTypes);
			// Object anActualObject = aConstructor.newInstance(anArgs);
			Object anActualObject = BasicProjectExecution.timedInvoke(
					aConstructor, anArgs);
			return createProxy(aReturnValueType, anActualObject);
			
		} catch (NoSuchMethodException e) {
			Tracer.info(
					BasicProjectIntrospection.class,
					"Cound not find in " + anActualClass
							+ " a public constructor with args:"
							+ Arrays.toString(aConctructArgsTypes));
			System.out.println(e);
			return null;

		} catch (Exception e) {
			Tracer.info(BasicProjectIntrospection.class,
					"Class instantiation failed:" + e);
			e.printStackTrace();
			return null;
		}

	}

	public static boolean hasProxyElement(Object aProxy) { // expecting aProxy
															// to be an array of
															// proxies.
//      This check isn't needed since it is implied by the next
//		if (!aProxy.getClass().isArray())
//			return false;
		if (!(aProxy instanceof Object[])) {
			return false;
		}
		Object[] aProxyArray = (Object[]) aProxy;
		return aProxyArray.length > 0 && isProxy(aProxyArray[0]);
	}

	public static boolean isProxy(Object aProxy) {
		return aProxy instanceof Proxy || hasProxyElement(aProxy);
	}

	public static Object getRealObject(Object aProxy) {
		if (isProxy(aProxy))
			return proxyToObject.get(aProxy);
		return aProxy;
	}

	public static Object getReverseRealObject(Object aProxy) {
		return reverseProxyToObject.get(aProxy);
	}

	public static Object getProxyObject(Object aRealObject) {
		return objectToProxy.get(aRealObject);
	}

	public static void associate(Object aRealObject, Object aProxyObject) {
		proxyToObject.put(aProxyObject, aRealObject);
		objectToProxy.put(aRealObject, aProxyObject);
	}

	public static List<Method> findMethod(Class aJavaClass, String aName,
			String[] aTag, String aNameMatch, String aTagMatch) {
		List<Method> result = new ArrayList();
		if (aName != null)
			result = findMethodByName(aJavaClass, aName);
		if (result != null && !result.isEmpty())
			return result;
		if (aTag != null)
			result = findMethodsByTag(aJavaClass, aTag);
		if (result != null && !result.isEmpty())
			return result;
		if (aNameMatch != null) {
			result = findMethodByNameMatch(aJavaClass, aNameMatch);
		}
		if (!result.isEmpty())
			return result;
		if (aTagMatch != null) {
			result = findMethodByTagMatch(aJavaClass, aTagMatch);
		}
		return result;
	}

	public static Set<Class> getAllInterfaces(Class aClass) {
		Set<Class> retVal = new HashSet();
		while (aClass != null) {
			retVal.addAll(Arrays.asList(aClass.getInterfaces()));
			aClass = aClass.getSuperclass();
		}
		return retVal;

	}

	public static Class getCommonInterface(Project project,
			String[][] aClassDescriptions) {
		Set<Class> interfaces = new HashSet<>(5);
		for (String[] classDescriptions : aClassDescriptions) {
			Class aClass = BasicProjectIntrospection.findClass(project,
					classDescriptions[0], classDescriptions[1],
					classDescriptions[2], classDescriptions[3]);
			if (aClass == null)
				continue;

			Set<Class> tokenInterfaces = BasicProjectIntrospection
					.getAllInterfaces(aClass);
			if (tokenInterfaces.size() == 0) {
				return null;
			} else {
				if (interfaces.isEmpty()) {
					interfaces.addAll(tokenInterfaces);
				} else {
					interfaces.retainAll(tokenInterfaces);
				}
			}
		}
		if (interfaces.size() != 1) {
			return null;
		} else {
			return interfaces.iterator().next();
		}
	}

	public static void addPredefinedTypes(String... aPackages) {
		predefinedPackages.addAll(Arrays.asList(aPackages));
	}

	public static boolean isPredefinedType(Class aType) {
		return (classToType.keySet().contains(aType)
				|| classToType.values().contains(aType) || ((aType.getPackage() != null) && (aType
				.getPackage().getName().startsWith("java") || predefinedPackages
				.contains(aType.getPackage().getName()))));

	}

	static {
		classToType.put(Integer.class, Integer.TYPE);
		classToType.put(Long.class, Long.TYPE);
		classToType.put(Short.class, Short.TYPE);
		classToType.put(Double.class, Double.TYPE);
		classToType.put(Boolean.class, Boolean.TYPE);
		classToType.put(Character.class, Character.TYPE);
		// more later

	}

	// public static String setPropertyInteractive (Class aClass, Method
	// aWriteMethod, Object aValue) {
	//
	//
	// }
	// public static void main (String[] args) {
	// String[] a1 = {"Hello", new String ("Goodbye")};
	// String[] a2 = {"Goodbye", "Hello"};
	// Set<String> a1Set = new HashSet( Arrays.asList(a1));
	// Set<String> a2Set = new HashSet( Arrays.asList(a2));
	// System.out.println (a1Set.equals(a2Set));
	// }
	public static Field findFieldOfType(Class aTargetClass, Class aDesiredType) {
		Field[] aFields = aTargetClass.getDeclaredFields();
		for (Field aField : aFields) {
			Class anActualFieldType = aField.getType();
			if (aDesiredType.isAssignableFrom(anActualFieldType)) {
				aField.setAccessible(true);
				return aField;
			}
		}
		return null;
	}

	public static Object getFieldValueOfType(Object anObject, Class aDesiredType) {
		Object anActualObject = BasicProjectExecution.maybeGetActual(anObject);
		Class anActualType = BasicProjectIntrospection.findClass(aDesiredType);

		Field aField = findFieldOfType(anActualObject.getClass(), anActualType);
		try {
			return aField.get(anActualObject);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static Set<String> findMissingClasses (Project aProject, Class aReferenceClass, Class anActualClass) {
    Map<String, Class> aReferenceMapping = getPropertyClassMapping(aProject, aReferenceClass);
    Map<String, Class> anActualMapping = getPropertyClassMapping(aProject, anActualClass);
    return diff(aReferenceMapping, anActualMapping );
  }
  public static Set<String> diff (
      Map<String, Class> aReferenceMapping, 
      Map<String, Class> anActualMapping) {
    Set<String> retVal = aReferenceMapping.keySet();
    retVal.removeAll(anActualMapping.keySet());
   return  retVal;
    
  }
  public static Collection findDuplicateValues (Map aMap) {
    List aValues =  new ArrayList (aMap.values());
    Set aUniqueValues = new HashSet<>(aValues);
    for (Object aValue:aUniqueValues) {
      aValues.remove(aValue);
    }
    return aValues;
  }
  
  public static Map<String, Class> getPropertyClassMapping(Project aProject, Class aConfigurationClass) {
    Map<String, Class> retVal = new HashMap<String, Class>();
    try {
    BeanInfo aBeanInfo = Introspector.getBeanInfo(aConfigurationClass);
    Object aConfiguration = aConfigurationClass.newInstance();
    PropertyDescriptor[] aPropertyDescriptors = aBeanInfo.getPropertyDescriptors();
    for (PropertyDescriptor aPropertyDescriptor : aPropertyDescriptors) {
      Method aReadMethod = aPropertyDescriptor.getReadMethod();
        if (aReadMethod == null) {
          continue;
        }
        Object aPropertyObject = aReadMethod.invoke(aConfiguration);
        if (aPropertyObject == null) {
          continue;
        }
        if (!(aPropertyObject instanceof Class)) 
          continue;
        Class aPropertyClass = (Class) aPropertyObject;
        if (aProject != null) {
          Class aProjectClass = findClassOrInterfaceByName(aProject, aPropertyClass.getName());
          if (aProjectClass == null) {
            System.err.println("Configured class " + aProjectClass + " is not a project class");
            continue;
          }
        }
        
        retVal.put(aPropertyDescriptor.getName(), aPropertyClass);

    }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return retVal;
    
  }
}
