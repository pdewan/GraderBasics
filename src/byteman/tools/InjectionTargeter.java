package byteman.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.jdt.internal.corext.refactoring.changes.CreateCompilationUnitChange;

import grader.basics.project.BasicProjectIntrospection;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.Project;
import grader.byteman.injector.target.custom.ClassInjectionData;
import grader.byteman.injector.target.custom.EnterExitInjectionSite;

public class InjectionTargeter {
	private Map<ClassInjectionData, ClassInjectionData> injectionData;
	private File injections;
	private Map<String, Object> nameMap;

	private static Class<? extends EnterExitInjectionSite> DEFAULT_INJECTION = null;

	private Map<String, List<String>> classToConfiguredTags;
	private Map<Set<String>, String> configuredTagsToClass;

	public InjectionTargeter(Map<String, Object> nameMap) throws IOException {
		setNameMap(nameMap);
		injections = new File("./injectionTarget.txt");
		injectionData = new HashMap<>();
	}

	public InjectionTargeter() throws IOException {
		this(new HashMap<>());
	}

	/**
	 * Adds all declared methods within the class
	 * 
	 * @param clazz
	 * @return
	 */
//	public boolean addClasses(Class<? extends EnterExitInjectionSite> injectedCode, Class<?>... clazzez) {
//		for (Class<?> clazz : clazzez)
//			if (!addMethods(injectedCode, clazz, clazz.getDeclaredMethods()))
////			if (!addMethods(injectedCode, clazz, clazz.getMethods()))
//
//				return false;
//		return true;
//	}

//	public void addDerivedTypes(Set<Class> aDerivedClasses, Class aClass) {
//		if (aClass == null) {
//			return;
//		}
//		aDerivedClasses.add(aClass);
//
//		addDerivedTypes(aDerivedClasses, aClass.getSuperclass());
//	}
//
//	public void addDerivedTypes(Set<Class> aDerivedClasses, Class... aClasses) {
//		for (Class aClass : aClasses) {
//			addDerivedTypes(aDerivedClasses, aClass);
//		}
//	}

	public boolean addClasses(Class<? extends EnterExitInjectionSite> injectedCode, Class<?>... clazzez) {
		Set<Class> aDerivedClasses = new HashSet();
//		addDerivedTypes(aDerivedClasses, clazzez);
		for (Class<?> clazz : clazzez) {
			if (!addClass(injectedCode, clazz)) {
				return false;
			}
			;
		}
		return true;
	}

	protected Set<Class> classesAdded = new HashSet();

	public boolean addClass(Class<? extends EnterExitInjectionSite> injectedCode, Class<?> aClass) {

		if (classesAdded.contains(aClass) 
//				|| Modifier.isAbstract( aClass.getModifiers() )
				) 
		{
			return true;
		}
		System.out.println("Adding class:" + aClass);
		if (!addMethods(injectedCode, aClass, aClass.getDeclaredMethods())) {

			return false;
		}
		classesAdded.add(aClass);

//		Class aSuperClass = aClass.getSuperclass();
//		if (aSuperClass != null) {
//			addClass(injectedCode, aSuperClass);
//		}

		return true;
	}

	public boolean addFromRegistry(String aConfigurationFileFullName,
			Map<String, Class<? extends EnterExitInjectionSite>> injectedCode) {
		Map<String, List<String>> aClassToConfiguredTags = processConfigurationFileName(aConfigurationFileFullName);
		boolean failed = false;
		for (String className : aClassToConfiguredTags.keySet()) {
			try {
				if (injectedCode.containsKey(className)) {
					addClasses(injectedCode.get(className), Class.forName(className));
				} else {
					System.err.println("No unique code injection found for " + className + " using default");
					addClasses(DEFAULT_INJECTION, Class.forName(className));
				}
			} catch (ClassNotFoundException e) {
				failed = true;
				System.err.println("Class: " + className + " not found");
			}
		}
		return failed;
	}

	public boolean sameTags(String[] aTags1, String[] aTags2) {
		Set<String> aTags1Set = new HashSet(Arrays.asList(aTags1));
		Set<String> aTags2Set = new HashSet(Arrays.asList(aTags2));
		return aTags1Set.equals(aTags2Set);
	}

	protected Map<Set<String>, String> createConfiguredTagsToClass(Map<String, List<String>> aClassToConfiguredTags) {
		Map<Set<String>, String> retVal = new HashMap();
		for (String aClassName : aClassToConfiguredTags.keySet()) {
			List<String> aTags = aClassToConfiguredTags.get(aClassName);
			retVal.put(new HashSet(aTags), aClassName);
		}
		return retVal;
	}

	public String getClassName(Set<String> aTags) {
		return configuredTagsToClass.get(aTags);
	}

	public boolean addFromClassTagMapping(Map<String, List<String>> aClassToConfiguredTags,
			Class<? extends EnterExitInjectionSite> injectedCode) {
		classToConfiguredTags = aClassToConfiguredTags;
		configuredTagsToClass = createConfiguredTagsToClass(aClassToConfiguredTags);
//		Map<String, List<String>> aClassToConfiguredTags = processConfigurationFileName(aConfigurationFileFullName);
		boolean failed = false;
		for (String className : aClassToConfiguredTags.keySet()) {
			try {
				Class<?> clazz = Class.forName(className);
				List<String> aTags = aClassToConfiguredTags.get(className);
				StringBuffer aTagsString = new StringBuffer();
				for (int index = 0; index < aTags.size(); index++) {
					if (index != 0) {
						aTagsString.append("+");
					}
					aTagsString.append("@" + aTags.get(index));
				}
				nameMap.put(className, aTagsString.toString());
				if (clazz.isInterface())
					continue;
				addClasses(injectedCode, clazz);
			} catch (ClassNotFoundException e) {
				failed = true;
				System.err.println("Class: " + className + " not found");
			}
		}
		return failed;
	}

	public boolean addFromCheckstyleConfiguration(File aConfigurationFile,
			Class<? extends EnterExitInjectionSite> injectedCode) {
		Map<String, List<String>> aClassToConfiguredTags = processCheckstyleConfiguration(aConfigurationFile);
		return addFromClassTagMapping(aClassToConfiguredTags, injectedCode);
	}

	public boolean addFromRegistry(String aConfigurationFileFullName,
			Class<? extends EnterExitInjectionSite> injectedCode) {
		Map<String, List<String>> aClassToConfiguredTags = processConfigurationFileName(aConfigurationFileFullName);
		return addFromClassTagMapping(aClassToConfiguredTags, injectedCode);
//		boolean failed = false;
//		for (String className : aClassToConfiguredTags.keySet()) {
//			try {
//				Class<?> clazz = Class.forName(className);
//				List<String> aTags = aClassToConfiguredTags.get(className);
//				StringBuffer aTagsString = new StringBuffer();
//				for (int index = 0; index < aTags.size(); index++) {
//					if (index != 0) {
//						aTagsString.append("+");
//					}
//					aTagsString.append("@" + aTags.get(index));
//				}
//				nameMap.put(className, aTagsString.toString());
//				if (clazz.isInterface())
//					continue;
//				addClasses(injectedCode, clazz);
//			} catch (ClassNotFoundException e) {
//				failed = true;
//				System.err.println("Class: " + className + " not found");
//			}
//		}
//		return failed;
	}

	private Map<String, List<String>> processConfigurationFileName(String aConfigurationFileFullName) {
		Map<String, List<String>> aClassToConfiguredTags = new HashMap<>();
		Scanner aScanner;
		try {
			aScanner = new Scanner(new File(aConfigurationFileFullName));

			while (aScanner.hasNext()) {
				String aLine = aScanner.nextLine();
				String[] aLineTokens = aLine.split(",");
				if (aLineTokens.length != 2) {
					aScanner.close();
					return null;
				}
				String aClassName = aLineTokens[0];
				try {
					Class aClass = Class.forName(aClassName);
					if (aClass.isInterface()) {
						continue;
					}

					String aTag = aLineTokens[1];
					List<String> aTags = aClassToConfiguredTags.get(aClassName);
					if (aTags == null) {
						aTags = new ArrayList<>();
						aClassToConfiguredTags.put(aClassName, aTags);
					}
					if (!aTags.contains(aTag)) {
						aTags.add(aTag);
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}
			aScanner.close();
		} catch (FileNotFoundException e) {
			return null;
		}
		return aClassToConfiguredTags;
	}

	private void moveToStartOfTagsList(Scanner aScanner) {
		// skip all lines util we reach the expected types line
		while (aScanner.hasNext()) {
			String aLine = aScanner.nextLine();
			if (aLine.contains("<property name=\"expectedTypes\" value=\"")) {
				break;
			}
		}
	}

	private String segmentToTag(String aSegment) {
		return aSegment.replace("@", "").replace(",", "");
//		int aStartOfTag = 0;
//		if (aSegment.charAt(0) == '@') {
//			aStartOfTag = 1;
//		}
//		int anEndOfTag = aSegment.length();
//		if (aSegment.charAt(aSegment.length() - 1) == ',') {
//			anEndOfTag = aSegment.length() - 1; 
//		}
//		return aSegment.substring(aStartOfTag, anEndOfTag);
	}

	private String[] lineToTags(String aLine) {
		String[] aTagsList = aLine.trim().split("\\+");
		String[] retVal = new String[aTagsList.length];
		for (int index = 0; index < retVal.length; index++) {
			retVal[index] = segmentToTag(aTagsList[index]);
		}
		return retVal;

	}

//	public static void testClassSearch() {
////		String[] aTags = BasicClassDescription.getTags(TaggedClass.class);
////		System.out.println("Tags" + Arrays.toString(aTags));
////		CurrentProjectHolder.setProject(".java");
//		Project aProject = CurrentProjectHolder.getCurrentProject();
//		Class aClass = BasicProjectIntrospection.findClassByTags("Tag1", "Tag2");		
//		System.out.println("Class by Tag" + aClass);		
////		aClass = BasicProjectIntrospection.findClassByExistingSupertype(aProject, TaggedInterface.class);				
//		System.out.println("Class by Super Type" + aClass);		
//		System.out.println(aProject);
//	}
	public static void testClassSearch() {
//		String[] aTags = BasicClassDescription.getTags(TaggedClass.class);
//		System.out.println("Tags" + Arrays.toString(aTags));
//		CurrentProjectHolder.setProject(".java");
		Project aProject = CurrentProjectHolder.getCurrentProject();
		Class aClass = BasicProjectIntrospection.findClassByTags("Tag1", "Tag2");
		System.out.println("Class by Tag" + aClass);
//		aClass = BasicProjectIntrospection.findClassByExistingSupertype(aProject, TaggedInterface.class);				
		System.out.println("Class by Super Type" + aClass);
		System.out.println(aProject);
	}
	/*
	 * <property name="expectedTypes" value="
	 * 
	 * 
	 * @DistributedTags.SERVER_REMOTE_INTERFACE+@DistributedTags.RMI,
	 * 
	 * @DistributedTags.CLIENT_OUT_COUPLER+@DistributedTags.RMI+@DistributedTags.
	 * GIPC,
	 * 
	 * @DistributedTags.SERVER_REMOTE_INTERFACE+@DistributedTags.GIPC,
	 * 
	 * @DistributedTags.SERVER_CONFIGURER+@DistributedTags.RMI+@DistributedTags.
	 * GIPC,
	 * 
	 * @DistributedTags.REGISTRY+@DistributedTags.RMI,
	 * 
	 * @DistributedTags.CLIENT_REMOTE_INTERFACE+@DistributedTags.RMI,
	 * 
	 * @DistributedTags.CLIENT_REMOTE_INTERFACE+@DistributedTags.GIPC,
	 * 
	 * @DistributedTags.CLIENT_CONFIGURER+@DistributedTags.RMI+@DistributedTags.
	 * GIPC,
	 * 
	 * @DistributedTags.SERVER+@DistributedTags.RMI+@DistributedTags.GIPC,
	 * 
	 * @DistributedTags.CLIENT+@DistributedTags.RMI+@DistributedTags.GIPC,
	 * 
	 * @DistributedTags.CLIENT_REMOTE_OBJECT+@DistributedTags.RMI+@DistributedTags.
	 * GIPC,
	 * 
	 * @DistributedTags.SERVER_REMOTE_OBJECT+@DistributedTags.RMI+@DistributedTags.
	 * GIPC,
	 * 
	 * 
	 * "/>
	 */

	private Map<String, List<String>> processCheckstyleConfiguration(File aConfigurationFile) {
		Map<String, List<String>> aClassToConfiguredTags = new HashMap<>();
		Scanner aScanner;
		try {
			aScanner = new Scanner(aConfigurationFile);
			// skip all lines until we reach the expected types line
			moveToStartOfTagsList(aScanner);
			List<String[]> aTagsList = new ArrayList();
			while (aScanner.hasNext()) {

				String aLine = aScanner.nextLine();
				if (aLine.contains("\"/>")) {
					break;
				}
				// skip any spurious blank line
				if (!aLine.contains("@")) {
					continue;
				}
				String[] aTags = lineToTags(aLine);
				aTagsList.add(aTags);

			}
//			Map<String, List<String>> aClassToConfiguredTags = new HashMap<>();

			for (String[] aTags : aTagsList) {
				String[] anOriginalTags = Arrays.copyOf(aTags, aTags.length);
				Class aClass = BasicProjectIntrospection.findClassByTags(aTags);
				if (aClass == null) {
					System.err.println("No class found for tags:" + Arrays.asList(aTags));
					continue;
				}
				aClassToConfiguredTags.put(aClass.getName(), Arrays.asList(anOriginalTags));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return aClassToConfiguredTags;
	}

	/**
	 * Adds methods to be injected to
	 * 
	 * @param methods
	 * @return
	 * @throws IOException
	 */
	public boolean addMethods(Class<? extends EnterExitInjectionSite> injectedCode, Method... methods) {
		for (Method method : methods)
			if (!addMethods(injectedCode, method.getDeclaringClass(), method))
				return false;
		return true;
	}

	/**
	 * Adds methods to be injected to
	 * 
	 * @param methods
	 * @return
	 * @throws IOException
	 */
	public boolean addMethods(Class<? extends EnterExitInjectionSite> injectedCode, Class<?> clazz,
			Method... methodNames) {
		ClassInjectionData classData;
		String className = clazz.getName();
		if (nameMap.containsKey(className)) {
			classData = new ClassInjectionData(className, injectedCode.getName(), nameMap.get(className).toString(),
					true, methodNames);
		} else {
			classData = new ClassInjectionData(className, injectedCode.getName(), true, methodNames);
		}

		if (injectionData.containsKey(classData)) {
			injectionData.get(classData).addMethod(classData.getTargetedMethods());
		} else {
			injectionData.put(classData, classData);
		}
		return true;
	}

	/**
	 * Adds methods to be injected to
	 * 
	 * @param methods
	 * @return
	 * @throws IOException
	 */
	public boolean addMethods(Class<? extends EnterExitInjectionSite> injectedCode, Class<?> clazz,
			String... methodNames) {
		return addMethods(injectedCode, clazz, findMethods(clazz.getMethods(), methodNames));
	}

	/**
	 * Adds all methods declared in a class except for the ones listed
	 * 
	 * @param methods
	 * @return
	 */
	public boolean excludeMethods(Class<? extends EnterExitInjectionSite> injectedCode, Class<?> clazz,
			String... methods) {
		List<Method> included = new ArrayList<>();
		for (Method method : clazz.getDeclaredMethods())
			if (!contains(method.getName(), methods))
				included.add(method);
		return addMethods(injectedCode, included.toArray(new Method[included.size()]));
	}

	private Method[] findMethods(Method[] methods, String[] names) {
		List<Method> retval = new ArrayList<>();
		List<String> nameArr = new ArrayList<>();
		nameArr.addAll(Arrays.asList(names));
		for (Method method : methods)
			for (int i = 0; i < nameArr.size(); i++)
				if (method.getName().equals(nameArr.get(i))) {
					retval.add(method);
					nameArr.remove(i);
					break;
				}
		return retval.toArray(new Method[retval.size()]);
	}

	/**
	 * Ends and closes the file writing, must be done before process builder
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean write() throws IOException {
		injections.createNewFile();
		FileWriter write = new FileWriter(injections);
		for (ClassInjectionData classes : injectionData.keySet()) {
			write.append(classes.getPrintValue());
			write.append('\n');
		}

		write.close();
		return true;
	}

	/**
	 * Deletes the file created
	 * 
	 * @return
	 */
	public boolean delete() {
		return injections.delete();
	}

	private boolean contains(Object val, Object[] arr) {
		for (Object arrVal : arr)
			if (val.equals(arrVal))
				return true;
		return false;
	}

	public Map<String, Object> getNameMap() {
		return nameMap;
	}

	public void setNameMap(Map<String, Object> nameMap) {
		this.nameMap = nameMap;
	}
}
