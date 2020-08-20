package grader.basics;

import java.util.HashMap;
import java.util.Map;

import grader.basics.config.BasicExecutionSpecificationSelector;
import grader.basics.execution.ExecutableFinderSelector;
import grader.basics.execution.JavaMainClassFinderSelector;
//import grader.checkStyle.CheckStyleInvoker;
//import grader.checkStyle.JavaCheckStyleInvokerFactory;
//import grader.compilation.ClassFilesCompiler;
//import grader.compilation.JavaClassFilesCompilerSelector;
//import grader.compilation.c.CFilesCompilerSelector;
//import grader.config.ConfigurationManager;
//import grader.config.StaticConfigurationUtils;
//import grader.execution.ExecutableFinderSelector;
import grader.basics.execution.CommandGenerator;
//import grader.permissions.Permissible;
//import grader.permissions.PermissionsGenerator;
//import grader.permissions.java.DefaultJavaPermissible;
//import grader.permissions.java.JavaPermissionsGenerator;
import grader.basics.execution.lisp.CLispCommandGenerator;
import grader.basics.execution.lisp.LispCommandGeneratorSelector;
import grader.basics.execution.prolog.PrologCommandGeneratorSelector;
import grader.basics.execution.sml.SMLCommandGeneratorSelector;

public class BasicLanguageDependencyManager {
	static  String sourceFileSuffix;

	 static Map<String, String> languageToSourceFileSuffix = new HashMap<>();
	 protected static Map<String, String> languageToBinaryFileSuffix = new HashMap<>();
	 protected static Map<String, CommandGenerator> languageToMainClassFinder = new HashMap();

		
		public static String JAVA_LANGUAGE = "Java";
		public static String C_LANGUAGE = "C";
		public static String LISP_LANGUAGE = "Lisp";
		public static String PROLOG_LANGUAGE = "Prolog";
		public static String SML_LANGUAGE = "SML";

		public static String CPlusPlus_LANGUAGE = "C++";

		public static String PYTHON_LANGUAGE = "Python";
		 static String DEFAULT_LANGUAGE =JAVA_LANGUAGE ;




	
//	public static  String binaryFileSuffix = ".class";


	public static void setSourceFileSuffix(String sourceFileSuffix) {
		BasicLanguageDependencyManager.sourceFileSuffix = sourceFileSuffix;
	}


	public static void setLanguageToSourceFileSuffix(
			Map<String, String> languageToSourceFileSuffix) {
		BasicLanguageDependencyManager.languageToSourceFileSuffix = languageToSourceFileSuffix;
	}


	



	public static void setLanguage(String language) {
//		BasicLanguageDependencyManager.language = language;
//	sourceFileSuffix = languageToSourceFileSuffix.get(language);
//	binaryFileSuffix = languageToBinaryFileSuffix.get(language);
	}


	


	public static void setBinaryFileSuffix(String binaryFileSuffix) {
//		BasicLanguageDependencyManager.binaryFileSuffix = binaryFileSuffix;
	}


	public static String getSourceFileSuffix() {
		String aLanguage = getLanguage();
		return languageToSourceFileSuffix.get(aLanguage);
//		if (sourceFileSuffix == null) {
//			getLanguage(); // has side effect of setting source file suffix
//		}
//		return sourceFileSuffix;
	}

//	public static boolean isJava() {
//		String aLanguage = getLanguage();
//		return JAVA_LANGUAGE.equals(JAVA_LANGUAGE);
//	}
	// do not cache value as different pro
	public static String getLanguage() {
//		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getLanguage();
//		if (language ==  null) {
//			language = 	BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getStringProperty(BasicStaticConfigurationUtils.LANGUAGE, JAVA_LANGUAGE);
			String language = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getLanguage();
			setLanguage (language);

//
//			if (BasicStaticConfigurationUtils.isUseProjectConfiguration()) {
//				language = BasicStaticConfigurationUtils.getLanguage();
//			}
//		}
//
		return language;
	}


	public static String getBinaryFileSuffix() {
		String aLanguage = getLanguage();
		return languageToBinaryFileSuffix.get(aLanguage);
//		return binaryFileSuffix;
	}
	public static CommandGenerator getMainClassFinder() {
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
		return JAVA_LANGUAGE. equals(getLanguage());
	}
	public static void setCOBj(String aCObj) {
		if (aCObj != null)
	
		languageToBinaryFileSuffix.put(C_LANGUAGE, "." + aCObj);
		languageToBinaryFileSuffix.put(CPlusPlus_LANGUAGE, "." + aCObj);


	
}
	public static boolean hasBinaryFolder() {
		return languageToMainClassFinder.get(getLanguage()).hasBinaryFolder();
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
		languageToSourceFileSuffix.put(CPlusPlus_LANGUAGE, ".cpp");
		languageToBinaryFileSuffix.put(C_LANGUAGE, ".o");
		languageToBinaryFileSuffix.put(CPlusPlus_LANGUAGE, ".o");
		languageToSourceFileSuffix.put(PYTHON_LANGUAGE, ".py");
		languageToBinaryFileSuffix.put(PYTHON_LANGUAGE, ".py"); // does it have a compiled class
		
		languageToSourceFileSuffix.put(LISP_LANGUAGE, ".lisp");
		languageToBinaryFileSuffix.put(LISP_LANGUAGE, ".lisp"); // does it have a compiled class
		
		languageToSourceFileSuffix.put(PROLOG_LANGUAGE, ".pl");
		languageToBinaryFileSuffix.put(PROLOG_LANGUAGE, ".pl"); // does it have a compiled class
		
		languageToSourceFileSuffix.put(SML_LANGUAGE, ".sml");
		languageToBinaryFileSuffix.put(SML_LANGUAGE, ".sml"); // does it have a compiled class
		
		languageToMainClassFinder.put(JAVA_LANGUAGE, JavaMainClassFinderSelector.getMainClassFinder());
		languageToMainClassFinder.put(C_LANGUAGE, ExecutableFinderSelector.getMainClassFinder());
		languageToMainClassFinder.put(CPlusPlus_LANGUAGE, ExecutableFinderSelector.getMainClassFinder());
		languageToMainClassFinder.put(LISP_LANGUAGE, LispCommandGeneratorSelector.getCommandGenerator());
		languageToMainClassFinder.put(PROLOG_LANGUAGE, PrologCommandGeneratorSelector.getCommandGenerator());
		languageToMainClassFinder.put(SML_LANGUAGE, SMLCommandGeneratorSelector.getCommandGenerator());


		
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
