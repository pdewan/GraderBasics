package grader.basics;

//import grader.checkStyle.CheckStyleInvoker;
//import grader.checkStyle.JavaCheckStyleInvokerFactory;
//import grader.compilation.ClassFilesCompiler;
//import grader.compilation.JavaClassFilesCompilerSelector;
//import grader.compilation.c.CFilesCompilerSelector;
//import grader.config.ConfigurationManager;
//import grader.config.StaticConfigurationUtils;
//import grader.execution.ExecutableFinderSelector;
import grader.basics.execution.MainClassFinder;
//import grader.permissions.Permissible;
//import grader.permissions.PermissionsGenerator;
//import grader.permissions.java.DefaultJavaPermissible;
//import grader.permissions.java.JavaPermissionsGenerator;

import java.util.HashMap;
import java.util.Map;

public class BasicLanguageDependencyManager {
	static  String sourceFileSuffix = ".java";
	 static Map<String, String> languageToSourceFileSuffix = new HashMap<>();
	 protected static Map<String, String> languageToBinaryFileSuffix = new HashMap<>();
	 protected static Map<String, MainClassFinder> languageToMainClassFinder = new HashMap();
//	 static Map<String, ClassFilesCompiler> languageToCompiler = new HashMap();
//	 static Map<String, CheckStyleInvoker> languageToCheckStyleInvoker = new HashMap();
//	 static Map<String, Permissible> languageToDefaultPermissible = new HashMap<>();
//	 static Map<String, PermissionsGenerator> languageToPermissionGenerator = new HashMap<>();;

	 static String language;
		
		public static String JAVA_LANGUAGE = "Java";
		public static String C_LANGUAGE = "C";
		public static String PYTHON_LANGUAGE = "Python";

	
	public static  String binaryFileSuffix = ".class";


	public static void setSourceFileSuffix(String sourceFileSuffix) {
		BasicLanguageDependencyManager.sourceFileSuffix = sourceFileSuffix;
	}


	public static void setLanguageToSourceFileSuffix(
			Map<String, String> languageToSourceFileSuffix) {
		BasicLanguageDependencyManager.languageToSourceFileSuffix = languageToSourceFileSuffix;
	}


	



	public static void setLanguage(String language) {
		BasicLanguageDependencyManager.language = language;
	sourceFileSuffix = languageToSourceFileSuffix.get(getLanguage());
	binaryFileSuffix = languageToBinaryFileSuffix.get(getLanguage());
	}


	


	public static void setBinaryFileSuffix(String binaryFileSuffix) {
		BasicLanguageDependencyManager.binaryFileSuffix = binaryFileSuffix;
	}


	public static String getSourceFileSuffix() {
		return sourceFileSuffix;
	}


	public static String getLanguage() {
		return language;
	}


	public static String getBinaryFileSuffix() {
		return binaryFileSuffix;
	}
	public static MainClassFinder getMainClassFinder() {
		return languageToMainClassFinder.get(getLanguage());
	}
	
//	public static ClassFilesCompiler getSourceFilesCompiler() {
//		return languageToCompiler.get(getLanguage());
//	}
//	public static CheckStyleInvoker getCheckStyleInvoker() {
//		return languageToCheckStyleInvoker.get(getLanguage());
//	}
//	public static Permissible getDefaultPermissible() {
//		return languageToDefaultPermissible.get(getLanguage());
//	}
//	public static PermissionsGenerator getPermissionGenerator() {
//		return languageToPermissionGenerator.get(getLanguage());
//	}
	public static boolean isJava() {
		return getLanguage() == JAVA_LANGUAGE;
	}
//	public static void setCOBj(ConfigurationManager aConfigurationManager) {
//		String cObj = aConfigurationManager.getCourseConfiguration().getString(StaticConfigurationUtils.C_OBJ);
//		if (cObj == null)
//			cObj = aConfigurationManager.getStaticConfiguration().getString(StaticConfigurationUtils.C_OBJ);
//		if (cObj != null)
//			languageToBinaryFileSuffix.put(C_LANGUAGE, "." + cObj);
//
//		
//	}
//	public static void setCOBj(String aCObj) {
//			if (aCObj != null)
//		
//			languageToBinaryFileSuffix.put(C_LANGUAGE, "." + aCObj);
//
//		
//	}
	static {
		languageToSourceFileSuffix.put(JAVA_LANGUAGE, ".java");
		languageToBinaryFileSuffix.put(JAVA_LANGUAGE, ".class");
		languageToSourceFileSuffix.put(C_LANGUAGE, ".c");
		languageToBinaryFileSuffix.put(C_LANGUAGE, ".o");
		languageToSourceFileSuffix.put(PYTHON_LANGUAGE, ".py");
		languageToBinaryFileSuffix.put(PYTHON_LANGUAGE, ".py"); // does it have a compiled class
		
//		languageToMainClassFinder.put(JAVA_LANGUAGE, JavaMainClassFinderSelector.getMainClassFinder());
//		languageToMainClassFinder.put(C_LANGUAGE, ExecutableFinderSelector.getMainClassFinder());
//		
//		languageToCompiler.put(JAVA_LANGUAGE, JavaClassFilesCompilerSelector.getClassFilesCompiler() );
//		languageToCompiler.put(C_LANGUAGE, CFilesCompilerSelector.getClassFilesCompiler());
//		languageToCheckStyleInvoker.put(JAVA_LANGUAGE, JavaCheckStyleInvokerFactory.getSingleton());
//		
//		languageToDefaultPermissible.put(JAVA_LANGUAGE, new DefaultJavaPermissible());
//
//		languageToPermissionGenerator.put(JAVA_LANGUAGE, new JavaPermissionsGenerator());

		
	}
	

}
