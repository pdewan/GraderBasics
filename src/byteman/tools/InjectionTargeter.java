package byteman.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import grader.byteman.injector.target.custom.ClassInjectionData;
import grader.byteman.injector.target.custom.EnterExitInjectionSite;

public class InjectionTargeter {
	private Map<ClassInjectionData,ClassInjectionData> injectionData;
	private File injections;
	private Map<String,Object> nameMap;
	
	private static Class<? extends EnterExitInjectionSite> DEFAULT_INJECTION = null;
	
	public InjectionTargeter(Map<String,Object> nameMap) throws IOException {
		setNameMap(nameMap);
		injections = new File("./injectionTarget.txt");
		injectionData = new HashMap<>();
	}
	
	public InjectionTargeter() throws IOException {
		this(new HashMap<>());
	}
	
	/**
	 * Adds all declared methods within the class
	 * @param clazz
	 * @return
	 */
	public boolean addClasses(Class<? extends EnterExitInjectionSite> injectedCode,Class<?> ... clazzez) {
		for(Class<?> clazz:clazzez) 
			if(!addMethods(injectedCode,clazz,clazz.getDeclaredMethods())) return false;
		return true;
	}

	public boolean addFromRegistry(String aConfigurationFileFullName, Map<String, Class<? extends EnterExitInjectionSite>> injectedCode) {
		Map<String, List<String>> aClassToConfiguredTags = processConfigurationFileName(aConfigurationFileFullName);
		boolean failed = false;
		for(String className:aClassToConfiguredTags.keySet()) {
			try {
				if(injectedCode.containsKey(className)) {
					addClasses(injectedCode.get(className),Class.forName(className));
				}else {
					System.err.println("No unique code injection found for "+className+" using default");
					addClasses(DEFAULT_INJECTION,Class.forName(className));
				}
			}catch(ClassNotFoundException e) {
				failed=true;
				System.err.println("Class: "+className+" not found");
			}
		}
		return failed;
	}
	
	public boolean addFromRegistry(String aConfigurationFileFullName, Class<? extends EnterExitInjectionSite> injectedCode) {
		Map<String, List<String>> aClassToConfiguredTags = processConfigurationFileName(aConfigurationFileFullName);
		boolean failed = false;
		for(String className:aClassToConfiguredTags.keySet()) {
			try {
				Class<?> clazz = Class.forName(className);
				if(clazz.isInterface()) continue;
				addClasses(injectedCode,clazz);
			}catch(ClassNotFoundException e) {
				failed=true;
				System.err.println("Class: "+className+" not found");
			}
		}
		return failed;
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
		        String aClass = aLineTokens[0];
		        String aTag = aLineTokens[1];
		        List<String> aTags = aClassToConfiguredTags.get(aClass);
		        if (aTags == null) {
		        	aTags = new ArrayList<>();
		        	aClassToConfiguredTags.put(aClass, aTags);
		        }
	        	if (!aTags.contains(aTag)) {
	        		aTags.add(aTag);
	        	}
	    	}
	    	aScanner.close();
	    } catch (FileNotFoundException e) {
	    	return null;
	    }
	    return aClassToConfiguredTags;
	 }
	
	/**
	 * Adds methods to be injected to
	 * @param methods
	 * @return
	 * @throws IOException 
	 */
	public boolean addMethods(Class<? extends EnterExitInjectionSite> injectedCode, Method ... methods) {
		for(Method method:methods) 
			if(!addMethods(injectedCode,method.getDeclaringClass(),method)) return false;
		return true;
	}
	
	/**
	 * Adds methods to be injected to
	 * @param methods
	 * @return
	 * @throws IOException 
	 */
	public boolean addMethods(Class<? extends EnterExitInjectionSite> injectedCode, Class<?> clazz, Method ... methodNames) {
		ClassInjectionData classData;
		String className = clazz.getName();
		if(nameMap.containsKey(className)) {
			classData = new ClassInjectionData(className,injectedCode.getName(),nameMap.get(className).toString(),true,methodNames);
		}else {
			classData = new ClassInjectionData(className,injectedCode.getName(),true,methodNames);
		}
		
		if(injectionData.containsKey(classData)) {
			injectionData.get(classData).addMethod(classData.getTargetedMethods());
		}else {
			injectionData.put(classData,classData);
		}
		return true;
	}
	
	/**
	 * Adds methods to be injected to
	 * @param methods
	 * @return
	 * @throws IOException 
	 */
	public boolean addMethods(Class<? extends EnterExitInjectionSite> injectedCode, Class<?> clazz, String ... methodNames) {
		return addMethods(injectedCode,clazz,findMethods(clazz.getMethods(),methodNames));
	}
	
	/**
	 * Adds all methods declared in a class except for the ones listed
	 * @param methods
	 * @return
	 */
	public boolean excludeMethods(Class<? extends EnterExitInjectionSite> injectedCode, Class<?> clazz, String ... methods) {
		List<Method> included = new ArrayList<>();
		for(Method method:clazz.getDeclaredMethods())
			if(!contains(method.getName(), methods))
				included.add(method);
		return addMethods(injectedCode,included.toArray(new Method[included.size()]));		
	}
	
	private Method[] findMethods(Method [] methods, String [] names) {
		List<Method> retval = new ArrayList<>();
		List<String> nameArr = new ArrayList<>();
		nameArr.addAll(Arrays.asList(names));
		for(Method method:methods) 
			for(int i=0;i<nameArr.size();i++) 
				if(method.getName().equals(nameArr.get(i))) {
					retval.add(method);
					nameArr.remove(i);
					break;
				}
		return retval.toArray(new Method[retval.size()]);
	}
	
	/**
	 * Ends and closes the file writing, must be done before process builder
	 * @return
	 * @throws IOException
	 */
	public boolean write() throws IOException {
		injections.createNewFile();
		FileWriter write = new FileWriter(injections);
		for(ClassInjectionData classes:injectionData.keySet()) {
			write.append(classes.getPrintValue());
			write.append('\n');
		}
		
		write.close();
		return true;
	}
	
	/**
	 * Deletes the file created
	 * @return
	 */
	public boolean delete() {
		return injections.delete();
	}
	
	private boolean contains(Object val, Object [] arr) {
		for(Object arrVal:arr)
			if(val.equals(arrVal)) return true;
		return false;
	}

	public Map<String,Object> getNameMap() {
		return nameMap;
	}

	public void setNameMap(Map<String,Object> nameMap) {
		this.nameMap = nameMap;
	}
}