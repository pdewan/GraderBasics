package grader.basics.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;

import grader.basics.BasicLanguageDependencyManager;
import grader.basics.execution.GradingMode;
import grader.basics.execution.lisp.CLispCommandGenerator;
import grader.basics.execution.prolog.SwiplCommandGenerator;
import grader.basics.execution.sml.SMLCommandGenerator;
import grader.basics.project.CurrentProjectHolder;
import grader.basics.project.Project;
import grader.basics.settings.BasicGradingEnvironment;
import util.trace.Tracer;

/**
 * 
 * This serves two purposes. It defines defaults for configurable properties. It
 * also reads project-based properties to be used in localchecks on student
 * computer. Ultimate authority over properties in localchecks mode is
 * ABasicExecution
 *
 */
public class BasicStaticConfigurationUtils {
	public static final String PRIVACY = "privacy";
	public static final String EXECUTION_COMMAND = "execution";

	public static final String LANGUAGE = "language";

	public static final String ENTRY_POINT = "entryPoint";
	public static final String SOURCE_FILES = "sourceFiles";

	public static final String BUILD_FOLDER = "buildFolder";
	public static final String PERMISSIONS = "permissions";

	public static final String CLASS_PATH = "classPath";
	public static final String OE_PATH = "oePath";
	public static final String JUNIT_PATH = "junitPath";
	public static final String LOCAL_GRADER_PATH = "localGraderPath";
	public static final String OE_AND_CLASS_PATH = "oeAndClassPath";
	public static final String CLASS_PATH_SEPARATOR = ":";

	public static final String PROCESS_TEAMS = "processTeams";

	public static final String ENTRY_TAG = "entryTag";
	public static final String ENTRY_TAGS = "entryTags";
	public static final String RESOURCE_RELEASE_TIME = "sleepTime";
	public static final String ARGS = "args";
	public static final String START_TAGS = "startTags";
	public static final String TERMINATING = "terminating";
	public static final String GENERATE_TRACE_FILES = "trace";

//	public static final String JAVA = "Java";

	public static final String CLASS_PATH_VAR = toVariable(CLASS_PATH);
	public static final String CLASS_PATH_SEPARATOR_VAR = toVariable(CLASS_PATH_SEPARATOR);
	public static final String OE_PATH_VAR = toVariable(OE_PATH);
	public static final String LOCAL_GRADER_PATH_VAR = toVariable(LOCAL_GRADER_PATH);

	public static final String JUNIT_PATH_VAR = toVariable(JUNIT_PATH);
	public static final String OE_AND_CLASS_PATH_VAR = toVariable(OE_AND_CLASS_PATH);
	public static final String PERMISSIONS_VAR = toVariable(PERMISSIONS);
	public static final String BUILD_FOLDER_VAR = toVariable(BUILD_FOLDER);
	public static final String IMPLICIT_REQUIRMENTS_ROOT = "implicitRequirementsRoot";
	public static final String DEFAULT_IMPLICIT_REQUIRMENTS_ROOT = "gradingTools";
	public static final String USE_EXECEUTOR = "useExecutor";
	public static final boolean DEFAULT_USE_EXECUTOR = false;
	public static final String EXECUTOR = "executor";
	// this makes no sense, but it is consistent with config file
	public static final String DEFAULT_EXECUTOR = "D:/dewan_backup/Java/Grader/executor";
	public static final String C_OBJ = "language.C.obj";
	public static final String DEFAULT_C_OBJ = "o";
	public static final String MODULES = "modules";
//	public static final String DEFAULT_MODULE = "Comp101";
	public static final String DEFAULT_MODULE = "NoModule";

	public static final String FORK_MAIN = "forkMain";
	public static final Boolean DEFAULT_FORK_MAIN = true;
	public static final String GRADABLE_PROJECT_LOCATION = "gradableProjectLocation";
	public static final String REQUIREMENTS_LOCATION = "requirementsLocation";

	public static final String SOURCE_LOCATION = "sourceLocation";
	public static final String BINARY_LOCATION = "binaryLocation";
	public static final String OBJECT_LOCATION = "objectLocation";
	public static final String FIRST_INPUT_DELAY = "firstInputDelay";
	public static final String BETWEEN_INPUT_DELAY = "betweenInputDelay";

	public static final int DEFAULT_FIRST_INPUT_DELAY = 500;
	public static final int DEFAULT_BETWEEN_INPUT_DELAY = 20;


	public static final String OUTPUT_WAIT_TIME = "outputWaitTime";
	public static final String TEAM_OUTPUT_WAIT_TIME = "teamOutputWaitTime";
	public static final String WAIT_FOR_RESORT = "waitForResort";
	public static final String RESORT_TIME = "resortTime";
	public static final String PROCESS_TIMEOUT = "processTimeout";
	public static final String METHOD_TIMEOUT = "methodTimeout";
	public static final String CONSTRUCTOR_TIMEOUT = "constructorTimeout";
	public static final String USE_METHOD_CONSTRUCTOR_TIMEOUT = "useMethodConstructorTimeout";
	public static final String USE_PROCESS_TIMEOUT = "useProcessTimeout";
	public static final String WAIT_FOR_METHOD_CONSTRUCTOR_AND_PROCESSES = "waitFotMethodConstructorAndProcesses";
	public static final String USE_PROJECT_CONFIGURATION = "useProjectConfiguration";
	public static final String RE_RUN_TESTS = "Rerun tests";
	public static final String SUB_DOCUMENTS = "Sub Documents";
	public static final String USER_PATH = "USER_PATH";
	public static final String HEADLESS_PATH = "grader.headless.path";

	
	public static final boolean DEFAULT_USE_PROJECT_CONFIGURATION = false;
	public static final String DEFAULT_REQUIREMENTS_LOCATION = null;
	public static final String DEFAULT_COURSE = "SampleCourse";
	public static final String DEFAULT_PROBLEM = "SampleAssignent";
	
	public static final boolean DEFAULT_RE_RUN_TESTS = true;
	public static final boolean DEFAULT_SUB_DOCUMENTS= true;
	public static final String DEFAULT_HEADLESS_PATH = "./Test Data/Data";
	public static final boolean DEFAULT_PRIVACY = false;
	public static final String CHECK_STYLE = "checkStyle";
	public static final boolean DEFAULT_CHECK_STYLE = false;

	public static final String CHECK_STYLE_FILE = "checkStyleFile";
	public static final String DEFAULT_CONFIGURATION_FILE = "checks.xml";
	

	
	public static final String CHECK_STYLE_CONFIGURATION_DIRECTORY = "checkStyleDirectory";
	public static final String DEFAULT_CONFIGURATION_DIRECTORY = null;
	
	
	
	public static final String CHECK_STYLE_OUTPUT_DIRECTORY = "checkStyleOutputDirectory";
	public static final String DEFAULT_CHECKSTYLE_OUTPUT_DIRECTORY = null;
	
	public static final String VALGRIND_IMAGE = "valgrindImage";
//	public static final String DEFAULT_VALGRIND_IMAGE = "nalingaddis/valgrind";
//	public static final String DEFAULT_VALGRIND_IMAGE = "prasundewan/pd-mpi";
//	public static final String DEFAULT_VALGRIND_IMAGE = "prasundewan/pd-valgrind:1.0";
	public static final String DEFAULT_VALGRIND_IMAGE = "prasundewan/pd-pdc";
	
	public static final String VALGRIND_INCLUDE = "valgrindInclude";
	public static final String DEFAULT_VALGRIND_INCLUDE = "/usr/include/valgrind/valgrind.h";
	
//	public static final String DEFAILT_VALGRIND_INCLUDE = "/valgrind/include/valgrind.h";

	
	public static final String VALGRIND_CONFIGURATION = "valgrindConfiguration";
	public static final String DEFAULT_VALGRIND_CONFIGURATION = null;
	

	public static final String VALGRIND_CONFIGURATION_DIRECTORY = "valgrindDirectory";
	public static final String DEFAULT_VALGRIND_CONFIGURATION_DRECTORY = "config//valgrind";
	
	public static final String VALGRIND_TRACE_FILE = "valgrindTraceFile";
	public static final String DEFAULT_VALGRIND_TRACE_FILE = "ValgrindTrace.txt";
	
	public static final String VALGRIND_TRACE_DIRECTORY = "valgrindTraceDirectory";
	public static final String DEFAULT_VALGRIND_TRACE_DIRECTORY = "Logs//Valgrind";
	public static final String DEREFERENCED_ARGUMENT_TYPES = "dereferencedArgumentTypes";
	public static final List<String> DEFAULT_DEREFERENCED_ARGUMENT_TYPES = new ArrayList();

	
	public static final String DOCKER_PROGRAM_NAME = "docker_program_name";
	public static final String DEFAULT_DOCKER_PRORAM_NAME = "docker";
	
	public static final String DOCKER_CONTAINER_NAME = "docker_container";
	public static final String DEFAULT_DOCKER_CONTAINER_NAME = "grader-container";

	public static final String TRACING= "tracing";
	public static final boolean DEFAULT_TRACING = true;
	
	public static final String MAX_TRACES = "maxTraces";
	public static final int DEFAULT_MAX_TRACES = 2000;
	
	public static final String MAX_OUTPUT_LINES = "maxOutputLines";
	public static final int DEFAULT_MAX_OUTPUT_LINES = 600;
	
	public static final String MAX_PRINTED_TRACES = "maxPrintedTraces";
	public static final int DEFAULT_MAX_PRINTED_TRACES = 600;

	public static final String BUFFER_TRACED_MESSAGES = "bufferTracedMessages";
	public static final boolean DEFAULT_BUFFER_TRACED_MESSAGES = true;
	
	public static final String SML_IS_BAT = "smlIsBat";
	public static final boolean DEFAULT_SML_IS_BAT = true;
	
	public static final String FORK_IN_PROJECT_FOLDER = "forkInProjectFolder";
	public static final boolean DEFAULT_FORK_IN_PROJECT_FOLDER = true;

	
	public static final String SHIFT_ASSIGNMENT_DATES = "shiftAssignmentDates";
	public static final boolean DEFAULT_SHIFT_ASSIGNMENT_DATES = false;
	
	public static final String MANUAL_GRADING = "Manual";
	public static final String AUTOMATIC_GRADING = "Automatic";
	public static final String HYBRID_GRADING = "Automatic and then manual";
    public static final String NAVIGATION_KIND = "navigationKind";
  
    
	public static final String DEFAULT_NAVIGATION_KIND = HYBRID_GRADING;
	
	public static final String STARTER_UI = "starterUI";
	public static final boolean DEFAULT_CREATE_STARTER_UI = true;
	
	public static final String SAVE_INTERACTIVE_SETTINGS = "saveInteractiveSettings";
	public static final boolean DEFAULT_SAVE_INTERACTIVE_SETTINGS = true;
	
	public static final String ECHO_OUTPUT = "echoOutput";
	public static final boolean DEFAULT_ECHO_OUTPUT = true;
	
	public static final String OUTPUT_TRACE = "output";
	public static final boolean DEFAULT_OUTPUT_TRACE = false;

	public static final String DOCKER_MOUNT_IS_COPY = "dockerMountIsCopy";
	public static final boolean DEFAULT_DOCKER_MOUNT_IS_COPY = false;
	
	public static final String CREATE_DOCKER_CONTAINER = "createDockerContainer";
	public static final Boolean DEFAULT_CREATE_DOCKER_CONTAINER = true;
	public static final String INSTRUMENT_USING_VALGRIND = "instrumentUsingValgrind";
	public static final Boolean DEFAULT_INSTRUMENT_USING_VALGRIND = true;
	public static final String LOG_TEST_DATA = "logTestData";	
	public static final Boolean DEFAULT_LOG_TEST_DATA = true;
	public static final String LOAD_CLASSES = "loadClasses";	
	public static final Boolean DEFAULT_LOAD_CLASSES= true;
	public static final String ENABLE_INSTRUCTOR_HELP = "enableInstructorHelp";	
	public static final Boolean DEFAULT_ENABLE_INSTRUCTOR_HELP = false;
	


//	private static  List<String> basicCommand;
//	private static Map<String, List<String>> processToBasicCommand = new HashMap();
	private static String duplicatedClassPathSeparator;
	private static List<String> graderProcessTeams;

	private static boolean useProjectConfiguration;
	protected static String module;

	protected static String problem;
	protected static String test;
	protected static String testSuite;
	protected static List<String> emptyList = new ArrayList();

//	Comp533s18.execution = java, -cp, .{:}..{:}{classPath}{:}{oePath}{:}{junitPath}, {entryTags}, {args}

	// public static final String[] DEFAULT_JAVA_BASIC_COMMAND_ARRAY = {
//			"java", "-cp", ".{:}..{:}{classPath}{:}{:}{junitPath}{:}{localGraderPath}", "{entryTags}", "{args}"
//	};
	public static final String[] DEFAULT_JAVA_BASIC_COMMAND_ARRAY = { "java", "-cp",
			".{:}..{:}{classPath}{:}{:}{junitPath}{:}{localGraderPath}", "{entryPoint}", "{args}" };
	public static final String[] DEFAULT_LISP_BASIC_COMMAND_ARRAY = { CLispCommandGenerator.CLISP, "-i",
//			toVariable(SOURCE_FILES),
			toVariable(ENTRY_POINT), "{args}" };
	public static final String[] DEFAULT_PROLOG_BASIC_COMMAND_ARRAY = { SwiplCommandGenerator.SWIPL,
//			toVariable(SOURCE_FILES),
			toVariable(ENTRY_POINT), "{args}" };
	public static final String[] DEFAULT_SML_BAT_BASIC_COMMAND_ARRAY = { SMLCommandGenerator.SML_BAT,
//			toVariable(SOURCE_FILES),
			toVariable(ENTRY_POINT), "{args}" };
	public static final String[] DEFAULT_SML_BASIC_COMMAND_ARRAY = { SMLCommandGenerator.SML,
//			toVariable(SOURCE_FILES),
			toVariable(ENTRY_POINT), "{args}" };
	public static final String[] DEFAULT_OE_BASIC_COMMAND_ARRAY = { "java", "-cp",
			".{:}..{:}{classPath}{:}{oePath}{:}{junitPath}{:}{localGraderPath}", "{entryTags}", "{args}" };

	public static final List<String> DEFAULT_JAVA_BASIC_COMMAND = Arrays.asList(DEFAULT_JAVA_BASIC_COMMAND_ARRAY);
	public static final List<String> DEFAULT_BASIC_LISP_COMMAND = Arrays.asList(DEFAULT_LISP_BASIC_COMMAND_ARRAY);
	public static final List<String> DEFAULT_BASIC_PROLOG_COMMAND = Arrays.asList(DEFAULT_PROLOG_BASIC_COMMAND_ARRAY);
	public static final List<String> DEFAULT_BASIC_SML_BAT_COMMAND = Arrays.asList(DEFAULT_SML_BAT_BASIC_COMMAND_ARRAY);
	public static final List<String> DEFAULT_BASIC_SML_COMMAND = Arrays.asList(DEFAULT_SML_BASIC_COMMAND_ARRAY);

	public static final List<String> DEFAULT_OE_BASIC_COMMAND = Arrays.asList(DEFAULT_OE_BASIC_COMMAND_ARRAY);
	public static final int DEFAULT_RESOURCE_RELEASE_TIME = 2000;
	public static final int DEFAULT_CONSTRUCTOR_TIME_OUT = 2000;// in
	// milliseconds
	public static final int DEFAULT_METHOD_TIME_OUT = 2000; // in milliseconds
	public static final int DEFAULT_PROCESS_TIME_OUT = 4; // in seconds
	public static final String CHECK_ALL_SPECIFIED_TAGS = "checkAllSpecifiedTags";
	public static final boolean DEFAULT_CHECK_ALL_SPECIFIED_TAGS = false;
	
	public static final String HIDE_REDIRECTED_OUTPUT = "hideRedirectedOuput";
	public static final boolean DEFAULT_HIDE_REDIRECTED_OUTPUT = false;


	public static String toVariable(String aVariableName) {
		return "{" + aVariableName + "}";
	}

	public static void setBasicCommandToDefaultEntryPointCommand() {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
				.setGraderBasicCommand(DEFAULT_JAVA_BASIC_COMMAND);
//		basicCommand = DEFAULT_JAVA_BASIC_COMMAND;
	}

	public static void setBasicCommandToDefaultEntryTagCommand() {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification()
				.setGraderBasicCommand(DEFAULT_OE_BASIC_COMMAND);
//		basicCommand =  DEFAULT_OE_BASIC_COMMAND;
	}

	public static List<String> getBasicCommand() {
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getBasicCommand();
//		if (basicCommand == null) {
//			basicCommand = DEFAULT_OE_BASIC_COMMAND;
//		}
//////		return getInheritedListModuleProblemProperty(EXECUTION_COMMAND);
//		return basicCommand;
	}

	public static boolean hasClassPath() {
//		getBasicCommand();
		return hasClassPath(getBasicCommand());
//		if (basicCommand == null) {
//			return false;
//		}
//		for (String aCommand:basicCommand) {
//			if (aCommand.contains(CLASS_PATH_VAR) || aCommand.contains(OE_AND_CLASS_PATH_VAR)) {
//				return true;
//			}
//		}
//		return false;	
	}

	public static boolean hasClassPath(List<String> basicCommand) {
//		getBasicCommand();
		if (basicCommand == null) {
			return false;
		}
		for (String aCommand : basicCommand) {
			if (aCommand.contains(CLASS_PATH_VAR) || aCommand.contains(OE_AND_CLASS_PATH_VAR)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasOEClassPath() {
		return hasOEClassPath(getBasicCommand());
//		getBasicCommand();
//		if (basicCommand == null) {
//			return false;
//		}
//		for (String aCommand:basicCommand) {
//			if (aCommand.contains(OE_PATH_VAR) || aCommand.contains(OE_AND_CLASS_PATH_VAR)) {
//				return true;
//			}
//		}
//		return false;	
	}

	public static boolean hasOEClassPath(List<String> basicCommand) {
//		getBasicCommand();
		if (basicCommand == null) {
			return false;
		}
		for (String aCommand : basicCommand) {
			if (aCommand.contains(OE_PATH_VAR) || aCommand.contains(OE_AND_CLASS_PATH_VAR)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasOEOrClassPath() {
		return hasClassPath(getBasicCommand()) || hasOEClassPath(getBasicCommand());
	}

	public static boolean hasOEOrClassPath(List<String> basicCommand) {
		return hasClassPath(basicCommand) || hasOEClassPath(basicCommand);
	}

	protected static String[] emptyEntryPoints = {};
	protected static String[] potentialMainEntryPoints;

//	public static String[] getPotentialMainEntryPointNames() {
//		String retVal = getInheritedStringModuleProblemProperty(ENTRY_POINT, null);
//		if (retVal != null) {
//			GraderSettingsManager manager = GraderSettingsManagerSelector.getGraderSettingsManager();
//			retVal = manager.replaceModuleProblemVars(retVal);
//			return new String[]{retVal.replaceAll(" ", "")};
//		}
//		return emptyEntryPoints;
//	}
	public static String[] getPotentialMainEntryPointNames() {
		return potentialMainEntryPoints;
//		if (potentialMainEntryPoints == null) {
//		String retVal = getInheritedStringModuleProblemProperty(ENTRY_POINT, null);
//		if (retVal != null) {
//			GraderSettingsManager manager = GraderSettingsManagerSelector.getGraderSettingsManager();
//			retVal = manager.replaceModuleProblemVars(retVal);
//			potentialMainEntryPoints = new String[]{retVal.replaceAll(" ", "")};
//		} else {
//			potentialMainEntryPoints = emptyEntryPoints;
//		}
//		}
//		return potentialMainEntryPoints;
	}

	public static void setPotentialMainEntryPointNames(String[] aNames) {
		potentialMainEntryPoints = aNames;
	}

	// we really do not need this method, for backwards compatibility
	public static List<String> getBasicCommand(String aProcessName) {
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getBasicCommand(aProcessName);
//		List<String> retVal = processToBasicCommand.get(aProcessName);
//		if (retVal == null) {
////			retVal = getBasicCommand();
//			retVal = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getBasicCommand();
//		}
//		
//		return retVal;

	}

	public static void setBasicCommand(String aProcessName, List<String> aCommand) {
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setGraderBasicCommand(aProcessName,
				aCommand);
//		processToBasicCommand.put(aProcessName, aCommand);

	}

	public static boolean hasEntryPoint(List<String> aCommand) {
		return hasSubString(aCommand, ENTRY_POINT);
	}

	public static boolean hasSubString(List<String> aCommand, String aSubString) {
		for (String aCommmandComponent : aCommand) {
			if (aCommmandComponent.contains(aSubString)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasEntryTag(List<String> aProcessCommand) {
		return hasSubString(aProcessCommand, ENTRY_TAG);
	}

	public static boolean hasEntryTags(List<String> aProcessCommand) {
		return hasSubString(aProcessCommand, ENTRY_TAGS);
	}

	public static boolean haArgs(String aProcessCommand) {
		return aProcessCommand.contains(ARGS);
	}

//	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String FILE_SEPARATOR = "/";

	public static String quotePath(String path) {
		if (!path.contains(" "))
			return path;
		boolean startSlash = path.startsWith("\\") || path.startsWith("/");
		boolean endSlash = path.endsWith("\\") || path.endsWith("/");
		String[] split = path.split("[\\\\/]+");

		StringBuilder quotPath = new StringBuilder(path.length());

		if (startSlash) {
			quotPath.append(FILE_SEPARATOR);
		}

		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			if (s.contains(" ")) {
//	            s = "\"" + s + "\"";
				s = "\\\"" + s + "\\\"";

			}
			quotPath.append(s);
			if (i + 1 < split.length) {
				quotPath.append(FILE_SEPARATOR);
			}
		}

		if (endSlash) {
			quotPath.append(FILE_SEPARATOR);
		}

		return quotPath.toString();
	}

	public static int getClassPathFlagIndex(List<String> aBasicCommand) {
		int aCpIndex = aBasicCommand.indexOf("-cp");
		if (aCpIndex < 0)
			aCpIndex = aBasicCommand.indexOf("-classpath");
		return aCpIndex;
	}

	public static String getExecutionCommandRawClassPath() {
		return getExecutionCommandRawClassPath(getBasicCommand());
//		List<String> aBasicCommand = getBasicCommand();
//		int aCpIndex = getClassPathFlagIndex(aBasicCommand);
//		if (aCpIndex < 0)
//			return null;
//		if (aCpIndex + 1 >= aBasicCommand.size())
//			return null;
//		return getReplacedRawClassPath(aBasicCommand.get(aCpIndex + 1));

	}

	public static String getExecutionCommandRawClassPath(List<String> aBasicCommand) {
//		List<String> aBasicCommand = getBasicCommand();
		int aCpIndex = getClassPathFlagIndex(aBasicCommand);
		if (aCpIndex < 0)
			return null;
		if (aCpIndex + 1 >= aBasicCommand.size())
			return null;
		return getReplacedRawClassPath(aBasicCommand.get(aCpIndex + 1));

	}

	public static String getReplacedRawClassPath(String command) {
		// do we really need all of these ifs, more efficient without them? - debugging
		// will be easier
		// all of these will be in the same command
		if (command.contains(CLASS_PATH_VAR)) {

			command = command.replace(CLASS_PATH_VAR, BasicGradingEnvironment.get().getClassPath());
		}

		if (command.contains(CLASS_PATH_SEPARATOR_VAR)) {
			command = command.replace(CLASS_PATH_SEPARATOR_VAR, BasicGradingEnvironment.get().getClassPathSeparator());
		}

		if (command.contains(LOCAL_GRADER_PATH_VAR)) { // the config property does not have this, I suppose it is to
														// allow student code to invoke api calls in grader

			command = command.replace(LOCAL_GRADER_PATH_VAR,
					// BasicGradingEnvironment.get().getClasspath());
					BasicGradingEnvironment.get().getLocalGraderClassPath());

		}
		if (command.contains(OE_PATH_VAR)) {
			if (command.toLowerCase().contains("local")) { // we already have oeall
				command = command.replace(OE_PATH_VAR,
						// BasicGradingEnvironment.get().getClasspath());
						"");
			} else {
				command = command.replace(OE_PATH_VAR,
						// BasicGradingEnvironment.get().getClasspath());
						BasicGradingEnvironment.get().getOEClassPath());
			}

		}

		if (command.contains(JUNIT_PATH_VAR)) {
			command = command.replace(JUNIT_PATH_VAR, BasicGradingEnvironment.get().getJUnitClassPath());
			// } else if (command.contains(OE_AND_CLASS_PATH_VAR)) {
			// command = command.replace(OE_AND_CLASS_PATH_VAR,
			// BasicGradingEnvironment.get().getClassPath());
		}
		String aClassPathSeparator = BasicGradingEnvironment.get().getClassPathSeparator();
		if (duplicatedClassPathSeparator == null) {
			// just avoding new String creation
			duplicatedClassPathSeparator = aClassPathSeparator + aClassPathSeparator;
		}
		// certain libraries may not exist, specially in the server, see what happens
		// without them
		command = command.replaceAll(duplicatedClassPathSeparator, aClassPathSeparator);
		return command;
		// javac wants no quotes!
//					String anOSPath = BasicGradingEnvironment.get().toOSClassPath(command);
//					return anOSPath;
	}

	public static void replaceClassPathVars(List<String> basicCommand) {
		int aCpIndex = getClassPathFlagIndex(basicCommand);
		if (aCpIndex < 0)
			return;

		if (aCpIndex + 1 >= basicCommand.size()) {
			Tracer.warning("Nothing follows classpath flag");
			return;
		}
		String aReplacement = getReplacedRawClassPath(basicCommand.get(aCpIndex + 1));
		String anOSPath = BasicGradingEnvironment.get().toOSClassPath(aReplacement);
		basicCommand.set(aCpIndex + 1, anOSPath);

//		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {
//
//			String command = basicCommand.get(aCommandIndex);
//			
////			// do we really need all of these ifs, more efficient without them? - debugging will be easier
////			// all of these will be in the same command
////			if (command.contains(CLASS_PATH_VAR)) {
////
////				command = command.replace(CLASS_PATH_VAR,
////						BasicGradingEnvironment.get().getClassPath());
////			}
////
////			if (command.contains(CLASS_PATH_SEPARATOR_VAR)) {
////				command = command.replace(CLASS_PATH_SEPARATOR_VAR,
////						BasicGradingEnvironment.get().getClassPathSeparator());
////			}
////			if (command.contains(OE_PATH_VAR)) {
////
////				command = command.replace(OE_PATH_VAR,
////				// BasicGradingEnvironment.get().getClasspath());
////						BasicGradingEnvironment.get().getOEClassPath());
////
////			} 
////			if (command.contains(JUNIT_PATH_VAR)) {
////				command = command.replace(JUNIT_PATH_VAR,
////						BasicGradingEnvironment.get().getJUnitClassPath());
////				// } else if (command.contains(OE_AND_CLASS_PATH_VAR)) {
////				// command = command.replace(OE_AND_CLASS_PATH_VAR,
////				// BasicGradingEnvironment.get().getClassPath());
////			} 
//			command = getReplacedClassPath(command);
//			basicCommand.set(aCommandIndex, command);
//		}
	}

//	public static void replacePermissionVariables(List<String> basicCommand, Project aProject) {
//		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {
//
//			String command = basicCommand.get(aCommandIndex);
//			if (doPermissions && command.contains(PERMISSIONS_VAR)) {
//
//				String aPolicyFilePath = JavaProjectToPermissionFile
//						.getPermissionFile(aProject).getAbsolutePath();
//				try {
//					aPolicyFilePath = JavaProjectToPermissionFile
//							.getPermissionFile(aProject).getCanonicalPath();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				aPolicyFilePath = aPolicyFilePath.replace("\\", "/");
//
//				aPolicyFilePath = quotePath(aPolicyFilePath);
//
//				command = command.replace(PERMISSIONS_VAR, aPolicyFilePath);
//				basicCommand.set(aCommandIndex, command);
//
//			}
//		}
//	}
	static final String emptyString = "";
	public static void replaceEntryPoint(List<String> basicCommand, String anEntryPoint, String anEntryTagTarget) {
		if (anEntryPoint == null) {
			anEntryPoint = emptyString;
		}
		if (anEntryTagTarget == null) {
			anEntryTagTarget = emptyString;
		}
		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {

			String command = basicCommand.get(aCommandIndex);
			if (anEntryPoint != null) {
				command = command.replace(toVariable(ENTRY_POINT), anEntryPoint);

			}
			if (anEntryTagTarget != null ) {
				command = command.replace(toVariable(ENTRY_TAGS), anEntryTagTarget);
				command = command.replace(toVariable(ENTRY_TAG), anEntryTagTarget); // will match tags also

			}
			// check if entryTagTarget is empty and replace it with entry point
			basicCommand.set(aCommandIndex, command);
		}
	}
	static StringBuffer filesToString = new StringBuffer();
	public static String toString(List<File> aFiles) {
		filesToString.setLength(0);
		for (File aFile:aFiles) {
			filesToString.append(" " + aFile.getAbsolutePath());
		}
		return filesToString.toString();
	}
	public static void replaceSourceFiles(List<String> basicCommand ) {
		String aSourceFiles = toString(CurrentProjectHolder.getCurrentProject().getSourceFiles());
		int anIndex = basicCommand.indexOf(toVariable(SOURCE_FILES));
		if (anIndex < 0) {
			return;
		}
		basicCommand.remove(anIndex);
		List<File> aFiles = CurrentProjectHolder.getCurrentProject().getSourceFiles();
		for (File aFile:aFiles) {
			String aFilePath = aFile.getAbsolutePath();
			if (!basicCommand.contains(aFilePath)) {
				basicCommand.add(anIndex, aFilePath);
			}
//			basicCommand.add(anIndex, aFile.getAbsolutePath());
		}
		
//				
//		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {
//
//			String command = basicCommand.get(aCommandIndex);
//			
//				command = command.replace(toVariable(SOURCE_FILES), aSourceFiles);
//				basicCommand.set(aCommandIndex, command);
//
//
//			}
			
		
		
	}

	public static void replaceBuildFolder(List<String> basicCommand, File aBuildFolder) {
		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {

			String command = basicCommand.get(aCommandIndex);
			// we should always have a build folder I suppose
			// this is meant for C like programs
			if (command.contains(BUILD_FOLDER_VAR)) {
				command = command.replace(BUILD_FOLDER_VAR, aBuildFolder.getAbsolutePath());
				basicCommand.set(aCommandIndex, command);

			}
		}
	}

	public static void replaceArgs(List<String> basicCommand, String[] anArgs) {
		int argsIndex = basicCommand.indexOf(toVariable(ARGS));
		if (argsIndex >= 0) {
			basicCommand.remove(argsIndex);
			if (anArgs != null) {
				for (int i = 0; i < anArgs.length; i++) {
					basicCommand.add(argsIndex + i, anArgs[i]);
				}
			}

		}
	}

//	public static String[] getExecutionCommand(Project aProject,
//			String aProcessName, File aBuildFolder, String anEntryPoint,
//			String anEntryTagTarget, String[] anArgs) {
//
//		List<String> basicCommand = null;
//		if (aProcessName == null || aProcessName.isEmpty()) {
//		    if (anEntryPoint != null) {
//		    	basicCommand = getBasicCommand();
////		    	basicCommand = DEFAULT_JAVA_BASIC_COMMAND;
//
//		    } else {
//			basicCommand = getBasicCommand();
//		    }
//		} else {
//		
//			basicCommand = getBasicCommand(aProcessName);
//
//		}
//		return getExecutionCommand(basicCommand, aProject, aProcessName, aBuildFolder, anEntryPoint, anEntryTagTarget, anArgs);
//////		List<String> retVal = new ArrayList(basicCommand.size());
////		List<String> retVal = new ArrayList(basicCommand.size() + 5); // to accommodate args
////		retVal.addAll(basicCommand);
////		replaceClassPathVars(retVal);
//////		replacePermissionVariables(retVal, aProject);
////		replaceEntryPoint(retVal, anEntryPoint, anEntryTagTarget);
////		replaceBuildFolder(retVal, aBuildFolder);
////		replaceArgs(retVal, anArgs);
////		return retVal.toArray(new String[0]);
//
//
////		
////		
////		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {
////
////			String command = basicCommand.get(aCommandIndex);
////			if (command.contains(CLASS_PATH_VAR)) {
////
////				command = command.replace(CLASS_PATH_VAR,
////						BasicGradingEnvironment.get().getClassPath());
////
////			} else if (command.contains(CLASS_PATH_SEPARATOR_VAR)) {
////				command = command.replace(CLASS_PATH_SEPARATOR_VAR,
////						BasicGradingEnvironment.get().getClassPathSeparator());
////			} else if (command.contains(OE_PATH_VAR)) {
////
////				command = command.replace(OE_PATH_VAR,
////				// BasicGradingEnvironment.get().getClasspath());
////						BasicGradingEnvironment.get().getOEClassPath());
////
////			} else if (command.contains(JUNIT_PATH_VAR)) {
////				command = command.replace(JUNIT_PATH_VAR,
////						BasicGradingEnvironment.get().getJUnitClassPath());
////				// } else if (command.contains(OE_AND_CLASS_PATH_VAR)) {
////				// command = command.replace(OE_AND_CLASS_PATH_VAR,
////				// BasicGradingEnvironment.get().getClassPath());
////			} else if (doPermissions && command.contains(PERMISSIONS_VAR)) {
////
////				String aPolicyFilePath = JavaProjectToPermissionFile
////						.getPermissionFile(aProject).getAbsolutePath();
////				try {
////					aPolicyFilePath = JavaProjectToPermissionFile
////							.getPermissionFile(aProject).getCanonicalPath();
////				} catch (IOException e1) {
////					e1.printStackTrace();
////				}
////				aPolicyFilePath = aPolicyFilePath.replace("\\", "/");
////
////				aPolicyFilePath = quotePath(aPolicyFilePath);
////
////				command = command.replace(PERMISSIONS_VAR, aPolicyFilePath
////
////				);
////			}
////
////			if (anEntryPoint != null) {
////				command = command
////						.replace(toVariable(ENTRY_POINT), anEntryPoint);
////			}
////			if (anEntryTagTarget != null) {
////				command = command.replace(toVariable(ENTRY_TAGS),
////						anEntryTagTarget);
////				command = command.replace(toVariable(ENTRY_TAG),
////						anEntryTagTarget); // will match tags also
////
////			}
////
////			command = command.replace(toVariable(BUILD_FOLDER),
////					aBuildFolder.getAbsolutePath());
////
////			retVal.add(command);
////		}
////		int argsIndex = retVal.indexOf(toVariable(ARGS));
////		if (argsIndex >= 0) {
////			retVal.remove(argsIndex);
////			for (int i = 0; i < anArgs.length; i++) {
////				retVal.add(argsIndex + i, anArgs[i]);
////			}
////
////		}
////		return retVal.toArray(new String[0]);
//
//	}
	public static String[] getExecutionCommand(Project aProject, String aProcessName, File aBuildFolder,
			String anEntryPoint, String anEntryTagTarget, String[] anArgs) {

		List<String> basicCommand = null;
		if (aProcessName == null || aProcessName.isEmpty()) {
			basicCommand = BasicExecutionSpecificationSelector.getBasicExecutionSpecification().getBasicCommand();
//		    if (anEntryPoint != null) {
//		    	basicCommand = getBasicCommand();
////		    	basicCommand = DEFAULT_JAVA_BASIC_COMMAND;
//
//		    } else {
//			basicCommand = getBasicCommand();
//		    }
		} else {

			basicCommand = getBasicCommand(aProcessName);

		}
		
		return getExecutionCommand(basicCommand, aProject, aProcessName, aBuildFolder, anEntryPoint, anEntryTagTarget,
				anArgs);
////		List<String> retVal = new ArrayList(basicCommand.size());
//		List<String> retVal = new ArrayList(basicCommand.size() + 5); // to accommodate args
//		retVal.addAll(basicCommand);
//		replaceClassPathVars(retVal);
////		replacePermissionVariables(retVal, aProject);
//		replaceEntryPoint(retVal, anEntryPoint, anEntryTagTarget);
//		replaceBuildFolder(retVal, aBuildFolder);
//		replaceArgs(retVal, anArgs);
//		return retVal.toArray(new String[0]);

//		
//		
//		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {
//
//			String command = basicCommand.get(aCommandIndex);
//			if (command.contains(CLASS_PATH_VAR)) {
//
//				command = command.replace(CLASS_PATH_VAR,
//						BasicGradingEnvironment.get().getClassPath());
//
//			} else if (command.contains(CLASS_PATH_SEPARATOR_VAR)) {
//				command = command.replace(CLASS_PATH_SEPARATOR_VAR,
//						BasicGradingEnvironment.get().getClassPathSeparator());
//			} else if (command.contains(OE_PATH_VAR)) {
//
//				command = command.replace(OE_PATH_VAR,
//				// BasicGradingEnvironment.get().getClasspath());
//						BasicGradingEnvironment.get().getOEClassPath());
//
//			} else if (command.contains(JUNIT_PATH_VAR)) {
//				command = command.replace(JUNIT_PATH_VAR,
//						BasicGradingEnvironment.get().getJUnitClassPath());
//				// } else if (command.contains(OE_AND_CLASS_PATH_VAR)) {
//				// command = command.replace(OE_AND_CLASS_PATH_VAR,
//				// BasicGradingEnvironment.get().getClassPath());
//			} else if (doPermissions && command.contains(PERMISSIONS_VAR)) {
//
//				String aPolicyFilePath = JavaProjectToPermissionFile
//						.getPermissionFile(aProject).getAbsolutePath();
//				try {
//					aPolicyFilePath = JavaProjectToPermissionFile
//							.getPermissionFile(aProject).getCanonicalPath();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				aPolicyFilePath = aPolicyFilePath.replace("\\", "/");
//
//				aPolicyFilePath = quotePath(aPolicyFilePath);
//
//				command = command.replace(PERMISSIONS_VAR, aPolicyFilePath
//
//				);
//			}
//
//			if (anEntryPoint != null) {
//				command = command
//						.replace(toVariable(ENTRY_POINT), anEntryPoint);
//			}
//			if (anEntryTagTarget != null) {
//				command = command.replace(toVariable(ENTRY_TAGS),
//						anEntryTagTarget);
//				command = command.replace(toVariable(ENTRY_TAG),
//						anEntryTagTarget); // will match tags also
//
//			}
//
//			command = command.replace(toVariable(BUILD_FOLDER),
//					aBuildFolder.getAbsolutePath());
//
//			retVal.add(command);
//		}
//		int argsIndex = retVal.indexOf(toVariable(ARGS));
//		if (argsIndex >= 0) {
//			retVal.remove(argsIndex);
//			for (int i = 0; i < anArgs.length; i++) {
//				retVal.add(argsIndex + i, anArgs[i]);
//			}
//
//		}
//		return retVal.toArray(new String[0]);

	}

	public static String[] getExecutionCommand(List<String> basicCommand, Project aProject, String aProcessName,
			File aBuildFolder, String anEntryPoint, String anEntryTagTarget, String[] anArgs) {

//		List<String> basicCommand = null;
//		if (aProcessName == null || aProcessName.isEmpty()) {
//		
//			basicCommand = getBasicCommand();
//		} else {
//		
//			basicCommand = getBasicCommand(aProcessName);
//
//		}
//		List<String> retVal = new ArrayList(basicCommand.size());
		List<String> retVal = new ArrayList(basicCommand.size() + 5); // to accommodate args
		retVal.addAll(basicCommand);
		replaceClassPathVars(retVal);
//		replacePermissionVariables(retVal, aProject);
		replaceEntryPoint(retVal, anEntryPoint, anEntryTagTarget);
		replaceBuildFolder(retVal, aBuildFolder);
		replaceSourceFiles(retVal);
		replaceArgs(retVal, anArgs);
		retVal = BasicLanguageDependencyManager.getMainClassFinder().processedCommands(retVal, aProject, aProcessName, aBuildFolder, anEntryPoint, anEntryTagTarget, anArgs);
		return retVal.toArray(new String[0]);

//		
//		
//		for (int aCommandIndex = 0; aCommandIndex < basicCommand.size(); aCommandIndex++) {
//
//			String command = basicCommand.get(aCommandIndex);
//			if (command.contains(CLASS_PATH_VAR)) {
//
//				command = command.replace(CLASS_PATH_VAR,
//						BasicGradingEnvironment.get().getClassPath());
//
//			} else if (command.contains(CLASS_PATH_SEPARATOR_VAR)) {
//				command = command.replace(CLASS_PATH_SEPARATOR_VAR,
//						BasicGradingEnvironment.get().getClassPathSeparator());
//			} else if (command.contains(OE_PATH_VAR)) {
//
//				command = command.replace(OE_PATH_VAR,
//				// BasicGradingEnvironment.get().getClasspath());
//						BasicGradingEnvironment.get().getOEClassPath());
//
//			} else if (command.contains(JUNIT_PATH_VAR)) {
//				command = command.replace(JUNIT_PATH_VAR,
//						BasicGradingEnvironment.get().getJUnitClassPath());
//				// } else if (command.contains(OE_AND_CLASS_PATH_VAR)) {
//				// command = command.replace(OE_AND_CLASS_PATH_VAR,
//				// BasicGradingEnvironment.get().getClassPath());
//			} else if (doPermissions && command.contains(PERMISSIONS_VAR)) {
//
//				String aPolicyFilePath = JavaProjectToPermissionFile
//						.getPermissionFile(aProject).getAbsolutePath();
//				try {
//					aPolicyFilePath = JavaProjectToPermissionFile
//							.getPermissionFile(aProject).getCanonicalPath();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				aPolicyFilePath = aPolicyFilePath.replace("\\", "/");
//
//				aPolicyFilePath = quotePath(aPolicyFilePath);
//
//				command = command.replace(PERMISSIONS_VAR, aPolicyFilePath
//
//				);
//			}
//
//			if (anEntryPoint != null) {
//				command = command
//						.replace(toVariable(ENTRY_POINT), anEntryPoint);
//			}
//			if (anEntryTagTarget != null) {
//				command = command.replace(toVariable(ENTRY_TAGS),
//						anEntryTagTarget);
//				command = command.replace(toVariable(ENTRY_TAG),
//						anEntryTagTarget); // will match tags also
//
//			}
//
//			command = command.replace(toVariable(BUILD_FOLDER),
//					aBuildFolder.getAbsolutePath());
//
//			retVal.add(command);
//		}
//		int argsIndex = retVal.indexOf(toVariable(ARGS));
//		if (argsIndex >= 0) {
//			retVal.remove(argsIndex);
//			for (int i = 0; i < anArgs.length; i++) {
//				retVal.add(argsIndex + i, anArgs[i]);
//			}
//
//		}
//		return retVal.toArray(new String[0]);

	}

	public static List<String> getProcessTeams() {
		return graderProcessTeams;
	}

	public static void setProcessTeams(List<String> newVal) {
		graderProcessTeams = newVal;
		;
	}

	public static boolean isTeamProcess() {
		return graderProcessTeams != null && graderProcessTeams.isEmpty();
	}

	public static boolean isUseProjectConfiguration() {
		return BasicExecutionSpecificationSelector.getBasicExecutionSpecification().isUseProjectConfiguration();
//		return useProjectConfiguration;
	}

	public static void setUseProjectConfiguration(boolean newVal) {
//		BasicStaticConfigurationUtils.useProjectConfiguration = useProjectConfiguration;
		BasicExecutionSpecificationSelector.getBasicExecutionSpecification().setUseProjectConfiguration(newVal);
	}

	public static Boolean getInheritedBooleanModuleProblemProperty(PropertiesConfiguration configuration, String module,
			String problem, String aTest, String property, Boolean defaultValue) {
		if (configuration == null) {
			return defaultValue;
		}

		Boolean retVal = configuration.getBoolean(module + "." + problem + "." + property, null);

		if (retVal == null) {
			retVal = configuration.getBoolean(module + "." + property, null);
		}
		if (retVal == null) {
			retVal = configuration.getBoolean(DEFAULT + "." + property, defaultValue);
		} 
        if (retVal == null) {
			retVal = configuration.getBoolean(property, defaultValue);
		}

		return retVal;

	}

	public static final String DEFAULT = "default";
	

	public static Integer getInheritedIntegerModuleProblemProperty(PropertiesConfiguration configuration, String module,
			String problem, String test, String property, Integer defaultValue) {
		if (configuration == null) {
			return defaultValue;
		}

		Integer retVal = configuration.getInteger(module + "." + problem + "." + property, null);

		if (retVal == null) {
			retVal = configuration.getInteger(module + "." + property, null);
		}
		if (retVal == null) {
			retVal = configuration.getInteger(DEFAULT + "." + property, defaultValue);
		}
		if (retVal == null) {
			retVal = configuration.getInteger(property, defaultValue);

		}

		return retVal;

	}

	public static String getConfigurationBasicDirectString(String property, String defaultValue) {

		if (!isUseProjectConfiguration()) {
			// cannot use project configuration before location is known to create project
			return defaultValue;
		}
		PropertiesConfiguration aConfiguration = BasicConfigurationManagerSelector.getConfigurationManager()
				.getOrCreateProjectConfiguration();
		if (aConfiguration == null) {
			return defaultValue;
		}
		return aConfiguration.getString(property, defaultValue);

	}

	public static List getConfigurationBasicDirectList(String property, List defaultValue) {

		if (property == BasicStaticConfigurationUtils.MODULES || // infinite recursion
				!isUseProjectConfiguration()) {
			// cannot use project configuration before location is known to create project
			return defaultValue;
		}
		PropertiesConfiguration aConfiguration = BasicConfigurationManagerSelector.getConfigurationManager()
				.getOrCreateProjectConfiguration();
		if (aConfiguration == null) {
			return defaultValue;
		}
		List retVal = aConfiguration.getList(property);
		if (retVal == null) {
			return defaultValue;
		}
		return retVal;

	}

//	public static String getBasicInheritedStringModuleProblemProperty(String property, String defaultValue) {
//		if (
//		// cannot use project configuration before location is known to create project
//		property == BasicStaticConfigurationUtils.GRADABLE_PROJECT_LOCATION || // can do == as we are using named
//																				// constants
//				!isUseProjectConfiguration()) {
////			property == BasicStaticConfigurationUtils.USE_PROJECT_CONFIGURATION){ 
//			return defaultValue;
//		}
//
//		return getInheritedStringModuleProblemProperty(
//				BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), module,
//				problem, test, property, defaultValue);
//	}
	/**
	 * Maybe we should also have static configuration
	 * 
	 */
	public static String getBasicInheritedStringModuleProblemProperty(String property, String defaultValue) {
		String retVal = null;
		if (
		// cannot use project configuration before location is known to create project
		property != BasicStaticConfigurationUtils.GRADABLE_PROJECT_LOCATION && // can do == as we are using named
																				// constants
				isUseProjectConfiguration()) {
			retVal =
		 getInheritedStringModuleProblemProperty(
				BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), module,
				problem, test, property, null);
			if (retVal != null) {
				return retVal;
			}
		}
		return getInheritedStringModuleProblemProperty(
				BasicConfigurationManagerSelector.getConfigurationManager().getCourseConfiguration(), module,
				problem, test, property, defaultValue);
	}

	public static PropertiesConfiguration maybeGetLocalCourseConfguration() {
		if (GradingMode.getGraderRun()) {
			return null;
		}
		return BasicConfigurationManagerSelector.getConfigurationManager().getCourseConfiguration();

	}

	public static Boolean getBasicInheritedBooleanModuleProblemProperty(String property, Boolean defaultValue) {
		Boolean retVal = null;
//		if (
//			property == BasicStaticConfigurationUtils.USE_PROJECT_CONFIGURATION || // avoiding recursion
//			!isUseProjectConfiguration() ) {			  
//			return defaultValue;
//		}
//	 return getInheritedBooleanModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), module, problem, test, property, defaultValue);
		if (property != BasicStaticConfigurationUtils.USE_PROJECT_CONFIGURATION && // avoiding recursion
				isUseProjectConfiguration()) {

			retVal = getInheritedBooleanModuleProblemProperty(
					BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(),
					module, problem, test, property, null);

			if (retVal != null) {
				return retVal;
			}
		}
		return getInheritedBooleanModuleProblemProperty(
				BasicConfigurationManagerSelector.getConfigurationManager().getCourseConfiguration(), module, problem,
				test, property, defaultValue);

	}

//	public static Integer getBasicInheritedIntegerModuleProblemProperty(
//			String property, Integer defaultValue) {
//		if (!isUseProjectConfiguration()) {
//			return defaultValue;
//		}
//
//	 return getInheritedIntegerModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), module, problem, test, property, defaultValue);
//	}
	public static Integer getBasicInheritedIntegerModuleProblemProperty(String property, Integer defaultValue) {
		Integer retVal = null;
		if (isUseProjectConfiguration()) {
			retVal = getInheritedIntegerModuleProblemProperty(
					BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(),
					module, problem, test, property, null);
			if (retVal != null) {
				return retVal;
			}
		}
		return getInheritedIntegerModuleProblemProperty(
				BasicConfigurationManagerSelector.getConfigurationManager().getCourseConfiguration(), module, problem,
				test, property, defaultValue);

	}

//	public static List getBasicInheritedListModuleProblemProperty(
//			String property, List defaultValue) {
//		if (!isUseProjectConfiguration() ) { // can do == as we are using named constants
//			return defaultValue;
//		}
//
//	 return getInheritedListModuleProblemProperty(BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), module, problem, test, property, defaultValue);
//	}
	public static List getBasicInheritedListModuleProblemProperty(String property, List defaultValue) {
		List retVal = null;
		if (isUseProjectConfiguration()) { // can do == as we are using named constants
			retVal = getInheritedListModuleProblemProperty(
					BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(),
					module, problem, test, property, null);
			if (retVal != null) {
				return retVal;
			}
		}
		return getInheritedListModuleProblemProperty(
				BasicConfigurationManagerSelector.getConfigurationManager().getCourseConfiguration(), module, problem,
				test, property, defaultValue);
		
	}

	public static String getInheritedStringModuleProblemProperty(PropertiesConfiguration configuration, String module,
			String problem, String aTest, String property, String defaultValue) {
		if (configuration == null) {
//			System.err.println("Null configuration, returning " + defaultValue + " for " + property);
			return defaultValue;
		}
		String retVal = configuration.getString(module + "." + problem + "." + aTest + "." + property, null);
		if (retVal != null) {
			return retVal;
		}
		retVal = configuration.getString(module + "." + problem + "." + property, null);

//		String retVal = configuration.getString(module + "." + problem + "."
//				+ property, null);
		if (retVal != null) {
			return retVal;
		}
		retVal = configuration.getString(module + "." + property, null);
		if (retVal != null) {
			return retVal;
		}
//		if (retVal == null) {
		retVal = configuration.getString(DEFAULT + "." + property, defaultValue);
//		}
		if (retVal == null) {
			retVal = configuration.getString(property, defaultValue);
		}
		return retVal;

	}

	public static List getInheritedListModuleProblemProperty(PropertiesConfiguration configuration, String module,
			String problem, String aTest, String property, List<String> aDefaultValue) {
		if (configuration == null) {
			return aDefaultValue;
		}
		List retVal = configuration.getList(module + "." + problem + "." + aTest + "." + property);

		if (retVal.isEmpty()) {
			retVal = configuration.getList(module + "." + problem + "." + property);
		}

//		List retVal = configuration.getList(module + "." + problem + "."
//				+ property);

		if (retVal.isEmpty()) {
			retVal = configuration.getList(module + "." + property);
		}
		
		if (retVal.isEmpty()) {
			retVal = configuration.getList(DEFAULT + "." + property);
		}
		if (retVal.isEmpty()) {
			retVal = aDefaultValue;
		}

		return retVal;

	}

	public static String getModule() {
		return module;
	}

//	public static void setTest(Class aTest) {
//		test = aTest.getSimpleName();
//	}
	public static void setTest(String aTest) {
		test = aTest;
	}

	public static void setModuleProblemAndSuite(Class aSuiteClass) {
		if (module != DEFAULT_MODULE && problem != DEFAULT_PROBLEM) {
			return; // we have set the module and problem explicitly
		}
		testSuite = aSuiteClass.getSimpleName();
		Package aPackage = aSuiteClass.getPackage();
		String aPackageName = aPackage.getName();
		String[] aPackageComponents = aPackageName.split("\\.");
		if (aPackageComponents.length < 3) {
			System.err.println(aPackageName + " has < 3 components, cannot set module and problem ");
		}
		String aRawModuleName = aPackageComponents[1];
		String aRawProblemName = aPackageComponents[2];
		module = firstCharacterUpperCase(aRawModuleName);
		problem = firstCharacterUpperCase(aRawProblemName);

	}

	public static String firstCharacterUpperCase(String aLowerCaseString) {
		return Character.toUpperCase(aLowerCaseString.charAt(0)) + aLowerCaseString.substring(1);
	}

	public static void setModule(String module) {
		BasicStaticConfigurationUtils.module = module;
	}

	public static String getProblem() {
		return problem;
	}

	public static void setProblem(String problem) {
		BasicStaticConfigurationUtils.problem = problem;
	}

	public static String getTest() {
		return test;
	}

	public static void settest(String newVal) {
		test = newVal;
	}

	public static String getLanguage() {
		if (!isUseProjectConfiguration())
//			return JAVA;
			return BasicLanguageDependencyManager.JAVA_LANGUAGE;

		return getInheritedStringModuleProblemProperty(
				BasicConfigurationManagerSelector.getConfigurationManager().getOrCreateProjectConfiguration(), module,
				problem, test, LANGUAGE, BasicLanguageDependencyManager.JAVA_LANGUAGE);

	}

	public static String toCompoundProperty(String aParent, String aChild) {
		return aParent + "." + aChild;
	}
//	public static String getCheckStyleFile() {
//
//		return getInheritedStringModuleProblemProperty(
//				 CHECK_STYLE_FILE, BasicStaticConfigurationUtils.DEFAULT_CONFIGURATION_FILE);
//
//	}

}
