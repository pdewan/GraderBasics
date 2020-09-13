package gradingTools.shared.testcases.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import bus.uigen.introspect.IntrospectUtility;
import grader.basics.project.Project;

public class ConfigurationWriter {
	public static void writeConfiguration(Project aProject, Class aConfigurationClass, Object aConfiguration) {
		File aProjectDirectory = aProject.getProjectFolder();
		 String aConfigurationFileName;
		try {
			aConfigurationFileName = aProjectDirectory.getCanonicalPath() + "/" + 
			aConfigurationClass.getSimpleName() + ".csv";
			writeConfiguration(aConfigurationFileName, aConfiguration);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static final String CLASS_REGISTRY_FILE = "ClassRegistry.csv";
	public static void writeConfiguration(Project aProject,  Object aConfiguration) {
		File aProjectDirectory = aProject.getProjectFolder();
		 String aConfigurationFileName;
		try {
			aConfigurationFileName = aProjectDirectory.getCanonicalPath() + "/" + 
					CLASS_REGISTRY_FILE;
			writeConfiguration(aConfigurationFileName, aConfiguration);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void writeConfiguration(String aConfigurationFileName, Object aConfiguration) {
		try {
		
			File aFile = new File(aConfigurationFileName);
			if (!aFile.exists()) {
				aFile.createNewFile();
			}
			PrintWriter aPrintWriter = new PrintWriter (aFile);
			Class aConfigurationClass = aConfiguration.getClass();
			
			BeanInfo aBeanInfo = Introspector.getBeanInfo(aConfigurationClass);
			PropertyDescriptor[] aPropertyDescriptors = aBeanInfo.getPropertyDescriptors();
			for (PropertyDescriptor aPropertyDescriptor:aPropertyDescriptors) {
				processProperty(aPrintWriter, aPropertyDescriptor, aConfiguration);
			}
			aPrintWriter.close();
		} catch (Exception e) {
			return;
		}
	}
	
	public static void processProperty (PrintWriter aPrintWriter, PropertyDescriptor aPropertyDescriptor, Object aConfiguration) {
		Object aType = aPropertyDescriptor.getPropertyType();
		if (!(aType == Class.class)) return;
		String aPropertyName = aPropertyDescriptor.getName();		
		if ("class".equals(aPropertyName)) return;
		String aClassName = Character.toUpperCase(aPropertyName.charAt(0))+  aPropertyName.substring(1);
		Method aReadMethod = aPropertyDescriptor.getReadMethod();
		try {
			Class aPropertyClass = (Class) aReadMethod.invoke(aConfiguration);
			if (aPropertyClass == null) {
				return;
			}
			Class[] anInterfaces = aPropertyClass.getInterfaces();
			String aPropertyClassName = aPropertyClass.getName();
//			aPrintWriter.println(aClassName + "," + aPropertyClassName);
			aPrintWriter.println(aPropertyClassName + "," + aClassName);
			for (Class anInterface:anInterfaces) {
				aPrintWriter.println(anInterface.getSimpleName() + "," + aClassName);
			}

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return;
		}
	}

}